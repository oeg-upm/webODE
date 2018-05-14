package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;

/**
 * The descriptor for references.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.5
 */
public class ReferenceDescriptor implements java.io.Serializable
{
    /**
     * The ontology the reference is related to.
     */
    public String ontology;

    /**
     * The name of the reference.
     */
    public String name;

    /**
     * The reference description.
     */
    public String description;

    public ReferenceDescriptor (String name, String description, String ontology)
    {
	this.name   = name;
	this.description = description;
	this.ontology = ontology;
    }

    public ReferenceDescriptor (String name, String description)
    {
	this (name, description, null);
    }

    public void store (DBConnection con) throws SQLException, WebODEException
    {
	store (con, true);
    }


    public void store (DBConnection con, boolean b) throws SQLException, WebODEException
    {
	PreparedStatement pstmt  = null;
	PreparedStatement pstmt1 = null;
	ResultSet rset = null;
	boolean bTransaction = false;
	try {
	    if (ontology == null)
		throw new WebODEException ("The ontology name is null.");

	    String foo1, foo2 = null;
	    if (name == null || (foo1 = name.trim()).equals (""))
		throw new WebODEException ("Name is null or empty.");

	    pstmt = con.prepareStatement ("select ontology_id from ode_ontology where name = ?");
	    pstmt.setString (1, ontology);
	    rset = pstmt.executeQuery();
	    int id;
	    if (rset.next()) {
		id = rset.getInt (1);
		rset.close();
		pstmt.close();
	    }
	    else {
		throw new WebODEException ("No such ontology: " + ontology + ".");
	    }

	    int refId  = SQLUtil.getReferenceId (con, ontology, name);
	    if (refId >= 0)
		throw new WebODEException ("A reference with name " + name + " already exists.");

	    // Get the next reference id.
	    refId = SQLUtil.nextVal (con, "ode_sequence_reference_id");


	    // We are going to do two insertions in one transaction.
	    con.setAutoCommit (false);
	    bTransaction = true;
	    pstmt = con.prepareStatement ("insert into ode_reference (ontology_id, reference_id, name, description) " +
					  "values (?, ?, ?, ?)");
	    pstmt.setInt    (1, id);
	    pstmt.setInt    (2, refId);
	    pstmt.setString (3, foo1);
	    if (description == null || (foo2 = description.trim()).equals(""))
		pstmt.setNull (4, Types.VARCHAR);
	    else
		pstmt.setString (4, foo2);
	    pstmt.executeUpdate();

	    if (b) {
		pstmt1 = con.prepareStatement ("insert into ode_ontology_has_reference (ontology_id, reference_id) " +
					       "values (?, ?)");
		pstmt1.setInt (1, id);
		pstmt1.setInt (2, refId);
		pstmt1.executeUpdate();
	    }

	    con.commit();
	} catch (SQLException sqle) {
	    if (bTransaction) con.rollback();
	    throw sqle;
	} finally {
	    if (rset != null) rset.close();
	    if (pstmt != null) pstmt.close();
	    if (pstmt1 != null) pstmt1.close();
	    con.setAutoCommit (true);
	}
    }

