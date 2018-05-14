package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.io.*;
import java.util.*;

//Jena related imports
import com.hp.hpl.jena.daml.*;
import com.hp.hpl.jena.daml.common.DAMLModelImpl;
import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.jena.rdf.query.*;
import com.hp.hpl.mesa.rdf.jena.vocabulary.RDF;

import es.upm.fi.dia.ontology.webode.translat.logs.LogTextImpl;

import es.upm.fi.dia.ontology.webode.translat.namescontainers.WEBODENamesContainer;
import es.upm.fi.dia.ontology.webode.translat.namescontainers.DAMLNamesContainer;

class DAMLISStructureImpl extends ISStructureImpl
{
  //Attributes
  //private ISOntology ontology;
  private ISInstancesSet instances_set;
  private ISConceptualization conceptualization;
  private DAMLModel daml_model;
//	private WrapperLog
  private Vector disjoint_with_groups=new Vector();
  private WEBODENamesContainer webode_names_container;
  private DAMLNamesContainer daml_names_container;

  //Methods
  public DAMLISStructureImpl (Reader input,String name, String namespace)
  {
    webode_names_container= new WEBODENamesContainer(namespace);
    daml_names_container=new DAMLNamesContainer(namespace);
    log =new LogTextImpl();
    daml_model = new DAMLModelImpl();
    try
    {
      daml_model.read(input,webode_names_container.namespace,"RDF/XML");
    }
    catch (Exception e)
    {
      log.addError("Something went wrong when JENA tried to create de DAML+OIL model: "+e);
    }

    conceptualization = new ISConceptualization();
    _mapOntology(name,namespace);
    ontology.setConceptualization(conceptualization);
    _mapPropertiesDefinitions();
    _mapClasses ();
    _mapGlobalObjectProperties();
    _checkImportedTerms ();
    _mapInstances();
  }


  private void _mapSpecialClasses ()
  {
    log.addLine ("Adding the classes: DAML:Thing and DAML:Nothing");
    ISConcept thing_concept = new ISConcept(daml_names_container.THING);
    ISConcept nothing_concept = new ISConcept(daml_names_container.NOTHING);
    conceptualization.addConcept(thing_concept);
    conceptualization.addConcept(nothing_concept);
  }

  private void _showStatements ()
  {
    //This method shows all the rdf statements that conform the ontology
    String ontology_comments =null;

    String query_string = "SELECT ?x,?y,?z WHERE (?x,?y,?z)" +

    "USING daml FOR <http://www.daml.org/2001/03/daml+oil#>,"+	"rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";

    Query query = new Query(query_string);
    query.setSource(daml_model);
    QueryExecution qe= new QueryEngine(query);
    QueryResults results = qe.exec();
    for(Iterator iter=results; iter.hasNext(); )
    {
      ResultBinding res = (ResultBinding)iter.next();
      System.out.println("----------------------------------------");

      System.out.println(res.get("x"));
      System.out.println(res.get("y"));
      System.out.println(res.get("z"));
      System.out.println("----------------------------------------");

    }

  }


  private void _mapOntology (String name,String namespace)
  {

    DAMLOntology daml_ontology = daml_model.createDAMLOntology(null);

    String date =(new Date(System.currentTimeMillis())).toString();
    log.addTitle ("Adding the ontology Header and Version Info. Wrapping time"+date,3);
    ontology=new ISOntology(name,namespace,date);
    _mapHeader();
    _mapVersionInfo();
    ontology.setAuthor("ODEWrapper(DAML)");
  }


  private void _mapHeader()
  {
    String ontology_comments =null;
    String query_string = "SELECT ?z WHERE (?x,<rdfs:comment>,?z),(?x,<daml:versionInfo>,?y)" +

    "USING daml FOR <http://www.daml.org/2001/03/daml+oil#>,"+	"rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";

    Query query = new Query(query_string);
    query.setSource(daml_model);
    QueryExecution qe= new QueryEngine(query);
    QueryResults results = qe.exec();
    for(Iterator iter=results; iter.hasNext(); )
    {
      ResultBinding res = (ResultBinding)iter.next();
      ontology.setDescription((res.get("z")).toString());
    }

  }

  private void _mapVersionInfo()
  {
    String query_string = "SELECT ?y WHERE (?x,<daml:versionInfo>,?y)" +

    "USING daml FOR <http://www.daml.org/2001/03/daml+oil#>";
    Query query = new Query(query_string);
    query.setSource(daml_model);
    QueryEngine qe= new QueryEngine(query);
    QueryResults results = qe.exec();
    for(Iterator iter=results; iter.hasNext(); )
    {
      ResultBinding res = (ResultBinding)iter.next();
      ontology.setDescription((res.get("y")).toString());
    }

  }


  private void _mapClasses ()
  {
    log.addTitle("ADDING CLASSES",3);
    //Here we just add NAMED classes
    Iterator it = daml_model.listDAMLClasses();
    while (it.hasNext())
    {
      DAMLClass daml_class = (DAMLClass)it.next();

      //First we check whether the class is named or not, then the class is added
      if (!daml_class.isAnon())
        _mapClass(daml_class,null);
    }

  }




  private void _mapClass (DAMLClass daml_class,String alternative_name)
  {
    ISConcept is_concept=null;
    String class_name=null;

    if (!daml_class.isAnon())
    {
      //SI USAS LOCALES	class_name=daml_class.getLocalName();
      class_name=_resolveTermName(daml_class.getURI());
      //This is writen to the log
      log.addTitle("Translating or adding new restrictions to the class "+class_name,1);
      //Now, the first thing to check is if the concept was already
      //in the conceptualization. A DAML+OIL Class is just defined once but
      //it may be updated many times, usin rdf:about statement
      if (!conceptualization.hasConcept(class_name))
      {
        //If the concept wasn't in the conceptualization, it is created
        is_concept = new ISConcept(class_name);
        conceptualization.addConcept(is_concept);


      }
      else
      {
        //If it was in the conceptualization, it is retrieved from it
        is_concept =conceptualization.getConcept(class_name);
        conceptualization.addConcept(is_concept);
      }
    }
    else
    {
      //If it is an anonymous class, then we use the alternative name
      //which was passed as a parameter
      class_name=alternative_name;
      is_concept = new ISConcept(class_name);
      conceptualization.addConcept(is_concept);
    }

    _mapDocumentation(daml_class,is_concept);

    _mapSubclassofProperties(daml_class,class_name);

    _mapIntersectionOf (daml_class,class_name);
    _mapUnionOf(daml_class,class_name);
    _mapDisjointWith(daml_class,class_name);
    _mapDisjointUnionOf(daml_class,class_name);
    _mapEnumeration(daml_class,class_name);

    _mapEquivalenceProperties(daml_class,class_name);
    _mapComplementOf (daml_class,class_name);

  }


  private void _mapDocumentation (Resource resource, ISStructureElement is_element)
  {

    String annotation_properties[]=
    {daml_names_container.LABEL,daml_names_container.COMMENT,daml_names_container.IS_DEFINED_BY,
      daml_names_container.SEE_ALSO};
    int index;

    for(index=0;index<annotation_properties.length;index++)
    {
      Enumeration enu = _getPropertyObjects(resource,annotation_properties[index]);

      while (enu.hasMoreElements())
      {
        Object aux_obj = enu.nextElement();
        if (aux_obj instanceof Literal)
        {
          try
          {
            is_element.addDescription(annotation_properties[index]+" : "+((Literal)aux_obj).getString()+"||");

          }
          catch(Exception e)
              {}

        }
        else
        {
          if (aux_obj instanceof Resource)
          {
            //OWLNamedElement named_element = (OWLNamedElement)aux_obj;
            is_element.addDescription(annotation_properties[index]+" : "+((Resource)aux_obj).getURI()+"||");
          }
        }
      }
    }
  }










  private void _mapSubclassofProperties(DAMLClass daml_class,String class_name)
  {
    String subclassof_properties[]=
    {daml_names_container.RDFS_NAMESPACE+"subClassOf",daml_names_container.DAMLOIL_NAMESPACE+"subClassOf"};

 /* LO DE ANTES DEL PFC
 String subclassof_properties[]=
 {daml_names_container.rdfs_namespace+"subClassOf",daml_names_container.daml_namespace+"subClassOf",daml_names_container.daml_namespace+"sameClassAs",
  daml_names_container.daml_namespace+"intersectionOf",daml_names_container.daml_namespace+"equivalentTo"};
 */



    int index=0;
    while (index<subclassof_properties.length)
    {
      try
      {
        Enumeration enu = _getPropertyObjects(daml_class,subclassof_properties[index]);
        do
        {
          _mapClassExpression(enu.nextElement(),class_name,webode_names_container.SUBCLASS_OF,null,false);
          log.addWarning(subclassof_properties[index]+" is trasnformed into a WebODE:Subclass-of");
        }
        while (enu.hasMoreElements());
      }
      catch (Exception e)
      {
        log.addLine ("No "+subclassof_properties[index]+" to transform");
      }
      index++;
    }
  }




