package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;

/**
 * Relation Instance descriptor.
 *
 * @version 1.1
 */
public class TermRelationInstance implements java.io.Serializable
{
  /** The name of the instance. */
  public String name;

  /** Description. */
  public String description;

  /** The name of the instance set. */
  public String instanceSet;

  /** Origin instance. */
  public String origin;

  /** Destination instance. */
  public String destination;

  /** Relation */
  public TermRelation termRelation;

  public TermRelationInstance (String name, TermRelation relation, String instanceSet, String description,
                               String origin, String destination) {
    this.name = name;
    this.description = description;
    this.instanceSet = instanceSet;
    this.origin = origin;
    this.destination = destination;
    this.termRelation = relation;
  }

  public static TermRelationInstance[] getRelationInstances (DBConnection con, String ontology, String instanceSet)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      //System.out.println ("---------1:"+ ontology+"@"+instanceSet + "-");
      pstmt = con.prepareStatement ("select reli.name, rel.name, reli.description, origin.name, destination.name, " +
                                    "       concept_orig.name, concept_dest.name " +
                                    "from ode_terms_glossary reli, ode_terms_glossary rel, " +
                                    "     ode_terms_glossary origin, ode_terms_glossary destination, " +
                                    "     ode_terms_glossary iset, " +
                                    "     ode_ontology onto, ode_relation_instance ri, " +
                                    "     ode_terms_glossary concept_orig, ode_terms_glossary concept_dest, " +
                                    "     ode_relation orel " +
                                    "where " +
                                    "     reli.ontology_id = onto.ontology_id and onto.name = ? and " +
                                    "     reli.type = " + TermTypes.RELATION_INSTANCE + " and " +
                                    "     iset.name = ? and reli.parent_id = iset.term_id and " +
                                    "     ri.instance_id = reli.term_id and " +
                                    "     ri.origin_id = origin.term_id and " +
                                    "     ri.destination_id = destination.term_id and " +
                                    "     ri.term_id = rel.term_id and " +
                                    "     rel.term_id = orel.name_id and " +
                                    "     orel.origin_term_id = concept_orig.term_id and " +
                                    "     orel.destination_term_id = concept_dest.term_id");
      pstmt.setString (1, ontology);
      pstmt.setString (2, instanceSet);

