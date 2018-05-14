package es.upm.fi.dia.ontology.webode.service;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;

import java.sql.*;
import java.util.*;

public class AttributeInference implements java.io.Serializable
{
    public String term, iterm, attr, inferredAttr;
    public boolean isClassAttribute;

    public AttributeInference (String term, String attr, String iterm, String inferredAttr)
    {
	this.term         = term;
	this.attr         = attr;
	this.iterm        = iterm;
	this.inferredAttr = inferredAttr;
    }

    public void store (DBConnection con, String ontology) throws WebODEException, SQLException
    {
	if (term == null || (term = term.trim()).equals ("") ||
	    iterm == null || (iterm = iterm.trim()).equals ("") ||
	    attr == null || (attr = attr.trim()).equals ("") ||
	    inferredAttr == null || (inferredAttr = inferredAttr.trim()).equals (""))
	    throw new WebODEException ("Invalid attributes");

	PreparedStatement pstmt = null;
	try {
	    int termId = SQLUtil.getTermId (con, ontology, term, TermTypes.CONCEPT);
	    int attrId = SQLUtil.getTermId (con, ontology, attr, TermTypes.CLASS_ATTRIBUTE, termId);
	    if (attrId < 0) {
		attrId = SQLUtil.getTermId (con, ontology, attr, TermTypes.INSTANCE_ATTRIBUTE, termId);
	    }

	    int itermId = SQLUtil.getTermId (con, ontology, iterm, TermTypes.CONCEPT);
	    int inferredAttrId = SQLUtil.getTermId (con, ontology, inferredAttr, TermTypes.CLASS_ATTRIBUTE, itermId);
	    if (inferredAttrId < 0) {
		inferredAttrId = SQLUtil.getTermId (con, ontology, inferredAttr, TermTypes.INSTANCE_ATTRIBUTE, itermId);
	    }
	    if (termId < 0 || attrId < 0 || itermId < 0 || inferredAttrId < 0)
		throw new WebODEException ("Cannot find attributes.");

	    pstmt = con.prepareStatement ("insert into ode_attribute_inference " +
					  "(attribute_id, inferred_by_id) values (?, ?)");
	    pstmt.setInt   (1, inferredAttrId);
	    pstmt.setInt   (2, attrId);

	    pstmt.executeUpdate();
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }

    public void remove (DBConnection con, String ontology) throws WebODEException, SQLException
    {
	if (term == null || (term = term.trim()).equals ("") ||
	    iterm == null || (iterm = iterm.trim()).equals ("") ||
	    attr == null || (attr = attr.trim()).equals ("") ||
	    inferredAttr == null || (inferredAttr = inferredAttr.trim()).equals (""))
	    throw new WebODEException ("Invalid attributes");

	PreparedStatement pstmt = null;
	try {
	    int termId = SQLUtil.getTermId (con, ontology, term, TermTypes.CONCEPT);
	    int attrId = SQLUtil.getTermId (con, ontology, attr, TermTypes.CLASS_ATTRIBUTE, termId);
	    if (attrId < 0) {
		attrId = SQLUtil.getTermId (con, ontology, attr, TermTypes.INSTANCE_ATTRIBUTE, termId);
	    }

	    int itermId = SQLUtil.getTermId (con, ontology, iterm, TermTypes.CONCEPT);
	    int inferredAttrId = SQLUtil.getTermId (con, ontology, inferredAttr, TermTypes.CLASS_ATTRIBUTE, itermId);
	    if (inferredAttrId < 0) {
		inferredAttrId = SQLUtil.getTermId (con, ontology, inferredAttr, TermTypes.INSTANCE_ATTRIBUTE, itermId);
	    }
	    if (termId < 0 || attrId < 0 || itermId < 0 || inferredAttrId < 0)
		throw new WebODEException ("Cannot find attributes.");

	    pstmt = con.prepareStatement ("delete from ode_attribute_inference " +
					  "where attribute_id = ? and inferred_by_id = ?");
	    pstmt.setInt   (1, inferredAttrId);
	    pstmt.setInt   (2, attrId);

	    pstmt.executeUpdate();
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }


    public static AttributeInference[] getAttributesInferredBy (DBConnection con, String ontology,
								String term, String attr)
	throws SQLException, WebODEException
    {
	int termId = SQLUtil.getTermId (con, ontology, term, TermTypes.CONCEPT);
	int attrId = SQLUtil.getTermId (con, ontology, attr, TermTypes.CLASS_ATTRIBUTE, termId);
	if (attrId < 0) {
	    attrId = SQLUtil.getTermId (con, ontology, attr, TermTypes.INSTANCE_ATTRIBUTE, termId);
	}
	PreparedStatement pstmt = null;
	ResultSet          rset = null;
	try {
	    pstmt = con.prepareStatement ("select t1.name, t2.name " +
					  " from ode_terms_glossary t1, ode_terms_glossary t2," +
					  "      ode_attribute_inference oai " +
					  " where t2.term_id = oai.attribute_id and " +
					  "       t2.parent_id = t1.term_id and " +
					  "       oai.inferred_by_id = ? " +
					  " order by t1.name, t2.name");

	    pstmt.setInt (1, attrId);
	    rset = pstmt.executeQuery();
	    if (rset.next()) {
		Vector v = new Vector(200);
		do {
		    v.addElement (new AttributeInference (term, attr, rset.getString (1), rset.getString(2)));
		} while (rset.next());
		AttributeInference[] aai = new AttributeInference[v.size()];
		v.copyInto (aai);

		return aai;
	    }
	    else
		return null;
	} finally {
	    if (rset  != null) rset.close();
	    if (pstmt != null) pstmt.close();
	}
    }

    public static AttributeInference[] getInferringAttributes (DBConnection con, String ontology,
							       String term, String attr)
	throws SQLException, WebODEException
    {
	int termId = SQLUtil.getTermId (con, ontology, term, TermTypes.CONCEPT);
	int attrId = SQLUtil.getTermId (con, ontology, attr, TermTypes.CLASS_ATTRIBUTE, termId);
	if (attrId < 0) {
	    attrId = SQLUtil.getTermId (con, ontology, attr, TermTypes.INSTANCE_ATTRIBUTE, termId);
	}
	PreparedStatement pstmt = null;
	ResultSet          rset = null;
	try {
	    pstmt = con.prepareStatement ("select t1.name, t2.name " +
					  " from ode_terms_glossary t1, ode_terms_glossary t2," +
					  "      ode_attribute_inference oai " +
					  " where t2.term_id = oai.inferred_by_id and " +
					  "       t2.parent_id = t1.term_id and " +
					  "       oai.attribute_id = ? " +
					  " order by t1.name, t2.name ");
	    pstmt.setInt (1, attrId);
	    rset = pstmt.executeQuery();
	    if (rset.next()) {
		Vector v = new Vector(200);
		do {
		    v.addElement (new AttributeInference (rset.getString (1), rset.getString(2), term, attr));
		} while (rset.next());
		AttributeInference[] aai = new AttributeInference[v.size()];
		v.copyInto (aai);

		return aai;
	    }
	    else
		return null;
	} finally {
	    if (rset  != null) rset.close();
	    if (pstmt != null) pstmt.close();
	}
    }
}

