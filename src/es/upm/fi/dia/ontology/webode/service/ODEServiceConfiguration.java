package es.upm.fi.dia.ontology.webode.service;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * Class that describes the configuration for the ODE service.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.2
 */
public class ODEServiceConfiguration extends MinervaServiceConfiguration
{
    /** DB service to use. */
    public String dbService;

    /** Cache service */
    public String cacheService;

    public Boolean trace;

    public ODEServiceConfiguration ()
    {
	super (false, STATEFUL);
        this.trace=new Boolean(false);
    }

    /**
     * Constructor.
     *
     * @param dbService The name of the database service to use
     *                  to access the ontologies in the database.
     */
    public ODEServiceConfiguration (String dbService)
    {
	super (false, STATEFUL);

	this.dbService = dbService;
        this.trace=new Boolean(false);
    }

    /**
     * Constructor.
     *
     * @param dbService The name of the database service to use
     *                  to access the ontologies in the database.
     * @param cacheService The name of the consistency cache service.
     */
    public ODEServiceConfiguration (String dbService, String cacheService)
    {
	super (false, STATEFUL);

	this.dbService    = dbService;
	this.cacheService = cacheService;
        this.trace=new Boolean(false);
    }
}
