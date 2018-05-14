package es.upm.fi.dia.ontology.webode.translat.UML.ode;

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

public class ODEModelImpl implements ODEModel {

  class RelationKey implements Comparable {

    public String relationName   =null;
    public String originName     =null;
    public String destinationName=null;

    public RelationKey(String name,
                       String origin,
                       String destination) {
      relationName   =name;
      originName     =origin;
      destinationName=destination;
    }

    public int compareTo(Object o) {
      if(!(o instanceof RelationKey))
        return -1;

      RelationKey k=(RelationKey)o;

      int resultado=originName.compareTo(k.originName);

      if(resultado==0) {
        resultado=destinationName.compareTo(k.destinationName);
        if(resultado==0) {
          resultado=relationName.compareTo(k.relationName);
        }
      }

      return resultado;
    }
  }

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private String  modelName                 =null;
  private String  modelDescription          =null;

  private String  modelOwner                =null;
  private String  modelGroupName            =null;
  private Date    modelCreationDate         =null;
  private Date    modelModificationDate     =null;
  private boolean modelState                =false;

  private HashMap modelConcepts             =null;
  private HashMap modelSubclassOfRelations  =null;
  private HashMap modelAdhocRelations       =null;
  private HashMap modelMereologicalRelations=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void updateModificationDate() {
    modelModificationDate=new Date(System.currentTimeMillis());
  }

  private void updateModelConcepts(HashMap concepts, ODEModel cloned) {
    Iterator elements=concepts.values().iterator();
    while(elements.hasNext()) {
      ODEConcept concept=(ODEConcept)elements.next();

      concept.setModel(cloned);
    }
  }

  private void updateModelSubclassOfRelations(HashMap relations,
                                              ODEModel cloned) {
    Iterator elements=relations.values().iterator();
    while(elements.hasNext()) {
      ODESubclassOfRelation relation=(ODESubclassOfRelation)elements.next();

      relation.setModel(cloned);
      ODEConcept origin=cloned.findConcept(relation.getOrigin().getName());
      ODEConcept destination=cloned.findConcept(relation.getDestination().getName());
    }
  }

  private void updateModelAdhocRelations(HashMap  relations,
                                         ODEModel cloned) {
    Iterator elements=relations.values().iterator();
    while(elements.hasNext()) {
      ODEAdhocRelation relation=(ODEAdhocRelation)elements.next();

      relation.setModel(cloned);
      ODEConcept origin=cloned.findConcept(relation.getOrigin().getName());
      ODEConcept destination=cloned.findConcept(relation.getDestination().getName());
    }
  }

  private void updateModelMereologicalRelations(HashMap  relations,
                                                ODEModel cloned) {
    Iterator elements=relations.values().iterator();
    while(elements.hasNext()) {
      ODEMereologicalRelation relation=(ODEMereologicalRelation)elements.next();

      relation.setModel(cloned);
      ODEConcept origin     =cloned.findConcept(relation.getOrigin().getName());
      ODEConcept destination=cloned.findConcept(relation.getDestination().getName());
    }
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEModelImpl(String modelName) {
    this.modelName            =modelName;
    modelCreationDate         =new Date(System.currentTimeMillis());
    modelModificationDate     =(Date)modelCreationDate.clone();
    modelConcepts             =new HashMap();
    modelSubclassOfRelations  =new HashMap();
    modelAdhocRelations       =new HashMap();
    modelMereologicalRelations=new HashMap();
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf+="ODEModel {";
    mySelf+=getName()+", ";
    mySelf+=getDescription()+", ";
    mySelf+=getOwner()+", ";
    mySelf+=getGroupName()+", ";
    mySelf+=getCreationDate()+", ";
    mySelf+=getModificationDate()+", ";
    mySelf+=new Boolean(getState())+"}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    try {
      ODEModelImpl cloned=(ODEModelImpl)super.clone();

      cloned.modelConcepts=(HashMap)modelConcepts.clone();
      cloned.modelSubclassOfRelations=(HashMap)modelSubclassOfRelations.clone();
      cloned.modelAdhocRelations=(HashMap)modelAdhocRelations.clone();
      cloned.modelMereologicalRelations=(HashMap)modelMereologicalRelations.clone();

      updateModelConcepts(cloned.modelConcepts,cloned);
      updateModelSubclassOfRelations(cloned.modelSubclassOfRelations,cloned);
      updateModelAdhocRelations(cloned.modelAdhocRelations,cloned);

      cloned.modelCreationDate=(Date)modelCreationDate.clone();
      cloned.modelModificationDate=(Date)modelModificationDate.clone();

      return cloned;
    } catch (CloneNotSupportedException ex) {
      return null;
    }
  }

