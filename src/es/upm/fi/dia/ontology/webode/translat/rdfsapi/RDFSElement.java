package es.upm.fi.dia.ontology.webode.translat.rdfsapi;


import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.RDFSNamesContainer;

public abstract class RDFSElement implements RDFSCommon
{

	Hashtable properties_table;
	protected RDFSNamesContainer names_container;

	RDFSElement()
	{
		properties_table=new Hashtable();
	}



	public void addProperty (String relation_name, RDFSCommon destination)
	{
//		System.out.println("----------------------------------> "+relation_name+"->"+class_description);
//		System.out.println("----------------------------------> "+class_descriptions_table);
		Vector related_destinations=(Vector)properties_table.get(relation_name);
		if (related_destinations==null)
		{
			related_destinations = new Vector();
			properties_table.put(relation_name,related_destinations);
		}
		related_destinations.add(destination);


	}



	public Enumeration getProperty (String property_name)
	{
		//System.out.println(class_descriptions_table);
		Vector related_properties=(Vector)properties_table.get(property_name);
		if (related_properties!=null)
		{
			return related_properties.elements();
		}
		else
		{
			return ((new Vector()).elements());
		}

	}


	public RDFSCommon getFirstOfProperty (String property_name)
	{
		Vector related_properties=(Vector)properties_table.get(property_name);
		if (related_properties!=null)
		{
			return ((RDFSCommon)related_properties.get(0));
		}
		return null;

	}


	public Enumeration getPropertiesNames()
	{
		return (properties_table.keys());
	}







	protected  QueryResults _queryRDFModel (String query_string, Model model)
	{

	Query query = new Query(query_string);

	query.setSource(model);
	QueryExecution qe= new QueryEngine(query);
	QueryResults results = qe.exec();
	return results;
	}

	protected Enumeration _queryRDFProperty(String element_url,String property_url,Model model)
	{

	Vector destinations = new Vector();
	String relations_query_string =
			"SELECT ?destination WHERE"
			+ "(<"+element_url+">,<"+property_url+">,?destination)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;


	Query query = new Query(relations_query_string);
	query.setSource(model);
	QueryExecution qe= new QueryEngine(query);
	QueryResults results = qe.exec();
	Iterator relations_it=results;


	while (relations_it.hasNext())
	{
		ResultBinding relation_res= (ResultBinding)relations_it.next();
		Object destination = relation_res.get("destination");
		destinations.add(destination);
	}
	return destinations.elements();
	}



}


