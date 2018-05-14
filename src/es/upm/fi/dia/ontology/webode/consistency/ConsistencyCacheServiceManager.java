package es.upm.fi.dia.ontology.webode.consistency;

import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * The manager for the consistency cache module.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public class ConsistencyCacheServiceManager extends MinervaManagerImp
{
    public static final String DB_SERVICE = "Database service";
    public static final String SCHEDULER  = "Batch worker service";
    public static final String TIME       = "Time";
  
    public ConsistencyCacheServiceManager () throws RemoteException {}
    
    public MinervaServiceDescription getServiceDescription() 
    {
	return new MinervaServiceDescription 
	    ("Consistency cache service", "Service used to do consistency checks on data.",
	     new MinervaAttributeDescription[] {
		new MinervaAttributeDescription (DB_SERVICE,
						 "The database service to be used for accessing ontologies.",
						 String.class,
						 null,
						 ((ConsistencyCacheServiceConfiguration) serviceConfig).dbService),
		    new MinervaAttributeDescription (SCHEDULER,
						     "The batch worker service to be used for cache keeping.",
						     String.class,
						     null,
						     ((ConsistencyCacheServiceConfiguration) serviceConfig).scheduler),
		    new MinervaAttributeDescription (TIME,
						     "The time to carry out cache keeping.",
						     Integer.class,
						     null,
						     new Integer 
						     (((ConsistencyCacheServiceConfiguration) serviceConfig).time))
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
	ConsistencyCacheServiceConfiguration odesc = (ConsistencyCacheServiceConfiguration) serviceConfig;

	if (attrName.equals (DB_SERVICE)) {
	    odesc.dbService = attrValue.toString();
	}
	else if (attrName.equals (SCHEDULER)) {
	    odesc.scheduler = attrValue.toString();
	}
	else if (attrName.equals (TIME)) {
	    odesc.time = ((Integer) attrValue).intValue();
	}
	else
	    throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
    }
}

 