  //-- Interface: Model --------------------------------------------------------

  public String getName() {
    return modelName;
  }

  public void setName(String modelName) {
    this.modelName=modelName;
  }

  public String getDescription() {
    return modelDescription;
  }

  public void setDescription(String modelDescription) {
    this.modelDescription=modelDescription;
    updateModificationDate();
  }

  //-- Interface: ODEModel -----------------------------------------------------

  public String getOwner() {
    return modelOwner;
  }

  public void setOwner(String owner) {
    modelOwner=owner;
    updateModificationDate();
  }

  public String getGroupName() {
    return modelGroupName;
  }

  public void setGroupName(String groupName) {
    modelGroupName=groupName;
    updateModificationDate();
  }

  public Date getCreationDate() {
    return modelCreationDate;
  }

  public void setCreationDate(Date creationDate) {
    modelCreationDate=(Date)creationDate.clone();
    updateModificationDate();
  }

  public Date getModificationDate() {
    return modelModificationDate;
  }

  public void setModificationDate(Date modificationDate) {
    modelModificationDate=(Date)modificationDate.clone();
  }

  public boolean getState() {
    return modelState;
  }

  public void setState(boolean state) {
    modelState=state;
    updateModificationDate();
  }

  public ODEConcept addConcept(ODEConcept concept) throws ODEException {
    ODEConcept newConcept=null;

    if(modelConcepts.containsKey(concept.getName()))
      throw new ODEException("Concept \""+concept.getName()+"\" already exists",
                             this.getClass());

    newConcept=(ODEConcept)concept.clone();

    newConcept.setModel(this);

    modelConcepts.put(newConcept.getName(),newConcept);

    updateModificationDate();

    return newConcept;
  }

  public void removeConcept(ODEConcept concept) throws ODEException {
    removeConcept(concept.getName());
  }

  public void removeConcept(String conceptName) throws ODEException {
    if(!modelConcepts.containsKey(conceptName))
      throw new ODEException("Concept \""+conceptName+"\" does not exist",
                             this.getClass());

    modelConcepts.remove(conceptName);

    updateModificationDate();
  }

  public ODEConcept[] getConcepts() {
    Collection conceptCollection=modelConcepts.values();
    ODEConcept[] concepts=new ODEConcept[conceptCollection.size()];
    conceptCollection.toArray(concepts);

    return concepts;
  }

  public String[] getConceptNames() {
    ODEConcept[] concepts=getConcepts();
    String[] conceptNames=new String[concepts.length];
    for(int i=0;i<concepts.length;i++)
      conceptNames[i]=concepts[i].getName();

    return conceptNames;
  }

  public ODEConcept findConcept(String conceptName) {
    ODEConcept concept=(ODEConcept)modelConcepts.get(conceptName);

    return concept;
  }

  public ODESubclassOfRelation addSubclassOfRelation(ODESubclassOfRelation relation) throws ODEException {
    ODESubclassOfRelation newSubclassOfRelation=null;

    RelationKey key=new RelationKey(relation.getName(),
                                    relation.getOrigin().getName(),
                                    relation.getDestination().getName());

    if(modelSubclassOfRelations.containsKey(key))
      throw new ODEException("Subclass-Of relation \""+relation+"\" already exists",
                             this.getClass());

    newSubclassOfRelation=(ODESubclassOfRelation)relation.clone();

    /**@todo Check necesity of doing this */
    newSubclassOfRelation.setModel(this);

    modelSubclassOfRelations.put(key,newSubclassOfRelation);

    updateModificationDate();

    return newSubclassOfRelation;
  }

