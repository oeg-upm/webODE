package es.upm.fi.dia.ontology.minerva.server.services;

/**
 * Exception thrown when requesting non-existing information about a service.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class NoServiceConfigurationException extends es.upm.fi.dia.ontology.minerva.server.MinervaException
{
    public NoServiceConfigurationException (String str) 
    {
	super (str);
    }

    public NoServiceConfigurationException () 
    {
    }
}
