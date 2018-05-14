package es.upm.fi.dia.ontology.webode.consistency;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * Class that describes the configuration for the consistency module.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.2
 */
public class ConsistencyCacheServiceConfiguration extends MinervaServiceConfiguration
{
    /** DB service to use. */
    public String dbService;

    /** Scheduler to use. */
    public String scheduler;

    /** The time to do cache keeping. */
    public int time;
    
    public ConsistencyCacheServiceConfiguration ()
    {
	// This service is local
	super (true, STATELESS);

	time = ConsistencyCacheService.DEFAULT_TIME;
    }
    
    /**
     * Constructor.
     *
     * @param dbService The name of the database service to use
     *                  to access the ontologies in the database.
     * @param scheduler The batch worker to use for cache refreshing.
     * @param time      The period of time to keep track of cache usage.
     */
    public ConsistencyCacheServiceConfiguration (String dbService, String scheduler, int time)
    {
	super (true, STATELESS);
	
	this.dbService = dbService;
	this.scheduler = scheduler;
	this.time      = time;
    }
}
