package es.upm.fi.dia.ontology.webode.translat.rdfsapi;
import java.util.Iterator;
import java.util.Hashtable;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.RDFSNamesContainer;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

public class RDFSClass extends RDFSNamedElement
{

	private boolean is_anonymous;
	public RDFSClass (Resource resource,Model model,RDFSNamesContainer class_names_container, Hashtable classes_table)
	{

		super();
		names_container=class_names_container;

		if (!resource.isAnon())
		{
			local_name=resource.getLocalName();
			uri = resource.getURI();
			namespace=resource.getNameSpace();
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



	while (relations_it.hasNext())
	{
		ResultBinding properties_res= (ResultBinding)relations_it.next();
		Object destination = properties_res.get("destination");
		Resource property_resource= (Resource)properties_res.get("relation");
		if (destination instanceof Literal)
		{
			destination=new RDFSDataValue ((Literal)destination);
			addProperty(property_resource.getURI(),(RDFSDataValue)destination);
		}
		else
		{
			if ((destination instanceof Resource))
			{
				if (!((Resource)destination).isAnon())
				{
					RDFSURIReference reference = new RDFSURIReference((Resource)destination,names_container);
					addProperty(property_resource.getURI(),reference);

				}
				else
				{
					if ((!_isContainer((Resource) destination,model)))
					{
						RDFSClass anon_class= new RDFSClass ((Resource) destination,model,class_names_container,classes_table);
						classes_table.put(anon_class.getURI(),anon_class);
						addProperty(property_resource.getURI(),anon_class);

					}
				}
			}
		}
	}


//	System.out.println ("TABLA DE "+uri+ properties_table);
	}





	public String toString()
	{
		return ("RDFSClass:["+uri+"]");
	}


	private boolean _isClass(Resource resource,Model model)
	{
	String relations_query_string = 	"SELECT ?origin WHERE"
		+ "(?origin,<rdf:type>,<rdfs:Class>)"
		+ "AND (?origin eq <"+resource+">)"
		+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
		+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";

	Query query = new Query(relations_query_string);
	query.setSource(model);
	QueryExecution qe= new QueryEngine(query);
		QueryResults results = qe.exec();
		Iterator res_it=results;
	if (res_it.hasNext())
	{
	//	System.out.println("ESTTE ES EL CLASS "+res_it.next());
		return true;
	}
	return (res_it.hasNext());
	}

	private boolean _isContainer(Resource resource,Model model)
	{
	boolean is_container=false;
	String query_string = 	"SELECT ?origin WHERE"
		+ "(?origin,<rdf:type>,?destination)"
		+ "AND (?origin eq <"+resource+">)"
		+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
		+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";
	Iterator res_it=_queryRDFModel (query_string,model);
/*	while (res_it.hasNext())
	{
		System.out.println("NO ES UN ALT"+res_it.next());
	}
*/	is_container=res_it.hasNext();

	if (!is_container)
	{
		System.out.println("NO ES UN ALT");
	 	query_string = 	"SELECT ?origin WHERE"
		+ "(?origin,<rdf:type>,<rdfs:Seq>)"
		+ "AND (?origin eq <"+resource+">)"
		+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
		+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";
		res_it=_queryRDFModel (query_string,model);
		is_container=res_it.hasNext();
	}
	if (!is_container)
	{
		query_string = 	"SELECT ?origin WHERE"
		+ "(?origin,<rdf:type>,<rdfs:Bag>)"
		+ "AND (?origin eq <"+resource+">)"
		+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
		+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";
	 	res_it=_queryRDFModel (query_string,model);
		is_container=res_it.hasNext();
	}
	return is_container;

	}
}




