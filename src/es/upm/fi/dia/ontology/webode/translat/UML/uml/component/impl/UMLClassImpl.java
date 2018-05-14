package es.upm.fi.dia.ontology.webode.translat.UML.uml.component.impl;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;

public class UMLClassImpl extends    UMLComponentHandler
                          implements UMLClass {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private int     classVisibility=-1;
  private String  classStereotype=null;
  private boolean classIsAbstract=false;

  private HashMap classAttributes=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods -------------------------------------------------------
  //----------------------------------------------------------------------------

  private void updateParentClass(HashMap attributes, UMLClass cloned) {
    Iterator elements=attributes.values().iterator();
    while(elements.hasNext()) {
      UMLAttribute attribute=(UMLAttribute)elements.next();

      attribute.setParentClass(cloned);
    }
  }

  private void checkAttributeAbsence(UMLAttribute attribute) throws UMLException {
    if(classAttributes.containsKey(attribute.getName()))
      throw new UMLException("Attribute \""+attribute.getName()+"\" already exists",
                             this.getClass());
  }

  private void checkAttributePresence(String classAttributeName) throws UMLException {
    if(!classAttributes.containsKey(classAttributeName))
      throw new UMLException("Attribute \""+classAttributeName+"\" does not exist",
                             this.getClass());
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public UMLClassImpl(UMLModel model, String className) {
    super(model,className);
    classVisibility=VisibilityTypes.PUBLIC;
    classAttributes=new HashMap();
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf+="UMLClass {";
    mySelf+=getName()+", ";
    mySelf+=getDescription()+", ";
    mySelf+=VisibilityTypes.toString(getVisibility())+", ";
    mySelf+=getStereotype()+", ";
    mySelf+=isAbstract();
    mySelf+="}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    UMLClassImpl cloned=(UMLClassImpl)super.clone();

    cloned.classAttributes=(HashMap)classAttributes.clone();

    updateParentClass(cloned.classAttributes,cloned);

    return cloned;
  }

  //-- Interface: UMLClass -----------------------------------------------------

  public int getVisibility() {
    return classVisibility;
  }

  public void setVisibility(int visibility) {
    if(VisibilityTypes.isValid(visibility))
      classVisibility=visibility;
  }

  public String getStereotype() {
    return classStereotype;
  }

  public void setStereotype(String stereotype) {
    classStereotype=stereotype;
  }

  public boolean isAbstract() {
    return classIsAbstract;
  }

  public void setAbstract(boolean makeAbstract) {
    classIsAbstract=makeAbstract;
  }

  public UMLAttribute addAttribute(UMLAttribute attribute) throws UMLException {
    checkAttributeAbsence(attribute);

    UMLAttribute newAttribute=(UMLAttribute)attribute.clone();

    newAttribute.setParentClass(this);

    classAttributes.put(attribute.getName(),newAttribute);

    return newAttribute;
  }

  public void removeAttribute(UMLAttribute attribute) throws UMLException {
    removeAttribute(attribute.getName());
  }

  public void removeAttribute(String attributeName) throws UMLException {
    checkAttributePresence(attributeName);

    classAttributes.remove(attributeName);
  }

  public UMLAttribute[] getAttributes() {
    Collection classAttributesCollection=classAttributes.values();
    UMLAttribute[] attributes=new UMLAttribute[classAttributes.size()];
    classAttributesCollection.toArray(attributes);

    return attributes;
  }

  public String[] getAttributeNames() {
    Set namesSet=classAttributes.keySet();
    String[] names=new String[classAttributes.size()];
    namesSet.toArray(names);

    return names;
  }

  public UMLAttribute findAttribute(String attributeName) {
    return (UMLAttribute)classAttributes.get(attributeName);
  }

}