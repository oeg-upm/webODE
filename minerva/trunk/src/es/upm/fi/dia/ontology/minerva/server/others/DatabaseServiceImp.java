package es.upm.fi.dia.ontology.minerva.server.others;

import java.sql.*;
import java.rmi.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * This service provides access to a database.
 * <p>
 * Later modifications include a counter to keep track of the
 * number of uses a connection has had.  This is because Oracle
 * drivers don't work propertly after a number of them.  For this reason,
 * after the number of uses has been reached, the connection is closed
 * and a new one is opened.
 * <p>
 * All these parameters may be configured using the service's manager.
 *
 * @author  Julio César Arpírez Vega
 * @version 1.2
 */
public class DatabaseServiceImp extends MinervaServiceImp
    implements DatabaseService, Runnable {
  private Vector vBackup, connectionPool;
  private HashMap hashUsage;
  private Vector vReestablish;
  private int manager;
  private int majorVersion;
  private int minorVersion;

  private volatile boolean bRun;


  class ConnectionUsage {
    static final long MAX_TIME_USAGE = 1000*60*60; // 1 Hour

    int usages=0;
    long lastUsage=0;

    ConnectionUsage(int usages, long lastUsage) {
      this.usages=usages;
      this.lastUsage=lastUsage;
    }
  }

  /**
   * Constructor.
   */
  public DatabaseServiceImp () throws RemoteException
  {
    vBackup = new Vector();
    connectionPool = new Vector();
    vReestablish = new Vector();
    hashUsage = new HashMap();
  }


  // Life-cycle -----------------------------------------------------
  /**
   * Starts the service by establishing some connections
   * to a database server.
   */
  public void start () throws CannotStartException
  {
    DatabaseServiceConfiguration dsc = (DatabaseServiceConfiguration) config;

    if (dsc.URL == null) {
      throw new CannotStartException ("service " + context.getName() + " not configured.");
    }
    else {
      vReestablish = new Vector();

      try {
        context.logDebug ("loading database driver...");
        // Load driver
        Class.forName (dsc.driver);
        context.logDebug ("Done!");

        // Open connections
        context.logDebug ("Establishing connections (" + dsc.connections + ")...");
        for (int i = 0; i < dsc.connections; i++) {
          Connection con = null;         
	  if (dsc.URL.indexOf("mysql") != -1) 
          	con = DriverManager.getConnection (dsc.URL+"?zeroDateTimeBehavior=convertToNull", dsc.username, dsc.password);
          else 
          	con = DriverManager.getConnection (dsc.URL, dsc.username, dsc.password);
          vBackup.addElement (con);
          connectionPool.addElement (con);

          hashUsage.put (con, new ConnectionUsage(0, System.currentTimeMillis()));
        }

        String managerName=((Connection)connectionPool.get(0)).getMetaData().getDatabaseProductName();
        this.majorVersion=((Connection)connectionPool.get(0)).getMetaData().getDatabaseMajorVersion();
        this.minorVersion=((Connection)connectionPool.get(0)).getMetaData().getDatabaseMinorVersion();
        if(managerName.equalsIgnoreCase("Oracle"))
          manager=DBConnection.ORACLE;
        else if(managerName.equalsIgnoreCase("MySQL"))
          manager=DBConnection.MYSQL;
        else
          manager=DBConnection.UNKNOWN;

        context.logDebug ("Done!");

        bRun = true;
        Thread th = new Thread (this, "database allocator");
        th.setDaemon (true);
        th.start();

        if(manager==DBConnection.ORACLE) {
          DBConnection conn=null;
          CallableStatement cstmt=null;
          try {
            conn=this.getConnection();
            cstmt=conn.prepareCall("BEGIN DBMS_Utility.Analyze_Schema(?,'ESTIMATE'); END;");
            cstmt.setString(1, dsc.username.toUpperCase());
            cstmt.execute();
          }
          catch(SQLException e) {
            System.out.println("Cannot analize the schems '" + dsc.username.toUpperCase() + "'");
            e.printStackTrace();
            context.logError("Cannot analize the schems '" + dsc.username.toUpperCase() + "'",e);
          }
          finally {
            if(cstmt!=null) cstmt.close();
            if(conn!=null) this.releaseConnection(conn);
          }
        }

      } catch (Exception e) {
        throw new CannotStartException ("service " + context.getName() + " startup error: " +
                                        e.getMessage(), e);
      }
    }
  }


  /**
   * Stops the service by closing all opened connections.
   */
  public void stop ()
  {
    for (int i = 0; i < vBackup.size(); i++) {
      try {
        ((Connection) vBackup.elementAt (i)).close();
      } catch (Exception e) {
        context.logError ("error closing database connection: " + e.getMessage());
      }
    }

    vBackup.removeAllElements();
    connectionPool.removeAllElements();

    // Stop allocator thread
    bRun = false;
  }

  // Business methods -----------------------------------------------
  /**
   * Gets a connection from the pool.
   */
  public DBConnection getConnection ()
  {
    synchronized (connectionPool) {
      Connection con=null;
      while(con==null) {
        while (connectionPool.size() == 0) {
          try {
            this.context.logDebug("Connection Pool size: 0. Waiting for release.");
            connectionPool.wait();
          } catch (Exception e) {
            continue;
          }
        }

        con = (Connection) connectionPool.elementAt (0);
        connectionPool.removeElementAt (0);

        boolean reconnect=false;
        try {
          reconnect=con.isClosed();
          synchronized(hashUsage) {
            ConnectionUsage usage=(ConnectionUsage)hashUsage.get(con);
            if(System.currentTimeMillis()-usage.lastUsage>ConnectionUsage.MAX_TIME_USAGE)
              reconnect=true;
          }
        }
        catch(SQLException sqle) {
          reconnect=true;
        }

        if(reconnect) {
          this._reestablishConnection(con);
          con=null;
        }
      }

      try {
        con.setAutoCommit (true);
      } catch (Exception e) {
        context.logError ("Error setting auto-commit: " + e, e);
        // Recover<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
      }

      this.context.logDebug("Retrieve connection. Connection Pool size: " + connectionPool.size());

      return new DBConnection(con,this.manager,this.majorVersion,this.minorVersion);
    }
  }

  /**
   * Get the name of the database manager
   * @return The identifier of the database manager: ORACLE, MYSQL
   */
  public int getDBManager() throws RemoteException {
    return manager;
  }

  /**
   * Release a connection.
   *
   * @param con The connection to be released.
   */
  public void releaseConnection (DBConnection con)
  {
    // Check if the user is trying to give a connection
    // not from the pool
    if (hashUsage.containsKey (con.conn)) {
      DatabaseServiceConfiguration dsc = (DatabaseServiceConfiguration) config;

      synchronized (connectionPool) {
        try {
          if(!con.conn.getAutoCommit())
            con.conn.commit();

          ConnectionUsage usage=(ConnectionUsage) hashUsage.get(con.conn);

          usage.usages++;
          usage.lastUsage=System.currentTimeMillis();
          if (dsc.maxUsages > 0 && usage.usages > dsc.maxUsages)
            _reestablishConnection (con.conn);
          else {
            connectionPool.addElement (con.conn);

            connectionPool.notify();

            context.logDebug ("connection sucessfully returned to pool.");
          }
        } catch (Exception e) {
          context.logDebug ("error committing transaction: " + e);
          _reestablishConnection (con.conn);
        }

        this.context.logDebug("Release connection. Connection Pool size: " + connectionPool.size());
      }
    }
    else
      context.logDebug ("user tried to return a connection not from the pool.");
  }

  /**
   * Reestablishes a connection.
   */
  private void _reestablishConnection (Connection con)
  {
    synchronized (vReestablish) {
      vReestablish.addElement (con);
      vReestablish.notify();
    }
  }

  /**
   * Method run.
   * <p>
   * This thread is responsible for allocating and deallocating
   * database connections.
   */
  public void run ()
  {
    context.logDebug ("database allocator started.");
    while (bRun) {
      Connection con = null;

      synchronized (vReestablish) {
        if (vReestablish.isEmpty()) {
          try {
            vReestablish.wait();
            } catch (Exception e) {}

        }
        con = (Connection) vReestablish.elementAt (0);
        vReestablish.removeElementAt (0);
      }

      try {
        context.logDebug ("removing connection from pools.");
        synchronized (vBackup) {
          vBackup.removeElement (con);
        }

        synchronized (hashUsage) {
          hashUsage.remove (con);
        }

        synchronized (connectionPool) {
          connectionPool.removeElement (con);
        }

        context.logDebug ("closing connection...");
        con.close();
        context.logDebug ("done!");
      } catch (Exception e) {
        context.logDebug ("error closing connection in allocator thread: " + e);
      }

      while (true) {
        // Try to open the connection until sucess
        try {
          DatabaseServiceConfiguration dsc = (DatabaseServiceConfiguration) config;
	  if (dsc.URL.indexOf("mysql") != -1)
          	con = DriverManager.getConnection (dsc.URL+"?zeroDateTimeBehavior=convertToNull", dsc.username, dsc.password);
	  else          
          	con = DriverManager.getConnection (dsc.URL, dsc.username, dsc.password);
          context.logDebug ("database connection sucessfully established.");

          synchronized (hashUsage) {
            hashUsage.put (con, new ConnectionUsage(0, System.currentTimeMillis()));
          }

          synchronized (vBackup) {
            vBackup.addElement (con);
          }

          synchronized (connectionPool) {
            connectionPool.addElement (con);
            connectionPool.notify();
          }

          break;
        } catch (Exception e) {
          context.logError ("error opening database connection (" + e + ").  Retrying...");
          try {
            Thread.sleep (5000);
          } catch (Exception ignoreMe) {
          }
        }
      }
    }
  }

  /**
   * Execute a query.
   *
   * @param str The query to be executed.
   */
  public ResultSet executeQuery (String str) throws RemoteException, SQLException
  {
    DBConnection con = null;
    Statement stmt = null;
    try {
      con = getConnection();

      stmt = con.conn.createStatement();
      return stmt.executeQuery (str);
    } finally {
      try {
        if (stmt != null) stmt.close();
        } catch (Exception e) {}
        // Release resources...
        releaseConnection (con);
    }
  }

  protected void localDisconnect() {
  }
}