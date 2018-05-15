package es.upm.fi.dia.ontology.minerva.server.builtin;

// Java stuff
import java.util.*;
import java.io.*;
import java.rmi.*;
import javax.swing.*;

// own stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.gui.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;
import es.upm.fi.dia.ontology.minerva.server.admin.AdministrationService;
import es.upm.fi.dia.ontology.minerva.server.admin.ServiceDescriptor;

/**
 * This class provides a manager for the authentication service,
 * with a customized user interface.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.3
 */
public class AuthenticationServiceManager extends MinervaManagerImp
    implements AuthenticationServiceManagerIntf
{
    /**
     * Constructor to fulfill the contract.
     */
    public AuthenticationServiceManager () throws RemoteException
    {
    }

    /**
     * Gets a complete GUI to manage users, groups and services.
     */
    public MinervaUI getServiceUI() throws RemoteException
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;
	return new UserManagementUI ();
    }

    /**
     * Creates a new user.
     *
     * @param login The login for the user.
     * @param password The user's password.
     * @param description The user's description.
     */
    public void createUser (String login, String password, String description)
	throws RemoteException, MinervaException
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;

	if (asc.getUser (login) != null)
	    throw new MinervaException ("user " + login + " already exists.");

	asc.addUser (new MinervaUser (login, password, description));
	// Commit changes inmediately
	commit();
    }

    /**
     * Updates a user.
     *
     * @param login The login for the user.
     * @param password The user's password.
     * @param description The user's description.
     */
    public void updateUser (String login, String password, String description)
	throws RemoteException, MinervaException
    {
	MinervaUser mu = getUser (login);

	mu.setDescription (description);
	mu.setPassword (password);

	// Commit changes
	commit();
    }

    /**
     * Deletes the specified user.
     *
     * @param login The login for the user.
     */
    public void deleteUser (String login) throws RemoteException, MinervaException
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;
	if (asc.getUser (login) == null)
	    throw new MinervaException ("user " + login + " does not exist.");

	asc.deleteUser (new MinervaUser (login, null, null));

	// Commit changes
	commit();
    }

    /**
     * Gets a given user.
     *
     * @param user The user's login.
     */
    public MinervaUser getUser (String login) throws RemoteException, MinervaException
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;

	MinervaUser mu = asc.getUser (login);
	if (mu == null)
	    throw new MinervaException ("No such user (" + login + ")");
	else
	    return mu;
    }

    /**
     * List all the users.
     *
     * @return An array holding all the users present in the system.
     */
    public MinervaUser[] getUsers () throws RemoteException
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;

	return asc.getUsers();
    }

    /**
     * Creates a new group.
     *
     * @param name The group's name.
     * @param description The group's description.
     */
    public void createGroup (String name, String description) throws RemoteException, MinervaException
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;

	asc.addGroup (new MinervaGroup (name, description));
	commit();
    }

    /**
     * Updates a group.
     *
     * @param description The group's description.
     */
    public void updateGroup (String name, String description) throws RemoteException, MinervaException
    {
	MinervaGroup mg = getGroup (name);
	mg.setDescription (description);

	commit();
    }

    /**
     * Deletes a group.
     *
     * @param name The group's name.
     */
    public void deleteGroup (String name)  throws RemoteException, MinervaException
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;
	asc.deleteGroup (new MinervaGroup (name, null));

	commit();
    }

    /**
     * Gets a given group.
     *
     * @param name The group's name.
     * @return The description for the group.
     */
    public MinervaGroup getGroup (String name) throws RemoteException, MinervaException
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;
	MinervaGroup mg = asc.getGroup(name);
	if (mg == null)
	    throw new MinervaException ("No such group (" + name + ")");

	return mg;
    }


    /**
     * Gets a given group.
     *
     * @return All the groups available in the system.
     */
    public MinervaGroup[] getGroups ()
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;

	return asc.getGroups();
    }

    /**
     * Adds a user to a group.
     *
     * @param group The group's name.
     * @param login The user's login to add to the group.
     */
    public void addUserToGroup (String group, String login) throws RemoteException, MinervaException
    {
	MinervaUser mu = getUser (login);
	MinervaGroup mg = getGroup (group);

	mg.addUser (mu);

	// commit
	commit();
    }

    /**
     * Removes a user from a group.
     *
     * @param group The group's name.
     * @param login The user's login to remove from the group.
     */
    public void removeUserFromGroup (String group, String login) throws RemoteException, MinervaException
    {
	MinervaUser mu = getUser (login);
	MinervaGroup mg = getGroup (group);

	mg.removeUser (mu);

	// commit
	commit();
    }

    /**
     * Gets current service access information.
     */
    public MinervaServiceAccess[] getServices () throws RemoteException, MinervaException
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;
        AdministrationService admin=(AdministrationService)this.context.getService(MinervaServerConstants.ADMINISTRATION_SERVICE);
        ServiceDescriptor[] asd=admin.getServices();
        TreeMap adminServices=new TreeMap();
        for(int i=0; asd!=null && i<asd.length; i++)
          adminServices.put(asd[i].name, new MinervaServiceAccess(asd[i].name));
        MinervaServiceAccess[] amsa=asc.getServiceAccess();
        TreeMap authServices=new TreeMap();
        for(int i=0; amsa!=null && i<amsa.length; i++)
          authServices.put(amsa[i].getName(), amsa[i]);

        adminServices.keySet().removeAll(authServices.keySet());
        MinervaServiceAccess msa;
        for(Iterator it=adminServices.values().iterator(); it.hasNext(); ) {
          msa=(MinervaServiceAccess)it.next();
          authServices.put(msa.getName(), msa);
          asc.addService(msa);
        }

        if(authServices.size()>0)
          return (MinervaServiceAccess[])authServices.values().toArray(new MinervaServiceAccess[0]);
        else
          return null;
    }

    /**
     * Gets access information about a particular service.
     */
    public MinervaServiceAccess getService (String name) throws RemoteException, MinervaException
    {
	AuthenticationServiceConfiguration asc = (AuthenticationServiceConfiguration) serviceConfig;

	return asc.getServiceAccess(name);
    }

    /**
     * Grants access to a service from a particular group.
     *
     * @param name The service's name.
     * @param group The group to grant access to.
     */
    public void grantAccessToGroup (String name, String group) throws RemoteException, MinervaException
    {
	MinervaServiceAccess msa = getService (name);
	msa.grantAccess (getGroup (group));

	// commit
	commit();
    }

    /**
     * Denies access to a service from a particular group.
     *
     * @param name The service's name.
     * @param group The group to grant access to.
     */
    public void denyAccessToGroup (String name, String group) throws RemoteException, MinervaException
    {
	MinervaServiceAccess msa = getService (name);
	msa.denyAccess (getGroup (group));

	// commit
	commit();
    }
}