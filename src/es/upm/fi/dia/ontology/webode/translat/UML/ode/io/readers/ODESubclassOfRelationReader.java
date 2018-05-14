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

public class ODESubclassOfRelationReader {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODESubclassOfRelation read(ODEModel     model,
                                     TermRelation descriptor) {
    ODESubclassOfRelation newRelation=null;

    Logger.initOperationLog("Reading subclass-of relation",
                            descriptor.name+"("+descriptor.origin+","+descriptor.destination+")");

    ODEConcept origin     =model.findConcept(descriptor.origin);
    ODEConcept destination=model.findConcept(descriptor.destination);

    newRelation=ODEComponentFactory.createSubclassOfRelation(origin,destination);

    Logger.finishOperationLog("Reading subclass-of relation");

    return newRelation;
  }

  private ODESubclassOfRelation[] readGroup(ODEModel     model,
                                     TermRelation descriptor) throws WebODEException, RemoteException {
    ODESubclassOfRelation[] newRelations=null;

    Logger.initOperationLog("Reading subclass-of relation",
                            descriptor.name+"("+descriptor.origin+","+descriptor.destination+")");

    Group group=odeService.getGroup(model.getName(),descriptor.origin);

    if(group!=null) {
      ODEConcept destination=model.findConcept(descriptor.destination);

      newRelations=new ODESubclassOfRelation[group.concepts.length];

      for(int i=0; i<newRelations.length; i++) {

        ODEConcept origin=model.findConcept(group.concepts[i]);

        newRelations[i]=ODEComponentFactory.createSubclassOfRelation(origin,destination);
      }
    }


    Logger.finishOperationLog("Reading subclass-of relation");

    return newRelations;
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODESubclassOfRelationReader(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void read(ODEModel model) throws ODEIOException {
    TermRelation[] relations       =null;
    Logger.initOperationLog("ODE Subclass-Of Relations Reading",
                            "Reading model \""+model.getName()+"\" subclass-of relations...");
    try {
      relations=odeService.getTermRelations(model.getName(),true);

      if(relations!=null) {
        for(int i=0;i<relations.length;i++) {
          ODESubclassOfRelation newRelation=read(model,relations[i]);
          model.addSubclassOfRelation(newRelation);
        }
      }

      relations=odeService.getTermRelations(model.getName(),TermRelation.EXHAUSTIVE);
      if(relations!=null) {
        for(int i=0;i<relations.length;i++) {
          ODESubclassOfRelation[] newRelations=readGroup(model,relations[i]);
          if(newRelations!=null)
            for(int j=0; j<newRelations.length; j++)
              model.addSubclassOfRelation(newRelations[j]);
        }
      }

      relations=odeService.getTermRelations(model.getName(),TermRelation.DISJOINT);
      if(relations!=null) {
        for(int i=0;i<relations.length;i++) {
          ODESubclassOfRelation[] newRelations=readGroup(model,relations[i]);
          if(newRelations!=null)
            for(int j=0; j<newRelations.length; j++)
              model.addSubclassOfRelation(newRelations[j]);
        }
      }

    } catch (ODEException    ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } finally {
      Logger.finishOperationLog("ODE Subclass-Of Relations Reading");
    }
  }
}