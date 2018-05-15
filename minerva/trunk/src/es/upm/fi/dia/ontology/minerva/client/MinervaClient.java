package es.upm.fi.dia.ontology.minerva.client;

import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.MinervaUserMessageDigest;
import es.upm.fi.dia.ontology.minerva.server.admin.*;

import java.rmi.*;

/**
 * This class provides a mini API for connecting to a server
 * given a URL.
 * <p>
 * A factory pattern is used.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class MinervaClient
{
  // Don't allow user to create instances.
  private MinervaClient () {}

  /**
   * Gets a session to a Minerva Server.
   *
   * @param url The URL the server resides in.
   * @param username Username.
   * @param password User's password.
   * @exception RemoteException In case of a communication error.
   * @exception AuthenticationException In case of an invalid username/password pair.
   * @exception MinervaException  If any other error occurs.
   */
  public static MinervaSession getMinervaSession (MinervaURL url, String username, String password) throws MinervaException, AuthenticationException, RemoteException {
    AuthenticationService ms=null;
    try {
      // Get a reference to the authentication server.

      ms = (AuthenticationService) Naming.lookup
      ("//" + url.getHost() + ":" +
          (url.getPort() < 0 ? MinervaServerConstants.MINERVA_SERVER_PORT : url.getPort()) +
          "/" + MinervaServerConstants.AUTHENTICATION_SERVICE);

      return ms.getSession (username, password);
    } catch (AuthenticationException ae) {
      throw ae;
    } catch (Exception e) {
      throw new MinervaException (e.getMessage(), e);
    }
  }

  /**
   * Gets a session to a Minerva Server.
   *
   * @param url The URL the server resides in.
   * @param username The user's login.
   * @param messageDigest The digester of the use and its password.
   * @param digest The digest message to compare to.
   * @exception RemoteException In case of a communication error.
   * @exception AuthenticationException In case of an invalid username/digest pair.
   * @exception MinervaException  If any other error occurs.
   */
  public static MinervaSession getMinervaSession (MinervaURL url, String username, MinervaUserMessageDigest messageDigest, byte[] digest) throws MinervaException, AuthenticationException, RemoteException {
    AuthenticationService ms=null;
    try {
      // Get a reference to the authentication server.

      ms = (AuthenticationService) Naming.lookup
      ("//" + url.getHost() + ":" +
          (url.getPort() < 0 ? MinervaServerConstants.MINERVA_SERVER_PORT : url.getPort()) +
          "/" + MinervaServerConstants.AUTHENTICATION_SERVICE);

      return ms.getSession (username, messageDigest, digest);
    } catch (AuthenticationException ae) {
      throw ae;
    } catch (Exception e) {
      throw new MinervaException (e.getMessage(), e);
    }
  }

  /**
   * Gets the administration service.
   *
   * @param ms The Minerva Session.
   * @exception RemoteException In case of a communication error.
   * @exception AuthenticationException If the user does not have permission to administrate.
   */
  public static AdministrationService getAdministrator (MinervaSession ms)
  throws AuthenticationException, NoSuchServiceException, MinervaException, RemoteException
  {
    return (AdministrationService) ms.getService (MinervaServerConstants.ADMINISTRATION_SERVICE);
  }
}




