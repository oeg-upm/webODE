package es.upm.fi.dia.ontology.webode.translat.rdfsapi;


public class RDFSContainer extends RDFSNamedElement
{

	public RDFSContainer (String class_uri)
	{
		uri=class_uri;
	}

	public String getType()
	{
		return ("Container");
	}

}