package es.upm.fi.dia.ontology.minerva.server.others;

import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * This service provides a basic scheduler to perform
 * periodic jobs.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public interface BatchWorkerService extends MinervaService
{
    /**
     * Registers a service to be called when it decides.
     * <p>
     * The job is put onto the queue of pending jobs.
     *
     * @param jobDescription The job description object.
     * @return The job identifier.
     */
    long registerJob (JobDescription jobDescription)
	throws RemoteException;
    
    /**
     * Unregisters a previously scheduled job.
     *
     * @param jobId  The job identifier returned by the <tt>registerJob</tt>
     *               method.
     */
    void unregisterJob (int jobId) throws RemoteException;
}
