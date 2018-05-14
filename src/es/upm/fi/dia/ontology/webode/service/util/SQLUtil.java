package es.upm.fi.dia.ontology.webode.service.util;

import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import java.sql.*;

/**
 * This class provides some convenience methods to deal with
 * databases.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.5
 */
public class SQLUtil
{
  // Prevent from instantiating.
  private SQLUtil() {}

  /**
   * Gets the next val in the specified sequence.
   *
   * @param con The connection to the database to be used.
   * @param seqName The name of the sequence.
   */
  public static int nextVal (DBConnection con, String seqName)
      throws SQLException
  {
    Statement stmt = null;
    ResultSet rset = null;
    if(con.dbManager==DBConnection.ORACLE) {
      try {
        stmt = con.createStatement();
        rset = stmt.executeQuery ("select " + seqName + ".nextval from dual");
        rset.next();
        return rset.getInt (1);
      } finally {
        if (rset != null) rset.close();
        if (stmt != null) stmt.close();
      }
    }
    else if (con.dbManager==DBConnection.MYSQL) {
      try {
        stmt = con.createStatement();
        stmt.executeUpdate("UPDATE " + seqName + " SET id=LAST_INSERT_ID(id+1)");
        stmt.close();
        stmt = con.createStatement();
        rset = stmt.executeQuery("SELECT LAST_INSERT_ID()");
        rset.next();
        return rset.getInt (1);
      } finally {
        if (rset != null) rset.close();
        if (stmt != null) stmt.close();
      }
    }
    else
      return -1;
  }

  /**
   * Gets the ontology id related to a name.
   *
   * @return The ontology id related to a name.
   */
  public static int getOntologyId (DBConnection con, String name) throws SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      pstmt = con.prepareStatement ("select ontology_id from ode_ontology where name = ?");
      pstmt.setString (1, name);
      rset = pstmt.executeQuery();
      if (rset.next())
        return rset.getInt (1);
      else
        return -1;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /**
   * Gets the term id related to a name.
   *
   * @return The term id related to a name.
   */
  public static int getTermId (DBConnection con, String ontology, String name,
                               int type) throws SQLException
  {
    return getTermId (con, ontology, name, type, -1);
  }

  /**
   * Gets the term id related to a name.
   *
   * @return The term id related to a name.
   */
  public static int getTermId (DBConnection con, String ontology, String name,
                               int type, int parentId) throws SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      pstmt = con.prepareStatement ("select t.term_id " +
                                    " from ode_terms_glossary t, ode_ontology o " +
                                    " where t.name = ? and t.ontology_id = o.ontology_id and " +
                                    " o.name = ? and t.type = ? and t.parent_id " +
                                    (parentId < 0 ? " is null" : " = ?"));
      pstmt.setString (1, name);
      pstmt.setString (2, ontology);
      pstmt.setInt    (3, type);
      if (parentId >= 0)
        pstmt.setInt (4, parentId);
      rset = pstmt.executeQuery();
      if (rset.next())
        return rset.getInt (1);
      else
        return -1;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /**
   * Gets the term id related to a name.
   *
   * @return The term id related to a name.
   */
  public static int getTermId (DBConnection con, String ontology, String name) throws SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      pstmt = con.prepareStatement ("select t.term_id " +
                                    " from ode_terms_glossary t, ode_ontology o " +
                                    " where t.name = ? and t.ontology_id = o.ontology_id and " +
                                    " o.name = ?");
      pstmt.setString (1, name);
      pstmt.setString (2, ontology);
      rset = pstmt.executeQuery();
      if (rset.next())
        return rset.getInt (1);
      else
        return -1;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }


  /**
   * Gets the reference id related to a name.
   *
   * @return The reference id related to a name.
   */
  public static int getReferenceId (DBConnection con, String ontology, String reference) throws SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      pstmt = con.prepareStatement ("select r.reference_id " +
                                    " from ode_reference r, ode_ontology o " +
                                    " where r.name = ? and r.ontology_id = o.ontology_id and " +
                                    " o.name = ?");
      pstmt.setString (1, reference);
      pstmt.setString (2, ontology);
      rset = pstmt.executeQuery();
      if (rset.next())
        return rset.getInt (1);
      else
        return -1;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  public static int getValueTypeId(DBConnection con, String ontology, String name) throws SQLException {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      int ontology_id=getOntologyId(con, ontology);
      pstmt = con.prepareStatement ("select type.value_type_id " +
                                    "from ode_value_type type " +
                                    "where " +
                                    "  (type.ontology_id = ? or type.ontology_id is null) and " +
                                    "  type.name = ?");
      pstmt.setInt (1, ontology_id);
      pstmt.setString (2, name);
      rset = pstmt.executeQuery();
      if (rset.next())
        return rset.getInt (1);
      else
        return -1;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  public static int getEnumeratedValueId(DBConnection con, String ontology, String name, String value) throws SQLException {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      pstmt = con.prepareStatement ("select value.value_id " +
                                    "from ode_value_type type, ode_ontology o, ode_value value " +
                                    "where " +
                                    "  o.ontology_id = type.ontology_id and " +
                                    "  type.value_type_id = value.value_type_id and "+
                                    "  o.name = ? and"+
                                    "  type.name = ? and"+
                                    "  value.value= ?");
      pstmt.setString (1, ontology);
      pstmt.setString (2, name);
      pstmt.setString (3, value);
      rset = pstmt.executeQuery();
      if (rset.next())
        return rset.getInt (1);
      else
        return -1;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

}
