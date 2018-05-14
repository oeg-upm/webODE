package es.upm.fi.dia.ontology.webode.translat.UML.uml.component.impl;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;

public class UMLAggregationImpl extends    UMLComponentHandler
                                implements UMLAggregation {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private UMLClass aggregationOriginClass     =null;
  private UMLClass aggregationDestinationClass=null;

  private String aggregationOriginRole              =new String();
  private int    aggregationOriginMinimumCardinality=0;
  private int    aggregationOriginMaximumCardinality=0;

  private String aggregationDestinationRole              =new String();
  private int    aggregationDestinationMinimumCardinality=0;
  private int    aggregationDestinationMaximumCardinality=0;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public UMLAggregationImpl(UMLClass originClass, UMLClass destinationClass) {
    super(originClass.getModel(),"Aggregation");
    aggregationOriginClass=originClass;
    aggregationDestinationClass=destinationClass;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Interface: UMLAssociation -----------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf="UMLAssociation {"+
           getName()+":"+
           aggregationOriginClass.getName()+"-->"+
           aggregationDestinationClass.getName()+"}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    UMLAggregationImpl cloned=(UMLAggregationImpl)super.clone();

    return cloned;
  }

  //-- Interface:  UMLAssociation ----------------------------------------------

  public UMLClass getOriginClass() {
    return aggregationOriginClass;
  }

  public void setOriginClass(UMLClass originClass) {
    /**@todo Check the class belongs to the model */
    aggregationOriginClass=originClass;
  }

  public String getOriginClassRole() {
    return aggregationOriginRole;
  }

  public void setOriginClassRole(String role) {
    /**@todo Check that there is no other association with the same rolename for the origin */
    aggregationOriginRole=role;
  }

  public int getOriginClassMinimumCardinality() {
    return aggregationOriginMinimumCardinality;
  }

  public void setOriginClassMinimumCardinality(int minimumCardinality) {
    aggregationOriginMinimumCardinality=minimumCardinality;
  }

  public int getOriginClassMaximumCardinality() {
    return aggregationOriginMaximumCardinality;
  }
  public void setOriginClassMaximumCardinality(int maximumCardinality) {
    aggregationOriginMaximumCardinality=maximumCardinality;
  }

  public UMLClass getDestinationClass() {
    return aggregationDestinationClass;
  }

  public void setDestinationClass(UMLClass destinationClass) {
    /**@todo Check the class belongs to the model */
    aggregationDestinationClass=destinationClass;
  }

  public String getDestinationClassRole() {
    return aggregationDestinationRole;
  }

  public void setDestinationClassRole(String role) {
    /**@todo Check that there is no other association with the same rolename for the destination */
    aggregationDestinationRole=role;
  }

  public int getDestinationClassMinimumCardinality() {
    return aggregationDestinationMinimumCardinality;
  }

  public void setDestinationClassMinimumCardinality(int minimumCardinality) {
    aggregationDestinationMinimumCardinality=minimumCardinality;
  }

  public int getDestinationClassMaximumCardinality() {
    return aggregationDestinationMaximumCardinality;
  }
  public void setDestinationClassMaximumCardinality(int maximumCardinality) {
    aggregationDestinationMaximumCardinality=maximumCardinality;
  }

}