package es.upm.fi.dia.ontology.minerva.server.builtin;

// Own stuff
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;

import java.util.*;


/**
 * The configuration for the authentication service.
 *
 * @author  Julio César Arpírez Vega
 * @version 1.4
 */
public class AuthenticationServiceConfiguration extends MinervaServiceConfiguration 
    implements MinervaServerConstants
{
    /** Groups. */
    private Vector groups;
    /** Available services and their access control. */
    private HashMap services;
    /** Users (all) */
    private Vector users;
    
    public AuthenticationServiceConfiguration ()
    {
	this (MinervaUser.ADMIN_PASSWORD);
    }
    
    /**
     * Builds a default configuration with a sole administrator
     * with access to all the resources.
     */
    public AuthenticationServiceConfiguration (String password)
    {
	super (false);

	groups = new Vector();
	services = new HashMap();
	users = new Vector();

	// Create a default user named admin with the same password.
	MinervaGroup mg = new MinervaGroup (MinervaGroup.ADMIN_GROUP,
					    MinervaGroup.ADMIN_DESC);
	MinervaUser mu;
	mg.addUser (mu = new MinervaUser (MinervaUser.ADMIN_USER, password,
					  MinervaUser.ADMIN_DESC));
	groups.addElement (mg);

	// Add the user to the user database.
	users.addElement (mu);

	// Create service access
	MinervaServiceAccess msa;
	services.put (AUTHENTICATION_SERVICE,
		      msa = new MinervaServiceAccess (AUTHENTICATION_SERVICE));
	msa.grantAccess (mg);
	services.put (ADMINISTRATION_SERVICE, 
		      msa = new MinervaServiceAccess (ADMINISTRATION_SERVICE));
	msa.grantAccess (mg);
	services.put (LOG_SERVICE, 
		      msa = new MinervaServiceAccess (LOG_SERVICE));
	msa.grantAccess (mg);
    }

    /**
     * Returns the user descriptor or <tt>null</tt> if no matches are found.
     */
    public MinervaUser getUser (String login)
    {
	MinervaUser muaux = null;
	for (int i = 0; i < groups.size(); i++) {
	    muaux = (MinervaUser) ((MinervaGroup) groups.elementAt (i)).getUser (login);
	    if (muaux != null)
		return muaux;
	}
	synchronized (users) {
	    int index = users.indexOf (new MinervaUser (login, null, null));
	    if (index < 0)
		return null;
	    return (MinervaUser) users.elementAt (index);
	}
    }	

    /**
     * Checks if the user is allowed to access a given service.
     *
     * @param mu The user to check.
     * @param serviceName The service to check.
     * @return <tt>true</tt> if the user is allowed.  <tt>false</tt>
     *         otherwise.
     */
    public boolean hasAccess (MinervaUser mu, String serviceName)
    {
	synchronized (services) {
	    MinervaServiceAccess msa = (MinervaServiceAccess) services.get (serviceName);
	    
	    return msa != null && msa.hasAccess (mu.getGroups());
	}
    }

    /**
     * Gets all the users.
     */
    public MinervaUser[] getUsers ()
    {
	synchronized (users) {
	    MinervaUser[] amu = new MinervaUser[users.size()];
	    users.copyInto (amu);
	    return amu;
	}
    } 

    /**
     * Gets all the groups.
     */
    public MinervaGroup[] getGroups ()
    {
	synchronized (groups) {
	    MinervaGroup[] amg = new MinervaGroup[groups.size()];

	    groups.copyInto (amg);
	    return amg;
	}
    }

    /**
     * Gets a given group
     */
    public MinervaGroup getGroup (String name)
    {
	synchronized (groups) {
	    MinervaGroup foo = new MinervaGroup (name, null);
	    int index = groups.indexOf (foo);
	    if (index < 0)
		return null;
	    return (MinervaGroup) groups.elementAt (index);
	}
    }
    

    /**
     * Add a user.
     */
    public void addUser (MinervaUser mu)
    {
	synchronized (users) {
	    users.addElement (mu);
	    synchronized (groups) {
		for (int i = 0; i < groups.size(); i++)
		    ((MinervaGroup) groups.elementAt(i)).removeUser (mu);
	    }
	}
    }

    /**
     * Removes a user.
     */
    public void deleteUser (MinervaUser mu)
    {
	synchronized (users) {
	    users.removeElement (mu);
	}
    }	
    
    /**
     * Adds a gropu.
     */
    public void addGroup (MinervaGroup mg) throws MinervaException
    {
	synchronized (groups) {
	    if (groups.contains (mg))
		throw new MinervaException ("Group " + mg.getName() + " already exists.");
	    groups.addElement (mg);
	}
    }
    
    /**
     * Deletes a group.
     */
    public void deleteGroup (MinervaGroup mg)
    {
	synchronized (groups) {
	    groups.removeElement (mg);

	    synchronized (services) {
		Iterator it = services.values().iterator();
		// Delete group from access objects.
		while (it.hasNext()) {
		    ((MinervaServiceAccess) it.next()).denyAccess (mg);
		}
	    }	    
	}
    }	

    /**
     * Gets access information.
     */
    public MinervaServiceAccess[] getServiceAccess ()
    {
	synchronized (services) {
	    return (MinervaServiceAccess[]) services.values().toArray (new MinervaServiceAccess[0]);
	}
    }	    

    /**
     * Get access information for a particular service.
     */
    public MinervaServiceAccess getServiceAccess (String name) throws MinervaException
    {
	MinervaServiceAccess msa;
	synchronized (services) {
	    msa = (MinervaServiceAccess) services.get (name);
	}
	if (msa == null)
	    throw new MinervaException ("No such service (" + name + ")");
	
	return msa;
    }

    /**
     * Adds a service
     */
    public void addService (MinervaServiceAccess msa)
    {
	synchronized (services) {
	    services.put (msa.getName(), msa);
	}
    }

    /**
     * Adds a service
     */
    public void removeService (MinervaServiceAccess msa)
    {
	synchronized (services) {
	    services.remove (msa.getName());
	}
    }
}










