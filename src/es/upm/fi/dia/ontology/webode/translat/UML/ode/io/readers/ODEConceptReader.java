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

public class ODEConceptReader {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEConcept read(ODEModel model,
                          Term     conceptTerm) throws ODEIOException {
    ODEConcept concept=ODEComponentFactory.createConcept(model,conceptTerm.term);

    Logger.initOperationLog("Concept reading",
                            "\""+conceptTerm.term+"\"");

    concept.setDescription(conceptTerm.des);

    readClassAttributes(concept);
    readInstanceAttributes(concept);

    Logger.finishOperationLog("Concept reading");

    return concept;
  }

  private void readClassAttributes(ODEConcept concept) throws ODEIOException {
    ODEClassAttributeReader classAttributeReader=new ODEClassAttributeReader(odeService);
    classAttributeReader.read(concept);
  }

  private void readInstanceAttributes(ODEConcept concept) throws ODEIOException {
    ODEInstanceAttributeReader instanceAttributeReader=new ODEInstanceAttributeReader(odeService);
    instanceAttributeReader.read(concept);
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEConceptReader(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void read(ODEModel model) throws ODEIOException {
    Vector updatedConcepts=new Vector();
    Logger.initOperationLog("ODE Concepts Reading",
                            "Reading model \""+model.getName()+"\" concepts...");
    try {
      Term[] terms=odeService.getTerms(model.getName(),
                                       new int[]{TermTypes.CONCEPT});
      if(terms!=null) {
        for(int i=0;i<terms.length;i++) {
          ODEConcept newConcept=read(model,terms[i]);
          model.addConcept(newConcept);
          updatedConcepts.add(newConcept);
        }
      }
    } catch (ODEException    ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } finally {
      Logger.finishOperationLog("ODE Concepts Reading");
    }
  }
}