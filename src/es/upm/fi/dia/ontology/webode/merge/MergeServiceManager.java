/*****************************************/
/* MergeServiceImp class *****************/
/*****************************************/
/* Author: J.A.R.G. **********************/
/*****************************************/
package es.upm.fi.dia.ontology.webode.merge;

import java.rmi.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;


public class MergeServiceManager extends MinervaManagerImp
{
   public static final String ODE_SERVICE = "ODE Service"; 

   public MergeServiceManager() throws RemoteException {};

   public MinervaServiceDescription getServiceDescription()
   {
      return new MinervaServiceDescription("Merge service", "Service used to merge ontologies",
       new MinervaAttributeDescription[]
       {
          new MinervaAttributeDescription(ODE_SERVICE, "The ODEService to be used for accessing ontologies",
           String.class, null, ((MergeServiceConfiguration) serviceConfig).odeService)
       }
       );
   }

  public void setAttribute (String attrName, Object attrValue)
	throws NoSuchAttributeException, RemoteException
  {
    MergeServiceConfiguration desc= (MergeServiceConfiguration) serviceConfig;
    if (attrName.equals (ODE_SERVICE)) {
	desc.odeService = attrValue.toString();
    }
    else
	throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
    }
}
