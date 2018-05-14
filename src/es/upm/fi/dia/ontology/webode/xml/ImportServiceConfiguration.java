package es.upm.fi.dia.ontology.webode.xml;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * Class that describes the configuration for the XML import service.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class ImportServiceConfiguration extends MinervaServiceConfiguration
{
    /** DB service to use. */
    public String odeService;

    public ImportServiceConfiguration ()
    {
	super (false, STATEFUL);
    }

    /**
     * Constructor.
     *
     * @param dbService The name of the database service to use
     *                  to access the ontologies in the database.
     */
    public ImportServiceConfiguration (String odeService)
    {
	super (false, STATEFUL);

	this.odeService = odeService;
    }
}
