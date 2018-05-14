package es.upm.fi.dia.ontology.webode.translat.owlapi;

//First the Java related stuff
import java.util.*;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLDatatypeProperty extends OWLProperty
{


	public OWLDatatypeProperty  (Resource property_resource,Model model,OWLNamesContainer property_names_container)
	{
	super(property_resource,model,property_names_container);
	OWLClassDescriptionReader class_description_reader=null;
	OWLClassDescription destination=null;
	local_name=property_resource.getLocalName();
	uri = property_resource.getURI();
	names_container=property_names_container;
	names_container.bind(property_resource,uri);




	String properties_query_string =
			"SELECT ?destination WHERE"
			+ "(?origin,?relation,?destination)"
			+ "AND (?origin eq <"+property_resource+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;


	Iterator properties_it=_queryRDFModel (properties_query_string, model);

	OWLClassDescription class_description;
	while (properties_it.hasNext())
	{
		ResultBinding property_res= (ResultBinding)properties_it.next();
		Object destination_object = property_res.get("destination");
		Resource relation_resource= (Resource)property_res.get("relation");
		if (destination_object instanceof Literal)
		{
			addRangeElement(relation_resource.getURI(),new OWLDataValue((Literal)destination_object,names_container));
		}
		else
		{

			if (!_isDataRange((Resource)destination_object,model))
			{
				if (class_description_reader==null)
				{
				class_description_reader=
				new	OWLClassDescriptionReader(model,names_container);
				}


			class_description=class_description_reader.read((Resource)destination_object);
			System.out.println("-->>>"+class_description);
			addRangeElement(relation_resource.getURI(),class_description);
			}
			else
			{
			OWLDataRange data_range = new OWLDataRange((Resource)destination_object,model,names_container);
			addRangeElement(relation_resource.getURI(),data_range);
			}
		}
	}

//	System.out.println("---PROPERTY:PROPERTIES------------> "+property_properties_table);
}




	public String getType()
	{
		return (names_container.DATATYPE_PROPERTY);
	}


}
