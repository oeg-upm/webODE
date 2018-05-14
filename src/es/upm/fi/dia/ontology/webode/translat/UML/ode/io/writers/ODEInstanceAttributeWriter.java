package es.upm.fi.dia.ontology.webode.translat.UML.ode.io.writers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.rmi.*;

import es.upm.fi.dia.ontology.webode.service.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;
import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.*;

public class ODEInstanceAttributeWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void read(ODEInstanceAttribute att) throws ODEIOException {

    Logger.initOperationLog("Instance attribute writing",
                            "\""+att.getName()+"\"");

    InstanceAttributeDescriptor descriptor=null;
    try {
      descriptor=
          new InstanceAttributeDescriptor(att.getName(),
                                          att.getParentConcept().getName(),
                                          att.getModel().getName(),
                                          att.getMeasurementUnit(),
                                          att.getPrecision(),
                                          att.getMinimumCardinality(),
                                          att.getMaximumCardinality(),
                                          att.getDescription(),
                                          Integer.toString(att.getMinimumValue()),
                                          Integer.toString(att.getMaximumValue()));

      odeService.insertInstanceAttribute(att.getModel().getName(),descriptor);

      String[] theValues=att.getValues();
      for(int i=0;i<theValues.length;i++) {
        odeService.addValueToInstanceAttribute(att.getModel().getName(),
                                               att.getName(),
                                               att.getParentConcept().getName(),
                                               theValues[i]);
      }

    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    }

    Logger.finishOperationLog("Instance attribute writing");
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEInstanceAttributeWriter(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void read(ODEConcept concept) throws ODEIOException {
    Logger.initOperationLog("ODE Instance Attribute Writing",
                            "Writing instance attributes for concept \""+concept.getName()+"\"");

    ODEInstanceAttribute[] atts=concept.getInstanceAttributes();
    if(atts!=null) {
      for(int i=0;i<atts.length;i++) {
        read(atts[i]);
      }
    }

    Logger.finishOperationLog("ODE Instance Attribute Writing");
  }
}