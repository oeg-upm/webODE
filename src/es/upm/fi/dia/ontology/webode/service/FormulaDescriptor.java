package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.webode.service.util.*;

/**
 * The descriptor for reasoning items.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class FormulaDescriptor implements java.io.Serializable, TermTypes
{
    /**
     * The ontology the formula is related to.
     */
    public String ontology;

    /**
     * The name of the formula.
     */
    public String name;

    /**
     * The formula description.
     */
    public String description;

    /**
     * The reasoning element expresion.
     */
    public String expression;

    /**
     * The prolog expresion.
     */
    public String prolog_expression;

    /**
     * The type of the reasoning item.
     *
     * @see es.upm.fi.dia.ontology.webode.service.TermTypes#AXIOM
     * @see es.upm.fi.dia.ontology.webode.service.TermTypes#RULE
     * @see es.upm.fi.dia.ontology.webode.service.TermTypes#PROCEDURE
     */
    public int type;


    public FormulaDescriptor (String ontology, String name, String description, String expression, int type)
    {
    this.name   = name;
    this.description = description;
    this.ontology = ontology;
    this.expression = expression;
    this.prolog_expression = "";
    this.type = type;

    }



    public FormulaDescriptor (String ontology, String name, String description, String expression,String prolog_expression, int type)
    {
    this.name   = name;
    this.description = description;
    this.ontology = ontology;
    this.expression = expression;
    this.prolog_expression = prolog_expression;
    this.type = type;

    }

    public static int getNumericType (String type)
    {
    if (type.equals ("Axiom"))
        return AXIOM;
    if (type.equals ("Rule"))
        return RULE;
    if (type.equals ("Procedure"))
        return PROCEDURE;
    return -1;
    }

    public void store (DBConnection con) throws SQLException, WebODEException
    {
    PreparedStatement pstmt  = null;


    try {
        if (ontology == null || (ontology = ontology.trim()).equals(""))
        throw new WebODEException ("The ontology name is null.");

        String foo1, foo2 = null;
        if (name == null || (foo1 = name.trim()).equals (""))
        throw new WebODEException ("Name is null or empty.");

        if (expression == null || (expression = expression.trim()).equals (""))
        throw new WebODEException ("Expression is null or empty.");

        // Insert a reasoning element
        con.setAutoCommit (false);
        int termId = new Term (ontology, name, description, type).store (con);

        pstmt = con.prepareStatement ("insert into ode_reasoning (term_id, expression, prologexpression) " +
                      " values (?, ?, ?)");
        pstmt.setInt    (1, termId);
        pstmt.setString (2, expression);
        System.out.println(prolog_expression+": store1 formulaDescriptor");
        pstmt.setString (3,prolog_expression);
        System.out.println(prolog_expression+": store2 formulaDescriptor");
        pstmt.executeUpdate();
        System.out.println(prolog_expression+": store3 formulaDescriptor");
        con.commit();
        System.out.println(prolog_expression+": store4 formulaDescriptor");
    } catch (SQLException sqle) {
        con.rollback();

        throw sqle;
    } finally {
        if (pstmt != null) pstmt.close();
        con.setAutoCommit (true);
    }
    }

    /**
     * Updates a formula.
     */
    public void update (DBConnection con, String originalName) throws WebODEException, SQLException
    {
    PreparedStatement pstmt  = null;

    System.out.println("NOMBRE NUEVO:"+originalName);
    try {
        if (ontology == null || (ontology = ontology.trim()).equals(""))
        throw new WebODEException ("The ontology name is null.");

        if (originalName == null || (originalName = originalName.trim()).equals(""))
        throw new WebODEException ("The ontology name is null.");

        String foo1, foo2 = null;
        if (name == null || (foo1 = name.trim()).equals (""))
        throw new WebODEException ("Name is null or empty.");

        if (expression == null || (expression = expression.trim()).equals (""))
        throw new WebODEException ("Expression is null or empty.");

        // Insert a reasoning element
        con.setAutoCommit (false);
        int termId = new Term (ontology, name, description, type).update (con, originalName, type);

        pstmt = con.prepareStatement ("update ode_reasoning set expression = ? , prologexpression = ?" +
                      " where  term_id = ? ");
        pstmt.setString (1, expression);
        pstmt.setString (2, prolog_expression);
        pstmt.setInt    (3, termId);

        pstmt.executeUpdate();
        con.commit();
    } catch (SQLException sqle) {
        con.rollback();

        throw sqle;
    } finally {
        if (pstmt != null) pstmt.close();
        con.setAutoCommit (true);
    }
    }


    /**
     * Retrieves a formula.
     */
    public static FormulaDescriptor getReasoningElement (DBConnection con, String ontology, String name)
    throws WebODEException, SQLException
    {
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
        if (ontology == null || (ontology = ontology.trim()).equals(""))
        throw new WebODEException ("The ontology name is null.");

        if (name == null || (name = name.trim()).equals(""))
        throw new WebODEException ("The name is null.");

        // Get specific information
        pstmt = con.prepareStatement ("select t.description, re.expression, re.prologexpression, t.type " +
                      " from ode_terms_glossary t, ode_ontology o, ode_reasoning re " +
                      " where t.ontology_id = o.ontology_id and o.name = ? " +
                      "  and t.name = ? and t.term_id = re.term_id");
        pstmt.setString (1, ontology);
        pstmt.setString (2, name);
        rset = pstmt.executeQuery();

        if (rset.next()) {

        return new FormulaDescriptor (ontology, name, rset.getString (1), rset.getString (2), rset.getString (3),
                          rset.getInt (4));
        }
        else return null;
    } finally {
        try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
    }

    /**
     * Retrieves all available formulas in the ontology.
     */
    public static FormulaDescriptor[] getReasoningElements (DBConnection con, String ontology)
    throws WebODEException, SQLException
    {
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
        if (ontology == null || (ontology = ontology.trim()).equals(""))
        throw new WebODEException ("The ontology name is null.");

        // Get specific information
        pstmt = con.prepareStatement ("select t.name, t.description, re.expression,re.prologexpression, t.type " +
                      " from ode_terms_glossary t, ode_ontology o, ode_reasoning re " +
                      " where t.ontology_id = o.ontology_id and o.name = ? " +
                      " and t.term_id = re.term_id");
        pstmt.setString (1, ontology);
        rset = pstmt.executeQuery();
        if (rset.next()) {
        Vector v = new Vector (20);
        do {
            v.addElement (new FormulaDescriptor (ontology, rset.getString (1),
                             rset.getString (2), rset.getString (3), rset.getString(4),
                             rset.getInt (5)));
        } while (rset.next());
        FormulaDescriptor[] afd = new FormulaDescriptor[v.size()];
        v.copyInto (afd);

        return afd;
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

    /**
     * Retrieves all available formulas in the ontology tied to a term.
     */
    public static FormulaDescriptor[] getReasoningElements (DBConnection con, String ontology, String name)
    throws WebODEException, SQLException
    {
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
        if (ontology == null || (ontology = ontology.trim()).equals(""))
        throw new WebODEException ("The ontology name is null.");

        if (name == null || (name = name.trim()).equals(""))
        throw new WebODEException ("The term's name is null.");

        // Get specific information
        pstmt = con.prepareStatement ("select t.name, t.description, re.expression,re.prologexpression, t.type " +
                      " from ode_terms_glossary t, ode_ontology o, ode_reasoning re, " +
                      "   ode_has_reasoning ohr, ode_terms_glossary t1 " +
                      " where t.ontology_id = o.ontology_id and o.name = ? " +
                      " and t.term_id = re.term_id and re.term_id = ohr.formula_id and " +
                      " ohr.term_id = t1.term_id and t1.name = ?");
        pstmt.setString (1, ontology);
        pstmt.setString (2, name);
        rset = pstmt.executeQuery();
        if (rset.next()) {
        Vector v = new Vector (20);
        do {
            v.addElement (new FormulaDescriptor (ontology, rset.getString (1),
                             rset.getString (2), rset.getString (3), rset.getString (4),
                             rset.getInt (5)));
        } while (rset.next());
        FormulaDescriptor[] afd = new FormulaDescriptor[v.size()];
        v.copyInto (afd);

        return afd;
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


    /**
     * Retrieves all available formulas in the ontology tied to a term.
     */
    public static FormulaDescriptor[] getReasoningElements (DBConnection con, String ontology, String name, String parent)
    throws WebODEException, SQLException
    {
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
        if (ontology == null || (ontology = ontology.trim()).equals(""))
        throw new WebODEException ("The ontology name is null.");

        if (name == null || (name = name.trim()).equals(""))
        throw new WebODEException ("The term's name is null.");

        if (parent == null || (parent = parent.trim()).equals(""))
        throw new WebODEException ("The parent name is null.");

        // Get specific information
        pstmt = con.prepareStatement ("select t.name, t.description, re.expression,re.prologexpression, t.type " +
                      " from ode_terms_glossary t, ode_ontology o, ode_reasoning re, " +
                      "   ode_has_reasoning ohr, ode_terms_glossary t1, " +
                      "   ode_terms_glossary t2 " +
                      " where t.ontology_id = o.ontology_id and o.name = ? " +
                      " and t.term_id = re.term_id and re.term_id = ohr.formula_id and " +
                      " ohr.term_id = t1.term_id and t1.name = ? and " +
                      " t1.parent_id = t2.term_id and t2.name = ?");
        pstmt.setString (1, ontology);
        pstmt.setString (2, name);
        pstmt.setString (3, parent);
        rset = pstmt.executeQuery();
        if (rset.next()) {
        Vector v = new Vector (20);
        do {
            v.addElement (new FormulaDescriptor (ontology, rset.getString (1),
                             rset.getString (2), rset.getString (3), rset.getString (4),
                             rset.getInt (5)));
        } while (rset.next());
        FormulaDescriptor[] afd = new FormulaDescriptor[v.size()];
        v.copyInto (afd);

        return afd;
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


    /**
     * Ties a formula to a term.
     *
     */
    public static void relateFormulaToTerm (DBConnection con, String ontology, String formula, String term)
    throws SQLException, WebODEException
    {
    if (formula == null || (formula = formula.trim()).equals ("") ||
        term == null || (term = term.trim()).equals ("") ||
        ontology == null || (ontology = ontology.trim()).equals(""))
        throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    try {
        int termId = SQLUtil.getTermId (con, ontology, term);
        int forId  = SQLUtil.getTermId (con, ontology, formula);

        pstmt = con.prepareStatement ("insert into ode_has_reasoning (term_id, formula_id) " +
                      " values (?, ?)");
        pstmt.setInt (1, termId);
        pstmt.setInt (2, forId);
        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) pstmt.close();
    }
    }

    /**
     * Ties a formula to a term.
     *
     */
    public static void relateFormulaToTerm (DBConnection con, String ontology,
                        String formula, String term, String parent)
    throws SQLException, WebODEException
    {
    if (formula == null || (formula = formula.trim()).equals ("") ||
        term == null || (term = term.trim()).equals ("") ||
        ontology == null || (ontology = ontology.trim()).equals("") ||
        parent == null || (parent = parent.trim()).equals(""))
        throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    try {
        int parentId = SQLUtil.getTermId (con, ontology, parent);
        if (parentId < 0)
        parentId = SQLUtil.getTermId (con, ontology, parent);

        int termId = SQLUtil.getTermId (con, ontology, term, TermTypes.CLASS_ATTRIBUTE, parentId);
        if (termId < 0)
        termId = SQLUtil.getTermId (con, ontology, term, TermTypes.INSTANCE_ATTRIBUTE, parentId);
        int forId  = SQLUtil.getTermId (con, ontology, formula);

        pstmt = con.prepareStatement ("insert into ode_has_reasoning (term_id, formula_id) " +
                      " values (?, ?)");
        pstmt.setInt (1, termId);
        pstmt.setInt (2, forId);
        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) pstmt.close();
    }
    }

    /**
     * Unties a formula from a term.
     *
     */
    public static void unrelateFormulaFromTerm (DBConnection con, String ontology, String formula, String term)
    throws SQLException, WebODEException
    {
    if (formula == null || (formula = formula.trim()).equals ("") ||
        term == null || (term = term.trim()).equals ("") ||
        ontology == null || (ontology = ontology.trim()).equals(""))
        throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    try {
        int termId = SQLUtil.getTermId (con, ontology, term);
        int forId  = SQLUtil.getTermId (con, ontology, formula);

        pstmt = con.prepareStatement ("delete from ode_has_reasoning where " +
                      "       term_id = ? and formula_id = ?");

        pstmt.setInt (1, termId);
        pstmt.setInt (2, forId);
        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) pstmt.close();
    }
    }

    /**
     * Unties a formula from a term.
     *
     */
    public static void unrelateFormulaFromTerm (DBConnection con, String ontology,
                        String formula, String term, String parent)
    throws SQLException, WebODEException
    {
    if (formula == null || (formula = formula.trim()).equals ("") ||
        term == null || (term = term.trim()).equals ("") ||
        ontology == null || (ontology = ontology.trim()).equals(""))
        throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    try {
        int parentId = SQLUtil.getTermId (con, ontology, parent);
        if (parentId < 0)
        parentId = SQLUtil.getTermId (con, ontology, parent);

        int termId = SQLUtil.getTermId (con, ontology, term, TermTypes.CLASS_ATTRIBUTE, parentId);
        if (termId < 0)
        termId = SQLUtil.getTermId (con, ontology, term, TermTypes.INSTANCE_ATTRIBUTE, parentId);
        int forId  = SQLUtil.getTermId (con, ontology, formula);

        pstmt = con.prepareStatement ("delete from ode_has_reasoning where " +
                      "       term_id = ? and formula_id = ?");

        pstmt.setInt (1, termId);
        pstmt.setInt (2, forId);
        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) pstmt.close();
    }
    }

}
