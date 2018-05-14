package es.upm.fi.dia.ontology.webode.service;

import java.util.*;
import java.sql.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.webode.service.util.SQLUtil;

/**
 * Class that encapsulates information about a given ontology.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.2
 */
public class OntologyDescriptor implements java.io.Serializable
{
  //Add namespace
  public String name,namespace, description, login, groupName;
  public java.util.Date modificationDate, creationDate;
  public boolean state; // Allow user interaction


  public OntologyDescriptor (String name, String description,
                             String login, String groupName,
                             java.util.Date modificationDate, java.util.Date creationDate)
  {
    this.name = name;
    this.description = description;
    this.login = login;
    this.groupName = groupName;
    this.modificationDate = modificationDate;
    this.creationDate = creationDate;
  }


  public OntologyDescriptor (String name, String description,
                             String login, String groupName,
                             java.util.Date modificationDate,
                             java.util.Date creationDate, boolean state)
  {
    this.name = name;
    this.description = description;
    this.login = login;
    this.groupName = groupName;
    this.modificationDate = modificationDate;
    this.creationDate = creationDate;
    this.state = state;
  }

  //Add namespace
  public OntologyDescriptor (String name,String namespace,  String description,
                             String login, String groupName,
                             java.util.Date modificationDate, java.util.Date creationDate)
  {
    this.name = name;
    this.namespace = namespace; //new
    this.description = description;
    this.login = login;
    this.groupName = groupName;
    this.modificationDate = modificationDate;
    this.creationDate = creationDate;
  }

  //Add namespace
  public OntologyDescriptor (String name, String namespace, String description,
                             String login, String groupName,
                             java.util.Date modificationDate,
                             java.util.Date creationDate, boolean state)
  {
    this.name = name;
    this.namespace = namespace; //new
    this.description = description;
    this.login = login;
    this.groupName = groupName;
    this.modificationDate = modificationDate;
    this.creationDate = creationDate;
    this.state = state;
  }


  //Add namespace
  public String toString ()
  {
    return "Name: " + name + ".  Namespace: " + namespace + ".  Description: " + description + ".  " +
        "Login: " + login + ".  Group: " + groupName + ".  CD: " + creationDate + ".  " +
        modificationDate + ".";
  }

  /**
   * Factory to get all the available ontologies in a given time.
   * <p>They are ordered alphabetically.
   *
   * @param con The connection to a database to be used.
   */
  public static OntologyDescriptor[] getAvailableOntologies (DBConnection con, String user, String group)
      throws SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      //add namespace
      pstmt = con.prepareStatement ("select name, namespace, description, login, group_name, " +
                                    "   creation_date, modification_date from ode_ontology  " +
                                    "   where ontology_id <> 0 and " +
                                    "         (login = ? or (group_name = ? and state = 1)) " +
                                    " order by name");
      pstmt.setString (1, user);
      pstmt.setString (2, group);
      // Do not include the meta-ontology.

      rset = pstmt.executeQuery();

      Vector vres = new Vector(200);
      while (rset.next()) {
        vres.addElement (new OntologyDescriptor
                         (rset.getString (1),  // Name
                         rset.getString (2),  // Add--> Namespace
                         rset.getString (3),  // Description
                         rset.getString (4),  // login
                         rset.getString (5),  // Group name
                         rset.getDate   (6),  // Creation Date
                         rset.getDate   (7)));// Modification Date
      }
      OntologyDescriptor[] aod = new OntologyDescriptor[vres.size()];
      vres.copyInto (aod);

      return aod;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /**
   * Factory to get all the available ontologies in a given time.
   * <p>They are ordered alphabetically.
   *
   * @param con The connection to a database to be used.
   */
  public static OntologyDescriptor[] getAvailableOntologies (DBConnection con, String user, String[] groups)
      throws SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String strGroups = "('" + groups[0] + "'";
    for (int i = 1; i < groups.length; i++)
      strGroups += ",'" + groups[i] + "'";
    strGroups += ")";
    try {
      //add namespace
      pstmt = con.prepareStatement ("select name, namespace, description, login, group_name, " +
                                    "   creation_date, modification_date from ode_ontology  " +
                                    "   where ontology_id <> 0 and " +
                                    "         (login = ? or (group_name in " + strGroups + " and state = 1)) " +
                                    " order by name");
      pstmt.setString (1, user);
      //pstmt.setString (2, group);
      // Do not include the meta-ontology.

      rset = pstmt.executeQuery();

      Vector vres = new Vector(200);
      while (rset.next()) {
        vres.addElement (new OntologyDescriptor
                         (rset.getString (1),  // Name
                         rset.getString (2),  // Add--> Namespace
                         rset.getString (3),  // Description
                         rset.getString (4),  // login
                         rset.getString (5),  // Group name
                         rset.getDate   (6),  // Creation Date
                         rset.getDate   (7)));// Modification Date
      }
      OntologyDescriptor[] aod = new OntologyDescriptor[vres.size()];
      vres.copyInto (aod);

      return aod;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }


