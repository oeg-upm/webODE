package es.upm.fi.dia.ontology.webode.translat.UML.ode.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public class ODEAbbreviation {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private String abbreviationName       =null;
  private String abbreviationDescription=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEAbbreviation(String abbreviationName,
                         String abbreviationDescription) {
    this.abbreviationName       =abbreviationName;
    this.abbreviationDescription=abbreviationDescription;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String getName() {
    return abbreviationName;
  }

  public void setName(String abbreviationName) {
    this.abbreviationName=abbreviationName;
  }

  public String getDescription() {
    return abbreviationDescription;
  }

  public void setDescription(String abbreviationDescription) {
    this.abbreviationDescription=abbreviationDescription;
  }

}