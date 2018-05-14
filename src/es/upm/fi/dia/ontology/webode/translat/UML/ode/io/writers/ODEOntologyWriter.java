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

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;
import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.*;

public class ODEOntologyWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEOntologyWriter(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(ODEModel model) throws ODEIOException {
    String ontologyName=null;
    ODEModel newModel=null;

    Logger.initOperationLog("Ontology writing",
                            "Writing basic ontology information");


    try {
      OntologyDescriptor descriptor=new OntologyDescriptor(model.getName(),
                                                           model.getDescription(),
                                                           model.getOwner(),
                                                           model.getGroupName(),
                                                           model.getModificationDate(),
                                                           model.getCreationDate(),
                                                           model.getState());
      odeService.createOntology(descriptor);
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } finally {
      Logger.finishOperationLog("Ontology writing");
    }
  }
}