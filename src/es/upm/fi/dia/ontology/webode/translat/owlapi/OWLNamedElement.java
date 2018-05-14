package es.upm.fi.dia.ontology.webode.translat.owlapi;

public abstract class OWLNamedElement extends OWLCommon
{
	protected String uri;
	protected String namespace;
	protected String local_name;
	//protected OWLNamesContainer names_container;


	public String getURI()
	{
	return uri;
	}

	public String getNamespace()
	{
	return namespace;
	}

	public String getLocalName()
	{
	return local_name;
	}

	public void setLocalName(String  new_local_name)
	{
	local_name=new_local_name;
	}

	public void setNameSpace(String  element_namespace)
	{
	namespace=element_namespace;
	}

	public void setURI(String element_uri)
	{
	uri=element_uri;
	}

}