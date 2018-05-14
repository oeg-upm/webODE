package es.upm.fi.dia.ontology.webode.translat.owlapi;

import com.hp.hpl.mesa.rdf.jena.model.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLUnion extends OWLClassOperator
{
	public OWLUnion (Resource union_resource,Model model,OWLNamesContainer names_container)
	{
		super(union_resource,model,names_container);
	}

	public String getOperationType()
	{
		return(names_container.UNION);
	}

}