package es.upm.fi.dia.ontology.webode.translat.owlapi;
import com.hp.hpl.mesa.rdf.jena.model.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLURIReference extends OWLClassDescription
{
	public OWLURIReference (Resource reference_resource, OWLNamesContainer	reference_names_container)
	{
		uri=reference_resource.getURI();
		namespace=reference_names_container.namespace;
		local_name = reference_resource.getLocalName();
		names_container=reference_names_container;
	}


	public OWLURIReference(String reference_namespace, String reference_local_name)
	{
		namespace=reference_namespace;
		local_name=reference_local_name;
		uri=namespace+local_name;
	}


	public String getType ()
	{
		return names_container.URI_REFERENCE;
	}


}
