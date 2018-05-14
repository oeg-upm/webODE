package es.upm.fi.dia.ontology.webode.consistency;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * The exception for the <tt>ConsistencyCacheService</tt> module.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class ConsistencyCacheException extends WebODEException
{
    public ConsistencyCacheException ()
    {
    }

    public ConsistencyCacheException (String msg)
    {
	super (msg);
    }

    public ConsistencyCacheException (Throwable e) {
        super(e);
    }

    public ConsistencyCacheException (String msg, Throwable e)
    {
        super (msg, e);
    }

}
