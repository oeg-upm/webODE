package es.upm.fi.dia.ontology.minerva.server.others;

import java.net.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * The configuration class for the backup service.
 *
 * @author  Julio César Arpírez Vega
 * @version 1.0
 */
public class BackupServiceConfiguration extends MinervaServiceConfiguration
{
    /** The scheduler service to use. */
    public String scheduler;

    /** The destination URL for the backup elements. */
    public URL destinationURL;

    /** The time between two consecutive back-ups (in minutes). */
    public int time;

    /** The source for the backup. */
    public String source;

    public BackupServiceConfiguration ()
    {
	// local
	super (false);
    }
}
