package es.upm.fi.dia.ontology.minerva.server;

import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * Class thrown to indicate the service being requested does not exist
 * on server.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class NoSuchServiceException extends MinervaException
{
    public NoSuchServiceException (String str) 
    {
	super (str);
    }
    
    public NoSuchServiceException () 
    {
    }
}
