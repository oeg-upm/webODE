package es.upm.fi.dia.ontology.webode.translat.UML.uml;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;
import java.util.*;

public interface UMLModel extends Model {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- UML Model basic information management ----------------------------------

  public Date getCreationDate();
  public void setCreationDate(Date creationDate);

  public Date getModificationDate();
  public void setModificationDate(Date modificationDate);

  //-- Class management --------------------------------------------------------

  public UMLClass addClass(UMLClass umlClass)    throws UMLException;
  public void     removeClass(UMLClass umlClass) throws UMLException;
  public void     removeClass(String className)  throws UMLException;

  public UMLClass[] getClasses();
  public String[]   getClassNames();
  public UMLClass   findClass(String className);

  //-- Generalization management -----------------------------------------------

  public UMLGeneralization addGeneralization(UMLGeneralization generalization)    throws UMLException;
  public void              removeGeneralization(UMLGeneralization generalization) throws UMLException;

  public UMLGeneralization[] getGeneralizations();

  //-- Association management --------------------------------------------------

  public UMLAssociation addAssociation(UMLAssociation association)    throws UMLException;
  public void           removeAssociation(UMLAssociation association) throws UMLException;

  public UMLAssociation[] getAssociations();

  //-- Aggregation management --------------------------------------------------

  public UMLAggregation addAggregation(UMLAggregation association)    throws UMLException;
  public void           removeAggregation(UMLAggregation association) throws UMLException;

  public UMLAggregation[] getAggregations();
}