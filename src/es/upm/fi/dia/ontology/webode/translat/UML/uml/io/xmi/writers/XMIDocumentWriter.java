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
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.*;

import java.io.*;
import java.util.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.containers.*;

public class XMIDocumentWriter extends    XMIWriterHandler
                               implements UMLModelWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private Vector taggedValues=null;
  private String dtd         ="UML1311.dtd";

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void writeContent(UMLModel model) {
    String tabulation=tabulator.getTabulator();

    writer.println(tabulation+"<XMI.content>");

    tabulator.increaseTabulation();

    writeModel(model);
    writeTaggedValues();

    tabulator.decreaseTabulation();

    writer.println(tabulation+"</XMI.content>");
  }

  private void writeHeader() {
    String tabulation=tabulator.getTabulator();

    writer.println(tabulation+"<XMI.header>");

    tabulator.increaseTabulation();

    writeDocumentation();
    writeMetamodel();

    tabulator.decreaseTabulation();

    writer.println(tabulation+"</XMI.header>");
  }

  private void writeMetamodel() {
    String tabulation=tabulator.getTabulator();

    writer.println(tabulation+"<XMI.metamodel xmi.name='UML' "+
                   "xmi.version='1.3'/>");
  }

  private void writeDocumentation() {
    String tabulation=tabulator.getTabulator();

    writer.println(tabulation+"<XMI.documentation>");

    tabulator.increaseTabulation();

    writeExporter();

    tabulator.decreaseTabulation();

    writer.println(tabulation+"</XMI.documentation>");
  }

  private void writeExporter() {
    String tabulation=tabulator.getTabulator();

    writer.println(tabulation+"<XMI.exporter>O2U XMI Writer</XMI.exporter>");
    writer.println(tabulation+"<XMI.exporterVersion>0.0.3</XMI.exporterVersion>");
  }

  private void writeModel(UMLModel model) {
    XMIModelWriter modelWriter=new XMIModelWriter(this,writer);

    modelWriter.setTabulator(tabulator);
    modelWriter.write(model);
  }

  private void writeTaggedValues() {
    if(taggedValues.size()==0)
      return;

    XMITaggedValueWriter taggedValueWriter=new XMITaggedValueWriter(writer);
    taggedValueWriter.setTabulator(tabulator);
    for(int i=0;i<taggedValues.size();i++)
      taggedValueWriter.write((XMITaggedValue)taggedValues.get(i));
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIDocumentWriter(PrintWriter writer) {
    super(null,writer);
    taggedValues=new Vector();
  }

  public XMIDocumentWriter(PrintStream stream) {
    this(new PrintWriter(stream));
  }

  //----------------------------------------------------------------------------
  //-- Business logic ----------------------------------------------------------
  //----------------------------------------------------------------------------

  public void setDTDFile(String dtdFile) {
    dtd=dtdFile;
  }

  //-- Interface: UMLModelWriter -----------------------------------------------

  public void write(UMLModel model) {
    writer.println("<?xml version = '1.0' encoding = 'ISO-8859-1'?>");
    writer.println("<!DOCTYPE XMI SYSTEM '" + dtd + "' >");
    writer.println("<XMI xmi.version='1.1' "+
                   "xmlns:UML='href://org.omg/UML/1.3' "+
                   /*"timestamp='"+
                   new Date(System.currentTimeMillis())+"'*/">");

    tabulator.increaseTabulation();

    writeHeader();
    writeContent(model);

    tabulator.increaseTabulation();

    writer.println("</XMI>");
  }

  //-- Interface: XMIWriter ----------------------------------------------------

  public void write(XMITaggedValue taggedValue) {
    taggedValues.add(taggedValue);
  }

}