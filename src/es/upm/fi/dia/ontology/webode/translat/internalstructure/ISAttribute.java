package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import es.upm.fi.dia.ontology.webode.service.*;
import org.w3c.dom.*;

public class ISAttribute extends ISStructureElement
{
  private String name;
  private String value;

  public void toWebODE(ODEService ode, String ontologyName, String instanceSetName, String instanceName, String conceptName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException
  {
    try
    {
      ode.addValueToInstance(ontologyName,instanceSetName,instanceName,conceptName,name,value);
    }
    catch (Exception ex)
    {
      System.out.println("This is the instance that fails------name-> "+name+"value>"+value);
    }
  }

  public ISAttribute (String attribute_name, String attribute_value)
  {
    name=attribute_name;
    value=attribute_value;
  }

  public Node obtainXML (Document owner_document)
  {

    Element attribute_element = owner_document.createElement("Attribute");
    if (name!=null)
      addElement(owner_document,attribute_element,"Name",name);

    if (value!=null)
      addElement(owner_document,attribute_element,"Value",value);

    return attribute_element;
  }

}


