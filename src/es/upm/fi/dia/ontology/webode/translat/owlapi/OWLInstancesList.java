package es.upm.fi.dia.ontology.webode.translat.owlapi;

import com.hp.hpl.mesa.rdf.jena.model.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLInstancesList extends OWLList
{



	public OWLInstancesList (Resource resource,Model model,OWLNamesContainer names_container)
	{
	super();
	boolean has_more_elements=!(names_container.LIST_NIL.equals(resource.toString()));
	Resource rest_resource=null;
	Resource first_resource=null;


	while (has_more_elements)
	{

		rest_resource=_getRest(resource,model);
		first_resource=(Resource)_getFirst(resource,model);

	//	System.out.println("Este es el rest "+rest_resource);
	//	System.out.println("Este es el first "+first_resource);
		OWLIndividual new_instance= new OWLIndividual (first_resource,model,names_container);
		list_elements.add(new_instance);
		System.out.println("))))))))))))))))))))))---->"+new_instance.getURI());
		if (names_container.LIST_NIL.equals(rest_resource.toString()))
		has_more_elements=false;
		resource=rest_resource;
	}



	}

}