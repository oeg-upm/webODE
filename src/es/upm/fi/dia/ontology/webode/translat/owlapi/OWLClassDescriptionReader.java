package es.upm.fi.dia.ontology.webode.translat.owlapi;
import java.util.Iterator;


import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLClassDescriptionReader
{
	Model model;
	OWLNamesContainer names_container;

	public OWLClassDescriptionReader (Model reader_model, OWLNamesContainer reader_names_container)
	{
		model=reader_model;
		names_container=reader_names_container;
	}

	public OWLClassDescription read(Resource resource)
	{
	//System.out.println("--------------//"+resource);
	if (!resource.isAnon())
	{

	//	System.out.println("Es un URI reference "+resource);
		return (new OWLURIReference(resource,names_container));

	}
	else
	{

		if (_isRestriction(resource))
		{
			//System.out.println("Is a restriction!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+resource);
			return (new OWLRestriction(resource,model,names_container));
		}
		else
		{
			if (_isClass(resource))
			{
			//System.out.println("Is a anonymclass!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+resource);
			return(	new OWLClass(resource,model,names_container));

			}
			else
			{
				if (_isEnumeration(resource))
				{
					//System.out.println("Es un one of, joete tu"+resource);
					return(new OWLEnumeration(resource,model,names_container));
					//System.out.println("Era un one of, joete otra vez");
				}

				if (_isClassOperator(resource,names_container.UNION_OF))
				{
					return(new OWLUnion(resource,model,names_container));
				}

				if (_isClassOperator(resource,names_container.INTERSECTION_OF))
				{
					return(new OWLIntersection(resource,model,names_container));
				}



			}

		}

	}
	return null;
}



private boolean _isRestriction(Resource resource)
{
	String relations_query_string ="SELECT ?origin WHERE"
			+ "(?origin,<rdf:type>,<owl:Restriction>)"
			+ "AND (?origin eq <"+resource+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;

	Query query = new Query(relations_query_string);
	query.setSource(model);
	QueryExecution qe= new QueryEngine(query);
	QueryResults results = qe.exec();
	Iterator res_it=results;
	return (res_it.hasNext());
}

private boolean _isEnumeration(Resource resource)
{
	String relations_query_string = 	"SELECT ?destination WHERE"
		+ "(?origin,<owl:oneOf>,?destination)"
		+ "AND (?destination eq <"+resource+">)"
		+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
		+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
		+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;

	Query query = new Query(relations_query_string);
	query.setSource(model);
	QueryExecution qe= new QueryEngine(query);
	QueryResults results = qe.exec();
	Iterator res_it=results;
	return (res_it.hasNext());

}

private boolean _isClass(Resource resource)
{
	String relations_query_string = 	"SELECT ?origin WHERE"
		+ "(?origin,<rdf:type>,<owl:Class>)"
		+ "AND (?origin eq <"+resource+">)"
		+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
		+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
		+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;

	Query query = new Query(relations_query_string);
	query.setSource(model);
	QueryExecution qe= new QueryEngine(query);
		QueryResults results = qe.exec();
		Iterator res_it=results;
	return (res_it.hasNext());


}


private boolean _isClassOperator(Resource resource, String url_of_operator)
{
	String relations_query_string = 	"SELECT ?destination WHERE"
		+ "(?origin,<"+url_of_operator+">,?destination)"
		+ "AND (?destination eq <"+resource+">)"
		+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
		+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
		+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;

	Query query = new Query(relations_query_string);
	query.setSource(model);
	QueryExecution qe= new QueryEngine(query);
		QueryResults results = qe.exec();
		Iterator res_it=results;
	return (res_it.hasNext());


}






}
