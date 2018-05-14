package es.upm.fi.dia.ontology.webode.translat.owlapi;

import com.hp.hpl.mesa.rdf.jena.model.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;


public class OWLIntersection extends OWLClassOperator
{
	public OWLIntersection (Resource intersection_resource,Model model,OWLNamesContainer names_container)
	{
		super(intersection_resource,model,names_container);
	}

	public String getOperationType()
	{
		return(names_container.INTERSECTION);
	}

}