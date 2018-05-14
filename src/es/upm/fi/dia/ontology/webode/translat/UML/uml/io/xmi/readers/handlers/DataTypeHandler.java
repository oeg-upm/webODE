//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\DataTypeHandler.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.handlers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.start.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.end.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;

public class DataTypeHandler extends SimpleEventHandler {

  public DataTypeHandler() {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DataTypeEnd event) {
    binder.unbind(this);
  }

  /**
   * @param event
   */
  public void handle(DataTypeStart event) throws EventNotSupported {
    String id=event.atts.getValue(XMIAttributes.XMI_ID);
    String name=event.atts.getValue(XMIAttributes.NAME);

    logger.info("Found type '"+name+"' with id="+id);

    t_table.addType(name,id);
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @param event
   */
  public void handle(DataTypeEnd event) throws EventNotSupported {
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @return String
   */
  public String toString() {
   return "DataTypeHandler";
  }
}
