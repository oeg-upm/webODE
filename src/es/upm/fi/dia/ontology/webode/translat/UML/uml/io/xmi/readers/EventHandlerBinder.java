//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\EventHandlerBinder.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.data.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.start.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.end.*;

import java.util.*;

public class EventHandlerBinder implements EventHandler {

  private RegistrableEventHandler handler   =null;
  private EventDispatcher         dispatcher=null;

  private Vector shToBind=null;
  private Vector chToBind=null;

  private boolean rollback_needed=false;

  /**
   * @param handler The handler to be controlled
   * @param dispatcher The dispatcher where the handlers must be bound/unbound
   */
  public EventHandlerBinder(RegistrableEventHandler handler,
                            EventDispatcher         dispatcher) {
    this.handler   =handler;
    this.dispatcher=dispatcher;
    shToBind=new Vector();
    chToBind=new Vector();
  }

  /**
   * @param event
   */
  private void postProcess(StartElement event) {
    event.accept(handler,this);

    if(shToBind.size()>0) {
      for(int i=0;i<shToBind.size();i++) {
        SimpleEventHandler sh=(SimpleEventHandler)shToBind.get(i);
        dispatcher.bind(sh);
      }
    }
    if(chToBind.size()>0) {
      for(int i=0;i<chToBind.size();i++) {
        CompositeEventHandler ch=(CompositeEventHandler)chToBind.get(i);
        dispatcher.bind(ch);
      }
    }
    if(shToBind.size()>0||chToBind.size()>0) {
      dispatcher.commit();
      shToBind.removeAllElements();
      chToBind.removeAllElements();
    }
  }

  /**
   * @param event
   */
  private void postProcess(EndElement event) {
    event.accept(handler,this);

    if(rollback_needed) {
      dispatcher.rollback();
      rollback_needed=false;
    }
  }

  /**
   * @param event
   */
  private void postProcess(Data event) {
    // Nothing to be done here
  }

  /**
   * @param handler The simple handler event to be bound to the dispatcher
   */
  public void bind(SimpleEventHandler handler) {
    shToBind.add(handler);
  }

  /**
   * @param handler The composite event handler to be bound to the dispatcher
   */
  public void bind(CompositeEventHandler handler) {
    chToBind.add(handler);
  }

  /**
   * @param handler
   */
  public void unbind(SimpleEventHandler handler) {
    rollback_needed=true;
  }

  /**
   * @param handler
   */
  public void unbind(CompositeEventHandler handler) {
    rollback_needed=true;
  }

  /**
   * @param translationTable
   */
  public void useTranslationTable(TranslationTable translationTable) {
    handler.useTranslationTable(translationTable);
  }

  /**
   * @param event
   */
  public void handle(Event event) throws EventNotSupported {
    handler.handle(event);
  }

  /**
   * @param event
   */
  public void handle(XMLEvent event) throws EventNotSupported {
    handler.handle(event);
  }

  /**
   * @param event
   */
  public void handle(StartElement event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(Data event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(EndElement event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(AssociationEndStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(AssociationEndEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(AssociationStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(AssociationEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(AttributeStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(AttributeEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(ClassStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(ClassEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(DataTypeStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(DataTypeEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(DocumentStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(DocumentData event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(DocumentEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(GeneralizationStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(GeneralizationEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(ModelStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(ModelEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(MultiplicityStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(MultiplicityEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(NamespaceStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(NamespaceEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(StereotypeStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(StereotypeEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(TaggedValueStart event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(TaggedValueData event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @param event
   */
  public void handle(TaggedValueEnd event) throws EventNotSupported {
    handler.handle(event);
    postProcess(event);
  }

  /**
   * @return String
   */
  public String toString() {
   return handler.toString();
  }
}
