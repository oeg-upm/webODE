package es.upm.fi.dia.ontology.minerva.server.admin;

// Java stuff
import java.io.*;
import java.util.*;
import java.net.*;

// RMI stuff
import java.rmi.*;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.util.*;


/**
 * This is the <i>Minerva</i> administration service.  Without it,
 * there woundn't be a chance to configure the server remotely.
 * <p>
 * This service is tightly related to the authentication service,
 * since the later does not allow anyone not being an administrator
 * to manage de server.
 *
 * @author   Julio César Arpírez Vega
 * @version  1.2
 * @see      es.upm.fi.dia.ontology.minerva.server.builtin.AuthenticationService
 */
public class AdministrationServiceImp extends MinervaServiceImp implements AdministrationService
{
  /** The suffix for the service managers. */
  public static final String MANAGER = "Manager";

  private LogServiceImp log;
  private ServerConfiguration serverConfig;
  private String configDirectory;
  private HashMap services;
  private String othersDir;
  private AuthenticationServiceImp authService;

  private MinervaClassLoader minervaClassLoader;

  /**
   * Constructor.
   *
   * @param log The logger to use.
   * @param configDir The configuration directory.
   * @param services The services currently present in the server.
   * @param othersDir The directory where 3rd party services will be stored.
   */
  public AdministrationServiceImp (LogServiceImp log,
                                   String configDir,
                                   String othersDir,
                                   HashMap services,
                                   AuthenticationServiceImp authService,
                                   MinervaClassLoader mcl) throws RemoteException
  {
    // Set logger.
    this.log = log;
    this.configDirectory = configDir;
    this.services = services;
    this.othersDir = othersDir;
    this.authService = authService;
    minervaClassLoader = mcl;
  }

  /**
   * Start the administration service.
   */
  public void start () throws CannotStartException
  {
    log.logInfo ("starting administration service...");
    log.logInfo ("reading server configuration...");

    try {
      _readServerConfiguration ();

      // Mark default services as running
      ServiceDescriptor sd = (ServiceDescriptor) serverConfig.getService (MinervaServerConstants.LOG_SERVICE);
      sd.state = sd.STARTED;
      sd = (ServiceDescriptor) serverConfig.getService (MinervaServerConstants.AUTHENTICATION_SERVICE);
      sd.state = sd.STARTED;
    } catch (Exception e) {
      throw new CannotStartException ("unable to start administration service: " + e.getMessage(), e);
    }

    log.logInfo ("Done.");
  }

  /**
   * Function to read server's configuration.
   *
   * @exception Exception In case of error.
   */
  protected void _readServerConfiguration () throws Exception
  {
    ObjectInputStream ois = null;
    AdministrationServiceConfiguration asc = (AdministrationServiceConfiguration) config;

    // Read server's configuration
    try {
      serverConfig = ServerConfiguration.loadServerConfiguration (asc.configFile);
    } catch (FileNotFoundException fnfe) {
      log.logInfo ("server configuration not found.  Creating one.");

      serverConfig = _createDefaultConfiguration (asc);
    } catch (Exception e) {
      log.logError ("error loading server configuration file: " + e.getMessage(), e);
      throw e;
    } finally {
      try {
        ois.close();
        } catch (Exception e) {}
    }
    log.logDebug ("updating classpath to: " + serverConfig.getClassPath());
    System.setProperty ("java.class.path", serverConfig.getClassPath());
  }

  /**
   * Creates a default server configuration containing the authentication
   * and the log services.
   */
  private ServerConfiguration _createDefaultConfiguration (AdministrationServiceConfiguration asc) throws Exception
  {
    ServerConfiguration sc = new ServerConfiguration ();

    sc.addService (asc.logService);
    sc.addService (asc.authService);
    sc.setClassPath (""); //System.getProperty ("java.class.path"));

    sc.storeServerConfiguration (asc.configFile);

    return sc;
  }


  /**
   * Gets the services currently installed in the server.
   *
   * @return The descriptors of the services installed.
   */
  public synchronized ServiceDescriptor[] getServices () throws RemoteException
  {
    log.logDebug ("AdministrationServiceImp: getServices().");

    return (ServiceDescriptor[]) ServerUtils.iteratorToArray(serverConfig.getServices());
  }

