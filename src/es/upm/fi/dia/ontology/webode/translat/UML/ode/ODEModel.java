package es.upm.fi.dia.ontology.webode.translat.UML.ode;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.*;

import es.upm.fi.dia.ontology.webode.translat.UML.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;

public interface ODEModel extends Model, Cloneable {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Interface: Cloneable ----------------------------------------------------

  public Object  clone();

  //-- Ontology basic information management -----------------------------------

  public String  getOwner();
  public void    setOwner(String owner);

  public String  getGroupName();
  public void    setGroupName(String groupName);

  public Date    getCreationDate();
  public void    setCreationDate(Date creationDate);

  public Date    getModificationDate();
  public void    setModificationDate(Date modificationDate);

  public boolean getState();
  public void    setState(boolean state);

  //-- Concept management ------------------------------------------------------

  public ODEConcept addConcept(ODEConcept concept) throws ODEException;
  public void       removeConcept(ODEConcept concept) throws ODEException;
  public void       removeConcept(String conceptName) throws ODEException;

  public ODEConcept[] getConcepts();
  public String[]     getConceptNames();
  public ODEConcept   findConcept(String conceptName);

  //-- Subclass-Of relation management -----------------------------------------

  public ODESubclassOfRelation addSubclassOfRelation(ODESubclassOfRelation relation) throws ODEException;
  public void                  removeSubclassOfRelation(ODESubclassOfRelation relation) throws ODEException;

  public ODESubclassOfRelation[] getSubclassOfRelations();

  //-- Adhoc relation management -----------------------------------------------

  public ODEAdhocRelation addAdhocRelation(ODEAdhocRelation relation) throws ODEException;
  public void             removeAdhocRelation(ODEAdhocRelation relation) throws ODEException;

  public ODEAdhocRelation[] getAdhocRelations();

  //-- Mereological relation management ----------------------------------------

  public ODEMereologicalRelation addMereologicalRelation(ODEMereologicalRelation relation) throws ODEException;
  public void             removeMereologicalRelation(ODEMereologicalRelation relation) throws ODEException;

  public ODEMereologicalRelation[] getMereologicalRelations();

  /**@todo Create filter architecture for searching stuff */

}