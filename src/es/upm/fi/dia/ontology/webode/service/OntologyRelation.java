package es.upm.fi.dia.ontology.webode.service;

import java.util.*;
import java.sql.*;

import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.webode.service.util.*;

/**
 *
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class OntologyRelation implements java.io.Serializable
{
    public String originalOntologyName;
    public String originOntologyName;
    public String destOntologyName;
    public String relationName;

    public OntologyRelation (String originalOntologyName,
			     String originOntologyName,
			     String destOntologyName,
			     String relationName)
    {
	this.originalOntologyName = originalOntologyName;
	this.destOntologyName = destOntologyName;
	this.originOntologyName = originOntologyName;
	this.relationName = relationName;
    }


    /**
     * Get related ontologies in the context of the given ontology.
     */
    public static OntologyRelation[] getRelatedOntologies (DBConnection con, String name)
	throws SQLException, WebODEException
    {
	if (name == null || (name = name.trim()).length() == 0)
	    throw new WebODEException ("Invalid ontology name.");

	PreparedStatement pstmt = null;
	ResultSet rset          = null;
	try {
	    pstmt = con.prepareStatement ("select o1.name, o.name, ore.name " +
					  " from ode_ontology o, ode_ontology o1, ode_ontology_uses_ontology ore, " +
					  " ode_ontology o2 " +
					  " where o.ontology_id = ore.dest_ontology_id and " +
					  " ore.context_ontology_id = o2.ontology_id and o2.name = ? and " +
					  " o1.ontology_id = ore.or_ontology_id");
	    pstmt.setString (1, name);
	    rset = pstmt.executeQuery();
	    if (rset.next()) {
		Vector v = new Vector(200);

		do {
		    v.addElement (new OntologyRelation (name,
							rset.getString (1),   // Origin ontology
							rset.getString (2),   // Destination ontology
							rset.getString (3))); // Relation name
		} while (rset.next());

		OntologyRelation[] aor = new OntologyRelation[v.size()];
		v.copyInto (aor);
		return aor;
	    }
	    else
		return null;
	} finally {
	    try {
		if (rset != null) rset.close();
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {}
	}
    }

    /**
     * Inserts current relation.
     *
     * @param force If <tt>true</tt> creates an ontology if it already does not exist.
     */
    public void store (DBConnection con,
		       boolean force, String login, String groupName) throws SQLException, WebODEException
    {
	if (originOntologyName == null || (originOntologyName = originOntologyName.trim()).equals("") ||
	    originalOntologyName == null || (originalOntologyName = originalOntologyName.trim()).equals("") ||
	    destOntologyName == null || (destOntologyName = destOntologyName.trim()).equals("") ||
	    relationName == null || (relationName = relationName.trim()).equals(""))
	    throw new WebODEException ("Invalid state.");

	PreparedStatement pstmt = null;
	try {
	    // -------------------------- Transaction<<<<<<<<<<<<< Level
	    //con.setAutoCommit (false);

	    int originOntologyId = -1;
	    int destOntologyId   = -1;
	    if (force) {
		try {
		    OntologyDescriptor.getOntologyDescriptor (con, originOntologyName);
		} catch (WebODEException me) {
		    // Insert ontology
		    System.err.println ("Inserting ontology " + originOntologyName);
		    originOntologyId = new OntologyDescriptor (originOntologyName, "", login, groupName,
							       new java.util.Date(), new java.util.Date()).store(con);
		}

		try {
		    OntologyDescriptor.getOntologyDescriptor (con, destOntologyName);
		} catch (WebODEException me) {
		    // Insert ontology
		    System.err.println ("Inserting ontology " + destOntologyName);
		    destOntologyId = new OntologyDescriptor (destOntologyName, "", login, groupName,
							     new java.util.Date(), new java.util.Date()).store(con);
		}
	    }

	    if (originOntologyId < 0)
		originOntologyId = SQLUtil.getOntologyId (con, originOntologyName);
	    if (destOntologyId < 0)
		destOntologyId = SQLUtil.getOntologyId (con, destOntologyName);

	    int originalOntologyId = SQLUtil.getOntologyId (con, originalOntologyName);

	    // Insert relation.
	    pstmt = con.prepareStatement ("insert into ode_ontology_uses_ontology (context_ontology_id, " +
					  " or_ontology_id, dest_ontology_id, name) values (?, ?, ?, ?)");
	    pstmt.setInt    (1, originalOntologyId);
	    pstmt.setInt    (2, originOntologyId);
	    pstmt.setInt    (3, destOntologyId);
	    pstmt.setString (4, relationName);
	    pstmt.executeUpdate();
	} finally {
	    try {
		if (pstmt  != null) pstmt.close();
	    } catch (Exception e) {}
	}
    }

    /**
     * Removes an ontology in the context of another.
     */
    public static void removeRelatedOntology (DBConnection con, String ontology, String ontologyToRemove)
	throws WebODEException, SQLException
    {
	if (ontology == null || (ontology = ontology.trim()).equals("") ||
	    ontologyToRemove == null || (ontologyToRemove = ontologyToRemove.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt  = null;
	PreparedStatement pstmt1  = null;
	try {
	    // A single transaction
	    con.setAutoCommit (false);

	    // Relations are removed as well
	    int contextOntologyId = SQLUtil.getOntologyId (con, ontology);
	    int ontologyId        = SQLUtil.getOntologyId (con, ontologyToRemove);

	    pstmt = con.prepareStatement ("delete from ode_ontology_uses_ontology " +
					  " where (or_ontology_id = ? or dest_ontology_id = ?) " +
					  " and context_ontology_id = ?");
	    pstmt.setInt (1, ontologyId);
	    pstmt.setInt (2, ontologyId);
	    pstmt.setInt (3, contextOntologyId);
	    pstmt.executeUpdate();

	    pstmt1 = con.prepareStatement ("delete from ode_o_uses_o_info " +
					   " where context_ontology_id = ? and ontology_id = ?");
	    pstmt1.setInt (1, contextOntologyId);
	    pstmt1.setInt (2, ontologyId);

	    pstmt1.executeUpdate();

	    // Commit changes.
	    con.commit();
	} catch (SQLException sqle) {
	    con.rollback();
	    con.setAutoCommit (true);

	    throw sqle;
	}
	finally {
	    try {
		if (pstmt  != null) pstmt.close();
		if (pstmt1 != null) pstmt1.close();
	    } catch (Exception e) {
	    }
	}
    }

    /**
     * Removes a single relation.
     */
    public static void removeOntologyRelation (DBConnection con, String ontology, String relation)
	throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals("") ||
	    relation == null || (relation = relation.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt  = null;
	PreparedStatement pstmt1  = null;
	try {
	    // Relations are removed as well
	    int contextOntologyId = SQLUtil.getOntologyId (con, ontology);

	    pstmt = con.prepareStatement ("delete from ode_ontology_uses_ontology " +
					  " where context_ontology_id = ? and name = ?");
	    pstmt.setInt (1, contextOntologyId);
	    pstmt.setString (2, relation);
	    pstmt.executeUpdate();
	} finally {
	    try {
		if (pstmt  != null) pstmt.close();
		if (pstmt1 != null) pstmt1.close();
	    } catch (Exception e) {
	    }
	}
    }
}



