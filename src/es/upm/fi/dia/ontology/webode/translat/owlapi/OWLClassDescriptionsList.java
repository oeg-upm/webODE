package es.upm.fi.dia.ontology.webode.translat.owlapi;

import com.hp.hpl.mesa.rdf.jena.model.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLClassDescriptionsList extends OWLList
{

	public OWLClassDescriptionsList ()
	{
	super();
	}

	public OWLClassDescriptionsList (Resource resource,Model model,OWLNamesContainer names_container)
	{

	super();
	boolean has_more_elements=!(names_container.LIST_NIL.equals(resource.toString()));
	Resource rest_resource=null;
	Resource first_resource=null;
	OWLClassDescriptionReader class_description_reader=null;
	OWLClassDescription class_description=null;
	if (has_more_elements)
	{
		class_description_reader= new OWLClassDescriptionReader(model,names_container);
	}
	//System.out.println("CREANDO LA LISTA.........................................................");


	while (has_more_elements)
	{
		rest_resource=_getRest(resource,model);
		first_resource=(Resource)_getFirst(resource,model);

	//	System.out.println("Este es el rest "+rest_resource);
	//	System.out.println("Este es el first "+first_resource);
		class_description = class_description_reader.read(first_resource);
		list_elements.add(class_description);
		if (names_container.LIST_NIL.equals(rest_resource.toString()))
			has_more_elements=false;
		resource=rest_resource;
	}

	//System.out.println("LA LISTA ES:   "+list_elements);

	}




}












