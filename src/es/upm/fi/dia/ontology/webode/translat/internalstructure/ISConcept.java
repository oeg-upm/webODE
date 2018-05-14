package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;

public class ISConcept extends ISStructureElement
{
  private String name; //at least one

  private Hashtable class_attributes;//the rest are cero or many
  private Hashtable instance_attributes;
  private Hashtable outgoing_term_relations;
  private Vector synonyms;
  private Vector related_formulas;
  private Vector abbreviations;
  private Vector related_references;//Contains just the names of the references
  //
  private Vector superclasses;

  public void toWebODE(ODEService ode, String ontologyName) throws java.rmi.RemoteException, WebODEException, java.sql.SQLException, ISException
  {
    try {
      ode.insertTerm(ontologyName,name,description,TermTypes.CONCEPT);

      ISClassAttributeDescriptor catt;
      for(Enumeration enum=class_attributes.elements(); enum.hasMoreElements(); )
      {
        catt=(ISClassAttributeDescriptor)enum.nextElement();
        catt.toWebODE(ode,ontologyName,name);
      }

      ISInstanceAttributeDescriptor iatt;
      for(Enumeration enum=instance_attributes.elements(); enum.hasMoreElements(); )
      {
        iatt=(ISInstanceAttributeDescriptor)enum.nextElement();
        iatt.toWebODE(ode,ontologyName,name);
      }
    }
    finally {
      System.out.println("ode.insertTerm('" + ontologyName + "','" + name + "','" + description + "'," + TermTypes.CONCEPT + ");");
    }
/* falta synonyms, related_formulas, abbreviations, related_references */
  }

  public ISConcept()
  {
    empty=false;
    class_attributes= new Hashtable();
    instance_attributes= new Hashtable();
    synonyms = new Vector();
    related_formulas = new Vector();
    abbreviations= new Vector();
    related_references= new Vector();
    outgoing_term_relations= new Hashtable();


  }

  public ISConcept(String concept_name)
  {
    this();
    name=concept_name;


  }




  public void setName (String concept_name)
  {
    name = concept_name;
  }

  public String getName ()
  {
    return name;
  }




  public ISInstanceAttributeDescriptor getInstanceAttribute (String attr_name)
  {
    return((ISInstanceAttributeDescriptor)instance_attributes.get(attr_name));
  }

  public void addInstanceAttribute(ISInstanceAttributeDescriptor instance_attribute)
  {
    //	System.out.println ("Añado el atributo de instancia "+instance_attribute.getName()+" en "+ getName());
    instance_attributes.put(instance_attribute.getName(),instance_attribute);
  }


  public boolean hasClassAttribute (String attr_name)
  {
    return(class_attributes.containsKey(attr_name));
  }

  public boolean hasInstanceAttribute (String attr_name)
  {
    return(instance_attributes.containsKey(attr_name));
  }

  public boolean hasAttribute (String attr_name)
  {
    return(hasClassAttribute(attr_name)||hasInstanceAttribute(attr_name));
  }




  public ISClassAttributeDescriptor getClassAttribute (String attr_name)
  {
    return((ISClassAttributeDescriptor)class_attributes.get(attr_name));
  }


  public void addClassAttribute(ISClassAttributeDescriptor class_attribute)
  {
    class_attributes.put(class_attribute.getName(),class_attribute);
  }


  public void deleteInstanceAttribute(String attr_name)
  {
    instance_attributes.remove(attr_name);
  }

  public void addSynonym (ISSynonym new_synonym)
  {
    synonyms.add(new_synonym);
  }




  public Node obtainXML (Document owner_document)
  {
    Element concept_element = owner_document.createElement("Concept");

    if (name!=null)
      addElement (owner_document,concept_element,"Name",name);

    if (description!=null)
      addElement (owner_document,concept_element,"Description",description);

    for (Enumeration e = class_attributes.elements() ; e.hasMoreElements() ;)
    {
      concept_element.appendChild(((ISClassAttributeDescriptor) e.nextElement()).obtainXML(owner_document));
    }

    for (Enumeration e = instance_attributes.elements() ; e.hasMoreElements() ;)
    {
      concept_element.appendChild(((ISInstanceAttributeDescriptor) e.nextElement()).obtainXML(owner_document));
    }


    for (Enumeration e = synonyms.elements() ; e.hasMoreElements() ;)
    {
      concept_element.appendChild(((ISSynonym) e.nextElement()).obtainXML(owner_document));
    }

    for (Enumeration e = related_formulas.elements() ; e.hasMoreElements() ;)
      addElement (owner_document,concept_element,"Related-Formula",(String)(e.nextElement()));


    for (Enumeration e = abbreviations.elements() ; e.hasMoreElements() ;)
    {
      concept_element.appendChild(((ISAbbreviation) e.nextElement()).obtainXML(owner_document));
    }

    for (Enumeration e = related_references.elements() ; e.hasMoreElements() ;)
      addElement (owner_document,concept_element,"Related-References",(String)(e.nextElement()));





    return concept_element;
  }

