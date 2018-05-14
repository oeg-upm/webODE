package es.upm.fi.dia.ontology.webode.translat.rdfsapi;

//First the Java related stuff
import com.hp.hpl.jena.rdf.query.*;
import com.hp.hpl.mesa.rdf.jena.mem.*;
import com.hp.hpl.mesa.rdf.jena.model.*;
import es.upm.fi.dia.ontology.webode.translat.namescontainers.*;
import java.io.*;
import java.util.*;


public class RDFSModel extends RDFSElement
{
  Model model;
  Hashtable model_classes_table=null;
  Hashtable model_properties_table=null;
  Hashtable model_instances_table=null;
  Hashtable model_containers_table=null;
  Hashtable model_reified_table=null;
  String uri;
  RDFSNamesContainer names_container;

  public RDFSModel()
  {
    model_classes_table = new Hashtable();
    model_properties_table = new Hashtable ();
    model_instances_table = new Hashtable();
  }


  private void _readJenaModel(Reader input,String namespace)
  {
    model = new ModelMem();
    try
    {



      model.read(input,namespace,"RDF/XML");
      //	model.read("file://"+uri);
    }
    catch (Exception e)
    {
      System.out.println("FATAL ERROR: Something went wrong while creating the RDF Jena model");
      e.printStackTrace();
    }
  }

  private void _readClasses()
  {
//	System.out.println ("HOLA?");

    String query_string = 	"SELECT ?destination WHERE"
			+ "(?destination,<rdf:type>,<rdfs:Class>)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";
    Iterator iter=(Iterator)_queryRDFModel(query_string,model);
    while (iter.hasNext())
    {
      ResultBinding res = (ResultBinding)iter.next();

      Resource resource = (Resource)res.get("destination");

      if (!resource.isAnon())
      {
	RDFSClass new_class = new RDFSClass(resource,model,names_container,model_classes_table);
	model_classes_table.put(new_class.getURI(),new_class);
      }
    }
  }













  private void _readProperties()
  {
    RDFSProperty rdfs_property=null;
//	showStatements();
    String query_string =
	"SELECT ?destination WHERE"
	+"(?origin,<rdf:type>,<rdf:Property>)"
	+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
	+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";

    Iterator iter=(Iterator)_queryRDFModel(query_string,model);
    while (iter.hasNext())
    {
      ResultBinding res = (ResultBinding)iter.next();

      //System.out.println(obj);
      Resource property_res = (Resource)res.get("origin");
      //System.out.println(property_res);


      RDFSProperty new_property = new RDFSProperty(property_res,model,names_container);

      model_properties_table.put(new_property.getURI(),new_property);
    }

  }

  private String _resolveName(Object object)
  {
    String object_name=null;
    if (names_container.isBinded(object))
      object_name=names_container.lookUp(object);
    else
    {
      if (object instanceof Resource)
      {
	object_name=((Resource)object).getURI();
	names_container.bind(object,object_name);
      }
      else
	object_name=names_container.bindAnonymous(object,"Unknown");
    }
    return object_name;
  }




  private void _readDefinedValues(Resource property,RDFSProperty rdfs_property)
  {
    //System.out.println("HOLA!!!!!!!!!!!!");

    String query_string =
	"SELECT ?x WHERE"
	+"(?x,<"+rdfs_property.getURI()+">,?y)"
	+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
	+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";
    //System.out.println("::::: "+query_string);
    Query query = new Query(query_string);
 /*
 if (model==null)
  System.out.println("MODELO NULO ");
 */
    query.setSource(model);
    QueryEngine qe= new QueryEngine(query);
    Iterator results_it= qe.exec();
    while (results_it.hasNext())
    {
      ResultBinding res = (ResultBinding)results_it.next();

//			rdfs_property.addValue(res.get("x").toString(),res.get("y").toString());


    }



  }



