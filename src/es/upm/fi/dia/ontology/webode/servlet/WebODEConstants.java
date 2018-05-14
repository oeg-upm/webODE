package es.upm.fi.dia.ontology.webode.servlet;

public interface WebODEConstants
{
    String VERSION     = "WebODE 2.0";

    String HOST        = "host";
    String MINERVA_URL = "url";
    String MINERVA_PORT = "minerva_port";

    String LOGIN_SUCCESS = "success";
    String LOGIN_FAILURE = "failure";
    String ODE_SERVICE = "ode";
    String MINERVA_SESSION = "minerva-session";
    String AXIOM_SERVICE ="axiom";
    String INFERENCE_SERVICE = "inference";
    String EXPORT_SERVICE = "xml-export";
    String IMPORT_SERVICE = "xml-import";
    String ALLOW_GROUP = "allow_group";
    String ONTOCLEAN_SERVICE = "ontoclean";
    String PROLOG_EXPORT_SERVICE = "prolog";
    String DAMLOIL_SERVICE = "damloil";
    String RDFS_SERVICE = "rdfs";
    String OWL_SERVICE = "owl";
    String OWL_IMPORT_SERVICE = "owl-import";
    String DAMLOIL_IMPORT_SERVICE = "damloil-import";
    String RDFS_IMPORT_SERVICE = "rdfs-import";
    String UML_SERVICE = "UML";
    String UML_IMPORT_SERVICE = "UML";
    String DIFFERENCES_SERVICE = "differences";

    //Variables usadas en OntoClean
    String TOP_LEVEL_ONTOLOGY = "top level ontology of universals";
    String TOP_UNIVERSALS = "property";

    String REDIRECTION_URL = "url";
    String JAVASCRIPT      = "javascript";
    String PARAM           = "param";

    String TIME_LIMIT   = "time_limit";
    String ALLOWED      = "allow";
    String UNAUTHORIZED = "unauthorized";

    String USER = "user";
    String PASSWORD = "password";
    String GROUP  = "group";
    String GROUPS = "groups";

    String INSTANCE = "instance";

    String LANGUAGE = "language";

    String ONTOLOGY_NAME = "ontology_name";
    String ORIGINAL_ONTOLOGY_NAME = "original_ontology_name";
    String ONTOLOGY_DESCRIPTION = "ontology_description";

    String REFERENCE_NAME = "reference_name";
    String REFERENCE_DESCRIPTION = "reference_description";
    String ORIGINAL_REFERENCE_NAME = "original_reference_name";

    String CURRENT_ONTOLOGY = "current_ontology";

    String INFERRED_TERM_NAME = "inferred_term_name";
    String INFERRED_ATTRIBUTE_NAME = "inferred_attribute_name";
    String ORIGINAL_TERM_NAME = "original_term_name";
    String TERM_NAME = "term_name";
    String TERM_TYPE = "term_type";
    String TERM_DESCRIPTION = "term_description";
    String CAU = "carries_anti_unity";
    String AR  = "anti_rigid";
    String CIC = "carries_identity_criterion";
    String SIC = "supplies_identity_criterion";
    String CU  = "carries_unity";
    String ID  = "dependent";
    String IR  = "rigid";
    String PARENT_NAME      = "parent_name";

    String ATTRIBUTE_NAME = "attribute_name";
    String ORIGINAL_ATTRIBUTE_NAME = "original_attribute_name";
    String ATTRIBUTE_DESCRIPTION = "attribute_description";
    String ATTRIBUTE_TYPE = "attribute_type";
    String ATTRIBUTE_SUBTYPE = "attribute_subtype";
    //String ATTRIBUTE_ONTOLOGY = "attribute_ontology";
    String XML_SCHEMA_DATATYPE = "XML_Schema_datatype";
    String ATTRIBUTE_MEASUREMENT_UNIT = "measurement_unit";
    String MIN_CARDINALITY = "min_cardinality";
    String MAX_CARDINALITY = "max_cardinality";
    String MAX_VALUE       = "max_value";
    String MIN_VALUE       = "min_value";
    String PRECISION       = "precision";

    String SYNONYM_NAME       = "synonym_name";
    String ABBREVIATION_NAME  = "abbreviation_name";

    String ATTRIBUTE_VALUE    = "attribute_value";

    String RELATION_NAME      = "relation_name";

    String EXPRESSION         = "expression";
    String PROLOG_EXPRESSION = "prolog_expression";
    String TYPE               = "type";

    String FORMULA_NAME       = "formula_name";

    String LABEL              = "label";
    String BACK_URL           = "backURL";
    String ACTIVE_INSTANCE_SET = "active_instance_set";
    String INSTANCE_NAME      = "instance_name";
    String INSTANCE_SETS      = "instance_sets";
    String CONCEPTUALIZATION  = "conceptualization";
    String TRUE               = "true";
    String CONCEPTS           = "concepts";
    String ORIGIN             = "origin";
    String DESTINATION        = "destination";
    String INSTANCE_OF        = "instance_of";
    String VIEWS              = "views";

    //Imported terms
    String URI 		      = "URI";
    String NAMESPACE	      = "namespace";
    String NAMESPACE_IDENTIFIER	    = "namespace_identifier";

    // Enumerated types specific
    String ENUMERATED_TYPE_NAME      ="enumerated_type_name";
    String ENUMERATED_TYPE_VALUE_NAME="enumerated_value_name";
    String ORIGINAL_ENUMERATED_TYPE_NAME="original_enumerated_type";
    String ENUMERATED_TYPE_DESCRIPTION="enumerated_type_description";
}
