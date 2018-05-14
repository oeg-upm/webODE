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

public class ODEAdhocRelationWriter {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private ODEService odeService=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void write(ODEAdhocRelation relation) throws ODEIOException {
    Logger.initOperationLog("Writing adhoc relation",
                            relation.getName()+"("+relation.getOrigin().getName()+","+relation.getDestination().getName()+")");

    TermRelation descriptor=
        new TermRelation(relation.getModel().getName(),
                         null,
                         relation.getName(),
                         relation.getOrigin().getName(),
                         relation.getDestination().getName(),0);

    descriptor.maxCardinality=(relation.allowsMultipleInstances()?-1:0);
    if(relation.getProperties()!=null)
      descriptor.properties=relation.getProperties();

    // Falta escritura user defined properties
    // Inserción de términos tipo TermType.PROPERTY con el nombre dado
    // Ver JSP InsertInheritanceRelation.jsp

    try {
      odeService.insertTermRelation(descriptor);

      TermRelation[] relations=
          odeService.getTermRelations(relation.getModel().getName(),
                                      TermRelation.ADHOC);
      if(relations!=null) {
        String actualName=null;
        for(int i=0;i<relations.length&&actualName==null;i++) {
          if(relations[i].name.equals(relation.getName())) {
            actualName=relations[i].actualName;
          }
        }
        if(actualName!=null) {
          Term term=odeService.getTerm(relation.getModel().getName(),actualName);
          term.des=relation.getDescription();
          odeService.updateTerm(actualName,term);
        }
      }
    } catch (WebODEException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    } catch (RemoteException ex) {
      throw new ODEIOException(ex.getMessage(),getClass(), ex);
    }

    Logger.finishOperationLog("Writing adhoc relation");
  }


  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEAdhocRelationWriter(ODEService odeService) {
    this.odeService=odeService;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public void write(ODEModel model) throws ODEIOException {
    Logger.initOperationLog("ODE Adhoc Relations Writing",
                            "Writing model \""+model.getName()+"\" adhoc relations...");
    ODEAdhocRelation[] relations=model.getAdhocRelations();

    if(relations!=null) {
      for(int i=0;i<relations.length;i++) {
        write(relations[i]);
      }
    }

    Logger.finishOperationLog("ODE Adhoc Relations Writing");
  }
}
