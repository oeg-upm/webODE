package es.upm.fi.dia.ontology.webode.translat.owlapi;

//First the Java related stuff
import java.util.*;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public abstract class OWLProperty extends OWLNamedElement
{
	Hashtable property_properties_table;
	HashSet property_type;

	public OWLProperty (Resource property_resource,Model model,OWLNamesContainer names_container )
	{
	property_properties_table = new Hashtable();
	property_type = new HashSet();


	String properties_query_string =
			"SELECT ?destination WHERE"
			+ "(?origin,<rdf:type>,?destination)"
			+ "AND (?origin eq <"+property_resource+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;


	Iterator properties_it=_queryRDFModel (properties_query_string, model);

/*
	Query query = new Query(properties_query_string);
	query.setSource(model);
	QueryExecution qe= new QueryEngine(query);
	QueryResults results = qe.exec();
	Iterator properties_it=results;
*/
	OWLClassDescription class_description;
	while (properties_it.hasNext())
	{
		ResultBinding property_res= (ResultBinding)properties_it.next();
		Resource destination_resource = (Resource)property_res.get("destination");
	// This is how it was supposed to be...
	//	property_type.add(new OWLURIReference(destination_resource,names_container));
		property_type.add((new OWLURIReference(destination_resource,names_container)).getURI());


	}








	}


	public void addRangeElement (String relation_name, OWLCommon range_element)
	{
		/*
		System.out.println("----------------------------------> "+relation_name+"->"+range_element);
		System.out.println("----------------------------------> "+property_properties_table);
		*/
		Vector related_descriptions=(Vector)property_properties_table.get(relation_name);
		if (related_descriptions==null)
		{
			related_descriptions = new Vector();
			property_properties_table.put(relation_name,related_descriptions);
		}
		related_descriptions.add(range_element);


	}


	public Enumeration getProperty (String property_name)
	{

		Vector related_properties=(Vector)property_properties_table.get(property_name);
		if (related_properties!=null)
		{
			return related_properties.elements();
		}
		else
		{
			return ((new Vector()).elements());
		}

	}



	public OWLCommon getFirstOfProperty (String property_name)
	{
		Vector related_properties=(Vector)property_properties_table.get(property_name);
		if (related_properties!=null)
		{
			return ((OWLCommon)related_properties.get(0));
		}
		return null;

	}



	public HashSet getPropertyTypes()
	{
		return property_type;
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
