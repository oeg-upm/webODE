package es.upm.fi.dia.ontology.minerva.server.others;

import java.sql.*;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Administrador
 * Date: 17-feb-2004
 * Time: 0:03:07
 * To change this template use Options | File Templates.
 */
public class DBConnection {
  public static final int UNKNOWN=-1;
  public static final int ORACLE=0;
  public static final int MYSQL=1;

  public Connection conn;
  public int dbManager;
  public int dbMajorVersion;
  public int dbMinorVersion;

  public DBConnection(Connection conn, int manager, int majorVersion, int minorVersion) {
    this.conn=conn;
    this.dbManager=manager;
    this.dbMajorVersion=majorVersion;
    this.dbMinorVersion=minorVersion;
  }

  public void close() throws SQLException {
    conn.close();
  }

  public void commit() throws SQLException {
    conn.commit();
  }

  public Statement createStatement() throws SQLException {
    return conn.createStatement();
  }

  public PreparedStatement prepareStatement(String sql) throws SQLException {
//    Logger logger=Logger.getLogger(this.getClass());
//    if(logger!=null) {
//      return new DBPreparedStatement(conn.prepareStatement(sql),sql,logger);
//    }
//    else
    return conn.prepareStatement(sql);
  }

  public CallableStatement prepareCall(String sql) throws SQLException {
    return conn.prepareCall(sql);
  }

  public void setAutoCommit(boolean auto) throws SQLException {
    conn.setAutoCommit(auto);
  }

  public void rollback() throws SQLException {
    conn.rollback();
  }
}
