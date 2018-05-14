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
import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;
import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.*;

public class ODEMereologicalRelationWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void write(ODEMereologicalRelation relation) throws ODEIOException {
    Logger.initOperationLog("Writing mereological relation",
                            relation.getName()+"("+relation.getOrigin().getName()+","+relation.getDestination().getName()+")");

    TermRelation descriptor=
        new TermRelation(relation.getModel().getName(),
                         null,
                         relation.getName(),
                         relation.getOrigin().getName(),
                         relation.getDestination().getName(),
                         0);

    try {
      odeService.insertTermRelation(descriptor);
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    }

    Logger.finishOperationLog("Writing mereological relation");
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEMereologicalRelationWriter(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(ODEModel model) throws ODEIOException {
    Logger.initOperationLog("ODE Mereological Relations Writing",
                            "Writing model's mereological relations...");

    ODEMereologicalRelation[] relations=model.getMereologicalRelations();

    if(relations!=null) {
      for(int i=0;i<relations.length;i++) {
        write(relations[i]);
      }
    }

    Logger.finishOperationLog("ODE Mereological Relations Writing");
  }
}