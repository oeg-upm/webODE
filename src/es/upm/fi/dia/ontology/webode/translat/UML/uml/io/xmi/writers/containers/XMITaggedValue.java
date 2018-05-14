package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.containers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public class XMITaggedValue {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private String tag         =null;
  private String value       =null;
  private String modelElement=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMITaggedValue(String tag, String value, String modelElement) {
    this.tag=tag;
    this.value=value;
    this.modelElement=modelElement;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag=tag;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value=value;
  }

  public String getModelElement() {
    return modelElement;
  }

  public void setModelElement(String modelElement) {
    this.modelElement=modelElement;
  }
}