    /**
     * Removes a reference.
     */
    public void remove (DBConnection con) throws SQLException, WebODEException
    {
	if (name == null || (name = name.trim()).equals (""))
	    throw new WebODEException ("Invalid name for the reference.");

	if (ontology == null || (ontology = ontology.trim()).equals(""))
	    throw new WebODEException ("Invalid ontology name for the reference.");

	PreparedStatement pstmt  = null;
	try {
	    int ontologyId = SQLUtil.getOntologyId (con, ontology);
	    pstmt = con.prepareStatement ("delete from ode_reference where " +
					  " ontology_id = ? and name = ?");
	    pstmt.setInt    (1, ontologyId);
	    pstmt.setString (2, name);
	    pstmt.executeUpdate ();
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Ties a reference to a term.
     *
     */
    public static void relateReferenceToTerm (DBConnection con, String ontology, String reference, String term)
	throws SQLException, WebODEException
    {
	if (reference == null || (reference = reference.trim()).equals ("") ||
	    term == null || (term = term.trim()).equals ("") ||
	    ontology == null || (ontology = ontology.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters");

	PreparedStatement pstmt = null;
	try {
	    int ontoId = SQLUtil.getOntologyId (con, ontology);
	    int termId = SQLUtil.getTermId (con, ontology, term);
	    int refId  = SQLUtil.getReferenceId (con, ontology, reference);

	    pstmt = con.prepareStatement ("insert into ode_has_reference (ontology_id, term_id, reference_id) " +
					  " values (?, ?, ?)");
	    pstmt.setInt (1, ontoId);
	    pstmt.setInt (2, termId);
	    pstmt.setInt (3, refId);
	    pstmt.executeUpdate();
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Ties a reference to a term.
     *
     */
    public static void relateReferenceToTerm (DBConnection con, String ontology,
					      String reference, String term, String parent)
	throws SQLException, WebODEException
    {
	if (reference == null || (reference = reference.trim()).equals ("") ||
	    term == null || (term = term.trim()).equals ("") ||
	    ontology == null || (ontology = ontology.trim()).equals("") ||
	    parent == null || (parent = parent.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters");

	PreparedStatement pstmt = null;
	try {
	    int parentId = SQLUtil.getTermId (con, ontology, parent, TermTypes.CONCEPT);
	    if (parentId < 0)
		parentId = SQLUtil.getTermId (con, ontology, parent);
	    if (parentId < 0)
		parentId = SQLUtil.getTermId (con, ontology, parent, TermTypes.INSTANCE_SET);

	    int ontoId = SQLUtil.getOntologyId (con, ontology);
	    int termId = SQLUtil.getTermId (con, ontology, term, TermTypes.CLASS_ATTRIBUTE, parentId);
	    if (termId < 0)
		termId = SQLUtil.getTermId (con, ontology, term, TermTypes.INSTANCE_ATTRIBUTE, parentId);
	    if (termId < 0)
		termId = SQLUtil.getTermId (con, ontology, term, TermTypes.INSTANCE, parentId);
	    int refId  = SQLUtil.getReferenceId (con, ontology, reference);

	    pstmt = con.prepareStatement ("insert into ode_has_reference (ontology_id, term_id, reference_id) " +
					  " values (?, ?, ?)");
	    pstmt.setInt (1, ontoId);
	    pstmt.setInt (2, termId);
	    pstmt.setInt (3, refId);
	    pstmt.executeUpdate();
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Unties a reference to a term.
     *
     */
    public static void unrelateReferenceToTerm (DBConnection con, String ontology, String reference, String term)
	throws SQLException, WebODEException
    {
	if (reference == null || (reference = reference.trim()).equals ("") ||
	    term == null || (term = term.trim()).equals ("") ||
	    ontology == null || (ontology = ontology.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters");

	PreparedStatement pstmt = null;
	try {
	    int ontoId = SQLUtil.getOntologyId (con, ontology);
	    int termId = SQLUtil.getTermId (con, ontology, term);
	    int refId  = SQLUtil.getReferenceId (con, ontology, reference);

	    pstmt = con.prepareStatement ("delete from ode_has_reference where ontology_id = ? and " +
					  "       term_id = ? and reference_id = ?");
	    pstmt.setInt (1, ontoId);
	    pstmt.setInt (2, termId);
	    pstmt.setInt (3, refId);
	    pstmt.executeUpdate();
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Unties a reference to a term.
     *
     */
    public static void unrelateReferenceToTerm (DBConnection con, String ontology,
						String reference, String term, String parent)
	throws SQLException, WebODEException
    {
	if (reference == null || (reference = reference.trim()).equals ("") ||
	    term == null || (term = term.trim()).equals ("") ||
	    ontology == null || (ontology = ontology.trim()).equals(""))
	    throw new WebODEException ("Invalid parameters");

	PreparedStatement pstmt = null;
	try {
	    int parentId = SQLUtil.getTermId (con, ontology, parent, TermTypes.CONCEPT);
	    if (parentId < 0)
		parentId = SQLUtil.getTermId (con, ontology, parent);
	    if (parentId < 0)
		parentId = SQLUtil.getTermId (con, ontology, parent, TermTypes.INSTANCE_SET);

	    int ontoId = SQLUtil.getOntologyId (con, ontology);
	    int termId = SQLUtil.getTermId (con, ontology, term, TermTypes.CLASS_ATTRIBUTE, parentId);
	    if (termId < 0)
		termId = SQLUtil.getTermId (con, ontology, term, TermTypes.INSTANCE_ATTRIBUTE, parentId);
	    if (termId < 0)
		termId = SQLUtil.getTermId (con, ontology, term, TermTypes.INSTANCE, parentId);
	    int refId  = SQLUtil.getReferenceId (con, ontology, reference);

	    pstmt = con.prepareStatement ("delete from ode_has_reference where ontology_id = ? and " +
					  "       term_id = ? and reference_id = ?");
	    pstmt.setInt (1, ontoId);
	    pstmt.setInt (2, termId);
	    pstmt.setInt (3, refId);
	    pstmt.executeUpdate();
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }



    /**
     * Retrieves the references related to a given ontology by means of a relation <i>ontology has reference</i>.
     *
     * @return The list of references.
     */
    public static ReferenceDescriptor[] getOntologyReferences (DBConnection con, String name)
	throws WebODEException, SQLException
    {
	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
	    pstmt = con.prepareStatement ("select r.name, r.description " +
					  "  from ode_ontology_has_reference o, ode_reference r, ode_ontology oo" +
					  "  where oo.name = ? and oo.ontology_id = o.ontology_id and " +
					  "  o.ontology_id = r.ontology_id and o.reference_id = r.reference_id");
	    String foo;
	    if (name == null || (foo = name.trim()).equals(""))
		throw new WebODEException ("Invalid ontology name: " + name + ".");
	    pstmt.setString (1, foo);
	    rset = pstmt.executeQuery();

	    Vector v = new Vector (100);
	    while (rset.next()) {
		v.addElement (new ReferenceDescriptor (rset.getString(1),
						       rset.getString(2),
						       foo));
	    }
	    ReferenceDescriptor[] ard = new ReferenceDescriptor[v.size()];
	    v.copyInto (ard);
	    return ard;
	}  finally {
	    if (rset != null) rset.close();
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Retrieves all the references contained in an ontology.
     *
     * @return The list of references.
     */
    public static ReferenceDescriptor[] getReferences (DBConnection con, String name)
	throws WebODEException, SQLException
    {
	if (name == null || (name = name.trim()).equals (""))
	    throw new WebODEException ("Invalid parameter");

	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
	    pstmt = con.prepareStatement ("select r.name, r.description " +
					  "  from ode_reference r, ode_ontology oo" +
					  "  where oo.name = ? and oo.ontology_id = r.ontology_id");
	    pstmt.setString (1, name);
	    rset = pstmt.executeQuery();

	    if (rset.next()) {
		Vector v = new Vector (100);
		do {
		    v.addElement (new ReferenceDescriptor (rset.getString(1),
							   rset.getString(2),
							   name));
		} while (rset.next());
		ReferenceDescriptor[] ard = new ReferenceDescriptor[v.size()];
		v.copyInto (ard);
		return ard;
	    }
	    else
		return null;
	}  finally {
	    if (rset != null) rset.close();
	    if (pstmt != null) pstmt.close();
	}
    }


    /**
     * Retrieves all the references contained in an ontology for a given term.
     *
     * @return The list of references.
     */
    public static ReferenceDescriptor[] getTermReferences (DBConnection con, String name, String term)
	throws WebODEException, SQLException
    {
	if (name == null || (name = name.trim()).equals ("") ||
	    term == null || (term = term.trim()).equals (""))
	    throw new WebODEException ("Invalid parameters");

	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
	    pstmt = con.prepareStatement ("select r.name, r.description " +
					  "  from ode_reference r, ode_ontology oo, " +
					  "       ode_has_reference ohr, ode_terms_glossary otg " +
					  "  where oo.name = ? and oo.ontology_id = r.ontology_id and " +
					  "        ohr.reference_id = r.reference_id and " +
					  "        ohr.term_id = otg.term_id and otg.name = ?");
	    pstmt.setString (1, name);
	    pstmt.setString (2, term);
	    rset = pstmt.executeQuery();

	    if (rset.next()) {
		Vector v = new Vector (100);
		do {
		    v.addElement (new ReferenceDescriptor (rset.getString(1),
							   rset.getString(2),
							   name));
		} while (rset.next());
		ReferenceDescriptor[] ard = new ReferenceDescriptor[v.size()];
		v.copyInto (ard);
		return ard;
	    }
	    else
		return null;
	}  finally {
	    if (rset != null) rset.close();
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Retrieves all the references contained in an ontology for a given term.
     *
     * @return The list of references.
     */
    public static ReferenceDescriptor[] getTermReferences (DBConnection con, String name, String term, String parent)
	throws WebODEException, SQLException
    {
	if (name == null || (name = name.trim()).equals ("") ||
	    term == null || (term = term.trim()).equals ("") ||
	    parent == null || (parent = parent.trim()).equals (""))
	    throw new WebODEException ("Invalid parameters");

	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
	    int parentId = SQLUtil.getTermId (con, name, parent, TermTypes.CONCEPT);
	    if (parentId < 0)
		parentId = SQLUtil.getTermId (con, name, parent);

	    pstmt = con.prepareStatement ("select r.name, r.description " +
					  "  from ode_reference r, ode_ontology oo, " +
					  "       ode_has_reference ohr, ode_terms_glossary otg " +
					  "  where oo.name = ? and oo.ontology_id = r.ontology_id and " +
					  "        ohr.reference_id = r.reference_id and " +
					  "        ohr.term_id = otg.term_id and otg.name = ? and " +
					  "        otg.parent_id = ?");
	    pstmt.setString (1, name);
	    pstmt.setString (2, term);
	    pstmt.setInt    (3, parentId);
	    rset = pstmt.executeQuery();

	    if (rset.next()) {
		Vector v = new Vector (100);
		do {
		    v.addElement (new ReferenceDescriptor (rset.getString(1),
							   rset.getString(2),
							   name));
		} while (rset.next());
		ReferenceDescriptor[] ard = new ReferenceDescriptor[v.size()];
		v.copyInto (ard);
		return ard;
	    }
	    else
		return null;
	}  finally {
	    if (rset != null) rset.close();
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Retrieves information for a given reference.
     *
     * @param name The name of the reference.
     * @param ontology The name of the ontology the reference is tied to.
     */
    public static ReferenceDescriptor getReference (DBConnection con, String name, String ontologyName)
	throws SQLException, WebODEException
    {
	if (name == null || (name = name.trim()).equals(""))
	    throw new WebODEException ("Invalid reference name.");
	if (ontologyName == null || (ontologyName = ontologyName.trim()).equals(""))
	    throw new WebODEException ("Invalid ontology name.");

	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
	    pstmt = con.prepareStatement ("select r.name, r.description from  " +
					  " ode_reference r, ode_ontology o " +
					  " where r.name = ? and r.ontology_id = o.ontology_id and " +
					  " o.name = ?");
	    pstmt.setString (1, name);
	    pstmt.setString (2, ontologyName);
	    rset = pstmt.executeQuery();
	    if (rset.next()) {
		return new ReferenceDescriptor (rset.getString (1),
						rset.getString (2),
						ontologyName);
	    }
	    else
		throw new WebODEException ("No such reference (" + name + ") in ontology " + ontologyName + "." );

	} finally {
	    if (rset != null) rset.close();
	    if (pstmt != null) pstmt.close();
	}
    }


    /**
     * Removes a reference from an ontology.  However, the reference is not deleted.
     */
    public void removeOntologyReference (DBConnection con)
	throws SQLException, WebODEException
    {
	if (ontology == null)
	    throw new WebODEException ("Ontology name cannot be null.");
	if (name == null)
	    throw new WebODEException ("Name cannot be null.");

	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
	    pstmt = con.prepareStatement ("select ontology_id from ode_ontology where name = ?");
	    pstmt.setString (1, ontology = ontology.trim());
	    rset = pstmt.executeQuery();
	    if (!rset.next())
		return;
	    int id = rset.getInt (1);

	    rset.close();
	    pstmt.close();

	    pstmt = con.prepareStatement ("select reference_id from ode_reference where name = ?");
	    pstmt.setString (1, name = name.trim());
	    rset = pstmt.executeQuery();
	    if (!rset.next())
		return;
	    int refId = rset.getInt (1);


	    pstmt = con.prepareStatement ("delete from ode_ontology_has_reference " +
					  " where ontology_id = ? and reference_id = ?");
	    pstmt.setInt    (1, id);
	    pstmt.setInt    (2, refId);
	    pstmt.executeUpdate();
	} finally {
	    if (rset != null) rset.close();
	    if (pstmt != null) pstmt.close();
	}
    }

    /**
     * Updates a given reference.
     *
     * @param con The database connection to be used.
     * @param name The name of the reference to mofify.
     * @param ReferenceDescriptor rd The new data.
     */
    public static void update (DBConnection con, String name, ReferenceDescriptor rd)
	throws WebODEException, SQLException
    {
	if (name == null || (name = name.trim()).equals (""))
	    throw new WebODEException ("Invalid name for the reference.");

	if (rd.name == null || (rd.name = rd.name.trim()).equals(""))
	    throw new WebODEException ("Invalid new name for the reference.");

	if (rd.ontology == null || (rd.ontology = rd.ontology.trim()).equals(""))
	    throw new WebODEException ("Invalid ontology name for the reference.");


	PreparedStatement pstmt = null;
	try {
	    int id = SQLUtil.getOntologyId (con, rd.ontology);

	    pstmt = con.prepareStatement ("update ode_reference set name = ?, description = ? " +
					  " where name = ? and ontology_id = ?");
	    pstmt.setString (1, rd.name);
	    if (rd.description == null || (rd.description = rd.description.trim()).equals(""))
		pstmt.setNull   (2, Types.VARCHAR);
	    else
		pstmt.setString (2, rd.description);
	    pstmt.setString (3, name);
	    pstmt.setInt    (4, id);
	    pstmt.executeUpdate();
	} finally {
	    if (pstmt != null) pstmt.close();
	}
    }
}










