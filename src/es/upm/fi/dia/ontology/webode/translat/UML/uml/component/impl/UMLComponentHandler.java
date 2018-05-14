package es.upm.fi.dia.ontology.webode.translat.UML.uml.component.impl;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;

public class UMLComponentHandler implements UMLComponent {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  protected UMLModel model                 =null;

  private   String   componentName         =null;
  private   String   componentDescription  =null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public UMLComponentHandler(String componentName) {
    this(null,componentName);
  }

  public UMLComponentHandler(UMLModel model, String componentName) {
    this.model            =model;
    this.componentName    =componentName;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public int hashCode() {
    return componentName.hashCode();
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    try {
      UMLComponentHandler cloned=(UMLComponentHandler)super.clone();

      return cloned;
    } catch (CloneNotSupportedException ex) {
      return null;
    }
  }

  //-- Interface: UMLComponent -------------------------------------------------

  public void setModel(UMLModel model) {
    this.model=model;
  }

  /**@todo Shouldn't it return a read-only proxy instead of the original copy?  */
  public UMLModel getModel() {
    return model;
  }

  public String getName() {
    return componentName;
  }

  /**@todo Check if there is any other component in the model with the same name */
  public void setName(String componentName) {
    this.componentName=componentName;
  }

  public String getDescription() {
    return componentDescription;
  }

  public void setDescription(String componentDescription) {
    this.componentDescription=componentDescription;
  }
}