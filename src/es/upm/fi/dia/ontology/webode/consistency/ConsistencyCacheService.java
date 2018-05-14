package es.upm.fi.dia.ontology.webode.consistency;

import es.upm.fi.dia.ontology.minerva.server.services.*;
// Webode stuff
import es.upm.fi.dia.ontology.webode.service.*;

import java.rmi.*;

/**
 * This interface defines the contract for the consistency
 * cache used by the <tt>ODEService</tt> service.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.2
 * @see     es.upm.fi.dia.ontology.webode.service.ODEService
 */
public interface ConsistencyCacheService extends MinervaService
{
    /** The default time to check for caches */
    public static final int DEFAULT_TIME = 16 * 60 * 1000;

    /**
     * Opens an ontology (load it into the cache).
     *
     * @param ontology The ontology to be loaded.
     */
    void loadOntology (String ontology) throws RemoteException, WebODEException, ConsistencyCacheException;

    /**
     * Unloads an ontology, discarding it.
     *
     * @param ontology The ontology to be discarded.
     */
    void unloadOntology (String ontology) throws RemoteException, WebODEException, ConsistencyCacheException;

    /**
     * Update ontology name.
     *
     * @param oldName The old name.
     * @param newName The new name.
     */
    void updateOntologyName (String oldName, String newName)
	throws RemoteException, WebODEException, ConsistencyCacheException;

    /**
     * Insert a new concept.
     *
     * @param ontology The ontology the concept is added to.
     * @param name     The concept's name.
     */
    void insertConcept (String ontology, String name)
	throws RemoteException, WebODEException, ConsistencyCacheException;

    /**
     * Updates a concept.
     *
     * @param ontology The ontology the concept is added to.
     * @param oldName  The old concept's name.
     * @param newName  The new concept's name.
     */
    void updateConcept (String ontology, String oldName, String newName)
	throws RemoteException, WebODEException, ConsistencyCacheException;

    /**
     * Removes a concept.
     *
     * @param ontology The ontology the concept is added to.
     * @param name     The concept's name.
     */
    void removeConcept (String ontology, String name)
	throws RemoteException, WebODEException, ConsistencyCacheException;

    /**
     * Inserts a new relationship.
     *
     * @param ontology The ontology the concept is added to.
     * @param name     The relation's name.
     * @param origin   The origin's name.
     * @param destination The destination's name.
     */
    void insertTermRelation (String ontology, String name, String origin, String destination)
	throws RemoteException, WebODEException, ConsistencyCacheException;

    /**
     * Removes a relationship.
     *
     * @param ontology The ontology the concept is added to.
     * @param name     The relation's name.
     * @param origin   The origin's name.
     * @param destination The destination's name.
     */
    void removeTermRelation (String ontology, String name, String origin, String destination)
	throws RemoteException, WebODEException, ConsistencyCacheException;

    /**
     * Insert a new group.
     *
     * @param ontology The ontology the concept is added to.
     * @param group    The group.
     */
    void insertGroup (String ontology, Group group)
	throws RemoteException, WebODEException, ConsistencyCacheException;

    /**
     * Updates a group.
     *
     * @param ontology The ontology the concept is added to.
     * @param oldName  The old group's name.
     * @param group    The group.
     */
    void updateGroup (String ontology, String oldName, Group group)
	throws RemoteException, WebODEException, ConsistencyCacheException;

    /**
     * "Touches" an ontology to modify its access time.
     */
    void touch (String ontology) throws RemoteException;
}




