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

public class XMIGeneralizationWriter extends XMIWriterHandler {

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void writeDescription(UMLGeneralization generalization, String id) {
    String description=generalization.getDescription();

    if(description!=null) {
      XMITaggedValue taggedValue=new XMITaggedValue("documentation",description,id);
      write(taggedValue);
    }
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIGeneralizationWriter(XMIWriter parentWriter, PrintWriter writer) {
    super(parentWriter,writer);
  }

  public XMIGeneralizationWriter(XMIWriter parentWriter, PrintStream stream) {
    super(parentWriter,stream);
  }

  //----------------------------------------------------------------------------
  //-- Business logic ----------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(XMIGeneralization generalization) {
    String tabulation=tabulator.getTabulator();

    String id=XMIResourceIdGenerator.generateId(generalization);

    writer.println(tabulation+"<UML:Generalization xmi.id       ='"+id+"'");
    writer.println(tabulation+"                    name         ='' visibility = 'public' isSpecification='false'");
    writer.println(tabulation+"                    discriminator=''");
    writer.println(tabulation+"                    parent       ='"+generalization.getBaseClass()+"'");
    writer.println(tabulation+"                    child        ='"+generalization.getDerivedClass()+"' />");

    writeDescription(generalization.getGeneralization(),id);
  }
}