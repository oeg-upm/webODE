package es.upm.fi.dia.ontology.webode.translat.UML.ode.io.writers;

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

import es.upm.fi.dia.ontology.webode.service.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;
import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.*;

public class ODEModelWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService     ode       =null;
  private String         user      =null;
  private String         userGroup =null;


  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void writeOntology(ODEModel model) throws ODEIOException {
    ODEOntologyWriter writer=new ODEOntologyWriter(ode);

    try {
      model.setOwner(this.user);
      model.setGroupName(this.userGroup);

      writer.write(model);
    } catch (ODEIOException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    }
  }

  private void writeConcepts(ODEModel model) throws ODEIOException {
    ODEConceptWriter writer=new ODEConceptWriter(ode);

    writer.write(model);
  }

  private void writeSubclassOfRelations(ODEModel model) throws ODEIOException {
    ODESubclassOfRelationWriter writer=new ODESubclassOfRelationWriter(ode);

    writer.write(model);
  }

  private void writeAdhocRelations(ODEModel model) throws ODEIOException {
    ODEAdhocRelationWriter writer=new ODEAdhocRelationWriter(ode);

    writer.write(model);
  }

  private void writeMereologicalRelations(ODEModel model) throws ODEIOException {
    ODEMereologicalRelationWriter writer=new ODEMereologicalRelationWriter(ode);

    writer.write(model);
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEModelWriter(ODEService ode, String user, String userGroup) throws ODEIOException {
    this.ode=ode;
    this.user=user;
    this.userGroup=userGroup;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void close() throws ODEIOException {
  }

  public void write(ODEModel model) throws ODEIOException {
    Logger.initOperationLog("ODE Model Writing",
                            "Writing model "+model.getName());

    writeOntology(model);
    writeConcepts(model);
    writeSubclassOfRelations(model);
    writeAdhocRelations(model);
    writeMereologicalRelations(model);

    Logger.finishOperationLog("ODE Model Writing");
  }
}