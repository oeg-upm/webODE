//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\TranslationTable.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.io.*;
import java.util.logging.*;

//import o2u.*;

import java.util.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.impl.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.tools.*;

public class TranslationTable {

  class ClassRecord {

    public String  classId       =null;
    public boolean navegability  =false;
    public int     maxCardinality=0;
    public int     minCardinality=0;

    public boolean aggregation   =false;

    private boolean exists        =false;

    public boolean exists() {
      return exists;
    }

    public void exists(boolean exists) {
      this.exists=exists;
    }
  }

  class AssociationRecord {

    private TranslationTable t_table=null;

    public  String name=null;
    public  String id  =null;
    private Vector ends=null;

    public AssociationRecord(TranslationTable t_table,
                             String name, String id) {
      this.t_table=t_table;
      this.name   =name;
      this.id     =id;
      ends        =new Vector();
    }

    public ClassRecord get(int i) {
      return (ClassRecord)ends.get(i);
    }

    public void add(ClassRecord record) {
      ends.add(record);
    }

    public void notify(String classId) {
      for(int i=0;i<ends.size();i++) {
        ClassRecord end=get(i);
        if(!end.exists())
          end.exists(end.classId.equals(classId));
      }
    }

    public boolean canBeBuilt() {
      boolean resultado=false;

      if(ends.size()==2) {
        ClassRecord end1=get(0);
        ClassRecord end2=get(1);
        resultado=end1.exists()&&end2.exists();
      }

      return resultado;
    }

    public void build() {
      ClassRecord end1=get(0);
      ClassRecord end2=get(1);

      ClassRecord endOrigin     =end1;
      ClassRecord endDestination=end2;

      UMLClass origin     =null;
      UMLClass destination=null;

      if(end1.aggregation||end2.aggregation) {

        if(end2.aggregation) {
          endOrigin=end2;
          endDestination=end1;
        }

        origin     =t_table.getClass(endOrigin.classId);
        destination=t_table.getClass(endDestination.classId);

        UMLAggregation aggregation=new UMLAggregationImpl(origin,destination);

        aggregation.setOriginClassMinimumCardinality(endOrigin.minCardinality);
        aggregation.setOriginClassMaximumCardinality(endOrigin.maxCardinality);
        aggregation.setDestinationClassMinimumCardinality(endDestination.minCardinality);
        aggregation.setDestinationClassMaximumCardinality(endDestination.maxCardinality);

        logger.info("Created aggregation with origin '"+
                    aggregation.getOriginClass().getName()+
                    "' and destination '"+
                    aggregation.getDestinationClass().getName()+"'");

        try {
          aggregation=t_table.getModel().addAggregation(aggregation);
          logger.info("Added aggregation '"+aggregation.getName()+
                      "' to model '"+t_table.getModel().getName()+"'");

          t_table.updateDocumentation(aggregation,id);
        } catch (UMLException ex) {
          logger.info(ex.getMessage());
          ex.printStackTrace();
          System.exit(0);
        }

      } else {

        if(end1.navegability==false&&end2.navegability==true) {
          endOrigin=end2;
          endDestination=end1;
        }

        origin     =t_table.getClass(endOrigin.classId);
        destination=t_table.getClass(endDestination.classId);

        UMLAssociation association=new UMLAssociationImpl(name,origin,destination);

        association.setOriginClassMinimumCardinality(endOrigin.minCardinality);
        association.setOriginClassMaximumCardinality(endOrigin.maxCardinality);
        association.setDestinationClassMinimumCardinality(endDestination.minCardinality);
        association.setDestinationClassMaximumCardinality(endDestination.maxCardinality);

        logger.info("Created association '"+
                    association.getName()+
                    "' with origin '"+
                    association.getOriginClass().getName()+
                    "' and destination '"+
                    association.getDestinationClass().getName()+"'");

        try {
          association=t_table.getModel().addAssociation(association);
          logger.info("Added association '"+association.getName()+
                      "' to model '"+t_table.getModel().getName()+"'");

          t_table.updateDocumentation(association,id);
        } catch (UMLException ex) {
          logger.info(ex.getMessage());
          ex.printStackTrace();
          System.exit(0);
        }
      }
    }
  }

  class GeneralizationRecord {
    private TranslationTable t_table=null;

    public  String id  =null;
    private Vector ends=null;

    public GeneralizationRecord(TranslationTable t_table, String id) {
      this.t_table=t_table;
      this.id     =id;
      ends        =new Vector();
    }

    public ClassRecord get(int i) {
      return (ClassRecord)ends.get(i);
    }

    public void add(ClassRecord record) {
      ends.add(record);
    }

    public void notify(String classId) {
      for(int i=0;i<ends.size();i++) {
        ClassRecord end=get(i);
        if(!end.exists())
          end.exists(end.classId.equals(classId));
      }
    }