  private void _mapIntersectionOf (DAMLClass daml_class, String class_name)
  {

    String expression_name=null;
    String axiom_expression="forall (?x) ("+class_name+"(?x))->(";
    try
    {
      Iterator i = ((DAMLList)(daml_class.prop_intersectionOf().getDAMLValue())).getAll();


      while (i.hasNext())
      {
        expression_name=_mapClassExpression(i.next(),class_name,null,null,true);
        if (expression_name!=null)
        {
          axiom_expression=axiom_expression+"("+expression_name+"(?x))";
          if (i.hasNext())
            axiom_expression=axiom_expression + (" and ");
        }
        else
          System.out.println(" AQUI ");
      }
      axiom_expression=axiom_expression+")";

      ISFormula intersection_axiom = new ISFormula();
      String axiom_name=webode_names_container.bindAnonymous(intersection_axiom,webode_names_container.AXIOM);
      intersection_axiom.setName(axiom_name);
      intersection_axiom.setType(webode_names_container.AXIOM);
      intersection_axiom.setExpression(axiom_expression);

      intersection_axiom.setDescription("This axiom describes as an intersection the concept "+class_name);
      conceptualization.addFormula(intersection_axiom);

    }
    catch (Exception e)	{log.addLine("No daml:intersectionOf to trasnform");}
  }



  private Enumeration _getPropertyObjects(Resource resource,String property_name)
  {
    Vector property_objects= new Vector();
    try
    {

      StmtIterator properties_it =resource.listProperties();
      while (properties_it.hasNext())
      {
        Statement statement=properties_it.next();

        if (statement.getPredicate().getURI().equals(property_name))
        {
          Object aux_obj=statement.getObject();
          if ((aux_obj instanceof DAMLList))
          {
            DAMLList list = (DAMLList) aux_obj;
            Iterator list_elements_it=list.getAll();
            while (list_elements_it.hasNext())
            {
              property_objects.add(list_elements_it.next());
            }
          }
          else
          {
            //	log.addLine(aux_obj.toString());
            property_objects.add(aux_obj);
          }
        }
      }
    }
    catch (Exception e)
    {
      log.addError("There was a problem adding the property "+property_name);
    }
    //log.addLine(property_objects.toString());
    return(property_objects.elements());
  }

  private void _mapComplementOf (DAMLClass daml_class, String class_name)
  {
    ISFormula complement_axiom;
    try
    {
      Enumeration enu = _getPropertyObjects(daml_class,daml_names_container.COMPLEMENT_OF);

      do
      {
        String complement_name =_mapClassExpression(enu.nextElement(),class_name,null,null,true);
        String begining_expression="forall (?x) ("+class_name+"(?x))<->not";
        String axiom_description=null;
        String axiom_expression=begining_expression+"("+complement_name+" (?x))";
        complement_axiom= new ISFormula();
        String axiom_name = webode_names_container.bindAnonymous(complement_axiom,webode_names_container.AXIOM);
        complement_axiom.setName(axiom_name);
        complement_axiom.setType(webode_names_container.AXIOM);
        complement_axiom.setExpression(axiom_expression);
        axiom_description="The axiom that describes that the concept "+ class_name
                         +" is the complement of "+complement_name;
        complement_axiom.setDescription(axiom_description);
        conceptualization.addFormula(complement_axiom);
        log.addLine("This concept is defined as the owl:complementOf "+complement_name);

      }
      while (enu.hasMoreElements());
    }
    catch (Exception e)
    {
      log.addLine ("No daml:complementOf class expressions to transform");
    }
  }





  private void _mapEquivalenceProperties (DAMLClass daml_class, String class_name)
  {
    String equivalence_properties[]=
    {daml_names_container.EQUIVALENT_TO,daml_names_container.SAME_CLASS_AS};

    String equivalence_properties_short[]=
    {"daml:equivalentTo","daml:sameClassAs"};

    ISFormula equivalence_axiom;
    int i;
    for (i=0;i<equivalence_properties.length;i++)
    {
      try
      {
      Enumeration enu = _getPropertyObjects(daml_class,equivalence_properties[i]);

      do
      {
        String equivalent_name =_mapClassExpression(enu.nextElement(),class_name,null,null,true);
        String begining_expression="forall (?x) ("+class_name+"(?x))<->";
        String axiom_description=null;
        String axiom_expression=begining_expression+"("+equivalent_name+" (?x))";
        equivalence_axiom= new ISFormula();
        String axiom_name = webode_names_container.bindAnonymous(equivalence_axiom,webode_names_container.AXIOM);
        equivalence_axiom.setName(axiom_name);
        equivalence_axiom.setType(webode_names_container.AXIOM);
        equivalence_axiom.setExpression(axiom_expression);
        axiom_description="The axiom that describes that the concept "+ class_name
                         +" is equivalent "+equivalent_name;
        equivalence_axiom.setDescription(axiom_description);
        conceptualization.addFormula(equivalence_axiom);
        log.addLine("This concept is defined as "+equivalence_properties_short[i]+" of "+equivalent_name);

      }
      while (enu.hasMoreElements());
    }
    catch (Exception e)
    {
      log.addLine ("No "+equivalence_properties_short[i]+" class expressions to transform");
    }
    }
  }


  private void _mapEnumeration(DAMLClass daml_class, String class_name)
  {

//A class expression can have an enumeration statement, so it must be properly
    //treated as well

    String axiom_expression="forall (?x) ("+class_name+"(?x))->(";
    ISInstancesSet instances_set =null;

    try
    {

      Enumeration enu = _getPropertyObjects(daml_class,daml_names_container.ONE_OF);

      if (enu.hasMoreElements())
      {
  /*
   log.addLine ("A WebODE:InstanceSet is created to add the instances of the enumeration");
   instances_set = new ISInstancesSet("Enumeration of"+class_name);
   ontology.addInstancesSet(instances_set);
  */


        while (enu.hasMoreElements())
        {
          DAMLCommon daml_instance = ((DAMLCommon)enu.nextElement());
          //Additional information about this mapping should appear
          String instance_name=_resolveTermName(daml_instance.getURI());
          Resource type_resource=(Resource)daml_instance.prop_type().get();
          String type_name=type_resource.getURI();

          //ISInstance ie_instance = new ISInstance(instance_name,type_name);

          axiom_expression=axiom_expression +("(?x="+instance_name+")");
          //instances_set.addInstance(ie_instance);
          if (enu.hasMoreElements())
            axiom_expression=axiom_expression + (" or ");

        }
        axiom_expression=axiom_expression+(")");

        ISFormula enumeration_axiom = new ISFormula();
        String axiom_name=webode_names_container.bindAnonymous(enumeration_axiom,webode_names_container.AXIOM);
        enumeration_axiom.setName(axiom_name);
        enumeration_axiom.setType(webode_names_container.AXIOM);
        enumeration_axiom.setExpression(axiom_expression);
        enumeration_axiom.setDescription("This axiom describes an enumeration for the concept "+class_name);
        conceptualization.addFormula(enumeration_axiom);


      }

    }
    catch (Exception e)
    {
      log.addLine("No daml:oneOf statements to transform");
    }



  }

  private void _mapUnionOf (DAMLClass daml_class, String class_name)
  {

    String expression_name=null;
    String axiom_expression="forall (?x) ("+class_name+"(?x))->(";
    try
    {
      Iterator i = ((DAMLList)(daml_class.prop_unionOf().getDAMLValue())).getAll();

      while (i.hasNext())
      {
        expression_name=_mapClassExpression(i.next(),class_name,null,null,true);
        if (expression_name!=null)
        {
          axiom_expression=axiom_expression+"("+expression_name+"(?x))";
          if (i.hasNext())
          {
            axiom_expression=axiom_expression + (" or ");
          }
        }

      }
      axiom_expression=axiom_expression+")";


      ISFormula enumeration_axiom = new ISFormula();
      String axiom_name=webode_names_container.bindAnonymous(enumeration_axiom,webode_names_container.AXIOM);
      enumeration_axiom.setName(axiom_name);
      enumeration_axiom.setType(webode_names_container.AXIOM);
      enumeration_axiom.setExpression(axiom_expression);

      enumeration_axiom.setDescription("This axiom describes an enumeration for the concept "+class_name);
      conceptualization.addFormula(enumeration_axiom);
    }
    catch (Exception e)	{log.addLine("No daml:unionOf to trasnform");}
  }





  public void _mapDisjointUnionOf(DAMLClass daml_class, String class_name)
  {


    try
    {
      Enumeration enu = _getPropertyObjects(daml_class,daml_names_container.DISJOINT_UNION_OF);
      if (enu.hasMoreElements())
      {
        log.addLine ("The transformation of a daml:disjointUnionOf is performed");
        ISGroup disjoint_union_group= new ISGroup();
        String group_name = webode_names_container.bindAnonymous(disjoint_union_group, webode_names_container.GROUP);
        disjoint_union_group.setName(group_name);
        log.addLine ("The WebODE:Group "+group_name+" is added to the conceptualization");

        while (enu.hasMoreElements())
        {
          Object aux_obj = enu.nextElement();
          String expression_name;
          expression_name=_mapClassExpression(aux_obj,null,null,null,true);
          disjoint_union_group.addConcept(expression_name);
          log.addLine("The class expression "+expression_name+" is added to the WebODE:Group "+group_name);
        }


        conceptualization.addGroup(disjoint_union_group,daml_names_container.DISJOINT_UNION_OF);
        ISTermRelation new_relation = new ISTermRelation(webode_names_container.EXHAUSTIVE_SUBCLASS_PARTITION+"2",group_name,class_name,"1");
        conceptualization.addTermRelation(new_relation);
      }


    }
    catch (Exception e)
    {
      log.addLine ("No daml:disjointUnionOf class expressions to transform");
    }

  }


