package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.tools;

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

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.containers.*;

public class XMIResourceIdGenerator {

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public static String generateId(XMITaggedValue tv) {
    String id=tv.getModelElement()+"::TaggedValue_"+tv.getTag().hashCode();

    return id.replace(' ','_');
  }

  public static String generateId(XMIDataType dataType) {
    String id="DataType::"+dataType.getName();;

    return id.replace(' ','_');
  }

  public static String generateId(XMIStereotype stereotype) {
    String id=stereotype.getModelElement()+"::Stereotype";

    return id.replace(' ','_');
  }

  public static String generateId(XMIGeneralization generalization) {
    String id="Generalization::"+generalization.getBaseClass()+"::"+generalization.getDerivedClass();

    return id.replace(' ','_');
  }

  public static String generateId(UMLModel model) {
    String id="ODEModel::"+model.getName();

    return id.replace(' ','_');
  }

  public static String generateId(UMLClass umlClass) {
    String id=umlClass.getModel().getName()+"::"+umlClass.getName();

    return id.replace(' ','_');
  }

  public static String generateId(UMLAttribute attribute) {
    String id=attribute.getParentClass().getName()+"::"+attribute.getName();

    return id.replace(' ','_');
  }

  public static String generateId(UMLAssociation association) {
    String id="Association::"+association.getModel().getName()+"::"+
              association.getName()+"."+
              association.getOriginClass().getName()+"."+
              association.getDestinationClass().getName();

    return id.replace(' ','_');
  }

  public static String generateId(UMLAssociation association, boolean isOrigin) {
    String   appendix=null;

    if(isOrigin) {
      appendix="Origin";
    } else {
      appendix="Destination";
    }

    String id="Association::"+association.getModel().getName()+"::"+
              association.getName()+"."+
              association.getOriginClass().getName()+"."+
              association.getDestinationClass().getName()+"."+
              appendix;
    return id.replace(' ','_');
  }

  public static String generateId(UMLAggregation aggregation) {
    String id="Aggregation::"+aggregation.getModel().getName()+"::"+
              aggregation.getName()+"."+
              aggregation.getOriginClass().getName()+"."+
              aggregation.getDestinationClass().getName();

    return id.replace(' ','_');
  }

  public static String generateId(UMLAggregation aggregation, boolean isOrigin) {
    String   appendix=null;

    if(isOrigin) {
      appendix="Origin";
    } else {
      appendix="Destination";
    }

    String id="Aggregation::"+aggregation.getModel().getName()+"::"+
              aggregation.getName()+"."+
              aggregation.getOriginClass().getName()+"."+
              aggregation.getDestinationClass().getName()+"."+
              appendix;
    return id.replace(' ','_');
  }
}