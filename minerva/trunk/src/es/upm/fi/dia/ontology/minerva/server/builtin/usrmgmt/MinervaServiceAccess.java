package es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt;

import java.util.*;

/**
 * Class to encapsulate the access information about a Minerva service.
 * <p>
 * An ACL (Access Control List) is kept for every service.  
 * It can include groups or individuals.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.3
 */
public class MinervaServiceAccess implements java.io.Serializable 
{
    private String name;
    private HashMap acl;

    /**
     * Creates a new access object.
     * 
     * @param name The name of the service.
     */
    public MinervaServiceAccess (String name)
    {
	this.name = name;

	acl = new HashMap();
    }

    /**
     * Returns whether a group has access to a service or not.
     *
     * @return <tt>true</tt> if one of the groups has access.  
     *         <tt>false</tt> otherwise.
     */
    public synchronized boolean hasAccess (MinervaGroup[] amg)
    {
	for (int i = 0; i < amg.length; i++) {
	    if (acl.containsKey (amg[i]))
		return true;
	}

	return false;
    }

    /**
     * Gets a list of groups allowed to access this service.
     *
     * @return A list of groups allowed to access the service.
     */
    public synchronized MinervaGroup[] getGroups ()
    {
	return (MinervaGroup[]) acl.values().toArray (new MinervaGroup[0]);
    }

    /**
     * Grant access to a this service to the specified group.
     *
     * @param group The group to grant access to.
     */
    public synchronized void grantAccess (MinervaGroup group)
    {
	acl.put (group, group);
    }

    /**
     * Deny access to a this service to the specified group.
     *
     * @param group The group to grant access to.
     */
    public synchronized void denyAccess (MinervaGroup group)
    {
	acl.remove (group);
    }


    /**
     * Gets the service name.
     * 
     * @return The service name.
     */
    public String getName ()
    {
	return name;
    }
 
    public String toString()
    {
	return name;
    }
}
	
					     
