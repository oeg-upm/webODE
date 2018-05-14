package es.upm.fi.dia.ontology.webode.translat.owlapi;
import java.util.Vector;
import java.util.Enumeration;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

public abstract class OWLList
{

	protected Vector list_elements;


	public OWLList ()
	{
		list_elements=new Vector();
	}





	protected Resource _getRest(Resource resource,Model model)
	{

		Resource rest_resource=null;
		String rest_query_string ="SELECT ?destination WHERE"
			+ "(?x,<rdf:rest>,?destination)"
			+ "AND (?x eq <"+resource+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;

		Query query = new Query(rest_query_string);
		query.setSource(model);
		QueryExecution qe= new QueryEngine(query);
		QueryResults results = qe.exec();

		while (results.hasNext())
		{
			ResultBinding rest_res= (ResultBinding)results.next();
			rest_resource = (Resource)rest_res.get("destination");
		}

		return rest_resource;

	}


	protected Object _getFirst(Resource resource,Model model)
	{
	Object first=null;
	Resource rest_resource=null;
	String rest_query_string ="SELECT ?destination WHERE"
			+ "(?x,<rdf:first>,?destination)"
			+ "AND (?x eq <"+resource+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;

		Query query = new Query(rest_query_string);
		query.setSource(model);
		QueryExecution qe= new QueryEngine(query);
		QueryResults results = qe.exec();

		while (results.hasNext())
		{
			ResultBinding first_res= (ResultBinding)results.next();
			first = first_res.get("destination");
		}

	return first;

	}


	public Enumeration getElements()
	{
		return (list_elements.elements());
	}

	public void addElement(Object element)
	{
		list_elements.add(element);
	}

	public boolean hasElement(Object element)
	{
		return (list_elements.indexOf(element)>0);
	}


}
