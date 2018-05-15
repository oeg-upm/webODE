package es.upm.fi.dia.ontology.minerva.server.others;

import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * The configuration class for the batch worker service.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class BatchWorkerServiceConfiguration extends MinervaServiceConfiguration
{
    /** The thread pool to be used. */
    public String threadPool;

    public BatchWorkerServiceConfiguration ()
    {
	// The service can be accessed remotely
	super (false);
    }

    public BatchWorkerServiceConfiguration (String threadPool)
    {
	super (false);

	this.threadPool = threadPool;
    }
}
