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

public class ODEClassAttributeWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void write(ODEClassAttribute att) throws ODEIOException {

    Logger.initOperationLog("Class attribute writing",
                            "\""+att.getName()+"\"");

    try {
      ClassAttributeDescriptor descriptor=
          new ClassAttributeDescriptor(att.getName(),
                                       att.getParentConcept().getName(),
                                       att.getModel().getName(),
                                       att.getMeasurementUnit(),
                                       att.getPrecision(),
                                       att.getMinimumCardinality(),
                                       att.getMaximumCardinality(),
                                       att.getDescription());

      odeService.insertClassAttribute(att.getModel().getName(),descriptor);

      String[] theValues=att.getValues();
      for(int i=0;i<theValues.length;i++) {
        odeService.addValueToClassAttribute(att.getModel().getName(),
                                            att.getName(),
                                            att.getParentConcept().getName(),
                                            theValues[i]);
      }

    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    }

    Logger.finishOperationLog("Class attribute writing");
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEClassAttributeWriter(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(ODEConcept concept) throws ODEIOException {
    Logger.initOperationLog("ODE Class Attribute Writing",
                            "Writing class attributes for concept \""+concept.getName()+"\"");

    ODEClassAttribute[] atts=concept.getClassAttributes();

    if(atts!=null) {
      for(int i=0;i<atts.length;i++) {
        write(atts[i]);
      }
    }

    Logger.finishOperationLog("ODE Class Attribute Writing");
  }
}