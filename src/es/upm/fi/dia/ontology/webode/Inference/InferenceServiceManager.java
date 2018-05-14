package es.upm.fi.dia.ontology.webode.Inference;


import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.management.*;


public class InferenceServiceManager extends MinervaManagerImp
{
// public static final String DB_SERVICE = "Database Service";
  public static final String PROLOG_SERVICE = "Prolog Service";
  public static final String CONNECTIONS = "Connections";
  public static final String PROLOG_PATH = "Prolog path";

  public InferenceServiceManager () throws RemoteException {};

  public MinervaServiceDescription getServiceDescription ()
  {
    return new MinervaServiceDescription
    ("Inference service", "Service used to Inference",
    new MinervaAttributeDescription[] {/*
  new MinervaAttributeDescription (DB_SERVICE,
      "The database to be used for accessing ontologies",
      String.class,
      null,
      ((InferenceServiceConfiguration) serviceConfig).dbService),*/
      new MinervaAttributeDescription (PROLOG_SERVICE,
                                       "The service used to traslate the ontologies to prolog",
                                       String.class,
                                       null,
                                       ((InferenceServiceConfiguration) serviceConfig).prologService),

      new MinervaAttributeDescription (CONNECTIONS,
      "Number of connections",
      String.class,
      null,
      ((InferenceServiceConfiguration) serviceConfig).connections),
          new MinervaAttributeDescription (PROLOG_PATH,
          "Prolog path",
          String.class,
          null,
          ((InferenceServiceConfiguration) serviceConfig).prologPath)

    });
  }

  public void setAttribute (String attrName, Object attrValue)
      throws NoSuchAttributeException, RemoteException
  {
    InferenceServiceConfiguration desc= (InferenceServiceConfiguration) serviceConfig;
 /*
            if (attrName.equals (DB_SERVICE)) {
  desc.dbService = attrValue.toString();

     } else
       */
    if(attrName.equals (PROLOG_SERVICE)) {
      desc.prologService = attrValue.toString();

    } else if(attrName.equals (CONNECTIONS)) {
      desc.connections = attrValue.toString();

    } else if(attrName.equals (PROLOG_PATH)) {
      desc.prologPath = attrValue.toString();

    } else
      throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
  }
}