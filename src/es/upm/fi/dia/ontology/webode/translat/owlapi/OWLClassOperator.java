package es.upm.fi.dia.ontology.webode.translat.owlapi;
import java.util.Enumeration;

import com.hp.hpl.mesa.rdf.jena.model.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public abstract class OWLClassOperator extends OWLClassDescription
{
	protected OWLNamesContainer names_container;
	protected OWLClassDescriptionsList class_descriptions_list;

	public OWLClassOperator ()
	{
	}


	public OWLClassOperator (Resource operator_resource,Model model,OWLNamesContainer operator_names_container)
	{
	names_container=operator_names_container;
	class_descriptions_list = new OWLClassDescriptionsList(operator_resource,model,names_container);
	}

	public void addOperand (OWLClassDescription class_descriptor)
	{
		class_descriptions_list.addElement(class_descriptor);
	}


	public Enumeration getOperands()
	{
		return (class_descriptions_list.getElements());
	}

	public String getType()
	{
		return (names_container.OPERATOR);
	}

	public abstract String getOperationType();


}
