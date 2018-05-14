package es.upm.fi.dia.ontology.webode.translat.UML.uml.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public interface UMLGeneralization extends UMLComponent {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Base class management ---------------------------------------------------

  public UMLClass getBaseClass();
  public void     setBaseClass(UMLClass baseClass);

 //-- Derived class management -------------------------------------------------

  public UMLClass getDerivedClass();
  public void     setDerivedClass(UMLClass derivedClass);

}