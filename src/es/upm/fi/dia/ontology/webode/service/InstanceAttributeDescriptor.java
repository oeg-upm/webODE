package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.webode.service.util.*;

/**
 * A descriptor class for the instance attributes.
 *
 * @author  Julio César Arpírez Vega
 * @author  David Manzano
 * @author  Oscar Corcho
 *
 * @version 1.1
 */
public class InstanceAttributeDescriptor extends Attribute
    implements java.io.Serializable, TermTypes
{
  /** Minimum value. */
  public String minValue;

  /** Maximum value. */
  public String maxValue;

  /**
   * Constructor for the class attribute descriptor.
   *
   * @param name     The class attribute name.
   * @param termName The term's name.
   * @param valueTypeName The value type as defined in <tt>ValueTypeDescriptor</tt>.
   * @param measurementUnit Measurement unit.
   * @param precision Precision.
   * @param minCardinality Minimum cardinality.
   * @param maxCardinality Maximum cardinality.
   * @param description The attribute's description.
   * @param maxValue    The maximum value.
   * @param minValue    The minimum value.
   *
   * @see ValueTypes
   */
  public InstanceAttributeDescriptor (String name, String termName,
                                      String valueTypeName, String measurementUnit,
                                      String precision, int minCardinality, int maxCardinality,
                                      String description, String minValue, String maxValue)
  {
    this.name = name;
    this.termName        = termName;
    this.valueTypeName   = valueTypeName;
    this.measurementUnit = measurementUnit;
    this.precision       = precision;
    this.minCardinality  = minCardinality;
    this.maxCardinality  = maxCardinality;
    this.description     = description;
    this.minValue        = minValue;
    this.maxValue        = maxValue;
  }

  protected InstanceAttributeDescriptor (String name, String termName, int valueType,
                                         String valueTypeName, String measurementUnit,
                                         String precision, int minCardinality, int maxCardinality,
                                         String description, String minValue, String maxValue)
  {
    this.name = name;
    this.termName        = termName;
    this.valueType       = valueType;
    this.valueTypeName   = valueTypeName;
    this.measurementUnit = measurementUnit;
    this.precision       = precision;
    this.minCardinality  = minCardinality;
    this.maxCardinality  = maxCardinality;
    this.description     = description;
    this.minValue        = minValue;
    this.maxValue        = maxValue;
  }

/*  public String getValueName ()
  {
    return ValueTypes.NAMES[valueType - 1];
  }
*/

  private static void getAncestors(DBConnection conn, String ontology, String child, ArrayList order, HashSet visited)
      throws SQLException, WebODEException {
    Concept[] parents=Concept.getParentConcepts(conn,ontology,child);
    for(int i=0; parents!=null && i<parents.length; i++)
      getAncestors(conn,ontology,parents[i].term,order,visited);
    if(!visited.contains(child)) {
      order.add(child);
      visited.add(child);
    }
  }

  protected static ArrayList getAncestors(DBConnection conn, String ontology, String child)
      throws WebODEException, SQLException {
    ArrayList ancestors=new ArrayList();
    HashSet visited=new HashSet();

    getAncestors(conn,ontology,child,ancestors,visited);

    if(ancestors.size()==0)
      ancestors=null;
    return ancestors;
  }

  /**
   * Gets all the instance attributes for a given term.
   *
   * @param con The database connection.
   * @param ontology The ontology.
   * @param name     The term's name.
   */
  public static InstanceAttributeDescriptor[] getInstanceAttributes (DBConnection con, String ontology, String name)
      throws WebODEException, SQLException
  {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        name == null || (name = name.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      if(con.dbManager==DBConnection.ORACLE)
        pstmt = con.prepareStatement ("select ia_attribute.name,  ia_concept.name, " +
                                      "   type.value_type, type.name, " +
                                      "   oia.MEASUREMENT_UNIT, " +
                                      "   oia.PRECISION, oia.MIN_CARDINALITY, " +
                                      "   oia.MAX_CARDINALITY, ia_attribute.description, " +
                                      "   oia.MIN_VALUE, oia.MAX_VALUE " +
                                      " from ode_terms_glossary ia_concept, ode_terms_glossary ia_attribute, " +
                                      "      ode_instance_attribute oia, ode_value_type type " +
                                      " where " +
                                      "  ia_concept.name = ? and ia_concept.ontology_id = ? and " +
                                      "  ia_concept.term_id = ia_attribute.parent_id and " +
                                      "  oia.is_ia_attribute_term_id = ia_attribute.term_id and " +
                                      "  oia.value_type_id = type.value_type_id " +
                                      "order by ia_attribute.name");
      else
        pstmt = con.prepareStatement ("select ia_attribute.name,  ia_concept.name, " +
                                      "   type.value_type, type.name, " +
                                      "   oia.MEASUREMENT_UNIT, " +
                                      "   oia.`PRECISION`, oia.MIN_CARDINALITY, " +
                                      "   oia.MAX_CARDINALITY, ia_attribute.description, " +
                                      "   oia.MIN_VALUE, oia.MAX_VALUE " +
                                      " from ode_terms_glossary ia_concept, ode_terms_glossary ia_attribute, " +
                                      "      ode_instance_attribute oia, ode_value_type type " +
                                      " where " +
                                      "  ia_concept.name = ? and ia_concept.ontology_id = ? and " +
                                      "  ia_concept.term_id = ia_attribute.parent_id and " +
                                      "  oia.is_ia_attribute_term_id = ia_attribute.term_id and " +
                                      "  oia.value_type_id = type.value_type_id " +
                                      "order by ia_attribute.name");
      pstmt.setString (1, name);
      pstmt.setInt    (2, SQLUtil.getOntologyId (con, ontology));
      rset = pstmt.executeQuery ();

      if (rset.next()) {
        Vector v = new Vector(200);

        do {
          InstanceAttributeDescriptor foo;
          v.addElement (foo =
                        new InstanceAttributeDescriptor (rset.getString (1),   // Attr name
                        rset.getString (2),   // Term name
                        rset.getInt    (3),   // Value type
                        rset.getString (4),   // value type name
                        rset.getString (5),   // Measurement name
                        rset.getString (6),   // Precision
                        rset.getInt    (7),   // Min cardinality
                        rset.getInt    (8),   // Max cardinality
                        rset.getString (9),   // Description
                        rset.getString (10), // Min value
                        rset.getString (11)));// Max value

          // Add values
          new ClassAttributeValue (foo).getValues(con, ontology);

        } while (rset.next());
        InstanceAttributeDescriptor[] acad = new InstanceAttributeDescriptor[v.size()];
        v.copyInto (acad);

        return acad;
      }
      else
        return null;
    } finally {
      if (rset  != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /**
   * Gets all the instance attributes for a given term, included the inheritance attributes.
   *
   * @param con The database connection.
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param inheritance     Look for inherited attributes too.
   */
  public static InstanceAttributeDescriptor[] getInstanceAttributes (DBConnection con, String ontology, String name, boolean inheritance)
      throws WebODEException, SQLException {
    InstanceAttributeDescriptor[] atts=null;
    if(!inheritance)
      atts=getInstanceAttributes (con, ontology, name);
    else {
      ArrayList latts=new ArrayList();
      ArrayList lconcepts=getAncestors(con,ontology,name);
      InstanceAttributeDescriptor[] atts_concept;
      int length=0;
      for(int i=0; i<lconcepts.size(); i++) {

        atts_concept=getInstanceAttributes (con, ontology, (String)lconcepts.get(i));
        if(atts_concept!=null) {
          length+=atts_concept.length;
          latts.add(atts_concept);
        }
      }

      atts=new InstanceAttributeDescriptor[length];
      int k=0;
      for(int i=0; i<latts.size(); i++) {
        atts_concept=(InstanceAttributeDescriptor[])latts.get(i);
        for(int j=0; j<atts_concept.length; j++)
          atts[k++]=atts_concept[j];
      }
    }
    return atts;
  }

  /**
   * Gets the instance attribute of a concept.
   *
   * @param con The database connection.
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param attribute The attribute's name.
   */
  public static InstanceAttributeDescriptor getInstanceAttribute (DBConnection con, String ontology,
      String name, String attribute)
      throws WebODEException, SQLException
  {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        name == null || (name = name.trim()).equals("") ||
        attribute == null || (attribute = attribute.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      if(con.dbManager==DBConnection.ORACLE)
        pstmt = con.prepareStatement ("select ia_attribute.name,  ia_concept.name, " +
                                      "   type.value_type, type.name, " +
                                      "   oia.MEASUREMENT_UNIT, " +
                                      "   oia.PRECISION, oia.MIN_CARDINALITY, " +
                                      "   oia.MAX_CARDINALITY, ia_attribute.description, " +
                                      "   oia.MIN_VALUE, oia.MAX_VALUE " +
                                      " from ode_terms_glossary ia_concept, ode_terms_glossary ia_attribute, " +
                                      "      ode_instance_attribute oia, ode_value_type type " +
                                      " where " +
                                      "  ia_attribute.name = ? and ia_attribute.ontology_id = ? and " +
                                      "  ia_attribute.parent_id = ia_concept.term_id and " +
                                      "  ia_concept.name = ? and " +
                                      "  oia.is_ia_attribute_term_id = ia_attribute.term_id and " +
                                      "  oia.value_type_id = type.value_type_id " +
                                      " order by ia_attribute.name");
      else
        pstmt = con.prepareStatement ("select ia_attribute.name,  ia_concept.name, " +
                                      "   type.value_type, type.name, " +
                                      "   oia.MEASUREMENT_UNIT, " +
                                      "   oia.`PRECISION`, oia.MIN_CARDINALITY, " +
                                      "   oia.MAX_CARDINALITY, ia_attribute.description, " +
                                      "   oia.MIN_VALUE, oia.MAX_VALUE " +
                                      " from ode_terms_glossary ia_concept, ode_terms_glossary ia_attribute, " +
                                      "      ode_instance_attribute oia, ode_value_type type " +
                                      " where " +
                                      "  ia_attribute.name = ? and ia_attribute.ontology_id = ? and " +
                                      "  ia_attribute.parent_id = ia_concept.term_id and " +
                                      "  ia_concept.name = ? and " +
                                      "  oia.is_ia_attribute_term_id = ia_attribute.term_id and " +
                                      "  oia.value_type_id = type.value_type_id " +
                                      " order by ia_attribute.name");
      pstmt.setString (1, attribute);
      pstmt.setInt    (2, SQLUtil.getOntologyId (con, ontology));
      pstmt.setString (3, name);
      rset = pstmt.executeQuery ();

      if (rset.next()) {
        InstanceAttributeDescriptor foo =
            new InstanceAttributeDescriptor (rset.getString (1), // Attr name
            rset.getString (2), // Term name
            rset.getInt    (3), // Value type
            rset.getString (4), // value type name
            rset.getString (5), // Measurement name
            rset.getString (6), // Precision
            rset.getInt    (7), // Min cardinality
            rset.getInt    (8), // Max cardinality
            rset.getString (9), // Description
            rset.getString (10), // Min value
            rset.getString (11));// Max value
        // Add values
        new ClassAttributeValue (foo).getValues(con, ontology);

        return foo;
      }
      else
        return null;
    } finally {
      if (rset  != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }


  /**
   * Stores current class attribute information.
   *
   * @param con The database connection.
   * @param ontologyName The ontology.
   */
  public void store (DBConnection con, String ontologyName)
      throws WebODEException, SQLException
  {
    // Check fields are right.
    if (ontologyName == null || (ontologyName = ontologyName.trim()).equals("") ||
        name == null || (name = name.trim()).equals("") ||
        termName == null || (termName = termName.trim()).equals(""))
      throw new WebODEException ("Invalid state and/or parameters in instance attribute storage.");

    int typeId=SQLUtil.getValueTypeId(con, ontologyName, this.valueTypeName);
    if(typeId==-1)
      throw new WebODEException("The value type '" + this.valueTypeName + "' doesn't exists");
    this.valueType=typeId;

    PreparedStatement pstmt = null;
    try {
      con.setAutoCommit (false);

      // Get related term's id.
      int relatedId = SQLUtil.getTermId (con, ontologyName, termName, TermTypes.CONCEPT);

      // First, insert the attribute into the glossary of terms
      Term t = new Term (ontologyName, name, description, INSTANCE_ATTRIBUTE, relatedId);
      int attrId = t.store (con);

      // Now insert the attribute's properties.
      if(con.dbManager!=DBConnection.MYSQL)
        pstmt = con.prepareStatement ("insert into ode_instance_attribute " +
                                      "(IS_IA_ATTRIBUTE_TERM_ID, " +
                                      " VALUE_TYPE_ID, " +
                                      " MEASUREMENT_UNIT, " +
                                      " PRECISION, MIN_CARDINALITY, MAX_CARDINALITY, MIN_VALUE, MAX_VALUE " +
                                      ") values (?, ?, ?, ?, ?, ?, ?, ?)");
      else
        pstmt = con.prepareStatement ("insert into ode_instance_attribute " +
                                      "(IS_IA_ATTRIBUTE_TERM_ID, " +
                                      " VALUE_TYPE_ID, " +
                                      " MEASUREMENT_UNIT, " +
                                      " `PRECISION`, MIN_CARDINALITY, MAX_CARDINALITY, MIN_VALUE, MAX_VALUE " +
                                      ") values (?, ?, ?, ?, ?, ?, ?, ?)");
      pstmt.setInt    (1, attrId);
      pstmt.setInt    (2, valueType);
      if (measurementUnit == null || (measurementUnit = measurementUnit.trim()).equals(""))
        pstmt.setNull   (3, Types.VARCHAR);
      else
        pstmt.setString (3, measurementUnit);
      if (precision == null || (precision = precision.trim()).equals(""))
        pstmt.setNull   (4, Types.VARCHAR);
      else
        pstmt.setString (4, precision);
      pstmt.setInt    (5, minCardinality);
      pstmt.setInt    (6, maxCardinality);
      if(minValue==null)
        pstmt.setNull (7, Types.VARCHAR);
      else
        pstmt.setString (7, minValue);
      if(maxValue==null)
        pstmt.setNull (8, Types.VARCHAR);
      else
        pstmt.setString (8, maxValue);

      // Update
      pstmt.executeUpdate();

      // Commit changes.
      con.commit();
    } catch (SQLException sqle) {
      // Rollback transaction
      con.rollback();
      throw sqle;
    } finally {
      if (pstmt != null) pstmt.close();
      con.setAutoCommit (true);
    }
  }

  public void update (DBConnection con, String ontologyName, String attribute, String parent)
      throws SQLException, WebODEException
  {
    // Check fields are right.
    if (ontologyName == null || (ontologyName = ontologyName.trim()).equals("") ||
        name == null || (name = name.trim()).equals("") ||
        attribute == null || (attribute = attribute.trim()).equals(""))
      throw new WebODEException ("Invalid state and/or parameters.");

    int typeId=SQLUtil.getValueTypeId(con, ontologyName, this.valueTypeName);
    if(typeId==-1)
      throw new WebODEException("The value type '" + this.valueTypeName + "' doesn't exists");
    this.valueType=typeId;

    PreparedStatement pstmt = null;
    try {
      // Init a new transaction
      con.setAutoCommit (false);

      // Update term information about the attribute
      int relatedId = SQLUtil.getTermId (con, ontologyName, parent, TermTypes.CONCEPT);
      Term t = new Term (ontologyName, name, description, INSTANCE_ATTRIBUTE,
                         relatedId);
      int attrId = t.update (con, attribute, parent);
      //int ontologyId = -1;
      //if (ontology != null && (ontology = ontology.trim()).equals(""))
      //  ontologyId = SQLUtil.getOntologyId (con, ontologyName);

      // Now update the attribute's properties.
      if(con.dbManager!=DBConnection.MYSQL)
        pstmt = con.prepareStatement ("update ode_instance_attribute " +
                                      "set  " +
                                      " VALUE_TYPE_ID = ?, " +
                                      " MEASUREMENT_UNIT = ?, " +
                                      " PRECISION = ?, MIN_CARDINALITY = ?, MAX_CARDINALITY = ?, " +
                                      " MIN_VALUE = ?, MAX_VALUE = ? " +
                                      " where IS_IA_ATTRIBUTE_TERM_ID = ?");
      else
        pstmt = con.prepareStatement ("update ode_instance_attribute " +
                                      "set  " +
                                      " VALUE_TYPE_ID = ?, " +
                                      " MEASUREMENT_UNIT = ?, " +
                                      " `PRECISION` = ?, MIN_CARDINALITY = ?, MAX_CARDINALITY = ?, " +
                                      " MIN_VALUE = ?, MAX_VALUE = ? " +
                                      " where IS_IA_ATTRIBUTE_TERM_ID = ?");

      pstmt.setInt    (1, valueType);
      if (measurementUnit == null || (measurementUnit = measurementUnit.trim()).equals(""))
        pstmt.setNull   (2, Types.VARCHAR);
      else
        pstmt.setString (2, measurementUnit);
      if (precision == null || (precision = precision.trim()).equals(""))
        pstmt.setNull   (3, Types.VARCHAR);
      else
        pstmt.setString (3, precision);
      pstmt.setInt    (4, minCardinality);
      pstmt.setInt    (5, maxCardinality);
      if(minValue==null)
        pstmt.setNull (6, Types.VARCHAR);
      else
        pstmt.setString (6, minValue);
      if(maxValue==null)
        pstmt.setNull (7, Types.VARCHAR);
      else
        pstmt.setString (7, maxValue);
      pstmt.setInt    (8, attrId);
      // Update
      pstmt.executeUpdate();

      // Commit changes.
      con.commit();
    } catch (SQLException sqle) {
      // Rollback transaction
      con.rollback();
      throw sqle;
    } finally {
      if (pstmt != null) pstmt.close();
      con.setAutoCommit (true);
    }
  }
}
