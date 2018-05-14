package es.upm.fi.dia.ontology.webode.OntoClean;

public interface OntoCleanConstants
{
    // Para acceder a la top level ontology of universals
    String TOP_LEVEL                          = "top level ontology of universals";
    String PROPERTY                           = "property";
    //Simbolos para mandarlos al OntoDesigner 	
    String ANTI_RIGID                         = "~R";
    String IS_RIGID                           = "+R";
    String IS_NOT_RIGID                       = "-R";
    String IS_DEPENDENT                       = "+D";
    String IS_NOT_DEPENDENT 		      = "-D";
    String CARRIES_IDENTITY_CRITERION         = "+I";
    String NO_CARRIES_IDENTITY_CRITERION      = "-I";
    String SUPPLIES_IDENTITY_CRITERION        = "+O";
    String NO_SUPPLIES_IDENTITY_CRITERION     = "-O";
    String ANTI_UNITY                         = "~U";
    String CARRIES_UNITY                      = "+U";
    String NO_CARRIES_UNITY                   = "-U";
    //Valores para los valores de los atributos de instancia de la top level ontology of universals
    String T                                  = "true";
    String F                                  = "false";
    String N                                  = "no_value"; 
    //Nombre de los atributos de instancia del concepto "property" de la top level.
    String ATT_CARRIES_ANTI_UNITY = "carries anti-unity";
    String ATT_CARRIES_UNITY = "carries unity";
    String ATT_IS_DEPENDENT = "is dependent";
    String ATT_ANTI_RIGID = "anti-rigid";
    String ATT_CARRIES_IDENTITY_CRITERION ="carries identity criterion";
    String ATT_SUPPLIES_IDENTITY_CRITERION = "supplies identity criterion";
    String ATT_IS_RIGID = "is-rigid";
}
