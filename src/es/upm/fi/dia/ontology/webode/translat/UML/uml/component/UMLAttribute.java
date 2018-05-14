package es.upm.fi.dia.ontology.webode.translat.UML.uml.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public interface UMLAttribute extends UMLComponent {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Parent class management -------------------------------------------------

  public UMLClass getParentClass();
  public void     setParentClass(UMLClass parentClass);

  //-- Visibility management ---------------------------------------------------

  public int  getVisibility();
  public void setVisibility(int visibility);

  //-- Stereotype management ---------------------------------------------------

  public String getStereotype();
  public void   setStereotype(String stereotype);

  //-- Type management ---------------------------------------------------------

  public String getType();
  public void   setType(String type);

  //-- Initial value management ------------------------------------------------

  public String getInitialValue();
  public void   setInitialValue(String initialValue);

  //-- Multiplicity management -------------------------------------------------

  /**@todo Check usefulness */
  public String getMultiplicity();
  public void   setMultiplicity(String multiplicity);

}