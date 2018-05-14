package es.upm.fi.dia.ontology.webode.Axiom;

import es.upm.fi.dia.ontology.minerva.server.services.*;

public interface AxiomService extends MinervaService
{
  Result parseAxiom(String ontology, String axiom) throws java.rmi.RemoteException;
  Result parseRule(String ontology, String rule) throws java.rmi.RemoteException;
}