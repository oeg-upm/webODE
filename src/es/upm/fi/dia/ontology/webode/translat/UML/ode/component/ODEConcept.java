package es.upm.fi.dia.ontology.webode.translat.UML.ode.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;

public interface ODEConcept extends ODEComponent {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Class attributes management ---------------------------------------------

  public ODEClassAttribute addClassAttribute(ODEClassAttribute classAttribute) throws ODEException;
  public void              removeClassAttribute(ODEClassAttribute classAttribute) throws ODEException;
  public void              removeClassAttribute(String classAttributeName) throws ODEException;

  public ODEClassAttribute[] getClassAttributes();
  public String[]            getClassAttributeNames();
  public ODEClassAttribute   findClassAttribute(String classAttributeName);

  //-- Class attributes management ---------------------------------------------

  public ODEInstanceAttribute addInstanceAttribute(ODEInstanceAttribute instanceAttribute) throws ODEException;
  public void              removeInstanceAttribute(ODEInstanceAttribute instanceAttribute) throws ODEException;
  public void              removeInstanceAttribute(String instanceAttributeName) throws ODEException;

  public ODEInstanceAttribute[] getInstanceAttributes();
  public String[]               getInstanceAttributeNames();
  public ODEInstanceAttribute   findInstanceAttribute(String instanceAttributeName);

  //-- Parent concept management -----------------------------------------------

  /**@todo Add parent concept management via subclass relation */

  //-- Child concept management ------------------------------------------------

  /**@todo Add child concept management via subclass relation */
}
