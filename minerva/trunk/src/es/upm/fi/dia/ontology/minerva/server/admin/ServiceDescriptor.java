package es.upm.fi.dia.ontology.minerva.server.admin;


import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * Descriptor for a service.
 * <p>
 * Shows relevant information about a server.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.2
 */
public class ServiceDescriptor implements java.io.Serializable
{
    /**
     * State: STARTED.
     */
    public static final int STARTED = 1;

    /**
     * State: STOPPED.
     */
    public static final int STOPPED = 2;

    /**
     * The name of the service.
     */
    public String name;

    /**
     * The class name of the service.
     */
    public String className;

    /**
     * The service interface.
     */
    public String interfaceName;

    /**
     * The name of the jar file (if any).
     */
    public String jarFile;

    /**
     * State
     */
    public byte type;

    /**
     * Can the service be accessed remotely?
     */
    public boolean bRemote;

    /**
     * Number of initial instances.
     */
    public int nInstances;

    /**
     * State of the service (started, stopped).  Defaults to STOPPED.
     */
    public transient int state = STOPPED;

    /**
     * Constructor for a service descriptor for a stateless service
     * an that cannot be accessed remotely.
     *
     * @param name The service name.
     * @param className The service class.
     * @param interfaceName The service interface.
     */
    public ServiceDescriptor (String name, String className, String interfaceName)
    {
	this (name, className, interfaceName, null, MinervaServiceConfiguration.STATELESS, false, 1);
    }

    /**
     * Constructor.
     *
     * @param name The service name.
     * @param className The service class.
     * @param interfaceName The service interface.
     * @param jarFile The jar file the service is stored in.
     * @param type The type of the service (stateless or stateful);
     * @param bRemote If <tt>true</tt> indicates the service can be accesed remotely.  
     *                Otherwise, it is not possible.
     * @param nInstances Number of initial instances.
     */
    public ServiceDescriptor (String name, String className, String interfaceName, String jarFile,
			      byte type, boolean bRemote, int nInstances)
    {
	this.type = type;
	this.name = name;
	this.className = className;
	this.interfaceName = interfaceName;
	this.jarFile = jarFile;
	this.bRemote = bRemote;
	this.nInstances = nInstances < 1 ? 1 : nInstances;
    }

    /**
     * Checks the state of the service.
     *
     * @return <tt>true</tt> if the service is stopped. <tt>false</tt>
     *         otherwise.
     */
    public boolean isStopped()
    {
	return state == STOPPED;
    }

    public boolean equals (Object o)
    {
	if (o instanceof ServiceDescriptor) {
	    ServiceDescriptor sd = (ServiceDescriptor) o;
	    return sd.name.equals (name);
	}
	return false;
    }
}



