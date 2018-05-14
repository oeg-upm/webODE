package es.upm.fi.dia.ontology.webode.translat.rdfsapi;
import com.hp.hpl.mesa.rdf.jena.model.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.RDFSNamesContainer;

public class RDFSURIReference extends RDFSNamedElement
{

	public RDFSURIReference (Resource reference_resource, RDFSNamesContainer	reference_names_container)
	{
		uri=reference_resource.getURI();
		namespace=reference_resource.getNameSpace();
		local_name = reference_resource.getLocalName();
		names_container=reference_names_container;
	}


	public RDFSURIReference(String reference_namespace, String reference_local_name)
	{
		namespace=reference_namespace;
		local_name=reference_local_name;
		uri=namespace+local_name;
	}


	public String getType ()
	{
		return names_container.URI_REFERENCE;
	}

	public String toString ()
	{
		return ("RDFSURIReference:"+uri);
	}

}
