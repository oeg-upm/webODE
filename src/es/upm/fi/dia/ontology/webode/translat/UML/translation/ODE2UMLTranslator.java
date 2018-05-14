package es.upm.fi.dia.ontology.webode.translat.UML.translation;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.impl.*;

import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;

import es.upm.fi.dia.ontology.webode.translat.UML.translation.tools.*;
import java.io.*;

public class ODE2UMLTranslator {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private UMLModel umlModel=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  /**@todo Use a factory for creating UML components instead of the classes themselves */
  private UMLAttribute translateAttribute(ODEAttribute odeAttribute,
                                          UMLClass     umlClass) throws IOException {
    String attributeType=(odeAttribute instanceof ODEClassAttribute?"class":"instance");
    Logger.initOperationLog("Translating "+attributeType+" attribute",
                            "\""+odeAttribute.getName()+"\"");

    UMLAttribute umlAttribute=new UMLAttributeImpl(umlClass,
                                                   odeAttribute.getName());


    AttributeDocTranslator dt=new AttributeDocTranslator();
    umlAttribute.setDescription(dt.fromOde(odeAttribute));

    AttributeMultiplicityTranslator mt=
        new AttributeMultiplicityTranslator(odeAttribute.getMinimumCardinality(),
                                            odeAttribute.getMaximumCardinality());
    umlAttribute.setMultiplicity(mt.getMultiplicity());

    umlAttribute.setType(
       AttributeValueTypes.toString(odeAttribute.getValueType()));

    AttributeValuesTranslator vt=
        new AttributeValuesTranslator(odeAttribute.getValues());

    umlAttribute.setInitialValue(vt.getInitialValue());

    Logger.finishOperationLog("Translating "+attributeType+" attribute");

    return umlAttribute;
  }

  private void translateClassAttributes(ODEClassAttribute[] attributes,
                                        UMLClass            umlClass)
                                                           throws UMLException,
                                                                  IOException {
    Logger.initOperationLog("ODE Class Attribute Translation",
                            "Translating concept \""+umlClass.getName()+"\" class attributes");

    for(int i=0;i<attributes.length;i++) {
      UMLAttribute umlAttribute=translateAttribute(attributes[i],umlClass);
      umlAttribute.setStereotype("ClassAttribute");
      umlClass.addAttribute(umlAttribute);
    }

    Logger.finishOperationLog("ODE Class Attribute Translation");
  }

  private void translateInstanceAttributes(ODEInstanceAttribute[] attributes,
                                           UMLClass               umlClass)
                                                           throws UMLException,
                                                                  IOException {
    Logger.initOperationLog("ODE Instance Attribute Translation",
                            "Translating concept \""+umlClass.getName()+"\" instance attributes");

    for(int i=0;i<attributes.length;i++) {
      UMLAttribute umlAttribute=translateAttribute(attributes[i],umlClass);
      umlAttribute.setStereotype("InstanceAttribute");
      umlClass.addAttribute(umlAttribute);
    }


    Logger.finishOperationLog("ODE Instance Attribute Translation");
  }

  /**@todo Use a factory for creating UML components instead of the classes themselves */
  private void translateConcept(ODEConcept concept) throws UMLException,
                                                           IOException {

    Logger.initOperationLog("Translating concept",
                            "\""+concept.getName()+"\"");

    UMLClass newClass=new UMLClassImpl(null,concept.getName());

    newClass.setDescription(concept.getDescription());

    translateClassAttributes(concept.getClassAttributes(),newClass);
    translateInstanceAttributes(concept.getInstanceAttributes(),newClass);

    umlModel.addClass(newClass);

    Logger.finishOperationLog("Translating concept");
  }

  private void translateConcepts(ODEModel odeModel) throws UMLException,
                                                           IOException {
    Logger.initOperationLog("ODE Concepts Translation",
                            "Translating model \""+odeModel.getName()+"\" concepts");

    ODEConcept[] concepts=odeModel.getConcepts();

    for(int i=0;i<concepts.length;i++)
      translateConcept(concepts[i]);

    Logger.finishOperationLog("ODE Concepts Translation");
  }

