package es.upm.fi.dia.ontology.webode.OntoClean;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import java.rmi.*;
import es.upm.fi.dia.ontology.webode.service.*;



public interface OntoCleanService extends MinervaService
{
  void createOntology (OntologyDescriptor od) throws RemoteException, WebODEException;

  void initializeOntology (String user, String ontologyName)  throws WebODEException, RemoteException;

  void removeOntology (String user, String ontologyName)  throws RemoteException, WebODEException;

  void insertTerm (String ontology, String term,
                   String desc, int type,int anti_unity,int is_dependent,
                   int carries_unity,int anti_rigid,int carries_identity,
                   int supplies_identity,int rigid)
      throws RemoteException, WebODEException;

  void updateTerm (String originalName, String parent, Term td,
                   int anti_unity,int is_dependent,int carries_unity,
                   int anti_rigid,int carries_identity,int supplies_identity,int rigid)
      throws RemoteException, WebODEException;

  void removeTerm (String ontology, String term, String parent, String relatedTerm)
      throws RemoteException, WebODEException;

  ErrorOntoClean [] evaluationOntoClean (String ontology, String user, String id)
      throws RemoteException, WebODEException;

  void changeEvaluationMode () throws RemoteException, WebODEException;

  String getEvaluationMode () throws RemoteException, WebODEException;

  void putEvaluationMode (int mode) throws RemoteException, WebODEException;

  void insertMetaproperties(String ontology,String name, String mp)
      throws RemoteException, WebODEException;

  String [] getStringValues (String ontology) throws RemoteException, WebODEException;

}