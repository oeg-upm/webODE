package es.upm.fi.dia.ontology.webode.translat.UML.uml.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.*;

public interface UMLClass extends UMLComponent {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Visibility management ---------------------------------------------------

  public int  getVisibility();
  public void setVisibility(int visibility);

  //-- Stereotype management ---------------------------------------------------

  public String getStereotype();
  public void   setStereotype(String stereotype);

  //-- Abstraction management --------------------------------------------------

  public boolean isAbstract();
  public void    setAbstract(boolean makeAbstract);

  //-- Attribute management ----------------------------------------------------

  public UMLAttribute addAttribute(UMLAttribute attribute) throws UMLException;
  public void         removeAttribute(UMLAttribute attribute) throws UMLException;
  public void         removeAttribute(String attributeName) throws UMLException;

  public UMLAttribute[] getAttributes();
  public String[]       getAttributeNames();
  public UMLAttribute   findAttribute(String attributeName);

}