  private void _readInstances ()
  {
    Enumeration classes_names_enu=model_classes_table.keys();
    while (classes_names_enu.hasMoreElements())
    {
      String parent_class_name = (String)classes_names_enu.nextElement();
      String query_string = 	"SELECT ?origin WHERE"
			  + "(?origin,<rdf:type>,?destination)"
			  + "AND (?destination eq <"+parent_class_name+">)"
			  + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			  + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			  + "owl  FOR <http://www.w3.org/2002/07/owl#>";
      Query query = new Query(query_string);
      query.setSource(model);
      QueryExecution qe= new QueryEngine(query);
      QueryResults results = qe.exec();
      for(Iterator iter=results; iter.hasNext(); )
      {
	ResultBinding res = (ResultBinding)iter.next();
	Resource origin_resource =(Resource)res.get("origin");
	RDFSInstance new_instance= new RDFSInstance (origin_resource,model,names_container,model_instances_table);
	model_instances_table.put(new_instance.getURI(),new_instance);
      }


    }
  }




















  public void read(Reader input,String namespace)
  {

    names_container= new RDFSNamesContainer(namespace);
    try
    {
      _readJenaModel(input,namespace);
      _readClasses();
      _readInstances();

      _readProperties();

      //System.out.println("CLASSES TABLE: "+model_classes_table);
      //System.out.println("PROPS TABLE: "+model_properties_table);
      System.out.println("INSTANCES TABLE: "+model_instances_table);

    }
    catch (Exception e)
    {
      System.out.println("Algo peto pero que bien");
      e.printStackTrace();
    }
  }





  public Enumeration getClasses()
  {
    return model_classes_table.elements();
  }

  public Enumeration getInstances()
  {
    return model_instances_table.elements();
  }

  public Enumeration getProperties ()
  {

    return model_properties_table.elements();
  }


  public String getOntologyName()
  {
    return	names_container.getOntologyName();
  }

  public String getOntologyNamespace()
  {
    return names_container.getOntologyNamespace();
  }

  public void showStatements ()
  {
    //This method shows all the rdf statements that conform the ontology

    System.out.println("Showing statements");
    String query_string =
	"SELECT ?x,?y,?z WHERE (?x,?y,?z)"
	+ "USING daml FOR <http://www.daml.org/2001/03/daml+oil#>,"
	+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";

    Query query = new Query(query_string);
    query.setSource(model);
    QueryExecution qe= new QueryEngine(query);
    QueryResults results = qe.exec();
    for(Iterator iter=results; iter.hasNext(); )
    {
      ResultBinding res = (ResultBinding)iter.next();
      System.out.println("----------------------------------------");

      System.out.println(res.get("x"));
      System.out.println(res.get("y"));
      System.out.println(res.get("z"));
      System.out.println("----------------------------------------");

    }


  }

/*
"SELECT ?destination WHERE"
    + "(?origin,?relation,?destination)"
    + "AND (?origin eq <"+resource+">)"
    + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
    + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
    + "owl  FOR <http://www.w3.org/2002/07/owl#>" ;

*/



  public boolean guessIfObjectProperty (RDFSProperty property)
  {
    String query_string=null;
    boolean is_object=false;
    query_string =
	"SELECT ?destination WHERE (<"+property.getURI()+">,<rdfs:range>,?destination)"
	+ "AND (?destination eq <"+names_container.RDFS_NAMESPACE+"Class>)"
	+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
	+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
	+ "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
    is_object=_existStatement(query_string);
    if (!is_object)
    {
      query_string =
	  "SELECT ?destination WHERE (<"+property.getURI()+">,<rdfs:range>,?destination),"
	  + "(?destination,<rdf:type>,<rdfs:Class>)"
	  + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
	  + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
	  + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
      is_object=_existStatement(query_string);
    }
    if (!is_object)
    {
      query_string =
	  "SELECT ?destination WHERE (<"+property.getURI()+">,<rdfs:range>,?destination)"
	  + "AND (?destination eq <"+names_container.RDF_NAMESPACE+"Property>)"
	  + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
	  + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
	  + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
      is_object=_existStatement(query_string);
    }
    return is_object;
  }






