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

public class XMIStereotypeWriter extends XMIWriterHandler {

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIStereotypeWriter(PrintWriter writer) {
    super(null,writer);
  }

  public XMIStereotypeWriter(PrintStream stream) {
    super(null,stream);
  }

  //----------------------------------------------------------------------------
  //-- Business logic ----------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(XMIStereotype stereotype) {
    String tabulation=tabulator.getTabulator();

    String id=XMIResourceIdGenerator.generateId(stereotype);

    writer.println(tabulation+"<UML:Stereotype xmi.id='"+id+"'");
    writer.println(tabulation+"                name  ='"+stereotype.getName()+"' visibility='public' isSpecification='false'");
    writer.println(tabulation+"                isRoot='false' isLeaf='false' isAbstract='false'");
    writer.println(tabulation+"                icon='' baseClass='"+stereotype.getBase()+"'");
    writer.println(tabulation+"                extendedElement='"+stereotype.getModelElement()+"'/>");
  }

}