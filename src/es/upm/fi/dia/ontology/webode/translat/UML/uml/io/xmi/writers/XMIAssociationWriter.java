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

import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.tools.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.containers.*;

public class XMIAssociationWriter extends XMIWriterHandler {

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void writeConnectionEnd(UMLAssociation association,
                                  UMLClass umlClass,
                                  String   roleName,
                                  int      minimumCardinality,
                                  int      maximumCardinality,
                                  boolean  isOrigin) {

    String id     =XMIResourceIdGenerator.generateId(association,isOrigin);
    String classId=XMIResourceIdGenerator.generateId(umlClass);

    writer.println(tabulator.getTabulator()+"<UML:AssociationEnd xmi.id         ='"+id+"'");
    writer.println(tabulator.getTabulator()+"                    name           ='"+roleName+"'");
    writer.println(tabulator.getTabulator()+"                    visibility     ='public'");
    writer.println(tabulator.getTabulator()+"                    isSpecification='false'");
    writer.println(tabulator.getTabulator()+"                    isNavigable    ='"+isOrigin+"'");
    writer.println(tabulator.getTabulator()+"                    ordering       ='unordered'");
    writer.println(tabulator.getTabulator()+"                    aggregation    ='none'");
    writer.println(tabulator.getTabulator()+"                    targetScope    ='instance'");
    writer.println(tabulator.getTabulator()+"                    changeability  ='changeable'");
    writer.println(tabulator.getTabulator()+"                    type           ='"+classId+"' >");
    tabulator.increaseTabulation();
    writer.println(tabulator.getTabulator()+"<UML:AssociationEnd.multiplicity>");
    tabulator.increaseTabulation();
    writer.println(tabulator.getTabulator()+"<UML:Multiplicity>");
    tabulator.increaseTabulation();
    writer.println(tabulator.getTabulator()+"<UML:Multiplicity.range>");
    tabulator.increaseTabulation();
    writer.println(tabulator.getTabulator()+"<UML:MultiplicityRange xmi.id='"+id+"_MultiplicityRange'");
    writer.println(tabulator.getTabulator()+"                       lower ='"+minimumCardinality+"'");
    writer.println(tabulator.getTabulator()+"                       upper ='"+maximumCardinality+"' />");
    tabulator.decreaseTabulation();
    writer.println(tabulator.getTabulator()+"</UML:Multiplicity.range>");
    tabulator.decreaseTabulation();
    writer.println(tabulator.getTabulator()+"</UML:Multiplicity>");
    tabulator.decreaseTabulation();
    writer.println(tabulator.getTabulator()+"</UML:AssociationEnd.multiplicity>");
    tabulator.decreaseTabulation();
    writer.println(tabulator.getTabulator()+"</UML:AssociationEnd>");
  }

  private void writeConnection(UMLAssociation association) {
    String tabulation=tabulator.getTabulator();

    writer.println(tabulation+"<UML:Association.connection>");
    tabulator.increaseTabulation();

    writeConnectionEnd(association,
                       association.getOriginClass(),
                       association.getOriginClassRole(),
                       association.getOriginClassMinimumCardinality(),
                       association.getOriginClassMaximumCardinality(),true);

    writeConnectionEnd(association,
                       association.getDestinationClass(),
                       association.getDestinationClassRole(),
                       association.getDestinationClassMinimumCardinality(),
                       association.getDestinationClassMaximumCardinality(),false);

    tabulator.decreaseTabulation();
    writer.println(tabulation+"</UML:Association.connection>");
  }

  private void writeDescription(UMLAssociation association, String id) {
    String description=association.getDescription();

    if(description!=null) {
      XMITaggedValue taggedValue=new XMITaggedValue("documentation",description,id);
      write(taggedValue);
    }
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIAssociationWriter(XMIWriter parentWriter, PrintWriter writer) {
    super(parentWriter,writer);
  }

  public XMIAssociationWriter(XMIWriter parentWriter, PrintStream stream) {
    this(parentWriter,new PrintWriter(stream));
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(UMLAssociation association) {
    String tabulation=tabulator.getTabulator();

    String id=XMIResourceIdGenerator.generateId(association);

    writer.println(tabulation+"<UML:Association xmi.id         ='"+id+"'");
    writer.println(tabulation+"                 name           ='"+association.getName()+"'");
    writer.println(tabulation+"                 visibility     ='public'");
    writer.println(tabulation+"                 isSpecification='false'");
    writer.println(tabulation+"                 isRoot         ='false'");
    writer.println(tabulation+"                 isLeaf         ='false'");
    writer.println(tabulation+"                 isAbstract     ='false'>");

    tabulator.increaseTabulation();

    writeConnection(association);

    tabulator.decreaseTabulation();

    writer.println(tabulation+"</UML:Association>");

    writeDescription(association, id);
  }

}
