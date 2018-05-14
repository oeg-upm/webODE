package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;

/**
 * The class to encapsulate class attribute values.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.5
 */
public class ClassAttributeValue
{
    private transient Attribute cad;

    public ClassAttributeValue (Attribute cad)
    {
	this.cad = cad;
    }

    public int addValue (DBConnection con, String value, String ontology) throws WebODEException, SQLException
    {
	if (cad.values != null && cad.maxCardinality >= 0 && cad.values.length == cad.maxCardinality)
	    throw new WebODEException ("Maximum cardinality exceeded (" + cad.maxCardinality + ").");

	PreparedStatement pstmt = null;
	int id;
	try {
	    pstmt = con.prepareStatement ("insert into ode_concept_attribute_value " +
					  "(concept_attribute_value_id, attribute_id, value) " +
					  " values (?, ?, ?)");
	    pstmt.setInt   (1, id = SQLUtil.nextVal (con, "ode_sequence_value_id"));
	    pstmt.setInt   (2, SQLUtil.getTermId (con, ontology, cad.name,
						  cad instanceof ClassAttributeDescriptor ? TermTypes.CLASS_ATTRIBUTE :
						  TermTypes.INSTANCE_ATTRIBUTE,
						  SQLUtil.getTermId (con, ontology, cad.termName, TermTypes.CONCEPT)));
	    if (value == null || (value = value.trim()).equals(""))
		pstmt.setNull   (3, Types.VARCHAR);
	    else
		pstmt.setString (3, value);

	    pstmt.executeUpdate();

	    return id;
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }

    public void removeValue (DBConnection con, String value, String ontology)
	throws WebODEException, SQLException
    {
	if (value == null || (value = value.trim()).equals(""))
	    throw new WebODEException ("Empty values not allowed.");

	PreparedStatement pstmt = null;
	try {
	    pstmt = con.prepareStatement ("delete from ode_concept_attribute_value " +
					  "where attribute_id = ? and value = ? ");
	    pstmt.setInt    (1, SQLUtil.getTermId (con, ontology, cad.name,
						   cad instanceof ClassAttributeDescriptor ? TermTypes.CLASS_ATTRIBUTE :
						   TermTypes.INSTANCE_ATTRIBUTE,
						   SQLUtil.getTermId (con, ontology, cad.termName, TermTypes.CONCEPT)));
	    pstmt.setString (2, value);

	    pstmt.executeUpdate();
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Gets the number of values for a given class attribute.
     */
    public int getValueCount (DBConnection con, String ontology) throws SQLException, WebODEException
    {
	PreparedStatement pstmt = null;
	ResultSet          rset = null;
	try {
		int parentId=SQLUtil.getTermId(con, ontology, cad.termName, TermTypes.CONCEPT);
	    pstmt = con.prepareStatement ("select count(*) from ode_concept_attribute_value " +
					  " where attribute_id = ?");
	    pstmt.setInt   (1, SQLUtil.getTermId (con, ontology, cad.name, TermTypes.CLASS_ATTRIBUTE, parentId));
	    rset = pstmt.executeQuery();

	    rset.next();
	    return rset.getInt (1);
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Get values and stores them in the class attibute descriptor.
     */
    public void getValues (DBConnection con, String ontology) throws SQLException, WebODEException
    {
	PreparedStatement pstmt = null;
	ResultSet          rset = null;
	try {
	    pstmt = con.prepareStatement ("select ocav.value from ode_concept_attribute_value ocav " +
					  (cad instanceof InstanceAttributeDescriptor ?
					   ", ode_instance_attribute oia " : "") +
					  " where ocav.attribute_id = ? " +
					  (cad instanceof InstanceAttributeDescriptor ?
					   " and oia.is_ia_attribute_term_id = ocav.attribute_id and " +
					   "  not " +
					   " ((oia.min_value is not null and " +
					   "   ocav.concept_attribute_value_id = oia.min_value) " +
					   "  or (oia.max_value is not null and " +
					   "     ocav.concept_attribute_value_id = oia.max_value))" : ""));

	    pstmt.setInt   (1, SQLUtil.getTermId (con, ontology, cad.name,
						  cad instanceof ClassAttributeDescriptor ? TermTypes.CLASS_ATTRIBUTE :
						  TermTypes.INSTANCE_ATTRIBUTE,
						  SQLUtil.getTermId (con, ontology, cad.termName, TermTypes.CONCEPT)));
	    rset = pstmt.executeQuery();

	    if (rset.next()) {
		Vector v = new Vector();
		do {
		    v.addElement (rset.getString(1));
		} while (rset.next());

		cad.values = new String[v.size()];
		v.copyInto (cad.values);
	    }
	    else
		cad.values = null;
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }
}


