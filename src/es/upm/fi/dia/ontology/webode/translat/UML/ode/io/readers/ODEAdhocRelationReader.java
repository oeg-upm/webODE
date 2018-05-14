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

public class ODEAdhocRelationReader {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEAdhocRelation read(ODEModel     model,
                                TermRelation descriptor) throws ODEException {
    ODEAdhocRelation newRelation=null;

    Logger.initOperationLog("Reading adhoc relation",
                            descriptor.name+"("+descriptor.origin+","+descriptor.destination+")");

    ODEConcept origin     =model.findConcept(descriptor.origin);
    ODEConcept destination=model.findConcept(descriptor.destination);

    newRelation=ODEComponentFactory.createAdhocRelation(descriptor.name,
                                                        origin,
                                                        destination);

    // Lectura descripción
    try {
/*
      Term[] ts=odeService.getTerms(model.getName(),new int[]{TermTypes.RELATION});
      if(ts!=null) {
        Term t=null;
        for(int i=0;i<ts.length&&t==null;i++) {
          System.out.println(ts[i].term+":"+ts[i].des);
          if(ts[i].term.equals(descriptor.actualName))
            t=ts[i];
        }
*/
        Term t=odeService.getTerm(model.getName(),descriptor.actualName);
        if(t!=null)
          newRelation.setDescription(t.des);
/*
      }
*/
    } catch (RemoteException ex) {
    } catch (WebODEException ex) {
    }

    newRelation.setMultipleInstanceAllowance(descriptor.maxCardinality<0);
    if(descriptor.properties!=null)
      newRelation.setProperties(descriptor.properties);

    // Falta lectura de las propiedades de la relación:
    // * User defined
    // Ver JSP InsertInheritanceRelation.jsp
    // Ver SERVLET  UpdateTerm.java

    Logger.finishOperationLog("Reading adhoc relation");

    return newRelation;
  }


  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEAdhocRelationReader(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void read(ODEModel model) throws ODEIOException {
    TermRelation[] relations       =null;
    Logger.initOperationLog("ODE Adhoc Relations Reading",
                            "Reading model \""+model.getName()+"\" adhoc relations...");
    try {
      relations=odeService.getTermRelations(model.getName(), TermRelation.ADHOC);

      if(relations!=null) {
        for(int i=0;i<relations.length;i++) {
          ODEAdhocRelation newRelation=read(model,relations[i]);
          model.addAdhocRelation(newRelation);
        }
      }
    } catch (ODEException    ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } finally {
      Logger.finishOperationLog("ODE Adhoc Relations Reading");
    }
  }
}
