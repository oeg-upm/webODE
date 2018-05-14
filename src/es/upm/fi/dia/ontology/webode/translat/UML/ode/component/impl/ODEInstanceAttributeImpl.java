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

public class ODEInstanceAttributeImpl extends    ODEAttributeHandler
                                      implements ODEInstanceAttribute{
  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private int instanceAttributeMinimumValue=0;
  private int instanceAttributeMaximumValue=0;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEInstanceAttributeImpl(String attributeName) {
    this(null,attributeName);
  }

  public ODEInstanceAttributeImpl(ODEConcept odeConcept, String attributeName) {
    super(odeConcept,attributeName);
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf+="ODEInstanceAttribute {";
    mySelf+=getName()+", ";
    mySelf+=getDescription()+", ";
    mySelf+=getParentConcept().getName()+", ";
    mySelf+=getMinimumCardinality()+", ";
    mySelf+=getMaximumCardinality()+", ";
    mySelf+=getMeasurementUnit()+", ";
    mySelf+=getPrecision()+", ";
    mySelf+=AttributeValueTypes.toString(getValueType())+", ";
    mySelf+=getMinimumValue()+", ";
    mySelf+=getMaximumValue();
    mySelf+="}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    ODEInstanceAttributeImpl cloned=(ODEInstanceAttributeImpl)super.clone();

    return cloned;
  }

  //-- Interface: ODEInstanceAttribute -----------------------------------------

  public int getMinimumValue() {
    return instanceAttributeMinimumValue;
  }

  public void setMinimumValue(int minimumValue) {
    instanceAttributeMinimumValue=minimumValue;
  }

  public int  getMaximumValue() {
    return instanceAttributeMaximumValue;
  }

  public void setMaximumValue(int maximumValue) {
    instanceAttributeMaximumValue=maximumValue;
  }

}