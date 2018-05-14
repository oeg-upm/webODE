package es.upm.fi.dia.ontology.webode.translat.UML.ode.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;

public interface ODEComponent extends Component, Cloneable {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Clonability management --------------------------------------------------

  public Object clone();

  //-- Model management --------------------------------------------------------

  public void     setModel(ODEModel model);
  public ODEModel getModel();

  //-- Basic information management --------------------------------------------

  public String getName();
  public void   setName(String componentName);

  public String getDescription();
  public void   setDescription(String componentDescription);

  //-- Synonym management ------------------------------------------------------

  public void addSynonym(ODESynonym synonym);
  public void removeSynonym(ODESynonym synonym);
  public void addSynonyms(ODESynonym[] synonyms);
  public void removeSynonyms(ODESynonym[] synonyms);
  public ODESynonym[] getSynonyms();

  //-- Abbreviation management -------------------------------------------------

  public void addAbbreviation(ODEAbbreviation abbreviation);
  public void removeAbbreviation(ODEAbbreviation abbreviation);
  public void addAbbreviations(ODEAbbreviation[] abbreviations);
  public void removeAbbreviations(ODEAbbreviation[] abbreviations);
  public ODEAbbreviation[] getAbbreviations();

  //-- Reference management ----------------------------------------------------

  /**@todo Add reference management */

  //-- Formulae management -----------------------------------------------------

  /**@todo Add formulae management */
}