  public void _mapDisjointWith (DAMLClass daml_class, String class_name)
  {
    try
    {
      Enumeration enu = _getPropertyObjects(daml_class,daml_names_container.DISJOINT_WITH);


      while (enu.hasMoreElements())
      {

        ISGroup disjoint_with_group= new ISGroup();
        String group_name = webode_names_container.bindAnonymous(disjoint_with_group,webode_names_container.GROUP);
        disjoint_with_group.setName(group_name);
        disjoint_with_group.addConcept(class_name);
        Object aux_obj;

        String expression_name=	_mapClassExpression(enu.nextElement(),null,null,null,true);
        disjoint_with_group.addConcept(expression_name);
        log.addLine("The  "+expression_name+"is added to the WebODE:Group "+group_name);
        disjoint_with_group.setDescription("The group which describes an disjoint partition for the concept "+class_name);

        conceptualization.addGroup(disjoint_with_group,daml_names_container.DISJOINT_WITH);

      }




    }
    catch (Exception e)
    {
      log.addLine ("No daml:disjointWith class expressions to transform");
    }
  }







  public String	_mapClassExpression(Object daml_object,String domain_class,
      String kind_of_relation,String alternative_name,
      boolean on_rest_generate)
  {

    String class_name=null;

    if (daml_object instanceof DAMLClassExpression)
    {
      DAMLClassExpression daml_expression= (DAMLClassExpression)daml_object;
      if (daml_expression.isNamedClass())
      {
        class_name= _resolveTermName(class_name=((DAMLClass)daml_expression).getURI());
        log.addLine ("Adding the named daml:ClassExpression "+((DAMLCommon)daml_expression).getLocalName());
        if (kind_of_relation!=null)
        {
          ISTermRelation new_relation =
              new ISTermRelation(kind_of_relation,domain_class,class_name,webode_names_container.N_CARDINALITY);
          log.addLine("Adding the WebODE:TermRelation");
          log.addWarning("The WebODE:TermRelation is added with the default maxCardinality (N)");
          conceptualization.addTermRelation(new_relation);
        }

      }
      else
      {
        System.out.println("ALT "+daml_object);
        if (daml_expression.isRestriction())
        {
          if (!on_rest_generate)
          {
            ISConcept ie_concept =conceptualization.getConcept(domain_class);
            _mapRestrictionProperty(ie_concept,domain_class,(DAMLRestriction) daml_expression);
            class_name=null;
          }
          else
          {
            if (alternative_name==null)
            {

              //ESTA MAL TieNES QUE MIRARLO!!!
              //class_name =base_namespace+"ANONYM(RESTRICTION)"+kind_of_relation+(new Integer(anonymous_classes_counter));
              //anonymous_classes_counter++;
              class_name=webode_names_container.bindAnonymous(daml_object,webode_names_container.CONCEPT);
            }
            else
              class_name = alternative_name;

            //	_mapClass((DAMLClass)daml_expression,class_name);
            ISConcept ie_concept_rest = new ISConcept(class_name);
            conceptualization.addConcept(ie_concept_rest);


            _mapRestrictionProperty(ie_concept_rest,class_name,(DAMLRestriction)daml_expression);
            if (kind_of_relation!=null)
            {
              ISTermRelation new_relation = new ISTermRelation(kind_of_relation,domain_class,class_name,webode_names_container.N_CARDINALITY);
              conceptualization.addTermRelation(new_relation);
            }

          }
          //System.out.println("IS A RESTRICTION"+kind_of_relation+"::"+domain_class);
        }
        else
        {
          if (daml_expression instanceof DAMLClass)
          {
            if (alternative_name==null)
            {
              //class_name =base_namespace+"ANONYM"+(new Integer(anonymous_classes_counter));
              //anonymous_classes_counter++;
              class_name=webode_names_container.bindAnonymous(daml_object,webode_names_container.CONCEPT);
            }
            else
              class_name = alternative_name;

            _mapClass((DAMLClass)daml_expression,class_name);
            if (kind_of_relation!=null)
            {
              ISTermRelation new_relation = new ISTermRelation(kind_of_relation,domain_class,class_name,webode_names_container.N_CARDINALITY);
              conceptualization.addTermRelation(new_relation);
            }

          }
          else
            System.out.println("??????QUE ES ESTO ??????????");
        }
      }
    }
    else
    {
      if (daml_object instanceof Resource)
      {
        Resource resource = (Resource)daml_object;
        class_name=_resolveTermName(resource.getURI());
        if (kind_of_relation!=null)
        {
          ISTermRelation new_relation = new ISTermRelation(kind_of_relation,domain_class,class_name,webode_names_container.N_CARDINALITY);
          conceptualization.addTermRelation(new_relation);
        }

      }



    }

    return class_name;
  }








  private void _mapRestrictionProperty (ISConcept is_concept, String domain_class,DAMLRestriction rest)
  {
    String property_name = null;
    String property_uri = null;
    boolean defined_as_datatype=false;
    boolean defined_as_object=false;
    try
    {
      PropertyAccessor p_a= rest.prop_onProperty();
      property_uri = ((Resource) p_a.get()).getURI();
      property_name=_resolveTermName(property_uri);
    }
    catch (Exception e)
    {
      log.addError("Something went wrong transforming the restriction "+rest);
    }
    if (ontology.belongsTo(property_uri))
    {
      log.addLine ("Adding daml:Restriction on property "+property_name);

      if ((conceptualization.hasAttributeDefinition(property_name)||is_concept.hasInstanceAttribute(property_name))
          ||is_concept.hasClassAttribute(property_name))
      {
        defined_as_datatype=true;
        log.addLine (property_name+" was defined as a daml:DatatypeProperty");
        _mapDatatypeRestriction(is_concept,domain_class,rest);
      }


      if (conceptualization.hasTermRelationDefinition(property_name)||
          is_concept.hasTermRelation(property_name))
      {
        defined_as_object=true;
        log.addLine (property_name+" was defined as a daml:ObjectProperty");
        _mapObjectRestriction(is_concept,domain_class,rest);
      }

      if (!(defined_as_datatype||defined_as_object))
      {
        log.addLine(property_name+"is a localy defined property in "+domain_class);
        if (_guessIfClassObjectProperty(domain_class,property_name))
        {
          log.addLine(property_name+"it's supposed to be a daml:ObjectProperty");
          _mapObjectRestriction(is_concept,domain_class,rest);
        }
        else
        {
          log.addLine(property_name+"it cannot be classified, both kinds of restriction are added");
          _mapObjectRestriction(is_concept,domain_class,rest);
          _mapDatatypeRestriction(is_concept,domain_class,rest);
        }
      }
    }


  }







