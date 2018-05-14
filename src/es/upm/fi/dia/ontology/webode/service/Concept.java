package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;
import es.upm.fi.dia.ontology.webode.service.util.SQLUtil;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;

/**
 * A class to describe terms.
 *
 * @author  Angel López Cima
 * @version 1.3
 */
public class Concept extends Term implements java.io.Serializable, TermTypes
{
    public Concept (String ontology, String name, String des, int parentId)
    {
      super(ontology, name, des, CONCEPT, parentId);
    }

    public Concept (String ontology, String name, String des)
    {
	this (ontology, name, des, -1);
    }

  /**
   * This method returns all the root concepts fron an ontology
   *
   * @param con Conection to the datebase
   * @param ontology The ontology in which the concepts are defined
   * @param imported Specified if you the imported concepts are included.
   * @return Return an array of root concepts of an ontology. It returns null when none concepts has been founded.
   */
  public static Concept[] getRootConcepts (DBConnection con, String ontology, boolean imported)
        throws WebODEException, SQLException {
    Vector v = new Vector (200);

    PreparedStatement pstmt = null;
    ResultSet          rset = null;

    try {
      if(con.dbManager==DBConnection.ORACLE) {
        pstmt = con.prepareStatement("select name,description " +
                                     "from ode_terms_glossary " +
                                     "where " +
                                     "term_id in ( " +
                                     "select ode_terms_glossary.term_id " +
                                     "from ode_ontology, ode_terms_glossary " +
                                     "where " +
                                     "  ode_ontology.name = ? and " +
                                     "  ode_ontology.ontology_id = ode_terms_glossary.ontology_id and " +
                                     "  ode_terms_glossary.type= " + TermTypes.CONCEPT + " " +
                                     "minus " +
                                     "( " +
                                     "select ode_terms_glossary.term_id " +
                                     "from ode_ontology, ode_terms_glossary, ode_relation " +
                                     "where " +
                                     "  ode_ontology.name = ? and " +
                                     "  ode_ontology.ontology_id = ode_terms_glossary.ontology_id and " +
                                     "  ode_terms_glossary.type =  " + TermTypes.CONCEPT + " and " +
                                     "  ode_terms_glossary.term_id =ode_relation.origin_term_id and " +
                                     "  ode_relation.name_id = " + TermRelation.SUBCLASS_OF_I + " " +
                                     "union " +
                                     "select ode_group.term_id " +
                                     "from ode_group, ode_terms_glossary, ode_ontology, ode_relation " +
                                     "where " +
                                     "  ode_ontology.name = ? and " +
                                     "  ode_ontology.ontology_id = ode_terms_glossary.ontology_id and " +
                                     "  ode_terms_glossary.term_id=ode_group.group_id and " +
                                     "  ode_terms_glossary.term_id = ode_relation.origin_term_id and " +
                                     "  (ode_relation.name_id = " + TermRelation.EXHAUSTIVE_I + " OR " +
                                     "   ode_relation.name_id = " + TermRelation.DISJOINT_I + ") " +
                                     (!imported?
                                     "union " +
                                     "select ode_imported_term.term_id " +
                                     "from ode_ontology, ode_imported_term " +
                                     "where " +
                                     "  ode_ontology.name = ? and " +
                                     "  ode_ontology.ontology_id = ode_imported_term.ontology_id":"")+"))");
        pstmt.setString(1,ontology);
        pstmt.setString(2,ontology);
        pstmt.setString(3,ontology);
        if(!imported) pstmt.setString(4,ontology);
      }
      else {
        pstmt = con.prepareStatement("select concept.name, concept.description " +
                                     "from " +
                                     "  ode_ontology onto, ode_terms_glossary concept " +
                                     "left join " +
                                     "  ode_relation r_subclass " +
                                     "  on " +
                                     "    concept.term_id = r_subclass.origin_term_id and " +
                                     "    r_subclass.name_id = " + TermRelation.SUBCLASS_OF_I + " " +
                                     "left join " +
                                     "  ode_group `group` " +
                                     "  on " +
                                     "    concept.term_id = `group`.term_id " +
                                     "left join " +
                                     "  ode_relation r_group " +
                                     "  on " +
                                     "    `group`.group_id = r_group.origin_term_id and " +
                                     "    (r_group.name_id = " + TermRelation.EXHAUSTIVE_I + " or r_group.name_id = " + TermRelation.DISJOINT_I + ") " +
                                     ((!imported)?
                                     "left join " +
                                     "  ode_imported_term imported " +
                                     "  on " +
                                     "    concept.term_id = imported.term_id ":"") +
                                     "where  " +
                                     "  onto.name = ? and  " +
                                     "  onto.ontology_id = concept.ontology_id and  " +
                                     "  concept.type = " + TermTypes.CONCEPT + " and " +
                                     "  r_subclass.origin_term_id IS NULL and " +
                                     "  `group`.group_id is NULL " +
                                     ((!imported)?"and imported.term_id is NULL":""));
        pstmt.setString(1,ontology);
      }

      rset = pstmt.executeQuery();
      while(rset.next())
        v.addElement(new Concept(ontology,rset.getString(1),rset.getString(2)));
      rset.close();
      pstmt.close();

      if (v.size()>0) {
      	Concept[] concepts = new Concept[v.size()];
      	v.copyInto (concepts);
      	return concepts;
      }
      else
        return null;
    }
    finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
      }
      catch (Exception e) {
      }
    }
  }

  /**
   * This method returns all the child concepts from a given concept
   *
   * @param con Conection to the datebase
   * @param ontology The ontology in which the concepts are defined
   * @param concept The parent concept of an hierarchy
   * @return Return an array of child concepts from the specified concept of an ontology. It returns null when none concepts has been founded.
   */
  public static Concept[] getChildConcepts (DBConnection con, String ontology, String concept)
        throws WebODEException, SQLException {
    Vector v = new Vector (200);

    PreparedStatement pstmt = null;
    ResultSet          rset = null;

    try {
      pstmt = con.prepareStatement("select child.name, child.description " +
                                   "from ode_ontology, ode_terms_glossary parent, ode_terms_glossary child, ode_relation " +
                                   "where " +
                                   "  ode_ontology.name = ? and " +
                                   "  ode_ontology.ontology_id = parent.ontology_id and " +
                                   "  parent.name = ? and " +
                                   "  parent.type = " + TermTypes.CONCEPT + " and " +
                                   "  ode_relation.destination_term_id=parent.term_id and " +
                                   "  ode_relation.origin_term_id=child.term_id and " +
                                   "  ode_relation.name_id = " + TermRelation.SUBCLASS_OF_I);
      pstmt.setString(1,ontology);
      pstmt.setString(2,concept);
      rset = pstmt.executeQuery();
      while(rset.next())
        v.addElement(new Concept(ontology,rset.getString(1),rset.getString(2)));
      rset.close();
      pstmt.close();


      pstmt = con.prepareStatement("select child.name, child.description " +
                                   "from ode_ontology, ode_terms_glossary parent, ode_terms_glossary child, " +
                                   "     ode_relation, ode_group, ode_terms_glossary relation " +
                                   "where " +
                                   "  ode_ontology.name = ? and " +
                                   "  ode_ontology.ontology_id = parent.ontology_id and " +
                                   "  parent.name = ? and " +
                                   "  parent.type = " + TermTypes.CONCEPT + " and " +
                                   "  parent.term_id = ode_relation.destination_term_id and " +
                                   "  ode_relation.origin_term_id = relation.term_id and " +
                                   "  ode_group.group_id=relation.term_id and " +
                                   "  ode_group.term_id=child.term_id and " +
                                   "  ode_relation.name_id in (" + TermRelation.EXHAUSTIVE_I + ", " +
                                                                   TermRelation.DISJOINT_I + ")");
      pstmt.setString(1,ontology);
      pstmt.setString(2,concept);
      rset = pstmt.executeQuery();
      while(rset.next())
        v.addElement(new Concept(ontology,rset.getString(1),rset.getString(2)));
      rset.close();
      pstmt.close();

      if (v.size()>0) {
      	Concept[] concepts = new Concept[v.size()];
      	v.copyInto (concepts);
      	return concepts;
      }
      else
        return null;
    }
    finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
      }
      catch (Exception e) {
      }
    }
  }

  /**
   * This method returns all parents concepts from a given concept
   *
   * @param con Conection to the datebase
   * @param ontology The ontology in which the concepts are defined
   * @param concept The child concept of an hierarchy
   * @return Return an array of parent concepts from the specified concept of an ontology. It returns null when concept is a root concept of the ontology.
   */
  public static Concept[] getParentConcepts(DBConnection con, String ontology, String concept)
        throws WebODEException, SQLException  {
    Vector v = new Vector (200);

    PreparedStatement pstmt = null;
    ResultSet          rset = null;

    try {
      pstmt = con.prepareStatement("select parent.name, parent.description " +
                                   "from ode_ontology, ode_terms_glossary parent, ode_terms_glossary child, ode_relation " +
                                   "where " +
                                   "  ode_ontology.name = ? and " +
                                   "  ode_ontology.ontology_id = child.ontology_id and " +
                                   "  child.type = " + TermTypes.CONCEPT + " and " +
                                   "  child.name = ? and " +
                                   "  ode_relation.destination_term_id=parent.term_id and " +
                                   "  ode_relation.origin_term_id=child.term_id and " +
                                   "  ode_relation.name_id = " + TermRelation.SUBCLASS_OF_I);
      pstmt.setString(1,ontology);
      pstmt.setString(2,concept);
      rset = pstmt.executeQuery();
      while(rset.next())
        v.addElement(new Concept(ontology,rset.getString(1),rset.getString(2)));
      rset.close();
      pstmt.close();


      pstmt = con.prepareStatement("select parent.name, parent.description " +
                                   "from ode_ontology, ode_terms_glossary parent, ode_terms_glossary child, " +
                                   "     ode_relation, ode_group, ode_terms_glossary relation " +
                                   "where " +
                                   "  ode_ontology.name = ? and " +
                                   "  ode_ontology.ontology_id = child.ontology_id and " +
                                   "  child.name = ? and " +
                                   "  child.type = " + TermTypes.CONCEPT + " and " +
                                   "  child.term_id = ode_group.term_id and " +
                                   "  ode_group.group_id = relation.term_id and " +
                                   "  relation.term_id = ode_relation.origin_term_id and " +
                                   "  ode_relation.destination_term_id = parent.term_id and " +
                                   "  relation.type = " + TermTypes.GROUP + " and " +
                                   "  ode_relation.name_id in (" + TermRelation.EXHAUSTIVE_I + ", " +
                                                                   TermRelation.DISJOINT_I + ")");
      pstmt.setString(1,ontology);
      pstmt.setString(2,concept);
      rset = pstmt.executeQuery();
      while(rset.next())
        v.addElement(new Concept(ontology,rset.getString(1),rset.getString(2)));
      rset.close();
      pstmt.close();

      if (v.size()>0) {
      	Concept[] concepts = new Concept[v.size()];
      	v.copyInto (concepts);
      	return concepts;
      }
      else
        return null;
    }
    finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
      }
      catch (Exception e) {
      }
    }
  }

  static public boolean isAccesible(DBConnection conn, String ontology, String ancestor, String children) throws SQLException, WebODEException {
    ArrayList aconcepts=new ArrayList();
    Concept[] parents=null;
    boolean accesible=children.equals(ancestor);
    aconcepts.add(children);

    String concept;
    for(; !accesible && aconcepts.size()>0; ) {
      concept=(String)aconcepts.remove(0);
      parents=getParentConcepts(conn,ontology,concept);
      for(int j=0; !accesible && parents!=null && j<parents.length; j++) {
        accesible=parents[j].term.equals(ancestor);
        aconcepts.add(parents[j].term);
      }
    }
    return accesible;
  }

  static public boolean inExhaustiveGroup(DBConnection conn, String ontology, String concept) throws SQLException, WebODEException {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (concept == null || (concept = concept.trim()).equals(""))
      throw new WebODEException ("Invalid concept parameter.");

    int conceptID=SQLUtil.getTermId(conn, ontology, concept, TermTypes.CONCEPT);
    if(conceptID==-1)
      throw new WebODEException ("Concept '" + concept + "' doesn't exist.");

    PreparedStatement pstmt=null;
    ResultSet rs=null;
    try {
      pstmt = conn.prepareStatement("select gr.* " +
                                    "from ode_relation rel, " +
                                    "     ode_group gr, " +
                                    "     ode_group group_term " +
                                    "where " +
                                    "  gr.term_id = ? and " +
                                    "  gr.group_id = group_term.term_id and " +
                                    "  group_term.term_id = rel.origin_term_id and " +
                                    "  rel.name_id = " + TermRelation.EXHAUSTIVE_I);
      pstmt.setInt(1,conceptID);
      rs=pstmt.executeQuery();
      return rs.next();
    }
    finally {
      if(rs!=null) rs.close();
      if(pstmt!=null) pstmt.close();
    }
  }

  static public boolean inDisjointGroup(DBConnection conn, String ontology, String concept) throws SQLException, WebODEException {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (concept == null || (concept = concept.trim()).equals(""))
      throw new WebODEException ("Invalid concept parameter.");

    int conceptID=SQLUtil.getTermId(conn, ontology, concept, TermTypes.CONCEPT);
    if(conceptID==-1)
      throw new WebODEException ("Concept '" + concept + "' doesn't exist.");

    PreparedStatement pstmt=null;
    ResultSet rs=null;
    try {
      pstmt = conn.prepareStatement("select gr.* " +
                                    "from ode_relation rel, " +
                                    "     ode_group gr, " +
                                    "     ode_group group_term " +
                                    "where " +
                                    "  gr.term_id = ? and " +
                                    "  gr.group_id = group_term.term_id and " +
                                    "  group_term.term_id = rel.origin_term_id and " +
                                    "  rel.name_id = " + TermRelation.DISJOINT_I);
      pstmt.setInt(1,conceptID);
      rs=pstmt.executeQuery();
      return rs.next();
    }
    finally {
      if(rs!=null) rs.close();
      if(pstmt!=null) pstmt.close();
    }
  }
}