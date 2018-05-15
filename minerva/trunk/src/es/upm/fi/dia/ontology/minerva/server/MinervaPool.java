package es.upm.fi.dia.ontology.minerva.server;

import es.upm.fi.dia.ontology.minerva.server.services.*;

import java.util.*;

/**
 * This class represents a pool of services of the same type.
 * <p>
 * It is intended to work closely related with the <tt>unreference</tt>
 * method in a <tt>MinervaServiceImp</tt> object.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.2
 * @see es.upm.fi.dia.ontology.minerva.server.services.MinervaServiceImp#disconnect
 * @see es.upm.fi.dia.ontology.minerva.server.services.MinervaServiceImp#setPool
 */
public class MinervaPool 
{
    private HashMap services;
    private String serviceName;

    /**
     * Constructor.
     *
     * @param services A hashmap containing services.
     * @param serviceName The service name to take care of.
     */
    public MinervaPool (HashMap services, String serviceName)
    {
	this.services = services;
	this.serviceName = serviceName;
    }
    

    /**
     * Adds the service to the pool.
     *
     * @param ms The service to be added.
     */
    public void add (MinervaService ms)
    {
	Vector v;
	synchronized (services) {
	    v = (Vector) services.get (serviceName);
	}

	//System.out.println (">>>>>>>>>" + serviceName + " V: " + v);
	synchronized (v) {
	    if (!v.contains (ms)) {
		v.add (ms);
		//v.notifyAll();
		
		//System.out.println ("POOL: " + v.size());
	    }
	}
    }
}