  private void _mapDatatypeRestriction(ISConcept is_concept, String domain_name, DAMLRestriction rest)
  {
    ISAttributeDescriptor is_attr;
    Object aux_obj;
    //First of all we should now the property that is afected by this restriction
    PropertyAccessor p_a= rest.prop_onProperty();
    String attr_name=_resolveTermName(((Resource) p_a.get()).getURI());

    log.addTitle("Transforming datatype restriction on daml:Property "+attr_name,1);

    if (is_concept.hasInstanceAttribute(attr_name))
      is_attr=is_concept.getInstanceAttribute(attr_name);
    else
    {
      if (is_concept.hasClassAttribute(attr_name))
        is_attr=is_concept.getClassAttribute(attr_name);
      else
      {
        //THIS METHOND RETURNS A NEW IS_ATTR!!!!!!!!!!!!!!!!

        if (conceptualization.hasAttributeDefinition(attr_name))
        {
          //ie_attr=getDatatypeProperty(attr_name);
          is_attr=(conceptualization.getAttributeDefinition(attr_name)).obtainInstanceAttributeDescriptor();
        }
        else
        {
          is_attr=new ISInstanceAttributeDescriptor (attr_name,"String","0",webode_names_container.N_CARDINALITY);
        }
        is_concept.addInstanceAttribute((ISInstanceAttributeDescriptor)is_attr);

      }
    }


    aux_obj=rest.prop_hasValue();
    try
    {
      //(DAMLDataInstance)
      PropertyAccessor prop_accessor=((PropertyAccessor)aux_obj);
      String has_value=(((DAMLDataInstance)(prop_accessor).getDAMLValue()).getValue().toString());
      log.addLine("Transforming a daml:hasValue, adding "+has_value+" on atribute"+attr_name);
      if (is_attr instanceof ISInstanceAttributeDescriptor)
      {
        ISInstanceAttributeDescriptor instance_attr=((ISInstanceAttributeDescriptor)is_attr);
        is_attr=instance_attr.upgradeToClassAttribute();
        is_concept.deleteInstanceAttribute(attr_name);
        is_concept.addClassAttribute((ISClassAttributeDescriptor)is_attr);

      }
      //Whether it already was a class attribute or not, the new value is added
      is_attr.addValue(has_value);

    }
    catch (Exception e)
    {
      log.addLine ("No daml:hasValue to transform");
    }




    //Now we check each of the possible fields that can change a DAML+OIL restriction
    aux_obj = rest.prop_minCardinality();
    try
    {
      LiteralAccessor lit_accessor=((LiteralAccessor)aux_obj);
      String min_cardinality  = lit_accessor.getValue().getString();
      log.addLine("The daml:minCardinality is updated from "+is_attr.getMinCardinality()+" to "+min_cardinality);
      is_attr.setMinCardinality(min_cardinality);
    }
    catch (Exception e)
    {
      log.addLine("No daml:minCardinality to transform");
    }

    aux_obj = rest.prop_maxCardinality();
    try
    {
      LiteralAccessor lit_accessor=((LiteralAccessor)aux_obj);
      String max_cardinality = lit_accessor.getValue().getString();
      log.addLine("The daml:maxCardinality is updated from "+is_attr.getMaxCardinality()+" to "+max_cardinality);
      is_attr.setMaxCardinality(max_cardinality);

    }
    catch (Exception e)
    {
      log.addLine("No daml:maxCardinality to transform");
    }


    aux_obj = rest.prop_cardinality();
    try
    {
      LiteralAccessor lit_accessor=((LiteralAccessor)aux_obj);
      String cardinality = lit_accessor.getValue().getString();
      log.addLine("A daml:cardinality statement has been found, both max and min cardinalities are changed");
      log.addLine("The daml:maxCardinality is changed from "+is_attr.getMaxCardinality()+" to "+cardinality);
      is_attr.setMaxCardinality(cardinality);
      log.addLine("The daml:minCardinality is changed from "+is_attr.getMinCardinality()+" to "+cardinality);
      is_attr.setMinCardinality(cardinality);
    }
    catch (Exception e)
    {
      log.addLine("No daml:cardinality to transform");
    }


    log.addLine("Transforming daml:toClass");
    p_a =(PropertyAccessor) rest.prop_toClass();
    aux_obj =  p_a.get();

    //if (aux_obj!=null)
    try
    {
      String range_name=webode_names_container.translateDatatype(_mapClassExpression(aux_obj,domain_name,null,null,true));
      String type_name = (is_attr.getType());
      if (!(range_name.equals(type_name)))
      {
        log.addLine("The daml:range is updated from "+type_name+" to "+range_name);
        log.addWarning("There was a change of type in the daml:DatatypeProperty "+attr_name);
      }
      is_attr.setType(range_name);
    }
    catch (Exception e)
    {
      log.addLine("No daml:toClass to transform");
    }

    try
    {
      log.addLine("Transforming daml:hasClass");
      p_a =(PropertyAccessor) rest.prop_hasClass();
      aux_obj =  p_a.get();
      if (aux_obj!=null)
      {
        String range_name=webode_names_container.translateDatatype(_mapClassExpression(aux_obj,domain_name,null,null,true));
        String type_name = is_attr.getType();
        if (!(range_name.equals(type_name)))
        {
          log.addLine("The daml:range is updated from "+type_name+" to "+range_name);
          log.addWarning("There was a change of type in the daml:DatatypeProperty "+attr_name);
        }
        is_attr.setType(range_name);

        if (is_attr.getMinCardinality().equals("0"))
        {
          log.addLine("The minCardinality is updated from 0 to 1");
          is_attr.setMinCardinality(1);
        }
      }
    }
    catch (Exception e)
    {
      log.addLine("No daml:hasClass");
    }

  }

  private void _mapObjectRestriction(ISConcept is_concept, String domain_name, DAMLRestriction rest)
  {
    Object aux_obj;
    PropertyAccessor p_a= rest.prop_onProperty();




    String prop_name=_resolveTermName(((Resource) p_a.get()).getURI());
    ISTermRelation term_relation=null;
    String range_name =null;
    boolean well_defined=true;
    log.addTitle("Transforming a restriction on the daml:Property "+prop_name+" for the daml:Class "+domain_name,1);

    //Now we check the range of this restriction
    try
    {
      p_a =(PropertyAccessor) rest.prop_toClass();
      aux_obj =  p_a.get();
      if (aux_obj!=null)
      {
        range_name=_mapClassExpression(aux_obj,domain_name,null,null,true);
        log.addLine("daml:toClass found, pointing to "+range_name);
        well_defined=true;
      }
    }
    catch (Exception e)
    {
      log.addLine("daml:toClass not present at this daml:Restriction");
    }

    try
    {
      p_a =(PropertyAccessor) rest.prop_hasClass();
      aux_obj =  p_a.get();
      if (aux_obj!=null)
      {
        range_name=_mapClassExpression(aux_obj,domain_name,null,null,true);
        log.addLine("daml:hasClass found pointing to "+range_name);
        log.addError ("WebODE cannot handle minimum cardinalities for term relation so this information is information lost");
        well_defined=true;
        if (!conceptualization.hasTermRelation(prop_name,domain_name,daml_names_container.THING))
        {
          ISTermRelation new_relation = new ISTermRelation(prop_name,domain_name,range_name,webode_names_container.N_CARDINALITY);
          conceptualization.addTermRelation(new_relation);
        }
      }
    }
    catch (Exception e)
    {
      log.addLine("daml:hasClass not present at this daml:Restriction");
    }

    try
    {

      p_a =(PropertyAccessor) rest.prop_hasValue();
      aux_obj =  p_a.get();
      if (aux_obj!=null)
      {
        String has_value=rest.prop_hasValue().get().toString();
        log.addLine ("daml:hasValue found");
        log.addError("WebODE cannot state a relation between a WebODE:Concept and a WebODE:ConceptInstance");
        well_defined=false;
      }
    }
    catch (Exception e)
    {
      log.addLine ("No daml:hasValue statement found");
    }

    if (well_defined && range_name==null)
      range_name=_resolveTermName(daml_names_container.THING);

    if (well_defined)
    {
      if (!is_concept.hasTermRelation(prop_name,domain_name,range_name))
      {
        log.addLine ("The WebODE:TermRelation "+prop_name+"("+domain_name+","+range_name+") doesn't exist");

        if (conceptualization.hasTermRelationDefinition(prop_name))
        {
          log.addLine("The property "+prop_name+" was globally defined");
          term_relation=(conceptualization.getTermRelationDefinition(prop_name).obtainTermRelation());
          String global_range=term_relation.getDestination();
          String global_dominion=term_relation.getOrigin();
          if (global_range!=null)
          {
            if (!global_range.equals(range_name))
            {
              log.addWarning("The range in this restriction doesn't match the global rdfs:range defined for this property");
            }
          }
          if (global_dominion!=null)
          {
            if (!global_dominion.equals(domain_name))
            {
              log.addWarning("The domain in this restriction doesn't match the global rdfs:domain defined for this property");
            }
          }
          term_relation.setDestination(range_name);
          term_relation.setOrigin(domain_name);
        }
        else
        {
          log.addLine("The  "+prop_name+" is local to this daml:Class and is created");
          log.addWarning("The WebOde:TermRelation is created with the default daml:maxCardinality");
          term_relation =
              new ISTermRelation(prop_name,domain_name,range_name,webode_names_container.N_CARDINALITY);
        }
        is_concept.addTermRelation(term_relation);
        conceptualization.addTermRelation(term_relation);
        //	addClassObjectProperty (domain_name,term_relation);
      }
      else
      {
        log.addLine("The property "+prop_name+" has a WebODE:TermRelation already defined for this domain and range");
        term_relation = is_concept.getTermRelation(prop_name,domain_name,range_name);
        //term_relation = getClassObjectProperty(domain_name,prop_name,domain_name,range_name);

      }

      log.addLine ("Updating cardinalities of the WebODE:TermRelation");
      _updateTermRelation(term_relation,rest);
    }
    else
    {
      log.addLine ("PETA AQUI");
    }

  }

  private void _updateTermRelation (ISTermRelation term_relation,DAMLRestriction rest)
  {
    Object aux_object=null;
    aux_object = rest.prop_minCardinality();
    try
    {
      LiteralAccessor lit_accessor=((LiteralAccessor)aux_object);
      String min_cardinality  = lit_accessor.getValue().getString();
      log.addLine("daml:minCardinality found");
      log.addError("WebODE cannot handle minimum cardinalities for term relation so here exists an information lost");
    }
    catch (Exception e)
    {
      log.addLine("No daml:minCardinality to transform");
    }

    aux_object = rest.prop_maxCardinality();
    try
    {
      LiteralAccessor lit_accessor=((LiteralAccessor)aux_object);
      String max_cardinality = lit_accessor.getValue().getString();
      log.addLine("The daml:maxCardinality is changed from "+term_relation.getMaxCardinality()+" to "+max_cardinality);
      term_relation.setMaxCardinality(max_cardinality);
    }
    catch (Exception e)
    {
      log.addLine("No daml:maxCardinality to transform");
    }

    aux_object = rest.prop_cardinality();
    try
    {
      LiteralAccessor lit_accessor=((LiteralAccessor)aux_object);
      String cardinality = lit_accessor.getValue().getString();
      log.addLine("A cardinality statement has been found, both max and min cardinalities are changed");
      log.addLine("The daml:maxCardinality is changed from "+term_relation.getMaxCardinality()+" to "+cardinality);
      log.addError("WebODE cannot handle minimum cardinalities for term relation so here exists an information lost");
      term_relation.setMaxCardinality(cardinality);
    }
    catch (Exception e)
    {
      log.addLine("No daml:cardinality to transform");
    }

    //The part of the cardinalityQ

    aux_object = rest.prop_minCardinalityQ();
    try
    {
      LiteralAccessor lit_accessor=((LiteralAccessor)aux_object);
      String min_cardinality  = lit_accessor.getValue().getString();
      log.addLine("daml:minCardinalityQ found");
      log.addError("daml:minCardinalityQ cannot be translated");
    }
    catch (Exception e)
    {
      log.addLine("No DAML:minCardinalityQ to transform");
    }

    aux_object = rest.prop_maxCardinalityQ();
    try
    {
      LiteralAccessor lit_accessor=((LiteralAccessor)aux_object);
      String max_cardinality = lit_accessor.getValue().getString();
      log.addLine("daml:maxCardinalityQ found");
      log.addError("daml:maxCardinalityQ cannot be translated");
    }
    catch (Exception e)
    {
      log.addLine("No daml:maxCardinality to transform");
    }

    aux_object = rest.prop_cardinalityQ();
    try
    {
      LiteralAccessor lit_accessor=((LiteralAccessor)aux_object);
      String cardinality = lit_accessor.getValue().getString();
      log.addLine("daml:cardinalityQ found");
      log.addError("daml:cardinalityQ cannot be translated");

    }
    catch (Exception e)
    {
      log.addLine("No daml:cardinalityQ to transform");
    }

  }


