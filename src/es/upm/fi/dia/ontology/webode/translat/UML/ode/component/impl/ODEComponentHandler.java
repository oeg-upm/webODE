package es.upm.fi.dia.ontology.webode.translat.UML.ode.component.impl;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;

public class ODEComponentHandler implements ODEComponent {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  protected ODEModel model=null;

  private   String   componentName         =null;
  private   String   componentDescription  =null;

  private   Vector   componentSynonyms     =null;
  private   Vector   componentAbbreviations=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEComponentHandler(String componentName) {
    this(null,componentName);
  }

  public ODEComponentHandler(ODEModel model, String componentName) {
    this.model            =model;
    this.componentName    =componentName;
    componentSynonyms     =new Vector();
    componentAbbreviations=new Vector();
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
      ODEComponentHandler cloned=(ODEComponentHandler)super.clone();

      cloned.componentSynonyms     =(Vector)componentSynonyms.clone();
      cloned.componentAbbreviations=(Vector)componentAbbreviations.clone();

      return cloned;
    } catch (CloneNotSupportedException ex) {
      return null;
    }
  }

  //-- Interface: ODEComponent -------------------------------------------------

  public void setModel(ODEModel model) {
    this.model=model;
  }

  /**@todo Shouldn't it return a read-only proxy instead of the original copy?  */
  public ODEModel getModel() {
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

  public void addSynonym(ODESynonym synonym) {
    if(!componentSynonyms.contains(synonym))
      componentSynonyms.add(synonym);
  }

  public void removeSynonym(ODESynonym synonym) {
    if(componentSynonyms.contains(synonym))
      componentSynonyms.remove(synonym);
  }

  public void addSynonyms(ODESynonym[] synonyms) {
    for(int i=0;i<synonyms.length;i++)
      addSynonym(synonyms[i]);
  }

  public void removeSynonyms(ODESynonym[] synonyms) {
    for(int i=0;i<synonyms.length;i++)
      removeSynonym(synonyms[i]);
  }

  public ODESynonym[] getSynonyms() {
    ODESynonym[] synonyms=new ODESynonym[componentSynonyms.size()];
    componentSynonyms.toArray(synonyms);
    return synonyms;
  }

  public void addAbbreviation(ODEAbbreviation abbreviation) {
    if(!componentAbbreviations.contains(abbreviation))
      componentAbbreviations.add(abbreviation);
  }

  public void removeAbbreviation(ODEAbbreviation abbreviation) {
    if(componentAbbreviations.contains(abbreviation))
      componentAbbreviations.remove(abbreviation);
  }

  public void addAbbreviations(ODEAbbreviation[] abbreviations) {
    for(int i=0;i<abbreviations.length;i++)
      addAbbreviation(abbreviations[i]);
  }

  public void removeAbbreviations(ODEAbbreviation[] abbreviations) {
    for(int i=0;i<abbreviations.length;i++)
      removeAbbreviation(abbreviations[i]);
  }

  public ODEAbbreviation[] getAbbreviations() {
    ODEAbbreviation[] abbreviations=
       new ODEAbbreviation[componentAbbreviations.size()];
    componentAbbreviations.toArray(abbreviations);
    return abbreviations;
  }
}