      //System.out.println ("----2.001");
      rset = pstmt.executeQuery();
      //System.out.println ("----2.01");
      if (rset.next()) {
        //System.out.println ("---------2.1");
        Vector v = new Vector(200);
        do {
          TermRelation rel=new TermRelation(ontology,rset.getString(2), TermRelation._getRelationName (rset.getString(2)),rset.getString(6),rset.getString(7));
          v.addElement (new TermRelationInstance (rset.getString (1),
              rel,
              instanceSet, rset.getString (3),
              rset.getString (4), rset.getString (5)));
        } while (rset.next());

        TermRelationInstance[] at = new TermRelationInstance[v.size()];
        v.copyInto (at);

        return at;
      }
      else {
        //System.out.println ("---------2.2");
        return null;
      }
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  public static TermRelationInstance[] getRelationInstances (DBConnection con, String ontology,
      String instanceSet, String origin)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (origin == null || (origin = origin.trim()).equals(""))
      throw new WebODEException ("Invalid origin parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      //System.out.println ("---------*1:"+ ontology+"@"+instanceSet + "-" + origin);
      pstmt = con.prepareStatement ("select reli.name, rel.name, reli.description, origin.name, destination.name, " +
                                    "       concept_orig.name, concept_dest.name " +
                                    "from ode_terms_glossary reli, ode_terms_glossary rel, " +
                                    "     ode_terms_glossary origin, ode_terms_glossary destination, " +
                                    "     ode_terms_glossary iset, " +
                                    "     ode_ontology onto, ode_relation_instance ri, " +
                                    "     ode_terms_glossary concept_orig, ode_terms_glossary concept_dest, " +
                                    "     ode_relation orel " +
                                    "where " +
                                    "     reli.ontology_id = onto.ontology_id and onto.name = ? and " +
                                    "     reli.type = " + TermTypes.RELATION_INSTANCE + " and " +
                                    "     iset.name = ? and reli.parent_id = iset.term_id and " +
                                    "     ri.instance_id = reli.term_id and " +
                                    "     ri.origin_id = origin.term_id and " +
                                    "     ri.destination_id = destination.term_id and " +
                                    "     ri.term_id = rel.term_id and " +
                                    "     rel.term_id = orel.name_id and " +
                                    "     orel.origin_term_id = concept_orig.term_id and " +
                                    "     orel.destination_term_id = concept_dest.term_id and " +
                                    "     concept_orig.name = ?");
      pstmt.setString (1, ontology);
      pstmt.setString (2, instanceSet);
      pstmt.setString (3, origin);

      //System.out.println ("---------*1.1");
      rset = pstmt.executeQuery();
      //System.out.println ("---------*1.2");
      if (rset.next()) {
        //System.out.println ("---------2.1");
        Vector v = new Vector(200);
        do {
          TermRelation rel=new TermRelation(ontology,rset.getString(2), TermRelation._getRelationName (rset.getString(2)),rset.getString(6),rset.getString(7));
          v.addElement (new TermRelationInstance (rset.getString (1),
              rel,
              instanceSet, rset.getString (3),
              rset.getString (4), rset.getString (5)));
        } while (rset.next());

        TermRelationInstance[] at = new TermRelationInstance[v.size()];
        v.copyInto (at);

        return at;
      }
      else {
        //System.out.println ("---------2.2");
        return null;
      }
    }
    finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  public static TermRelationInstance[] getRelationInstancesFromInstance (DBConnection con, String ontology,
      String instanceSet, String origin_instance)
      throws SQLException, WebODEException {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (origin_instance == null || (origin_instance = origin_instance.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");

    Instance orig=Instance.getInstance(con,ontology,instanceSet,origin_instance);
    if(orig==null)
      throw new WebODEException("Instance '" + origin_instance + "' not found in instance set '" + instanceSet + "' in ontology '" + ontology + "'");
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      ArrayList v=new ArrayList();


      //System.out.println ("---------*1:"+ ontology+"@"+instanceSet + "-" + origin);
      pstmt = con.prepareStatement ("select reli.name, rel.name, reli.description, origin.name, destination.name, " +
                                    "       concept_orig.name, concept_dest.name " +
                                    "from ode_terms_glossary reli, ode_terms_glossary rel, " +
                                    "     ode_terms_glossary origin, ode_terms_glossary destination, " +
                                    "     ode_terms_glossary iset, " +
                                    "     ode_ontology onto, ode_relation_instance ri, " +
                                    "     ode_terms_glossary concept_orig, ode_terms_glossary concept_dest, " +
                                    "     ode_relation orel " +
                                    "where " +
                                    "     reli.ontology_id = onto.ontology_id and onto.name = ? and " +
                                    "     reli.type = " + TermTypes.RELATION_INSTANCE + " and " +
                                    "     iset.name = ? and reli.parent_id = iset.term_id and " +
                                    "     ri.instance_id = reli.term_id and " +
                                    "     ri.origin_id = origin.term_id and " +
                                    "     ri.destination_id = destination.term_id and " +
                                    "     ri.term_id = rel.term_id and " +
                                    "     origin.name = ? and " +
                                    "     rel.term_id = orel.name_id and " +
                                    "     orel.origin_term_id = concept_orig.term_id and " +
                                    "     orel.destination_term_id = concept_dest.term_id");
      pstmt.setString (1, ontology);
      pstmt.setString (2, instanceSet);
      pstmt.setString (3, origin_instance);

      //System.out.println ("---------*1.1");
      rset = pstmt.executeQuery();
      //System.out.println ("---------*1.2");
      while(rset.next()) {
        //System.out.println ("---------2.1");
        TermRelation rel=new TermRelation(ontology,rset.getString(2), TermRelation._getRelationName (rset.getString(2)),rset.getString(6),rset.getString(7));
        v.add(new TermRelationInstance (rset.getString (1),
                                        rel,
                                        instanceSet, rset.getString (3),
                                        rset.getString (4), rset.getString (5)));
      }

      rset.close();
      rset=null;
      pstmt.close();
      pstmt=null;
      if(v.size()>0)
        return (TermRelationInstance[]) v.toArray(new TermRelationInstance[0]);
      else
        return null;
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  public static TermRelationInstance[] getRelationInstancesToInstance (DBConnection con, String ontology,
      String instanceSet, String dest_instance)
      throws SQLException, WebODEException {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (dest_instance == null || (dest_instance = dest_instance.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");

    Instance dest=Instance.getInstance(con,ontology,instanceSet,dest_instance);
    if(dest==null)
      throw new WebODEException("Instance '" + dest + "' not found in instance set '" + instanceSet + "' in ontology '" + ontology + "'");
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      ArrayList v=new ArrayList();


      //System.out.println ("---------*1:"+ ontology+"@"+instanceSet + "-" + origin);
      pstmt = con.prepareStatement ("select reli.name, rel.name, reli.description, origin.name, destination.name, " +
                                    "       concept_orig.name, concept_dest.name " +
                                    "from ode_terms_glossary reli, ode_terms_glossary rel, " +
                                    "     ode_terms_glossary origin, ode_terms_glossary destination, " +
                                    "     ode_terms_glossary iset, " +
                                    "     ode_ontology onto, ode_relation_instance ri, " +
                                    "     ode_terms_glossary concept_orig, ode_terms_glossary concept_dest, " +
                                    "     ode_relation orel " +
                                    "where " +
                                    "     reli.ontology_id = onto.ontology_id and onto.name = ? and " +
                                    "     reli.type = " + TermTypes.RELATION_INSTANCE + " and " +
                                    "     iset.name = ? and reli.parent_id = iset.term_id and " +
                                    "     ri.instance_id = reli.term_id and " +
                                    "     ri.origin_id = origin.term_id and " +
                                    "     ri.destination_id = destination.term_id and " +
                                    "     ri.term_id = rel.term_id and " +
                                    "     destination.name = ? and " +
                                    "     rel.term_id = orel.name_id and " +
                                    "     orel.origin_term_id = concept_orig.term_id and " +
                                    "     orel.destination_term_id = concept_dest.term_id");
      pstmt.setString (1, ontology);
      pstmt.setString (2, instanceSet);
      pstmt.setString (3, dest_instance);

      //System.out.println ("---------*1.1");
      rset = pstmt.executeQuery();
      //System.out.println ("---------*1.2");
      while(rset.next()) {
        //System.out.println ("---------2.1");
        TermRelation rel=new TermRelation(ontology,rset.getString(2), TermRelation._getRelationName (rset.getString(2)),rset.getString(6),rset.getString(7));
        v.add(new TermRelationInstance (rset.getString (1),
                                        rel,
                                        instanceSet, rset.getString (3),
                                        rset.getString (4), rset.getString (5)));
      }

      rset.close();
      rset=null;
      pstmt.close();
      pstmt=null;
      if(v.size()>0)
        return (TermRelationInstance[]) v.toArray(new TermRelationInstance[0]);
      else
        return null;
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  public static TermRelationInstance[][] getRelationInstances (DBConnection con, String ontology,
      String instance_set, String[] instances) throws SQLException, WebODEException {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        instance_set == null || (instance_set = instance_set.trim()).equals("") ||
        instances == null)
      throw new WebODEException ("Invalid parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      HashMap relations=new HashMap();
      StringBuffer names=new StringBuffer();
      for(int i=0; i<instances.length; i++) {
        if(instances[i]!=null) {
          relations.put(instances[i], new TreeSet(new RelationInstanceComparator()));
          if(names.length()>0)
            names.append(", ?");
          else
            names.append("?");
        }
      }

      pstmt = con.prepareStatement ("select reli.name, rel.name, reli.description, origin.name, destination.name, " +
                                    "       concept_orig.name, concept_dest.name " +
                                    "from ode_terms_glossary reli, ode_terms_glossary rel, " +
                                    "     ode_terms_glossary origin, ode_terms_glossary destination, " +
                                    "     ode_terms_glossary iset, " +
                                    "     ode_ontology onto, ode_relation_instance ri, " +
                                    "     ode_terms_glossary concept_orig, ode_terms_glossary concept_dest, " +
                                    "     ode_relation orel " +
                                    "where " +
                                    "  reli.ontology_id = onto.ontology_id and " +
                                    "  onto.name = ? and " +
                                    "  reli.type = " + TermTypes.RELATION_INSTANCE + " and " +
                                    "  iset.name = ? and " +
                                    "  reli.parent_id = iset.term_id and " +
                                    "  ri.instance_id = reli.term_id and " +
                                    "  ri.origin_id = origin.term_id and " +
                                    "  ri.destination_id = destination.term_id and " +
                                    "  ri.term_id = rel.term_id and " +
                                    "  origin.name in (" + names + ") and " +
                                    "  rel.term_id = orel.name_id and " +
                                    "  orel.origin_term_id = concept_orig.term_id and " +
                                    "  orel.destination_term_id = concept_dest.term_id " +
                                    "order by origin.name");
      pstmt.setString (1, ontology);
      pstmt.setString (2, instance_set);
      int idx=3;
      for(Iterator it=relations.keySet().iterator(); it.hasNext(); ) {
        pstmt.setString (idx++, (String)it.next());
      }

      rset=pstmt.executeQuery();
      String previousInstance=null;
      String actualInstance;
      TreeSet v;
      String origin, destination, originConcept, destConcept, relName;
      TermRelation rel;
      while(rset.next()) {
        actualInstance=rset.getString(4);
        v=(TreeSet)relations.get(actualInstance);
        if(v==null)
          relations.put(actualInstance, v=new TreeSet());

        relName=rset.getString(2);
        origin=rset.getString(4);
        destination=rset.getString(5);
        originConcept=rset.getString(6);
        destConcept=rset.getString(7);
        rel=new TermRelation(ontology, relName, TermRelation._getRelationName(relName), originConcept, destConcept);
        v.add (new TermRelationInstance(rset.getString(1), rel,  instance_set, rset.getString(3), origin, destination));
      }

      TermRelationInstance[][] result=new TermRelationInstance[instances.length][];
      for(int i=0; i<result.length; i++) {
        result[i]=(TermRelationInstance[])((TreeSet)relations.get(instances[i])).toArray(new TermRelationInstance[0]);
      }

      return result;
    }
    finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
      }
      catch (Exception e) {}
    }
  }

  public void store (DBConnection con, String ontology) throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (name == null || (name = name.trim()).equals(""))
      throw new WebODEException ("Invalid name parameter.");
    if (termRelation.name == null || (termRelation.name = termRelation.name.trim()).equals(""))
      throw new WebODEException ("Invalid term parameter.");
    if (origin == null || (origin = origin.trim()).equals(""))
      throw new WebODEException ("Invalid origin parameter.");
    if (destination == null || (destination = destination.trim()).equals(""))
      throw new WebODEException ("Invalid destination parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      con.setAutoCommit (false);

      // Get the term for the instances
      String originTerm = _getTerm (con, ontology, origin);
      String destinationTerm = _getTerm (con, ontology, destination);

      int originIdT = SQLUtil.getTermId (con, ontology, originTerm, TermTypes.CONCEPT);
      int destinationIdT = SQLUtil.getTermId (con, ontology, destinationTerm, TermTypes.CONCEPT);

      System.out.println ("T:" + termRelation.name + "#" + origin + "#" + destination+ "-" + name);
      // Get info about the relation
      // Angelito -->
//	    TermRelation tr = TermRelation.getTermRelation (con, ontology,
//							    "_" + originIdT + "_" + destinationIdT + "_" + term);
//	    System.out.println ("TR: " + tr);

      TermRelation tr;
      if((tr=TermRelation.getTermRelation(con,termRelation.ontology,termRelation.name,termRelation.origin,termRelation.destination))==null)
        throw new WebODEException("Cannot create a relation instance from an inexistent relation");

      int originIdTRel = SQLUtil.getTermId (con, ontology, tr.origin, TermTypes.CONCEPT);
      int destinationIdTRel = SQLUtil.getTermId (con, ontology, tr.destination, TermTypes.CONCEPT);

      // <--Angelito
      if (originTerm == null || destinationTerm == null
         /* OSCAR (25Agosto2002)
         || !originTerm.equals(tr.origin) || !destinationTerm.equals (tr.destination)
         END OSCAR*/
          // Angelito -->
          || !Concept.isAccesible(con,ontology,tr.origin,originTerm)
          || !Concept.isAccesible(con,ontology,tr.destination,destinationTerm)
          // <--Angelito
          )
        throw new WebODEException ("The instance relation "+termRelation.name+" cannot be set from the origin instance to " +
        "the destination instance");

      int currentCardinality = _getCurrentCardinality (con, ontology);
      if (tr.maxCardinality >= 0 && currentCardinality == tr.maxCardinality)
        throw new WebODEException ("Maximum cardinality exceeded.");

      // Check the insertion is correct.
      //----------------------------------------------

      int instanceSetId = SQLUtil.getTermId (con, ontology, instanceSet, TermTypes.INSTANCE_SET);
      int instanceId = new Term (ontology, name, description, TermTypes.RELATION_INSTANCE, instanceSetId).store (con);
      int termId     = SQLUtil.getTermId (con, ontology, "_" + originIdTRel + "_" +
          destinationIdTRel + "_" + termRelation.name);
      int originId   = SQLUtil.getTermId (con, ontology, origin, TermTypes.INSTANCE, instanceSetId);
      int destinationId = SQLUtil.getTermId (con, ontology, destination, TermTypes.INSTANCE, instanceSetId);

      // Check
      //-----------------------------------

      System.out.println ("#" + instanceSetId + "#" + instanceId + "#" + termId + "@" + originId + "@" +
                          destinationId);

      pstmt = con.prepareStatement ("insert into ode_relation_instance " +
                                    "(instance_set_id, term_id, instance_id, origin_id, destination_id) values " +
                                    "(?, ?, ?, ?, ?)");
      pstmt.setInt (1, instanceSetId);
      pstmt.setInt (2, termId);
      pstmt.setInt (3, instanceId);
      pstmt.setInt (4, originId);
      pstmt.setInt (5, destinationId);
      pstmt.executeUpdate ();

      con.commit();
    } catch (SQLException e) {
      con.rollback();
      throw e;
    } finally {
      if (pstmt != null) pstmt.close();
      con.setAutoCommit (true);
    }
  }

  private String _getTerm (DBConnection con, String ontology, String instance)
      throws SQLException, WebODEException
  {
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t2.name " +
                                    " from ode_terms_glossary t, ode_ontology o, " +
                                    "      ode_terms_glossary t1, ode_instance i, " +
                                    "      ode_terms_glossary t2 " +
                                    " where " +
                                    "   t.ontology_id = o.ontology_id and o.name = ? and " + // Ontology
                                    "   t.name = ? and t.parent_id = t1.term_id and " +
                                    "   t1.name = ? and " + // Name and instance set
                                    "   t.term_id = i.instance_id and t1.term_id = i.instance_set_id and " +
                                    "   i.term_id = t2.term_id");
      pstmt.setString (1, ontology);
      pstmt.setString (2, instance);
      pstmt.setString (3, instanceSet);
      rset = pstmt.executeQuery();
      if (rset.next())
        return rset.getString (1);
      else
        return null;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  private int _getCurrentCardinality (DBConnection con, String ontology)
      throws WebODEException, SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select count(ri.term_id) " +
                                    " from ode_terms_glossary t, ode_terms_glossary t1, " +
                                    "      ode_terms_glossary t2, ode_terms_glossary t3, " +
                                    "      ode_relation_instance ri, ode_ontology o " +
                                    " where " +
                                    "   t.ontology_id = o.ontology_id and o.name = ? and " + // Ontology
                                    "   t.name = ? and ri.term_id = t.term_id and " + // Term
                                    "   t1.name = ? and ri.instance_set_id = t1.term_id and " + // Instance set
                                    "   t2.name = ? and ri.origin_id = t2.term_id and " + // Origin
                                    "   t3.name = ? and ri.destination_id = t3.term_id");
      pstmt.setString (1, ontology);
      pstmt.setString (2, termRelation.name);
      pstmt.setString (3, instanceSet);
      pstmt.setString (4, origin);
      pstmt.setString (5, destination);
      rset = pstmt.executeQuery();
      rset.next();
      return rset.getInt (1);
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }
}