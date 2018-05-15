package es.upm.fi.dia.ontology.minerva.server.others;

import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * This service provides a pool of threads to improve server
 * performance.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public interface ThreadPoolService extends MinervaService
{
    /**
     * Asks the pool to execute something asynchronously.
     * <p>
     * The method is non-blocking.  If no threads are available,
     * the job is queued to be executed when any thread is
     * ready.
     */
    void executeWork (Runnable runnable) throws RemoteException;
}
