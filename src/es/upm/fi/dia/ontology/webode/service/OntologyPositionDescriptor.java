package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.webode.service.util.*;


/**
 *
 */
public class OntologyPositionDescriptor implements java.io.Serializable
{
    public String ontologyName;
    public int x, y;

    public OntologyPositionDescriptor (String name,
				       int x,
				       int y)
    {
	this.ontologyName = name;
	this.x = x;
	this.y = y;
    }

    public static OntologyPositionDescriptor[] getOntologyPositions (DBConnection con,
								     String name)
	throws SQLException, WebODEException
    {
	if (name == null || (name = name.trim()).equals(""))
	    throw new WebODEException ("Invalid ontology name.");

	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
	    pstmt = con.prepareStatement ("select o.name, oi.x, oi.y " +
					  " from ode_o_uses_o_info oi, ode_ontology o, ode_ontology o1 " +
					  " where o.ontology_id = oi.ontology_id and " +
					  " o1.ontology_id = oi.context_ontology_id and " +
					  " o1.name = ?");
	    pstmt.setString (1, name);
	    rset = pstmt.executeQuery();
	    if (rset.next()) {
		Vector v = new Vector (200);

		do {
		    v.addElement (new OntologyPositionDescriptor (rset.getString(1),
								  rset.getInt (2),
								  rset.getInt (3)));
		} while (rset.next());

		OntologyPositionDescriptor[] aopd = new OntologyPositionDescriptor[v.size()];
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

    public void store (DBConnection con, String contextOntology) throws SQLException
    {
	PreparedStatement pstmt = null;
	try {
	    int contextId = SQLUtil.getOntologyId (con, contextOntology);
	    int ontologyId = SQLUtil.getOntologyId (con, ontologyName);

	    pstmt = con.prepareStatement ("insert into ode_o_uses_o_info (context_ontology_id, " +
					  "ontology_id, x, y) values (?, ?, ?, ?)");
	    pstmt.setInt (1, contextId);
	    pstmt.setInt (2, ontologyId);
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

    /**
     * Updates a position.
     */
    public void update (DBConnection con, String contextOntology) throws SQLException, WebODEException
    {
	if (ontologyName == null || (ontologyName = ontologyName.trim()).equals (""))
	    throw new WebODEException ("Invalid object state.");
	if (contextOntology == null || (contextOntology = contextOntology.trim()).equals (""))
	    throw new WebODEException ("Invalid parameter.");

	PreparedStatement pstmt = null;
	try {
	    int contextId = SQLUtil.getOntologyId (con, contextOntology);
	    int ontologyId = SQLUtil.getOntologyId (con, ontologyName);

	    pstmt = con.prepareStatement ("update ode_o_uses_o_info set x = ?, y = ? " +
					  " where context_ontology_id = ? and ontology_id = ?");
	    pstmt.setInt (1, x);
	    pstmt.setInt (2, y);
	    pstmt.setInt (3, contextId);
	    pstmt.setInt (4, ontologyId);

	    pstmt.executeUpdate();
	} finally {
	    try {
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {
	    }
	}
    }
}


