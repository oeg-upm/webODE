package es.upm.fi.dia.ontology.webode.translat.UML.ode.io.readers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.rmi.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;
import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.*;

public class ODEClassAttributeReader {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEClassAttribute read(ODEConcept concept, ClassAttributeDescriptor descriptor) throws ODEIOException {

    Logger.initOperationLog("Class attribute reading",
                            "\""+descriptor.name+"\"");

    ODEClassAttribute classAttribute=ODEComponentFactory.createClassAttribute(concept,descriptor.name);
    classAttribute.setDescription(descriptor.description);
    classAttribute.setMinimumCardinality(descriptor.minCardinality);
    classAttribute.setMaximumCardinality(descriptor.maxCardinality);
    classAttribute.setMeasurementUnit(descriptor.measurementUnit);
    classAttribute.setPrecision(descriptor.precision);
    classAttribute.setValues(descriptor.values);

    int valueType=AttributeValueTypes.toODEDataType(descriptor.valueType);

    classAttribute.setValueType(valueType);

    Logger.finishOperationLog("Class attribute reading");

    return classAttribute;
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEClassAttributeReader(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void read(ODEConcept concept) throws ODEIOException {
    Vector                     updatedAttributes=new Vector();
    ClassAttributeDescriptor[] classAttributes  =null;
    Logger.initOperationLog("ODE Class Attribute Reading",
                            "Reading class attributes for concept \""+concept.getName()+"\"");
    try {
      classAttributes=
          odeService.getClassAttributes(concept.getModel().getName(),
                                        concept.getName());
      if(classAttributes!=null) {
        for(int i=0;i<classAttributes.length;i++) {
          ODEClassAttribute newClassAttribute=read(concept,classAttributes[i]);
          concept.addClassAttribute(newClassAttribute);
          updatedAttributes.add(newClassAttribute);
        }
      }
    } catch (ODEException    ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } finally {
      Logger.finishOperationLog("ODE Class Attribute Reading");
    }
  }
}