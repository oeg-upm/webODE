package es.upm.fi.dia.ontology.minerva.server.builtin;

// Java stuff
import java.util.*;

// RMI stuff
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.admin.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;

/**
 * The implementation for the authentication service.
 *
 * @author   Julio César Arpírez Vega
 * @version  1.4
 */
public class AuthenticationServiceImp extends MinervaServiceImp implements AuthenticationService
{
  public static final String SESSION = "session";

  private transient LogServiceImp log;

  private transient Registry registry;
  private transient AdministrationServiceImp admService;
  private transient HashMap services;
  private transient String directory, name;
  private transient String host=null;
  private transient int port;

  // Sweeper thread
  private SweeperThread sweeperThread;

  public AuthenticationServiceImp (LogServiceImp log, String directory, String name, int port) throws RemoteException {
    this.log       = log;
    this.directory = directory;
    this.name      = name;
    this.host      = null;
    this.port      = port;

    // Start sweeper thread
    sweeperThread = new SweeperThread (log);
    sweeperThread.start();
  }
  
  /**
   * Constructor.
   *
   * @param log The logger to be used.
   */
  public AuthenticationServiceImp (LogServiceImp log, String directory, String name,
                                   String host, int port) throws RemoteException {
    this.log       = log;
    this.directory = directory;
    this.name      = name;
    this.host      = host;
    this.port      = port;

    // Start sweeper thread
    sweeperThread = new SweeperThread (log);
    sweeperThread.start();
  }

  public int getPort() {
    return this.port;
  }

  public void setAdministrationService (AdministrationServiceImp adm)
  {
    admService = adm;
  }

  public void setServices (HashMap services)
  {
    this.services = services;
  }

  /**
   * Adds a new service.
   * This method is aimed at keeping synchronized both the adminstration service
   * database and the authenticator's one.
   */
  public void addService (String service)
  {
    // Get configuration
    AuthenticationServiceConfiguration asc =
        (AuthenticationServiceConfiguration) config;

    asc.addService (new MinervaServiceAccess (service));

    try {
      // Commit changes
      asc.setDirectory (directory, name);
      asc.storeConfig();
      // Reconfigure service.
      context.logDebug ("reconfiguring authentication service...");
      admService.reconfigure (MinervaServerConstants.AUTHENTICATION_SERVICE, asc);
      context.logDebug ("Done!");
    } catch (Exception e) {
      context.logError ("error when trying to synchronize authenticator configuration: " +
                        e.getMessage());
    }
  }


    /*
  * Removes an existing service.
  * This method is aimed at keeping synchronized both the adminstration service
  * database and the authenticator's one.
     */
  public void removeService (String service)
  {
    // Get configuration
    AuthenticationServiceConfiguration asc =
        (AuthenticationServiceConfiguration) config;

    asc.removeService (new MinervaServiceAccess (service));
    try {
      // Commit changes
      asc.setDirectory (directory, name);
      asc.storeConfig();
      // Reconfigure service.
      context.logDebug ("reconfiguring authentication service...");
      admService.reconfigure (MinervaServerConstants.AUTHENTICATION_SERVICE, asc);
      context.logDebug ("Done!");
    } catch (Exception e) {
      context.logError ("error when trying to synchronize authenticator configuration: " +
                        e.getMessage());
    }
  }

  // Business methods -----------------------------------------------------

  /**
   * This method provides a valid session once the user has
   * been properly authenticated.
   *
   * @param username The user's login.
   * @param password The user's password.
   * @exception AuthenticationException In case of a wrong user/password pair.
   * @exception RemoteException In case of a communication problem.
   */
  public MinervaSession getSession (String username, String password)
      throws AuthenticationException, RemoteException {
    // Get configuration
    AuthenticationServiceConfiguration asc =
        (AuthenticationServiceConfiguration) config;

    log.logInfo ("new session requested for user " + username + ".");

    MinervaUser mu = asc.getUser (username);
    if (mu != null && mu.isRightPassword (password)) {
      log.logInfo ("session granted for user " + username + ".");
      // return a valid session
      MinervaSessionImp msimp = new MinervaSessionImp (username,       // Administrator's name
          mu.getGroups(), // Group
          admService,     // Administration service.
          this);          // Authentication service.
      // Set a context
      msimp.setContext (new MinervaContext (this, log, msimp, SESSION));
      // Sets the configuration object (it does not have).
      msimp.setConfiguration (null);

      return msimp;
    }
    else {
      // Oops!
      log.logInfo ("session denied for user " + username + ".");
      throw new AuthenticationException ("invalid user/password.");
    }
  }