  private void _mapInstances (DAMLClass daml_class)
  {
    Iterator instances_iterator = daml_class.getInstances();
    while (instances_iterator.hasNext())
    {
      DAMLInstance instance = (DAMLInstance)instances_iterator.next();
      //	System.out.println (instance.getLocalName());
    }
  }

  private void _mapGlobalObjectProperties ()
  {
    //This method adds the TermRelations which are associated with the Properties
    //Objects that in their declarations had already defined a source and a destination
    //classes
    ISTermRelation term_relation = null;

    Enumeration object_properties_enu=conceptualization.getTermRelationDefinitions();
    while (object_properties_enu.hasMoreElements())
    {
      term_relation=((ISTermRelationDefinition) object_properties_enu.nextElement()).obtainTermRelation();
      if (!conceptualization.hasTermRelation(term_relation.getName(),
          term_relation.getOrigin(),term_relation.getDestination()));
      {
        conceptualization.addTermRelation(term_relation);
      }
    }
  }





  private void _mapPropertiesDefinitions()
  {

    log.addTitle("READING PROPERTIES DEFINITIONS",3);

    Iterator prop_it = daml_model.listDAMLProperties();
    while (prop_it.hasNext())
    {
      Object aux_obj = prop_it.next();
      log.addTitle("Reading the property "+((Resource)aux_obj).getURI()+"||",1);
      if (aux_obj instanceof DAMLObjectProperty)
      {
        log.addLine("The daml:Property "+((Resource)aux_obj).getURI()+" is a daml:ObjectProperty so it will be transformed as a WebODE:TermRelation");

        _mapObjectPropertyDefinition ((DAMLProperty)aux_obj);


      }
      else
      {
        if (aux_obj instanceof DAMLDatatypeProperty)
        {
          System.out.println("The daml:Property "+((Resource)aux_obj).getURI()+" IS a DAML:DatatypeProperty so it will be transformed (by the moment) as a WebODE:InstanceAttribute");
          _mapDatatypePropertyDefinition((DAMLDatatypeProperty)aux_obj);
          log.addLine("The daml:Property "+((Resource)aux_obj).getURI()+" IS a DAML:DatatypeProperty so it will be transformed (by the moment) as a WebODE:InstanceAttribute");
        }
        else
        {

          if ((aux_obj instanceof DAMLProperty))
          {

            if (_guessIfObjectProperty(((Resource)aux_obj).getURI()))
            {

              _mapObjectPropertyDefinition((DAMLProperty)aux_obj);
              log.addLine("The daml:Property "+((Resource)aux_obj).getURI()+" IS CLASIFIED as a DAML:ObjectProperty");
            }
            else
            {
              _mapDatatypePropertyDefinition((DAMLProperty)aux_obj);
              _mapObjectPropertyDefinition((DAMLProperty)aux_obj);
              log.addWarning("The DAML:Property "+((Resource)aux_obj).getURI()+" CANNOT BE CLASIFIED, it is classified as a DAML:DatatypeProperty");
            }
          }
          else
            log.addLine("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");






        }
      }
    }
  }



  private void _mapObjectPropertyDefinition(DAMLProperty object_property)
  {
    String relation_comments=null;
    String relation_origin=null;
    String relation_destination=null;
    String relation_max_cardinality =webode_names_container.N_CARDINALITY;
    String relation_min_cardinality = "0";
    String relation_name=null;

    if (ontology.belongsTo(object_property.getURI()))
    {
      relation_name=_resolveTermName(object_property.getURI());
      Iterator i;
      _mapSubproperties(object_property);
      _mapInverseOfProperties(object_property);
      _mapEquivalentProperties(object_property);
      int domain_counter=0;
      Iterator k=object_property.prop_range().getAll(false);
      while (k.hasNext())
      {
        domain_counter++;
        k.next();
      }

      //Both extremes of the relation are added. The idea is to a single class expression
      //(whatever it could be) or an intersection of two or more expressions
      relation_origin= _resolveRelationExtreme("DOMAIN",object_property.prop_domain().getAll(false),object_property.prop_domain().getAll(false));
      relation_destination=_resolveRelationExtreme("RANGE",object_property.prop_range().getAll(false),object_property.prop_range().getAll(false));

      //If for a global relationship there is not defined range/domain constraint, then any
      //class can be part of domain/range.
  /*	if (relation_origin==null)
    relation_origin=_resolveTermName(daml_names_container.THING);
   if (relation_destination==null)
    relation_destination=_resolveTermName(daml_names_container.THING);
  */
      //At this moment, two strings should be defined, relation_origin and relation_destination. Then, a WebODE's term relation is
      //created and added to the term relations table

      ISTermRelationDefinition term_relation_definition =
          new ISTermRelationDefinition (relation_name,relation_origin,relation_destination,relation_max_cardinality,relation_min_cardinality);
      conceptualization.addTermRelationDefinition(term_relation_definition);


      _mapDocumentation(object_property,term_relation_definition);

  /*
   ISTermRelation term_relation= new ISTermRelation(relation_name,relation_origin,relation_destination,relation_max_cardinality);
  */
      _mapProperties(object_property,term_relation_definition);


    }
  }

  private void _mapProperties(DAMLProperty property,ISTermRelationDefinition term_relation_definition)
  {
    boolean is_unambiguous=false;
    boolean is_transitive=false;
    boolean is_unique=false;

    is_unique=property.isUnique();
    if (property instanceof DAMLObjectProperty)
    {
      is_unambiguous=((DAMLObjectProperty)property).isUnambiguous();
      is_transitive=((DAMLObjectProperty)property).isTransitive();
    }

    if (is_unique)
    {
      log.addLine ("The daml:ObjectProperty "+property.getURI()+"is unique, so its maximum cardinality should set to 1");
      term_relation_definition.setMaxCardinality("1");
    }

    if (is_unambiguous)
    {
      if (!conceptualization.hasProperty("Unambigous Property"))
      {
        ISProperty unambigous_prop= new ISProperty("Unambigous Property");
        conceptualization.addProperty(unambigous_prop);
        String formula_expression="forall (?x,?y,?z) (p(?x,?y) and p(?x,?z))->(?y=?z)";
        ISFormula unambigous_formula =
            new ISFormula ("Unambigous Formula","Formula",formula_expression);
        unambigous_prop.addFormula("Unambigous Formula");
        conceptualization.addFormula(unambigous_formula);
      }
      term_relation_definition.addProperty("Unambigous Property");

    }

    if (is_transitive)
    {
      log.addLine ("The daml:ObjectProperty "+property.getURI()+" has the transitive property");
    }
  }

  private void _mapSubproperties(DAMLProperty property)
  {
    ISFormula subproperty_axiom=null;
    String property_name=null;
    String subproperty_name=property.getLocalName();
    String begining_expression="forall (?x,?y) ("+subproperty_name+"(?x,?y))->(";
    String axiom_description=null;

    try
    {
      Iterator i=property.getSuperProperties(false);
      do
      {
        property_name=_resolveTermName(((Resource)i.next()).getURI());
        log.addLine("This property is defined as the daml:subPropertyOf "+property_name);
        String axiom_expression=begining_expression+"("+property_name+"(?x,?y))";
        subproperty_axiom= new ISFormula();
        String axiom_name = webode_names_container.bindAnonymous(subproperty_axiom,webode_names_container.AXIOM);
        subproperty_axiom.setName(axiom_name);
        subproperty_axiom.setType(webode_names_container.AXIOM);
        subproperty_axiom.setExpression(axiom_expression);
        conceptualization.addFormula(subproperty_axiom);
        axiom_description="This axiom describes that the property "+property.getLocalName()
                         +" is the subproperty of the property "+property_name;
        subproperty_axiom.addDescription(axiom_description);
      }
      while (i.hasNext());
    }
    catch (Exception e)
    {
      log.addLine ("No daml:inverseOf properties to transform");
    }
  }

