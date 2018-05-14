package es.upm.fi.dia.ontology.webode.translat.owlapi;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Hashtable;

import com.hp.hpl.mesa.rdf.jena.model.Resource;
import com.hp.hpl.mesa.rdf.jena.model.Model;
import com.hp.hpl.mesa.rdf.jena.model.Literal;

import com.hp.hpl.jena.rdf.query.ResultBinding;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLIndividual extends OWLNamedElement
{
	Hashtable individual_properties_table;

	OWLIndividual (Resource individual_resource, Model model,OWLNamesContainer individual_names_container, Hashtable individuals_table)
	{
		individual_properties_table= new Hashtable();
		System.out.println(".-.-.-.-.-.-.>>"+individual_resource);
		names_container=individual_names_container;
		if (!individual_resource.isAnon())
		{
		local_name=individual_resource.getLocalName();
		uri = individual_resource.getURI();
		names_container.bind(individual_resource,uri);

		}
		else
		{
			if (!names_container.isBinded(individual_resource))
				{
					local_name=names_container.bindAnonymous(individual_resource,names_container.INDIVIDUAL);

				}
			else
			{
			local_name = names_container.lookUp(individual_resource);

			}
			uri = names_container.namespace+local_name;
	//	names_container.bind(individual_resource,uri);
		}
		namespace=names_container.namespace;



		String query_string =
				"SELECT ?destination,?relation WHERE"
				+ "(?origin,?relation,?destination)"
				+ "AND (?origin eq <"+individual_resource+">)"
				+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
				+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
				+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;


		Iterator properties_it=_queryRDFModel (query_string,model);


		while (properties_it.hasNext())
		{
			ResultBinding res= (ResultBinding)properties_it.next();
			Object destination = res.get("destination");
			Resource relation_resource= (Resource)res.get("relation");

			if (destination instanceof Resource)
			{
				Resource destination_resource=(Resource)destination;
			//	System.out.println ("AQUIS "+destination_resource);
				System.out.println(destination_resource.getURI());
				if (!destination_resource.isAnon())
				{
					addProperty(relation_resource.getURI(),(new OWLURIReference(destination_resource,names_container)));
				}
				else
				{

					if (!names_container.isBinded(destination_resource))
					{
						OWLIndividual anon_individual=new OWLIndividual(destination_resource,model,names_container,individuals_table);
				//	individuals_table.put(anon_individual.getURI(),anon_individual);
				//	OWLURIReference owl_uri_reference= new OWLURIReference ();
				//	owl_uri_reference.setURI(anon_individual.getURI());
					System.out.println("SE CREA"+anon_individual.getURI());
					addProperty(relation_resource.getURI(),new OWLURIReference(anon_individual.getNamespace(),anon_individual.getLocalName()));
					}
					else
					{
						String destination_name = names_container.lookUp(destination_resource);

						addProperty(relation_resource.getURI(),new OWLURIReference(names_container.namespace,destination_name));
					}
				}

			}
			else
			{
				if (destination instanceof Literal)
				{
					addProperty(relation_resource.getURI(),new OWLDataValue((Literal)destination,names_container));
				}
			}


		}

	}


        public OWLIndividual (Resource individual_resource, Model model,OWLNamesContainer individual_names_container)
        {
                individual_properties_table= new Hashtable();
                System.out.println(".-.-.-.-.-.-.>>"+individual_resource);
                names_container=individual_names_container;
                if (!individual_resource.isAnon())
                {
                local_name=individual_resource.getLocalName();
                uri = individual_resource.getURI();
                names_container.bind(individual_resource,uri);

                }
                else
                {
                        if (!names_container.isBinded(individual_resource))
                                {
                                        local_name=names_container.bindAnonymous(individual_resource,names_container.INDIVIDUAL);

                                }
                        else
                        {
                        local_name = names_container.lookUp(individual_resource);

                        }
                        uri = names_container.namespace+local_name;
        //	names_container.bind(individual_resource,uri);
                }
                namespace=names_container.namespace;



                String query_string =
                                "SELECT ?destination,?relation WHERE"
                                + "(?origin,?relation,?destination)"
                                + "AND (?origin eq <"+individual_resource+">)"
                                + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
                                + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
                                + "owl  FOR <http://www.w3.org/2002/07/owl#>" ;


                Iterator properties_it=_queryRDFModel (query_string,model);


                while (properties_it.hasNext())
                {
                        ResultBinding res= (ResultBinding)properties_it.next();
                        Object destination = res.get("destination");
                        Resource relation_resource= (Resource)res.get("relation");

                        if (destination instanceof Resource)
                        {
                                Resource destination_resource=(Resource)destination;
                        //	System.out.println ("AQUIS "+destination_resource);
                                System.out.println(destination_resource.getURI());
                                if (!destination_resource.isAnon())
                                {
                                        addProperty(relation_resource.getURI(),(new OWLURIReference(destination_resource,names_container)));
                                }
                                else
                                {

                                        if (!names_container.isBinded(destination_resource))
                                        {
                                                OWLIndividual anon_individual=new OWLIndividual(destination_resource,model,names_container);
                                //	individuals_table.put(anon_individual.getURI(),anon_individual);
                                //	OWLURIReference owl_uri_reference= new OWLURIReference ();
                                //	owl_uri_reference.setURI(anon_individual.getURI());
                                        System.out.println("SE CREA"+anon_individual.getURI());
                                        addProperty(relation_resource.getURI(),new OWLURIReference(anon_individual.getNamespace(),anon_individual.getLocalName()));
                                        }
                                        else
                                        {
                                                String destination_name = names_container.lookUp(destination_resource);

                                                addProperty(relation_resource.getURI(),new OWLURIReference(names_container.namespace,destination_name));
                                        }
                                }

                        }
                        else
                        {
                                if (destination instanceof Literal)
                                {
                                        addProperty(relation_resource.getURI(),new OWLDataValue((Literal)destination,names_container));
                                }
                        }


                }

        }


        public Enumeration getPropertyNames()
	{
		return (individual_properties_table.keys());
	}




	public Enumeration getParents()
	{
		return (getProperty(names_container.TYPE));
	}

	public OWLURIReference getParent()
	{
		return ((OWLURIReference)getProperty(names_container.TYPE));
	}


	public String getType()
	{
		return (names_container.INDIVIDUAL);
	}




	public Enumeration getProperty (String property_name)
	{

		Vector related_properties=(Vector)individual_properties_table.get(property_name);
		if (related_properties!=null)
		{
			return related_properties.elements();
		}
		else
		{
			return ((new Vector()).elements());
		}

	}






	public void addProperty (String relation_name, Object destination)//OWLCommon destination
	{
//		System.out.println("----------------------------------> "+relation_name+"->"+class_description);
//		System.out.println("----------------------------------> "+class_descriptions_table);
		Vector related_destinations=(Vector)individual_properties_table.get(relation_name);
		if (related_destinations==null)
		{
			related_destinations = new Vector();
			individual_properties_table.put(relation_name,related_destinations);
		}
		related_destinations.add(destination);


	}


	public OWLCommon getFirstOfProperty (String property_name)
	{
		Vector related_properties=(Vector)individual_properties_table.get(property_name);
		if (related_properties!=null)
		{
			return ((OWLCommon)related_properties.get(0));
		}
		return null;

	}


}