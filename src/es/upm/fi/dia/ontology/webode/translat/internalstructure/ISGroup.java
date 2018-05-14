package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;

public class ISGroup extends ISStructureElement
{
    private String name;

    private String type;
    private Vector related_concept;

    public void toWebODE(ODEService ode, String ontologyName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException
    {
      ode.addGroup(ontologyName, new Group(name,description,(String[])related_concept.toArray(new String[0])));
    }
	public ISGroup ()
	{
	related_concept= new Vector();
	}


	public ISGroup (String group_name)
	{
		this();
		name=group_name;
	}

	public void setName (String group_name)
	{
	name=group_name;
	}
	public void setDescription(String group_description)
	{
		description=group_description;
	}


	public String getName ()
	{
	return name;
	}


	public void addConcept (String concept_name)
	{
	related_concept.add(concept_name);
	}

	public Enumeration getConceptsNames ()
	{
	return (related_concept.elements());
	}

	public String getType()
	{
	return ((String)type);
	}

	public void setType (String type_of_group)
	{
	type=type_of_group;
	}

	public Node obtainXML (Document owner_document)
    {
	Element group_element = owner_document.createElement("Group");
	if (name!=null)
	    addElement(owner_document,group_element,"Name",name);

	if (description!=null)
	    addElement(owner_document,group_element,"Description",description);

	for (Enumeration e = related_concept.elements() ; e.hasMoreElements() ;)
		addElement (owner_document,group_element,"Related-Concept",(String)(e.nextElement()));

	return group_element;
	}
}

