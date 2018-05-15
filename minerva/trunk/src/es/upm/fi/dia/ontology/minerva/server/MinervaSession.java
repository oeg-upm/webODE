package es.upm.fi.dia.ontology.minerva.server;

// RMI stuff
import java.rmi.*;
import java.net.URL;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * A session with which the user can access to the services
 * currently running in the server.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public interface MinervaSession extends MinervaService
{
  /**
   * Gets the service with the name given.
   *
   * @param serviceName The service name.
   * @exception AuthenticationException If the user has no access to
   *            the server being requested.
   * @exception NoSuchServiceException If there is no service with the given name.
   * @exception MinervaException In case of other error.
   */
  MinervaService getService (String serviceName)
      throws AuthenticationException, NoSuchServiceException, MinervaException, RemoteException;

  /**
   * Gets current user.
   *
   * @return The user for this session.
   */
  String getUser () throws RemoteException;

  /**
   * Gets current group.
   *
   * @return The group for this session.
   */
  String[] getGroups () throws RemoteException;

  /**
   * Gets the URL of Minerva.
   *
   * @return The URL of the application server Mineva.
   */
  MinervaURL getMinervaURL() throws MinervaException, RemoteException;
}
