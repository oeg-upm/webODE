package es.upm.fi.dia.ontology.webode.translat.UML.uml.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public interface UMLAssociation extends UMLComponent {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Origin class management -------------------------------------------------

  public UMLClass getOriginClass();
  public void     setOriginClass(UMLClass originClass);

  //-- Origin class role management --------------------------------------------

  public String   getOriginClassRole();
  public void     setOriginClassRole(String role);

  //-- Origin class cardinality management -------------------------------------

  public int      getOriginClassMinimumCardinality();
  public void     setOriginClassMinimumCardinality(int minimumCardinality);
  public int      getOriginClassMaximumCardinality();
  public void     setOriginClassMaximumCardinality(int maximumCardinality);

  //-- Origin class navigability management ------------------------------------

  /**@todo Is origin class navegability management necessary? */

  //-- Destination class management --------------------------------------------

  public UMLClass getDestinationClass();
  public void     setDestinationClass(UMLClass destinationClass);

  //-- Destination class role management ---------------------------------------

  public String   getDestinationClassRole();
  public void     setDestinationClassRole(String role);

  //-- Destination class cardinality management --------------------------------

  public int      getDestinationClassMinimumCardinality();
  public void     setDestinationClassMinimumCardinality(int minimumCardinality);
  public int      getDestinationClassMaximumCardinality();
  public void     setDestinationClassMaximumCardinality(int maximumCardinality);

  //-- Destination class navigability management -------------------------------

  /**@todo Is destination class navegability management necessary? */

}