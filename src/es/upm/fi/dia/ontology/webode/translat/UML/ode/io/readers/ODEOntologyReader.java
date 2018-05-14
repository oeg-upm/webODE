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

import es.upm.fi.dia.ontology.webode.service.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;
import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.*;

public class ODEOntologyReader {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEOntologyReader(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEModel read(String ontologyName) throws ODEIOException {
    ODEModel newModel=null;

    Logger.initOperationLog("Ontology reading",
                        "Reading basic ontology information");


    try {
      OntologyDescriptor od=odeService.getOntologyDescriptor(ontologyName);

      newModel=new ODEModelImpl(od.name);
      newModel.setDescription(od.description);
      newModel.setOwner(od.login);
      newModel.setGroupName(od.groupName);
      newModel.setCreationDate(od.creationDate);
      newModel.setState(od.state);
      newModel.setModificationDate(od.modificationDate);

    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } finally {
      Logger.finishOperationLog("Ontology reading");

    }

    return newModel;
  }
}