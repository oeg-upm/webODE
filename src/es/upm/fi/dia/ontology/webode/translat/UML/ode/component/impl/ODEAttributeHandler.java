package es.upm.fi.dia.ontology.webode.translat.UML.ode.component.impl;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;

public class ODEAttributeHandler extends    ODEComponentHandler
                                 implements ODEAttribute {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEConcept attributeParentConcept     =null;
  private int        attributeMinimumCardinality=0;
  private int        attributeMaximumCardinality=0;
  private String     attributeMeasurementUnit   =null;
  private String     attributePrecision         =null;
  private int        attributeValueType         =0;
  private Vector     attributeValues            =null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEAttributeHandler(String attributeName) {
    this(null,attributeName);
  }

  public ODEAttributeHandler(ODEConcept odeConcept, String attributeName) {
    super(odeConcept.getModel(),attributeName);
    attributeParentConcept=odeConcept;
    attributeValueType=AttributeValueTypes.INTEGER;
    attributeMinimumCardinality=1;
    attributeMaximumCardinality=1;
    attributeValues=new Vector();
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf="ODEAttribute {"+getName()+", "+getDescription()+"}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    ODEAttributeHandler cloned=(ODEAttributeHandler)super.clone();

    cloned.attributeValues=(Vector)attributeValues.clone();

    return cloned;
  }

  //-- Interface: ODEAttribute -------------------------------------------------

  public ODEConcept getParentConcept() {
    return attributeParentConcept;
  }

  public void setParentConcept(ODEConcept parentConcept) {
    attributeParentConcept=parentConcept;
    setModel(attributeParentConcept.getModel());
  }

  public int getMinimumCardinality() {
    return attributeMinimumCardinality;
  }

  public void setMinimumCardinality(int minimumCardinality) {
    attributeMinimumCardinality=minimumCardinality;
  }

  public int getMaximumCardinality() {
    return attributeMaximumCardinality;
  }

  public void setMaximumCardinality(int maximumCardinality) {
    attributeMaximumCardinality=maximumCardinality;
  }

  public String getMeasurementUnit() {
    return attributeMeasurementUnit;
  }

  public void setMeasurementUnit(String measurementUnit) {
    attributeMeasurementUnit=measurementUnit;
  }

  public String getPrecision() {
    return attributePrecision;
  }

  public void setPrecision(String precision) {
    attributePrecision=precision;
  }

  public int getValueType() {
    return attributeValueType;
  }

  public void setValueType(int valueType) {
    if(AttributeValueTypes.isValid(valueType))
      attributeValueType=valueType;
  }

  public String[] getValues() {
    String[] values=new String[attributeValues.size()];
    attributeValues.toArray(values);

    return values;
  }

  public void setValues(String[] values) {
    attributeValues.clear();
    if(values!=null) {
      for(int i=0;i<values.length;i++)
        attributeValues.add(values[i]);
    }
  }

  public void addValue(String value) {
    if(!attributeValues.contains(value))
      attributeValues.add(value);
  }

  public void removeValue(String value) {
    attributeValues.remove(value);
  }

}