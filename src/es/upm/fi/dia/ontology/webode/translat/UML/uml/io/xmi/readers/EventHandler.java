//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\EventHandler.java

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

public interface EventHandler {

  /**
   * @param translationTable
   */
  public void useTranslationTable(TranslationTable translationTable);

  /**
   * @param event
   */
  public void handle(Event event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(XMLEvent event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(StartElement event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(Data event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(EndElement event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(AssociationEndStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(AssociationEndEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(AssociationStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(AssociationEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(AttributeStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(AttributeEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(ClassStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(ClassEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(DataTypeStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(DataTypeEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(DocumentStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(DocumentData event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(DocumentEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(GeneralizationStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(GeneralizationEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(ModelStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(ModelEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(MultiplicityStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(MultiplicityEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(NamespaceStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(NamespaceEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(StereotypeStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(StereotypeEnd event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(TaggedValueStart event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(TaggedValueData event) throws EventNotSupported;

  /**
   * @param event
   */
  public void handle(TaggedValueEnd event) throws EventNotSupported;

  /**
   * @return String
   */
  public String toString();
}
