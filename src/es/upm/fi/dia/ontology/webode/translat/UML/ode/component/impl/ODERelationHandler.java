package es.upm.fi.dia.ontology.webode.translat.UML.ode.component.impl;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.ode.ODEModel;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;

public class ODERelationHandler extends    ODEComponentHandler
                                implements ODERelation {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private int relationMaximumCardinality=0;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODERelationHandler(ODEModel model, String relationName) {
    super(model,relationName);
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf="ODERelation {"+getName()+", "+getDescription()+"}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    ODERelationHandler cloned=(ODERelationHandler)super.clone();

    return cloned;
  }

  //-- Interface: ODERelation --------------------------------------------------

  public String getActualName() {
    return getName();
  }

  public void setActualName(String actualName) {
    // Nonsense operation, throw an exception?
  }

  public int getMaximumCardinality() {
    return relationMaximumCardinality;
  }

  public void setMaximumCardinality(int maximumCardinality) {
    relationMaximumCardinality=maximumCardinality;
  }
}