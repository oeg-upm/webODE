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
import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;
import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.*;

public class ODEMereologicalRelationReader {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEMereologicalRelation read(ODEModel     model,
                                       TermRelation descriptor) throws ODEException {
    ODEMereologicalRelation newRelation=null;

    boolean isTransitive=descriptor.name.equals(TermRelation.TRANSITIVE_PART_OF);

    Logger.initOperationLog("Reading mereological relation",
                            descriptor.name+"("+descriptor.origin+","+descriptor.destination+")");

    ODEConcept origin     =model.findConcept(descriptor.origin);
    ODEConcept destination=model.findConcept(descriptor.destination);


    newRelation=ODEComponentFactory.createMereologicalRelation(isTransitive,
                                                               origin,
                                                               destination);

    Logger.finishOperationLog("Reading mereological relation");

    return newRelation;
  }


  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEMereologicalRelationReader(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void read(ODEModel model) throws ODEIOException {
    TermRelation[] relations=null;
    Logger.initOperationLog("ODE Mereological Relations Reading",
                            "Reading model's mereological relations...");

    try {
      relations=odeService.getTermRelations(model.getName(), TermRelation.TRANSITIVE_PART_OF);

      if(relations!=null) {
        for(int i=0;i<relations.length;i++) {
          ODEMereologicalRelation newRelation=read(model,relations[i]);
          model.addMereologicalRelation(newRelation);
        }
      }

      relations=odeService.getTermRelations(model.getName(), TermRelation.INTRANSITIVE_PART_OF);

      if(relations!=null) {
        for(int i=0;i<relations.length;i++) {
          ODEMereologicalRelation newRelation=read(model,relations[i]);
          model.addMereologicalRelation(newRelation);
        }
      }
    } catch (ODEException    ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } finally {
      Logger.finishOperationLog("ODE Mereological Relations Reading");
    }
  }
}