package es.upm.fi.dia.ontology.webode.translat.owlapi;
import java.util.Enumeration;

import com.hp.hpl.mesa.rdf.jena.model.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLEnumeration extends OWLClassDescription
{
	private OWLInstancesList instances;

	public OWLEnumeration ()
	{
	}

	public OWLEnumeration (Resource enumeration_resource,Model model,OWLNamesContainer enumeration_names_container)
	{
	names_container=enumeration_names_container;
	instances = new OWLInstancesList(enumeration_resource,model,names_container);
	}

	public void addInstance (OWLIndividual instance)
	{
		instances.addElement(instance.getURI());
	}

	public void addInstance (String instance_name)
	{
		instances.addElement(instance_name);
	}

	public boolean hasInstance(String name)
	{
		return (instances.hasElement(name));
	}

	public boolean hasInstance(OWLIndividual instance)
	{
		return (instances.hasElement(instance.getURI()));
	}

	public Enumeration getInstances()
	{
		return (instances.getElements());
	}

	public String getType()
	{
		return (names_container.ENUMERATION);
	}

}