  private synchronized ClassLoader _getClassLoader ()
  {
    try {
      String classpath = System.getProperty("java.class.path");

      if (classpath == null || classpath.trim().length() == 0) { 
        if(this.minervaClassLoader!=null)
          return this.minervaClassLoader;
        else
          return ClassLoader.getSystemClassLoader();
      }
      Vector v = new Vector();
      Vector cURLs = null;
      if (minervaClassLoader != null) {
        cURLs = new Vector ();
        URL[] xx = minervaClassLoader.getURLs();
        for (int i = 0; i < xx.length; i++)
          cURLs.addElement (xx[i]);
      }
      StringTokenizer strTok = new StringTokenizer (classpath, System.getProperty ("path.separator"));
      while (strTok.hasMoreElements()) {
        URL kk = new URL ("file:////" + new File (othersDir, strTok.nextToken()).toString());
        if (cURLs != null && !cURLs.contains (kk)) {
          minervaClassLoader.addURL (kk);
        }
        else
          v.addElement (kk);

        log.logDebug ("URL: " + kk);
      }
      
      if (minervaClassLoader == null) {
        URL[] aurl = new URL[v.size()];
        v.copyInto (aurl);
        minervaClassLoader = new MinervaClassLoader (aurl, ClassLoader.getSystemClassLoader ());
      }

      return minervaClassLoader;
    } catch (Exception e) {
      log.logError ("error creating MinervaClassLoader: " + e + ".", e);

      return ClassLoader.getSystemClassLoader();
    }
  }

  /**
   * Manage a service.
   *
   * @param name The name of the service to manage.
   * @exception NotManageableException If the service has no manager.
   * @exception MinervaException In case of other error (e.g. attempt
   *            to load the service failure).
   */
  public synchronized MinervaManager getServiceManager (String name)
      throws RemoteException, NotManageableException, MinervaException
  {
    log.logDebug ("AdministrationServiceImp: getServiceManager().");

    // To get the manager for a service, we build an instance of
    // a class named as the service but with a Manager suffix.
    ServiceDescriptor sd = serverConfig.getService (name);

    if (sd == null)
      throw new MinervaException ("no such service: " + name + ".");

    try {
      // log
      log.logDebug ("AdministrationService: trying to load " + sd.interfaceName + MANAGER + " class.");
      if (name.equals (MinervaServerConstants.LOG_SERVICE))
        Class.forName ("es.upm.fi.dia.ontology.minerva.server.builtin.LogConfiguration", true, _getClassLoader());
      else
        Class.forName (sd.interfaceName + "Configuration", true, _getClassLoader());

      // load class
      Class cl = Class.forName (sd.interfaceName + MANAGER, true, _getClassLoader());
      MinervaManagerImp mmag = (MinervaManagerImp) cl.newInstance();

      // Load configuration.
      MinervaServiceConfiguration msc = MinervaServiceConfiguration.loadConfig (configDirectory, name);
      msc.setDirectory (configDirectory, name);

      // Init life-cycle.------
      // Set context
      MinervaContext serviceContext=new MinervaContext(this.authService, this.log, null, name);
      mmag.setContext (serviceContext);
      // set configuration object
      mmag.setConfiguration (msc);
      // Set the administrator
      mmag.setAdministrator (this, name);
//      mmag.setAuthentication(this.authService);

      log.logDebug ("done");

      return mmag;
    } catch (ClassNotFoundException cnfe) {
      throw new NotManageableException ("the service " + name + " doesn't seem to have a manager.", cnfe);
    } catch (Exception e) {
      throw new MinervaException ("Error: " + e.getMessage());
    }
  }

