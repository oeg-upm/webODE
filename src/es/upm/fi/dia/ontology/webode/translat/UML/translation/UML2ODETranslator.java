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

import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;

import es.upm.fi.dia.ontology.webode.translat.UML.translation.tools.*;

import java.io.*;

public class UML2ODETranslator {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEModel odeModel=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void translateAttribute(UMLAttribute attribute,
                                  ODEConcept concept) throws ODEException {

    Logger.initOperationLog("UML Attribute Translation",
                            "Translating "+attribute.getName()+" attribute");

    boolean isClassAttribute=false;

    if(attribute.getStereotype()!=null)
      isClassAttribute=attribute.getStereotype().equals("ClassAttribute");

    ODEAttribute         newAttribute        =null;
    ODEClassAttribute    theClassAttribute   =null;
    ODEInstanceAttribute theInstanceAttribute=null;

    if(isClassAttribute) {
      theClassAttribute=ODEComponentFactory.createClassAttribute(concept,attribute.getName());
      newAttribute=theClassAttribute;
    } else {
      theInstanceAttribute=ODEComponentFactory.createInstanceAttribute(concept,attribute.getName());
      newAttribute=theInstanceAttribute;
    }

    AttributeMultiplicityTranslator mt=
        new AttributeMultiplicityTranslator(attribute.getMultiplicity());

    newAttribute.setMinimumCardinality(mt.getMinimumCardinality());
    newAttribute.setMaximumCardinality(mt.getMaximumCardinality());

    newAttribute.setValueType(AttributeValueTypes.fromString(attribute.getType()));

    AttributeValuesTranslator vt=
        new AttributeValuesTranslator(attribute.getInitialValue());

    newAttribute.setValues(vt.getValues());

    try {
      ODEDocManager          dm=new ODEDocManager(attribute.getDescription());
      AttributeDocTranslator dt=new AttributeDocTranslator();

      dt.toOde(newAttribute,dm);
    } catch (IOException ex) {
      // Can't parse data properly, but process can go on
    }

    if(AttributeValueTypes.isValid(AttributeValueTypes.fromString(attribute.getType()))) {
      if(isClassAttribute) {
        concept.addClassAttribute(theClassAttribute);
      } else {
        concept.addInstanceAttribute(theInstanceAttribute);
      }
    }

    Logger.finishOperationLog("UML Attribute Translation");
  }

  private void translateAttributes(UMLAttribute[] attributes,
                                   ODEConcept     concept) throws ODEException {
    if(attributes.length==0)
      return;

    Logger.initOperationLog("UML Attributes Translation",
                            "Translating class \""+concept.getName()+"\" attributes");

    for(int i=0;i<attributes.length;i++) {
      translateAttribute(attributes[i],concept);
    }

    Logger.finishOperationLog("UML Attributes Translation");
  }

  private void translateClass(UMLClass umlClass) throws ODEException {

    Logger.initOperationLog("Translating class",
                            "\""+umlClass.getName()+"\"");

    ODEConcept newConcept=
        ODEComponentFactory.createConcept(odeModel,umlClass.getName());

    newConcept.setDescription(umlClass.getDescription());

    translateAttributes(umlClass.getAttributes(),newConcept);

    odeModel.addConcept(newConcept);

    Logger.finishOperationLog("Translating class");
  }

  private void translateClasses(UMLModel umlModel) throws ODEException {
    Logger.initOperationLog("UML Classes Translation",
                            "Translating model \""+umlModel.getName()+"\" concepts");

    UMLClass[] classes=umlModel.getClasses();

    for(int i=0;i<classes.length;i++)
      translateClass(classes[i]);

    Logger.finishOperationLog("UML Classes Translation");
  }

