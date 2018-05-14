package es.upm.fi.dia.ontology.webode.translat.owlapi;
import java.util.Enumeration;
import java.util.Iterator;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLDataRange extends OWLCommon
{
	private OWLValuesList values;

	public OWLDataRange ()
	{
	}




	public OWLDataRange (Resource data_range_resource,Model model,OWLNamesContainer data_range_names_container)
	{
	names_container=data_range_names_container;
	String oneof_query_string = 	"SELECT ?destination WHERE"
		+ "(?origin,<owl:oneOf>,?destination)"
		+ "AND (?origin eq <"+data_range_resource+">)"
		+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
		+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
		+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;

		Iterator iter=(Iterator)_queryRDFModel(oneof_query_string,model);
		Resource oneof_resource=null;
		while (iter.hasNext())
		{
			ResultBinding res = (ResultBinding)iter.next();
			oneof_resource = (Resource)res.get("destination");
			System.out.println("Este es values>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			values = new OWLValuesList(oneof_resource,model,names_container);
		}
	}

	public void addValue (OWLDataValue value)
	{
		values.addElement(value);
	}

	public Enumeration getValues()
	{
//		System.out.println("Este es values -Z"+values);
		return (values.getElements());
	}

	public String getType()
	{
		return (names_container.DATA_RANGE);
	}

}
