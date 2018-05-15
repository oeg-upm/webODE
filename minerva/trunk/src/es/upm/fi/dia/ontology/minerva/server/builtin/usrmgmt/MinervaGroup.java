package es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt;

import java.util.*;

/**
 * Class to encapsulate the information about a Minerva group.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class MinervaGroup implements java.io.Serializable 
{
    /** The administration group name. */
    public static final String ADMIN_GROUP = "admin";
    /** The administration group description. */
    public static final String ADMIN_DESC  = "The administrator's group.";

    private String name, description;
    private Date creationDate;
    
    private HashMap users;

    /**
     * Creates a new group.
     * 
     * @param name The group's name.
     * @param desc The group's description.
     */
    public MinervaGroup (String name, String desc)
    {
	this.name = name;
	this.description = desc;

	creationDate = new Date();

	users = new HashMap();
    }

    /**
     * Gets the creation date for the user.
     */
    public java.util.Date getCreationDate ()
    {
	return creationDate;
    }

    /**
     * Adds a new user to the group.
     *
     * @param user The new user to be added.
     */
    public void addUser (MinervaUser user)
    {
	synchronized (users) {
	    user.addGroup (this);
	    users.put (user.getLogin(), user);
	}
    }
    
    /**
     * Removes a user from a group.
     *
     * @param user
     */
    public void removeUser (MinervaUser user)
    {
	synchronized (users) {
	    user.removeGroup (this);
	    users.remove (user.getLogin());
	}
    }

    /**
     * Gets users.
     */
    public MinervaUser[] getUsers ()
    {
	synchronized (users) {
	    return (MinervaUser[]) users.values().toArray (new MinervaUser[0]);
	}
    }

    /**
     * Set equality to just the name field.
     */
    public boolean equals (Object o) 
    {
	if (o instanceof MinervaGroup) {
	    MinervaGroup mg = (MinervaGroup) o;
	    return mg.name.equals (name);
	}
	return false;
    }

    /**
     * Sets a user description.
     *
     * @param desc The description.
     */
    public void setDescription (String desc)
    {
	this.description = desc;
    }


    /**
     * Gets a user. 
     * <tt>null</tt> if not found.
     */
    public MinervaUser getUser (String login)
    {
	synchronized (users) {
	    return (MinervaUser) users.get (login);
	}
    }

    /**
     * The hash code comes from the group's name field.
     */
    public int hashCode ()
    {
	return name.hashCode();
    }

    /**
     * Gets the group's name.
     */
    public String getName ()
    {
	return name;
    }

    /**
     * Gets the group´s description.
     */
    public String getDescription ()
    {
	return description;
    }


    public String toString ()
    {
	return name + " - " + (description == null ? "<No description>" : description);
    }
}


    