  private void _mapInverseOfProperties (DAMLProperty property)
  {
    if (property instanceof DAMLObjectProperty)
    {
      ISFormula inverse_of_axiom=null;
      String property_name=null;
      String inverse_name=property.getLocalName();
      String begining_expression="forall (?x,?y) ("+inverse_name+"(?x,?y))->(";
      String axiom_description=null;
      Iterator i= ((DAMLObjectProperty)property).prop_inverseOf().getAll(false);
      try
      {
        do
        {
          property_name=_resolveTermName(((Resource)i.next()).getURI());
          log.addLine("This property "+property.getLocalName()+" is defined as the daml:inverseOf "+property_name);
          String axiom_expression=begining_expression+"("+property_name+"(?y,?x))";
          inverse_of_axiom= new ISFormula();
          String axiom_name = webode_names_container.bindAnonymous(inverse_of_axiom,webode_names_container.AXIOM);
          inverse_of_axiom.setName(axiom_name);
          inverse_of_axiom.setType(webode_names_container.AXIOM);
          inverse_of_axiom.setExpression(axiom_expression);
          axiom_description="This axiom describes that the property "+property.getLocalName()
                           +"is the inverse of the property "+property_name;
          inverse_of_axiom.setDescription(axiom_description);
          conceptualization.addFormula(inverse_of_axiom);
        }
        while (i.hasNext());
      }
      catch (Exception e)
      {
        log.addLine ("No daml:inverseOf properties to transform");
      }
    }
  }

  private void _mapEquivalentProperties(DAMLProperty property)
  {
    String property_name=null;
    String equivalence_properties[]=
    {daml_names_container.EQUIVALENT_TO,daml_names_container.SAME_PROPERTY_AS};
    String equivalence_properties_short[]=
    {"daml:equivalentTo","daml:samePropertyAs"};
    ISFormula subproperty_axiom=null;
    ISFormula equivalence_axiom;
    int i;
    for (i=0;i<equivalence_properties.length;i++)
    {
      try
      {
      Enumeration enu = _getPropertyObjects(property,equivalence_properties[i]);
      do
      {
        property_name=_resolveTermName(((Resource)enu.nextElement()).getURI());
        String begining_expression="forall (?x) ("+property.getLocalName()+"(?x))<->";
        String axiom_description=null;
        String axiom_expression=begining_expression+"("+property_name+" (?x))";
        equivalence_axiom= new ISFormula();
        String axiom_name = webode_names_container.bindAnonymous(equivalence_axiom,webode_names_container.AXIOM);
        equivalence_axiom.setName(axiom_name);
        equivalence_axiom.setType(webode_names_container.AXIOM);
        equivalence_axiom.setExpression(axiom_expression);
        axiom_description="The axiom that describes that the concept "+ property.getLocalName()
                         +" is equivalent "+property_name;
        equivalence_axiom.setDescription(axiom_description);
        conceptualization.addFormula(equivalence_axiom);
        log.addLine("This concept is defined as "+equivalence_properties_short[i]+" of "+property_name);
      }
      while (enu.hasMoreElements());
    }
    catch (Exception e)
    {
      log.addLine ("No "+equivalence_properties_short[i]+" class expressions to transform");
    }
    }
  }

  private String _resolveRelationExtreme(String extreme,Iterator extreme_it, Iterator count_iterator)
  {
    String extreme_name=null;
    Object aux_object=null;
    int extreme_counter=0;

    Iterator aux_it=count_iterator;
    while (aux_it.hasNext())
    {
      aux_it.next();
      extreme_counter++;
    }

    if (extreme_counter>0)
    {



      if (extreme_counter==1)
      {
        aux_object=extreme_it.next();
        extreme_name=_mapClassExpression(aux_object,extreme_name,null,null,true);
      }

      else
      {
        System.out.println("ENTRA");
        ISConcept is_concept = new ISConcept();
        extreme_name =webode_names_container.bindAnonymous(is_concept,webode_names_container.CONCEPT);
        is_concept.setName(extreme_name);
        conceptualization.addConcept(is_concept);

        while (extreme_it.hasNext())
          _mapClassExpression(extreme_it.next(),extreme_name,webode_names_container.SUBCLASS_OF,null,false);


      }
    }

    if (extreme_name==null)
      extreme_name=_resolveTermName(daml_names_container.THING);


    return extreme_name;
  }





  private void _mapDatatypePropertyDefinition (DAMLProperty datatype_property)
  {

    String attr_description = null;
    String attr_type = null;
    String attr_min_cardinality = "0";
    String attr_max_cardinality = webode_names_container.N_CARDINALITY;
    String attr_domain=_resolveRelationExtreme("DOMAIN",datatype_property.prop_domain().getAll(false),datatype_property.prop_domain().getAll(false));

    if (ontology.belongsTo(datatype_property.getURI()))
    {
      String attr_name= _resolveTermName(datatype_property.getURI());

      Iterator i;
      i=datatype_property.getEquivalentValues();





      Iterator range_it=datatype_property.getRangeClasses();
      try
      {
        if (range_it.hasNext())
        {
          Resource r=(Resource)range_it.next();
          attr_type=webode_names_container.translateDatatype(r.getURI()); //I will use the datatype name, including it's namespace
        }
        else
          attr_type="String"; //This is already traslated to webODE's datatypes
        if (range_it.hasNext())
        {
          //If we are here, the datatype property seems to have more than a datatype.
          //This is written to the log
        }
      }
      catch (Exception e)
      {
        //System.out.println("KKKKKKKKKKKKKKKKKKK");
        e.printStackTrace();
      }

      ISAttributeDefinition attr_definition = new
          ISAttributeDefinition (attr_name,attr_domain,attr_type,attr_max_cardinality, attr_min_cardinality);
      conceptualization.addAttributeDefinition(attr_definition);

      _mapDocumentation(datatype_property,attr_definition);
      _mapSubproperties(datatype_property);
      _mapEquivalentProperties(datatype_property);

      boolean is_unique=false;

      is_unique=datatype_property.isUnique();
      if (is_unique)
      {
//	log.addLine ("The daml:DatatypeProperty "+datatype_property.getURI()+"is unique, so its maximum cardinality should set to 1");
        attr_definition.setMaxCardinality("1");
      }

//	datatype_properties_table.put(attr_name,instance_attribute);

      ISConcept ie_concept=null;
//	System.out.println("ESTE ES EL RELATION ORIGIN "+relation_origin+" DE "+attr_name);




      ISInstanceAttributeDescriptor instance_attribute=null;


      //	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>"+instance_attribute.getName()+attr_domain );
      ISConcept is_concept=null;

      if (conceptualization.hasConcept(attr_domain))
      {
        instance_attribute=attr_definition.obtainInstanceAttributeDescriptor();
        is_concept=conceptualization.getConcept(attr_domain);
        is_concept.addInstanceAttribute(instance_attribute);
      }
      else
      {
        instance_attribute=attr_definition.obtainInstanceAttributeDescriptor();
        is_concept = new ISConcept(attr_domain);
        conceptualization.addConcept(is_concept);
        is_concept.addInstanceAttribute(instance_attribute);
      }

    }
  }








  private boolean _hasPropertyVector(Vector prop_vector,String prop_name,String domain_name,String range_name)
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









  private boolean _guessIfClassObjectProperty (String class_name,String  property_name)
  {
    //System.out.println("ENTRO EN EL PROPERTY GUESSING"+property_name);
    String relations[] = {"<daml:toClass>","<daml:hasClass>","<daml:hasClassQ>","<rdfs:range>"};
    boolean isObject=false;
    int i=0;
    int j;
    while(i<relations.length)
    {
      String query_string =
          "SELECT ?destination WHERE"
          +"(?destination,<rdf:type>,<daml:Class>),"
          +"(?restriction,"+relations[i]+",?destination),"
          +"(?restriction,<daml:onProperty>,<"+property_name+">),"
          +"(?restriction,<rdf:type>,<daml:Restriction>)"
          + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
          + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
          + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";

      Query query = new Query(query_string);
      query.setSource(daml_model);
      QueryExecution qe= new QueryEngine(query);
      QueryResults results = qe.exec();
      Iterator iter=results;
      isObject=isObject||iter.hasNext();
      while (iter.hasNext())
      {
        ResultBinding res = (ResultBinding)iter.next();
        Resource resource = (Resource)res.get("destination");
        //log.addLine("---------->>>>>>>>>>>>>>>>>>>>>>>"+resource.getURI());
      }
      i++;
    }
    if (!isObject)
    {
      //System.out.println("创创创创创创创创创创创创");
      //System.out.println("NO ES UN OBJECT PROPERTY");
    }
    return isObject;



  }




