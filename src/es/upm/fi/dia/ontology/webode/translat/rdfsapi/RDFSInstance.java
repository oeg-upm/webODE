package es.upm.fi.dia.ontology.webode.translat.rdfsapi;
import com.hp.hpl.jena.rdf.query.*;
import com.hp.hpl.mesa.rdf.jena.model.*;
import es.upm.fi.dia.ontology.webode.translat.namescontainers.*;
import java.util.*;

public class RDFSInstance extends RDFSNamedElement
{
  RDFSInstance (Resource instance_resource, Model model,RDFSNamesContainer instance_names_container, Hashtable instances_table)
  {
    super();
    names_container=instance_names_container;
    if (!instance_resource.isAnon())
    {
      local_name=instance_resource.getLocalName();
      uri = instance_resource.getURI();
      namespace=instance_resource.getNameSpace();
      names_container.bind(instance_resource,uri);
    }
    else
    {
      if (!names_container.isBinded(instance_resource))
      {
	local_name=names_container.bindAnonymous(instance_resource,names_container.INSTANCE);
      }
      else
      {
	local_name = names_container.lookUp(instance_resource);
      }
      uri = names_container.namespace+local_name;
    }
    namespace=names_container.namespace;

    String query_string =
	"SELECT ?destination,?relation WHERE"
	+ "(?origin,?relation,?destination)"
	+ "AND (?origin eq <"+instance_resource+">)"
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
	if (!destination_resource.isAnon())
	{
	  addProperty(relation_resource.getURI(),(new RDFSURIReference(destination_resource,names_container)));
	}
	else
	{
	  if (!_isList((Resource)destination,model))
	  {
	    if (!names_container.isBinded(destination_resource))
	    {
	      RDFSInstance anon_instance=new RDFSInstance(destination_resource,model,names_container,instances_table);
	      //	s_table.put(anon_individual.getURI(),anon_individual);
	      //	OWLURIReference owl_uri_reference= new OWLURIReference ();
	      //	owl_uri_reference.setURI(anon_individual.getURI());
	      //		System.out.println("SE CREA"+anon_individual.getURI());
	      addProperty(relation_resource.getURI(),new RDFSURIReference(anon_instance.getNamespace(),anon_instance.getLocalName()));
	    }
	    else
	    {
	      String destination_name = names_container.lookUp(destination_resource);

	      addProperty(relation_resource.getURI(),new RDFSURIReference(names_container.namespace,destination_name));
	    }
	  }
	}

      }
      else
      {
	if (destination instanceof Literal)
	{
	  addProperty(relation_resource.getURI(),new RDFSDataValue((Literal)destination));
	}
      }
    }
  }


  RDFSInstance (Resource instance_resource, Model model,RDFSNamesContainer instance_names_container)
  {
    super();
    //System.out.println(".-.-.-.-.-.-.>>"+individual_resource);
    names_container=instance_names_container;
    if (!instance_resource.isAnon())
    {
      local_name=instance_resource.getLocalName();
      uri = instance_resource.getURI();
      names_container.bind(instance_resource,uri);
    }
    else
    {
      if (!names_container.isBinded(instance_resource))
      {
	local_name=names_container.bindAnonymous(instance_resource,names_container.INSTANCE);
      }
      else
      {
	local_name = names_container.lookUp(instance_resource);
      }
      uri = names_container.namespace+local_name;
    }
    namespace=names_container.namespace;

    String query_string =
	"SELECT ?destination,?relation WHERE"
	+ "(?origin,?relation,?destination)"
	+ "AND (?origin eq <"+instance_resource+">)"
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
	if (!destination_resource.isAnon())
	{
	  addProperty(relation_resource.getURI(),(new RDFSURIReference(destination_resource,names_container)));
	}
	else
	{
	  if (!_isList((Resource)destination,model))
	  {
	    System.out.println("NO SE HA DETECTADO UNA LISTA"+(Resource)destination);
	    if (!names_container.isBinded(destination_resource))
	    {
	      RDFSInstance anon_instance=new RDFSInstance(destination_resource,model,names_container);
	      //	individuals_table.put(anon_individual.getURI(),anon_individual);
	      //	OWLURIReference owl_uri_reference= new OWLURIReference ();
	      //	owl_uri_reference.setURI(anon_individual.getURI());
	      //		System.out.println("SE CREA"+anon_individual.getURI());
	      addProperty(relation_resource.getURI(),new RDFSURIReference(anon_instance.getNamespace(),anon_instance.getLocalName()));
	    }
	    else
	    {
	      String destination_name = names_container.lookUp(destination_resource);
	      addProperty(relation_resource.getURI(),new RDFSURIReference(names_container.namespace,destination_name));
	    }
	  }
	  else
	  {
	    System.out.println("SE HA DETECTADO UNA LISTA"+(Resource)destination);
	  }
	}

      }
      else
      {
	if (destination instanceof Literal)
	{
	  addProperty(relation_resource.getURI(),new RDFSDataValue((Literal)destination));
	}
      }


    }

  }




  public Enumeration getParents()
  {
    return (getProperty(names_container.TYPE));
  }

  public RDFSURIReference getParent()
  {
    return ((RDFSURIReference)getProperty(names_container.TYPE));
  }


  public String getType()
  {
    return (names_container.INSTANCE);
  }

  private boolean _isList(Resource resource,Model model)
  {
    String query_string = 	"SELECT ?origin WHERE"
			+ "(?origin,<rdf:type>,<rdf:List>)"
			+ "AND (?origin eq <"+resource+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";

    Iterator res_it=_queryRDFModel (query_string,model);
    if (res_it.hasNext())
    {
      //System.out.println("ESTTE ES EL CLASS "+res_it.next());
      return true;
    }
    return (res_it.hasNext());
  }

}