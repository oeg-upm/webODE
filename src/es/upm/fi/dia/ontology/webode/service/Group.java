package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.webode.service.util.*;

/**
 * A class to describe groups.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class Group implements java.io.Serializable, TermTypes
{
    /** Name for the group. */
    public String name;

    /** Description. */
    public String description;

    /** Concepts. */
    public String[] concepts;

    public Group (String name, String description, String[] concepts)
    {
	this.name        = name;
	this.description = description;
	this.concepts    = concepts;
    }

    public void store (DBConnection con, String ontology) throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals("") ||
	    name == null || (name = name.trim()).equals("") ||
	    concepts == null || concepts.length == 0)
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt = null;
	try {
	    // Transaction
	    con.setAutoCommit (false);

	    // Term data
	    int groupId = new Term (ontology, name, description, GROUP).store (con);

	    // Group data
	    pstmt = con.prepareStatement ("insert into ode_group (group_id, term_id) values (?, ?)");
	    for (int i = 0; i < concepts.length; i++) {
		int termId = SQLUtil.getTermId (con, ontology, concepts[i], TermTypes.CONCEPT);
		pstmt.setInt (1, groupId);
		pstmt.setInt (2, termId);
		pstmt.executeUpdate();
	    }

	    con.commit();
	} catch (SQLException sqle) {
	    con.rollback();
	    throw sqle;
	} finally {
	    try {
		if (pstmt != null) pstmt.close();
		con.setAutoCommit (true);
	    } catch (Exception e) {}
	}
    }

    /**
     * Retrieves available groups.
     */
    public static Group[] getGroups (DBConnection con, String ontology)
	throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
	    pstmt = con.prepareStatement ("select t.name, t.description, t1.name " +
					  " from ode_terms_glossary t, ode_terms_glossary t1, " +
					  "      ode_group g, ode_ontology o " +
					  " where t.type = 15 and t.term_id = g.group_id and " +
					  "       g.term_id = t1.term_id and " +
					  "       t.ontology_id = o.ontology_id and o.name = ?");
	    pstmt.setString (1, ontology);
	    rset = pstmt.executeQuery ();
	    if (rset.next()) {
		Vector v = new Vector(20);
		Vector aux = new Vector();
		String previousGroup = "";
		String currentGroup = "", concept, desc;
		do {
		    currentGroup = rset.getString (1);
		    desc = rset.getString (2);
		    concept = rset.getString (3);

		    if (previousGroup == "")
			previousGroup = currentGroup;

		    if (previousGroup.equals (currentGroup)) {
			aux.addElement (concept);
		    }
		    else {
			String[] saux = new String[aux.size()];
			aux.copyInto (saux);
			aux = new Vector();
			aux.addElement (concept);

			v.addElement (new Group (previousGroup, desc, saux));
			previousGroup = currentGroup;
		    }
		} while (rset.next());
		String[] saux = new String[aux.size()];
		aux.copyInto (saux);

		v.addElement (new Group (previousGroup, desc, saux));
		Group[] groups = new Group[v.size()];
		v.copyInto (groups);

		return groups;
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
     * Retrieves a given group.
     */
    public static Group getGroup (DBConnection con, String ontology, String name)
	throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals("") ||
	    name == null || (name = name.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
	    pstmt = con.prepareStatement ("select t.name, t.description, t1.name " +
					  " from ode_terms_glossary t, ode_terms_glossary t1, " +
					  "      ode_group g, ode_ontology o " +
					  " where t.type = 15 and t.term_id = g.group_id and " +
					  "       g.term_id = t1.term_id and t.name = ? and " +
					  "       t.ontology_id = o.ontology_id and o.name = ?");
	    pstmt.setString (1, name);
	    pstmt.setString (2, ontology);
	    rset = pstmt.executeQuery ();
	    if (rset.next()) {
		Vector aux = new Vector();
		String previousGroup = "";
		String currentGroup = "", concept, desc;
		do {
		    currentGroup = rset.getString (1);
		    desc = rset.getString (2);
		    concept = rset.getString (3);
		    aux.addElement (concept);
		} while (rset.next());
		String[] saux = new String[aux.size()];
		aux.copyInto (saux);

		return new Group (currentGroup, desc, saux);
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
     * Removes concepts.
     */
    public void removeConcepts (DBConnection con, String ontology)
	throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals("") ||
	    name == null || (name = name.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt = null;
	try {
	    int termId = SQLUtil.getTermId (con, ontology, name, TermTypes.GROUP);

	    pstmt = con.prepareStatement ("delete from ode_group where group_id = ?");
	    pstmt.setInt (1, termId);
	    pstmt.executeUpdate();
	} finally {
	    try {
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {}
	}
    }


    /**
     * Removes concepts.
     */
    public void addConcepts (DBConnection con, String ontology)
	throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals("") ||
	    name == null || (name = name.trim()).equals("") ||
	    concepts == null || concepts.length == 0)
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt = null;
	try {
	    int groupId = SQLUtil.getTermId (con, ontology, name, TermTypes.GROUP);

	    // Group data
	    pstmt = con.prepareStatement ("insert into ode_group (group_id, term_id) values (?, ?)");
	    for (int i = 0; i < concepts.length; i++) {
		int termId = SQLUtil.getTermId (con, ontology, concepts[i], TermTypes.CONCEPT);
		pstmt.setInt (1, groupId);
		pstmt.setInt (2, termId);
		pstmt.executeUpdate();
	    }
	} finally {
	    try {
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {}
	}
    }


    /**
     * Updates group.
     */
    public void update (DBConnection con, String ontology, String originalName)
	throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals("") ||
	    name == null || (name = name.trim()).equals("") ||
	    concepts == null || concepts.length == 0)
	    throw new WebODEException ("Invalid parameters.");

	try {
	    con.setAutoCommit (false);
	    new Term (ontology, name, description, GROUP).update (con, originalName);

	    removeConcepts (con, ontology);
	    addConcepts (con, ontology);
	} finally {
	    con.setAutoCommit (true);
	}
    }


}