  private boolean _guessIfObjectProperty (String  property_name)
  {
    //System.out.println("ENTRO EN EL OBJECT GUESSING"+property_name);
    String relation_from_property[] = {"<rdfs:range>"};
    String relation_from_restriction[]={"<daml:toClass>","<daml:hasClass>","<daml:hasClassQ>"};
    String prop_relations[]={"<rdfs:subPropertyOf>","<daml:samePropertyAs>","<daml:equivalentTo>","<daml:inverseOf>"};
    String type_class[] = {"<daml:Class>","<rdfs:Class>"};
    String query_string=null;
    boolean isObject=false;
    int i=0;
    while((i<relation_from_property.length)&&(!isObject))
    {
      int class_index=0;
      while ((class_index<type_class.length)&&(!isObject))
      {
        query_string =
            "SELECT ?destination WHERE (<"+property_name+">,"+relation_from_property[i]+",?destination),"
            + "(?destination,<rdf:type>,"+type_class[class_index]+")"
            + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
            + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
            + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
        isObject=_existStatement(query_string);
        class_index++;
      }
      i++;
    }

    if (!isObject)
    {
      i=0;
      while((i<relation_from_restriction.length)&&(!isObject))
      {
        int class_index=0;
        while ((class_index<type_class.length)&&(!isObject))
        {
          query_string =
              "SELECT ?destination WHERE"
              + "(?damlclass,<rdf:type>,"+type_class[class_index]+"),"
              + "(?restriction,"+relation_from_restriction[i]+",?damlclass),"
              + "(?restriction,<daml:onProperty>,<"+property_name+">)"
              + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
              + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
              + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
          isObject=_existStatement(query_string);
          class_index++;
        }
        i++;
      }


    }




    if (!isObject)
    {
      //	System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO "+property_name);
      int j=0;
      while((j<prop_relations.length)&&(!isObject))
      {
        query_string =
            "SELECT ?destination WHERE (<"+property_name+">,"+prop_relations[j]+",?destination),"
            + "(?destination,<rdf:type>,<daml:ObjectProperty>)"
            + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
            + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
            + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
        isObject=_existStatement(query_string);
        j++;

      }
      if (!isObject)
      {
        int k=0;
        while((k<prop_relations.length)&&(!isObject))
        {
          query_string =
              "SELECT ?origin WHERE (?origin,"+prop_relations[k]+",<"+property_name+">),"
              + "(?origin,<rdf:type>,<daml:ObjectProperty>)"
              + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
              + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>,"
              + "daml FOR <http://www.daml.org/2001/03/daml+oil#>";
          isObject=_existStatement(query_string);
          k++;
        }
      }
    }


    if (isObject)
    {
      //	System.out.println("SSIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII "+property_name);
      log.addLine("The query string "+query_string);

    }

    return isObject;


  }


  private boolean _existStatement(String query_string)
  {
    Query query = new Query(query_string);
    query.setSource(daml_model);
    QueryExecution qe= new QueryEngine(query);
    QueryResults results = qe.exec();
    Iterator iter=results;
/*	System.out.println("////////////////////////////////////////////");
 while (iter.hasNext())
 {
 ResultBinding res = (ResultBinding)iter.next()
 Resource resource = (Resource)res.get("destination");
 Resource re
 System.out.println("---------->>>>>>>>>>>>>>>>>>>>>>>"+resource.getURI());
 }
 */
    return (iter.hasNext());
  }


  private void _checkDisjointWithGroups()
  {
    //CHAPUZA ENORME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //A馻dir al ISConceptualization la capacidad de obtener grupos por tipo o  nombre o algo
    Enumeration group_enu = disjoint_with_groups.elements();

    while (group_enu.hasMoreElements())
    {
      ISGroup group=(ISGroup)group_enu.nextElement();

      Enumeration concepts_enu=conceptualization.getConcepts();
      while (concepts_enu.hasMoreElements())
      {
        ISConcept concept = (ISConcept)concepts_enu.nextElement();
        _checkDisjointWithGroup(concept,group);

      }
    }
  }

  private void _checkDisjointWithGroup (ISConcept concept, ISGroup group)
  {
    boolean subclass_of_all=true;
    String origin_name=null;
    String destination_name=concept.getName();
    Enumeration group_concepts = group.getConceptsNames();
    while (group_concepts.hasMoreElements()&&(subclass_of_all))
    {
      origin_name=(String)group_concepts.nextElement();
      ISConcept group_concept= conceptualization.getConcept(origin_name);
      origin_name=group_concept.getName();
      subclass_of_all= subclass_of_all &&
                       conceptualization.hasTermRelation(webode_names_container.SUBCLASS_OF,origin_name,destination_name);


    }

    if (subclass_of_all)
    {
      ISTermRelation term_relation= new ISTermRelation("disjoint-subclass-partition",group.getName(),destination_name,webode_names_container.N_CARDINALITY);
      conceptualization.addTermRelation(term_relation);
    }


  }

  private void _checkThingTaxonomy()
  {
    String thing_name=daml_names_container.THING;
    String nothing_name=daml_names_container.NOTHING;
    Enumeration root_concepts_enu = conceptualization.getRootConceptsNames(webode_names_container.SUBCLASS_OF);
    while (root_concepts_enu.hasMoreElements())
    {
      String root_concept_name = (String)root_concepts_enu.nextElement();

      if (!conceptualization.hasTermRelation(webode_names_container.SUBCLASS_OF,root_concept_name,thing_name)
          &&(!(thing_name.equals(root_concept_name)||(nothing_name.equals(root_concept_name)))))
      {
        ISTermRelation new_term_relation= new ISTermRelation(webode_names_container.SUBCLASS_OF,root_concept_name,thing_name,webode_names_container.N_CARDINALITY);
        conceptualization.addTermRelation(new_term_relation);
        log.addLine ("As a root concept, the "+root_concept_name+" is a child of daml:Thing");
      }
    }
  }


  private void _checkNothingTaxonomy()
  {
    String thing_name=daml_names_container.THING;
    String nothing_name=daml_names_container.NOTHING;
    Enumeration leaf_concepts_enu = conceptualization.getLeafConceptsNames(webode_names_container.SUBCLASS_OF);
    while (leaf_concepts_enu.hasMoreElements())
    {
      String leaf_concept_name = (String)leaf_concepts_enu.nextElement();



      if (!conceptualization.hasTermRelation(webode_names_container.SUBCLASS_OF,nothing_name,leaf_concept_name)
          &&(!(nothing_name.equals(leaf_concept_name)||(thing_name.equals(leaf_concept_name)))))

      {
        ISTermRelation new_term_relation= new ISTermRelation(webode_names_container.SUBCLASS_OF,nothing_name,leaf_concept_name,webode_names_container.N_CARDINALITY);
        conceptualization.addTermRelation(new_term_relation);
        log.addLine ("As a leaf concept, the daml:Nothing concept is a child of "+leaf_concept_name);

      }
    }
  }




  private String _translateValue (RDFNode node)
  {
    try
    {
      if (node instanceof Literal)
      {
        // parse the string value into an integer
        return (((Literal) node).getString() );
      }
      else
      {
        if (node instanceof Resource)
        {
          // assume we have have resource whose rdf:value is the value of the node
          RDFNode val = ((Resource) node).getProperty( RDF.value ).getObject();
          return _translateValue( val );
        }
      }
    }
    catch (RDFException e)
    {
      log.addError( "RDF exception while converting datatype instance: " + e);
      e.printStackTrace();
    }

    return null;
  }




  void _mapInstances ()
  {
    Hashtable attributes_table = new Hashtable();
    Enumeration concepts_enu = conceptualization.getConcepts();
    while (concepts_enu.hasMoreElements())
    {
      ISConcept is_concept= (ISConcept)concepts_enu.nextElement();
      attributes_table.put(is_concept.getName(),is_concept.getAttributeNames());
      //log.addLine("Atributo-> "+attributes_table);
    }

    log.addTitle("Mapping the instances",3);
    ISInstancesSet instances_set = new ISInstancesSet ("Ontology Instances");
    ontology.addInstancesSet(instances_set);

    Iterator instances_it=daml_model.listDAMLInstances();
    String type_name=null;

    while (instances_it.hasNext())
    {
      DAMLCommon daml_object = ((DAMLCommon)instances_it.next());
      Resource type_resource=(Resource)daml_object.prop_type().get();
      if (type_resource instanceof DAMLClass)
      {
        log.addLine(daml_object.toString());
        _mapInstance(daml_object,instances_set,attributes_table);
      }
    }

    /*
 Enumeration owl_individuals_enu = owl_model.getIndividuals();
 while (owl_individuals_enu.hasMoreElements())
 {
  OWLIndividual owl_individual = ((OWLIndividual)owl_individuals_enu.nextElement());
    //log.addTitle(owl_individual.getURI(),1);
 _mapIndividual (owl_individual,instances_set,attributes_table );
 }
 */
  }



