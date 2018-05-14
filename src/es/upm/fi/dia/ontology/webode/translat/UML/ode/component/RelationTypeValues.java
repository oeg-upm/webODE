package es.upm.fi.dia.ontology.webode.translat.UML.ode.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public interface RelationTypeValues {

  /**
   * Values taken from es.upm.fi.dia.ontology.webode.service.TermRelation
   */

  public static final String ADHOC               ="Adhoc";
  public static final String DISJOINT            ="Disjoint";
  public static final String EXHAUSTIVE          ="Exhaustive";
  public static final String INTRANSITIVE_PART_OF="Intransitive-Part-of";
  public static final String NOT_SUBCLASS_OF     ="Not-Subclass-of";
  public static final String SUBCLASS_OF         ="Subclass-of";
  public static final String TRANSITIVE_PART_OF  ="Transitive-Part-of";

}