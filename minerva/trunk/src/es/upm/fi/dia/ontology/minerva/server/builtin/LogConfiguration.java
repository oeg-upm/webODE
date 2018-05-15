package es.upm.fi.dia.ontology.minerva.server.builtin;

// Own stuff
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * The configuration for the log service.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class LogConfiguration extends MinervaServiceConfiguration
{
    /** The log directory */
    public String logDirectory;
    /** The suffix for log files. */
    public String  suffix;
    /** The preffix for log files. */
    public String preffix;
    /** 
     * The log level.  One of LogService constants.
     * 
     * @see LogService
     */
    public int level;

    /**
     * Constructor for the configuration object for the log service.
     *
     * @param logDirectory The directory log will be written to.
     * @param preffix      The preffix to apply to log files.
     * @param suffix       The suffix to apply to log files.
     * @param level        The log level.
     */
    public LogConfiguration (String logDirectory, String preffix, String suffix, int level)
    {
	super (true);

	this.logDirectory = logDirectory;
	this.level        = level;
	this.suffix       = suffix;
	this.preffix      = preffix;
    }
}

