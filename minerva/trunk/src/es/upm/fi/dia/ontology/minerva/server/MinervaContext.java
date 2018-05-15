package es.upm.fi.dia.ontology.minerva.server;

import es.upm.fi.dia.ontology.minerva.server.builtin.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;

import java.net.URL;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

/**
 * This class defines the server context, that is, the means by which
 * services communicate with the server and with each other.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class MinervaContext
{
  public static final String SERVER = "server";

  private AuthenticationServiceImp authService;
  private LogService logService;
  private MinervaSessionImp session;
  private String serviceName;

  public MinervaContext (AuthenticationServiceImp authService,
                         LogService logService,
                         MinervaSessionImp session,
                         String serviceName)
  {
    this.authService = authService;
    this.logService  = logService;
    this.session     = session;
    this.serviceName = serviceName;
  }

  /**
   * Log errors.
   */
  public void logError (String str)
  {
    try {
      logService.logError (str);
      } catch (Exception e) {}
  }

  /**
   * Log errors.
   */
  public void logError (String str, Throwable th)
  {
    try {
      logService.logError (str, th);
      } catch (Exception e) {}
  }

  /**
   * Log debug information.
   */
  public void logDebug (String str)
  {
    try {
      logService.logDebug (str);
    } catch (Exception e) {
    }
  }

  /**
   * Log descriptive information.
   */
  public void logInfo (String str)
  {
    try {
      logService.logInfo (str);
      } catch (Exception e) {}
  }

  /**
   * Gets who is using the service now.
   */
  public String getUser ()
  {
    if (session == null)
      return SERVER; //<<<<<<<<<<<<<<<<<<<<<<<<<<
    else
      return session.username;
  }

  /**
   * Gets the first group of who is using the service.
   */
  public String getGroup ()
  {
    if (session == null)
      return SERVER;  // <<<<<<<<<<<<<<<<<<<<<<
    else
      return session.groups[0].getName();
  }

  /**
   * Gets all the groups.
   */
  public String[] getGroups()
  {
    if (session == null)
      return new String[] { SERVER };

    String[] astr = new String[session.groups.length];
    for (int i = 0; i < astr.length; i++)
      astr[i] = session.groups[i].getName();
    return astr;
  }

  /**
   * Gets a service.
   *
   * @param name The name of the service to be retrieved.
   * @exception NoSuchServiceException If there is no service with the given name.
   * @exception MinervaException In case of other error.
   */
  public MinervaService getService (String name)
      throws NoSuchServiceException, MinervaException
  {
    return authService.getServiceWithoutAuthentication (name, session);
  }

  public void releaseService(MinervaService ms) {
    authService.releaseService(ms);
  }

  /**
   * Returns the service's name.
   *
   * @return The service's name.
   */
  public String getName ()
  {
    return serviceName;
  }

  public MinervaURL getMinervaURL() throws MinervaException {
    try {
      return new MinervaURL(InetAddress.getLocalHost().getCanonicalHostName(), this.authService.getPort());
    }
    catch (UnknownHostException ex) {
      throw new MinervaException("It couldn't form a correct URL. Please, contact with the administrator to report the issue", ex);
    }
//    catch (MalformedURLException ex) {
//      throw new MinervaException("It couldn't form a correct URL. Please, contact with the administrator to report the issue", ex);
//    }
  }
}