  /**
   * Install a new service.
   *
   * @param sd The service descriptor.
   * @param classDefinition The jar file containing the service or null if not specified
   *                        (the service will have to be in an already installed jar file).
   * @exception NameAlreadyBoundException In case of trying to set a service
   *            with an existing name.
   */
  public void installService (ServiceDescriptor sd, byte[] classDefinition)
      throws NameAlreadyBoundException, MinervaException, RemoteException
  {
    log.logInfo ("Request to install service " + sd.name + ".");
    log.logDebug ("Type of service: " + sd.type);

    String jarFile;
    if (classDefinition != null) {
      jarFile = System.currentTimeMillis() +".jar";
      sd.jarFile = jarFile;
    }
    else {
      jarFile = null;
      sd.jarFile = null;
    }

    synchronized (serverConfig) {
      // Check if a service with the same name exists.
      if (serverConfig.getService (sd.name) != null)
        throw new NameAlreadyBoundException ("there is already a service called " + sd.name + ".");

      serverConfig.addService (sd);
    }

    String newClasspath = null;
    // Put the jar file in the appropiate directory
    if (classDefinition != null) {
      try {
        _saveJarFile (jarFile, classDefinition);
      } catch (Exception e) {
        synchronized (serverConfig) {
          serverConfig.removeService (sd);
        }
        log.logError ("error storing jar file: " + e.getMessage(), e);
        throw new MinervaException ("error storing jar file: " + e.getMessage());
      }
      // Update classpath;
      newClasspath = System.getProperty("java.class.path") +
                     System.getProperty ("path.separator") + jarFile;
    }

    // Synchronize server configuration with persistent storage.
    synchronized (serverConfig) {
      try {
        AdministrationServiceConfiguration asc = (AdministrationServiceConfiguration) config;
        if (newClasspath != null)
          serverConfig.setClassPath (newClasspath);
        serverConfig.storeServerConfiguration (asc.configFile);
      } catch (Exception e) {
        log.logError ("Error synchronizing server configuration: " + e.getMessage(), e);
        throw new MinervaException ("Error synchronizing server configuration: " + e.getMessage());
      }
    }

    if (newClasspath != null) {
      // Update class path
      System.getProperties().setProperty ("java.class.path", newClasspath);
    }

    log.logDebug ("NEW CLASSPATH: " + System.getProperty ("java.class.path"));

    log.logInfo ("Service " + sd.name + " installed successfully.");

    // Create a default configuration for the service
    createConfiguration (sd);

    sd.state = sd.STOPPED;

    // Notify authenticator about the inclusion of a new service
    log.logDebug ("notifying authenticator...");
    authService.addService (sd.name);
    log.logDebug ("Done.");

 /*synchronized (services) {
   services.put (sd.name, sd);
   }	*/
  }

  /**
   * Creates a default configuration for a service.
   */
  private void createConfiguration (ServiceDescriptor sd)
  {
    try {
      log.logDebug ("Creating configuration for service " + sd.name + ".");
      Class cl1 = Class.forName (sd.interfaceName + "Configuration", true, _getClassLoader());
      MinervaServiceConfiguration msc =  (MinervaServiceConfiguration) cl1.newInstance();
      msc.setDirectory (configDirectory, sd.name);
      msc.storeConfig();

      if (sd.type == MinervaServiceConfiguration.STATELESS)
        msc.setStateless();
      else
        msc.setStateful();

      log.logDebug ("Done!");
    } catch (Exception e) {
      log.logError ("error creating configuration for service " + sd.name + ": " + e, e);
    }
  }

  /**
   * Stores a jar file into the directory for 3rd party services.
   */
  private void _saveJarFile (String jarFile, byte[] classDefinition) throws IOException
  {
    BufferedOutputStream bos = null;
    try {
      // Check 3rdparty directory is created
      File ffoo = new File (othersDir);
      if (!ffoo.exists())
        ffoo.mkdir();

      bos = new BufferedOutputStream (new FileOutputStream (othersDir + File.separator + jarFile));

      bos.write (classDefinition);
      bos.flush();
    } finally {
      try {
        bos.close();
        } catch (Exception e) {}
    }
  }

  /**
   * Remove a service.
   *
   * @param name The name of the service to be removed.
   * @exception MinervaException In case of error during removal.
   */
  public void removeService (String name) throws MinervaException, RemoteException
  {
    log.logInfo ("Request to remove service " + name + ".");
    if (name != null && (name.equals (MinervaServerConstants.AUTHENTICATION_SERVICE) ||
                         name.equals (MinervaServerConstants.ADMINISTRATION_SERVICE) ||
                         name.equals (MinervaServerConstants.LOG_SERVICE)))
      throw new MinervaException ("Service " + name + " cannot be removed.");

    // Stop the service
    stopService (name);

    try {
      AdministrationServiceConfiguration asc = (AdministrationServiceConfiguration) config;
      serverConfig.removeService (serverConfig.getService (name));
      serverConfig.storeServerConfiguration (asc.configFile);
    } catch (Exception e) {
      log.logError ("error removing service: " + e.getMessage() + ".", e);
      return;
    }

    services.remove (name);
    // Notify authenticator about the removal of an existing service
    authService.removeService (name);

    // Remove configuration
    File f = new File (configDirectory, name + ".cfg");
    f.delete();

    log.logInfo ("service " + name + " removed successfully.");
  }