  public void removeSubclassOfRelation(ODESubclassOfRelation relation) throws ODEException {
    RelationKey key=new RelationKey(relation.getName(),
                                    relation.getOrigin().getName(),
                                    relation.getDestination().getName());

    if(!modelSubclassOfRelations.containsKey(key))
      throw new ODEException("Subclass-Of relation \""+relation+"\" does not exist",
                             this.getClass());

    modelSubclassOfRelations.remove(key);

    updateModificationDate();
  }

  public ODESubclassOfRelation[] getSubclassOfRelations() {
    Collection relationCollection=modelSubclassOfRelations.values();
    ODESubclassOfRelation[] relations=new ODESubclassOfRelation[relationCollection.size()];
    relationCollection.toArray(relations);

    return relations;
  }

  public ODEAdhocRelation addAdhocRelation(ODEAdhocRelation relation) throws ODEException {
    ODEAdhocRelation newAdhocRelation=null;

    RelationKey key=new RelationKey(relation.getName(),
                                    relation.getOrigin().getName(),
                                    relation.getDestination().getName());

    if(modelAdhocRelations.containsKey(key))
      throw new ODEException("Adhoc relation \""+relation+"\" already exists",
                             this.getClass());

    newAdhocRelation=(ODEAdhocRelation)relation.clone();

    /**@todo Check necesity of doing this */
    newAdhocRelation.setModel(this);

    modelAdhocRelations.put(key,newAdhocRelation);

    updateModificationDate();

    return newAdhocRelation;
  }

  public void removeAdhocRelation(ODEAdhocRelation relation) throws ODEException {
    RelationKey key=new RelationKey(relation.getName(),
                                    relation.getOrigin().getName(),
                                    relation.getDestination().getName());

    if(!modelAdhocRelations.containsKey(key))
      throw new ODEException("Adhoc relation \""+relation+"\" does not exist",
                             this.getClass());

    modelAdhocRelations.remove(key);

    updateModificationDate();
  }

  public ODEAdhocRelation[] getAdhocRelations() {
    Collection relationCollection=modelAdhocRelations.values();
    ODEAdhocRelation[] relations=new ODEAdhocRelation[relationCollection.size()];
    relationCollection.toArray(relations);

    return relations;
  }

  public ODEMereologicalRelation addMereologicalRelation(ODEMereologicalRelation relation) throws ODEException {
    ODEMereologicalRelation newMereologicalRelation=null;

    RelationKey key=new RelationKey(relation.getName(),
                                    relation.getOrigin().getName(),
                                    relation.getDestination().getName());

    if(modelMereologicalRelations.containsKey(key))
      throw new ODEException("Mereological relation \""+relation+"\" already exists",
                             this.getClass());

    newMereologicalRelation=(ODEMereologicalRelation)relation.clone();

    /**@todo Check necesity of doing this */
    newMereologicalRelation.setModel(this);

    modelMereologicalRelations.put(key,newMereologicalRelation);

    updateModificationDate();

    return newMereologicalRelation;
  }

  public void removeMereologicalRelation(ODEMereologicalRelation relation) throws ODEException {
    RelationKey key=new RelationKey(relation.getName(),
                                    relation.getOrigin().getName(),
                                    relation.getDestination().getName());

    if(!modelMereologicalRelations.containsKey(key))
      throw new ODEException("Mereological relation \""+relation+"\" does not exist",
                             this.getClass());

    modelMereologicalRelations.remove(key);

    updateModificationDate();
  }

  public ODEMereologicalRelation[] getMereologicalRelations() {
    Collection relationCollection=modelMereologicalRelations.values();
    ODEMereologicalRelation[] relations=new ODEMereologicalRelation[relationCollection.size()];
    relationCollection.toArray(relations);

    return relations;
  }
}