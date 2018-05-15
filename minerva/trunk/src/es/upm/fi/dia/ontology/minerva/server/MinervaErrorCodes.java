package es.upm.fi.dia.ontology.minerva.server;

/**
 * Interface containing start-up error codes.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public interface MinervaErrorCodes 
{
    /** Error with the environment. */
    int ERR_ENVIRONMENT            = 1;

    /** Error starting log service. */
    int ERR_LOG_SERVICE            = 2;

    /** Error starting the authentication service. */
    int ERR_AUTHENTICATION_SERVICE = 3;

    /** Error starting the administration service. */
    int ERR_ADMINISTRATION_SERVICE = 4;

    /** Error with the class loader. */
    int ERR_CLASS_LOADER           = 5;
}
