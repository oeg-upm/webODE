package es.upm.fi.dia.ontology.webode.translat.UML.ode.component.impl;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;

public class ODEConceptImpl extends    ODEComponentHandler
                            implements ODEConcept {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private HashMap conceptClassAttributes   =null;
  private HashMap conceptInstanceAttributes=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void updateParentConcept(HashMap attributes, ODEConceptImpl cloned) {
    Iterator elements=attributes.values().iterator();
    while(elements.hasNext()) {
      ODEAttribute attribute=(ODEAttribute)elements.next();

      attribute.setParentConcept(cloned);
    }
  }

  private void checkAttributeAbsence(ODEAttribute attribute) throws ODEException {
    if(conceptClassAttributes.containsKey(attribute.getName()))
      throw new ODEException("Class Attribute \""+attribute.getName()+"\" already exists",
                             this.getClass());

    if(conceptInstanceAttributes.containsKey(attribute.getName()))
      throw new ODEException("Instance Attribute \""+attribute.getName()+"\" already exists",
                             this.getClass());
  }

  private void checkClassAttributePresence(String classAttributeName) throws ODEException {
    if(!conceptClassAttributes.containsKey(classAttributeName))
      throw new ODEException("Class Attribute \""+classAttributeName+"\" does not exist",
                             this.getClass());
  }

  private void checkInstanceAttributePresence(String instanceAttributeName) throws ODEException {
    if(!conceptInstanceAttributes.containsKey(instanceAttributeName))
      throw new ODEException("Instance Attribute \""+instanceAttributeName+"\" does not exist",
                             this.getClass());
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEConceptImpl(ODEModel model, String conceptName) {
    super(model,conceptName);
    conceptClassAttributes   =new HashMap();
    conceptInstanceAttributes=new HashMap();
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf="ODEConcept {"+getName()+", "+getDescription()+"}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    ODEConceptImpl cloned=(ODEConceptImpl)super.clone();

    cloned.conceptClassAttributes   =(HashMap)conceptClassAttributes.clone();

    updateParentConcept(cloned.conceptClassAttributes,cloned);

    cloned.conceptInstanceAttributes=(HashMap)conceptInstanceAttributes.clone();

    updateParentConcept(cloned.conceptInstanceAttributes,cloned);

    return cloned;
  }

  //-- Interface: ODEComponent -------------------------------------------------

  public void setModel(ODEModel model) {
    super.setModel(model);

    updateParentConcept(conceptClassAttributes,this);
    updateParentConcept(conceptInstanceAttributes,this);
  }

  //-- Interface: ODEConcept ---------------------------------------------------

  public ODEClassAttribute addClassAttribute(ODEClassAttribute classAttribute) throws ODEException {
    checkAttributeAbsence(classAttribute);

    ODEClassAttribute newClassAttribute=(ODEClassAttribute)classAttribute.clone();

    newClassAttribute.setParentConcept(this);

    conceptClassAttributes.put(classAttribute.getName(),newClassAttribute);

    return newClassAttribute;
  }

  public void removeClassAttribute(ODEClassAttribute classAttribute) throws ODEException {
    removeClassAttribute(classAttribute.getName());
  }

  public void removeClassAttribute(String classAttributeName) throws ODEException {
    checkClassAttributePresence(classAttributeName);

    conceptClassAttributes.remove(classAttributeName);
  }

  public ODEClassAttribute[] getClassAttributes() {
    Collection classAttributesCollection=conceptClassAttributes.values();
    ODEClassAttribute[] classAttributes=new ODEClassAttribute[conceptClassAttributes.size()];
    classAttributesCollection.toArray(classAttributes);

    return classAttributes;
  }

  public String[] getClassAttributeNames() {
    Set namesSet=conceptClassAttributes.keySet();
    String[] names=new String[conceptClassAttributes.size()];
    namesSet.toArray(names);

    return names;
  }

  public ODEClassAttribute findClassAttribute(String classAttributeName) {
    return (ODEClassAttribute)conceptClassAttributes.get(classAttributeName);
  }

  public ODEInstanceAttribute addInstanceAttribute(ODEInstanceAttribute instanceAttribute) throws ODEException {
    checkAttributeAbsence(instanceAttribute);

    ODEInstanceAttribute newInstanceAttribute=(ODEInstanceAttribute)instanceAttribute.clone();

    newInstanceAttribute.setParentConcept(this);

    conceptInstanceAttributes.put(instanceAttribute.getName(),newInstanceAttribute);

    return newInstanceAttribute;
  }

  public void removeInstanceAttribute(ODEInstanceAttribute instanceAttribute) throws ODEException {
    removeInstanceAttribute(instanceAttribute.getName());
  }

  public void removeInstanceAttribute(String instanceAttributeName) throws ODEException {
    checkInstanceAttributePresence(instanceAttributeName);

    conceptInstanceAttributes.remove(instanceAttributeName);
  }

  public ODEInstanceAttribute[] getInstanceAttributes() {
    Collection instanceAttributesCollection=conceptInstanceAttributes.values();
    ODEInstanceAttribute[] instanceAttributes=new ODEInstanceAttribute[conceptInstanceAttributes.size()];
    instanceAttributesCollection.toArray(instanceAttributes);

    return instanceAttributes;
  }

  public String[] getInstanceAttributeNames() {
    Set namesSet=conceptInstanceAttributes.keySet();
    String[] names=new String[conceptInstanceAttributes.size()];
    namesSet.toArray(names);

    return names;
  }

  public ODEInstanceAttribute findInstanceAttribute(String instanceAttributeName) {
    return (ODEInstanceAttribute)conceptInstanceAttributes.get(instanceAttributeName);
  }

}