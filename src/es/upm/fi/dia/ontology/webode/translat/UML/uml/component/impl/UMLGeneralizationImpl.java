package es.upm.fi.dia.ontology.webode.translat.UML.uml.component.impl;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;

public class UMLGeneralizationImpl extends UMLComponentHandler
                                   implements UMLGeneralization {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private UMLClass generalizationBaseClass   =null;
  private UMLClass generalizationDerivedClass=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public UMLGeneralizationImpl(UMLClass baseClass, UMLClass derivedClass) {
    super(baseClass.getModel(),"Generalization");
    generalizationBaseClass=baseClass;
    generalizationDerivedClass=derivedClass;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();

    mySelf="UMLGeneralization {"+
           generalizationBaseClass.getName()+"-->"+
           generalizationDerivedClass.getName()+"}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    UMLGeneralizationImpl cloned=(UMLGeneralizationImpl)super.clone();

    return cloned;
  }

  //-- Interface: UMLGeneralization --------------------------------------------

  public UMLClass getBaseClass() {
    return generalizationBaseClass;
  }

  public void setBaseClass(UMLClass baseClass) {
    /**@todo Check that the base class belongs to the model */
    generalizationBaseClass=baseClass;
  }

  public UMLClass getDerivedClass() {
    return generalizationDerivedClass;
  }

  public void setDerivedClass(UMLClass derivedClass) {
    /**@todo Check that the derived class belongs to the model */
    generalizationDerivedClass=derivedClass;
  }

}