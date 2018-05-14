package es.upm.fi.dia.ontology.webode.translat.UML;

import java.rmi.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class UMLServiceManager extends MinervaManagerImp {
  public static final String ODE_SERVICE = "ODE Service";

  public UMLServiceManager () throws RemoteException {};

  public MinervaServiceDescription getServiceDescription ()
  {
    return new MinervaServiceDescription
    ("UML export service", "Service used to translate ontologies into UML",
    new MinervaAttributeDescription[] {
      new MinervaAttributeDescription (ODE_SERVICE,
                                       "The ODE Service to be used for accessing ontologies",
                                       String.class,
                                       null,
                                       ((UMLServiceConfiguration) serviceConfig).odeService)
    });
  }

  public void setAttribute (String attrName, Object attrValue)
      throws NoSuchAttributeException, RemoteException
  {
    UMLServiceConfiguration desc= (UMLServiceConfiguration) serviceConfig;
    if (attrName.equals (ODE_SERVICE)) {
      desc.odeService = attrValue.toString();
    }
    else
      throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
  }
}