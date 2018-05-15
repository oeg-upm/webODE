package es.upm.fi.dia.ontology.minerva.server.management;

import java.rmi.Remote;
import java.rmi.RemoteException;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * This interface provides the basic behaviour of a <i>Minerva
 * Management Service</i> (MMS).
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public interface MinervaManager extends Remote
{
    /**
     * This method sets the configuration information for
     * the service to be configured.
     *
     * @param serviceConfig The service configuration object.
     */
    void setConfiguration (MinervaServiceConfiguration serviceConfig) throws RemoteException;;
 
    /**
     * This method returns information about the management attributes
     * in the service.
     *
     * @return A <tt>MinervaServiceDescriptor</tt> providing information about a service.
     *         <tt>null</tt> in case of providing no information.
     */
    MinervaServiceDescription getServiceDescription () throws RemoteException;

    /**
     * Gets a user interface to configure the service in the 
     * <i>Minerva Management Console</i>.
     * 
     * @return A user interface or <tt>null</tt> if no one is provided.
     */
    MinervaUI getServiceUI () throws RemoteException;

    /**
     * This method is used if no UI has been defined over the service.
     *
     * @param attrName  Name of the configurable attribute.
     * @param attrValue The attribute's value.
     */
    void setAttribute (String attrName, Object attrValue) throws NoSuchAttributeException, RemoteException;

    /**
     * Commit changes to persistent storage and notifies running services.
     *
     * @exception MinervaException In case of error storing configuration.
     */
    void commit () throws MinervaException, RemoteException;
}
