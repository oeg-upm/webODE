//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\ModelHandler.java

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

public class ModelHandler extends CompositeEventHandler {

  private static String MODEL="UML:Model";

  private NamespaceHandler namespaceHandler=null;

  public ModelHandler() {
    namespaceHandler=new NamespaceHandler();
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, ModelStart event) {
    binder.bind(namespaceHandler);
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, ModelEnd event) {
    binder.unbind(this);
  }

  /**
   * @param event
   */
  public void handle(ModelStart event) throws EventNotSupported {
    String name=event.atts.getValue(XMIAttributes.NAME);

    t_table.setModel(name);

    logger.info("Created model '"+name+"'");
  }

  /**
   * @param event
   */
  public void handle(ModelEnd event) throws EventNotSupported {

  }

  /**
   * @return String
   */
  public String toString() {
   return "ModelHandler";
  }
}
