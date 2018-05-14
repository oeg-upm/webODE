package es.upm.fi.dia.ontology.webode.service;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import java.sql.*;
import java.util.*;

/**
 * A class that modells synonyms and abbreviations.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.2
 */
public class SynonymAbbreviation implements java.io.Serializable, TermTypes
{
    /** The name of the synonym/abbreviation. */
    public String name;

    /** The name of the related term. */
    public String relatedTerm;

    /** Parent term */
    public String parentTerm;

    /** Description. */
    public String description;

    /**
     * The type (synonym/abbreviation) as defined in <tt>TermTypes</tt>.
     *
     * @see es.upm.fi.dia.ontology.webode.service.TermTypes
     */
    public int type;

    /**
     * Constructor.
     *
     * @param name   The name of the synonym/abbreviation.
     * @param relatedTerm The name of the related term.
     * @param type   The type (synonym/abbreviation) as defined in <tt>TermTypes</tt>.
     * @param parentTerm  The parent term.
     * @param desc   Description.
     *
     * @see es.upm.fi.dia.ontology.webode.service.TermTypes
     */
    public SynonymAbbreviation (String name, String relatedTerm, String parentTerm, int type, String desc)
    {
	this.name        = name;
	this.relatedTerm = relatedTerm;
	this.parentTerm  = parentTerm;
	this.type        = type;
	this.description = desc;
    }

    public void store (DBConnection con, String ontology) throws SQLException, WebODEException
    {
	if ((name == null || (name = name.trim()).equals("") ||
	     ontology == null || (ontology = ontology.trim()).equals("") ||
	     relatedTerm == null || (relatedTerm = relatedTerm.trim()).equals("")) &&
	    type != TermTypes.SYNONYM && type != TermTypes.ABBREVIATION)
	    throw new WebODEException ("Invalid parameters.");

	int parentId;
	int relatedId;
	if (parentTerm == null || parentTerm.trim().equals("")) {
	    relatedId = SQLUtil.getTermId (con, ontology, relatedTerm, TermTypes.CONCEPT);
	}
	else {
	    relatedId = SQLUtil.getTermId (con, ontology, relatedTerm, TermTypes.INSTANCE_ATTRIBUTE,
					   parentId = SQLUtil.getTermId (con, ontology, parentTerm, TermTypes.CONCEPT));
	    if (relatedId < 0)
		relatedId = SQLUtil.getTermId (con, ontology, relatedTerm, TermTypes.CLASS_ATTRIBUTE, parentId);
	}
	Term term = new Term (ontology, name, description, type, relatedId);
	term.store(con);
    }

    public static SynonymAbbreviation[] getSynonyms (DBConnection con,
						     String ontology, String term, String container)
	throws SQLException, WebODEException
    {
	return getItems (con, ontology, term, container, SYNONYM);
    }

    public static SynonymAbbreviation[] getAbbreviations (DBConnection con,
							  String ontology, String term, String container)
	throws SQLException, WebODEException
    {
	return getItems (con, ontology, term, container, ABBREVIATION);
    }

    protected static SynonymAbbreviation[] getItems (DBConnection con, String ontology,
						     String name, String container, int type)
	throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals("") ||
	    name == null || (name = name.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt = null;
	ResultSet          rset = null;
	try {
	    int containerId = -1;
	    if (container != null)
		containerId = SQLUtil.getTermId (con, ontology, container, TermTypes.CONCEPT);
	    pstmt = con.prepareStatement ("select t.name, t.description " +
					  " from ode_terms_glossary t, ode_terms_glossary t1, ode_ontology o " +
					  " where t.parent_id = t1.term_id and t1.name = ? " +
					  " and o.ontology_id = t1.ontology_id and o.name = ? and " +
					  " t.type = ? " + (containerId < 0 ? "" : "and t1.parent_id = ?"));
	    pstmt.setString (1, name);
	    pstmt.setString (2, ontology);
	    pstmt.setInt    (3, type);
	    if (containerId >= 0)
		pstmt.setInt (4, containerId);

	    rset = pstmt.executeQuery ();
	    if (rset.next()) {
		Vector v = new Vector(200);
		do {
		    v.addElement (new SynonymAbbreviation (rset.getString (1), // Name of the synonym
							   name,
							   container,
							   type,
							   rset.getString (2))); // Description
		} while (rset.next());
		SynonymAbbreviation[] asa = new SynonymAbbreviation[v.size()];
		v.copyInto (asa);
		return asa;
	    }
	    else
		return null;
	} finally {
	    if (rset != null) rset.close();
	    if (pstmt != null) pstmt.close();
	}
    }