  /**
   * Factory to get all the available ontologies in a given time.
   * <p>They are ordered alphabetically.
   *
   * @param con The connection to a database to be used.
   */
  public static OntologyDescriptor[] getAvailableOntologies (DBConnection con)
      throws SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      pstmt = con.prepareStatement ("select name, namespace, description, login, group_name, " +
                                    "   creation_date, modification_date from ode_ontology  " +
                                    "   where ontology_id <> 0 " +
                                    " order by name");
      // Do not include the meta-ontology.

      rset = pstmt.executeQuery();

      Vector vres = new Vector(200);
      while (rset.next()) {
        vres.addElement (new OntologyDescriptor
                         (rset.getString (1),  // Name
                         rset.getString (2),  // Add--> Namespace
                         rset.getString (2),  // Description
                         rset.getString (3),  // login
                         rset.getString (4),  // Group name
                         rset.getDate   (5),  // Creation Date
                         rset.getDate   (6)));// Modification Date
      }
      OntologyDescriptor[] aod = new OntologyDescriptor[vres.size()];
      vres.copyInto (aod);

      return aod;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }


  /**
   * Factory to get data about a specific ontology.
   *
   * @param con The connection to a database to be used.
   * @param name The name of the ontology.
   */
  public static OntologyDescriptor getOntologyDescriptor (DBConnection con, String name)
      throws SQLException, WebODEException
  {
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try {
      stmt = con.prepareStatement ("select name, namespace, description, login, group_name, " +
                                   "   creation_date, modification_date, state " +
                                   " from ode_ontology where name = ?");
      if (name == null || name.trim().equals (""))
        throw new WebODEException ("Invalid name");

      stmt.setString (1, name.trim());
      rset = stmt.executeQuery ();

      if (rset.next()) {
        return new OntologyDescriptor
        (rset.getString (1),  // Name
        rset.getString (2),  // Add--> Namespace
        rset.getString (3),  // Description
        rset.getString (4),  // login
        rset.getString (5),  // Group name
        rset.getDate   (6),  // Creation Date
        rset.getDate   (7),  // Modification Date
        rset.getInt (8) == 1); // Allow access to group
      }
      else
        throw new WebODEException ("No such ontology " + name + ".");
    } finally {
      if (rset != null) rset.close();
      if (stmt != null) stmt.close();
    }
  }

  public static OntologyDescriptor[] findOntologyDescriptorByNamespace (DBConnection con, String namespace)
      throws SQLException, WebODEException
  {
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try {
      stmt = con.prepareStatement ("select name, namespace, description, login, group_name, " +
                                   "   creation_date, modification_date, state " +
                                   " from ode_ontology where namespace = ?");
      if (namespace == null || namespace.trim().equals (""))
        throw new WebODEException ("Invalid namespace");

      stmt.setString (1, namespace.trim());
      rset = stmt.executeQuery ();

      ArrayList ontos=new ArrayList();

      while (rset.next()) {
        ontos.add(new OntologyDescriptor(rset.getString (1),  // Name
                                         rset.getString (2),  // Add--> Namespace
                                         rset.getString (3),  // Description
                                         rset.getString (4),  // login
                                         rset.getString (5),  // Group name
                                         rset.getDate   (6),  // Creation Date
                                         rset.getDate   (7),  // Modification Date
                                         rset.getInt (8) == 1)); // Allow access to group
      }
      if(ontos.size()>0)
        return (OntologyDescriptor[])ontos.toArray(new OntologyDescriptor[0]);
      else
        return null;
    } finally {
      if (rset != null) rset.close();
      if (stmt != null) stmt.close();
    }
  }


  /**
   * Stores current ontology descriptor in the database.
   *
   * @param con The connection to be used.
   * @return The ontology id.
   */
  public int store (DBConnection con) throws SQLException, WebODEException
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      // Init a transaction
      int id = SQLUtil.nextVal(con,"ode_sequence_ontology_id");

      pstmt = con.prepareStatement ("insert into ode_ontology " +
                                    "(ontology_id, name, namespace, creation_date, modification_date, " +
                                    "description, login, group_name, state) values " +
                                    "(?, ?, ?, ?, ?, ?, ?, ?, ?)");
      // Previous checks.
      if (creationDate == null)
        creationDate = new java.util.Date();
      if (modificationDate == null)
        modificationDate = new java.util.Date();

      pstmt.setInt    (1, id);
      pstmt.setString (2, name = name.trim());

      if(namespace == null || (namespace = namespace.trim()).equals("") ){
        pstmt.setNull (3, Types.VARCHAR);
      } else {
        pstmt.setString (3, namespace);
      }
      pstmt.setDate   (4, new java.sql.Date (creationDate.getTime()));
      pstmt.setDate   (5, new java.sql.Date (modificationDate.getTime()));

      if (description == null || (description = description.trim()).equals(""))
        pstmt.setNull (6, Types.VARCHAR);
      else
        pstmt.setString (6, description);

      pstmt.setString (7, login);
      pstmt.setString (8, groupName);
      pstmt.setInt (9, state ? 1 : 0);

      pstmt.executeUpdate();

      // Insert built-in terms
      //_insertBuiltIn (con);

      return id;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  private void _insertBuiltIn (DBConnection con) throws SQLException, WebODEException
  {
    // Build ontology built-in terms
    // Insert a new term holding the subclass-of
    new Term (name, TermRelation.SUBCLASS_OF,
              "Subclass-of built-in term", TermTypes.RELATION).store(con);
    new Term (name, TermRelation.NOT_SUBCLASS_OF,
              "Not-Subclass-of built-in term", TermTypes.RELATION).store(con);
    new Term (name, TermRelation.DISJOINT,
              "Disjoint built-in term", TermTypes.RELATION).store(con);
    new Term (name, TermRelation.EXHAUSTIVE,
              "Exhaustive built-in term", TermTypes.RELATION).store(con);
    new Term (name, TermRelation.TRANSITIVE_PART_OF,
              "Transitive-Part-of built-in term", TermTypes.RELATION).store(con);
    new Term (name, TermRelation.INTRANSITIVE_PART_OF,
              "Inransitive-Part-of built-in term", TermTypes.RELATION).store(con);
    // Properties
    new Term (name, TermProperty.REFLEXIVE,
              "Reflexive built-in term", TermTypes.PROPERTY).store(con);
    new Term (name, TermProperty.IRREFLEXIVE,
              "Irreflexive built-in term", TermTypes.PROPERTY).store(con);
    new Term (name, TermProperty.SYMMETRICAL,
              "Symmetrical built-in term", TermTypes.PROPERTY).store(con);
    new Term (name, TermProperty.ASYMMETRICAL,
              "Asymmetrical built-in term", TermTypes.PROPERTY).store(con);
    new Term (name, TermProperty.ANTISYMMETRICAL,
              "Antiymmetrical built-in term", TermTypes.PROPERTY).store(con);
    new Term (name, TermProperty.TRANSITIVE,
              "Transitive built-in term", TermTypes.PROPERTY).store(con);
  }

  /**
   * Update the data about an ontology.
   *
   * @param con  The connection to the database.
   * @param name The name of the ontology to modify.
   * @param od   The ontology descriptor holding the new data.  Date fields are ignored.  User and group are ignored
   *             as well.
   * @exception Exception If no ontology with the given name exists.
   */
  public static void update (DBConnection con, String name, OntologyDescriptor od) throws SQLException, Exception
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      // Get ontology_id
      pstmt = con.prepareStatement ("select ontology_id from ode_ontology where name = ?");
      pstmt.setString (1, name);
      rset = pstmt.executeQuery ();
      int id;
      if (rset.next()) {
        id = rset.getInt (1);
        rset.close();
        rset = null;
      }
      else {
        throw new Exception ("No ontology with name " + name + " currently available.");
      }

      pstmt = con.prepareStatement ("update ode_ontology " +
                                    "set name = ?, namespace = ?, modification_date = ?, " +
                                    "description = ?, state = ?, group_name = ? where ontology_id = ?");
      String foo = od.name.trim();
      if (foo.equals(""))
        throw new Exception ("Name cannot be empty.");

      pstmt.setString (1, foo);
      pstmt.setString (2, od.namespace.trim());
      pstmt.setDate   (3, new java.sql.Date (System.currentTimeMillis()));

      if (od.description == null || (foo = od.description.trim()).equals(""))
        pstmt.setNull (4, Types.VARCHAR);
      else
        pstmt.setString (4, foo);

      pstmt.setInt    (5, od. state ? 1 : 0);
      pstmt.setString (6, od.groupName);
      pstmt.setInt    (7, id);

      pstmt.executeUpdate();
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /**
   * Removes an ontology and all its related data.
   */
  public void remove (DBConnection con) throws SQLException, WebODEException
  {
    if (name == null || (name = name.trim()).equals(""))
      throw new WebODEException ("Invalid ontology name.");
    PreparedStatement pstmt = null;
    try {
      pstmt = con.prepareStatement ("delete from ode_ontology where name = ?");
      pstmt.setString (1, name);
      pstmt.executeUpdate();
    } finally {
      if (pstmt != null) pstmt.close();
    }
  }
}
