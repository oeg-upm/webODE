package es.upm.fi.dia.ontology.minerva.server.builtin;

// RMI stuff
import java.rmi.*;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * This interface defines the functionality of the logging mechanism.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public interface LogService extends MinervaService
{
    /** Numeric constant for specifying debug log level. */
    static final int DEBUG_LEVEL       = 0;
    /** Numeric constant for specifying informative log level. */
    static final int INFORMATIVE_LEVEL = 1;
    /** Numeric constant for specifying error log level. */
    static final int ERROR_LEVEL       = 2;

    /**
     * Writes an entry with an informative message.
     *
     * @param msg The message to be written.
     */
    public void logInfo (String msg) throws RemoteException;

    /**
     * Writes an entry with an error message.
     *
     * @param msg The message to be written.
     */
    public void logError (String msg) throws RemoteException;

    /**
     * Writes an entry with an error message.
     *
     * @param msg The message to be written.
     * @param th The Throwable to be trace.
     */
    public void logError (String msg, Throwable th) throws RemoteException;

    /**
     * Writes an entry with a debug message.
     *
     * @param msg The message to be written.
     */
    public void logDebug (String msg) throws RemoteException;

    /**
     * Sets a unique identifier for log entries.
     *
     * @param id Identifier.
     */
    public void setId (String id) throws RemoteException;
}



