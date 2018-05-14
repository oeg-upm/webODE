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

public class XMIAttributeWriter extends XMIWriterHandler {

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void writeInitialValue(UMLAttribute attribute) {
    writer.println(tabulator.getTabulator()+"<UML:Attribute.initialValue>");
    tabulator.increaseTabulation();
    writer.println(tabulator.getTabulator()+"<UML:Expression language = '' body = '"+attribute.getInitialValue()+"' />");
    tabulator.decreaseTabulation();
    writer.println(tabulator.getTabulator()+"</UML:Attribute.initialValue>");
  }

  private void writeMultiplicity(UMLAttribute attribute) {
    String multiplicity=attribute.getMultiplicity();

    String minimum=multiplicity.substring(0,multiplicity.indexOf('.'));
    String maximum=multiplicity.substring(multiplicity.lastIndexOf('.')+1,multiplicity.length());

    String id=XMIResourceIdGenerator.generateId(attribute);

    writer.println(tabulator.getTabulator()+"<UML:StructuralFeature.multiplicity>");
    tabulator.increaseTabulation();
    writer.println(tabulator.getTabulator()+"<UML:Multiplicity >");
    tabulator.increaseTabulation();
    writer.println(tabulator.getTabulator()+"<UML:Multiplicity.range>");
    tabulator.increaseTabulation();
    writer.println(tabulator.getTabulator()+"<UML:MultiplicityRange xmi.id='"+id+"_MultiplicityRange'");
    writer.println(tabulator.getTabulator()+"                       lower ='"+minimum+"'");
    writer.println(tabulator.getTabulator()+"                       upper ='"+maximum+"'/>");
    tabulator.decreaseTabulation();
    writer.println(tabulator.getTabulator()+"</UML:Multiplicity.range>");
    tabulator.decreaseTabulation();
    writer.println(tabulator.getTabulator()+"</UML:Multiplicity>");
    tabulator.decreaseTabulation();
    writer.println(tabulator.getTabulator()+"</UML:StructuralFeature.multiplicity>");
  }

  private void writeDescription(UMLAttribute attribute) {
    String id=XMIResourceIdGenerator.generateId(attribute);

    XMITaggedValue taggedValue=new XMITaggedValue("documentation",
                                                  attribute.getDescription(),
                                                  id);
    write(taggedValue);
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIAttributeWriter(XMIWriter parentWriter, PrintWriter writer) {
    super(parentWriter,writer);
  }

  public XMIAttributeWriter(XMIWriter parentWriter, PrintStream stream) {
    super(parentWriter,stream);
  }

  //----------------------------------------------------------------------------
  //-- Business logic ----------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(UMLAttribute attribute) {
    String tabulation=tabulator.getTabulator();

    String attributeId=XMIResourceIdGenerator.generateId(attribute);

    XMIDataType dataType=new XMIDataType(attribute.getType());

    String dataTypeId=XMIResourceIdGenerator.generateId(dataType);

    writer.println(tabulation+"<UML:Attribute xmi.id         ='"+attributeId+"'");
    writer.println(tabulation+"               name           ='"+attribute.getName()+"'");
    writer.println(tabulation+"               visibility     ='"+VisibilityTypes.toString(attribute.getVisibility())+"'");
    writer.println(tabulation+"               isSpecification='false'");
    writer.println(tabulation+"               ownerScope     ='classifier'");
    writer.println(tabulation+"               changeability  ='changeable'");
    writer.println(tabulation+"               targetScope    ='instance'");
    writer.println(tabulation+"               type           ='"+dataTypeId+"' >");

    tabulator.increaseTabulation();

    writeMultiplicity(attribute);
    writeInitialValue(attribute);

    tabulator.decreaseTabulation();

    writer.println(tabulation+"</UML:Attribute>");

    writeDescription(attribute);
    write(dataType);

    writeStereotype(attribute);
  }

  private void writeStereotype(UMLAttribute attribute) {
    if(attribute.getStereotype()!=null) {
      String id=XMIResourceIdGenerator.generateId(attribute);
      XMIStereotype stereotype=new XMIStereotype(attribute.getStereotype(),id,"Attribute");
      write(stereotype);
    }
  }
}