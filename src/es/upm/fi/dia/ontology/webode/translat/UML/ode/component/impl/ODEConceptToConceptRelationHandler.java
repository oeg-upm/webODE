package es.upm.fi.dia.ontology.webode.translat.UML.ode.component.impl;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;

public class ODEConceptToConceptRelationHandler extends    ODERelationHandler
                                                implements ODEConceptToConceptRelation {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEConcept relationOrigin     =null;
  private ODEConcept relationDestination=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEConceptToConceptRelationHandler(String relationName,
                                            ODEConcept origin,
                                            ODEConcept destination) {

    /**@todo Check both concepts belong to the same ontology */
    super(origin.getModel(),relationName);
    relationOrigin=origin;
    relationDestination=destination;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf="ODEConceptToConceptRelation {"+
           getName()+":"+
           relationOrigin.getName()+"-->"+
           relationDestination.getName()+"}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    ODEConceptToConceptRelationHandler cloned=(ODEConceptToConceptRelationHandler)super.clone();

    return cloned;
  }

  //-- Interface: ODEConceptToConceptRelation ----------------------------------

  public ODEConcept getOrigin() {
    return relationOrigin;
  }

  public void setOrigin(ODEConcept origin) {
    /**@todo Check that the origin concept belongs to the model */
    relationOrigin=origin;
  }

  public ODEConcept getDestination() {
    return relationDestination;
  }

  public void setDestination(ODEConcept destination) {
    /**@todo Check that the destination concept belongs to the model */
    relationDestination=destination;
  }
}