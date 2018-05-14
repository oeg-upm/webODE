package es.upm.fi.dia.ontology.webode.translat.owlapi;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;


public class OWLOntology extends OWLCommon
{
	private Hashtable ontology_properties_table= new Hashtable();
	private OWLNamesContainer names_container;

	public OWLOntology (Model model,OWLNamesContainer ontology_names_container)
	{

	names_container=ontology_names_container;
	ontology_properties_table= new Hashtable();



	String ontology_query_string =
			"SELECT ?destination WHERE"
			+ "(?origin,<rdf:type>,<"+names_container.ONTOLOGY+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;



	Iterator ontologies_it=_queryRDFModel (ontology_query_string,model);

	Resource ontology_resource=null;
	while (ontologies_it.hasNext())
	{
		ResultBinding ontology_res= (ResultBinding)ontologies_it.next();

		ontology_resource= (Resource)ontology_res.get("origin");


	}
	if (ontology_resource!=null)
	{
		String properties_query_string =
			"SELECT ?origin,?destination WHERE"
			+ "(?origin,?relation,?destination)"
			+ "AND (?origin eq <"+ontology_resource+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;


		Iterator properties_it=_queryRDFModel (properties_query_string, model);

		OWLClassDescriptionReader class_description_reader=null;
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
	}

}






	public Enumeration getProperty (String property_name)
	{
		System.out.println(ontology_properties_table.get(property_name));
		Vector related_properties=(Vector)ontology_properties_table.get(property_name);
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
		Vector related_destinations=(Vector)ontology_properties_table.get(relation_name);
		if (related_destinations==null)
		{
			related_destinations = new Vector();
			ontology_properties_table.put(relation_name,related_destinations);
		}
		related_destinations.add(destination);


	}


	public String getType()
	{
		return (names_container.ONTOLOGY);
	}


}

