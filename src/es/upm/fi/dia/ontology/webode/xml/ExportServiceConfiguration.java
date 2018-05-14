package es.upm.fi.dia.ontology.webode.xml;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * Class that describes the configuration for the XML export service.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class ExportServiceConfiguration extends MinervaServiceConfiguration
{
    /** DB service to use. */
    public String odeService;

    public ExportServiceConfiguration ()
    {
	super (false, STATELESS);
    }

    /**
     * Constructor.
     *
     * @param dbService The name of the database service to use
     *                  to access the ontologies in the database.
     */
    public ExportServiceConfiguration (String odeService)
    {
	super (false, STATELESS);

	this.odeService = odeService;
    }
}
