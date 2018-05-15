package es.upm.fi.dia.ontology.minerva.server.admin;

// Own stuff
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * The configuration for the administration service.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 * @see es.upm.fi.dia.ontology.minerva.server.admin.AdministrationService
 */
public class AdministrationServiceConfiguration extends MinervaServiceConfiguration
{
    /** The configuration file name. */
    public String configFile;

    /** A reference to the log service descriptor. */
    public ServiceDescriptor logService;

    /** A reference to the authentication service descriptor. */
    public ServiceDescriptor authService;
    
    /**
     * Constructor.
     *
     * @param configFile The configuration file name.
     * @param logService The log service descriptor.
     * @param authService The authentication service descriptor. 
     */
    public AdministrationServiceConfiguration (String configFile, ServiceDescriptor logService,
					       ServiceDescriptor authService)
    {
	super (false);

	this.configFile = configFile;
	this.authService = authService;
	this.logService  = logService;
    }
}