  /**
   * Stops the server.
   */
  public void stopServer () throws RemoteException
  {
    log.logInfo ("stopping server...");

    // Services should be stopped.
    Object[] ao = services.values().toArray();
    for (int i = 0; i < ao.length; i++) {
      if (ao[i] instanceof MinervaServiceImp)
        ((MinervaServiceImp) ao[i]).stop();
      else {
        Vector v = (Vector) ao[i];
        for (int j = 0; j < v.size(); j++)
          ((MinervaServiceImp) v.elementAt (j)).stop();
      }
    }

    System.err.println ("Minerva Server: stopped.");

    new Thread() {
      public void run() {
        try {
          sleep (5000);
          } catch (Exception e) {}
          System.exit (0);
      }
      }.start();
  }

  /**
   * Stops a given service.
   *
   * @param serviceName The name of the service to be stopped.
   */
  public void stopService (String serviceName) throws MinervaException, RemoteException
  {
    ServiceDescriptor sd = (ServiceDescriptor) serverConfig.getService (serviceName);

    if (sd.isStopped()) return;

    Object obj = (Object) services.get (serviceName);
    if (obj == null)
      throw new MinervaException ("No such service: " + serviceName + ".");
    if (obj instanceof Vector) {
      Vector v = (Vector) obj;
      for (int i = 0; i < v.size(); i++) {
        ((MinervaServiceImp) v.elementAt(i)).stop();
      }
    }
    else {
      MinervaServiceImp ms = (MinervaServiceImp) obj;
      ms.stop();
    }
    sd.state = sd.STOPPED;
  }

  /**
   * Reconfigures a service by changing its config object.
   *
   * @param serviceName The name of the service to be reconfigured.
   */
  public void reconfigure (String serviceName, MinervaServiceConfiguration msc)
      throws MinervaException, RemoteException
  {
    ServiceDescriptor sd = (ServiceDescriptor) serverConfig.getService (serviceName);

    // If the service is stopped, there is no need to reconfigure it.
    if (sd.isStopped()) {
      // Start service with the new configuration
      startService (serviceName);
    }
    else {
      Object obj = (Object) services.get (serviceName);
      if (obj == null)
        throw new MinervaException ("No such service: " + serviceName + ".");
      if (obj instanceof Vector) {
        Vector v = (Vector) obj;
        for (int i = 0; i < v.size(); i++) {
          ((MinervaServiceImp) v.elementAt(i)).setConfiguration (msc);
        }
      }
      else {
        MinervaServiceImp ms = (MinervaServiceImp) obj;
        ms.setConfiguration (msc);
      }
    }
  }

  /**
   * Starts a service.
   *
   * @param serviceName The name of the service to be started.
   */
  public void startService (String serviceName) throws MinervaException, RemoteException
  {
    Object obj = (Object) services.get (serviceName);
    if (obj == null) {
      try {
        // Try to start in now.
        startService (serverConfig.getService(serviceName));
      } catch (Exception e) {
        throw new MinervaException (e.getMessage());
      }
      return;
    }

    // Start only if stopped.
    if (serverConfig.getService (serviceName).isStopped()) {
      if (obj instanceof Vector) {
        Vector v = (Vector) obj;
        for (int i = 0; i < v.size(); i++) {
          ((MinervaServiceImp) v.elementAt(i)).start();
        }
      }
      else {
        MinervaServiceImp ms = (MinervaServiceImp) obj;
        ms.start();
      }
      ServiceDescriptor sd = (ServiceDescriptor) serverConfig.getService (serviceName);
      sd.state = sd.STARTED;
    }
  }

  /**
   * Start all services but the log and authenticator ones.
   */
  public void startAllServices (HashMap services)
  {
    ArrayList asd=new ArrayList();
    for (Iterator it = serverConfig.getServices(); it.hasNext(); )
      asd.add(it.next());

    for(Iterator it=asd.iterator(); it.hasNext(); ) {
      ServiceDescriptor sd = (ServiceDescriptor) it.next();
      if((sd=serverConfig.getService(sd.name))!=null) {
        try {
          loadService(sd);
        } catch (Exception e) {
          log.logError ("error starting service " + sd.name + " due to the following problem: " + e, e);
          //e.printStackTrace();
          sd.state = sd.STOPPED;
        }
      }
    }

    for(Iterator it=asd.iterator(); it.hasNext(); ) {
      ServiceDescriptor sd = (ServiceDescriptor) it.next();
      if((sd=serverConfig.getService(sd.name))!=null) {
        try {
          startService (sd);
        } catch (Exception e) {
          log.logError ("error starting service " + sd.name + " due to the following problem: " + e, e);
          //e.printStackTrace();
          sd.state = sd.STOPPED;
        }
      }
    }
  }

