package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;

/**
 * Class for handling term relations.
 *
 * @author  Julio César Arpírez Vega.
 * @author  Oscar Corcho
 * @version 2.0.6
 */
public class TermRelation implements java.io.Serializable
{
  public static final String SUBCLASS_OF                = "Subclass-of";
  public static final String NOT_SUBCLASS_OF            = "Not-Subclass-of";
  public static final String DISJOINT                   = "Disjoint";
  public static final String EXHAUSTIVE                 = "Exhaustive";
  public static final String TRANSITIVE_PART_OF         = "Transitive-Part-of";
  public static final String INTRANSITIVE_PART_OF       = "Intransitive-Part-of";
  public static final String ADHOC                      = "Adhoc";

  public static final int SUBCLASS_OF_I                = 1;
  public static final int NOT_SUBCLASS_OF_I            = 2;
  public static final int DISJOINT_I                   = 3;
  public static final int EXHAUSTIVE_I                 = 4;
  public static final int TRANSITIVE_PART_OF_I         = 5;
  public static final int INTRANSITIVE_PART_OF_I       = 6;
  public static final int ADHOC_I                      = 7;

  public String ontology;
  public String name, actualName;
  public String origin;
  public String destination;
  public String[] properties;
  /**
   * Maximum cardinality.
   */
  public int maxCardinality;

  public TermRelation (String ontology, String actual, String name, String origin, String destination)
  {
    this (ontology, actual, name, origin, destination, -1, null);
  }


  public TermRelation (String ontology, String actual, String name, String origin, String destination, int maxCardinality)
  {
    this (ontology, actual, name, origin, destination, maxCardinality, null);
  }

  public TermRelation (String ontology, String actual, String name, String origin,
                       String destination, int maxCardinality, String[] properties)
  {
    this.ontology    = ontology;
    this.name        = name;
    this.actualName  = actual;
    this.origin      = origin;
    this.destination = destination;
    this.properties  = properties;
    this.maxCardinality = maxCardinality;
  }

