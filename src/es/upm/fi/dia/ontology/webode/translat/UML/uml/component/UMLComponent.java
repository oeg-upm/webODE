package es.upm.fi.dia.ontology.webode.translat.UML.uml.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.*;

public interface UMLComponent extends Component, Cloneable {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  //-- Clonability management --------------------------------------------------

  public Object clone();

  //-- Model Management --------------------------------------------------------

  public void     setModel(UMLModel model);
  public UMLModel getModel();

  //-- Basic information management --------------------------------------------

  public String getName();
  public void   setName(String componentName);

  public String getDescription();
  public void   setDescription(String componentDescription);

}