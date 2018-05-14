//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\RegistrableEventHandler.java

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

public interface RegistrableEventHandler extends EventHandler {

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, Event event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, XMLEvent event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, StartElement event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, Data event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, EndElement event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AssociationEndStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AssociationEndEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AssociationStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AssociationEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AttributeStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AttributeEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, ClassStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, ClassEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DataTypeStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DataTypeEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DocumentStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DocumentData event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DocumentEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, GeneralizationStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, GeneralizationEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, ModelStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, ModelEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, MultiplicityStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, MultiplicityEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, NamespaceStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, NamespaceEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, StereotypeStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, StereotypeEnd event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, TaggedValueStart event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, TaggedValueData event);

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, TaggedValueEnd event);
}