    public boolean canBeBuilt() {
      boolean resultado=false;

      if(ends.size()==2) {
        ClassRecord end1=get(0);
        ClassRecord end2=get(1);
        resultado=end1.exists()&&end2.exists();
      }

      return resultado;
    }

    public void build() {
      ClassRecord end1=get(0);
      ClassRecord end2=get(1);

      UMLClass          parent=t_table.getClass(end1.classId);
      UMLClass          child =t_table.getClass(end2.classId);
      UMLGeneralization generalization=new UMLGeneralizationImpl(parent,child);

      logger.info("Created generalization '"+
                  generalization.getName()+
                  "' with base class '"+
                  generalization.getBaseClass().getName()+
                  "' and derived class '"+
                  generalization.getDerivedClass().getName()+"'");

      try {
        generalization=t_table.getModel().addGeneralization(generalization);
        logger.info("Added generalization '"+generalization.getName()+
                    "' to model '"+t_table.getModel().getName()+"'");

        t_table.updateDocumentation(generalization,id);
      } catch (UMLException ex) {
        logger.info(ex.getMessage());
        ex.printStackTrace();
        System.exit(0);
      }
    }
  }

  public static Logger         logger       =null;
  public static WindowHandler  windowHandler=null;
  public static EventFormatter formatter    =null;

  static {
    try {
      logger=Logger.getLogger("es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.TranslationTable");
      logger.setUseParentHandlers(false);

      formatter=new EventFormatter("TRANSLATION TABLE");

      FileHandler fileHandler=new FileHandler("..\\var\\log\\XMI_translation_table.log");
      fileHandler.setFormatter(formatter);
      logger.setLevel(Level.FINE);
      fileHandler.setLevel(Level.FINE);
      logger.addHandler(fileHandler);
/*
      windowHandler=new WindowHandler();
      windowHandler.setFormatter(formatter);
      windowHandler.setLevel(Level.FINE);
      logger.setLevel(Level.FINE);
      logger.addHandler(windowHandler);
*/
    } catch (IOException ex) {
    } catch (SecurityException ex) {
    }
  }

  private UMLModel model         =null;

  private UMLClass lastClassAdded=null;

  private TreeMap  classes                 =null;

  private TreeMap  types                   =null;
  private TreeMap  pendingTypeUpdate       =null;

  private TreeMap  stereotypes             =null;
  private TreeMap  pendingStereotypeUpdate =null;

  private TreeMap  pendingDocumentationUpdate=null;

  private Vector            pendingAssociationUpdate=null;
  private AssociationRecord lastAssociationAdded    =null;

  private Vector            pendingGeneralizationUpdate=null;

  private void updateType(UMLAttribute attribute, String typeId) {
    if(types.containsKey(typeId)) {
      String type=(String)types.get(typeId);
      attribute.setType(type);

      logger.info("Updated attribute '"+attribute.getName()+"' type: "+type);
    } else {
      Vector v=null;

      if(pendingTypeUpdate.containsKey(typeId)) {
        v=(Vector)pendingTypeUpdate.get(typeId);
        pendingTypeUpdate.remove(typeId);
      } else {
        v=new Vector();
      }

      v.add(attribute);
      pendingTypeUpdate.put(typeId,v);
    }
  }

  private void updateStereotype(UMLAttribute attribute, String attributeId) {
    if(stereotypes.containsKey(attributeId)) {
      String stereotype=(String)stereotypes.get(attributeId);
      attribute.setStereotype(stereotype);

      logger.info("Updated attribute '"+attribute.getName()+
                  "' stereotype: "+stereotype);
    } else {
      pendingStereotypeUpdate.put(attributeId,attribute);
    }
  }

  private void updateDocumentation(UMLComponent component, String id) {
    pendingDocumentationUpdate.put(id,component);
  }

  private void notifyClass(String classId) {
    Vector            left       =new Vector();
    AssociationRecord association=null;

    for(int i=0;i<pendingAssociationUpdate.size();i++) {
      association=(AssociationRecord)pendingAssociationUpdate.get(i);

      association.notify(classId);

      if(association.canBeBuilt())
        association.build();
      else
        left.add(association);
    }

    pendingAssociationUpdate.removeAllElements();
    pendingAssociationUpdate.addAll(left);
    left.removeAllElements();

    GeneralizationRecord generalization=null;

    for(int i=0;i<pendingGeneralizationUpdate.size();i++) {
      generalization=(GeneralizationRecord)pendingGeneralizationUpdate.get(i);

      generalization.notify(classId);

      if(generalization.canBeBuilt())
        generalization.build();
      else
        left.add(generalization);
    }

    pendingGeneralizationUpdate.removeAllElements();
    pendingGeneralizationUpdate.addAll(left);
    left.removeAllElements();
  }

