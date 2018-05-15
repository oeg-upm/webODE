package es.upm.fi.dia.ontology.minerva.server.others;

import java.sql.*;
import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * This services provides access to a database.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public interface DatabaseService extends MinervaService {
  /**
   * Get the name of the database manager
   * @return The identifier of the database manager: ORACLE, MYSQL
   */
  int getDBManager() throws RemoteException;

  /**
   * Gets a connection from the pool.
   */
  DBConnection getConnection () throws RemoteException;

  /**
   * Release a connection.
   *
   * @param con The connection to be released.
   */
  void releaseConnection (DBConnection con) throws RemoteException;

  /**
   * Execute a query.
   *
   * @param str The query to be executed.
   */
  ResultSet executeQuery (String str) throws RemoteException, SQLException;
}
