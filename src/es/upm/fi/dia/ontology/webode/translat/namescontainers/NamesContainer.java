package es.upm.fi.dia.ontology.webode.translat.namescontainers;

import java.util.*;

//DOM related imports

public abstract class NamesContainer
{
  protected String uri;
  protected Hashtable types_names_table;
  protected Hashtable elements_numbers_table;
  protected Hashtable elements_table;
  protected Hashtable names_table;
  protected Hashtable datatypes_table;
  protected Hashtable well_known_namespaces;
  public final String DAMLOIL_NAMESPACE="http://www.daml.org/2001/03/daml+oil#";
  public final String RDFS_NAMESPACE="http://www.w3.org/2000/01/rdf-schema#";
  public final String RDF_NAMESPACE="http://www.w3.org/1999/02/22-rdf-syntax-ns#";
  public final String OWL_NAMESPACE="http://www.w3.org/2002/07/owl#";
//	public final String XSD_NAMESPACE="http://www.w3.org/2001/01/XMLSchema#";
  public final String TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
  public final String LIST_NIL = this.RDF_NAMESPACE+"nil";
  public final String LIST_REST = this.RDF_NAMESPACE+"rest";
  public final String LIST_FIRST = this.RDF_NAMESPACE+"first";
  public final String LIST_TYPE = this.RDF_NAMESPACE+"rest";

  public final String default_namespace="http://webode.dia.fi.upm.es/ontology#";
  public  String namespace;
  private final String rdfs_type = "RDFS";
  private HashSet xsd_namespaces=null;

  public final String NAMESPACE_IDENTIFIER= "NAMESPACE_IDENTIFIER";

  public NamesContainer ()
  {
    uri="Uknown URI";
    namespace = default_namespace;
    types_names_table = new Hashtable();
    elements_numbers_table = new Hashtable();
    elements_table = new Hashtable();
    names_table = new Hashtable();
    datatypes_table = new Hashtable();
    well_known_namespaces = new Hashtable();

    xsd_namespaces=new HashSet();
    xsd_namespaces.add("http://www.w3.org/2001/XMLSchema#");
    xsd_namespaces.add("http://www.w3.org/2000/10/XMLSchema#");

    well_known_namespaces.put(this.OWL_NAMESPACE,"owl");
    well_known_namespaces.put(this.RDF_NAMESPACE,"rdf");
    well_known_namespaces.put(this.DAMLOIL_NAMESPACE,"daml");
    well_known_namespaces.put(this.RDFS_NAMESPACE,"rdfs");
    well_known_namespaces.put("http://www.w3.org/2001/XMLSchema#","xsd");
    well_known_namespaces.put("http://www.w3.org/2000/10/XMLSchema#","xsd");

  }


  public NamesContainer (String ontology_namespace)
  {
//	System.out.println("Creating the namescontainer ");
//	System.out.println("    URI: "+ontology_uri);
//	System.out.println("	Namespaces: "+ontology_namespace);
//	uri=ontology_uri;
    if (ontology_namespace!=null)
      namespace = ontology_namespace;
    else
      namespace = default_namespace;

    types_names_table = new Hashtable();
    elements_numbers_table = new Hashtable();
    elements_table = new Hashtable();
    names_table = new Hashtable();
    datatypes_table = new Hashtable();
    xsd_namespaces=new HashSet();
    well_known_namespaces = new Hashtable();
    xsd_namespaces.add("http://www.w3.org/2001/XMLSchema#");
    xsd_namespaces.add("http://www.w3.org/2000/10/XMLSchema#");

    well_known_namespaces.put(this.OWL_NAMESPACE,"owl");
    well_known_namespaces.put(this.RDF_NAMESPACE,"rdf");
    well_known_namespaces.put(this.DAMLOIL_NAMESPACE,"daml");
    well_known_namespaces.put(this.RDFS_NAMESPACE,"rdfs");
    well_known_namespaces.put("http://www.w3.org/2001/XMLSchema#","xsd");
    well_known_namespaces.put("http://www.w3.org/2000/10/XMLSchema#","xsd");
  }

  public String getNamespaceIdentifier(String namespace)
  {
    String namespace_id=null;
    if(this.well_known_namespaces.containsKey(namespace))
    {
      namespace_id=((String)this.well_known_namespaces.get(namespace));
    }
    else
    {
      String base_name = (String)types_names_table.get(this.NAMESPACE_IDENTIFIER);
      Integer number = (Integer)elements_numbers_table.get(this.NAMESPACE_IDENTIFIER);

      namespace_id = base_name+(number).toString();
      number = new Integer(number.intValue()+1);

      elements_numbers_table.put(this.NAMESPACE_IDENTIFIER,number);

    }
    return namespace_id;
  }

  public String bindAnonymous (Object element, String type_of_element)
  {
    String base_name = (String)types_names_table.get(type_of_element);
    Integer number = (Integer)elements_numbers_table.get(type_of_element);
    System.out.println(number+"|||||||||"+element+"||||||||||||||||||||||||||||> "+type_of_element);
    String name = base_name+(number).toString();
    number = new Integer(number.intValue()+1);

    elements_table.put(name,element);
    names_table.put(element,name);
    elements_numbers_table.put(type_of_element,number);
//        if (type_of_element.equals(this.)
    return name;
  }

  public void bind (Object element,String element_name)
  {
    try
    {
      elements_table.put(element_name,element);
      names_table.put(element,element_name);
    }
    catch (Exception e)
    {
      e.printStackTrace();

    }
  }

  public Object lookUp (String element_name)
  {
    return (elements_table.get(element_name));
  }

  public String lookUp (Object element)
  {
    System.out.println("][ "+names_table);
    return ((String)names_table.get(element));
  }

  public boolean isBinded(Object element)
  {
    return (elements_table.contains(element));
  }

  public String getDefaultNamespace ()
  {
    return default_namespace;
  }


  public String getOntologyName()
  {
    return uri;
  }

  public String getOntologyNamespace()
  {
    return namespace;
  }

  protected String _getOntologyNamespace(String file_uri)
  {
/*
 String name=null;
 try
     {
  Document doc=null;
  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

     factory.setNamespaceAware(true);

  DocumentBuilder builder = factory.newDocumentBuilder();
  doc = builder.parse(file_uri);
  Element root = doc.getDocumentElement();
  name=root.getAttribute("xmlns");

  if (!root.hasAttribute("xmlns"))
   {
   return default_namespace;
   }

  return name;

     }

 catch (Exception e)
     {
     return default_namespace;
     }

*/
    return null;
  }

  //ESTO PA QUE VALE?
  public Object get (String name)
  {
    return elements_table.get(name);
  }

  public String translateDatatype (String datatype_name)
  {
    String translated_datatype= "String";
    int index= datatype_name.indexOf('#');
    String namespace=datatype_name.substring(0,index+1);
    if (xsd_namespaces.contains(namespace))
      translated_datatype=datatype_name;

    return translated_datatype;
  }

  public boolean isXsdNamespace(String namespace)
  {
    return xsd_namespaces.contains(namespace);
  }

}


