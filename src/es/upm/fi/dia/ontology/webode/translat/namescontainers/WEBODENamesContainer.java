package es.upm.fi.dia.ontology.webode.translat.namescontainers;






public class WEBODENamesContainer extends NamesContainer
{
  public final String N_CARDINALITY="-1";
  public final String SUBCLASS_OF="Subclass-of";
  public final String NOT_SUBCLASS_OF="Not-Subclass-of";
  public final String EXHAUSTIVE_SUBCLASS_PARTITION="Exhaustive";
  public final String AXIOM = "Axiom";
  public final String GROUP = "GROUP";
  public final String CONCEPT = "CONCEPT";
  public final String RELATION_INSTANCE= "RELATION_INSTANCE";
  public final String IMPORTED_TERM= "IMPORTED_TERM";




  public WEBODENamesContainer (String ontology_namespace)
  {
    super(ontology_namespace);
//		System.out.println("Creating the WebODE namescontainer ");
//		System.out.println("    URI: "+ontology_uri);
//		System.out.println("	Namespaces: "+ontology_namespace);
    //	elements_names_table.add("Class","RDF_CLASS_");
    types_names_table.put(CONCEPT,"ANONYMOUS_CONCEPT_");
    elements_numbers_table.put(CONCEPT,new Integer(0));
    types_names_table.put(GROUP,"GROUP_");
    elements_numbers_table.put(GROUP,new Integer(0));
    types_names_table.put(AXIOM,"AXIOM_");
    elements_numbers_table.put(AXIOM,new Integer(0));
    types_names_table.put(RELATION_INSTANCE,"RELATION_INSTANCE_");
    elements_numbers_table.put(RELATION_INSTANCE,new Integer(0));
    types_names_table.put(IMPORTED_TERM,"IMPORTED_TERM_");
    elements_numbers_table.put(IMPORTED_TERM,new Integer(0));
    types_names_table.put(NAMESPACE_IDENTIFIER,"ns");
    elements_numbers_table.put(NAMESPACE_IDENTIFIER,new Integer(0));



    datatypes_table.put("http://www.w3.org/2000/10/XMLSchema#string","String");
    datatypes_table.put("http://www.w3.org/2000/10/XMLSchema#nonNegativeInteger","Integer");
    datatypes_table.put("http://www.w3.org/2000/10/XMLSchema#float","Float");
    datatypes_table.put("http://www.w3.org/2000/10/XMLSchema#boolean","Boolean");
    datatypes_table.put("http://www.w3.org/2000/10/XMLSchema#decimal","Float");
    datatypes_table.put("http://www.w3.org/2000/10/XMLSchema#integer","Integer");
  }



}