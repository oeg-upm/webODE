package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;


/**
 * Descriptor for a term's position withing a view.
 */
public class TermPositionDescriptor implements java.io.Serializable
{
    public String name;
    public int x, y;

    public TermPositionDescriptor (String name,
				   int x,
				   int y)
    {
	this.name = name;
	this.x = x;
	this.y = y;
    }

    public static TermPositionDescriptor[] getTermPositions (DBConnection con,
							     String ontology, String view)
	throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals(""))
	    throw new WebODEException ("Invalid ontology name.");
	if (view == null || (view = view.trim()).equals(""))
	    throw new WebODEException ("Invalid view name.");

	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
	    int ontologyId = SQLUtil.getOntologyId (con, ontology);
	    pstmt = con.prepareStatement ("select tg.name, tp.x, tp.y " +
					  " from ode_terms_glossary tg, ode_term_position tp, " +
					  "      ode_terms_glossary t1 " +
					  " where tg.ontology_id = ? and tg.term_id = tp.term_id and " +
					  "       t1.name = ? and t1.term_id = tp.view_id");
	    pstmt.setInt    (1, ontologyId);
	    pstmt.setString (2, view);
	    rset = pstmt.executeQuery();
	    if (rset.next()) {
		Vector v = new Vector (200);
		do {
		    v.addElement (new TermPositionDescriptor (rset.getString(1),
							      rset.getInt (2),
							      rset.getInt (3)));
		} while (rset.next());
		TermPositionDescriptor[] aopd = new TermPositionDescriptor[v.size()];
		v.copyInto (aopd);

		return aopd;
	    }
	    else
		return null;
	} finally {
	    try {
		if (rset  != null) rset.close();
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {}
	}
    }

    public void store (DBConnection con, String contextOntology, String view) throws SQLException, WebODEException
    {
	if (name == null || (name = name.trim()).equals ("") ||
	    view == null || (view = view.trim()).equals (""))
	    throw new WebODEException ("Invalid parameter.");

	PreparedStatement pstmt = null;
	try {
	    int termId    = SQLUtil.getTermId (con, contextOntology, name, TermTypes.CONCEPT);
	    int viewId    = SQLUtil.getTermId (con, contextOntology, view, TermTypes.VIEW);

	    pstmt = con.prepareStatement ("insert into ode_term_position (term_id, view_id, x, y) " +
					  "values (?, ?, ?, ?)");
	    pstmt.setInt (1, termId);
	    pstmt.setInt (2, viewId);
	    pstmt.setInt (3, x);
	    pstmt.setInt (4, y);

	    pstmt.executeUpdate();
	} finally {
	    try {
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {
	    }
	}
    }

    public void update (DBConnection con, String contextOntology, String view) throws SQLException, WebODEException
    {
	if (name == null || (name = name.trim()).equals ("") ||
	    contextOntology == null || (contextOntology = contextOntology.trim()).equals("") ||
	    view == null || (view = view.trim()).equals (""))
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt = null;
	try {
	    int termId    = SQLUtil.getTermId (con, contextOntology, name, TermTypes.CONCEPT);
	    int viewId    = SQLUtil.getTermId (con, contextOntology, view, TermTypes.VIEW);

	    pstmt = con.prepareStatement ("update ode_term_position set x = ?, y = ? where term_id = ? and view_id = ?");
	    pstmt.setInt (1, x);
	    pstmt.setInt (2, y);
	    pstmt.setInt (3, termId);
	    pstmt.setInt (4, viewId);

	    pstmt.executeUpdate();
	} finally {
	    try {
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {
	    }
	}
    }

    public static void remove (DBConnection con, String name, String contextOntology,
			       String view) throws SQLException, WebODEException
    {
	if (name == null || (name = name.trim()).equals ("") ||
	    contextOntology == null || (contextOntology = contextOntology.trim()).equals("") ||
	    view == null || (view = view.trim()).equals (""))
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt = null;
	try {
	    int termId    = SQLUtil.getTermId (con, contextOntology, name, TermTypes.CONCEPT);
	    int viewId    = SQLUtil.getTermId (con, contextOntology, view, TermTypes.VIEW);

	    pstmt = con.prepareStatement ("delete from ode_term_position where term_id = ? and view_id = ?");
	    pstmt.setInt (1, termId);
	    pstmt.setInt (2, viewId);

	    pstmt.executeUpdate();
	} finally {
	    try {
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {
	    }
	}
    }
}





