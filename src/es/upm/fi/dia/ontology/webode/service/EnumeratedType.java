package es.upm.fi.dia.ontology.webode.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;
import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.webode.service.util.SQLUtil;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class EnumeratedType extends ValueTypes implements Serializable {
  private static final String SEQ_ENUM = "ode_sequence_value_type_id";
  private static final String SEQ_ENUM_VAL = "ode_sequence_enum_value_id";
  public String ontology;
  public String name;
  public String description;
  public String[] values;

  private static String getDescription(DBConnection conn, int ontology_id, String name) throws SQLException, WebODEException {
    PreparedStatement pstmt=null;
    ResultSet rset=null;

    pstmt=conn.prepareStatement("select type.description "+
                                "from ode_value_type type "+
                                "where"+
                                " type.name= ? and"+
                                " type.ontology_id=?");

    pstmt.setInt(2, ontology_id);
    pstmt.setString(1, name);
    rset=pstmt.executeQuery();
    rset.next();
    String description=rset.getString(1);
    pstmt.close();

    return description;
  }

  public EnumeratedType(String ontology, String name, String description, String[] values) {
    this.ontology=ontology;
    this.name=name;
    this.values=values;
    this.description=description;
  }

  public boolean contains(String value) {
    boolean result=false;
    for(int i=0;!result&&i<values.length;i++) {
      result=values[i].equals(value);
    }
    return result;
  }

  static EnumeratedType getEnumeratedType(DBConnection conn, String ontology, int value_type) throws SQLException, WebODEException {
    if(ontology==null || (ontology=ontology.trim()).length()==0)
      throw new WebODEException("Ontology name must not be empty");

    int ontology_id=SQLUtil.getOntologyId(conn, ontology);
    if(ontology_id==-1)
      return null;

    PreparedStatement pstmt=null;
    ResultSet rset=null;
    try {
      pstmt=conn.prepareStatement("select type.name, val.value " +
                                  "from ode_value_type type, ode_value val " +
                                  "where " +
                                  "  type.ontology_id = ? and " +
                                  "  type.value_type_id = ? and " +
                                  "  type.value_type_id = val.value_type_id " +
                                  "order by val.value");
      pstmt.setInt(1, ontology_id);
      pstmt.setInt(2, value_type);
      rset=pstmt.executeQuery();
      HashSet values=new HashSet();
      String name=null;
      while(rset.next()) {
        if(name==null) name=rset.getString(1);
        values.add(rset.getString(2));
      }

      String description=getDescription(conn,ontology_id,name);

      if(values.size()!=0)
        return new EnumeratedType(ontology, name, description, (String[])values.toArray(new String[0]));
      else
        return null;
    }
    finally {
      if(rset!=null) rset.close();
      if(pstmt!=null) pstmt.close();
    }
  }

  public static EnumeratedType getEnumeratedType(DBConnection conn, String ontology, String name) throws SQLException, WebODEException {
    if(ontology==null || (ontology=ontology.trim()).length()==0)
      throw new WebODEException("Ontology name must not be empty");
    if(name==null || (name=name.trim()).length()==0)
      throw new WebODEException("Enumerated value type name must not be empty");

    int ontology_id=SQLUtil.getOntologyId(conn, ontology);
    if(ontology_id==-1)
      return null;

    PreparedStatement pstmt=null;
    ResultSet rset=null;
    try {
      pstmt=conn.prepareStatement("select val.value " +
                                  "from ode_value_type type, ode_value val " +
                                  "where " +
                                  "  type.ontology_id = ? and " +
                                  "  type.name = ? and " +
                                  "  type.value_type_id = val.value_type_id " +
                                  "order by val.value");
      pstmt.setInt(1, ontology_id);
      pstmt.setString(2, name);
      rset=pstmt.executeQuery();
      HashSet values=new HashSet();
      while(rset.next())
        values.add(rset.getString(1));

      String description=getDescription(conn,ontology_id,name);

      if(values.size()!=0)
        return new EnumeratedType(ontology, name, description, (String[])values.toArray(new String[0]));
      else
        return null;
    }
    finally {
      if(rset!=null) rset.close();
      if(pstmt!=null) pstmt.close();
    }
  }

  public static EnumeratedType[] getEnumeratedTypes(DBConnection conn, String ontology) throws SQLException, WebODEException {
    if(ontology==null || (ontology=ontology.trim()).length()==0)
      throw new WebODEException("Ontology name must not be empty");

    int ontology_id=SQLUtil.getOntologyId(conn, ontology);
    if(ontology_id==-1)
      return null;

    PreparedStatement pstmt=null;
    ResultSet rset=null;
    try {
      pstmt=conn.prepareStatement("select type.name, val.value " +
                                  "from ode_value_type type, ode_value val " +
                                  "where " +
                                  "  type.ontology_id = ? and " +
                                  "  type.value_type_id = val.value_type_id " +
                                  "order by type.name, val.value");
      pstmt.setInt(1, ontology_id);
      rset=pstmt.executeQuery();
      HashSet values=new HashSet();
      ArrayList enums=new ArrayList();
      String last, name=null;
      last=null;
      if(rset.next()) {
        do {
//          System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//          System.out.println("Antes:");
//          System.out.println("  Name: "+name);
//          System.out.println("  Last: "+last);
          last=name;
          name=rset.getString(1);
//          System.out.println("Despues ["+rset.getString(2)+"]");
//          System.out.println("  Name: "+name);
//          System.out.println("  Last: "+last);
          if(last!=null && !last.equals(name)) {
            String description=getDescription(conn,ontology_id,last);
//            System.out.println("Descripcion: "+description);
            enums.add(new EnumeratedType(ontology, last, description, (String[])values.toArray(new String[0])));
//            System.out.println(" -> Hay creacion {"+last+", "+description+", "+values+"}");
            values=new HashSet();
          }
          values.add(rset.getString(2));
//          System.out.println(" -> Hay extension {"+rset.getString(2)+"}");

        }
        while(rset.next());
        if(!values.isEmpty()) {
          String description=getDescription(conn,ontology_id,name);
//          System.out.println("Descripcion: "+description);
//          System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
          enums.add(new EnumeratedType(ontology, name, description, (String[])values.toArray(new String[0])));
//          System.out.println(" -> Hay creacion {"+name+", "+description+", "+values+"}");
        }
      }
      if(enums.size()>0)
        return (EnumeratedType[])enums.toArray(new EnumeratedType[0]);
      else
        return null;
    }
    finally {
      if(rset!=null) rset.close();
      if(pstmt!=null) pstmt.close();
    }
  }

  public void remove(DBConnection conn) throws SQLException, WebODEException {
    if(ontology==null || (ontology=ontology.trim()).length()==0)
      throw new WebODEException("Ontology name must not be empty");
    if(name==null || (name=name.trim()).length()==0)
      throw new WebODEException("Enumerated value type name must not be empty");

    int ontology_id=SQLUtil.getOntologyId(conn, ontology);
    if(ontology_id==-1)
      throw new WebODEException("Ontology '" + ontology + "' not found in the server.");

    PreparedStatement pstmt=null;
    try {
      pstmt=conn.prepareStatement("delete ode_value_type where ontology_id = ? and name = ?");
      pstmt.setInt(1, ontology_id);
      pstmt.setString(2, name);
      pstmt.executeUpdate();
    }
    finally {
      if(pstmt!=null) pstmt.close();
    }
  }

  public void store(DBConnection conn) throws SQLException, WebODEException {
    if(ontology==null || (ontology=ontology.trim()).length()==0)
      throw new WebODEException("Ontology name must not be empty");
    if(name==null || (name=name.trim()).length()==0)
      throw new WebODEException("Enumerated value type name must not be empty");

//    System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//    System.out.println("Ontologia: "+ontology);

    int ontology_id=SQLUtil.getOntologyId(conn, ontology);
    if(ontology_id==-1)
      throw new WebODEException("Ontology '" + ontology + "' not found in the server.");

    int type_id=SQLUtil.getValueTypeId(conn, ontology, name);
    if(type_id!=-1)
      throw new WebODEException("There is already the type '" + name + "' in the ontology '" + ontology + "'");

    type_id=SQLUtil.nextVal(conn, SEQ_ENUM);

    PreparedStatement pstmt=null;
    try {
      pstmt=conn.prepareStatement("insert into ode_value_type (value_type_id, name, description, ontology_id) values (?, ?, ?, ?)");
      pstmt.setInt(1, type_id);
      pstmt.setString(2, name);
      pstmt.setString(3, description);
      pstmt.setInt(4, ontology_id);
      pstmt.executeUpdate();

      pstmt.close();
      pstmt=null;

      for(int i=0; values!=null && i<values.length; i++) {
        pstmt=conn.prepareStatement("insert into ode_value (value_id, value_type_id, value) values (?, ?, ?)");
        pstmt.setInt(1, SQLUtil.nextVal(conn, SEQ_ENUM_VAL));
        pstmt.setInt(2, type_id);
        pstmt.setString(3, values[i]);
        pstmt.executeUpdate();
        pstmt.close();
        pstmt=null;
      }
    }
    finally {
      if(pstmt!=null) pstmt.close();
    }
  }

  public void update(DBConnection conn, String old_name) throws SQLException, WebODEException {
    if(ontology==null || (ontology=ontology.trim()).length()==0)
      throw new WebODEException("Ontology name must not be empty");
    if(name==null || (name=name.trim()).length()==0)
      throw new WebODEException("Enumerated value type name must not be empty");

    int ontology_id=SQLUtil.getOntologyId(conn, ontology);
    if(ontology_id==-1)
      throw new WebODEException("Ontology '" + ontology + "' not found in the server.");

    int type_id=SQLUtil.getValueTypeId(conn, ontology, old_name);
    if(type_id==-1)
      throw new WebODEException("The enumerated type '" + name + "' doesn't exist in the ontology '" + ontology + "'");
    EnumeratedType old=getEnumeratedType(conn, ontology, old_name);


    PreparedStatement pstmt=null;
    try {
        pstmt=conn.prepareStatement("update ode_value_type set name = ?, description = ? where value_type_id = ?");
        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.setInt(3, type_id);
        pstmt.execute();
    }
    finally {
      if(pstmt!=null) pstmt.close();
    }
  }

  public static void updateValue(DBConnection conn, String ontology, String name, String oldValue, String newValue) throws SQLException, WebODEException {
    if(ontology==null || (ontology=ontology.trim()).length()==0)
      throw new WebODEException("Ontology name must not be empty");
    if(name==null || (name=name.trim()).length()==0)
      throw new WebODEException("Enumerated value type name must not be empty");

    int type_id=SQLUtil.getValueTypeId(conn, ontology, name);
    if(type_id==-1)
      throw new WebODEException("The enumerated type '" + name + "' doesn't exist in the ontology '" + ontology + "'");

    int value_id=SQLUtil.getEnumeratedValueId(conn, ontology, name,oldValue);
    if(value_id==-1)
      throw new WebODEException("The enumerated value '" + oldValue + "' doesn't exist in the type '" + name + "' of the ontology '"+ontology+"'");

    EnumeratedType type=getEnumeratedType(conn,ontology,name);
    if(type.contains(newValue))
      throw new WebODEException("The enumerated type '"+name+"' already contains a value named '"+newValue+"'");

    PreparedStatement pstmt=null;
    try {
      pstmt=conn.prepareStatement("update ode_value set value = ? where value_type_id=? and value_id=?");
      pstmt.setString(1, newValue);
      pstmt.setInt(2, type_id);
      pstmt.setInt(3, value_id);
      pstmt.executeUpdate();

      pstmt.close();
      pstmt=null;

      if(conn.dbManager==DBConnection.ORACLE) {
        pstmt=conn.prepareStatement("update ode_instance_value " +
                                    "set value = ? " +
                                    "where " +
                                    "  attribute_id in ( " +
                                    "    select iatt.is_ia_attribute_term_id " +
                                    "    from " +
                                    "      ode_instance_attribute iatt " +
                                    "    where " +
                                    "      iatt.value_type_id = ? " +
                                    "   ) and " +
                                    "   value = ?");
        pstmt.setString(1, newValue);
        pstmt.setInt(2, type_id);
        pstmt.setString(3, oldValue);
        pstmt.executeUpdate();

        pstmt.close();
        pstmt=null;

        pstmt=conn.prepareStatement("update ode_concept_attribute_value " +
                                    "set value = ? " +
                                    "where " +
                                    "  attribute_id in ( " +
                                    "    select iatt.is_ia_attribute_term_id " +
                                    "    from " +
                                    "      ode_instance_attribute iatt " +
                                    "    where " +
                                    "      iatt.value_type_id = ? " +
                                    "   ) and " +
                                    "   value = ?");
        pstmt.setString(1, newValue);
        pstmt.setInt(2, type_id);
        pstmt.setString(3, oldValue);
        pstmt.executeUpdate();

        pstmt=conn.prepareStatement("update ode_concept_attribute_value " +
                                    "set value = ? " +
                                    "where " +
                                    "  attribute_id in ( " +
                                    "    select catt.is_ca_attribute_term_id " +
                                    "    from " +
                                    "      ode_class_attribute catt " +
                                    "    where " +
                                    "      catt.value_type_id = ? " +
                                    "   ) and " +
                                    "   value = ?");
        pstmt.setString(1, newValue);
        pstmt.setInt(2, type_id);
        pstmt.setString(3, oldValue);
        pstmt.executeUpdate();
      }
      else {
        pstmt=conn.prepareStatement("update ode_instance_value val, " +
                                    "       ode_terms_glossary att, " +
                                    "       ode_instance_attribute iatt " +
                                    "set val.value = ? " +
                                    "where " +
                                    "  iatt.value_type_id = ? and " +
                                    "  iatt.is_ia_attribute_term_id=att.term_id and " +
                                    "  att.term_id=val.attribute_id and " +
                                    "  att.term_id = val.attribute_id and " +
                                    "  val.value = ?");
        pstmt.setString(1, newValue);
        pstmt.setInt(2, type_id);
        pstmt.setString(3, oldValue);
        pstmt.executeUpdate();

        pstmt.close();
        pstmt=null;

        pstmt=conn.prepareStatement("update ode_concept_attribute_value val, " +
                                    "       ode_terms_glossary att, " +
                                    "       ode_instance_attribute iatt " +
                                    "set val.value = ? " +
                                    "where " +
                                    "  iatt.value_type_id = ? and " +
                                    "  iatt.is_ia_attribute_term_id=att.term_id and " +
                                    "  att.term_id=val.attribute_id and " +
                                    "  att.term_id = val.attribute_id and " +
                                    "  val.value = ?");
        pstmt.setString(1, newValue);
        pstmt.setInt(2, type_id);
        pstmt.setString(3, oldValue);
        pstmt.executeUpdate();

        pstmt.close();
        pstmt=null;

        pstmt=conn.prepareStatement("update ode_concept_attribute_value val, " +
                                    "       ode_terms_glossary att, " +
                                    "       ode_class_attribute catt " +
                                    "set val.value = ? " +
                                    "where " +
                                    "  catt.value_type_id = ? and " +
                                    "  catt.is_ia_attribute_term_id=att.term_id and " +
                                    "  att.term_id=val.attribute_id and " +
                                    "  att.term_id = val.attribute_id and " +
                                    "  val.value = ?");
        pstmt.setString(1, newValue);
        pstmt.setInt(2, type_id);
        pstmt.setString(3, oldValue);
        pstmt.executeUpdate();
      }
    }
    finally {
      if(pstmt!=null) pstmt.close();
    }
  }

  public static void addValue(DBConnection conn, String ontology, String name, String value) throws SQLException, WebODEException {
    if(ontology==null || (ontology=ontology.trim()).length()==0)
      throw new WebODEException("Ontology name must not be empty");
    if(name==null || (name=name.trim()).length()==0)
      throw new WebODEException("Enumerated value type name must not be empty");
    if(value==null || (value=value.trim()).length()==0)
      throw new WebODEException("Enumerated value name must not be empty");

    int type_id=SQLUtil.getValueTypeId(conn, ontology, name);
    if(type_id==-1)
      throw new WebODEException("The enumerated type '" + name + "' doesn't exist in the ontology '" + ontology + "'");

    EnumeratedType type=getEnumeratedType(conn,ontology,name);

    if(type.contains(value))
      throw new WebODEException("The enumerated type '"+name+"' already contains a value named '"+value+"'");

    PreparedStatement pstmt=null;
    try {
      pstmt=conn.prepareStatement("insert into ode_value (value_id, value_type_id, value) values (?, ?, ?)");
      int key = SQLUtil.nextVal(conn, SEQ_ENUM_VAL);
      pstmt.setInt(1, key);
      pstmt.setInt(2, type_id);
      pstmt.setString(3, value);
      pstmt.executeUpdate();
    }
    finally {
      if(pstmt!=null) pstmt.close();
    }
  }

  public static void removeValue(DBConnection conn, String ontology, String name, String value) throws SQLException, WebODEException {
    if(ontology==null || (ontology=ontology.trim()).length()==0)
      throw new WebODEException("Ontology name must not be empty");
    if(name==null || (name=name.trim()).length()==0)
      throw new WebODEException("Enumerated value type name must not be empty");
    if(value==null || (value=value.trim()).length()==0)
      throw new WebODEException("Enumerated value name must not be empty");

    int type_id=SQLUtil.getValueTypeId(conn, ontology, name);
    if(type_id==-1)
      throw new WebODEException("The enumerated type '" + name + "' doesn't exist in the ontology '" + ontology + "'");

    int value_id=SQLUtil.getEnumeratedValueId(conn, ontology, name,value);
    if(value_id==-1)
      throw new WebODEException("The enumerated value '" + value + "' doesn't exist in the type '" + name + "' of the ontology '"+ontology+"'");

    PreparedStatement pstmt=null;
    try {
      pstmt=conn.prepareStatement("delete from ode_value where value_id=? and value_type_id=?");
      pstmt.setInt(1, value_id);
      pstmt.setInt(2, type_id);
      pstmt.executeUpdate();
    }
    finally {
      if(pstmt!=null) pstmt.close();
    }
  }
}