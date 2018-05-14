package es.upm.fi.dia.ontology.webode.translat.rdfsapi;

import com.hp.hpl.mesa.rdf.jena.model.Literal;

public class RDFSDataValue implements RDFSCommon
{
	String value;
	RDFSDataValue (Literal value_literal)
	{
		value=value_literal.toString();

	}


	public String getValueType()
	{
		return ("Uknown");
	}

	public String getType()
	{
		return ("DataValue");
	}

	public String getValue()
	{
		return (value);
	}
}
