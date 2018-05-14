package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.webode.service.util.*;

/**
 * Descriptor for constants.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public class ConstantDescriptor implements java.io.Serializable, TermTypes
{
  /** Constant's name. */
  public String name;

  /** Description. */
  public String description;

  /** Value type. */
  public int valueType;
  public String valueTypeName;

  /** Measurement unit. */
  public String measurementUnit;

  /** Value. */
  public String value;

  public ConstantDescriptor (String name, String description, String valueTypeName,
                             String measurementUnit, String value)
  {
    this.name   = name;
    this.description  = description;
    this.valueTypeName    = valueTypeName;
    this.measurementUnit = measurementUnit;
    this.value = value;
  }

  protected ConstantDescriptor (String name, String description, int valueType, String valueTypeName,
                                String measurementUnit, String value)
  {
    this.name   = name;
    this.description  = description;
    this.valueTypeName    = valueTypeName;
    this.valueType    = valueType;
    this.measurementUnit = measurementUnit;
    this.value = value;
  }

  /**
   * Stores the constant.
   */
  public void store (DBConnection con, String ontology) throws WebODEException, SQLException
  {
    if (name == null || (name = name.trim()).equals("") ||
        measurementUnit == null || (measurementUnit = measurementUnit.trim()).equals("") ||
        value == null || (value = value.trim()).equals(""))
      throw new WebODEException ("Invalid parameters");

    int typeId=SQLUtil.getValueTypeId(con, ontology, this.valueTypeName);
    if(typeId==-1)
      throw new WebODEException("The value type '" + this.valueTypeName + "' doesn't exists");
    this.valueType=typeId;

    PreparedStatement pstmt = null;
    try {
      // Transaction
      con.setAutoCommit (false);

      // Store the term
      int termId = new Term (ontology, name, description, CONSTANT).store (con);

      // If measurement unit is not present, add it.
      int measId = SQLUtil.getTermId (con, ontology, measurementUnit, TermTypes.CONCEPT);
      if (measId < 0)
        measId = SQLUtil.getTermId (con, ontology, measurementUnit, IMPORTED_TERM + CONCEPT);

      // Not present, store it.
      if (measId < 0)
        measId = new Term (ontology, measurementUnit, "", CONCEPT).store (con);

      // Store additional information
      pstmt = con.prepareStatement ("insert into ode_constant (term_id, value_type_id, " +
                                    " measurement_unit_id, value) values (?, ?, ?, ?)");
      pstmt.setInt    (1, termId);
      pstmt.setInt    (2, valueType);
      pstmt.setInt    (3, measId);
      pstmt.setString (4, value);

      pstmt.executeUpdate();

      // Commit changes
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
   * Gets a constant descriptor.
   */
  public static ConstantDescriptor getConstant (DBConnection con, String ontology, String name)
      throws SQLException, WebODEException
  {
    Term term = Term.getTerm (con, ontology, name);

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select type.value_type, type.name, tg2.name, c.value " +
                                    "from ode_terms_glossary tg1, ode_terms_glossary tg2, ode_constant c, " +
                                    "     ode_ontology oo, ode_value_type type " +
                                    "where " +
                                    "  c.term_id = tg1.term_id and " +
                                    "  tg1.name = ? and " +
                                    "  c.measurement_unit_id = tg2.term_id and " +
                                    "  tg1.ontology_id = oo.ontology_id and " +
                                    "  oo.name = ? and " +
                                    "  c.value_type_id = type.value_type_id");
      pstmt.setString (1, name);
      pstmt.setString (2, ontology);
      rset = pstmt.executeQuery();

      if (rset.next()) {
        return new ConstantDescriptor (name, term.des, rset.getInt(1), rset.getString (2),
                                       rset.getString (3), rset.getString (4));

      }
      else
        return null;
    } finally {
      if (rset  != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /**
   * Gets all the constant descriptors.
   */
  public static ConstantDescriptor[] getConstants (DBConnection con, String ontology)
      throws SQLException, WebODEException
  {
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select tg1.name, tg1.description, type.value_type, type.name, tg2.name, c.value " +
                                    "from ode_terms_glossary tg1, " +
                                    "     ode_terms_glossary tg2, " +
                                    "     ode_constant c, " +
                                    "     ode_ontology oo, " +
                                    "     ode_value_type type " +
                                    "where " +
                                    "  oo.name = ? and " +
                                    "  oo.ontology_id = tg1.ontology_id and " +
                                    "  tg1.term_id = c.term_id and " +
                                    "  c.measurement_unit_id = tg2.term_id and " +
                                    "  c.value_type_id = type.value_type_id");

      pstmt.setString (1, ontology);
      rset = pstmt.executeQuery();

      if (rset.next()) {
        Vector v = new Vector (20);
        do {
          v.addElement (new ConstantDescriptor (rset.getString (1), rset.getString (2),
                                                rset.getInt(3), rset.getString(4),
                                                rset.getString (5), rset.getString (6)));
        } while (rset.next());
        ConstantDescriptor[] acd = new ConstantDescriptor[v.size()];
        v.copyInto (acd);
        return acd;
      }
      else
        return null;
    } finally {
      if (rset  != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }


  /**
   * Update the constant.
   */
  public void update (DBConnection con, String ontology, String originalName)
      throws WebODEException, SQLException
  {
    if (name == null || (name = name.trim()).equals("") ||
        measurementUnit == null || (measurementUnit = measurementUnit.trim()).equals("") ||
        value == null || (value = value.trim()).equals("") ||
        originalName == null || (originalName = originalName.trim()).equals(""))
      throw new WebODEException ("Invalid parameters");

    int typeId=SQLUtil.getValueTypeId(con, ontology, this.valueTypeName);
    if(typeId==-1)
      throw new WebODEException("The value type '" + this.valueTypeName + "' doesn't exists");
    this.valueType=typeId;

    PreparedStatement pstmt = null;
    try {
      con.setAutoCommit (false);

      // If measurement unit is not present, add it.
      int measId = SQLUtil.getTermId (con, ontology, measurementUnit, TermTypes.CONCEPT);
      if (measId < 0)
        measId = SQLUtil.getTermId (con, ontology, measurementUnit, IMPORTED_TERM + CONCEPT);

      // Not present, store it.
      if (measId < 0)
        measId = new Term (ontology, measurementUnit, "", CONCEPT).store (con);

      int termId = new Term (ontology, name, description, TermTypes.CONSTANT).update (con, originalName);

      pstmt = con.prepareStatement ("update ode_constant set value_type_id = ?, " +
                                    "  measurement_unit_id = ?, value = ? where term_id = ?");
      pstmt.setInt    (1, valueType);
      pstmt.setInt    (2, measId);
      pstmt.setString (3, value);
      pstmt.setInt    (4, termId);

      pstmt.executeUpdate();

      // Commit changes
      con.commit();
    } catch (SQLException sqle) {
      con.rollback();
      throw sqle;
    } finally {
      if (pstmt != null) pstmt.close();
      con.setAutoCommit (true);
    }
  }
}
