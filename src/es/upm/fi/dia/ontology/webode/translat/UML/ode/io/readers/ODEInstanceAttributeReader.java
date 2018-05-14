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

public class ODEInstanceAttributeReader {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEInstanceAttribute read(ODEConcept concept, InstanceAttributeDescriptor descriptor) throws ODEIOException {

    Logger.initOperationLog("Instance attribute reading",
                            "\""+descriptor.name+"\"");

    ODEInstanceAttribute instanceAttribute=ODEComponentFactory.createInstanceAttribute(concept,descriptor.name);
    instanceAttribute.setDescription(descriptor.description);
    instanceAttribute.setMinimumCardinality(descriptor.minCardinality);
    instanceAttribute.setMaximumCardinality(descriptor.maxCardinality);
    instanceAttribute.setMeasurementUnit(descriptor.measurementUnit);
    instanceAttribute.setPrecision(descriptor.precision);
    instanceAttribute.setValues(descriptor.values);
    if(descriptor.minValue!=null)
      instanceAttribute.setMinimumValue(Integer.parseInt(descriptor.minValue));
    if(descriptor.maxValue!=null)
      instanceAttribute.setMaximumValue(Integer.parseInt(descriptor.maxValue));

    instanceAttribute.setValueType(
        AttributeValueTypes.toODEDataType(descriptor.valueType));

    Logger.finishOperationLog("Instance attribute reading");

    return instanceAttribute;
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEInstanceAttributeReader(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void read(ODEConcept concept) throws ODEIOException {
    Vector                        updatedAttributes =new Vector();
    InstanceAttributeDescriptor[] instanceAttributes=null;

    Logger.initOperationLog("ODE Instance Attribute Reading",
                            "Reading instance attributes for concept \""+concept.getName()+"\"");

    try {
      instanceAttributes=
          odeService.getInstanceAttributes(concept.getModel().getName(),
                                           concept.getName());
      if(instanceAttributes!=null) {
        for(int i=0;i<instanceAttributes.length;i++) {
          ODEInstanceAttribute newInstanceAttribute=read(concept,instanceAttributes[i]);
          concept.addInstanceAttribute(newInstanceAttribute);
          updatedAttributes.add(newInstanceAttribute);
        }
      }
    } catch (ODEException    ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } finally {
      Logger.finishOperationLog("ODE Instance Attribute Reading");
    }
  }
}