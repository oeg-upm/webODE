package es.upm.fi.dia.ontology.minerva.server.others;

/**
 * The exception to be thrown in case of an error during
 * the backup process.
 *
 * @author  Julio César Arpírez Vega
 * @version 1.0
 */
public class BackupException extends Exception
{
    public BackupException () { }

    public BackupException (String msg) 
    {
	super (msg);
    }
}

    
