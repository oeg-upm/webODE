package es.upm.fi.dia.ontology.webode.service;

import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * The manager for the ODE service.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public class ODEServiceManager extends MinervaManagerImp
{
  public static final String DB_SERVICE    = "Database service";
  public static final String CACHE_SERVICE = "Cache service";
  public static final String TRACE = "Trace events";

  public ODEServiceManager () throws RemoteException {}

  public MinervaServiceDescription getServiceDescription() {
    return new MinervaServiceDescription
    ("ODE service", "Service used to access ontologies.",
    new MinervaAttributeDescription[] {
      new MinervaAttributeDescription (DB_SERVICE,
                                       "The database service to be used for accessing ontologies.",
                                       String.class,
                                       null,
                                       ((ODEServiceConfiguration) serviceConfig).dbService),
      new MinervaAttributeDescription (CACHE_SERVICE,
                                       "The cache service to be used for accessing ontologies.",
                                       String.class,
                                       null,
                                       ((ODEServiceConfiguration) serviceConfig).cacheService),
      new MinervaAttributeDescription (TRACE,
                                       "Monitor all calls to ODEService.",
                                       boolean.class,
                                       null,
                                       ((ODEServiceConfiguration) serviceConfig).trace)
    });
  }

  /**
   * This method is used if no UI has been defined over the service.
   *
   * @param attrName  Name of the configurable attribute.
   * @param attrValue The attribute's value.
   */
  public void setAttribute (String attrName, Object attrValue)
      throws NoSuchAttributeException, RemoteException {
    ODEServiceConfiguration odesc = (ODEServiceConfiguration) serviceConfig;

    if (attrName.equals (DB_SERVICE)) {
      odesc.dbService = attrValue.toString();
    }
    else if (attrName.equals (CACHE_SERVICE)) {
      odesc.cacheService = attrValue.toString();
    }
    else if (attrName.equals (TRACE)) {
      odesc.trace = new Boolean(attrValue.toString());
    }
    else
      throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
  }
}











