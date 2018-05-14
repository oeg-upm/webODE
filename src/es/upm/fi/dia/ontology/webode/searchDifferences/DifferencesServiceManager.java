package es.upm.fi.dia.ontology.webode.searchDifferences;

import es.upm.fi.dia.ontology.minerva.server.management.MinervaManagerImp;
import java.rmi.RemoteException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import es.upm.fi.dia.ontology.minerva.server.management.*;

public class DifferencesServiceManager extends MinervaManagerImp {
  public static final String ODE_SERVICE = "ODE Service";

  public DifferencesServiceManager () throws RemoteException {};

  public MinervaServiceDescription getServiceDescription ()
  {
    return new MinervaServiceDescription
        ("Search Differences Service", "Service used to search differences between two ontologies",
          new MinervaAttributeDescription[] {
                new MinervaAttributeDescription (ODE_SERVICE,
                                                "The ODE Service to be used for accessing ontologies",
                                                String.class,
                                                null,
                                                ((DifferencesServiceConfiguration) serviceConfig).odeServiceName)
        });
  }

  public void setAttribute (String attrName, Object attrValue)
        throws NoSuchAttributeException, RemoteException
  {
    DifferencesServiceConfiguration desc= (DifferencesServiceConfiguration) serviceConfig;
    if (attrName.equals (ODE_SERVICE)) {
      desc.odeServiceName = attrValue.toString();
    }
    else
      throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
  }
}


