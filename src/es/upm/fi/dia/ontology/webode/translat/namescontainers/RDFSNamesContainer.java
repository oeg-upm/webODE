package es.upm.fi.dia.ontology.webode.translat.namescontainers;

public class RDFSNamesContainer extends NamesContainer
{
	public final String SUBCLASS_OF= this.RDFS_NAMESPACE+"subClassOf";
	public final String INSTANCE_OF= "INSTANCE_OF_RELATION";
	public final String N_CARDINALITY="-1";
	public final String CLASS  =this.RDFS_NAMESPACE+"Class";
	public final String URI_REFERENCE = "OWL_URI_REFERENCE";
	public final String SUB_PROPERTY_OF =this.RDFS_NAMESPACE+"subPropertyOf";
	public final String DOMAIN =this.RDFS_NAMESPACE+"domain";
	public final String RANGE =this.RDFS_NAMESPACE+"range";
	public final String LABEL=this.RDFS_NAMESPACE+"label";
	public final String COMMENT=this.RDFS_NAMESPACE+"comment";
	public final String IS_DEFINED_BY=this.RDFS_NAMESPACE+"isDefinedBy";
	public final String SEE_ALSO=this.RDFS_NAMESPACE+"seeAlso";
	public final String RESOURCE=this.RDFS_NAMESPACE+"Resource";
	public final String INSTANCE="Instance";
	public final String VALUE="Value";

	public RDFSNamesContainer (String ontology_namespace)
	{
		super(ontology_namespace);
	//	elements_names_table.add("Class","RDF_CLASS_");
		types_names_table.put(CLASS,"ANONYM_RDFS_CLASS_");
		elements_numbers_table.put(CLASS,new Integer(0));
		types_names_table.put(INSTANCE,"ANONYM_RDFS_INSTANCE_");
		elements_numbers_table.put(INSTANCE,new Integer(0));
		types_names_table.put("Unknown","UNKNOWN_RDFS_");
		elements_numbers_table.put("Unknown",new Integer(0));
	}
}