  private void translateGeneralization(UMLModel umlModel) throws ODEException {

    Logger.initOperationLog("UML Generalizations Translation",
                            "Translating model \""+umlModel.getName()+"\" generalizations");

    UMLGeneralization[] generalizations=umlModel.getGeneralizations();


    for(int i=0;i<generalizations.length;i++) {
      Logger.initOperationLog("Translating generalization",
                              generalizations[i].getName()+"("+
                              generalizations[i].getBaseClass().getName()+","+
                              generalizations[i].getDerivedClass().getName()+")");

      UMLClass baseClass=generalizations[i].getBaseClass();
      UMLClass derivedClass=generalizations[i].getDerivedClass();

      ODEConcept origin=odeModel.findConcept(derivedClass.getName());
      ODEConcept destination=odeModel.findConcept(baseClass.getName());

      ODESubclassOfRelation newRelation=
          ODEComponentFactory.createSubclassOfRelation(origin,destination);

      odeModel.addSubclassOfRelation(newRelation);

      Logger.finishOperationLog("Translating generalization");
    }

    Logger.finishOperationLog("UML Generalizations Translation");
  }

  private void translateAssociations(UMLModel umlModel) throws ODEException {

    Logger.initOperationLog("UML Associations Translation",
                            "Translating model \""+umlModel.getName()+"\" associations");

    UMLAssociation[] associations=umlModel.getAssociations();

    for(int i=0;i<associations.length;i++) {
      Logger.initOperationLog("Translating association",
                              associations[i].getName()+"("+
                              associations[i].getOriginClass().getName()+","+
                              associations[i].getDestinationClass().getName()+")");

      UMLClass originClass=associations[i].getOriginClass();
      UMLClass destinationClass=associations[i].getDestinationClass();
      ODEConcept origin=odeModel.findConcept(originClass.getName());
      ODEConcept destination=odeModel.findConcept(destinationClass.getName());

      ODEAdhocRelation newRelation=
          ODEComponentFactory.createAdhocRelation(associations[i].getName(),
                                                  origin,
                                                  destination);

      try {
        ODEDocManager              dm=new ODEDocManager(associations[i].getDescription());
        AdhocRelationDocTranslator dt=new AdhocRelationDocTranslator();

        dt.toOde(newRelation,dm);
      } catch (IOException ex) {
        // Can't parse data properly, but process can go on
      }

      odeModel.addAdhocRelation(newRelation);

      Logger.finishOperationLog("Translating association");
    }

    Logger.finishOperationLog("UML Associations Translation");
  }

  private void translateAggregations(UMLModel umlModel) throws ODEException {

    Logger.initOperationLog("UML Aggregations Translation",
                            "Translating model \""+umlModel.getName()+"\" aggregations");

    UMLAggregation[] aggregations=umlModel.getAggregations();

    for(int i=0;i<aggregations.length;i++) {
      Logger.initOperationLog("Translating aggregation",
                              aggregations[i].getName()+"("+
                              aggregations[i].getOriginClass().getName()+","+
                              aggregations[i].getDestinationClass().getName()+")");

      UMLClass originClass     =aggregations[i].getOriginClass();
      UMLClass destinationClass=aggregations[i].getDestinationClass();

      ODEConcept origin     =odeModel.findConcept(destinationClass.getName());
      ODEConcept destination=odeModel.findConcept(originClass.getName());

      ODEMereologicalRelation newRelation=
          ODEComponentFactory.createMereologicalRelation(true,
                                                         origin,
                                                         destination);
      try {
        ODEDocManager                     dm=new ODEDocManager(aggregations[i].getDescription());
        MereologicalRelationDocTranslator dt=new MereologicalRelationDocTranslator();

        dt.toOde(newRelation,dm);
      } catch (IOException ex) {
        // Can't parse data properly, but process can go on
      }

      odeModel.addMereologicalRelation(newRelation);

      Logger.finishOperationLog("Translating aggregation");
    }

    Logger.finishOperationLog("UML Aggregations Translation");
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public UML2ODETranslator() {
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  /**@todo Use a factory for creating ODE model instead of the class itself */
  public ODEModel translate(UMLModel umlModel) throws ODEException {
    Logger.initOperationLog("UML Model Translation",
                            "Translating model \""+umlModel.getName()+"\"");

    odeModel=new ODEModelImpl(umlModel.getName());
    odeModel.setDescription(umlModel.getDescription());

    translateClasses(umlModel);
    translateGeneralization(umlModel);
    translateAssociations(umlModel);
    translateAggregations(umlModel);

    Logger.finishOperationLog("UML Model Translation");

    return odeModel;
  }

}