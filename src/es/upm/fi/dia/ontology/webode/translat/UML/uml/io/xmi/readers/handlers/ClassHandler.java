//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\ClassHandler.java

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

public class ClassHandler extends CompositeEventHandler {

  private static String CLASS  ="UML:Class";

  private AttributeHandler attributeHandler=null;
  private ReducedNamespaceHandler namespaceHandler=null;

  public ClassHandler() {
    attributeHandler=new AttributeHandler();
    namespaceHandler=new ReducedNamespaceHandler();
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, ClassStart event) {
    binder.bind(attributeHandler);
    binder.bind(namespaceHandler);
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, ClassEnd event) {
    binder.unbind(this);
  }

  /**
   * @param event
   */
  public void handle(ClassStart event) throws EventNotSupported {
    String className=event.atts.getValue(XMIAttributes.NAME);
    String classId  =event.atts.getValue(XMIAttributes.XMI_ID);

    t_table.addClass(className,classId);

    logger.info("Added class '"+className+"' to model '"+t_table.getModel().getName()+"'");
  }

  /**
   * @param event
   */
  public void handle(ClassEnd event) throws EventNotSupported {
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @return String
   */
  public String toString() {
    return "ClassHandler";
  }
}
