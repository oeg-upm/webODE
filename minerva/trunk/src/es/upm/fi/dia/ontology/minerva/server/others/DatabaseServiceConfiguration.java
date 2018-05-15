package es.upm.fi.dia.ontology.minerva.server.others;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * The configuration class for the database service.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.5
 */
public class DatabaseServiceConfiguration extends MinervaServiceConfiguration
{
    /** URL */
    public String URL;
    /** Username */
    public String username;
    /** Password */
    public String password;
    /** Driver */
    public String driver;
    /** Number of connections */
    public int connections;

    /** Maximum number of usages for the connection. */
    public int maxUsages;

    public DatabaseServiceConfiguration() 
    {
	super (false);
    }

    public DatabaseServiceConfiguration (String URL, String username, 
					 String password, String driver,
					 int connections, int maxUsages) 
    {
	super (false);

	this.URL = URL;
	this.username    = username;
	this.password    = password;
	this.driver      = driver;	
	this.connections = connections;
	this.maxUsages   = maxUsages;
    }
}




