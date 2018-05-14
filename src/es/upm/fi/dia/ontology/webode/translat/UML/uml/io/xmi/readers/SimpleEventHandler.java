//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\SimpleEventHandler.java

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

public class SimpleEventHandler extends LoggableHandler {

  protected TranslationTable t_table=null;

  public SimpleEventHandler() {
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, Event event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, XMLEvent event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, StartElement event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, Data event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, EndElement event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, AssociationEndStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AssociationEndEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, AssociationStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AssociationEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, AttributeStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AttributeEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, ClassStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, ClassEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, DataTypeStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DataTypeEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, DocumentStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, DocumentData event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DocumentEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, GeneralizationStart event) {
    accept(binder,(StartElement)event);
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, GeneralizationEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, ModelStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, ModelEnd event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, MultiplicityStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, MultiplicityEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, NamespaceStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, NamespaceEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, StereotypeStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, StereotypeEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public final void accept(EventHandlerBinder binder, TaggedValueStart event) {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, TaggedValueData event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, TaggedValueEnd event) {
    // Nothing to be done here, should be overriden in the specializations
  }

  /**
   * @param translationTable
   */
  public final void useTranslationTable(TranslationTable translationTable) {
    t_table=translationTable;
  }

  /**
   * @param event
   */
  public void handle(Event event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(XMLEvent event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(StartElement event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(Data event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(EndElement event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(AssociationEndStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(AssociationEndEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(AssociationStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(AssociationEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(AttributeStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(AttributeEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(ClassStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(ClassEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(DataTypeStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(DataTypeEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(DocumentStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(DocumentData event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(DocumentEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(GeneralizationStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(GeneralizationEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(ModelStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(ModelEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(MultiplicityStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(MultiplicityEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(NamespaceStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(NamespaceEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(StereotypeStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(StereotypeEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(TaggedValueStart event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(TaggedValueData event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @param event
   */
  public void handle(TaggedValueEnd event) throws EventNotSupported {
    throw new EventNotSupported(this,event);
  }

  /**
   * @return String
   */
  public String toString() {
    return "SimpleEventHandler {}";
  }
}
