package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;

/**
 * A class to describe terms.
 *
 * @author  Julio César Arpírez Vega
 * @version 1.3
 */
public class Term implements java.io.Serializable, TermTypes
{
  public String ontology;
  public String term;
  public String des;
  public int type;
  private int parentId; // previously transcient

  public Term (String ontology, String name, String des, int type, int parentId)
  {
    this.ontology = ontology;
    this.term     = name;
    this.des      = des;
    this.type     = type;
    this.parentId = parentId;
  }

  public Term (String ontology, String name, String des, int type)
  {
    this (ontology, name, des, type, -1);
  }

  public String getTypeName ()
  {
    return NAMES[type - 1];
  }

  public static int getValueType (String type)
  {
    for (int i = 0; i < ValueTypes.NAMES.length; i++) {
      if (type.equals (ValueTypes.NAMES[i]))
        return i + 1;
    }
    return 1; // It is a XML Schema datatype, since it is not any of the previous ones
    //return -1;
  }

  public Term getParent(DBConnection con) throws SQLException {
    Term parent=null;
    if(this.parentId!=-1) {
      PreparedStatement pstmt = null;
      ResultSet          rset = null;
      try {
        pstmt = con.prepareStatement ("select t.name, t.description, t.type " +
                                      " from ode_terms_glossary t " +
                                      " where " +
                                      "  t.term_id = ?");
        pstmt.setInt (1, this.parentId);
        rset = pstmt.executeQuery();
        if (rset.next())
          parent = new Term (this.ontology, rset.getString (1), rset.getString (2), rset.getInt (3));
      }
      finally {
        try {
          if (rset  != null) rset.close();
          if (pstmt != null) pstmt.close();
        }
        catch (Exception e) {}
      }
    }
    return parent;
  }

  public int store (DBConnection con) throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        term == null || (term = term.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    PreparedStatement pstmt = null;
    try {
      if (type == CLASS_ATTRIBUTE || type == INSTANCE_ATTRIBUTE ||
          type == SYNONYM || type == ABBREVIATION || type == INSTANCE || type == RELATION_INSTANCE) {
        // Check whether there exists a term with the same name and same parent
        if (getTerm (con, ontology, term, parentId) != null)
          throw new WebODEException ("A term with name " + term + " already exists as " +
                                     "an abbreviation, synonym, class attribute or instance attribute " +
                                     "for this term or it is already an instance in the active instance set.");
      }
      else if (getTerm (con, ontology, term) != null)
        throw new WebODEException ("A term with name " + term + " already exists.");

      int termId = SQLUtil.nextVal (con, "ode_sequence_term_id");
      int ontologyId = SQLUtil.getOntologyId (con, ontology);

      pstmt = con.prepareStatement ("insert into ode_terms_glossary "+
                                    "(term_id, ontology_id, name, description, type " +
                                    (type == CLASS_ATTRIBUTE || type == INSTANCE_ATTRIBUTE ||
                                    type == SYNONYM || type == ABBREVIATION || type == INSTANCE ||
                                    type == RELATION_INSTANCE ?
                                    ", parent_id " : "") +
                                    ") values (?, ?, ?, ?, ?" +
                                    (type == CLASS_ATTRIBUTE || type == INSTANCE_ATTRIBUTE ||
                                    type == SYNONYM || type == ABBREVIATION || type == INSTANCE
                                    || type == RELATION_INSTANCE ?
                                    ", ?" : "")+ ")");
      pstmt.setInt    (1, termId);
      pstmt.setInt    (2, ontologyId);
      pstmt.setString (3, term);
      if (des == null || (des = des.trim()).equals(""))
        pstmt.setNull (4, Types.VARCHAR);
      else
        pstmt.setString (4, des);
      pstmt.setInt    (5, type);
      if (type == CLASS_ATTRIBUTE || type == INSTANCE_ATTRIBUTE ||
          type == SYNONYM || type == ABBREVIATION || type == INSTANCE || type == RELATION_INSTANCE)
        pstmt.setInt  (6, parentId);

      pstmt.executeUpdate();

      return termId;
    } finally {
      try {
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  private static String _toList (int[] ai)
  {
    StringBuffer strBuffer = new StringBuffer();
    strBuffer.append ("" + ai[0]);
    for (int i = 1; i < ai.length; i++)
      strBuffer.append ("," + ai[i]);

    return strBuffer.toString();
  }

  /**
   * Get terms.
   *
   * @param ontology The ontology.
   * @return null if no one is found.
   */
  public static Term[] getTerms (DBConnection con, String ontology, int[] types)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t.name, t.description, t.type " +
                                    " from ode_terms_glossary t, ode_ontology o " +
                                    " where t.ontology_id = o.ontology_id and o.name = ? " +
                                    (types == null ? "" : "and t.type in (" + _toList (types) + ")") +
                                    " order by t.name");

      pstmt.setString (1, ontology);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector(200);
        do {
          v.addElement (new Term (ontology, rset.getString (1),
                                  rset.getString (2), rset.getInt (3)));
        } while (rset.next());

        Term[] at = new Term[v.size()];
        v.copyInto (at);

        return at;
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
   * Get Terms whose parent is the one specified.
   *
   * @param ontology The ontology.
   * @return null if no one is found.
   */
  public static Term[] getTerms (DBConnection con, String ontology, String name)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t.name, t.description, t.type " +
                                    " from ode_terms_glossary t, ode_ontology o, ode_terms_glossary t1 " +
                                    " where t.ontology_id = o.ontology_id and o.name = ? " +
                                    " and t.parent_id = t1.term_id and t1.name = ? " +
                                    " order by t.name");

      pstmt.setString (1, ontology);
      pstmt.setString (2, name);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector(200);
        do {
          v.addElement (new Term (ontology, rset.getString (1),
                                  rset.getString (2), rset.getInt (3)));
        } while (rset.next());

        Term[] at = new Term[v.size()];
        v.copyInto (at);

        return at;
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
   * Get terms.
   *
   * @param ontology The ontology.
   * @return null if no one is found.
   */
  public static Term getTerm (DBConnection con, String ontology, String name)
      throws SQLException, WebODEException
  {
    return getTerm (con, ontology, name, -1);
  }

  public static Term getTerm (DBConnection con, String ontology, String name, String parent)
      throws SQLException, WebODEException
  {
    int foo = SQLUtil.getTermId (con, ontology, parent, TermTypes.INSTANCE_SET);
    return getTerm (con, ontology, name, foo);
  }


  /**
   * Get terms.
   *
   * @param ontology The ontology.
   * @param parentId The parent's id.
   * @return null if no one is found.
   */
  public static Term getTerm (DBConnection con, String ontology, String name, int parentId)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        name == null || (name = name.trim()).equals(""))
      throw new WebODEException ("Invalid parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t.name, t.description, t.type " +
                                    " from ode_terms_glossary t, ode_ontology o " +
                                    " where t.ontology_id = o.ontology_id and o.name = ? " +
                                    "  and t.name = ? and t.parent_id " + (parentId >= 0 ? "= ?" : "is null"));
      pstmt.setString (1, ontology);
      pstmt.setString (2, name);
      if (parentId >= 0)
        pstmt.setInt    (3, parentId);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        return new Term (ontology, rset.getString (1),
                         rset.getString (2), rset.getInt (3));
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