  /**
   * This method provides a valid session once the user has
   * been properly authenticated.   
   *
   * @param username The user's login.
   * @param messageDigest The digester of the use and its password.
   * @param digest The digest message to compare to.
   * @exception AuthenticationException In case of a wrong user/digest pair.
   * @exception RemoteException In case of a communication problem.
   */
  public MinervaSession getSession (String username, MinervaUserMessageDigest messageDigest, byte[] digest) throws AuthenticationException, RemoteException {
    // Get configuration
    AuthenticationServiceConfiguration asc =
        (AuthenticationServiceConfiguration) config;

    log.logInfo ("new session requested for user " + username + ".");

    MinervaUser mu = asc.getUser (username);
    if (mu != null && mu.isRightDigest(messageDigest, digest)) {
      log.logInfo ("session granted for user " + username + ".");
      // return a valid session
      MinervaSessionImp msimp = new MinervaSessionImp (username,       // Administrator's name
          mu.getGroups(), // Group
          admService,     // Administration service.
          this);          // Authentication service.
      // Set a context
      msimp.setContext (new MinervaContext (this, log, msimp, SESSION));
      // Sets the configuration object (it does not have).
      msimp.setConfiguration (null);

      return msimp;
    }
    else {
      // Oops!
      log.logInfo ("session denied for user " + username + ".");
      throw new AuthenticationException ("invalid user/password.");
    }
  }

  class RSSF implements RMIServerSocketFactory {
    protected final String host;
    public RSSF(String host) { this.host = host; }
    public ServerSocket createServerSocket(int port) throws IOException {
      return new ServerSocket(port, 50, InetAddress.getByName(host));
    }
  }
  class RCSF implements RMIClientSocketFactory, Serializable {
    public Socket createSocket(String host, int port) throws IOException {
      return new Socket(host, port);
    }
  }

  // Life-cycle methods ------------------------------------------------

  /**
   * Starts the authentication service by creating a new registry.
   */
  public void start () throws CannotStartException
  {
    try {
      log.logInfo ("creating registry on port " +
                   MinervaServerConstants.MINERVA_SERVER_PORT + "...");
      if(host==null)
        registry = LocateRegistry.createRegistry (port);
      else
        registry = LocateRegistry.createRegistry (port, new RCSF(), new RSSF(host));

      log.logInfo ("binding...");
      registry.bind (MinervaServerConstants.AUTHENTICATION_SERVICE,
                     this);

      log.logInfo ("done.");
    } catch (Exception e) {
      throw new CannotStartException (e.getMessage(), e);
    }
  }

  // Implementation methods.  Only for access within server -----------------------------------

  /**
   * Release a service from the sweeper.
   */
  public void releaseService(MinervaService ms) {
    sweeperThread.removeService(ms);
  }

  /**
   * Gets a given service.
   */
  public synchronized MinervaService getServiceWithoutAuthentication (String serviceName,
      MinervaSession session)
      throws NoSuchServiceException, MinervaException
  {
    // Check there is a service with that name
    Object serv = services.get (serviceName);
    if (serv == null)
      throw new NoSuchServiceException ("a service with name " + serviceName +
                                        " is not currently available in this server.");

    if (serv instanceof Vector) {
      log.logDebug ("starting stateful service (" + serviceName + ")...");


      Vector v = (Vector) serv;

      //System.out.println ("V.SIZE(): " + v.size());
      MinervaServiceImp msi = null;
      synchronized (v) {

        if (v.isEmpty()) {
          log.logDebug ("No more instances of service " + serviceName + " available.");
          throw new MinervaException
          ("No more instances of service " + serviceName + " available.");
          //v.wait(); --> Poner dos modos...
        }
        log.logDebug (v.size() + " available services in the pool of type " + serviceName + ".");
        // Remove the first service of the pool. FIFO policy
        msi = (MinervaServiceImp) v.elementAt (0);

        // Actually remove element from pool
        v.removeElementAt (0);

        // Notify sweeper thread
        sweeperThread.addService (msi);
      }

      // Init life-cycle (setConfiguration already called).
      msi.setPool (new MinervaPool (services, serviceName));
      msi.setContext (new MinervaContext (this, log, (MinervaSessionImp) session, serviceName));

      try {
        msi.start ();
      } catch (CannotStartException cse) {
        log.logError ("service " + serviceName + " cannot be started.");
        log.logError ("Reason: " + cse.getMessage(), cse);
        throw  new MinervaException (cse.getMessage(), cse);
      }

      return msi;
    }
    else {
      log.logDebug ("starting stateless service (" + serviceName + ")...");
      // Stateless.  Just return current instance...
      // Associate user with service... <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
      return (MinervaService) serv;
    }
  }


  public MinervaService getService (String serviceName,
                                    MinervaSession session)
      throws AuthenticationException, NoSuchServiceException, MinervaException, RemoteException
  {
    // Get configuration
    AuthenticationServiceConfiguration asc =
        (AuthenticationServiceConfiguration) config;

    MinervaUser mu = asc.getUser (session.getUser());
    if (asc.hasAccess (mu, serviceName)) {
      context.logInfo ("service " + serviceName + " granted for user " + mu.getLogin() + ".");
      return getServiceWithoutAuthentication (serviceName, session);
    }
    else {
      context.logInfo ("the user " + mu.getLogin() +
                       " cannot access service " + serviceName + ".");
      throw new AuthenticationException ("The user " + mu.getLogin() +
          " cannot access service " + serviceName + ".");
    }
  }

  protected void localDisconnect() {
  }
}