  public TranslationTable() {
    classes          =new TreeMap();

    types            =new TreeMap();
    pendingTypeUpdate=new TreeMap();

    stereotypes            =new TreeMap();
    pendingStereotypeUpdate=new TreeMap();

    pendingDocumentationUpdate=new TreeMap();

    pendingAssociationUpdate=new Vector();

    pendingGeneralizationUpdate=new Vector();
  }

  public void setModel(String name) {
    model=new UMLModelImpl(name);

    logger.info("Created model '"+name+"'");
  }

  public UMLClass addClass(String className,
                           String classId) {

    UMLClass newClass=new UMLClassImpl(model,className);

    logger.info("Created class '"+className+"' with id="+classId);

    try {
      newClass=model.addClass(newClass);
      logger.info("Added class '"+className+"' to model '"+model.getName()+"'");

      classes.put(classId,newClass);
      lastClassAdded=newClass;

      updateDocumentation(newClass,classId);
      addType(className,classId);
      notifyClass(classId);
    } catch (UMLException ex) {
      logger.info(ex.getMessage());
      ex.printStackTrace();
      System.exit(0);
    }

    return newClass;
  }

  public UMLAttribute addAttribute(String name, String type, String id) {
    UMLAttribute attribute=new UMLAttributeImpl(lastClassAdded,name);

    logger.info("Created attribute '"+name+"' with id="+id);

    try {
      attribute=lastClassAdded.addAttribute(attribute);
      logger.info("Added attribute '"+name+
                  "' to class '"+lastClassAdded.getName()+"'");

      updateType(attribute,type);
      updateStereotype(attribute,id);
      updateDocumentation(attribute,id);
    } catch (UMLException ex) {
      logger.info(ex.getMessage());
      ex.printStackTrace();
      System.exit(0);
    }

    return attribute;
  }

  public void addGeneralization(String parentId, String childId, String id) {
    GeneralizationRecord generalization=new GeneralizationRecord(this,id);

    ClassRecord parent=new ClassRecord();
    parent.classId=parentId;
    parent.exists(getClass(parentId)!=null);

    ClassRecord child=new ClassRecord();
    child.classId=childId;
    child.exists(getClass(childId)!=null);

    generalization.add(parent);
    generalization.add(child);

    if(generalization.canBeBuilt()) {
      generalization.build();
    } else {
      pendingGeneralizationUpdate.add(generalization);
    }
  }

  public void addAssociation(String associationName, String associationId) {
    lastAssociationAdded=new AssociationRecord(this,associationName,associationId);

    pendingAssociationUpdate.add(lastAssociationAdded);
  }

  public void addType(String typeName, String typeId) {
    types.put(typeId,typeName);

    if(pendingTypeUpdate.containsKey(typeId)) {
      String type=(String)types.get(typeId);
      Vector v   =(Vector)pendingTypeUpdate.get(typeId);

      for(int i=0;i<v.size();i++) {
        UMLAttribute attribute=(UMLAttribute)v.get(i);
        attribute.setType(type);

        logger.info("Updated attribute '"+attribute.getName()+"' type: "+type);
      }

      pendingTypeUpdate.remove(typeId);
    }
  }

  public void addStereotype(String stereotypeName, String attributeId) {
    stereotypes.put(attributeId,stereotypeName);

    if(pendingStereotypeUpdate.containsKey(attributeId)) {
      String       stereotype=(String)stereotypes.get(attributeId);
      UMLAttribute attribute =(UMLAttribute)pendingStereotypeUpdate.get(attributeId);

      attribute.setStereotype(stereotype);

      pendingStereotypeUpdate.remove(attributeId);

      logger.info("Updated attribute '"+attribute.getName()+
                  "' stereotype: "+stereotype);
    }
  }

  public void addDocumentation(String description, String id) {
    if(pendingDocumentationUpdate.containsKey(id)) {
      UMLComponent component=(UMLComponent)pendingDocumentationUpdate.get(id);

      component.setDescription(description);

      pendingDocumentationUpdate.remove(id);

      logger.info("Updated component '"+id+"' description: "+description);
    }
  }

  public void addAssociationEnd(String  classId,
                                boolean isNavigable,
                                int     minCardinality,
                                int     maxCardinality,
                                boolean isAggregation) {
    ClassRecord record=new ClassRecord();

    record.classId       =classId;
    record.navegability  =isNavigable;
    record.minCardinality=minCardinality;
    record.maxCardinality=maxCardinality;

    record.aggregation   =isAggregation;

    record.exists(getClass(classId)!=null);

    lastAssociationAdded.add(record);

    if(lastAssociationAdded.canBeBuilt()) {
      lastAssociationAdded.build();
      lastAssociationAdded=null;

      pendingAssociationUpdate.remove(lastAssociationAdded);
    }
  }

  public UMLModel getModel() {
    return model;
  }

  public UMLClass getClass(String classId) {
    return (UMLClass)classes.get(classId);
  }

  public UMLClass getLastClassAdded() {
    return lastClassAdded;
  }
}

