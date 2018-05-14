//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\StereotypeHandler.java

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

public class StereotypeHandler extends SimpleEventHandler {

  public StereotypeHandler() {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, StereotypeEnd event) {
    binder.unbind(this);
  }

  /**
   * @param event
   */
  public void handle(StereotypeStart event) throws EventNotSupported {
    String extend_element=event.atts.getValue(XMIAttributes.EXTENDED_ELEMENT);
    String name=event.atts.getValue(XMIAttributes.NAME);
    String baseClass=event.atts.getValue(XMIAttributes.BASE_CLASS);

    if(baseClass.equals("Attribute")) {
      logger.info("Found stereotype '"+name+"' for id="+extend_element);
      t_table.addStereotype(name,extend_element);
    }
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @param event
   */
  public void handle(StereotypeEnd event) throws EventNotSupported {
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @return String
   */
  public String toString() {
   return "StereotypeHandler";
  }
}