  protected void _mapInstance (Resource instance_resource, ISInstancesSet instances_set,Hashtable attributes_table)
  {

    if (ontology.belongsTo(instance_resource.getURI()))
    {
      HashSet added_values=new HashSet();
      HashSet taxonomy_relations = new HashSet();
      taxonomy_relations.add(daml_names_container.TYPE);
      HashSet annotation_properties = new HashSet();
      annotation_properties.add(daml_names_container.LABEL);
      annotation_properties.add(daml_names_container.COMMENT);
      annotation_properties.add(daml_names_container.IS_DEFINED_BY);
      annotation_properties.add(daml_names_container.LABEL);



      log.addTitle("Mapping the individual "+instance_resource.getURI(),1);



      ISInstance concept_instance =
          new	ISInstance (instance_resource.getLocalName());

      instances_set.addInstance(concept_instance);
      _mapIndividualType(instance_resource,concept_instance);


      StmtIterator properties_it=null;

      try
      {
        properties_it = instance_resource.listProperties();

        while (properties_it.hasNext())
        {

          Statement property_statement = (Statement)properties_it.next();
          String property_uri=((Resource)property_statement.getPredicate()).getURI();
          log.addLine(property_statement.getPredicate().toString());


          if (!taxonomy_relations.contains(property_uri))
          {
            if (!annotation_properties.contains(property_uri))
            {

              //Enumeration properties_enu = owl_individual.getProperty(property_name);
              //while (properties_enu.hasMoreElements())
              //{
              Object property_destination = property_statement.getObject();
              log.addLine("KAKA "+property_destination.toString());
              //  	log.addLine(property_name+"----->"+property_destination.toString());
              if (property_destination instanceof DAMLInstance)
              {
                if (!_isList((Resource)property_destination,daml_model))
                {
                  String uri_reference = ((Resource)property_destination).getURI();
                  log.addLine ("Add term relation("+property_uri+") instance");

                  ISRelationInstance relation_instance = new ISRelationInstance();
                  String relation_instance_name=webode_names_container.bindAnonymous(relation_instance,webode_names_container.RELATION_INSTANCE);
                  relation_instance.setName(relation_instance_name);
                  relation_instance.setParent(_resolveTermName(property_uri));
                  relation_instance.setOrigin(_resolveTermName(instance_resource.getURI()));
                  relation_instance.setDestination(_resolveTermName(uri_reference));
                  instances_set.addRelationInstance(relation_instance);
                }

              }
              else
              {

                if (property_destination instanceof DAMLDataInstance)
                {

                  String class_concept_name=_getClassConcept(_resolveTermName(property_uri),attributes_table);
                  //		System.out.println("---------->" + property_name + " ||| "+class_concept_name);
                  if (class_concept_name!=null)
                  {
                    log.addLine("HA ENTRADO");
                    ISClass instance_class= new ISClass(class_concept_name);
                    String property_name=_resolveTermName(property_uri);
                    if (!added_values.contains(property_name))
                    {
                      added_values.add(property_name);
                      ISAttribute attr = new ISAttribute(_resolveTermName(property_uri),_translateValue((RDFNode)property_destination));
                      instance_class.addAttribute(attr);
                      concept_instance.addClass(instance_class);
                    }
                    else
                    {
                      log.addLine("An instance cannot have two values whith the same name in WebODE");
                    }
                  }
                  else
                  {
                    //There isn't any attribute ....
                  }

                }
                else
                {

                  log.addLine ("ES UN CASO RARO DE VERDAD");
                }
              }
              //}

            }
            else
            {
              //This part is the mapping of the annotations properties
              //of the instance

              Object aux_obj = property_statement.getObject();
              if (aux_obj instanceof Literal)
              {

                concept_instance.addDescription(property_uri+" : "+((Literal)aux_obj).getString()+"||");
              }
              else
              {
                if (aux_obj instanceof Resource)
                {

                  concept_instance.addDescription(property_uri+" : "+((Resource)aux_obj).getURI()+"||");
                }
              }
            }
          }
        }
      }

      catch (Exception e)
      {
      }




    }

  }



  public String _getClassConcept(String attribute_name, Hashtable attributes_table)
  {
    log.addLine("Se busca "+attribute_name);
    String concept_with_property=null;
    Enumeration concept_names_enu = attributes_table.keys();
    while (concept_names_enu.hasMoreElements () && (concept_with_property==null))
    {
      String concept_name = (String)concept_names_enu.nextElement();
      log.addLine("Busco en el concepto "+concept_name);
      HashSet attributes_set= (HashSet)attributes_table.get(concept_name);
      log.addLine ("Estos son sus atributos "+attributes_set);
      if (attributes_set.contains(attribute_name))
        concept_with_property=concept_name;
    }
    return concept_with_property;
  }



  private void _mapIndividualType (Resource instance_resource,ISInstance is_instance)
  {
    Vector instance_types = new Vector();
    Enumeration types_enu = _getPropertyObjects(instance_resource,daml_names_container.TYPE);

    while (types_enu.hasMoreElements())
    {
      instance_types.add(((Resource)types_enu.nextElement()).getURI());
    }
    if (instance_types.size()==0)
    {
      log.addWarning ("There is no type for the individual "+instance_resource.getURI()+" it is set to the default type (owl:Thing)");
      is_instance.addParentConcept(_resolveTermName(daml_names_container.THING));
    }
    else
    {
      if (instance_types.size()==1)
      {
        is_instance.addParentConcept(_resolveTermName((String)instance_types.get(0)));
      }
      else
      {

        //If the extreme is composed by more than a class expression, then it is
        ISConcept is_concept = new ISConcept();
        String concept_name =webode_names_container.bindAnonymous(is_concept,webode_names_container.CONCEPT);

        is_concept.setName(concept_name);
        conceptualization.addConcept(is_concept);
        is_instance.addParentConcept(concept_name);

        types_enu  = instance_types.elements();
        while (types_enu.hasMoreElements())
        {
          ISTermRelation is_term_relation =
              new ISTermRelation (webode_names_container.SUBCLASS_OF,concept_name,(String)types_enu.nextElement(),webode_names_container.N_CARDINALITY);
          conceptualization.addTermRelation(is_term_relation);
        }
      }
    }
  }





  private String _resolveTermName(String uri)
  {
    String local_name=null;
    String namespace=null;
    int index;
    index= uri.indexOf('#');
    namespace=uri.substring(0,index+1);
    local_name=uri.substring(index+1,uri.length());
//	System.out.println("NCNCNCNCNCNCNCNCNCNCNCNCNCnCNCNCNCNCNCNCNCNNCNC"+uri);


    ISImportedTerm imported_term=null;
    //If the element is inner to the ontology its local name is used
    if (ontology.getNamespace().equals(namespace))
    {
      //	System.out.println("Es interno");
      return local_name;
    }
    else
    {
      //	System.out.println("Es un outer "+uri+" [" +namespace+ "||"+local_name+ "]");
      //It its an outer element, it should be an imported term

      if (conceptualization.hasImportedTerm(namespace,local_name))
      {

        imported_term=conceptualization.getImportedTerm(namespace,local_name);
        //		System.out.println("Se usa el imported term  "+imported_term.getName() );
        return (imported_term.getNamespaceIdentifier()+":"+local_name);
      }
      else
      {
        if (conceptualization.hasNamespaceIdentifier(namespace))
        {
          imported_term = new ISImportedTerm();
          //imported_term.setURL(owl_named_element.getNamespace());
          imported_term.setURI(uri); //ESTO ESTA MAL, HAY QUE CAMBIARLO
          imported_term.setNamespace(namespace);
          //System.out.println("---------;;"+conceptualization.getNamespaceIdentifier(namespace));
          imported_term.setNamespaceIdentifier(conceptualization.getNamespaceIdentifier(namespace));
          conceptualization.addImportedTerm(imported_term);
          return (imported_term.getNamespaceIdentifier()+":"+local_name);
        }
        else
        {
          System.out.println("Se crea un nuevo imported term de "+uri);
          imported_term = new ISImportedTerm();
          //imported_term.setURL(owl_named_element.getNamespace());
          imported_term.setURI(uri); //ESTO ESTA MAL, HAY QUE CAMBIARLO
          imported_term.setNamespace(namespace);
          Object aux_obj= new Object();
//	  String new_namespace_id=webode_names_container.bindAnonymous(aux_obj,webode_names_container.NAMESPACE_IDENTIFIER);
          String new_namespace_id=webode_names_container.getNamespaceIdentifier(namespace);
          System.out.println("NOMBRE ID "+new_namespace_id);
          imported_term.setNamespaceIdentifier(new_namespace_id);
          conceptualization.addImportedTerm(imported_term);
          conceptualization.addNamespaceIdentifier(namespace,new_namespace_id);
          return (imported_term.getNamespaceIdentifier()+":"+local_name);
        }
      }
    }



  }




  private void _checkImportedTerms ()
  {
    Enumeration concepts_enu = conceptualization.getConcepts();
    while (concepts_enu.hasMoreElements())
    {
      ISConcept concept = (ISConcept)concepts_enu.nextElement();
      if (conceptualization.hasImportedTermWithName(concept.getName()))
      {
        System.out.println ("Se borra el concepto "+ concept.getName());
        conceptualization.removeConcept(concept.getName());
      }
      else
        System.out.println("No se borra el concepto "+concept.getName());
    }
  }

  private boolean _isList(Resource resource,Model model)
  {
    String query_string = 	"SELECT ?origin WHERE"
                        + "(?origin,<rdf:type>,<rdf:List>)"
                        + "AND (?origin eq <"+resource+">)"
                        + "USING rdf FOR <http://www.w3.org/1999/02/22-rdf-syntax-ns#>,"
                        + "rdfs FOR <http://www.w3.org/2000/01/rdf-schema#>";

    Iterator res_it=_queryRDFModel (query_string,model);
    if (res_it.hasNext())
    {
      //System.out.println("ESTTE ES EL CLASS "+res_it.next());
      return true;
    }
    return (res_it.hasNext());
  }


  protected  QueryResults _queryRDFModel (String query_string, Model model)
  {
    Query query = new Query(query_string);
    query.setSource(model);
    QueryExecution qe= new QueryEngine(query);
    QueryResults results = qe.exec();
    return results;
  }





}

