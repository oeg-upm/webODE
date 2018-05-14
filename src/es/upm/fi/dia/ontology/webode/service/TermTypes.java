package es.upm.fi.dia.ontology.webode.service;

/**
 * A simple interface to hold valid term types.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.5
 */
public interface TermTypes 
{
    int CONCEPT            = 1;
    int RELATION           = 2;
    int INSTANCE           = 3;
    int FORMULA            = 4;
    int CLASS_ATTRIBUTE    = 5;
    int INSTANCE_ATTRIBUTE = 6;
    int SYNONYM            = 7;
    int ABBREVIATION       = 8;
    int CONSTANT           = 9;
    int AXIOM              = 10;
    int RULE               = 11;
    int PROPERTY           = 12;
    int INSTANCE_SET       = 13;
    int PROCEDURE          = 14;
    int GROUP              = 15;
    int VIEW               = 16;
    int RELATION_INSTANCE  = 17;

    int IMPORTED_TERM      = 20;

    public static final String[] NAMES = { "Concept",
					   "Relation",
					   "Instance",
					   "Formula", 
					   "Class Attribute",
					   "Instance Attribute",
					   "Synonym",
					   "Abbreviation",
					   "Constant",
					   "Axiom",
					   "Rule", 
					   "Property", 
					   "Instance Set",
					   "Procedure",
					   "Group",
					   "View" };    
}
