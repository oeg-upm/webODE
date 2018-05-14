package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;



public class ISClass extends ISStructureElement
{
    private String name;
    private Vector attributes;

    public void toWebODE(ODEService ode, String ontologyName, String instanceSetName, String instanceName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException
    {
      ISAttribute att;
      for(Enumeration enum=attributes.elements(); enum.hasMoreElements(); ) {
        att=(ISAttribute)enum.nextElement();
        att.toWebODE(ode, ontologyName, instanceSetName, instanceName, name);
      }
    }


    public ISClass (String class_name)
    {
	name=class_name;
	attributes=new Vector();
	}

	public void addAttribute(ISAttribute attr)
	{
		attributes.add(attr);
	}

	public boolean isEmpty()
	{
		return(attributes.size()!=0);
	}

    public Node obtainXML (Document owner_document)
    {

	Element class_element = owner_document.createElement("Class");
	if (name!=null)
	    addElement(owner_document,class_element,"Name",name);

	for (Enumeration e = attributes.elements() ; e.hasMoreElements() ;)
	    {
		class_element.appendChild(((ISAttribute)e.nextElement()).obtainXML(owner_document));
	    }

	return class_element;
    }




}