  public void store (DBConnection con) throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals ("") ||
        name == null || (name = name.trim()).equals ("") ||
        origin == null || (origin = origin.trim()).equals ("") ||
        destination == null || (destination = destination.trim()).equals (""))
      throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    try {
      // Init a transaction
      con.setAutoCommit (false);

      int relId = _getBuiltIn (name);
      int originId;
      if(relId==TermRelation.DISJOINT_I || relId==TermRelation.EXHAUSTIVE_I)
        originId = SQLUtil.getTermId (con, ontology, origin, TermTypes.GROUP);
      else
       originId = SQLUtil.getTermId (con, ontology, origin, TermTypes.CONCEPT);
      if(originId<0)
      if (originId < 0)
        // Term not present
        originId = new Term (ontology, origin, "", Term.CONCEPT).store (con);
      int destinationId = SQLUtil.getTermId (con, ontology, destination, TermTypes.CONCEPT);
      if (destinationId < 0)
        // Term not present
        destinationId = new Term(ontology, destination, "", Term.CONCEPT).store (con);

      // Store the relation in the glossary of terms if not built-in
      if (relId < 0) {
        // Constructs a new name, qualifying it with the ids of both origin
        // and destination concepts.  Thus, local relationships are allowed.
        relId = new Term (ontology, "_" + originId + "_" + destinationId + "_" + name,
                          "", Term.RELATION).store(con);
      }

      //System.out.println (originId + "#" + destinationId + "#" + relId);
      pstmt = con.prepareStatement ("insert into ode_relation (name_id, origin_term_id, " +
                                    "  destination_term_id, max_cardinality) " +
                                    " values (?, ?, ?, ?)");
      pstmt.setInt    (1, relId);
      pstmt.setInt    (2, originId);
      pstmt.setInt    (3, destinationId);
      pstmt.setInt    (4, maxCardinality < 0 ? - 1 : maxCardinality);
      pstmt.executeUpdate();

      if (properties != null) {
        pstmt.close();
        pstmt = con.prepareStatement ("insert into ode_relation_property (term_id, property_id) values (?, ?)");
        for (int i = 0; i < properties.length; i++) {
          int propertyId = _getPropertyId (con, ontology, properties[i]);
          pstmt.setInt (1, relId);
          pstmt.setInt (2, propertyId);
          pstmt.executeUpdate();
        }
      }

      con.commit();
    } catch (SQLException sqle) {
      con.rollback();
      throw sqle;
    } finally {
      try {
        if (pstmt != null) pstmt.close();
        con.setAutoCommit (true);
        } catch (Exception e) {}
    }
  }

  public static int _getBuiltIn (String name)
  {
    if (name.equals(SUBCLASS_OF)) {
      //relId = SQLUtil.getTermId (con, ontology, SUBCLASS_OF, TermTypes.RELATION);
      return 1;
    }
    else if (name.equals(NOT_SUBCLASS_OF)) {
      return 2;
      //relId = SQLUtil.getTermId (con, ontology, NOT_SUBCLASS_OF, TermTypes.RELATION);
    }
    else if (name.equals(DISJOINT)) {
      return 3;
      //relId = SQLUtil.getTermId (con, ontology, DISJOINT, TermTypes.RELATION);
    }
    else if (name.equals (EXHAUSTIVE)) {
      return 4;
    }
    else if (name.equals(TRANSITIVE_PART_OF)) {
      return 5;
      //relId = SQLUtil.getTermId (con, ontology, TRANSITIVE_PART_OF, TermTypes.RELATION);
    }
    else if (name.equals(INTRANSITIVE_PART_OF)) {
      //relId = SQLUtil.getTermId (con, ontology, INTRANSITIVE_PART_OF, TermTypes.RELATION);
      return 6;
    }
    return -1;
  }

  private int _getPropertyId (DBConnection con, String ontology, String property) throws SQLException
  {
    if (property.equals (TermProperty.REFLEXIVE))
      return 7;
    if (property.equals (TermProperty.IRREFLEXIVE))
      return 8;
    if (property.equals (TermProperty.SYMMETRICAL))
      return 9;
    if (property.equals (TermProperty.ASYMMETRICAL))
      return 10;
    if (property.equals (TermProperty.ANTISYMMETRICAL))
      return 11;
    if (property.equals (TermProperty.TRANSITIVE))
      return 12;
    return SQLUtil.getTermId (con, ontology, property, TermTypes.PROPERTY);
  }

  public static TermRelation[] getTermRelations (DBConnection con, String ontology, boolean bSubclass)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals (""))
      throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t3.name, t1.name, t2.name, r.max_cardinality " +
                                    " from ode_relation r, ode_terms_glossary t1, ode_terms_glossary t2, " +
                                    "  ode_terms_glossary t3, ode_ontology oo " +
                                    " where oo.ontology_id = t1.ontology_id and " +
                                    "  t1.term_id = r.origin_term_id and " +
                                    "  t3.term_id = r.name_id and " +
                                    "  r.destination_term_id = t2.term_id and oo.name = ? " +
                                    (bSubclass ? " and t3.name = ? " : "") +
                                    "  order by t3.name");
      pstmt.setString (1, ontology);
      if(bSubclass)
        pstmt.setString(2, TermRelation.SUBCLASS_OF);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector (200);
        do {
          String actual = rset.getString(1);
          v.addElement (new TermRelation (ontology, actual, _getRelationName (actual),
              rset.getString (2), rset.getString (3),
              rset.getInt (4)));
        } while (rset.next());

        TermRelation[] atr = new TermRelation[v.size()];
        v.copyInto (atr);

        return atr;
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

  public static TermRelation[] getTermRelations (DBConnection con, String ontology, String origin, boolean bSubclass)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals (""))
      throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t3.name, t1.name, t2.name, r.max_cardinality " +
                                    " from ode_relation r, ode_terms_glossary t1, ode_terms_glossary t2, " +
                                    "  ode_terms_glossary t3, ode_ontology oo " +
                                    " where oo.ontology_id = t1.ontology_id and " +
                                    "  t1.term_id = r.origin_term_id and " +
                                    "  t3.term_id = r.name_id and " +
                                    "  r.destination_term_id = t2.term_id and oo.name = ? and " +
                                    "  t1.name=? " +
                                    (bSubclass ? " and t3.name = ?" : "") +
                                    "  order by t3.name");
      pstmt.setString (1, ontology);
      pstmt.setString (2, origin);
      if(bSubclass)
        pstmt.setString (3, SUBCLASS_OF);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector (200);
        do {
          String actual = rset.getString(1);
          v.addElement (new TermRelation (ontology, actual, _getRelationName (actual),
              rset.getString (2), rset.getString (3),
              rset.getInt (4)));
        } while (rset.next());

        TermRelation[] atr = new TermRelation[v.size()];
        v.copyInto (atr);

        return atr;
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

  //OSCAR añadido (25Agosto2002)
  /**
   * It obtains all the ad-hoc relations that can be applied to the concept through inheritance.
   * As inheritance, it considers both subclass-of concept taxonomies and disjoint and exhaustive
   * subclass partitions.
   *
   * @author  Oscar Corcho
   * @param con A database connection
   * @param ontology The ontology where the concept is defined
   * @param concept The concept for which we wish to obtain the inherited term relations
   * @version 2.0.6
   */
  public static TermRelation[] getInheritedTermRelations (DBConnection con, String ontology, String concept)
      throws SQLException, WebODEException
  {
    try {
      if (ontology == null || (ontology = ontology.trim()).equals (""))
        throw new WebODEException ("Invalid parameters");

      System.out.println("Inside getInheritedTermRelations");
      Vector v = new Vector (200);
      TermRelation[] adhocRelations;

      System.out.println("Añadir relaciones asociadas al concepto");
      //Añadir relaciones asociadas al concepto
      adhocRelations = getAdHocTermRelationsAttachedToConcept(con,ontology,concept);
      if (adhocRelations!=null) {
        for (int j=0; j<adhocRelations.length; j++){
          v.addElement (adhocRelations[j]);
        }
      }

      System.out.println("Añadir relaciones asociadas a superclases del concepto");
      //Añadir relaciones asociadas a las superclases del concepto
      String[] superclasses=_getSuperclasses(con, ontology, concept);
      if (superclasses!=null) {
        for (int i=0; i<superclasses.length; i++){
          adhocRelations = getAdHocTermRelationsAttachedToConcept(con,ontology,superclasses[i]);
          if (adhocRelations!=null) {
            for (int j=0; j<adhocRelations.length; j++){
              v.addElement (adhocRelations[j]);
            }
          }
        }
      }

      System.out.println("Devolver relaciones");
      //Devolver todas las relaciones
      TermRelation[] finalAdHocRelations = new TermRelation[v.size()];
      v.copyInto (finalAdHocRelations);
      return finalAdHocRelations;
    }catch (Exception e) {
      throw new WebODEException ("Error in getInheritedTermRelations: " + e.getMessage(), e);
    }
  }


  /**
   * It gets all the superclasses of a concept in the ontology.
   * THIS METHOD SHOULD BE PLACED SOMEWHERE ELSE. MAYBE IN A HELPER CLASS OF ODE SERVICE.
   *
   * @author  Oscar Corcho
   * @param con A database connection
   * @param ontology The ontology where the concept is defined
   * @param concept The concept for which we wish to obtain the superclasses
   * @version 2.0.6
   */
  private static String[] _getSuperclasses (DBConnection con, String ontology, String concept)
      throws SQLException
  {
    Set superclasses = new HashSet();
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    PreparedStatement pstmt2 = null;
    ResultSet          rset2 = null;

    try {
      //****************Obtener subclass-of directas
      //System.out.println("Preparing statement with superclass-of ");
      pstmt = con.prepareStatement ("select t2.name " +
                                    " from ode_relation r, ode_terms_glossary t1, ode_terms_glossary t2, " +
                                    "  ode_ontology oo " +
                                    " where oo.name = ? and oo.ontology_id = t1.ontology_id and " +
                                    "  t1.name = ? and t1.term_id = r.origin_term_id and " +
                                    "  r.name_id = " + TermRelation.SUBCLASS_OF_I + " and " +
                                    "  r.destination_term_id = t2.term_id " +
                                    "  order by t2.name");
      pstmt.setString (1, ontology);
      pstmt.setString (2, concept);
      //System.out.println("About to execute superclass-of");
      rset = pstmt.executeQuery();
      //System.out.println("Executed superclass-of");
      if (rset.next()) {
        do {
          //System.out.println("Adding superclass-of superclass: " + rset.getString(1));
          superclasses.add (rset.getString (1));
        } while (rset.next());
      }

      //****************Obtener grupos directos
      //System.out.println("Preparing statement with group ");
      pstmt = con.prepareStatement ("select t.name, t1.name " +
                                    " from ode_terms_glossary t, ode_terms_glossary t1, " +
                                    "      ode_group g, ode_ontology o " +
                                    " where t.type = 15 and t.term_id = g.group_id and " +
                                    "       g.term_id = t1.term_id and t1.name = ? and " +
                                    "       t.ontology_id = o.ontology_id and o.name = ?");
      pstmt.setString (1, concept);
      pstmt.setString (2, ontology);
      //System.out.println("About to execute group");
      rset = pstmt.executeQuery ();
      //System.out.println("Executed group");
      if (rset.next()) {
        do {
          //System.out.println("Preparing statement inside group ");
          pstmt2 = con.prepareStatement ("select t2.name " +
              " from ode_relation r, ode_terms_glossary t1, ode_terms_glossary t2, " +
              "  ode_terms_glossary t3, ode_ontology oo " +
              " where oo.ontology_id = t1.ontology_id and " +
              "  t1.term_id = r.origin_term_id and " +
              "  t1.name = ? and " +
              "  t3.term_id = r.name_id and " +
              "  r.destination_term_id = t2.term_id and oo.name = ? " +
              " and t3.name in ('" + DISJOINT + "', '" + EXHAUSTIVE + "')"  +
              "  order by t3.name");
          pstmt2.setString (1, rset.getString(1));
          pstmt2.setString (2, ontology);
          //System.out.println("About to execute inside group");
          rset2 = pstmt2.executeQuery ();
          //System.out.println("Executed inside group ");
          if (rset2.next()) {
            do {
              //System.out.println("Adding group-based superclass " + rset.getString(1));
              superclasses.add (rset2.getString (1));
            } while (rset2.next());
          }
        } while (rset.next());
      }

      if (superclasses.size()==0) return null;
      else {
        Set newSuperclasses = new HashSet();
        for (Iterator iter=superclasses.iterator(); iter.hasNext(); ){
          //System.out.println("Go for more superclasses");
          String[] moreSuperclasses = _getSuperclasses(con, ontology, (String) iter.next());
          //System.out.println("End of more superclasses");
          if (moreSuperclasses != null) {
            //System.out.println("Hubo superclasses");
            for (int j=0; j<moreSuperclasses.length; j++){
              newSuperclasses.add(moreSuperclasses[j]);
            }
          }
        };
        for (Iterator iter2=superclasses.iterator(); iter2.hasNext(); ){
          newSuperclasses.add(iter2.next());
        };
        return (String[]) newSuperclasses.toArray();
      }
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        if (rset2  != null) rset.close();
        if (pstmt2 != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  /**
   * It obtains all the direct ad-hoc relations attached to a concept in the ontology.
   *
   * @author  Oscar Corcho
   * @param con A database connection
   * @param ontology The ontology where the concept is defined
   * @param concept The concept for which we wish to obtain the superclasses
   * @version 2.0.6
   */
  public static TermRelation[] getAdHocTermRelationsAttachedToConcept (DBConnection con, String ontology, String concept)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals (""))
      throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t3.name, t1.name, t2.name, r.max_cardinality, t3.term_id " +
                                    " from ode_relation r, ode_terms_glossary t1, ode_terms_glossary t2, " +
                                    "  ode_terms_glossary t3, ode_ontology oo " +
                                    " where oo.ontology_id = t1.ontology_id and " +
                                    "  t1.name = ? and " +
                                    "  t1.term_id = r.origin_term_id and " +
                                    "  t3.term_id = r.name_id and " +
                                    "  r.destination_term_id = t2.term_id and oo.name = ? " +
                                    " and t3.name not in ('" + SUBCLASS_OF + "', '" + NOT_SUBCLASS_OF + "', '" +
                                    DISJOINT + "', '" + EXHAUSTIVE + "', '" +
                                    TRANSITIVE_PART_OF + "', '" + INTRANSITIVE_PART_OF + "')" +
                                    "  order by t3.name");
      pstmt.setString (1, concept);
      pstmt.setString (2, ontology);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector (200);
        do {
          String actual = rset.getString(1);
          v.addElement (new TermRelation (ontology, actual, _getRelationName (actual),
              rset.getString (2), rset.getString (3),
              rset.getInt (4),
              _getProperties (con, rset.getInt (5))));
        } while (rset.next());

        TermRelation[] atr = new TermRelation[v.size()];
        v.copyInto (atr);

        return atr;
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
//FIN OSCAR AÑADIDO



  private static String[] _getProperties (DBConnection con, int termId) throws SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      pstmt = con.prepareStatement ("select t.name from ode_terms_glossary t, " +
                                    " ode_relation_property p where t.term_id = p.property_id and " +
                                    " p.term_id = " + termId);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector(20);
        do {
          v.addElement (rset.getString (1));
        } while (rset.next());
        String[] astr = new String[v.size()];
        v.copyInto (astr);
        return astr;
      }
      else return null;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  public static TermRelation[] getTermRelations (DBConnection con, String ontology, String type)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals ("") ||
        type == null || (type = type.trim()).equals(""))
      throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t3.name, t1.name, t2.name, r.max_cardinality, t3.term_id " +
                                    " from ode_relation r, ode_terms_glossary t1, ode_terms_glossary t2, " +
                                    "  ode_terms_glossary t3, ode_ontology oo " +
                                    " where oo.ontology_id = t1.ontology_id and " +
                                    "  t1.term_id = r.origin_term_id and " +
                                    "  t3.term_id = r.name_id and " +
                                    "  r.destination_term_id = t2.term_id and oo.name = ? " +
                                    " and t3.name " + (type.equals (TermRelation.ADHOC) ?
                                    "not in ('" + SUBCLASS_OF + "', '" + NOT_SUBCLASS_OF + "', '" +
                                    DISJOINT + "', '" + EXHAUSTIVE + "', '" +
                                    TRANSITIVE_PART_OF + "', '" + INTRANSITIVE_PART_OF + "')" :
                                    "= ?") +
                                    "  order by t3.name");
      pstmt.setString (1, ontology);
      if (!type.equals (ADHOC))
        pstmt.setString (2, type);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector (200);
        do {
          String actual = rset.getString(1);
          v.addElement (new TermRelation (ontology, actual, _getRelationName (actual),
              rset.getString (2), rset.getString (3),
              rset.getInt (4),
              _getProperties (con, rset.getInt (5))));
        } while (rset.next());

        TermRelation[] atr = new TermRelation[v.size()];
        v.copyInto (atr);

        return atr;
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

  // GEt information about an adhoc relation.
  public static TermRelation getTermRelation (DBConnection con, String ontology, String actualName)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals ("") ||
        actualName == null || (actualName = actualName.trim()).equals(""))
      throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t3.name, t1.name, t2.name, r.max_cardinality, t3.term_id " +
                                    " from ode_relation r, ode_terms_glossary t1, ode_terms_glossary t2, " +
                                    "  ode_terms_glossary t3, ode_ontology oo " +
                                    " where oo.ontology_id = t1.ontology_id and " +
                                    "  t1.term_id = r.origin_term_id and " +
                                    "  t3.term_id = r.name_id and " +
                                    "  r.destination_term_id = t2.term_id and oo.name = ? " +
                                    "  and t3.name = ?" +
                                    "  order by t3.name");
      pstmt.setString (1, ontology);
      pstmt.setString (2, actualName);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        String actual = rset.getString(1);
        return new TermRelation (ontology, actual, _getRelationName (actual),
                                 rset.getString (2), rset.getString (3),
                                 rset.getInt (4),
                                 _getProperties (con, rset.getInt (5)));
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


  // GEt information about an adhoc relation.
  public static TermRelation getTermRelation (DBConnection con, String ontology, String name, String origin, String destination)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals ("") ||
        name == null || (name = name.trim()).equals(""))
      throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      int originId=SQLUtil.getTermId(con,ontology,origin,TermTypes.CONCEPT);
      if(originId<0)
        throw new WebODEException("Origin concept doesn't exist is the ontology.");

      int destinationId=SQLUtil.getTermId(con,ontology,destination,TermTypes.CONCEPT);
      if(destinationId<0)
        throw new WebODEException("Destination concept doesn't exist is the ontology.");

      pstmt = con.prepareStatement ("select t3.name, t1.name, t2.name, r.max_cardinality, t3.term_id " +
                                    " from ode_relation r, ode_terms_glossary t1, ode_terms_glossary t2, " +
                                    "  ode_terms_glossary t3, ode_ontology oo " +
                                    " where oo.ontology_id = t1.ontology_id and " +
                                    "  t1.term_id = r.origin_term_id and " +
                                    "  t3.term_id = r.name_id and " +
                                    "  r.destination_term_id = t2.term_id and oo.name = ? and " +
                                    "  t3.name = ? and " +
                                    "  t1.name = ? and " +
                                    "  t2.name = ? " +
                                    "  order by t3.name");
      pstmt.setString (1, ontology);
      pstmt.setString (2, "_" + originId + "_" + destinationId + "_" + name);
      pstmt.setString (3, origin);
      pstmt.setString (4, destination);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        String actual = rset.getString(1);
        return new TermRelation (ontology, actual, _getRelationName (actual),
                                 rset.getString (2), rset.getString (3),
                                 rset.getInt (4),
                                 _getProperties (con, rset.getInt (5)));
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

  public static TermRelation[] getTermRelations (DBConnection con, String ontology)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals (""))
      throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t3.name, t1.name, t2.name, r.max_cardinality, t3.term_id " +
                                    " from ode_relation r, ode_terms_glossary t1, ode_terms_glossary t2, " +
                                    "  ode_terms_glossary t3, ode_ontology oo " +
                                    " where oo.ontology_id = t1.ontology_id and " +
                                    "  t1.term_id = r.origin_term_id and " +
                                    "  t3.term_id = r.name_id and " +
                                    "  r.destination_term_id = t2.term_id and oo.name = ? " +
                                    "  order by t3.name");
      pstmt.setString (1, ontology);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector (200);
        do {
          String actual = rset.getString(1);
          v.addElement (new TermRelation (ontology, actual, _getRelationName (actual),
              rset.getString (2), rset.getString (3),
              rset.getInt (4),
              _getProperties (con, rset.getInt (5))));
        } while (rset.next());

        TermRelation[] atr = new TermRelation[v.size()];
        v.copyInto (atr);

        return atr;
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


  public void remove (DBConnection con)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals (""))
      throw new WebODEException ("Invalid parameters");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      con.setAutoCommit (false);
      int originTermId, destinationTermId, nameId;
      originTermId = SQLUtil.getTermId (con, ontology, origin, TermTypes.CONCEPT);
      destinationTermId = SQLUtil.getTermId (con, ontology, destination, TermTypes.CONCEPT);
      nameId =  _getBuiltIn (name);
      if (nameId < 0) {
        nameId = SQLUtil.getTermId (con, ontology,
                                    actualName = "_" + originTermId + "_" + destinationTermId + "_" + name,
                                    TermTypes.RELATION);
      }

      //System.out.println (origin + "%" + destination + "%" + name);
      //System.out.println (originTermId + "#" + destinationTermId + "#" +
      //		nameId);

      pstmt = con.prepareStatement ("delete from ode_relation where name_id = ? and " +
                                    " origin_term_id = ? and destination_term_id = ?");
      pstmt.setInt    (1, nameId);
      pstmt.setInt    (2, originTermId);
      pstmt.setInt    (3, destinationTermId);
      pstmt.executeUpdate();

      // Remove relation if not built-in
      if(actualName!=null)
        Term.removeTerm (con, ontology, actualName);

      con.commit();
    } catch (SQLException sqle) {
      con.rollback();

      throw sqle;
    } finally {
      try {
        if (pstmt != null) pstmt.close();

        con.setAutoCommit (true);
        } catch (Exception e) {}
    }

  }

  public static String _getRelationName (String name)
  {
    if (name.charAt(0) == '_') {
      int i1 = name.indexOf ('_', 1);
      int i2 = name.indexOf ('_', i1 + 1);

      //System.out.println ("NAME: " + name.substring (i2 + 1));
      return name.substring (i2 + 1);
    }
    else
      return name;
  }

  public static TermRelation[] findTermRelation(DBConnection conn, String ontology, String name) throws SQLException {
    PreparedStatement pstmt=null;
    ResultSet rs=null;
    try {
      pstmt = conn.prepareStatement ("select t3.name, t1.name, t2.name, r.max_cardinality, t3.term_id " +
                                     " from ode_relation r, ode_terms_glossary t1, ode_terms_glossary t2, " +
                                     "  ode_terms_glossary t3, ode_ontology oo " +
                                     " where " +
                                     "  oo.ontology_id = t1.ontology_id and " +
                                     "  t3.type = " + TermTypes.RELATION + " and " +
                                     "  t1.term_id = r.origin_term_id and " +
                                     "  t3.term_id = r.name_id and " +
                                     "  r.destination_term_id = t2.term_id and oo.name = ? and " +
                                     "  t3.name = '_' || t1.term_id || '_' || t2.term_id || '_' || ?");
      pstmt.setString(1,ontology);
      pstmt.setString(2,name);
      rs=pstmt.executeQuery();
      ArrayList atrs=null;
      if(rs.next()) {
        String actualName;
        do {
          actualName=rs.getString(1);
          atrs.add (new TermRelation (ontology, actualName, _getRelationName (actualName),
                                      rs.getString (2), rs.getString (3), rs.getInt (4)));
        }
        while(rs.next());
        return (TermRelation[])atrs.toArray(new TermRelation[0]);
      }
      else
        return null;
    }
    finally {
      if(rs!=null) rs.close();
      if(pstmt!=null) pstmt.close();
    }
  }
}
