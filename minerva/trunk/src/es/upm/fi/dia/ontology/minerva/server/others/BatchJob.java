package es.upm.fi.dia.ontology.minerva.server.others;

import java.rmi.*;

/**
 * The interface every batched job has to implement.
 * <p>
 * The jobs to be executed may even be remote.  There's
 * no restriction on it.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public interface BatchJob extends Remote
{
    /**
     * This method will be invoked when the time to execute
     * the job has come.
     *
     * @exception BatchJobException If something happens (for
     *            logging purposes).
     * @param     jd The job description that originated this
     *            invocation.
     */
    void executeJob (JobDescription jd) throws BatchJobException, RemoteException;
}





