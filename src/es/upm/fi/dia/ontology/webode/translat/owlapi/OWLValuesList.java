package es.upm.fi.dia.ontology.webode.translat.owlapi;

import com.hp.hpl.mesa.rdf.jena.model.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLValuesList extends OWLList
{



	public OWLValuesList (Resource resource,Model model,OWLNamesContainer names_container)
	{
	super();
	boolean has_more_elements=!(names_container.LIST_NIL.equals(resource.toString()));
	Resource rest_resource=null;
	Object first=null;


	while (has_more_elements)
	{

		rest_resource=_getRest(resource,model);
		first=_getFirst(resource,model);
		try
		{

		OWLDataValue value = new OWLDataValue ((Literal)first,names_container);
	//	System.out.println("Este es el rest "+rest_resource);
	//	System.out.println("Este es el first "+first_resource);
		list_elements.add(value);
		System.out.println("-->>>>>>>>> "+value);
		if (names_container.LIST_NIL.equals(rest_resource.toString()))
		has_more_elements=false;
		resource=rest_resource;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}



	}

}