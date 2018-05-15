package es.upm.fi.dia.ontology.minerva.server.others;

// Java stuff
import java.util.*;
import java.io.*;
import java.rmi.*;

// own stuff
import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * Manager for the backup service.
 */
public class BackupServiceManager extends MinervaManagerImp
{
    public static final String URL         = "URL";
    public static final String SCHEDULER   = "Batch Worker Service";
    public static final String TIME        = "Period";
    public static final String SOURCE      = "Source Service";


    public BackupServiceManager () throws RemoteException {}
    
    public MinervaServiceDescription getServiceDescription() 
    {
	return new MinervaServiceDescription 
	    ("Backup service", "Service used to carry out backups.",
	     new MinervaAttributeDescription[] {
		new MinervaAttributeDescription (URL,
						 "The url to be used as the root for the backup.",
						 String.class,
						 null,
						 ((BackupServiceConfiguration) serviceConfig).destinationURL),
		    new MinervaAttributeDescription (SCHEDULER,
						     "The name of the scheduler service.",
						     String.class,
						     null,
						     ((BackupServiceConfiguration) serviceConfig).scheduler),
		    new MinervaAttributeDescription (TIME,
						     "The time between two backups.",
						     Integer.class,
						     null,
						     new Integer 
						     (((BackupServiceConfiguration) serviceConfig).time)),
		    new MinervaAttributeDescription (SOURCE,
						     "The source for the back-ups.",
						     String.class,
						     null,
						     ((BackupServiceConfiguration) serviceConfig).source)
		    
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
	BackupServiceConfiguration bsc = (BackupServiceConfiguration) serviceConfig;

	if (attrName.equals (URL)) {
	    try {
		bsc.destinationURL = new java.net.URL (attrValue.toString());
	    } catch (Exception e) {
		throw new NoSuchAttributeException ("Error in URL parameter:" + e.getMessage());
	    }
	}
	else if (attrName.equals (SCHEDULER)) {
	    bsc.scheduler = attrValue.toString();
	}
	else if (attrName.equals (TIME)) {
	    bsc.time = ((Integer) attrValue).intValue();
	}
	else if (attrName.equals (SOURCE)) {
	    bsc.source = attrValue.toString();
	}
	else
	    throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
    }
}
