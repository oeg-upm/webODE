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

public class XMITaggedValueWriter extends XMIWriterHandler {

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMITaggedValueWriter(PrintWriter writer) {
    super(null,writer);
  }

  public XMITaggedValueWriter(PrintStream stream) {
    super(null,stream);
  }

  //----------------------------------------------------------------------------
  //-- Business logic ----------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(XMITaggedValue tv) {
    if(tv.getValue().equals(""))
      return;

    String tabulation=tabulator.getTabulator();

    String id=XMIResourceIdGenerator.generateId(tv);

    writer.println(tabulation+"<UML:TaggedValue xmi.id      ='"+id+"'");
    writer.println(tabulation+"                 modelElement='"+tv.getModelElement()+"'");
    writer.println(tabulation+"                 tag         ='"+tv.getTag()+"'>");
    String aux=tv.getValue();

    aux=aux.replace('\'',' ');
    aux=aux.replaceAll("<","&lt;");
    aux=aux.replaceAll(">","&gt;");
    aux=aux.replaceAll("\n","&#x000d;&#x000a;");

    tabulator.increaseTabulation();
    String tab2=tabulator.getTabulator();

    writer.println(tab2+"<UML:TaggedValue.value>");

    tabulator.increaseTabulation();
    String tab3=tabulator.getTabulator();

    writer.println(tab3+""+aux);
    tabulator.decreaseTabulation();

    writer.println(tab2+"</UML:TaggedValue.value>");
    tabulator.decreaseTabulation();

    writer.println(tabulation+"</UML:TaggedValue>");
  }
}