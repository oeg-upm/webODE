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
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.tools.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.containers.*;

public class XMIDataTypeWriter extends XMIWriterHandler {

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIDataTypeWriter(PrintWriter writer) {
    super(null,writer);
  }

  public XMIDataTypeWriter(PrintStream stream) {
    super(null,stream);
  }

  //----------------------------------------------------------------------------
  //-- Business logic ----------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(XMIDataType dataType) {
    String tabulation=tabulator.getTabulator();
    String id=XMIResourceIdGenerator.generateId(dataType);
    writer.println(tabulation+"<UML:DataType xmi.id='"+id+"'");
    writer.println(tabulation+"              name='"+dataType.getName()+"' visibility='public' isSpecification='false'");
    writer.println(tabulation+"              isRoot='false' isLeaf='false' isAbstract='false' />");

  }


}