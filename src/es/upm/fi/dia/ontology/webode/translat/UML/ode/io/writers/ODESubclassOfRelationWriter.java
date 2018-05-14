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

public class ODESubclassOfRelationWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void read(ODESubclassOfRelation relation) throws ODEIOException {

    ODESubclassOfRelation newRelation=null;

    Logger.initOperationLog("Writing subclass-of relation",
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

    Logger.finishOperationLog("Writing subclass-of relation");

  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODESubclassOfRelationWriter(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(ODEModel model) throws ODEIOException {
    Logger.initOperationLog("ODE Subclass-Of Relations Writing",
                            "Writing model \""+model.getName()+"\" subclass-of relations...");

    ODESubclassOfRelation[] relations=model.getSubclassOfRelations();

    if(relations!=null) {
      for(int i=0;i<relations.length;i++) {
        read(relations[i]);
      }
    }

    Logger.finishOperationLog("ODE Subclass-Of Relations Writing");
  }
}