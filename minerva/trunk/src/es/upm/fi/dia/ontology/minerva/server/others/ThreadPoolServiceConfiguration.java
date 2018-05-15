package es.upm.fi.dia.ontology.minerva.server.others;

import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * The configuration class for the thread pool service.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class ThreadPoolServiceConfiguration extends MinervaServiceConfiguration
{
    /** The number of threads for the pool. */
    public int numThreads;
    /** The thread group priority. */
    public int priority;

    public ThreadPoolServiceConfiguration ()
    {
	// This service is local
	super (true);

	numThreads = 10;
	priority   = 5;
    }

    public ThreadPoolServiceConfiguration (int numThreads, int priority)
    {
	super (true);

	this.numThreads = numThreads;
	this.priority   = priority;
    }
}
