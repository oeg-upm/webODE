package es.upm.fi.dia.ontology.minerva.server.others;

import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * The manager for the thread pool service.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class ThreadPoolServiceManager extends MinervaManagerImp
{
    public static final String NUM_THREADS = "Number of threads";
    public static final String PRIORITY    = "Priority";
  
    public ThreadPoolServiceManager () throws RemoteException {}
    
    public MinervaServiceDescription getServiceDescription() 
    {
	return new MinervaServiceDescription 
	    ("Thread pool service", "Service used to hold a pool of threads and execute jobs.",
	     new MinervaAttributeDescription[] {
		new MinervaAttributeDescription (NUM_THREADS,
						 "The number of threads in the pool.",
						 Integer.class,
						 null,
						 new Integer 
						 (((ThreadPoolServiceConfiguration) serviceConfig).numThreads)),
		    new MinervaAttributeDescription (PRIORITY,
						     "The priority level for the threads in the pool.",
						     Integer.class,
						     null,
						     new Integer 
						     (((ThreadPoolServiceConfiguration) serviceConfig).priority))
		    
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
	ThreadPoolServiceConfiguration odesc = (ThreadPoolServiceConfiguration) serviceConfig;

	if (attrName.equals (NUM_THREADS)) {
	    odesc.numThreads = ((Integer) attrValue).intValue();
	}
	else if (attrName.equals (PRIORITY)) {
	    odesc.priority = ((Integer) attrValue).intValue();
	}
	else
	    throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
    }
}

 










