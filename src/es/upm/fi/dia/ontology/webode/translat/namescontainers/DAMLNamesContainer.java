package es.upm.fi.dia.ontology.webode.translat.namescontainers;

public class DAMLNamesContainer extends NamesContainer
{
	public final String THING=this.OWL_NAMESPACE+"Thing";
	public final String NOTHING=this.OWL_NAMESPACE+"Nothing";

	public final String SUBCLASS_OF= this.RDFS_NAMESPACE+"subClassOf";
	public final String EQUIVALENT_TO=this.DAMLOIL_NAMESPACE+"equivalentTo";
	public final String SAME_CLASS_AS=this.DAMLOIL_NAMESPACE+"sameClassAs";
	public final String DISJOINT_WITH=this.DAMLOIL_NAMESPACE+"disjointWith";
	public final String DISJOINT_UNION_OF=this.DAMLOIL_NAMESPACE+"disjointUnionOf";
	public final String COMPLEMENT_OF=this.DAMLOIL_NAMESPACE+"complementOf";
	public final String INTERSECTION_OF= this.DAMLOIL_NAMESPACE+"intersectionOf";
	public final String ONE_OF= this.DAMLOIL_NAMESPACE+"oneOf";
	public final String UNION_OF=this.DAMLOIL_NAMESPACE+"unionOf";


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
	public final String DATATYPE_PROPERTY=this.DAMLOIL_NAMESPACE+"DatatypeProperty";
	public final String OBJECT_PROPERTY=this.DAMLOIL_NAMESPACE+"ObjectProperty";
	public final String TRANSITIVE_PROPERTY=this.DAMLOIL_NAMESPACE+"TransitiveProperty";
	public final String SYMMETRIC_PROPERTY=this.DAMLOIL_NAMESPACE+"SymmetricProperty";
	public final String INVERSE_FUNCTIONAL_PROPERTY=this.DAMLOIL_NAMESPACE+"InverseFunctionalProperty";
	public final String FUNCTIONAL_PROPERTY=this.DAMLOIL_NAMESPACE+"FunctionalProperty";
	public final String SUB_PROPERTY_OF =this.RDFS_NAMESPACE+"subPropertyOf";
	public final String DOMAIN =this.RDFS_NAMESPACE+"domain";
	public final String RANGE =this.RDFS_NAMESPACE+"range";
//	public final String EQUIVALENT_TO=daml_namespace+"equivalentTo";
	public final String SAME_PROPERTY_AS=this.DAMLOIL_NAMESPACE+"samePropertyAs";
	public final String INVERSE_OF =this.DAMLOIL_NAMESPACE+"inverseOf";

	public final String LABEL=this.RDFS_NAMESPACE+"label";
	public final String COMMENT=this.RDFS_NAMESPACE+"comment";
	public final String IS_DEFINED_BY=this.RDFS_NAMESPACE+"isDefinedBy";
	public final String SEE_ALSO=this.RDFS_NAMESPACE+"seeAlso";




/*
	public static final String COMPLEMENT_OF="http://www.daml.org/2001/03/daml+oil#complementOf";
	public static final String EQUIVALENT_TO="http://www.daml.org/2001/03/daml+oil#equivalentTo";
	public static final String INTERSECTION_OF="http://www.daml.org/2001/03/daml+oil#intersectionOf";
	public static final String SAME_CLASS_AS= "http://www.daml.org/2001/03/daml+oil#sameClassAs";
*/
	public static final String N_CARDINALITY="-1";
/*
	public static final String DISJOINT_WITH= "DISJOINT_WITH_GROUP_RELATION";
	public static final String DISJOINT_UNION= "DISJOINT_UNION_GROUP_RELATION";
	public static final String UNION_OF= "UNION_OF_GROUP_RELATION";
	public static final String INSTANCE_OF= "INSTANCE_OF_RELATION";
*/

//	public static final String EXHAUSTIVE_SUBCLASS_PARTITION="exhaustive-subclass-partition";
	public DAMLNamesContainer (String base_namespace)
	{
		super(base_namespace);
	//	elements_names_table.add("Class","RDF_CLASS_");
		types_names_table.put("Class","ANONYM_DAML_CLASS_");
		elements_numbers_table.put("Class",new Integer(0));
	}

	public DAMLNamesContainer ()
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
