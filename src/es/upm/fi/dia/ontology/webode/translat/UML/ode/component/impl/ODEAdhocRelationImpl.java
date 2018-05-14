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
import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;

public class ODEAdhocRelationImpl extends    ODEConceptToConceptRelationHandler
                                  implements ODEAdhocRelation {
  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private boolean relationAllowsMultipleInstances=false;
  private Vector  relationProperties             =null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void validateProperty(String property) throws ODEException {
    for(int i=0;i<relationProperties.size();i++) {
      if(!BuiltinProperties.areCompatible(property,
                                          (String)relationProperties.get(i)))
        throw new ODEException("Property "+property+" is not compatible with"+
                               " previous properties "+relationProperties,
                               this.getClass());
    }
  }

  private String buildProperties(String[] theProperties) {
    String properties="[";

    for(int i=0;i<theProperties.length;i++) {
      if(i!=0)
        properties+=", ";
      properties+=theProperties[i];
    }

    properties+="]";

    return properties;
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEAdhocRelationImpl(String     relationName,
                              ODEConcept origin,
                              ODEConcept destination) {
    super(relationName,origin,destination);
    relationProperties=new Vector();
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();


    mySelf+="ODEAdhocRelation {";
    mySelf+=getName()+", ";
    mySelf+=getOrigin().getName()+", ";
    mySelf+=getDestination().getName()+", ";
    mySelf+=allowsMultipleInstances()+", ";
    mySelf+=buildProperties(getProperties());
    mySelf+="}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    ODEAdhocRelationImpl cloned=(ODEAdhocRelationImpl)super.clone();

    return cloned;
  }

  //-- Interface: ODERelation --------------------------------------------------

  public String getActualName() {
    return getOrigin().getName()+":"+getName()+":"+getDestination().getName();
  }

  //-- Interface: ODEAdhocRelation ---------------------------------------------

  public boolean allowsMultipleInstances() {
    return relationAllowsMultipleInstances;
  }

  public void setMultipleInstanceAllowance(boolean multipleInstancesAllowed) {
    relationAllowsMultipleInstances=multipleInstancesAllowed;
  }

  public String[] getProperties() {
    String[] properties=new String[relationProperties.size()];
    relationProperties.toArray(properties);

    return properties;
  }

  public void setProperties(String[] properties) throws ODEException {
    relationProperties.clear();
    for(int i=0;i<properties.length;i++)
      addProperty(properties[i]);
  }

  public void addProperty(String property) throws ODEException {
    if(!relationProperties.contains(property)) {
      validateProperty(property);
      relationProperties.add(property);
    }
  }

  public void removeProperty(String property) {
    relationProperties.remove(property);
  }


}