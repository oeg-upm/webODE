package es.upm.fi.dia.ontology.webode.translat.namescontainers;

public class OWLNamesContainer extends NamesContainer
{
	public final String THING=this.OWL_NAMESPACE+"Thing";
	public final String NOTHING=this.OWL_NAMESPACE+"Nothing";

	public final String SUBCLASS_OF= this.RDFS_NAMESPACE+"subClassOf";
	public final String EQUIVALENT_CLASS=this.OWL_NAMESPACE+"equivalentClass";
	public final String DISJOINT_WITH=this.OWL_NAMESPACE+"disjointWith";
	public final String COMPLEMENT_OF=this.OWL_NAMESPACE+"complementOf";
	public final String INTERSECTION_OF= this.OWL_NAMESPACE+"intersectionOf";
	public final String ONE_OF= this.OWL_NAMESPACE+"oneOf";
	public final String UNION_OF=this.OWL_NAMESPACE+"unionOf";


	public final String DISJOINT_UNION= "DISJOINT_UNION_GROUP_RELATION";

	public final String INSTANCE_OF= "INSTANCE_OF_RELATION";
	public final String ENUMERATION= "OWL_ENUMERATION";
	public final String URI_REFERENCE = "OWL_URI_REFERENCE";
	public final String CLASS  =this.OWL_NAMESPACE+"Class";
	public final String RESTRICTION =this.OWL_NAMESPACE+ "Restriction";
	public final String OPERATOR = "OWL_OPERATOR";
	public final String UNION = "OWL_UNION";
	public final String INTERSECTION = "OWL_INTERSECTION";
	public final String COMPLEMENT = "OWL_COMPLEMENT";
	public final String ONTOLOGY=this.OWL_NAMESPACE+"Ontology";

	//Properties constants
	public final String DATATYPE_PROPERTY=this.OWL_NAMESPACE+"DatatypeProperty";
	public final String OBJECT_PROPERTY=this.OWL_NAMESPACE+"ObjectProperty";
	public final String TRANSITIVE_PROPERTY=this.OWL_NAMESPACE+"TransitiveProperty";
	public final String SYMMETRIC_PROPERTY=this.OWL_NAMESPACE+"SymmetricProperty";
	public final String INVERSE_FUNCTIONAL_PROPERTY=this.OWL_NAMESPACE+"InverseFunctionalProperty";
	public final String FUNCTIONAL_PROPERTY=this.OWL_NAMESPACE+"FunctionalProperty";
	public final String SUB_PROPERTY_OF =this.RDFS_NAMESPACE+"subPropertyOf";
	public final String DOMAIN =this.RDFS_NAMESPACE+"domain";
	public final String RANGE =this.RDFS_NAMESPACE+"range";
	public final String EQUIVALENT_PROPERTY =this.OWL_NAMESPACE+"equivalentProperty";
	public final String INVERSE_OF =this.OWL_NAMESPACE+"inverseOf";
	public final String DATA_RANGE = this.OWL_NAMESPACE+"DataRange";

	public final String INDIVIDUAL="Individual";
	public final String VALUE="Value";
	//Restrictions constants
	public final String ON_PROPERTY=this.OWL_NAMESPACE+"onProperty";
	public final String ALL_VALUES_FROM=this.OWL_NAMESPACE+"allValuesFrom";
	public final String SOME_VALUES_FROM=this.OWL_NAMESPACE+"someValuesFrom";
	public final String HAS_VALUE =this.OWL_NAMESPACE+"hasValue";
	public final String MAX_CARDINALITY=this.OWL_NAMESPACE+"maxCardinality";
	public final String MIN_CARDINALITY=this.OWL_NAMESPACE+"minCardinality";
	public final String CARDINALITY=this.OWL_NAMESPACE+"cardinality";
	public final String IMPORTS=this.OWL_NAMESPACE+"import";
	public final String VERSION_INFO=this.OWL_NAMESPACE+"versionInfo";
	public final String PRIOR_VERSION=this.OWL_NAMESPACE+"priorVersion";
	public final String BACKWARD_COMPATIBLE_WITH=this.OWL_NAMESPACE+"backwardCompatibleWith";
	public final String INCOMPATIBLE_WITH=this.OWL_NAMESPACE+"incompatibleWith";
	public final String LABEL=this.RDFS_NAMESPACE+"label";
	public final String COMMENT=this.RDFS_NAMESPACE+"comment";
	public final String IS_DEFINED_BY=this.RDFS_NAMESPACE+"isDefinedBy";
	public final String SEE_ALSO=this.RDFS_NAMESPACE+"seeAlso";


//	public final String IMPORTS=owl_namespace+"import";


//	public final String UNION_OF=owl_namespace+"unionOf";
//	public static final String EXHAUSTIVE_SUBCLASS_PARTITION="exhaustive-subclass-partition";
	public OWLNamesContainer (String ontology_namespace)
	{

		super(ontology_namespace);
		/*
		System.out.println("Creating the OWL namescontainer ");
		System.out.println("    URI: "+ontology_uri);
		System.out.println("	Namespaces: "+ontology_namespace);
		*/
	//	elements_names_table.add("Class","RDF_CLASS_");
		types_names_table.put(CLASS,"OWL_ANONYM_CLASS_");
		elements_numbers_table.put(CLASS,new Integer(0));
		types_names_table.put(RESTRICTION,"OWL_RESTRICTION_");
		elements_numbers_table.put(RESTRICTION,new Integer(0));
		types_names_table.put(INDIVIDUAL,"OWL_INDIVIDUAL_");
		elements_numbers_table.put(INDIVIDUAL,new Integer(0));
	}

	public OWLNamesContainer ()
	{
		super(null);
	//	elements_names_table.add("Class","RDF_CLASS_");

	/*
		types_names_table.put("Class","OWL_ANONYM_CLASS_");
		elements_numbers_table.put("Class",new Integer(0));
		types_names_table.put("Restriction","OWL_CLASS_");
		elements_numbers_table.put("Restriction",new Integer(0));
	*/

	}


}