 /*
 String relation_from_property[] = {"<rdfs:range>"};
 String relation_from_restriction[]={"<daml:toClass>","<daml:hasClass>","<daml:hasClassQ>"};
 String prop_relations[]={"<rdfs:subPropertyOf>","<daml:samePropertyAs>","<daml:equivalentTo>","<daml:inverseOf>"};
 String type_class[] = {"<daml:Class>","<rdfs:Class>"};
 String query_string=null;
 boolean isObject=false;
 int i=0;
 while((i<relation_from_property.length)&&(!isObject))
  {
  int class_index=0;
  while ((class_index<type_class.length)&&(!isObject))
   {
   query_string =
   "SELECT ?destination WHERE (<"+property_name+">,"+relation_from_property[i]+",?destination),"
   + "(?destination,<rdf:type>,"+type_class[class_index]+")"
   + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
   + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
   + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
   isObject=_existStatement(query_string);
   class_index++;
   }
  i++;
  }

  if (!isObject)
   {
   i=0;
   while((i<relation_from_restriction.length)&&(!isObject))
    {
    int class_index=0;
    while ((class_index<type_class.length)&&(!isObject))
     {
     query_string =
     "SELECT ?destination WHERE"
     + "(?damlclass,<rdf:type>,"+type_class[class_index]+"),"
     + "(?restriction,"+relation_from_restriction[i]+",?damlclass),"
     + "(?restriction,<daml:onProperty>,<"+property_name+">)"
     + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
     + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
     + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
     isObject=_existStatement(query_string);
     class_index++;
     }
    i++;
    }


   }




   if (!isObject)
   {
  //	System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO "+property_name);
   int j=0;
   while((j<prop_relations.length)&&(!isObject))
    {
    query_string =
    "SELECT ?destination WHERE (<"+property_name+">,"+prop_relations[j]+",?destination),"
    + "(?destination,<rdf:type>,<daml:ObjectProperty>)"
    + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
    + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
    + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
    isObject=_existStatement(query_string);
    j++;

    }
    if (!isObject)
    {
    int k=0;
    while((k<prop_relations.length)&&(!isObject))
     {
     query_string =
     "SELECT ?origin WHERE (?origin,"+prop_relations[k]+",<"+property_name+">),"
     + "(?origin,<rdf:type>,<daml:ObjectProperty>)"
     + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
     + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
     + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
     isObject=_existStatement(query_string);
     k++;
     }
    }
   }


  if (isObject)
  {
  //	System.out.println("SSIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII "+property_name);
   System.out.println("The query string "+query_string);

  }

  return isObject;

*/




  public boolean guessIfDatatypeProperty (RDFSProperty property)
  {
    String range_name=null;
    String range_namespace=null;
    Enumeration range_enu=null;
    boolean is_datatype=false;
    range_enu = property.getProperty(names_container.RANGE);

    if (range_enu.hasMoreElements())
    {
      RDFSNamedElement rdfs_element= ((RDFSNamedElement)range_enu.nextElement());
      range_name=rdfs_element.getURI();
      range_namespace=rdfs_element.getNamespace();
//      System.out.println(">>"+range_name+"---> "+range_namespace+" =? "+names_container.xsd_namespace);

    }

    if (range_name!=null)
    {
      is_datatype=(range_name.equals(names_container.RDFS_NAMESPACE+"Literal"))||
		  (names_container.isXsdNamespace(range_namespace));
    }

    if (!is_datatype)

    {
      String query_string =
	  "SELECT ?property WHERE (?origin,?property,?destination),"
	  + "(?destination,<rdf:type>,<rdfs:Literal>)"
	  + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
	  + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
	  + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
      is_datatype=_existStatement(query_string);

    }


    if (!is_datatype)
    {
      String query_string =
	  "SELECT ?destination WHERE (?origin,?property,?destination)"
	  + "AND (?property eq <"+property.getURI()+">)"
	  + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
	  + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
	  + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";

      Iterator properties_it=_queryRDFModel (query_string,model);


      while (properties_it.hasNext()&&!is_datatype)
      {
	ResultBinding res= (ResultBinding)properties_it.next();
	Object destination = res.get("destination");
	is_datatype=(destination instanceof Literal);

      }
    }
    return is_datatype;
  }
















