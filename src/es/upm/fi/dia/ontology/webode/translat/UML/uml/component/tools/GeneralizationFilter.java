package es.upm.fi.dia.ontology.webode.translat.UML.uml.component.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;

public class GeneralizationFilter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private boolean  filterOperationMode=false;
  private UMLClass filterBaseClass    =null;
  private UMLClass filterDerivedClass =null;

  private GeneralizationConsumer filterConsumer=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public GeneralizationFilter() { }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void setOperationMode(boolean orMode) {
    filterOperationMode=orMode;
  }

  public void setBaseClass(UMLClass baseClass) {
    filterBaseClass=baseClass;
  }

  public void setDerivedClass(UMLClass derivedClass) {
    filterDerivedClass=derivedClass;
  }

  public void registerGeneralizationConsumer(GeneralizationConsumer consumer) {
    filterConsumer=consumer;
  }

  public void filter(UMLGeneralization generalization) {
    if(filterConsumer!=null) {
      if(filterOperationMode) {
        if(filterBaseClass.getName().equals(generalization.getBaseClass().getName())|
           filterDerivedClass.getName().equals(generalization.getDerivedClass().getName())) {
          filterConsumer.consumeGeneralization(generalization);
        }
      } else {
        if(filterBaseClass.getName().equals(generalization.getBaseClass().getName())&&
           filterDerivedClass.getName().equals(generalization.getDerivedClass().getName())) {
          filterConsumer.consumeGeneralization(generalization);
        }
      }
    }
  }

  public void filter(UMLGeneralization[] generalizations) {
    for(int i=0;i<generalizations.length;i++)
      filter(generalizations[i]);
  }
}