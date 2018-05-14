package es.upm.fi.dia.ontology.webode.translat.owlapi;

import com.hp.hpl.mesa.rdf.jena.model.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLComplement extends OWLClassOperator
{
	public OWLComplement (Resource complement_resource,Model model,OWLNamesContainer names_container)
	{
		super();
		OWLClassDescriptionReader class_description_reader =
		new OWLClassDescriptionReader(model,names_container);
		OWLClassDescription class_description = class_description_reader.read(complement_resource);
		addOperand(class_description);

		//super(complement_resource,model,names_container);

	}

	public String getOperationType()
	{
		return(names_container.COMPLEMENT);
	}

}