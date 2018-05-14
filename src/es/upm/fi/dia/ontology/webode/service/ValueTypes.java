package es.upm.fi.dia.ontology.webode.service;

import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.webode.service.WebODEException;
import es.upm.fi.dia.ontology.webode.service.util.SQLUtil;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This interface defines the valid values for the class and instance
 * attributes.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public class ValueTypes
{
  public static final int XML_SCHEMA_DATATYPE = 1;
  public static final int STRING              = 2;
  public static final int INTEGER             = 3;
  public static final int CARDINAL            = 4;
  public static final int FLOAT               = 5;
  public static final int BOOLEAN             = 6;
  public static final int DATE                = 7;
  public static final int RANGE               = 8;
  public static final int URL                 = 9;
  public static final int ENUMERATED          = 10;

  public static final String[] NAMES = { "XML Schema Datatype",
                                         "String",
                                         "Integer",
                                         "Cardinal",
                                         "Float",
                                         "Boolean",
                                         "Date",
                                         "Range",
                                         "URL",
                                         "Enumerated"
                                       };

  public int getValueType(DBConnection conn, String ontology, String type) throws SQLException, WebODEException {
    if(type==null || (type=type.trim()).length()==0)
      throw new WebODEException("Value type name must not be empty");
    if(ontology!=null)
     ontology=ontology.trim();

    int ontology_id=SQLUtil.getOntologyId(conn, ontology);
    PreparedStatement pstmt=null;
    ResultSet rset=null;
    try {
      pstmt = conn.prepareStatement ("select type.value_type " +
                                     "from ode_value_type type " +
                                     "where " +
                                     "  (type.ontology_id = ? or type.ontology is null) and " +
                                     "  type.name = ?");
      if(rset.next())
        return rset.getInt(1);
      else
        return -1;
    }
    finally {
      if(rset!=null) rset.close();
      if(pstmt!=null) pstmt.close();
    }
  }
}