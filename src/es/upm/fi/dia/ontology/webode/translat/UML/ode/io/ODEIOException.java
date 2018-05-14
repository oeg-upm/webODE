package es.upm.fi.dia.ontology.webode.translat.UML.ode.io;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;

public class ODEIOException extends ODEException {

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEIOException(String description, Class throwerClass) {
    super(description,throwerClass);
  }

  public ODEIOException(String description, Class throwerClass, Throwable th) {
    super(description, throwerClass, th);
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Interface: O2UException -------------------------------------------------

  public String exceptionModule() {
    return "ODE O2U Input/Output Module";
  }
}