package es.upm.fi.dia.ontology.minerva.server.builtin;

import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;

import java.rmi.*;

public interface AuthenticationServiceManagerIntf extends MinervaManager
{
    /**
     * Creates a new user.
     *
     * @param login The login for the user.
     * @param password The user's password.
     * @param description The user's description.
     */
    void createUser (String login, String password, String description) 
	throws RemoteException, MinervaException;

    /**
     * Updates a user.
     *
     * @param login The login for the user.
     * @param password The user's password.
     * @param description The user's description.
     */
    void updateUser (String login, String password, String description) 
	throws RemoteException, MinervaException;

    /**
     * Gets a given user.
     * 
     * @param user The user's login.
     */
    MinervaUser getUser (String login) throws RemoteException, MinervaException;

    /**
     * Deletes the specified user.
     * 
     * @param login The login for the user.
     */
    void deleteUser (String login) throws RemoteException, MinervaException;

    /**
     * List all the users.
     *
     * @return An array holding all the users present in the system.
     */
    MinervaUser[] getUsers () throws RemoteException;

    /**
     * Gets a given group.
     *
     * @param name The group's name.
     * @return The description for the group.
     */
    MinervaGroup getGroup (String name) throws RemoteException, MinervaException;

    /**
     * Gets a given group.
     *
     * @return All the groups available in the system.
     */
    MinervaGroup[] getGroups () throws RemoteException, MinervaException;

    /**
     * Creates a new group.
     * 
     * @param name The group's name.
     * @param description The group's description.
     */
    void createGroup (String name, String description) throws RemoteException, MinervaException;

    /**
     * Updates a group.
     *
     * @param description The group's description.
     */
    void updateGroup (String name, String description) throws RemoteException, MinervaException;

    /**
     * Deletes a group.
     *
     * @param name The group's name.
     */
    void deleteGroup (String name)  throws RemoteException, MinervaException;

   /**
     * Adds a user to a group.
     *
     * @param group The group's name.
     * @param login The user's login to add to the group.
     */
    void addUserToGroup (String group, String login) throws RemoteException, MinervaException;

    /**
     * Removes a user from a group.
     *
     * @param group The group's name.
     * @param login The user's login to remove from the group.
     */
    void removeUserFromGroup (String group, String login) throws RemoteException, MinervaException;


    /**
     * Gets current service access information.
     */
    MinervaServiceAccess[] getServices () throws RemoteException, MinervaException;

    /**
     * Gets access information about a particular service.
     */
    MinervaServiceAccess getService (String name) throws RemoteException, MinervaException;

    /**
     * Grants access to a service from a particular group.
     *
     * @param name The service's name.
     * @param group The group to grant access to.
     */
    void grantAccessToGroup (String name, String group) throws RemoteException, MinervaException;

    /**
     * Denies access to a service from a particular group.
     *
     * @param name The service's name.
     * @param group The group to grant access to.
     */
    void denyAccessToGroup (String name, String group) throws RemoteException, MinervaException;
}
