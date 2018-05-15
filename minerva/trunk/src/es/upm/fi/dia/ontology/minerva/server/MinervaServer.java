package es.upm.fi.dia.ontology.minerva.server;

//Java stuff
import java.rmi.RemoteException;
import java.util.*;
import java.io.*;
import java.net.*;

//Own stuff
import es.upm.fi.dia.ontology.minerva.client.MinervaClient;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;
import es.upm.fi.dia.ontology.minerva.server.admin.*;


/**
 * This class provides the necessary mechanisms to start the
 * <i><b>Minerva Application Server</b></i>.
 *
 * @author  Julio César Arpírez Vega
 * @version 1.2
 */
public class MinervaServer implements MinervaErrorCodes, MinervaServerConstants
{
  // Variables
  private static String minervaHome, minervaEtc, minervaVar, minerva3rdParty, minervaClasses;
  private static String host=null;
  private static int port;
  private static LogServiceImp logService;
  private static AuthenticationServiceImp authService;
  private static AdministrationServiceImp admService;
  private static HashMap services = new HashMap();

  /**
   * Start up the server.
   * <p>
   * The steps followed are shown below:
   * <ul>
   *  <li>Get environment variables (HOME, ETC, VAR).
   *  <li>Create MinervaClassLoader.
   *  <li>Read configuration object for the log service.  If no one exists,
   *      create a default object.
   *  <li>Start log service with the previous configuration object.
   *  <li>Read configuration object for the authentication service.  It there
   *      is no configuration, create a default one.
   *  <li>Start authentication service.
   *  <li>Start RMI registry.
   *  <li>Read server's configuration file to see what other services
   *      must be started.
   *  <li>Start these services by reading their configurarion files
   *      and invoking the life-cycle methods.
   *  <li>The server is now ready.
   */
  protected static void start() {
    _log ("starting up...  Please wait.");

    // Let's get environment.
    if (!_getEnvironment ())
      System.exit (ERR_ENVIRONMENT);

    // Create class loader --> for configurations
    MinervaClassLoader mcl = null;
    try {
      File thirdPartyDir=new File(minerva3rdParty);
      File classesDir=new File(minervaClasses);
      mcl=new MinervaClassLoader(new URL[]{thirdPartyDir.toURI().toURL(), classesDir.toURI().toURL()});
      loadLibraries(mcl, thirdPartyDir);
      loadLibraries(mcl, classesDir);

      // The configuration class must be loaded using this classpath
      // so that the server can work properly.
      //Class.forName ("minerva.server.services.MinervaServiceConfiguration", true, mcl);
      MinervaServiceConfiguration.cl = mcl;
    } catch (Exception e) {
      System.out.println (new Date() + ": unexpected error: " + e + ".");
      System.exit (ERR_CLASS_LOADER);
    }

    // Start log service.
    if (!_startLog())
      System.exit (ERR_LOG_SERVICE);

    logService.logInfo ("-------------------------------------------");
    logService.logInfo ("Starting up...");
    logService.logInfo ("-------------------------------------------");

    // Start authentication service.
    if (!_startAuthenticator ())
      System.exit (ERR_AUTHENTICATION_SERVICE);

    if (!_startAdministrator(mcl))
      System.exit (ERR_ADMINISTRATION_SERVICE);

    // Notify authenticator there is an administration service running.
    authService.setAdministrationService (admService);

    _log ("Done! Check log files for details!");

    logService.logInfo ("-------------------------------------------");
    logService.logInfo ("New session started.  Server status: ready.");
    logService.logInfo ("-------------------------------------------");
    logService.logInfo ("Starting additional services...");

    // Start all services and add the administrator to the pool of available
    // ones.
    services.put (ADMINISTRATION_SERVICE, admService);
    admService.startAllServices(services);
  }

  private static void loadLibraries(MinervaClassLoader cloader, File dir) throws Exception {
    if(cloader!=null && dir!=null && dir.exists()) {
      File[] libs=dir.listFiles(new FileFilter() {
        public boolean accept(File f) {
          return f!=null && f.isFile() && (f.getName().toLowerCase().endsWith(".jar") || f.getName().toLowerCase().endsWith(".zip"));
        }
      });
      for(int i=0; i<libs.length; i++)
        cloader.addURL(libs[i].toURI().toURL());
    }
  }

