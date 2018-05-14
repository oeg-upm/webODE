package es.upm.fi.dia.ontology.webode.service;

import java.util.HashSet;

public class Attribute implements java.io.Serializable {
  /** The class attribute name. */
  public String name;

  /** The term's name. */
  public String termName;

  /**
   * The value type as defined in <tt>ValueTypeDescriptor</tt>
   *
   * @see ValueTypeDescriptor
   */
  public int valueType;

    /* The ontology the attribute is class of (if any). */
  //public String ontology;

  /** The specific value type name. It use for the XML Schema datatype and Enumerated Type */
  public String valueTypeName;

  /** Measurement unit. */
  public String measurementUnit;

  /** Precision. */
  public String precision;

  /** Minimum cardinality. */
  public int minCardinality;

  /** Maximum cardinality. */
  public int maxCardinality;

  /** The attribute's description. */
  public String description;

  /** The attribute values. <tt>null</tt> if no values are present. */
  public String[] values;

  public static HashSet xsd_namespaces=null;

  private static void initNameSpaces() {
    xsd_namespaces=new HashSet();
    xsd_namespaces.add("http://www.w3.org/2001/XMLSchema#");
    xsd_namespaces.add("http://www.w3.org/2000/10/XMLSchema#");
  }

  public String getValueName () {
    return valueTypeName;
  }

  public static String getXMLSchemaDataTypeName(String dataTypeName) {
    if(xsd_namespaces==null)
      initNameSpaces();

    String typeName=null;
    int index= dataTypeName.indexOf('#');
    String namespace=dataTypeName.substring(0,index+1);
    if (xsd_namespaces.contains(namespace))
      typeName=dataTypeName.substring(index+1);
    return typeName;
  }

  public static boolean isXMLSchemaDataTypeName(String dataTypeName) {
    return getXMLSchemaDataTypeName(dataTypeName)!=null;
  }
}