  public static void removeTerm (DBConnection con, String ontology, String term)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        term == null || (term = term.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      int termId = SQLUtil.getTermId (con, ontology, term);
      pstmt = con.prepareStatement ("delete from ode_terms_glossary where term_id = ?");
      pstmt.setInt (1, termId);
      pstmt.executeUpdate();
    } finally {
      try {
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  public static void removeTerm (DBConnection con, String ontology, String term, String parent)
      throws SQLException, WebODEException
  {
    removeTerm (con, ontology, term, parent, null);
  }

  public static void removeTerm (DBConnection con, String ontology, String term, String parent, String relatedTerm)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        term == null || (term = term.trim()).equals("") ||
        parent == null || (parent = parent.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      int parentId = SQLUtil.getTermId (con, ontology, parent, TermTypes.CONCEPT);
      if (relatedTerm != null && relatedTerm.length() > 0) {
        int foo = parentId;
        parentId = SQLUtil.getTermId (con, ontology, relatedTerm, TermTypes.CLASS_ATTRIBUTE, foo);
        if (parentId < 0)
          parentId = SQLUtil.getTermId (con, ontology, relatedTerm, TermTypes.INSTANCE_ATTRIBUTE, foo);
      }

      int termId   = -1;
      if (parentId < 0) {
        // Instance
        parentId = SQLUtil.getTermId (con, ontology, parent, TermTypes.INSTANCE_SET);
        termId   = SQLUtil.getTermId (con, ontology, term, TermTypes.INSTANCE, parentId);
        if(termId < 0)
          termId   = SQLUtil.getTermId (con, ontology, term, TermTypes.RELATION_INSTANCE, parentId);
      }
      else {
        if (relatedTerm == null) {
          termId = SQLUtil.getTermId (con, ontology, term, TermTypes.CLASS_ATTRIBUTE, parentId);
          // If it is not a class attribute, try an instance attribute.
          if (termId < 0)
            termId   = SQLUtil.getTermId (con, ontology, term, TermTypes.INSTANCE_ATTRIBUTE, parentId);
        }
        // If not, a synonym
        if (termId < 0)
          termId   = SQLUtil.getTermId (con, ontology, term, TermTypes.SYNONYM, parentId);
        // Otherwise, an abbreviation
        if (termId < 0)
          termId   = SQLUtil.getTermId (con, ontology, term, TermTypes.ABBREVIATION, parentId);
      }

      pstmt = con.prepareStatement ("delete from ode_terms_glossary where term_id = ? and parent_id = ?");
      pstmt.setInt (1, termId);
      pstmt.setInt (2, parentId);
      pstmt.executeUpdate();
    } finally {
      try {
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }


  public int update (DBConnection con, String originalName)
      throws WebODEException, SQLException
  {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        term == null || (term = term.trim()).equals("") ||
        originalName == null || (originalName = originalName.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    PreparedStatement pstmt = null;
    try {
      int termId = SQLUtil.getTermId (con, ontology, originalName, type);

      pstmt = con.prepareStatement ("update ode_terms_glossary " +
                                    "set name = ?, description = ? where term_id = ?");
      pstmt.setString (1, term);
      if (des == null || (des = des.trim()).equals(""))
        pstmt.setNull (2, Types.VARCHAR);
      else
        pstmt.setString (2, des);
      pstmt.setInt    (3, termId);

      pstmt.executeUpdate();

      return termId;
    } finally {
      try {
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  public int update (DBConnection con, String originalName, int newType)
      throws WebODEException, SQLException
  {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        term == null || (term = term.trim()).equals("") ||
        originalName == null || (originalName = originalName.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    PreparedStatement pstmt = null;
    try {
      int termId = SQLUtil.getTermId (con, ontology, originalName, type);

      pstmt = con.prepareStatement ("update ode_terms_glossary " +
                                    "set name = ?, description = ?, type = ? where term_id = ?");
      pstmt.setString (1, term);
      if (des == null || (des = des.trim()).equals(""))
        pstmt.setNull (2, Types.VARCHAR);
      else
        pstmt.setString (2, des);
      pstmt.setInt    (3, newType);
      pstmt.setInt    (4, termId);

      pstmt.executeUpdate();

      return termId;
    } finally {
      try {
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }


  public int update (DBConnection con, String originalName, String parent)
      throws WebODEException, SQLException
  {
    return update (con, originalName, parent, null);
  }


  public int update (DBConnection con, String originalName, String parent, String relatedTerm)
      throws WebODEException, SQLException
  {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        term == null || (term = term.trim()).equals("") ||
        parent == null || (parent = parent.trim()).equals("") ||
        originalName == null || (originalName = originalName.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    PreparedStatement pstmt = null;
    try {
      int parentId = SQLUtil.getTermId (con, ontology, parent, TermTypes.CONCEPT);
      if (relatedTerm != null) {
        int foo = parentId;
        parentId = SQLUtil.getTermId (con, ontology, relatedTerm, TermTypes.CLASS_ATTRIBUTE, foo);
        if (parentId < 0)
          parentId = SQLUtil.getTermId (con, ontology, relatedTerm, TermTypes.INSTANCE_ATTRIBUTE, foo);
      }
      int termId = -1;
      if (relatedTerm == null) {
        termId = SQLUtil.getTermId (con, ontology, originalName, TermTypes.CLASS_ATTRIBUTE, parentId);
        if (termId < 0)
          termId = SQLUtil.getTermId (con, ontology, originalName, TermTypes.INSTANCE_ATTRIBUTE, parentId);
      }
      if (termId < 0)
        termId = SQLUtil.getTermId (con, ontology, originalName, TermTypes.SYNONYM, parentId);
      if (termId < 0)
        termId = SQLUtil.getTermId (con, ontology, originalName, TermTypes.ABBREVIATION, parentId);
      if (termId < 0)
        termId = SQLUtil.getTermId (con, ontology, originalName, TermTypes.INSTANCE, parentId);
      if (termId < 0)
        termId = SQLUtil.getTermId (con, ontology, originalName, TermTypes.RELATION_INSTANCE, parentId);


      pstmt = con.prepareStatement ("update ode_terms_glossary " +
                                    "set name = ?, description = ? where term_id = ?");
      pstmt.setString (1, term);
      if (des == null || (des = des.trim()).equals(""))
        pstmt.setNull (2, Types.VARCHAR);
      else
        pstmt.setString (2, des);
      pstmt.setInt    (3, termId);

      pstmt.executeUpdate();

      return termId;
    } finally {
      try {
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }
}
