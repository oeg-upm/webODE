package es.upm.fi.dia.ontology.minerva.server.others;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;

import es.upm.fi.dia.ontology.minerva.server.services.CannotStartException;
import es.upm.fi.dia.ontology.minerva.server.services.MinervaServiceImp;

public class DBCPServiceImp extends MinervaServiceImp implements DBCPService {
  protected BasicDataSource ds=null;
  private int manager;
  private int majorVersion;
  private int minorVersion;

  public DBCPServiceImp() throws RemoteException {
  }

  protected static BasicDataSource setupDataSource(DBCPServiceConfiguration dsc) {
    try {
      BasicDataSource ds = new BasicDataSource();
      DBCPServiceManager mng=new DBCPServiceManager();
      for(int i=0; i<DBCPServiceManager.PARAMS.length; i++) {
        Field field=(Field)DBCPServiceManager.PARAMS[i][0];
        Object val=field.get(dsc);
        if(field.getName().equals("url")) {
          val=val.toString();
          if(((String)val).indexOf("mysql") != -1)
          val=((String)val)+"?zeroDateTimeBehavior=convertToNull";
        }
        if(val==null || val.toString().trim().length()==0) continue;
        if(field.getType().equals(int.class) && Integer.parseInt(val.toString())<0) continue;
        if(field.getType().equals(long.class) && Long.parseLong(val.toString())<0) continue;
        String methodName=field.getName();
        methodName="set"+Character.toUpperCase(methodName.charAt(0))+methodName.substring(1);
        Method method=BasicDataSource.class.getMethod(methodName, new Class[]{field.getType()});
        method.invoke(ds, new Object[]{val});
      }
      return ds;
    }
    catch(Exception e) {
      e.printStackTrace(System.out);
      return null;
    }
  }

  // Life-cycle -----------------------------------------------------
  /**
   * Starts the service by establishing some connections
   * to a database server.
   */
  public void start () throws CannotStartException
  {
    DBCPServiceConfiguration dsc = (DBCPServiceConfiguration) config;

    if (dsc.url == null) {
      throw new CannotStartException ("service " + context.getName() + " not configured.");
    }
    else {
      try {
        context.logDebug ("loading database driver...");
        // Load driver
        Class.forName (dsc.driverClassName);
        context.logDebug ("Done!");

        // Open connections
        context.logDebug ("Establishing connections (" + dsc.initialSize + ")...");

        this.ds=setupDataSource(dsc);
        
        Connection conn=this.ds.getConnection();
        String managerName=conn.getMetaData().getDatabaseProductName();
        this.majorVersion=conn.getMetaData().getDatabaseMajorVersion();
        this.minorVersion=conn.getMetaData().getDatabaseMinorVersion();
        if(managerName.equalsIgnoreCase("Oracle"))
          manager=DBConnection.ORACLE;
        else if(managerName.equalsIgnoreCase("MySQL"))
          manager=DBConnection.MYSQL;
        else
          manager=DBConnection.UNKNOWN;
        conn.close();

        context.logDebug ("Done!");

        if(manager==DBConnection.ORACLE) {
          DBConnection con=null;
          CallableStatement cstmt=null;
          try {
            con=this.getConnection();
            cstmt=con.prepareCall("BEGIN DBMS_Utility.Analyze_Schema(?,'ESTIMATE'); END;");
            cstmt.setString(1, dsc.username.toUpperCase());
            cstmt.execute();
          }
          catch(SQLException e) {
            System.out.println("Cannot analize the schema '" + dsc.username.toUpperCase() + "'");
            e.printStackTrace();
            context.logError("Cannot analize the schema '" + dsc.username.toUpperCase() + "'",e);
          }
          finally {
            if(cstmt!=null) cstmt.close();
            if(con!=null) this.releaseConnection(con);
          }
        }

      } catch (Exception e) {
        throw new CannotStartException ("service " + context.getName() + " startup error: " +
            e.getMessage(), e);
      }
    }
  }

  public ResultSet executeQuery(String str) throws RemoteException, SQLException {
    DBConnection con = null;
    Statement stmt = null;
    try {
      con = getConnection();

      stmt = con.conn.createStatement();
      return stmt.executeQuery (str);
    }
    finally {
      try {
        if (stmt != null) stmt.close();
      } catch (Exception e) {}
      // Release resources...
      releaseConnection (con);
    }
  }

  public DBConnection getConnection() throws RemoteException {
    try {
      Connection conn=this.ds.getConnection();
      conn.setAutoCommit(true);
      return new DBConnection(conn,this.manager,this.majorVersion,this.minorVersion);
    }
    catch(SQLException sqle) {
      sqle.printStackTrace();
      return null;
    }
  }

  public int getDBManager() throws RemoteException {
    return manager;
  }

  public void releaseConnection(DBConnection con) throws RemoteException {
    try {
      con.close();
    }
    catch(SQLException sqle) {
      sqle.printStackTrace();
    }
  }

  public void stop () {
    try {
      this.ds.close();
    }
    catch(SQLException sqle) {
      sqle.printStackTrace();
    }
  }
}
