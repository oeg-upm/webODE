package es.upm.fi.dia.ontology.webode.translat.UML.ode.io.readers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.net.*;
import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.client.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.webode.service.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;
import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.*;

public class ODEModelReader {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private MinervaSession session=null;
  private ODEService     ode    =null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEModel readOntology(String modelName) throws ODEIOException {
    ODEOntologyReader reader=new ODEOntologyReader(ode);

    ODEModel model=reader.read(modelName);

    return model;
  }

  private void readConcepts(ODEModel model) throws ODEIOException {
    ODEConceptReader reader=new ODEConceptReader(ode);

    reader.read(model);
  }

  private void readSubclassOfRelations(ODEModel model) throws ODEIOException {
    ODESubclassOfRelationReader reader=new ODESubclassOfRelationReader(ode);

    reader.read(model);
  }

  private void readAdhocRelations(ODEModel model) throws ODEIOException {
    ODEAdhocRelationReader reader=new ODEAdhocRelationReader(ode);

    reader.read(model);
  }

  private void readMereologicalRelations(ODEModel model) throws ODEIOException {
    ODEMereologicalRelationReader reader=new ODEMereologicalRelationReader(ode);

    reader.read(model);
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEModelReader(ODEService ode) throws ODEIOException {
      this.ode=ode;
      this.session=null;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void close() throws ODEIOException {
  }

  public ODEModel read(String modelName) throws ODEIOException {

    Logger.initOperationLog("ODE Model Reading",
                            "Reading model "+modelName);

    ODEModel newModel=readOntology(modelName);

    readConcepts(newModel);
    readSubclassOfRelations(newModel);
    readAdhocRelations(newModel);
    readMereologicalRelations(newModel);

    Logger.finishOperationLog("ODE Model Reading");

    return newModel;
  }
}