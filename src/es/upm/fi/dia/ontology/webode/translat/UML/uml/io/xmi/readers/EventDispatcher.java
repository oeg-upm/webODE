//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\EventDispatcher.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

//import o2u.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.tools.*;

public class EventDispatcher {

  private EventHandler handlers[];
  private XMIReader    reader;

  private Vector activeHandlers       =null;
  private Vector handlersToBeActivated=null;
  private Stack  previousStatus       =null;

  private Vector rollbackedHandlers   =null;

  private boolean ocurredInhibitedCommit  =false;
  private boolean ocurredInhibitedRollback=false;

  private boolean inhibition_status       =false;

  private Logger logger;

  private String getNames() {
    String resultado=new String();

    for(int i=0;i<activeHandlers.size();i++) {
      EventHandler handler=(EventHandler)activeHandlers.get(i);
      if(i!=0)
        resultado+=", ";
      resultado+=handler.toString();
    }

    return resultado;
  }

  /**
   * @param reader
   */
  public EventDispatcher(XMIReader reader) {
    this.reader=reader;

    activeHandlers         =new Vector();
    handlersToBeActivated  =new Vector();
    previousStatus         =new Stack();

    rollbackedHandlers=new Vector();

    logger=Logger.getLogger("es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.EventDispatcher");
    try {
      logger.setUseParentHandlers(false);
      FileHandler fileHandler=new FileHandler("..\\var\\log\\XMI_event_dispatcher.log");
      fileHandler.setFormatter(new EventFormatter("EVENT DISPATCHER"));
      logger.setLevel(Level.FINE);
      fileHandler.setLevel(Level.FINE);

      logger.addHandler(fileHandler);
/*
      WindowHandler windowHandler=new WindowHandler();
      windowHandler.setFormatter(new EventFormatter("DISPATCHER"));
      logger.addHandler(windowHandler);
*/
    } catch (IOException ex) {
    } catch (SecurityException ex) {
    }
  }

  /**
   * @param handler
   */
  public void bind(SimpleEventHandler handler) {
    EventHandlerBinder newBinder=new EventHandlerBinder(handler,this);
    newBinder.useTranslationTable(reader.getTranslationTable());
    handlersToBeActivated.add(newBinder);

    logger.fine("Binding simple event handler {"+handler+"}");
  }

  /**
   * @param handler
   */
  public void bind(CompositeEventHandler handler) {
    EventHandlerBinder newBinder=new EventHandlerBinder(handler,this);
    newBinder.useTranslationTable(reader.getTranslationTable());
    handlersToBeActivated.add(newBinder);

    logger.fine("Binding composite event handler {"+handler+"}");
  }

  /**
   * @param event
   */
  public void dispatch(Event event) throws CannotDispatchEvent {
    boolean canBeDispatched=false;

    for(int i=0;i<activeHandlers.size()&&!canBeDispatched;i++) {
      EventHandler handler=(EventHandler)activeHandlers.get(i);
      try {
        logger.info("Sending "+event+" to "+handler);
        event.accept(handler);
        canBeDispatched=true;
      } catch (EventNotSupported ex) {
        logger.info("Event discarded by "+handler);
      }
    }

    if(!canBeDispatched&&rollbackedHandlers.size()>0) {
      inhibition_status=true;
      for(int i=0;i<rollbackedHandlers.size()&&!canBeDispatched;i++) {
        EventHandler handler=(EventHandler)rollbackedHandlers.get(i);
        try {
          logger.info("Sending "+event+" to rollbacked handler "+handler);
          event.accept(handler);
          canBeDispatched=true;
        } catch (EventNotSupported ex) {
          logger.info("Event discarded by rollbacked handler "+handler);
        }
      }
      if(canBeDispatched)
        forceCommit();

      inhibition_status=false;
    }

    if(!canBeDispatched&&previousStatus.size()>0) {
      inhibition_status=true;

      Vector handlers=(Vector)previousStatus.peek();
      for(int i=0;i<handlers.size()&&!canBeDispatched;i++) {
        EventHandler handler=(EventHandler)handlers.get(i);
        try {
          logger.info("Sending "+event+" to previously loaded handler "+handler);
          event.accept(handler);
          canBeDispatched=true;
        } catch (EventNotSupported ex) {
          logger.info("Event discarded by previously loaded handler "+handler);
        }
      }
      if(canBeDispatched)
        forceRollback();

      inhibition_status=false;
    }

    if(!canBeDispatched) {
      throw new CannotDispatchEvent(activeHandlers,event);
    }
  }

  public void forceCommit() {
    if(inhibition_status) {
      previousStatus.push(activeHandlers);

      if(ocurredInhibitedCommit) {
        previousStatus.push(rollbackedHandlers);
        activeHandlers=handlersToBeActivated;
        ocurredInhibitedCommit=false;
      } else {
        activeHandlers=rollbackedHandlers;
      }

      handlersToBeActivated=new Vector();
      rollbackedHandlers   =new Vector();

      inhibition_status=false;

      logger.fine("Forced handler activation {"+getNames()+"}\n"+
                  "Previous status    : "+previousStatus+"\n"+
                  "Rollbacked handlers: "+rollbackedHandlers);
    }
  }

  public void commit() {
    if(!inhibition_status) {
      previousStatus.push(activeHandlers);
      activeHandlers=handlersToBeActivated;
      handlersToBeActivated=new Vector();

      logger.fine("New handlers activated {"+getNames()+"}\n"+
                  "Previous status    : "+previousStatus+"\n"+
                  "Rollbacked handlers: "+rollbackedHandlers);
    } else {
      ocurredInhibitedCommit=true;
    }
  }

  public void forceRollback() {
    if(inhibition_status) {
      rollbackedHandlers=activeHandlers;
      activeHandlers=(Vector)previousStatus.pop();
      if(ocurredInhibitedRollback) {
        rollbackedHandlers=activeHandlers;
        activeHandlers=(Vector)previousStatus.pop();
        ocurredInhibitedRollback=false;
      }

      inhibition_status=false;

      logger.fine("Forced status rollback {"+getNames()+"}\n"+
                  "Previous status    : "+previousStatus+"\n"+
                  "Rollbacked handlers: "+rollbackedHandlers);
    }
  }

  public void rollback() {
    if(!inhibition_status) {
      rollbackedHandlers=activeHandlers;
      activeHandlers=(Vector)previousStatus.pop();

      handlersToBeActivated=new Vector();

      logger.fine("Status rollback {"+getNames()+"}\n"+
                  "Previous status    : "+previousStatus+"\n"+
                  "Rollbacked handlers: "+rollbackedHandlers);
    } else {
      ocurredInhibitedRollback=true;
    }
  }
}
