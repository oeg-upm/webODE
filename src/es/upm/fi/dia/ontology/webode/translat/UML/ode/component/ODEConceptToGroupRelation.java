package es.upm.fi.dia.ontology.webode.translat.UML.ode.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public interface ODEConceptToGroupRelation extends ODERelation {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Origin concept management -----------------------------------------------

  public ODEConcept getOrigin();
  public void setOrigin(ODEConcept origin);

  //-- Destination group management --------------------------------------------

  /**@todo Add destination group management */
/*
  public ODEGroup getDestination();
  public void setDestination(ODEGroup destination);
*/
}