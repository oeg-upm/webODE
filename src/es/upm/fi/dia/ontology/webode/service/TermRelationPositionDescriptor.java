package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;


/**
 *
 * @version 1.1
 */
public class TermRelationPositionDescriptor implements java.io.Serializable
{
    public String name;
    public int x, y;
    public String origin;
    public String destination;
    public int cardinality;

    public TermRelationPositionDescriptor (String name,
					   String origin,
					   String destination,
					   int x,
					   int y,
					   int cardinality)
    {
	this.name = name;
	this.origin = origin;
	this.destination = destination;
	this.x = x;
	this.y = y;
	this.cardinality = cardinality;
    }

    public TermRelationPositionDescriptor (String name,
					   String origin,
					   String destination,
					   int x,
					   int y)
    {
	this (name, origin, destination, x, y, -1);
    }


    public static TermRelationPositionDescriptor[] getTermRelationPositions (DBConnection con,
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
	    pstmt = con.prepareStatement ("select tg.name, t2.name, t3.name, vr.bendx, vr.bendy " +
					  " from ode_terms_glossary tg, ode_view_relation vr, " +
					  "      ode_terms_glossary t1, ode_terms_glossary t2,  " +
					  "      ode_terms_glossary t3 " +
					  " where tg.term_id = vr.relation_id and " +
					  "       t1.name = ? and t1.ontology_id = ? and t1.term_id = vr.view_id and " +
					  "       vr.origin_id = t2.term_id and vr.destination_id = t3.term_id");

	    pstmt.setString (1, view);
	    pstmt.setInt    (2, ontologyId);
	    rset = pstmt.executeQuery();
	    if (rset.next()) {
		Vector v = new Vector (200);
		do {
		    v.addElement (new TermRelationPositionDescriptor (TermRelation._getRelationName (rset.getString(1)),
								      rset.getString (2),
								      rset.getString (3),
								      rset.getInt (4),
								      rset.getInt (5)));
		} while (rset.next());
		TermRelationPositionDescriptor[] aopd = new TermRelationPositionDescriptor[v.size()];
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
	if (name == null || (name = name.trim()).equals (""))
	    throw new WebODEException ("Invalid parameter.");

	PreparedStatement pstmt = null;
	try {
	    int viewId    = SQLUtil.getTermId (con, contextOntology, view, TermTypes.VIEW);
	    int originId  = SQLUtil.getTermId (con, contextOntology, origin, TermTypes.CONCEPT);
	    int destinationId  = SQLUtil.getTermId (con, contextOntology, destination, TermTypes.CONCEPT);

	    // Is it a built-in relation
	    int termId    = TermRelation._getBuiltIn (name);
	    if (termId < 0)
		termId    = SQLUtil.getTermId (con, contextOntology,
					       "_" + originId + "_" + destinationId + "_" + name, TermTypes.RELATION);

	    //System.out.println ("TI " + termId + ". VI: " + viewId + ". OI: " + originId +
	    //". DI: " + destinationId);
	    pstmt = con.prepareStatement
		("insert into ode_view_relation (relation_id, view_id, origin_id, destination_id, bendx, bendy) " +
		 "values (?, ?, ?, ?, ?, ?)");
	    pstmt.setInt (1, termId);
	    pstmt.setInt (2, viewId);
	    pstmt.setInt (3, originId);
	    pstmt.setInt (4, destinationId);
	    pstmt.setInt (5, x);
	    pstmt.setInt (6, y);

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
	    contextOntology == null || (contextOntology = contextOntology.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters.");

	PreparedStatement pstmt = null;
	try {
	    int viewId    = SQLUtil.getTermId (con, contextOntology, view, TermTypes.VIEW);
	    int originId  = SQLUtil.getTermId (con, contextOntology, origin, TermTypes.CONCEPT);
	    int destinationId  = SQLUtil.getTermId (con, contextOntology, destination, TermTypes.CONCEPT);

	    // Is it a built-in relation
	    int termId    = TermRelation._getBuiltIn (name);
	    if (termId < 0)
		termId    = SQLUtil.getTermId (con, contextOntology,
					       "_" + originId + "_" + destinationId + "_" + name, TermTypes.RELATION);

	    pstmt = con.prepareStatement
		("update ode_view_relation set bendx = ?, bendy = ? where relation_id = ? and view_id = ? and " +
		 "origin_id = ? and destination_id = ?");
	    pstmt.setInt (1, x);
	    pstmt.setInt (2, y);
	    pstmt.setInt (3, termId);
	    pstmt.setInt (4, viewId);
	    pstmt.setInt (5, originId);
	    pstmt.setInt (6, destinationId);

	    pstmt.executeUpdate();
	} finally {
	    try {
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {
	    }
	}
    }

    public void remove (DBConnection con, String contextOntology, String view) throws SQLException, WebODEException
    {
	if (name == null || (name = name.trim()).equals ("") ||
	    contextOntology == null || (contextOntology = contextOntology.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters.");
	if (view == null || (view = view.trim()).equals(""))
	    throw new WebODEException ("Invalid view name.");

	PreparedStatement pstmt = null;
	try {
	    int viewId    = SQLUtil.getTermId (con, contextOntology, view, TermTypes.VIEW);
	    int originId  = SQLUtil.getTermId (con, contextOntology, origin, TermTypes.CONCEPT);
	    int destinationId  = SQLUtil.getTermId (con, contextOntology, destination, TermTypes.CONCEPT);

	    // Is it a built-in relation
	    int termId    = TermRelation._getBuiltIn (name);
	    if (termId < 0)
		termId    = SQLUtil.getTermId (con, contextOntology,
					       "_" + originId + "_" + destinationId + "_" + name, TermTypes.RELATION);

	    pstmt = con.prepareStatement
		("delete from ode_view_relation where relation_id = ? and view_id = ? and " +
		 "origin_id = ? and destination_id = ?");
	    pstmt.setInt (1, termId);
	    pstmt.setInt (2, viewId);
	    pstmt.setInt (3, originId);
	    pstmt.setInt (4, destinationId);

	    pstmt.executeUpdate();
	} finally {
	    try {
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {
	    }
	}
    }

}








