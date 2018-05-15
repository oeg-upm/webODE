package es.upm.fi.dia.ontology.minerva.server.others;

import java.rmi.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * This service provides a basic scheduler to perform
 * periodic jobs.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public class BatchWorkerServiceImp extends MinervaServiceImp
    implements BatchWorkerService, Runnable
{
    private ThreadPoolService threadPool;
    private Thread thread;
    private Vector jobs;
    private boolean bRun;

    public BatchWorkerServiceImp () throws RemoteException
    {
    }

    // Life-cycle methods -----------------------------------------------------
    public void start () throws CannotStartException
    {
	// See what databse service to use.
	String threadPoolStr = ((BatchWorkerServiceConfiguration) config).threadPool;
	if (threadPoolStr == null)
	    throw new CannotStartException ("no thread pool specified.  Configure the service.");

	try {
	    // Get the service
	    threadPool = (ThreadPoolService) context.getService (threadPoolStr);
	} catch (Exception e) {
	    throw new CannotStartException ("Consistency cache service cannot start because it cannot" +
					    " obtain the thread pool " + threadPoolStr + ":\n" +
					    e.getMessage(), e);
	}

	// Init data
	jobs = new Vector();

	thread = new Thread (this);
	thread.start();

	bRun = true;
    }

    public void stop ()
    {
	synchronized (jobs) {
	    context.logInfo ("all batched works will be disregarded (" + jobs.size() + ") on user demand.");

	    bRun = false;
	    jobs.notify();
	}
    }

    /**
     * Registers a service to be called when it decides.
     * <p>
     * The job is put onto the queue of pending jobs.
     *
     * @param jobDescription The job description object.
     * @return The job identifier.
     */
    public long registerJob (JobDescription jobDescription)
	throws RemoteException
    {
	boolean bReschedule = false;
	boolean b = false;
	// Put id.
	jobDescription.jobId = System.currentTimeMillis();
	synchronized (jobs) {
	    // Insert into the list and reorder it
	    if (jobs.isEmpty ()) {
		// First element
		// Add element
		jobs.addElement (jobDescription);

		bReschedule = true;
	    }
	    else {
		// Insert in order
		for (int i = 0, l = jobs.size(); i < l; i++) {
		    JobDescription jd = (JobDescription) jobs.elementAt (i);

		    if (jd.executionTime > jobDescription.executionTime) {
			jobs.insertElementAt (jobDescription, i);
			bReschedule = i == 0;
			b = true;
		    }
		}
		if (!b)
		    jobs.addElement (jobDescription);
	    }

	    if (bReschedule)
		reschedule();
	}
	// Return job id.
	return jobDescription.jobId;
    }

    private void reschedule ()
    {
	jobs.notify();
    }


    /**
     * Unregisters a previously scheduled job.
     *
     * @param jobId  The job identifier returned by the <tt>registerJob</tt>
     *               method.
     */
    public void unregisterJob (int jobId) throws RemoteException
    {
	synchronized (jobs) {
	    JobDescription jd = new JobDescription();
	    jd.jobId = jobId;
	    jobs.removeElement (jd);

	    reschedule();
	}
    }

    public void run()
    {
	while (bRun) {
	    JobDescription jd = null;
	    BatchJob batchWork;
	    try {
		// Get first list element
		synchronized (jobs) {
		    if (jobs.isEmpty())
			jobs.wait();

		    while (true) {
			// Check it's time
			jd = (JobDescription) jobs.elementAt (0);
			if (jd.executionTime <= System.currentTimeMillis()) {
			    batchWork = ((JobDescription) jobs.elementAt (0)).batchJob;
			    jobs.removeElementAt (0);
			    break;
			}
			else {
			    // Calculate awakening time
			    long awakeningTime = jd.executionTime - System.currentTimeMillis();
			    if (awakeningTime > 0) {
				jobs.wait (awakeningTime);
			    }
			}
			// Service stopped?
			if (!bRun) return;
		    }
		}

		final String name = jd.name;
		final BatchJob bj = batchWork;
		final MinervaContext context = this.context;
		final JobDescription jd1 = jd;
		// Execute job within the thread pool
		threadPool.executeWork (new Runnable() {
		    public void run () {
			try {
			    context.logInfo ("batch job " + name + " about to execute.");
			    bj.executeJob(jd1);
			    context.logInfo ("batch job " + name + " executed correctly.");
			} catch (Exception e) {
			    context.logError ("batch job " + name + " finished abruptly: " + e + ".");
			}
		    }
		});
	    } catch (Exception e) {
		context.logError ("The batch work " + (jd == null ? "<<unknown>>" : jd.name) +
				  " caused an unexpected exception: " + e);
	    }
	}
    }

    protected void localDisconnect() {
    }
}

