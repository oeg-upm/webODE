package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;

public class ISRelationInstance extends ISStructureElement
{
  private String name;
  private String instance_of;
  private String originConcept;
  private String destinationConcept;
  private String origin;
  private String destination;

  public void toWebODE(ODEService ode, String ontologyName, String instanceSetName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException
  {

    try
    {
      ode.insertRelationInstance(ontologyName,instance_of,instanceSetName,name,origin,destination);
//        ode.insertRelationInstance(ontologyName,new TermRelationInstance(name,new TermRelation(ontologyName,null,instance_of,origin,destination),instanceSetName,description,origin,destination));
    }
    catch (Exception ex)
    {
      System.out.println("The relation instance "+name+" instance of "+instance_of+" from "+" to "+"cannot be introduced into WebODE");
      System.out.println(ex.getMessage());
    }
  }

  public ISRelationInstance ()
  {
  }


  public ISRelationInstance (String relation_name, String relation_instance_of)
  {
    name=relation_name;
    instance_of=relation_instance_of;
  }

  public String getName()
  {
    return name;
  }

  public void setName (String relation_instance_name)
  {
    name=relation_instance_name;
  }

  public void setOrigin (String relation_instance_origin)
  {
    origin=relation_instance_origin;
  }

  public void setDestination (String relation_instance_destination)
  {
    destination=relation_instance_destination;
  }

  public void setParent(String parent_name)
  {
    instance_of=parent_name;
  }

  public Node obtainXML (Document owner_document)
  {

    Element relation_instance_element = owner_document.createElement("Relation-Instance");
    if (name!=null)
      addElement(owner_document,relation_instance_element,"Name",name);

    if (instance_of!=null)
      addElement(owner_document,relation_instance_element,"Instance-of",instance_of);

    if (description!=null)
      addElement(owner_document,relation_instance_element,"Description",description);

    if (origin!=null)
      addElement(owner_document,relation_instance_element,"Origin",origin);

    if (destination!=null)
      addElement(owner_document,relation_instance_element,"Destination",destination);
    return relation_instance_element;
  }

}