  //-------------------------------------------------------------------------------------------------------

  public boolean hasTermRelation (String term_relation_name)
  {
    return outgoing_term_relations.containsKey(term_relation_name);
  }



  public boolean hasTermRelation (String term_relation_name,String domain_name,String range_name)
  {
    boolean has_term_relation=false;

    if (outgoing_term_relations.containsKey(term_relation_name))
    {
      Vector term_relation_vector=(Vector)
                                  outgoing_term_relations.get(term_relation_name);

      has_term_relation=_hasTermRelation(term_relation_vector,term_relation_name,domain_name,range_name);
    }


    return has_term_relation;
  }

/*
public boolean hasLocalDefinition(String class_name,String prop_name)
{
 return hasClassObjectProperty(class_name,prop_name,class_name,null);
}


public boolean hasGlobalDefinition(String prop_name)
{
 return  object_properties_table.containsKey(prop_name);
}


public ISTermRelation getGlobalDefinition(String prop_name)
{
 return  (ISTermRelation)((ISTermRelation)(object_properties_table.get(prop_name))).clone();
}
*/







  private boolean _hasTermRelation(Vector prop_vector,String prop_name,String domain_name,String range_name)
  {
    boolean has_property=false;

    String destination_name=null;
    try
    {
      Enumeration enu= prop_vector.elements();
      while (enu.hasMoreElements()&&!has_property)
      {
        ISTermRelation term_relation=(ISTermRelation)enu.nextElement();
        destination_name=term_relation.getDestination();
        if (destination_name!=null)
          has_property=destination_name.equals(range_name);
        else
        {
          if (range_name==null)
            has_property= true;
          else
            has_property=false;
        }

      }

    }
    catch (Exception e)
    {

    }
    return has_property;
  }










  public ISTermRelation getTermRelation (String term_relation_name,String domain_name, String range_name)
  {
    ISTermRelation new_term_relation=null;



    Vector term_relation_vector =(Vector) outgoing_term_relations.get(term_relation_name);


    new_term_relation=
        _getFromVector(term_relation_vector,domain_name,range_name);



    return new_term_relation;
  }







  private ISTermRelation _getFromVector(Vector prop_vector,String domain_name,String range_name)
  {
    ISTermRelation term_relation=null;
    boolean finded=false;
    Enumeration enu= prop_vector.elements();
    while (enu.hasMoreElements()&&!finded)
    {
      term_relation=(ISTermRelation)enu.nextElement();
      String destination_name=term_relation.getDestination();
      if (destination_name!=null)
        finded=	term_relation.getDestination().equals(range_name);
      else
        finded= (range_name==null);
    }
    return term_relation;
  }



  public void addTermRelation(ISTermRelation term_relation)
  {
    String prop_name=term_relation.getName();
    Vector prop_vector=null;

    if (!outgoing_term_relations.containsKey(prop_name))
    {
      prop_vector = new Vector();
      outgoing_term_relations.put(prop_name,prop_vector);
    }
    else
      prop_vector=(Vector)outgoing_term_relations.get(prop_name);

    prop_vector.add(term_relation);

  }



  public Enumeration getTermRelations (String term_relation_name)
  {
    Vector term_relation_vector=(Vector)outgoing_term_relations.get(term_relation_name);
    if (term_relation_vector!=null)
      return(term_relation_vector.elements());
    else
      return null;
  }

  public HashSet getAttributeNames()
  {
    HashSet attr_set = new HashSet();

    Enumeration enu = instance_attributes.keys();
    while (enu.hasMoreElements())
    {
      attr_set.add(enu.nextElement());
    }
    enu = instance_attributes.keys();
    while (enu.hasMoreElements())
    {
      attr_set.add(enu.nextElement());
    }

    return attr_set;
  }
}


