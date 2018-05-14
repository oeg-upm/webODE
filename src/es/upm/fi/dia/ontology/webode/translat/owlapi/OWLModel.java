package es.upm.fi.dia.ontology.webode.translat.owlapi;

//First the Java related stuff
import java.io.*;
import java.util.*;

import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.mesa.rdf.jena.mem.ModelMem;
import com.hp.hpl.jena.rdf.query.*;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.OWLNamesContainer;

public class OWLModel extends OWLCommon
{
	Model model;
	Hashtable classes_table=null;
	Hashtable restrictions_table=null;
	Hashtable datatype_properties_table=null;
	Hashtable object_properties_table=null;
	Hashtable properties=null;
	Hashtable individuals_table=null;
	Vector annotation_properties;


	String uri;
	String namespace;

	OWLOntology ontology;

	public OWLModel()
	{

	classes_table = new Hashtable();

	individuals_table = new Hashtable();
	datatype_properties_table = new Hashtable ();
	object_properties_table = new Hashtable();
	properties = new Hashtable();
	annotation_properties= new Vector();
	}


	private void _readJenaModel(String uri,String namespace)
	{
	model = new ModelMem();
	try
		{
		 BufferedReader in = new BufferedReader(new FileReader(new File(uri)));


		model.read(in,namespace,"RDF/XML");
	//	model.read("file://"+uri);
		}
	catch (Exception e)
		{
		System.out.println("FATAL ERROR: Something went wrong while creating the RDF Jena model");
		e.printStackTrace();
		}
	}

        private void _readJenaModel(Reader in,String namespace)
        {
        model = new ModelMem();
        try
                {

                model.read(in,namespace,"RDF/XML");
        //	model.read("file://"+uri);
                }
        catch (Exception e)
                {
                System.out.println("FATAL ERROR: Something went wrong while creating the RDF Jena model");
                e.printStackTrace();
                }
        }

	public void read(String ontology_uri,String ontology_namespace)
	{
		uri=ontology_uri;
		namespace=ontology_uri;
		System.out.println("Reading the model");
		names_container= new OWLNamesContainer(ontology_namespace);
		_readJenaModel(ontology_uri,ontology_namespace);
		//showStatements();

		_readAnnotationProperties();


		_readClasses();

		_readObjectProperties();

		_readDatatypeProperties();

		_readIndividuals();
	//	System.out.println("TABLA DE INDIVIDUOSH---> "+individuals_table);
		 ontology=new OWLOntology(model,names_container);


	}

        public void read(Reader in,String ontology_namespace)
        {
//                uri=ontology_uri;
                namespace=ontology_namespace;
                System.out.println("Reading the model");
                names_container= new OWLNamesContainer(ontology_namespace);
                _readJenaModel(in,ontology_namespace);
                //showStatements();

                _readAnnotationProperties();


                _readClasses();

                _readObjectProperties();

                _readDatatypeProperties();

                _readIndividuals();
        //	System.out.println("TABLA DE INDIVIDUOSH---> "+individuals_table);
                 ontology=new OWLOntology(model,names_container);


        }

