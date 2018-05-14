package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.containers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public class XMIStereotype {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private String stereotypeName   =null;
  private String stereotypeElement=null;
  private String stereotypeBase   =null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIStereotype(String name, String element, String base) {
    stereotypeName=name;
    stereotypeElement=element;
    stereotypeBase=base;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String getName() {
    return stereotypeName;
  }

  public void setName(String name) {
    stereotypeName=name;
  }

  public String getModelElement() {
    return stereotypeElement;
  }

  public void setModelElement(String element) {
    stereotypeElement=element;
  }

  public String getBase() {
    return stereotypeBase;
  }

  public void setBase(String base) {
    stereotypeBase = base;
  }

}
