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
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.*;

import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;

public class ODEConceptWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void write(ODEConcept concept) throws ODEIOException {
    Logger.initOperationLog("Concept writing",
                            "\""+concept.getName()+"\"");

    try {
      odeService.insertTerm(concept.getModel().getName(),
                            concept.getName(),
                            concept.getDescription(),
                            TermTypes.CONCEPT);

      writeClassAttributes(concept);
      writeInstanceAttributes(concept);
    } catch (ODEIOException ex) {
      throw ex;
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    }

    Logger.finishOperationLog("Concept writing");
  }

  private void writeClassAttributes(ODEConcept concept) throws ODEIOException {
    ODEClassAttributeWriter classAttributeWriter=new ODEClassAttributeWriter(odeService);
    classAttributeWriter.write(concept);
  }

  private void writeInstanceAttributes(ODEConcept concept) throws ODEIOException {
    ODEInstanceAttributeWriter instanceAttributeWriter=new ODEInstanceAttributeWriter(odeService);
    instanceAttributeWriter.read(concept);
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEConceptWriter(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(ODEModel model) throws ODEIOException {
    Vector updatedConcepts=new Vector();
    Logger.initOperationLog("ODE Concepts Writing",
                            "Writing model \""+model.getName()+"\" concepts...");

    ODEConcept[] concepts=model.getConcepts();
    if(concepts!=null) {
      for(int i=0;i<concepts.length;i++) {
        write(concepts[i]);
      }
    }
  }
}