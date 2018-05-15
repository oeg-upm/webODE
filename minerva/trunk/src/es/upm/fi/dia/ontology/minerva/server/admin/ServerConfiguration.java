package es.upm.fi.dia.ontology.minerva.server.admin;

import java.util.*;
import java.io.*;

/**
 * Class that encapsulates the server configuration.
 * <b>Convert this into a Map</b><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
 * 
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class ServerConfiguration implements java.io.Serializable
{
    /** An "array" of service descriptors. */
    private HashMap services;

    /** Server's Classpath */
    private String classpath;

    /** A convenience variable to hold temporal order. */
    private LinkedList lServices;

    /**
     * Creates an empty server configuration.
     */
    public ServerConfiguration ()
    {
	services = new HashMap();
	lServices = new LinkedList();
    }

    /**
     * Add a service.
     *
     * @param service The new service to be added.
     */
    public synchronized void addService (ServiceDescriptor sd)
    {
	services.put (sd.name, sd);
	lServices.add (sd);
    }

    /**
     * Remove a service.
     *
     * @param service The service to be removed.
     */
    public void removeService (ServiceDescriptor sd)
    {
	services.remove (sd.name);
	lServices.remove (sd);
    }

    /**
     * Enumerate all services available.
     * <p>
     * <b>Warning:</b> this method cannot be invoked more than once
     * at the same time once the enumeration has been started to
     * be transversed.
     *
     * @return An iterator containing all the services.
     */
    public Iterator getServices ()
    {
	return lServices.listIterator (0);
	//return services.values().iterator();
    }

    /**
     * Gets a service descriptor.
     *
     * @param name The service name.
     * @return The server descriptor related to the name.  <tt>null</tt> if it is not present.
     */
    public ServiceDescriptor getService (String name)
    {
	return (ServiceDescriptor) services.get (name);
    }

    /**
     * Reads the object from persistent storage.
     *
     * @param file The file name the object is stored in.
     * @exception Exception In case of error.
     */
    public static ServerConfiguration loadServerConfiguration (String file)
	throws Exception
    {
	ObjectInputStream ois = null;
	try {
	    ois = new ObjectInputStream 
		(new FileInputStream (file));
	    
	    return (ServerConfiguration) ois.readObject();
	} finally {
	    try {
		ois.close();
	    } catch (Exception e) {}
	}
    }
    

    /**
     * Store in persistent storage.
     *
     * @param file The file to store the object in.
     * @exception Exception In case of error.
     */
    public void storeServerConfiguration (String file)
	throws Exception
    {
	ObjectOutputStream oos = null;
	try {
	    oos = new ObjectOutputStream (new FileOutputStream (file));

	    oos.writeObject (this);
	} finally {
	    try {
		oos.close();
	    } catch (Exception e) {}
	}
    }

    /**
     * Sets the classpath.
     * 
     * @param the new class path.
     */
    public void setClassPath (String classpath)
    {
	this.classpath = classpath;
    }

    /**
     * Gets current server classpath.
     *
     * @return Current classpath.
     */
    public String getClassPath ()
    {
	return this.classpath;
    }
}