  protected static void stop(MinervaURL url, String user, String password) throws AuthenticationException, RemoteException, MinervaException {
    MinervaSession ms=MinervaClient.getMinervaSession(url, user, password);
    AdministrationService admin=MinervaClient.getAdministrator(ms);
    admin.stopServer();
  }

  public static void main (String[] args) throws Exception {
    String command=null, user=null, password=null, host=null, port=null;
    boolean error=false;
    for(int i=0; !error && i<args.length; i++) {
      if(args[i].equals("-user"))
        if((i+1)>=args.length)
          error=true;
        else
          user=args[++i];
      else if(args[i].equals("-password"))
        if((i+1)>=args.length)
          error=true;
        else
          password=args[++i];
      else if(args[i].equals("-host"))
        if((i+1)>=args.length)
          error=true;
        else
          host=args[++i];
      else if(args[i].equals("-port"))
        if((i+1)>=args.length)
          error=true;
        else
          port=args[++i];
      else {
        command=args[i];
      }
    }
    error=error || (command!=null && command.equals("stop")&& (user==null || password==null));
    if(!error) {
      if(host==null) host="localhost";
      if(port==null) port="2000";
      MinervaURL url=new MinervaURL(host, Integer.parseInt(port));
      if(command!=null && command.endsWith("stop"))
        stop(url, user, password);
      else
        start();
    }
    else {
      System.err.println(MinervaServer.class.toString() + " [stop -user <<user>> -password <<password>> [-host <<host>>] [-port <<port>>]]");
    }
  }

  /**
   * Method to log to standard error.
   */
  private static void _log (String str)
  {
    System.err.println (new Date() + ".  Minerva Server: " + str);
  }

  /**
   * Start up the log service.
   */
  private static boolean _startLog ()
  {
    _log ("starting up log service...");

    MinervaServiceConfiguration logConfig = null;
    try {
      logConfig = MinervaServiceConfiguration.loadConfig (minervaEtc + File.separator + CFG_DIR, LOG_SERVICE);
    } catch (Exception e) {
      // It is the first time the server starts or an error has
      // happened.
      // We create a LogConfiguration and start the log service with it.
      _log ("creating new configuration for the log service.");
      logConfig = new LogConfiguration (minervaVar + File.separator + LOG_DIRECTORY, // Log directory
          PREFFIX_LOG,                                 // Log preffix
          EXT_LOG,                                     // Log extension
          LogService.INFORMATIVE_LEVEL);               // Log level

      // Save current configuration
      try {
        MinervaServiceConfiguration.storeConfig (minervaEtc + File.separator + CFG_DIR, LOG_SERVICE, logConfig);
      } catch (Exception e1) {
        _log ("error storing log service configuration: " + e1.getMessage());
        return false;
      }
    }

    try {
      MinervaContext context;
      // The context for this services is not valid.
      context = new MinervaContext(null, logService, null, LOG_SERVICE);
      // Start service.
      _startService (logService = new LogServiceImp(), logConfig, context);
    } catch (Exception e1) {
      _log ("error starting log service: " + e1.getMessage());
      return false;
    }


    return true;
  }


