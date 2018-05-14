package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.containers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.tools.*;

public class XMIGeneralization {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private UMLGeneralization generalization=null;

  private String generalizationBaseClass   =null;
  private String generalizationDerivedClass=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIGeneralization(UMLGeneralization generalization) {
    this.generalization=generalization;

    generalizationBaseClass   =
        XMIResourceIdGenerator.generateId(generalization.getBaseClass());
    generalizationDerivedClass=
        XMIResourceIdGenerator.generateId(generalization.getDerivedClass());
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String getBaseClass() {
    return generalizationBaseClass;
  }

  public String getDerivedClass() {
    return generalizationDerivedClass;
  }

  public UMLGeneralization getGeneralization() {
    return generalization;
  }

}