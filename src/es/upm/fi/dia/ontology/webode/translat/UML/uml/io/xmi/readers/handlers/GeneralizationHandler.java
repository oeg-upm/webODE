//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\GeneralizationHandler.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.handlers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.end.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.start.*;

public class GeneralizationHandler extends SimpleEventHandler {

  public GeneralizationHandler() {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, GeneralizationEnd event) {
    binder.unbind(this);
  }

  /**
   * @param event
   */
  public void handle(GeneralizationStart event) throws EventNotSupported {
    String id      =event.atts.getValue(XMIAttributes.XMI_ID);
    String parentId=event.atts.getValue(XMIAttributes.PARENT);
    String childId =event.atts.getValue(XMIAttributes.CHILD);

    t_table.addGeneralization(parentId,childId,id);
  }

  /**
   * @param event
   */
  public void handle(GeneralizationEnd event) throws EventNotSupported {
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @return String
   */
  public String toString() {
   return "GeneralizationHandler";
  }
}