  /**
   * Starts the authentication service.
   */
  public static boolean  _startAuthenticator ()
  {
    _log ("starting up authentication service...");

    MinervaServiceConfiguration authConfig = null;
    try {
      authConfig = MinervaServiceConfiguration.loadConfig
      (minervaEtc + File.separator + CFG_DIR, AUTHENTICATION_SERVICE);

    } catch (Exception e) {
      // No configuration file available.
      _log ("creating new configuration for the authentication service...");
      authConfig = new AuthenticationServiceConfiguration  ();
      authConfig.setDirectory (minervaEtc + File.separator + CFG_DIR,
          AUTHENTICATION_SERVICE);

      // Save current configuration
      try {
        MinervaServiceConfiguration.storeConfig (minervaEtc + File.separator + CFG_DIR,
            AUTHENTICATION_SERVICE, authConfig);
      } catch (Exception e1) {
        _log ("error storing authentication service configuration: " + e1.getMessage());
        logService.logError ("error storing authentication service configuration: " + e1.getMessage(), e1);
        return false;
      }
    }

    try {
      MinervaContext context;
      context = new MinervaContext(null, logService, null, AUTHENTICATION_SERVICE);
      // Start service.
      _startService (authService = new AuthenticationServiceImp(logService,
          minervaEtc + File.separator + CFG_DIR,
          AUTHENTICATION_SERVICE, host, port),
          authConfig, context);
    } catch (Exception e1) {
      _log ("error starting authentication service: " + e1.getMessage());
      logService.logError ("error starting authentication service: " + e1.getMessage(), e1);
      return false;
    }
    // Update services table and notify authentication service...
    services.put (AUTHENTICATION_SERVICE, authService);
    authService.setServices (services);

    return true;
  }

  /**
   * Start the administration service.
   */
  private static boolean _startAdministrator(MinervaClassLoader mcl)
  {
    // Build a table containing all the available services...
    services.put (LOG_SERVICE, logService);

    _log ("starting up administration service...");
    String foo1, foo2;
    try {
      admService = new AdministrationServiceImp (logService, minervaEtc + File.separator + CFG_DIR,
          minerva3rdParty,
          services, authService, mcl);
      MinervaContext context;
      context = new MinervaContext(authService, logService, null, ADMINISTRATION_SERVICE);
      _startService (admService,   // The service
          new AdministrationServiceConfiguration  /*Its configuration*/
          (minervaEtc + File.separator + MAS_CFG_DIR + File.separator + "minerva.cfg",
              new ServiceDescriptor (LOG_SERVICE,
                  foo1 = logService.getClass().getName(),
                  foo1.substring (0, foo1.length() - 3)),  /*To get the interface*/
                  new ServiceDescriptor (AUTHENTICATION_SERVICE,
                      foo2 = authService.getClass().getName(),
                      foo2.substring (0, foo2.length() - 3))),  /*To get the interface*/
                      context);  // The context
    } catch (Exception e) {
      return false;
    }
    return true;
  }


  /**
   * Get environment from command line.
   */
  private static boolean _getEnvironment ()
  {
    minervaHome = System.getProperty (MINERVA_HOME);
    minervaEtc  = System.getProperty (MINERVA_ETC);
    minervaVar  = System.getProperty (MINERVA_VAR);
    minerva3rdParty = System.getProperty (MINERVA_3RDPARTY);
    minervaClasses = System.getProperty (MINERVA_CLASSES);
    host = System.getProperty (MINERVA_HOST);

    // Get port
    try {
      port = Integer.parseInt (System.getProperty (MINERVA_PORT));
    } catch (Exception e) {
      port = MINERVA_SERVER_PORT;
    }

    _log ("Settings ----------------------------------");
    _log ("MINERVA_HOME        = " + minervaHome);
    _log ("MINERVA_ETC         = " + minervaEtc);
    _log ("MINERVA_VAR         = " + minervaVar);
    _log ("MINERVA_3RDPARTY    = " + minerva3rdParty);
    _log ("MINERVA_CLASSES     = " + minervaClasses);
    if(host!=null) _log ("MINERVA_HOST        = " + host);
    _log ("MINERVA_SERVER_PORT = " + port);
    _log ("-------------------------------------------");

    if (minervaHome == null || minervaEtc == null || minervaVar == null || minerva3rdParty == null) {
      _log ("cannot start due to environment problems (check the " +
          MINERVA_HOME + ", " + MINERVA_ETC + ", " + MINERVA_VAR + " and " +
          MINERVA_3RDPARTY + " variables).");
      return false;
    }

    return true;
  }

  /**
   * Starts a service.
   * <p>
   * This method is very important, since it established the life-cycle for
   * services.
   */
  private static void _startService (MinervaServiceImp msi, MinervaServiceConfiguration msc,
      MinervaContext mc) throws CannotStartException {
    // Set context
    msi.setContext (mc);

    // Set configuration
    msi.setConfiguration (msc);

    // Start service
    msi.start();
  }
}
