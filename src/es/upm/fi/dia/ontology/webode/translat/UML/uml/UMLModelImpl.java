package es.upm.fi.dia.ontology.webode.translat.UML.uml;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;

public class UMLModelImpl implements UMLModel {

  class RelationKey implements Comparable {

    public String keyRelationName    =null;
    public String keyBaseClassName   =null;
    public String keyDerivedClassName=null;

    public RelationKey(String relationName,
                       String baseClassName,
                       String derivedClassName) {
      keyRelationName    =relationName;
      keyBaseClassName   =baseClassName;
      keyDerivedClassName=derivedClassName;
    }

    public int compareTo(Object o) {
      if(!(o instanceof RelationKey))
        return -1;

      RelationKey k=(RelationKey)o;

      int resultado=keyBaseClassName.compareTo(k.keyBaseClassName);

      if(resultado==0) {
        resultado=keyDerivedClassName.compareTo(k.keyDerivedClassName);
        if(resultado==0)
          resultado=keyRelationName.compareTo(k.keyRelationName);
      }

      return resultado;
    }
  }

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private String  modelName            =null;
  private String  modelDescription     =null;

  private Date    modelCreationDate    =null;
  private Date    modelModificationDate=null;

  private TreeMap modelClasses         =null;
  private TreeMap modelGeneralizations =null;
  private TreeMap modelAssociations    =null;
  private TreeMap modelAggregations    =null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void updateModificationDate() {
    modelModificationDate=new Date(System.currentTimeMillis());
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public UMLModelImpl(String modelName) {
    this.modelName       =modelName;
    modelCreationDate    =new Date(System.currentTimeMillis());
    modelModificationDate=(Date)modelCreationDate.clone();
    modelClasses         =new TreeMap();
    modelGeneralizations =new TreeMap();
    modelAssociations    =new TreeMap();
    modelAggregations    =new TreeMap();
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf+="UMLModel {";
    mySelf+=getName()+", ";
    mySelf+=getDescription()+", ";
    mySelf+=getCreationDate()+", ";
    mySelf+=getModificationDate()+"}";

    return mySelf;
  }

  //-- Interface: Model --------------------------------------------------------

  public String getName() {
    return modelName;
  }

  public void setName(String modelName) {
    this.modelName=modelName;
    updateModificationDate();
  }

  public String getDescription() {
    return modelDescription;
  }

  public void setDescription(String modelDescription) {
    this.modelDescription=modelDescription;
    updateModificationDate();
  }

  //-- Interface: UMLModel -----------------------------------------------------

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

  public UMLClass addClass(UMLClass umlClass) throws UMLException {
    UMLClass newClass=null;

    if(modelClasses.containsKey(umlClass.getName()))
      throw new UMLException("Class \""+umlClass.getName()+"\" already exists",
                             this.getClass());

    newClass=(UMLClass)umlClass.clone();
    newClass.setModel(this);

    modelClasses.put(umlClass.getName(),newClass);

    updateModificationDate();

    return newClass;
  }

  public void removeClass(UMLClass umlClass) throws UMLException {
    removeClass(umlClass.getName());
  }

  public void removeClass(String className)  throws UMLException {
    if(!modelClasses.containsKey(className))
      throw new UMLException("Class \""+className+"\" does not exist",
                             this.getClass());
    modelClasses.remove(className);

    updateModificationDate();
  }

  public UMLClass[] getClasses() {
    Collection classesCollection=modelClasses.values();
    UMLClass[] classes=new UMLClass[classesCollection.size()];
    classesCollection.toArray(classes);

    return classes;
  }

  public String[] getClassNames() {
    UMLClass[] classes=getClasses();
    String[] names=new String[classes.length];

    for(int i=0;i<classes.length;i++)
      names[i]=classes[i].getName();

    return names;
  }

  public UMLClass findClass(String className) {
    return (UMLClass)modelClasses.get(className);
  }

  public UMLGeneralization addGeneralization(UMLGeneralization generalization) throws UMLException {
    RelationKey key=new RelationKey(generalization.getName(),
                                    generalization.getBaseClass().getName(),
                                    generalization.getDerivedClass().getName());

    if(modelGeneralizations.containsKey(key))
      throw new UMLException("Generalization \""+generalization+"\" already exists",
                             this.getClass());

    UMLGeneralization newGeneralization=(UMLGeneralization)generalization.clone();

    /**@todo Check the necessity of doing this */
    newGeneralization.setModel(this);

    modelGeneralizations.put(key,newGeneralization);

    updateModificationDate();

    return newGeneralization;
  }

  public void removeGeneralization(UMLGeneralization generalization) throws UMLException {
    RelationKey key=new RelationKey(generalization.getName(),
                                    generalization.getBaseClass().getName(),
                                    generalization.getDerivedClass().getName());
    if(!modelGeneralizations.containsKey(key))
      throw new UMLException("Generalization \""+generalization+"\" does not exist",
                             this.getClass());

    modelGeneralizations.remove(key);

    updateModificationDate();
  }

  public UMLGeneralization[] getGeneralizations() {
    Collection generalizationCollection=modelGeneralizations.values();
    UMLGeneralization[] generalizations=new UMLGeneralization[generalizationCollection.size()];
    generalizationCollection.toArray(generalizations);

    return generalizations;
  }

  public UMLAssociation addAssociation(UMLAssociation association) throws UMLException {
    RelationKey key=new RelationKey(association.getName(),
                                    association.getOriginClass().getName(),
                                    association.getDestinationClass().getName());

    if(modelAssociations.containsKey(key))
      throw new UMLException("Association \""+association+"\" already exists",
                             this.getClass());

    UMLAssociation newAssociation=(UMLAssociation)association.clone();

    /**@todo Check the necessity of doing this */
    newAssociation.setModel(this);

    modelAssociations.put(key,newAssociation);

    updateModificationDate();

    return newAssociation;
  }

  public void removeAssociation(UMLAssociation association) throws UMLException {
    RelationKey key=new RelationKey(association.getName(),
                                    association.getOriginClass().getName(),
                                    association.getDestinationClass().getName());
    if(!modelAssociations.containsKey(key))
      throw new UMLException("Association \""+association+"\" does not exist",
                             this.getClass());

    modelAssociations.remove(key);

    updateModificationDate();
  }

  public UMLAssociation[] getAssociations() {
    Collection associationCollection=modelAssociations.values();
    UMLAssociation[] associations=new UMLAssociation[associationCollection.size()];
    associationCollection.toArray(associations);

    return associations;
  }

  public UMLAggregation addAggregation(UMLAggregation aggregation) throws UMLException {
    RelationKey key=new RelationKey(aggregation.getName(),
                                    aggregation.getOriginClass().getName(),
                                    aggregation.getDestinationClass().getName());

    if(modelAggregations.containsKey(key))
      throw new UMLException("Aggregation \""+aggregation+"\" already exists",
                             this.getClass());

    UMLAggregation newAggregation=(UMLAggregation)aggregation.clone();

    /**@todo Check the necessity of doing this */
    newAggregation.setModel(this);

    modelAggregations.put(key,newAggregation);

    updateModificationDate();

    return newAggregation;
  }

  public void removeAggregation(UMLAggregation aggregation) throws UMLException {
    RelationKey key=new RelationKey(aggregation.getName(),
                                    aggregation.getOriginClass().getName(),
                                    aggregation.getDestinationClass().getName());
    if(!modelAggregations.containsKey(key))
      throw new UMLException("Aggregation \""+aggregation+"\" does not exist",
                             this.getClass());

    modelAggregations.remove(key);

    updateModificationDate();
  }

  public UMLAggregation[] getAggregations() {
    Collection associationCollection=modelAggregations.values();
    UMLAggregation[] associations=new UMLAggregation[associationCollection.size()];
    associationCollection.toArray(associations);

    return associations;
  }
}