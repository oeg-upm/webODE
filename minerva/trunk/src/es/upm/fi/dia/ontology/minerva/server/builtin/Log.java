package es.upm.fi.dia.ontology.minerva.server.builtin;

import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * This interface defines the functionality of the logging mechanism.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public interface Log extends MinervaService
{
    /** Numeric constant for specifying informative log level. */
    int INFORMATIVE_LEVEL = 0;
    /** Numeric constant for specifying debug log level. */
    int DEBUG_LEVEL       = 1;
    /** Numeric constant for specifying error log level. */
    int ERROR_LEVEL       = 2;

    /**
     * Writes an entry with an informative message.
     *  
     * @param msg The message to be written.
     */
    public void logInfo (String msg);

    /**
     * Writes an entry with an error message.
     *  
     * @param msg The message to be written.
     */
    public void logError (String msg);
    
    /**
     * Writes an entry with a debug message.
     *  
     * @param msg The message to be written.
     */
    public void logDebug (String msg);

    /**
     * Sets a unique identifier for log entries.
     *
     * @param id Identifier.
     */
    public void setId (String id);
}
