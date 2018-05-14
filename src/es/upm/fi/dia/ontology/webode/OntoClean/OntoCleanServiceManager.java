package es.upm.fi.dia.ontology.webode.OntoClean;

import java.rmi.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;


public class OntoCleanServiceManager extends MinervaManagerImp
{
  public static final String ODE_SERVICE = "ODE Service";
  public static final String INFERENCE_SERVICE = "Inference Service";
  public static final String PROLOG_SERVICE = "Prolog Service";;
  
  public OntoCleanServiceManager() throws RemoteException {}; 
  
  public MinervaServiceDescription getServiceDescription ()
  {
    return new MinervaServiceDescription
	("OntoClean service", "Service used for evaluation using the OntoClean method",
	  new MinervaAttributeDescription[] {
		new MinervaAttributeDescription (ODE_SERVICE,
						"The service to be used for accessing ontologies",
						String.class,
						null,
						((OntoCleanServiceConfiguration) serviceConfig).odeService), 
		new MinervaAttributeDescription (PROLOG_SERVICE,
						"The service used to translate the ontologies to prolog",
						String.class,
						null,
						((OntoCleanServiceConfiguration) serviceConfig).prologService), 

		new MinervaAttributeDescription (INFERENCE_SERVICE,
						"The service used to make inferences",
						String.class,
						null,
						((OntoCleanServiceConfiguration) serviceConfig).inferenceService)

	});
   }

   public void setAttribute (String attrName, Object attrValue)
	throws NoSuchAttributeException, RemoteException
   {
    	OntoCleanServiceConfiguration desc= (OntoCleanServiceConfiguration) serviceConfig;
		if (attrName.equals (ODE_SERVICE)) {
			desc.odeService = attrValue.toString();
    	} else if(attrName.equals (PROLOG_SERVICE)) {
			desc.prologService = attrValue.toString();
    	} else if(attrName.equals (INFERENCE_SERVICE)) {
			desc.inferenceService = attrValue.toString();
    	} else 
		throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
    }
}
