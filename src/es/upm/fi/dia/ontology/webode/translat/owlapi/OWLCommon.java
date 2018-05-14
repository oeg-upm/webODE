package es.upm.fi.dia.ontology.webode.translat.owlapi;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Iterator;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public abstract class OWLCommon
{
	protected OWLNamesContainer names_container;

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

	public abstract String getType();


}
