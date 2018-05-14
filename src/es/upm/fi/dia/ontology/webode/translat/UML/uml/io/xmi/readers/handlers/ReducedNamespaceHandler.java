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

public class ReducedNamespaceHandler extends CompositeEventHandler {

  private static String NAMESPACE="UML:Namespace.ownedElement";

  private GeneralizationHandler generalizationHandler=null;

  public ReducedNamespaceHandler() {
    generalizationHandler=new GeneralizationHandler();
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, NamespaceStart event) {
    binder.bind(generalizationHandler);
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, NamespaceEnd event) {
    binder.unbind(this);
  }

  /**
   * @param event
   */
  public void handle(NamespaceStart event) throws EventNotSupported {

  }

  /**
   * @param event
   */
  public void handle(NamespaceEnd event) throws EventNotSupported {

  }

  /**
   * @return String
   */
  public String toString() {
   return "ReducedNamespaceHandler";
  }
}
