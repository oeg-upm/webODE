package es.upm.fi.dia.ontology.minerva.server.builtin;

// RMI stuff
import java.rmi.*;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.MinervaUserMessageDigest;
import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * This interface defines the principles of the authentication 
 * mechanism.
 * 
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public interface AuthenticationService extends MinervaService {
    /**
     * This method provides a valid session once the user has
     * been properly authenticated.   
     *
     * @param username The user's login.
     * @param password The user's password.
     * @exception AuthenticationException In case of a wrong user/password pair.
     * @exception RemoteException In case of a communication problem.
     */
    MinervaSession getSession (String username, String password) throws AuthenticationException, RemoteException;

    /**
     * This method provides a valid session once the user has
     * been properly authenticated.   
     *
     * @param username The user's login.
     * @param messageDigest The digester of the use and its password.
     * @param digest The digest message to compare to.
     * @exception AuthenticationException In case of a wrong user/password pair.
     * @exception RemoteException In case of a communication problem.
     */
    MinervaSession getSession (String username, MinervaUserMessageDigest messageDigest, byte[] digest) throws AuthenticationException, RemoteException;
}