  //System.out.println("ENTRO EN EL OBJECT GUESSING"+property_name);
 /*
 String relation_from_property[] = {"<rdfs:range>"};
 String relation_from_restriction[]={"<daml:toClass>","<daml:hasClass>","<daml:hasClassQ>"};
 String prop_relations[]={"<rdfs:subPropertyOf>","<daml:samePropertyAs>","<daml:equivalentTo>","<daml:inverseOf>"};
 String type_class[] = {"<daml:Class>","<rdfs:Class>"};
 String query_string=null;
 boolean isObject=false;
 int i=0;
 while((i<relation_from_property.length)&&(!isObject))
  {
  int class_index=0;
  while ((class_index<type_class.length)&&(!isObject))
   {
   query_string =
   "SELECT ?destination WHERE (<"+property_name+">,"+relation_from_property[i]+",?destination),"
   + "(?destination,<rdf:type>,"+type_class[class_index]+")"
   + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
   + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
   + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
   isObject=_existStatement(query_string);
   class_index++;
   }
  i++;
  }

  if (!isObject)
   {
   i=0;
   while((i<relation_from_restriction.length)&&(!isObject))
    {
    int class_index=0;
    while ((class_index<type_class.length)&&(!isObject))
     {
     query_string =
     "SELECT ?destination WHERE"
     + "(?damlclass,<rdf:type>,"+type_class[class_index]+"),"
     + "(?restriction,"+relation_from_restriction[i]+",?damlclass),"
     + "(?restriction,<daml:onProperty>,<"+property_name+">)"
     + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
     + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
     + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
     isObject=_existStatement(query_string);
     class_index++;
     }
    i++;
    }


   }




   if (!isObject)
   {
  //	System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO "+property_name);
   int j=0;
   while((j<prop_relations.length)&&(!isObject))
    {
    query_string =
    "SELECT ?destination WHERE (<"+property_name+">,"+prop_relations[j]+",?destination),"
    + "(?destination,<rdf:type>,<daml:ObjectProperty>)"
    + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
    + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
    + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
    isObject=_existStatement(query_string);
    j++;

    }
    if (!isObject)
    {
    int k=0;
    while((k<prop_relations.length)&&(!isObject))
     {
     query_string =
     "SELECT ?origin WHERE (?origin,"+prop_relations[k]+",<"+property_name+">),"
     + "(?origin,<rdf:type>,<daml:ObjectProperty>)"
     + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
     + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
     + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
     isObject=_existStatement(query_string);
     k++;
     }
    }
   }


  if (isObject)
  {
  //	System.out.println("SSIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII "+property_name);
   System.out.println("The query string "+query_string);

  }

 */



  private boolean _existStatement(String query_string)
  {
    Query query = new Query(query_string);
    query.setSource(model);
    QueryExecution qe= new QueryEngine(query);
    QueryResults results = qe.exec();
    Iterator iter=results;
/*	System.out.println("////////////////////////////////////////////");
 while (iter.hasNext())
 {
 ResultBinding res = (ResultBinding)iter.next()
 Resource resource = (Resource)res.get("destination");
 Resource re
 System.out.println("---------->>>>>>>>>>>>>>>>>>>>>>>"+resource.getURI());
 }
 */
    return (iter.hasNext());
  }





  public boolean hasClass (String class_uri)
  {
    return (model_classes_table.containsKey(class_uri));

  }

  public boolean hasInstance (String instance_uri)
  {

    return (model_instances_table.containsKey(instance_uri));
  }


  public boolean hasProperty (String property_uri)
  {
    return (model_properties_table.containsKey(property_uri));
  }


}