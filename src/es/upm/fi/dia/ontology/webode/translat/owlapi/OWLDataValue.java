package es.upm.fi.dia.ontology.webode.translat.owlapi;

import com.hp.hpl.mesa.rdf.jena.model.Literal;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLDataValue extends OWLCommon
{
	String value;
	OWLDataValue (Literal value_literal, OWLNamesContainer value_names_container)
	{
		value=value_literal.toString();
		names_container=value_names_container;
	}


	public String getValueType()
	{
		return ("Uknown");
	}

	public String getType()
	{
		return (names_container.VALUE);
	}

	public String getValue()
	{
		return (value);
	}
}