    public static SynonymAbbreviation getSynonym (DBConnection con,
						  String ontology, String term,
						  String parent, String synonym)
	throws SQLException, WebODEException
    {
	return getItem (con, ontology, term, parent, synonym, SYNONYM);
    }

    public static SynonymAbbreviation getAbbreviation (DBConnection con,
						       String ontology, String term,
						       String parent, String abbr)
	throws SQLException, WebODEException
    {
	return getItem (con, ontology, term, parent, abbr, ABBREVIATION);
    }

    protected static SynonymAbbreviation getItem (DBConnection con, String ontology,
						  String name, String parent,
						  String synAbbr, int type)
	throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals("") ||
	    name == null || (name = name.trim()).equals("") ||
	    synAbbr == null || (synAbbr = synAbbr.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt = null;
	ResultSet          rset = null;
	try {
	    int containerId = -1;
	    if (parent != null && (parent = parent.trim()).length() > 0)
		containerId = SQLUtil.getTermId (con, ontology, parent, TermTypes.CONCEPT);
	    pstmt = con.prepareStatement ("select t.name, t.description " +
					  " from ode_terms_glossary t, ode_terms_glossary t1, ode_ontology o " +
					  " where t.parent_id = t1.term_id and t1.name = ? " +
					  " and o.ontology_id = t1.ontology_id and o.name = ? and " +
					  " t.type = ? and t.name = ?"  +
					  (containerId < 0 ? "" : "and t1.parent_id = ?"));
	    pstmt.setString (1, name);
	    pstmt.setString (2, ontology);
	    pstmt.setInt    (3, type);
	    pstmt.setString (4, synAbbr);
	    if (containerId >= 0)
		pstmt.setInt (5, containerId);

	    rset = pstmt.executeQuery ();
	    if (rset.next()) {
		return new SynonymAbbreviation (rset.getString (1), // Name of the synonym
						name,
						parent,
						type,
						rset.getString (2)); // Description
	    }
	    else
		return null;
	} finally {
	    if (rset != null) rset.close();
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Updates a given synonym/abbreviation.
     *
     * @param con   The database connection.
     * @param ontology The ontology.
     * @param originalName The original term's name.
     */
    public void update (DBConnection con, String ontology, String originalName)
	throws SQLException, WebODEException
    {
	if ((name == null || (name = name.trim()).equals("") ||
	     ontology == null || (ontology = ontology.trim()).equals("") ||
	     relatedTerm == null || (relatedTerm = relatedTerm.trim()).equals("")) &&
	    type != TermTypes.SYNONYM && type != TermTypes.ABBREVIATION)
	    throw new WebODEException ("Invalid parameters.");

	int parentId = -1;
	int relatedId = -1;
	System.out.println ("RT : "  +relatedTerm + ".  PT: " + parentTerm + "-" + (parentTerm == null));
	if (parentTerm == null || parentTerm.trim().equals("")) {
	    System.out.println ("1");
	    relatedId = SQLUtil.getTermId (con, ontology, relatedTerm, TermTypes.CONCEPT);
	    System.out.println ("2");
	}
	else {
	    System.out.println ("3");
	    relatedId = SQLUtil.getTermId (con, ontology, relatedTerm, TermTypes.INSTANCE_ATTRIBUTE,
					   parentId = SQLUtil.getTermId (con, ontology, parentTerm, TermTypes.CONCEPT));
	    if (relatedId < 0)
		relatedId = SQLUtil.getTermId (con, ontology, relatedTerm, TermTypes.CLASS_ATTRIBUTE, parentId);
	    System.out.println ("4");
	}

	System.out.println ("RID: " + relatedId + "..P: " + parentId);
	Term term = new Term (ontology, name, description, type,
			      relatedId);
	if (parentTerm == null)
	    term.update (con, originalName, relatedTerm);
	else
	    term.update (con, originalName, parentTerm, relatedTerm);
    }

    /**
     * Removes the synonym/abbreviation.
     */
    public void remove (DBConnection con, String ontology)
	throws SQLException, WebODEException
    {
	if ((name == null || (name = name.trim()).equals("") ||
	     ontology == null || (ontology = ontology.trim()).equals("") ||
	     relatedTerm == null || (relatedTerm = relatedTerm.trim()).equals("")) &&
	    type != TermTypes.SYNONYM && type != TermTypes.ABBREVIATION)
	    throw new WebODEException ("Invalid parameters.");

	Term.removeTerm (con, ontology, name, relatedTerm);
    }
}










