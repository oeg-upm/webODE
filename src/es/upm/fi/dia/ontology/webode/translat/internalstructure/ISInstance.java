package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


import es.upm.fi.dia.ontology.webode.service.*;


public class ISInstance extends ISStructureElement
{
    private String name;
    //private String instance_of;
    private Vector instance_of;

    private Vector instance_classes; //Match with DTD.class (java reserved word)


    public void toWebODE(ODEService ode, String ontologyName, String instanceSetName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException
    {
      try
      {
      ode.insertInstance(ontologyName, new Instance(name,(String)instance_of.elementAt(0),instanceSetName,description));
      }
      catch(Exception e)
      {
	System.out.println("Esta es la que peta "+name+" su padre es "+(String)instance_of.elementAt(0)+"en"+instanceSetName);
      }
      ISClass iclass;
      for(Enumeration enum=instance_classes.elements(); enum.hasMoreElements(); ) {
        iclass=(ISClass)enum.nextElement();
        iclass.toWebODE(ode, ontologyName, instanceSetName, name);
      }
    }

   public ISInstance (String instance_name)
    {
	empty=false;
	name=instance_name;
	instance_of= new Vector();
	instance_classes=new Vector();
    }







    public ISInstance (String instance_name, String instance_instance_of)
    {
	empty=false;
	name=instance_name;
	instance_of= new Vector();
	instance_of.add(instance_instance_of);
	//instance_of=instance_instance_of;
	instance_classes=new Vector();
    }

	public String getName()
	{
		return name;
	}

	public void addClass(ISClass new_class)
	{
		instance_classes.add(new_class);
	}

	public void addParentConcept(String parent_class)
	{
		instance_of.add(parent_class);
	}

	public	Enumeration getParents ()
	{
		return instance_of.elements();
	}

	public String getFirstParent()
	{
		return ((String)instance_of.firstElement());
	}


    public Node obtainXML (Document owner_document)
    {

	Element instance_element = owner_document.createElement("Instance");
	if (name!=null)
	    addElement(owner_document,instance_element,"Name",name);
	/*
	if (instance_of!=null)
	    addElement(owner_document,instance_element,"Instance-of",instance_of);
	*/



	for (Enumeration e = instance_of.elements() ; e.hasMoreElements() ;)
	   	addElement (owner_document,instance_element,"Instance-of",(String)(e.nextElement()));



	if (description!=null)
	    addElement(owner_document,instance_element,"Description",description);

	for (Enumeration e = instance_classes.elements() ; e.hasMoreElements() ;)
	    {
		instance_element.appendChild(((ISClass)e.nextElement()).obtainXML(owner_document));
	    }
	return instance_element;
	}
}
