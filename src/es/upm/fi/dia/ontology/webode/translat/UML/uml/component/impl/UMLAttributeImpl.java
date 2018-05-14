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

public class UMLAttributeImpl extends    UMLComponentHandler
                              implements UMLAttribute {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private UMLClass attributeParentClass =null;
  private int      attributeVisibility  =-1;
  private String   attributeStereotype  =null;
  private String   attributeType        =null;
  private String   attributeInitialValue=null;
  private String   attributeMultiplicity=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public UMLAttributeImpl(UMLClass parentClass, String className) {
    super(parentClass.getModel(),className);
    attributeVisibility=VisibilityTypes.PUBLIC;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf+="UMLAttribute {";
    mySelf+=getName()+", ";
    mySelf+=getDescription()+", ";
    mySelf+=VisibilityTypes.toString(getVisibility())+", ";
    mySelf+=getStereotype()+", ";
    mySelf+=getType()+", ";
    mySelf+=getInitialValue()+", ";
    mySelf+=getMultiplicity();
    mySelf+="}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    UMLAttributeImpl cloned=(UMLAttributeImpl)super.clone();

    return cloned;
  }

  //-- Interface: UMLAttribute--------------------------------------------------

  public UMLClass getParentClass() {
    return attributeParentClass;
  }

  public void setParentClass(UMLClass parentClass) {
    attributeParentClass=parentClass;
    setModel(parentClass.getModel());
  }

  public int getVisibility() {
    return attributeVisibility;
  }

  public void setVisibility(int visibility) {
    if(VisibilityTypes.isValid(visibility))
      attributeVisibility=visibility;
  }

  public String getStereotype() {
    return attributeStereotype;
  }

  public void setStereotype(String stereotype) {
    attributeStereotype=stereotype;
  }

  public String getType() {
    return attributeType;
  }

  public void setType(String type) {
    attributeType=type;
  }

  public String getInitialValue() {
    return attributeInitialValue;
  }

  public void setInitialValue(String initialValue) {
    attributeInitialValue=initialValue;
  }

  public String getMultiplicity() {
    return attributeMultiplicity;
  }

  public void setMultiplicity(String multiplicity) {
    attributeMultiplicity=multiplicity;
  }


}
