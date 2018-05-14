package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;

/**
 * A descriptor class for the class attributes.
 *
 * @author  Julio César Arpírez Vega
 * @author  David Manzano-Macho
 * @version 1.3
 */
public class ClassAttributeDescriptor extends Attribute
    implements java.io.Serializable, TermTypes
{
  /**
   * Constructor for the class attribute descriptor.
   *
   * @param name     The class attribute name.
   * @param termName The term's name.
   * @param valueType The value type as defined in <tt>ValueTypeDescriptor</tt>.
   * @param xmlSchemaDatatype  The specific XML Schema datatype defined by the user (if any).
   * @param measurementUnit Measurement unit.
   * @param precision Precision.
   * @param minCardinality Minimum cardinality.
   * @param maxCardinality Maximum cardinality.
   * @param description The attribute's description.
   *
   * @see ValueTypeDescriptor
   */


  public ClassAttributeDescriptor (String name, String termName,
                                   String valueTypeName, String measurementUnit,
                                   String precision, int minCardinality, int maxCardinality,
                                   String description) {
    this.name = name;
    this.termName           = termName;
    this.valueTypeName      = valueTypeName;
    this.measurementUnit    = measurementUnit;
    this.precision          = precision;
    this.minCardinality     = minCardinality;
    this.maxCardinality     = maxCardinality;
    this.description        = description;
  }

  protected ClassAttributeDescriptor (String name, String termName, int valueType,
                                   String valueTypeName, String measurementUnit,
                                   String precision, int minCardinality, int maxCardinality,
                                   String description) {
    this.name = name;
    this.termName           = termName;
    this.valueType          = valueType;
    this.valueTypeName      = valueTypeName;
    this.measurementUnit    = measurementUnit;
    this.precision          = precision;
    this.minCardinality     = minCardinality;
    this.maxCardinality     = maxCardinality;
    this.description        = description;
  }

  /**
   * Gets all the class attributes for a given term.
   *
   * @param con The database connection.
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param inheritance Apply inheritance.
   */
  public static ClassAttributeDescriptor[] getClassAttributes (DBConnection con, String ontology, String name)
      throws WebODEException, SQLException {
    return getClassAttributes(con,ontology,name,false);
  }

  /**
   * Gets all the class attributes for a given term.
   *
   * @param con The database connection.
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param inheritance Apply inheritance.
   */
  public static ClassAttributeDescriptor[] getClassAttributes (DBConnection con, String ontology, String name, boolean inheritance)
      throws WebODEException, SQLException {
    if (ontology == null || (ontology = ontology.trim()).equals("") ||
        name == null || (name = name.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      ArrayList lcad=new ArrayList();
      ArrayList concepts=new ArrayList();
      concepts.add(name);
      String concept;
      Term[] parents;
      while(concepts.size()>0) {
        concept=(String)concepts.remove(0);
        if(inheritance) {
          parents=Concept.getParentConcepts(con,ontology,concept);
          for(int i=0; parents!=null && i<parents.length; i++)
            concepts.add(parents[i].term);
        }

        if(con.dbManager!=DBConnection.MYSQL)
          pstmt = con.prepareStatement ("select ca_attribute.name,  ca_concept.name, " +
                                        "   type.value_type, type.name, " +
                                        "   oca.MEASUREMENT_UNIT, " +
                                        "   oca.PRECISION, oca.MIN_CARDINALITY, " +
                                        "   oca.MAX_CARDINALITY, ca_attribute.description " +
                                        " from ode_terms_glossary ca_concept, ode_terms_glossary ca_attribute, " +
                                        "      ode_class_attribute oca, ode_value_type type " +
                                        " where " +
                                        "  ca_concept.name = ? and ca_concept.ontology_id = ? and " +
                                        "  ca_concept.term_id = ca_attribute.parent_id and " +
                                        "  oca.is_ca_attribute_term_id = ca_attribute.term_id and " +
                                        "  oca.value_type_id = type.value_type_id " +
                                        "order by ca_attribute.name");
        else
          pstmt = con.prepareStatement ("select ca_attribute.name,  ca_concept.name, " +
                                        "   type.value_type, type.name, " +
                                        "   oca.MEASUREMENT_UNIT, " +
                                        "   oca.`PRECISION`, oca.MIN_CARDINALITY, " +
                                        "   oca.MAX_CARDINALITY, ca_attribute.description " +
                                        " from ode_terms_glossary ca_concept, ode_terms_glossary ca_attribute, " +
                                        "      ode_class_attribute oca, ode_value_type type " +
                                        " where " +
                                        "  ca_concept.name = ? and ca_concept.ontology_id = ? and " +
                                        "  ca_concept.term_id = ca_attribute.parent_id and " +
                                        "  oca.is_ca_attribute_term_id = ca_attribute.term_id and " +
                                        "  oca.value_type_id = type.value_type_id " +
                                        "order by ca_attribute.name");

        pstmt.setString (1, concept);
        pstmt.setInt    (2, SQLUtil.getOntologyId (con, ontology));
        rset = pstmt.executeQuery ();

        if (rset.next()) {
          do {
            ClassAttributeDescriptor foo;
            lcad.add (foo =
                      new ClassAttributeDescriptor (rset.getString (1), // Attr name
                      rset.getString (2), // Term name
                      rset.getInt(3), // Value type
                      rset.getString(4), // value type name
                      //null, // <<<<<<<<<<ontology
                      rset.getString (5), // Measurement name
                      rset.getString (6), // Precision
                      rset.getInt    (7), // Min cardinality
                      rset.getInt    (8), // Max cardinality
                      rset.getString (9)));// Description

            // Add values
            new ClassAttributeValue (foo).getValues(con, ontology);

          } while (rset.next());
        }

        rset.close();
        rset=null;
        pstmt.close();
        pstmt=null;
      }
      if(lcad.size()>0)
        return (ClassAttributeDescriptor[])lcad.toArray(new ClassAttributeDescriptor[0]);
      else
        return null;
    }
    finally {
      if (rset  != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }


  /**
   * Gets a given class attributes for a given term.
   *
   * @param con The database connection.
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param attribute The attribute's name.
   */
  public static ClassAttributeDescriptor getClassAttribute (DBConnection con, String ontology,
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
      if(con.dbManager!=DBConnection.MYSQL)
        pstmt = con.prepareStatement ("select ca_attribute.name,  ca_concept.name, " +
                                      "   type.value_type, type.name, " +
                                      "   oca.MEASUREMENT_UNIT, " +
                                      "   oca.PRECISION, oca.MIN_CARDINALITY, " +
                                      "   oca.MAX_CARDINALITY, ca_attribute.description " +
                                      "from ode_terms_glossary ca_concept, ode_terms_glossary ca_attribute, " +
                                      "     ode_class_attribute oca, ode_value_type type " +
                                      "where " +
                                      "  ca_concept.name = ? and ca_concept.ontology_id = ? and " +
                                      "  ca_attribute.name = ? and " +
                                      "  ca_concept.term_id = ca_attribute.parent_id and " +
                                      "  oca.is_ca_attribute_term_id = ca_attribute.term_id and " +
                                      "  oca.value_type_id = type.value_type_id");
      else
        pstmt = con.prepareStatement ("select ca_attribute.name,  ca_concept.name, " +
                                      "   type.value_type, type.name, " +
                                      "   oca.MEASUREMENT_UNIT, " +
                                      "   oca.`PRECISION`, oca.MIN_CARDINALITY, " +
                                      "   oca.MAX_CARDINALITY, ca_attribute.description " +
                                      "from ode_terms_glossary ca_concept, ode_terms_glossary ca_attribute, " +
                                      "     ode_class_attribute oca, ode_value_type type " +
                                      "where " +
                                      "  ca_concept.name = ? and ca_concept.ontology_id = ? and " +
                                      "  ca_attribute.name = ? and " +
                                      "  ca_concept.term_id = ca_attribute.parent_id and " +
                                      "  oca.is_ca_attribute_term_id = ca_attribute.term_id and " +
                                      "  oca.value_type_id = type.value_type_id");
      pstmt.setString (1, name);
      pstmt.setInt    (2, SQLUtil.getOntologyId (con, ontology));
      pstmt.setString (3, attribute);
      rset = pstmt.executeQuery ();

      if (rset.next()) {
        ClassAttributeDescriptor foo =
            new ClassAttributeDescriptor (rset.getString (1), // Attr name
            rset.getString (2), // Term name
            rset.getInt    (3), // Value type
            rset.getString (4), // value type name
            //null, // <<<<<<<<<<ontology
            rset.getString (5), // Measurement name
            rset.getString (6), // Precision
            rset.getInt    (7), // Min cardinality
            rset.getInt    (8), // Max cardinality
            rset.getString (9));// Description
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
      throw new WebODEException ("Invalid state and/or parameters.");

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
      Term t = new Term (ontologyName, name, description, CLASS_ATTRIBUTE, relatedId);
      int attrId = t.store (con);

      //System.out.println (relatedId + "@" + attrId + "#" + valueType);

      // Now insert the attribute's properties.
      if(con.dbManager!=DBConnection.MYSQL)
        pstmt = con.prepareStatement ("insert into ode_class_attribute " +
                                      "(IS_CA_ATTRIBUTE_TERM_ID, " +
                                      " VALUE_TYPE_ID, " +
                                      " MEASUREMENT_UNIT, " +
                                      " PRECISION, MIN_CARDINALITY, MAX_CARDINALITY " +
                                      ") values (?, ?, ?, ?, ?, ?)");
      else
        pstmt = con.prepareStatement ("insert into ode_class_attribute " +
                                      "(IS_CA_ATTRIBUTE_TERM_ID, " +
                                      " VALUE_TYPE_ID, " +
                                      " MEASUREMENT_UNIT, " +
                                      " `PRECISION`, MIN_CARDINALITY, MAX_CARDINALITY " +
                                      ") values (?, ?, ?, ?, ?, ?)");
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
      Term t = new Term (ontologyName, name, description, CLASS_ATTRIBUTE,
                         SQLUtil.getTermId (con, ontologyName, parent, TermTypes.CONCEPT));
      int attrId = t.update (con, attribute, parent);
     /*int ontologyId = -1;
     if (ontology != null && (ontology = ontology.trim()).equals(""))
  ontologyId = SQLUtil.getOntologyId (con, ontologyName);*/

      // Now update the attribute's properties.
      if(con.dbManager!=DBConnection.MYSQL)
        pstmt = con.prepareStatement ("update ode_class_attribute " +
                                      "set  " +
                                      " VALUE_TYPE_ID = ?, " +
                                      " MEASUREMENT_UNIT = ?, " +
                                      " PRECISION = ?, MIN_CARDINALITY = ?, MAX_CARDINALITY = ?" +
                                      " where IS_CA_ATTRIBUTE_TERM_ID = ?");
      else
        pstmt = con.prepareStatement ("update ode_class_attribute " +
                                      "set  " +
                                      " VALUE_TYPE_ID = ?, " +
                                      " MEASUREMENT_UNIT = ?, " +
                                      " `PRECISION` = ?, MIN_CARDINALITY = ?, MAX_CARDINALITY = ?" +
                                      " where IS_CA_ATTRIBUTE_TERM_ID = ?");

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
     pstmt.setInt    (6, attrId);

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
    }
  }
}
