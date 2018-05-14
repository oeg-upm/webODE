package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;

public class ISInstanceAttributeDescriptor extends ISAttributeDescriptor
{
  protected String minimum_value;
  protected String maximum_value;

  public void toWebODE(ODEService ode, String ontologyName, String conceptName) throws java.rmi.RemoteException, WebODEException, java.sql.SQLException, ISException
  {
    int idx=1;
    String dataTypeName=Attribute.getXMLSchemaDataTypeName(type);
    boolean found=dataTypeName!=null;

    for(; !found && idx<ValueTypes.NAMES.length; idx++)
      found=ValueTypes.NAMES[idx-1].equals(type);

    if(found)
      dataTypeName=type;
    else
      throw new ISException("Type '" + type + "'not found");

    ode.insertInstanceAttribute(ontologyName,new InstanceAttributeDescriptor(name,conceptName,dataTypeName,this.measurement_unit,this.precision,Integer.parseInt(this.minimum_cardinality),Integer.parseInt(this.maximum_cardinality),this.description,this.minimum_value,this.maximum_value));
  }

  public 	ISInstanceAttributeDescriptor(String attr_name, String attr_type,
      String attr_min_cardinality,String attr_max_cardinality)

  {
    name=attr_name;
    maximum_cardinality=attr_max_cardinality;
    minimum_cardinality=attr_min_cardinality;
    type=attr_type;
    values=new Vector();
    related_references=new Vector();
    synonyms= new Vector();
    abbreviations=new Vector();
    related_formulas= new Vector();
    inferreds= new Vector();

  }

  public	ISClassAttributeDescriptor upgradeToClassAttribute ()
  {
    ISClassAttributeDescriptor aux_attr= new ISClassAttributeDescriptor (name,minimum_cardinality,maximum_cardinality,type);
    aux_attr.setDescription(description);
    aux_attr.setMeasurementUnit(measurement_unit);
    aux_attr.setPrecision(precision);
    aux_attr.setRelatedReferences(related_references);
    aux_attr.setSynonyms(synonyms);
    aux_attr.setAbbreviations(abbreviations);
    aux_attr.setRelatedFormulas(related_formulas);
    aux_attr.setInferreds(inferreds);
    return aux_attr;

  }




/*
  IEInstanceAttributeDescriptor (String attribute_name,String attribute_minimum,
        String attribute_maximum, String attribute_type)
 {
  name=attribute_name;
  description=attribute_name+"  att descriptor";
  maximum_cardinality=attribute_maximum;
  minimum_cardinality=attribute_minimum;
  type=attribute_type;
  minimum_value=attribute_minimum;
  maximum_value=attribute_maximum;
  values=new Vector();
  related_references=new Vector();
  related_references.addElement("Patterson&Hennesy");
  related_references.addElement("Aho");
  synonyms= new Vector();
  abbreviations=new Vector();
  abbreviations.addElement(new IEAbbreviation("inc","incorporated"));
  abbreviations.addElement(new IEAbbreviation("SA","Sociedad Anonima"));
  related_formulas= new Vector();
  related_formulas.addElement("3+4=7");
  related_formulas.addElement("3+4=8");
  inferreds= new Vector();
  inferreds.addElement("inf1");
  inferreds.addElement("inf2");
  inferreds.addElement("inf3");


 }
*/

  public Node obtainXML (Document owner_document)
  {
    Element attribute_element= owner_document.createElement("Instance-Attribute");

    if (name!=null)
      addElement(owner_document,attribute_element,"Name",name);


    if (description!=null)
      addElement(owner_document,attribute_element,"Description",description);

    if (type!=null)
      addElement(owner_document,attribute_element,"Type",type);


    if (minimum_cardinality!=null)
      addElement(owner_document,attribute_element,"Minimum-Cardinality",minimum_cardinality);

    if (maximum_cardinality!=null)
      addElement(owner_document,attribute_element,"Maximum-Cardinality",maximum_cardinality);

    if (measurement_unit!=null)
      addElement(owner_document,attribute_element,"Measurement-Unit",measurement_unit);

    if (precision!=null)
      addElement(owner_document,attribute_element,"Precision",measurement_unit);


    if (minimum_value!=null)
      addElement(owner_document,attribute_element,"Minimum-Value",minimum_cardinality);


    if (maximum_value!=null)
      addElement(owner_document,attribute_element,"Maximum-Value",maximum_cardinality);

    for (Enumeration e = values.elements() ; e.hasMoreElements() ;)
      addElement (owner_document,attribute_element,"Value",(String)(e.nextElement()));


    for (Enumeration e = related_references.elements() ; e.hasMoreElements() ;)
      addElement (owner_document,attribute_element,"Related-Reference",(String)(e.nextElement()));

    for (Enumeration e = synonyms.elements() ; e.hasMoreElements() ;)
    {
      attribute_element.appendChild(((ISSynonym) e.nextElement()).obtainXML(owner_document));
    }

    for (Enumeration e = abbreviations.elements() ; e.hasMoreElements() ;)
    {
      attribute_element.appendChild(((ISAbbreviation) e.nextElement()).obtainXML(owner_document));
    }

    for (Enumeration e = related_formulas.elements() ; e.hasMoreElements() ;)
      addElement (owner_document,attribute_element,"Related-Formula",(String)(e.nextElement()));


    for (Enumeration e = inferreds.elements() ; e.hasMoreElements() ;)
      addElement (owner_document,attribute_element,"Inferred",(String)(e.nextElement()));

    return attribute_element;
  }
}