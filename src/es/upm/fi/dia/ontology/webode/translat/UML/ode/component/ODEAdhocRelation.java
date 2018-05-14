package es.upm.fi.dia.ontology.webode.translat.UML.ode.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;

public interface ODEAdhocRelation extends ODEConceptToConceptRelation {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Cardinality management --------------------------------------------------

  public boolean allowsMultipleInstances();
  public void    setMultipleInstanceAllowance(boolean multipleInstancesAllowed);

  //-- Property management -----------------------------------------------------

  public String[] getProperties();
  public void     setProperties(String[] properties) throws ODEException;
  public void     addProperty(String property) throws ODEException;
  public void     removeProperty(String property);

}