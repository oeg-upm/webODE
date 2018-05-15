package es.upm.fi.dia.ontology.minerva.server.others;

import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * The manager for the batch work service.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class BatchWorkerServiceManager extends MinervaManagerImp
{
    public static final String THREAD_POOL = "Thread pool";
    public static final String PRIORITY    = "Priority";
  
    public BatchWorkerServiceManager () throws RemoteException {}
    
    public MinervaServiceDescription getServiceDescription() 
    {
	return new MinervaServiceDescription 
	    ("Thread pool service", "Service used to hold a pool of threads and execute jobs.",
	     new MinervaAttributeDescription[] {
		new MinervaAttributeDescription (THREAD_POOL,
						 "The thread pool used by the scheduler.",
						 String.class,
						 null,
						 ((BatchWorkerServiceConfiguration) serviceConfig).threadPool)		    
		    });
    }
    
    /**
     * This method is used if no UI has been defined over the service.
     *
     * @param attrName  Name of the configurable attribute.
     * @param attrValue The attribute's value.
     */
    public void setAttribute (String attrName, Object attrValue) 
	throws NoSuchAttributeException, RemoteException
    {
	BatchWorkerServiceConfiguration odesc = (BatchWorkerServiceConfiguration) serviceConfig;

	if (attrName.equals (THREAD_POOL)) {
	    odesc.threadPool = attrValue.toString();
	}
	else
	    throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
    }
}

 










