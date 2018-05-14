package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;
import java.io.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.containers.*;

public class XMIWriterHandler implements XMIWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  protected PrintWriter writer      =null;
  protected XMIWriter   parentWriter=null;
  protected Tabulator   tabulator   =null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIWriterHandler(XMIWriter parentWriter, PrintWriter writer) {
    this.writer=writer;
    this.parentWriter=parentWriter;
    tabulator=new Tabulator();
  }

  public XMIWriterHandler(XMIWriter parentWriter, PrintStream stream) {
    this(parentWriter,new PrintWriter(stream));
  }

  //----------------------------------------------------------------------------
  //-- Business logic ----------------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Interface: XMIWriter ----------------------------------------------------

  public void setTabulator(Tabulator tabulator) {
    this.tabulator=tabulator;
  }

  public void write(XMITaggedValue taggedValue) {
    parentWriter.write(taggedValue);
  }

  public void write(XMIStereotype stereotype) {
    parentWriter.write(stereotype);
  }

  public void write(XMIDataType dataType) {
    parentWriter.write(dataType);
  }

  public void write(XMIGeneralization generalization) {
    parentWriter.write(generalization);
  }

}