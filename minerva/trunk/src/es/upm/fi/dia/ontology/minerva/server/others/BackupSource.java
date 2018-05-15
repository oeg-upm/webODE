package es.upm.fi.dia.ontology.minerva.server.others;

import java.rmi.*;

/**
 * The callback interface for backup sources.
 * <p>
 * A service wanting to be a backup source must implement
 * this interface completely.  In case of any error, an
 * exception of type <tt>BackupException</tt> must be 
 * thrown.
 *
 * @author  Julio César Arpírez Vega
 * @version 1.0
 */
public interface BackupSource extends Remote
{
    /**
     * Returns whether or not there are more elements to be backed up.
     *
     * @return <tt>true</tt> if there are elements remaining.  <tt>false</tt> otherwise.
     */
    boolean hasNext () throws RemoteException, BackupException;

    /**
     * Gets the name of current backup element.
     *
     * @return The name of the current backup element.
     */
    String getName() throws RemoteException, BackupException;
    
    /**
     * Gets the source for the current backup element.  
     * 
     * @return An object of a class implementing the <tt>java.io.Serializable</tt>
     *         interface, that is the item to be backed up.
     */
    Object getSource () throws RemoteException, BackupException;
}
