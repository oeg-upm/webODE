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

public class UMLAssociationImpl extends    UMLComponentHandler
                                implements UMLAssociation {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private UMLClass associationOriginClass     =null;
  private UMLClass associationDestinationClass=null;

  private String associationOriginRole              =new String();
  private int    associationOriginMinimumCardinality=0;
  private int    associationOriginMaximumCardinality=0;

  private String associationDestinationRole              =new String();
  private int    associationDestinationMinimumCardinality=0;
  private int    associationDestinationMaximumCardinality=0;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public UMLAssociationImpl(String relationName, UMLClass originClass, UMLClass destinationClass) {
    super(originClass.getModel(),relationName);
    associationOriginClass=originClass;
    associationDestinationClass=destinationClass;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Interface: UMLAssociation -----------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf="UMLAssociation {"+
           getName()+":"+
           associationOriginClass.getName()+"-->"+
           associationDestinationClass.getName()+"}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    UMLAssociationImpl cloned=(UMLAssociationImpl)super.clone();

    return cloned;
  }

  //-- Interface:  UMLAssociation ----------------------------------------------

  public UMLClass getOriginClass() {
    return associationOriginClass;
  }

  public void setOriginClass(UMLClass originClass) {
    /**@todo Check the class belongs to the model */
    associationOriginClass=originClass;
  }

  public String getOriginClassRole() {
    return associationOriginRole;
  }

  public void setOriginClassRole(String role) {
    /**@todo Check that there is no other association with the same rolename for the origin */
    associationOriginRole=role;
  }

  public int getOriginClassMinimumCardinality() {
    return associationOriginMinimumCardinality;
  }

  public void setOriginClassMinimumCardinality(int minimumCardinality) {
    associationOriginMinimumCardinality=minimumCardinality;
  }

  public int getOriginClassMaximumCardinality() {
    return associationOriginMaximumCardinality;
  }
  public void setOriginClassMaximumCardinality(int maximumCardinality) {
    associationOriginMaximumCardinality=maximumCardinality;
  }

  public UMLClass getDestinationClass() {
    return associationDestinationClass;
  }

  public void setDestinationClass(UMLClass destinationClass) {
    /**@todo Check the class belongs to the model */
    associationDestinationClass=destinationClass;
  }

  public String getDestinationClassRole() {
    return associationDestinationRole;
  }

  public void setDestinationClassRole(String role) {
    /**@todo Check that there is no other association with the same rolename for the destination */
    associationDestinationRole=role;
  }

  public int getDestinationClassMinimumCardinality() {
    return associationDestinationMinimumCardinality;
  }

  public void setDestinationClassMinimumCardinality(int minimumCardinality) {
    associationDestinationMinimumCardinality=minimumCardinality;
  }

  public int getDestinationClassMaximumCardinality() {
    return associationDestinationMaximumCardinality;
  }
  public void setDestinationClassMaximumCardinality(int maximumCardinality) {
    associationDestinationMaximumCardinality=maximumCardinality;
  }
}