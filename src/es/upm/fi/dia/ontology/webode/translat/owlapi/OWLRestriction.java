package es.upm.fi.dia.ontology.webode.translat.owlapi;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLRestriction extends OWLClassDescription
{


	Hashtable restriction_properties_table;



	public OWLRestriction (Resource restriction_resource,Model model,OWLNamesContainer restriction_names_container)
	{
		restriction_properties_table= new Hashtable();
		names_container=restriction_names_container;
		OWLClassDescriptionReader class_description_reader = null;
		/*
		HashSet n_relations = new HashSet();
		taxonomy_relations.add(names_container.TYPE_RELATION);
		*/
		local_name=names_container.bindAnonymous(restriction_resource,names_container.RESTRICTION);
		namespace =names_container.namespace;
		uri=namespace+local_name;


		String properties_query_string =
				"SELECT ?destination WHERE"
				+ "(?origin,?relation,?destination)"
				+ "AND (?origin eq <"+restriction_resource+">)"
				+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
				+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
				+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;


		Iterator properties_it=_queryRDFModel (properties_query_string,model);



		OWLClassDescription class_description;
		while (properties_it.hasNext())
		{
			ResultBinding properties_res= (ResultBinding)properties_it.next();
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
					if (_isDataRange((Resource)destination,model))
					{

					addProperty(property_resource.getURI(),new OWLDataRange((Resource)destination,model,names_container));
					}
					else
					{
						if (class_description_reader==null)
						{
							class_description_reader=
							new	OWLClassDescriptionReader(model,names_container);

						}
							destination=class_description_reader.read((Resource)destination);
							addProperty(property_resource.getURI(),(OWLClassDescription)destination);
					}

				}
				else
				{
				System.out.println("Misa non sepo");

				}
			}


//		System.out.println ("		"+"R: "+property_resource+"D: "+destination_resource);
		//class_description = class_description_reader.read(destination_resource);

		 //addClassDescription (relation_resource.getURI(),class_description);

		}



	System.out.println("Esta es su tabla de properties ");
	System.out.println("|"+	restriction_properties_table );


	}








	public String getType()
	{
		return names_container.RESTRICTION;
	}







	public Enumeration getProperty (String property_name)
	{
		System.out.println(restriction_properties_table.get(property_name));
		Vector related_properties=(Vector)restriction_properties_table.get(property_name);
		if (related_properties!=null)
		{
			return related_properties.elements();
		}
		else
		{
			return ((new Vector()).elements());
		}

	}

	public void addProperty (String relation_name, OWLCommon destination)
	{
//		System.out.println("----------------------------------> "+relation_name+"->"+class_description);
//		System.out.println("----------------------------------> "+class_descriptions_table);
		Vector related_destinations=(Vector)restriction_properties_table.get(relation_name);
		if (related_destinations==null)
		{
			related_destinations = new Vector();
			restriction_properties_table.put(relation_name,related_destinations);
		}
		related_destinations.add(destination);


	}


	public OWLCommon getFirstOfProperty (String property_name)
	{
		Vector related_properties=(Vector)restriction_properties_table.get(property_name);
		if (related_properties!=null)
		{
			return ((OWLCommon)related_properties.get(0));
		}
		return null;

	}

	protected boolean _isDataRange(Resource resource,Model model)
	{
	String relations_query_string ="SELECT ?origin WHERE"
			+ "(?origin,<rdf:type>,<owl:DataRange>)"
			+ "AND (?origin eq <"+resource+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;

	Iterator res_it=_queryRDFModel (relations_query_string,model);
	return (res_it.hasNext());
	}







}
