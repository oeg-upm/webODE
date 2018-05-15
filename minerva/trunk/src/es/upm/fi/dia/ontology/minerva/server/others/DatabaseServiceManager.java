package es.upm.fi.dia.ontology.minerva.server.others;

// Java stuff
import java.util.*;
import java.io.*;
import java.rmi.*;

// own stuff
import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * Manager for the database service.
 */
public class DatabaseServiceManager extends MinervaManagerImp
{
    public static final String URL         = "URL";
    public static final String USERNAME    = "USERNAME";
    public static final String PASSWORD    = "PASSWORD";
    public static final String CONNECTIONS = "CONNECTIONS";
    public static final String DRIVER      = "DRIVER";
    public static final String MAX_USAGE   = "MAXIMUM USAGE NUMBER";

    public DatabaseServiceManager () throws RemoteException {}
    
    public MinervaServiceDescription getServiceDescription() 
    {
	return new MinervaServiceDescription 
	    ("Database service", "Service used to access database sources.",
	     new MinervaAttributeDescription[] {
		new MinervaAttributeDescription (URL,
						 "The database url",
						 String.class,
						 null,
						 ((DatabaseServiceConfiguration) serviceConfig).URL),
		    new MinervaAttributeDescription (USERNAME,
						     "The username to connect to the database.",
						     String.class,
						     null,
						     ((DatabaseServiceConfiguration) serviceConfig).username),
		    new MinervaAttributeDescription (PASSWORD,
						     "The password used to connect to the database.",
						     String.class,
						     null,
						     ((DatabaseServiceConfiguration) serviceConfig).password),
		    new MinervaAttributeDescription (DRIVER,
						     "The driver used to connect to the database.",
						     String.class,
						     null,
						     ((DatabaseServiceConfiguration) serviceConfig).driver),
		    new MinervaAttributeDescription (CONNECTIONS,
						     "The number of database connections to store in the pool.",
						     Integer.class,
						     null,
						     new Integer 
						     (((DatabaseServiceConfiguration) serviceConfig).connections)),
		    new MinervaAttributeDescription (MAX_USAGE,
						     "The maximum number of usages a database connection will support.",
						     Integer.class,
						     null,
						     new Integer 
						     (((DatabaseServiceConfiguration) serviceConfig).maxUsages))

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
	DatabaseServiceConfiguration dsc = (DatabaseServiceConfiguration) serviceConfig;

	if (attrName.equals (URL)) {
	    dsc.URL = attrValue.toString();
	}
	else if (attrName.equals (PASSWORD)) {
	    dsc.password = attrValue.toString();
	}
	else if (attrName.equals (USERNAME)) {
	    dsc.username = attrValue.toString();
	}
	else if (attrName.equals (CONNECTIONS)) {
	    dsc.connections = ((Integer) attrValue).intValue();
	}
	else if (attrName.equals (DRIVER)) {
	    dsc.driver = attrValue.toString();
	}
	else if (attrName.equals (MAX_USAGE)) {
	    dsc.maxUsages = ((Integer) attrValue).intValue();
	}
	else
	    throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
    }
}
