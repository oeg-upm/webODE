package es.upm.fi.dia.ontology.webode.Axiom;


import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.management.*;


public class AxiomServiceManager extends MinervaManagerImp
{
  public static final String ODE_SERVICE = "ODE Service";

  public AxiomServiceManager () throws RemoteException {};

  public MinervaServiceDescription getServiceDescription ()
  {
    return new MinervaServiceDescription
    ("Axiom service", "Service used to parse Axioms",
    new MinervaAttributeDescription[] {
      new MinervaAttributeDescription (ODE_SERVICE,
                                       "The ODE service to be used for accessing ontologies",
                                       String.class,
                                       null,
                                       ((AxiomServiceConfiguration) serviceConfig).odeService)
    });
  }

  public void setAttribute (String attrName, Object attrValue)
      throws NoSuchAttributeException, RemoteException {
    AxiomServiceConfiguration desc= (AxiomServiceConfiguration) serviceConfig;
    if (attrName.equals (ODE_SERVICE)) {
      desc.odeService = attrValue.toString();
    }
    else
      throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
  }
}