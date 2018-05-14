package es.upm.fi.dia.ontology.webode.service.irs;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.others.*;
import es.upm.fi.dia.ontology.webode.service.*;

public class IntermediateRepresentation implements java.io.Serializable
{
    /** Terms glossary. */
    public static final int TERMS_GLOSSARY = 0;
    /** Concept classification tree. */
    public static final int CONCEPT_CLASSIFICATION_TREE = 1;
    /** Binary relations */
    public static final int BINARY_RELATIONS = 2;

    /** Intermediate representations names. */
    public static final String[] NAMES = {
	"Terms Glossary", "Concept Classification Tree", "Binary Relationships" };

    /** Related views. */
    public static final String[] VIEWS = {
	"ode_view_terms_glossary",
	"ode_view_concept_classification_tree",
	"ode_view_binary_relations" };

    /** Additional clauses */
    public static final String[] CLAUSES = {
	"order by \"Name\"",
	"order by \"Origin\", \"Destination\", \"Relation\"",
	"order by \"Origin\", \"Destination\", \"Relation\"" };
    
    /** Requieres instance set. */
    public static final boolean[] REQUIRES = {
	false, false, false, true };
    
    // Instance variables
    private String[] headers; // Headers for the result.
    private ArrayList alResults;

    public IntermediateRepresentation (String[] headers,
				       ArrayList alResults)
    {
	this.headers  = headers;
	this.alResults = alResults;
    }

    public Iterator getRowIterator ()
    {
	return alResults == null ? null : alResults.iterator();
    }    

    public String[] getHeader ()
    {
	return headers;
    }

    public static String getIRName (int ir)
    {
	return NAMES[ir];
    }

    /**
     * Gets the given intermediate representation for the ontology.
     *
     * @param con      The database connection.
     * @param ontology The ontology.
     * @param ir       The intermediate representation.  One of the constantes
     *                 defined above.
     */
    public static IntermediateRepresentation getIR (DBConnection con, String ontology, int ir)
	throws SQLException, WebODEException
    {
	return getIR (con, ontology, null, ir);
    }

    /**
     * Gets the given intermediate representation for the ontology.
     *
     * @param con      The database connection.
     * @param ontology The ontology.
     * @param ir       The intermediate representation.  One of the constantes
     *                 defined above.
     * @param instanceSet The instance set being used.
     */
    public static IntermediateRepresentation getIR (DBConnection con, String ontology, String instanceSet, int ir)
	throws SQLException, WebODEException
    {
	if (ontology == null || (ontology = ontology.trim()).equals("") ||
	    ir < 0 || ir >= VIEWS.length)
	    throw new WebODEException ("Invalid paramaters.");

	if (REQUIRES[ir] && (instanceSet == null || (instanceSet = instanceSet.trim()).equals("")))
	    throw new WebODEException ("Instance set required.");
	
	PreparedStatement pstmt = null;
	ResultSet          rset = null;
	ResultSetMetaData  rsmd = null;
	try {
	    pstmt = con.prepareStatement ("select * from " + VIEWS[ir] + 
					  " where \"Ontology Name\" = ? " +
					  (REQUIRES[ir] ? " and \"Instance Set\" = ? " : "") + 
					  (CLAUSES[ir] == null ? "" : CLAUSES[ir]));
	    pstmt.setString (1, ontology);
	    if (REQUIRES[ir])
		pstmt.setString (2, instanceSet);
	    
	    rset = pstmt.executeQuery ();
	    rsmd = rset.getMetaData();
	    int cols = rsmd.getColumnCount ();
	    String[] headers = new String[cols - 1];
	    int oname = -1;
	    // Get column names
	    for (int i = 0, j = 0; i < cols; i++) {
		String header = rsmd.getColumnName(i + 1);
		if (!header.equals ("Ontology Name")) {
		    headers[j++] = header;
		}
		else
		    oname = i;
	    }

	    if (rset.next()) {
		ArrayList al = new ArrayList (200);
		
		// Now the results.
		do {
		    Object[] row = new Object[cols - 1];
		    for (int i = 0, j = 0; i < cols; i++) {
			String foo = rset.getString (i + 1);
			if (i != oname)
			    row[j++] = foo;
		    }
		    
		    al.add (row);
		} while (rset.next());
		return new IntermediateRepresentation (headers, al);
	    } 
	    else {
		return new IntermediateRepresentation (headers, null);
	    }
	} finally {
	    try {
		if (rset != null) rset.close();
		if (pstmt != null) pstmt.close();
	    } catch (Exception e) {
	    }
	}
    }
}
