package es.upm.fi.dia.ontology.minerva.server.admin;

// RMI stuff
import java.rmi.*;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * This is the <i>Minerva</i> administration service.  Without it, 
 * there woundn't be a chance to configure the server remotely.
 * <p>
 * This service is tightly related to the authentication service,
 * since the later does not allow anyone not being an administrator
 * to manage de server.
 * 
 * @author   Julio César Arpírez Vega
 * @version  0.2
 * @see      es.upm.fi.dia.ontology.minerva.server.builtin.AuthenticationService
 */
public interface AdministrationService extends MinervaService
{
    /**
     * Gets the services currently installed in the server.
     *
     * @return The descriptors of the services installed.
     */
    ServiceDescriptor[] getServices () throws RemoteException;

    /**
     * Manage a service.
     *
     * @param name The name of the service to manage.
     * @exception NotManageableException If the service has no manager.
     * @exception MinervaException In case of other error (e.g. attempt
     *            to load the service failure).
     */
    MinervaManager getServiceManager (String name) 
	throws RemoteException, NotManageableException, MinervaException;

    /**
     * Install a new service.
     *
     * @param sd The service descriptor.
     * @param classDefinition The jar file containing the service.
     * @exception NameAlreadyBoundException In case of trying to set a service
     *            with an existing name.
     */
    void installService (ServiceDescriptor sd, byte[] classDefinition) 
	throws NameAlreadyBoundException, MinervaException, RemoteException;

    /**
     * Remove a service.
     *
     * @param name The name of the service to be removed.
     * @exception MinervaException In case of error during removal.
     */
    void removeService (String name) throws MinervaException, RemoteException;

    /**
     * Stops the server.
     */
    void stopServer () throws RemoteException;

    /**
     * Stops a given service.
     *
     * @param serviceName The name of the service to be stopped.
     */
    void stopService (String serviceName) throws MinervaException, RemoteException;

    /**
     * Starts a service.
     * 
     * @param serviceName The name of the service to be started.
     */
    void startService (String serviceName) throws MinervaException, RemoteException;

    /**
     * Gets the server's version.
     *
     * @return The server's version.
     */
    String getServerVersion () throws RemoteException;
}




