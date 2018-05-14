package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.io.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.tools.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.tools.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.containers.*;

public class XMIClassWriter extends    XMIWriterHandler
                            implements GeneralizationConsumer {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private TreeSet parentClasses=null;
  private TreeSet childClasses =null;

  private String inProccessClassName=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void writeAttributes(UMLClass umlClass) {
    String tabulation=tabulator.getTabulator();

    UMLAttribute[] attributes=umlClass.getAttributes();

    if(attributes.length>0) {
      writer.println(tabulation+"<UML:Classifier.feature>");
      tabulator.increaseTabulation();

      XMIAttributeWriter attributeWriter=new XMIAttributeWriter(this,writer);
      attributeWriter.setTabulator(tabulator);
      for(int i=0;i<attributes.length;i++)
        attributeWriter.write(attributes[i]);

      tabulator.decreaseTabulation();
      writer.println(tabulation+"</UML:Classifier.feature>");
    }
  }

  private void writeDescription(UMLClass umlClass, String id) {
    String description=umlClass.getDescription();

    if(description!=null) {
      XMITaggedValue taggedValue=new XMITaggedValue("documentation",description,id);
      write(taggedValue);
    }
  }

  private String buildName(TreeSet v) {
    String name=new String();
    int i=0;
    for(Iterator it=v.iterator();it.hasNext();i++) {
      if(i!=0)
        name+=" ";
      name+=(String)it.next();
    }
    return name;
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIClassWriter(XMIWriter parentWriter, PrintWriter writer) {
    super(parentWriter,writer);
    parentClasses=new TreeSet();
    childClasses=new TreeSet();
  }

  public XMIClassWriter(XMIWriter parentWriter, PrintStream stream) {
    this(parentWriter,new PrintWriter(stream));
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(UMLClass umlClass) {
    String tabulation=tabulator.getTabulator();
    childClasses.clear();
    parentClasses.clear();

    String id=XMIResourceIdGenerator.generateId(umlClass);
    String name=umlClass.getName();
    String visibility = VisibilityTypes.toString(umlClass.getVisibility());
    boolean isAbstract = umlClass.isAbstract();
    String namespace=XMIResourceIdGenerator.generateId(umlClass.getModel());

    inProccessClassName=umlClass.getName();

    writer.println(tabulation+"<UML:Class xmi.id         ='"+id+"'");
    writer.println(tabulation+"           name           ='"+name+"'");
    writer.println(tabulation+"           visibility     ='"+visibility+"'");
    writer.println(tabulation+"           isSpecification='false'");
    writer.println(tabulation+"           isAbstract     ='"+isAbstract+"'");
    writer.println(tabulation+"           isActive       ='false'");

    GeneralizationFilter filter=new GeneralizationFilter();
    filter.setBaseClass(umlClass);
    filter.setDerivedClass(umlClass);
    filter.setOperationMode(true);
    filter.registerGeneralizationConsumer(this);

    filter.filter(umlClass.getModel().getGeneralizations());

    writer.println(tabulation+"           isRoot         ='"+(parentClasses.size()==0)+"'");
    writer.println(tabulation+"           isLeaf         ='"+(childClasses.size()==0)+"'");

    if(childClasses.size()>0) {
      String specializations=buildName(childClasses);
      writer.println(tabulation+"           specialization ='"+specializations+"'");
    }

    if(parentClasses.size()>0) {
      String generalizations=buildName(parentClasses);
      writer.println(tabulation+"           generalization ='"+generalizations+"'");
    }

    writer.println(tabulation+"           namespace      ='"+namespace+"'>");

    tabulator.increaseTabulation();

    writeAttributes(umlClass);

    tabulator.decreaseTabulation();

    writer.println(tabulation+"</UML:Class>");

    writeDescription(umlClass, id);
  }

  //-- Interface: GeneralizationConsumer ---------------------------------------

  public void consumeGeneralization(UMLGeneralization generalization) {
    XMIGeneralization gen=new XMIGeneralization(generalization);

    String id=XMIResourceIdGenerator.generateId(gen);

    if(generalization.getBaseClass().getName().equals(inProccessClassName)) {
      childClasses.add(id);
    } else {
      parentClasses.add(id);
      write(gen);
    }
  }

}