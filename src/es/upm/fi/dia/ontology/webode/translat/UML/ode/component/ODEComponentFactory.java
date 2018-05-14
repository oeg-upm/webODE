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
import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.impl.*;

public class ODEComponentFactory {

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public static ODEAbbreviation  createAbbreviation(String name,
                                                    String description) {
    ODEAbbreviation abbreviation=new ODEAbbreviation(name,
                                                     description);
    return abbreviation;
  }

  public static ODESynonym createSynonym(String name,
                                         String description) {
    ODESynonym synonym=new ODESynonym(name,description);

    return synonym;
  }

  public static ODEConcept createConcept(ODEModel model,
                                         String conceptName) {
    ODEConcept concept=new ODEConceptImpl(model,conceptName);

    return concept;
  }

  public static ODEClassAttribute createClassAttribute(ODEConcept concept,
                                                       String attributeName) {
    ODEClassAttribute classAttribute=new ODEClassAttributeImpl(concept,attributeName);

    return classAttribute;
  }

  public static ODEInstanceAttribute createInstanceAttribute(ODEConcept concept,
                                                             String attributeName) {
    ODEInstanceAttribute instanceAttribute=new ODEInstanceAttributeImpl(concept,attributeName);

    return instanceAttribute;
  }

  public static ODESubclassOfRelation createSubclassOfRelation(ODEConcept origin,
                                                               ODEConcept destination) {
    ODESubclassOfRelation relation=new ODESubclassOfRelationImpl(origin,
                                                                 destination);
    return relation;
  }

  public static ODEAdhocRelation createAdhocRelation(String relationName,
                                                     ODEConcept origin,
                                                     ODEConcept destination) {
    ODEAdhocRelation relation=new ODEAdhocRelationImpl(relationName,
                                                       origin,
                                                       destination);

    return relation;
  }

  public static ODEMereologicalRelation createMereologicalRelation(
                                                     boolean transitive,
                                                     ODEConcept origin,
                                                     ODEConcept destination) {
    ODEMereologicalRelation relation=
        new ODEMereologicalRelationImpl(origin,destination,transitive);

    return relation;
  }
}