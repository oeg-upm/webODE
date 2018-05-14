package es.upm.fi.dia.ontology.webode.translat.rdfsapi;

//First the Java related stuff is imported
import java.util.*;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.RDFSNamesContainer;

public class RDFSProperty extends RDFSNamedElement
{


	public RDFSProperty (Resource property_resource,Model model,RDFSNamesContainer names_container )
	{
	super();
	try
	{
	//	System.out.println (">>"+property_resource);
		local_name=property_resource.getLocalName();
			uri = property_resource.getURI();
			namespace=property_resource.getNameSpace();
			names_container.bind(property_resource,uri);//Esto vale pa algo?
		;
	String properties_query_string =
			"SELECT ?destination WHERE"
			+ "(?origin,?predicate,?destination)"
			+ "AND (?origin eq <"+property_resource+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;


	Iterator properties_it=_queryRDFModel (properties_query_string, model);

;
	while (properties_it.hasNext())
	{
		ResultBinding property_res= (ResultBinding)properties_it.next();
		Resource predicate_resource = (Resource)property_res.get("predicate");

		Object aux_obj = property_res.get("destination");
		Resource destination_resource=null;
		if (aux_obj instanceof Resource)
		{
			destination_resource = (Resource)aux_obj;
			addProperty(predicate_resource.getURI(),(new RDFSURIReference(destination_resource,names_container)));


		}
		else
		{


			if (aux_obj instanceof Literal)
			{
				//	System.out.println ("ES UN _ "+predicate_resource.getURI());
				RDFSDataValue destination=new RDFSDataValue ((Literal)aux_obj);

				addProperty(predicate_resource.getURI(),(RDFSDataValue)destination);
			}
		}



	}

	}

	catch (Exception e)
	{
	e.printStackTrace();
	}






	}







	public String toString()
	{
		return ("RDFSProperty:["+uri+"]");
	}
}