	public String getOntologyName()
	{
		return uri;
	}


private void _readClasses()
{

	Resource resource;

	String query_string = 	"SELECT ?destination WHERE"
			+ "(?destination,<rdf:type>,<owl:Class>)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;



	Iterator iter=(Iterator)_queryRDFModel(query_string,model);
	while (iter.hasNext())
	{
	ResultBinding res = (ResultBinding)iter.next();
	resource = (Resource)res.get("destination");
//	System.out.println("Esta es una de las clases "+res.get("destination"));




	if (!resource.isAnon())

		{
		//	System.out.println("////////////////////////////////////////////////////////////");
		//	System.out.println("ESTA ES LA CLASE "+resource);
		//	System.out.println("////////////////////////////////////////////////////////////");
		OWLClass new_class = new OWLClass (resource,model,names_container);

		classes_table.put(new_class.getURI(),new_class);

		}
	}
}


private void _readDatatypeProperties()
{

	Resource resource;

	String query_string = 	"SELECT ?destination WHERE"
			+ "(?destination,<rdf:type>,<owl:DatatypeProperty>)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;



	Iterator iter=(Iterator)_queryRDFModel(query_string,model);
	while (iter.hasNext())
	{
	ResultBinding res = (ResultBinding)iter.next();
	resource = (Resource)res.get("destination");
	// System.out.println("Esta es una de las clases "+res.get("destination"));




	if (!resource.isAnon())

		{
		//System.out.println("////////////////////////////////////////////////////////////");
		//System.out.println("ESTA ES UN DATATYPE PROPERTY "+resource);
		//System.out.println("////////////////////////////////////////////////////////////");
		OWLDatatypeProperty new_datatype_property = new OWLDatatypeProperty (resource,model,names_container);
		datatype_properties_table.put(new_datatype_property.getURI(),new_datatype_property);

		}
	}
}


private void _readObjectProperties()
{

	Resource resource;
	String object_properties_classes[]=
	{names_container.OBJECT_PROPERTY,names_container.TRANSITIVE_PROPERTY,
	names_container.SYMMETRIC_PROPERTY,names_container.INVERSE_FUNCTIONAL_PROPERTY};

	int i=0;
	while (i<object_properties_classes.length)
	{

	String query_string = 	"SELECT ?destination WHERE"
			+ "(?destination,<rdf:type>,<"+object_properties_classes[i]+">)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;


	//System.out.println("|||||||||| "+query_string);
	Iterator iter=(Iterator)_queryRDFModel(query_string,model);
	while (iter.hasNext())
	{
	ResultBinding res = (ResultBinding)iter.next();
	resource = (Resource)res.get("destination");
	//	System.out.println("Esta es una de las clases "+res.get("destination"));




	if (!resource.isAnon())

		{
		System.out.println("////////////////////////////////////////////////////////////");
		System.out.println("ESTA ES LA OBJECT PROPERTY "+resource);
		System.out.println("////////////////////////////////////////////////////////////");
		OWLObjectProperty new_object_property = new OWLObjectProperty (resource,model,names_container);

		object_properties_table.put(new_object_property.getURI(),new_object_property);

		}
	else
		System.out.println("????????????????????????????????");
	}
	i++;
	}
}


	private void _readAnnotationProperties()
	{

	annotation_properties.add(names_container.LABEL);
	annotation_properties.add(names_container.COMMENT);
	annotation_properties.add(names_container.VERSION_INFO);
	annotation_properties.add(names_container.IS_DEFINED_BY);
	annotation_properties.add(names_container.SEE_ALSO);

	Resource resource;

	String query_string = 	"SELECT ?origin WHERE"
			+ "(?origin,<rdf:type>,<owl:AnnotationProperty>)"
			+ "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
			+ "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
			+ "owl  FOR <http://www.w3.org/2002/07/owl#>" ;



	Iterator iter=(Iterator)_queryRDFModel(query_string,model);
	while (iter.hasNext())
	{
	ResultBinding res = (ResultBinding)iter.next();
	resource = (Resource)res.get("origin");
	annotation_properties.add(resource.getURI());

	}


	//System.out.println("Esta es la tanbla de annotaciones"+annotation_properties  );


	}





	public Enumeration getClasses()
	{
		return classes_table.elements();
	}

	public Enumeration getIndividuals()
	{
		return individuals_table.elements();
	}

	public Enumeration getDatatypeProperties()
	{
		return datatype_properties_table.elements();
	}

	public Enumeration getObjectProperties()
	{
		return object_properties_table.elements();
	}

	public boolean hasAnnotationProperie(String property_uri)
	{
		return (annotation_properties.indexOf(property_uri)>0);
	}

	public Enumeration getAnnotationProperties()
	{
		return (annotation_properties.elements());
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

	private void _readIndividuals ()
	{
		Enumeration classes_names_enu=classes_table.keys();
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
				OWLIndividual new_individual= new OWLIndividual (origin_resource,model,names_container,individuals_table);
				individuals_table.put(new_individual.getURI(),new_individual);
			}


		}
	}








	public String getType()
	{
		return (names_container.DATATYPE_PROPERTY);
	}

	public	OWLOntology getOntology()
	{
		return ontology;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public String getFileURI()
	{
		return uri;
	}

	public HashSet getAnnotationPropertiesSet()
	{
		Enumeration annotation_enu = annotation_properties.elements();
		HashSet properties_set = new HashSet();
		while (annotation_enu.hasMoreElements())
		{
			Object aux_obj = annotation_enu.nextElement();
			if (!properties_set.contains(aux_obj))
			{
				properties_set.add(aux_obj);
			}
		}
		return properties_set;
	}





}