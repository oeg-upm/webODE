package es.upm.fi.dia.ontology.webode.translat.owlapi;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Iterator;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLClass extends OWLClassDescription
{

	Hashtable class_properties_table;
	boolean is_anonymous;

	public OWLClass (String class_uri)
	{
		//superclasses = new Vector();
		//subclasses = new Vector();
		uri=class_uri;
		class_properties_table = new Hashtable();
	}



	public OWLClass (Resource resource,Model model,OWLNamesContainer class_names_container)
	{
		//String class_name=resource.getLocalName();
		//String class_namespace=names_container.getOntologyNamespace();
		//String class_uri;

		class_properties_table= new Hashtable();
		names_container=class_names_container;
		HashSet non_taxonomy_relations = new HashSet();
		non_taxonomy_relations.add(names_container.TYPE);


		if (!resource.isAnon())
		{
		local_name=resource.getLocalName();
		uri = resource.getURI();
		names_container.bind(resource,uri);//Esto vale pa algo?
		is_anonymous=false;
		}
		else
		{
		local_name=names_container.bindAnonymous(resource,names_container.CLASS);
		uri=names_container.namespace+local_name;
		namespace=names_container.namespace;
		is_anonymous=true;

		}



		String relations_query_string =
				"SELECT ?destination WHERE"
				+ "(?origin,?relation,?destination)"
				+ "AND (?origin eq <"+resource+">)"
				+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
				+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
				+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;



		Query query = new Query(relations_query_string);
		query.setSource(model);
		QueryExecution qe= new QueryEngine(query);
		QueryResults results = qe.exec();
		Iterator relations_it=results;

		OWLClassDescription class_description;
		OWLClassDescriptionReader class_description_reader=null;


	while (relations_it.hasNext())
	{
		ResultBinding properties_res= (ResultBinding)relations_it.next();
		Object destination = properties_res.get("destination");
		Resource property_resource= (Resource)properties_res.get("relation");
		if (destination instanceof Literal)
		{
			destination=new OWLDataValue ((Literal)destination,names_container);
			addProperty(property_resource.getURI(),(OWLDataValue)destination);
		}
		else
		{
			if (destination instanceof Resource)
			{
				if (class_description_reader==null)
				{
					class_description_reader=
					new	OWLClassDescriptionReader(model,names_container);

				}
					destination=class_description_reader.read((Resource)destination);
					addProperty(property_resource.getURI(),(OWLClassDescription)destination);
			}
			else
			{
				System.out.println("Misa non sepo");

			}
		}


	}

//	System.out.println("Esta es su tabla de descriptions de "+uri);
//	System.out.println("|"+	class_properties_table );


	}








	public String getType()
	{
		return names_container.CLASS;
	}



	public void addProperty (String relation_name, OWLCommon destination)
	{
//		System.out.println("----------------------------------> "+relation_name+"->"+class_description);
//		System.out.println("----------------------------------> "+class_descriptions_table);
		Vector related_destinations=(Vector)class_properties_table.get(relation_name);
		if (related_destinations==null)
		{
			related_destinations = new Vector();
			class_properties_table.put(relation_name,related_destinations);
		}
		related_destinations.add(destination);


	}



	public Enumeration getProperty (String property_name)
	{
		//System.out.println(class_descriptions_table);
		Vector related_properties=(Vector)class_properties_table.get(property_name);
		if (related_properties!=null)
		{
			return related_properties.elements();
		}
		else
		{
			return ((new Vector()).elements());
		}

	}








}