  /**@todo Use a factory for creating UML components instead of the classes themselves */
  private void translateSubclassOfRelations(ODEModel model) throws UMLException,
                                                                   IOException {

    Logger.initOperationLog("ODE Subclass-Of Relations Translation",
                            "Translating model \""+model.getName()+"\" subclass-of relations");

    ODESubclassOfRelation[] relations=model.getSubclassOfRelations();

    for(int i=0;i<relations.length;i++) {
      Logger.initOperationLog("Translating subclass-of relation",
                              relations[i].getName()+"("+
                              relations[i].getOrigin().getName()+","+
                              relations[i].getDestination().getName()+")");

      ODEConcept origin=relations[i].getOrigin();
      ODEConcept destination=relations[i].getDestination();
      UMLClass baseClass=umlModel.findClass(destination.getName());
      UMLClass derivedClass=umlModel.findClass(origin.getName());

      UMLGeneralization newRelation=new UMLGeneralizationImpl(baseClass,
                                                              derivedClass);

      SubclassOfRelationDocTranslator dt=new SubclassOfRelationDocTranslator();
      newRelation.setDescription(dt.fromOde(relations[i]));

      umlModel.addGeneralization(newRelation);

      Logger.finishOperationLog("Translating subclass-of relation");
    }

    Logger.finishOperationLog("ODE Subclass-Of Relations Translation");
  }

  /**@todo Use a factory for creating UML components instead of the classes themselves */
  private void translateAdhocRelations(ODEModel model) throws UMLException,
                                                              IOException {

    Logger.initOperationLog("ODE Adhoc Relations Translation",
                            "Translating model \""+model.getName()+"\" adhoc relations");

    ODEAdhocRelation[] relations=model.getAdhocRelations();

    for(int i=0;i<relations.length;i++) {
      Logger.initOperationLog("Translating adhoc relation",
                              relations[i].getName()+"("+
                              relations[i].getOrigin().getName()+","+
                              relations[i].getDestination().getName()+")");

      ODEConcept origin=relations[i].getOrigin();
      ODEConcept destination=relations[i].getDestination();
      UMLClass originClass=umlModel.findClass(origin.getName());
      UMLClass destinationClass=umlModel.findClass(destination.getName());

      UMLAssociation newRelation=new UMLAssociationImpl(relations[i].getName(),
                                                        originClass,
                                                        destinationClass);

      AdhocRelationDocTranslator dt=new AdhocRelationDocTranslator();
      newRelation.setDescription(dt.fromOde(relations[i]));

      umlModel.addAssociation(newRelation);

      Logger.finishOperationLog("Translating adhoc relation");
    }

    Logger.finishOperationLog("ODE Adhoc Relations Translation");
  }

  /**@todo Use a factory for creating UML components instead of the classes themselves */
  private void translateMereologicalRelations(ODEModel model) throws UMLException,
                                                                     IOException {

    Logger.initOperationLog("ODE Mereological Relations Translation",
                            "Translating model \""+model.getName()+"\" mereological relations");

    ODEMereologicalRelation[] relations=model.getMereologicalRelations();

    for(int i=0;i<relations.length;i++) {
      Logger.initOperationLog("Translating mereological relation",
                              relations[i].getName()+"("+
                              relations[i].getOrigin().getName()+","+
                              relations[i].getDestination().getName()+")");
      ODEConcept origin=relations[i].getOrigin();
      ODEConcept destination=relations[i].getDestination();
      UMLClass originClass=umlModel.findClass(destination.getName());
      UMLClass destinationClass=umlModel.findClass(origin.getName());

      UMLAggregation newRelation=new UMLAggregationImpl(originClass,
                                                        destinationClass);
      MereologicalRelationDocTranslator dt=new MereologicalRelationDocTranslator();
      newRelation.setDescription(dt.fromOde(relations[i]));

      umlModel.addAggregation(newRelation);

      Logger.finishOperationLog("Translating mereological relation");
    }

    Logger.finishOperationLog("ODE Mereological Relations Translation");
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODE2UMLTranslator() {
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  /**@todo Use a factory for creating UML models instead of the class itself */
  public UMLModel translate(ODEModel odeModel) throws UMLException {

    Logger.initOperationLog("ODE Model Translation",
                            "Translating model \""+odeModel.getName()+"\"");

    umlModel=new UMLModelImpl(odeModel.getName());

    umlModel.setDescription(odeModel.getDescription());

    try {
      translateConcepts(odeModel);
      translateSubclassOfRelations(odeModel);
      translateAdhocRelations(odeModel);
      translateMereologicalRelations(odeModel);
    } catch (IOException ex) {
      throw new UMLException(ex.getMessage(),getClass(),ex);
    }

    Logger.finishOperationLog("ODE Model Translation");

    return umlModel;
  }

}