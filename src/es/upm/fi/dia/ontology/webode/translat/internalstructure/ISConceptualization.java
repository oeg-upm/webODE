
package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;

public class ISConceptualization extends ISStructureElement
{
  private Hashtable imported_terms;
  private Vector references;
  private Hashtable concepts;
  private Hashtable groups_by_type;

  private Vector term_relations;
  private Hashtable term_relation_definitions;
  private Hashtable attribute_definitions;

  private Hashtable formulas;
  private Vector constants;
  private Hashtable properties;
  private Hashtable namespaces;

  public void toWebODE(ODEService ode, String ontologyName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException, ISException
  {

    ISImportedTerm it;
    for(Enumeration enum=imported_terms.elements(); enum.hasMoreElements(); )
    {
      it=(ISImportedTerm)enum.nextElement();
      it.toWebODE(ode,ontologyName);
    }
/*
     ISReference ref;
     for(Enumeration enum=references.elements(); enum.hasMoreElements(); )
     {
       ref=(ISReference)enum.nextElement();
       ref.toWebODE(ode,...);
     }
*/

    ISConcept concept;
    for(Enumeration enum=concepts.elements(); enum.hasMoreElements(); )
    {
      concept=(ISConcept)enum.nextElement();
      concept.toWebODE(ode,ontologyName);
    }

    ISGroup group;
    Hashtable vgroup=null;
    for(Enumeration enum=groups_by_type.elements(); enum.hasMoreElements(); )
    {
      Object aux_obj2=enum.nextElement();
      if (aux_obj2 instanceof Hashtable)
      {
	vgroup=(Hashtable)aux_obj2;

	for(Enumeration enum2=vgroup.elements(); enum2.hasMoreElements(); )
	{
	  Object aux_obj =enum2.nextElement();
	  if (aux_obj instanceof ISGroup)
	  {
	    group=(ISGroup)aux_obj;
	    group.toWebODE(ode,ontologyName);
	  }
	  else
	  {
	    System.out.println("ESTE "+aux_obj+" NO ES UN GROUP");
	  }
	}
      }
      else
      {
	System.out.println("Peta fuera!!! Z> "+ aux_obj2);
      }
    }

    ISProperty prop;
    for(Enumeration enum=properties.elements(); enum.hasMoreElements(); )
    {
      prop=(ISProperty)enum.nextElement();
      prop.toWebODE(ode,ontologyName);
    }

    ISTermRelation relation;
    for(Enumeration enum=term_relations.elements(); enum.hasMoreElements(); )
    {
      relation=(ISTermRelation)enum.nextElement();
      relation.toWebODE(ode,ontologyName);
    }

    ISFormula form;
    for(Enumeration enum=formulas.elements(); enum.hasMoreElements(); )
    {
      form=(ISFormula)enum.nextElement();
      form.toWebODE(ode,ontologyName);
    }
/*
     ISTConstant conts;
     for(Enumeration enum=constants.elements(); enum.hasMoreElements(); )
     {
       conts=(ISTConstant)enum.nextElement();
       conts.toWebODE(ode,name);
     }*/

  }

  public ISConceptualization ()
  {
    empty=false;

    imported_terms= new Hashtable();
    references= new Vector();
    concepts= new Hashtable();

    groups_by_type= new Hashtable();
    term_relations= new Vector();
    formulas = new Hashtable();
    constants=new Vector();
    properties= new Hashtable();
    term_relation_definitions=new Hashtable();
    attribute_definitions=new Hashtable();
    namespaces=new Hashtable();

  }

  public void addConcept (ISConcept concept)
  {
    concepts.put(concept.getName(),concept);
  }


  public ISConcept getConcept (String concept_name)
  {
    return ((ISConcept)concepts.get(concept_name));
  }




  public void addTermRelation (ISTermRelation term_relation)
  {
    term_relations.addElement(term_relation);
  }

  public void addTermRelationDefinition (ISTermRelationDefinition relation_definition)
  {
    term_relation_definitions.put(relation_definition.getName(),relation_definition);
  }

  public void addAttributeDefinition (ISAttributeDefinition attr_definition)
  {
    attribute_definitions.put(attr_definition.getName(),attr_definition);
  }


  public ISAttributeDefinition getAttributeDefinition(String attr_name)
  {
    return ((ISAttributeDefinition)attribute_definitions.get(attr_name));
  }


  public Enumeration getTermRelationDefinitions ()
  {
    return term_relation_definitions.elements();
  }


  public ISTermRelationDefinition getTermRelationDefinition (String definition_name)
  {
    return ((ISTermRelationDefinition)term_relation_definitions.get(definition_name));
  }



  public Enumeration getTermRelationsTo(String concept_name,String relation_name)
  {
    Vector term_relations_to = new Vector ();
    Enumeration e = term_relations.elements();
    while (e.hasMoreElements())
    {
      ISTermRelation term_relation = (ISTermRelation)e.nextElement();
      if (concept_name.equals(term_relation.getDestination()))
	term_relations_to.add(term_relation);

    }
    return (term_relations_to.elements());
  }


  public Enumeration getTermRelationsFrom (String concept_name,String relation_name)
  {
    //System.out.println("Getting from "+concept_name+"  ----------------------------------");
    Vector term_relations_from = new Vector ();
    Enumeration e = term_relations.elements();
    while (e.hasMoreElements())
    {
      ISTermRelation term_relation = (ISTermRelation)e.nextElement();
      if (concept_name.equals(term_relation.getOrigin()))
      {
	if (relation_name!=null &&relation_name.equals(term_relation.getName()))
   term_relations_from.add(term_relation);
 //	System.out.println(">>"+term_relation.getName());
      }
    }
    return (term_relations_from.elements());
  }


  public Enumeration getTermRelationsFrom (String concept_name,HashSet searched_relations)
  {
    //System.out.println("Getting from "+concept_name+"  ----------------------------------");
    Vector term_relations_from = new Vector ();
    Enumeration e = term_relations.elements();
    while (e.hasMoreElements())
    {
      ISTermRelation term_relation = (ISTermRelation)e.nextElement();
      if (concept_name.equals(term_relation.getOrigin()))
      {
	if (searched_relations.contains(term_relation.getName()))
   term_relations_from.add(term_relation);
 //	System.out.println(">>"+term_relation.getName());
      }
    }
    return (term_relations_from.elements());
  }

  public Enumeration getTermRelationsTo (String concept_name,HashSet searched_relations)
  {
    try
    {
      Vector term_relations_to = new Vector ();
      Enumeration e = term_relations.elements();
      while (e.hasMoreElements())
      {
	ISTermRelation term_relation = (ISTermRelation)e.nextElement();
	if (concept_name.equals(term_relation.getDestination()))
	{
	  if (searched_relations.contains(term_relation.getName()))
     term_relations_to.add(term_relation);

	}
      }
      return (term_relations_to.elements());
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("Este es el concept_name "+concept_name);
      System.out.println(".....................................");
      return null;

    }
  }


  public void getReachablesFrom (String root_concept_name,HashSet relations,HashSet equivalence_relations,HashSet visited_concepts)
  {
    //HashSet relations_union = new HashSet();
    if (!visited_concepts.contains(root_concept_name))
    {
      visited_concepts.add(root_concept_name);
      Enumeration relations_from_concept = getTermRelationsFrom(root_concept_name,relations);
      while (relations_from_concept.hasMoreElements())
      {
	ISTermRelation term_relation = (ISTermRelation)relations_from_concept.nextElement();
	getReachablesFrom(term_relation.getDestination(),relations,equivalence_relations,visited_concepts);
      }


      Enumeration relations_to_concept = getTermRelationsTo(root_concept_name,equivalence_relations);
      while (relations_to_concept.hasMoreElements())
      {
	ISTermRelation term_relation = (ISTermRelation)relations_to_concept.nextElement();
	getReachablesFrom(term_relation.getOrigin(),relations,equivalence_relations,visited_concepts);
      }


    }


  }

  public void getReachables (String root_concept_name, HashSet relations, HashSet equivalence_relations, HashSet reachables_concepts)
  {
    HashSet equivalent_to_set = new HashSet();
    getReachablesTo (root_concept_name,relations,equivalence_relations,equivalent_to_set);
    //log.addLine("TO "+equivalent_to_base.toString());

    HashSet equivalent_from_set = new HashSet();
    getReachablesFrom (root_concept_name,relations,equivalence_relations,equivalent_from_set);
//	log.addLine("FROM "+equivalent_from_base.toString());
    reachables_concepts=_union(equivalent_to_set,equivalent_to_set);
  }

// !!!CHAPUZA

  private HashSet _union (HashSet set1, HashSet set2)
  {
    HashSet small_set = new HashSet();
    HashSet big_set= new HashSet();
    HashSet result_set= new HashSet();
    if (set1.size()>set2.size())
    {
      small_set=(HashSet)set2.clone();
      big_set=(HashSet)set1.clone();
    }
    else
    {
      small_set=(HashSet)set1.clone();
      big_set=(HashSet)set2.clone();
    }


    Iterator set_it = small_set.iterator();
    while (set_it.hasNext())
      big_set.add(set_it.next());




    return big_set;
  }





  public void getReachablesTo (String root_concept_name,HashSet relations,HashSet equivalence_relations,HashSet visited_concepts)
  {
    if (!visited_concepts.contains(root_concept_name))
    {
      visited_concepts.add(root_concept_name);
      Enumeration relations_to_concept = getTermRelationsTo(root_concept_name,relations);
      while (relations_to_concept.hasMoreElements())
      {
	ISTermRelation term_relation = (ISTermRelation)relations_to_concept.nextElement();
	getReachablesTo(term_relation.getOrigin(),relations,equivalence_relations,visited_concepts);
      }


      Enumeration relations_from_concept = getTermRelationsFrom(root_concept_name,equivalence_relations);

      while (relations_from_concept.hasMoreElements())
      {


	ISTermRelation term_relation = (ISTermRelation)relations_from_concept.nextElement();
	getReachablesTo(term_relation.getDestination(),relations,equivalence_relations,visited_concepts);

      }

    }

  }









  public Enumeration getTermRelations ()
	  {
		  return (term_relations.elements());
	  }


	  public void removeTermRelation (String name)
		  {
			  term_relations.remove(name);
	}

	public void setTermRelations(Vector new_term_relations)
		{
			term_relations=new_term_relations;
		}




  public Enumeration getRootConceptsNames(String relation_name)
  {
    Vector root_concepts_vector=new Vector();
    Enumeration concepts_enu=concepts.elements();
    while (concepts_enu.hasMoreElements())
    {
      boolean has_father=false;
      String concept_name= ((ISConcept)concepts_enu.nextElement()).getName();
      //	System.out.println(concept_name+"________________________________________");

      Enumeration term_relations_enu= getTermRelationsFrom(concept_name,relation_name);

      while (term_relations_enu.hasMoreElements()&&!has_father)
      {
	ISTermRelation term_relation=(ISTermRelation)term_relations_enu.nextElement();
	has_father=(term_relation.getName().equals(relation_name));
      }
      if (!has_father)
	root_concepts_vector.add(concept_name);


    }

    return(root_concepts_vector.elements());
  }


  public Enumeration getLeafConceptsNames(String relation_name)
  {
    Vector leaf_concepts_vector=new Vector();
    Enumeration concepts_enu=concepts.elements();
    while (concepts_enu.hasMoreElements())
    {
      boolean has_child=false;
      String concept_name= ((ISConcept)concepts_enu.nextElement()).getName();
      //System.out.println(concept_name+"________________________________________");
      Enumeration term_relations_enu= getTermRelationsTo(concept_name,relation_name);
      while (term_relations_enu.hasMoreElements()&&!has_child)
      {
	ISTermRelation term_relation=(ISTermRelation)term_relations_enu.nextElement();
	has_child=(term_relation.getName().equals(relation_name));
      }
      if (!has_child)
	leaf_concepts_vector.add(concept_name);


    }

    return(leaf_concepts_vector.elements());
  }



  public void addGroup (ISGroup group, String type_of_group)
  {
    group.setType(type_of_group);

    //	groups.put(group.getName(),group);

    Hashtable groups_of_type =(Hashtable) groups_by_type.get(type_of_group);
    if (groups_of_type==null)
    {
      groups_of_type=new Hashtable();

    }
    groups_of_type.put(group.getName(),group);
    groups_by_type.put(type_of_group,groups_of_type);

  }


  public Enumeration getGroupsByType(String type_of_group)
  {
    Hashtable groups_of_type;

    groups_of_type = (Hashtable)groups_by_type.get(type_of_group);
    if (groups_of_type==null)
      groups_of_type = new Hashtable();

    return groups_of_type.elements();
  }

  public Enumeration getGroups()
  {
    Vector all_groups = new Vector();
    Enumeration groups_by_type_enu = groups_by_type.elements();
    while (groups_by_type_enu.hasMoreElements())
    {
      Hashtable groups_of_type = (Hashtable)groups_by_type_enu.nextElement();
      Enumeration groups_enu =groups_of_type.elements();
      while (groups_enu.hasMoreElements())
      {
	all_groups.add((ISGroup)groups_enu.nextElement());
      }
    }

    return all_groups.elements();
  }








  public boolean hasTermRelation (String term_relation_name, String origin_name, String destination_name)
  {
    Enumeration vector_elements = term_relations.elements();
    ISTermRelation aux_term_relation=null;
    boolean finded= false;
    while (vector_elements.hasMoreElements()&&(!finded))
    {
      aux_term_relation=(ISTermRelation)vector_elements.nextElement();
      if ((aux_term_relation.getName().equals(term_relation_name)) &&
	  (aux_term_relation.getDestination().equals(destination_name)) &&
	  (aux_term_relation.getOrigin().equals(origin_name)))
      {
	finded=true;
      }
    }

    return finded;
  }

  public boolean hasConcept (String concept_name)
  {
    return (concepts.containsKey(concept_name));

  }

  public boolean hasAttributeDefinition(String attr_definition_name)
  {
    return (attribute_definitions.containsKey(attr_definition_name));
  }

  public boolean hasTermRelationDefinition(String relation_definition_name)
  {
    return (term_relation_definitions.containsKey(relation_definition_name));
  }










  public Enumeration getConcepts ()
  {
    return (concepts.elements());
  }
/*
  public ISConcept getConcept(String concept_name)
  {
    return ((ISConcept)concepts.get(concept_name));
  }
*/

  public void addFormula (ISFormula formula)
  {
    formulas.put(formula.getName(),formula);
  }

  public boolean hasProperty(String prop_name)
  {
    return properties.containsKey(prop_name);
  }


  public void addProperty (ISProperty new_property)
  {
    properties.put(new_property.getName(),new_property);
  }

/*
 public ISGroup getGroup(String group_name)
 {
  return(IEGroup)groups.get(group_name);
 }

*/



  public boolean hasImportedTerm (String imp_term_namespace,String imp_term_name)
  {

    //	String aux_namespace="webode://"+imp_term_namespace.replaceAll("http://","");
    //System.out.println("["+imp_term_namespace +"|"+imp_term_name+"]"+imported_terms);
    boolean finded=false;
    Enumeration imp_terms_enu = imported_terms.elements();
    while (imp_terms_enu.hasMoreElements()&&!finded)
    {
      ISImportedTerm imp_term= (ISImportedTerm)imp_terms_enu.nextElement();
      //System.out.println (imp_term.getURL()+","+imp_term.getOriginalName());
      finded = imp_term_namespace.equals(imp_term.getURI());
//					&& imp_term_name.equals(imp_term.getOriginalName()));
    }
    return finded;
  }

  public boolean hasImportedTermWithName (String imp_term_name)
  {
    String real_name;
    if (imp_term_name.indexOf(":")<0)
      return false;
    else
    {

      boolean finded=false;
      Enumeration imp_terms_enu = imported_terms.elements();
      while (imp_terms_enu.hasMoreElements()&&!finded)
      {
	ISImportedTerm imp_term= (ISImportedTerm)imp_terms_enu.nextElement();
	String local_name=null;
	String uri=imp_term.getURI();
	int index2;

	index2= uri.indexOf('#');
	local_name=uri.substring(index2+1,uri.length());


	finded = imp_term_name.equals(imp_term.getNamespaceIdentifier()+":"+local_name);
//					&& imp_term_name.equals(imp_term.getOriginalName()));
      }
      return finded;

    }
  }

  public boolean hasImportedTerm (String imported_term_name)
  {
//		System.out.println(imported_terms);
    return imported_terms.containsKey(imported_term_name);
  }

  public void addImportedTerm (ISImportedTerm imp_term)
  {
    imported_terms.put(imp_term.getURI(),imp_term);
  }

  public ISImportedTerm getImportedTerm(String imp_term_namespace,String imp_term_name)
  {
    boolean finded=false;
    ISImportedTerm returned_imp_term=null;
    Enumeration imp_terms_enu = imported_terms.elements();
//		String aux_namespace="webode://"+imp_term_namespace.replaceAll("http://","");

    while (imp_terms_enu.hasMoreElements()&&!finded)
    {
      ISImportedTerm imp_term= (ISImportedTerm)imp_terms_enu.nextElement();
      finded = imp_term_namespace.equals(imp_term.getURI());
      //&& imp_term_name.equals(imp_term.getOriginalName()));
      if(finded)
	returned_imp_term=imp_term;

    }
    return returned_imp_term;
  }


  public void addNamespaceIdentifier (String namespace, String identifier)
  {

    namespaces.put(namespace,identifier);
  }


  public boolean hasNamespaceIdentifier (String namespace)
  {
    return namespaces.containsKey(namespace);
  }

  public String getNamespaceIdentifier(String namespace)
  {
    return (String)namespaces.get(namespace);
  }


  public void removeConcept (String name)
  {
    concepts.remove(name);
  }










  public Enumeration getTransitiveClosureFrom (Vector relations,String concept)
  {
    return null;
  }

  public Node obtainXML (Document owner_document)
  {
    Element conceptualization_element=owner_document.createElement("Conceptualization");

    for (Enumeration e = imported_terms.elements() ; e.hasMoreElements() ;)
      conceptualization_element.appendChild(((ISImportedTerm)e.nextElement()).obtainXML(owner_document));


    for (Enumeration e = references.elements() ; e.hasMoreElements() ;)
      conceptualization_element.appendChild(((ISReference)e.nextElement()).obtainXML(owner_document));

    for (Enumeration e = concepts.elements() ; e.hasMoreElements() ;)
      conceptualization_element.appendChild(((ISConcept)e.nextElement()).obtainXML(owner_document));

    Enumeration groups_by_type_enu = groups_by_type.elements();
    while (groups_by_type_enu.hasMoreElements())
    {

      Hashtable groups_of_type = (Hashtable)groups_by_type_enu.nextElement();
      Enumeration groups_of_type_enu = groups_of_type.elements();
      while (groups_of_type_enu.hasMoreElements())
      {
	conceptualization_element.appendChild(((ISGroup)groups_of_type_enu.nextElement()).obtainXML(owner_document));
      }
    }
    for (Enumeration e = term_relations.elements() ; e.hasMoreElements() ;)
      conceptualization_element.appendChild(((ISTermRelation)e.nextElement()).obtainXML(owner_document));

    for (Enumeration e = formulas.elements() ; e.hasMoreElements() ;)
      conceptualization_element.appendChild(((ISFormula)e.nextElement()).obtainXML(owner_document));

    for (Enumeration e = constants.elements() ; e.hasMoreElements() ;)
      conceptualization_element.appendChild(((ISConstant)e.nextElement()).obtainXML(owner_document));

    for (Enumeration e = properties.elements() ; e.hasMoreElements() ;)
      conceptualization_element.appendChild(((ISProperty)e.nextElement()).obtainXML(owner_document));



    return conceptualization_element;
  }


}