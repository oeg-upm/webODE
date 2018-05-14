package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.*;


import java.io.*;
import java.util.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.tools.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.containers.*;

public class XMIModelWriter extends    XMIWriterHandler
                            implements UMLModelWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private HashMap modelDataTypes      =null;
  private HashMap modelUsedDataTypes  =null;
  private Vector  modelStereotypes    =null;
  private Vector  modelGeneralizations=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void writeModelElements(UMLModel model) {
    String tabulation=tabulator.getTabulator();

    UMLClass[] classes=model.getClasses();
    if(classes.length>0) {
      writer.println(tabulation+"<UML:Namespace.ownedElement>");

      tabulator.increaseTabulation();

      XMIClassWriter classWriter=new XMIClassWriter(this,writer);
      classWriter.setTabulator(tabulator);
      for(int i=0;i<classes.length;i++) {
        classWriter.write(classes[i]);
        writeDataTypes();
        writeStereotypes();
        writeGeneralizations();
      }

      writeAssociations(model);
      writeAggregations(model);

      tabulator.decreaseTabulation();

      writer.println(tabulation+"</UML:Namespace.ownedElement>");
    }
  }

  private void writeDataTypes() {
    if(modelDataTypes.size()>0) {
      XMIDataTypeWriter dataTypeWriter=new XMIDataTypeWriter(writer);
      dataTypeWriter.setTabulator(tabulator);
      Iterator dataTypes=modelDataTypes.values().iterator();
      while(dataTypes.hasNext()) {
        XMIDataType dataType=(XMIDataType)dataTypes.next();

        dataTypeWriter.write(dataType);
      }
      modelDataTypes.clear();
    }
  }

  private void writeStereotypes() {
    if(modelStereotypes.size()>0) {
      XMIStereotypeWriter stereotypeWriter=new XMIStereotypeWriter(writer);
      stereotypeWriter.setTabulator(tabulator);
      for(int i=0;i<modelStereotypes.size();i++) {
        XMIStereotype stereotype=(XMIStereotype)modelStereotypes.get(i);
        stereotypeWriter.write(stereotype);
      }
      modelStereotypes.clear();
    }
  }

  private void writeGeneralizations() {
    if(modelGeneralizations.size()>0) {
      XMIGeneralizationWriter generalizationWriter=new XMIGeneralizationWriter(this,writer);
      generalizationWriter.setTabulator(tabulator);
      for(int i=0;i<modelGeneralizations.size();i++) {
        XMIGeneralization generalization=(XMIGeneralization)modelGeneralizations.get(i);
        generalizationWriter.write(generalization);
      }
      modelGeneralizations.clear();
    }
  }

  private void writeAssociations(UMLModel model) {
    XMIAssociationWriter generalizationWriter=new XMIAssociationWriter(this,writer);
    UMLAssociation[] associations=model.getAssociations();
    generalizationWriter.setTabulator(tabulator);
    for(int i=0;i<associations.length;i++)
      generalizationWriter.write(associations[i]);
  }

  private void writeAggregations(UMLModel model) {
    XMIAggregationWriter generalizationWriter=new XMIAggregationWriter(this,writer);
    UMLAggregation[] associations=model.getAggregations();
    generalizationWriter.setTabulator(tabulator);
    for(int i=0;i<associations.length;i++)
      generalizationWriter.write(associations[i]);
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIModelWriter(XMIWriter parentWriter, PrintWriter writer) {
    super(parentWriter,writer);
    modelDataTypes  =new HashMap();
    modelStereotypes=new Vector();
    modelUsedDataTypes=new HashMap();
    modelGeneralizations=new Vector();
  }

  public XMIModelWriter(XMIWriter parentWriter, PrintStream stream) {
    this(parentWriter,new PrintWriter(stream));
  }

  //----------------------------------------------------------------------------
  //-- Business logic ----------------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Interface: XMIWriter ----------------------------------------------------

  public void write(XMIStereotype stereotype) {
    modelStereotypes.add(stereotype);
  }

  public void write(XMIDataType dataType) {
    if(!modelUsedDataTypes.containsKey(dataType.getName())) {
      modelUsedDataTypes.put(dataType.getName(),dataType);
      modelDataTypes.put(dataType.getName(),dataType);
    }
  }

  public void write(XMIGeneralization generalization) {
    modelGeneralizations.add(generalization);
  }

  //-- Interface: UMLModelWriter -----------------------------------------------

  public void write(UMLModel model) {
    String tabulation=tabulator.getTabulator();

    String id=XMIResourceIdGenerator.generateId(model);

    writer.println(tabulation+"<UML:Model xmi.id='"+id+"'");
    writer.println(tabulation+"           name  ='"+model.getName()+"' visibility='public' isSpecification='false'");
    writer.println(tabulation+"           isRoot='false' isLeaf='false' isAbstract='false' >");

    tabulator.increaseTabulation();

    writeModelElements(model);

    tabulator.decreaseTabulation();

    writer.println(tabulation+"</UML:Model>");
  }
}


