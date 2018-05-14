package es.upm.fi.dia.ontology.webode.translat.UML.uml;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.*;

public class UMLException extends O2UException {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private Class throwerClass=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public UMLException(String description, Class throwerClass) {
    super(description);
    this.throwerClass=throwerClass;
  }

  public UMLException(String description, Class throwerClass, Throwable th) {
    super(description, th);
    this.throwerClass=throwerClass;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Interface: O2UException -------------------------------------------------

  public String exceptionModule() {
    return "UML O2U Core Module";
  }

  public Class throwerClass() {
    return throwerClass;
  }
}