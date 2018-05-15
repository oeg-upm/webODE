package es.upm.fi.dia.ontology.minerva.server;

// Java stuff
import java.util.*;
import java.net.URL;
import java.net.InetAddress;

// RMI stuff
import java.rmi.*;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.admin.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;

/**
 * A session with which the user can access to the services
 * currently running in the server.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.2
 */
public class MinervaSessionImp extends MinervaServiceImp implements MinervaSession, MinervaServerConstants
{
  // Instance variables
  public String username;
  public MinervaGroup[] groups;
  public Date   creationTime;
  private AdministrationServiceImp admService;
  private AuthenticationServiceImp authService;
  private Vector services;

  /**
   * Constructor.
   * <p>
   * It requires the user information to be hold for later
   * service requirements.
   */
  public MinervaSessionImp (String username, MinervaGroup[] groups,
                            AdministrationServiceImp adm,
                            AuthenticationServiceImp authService) throws RemoteException
  {
    this.username = username;
    this.groups   = groups;
    creationTime  = new Date();
    admService    = adm;
    this.authService = authService;
    this.services =  new Vector();

    // Add session
 /*synchronized (sessions) {
   sessions.addElement (this);
   }*/
  }

  /**
   * Gets current user.
   *
   * @return The user for this session.
   */
  public String getUser () {
    return username;
  }

  /**
   * Gets current group.
   *
   * @return The groups for this session.
   */
  public String[] getGroups ()
  {
    String[] astr = new String[groups.length];
    for (int i = 0; i < astr.length; i++)
      astr[i] = groups[i].getName();
    return astr;
  }

  /**
   * Gets the URL of Minerva.
   *
   * @return The URL of the application server Mineva.
   */
  public MinervaURL getMinervaURL() throws MinervaException, RemoteException {
    return this.context.getMinervaURL();
  }

  /**
   * Gets the service with the name given.
   *
   * @param serviceName The service name.
   * @exception AuthenticationException If the user has no access to
   *            the server being requested.
   * @exception NoSuchServiceException If there is no service with the given name.
   * @exception MinervaException In case of other error.
   */
  public MinervaService getService (String serviceName)
      throws AuthenticationException, NoSuchServiceException, MinervaException, RemoteException
  {
    if (serviceName == null)
      throw new MinervaException ("The service name cannot be null.");

    context.logInfo ("request of service " + serviceName + ".");

    // Delegate
    ServiceDescriptor sd = admService.getServiceDescription (serviceName);
    if (!serviceName.equals (ADMINISTRATION_SERVICE) && sd == null)
      throw new NoSuchServiceException ("no such service: " + serviceName);

    if (sd != null && !sd.bRemote)
      throw new NoSuchServiceException ("the service " + serviceName +
                                        " is local.  It cannot be accessed remotely.");

    if (sd != null && sd.isStopped())
      throw new MinervaException ("the service " + serviceName + " is currently stopped.");

    context.logDebug ("About to authenticate...");
    MinervaService ms = authService.getService (serviceName, this);
    if(ms!=null)
      this.services.add(ms);
    return ms;


    // Manage server?
    /*	if (serviceName.equals (ADMINISTRATION_SERVICE) && group.equals (ADMINISTRATION_GROUP))
 return admService;
 else {
    // Delegate
 ServiceDescriptor sd = admService.getServiceDescription (serviceName);
     if (sd == null)
  throw new NoSuchServiceException (serviceName);

     if (!sd.bRemote)
  throw new NoSuchServiceException ("the service " + serviceName +
        " is local.  It cannot be accessed remotely.");

     if (sd.isStopped())
  throw new MinervaException ("the service " + serviceName + " is currently stopped.");
     return authService.getService (serviceName, this);
     }*/
  }

  protected void localDisconnect() {
    MinervaService ms;
    for(; !this.services.isEmpty(); ) {
      ms=(MinervaService)this.services.remove(0);
      try {
        if(ms.isStatefull())
          ms.disconnect();
      }
      catch(Throwable th) {
//          th.printStackTrace();
        context.logError("Error while shuting services.", th);
      }
    }
  }
}



