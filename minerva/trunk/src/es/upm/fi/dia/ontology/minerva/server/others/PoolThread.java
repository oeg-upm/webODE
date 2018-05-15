package es.upm.fi.dia.ontology.minerva.server.others;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;

import java.util.*;

/**
 * The worker thread in the thread pool.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.5
 */
public class PoolThread extends Thread
{
    private List jobs;
    private MinervaContext log;

    public PoolThread (ThreadGroup tg, String name, List jobs, MinervaContext log)
    {
	super (tg, name);

	this.log = log;
	this.jobs = jobs;
    }

    public void run ()
    {
	while (true) {
	    Runnable runnable;

	    try {
		synchronized (jobs) {
		    try {
			if (jobs.isEmpty()) {
			    log.logDebug ("about to block.");
			    jobs.wait();
			}
		    } catch (Exception e) {
			log.logError ("unexpected error in worker thread (" + this + 
				      ") when locking the job queue: " + e);
			continue;
		    }
		    log.logDebug ("awakened!  About to execute work.");
		    runnable = (Runnable) jobs.get (0);
		    jobs.remove (0);
		}
		
		// Execute work
		runnable.run();
		log.logDebug ("job executed successfully.");
	    } catch (Exception e) {
		log.logError ("The job executed by the pool thread " + this + " caused an exception (" +
			      e + ").");
	    }
	}
    }    
}