  /**
   * Starts a given service.
   */
  private void startService (ServiceDescriptor sd) throws Exception
  {
    if(!sd.isStopped())
      return;
    if(services.get(sd.name)==null)
      loadService(sd);
    Object value = services.get(sd.name);

    // Create some new instances (as many as the user specified).
    if (value instanceof Vector) {
      MinervaServiceImp[] ao = (MinervaServiceImp[])((Vector)value).toArray(new MinervaServiceImp[0]);
      for (int i=0; i<ao.length; i++) {
        // Start service
        ao[i].start();
      }
    }
    else {
      MinervaServiceImp msi = (MinervaServiceImp) value;

      // Life-cycle.
      msi.start();

      services.put (sd.name, msi);
    }

    // HABRIA QUE PARAR LOS QUE SE HAN PUESTOP En marcha en caso de error. <<<<<<<<<<<<<<<<
    sd.state = sd.STARTED;

  }

  /**
   * Starts a given service.
   */
  private void loadService (ServiceDescriptor sd) throws Exception
  {
    if (sd.name.equals ("authenticator") || sd.name.equals ("log") ||
        sd.name.equals ("administrator"))
      return;

    log.logDebug ("starting service " + sd.name + ".");

    // Try to load the class
    log.logDebug ("trying to load class " + sd.className);
//    Class cl = Class.forName (sd.className, true, _getClassLoader());
    Class cl=_getClassLoader().loadClass(sd.className);
    log.logDebug ("Done!");

    // Load configuration
    // The name of the configuration file will be the one of the service
    MinervaServiceConfiguration msc = null;
    Class cl1 = null;
    try {
      msc = MinervaServiceConfiguration.loadConfig (configDirectory, sd.name);
      msc.setDirectory (configDirectory, sd.name);
      //} catch (ClassNotFoundException cnfe) {
      //log.logError ("fatal error loading configuration for " + sd.name + ": " + cnfe + ".");
    } catch (Exception ioe) {
      log.logInfo ("no configuration found for service " + sd.name + ".  Creating default one...");

      // The configuration class has a name equal to the service's plus
      // Configuration
      log.logDebug ("trying to load class " + sd.interfaceName + "Configuration...");
      cl1 = Class.forName (sd.interfaceName + "Configuration", true, _getClassLoader());
      log.logDebug ("Done!");

      msc =  (MinervaServiceConfiguration) cl1.newInstance();
      msc.setDirectory (configDirectory, sd.name);
      msc.storeConfig();
      log.logInfo ("default configuration created for service " + sd.name + ".");
    }
    Vector v = new Vector();

    log.logDebug ("Type of service " + sd.name + ": " + sd.type);
    log.logInfo ("starting " +
                 (sd.type == MinervaServiceConfiguration.STATEFUL ? sd.nInstances : 1)
                 + " instances of service " + sd.name + ".");

    // Create some new instances (as many as the user specified).
    if (sd.type == MinervaServiceConfiguration.STATEFUL) {
      for (int i = 0; i < sd.nInstances; i++) {
        MinervaServiceImp msi = (MinervaServiceImp) cl.newInstance();

        // Life-cycle.
        // Set configuration
        msi.setConfiguration (msc);

        // Set pool
        msi.setPool (new MinervaPool (services, sd.name));

        // Set context
        msi.setContext (new MinervaContext (authService, log, null, sd.name));

        v.addElement (msi);

        // Notify sweeper thread
        //sweeperThread.addService (msi);
      }
      // Update table.
      services.put (sd.name, v);
    }
    else {
      MinervaServiceImp msi = (MinervaServiceImp) cl.newInstance();

      // Life-cycle.
      msi.setConfiguration (msc);
      msi.setContext (new MinervaContext (authService, log, null, sd.name));

      services.put (sd.name, msi);
    }

    // HABRIA QUE PARAR LOS QUE SE HAN PUESTOP En marcha en caso de error. <<<<<<<<<<<<<<<<
    sd.state = sd.STOPPED;

  }

  /**
   * Returns the attributes of a service.
   *
   * @param  name The name of the service.
   * @return The attributes of a service. <tt>Null</tt> if not found.
   */
  public ServiceDescriptor getServiceDescription (String name)
  {
    return serverConfig.getService (name);
  }

  /**
   * Gets the server's version.
   *
   * @return The server's version.
   */
  public String getServerVersion () throws RemoteException
  {
    return MinervaServerConstants.MINERVA_VERSION;
  }

  protected void localDisconnect() {
  }
}
