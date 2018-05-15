package es.upm.fi.dia.ontology.minerva.server.management;

import es.upm.fi.dia.ontology.minerva.server.admin.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.AuthenticationServiceImp;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;

import java.rmi.*;
import java.rmi.server.*;

/**
 * This is the root class for all managers.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.2
 */
public class MinervaManagerImp extends UnicastRemoteObject implements MinervaManager
{
    /** To update changes */
    private AdministrationServiceImp admServiceImp;
//    private AuthenticationServiceImp auth;
    private String serviceName;

    /**
     * The service configuration object.
     */
    protected MinervaServiceConfiguration serviceConfig;

    /**
     * Current context.
     */
    protected MinervaContext context;

    /**
     * Just to fulfil the contract.
     */
    public MinervaManagerImp() throws RemoteException {}


    /**
     * This method sets the configuration information for
     * the service to be configured.
     *
     * @param serviceConfig The service configuration object.
     */
    public void setConfiguration (MinervaServiceConfiguration serviceConfig)
    {
	this.serviceConfig = serviceConfig;
    }

    /**
     * Sets the context for the manager.  It's the same as
     * the one in the administration service current session.
     *
     * @param context
     */
    public void setContext (MinervaContext context)
    {
	this.context = context;
    }

    /**
     * This method returns information about the management attributes
     * in the service.
     *
     * @return A <tt>MinervaServiceDescriptor</tt> providing information about a service.
     *         <tt>null</tt> in case of providing no information.
     */
    public MinervaServiceDescription getServiceDescription () throws RemoteException
    {
	return null;
    }

    /**
     * Gets a user interface to configure the service in the
     * <i>Minerva Management Console</i>.
     *
     * @return A user interface or <tt>null</tt> if no one is provided.
     */
    public MinervaUI getServiceUI () throws RemoteException
    {
	return null;
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
	throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
    }

    /**
     * Internal use.
     */
    public final void setAdministrator (AdministrationServiceImp admServiceImp,
					String serviceName)
    {
	this.admServiceImp = admServiceImp;
	this.serviceName   = serviceName;
    }

    /**
     * Internal use
     * @param auth The Authentication Service
     */
//    public void setAuthentication(AuthenticationServiceImp auth) {
//      this.auth=auth;
//    }

//    protected MinervaService getService(String serviceName) {
//      return auth.getServiceWithoutAuthentication()
//      return null;
//    }

    /**
     * Commit changes to persistent storage.
     * <p>
     * Default implementation stores the configuration object into
     * its associated file and notifies all related services in a running state
     * that the configuration has changed.
     */
    public void commit () throws MinervaException
    {
	try {
	    serviceConfig.storeConfig();

	    // Reconfigure service.
	    admServiceImp.reconfigure (serviceName, serviceConfig);
	} catch (Exception e) {
	    throw new MinervaException ("Error committing changes: " + e.getMessage(), e);
	}
    }
}

