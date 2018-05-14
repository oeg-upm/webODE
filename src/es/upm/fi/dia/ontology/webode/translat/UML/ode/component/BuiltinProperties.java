package es.upm.fi.dia.ontology.webode.translat.UML.ode.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public final class BuiltinProperties {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  /**
   * Taken from es.upm.fi.dia.ontology.webode.service.TermProperty
   */
  public static final String SYMMETRICAL    ="Symmetrical";
  public static final String ASYMMETRICAL   ="Asymmetrical";
  public static final String ANTISYMMETRICAL="Antisymetrical";
  public static final String IRREFLEXIVE    ="Irreflexive";
  public static final String REFLEXIVE      ="Reflexive";
  public static final String TRANSITIVE     ="Transitive";

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private static final boolean isBuiltinProperty(String property) {
    return property.equals(SYMMETRICAL) ||
           property.equals(ASYMMETRICAL) ||
           property.equals(ANTISYMMETRICAL) ||
           property.equals(IRREFLEXIVE) ||
           property.equals(REFLEXIVE) ||
           property.equals(TRANSITIVE);
  }

  private static final boolean checkSymmetry(String p1, String p2) {
    return (p1.equals(SYMMETRICAL)&&
           (!p2.equals(ASYMMETRICAL)||
            !p2.equals(ANTISYMMETRICAL))) ||
           (p1.equals(ASYMMETRICAL)&&
            (!p2.equals(SYMMETRICAL)||
             !p2.equals(ANTISYMMETRICAL))) ||
           (p1.equals(ANTISYMMETRICAL)&&
            (!p2.equals(SYMMETRICAL)||
             !p2.equals(ASYMMETRICAL)));
  }

  private static final boolean checkReflexivity(String p1, String p2) {
    return (p1.equals(REFLEXIVE)&&(!p2.equals(IRREFLEXIVE)));
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public static final boolean areCompatible(String p1, String p2) {
    boolean result=false;
    if(!isBuiltinProperty(p1)||!isBuiltinProperty(p2))
      return true;

    if(p1.equals(TRANSITIVE)||p2.equals(TRANSITIVE))
      return true;

    if(checkSymmetry(p1,p2)||checkSymmetry(p2,p1))
      return true;

    if(checkReflexivity(p1,p2)||checkReflexivity(p2,p1))
      return false;

    return result;
  }

}