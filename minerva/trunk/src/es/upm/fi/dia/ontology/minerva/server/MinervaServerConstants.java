package es.upm.fi.dia.ontology.minerva.server;

/**
 * Interface containing constants about the server.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public interface MinervaServerConstants 
{
    /** MINERVA_HOME */
    public static final String MINERVA_HOME = "MINERVA_HOME";
    /** MINERVA_ETC */
    public static final String MINERVA_ETC  = "MINERVA_ETC";
    /** MINERVA_VAR */
    public static final String MINERVA_VAR  = "MINERVA_VAR";
    /** MINERVA_3RDPARTY */
    public static final String MINERVA_3RDPARTY  = "MINERVA_3RDPARTY";
    /** MINERVA_CLASSES */
    public static final String MINERVA_CLASSES  = "MINERVA_CLASSES";
    /** Server's version */
    public static final String MINERVA_VERSION   = "1.0.18.  August 28th, 2000.";

    /** Configuration directory for services. */
    public static final String CFG_DIR      = "services";
    /** Configuration directory for the server. */
    public static final String MAS_CFG_DIR  = "mas";

    /** Name of the log service */
    String LOG_SERVICE                      = "log";
    /** Log directory from VAR. */
    String LOG_DIRECTORY                    = "log";

    /** Name of the authentication service. */
    String AUTHENTICATION_SERVICE           = "authenticator";

    /** Name of the authentication service. */
    String ADMINISTRATION_SERVICE           = "administrator";

    /** Extension of configuration files. */
    String EXT_CONFIG                       = ".cfg";
    /** Extension of log files. */
    String EXT_LOG                          = ".log";
    
    /** Log files suffix (name + number). */
    String PREFFIX_LOG                      = "minerva";

    
    /** Initial storage for the session pool. */
    int    INITIAL_SESSION_STORAGE          = 200;


    /** The administration group. */
    String ADMINISTRATION_GROUP             = "admin";

    /**
     * The default port the server will listen to.
     */
    public static int MINERVA_SERVER_PORT       = 2000;

    public static String MINERVA_PORT = "MINERVA_PORT";
    
    public static String MINERVA_HOST = "MINERVA_HOST";
    
}



