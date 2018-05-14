package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;




public class ISClassAttributeDescriptor extends ISAttributeDescriptor
{
  public void toWebODE(ODEService ode, String ontologyName, String conceptName) throws java.rmi.RemoteException, WebODEException, java.sql.SQLException, ISException
  {
    int idx=1;
    String xmlDataTypeName=Attribute.getXMLSchemaDataTypeName(type);
    boolean found=xmlDataTypeName!=null;

    for(; !found && idx<ValueTypes.NAMES.length; idx++)
      found=ValueTypes.NAMES[idx-1].equals(type);

    if(!found)
      throw new ISException("Type '" + type + "'not found");
    ode.insertClassAttribute(ontologyName,new ClassAttributeDescriptor(name,conceptName,ontologyName,this.measurement_unit,this.precision,Integer.parseInt(this.minimum_cardinality),Integer.parseInt(this.maximum_cardinality),this.description));
  }

  public ISClassAttributeDescriptor (String attribute_name,String attribute_minimum,
                                     String attribute_maximum, String attribute_type)
  {
    name=attribute_name;
    maximum_cardinality=attribute_maximum;
    minimum_cardinality=attribute_minimum;
    type=attribute_type;
    values= new Vector();
    related_references= new Vector();
    synonyms=new Vector();
    abbreviations= new Vector();
    related_formulas= new Vector();
    inferreds= new Vector();


  }

  public Node obtainXML (Document owner_document)
  {
    Element attribute_element= owner_document.createElement("Class-Attribute");

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