package es.upm.fi.dia.ontology.minerva.server.services;

// Java stuff
import java.io.*;

// Own stuff
import es.upm.fi.dia.ontology.minerva.server.*;

import java.net.URLEncoder;

/**
 * This class provides the root for building more complex
 * configuration classes.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.3
 */
public class MinervaServiceConfiguration implements Serializable
{
    /**
     * The class loader to use when resolving configuration files.
     */
    public static ClassLoader cl;

    /**
     * Semaphore not to allow multiple writes at the same time.
     */
    private static Object sem = new Object[0];

    /**
     * Constant to indicate a service is STATELESS.
     */
    public static final byte STATELESS = 1;
    /**
     * Constant to indicate a service is STATEFUL.
     */
    public static final byte STATEFUL  = 2;

    /** Is this service local or can it be accessed remotely? */
    protected boolean local;

    /**
     * Type of the service (one of STATELESS or STATEFUL).
     * Default is stateless.
     */
    protected byte    type = STATELESS;

    private transient String directory, name;

    /**
     * Constructor.
     * Builds a configuration for a stateless service.
     *
     * @param local Says whether the service is local or can be
     *              accessed remotely.
     */
    public MinervaServiceConfiguration (boolean local)
    {
	this.local = local;
    }

    /**
     * Returns whether or not the service is stateful.
     */
    public boolean isStateful ()
    {
	return type == STATEFUL;
    }

    public void setStateless ()
    {
	type = STATELESS;
    }

    public void setStateful()
    {
	type = STATEFUL;
    }

    /**
     * Another constructor.
     * <p>
     * This takes into account the type of service.
     *
     * @param local Says whether the service is local or can be
     *              accessed remotely.
     * @param type  One of STATELESS or STATEFUL.
     */
    public MinervaServiceConfiguration (boolean local, byte type)
    {
	this.local = local;

	if (type != STATELESS && type != STATEFUL)
	    throw new RuntimeException ("Illegal value for the type parameter.");

	this.type = type;
    }

    /**
     * Sets the directory and filename.
     *
     * @param directory The directory to write the configuration file in.
     * @param name      The name of the file.
     */
    public void setDirectory (String directory, String name)
    {
	this.directory = directory;
	this.name = name;
    }

    /**
     * Method that loads a configuration object.
     *
     * @param dir  Directory where the configuration object resides in.
     * @param name Name of the service.
     * @exception NoServiceConfigurationException If no information about the service
     *            is available or it is incorrect.
     * @exception IOException If an IO error occurs.
     */
    public static MinervaServiceConfiguration loadConfig (String dir, String name)
	throws IOException, NoServiceConfigurationException
    {
	ObjectInputStream ois = null;

	try {
	    ois = new MinervaObjectInputStream
		(cl, new FileInputStream (new File (dir, URLEncoder.encode(name + MinervaServerConstants.EXT_CONFIG, "ISO-8859-1"))));

	    MinervaServiceConfiguration msc;
	    // Not to allow dirty reads.
	    synchronized (sem) {
		// Load configuration.
		msc = (MinervaServiceConfiguration) ois.readObject();
	    }

	    return msc;
	} catch (FileNotFoundException fnfe) {
	    throw new NoServiceConfigurationException
		("no configuration information available for service " + name + ".");
	} catch (ClassCastException cce) {
	    throw new NoServiceConfigurationException
		("incorrect configuration information for service " + name + ".");
	} catch (ClassNotFoundException cnfe) {
	    throw new NoServiceConfigurationException ("no configuration class found for service " + name + ".");
	} finally {
	    // Clean up
	    try {
		if (ois != null) ois.close();
	    } catch (Exception e) {}
	}
    }

    /**
     * Method that stores a configuration object.
     * <p>It provides a safe way to store
     * information since concurrency issues are dealt inside.
     *
     * @param dir  Directory where the configuration object resides in.
     * @param name Name of the service.
     * @param mc   The configuration object.
     * @exception IOException If an IO error occurs.
     */
    public static void storeConfig (String dir, String name, MinervaServiceConfiguration msc) throws IOException
    {
	ObjectOutputStream oos = null;

	try {
	    oos = new ObjectOutputStream
		(new FileOutputStream (new File (dir, URLEncoder.encode(name + MinervaServerConstants.EXT_CONFIG, "ISO-8859-1"))));

	    // Not to allow multiple concurrent writes.
	    synchronized (sem) {
		// Store configuration
		oos.writeObject (msc);
	    }
	} finally {
	    try {
		if (oos != null) oos.close();
	    } catch (Exception e) {}
	}
    }

    /**
     * Stores current configuration in the prevously set directory and file.
     * <p>It provides a safe way to store
     * information since concurrency issues are dealt inside.
     */
    public void storeConfig () throws IOException
    {
	storeConfig (directory, name, this);
    }
}


