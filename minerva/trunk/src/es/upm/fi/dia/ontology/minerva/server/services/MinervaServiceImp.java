package es.upm.fi.dia.ontology.minerva.server.services;

// Java stuff
import java.rmi.*;
import java.rmi.server.*;
import java.lang.reflect.Field;
// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.management.MinervaManager;
import es.upm.fi.dia.ontology.minerva.server.rmi.*;

/**
 * All Minerva Services must extend this class and implement an interface
 * inheriting from <tt>MinervaService</tt>.
 * <p>
 * Refer to the "Writing services" section in the user's manual for
 * further information.
 * <p>
 *
 * @author   Julio César Arpírez Vega
 * @version  1.2
 */
public abstract class MinervaServiceImp extends UnicastRemoteObject
    implements MinervaService {
  /** Variable holding current context. */
  protected MinervaContext context;
  /** Configuration object for the service. */
  protected MinervaServiceConfiguration config;

  protected boolean bDebug = false;

  private MinervaPool pool;
  private boolean bDisconnecting;

  private transient long lastAccess = System.currentTimeMillis();;
  private transient Object sem = new Object();

  /**
   * Constructor.
   * <p>
   * Just to fulfil the contract with RMI.
   */
  public MinervaServiceImp () throws RemoteException
  {
 /*	UnicastRemoteObject.exportObject (this, 0, // Use an anonymous port
  new MinervaClientSocketFactory (this),  // Factory for client sockets.
  new MinervaServerSocketFactory (this)); // Factory for server sockets.*/
  }


  // Life-cycle section ---------------------------------------------------------------
  // Note these methods are not exported.

  /**
   * This method sets the context to provide the service with a means
   * to communicate with the server and other services.
   *
   * @param context The server context.
   */
  public void setContext (MinervaContext context)
  {
    if (bDebug)
      System.err.println ("setContext()");

    this.context = context;
  }

  /**
   * Sets the configuration for this service as established by the
   * administration in the <i>Minerva Management Console</i>.
   *
   * @param config The configuration object.
   */
  public void setConfiguration (MinervaServiceConfiguration config)
  {
    if (bDebug)
      System.err.println ("setConfiguration()");

    this.config = config;
  }

  /**
   * For internal use.
   * <p>
   * Sets the pool of services of this type.
   */
  public final void setPool (MinervaPool pool)
  {
    this.pool = pool;
  }

  /**
   * This method is called every time the service is started.  It's assured
   * it is invoked when the service is stopped, either because the server
   * is starting or because the service has been restarted from the
   * <i>Minerva Management Console</i>.
   * <p>
   * The default implementation simply does nothing.
   *
   * @exception CannotStartException If the service cannot start due to any
   *            reason (e.g. misconfiguration).
   */
  public void start () throws CannotStartException
  {
    if (bDebug)
      System.err.println ("start()");
  }

  /**
   * Stops the service.
   * <p>
   * This method can be invoked any number of times and always with the
   * service started.
   * <p>
   * The default implementation does nothing.
   */
  public void stop ()
  {
    if (bDebug)
      System.err.println ("stop()");
  }


  // Management section ---------------------------------------------------------------

  /**
   * Obtains a reference to the service manager.
   * <p>
   * The default implementation throws an exception
   *
   * @return A reference to a service manager.
   * @exception NoManagerProvidedException In case of no management be required.
   */
    /*public MinervaManager getManager () throws NoManagerProvidedException, RemoteException
      {
      if (bDebug)
      System.err.println ("getManager()");

      throw new NoManagerProvidedException ("This method is not manageable.");
      }*/

     // Server methods -------------------------------------------------------------------

     /**
      * Invoke some time after the connection to the client has been lost.
      */
  synchronized public final void disconnect ()
  {
    //System.out.println ("<<DISCONNECT>> " + this + ". " + (context == null) + "--" + (pool == null));
    // Is already in the process of disconnecting?
    if (bDisconnecting)
      return;
    bDisconnecting = true;

    try {
// First, a local disconnection if service is called. Even for stateless and statefull services.
      this.localDisconnect();

      // Add service to pool and stop the service in case it is stateful.
      if (config != null && config.isStateful()) {
        context.logDebug ("<<DISCONNECTED>> " + this + ".---" + config.isStateful());
        this.stop();
        new Thread (new Runnable() {
          public void run () {
            try {
              // Thread.sleep (100);
              // Unregister service and register it again.
              UnicastRemoteObject.unexportObject (MinervaServiceImp.this, true);
              UnicastRemoteObject.exportObject (MinervaServiceImp.this);
       /*(MinervaServiceImp.this, 0, // Use an anonymous port
         new MinervaClientSocketFactory (MinervaServiceImp.this),  // Factory for client sockets.
         new MinervaServerSocketFactory (MinervaServiceImp.this)); // Factory for server sockets.*/
              // Return the service to the pool of available services.
              context.releaseService(MinervaServiceImp.this);
              if (pool != null) {
                pool.add (MinervaServiceImp.this);
              }
              if (context != null) {
                context.logDebug ("service " + this + " returned to pool.");
              }
            } catch (Exception e) {
              if (context != null)
                context.logError ("Disconnect: Unexpected problem: " + e);
              // e.printStackTrace();
              // Further work should be done here<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            }
          }
          }).start();
      }
      else if (config == null) {// || !config.isStateful()) {
        // It is a session
        new Thread (new Runnable() {
          public void run () {
            try {
              Thread.sleep (200);
              // Unregister service
              UnicastRemoteObject.unexportObject (MinervaServiceImp.this, true);
            } catch (Exception e) {
              if (context != null)
                context.logError ("Disconnect: Unexpected problem: " + e);
              // Further work should be done here<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            }
          }
          }).start();
      }
    }
    finally {
      bDisconnecting = false;
    }
  }

  public boolean isStatefull() throws RemoteException {
    return config!=null && this.config.isStateful();
  }

  protected void localDisconnect() {
    try {
      Field[] fields=this.getClass().getDeclaredFields();
      Object value;
      for(int i=0; fields!=null && i<fields.length; i++) {
        try {
          value=fields[i].get(this);
          if(value instanceof MinervaService && ((MinervaService)value).isStatefull())
            ((MinervaService)value).disconnect();
        }
        catch(Exception e) {
        }
      }
    }
    catch(Exception e) {
    }
  }

  /**
   * For internal use.
   */
  public final void sendBeacon ()
  {
    synchronized (sem) {
      lastAccess = System.currentTimeMillis();
    }
  }

  /**
   * For internal use.
   */
  public final long getLastAccessTime ()
  {
    synchronized (sem) {
      return lastAccess;
    }
  }

  // User section ----------------------------------------------------------------------
  // Subclasses provide the methods provided by the user.
}
