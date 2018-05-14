package es.upm.fi.dia.ontology.webode.translat.internalstructure;

//Jena related imports
//The
import es.upm.fi.dia.ontology.webode.translat.logs.*;
import es.upm.fi.dia.ontology.webode.translat.namescontainers.*;
import es.upm.fi.dia.ontology.webode.translat.owlapi.*;
import java.io.*;
import java.util.*;


class OWLDLISStructureImpl extends ISStructureImpl
{
  //Attributes
  private ISConceptualization conceptualization;

  private OWLModel owl_model;
  private WEBODENamesContainer webode_names_container;
  private OWLNamesContainer owl_names_container;




  public OWLDLISStructureImpl (Reader in,String name, String namespace)

  {
    log =new LogTextImpl();

    webode_names_container=new WEBODENamesContainer(namespace);
    owl_names_container=new OWLNamesContainer();
    //System.out.println("Leo el mdelo");
    owl_model=new OWLModel();
    owl_model.read(in,namespace);
//	owl_model.showStatements();


    _mapOntology(name,namespace);
//	_mapSpecialClasses();
    _mapPropertiesDefinitions();
    _mapClasses();


//	_checkThingTaxonomy();
//	_checkNothingTaxonomy();

    _addGlobalObjectProperties();
    _checkImportedTerms ();
    _mapIndividuals();

    //  _checkDisjointWithGroups();




  }
  private void _mapSpecialClasses ()
  {
    log.addLine ("Adding the classes: owl:Thing and owl:Nothing");
    ISConcept thing_concept = new ISConcept(_resolveTermName(owl_names_container.THING));
    ISConcept nothing_concept = new ISConcept(_resolveTermName(owl_names_container.NOTHING));
    conceptualization.addConcept(thing_concept);
    conceptualization.addConcept(nothing_concept);
  }


  private void _mapOntology(String name,String namespace)
  {

    OWLOntology owl_ontology;
    owl_ontology= owl_model.getOntology();
    String date =(new Date(System.currentTimeMillis())).toString();
    log.addTitle ("Adding the ontology Header and Version Info. Wrapping time"+date,3);
    ontology=new ISOntology(name,namespace,date);



    conceptualization=ontology.getConceptualization();

    ontology.setAuthor("ODEWrapper(OWL)");
//        ontology.setName(name);

    String ontology_properties[]=
    {owl_names_container.PRIOR_VERSION,owl_names_container.BACKWARD_COMPATIBLE_WITH,owl_names_container.INCOMPATIBLE_WITH};

    String properties_abbreviations[]={"owl:priorVersion:","owl:backwardCompatibleWith","owl:incompatibleWith"};

    int index=0;
    while (index<ontology_properties.length)
    {
      Enumeration enu = owl_ontology.getProperty(ontology_properties[index]);

      while (enu.hasMoreElements())
      {
	OWLCommon owl_element=(OWLCommon)enu.nextElement();
	if (owl_element instanceof OWLDataValue)
	{
	  OWLDataValue data_value=(OWLDataValue)owl_element;
	  ontology.addDescription(properties_abbreviations[index]+data_value.getValue()+"  ");
	}
	else
	{
	  if (owl_element instanceof OWLNamedElement)
	  {
	    ontology.addDescription(properties_abbreviations[index]+((OWLNamedElement)owl_element).getURI()+"  ");
	  }
	}
      }

      index++;
    }
    _mapDocumentation(owl_ontology,ontology);
  }

  private void _mapDocumentation (OWLOntology owl_ontology, ISStructureElement is_element)
  {

    Enumeration annotation_properties = owl_model.getAnnotationProperties();
    while (annotation_properties.hasMoreElements())
    {
      String annotation_tag = (String)annotation_properties.nextElement();
      Enumeration enu = owl_ontology.getProperty(annotation_tag);

      while (enu.hasMoreElements())
      {
	Object aux_obj = enu.nextElement();
	if (aux_obj instanceof OWLDataValue)
	{
	  OWLDataValue data_value=(OWLDataValue) aux_obj;
	  is_element.addDescription(annotation_tag+" : "+data_value.getValue()+"||");
	}
	else
	{
	  if (aux_obj instanceof OWLNamedElement)
	  {
	    OWLNamedElement named_element = (OWLNamedElement)aux_obj;
	    is_element.addDescription(annotation_tag+" : "+named_element.getURI()+"||");
	  }
	}
      }
    }
  }

  private void _mapClasses()
  {
    log.addTitle ("Adding the ontology concepts",3);
    Enumeration classes_enu = owl_model.getClasses();
    while (classes_enu.hasMoreElements())
    {
      OWLClass owl_class = (OWLClass) classes_enu.nextElement();
      _mapClass(owl_class);
    }
  }


  private void _mapClass(OWLClass owl_class)
  {
    if (ontology.belongsTo(owl_class.getURI()))
    {
      ISConcept is_concept=null;
      String class_name=null;
      class_name=owl_class.getLocalName();
      log.addTitle("Mapping the owl:class "+owl_class.getURI()+" into a WebODE:Concept",3);
      //log.addTitle("Translating or adding new restrictions to the class "+class_name,1);
      if (!conceptualization.hasConcept(class_name))
      {
	is_concept = new ISConcept(class_name);
	conceptualization.addConcept(is_concept);
      }
      else
      {
	is_concept =conceptualization.getConcept(class_name);
      }
      _mapDocumentation(owl_class,is_concept);
      _mapSubclassofProperties(owl_class);
      _mapDisjointWith(owl_class);
      _mapEnumeration(owl_class);
      _mapIntersectionOf(owl_class);
      _mapComplementOf (owl_class);
      _mapUnionOf(owl_class);
      _mapEquivalentClass(owl_class);
    }
  }


  private void _mapDocumentation (OWLClass owl_class, ISStructureElement is_element)
  {

    Enumeration annotation_properties = owl_model.getAnnotationProperties();
    while (annotation_properties.hasMoreElements())
    {
      String annotation_tag = (String)annotation_properties.nextElement();
      Enumeration enu = owl_class.getProperty(annotation_tag);

      while (enu.hasMoreElements())
      {
	Object aux_obj = enu.nextElement();
	if (aux_obj instanceof OWLDataValue)
	{
	  OWLDataValue data_value=(OWLDataValue) aux_obj;
	  is_element.addDescription(annotation_tag+" : "+data_value.getValue()+"||");
	}
	else
	{
	  if (aux_obj instanceof OWLNamedElement)
	  {
	    OWLNamedElement named_element = (OWLNamedElement)aux_obj;
	    is_element.addDescription(annotation_tag+" : "+named_element.getURI()+"||");
	  }
	}
      }
    }
  }

  private void _mapSubclassofProperties(OWLClass owl_class)
  {
    //System.out.println("--------------"+owl_class.getURI());
    String subclassof_properties[]=
    {owl_names_container.SUBCLASS_OF};

    int index=0;
    while (index<subclassof_properties.length)
    {
      Enumeration enu = owl_class.getProperty(subclassof_properties[index]);
      if (enu.hasMoreElements())
      {
	while (enu.hasMoreElements())
	{
	  OWLClassDescription class_description=(OWLClassDescription) enu.nextElement();
	  _mapClassDescription(class_description,owl_class.getLocalName(),webode_names_container.SUBCLASS_OF,null,false);
	  log.addWarning(subclassof_properties[index]+" is trasnformed into a WebODE:Subclass-of");
	}
      }
      else
      {
	log.addLine ("No "+subclassof_properties[index]+" to transform");
      }
      index++;
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
    //System.out.println(namespace+"|||"+local_name);


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

/*
private String _resolveTermName(String uri)
{
	String local_name=null;
	String namespace=null;
	int index;
	index= uri.indexOf('#');
	namespace=uri.substring(0,index+1);
	local_name=uri.substring(index+1,uri.length());
  //System.out.println(namespace+"|||"+local_name);


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
			return imported_term.getName();
		}
		else
		{
  //		System.out.println("Se crea un nuevo imported term de "+uri);
			imported_term = new ISImportedTerm();
  //imported_term.setURL(owl_named_element.getNamespace());
			imported_term.setURL("webode://"+namespace.replaceAll("http://","")); //ESTO ESTA MAL, HAY QUE CAMBIARLO
			imported_term.setOriginalName(local_name);
			imported_term.setName(local_name+" ("+webode_names_container.bindAnonymous(imported_term,webode_names_container.IMPORTED_TERM)+")");
			conceptualization.addImportedTerm(imported_term);
			return imported_term.getName();
		}
	}



}

*/


  private void _mapIntersectionOf (OWLClass owl_class)
  {
    String expression_name=null;

    try
    {
      Enumeration enu = owl_class.getProperty(owl_names_container.INTERSECTION_OF);


      while (enu.hasMoreElements())
      {
	String axiom_expression="forall (?x) ("+owl_class.getLocalName()+"(?x))->(";
	OWLIntersection owl_intersection=(OWLIntersection)enu.nextElement();
	Enumeration class_descriptions_enu=owl_intersection.getOperands();
	while (class_descriptions_enu.hasMoreElements())
	{
	  OWLClassDescription class_description = (OWLClassDescription)class_descriptions_enu.nextElement();
	  expression_name=_mapClassDescription(class_description,null,null,null,true);
	  if (expression_name!=null)
	  {
	    axiom_expression=axiom_expression+"("+expression_name+"(?x))";
	    if (class_descriptions_enu.hasMoreElements())
       axiom_expression=axiom_expression + (" and ");
	  }
	}
	axiom_expression=axiom_expression+")";
	ISFormula intersection_axiom = new ISFormula();
	String axiom_name=webode_names_container.bindAnonymous(intersection_axiom,webode_names_container.AXIOM);

	intersection_axiom.setName(axiom_name);
	intersection_axiom.setType(webode_names_container.AXIOM);
	intersection_axiom.setExpression(axiom_expression);

	intersection_axiom.setDescription("The axiom which describes the intersection for the concept "+owl_class.getURI());
	conceptualization.addFormula(intersection_axiom);
	log.addLine ("The WebODE:Formula Axiom "+axiom_name+" is added to the conceptualization");

      }
    }
    catch (Exception e)	{}
  }







  private String	_mapClassDescription(OWLClassDescription owl_class_description,String domain_axiom,
      String kind_of_relation,String alternative_name,

      boolean on_rest_generate)
  {

//	System.out.println("ADDING: "+kind_of_relation+"::"+domain_class+"/"+daml_expression);
    String class_description_name=null;

    //log.addLine("Entra con "+kind_of_relation+" "+owl_class_description);

    if ((owl_class_description instanceof OWLClass)||(owl_class_description instanceof OWLURIReference))
    {
      //The ClassDescription seems to be a OWL class
      //log.addLine ("Adding the named daml:ClassExpression "+((DAMLCommon)daml_expression).getLocalName());
      if (kind_of_relation!=null)
      {
	ISTermRelation new_relation =
	    new ISTermRelation(kind_of_relation,domain_axiom,_resolveTermName(owl_class_description.getURI()),webode_names_container.N_CARDINALITY);
	log.addLine("Adding the WebODE:TermRelation");
	log.addWarning("The WebODE:TermRelation is added with the default maxCardinality (N)");
	conceptualization.addTermRelation(new_relation);
      }
      if (owl_class_description instanceof OWLClass)
	_mapClass((OWLClass)owl_class_description);
      return _resolveTermName(owl_class_description.getURI());
    }

    if (owl_class_description instanceof OWLClassOperator)
    {
      Enumeration operands_enu = ((OWLClassOperator)owl_class_description).getOperands();
      while (operands_enu.hasMoreElements())
      {
	OWLClassDescription operand=(OWLClassDescription) operands_enu.nextElement();
	_mapClassDescription(operand,domain_axiom,kind_of_relation,null,false);
      }
      return null;
    }
    if (owl_class_description instanceof OWLRestriction)
    {
//	System.out.println("AQUI RESTRICCION SOBRE"+domain_axiom);
      if (!on_rest_generate)
      {

	ISConcept is_concept =conceptualization.getConcept(domain_axiom);
	_mapRestrictionProperty(is_concept,domain_axiom,(OWLRestriction) owl_class_description);
	return null;
      }
      else
      {
	ISConcept is_concept_rest = new ISConcept();
	class_description_name=webode_names_container.bindAnonymous(is_concept_rest,webode_names_container.CONCEPT);
	is_concept_rest.setName(class_description_name);
	conceptualization.addConcept(is_concept_rest);
	_mapRestrictionProperty(is_concept_rest,class_description_name,(OWLRestriction)owl_class_description);

	if (kind_of_relation!=null)
	{
	  ISTermRelation new_relation = new ISTermRelation(kind_of_relation,domain_axiom,class_description_name,webode_names_container.N_CARDINALITY);
	  conceptualization.addTermRelation(new_relation);
	}
	return class_description_name;
      }


    }

    return class_description_name;

  }


  private void _mapRestrictionProperty (ISConcept is_concept, String domain_axiom,OWLRestriction restriction)
  {
    String property_name = null;
    boolean defined_as_datatype=false;
    boolean defined_as_object=false;

    property_name=
	_resolveTermName(((OWLURIReference)restriction.getFirstOfProperty(owl_names_container.ON_PROPERTY)).getURI());



    log.addLine ("Adding owl:Restriction on property "+property_name);


    if ((conceptualization.hasAttributeDefinition(property_name)||is_concept.hasInstanceAttribute(property_name))
	||is_concept.hasClassAttribute(property_name))
    {
      //	System.out.println("ENTRA");
      defined_as_datatype=true;
      log.addLine (property_name+" was defined as a daml:DatatypeProperty");
      _mapDatatypeRestriction(is_concept,domain_axiom,restriction);
    }

    if (conceptualization.hasTermRelationDefinition(property_name)||
	is_concept.hasTermRelation(property_name))
    {
      defined_as_object=true;
      log.addLine (property_name+" was defined as a daml:ObjectProperty");
      _mapObjectRestriction(is_concept,domain_axiom,restriction);
    }
  }


  private void _mapDatatypeRestriction(ISConcept is_concept, String domain_name, OWLRestriction restriction)
  {
    ISAttributeDescriptor is_attr;
    Object aux_obj;


    //First of all we should now the property that is afected by this restriction
    String attr_name=_resolveTermName(((OWLURIReference)restriction.getFirstOfProperty(owl_names_container.ON_PROPERTY)).getURI());

    log.addTitle("Transforming datatype restriction on daml:Property "+attr_name,1);

    if (is_concept.hasInstanceAttribute(attr_name))
      is_attr=is_concept.getInstanceAttribute(attr_name);
    else
    {
      if (is_concept.hasClassAttribute(attr_name))
	is_attr=is_concept.getClassAttribute(attr_name);
      else
      {
	if (conceptualization.hasAttributeDefinition(attr_name))
	{
	  is_attr=(conceptualization.getAttributeDefinition(attr_name)).obtainInstanceAttributeDescriptor();
	}
	else
	{
	  is_attr=new ISInstanceAttributeDescriptor (attr_name,"String","0",webode_names_container.N_CARDINALITY);
	}
	is_concept.addInstanceAttribute((ISInstanceAttributeDescriptor)is_attr);
      }
    }


    OWLCommon owl_element = restriction.getFirstOfProperty(owl_names_container.HAS_VALUE);
    if (owl_element!=null)
    {
      if (owl_element instanceof OWLDataValue)
      {
	String value = ((OWLDataValue)owl_element).getValue();
	log.addLine("Transforming a owl:hasValue, adding "+value+" on atribute"+attr_name);
	if (is_attr instanceof ISInstanceAttributeDescriptor)
	{
	  ISInstanceAttributeDescriptor instance_attr=((ISInstanceAttributeDescriptor)is_attr);
	  is_attr=instance_attr.upgradeToClassAttribute();
	  is_concept.deleteInstanceAttribute(attr_name);
	  is_concept.addClassAttribute((ISClassAttributeDescriptor)is_attr);

	}
	//Whether it already was a class attribute or not, the new value is added
	is_attr.addValue(value);
      }
      else
      {
	log.addError("The owl:hasValue in a restriction on a owl:DatatypeProperty must contain a data value");
      }
    }
    else
    {
      log.addLine("owl:hasValue not present at this owl:Restriction");
    }



    owl_element = restriction.getFirstOfProperty(owl_names_container.ALL_VALUES_FROM);
    if (owl_element!=null)
    {
      if (owl_element instanceof OWLURIReference)
      {
	String range_name=((OWLURIReference)owl_element).getURI();
	range_name= webode_names_container.translateDatatype(range_name);
	String type_name = is_attr.getType();
	if (!(type_name.equals(range_name)))
	{
	  log.addLine("The owl:range is updated from "+type_name+" to "+range_name);
	  log.addWarning("There was a change of type in the owl:DatatypeProperty "+attr_name);
	}
	is_attr.setType(range_name);


      }
      else
      {
	log.addError("The owl:allValuesFrom in a restriction over an owl:DatatypeProperty must contain reference to a datatype");
      }
    }
    else
    {
      log.addLine("owl:allValuesFrom not present at this owl:Restriction");
    }
    owl_element = restriction.getFirstOfProperty(owl_names_container.SOME_VALUES_FROM);
    if (owl_element!=null)
    {
      if (owl_element instanceof OWLURIReference)
      {
	String range_name=((OWLURIReference)owl_element).getURI();
	range_name= webode_names_container.translateDatatype(range_name);
	String type_name = is_attr.getType();
	if (!(type_name.equals(range_name)))
	{
	  log.addLine("The owl:range is updated from "+type_name+" to "+range_name);
	  log.addWarning("There was a change of type in the owl:DatatypeProperty "+attr_name);
	}
	is_attr.setType(range_name);

	if (is_attr.getMinCardinality().equals("0"))
   is_attr.setMinCardinality(1);
      }
      else
      {
	log.addError("owl:someValuesFrom in a restriction over an owl:DatatypeProperty must contain reference to a datatype");
      }
    }
    else
    {
      log.addLine("owl:someValuesFrom not present at this owl:Restriction");
    }
    _updateAttributeDescriptor (is_attr,restriction);
  }


  private void _updateAttributeDescriptor (ISAttributeDescriptor attr_descriptor,OWLRestriction restriction)
  {
    //Now we check each of the possible fields that can change a OWL restriction


    OWLDataValue owl_data_value=null;
    owl_data_value = (OWLDataValue)restriction.getFirstOfProperty(owl_names_container.MIN_CARDINALITY);
    if (owl_data_value!=null)
    {
      String min_cardinality=owl_data_value.getValue();
      log.addLine("owl:minCardinality found");
      log.addLine("The owl:minCardinality is changed from "+attr_descriptor.getMinCardinality()+" to "+min_cardinality);
      attr_descriptor.setMinCardinality(min_cardinality);
    }
    else
      log.addLine("No owl:minCardinality to transform");

    owl_data_value=null;
    owl_data_value = (OWLDataValue)restriction.getFirstOfProperty(owl_names_container.MAX_CARDINALITY);
    if (owl_data_value!=null)
    {
      String max_cardinality=owl_data_value.getValue();
      log.addLine("owl:maxCardinality found");
      log.addLine("The owl:maxCardinality is changed from "+attr_descriptor.getMaxCardinality()+" to "+max_cardinality);
      attr_descriptor.setMaxCardinality(max_cardinality);
    }
    else
      log.addLine("No owl:maxCardinality to transform");



    owl_data_value=null;
    owl_data_value = (OWLDataValue)restriction.getFirstOfProperty(owl_names_container.CARDINALITY);
    if (owl_data_value!=null)
    {
      String cardinality=owl_data_value.getValue();
      log.addLine("owl:cardinality found");
      log.addLine("The owl:maxCardinality is changed from "+attr_descriptor.getMaxCardinality()+" to "+cardinality);
      log.addLine("The owl:minCardinality is changed from "+attr_descriptor.getMinCardinality()+" to "+cardinality);
      attr_descriptor.setMinCardinality(cardinality);
      attr_descriptor.setMaxCardinality(cardinality);
    }
    else
      log.addLine("No owl:cardinality to transform");
  }






  private void _addGlobalObjectProperties ()
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


  private void _mapObjectRestriction(ISConcept is_concept, String domain_name, OWLRestriction restriction)
  {

    System.out.println("///////////////////////OBJECT RESTRICTION///////////////////");

    Object aux_obj;
    boolean well_defined=false;
    String prop_name=_resolveTermName(((OWLURIReference)restriction.getFirstOfProperty(owl_names_container.ON_PROPERTY)).getURI());
    ISTermRelation term_relation=null;
    String range_name =null;
    log.addTitle("Transforming a restriction on the daml:Property "+prop_name+" for the daml:Class "+domain_name,1);

    System.out.println("Este es el concept "+is_concept.getName()+" y esta la propiedad "+prop_name);

    //Now we check the range of this restriction
    well_defined=true;

    OWLCommon owl_element = restriction.getFirstOfProperty(owl_names_container.ALL_VALUES_FROM);
    if (owl_element!=null)
    {
      if (owl_element instanceof OWLDataRange)
      {
	log.addError("An owl:ObjectProperty cannot have a owl:DataRange as range");
	well_defined=false;
      }
      else
      {
	range_name=_mapClassDescription((OWLClassDescription)owl_element,domain_name,null,null,true);
	log.addLine("owl:allValuesFrom found, pointing to "+range_name);

      }
    }
    else
      log.addLine("owl:allValuesFrom not present at this daml:Restriction");

    owl_element = restriction.getFirstOfProperty(owl_names_container.SOME_VALUES_FROM);
    if (owl_element!=null)
    {
      if (owl_element instanceof OWLDataRange)
      {
	log.addError("An owl:ObjectProperty cannot have a owl:DataRange as range");
	well_defined=false;
      }
      else
      {
	range_name=_mapClassDescription((OWLClassDescription)owl_element,domain_name,null,null,true);
	log.addLine("owl:someValuesFrom found, pointing to "+range_name);
      }
    }
    else
      log.addLine("owl:someValuesFrom not present at this daml:Restriction");


    owl_element = restriction.getFirstOfProperty(owl_names_container.HAS_VALUE);
    if (owl_element!=null)
    {
      if (owl_element instanceof OWLDataValue)
      {
	log.addError("An owl:ObjectProperty cannot have a owl:DataValue as range");
      }
      else
      {
	log.addError("A relation between a WebODE:Concept and an individual cannot be made");
      }
      well_defined=false;
    }
    else
      log.addLine("owl:hasValue not present at this daml:Restriction");


    if (well_defined && range_name==null)
      range_name=_resolveTermName(owl_names_container.THING);

    if (well_defined)
      System.out.println("Esta mal definidda");
    if (well_defined)
    {
      if (!is_concept.hasTermRelation(prop_name,domain_name,range_name))
      {
	log.addLine ("The WebODE:TermRelation "+prop_name+"("+domain_name+","+range_name+") doesn't exist");

	if (conceptualization.hasTermRelationDefinition(prop_name))
	{
	  log.addLine("The property "+prop_name+" was globally defined");
	  term_relation= (conceptualization.getTermRelationDefinition(prop_name)).obtainTermRelation();

	  String global_range=term_relation.getDestination();
	  String global_dominion=term_relation.getOrigin();
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
	is_concept.addTermRelation (term_relation);
	conceptualization.addTermRelation(term_relation);
      }
      else
      {
	log.addLine("The property "+prop_name+" has a WebODE:TermRelation already defined for this domain and range");
	term_relation = is_concept.getTermRelation(prop_name,domain_name,range_name);
      }

      log.addLine ("Updating cardinalities of the WebODE:TermRelation");
      _updateTermRelation(term_relation,restriction);
    }


  }

  private void _updateTermRelation (ISTermRelation term_relation,OWLRestriction restriction)
  {

    OWLDataValue owl_data_value=null;
    owl_data_value = (OWLDataValue)restriction.getFirstOfProperty(owl_names_container.MIN_CARDINALITY);
    if (owl_data_value!=null)
    {
      String min_cardinality=owl_data_value.getValue();
      term_relation.setMinCardinality(min_cardinality);
      log.addLine("owl:minCardinality found");
      log.addError("WebODE cannot handle minimum cardinalities for term relation so here exists an information lost");
    }
    else
      log.addLine("No owl:minCardinality to transform");


    owl_data_value=null;
    owl_data_value = (OWLDataValue)restriction.getFirstOfProperty(owl_names_container.MAX_CARDINALITY);
    if (owl_data_value!=null)
    {
      String max_cardinality=owl_data_value.getValue();
      log.addLine("owl:maxCardinality found");
      log.addLine("The owl:maxCardinality is changed from "+term_relation.getMaxCardinality()+" to "+max_cardinality);
      term_relation.setMaxCardinality(max_cardinality);
    }
    else
      log.addLine("No owl:maxCardinality to transform");


    owl_data_value=null;
    owl_data_value =(OWLDataValue) restriction.getFirstOfProperty(owl_names_container.CARDINALITY);
    if (owl_data_value!=null)
    {
      String cardinality=owl_data_value.getValue();
      log.addLine("owl:cardinality found");
      log.addLine("The owl:maxCardinality is changed from "+term_relation.getMaxCardinality()+" to "+cardinality);
      term_relation.setMinCardinality(cardinality);
      term_relation.setMaxCardinality(cardinality);
    }
    else
      log.addLine("No owl:cardinality to transform");
  }




/*CAMBIAR LOS NOMBRES DE LAS VARIABLES*/

  private void _mapComplementOf (OWLClass owl_class)
  {

    try
    {
      Enumeration enu = owl_class.getProperty(owl_names_container.COMPLEMENT_OF);

      while (enu.hasMoreElements())
      {
	String complement_name=
	    _mapClassDescription((OWLClassDescription)enu.nextElement(),owl_class.getURI(),null,null,true);

	ISFormula complement_axiom=null;

	String begining_expression="forall (?x) ("+owl_class.getLocalName()+"(?x))<->not";
	String axiom_description=null;
	String axiom_expression=begining_expression+"("+complement_name+" (?x))";
	complement_axiom= new ISFormula();
	String axiom_name = webode_names_container.bindAnonymous(complement_axiom,webode_names_container.AXIOM);
	complement_axiom.setName(axiom_name);
	complement_axiom.setType(webode_names_container.AXIOM);
	complement_axiom.setExpression(axiom_expression);
	axiom_description="The axiom that describes that the concept "+ owl_class.getLocalName()
		  +" is the complement of "+complement_name;
	complement_axiom.setDescription(axiom_description);
	conceptualization.addFormula(complement_axiom);
	log.addLine("This concept is defined as the owl:complementOf "+complement_name);
      }
    }
    catch (Exception e)
    {
      log.addLine ("No owl:complementOF class expressions to transform");
    }


  }


  private void _mapEquivalentClass (OWLClass owl_class)
  {

    try
    {
      Enumeration enu = owl_class.getProperty(owl_names_container.EQUIVALENT_CLASS);

      while (enu.hasMoreElements())
      {
	String equivalent_name=
	    _mapClassDescription((OWLClassDescription)enu.nextElement(),owl_class.getLocalName(),null,null,true);

	ISFormula equivalent_axiom=null;

	String begining_expression="forall (?x) ("+owl_class.getLocalName()+"(?x))<->";
	String axiom_description=null;


	String axiom_expression=begining_expression+"("+equivalent_name+" (?x))";
	equivalent_axiom= new ISFormula();
	String axiom_name = webode_names_container.bindAnonymous(equivalent_axiom,webode_names_container.AXIOM);
	equivalent_axiom.setName(axiom_name);
	equivalent_axiom.setType(webode_names_container.AXIOM);
	equivalent_axiom.setExpression(axiom_expression);
	axiom_description="The axiom that describes that the concept "+ owl_class.getLocalName()
		  +" is equivalent to "+equivalent_name;
	equivalent_axiom.setDescription(axiom_description);
	conceptualization.addFormula(equivalent_axiom);
	log.addLine("This concept is defined as the owl:equivalentClass "+equivalent_name);
      }
    }
    catch (Exception e)
    {
      log.addLine ("No owl:equivalentClass class expressions to transform");
    }

  }











  private void _mapEnumeration(OWLClass owl_class)
  {
    ISInstancesSet instances_set =null;
//	log.addTitle("........................",1);
    //A class description can have an enumeration statement, so it must be properly
    //treated as well


    try
    {
      Enumeration enu = owl_class.getProperty(owl_names_container.ONE_OF);
      //Enumeration enu = _getPropertyObjects(daml_class,daml_namespace+"oneOf");
      if (enu.hasMoreElements())
      {
			/*ESTO CAMBIARLO
			log.addLine ("A WebODE:InstanceSet is created to add the instances of the enumeration");
			instances_set = new ISInstancesSet("Enumeration of"+owl_class.getURI());
			ontology.addInstancesSet(instances_set);
			*/
	while (enu.hasMoreElements())
	{

	  String axiom_expression="forall (?x) ("+owl_class.getLocalName()+"(?x))->(";

	  OWLEnumeration owl_enumeration =(OWLEnumeration) enu.nextElement();
	  Enumeration instances_enu= owl_enumeration.getInstances();
	  //	log.addLine("-----LLLLLLL "+instances_enu.toString());
	  while (instances_enu.hasMoreElements())
	  {

	    OWLIndividual owl_instance = ((OWLIndividual)instances_enu.nextElement());
	    axiom_expression=axiom_expression +("(?x="+_resolveTermName(owl_instance.getURI())+")");
	    //instances_set.addInstance(ie_instance);
	    if (instances_enu.hasMoreElements())
       axiom_expression=axiom_expression + (" or ");

	  }
	  axiom_expression=axiom_expression+(")");

	  ISFormula enumeration_axiom = new ISFormula();
	  String axiom_name=webode_names_container.bindAnonymous(enumeration_axiom,webode_names_container.AXIOM);
	  enumeration_axiom.setName(axiom_name);
	  enumeration_axiom.setType(webode_names_container.AXIOM);
	  enumeration_axiom.setExpression(axiom_expression);

	  enumeration_axiom.setDescription("This axiom describes an enumeration for the concept "+owl_class.getLocalName());
	  conceptualization.addFormula(enumeration_axiom);
	  log.addLine ("The WebODE:Formula Axiom "+axiom_name+" is added to the conceptualization");
	}

      }
    }
    catch (Exception e)
    {
      log.addLine("No owl:oneOf statements to transform");
    }

  }


  public void _mapDisjointWith (OWLClass owl_class)
  {
    try
    {
      Enumeration enu = owl_class.getProperty(owl_names_container.DISJOINT_WITH);

      while (enu.hasMoreElements())
      {
	ISGroup disjoint_with_group= new ISGroup();
	String group_name = webode_names_container.bindAnonymous(disjoint_with_group,webode_names_container.GROUP);
	disjoint_with_group.setName(group_name);
	disjoint_with_group.addConcept(owl_class.getLocalName());
	disjoint_with_group.setDescription("The group which describes an disjoint partition for the concept "+owl_class.getURI());
	String expression_name=	_mapClassDescription((OWLClassDescription)enu.nextElement(),null,null,null,true);
	disjoint_with_group.addConcept(expression_name);
	log.addLine("The class description "+expression_name+"is added to the WebODE:Group "+group_name);
	conceptualization.addGroup(disjoint_with_group,owl_names_container.DISJOINT_WITH);
      }
    }
    catch (Exception e)
    {
      log.addLine ("No owl:disjointWith class expressions to transform");
    }
  }


  private void _checkDisjointWithGroups()
  {

    Enumeration group_enu = conceptualization.getGroupsByType(owl_names_container.DISJOINT_WITH);

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
    //System.out.println("CHECKING "+concept.getName());
    boolean subclass_of_all=true;
    String origin_name=null;
    String destination_name=concept.getName();
    Enumeration group_concepts = group.getConceptsNames();
    while (group_concepts.hasMoreElements()&&(subclass_of_all))
    {
      origin_name=(String)group_concepts.nextElement();
      //System.out.println(origin_name);
      if (conceptualization.hasConcept(origin_name))
      {
	ISConcept group_concept= conceptualization.getConcept(origin_name);
	origin_name=group_concept.getName();
	subclass_of_all= subclass_of_all &&
		  conceptualization.hasTermRelation(webode_names_container.SUBCLASS_OF,origin_name,destination_name);
      }
      else
	subclass_of_all=false;
    }

    if (subclass_of_all)
    {
      ISTermRelation term_relation= new ISTermRelation("Disjoint",group.getName(),destination_name,webode_names_container.N_CARDINALITY);
      conceptualization.addTermRelation(term_relation);
    }
    //METER EL BORRAR LOS SUBCLASSOF
  }


  private void _mapUnionOf (OWLClass owl_class)
  {
    String expression_name=null;

    try
    {
      Enumeration enu = owl_class.getProperty(owl_names_container.UNION_OF);


      while (enu.hasMoreElements())
      {
	String axiom_expression="forall (?x) ("+owl_class.getLocalName()+"(?x))->(";
	OWLUnion owl_union=(OWLUnion)enu.nextElement();
	Enumeration class_descriptions_enu=owl_union.getOperands();
	while (class_descriptions_enu.hasMoreElements())
	{
	  //log.addLine(axiom_expression);
	  OWLClassDescription class_description = (OWLClassDescription)class_descriptions_enu.nextElement();
	  expression_name=_mapClassDescription(class_description,null,null,null,true);
	  //log.addLine(expression_name);
	  if (expression_name!=null)
	  {
	    axiom_expression=axiom_expression+"("+expression_name+"(?x))";
	    if (class_descriptions_enu.hasMoreElements())
       axiom_expression=axiom_expression + (" or ");
	  }

	}
	axiom_expression=axiom_expression+")";
	ISFormula enumeration_axiom = new ISFormula();
	String axiom_name=webode_names_container.bindAnonymous(enumeration_axiom,webode_names_container.AXIOM);

	enumeration_axiom.setName(axiom_name);
	enumeration_axiom.setType(webode_names_container.AXIOM);
	enumeration_axiom.setExpression(axiom_expression);

	enumeration_axiom.setDescription("This axiom describes the union for the concept "+owl_class.getLocalName());
	conceptualization.addFormula(enumeration_axiom);
	log.addLine ("The WebODE:Formula Axiom "+axiom_name+" is added to the conceptualization");

      }
    }
    catch (Exception e)	{}


  }



  private void _mapDatatypePropertiesDefinitions()
  {
    Enumeration datatype_properties_enu;
    datatype_properties_enu=owl_model.getDatatypeProperties();
    while (datatype_properties_enu.hasMoreElements())
    {
      OWLDatatypeProperty datatype_property =(OWLDatatypeProperty)datatype_properties_enu.nextElement();
      _mapDatatypePropertyDefinition(datatype_property);
    }
  }





  private void _checkThingTaxonomy()
  {
    Enumeration root_concepts_enu = conceptualization.getRootConceptsNames(webode_names_container.SUBCLASS_OF);
    while (root_concepts_enu.hasMoreElements())
    {
      String root_concept_name = (String)root_concepts_enu.nextElement();

      if (!conceptualization.hasTermRelation(webode_names_container.SUBCLASS_OF,root_concept_name,owl_names_container.THING)
	  &&(!(owl_names_container.THING.equals(root_concept_name)||(owl_names_container.NOTHING.equals(root_concept_name)))))
      {
	ISTermRelation new_term_relation= new ISTermRelation(webode_names_container.SUBCLASS_OF,root_concept_name,owl_names_container.THING,webode_names_container.N_CARDINALITY);
	conceptualization.addTermRelation(new_term_relation);
	log.addLine ("As a root concept, the "+root_concept_name+" is a child of owl:Thing");
      }
    }
  }


  private void _checkNothingTaxonomy()
  {
    Enumeration leaf_concepts_enu = conceptualization.getLeafConceptsNames(webode_names_container.SUBCLASS_OF);
    while (leaf_concepts_enu.hasMoreElements())
    {
      String leaf_concept_name = (String)leaf_concepts_enu.nextElement();



      if ((!conceptualization.hasTermRelation(webode_names_container.SUBCLASS_OF,owl_names_container.NOTHING,leaf_concept_name))
	  &&(!(owl_names_container.NOTHING.equals(leaf_concept_name)||owl_names_container.THING.equals(leaf_concept_name))))

      {
	ISTermRelation new_term_relation= new ISTermRelation(webode_names_container.SUBCLASS_OF,owl_names_container.NOTHING,leaf_concept_name,webode_names_container.N_CARDINALITY);
	conceptualization.addTermRelation(new_term_relation);
	log.addLine ("As a leaf concept, the daml:Nothing concept is a child of "+leaf_concept_name);

      }
    }
  }



  private void _mapPropertiesDefinitions()
  {

    log.addTitle("Reading the property definitions",3);

    Enumeration datatype_properties_enu = owl_model.getDatatypeProperties();
    while (datatype_properties_enu.hasMoreElements())
    {
      _mapDatatypePropertyDefinition ((OWLDatatypeProperty)datatype_properties_enu.nextElement());
    }

    Enumeration object_properties_enu = owl_model.getObjectProperties();
    while (object_properties_enu.hasMoreElements())
    {
      _mapObjectPropertyDefinition ((OWLObjectProperty)object_properties_enu.nextElement());
    }
  }







  private void _mapDatatypePropertyDefinition(OWLDatatypeProperty datatype_property)
  {
    if (ontology.belongsTo(datatype_property.getURI()))
    {
      String attr_name= _resolveTermName(datatype_property.getURI());
      String attr_description = null;
      String attr_type = "String";
      String attr_min_cardinality = "0";
      String attr_max_cardinality = webode_names_container.N_CARDINALITY;
      String attr_domain= _resolveRelationExtreme(datatype_property.getProperty(owl_names_container.DOMAIN),datatype_property.getProperty(owl_names_container.DOMAIN));





      //String attr_destination=_resolveRelationExtreme(datatype_property.getProperty(owl_names_container.DOMAIN),datatype_property.getProperty(owl_names_container.DOMAIN));

	/*
	METER AQUI EL TRATAMIENTO DEL RANGE (OJITO DATARANGE)
	*/
      OWLCommon owl_element = datatype_property.getFirstOfProperty(owl_names_container.RANGE);
      if (owl_element!=null)
      {
	if (!(owl_element instanceof OWLURIReference))
	{
	  log.addError("An owl:ObjectDatatype cannot have a owl:DataRange as range");
	}
	else
   attr_type = webode_names_container.translateDatatype(((OWLURIReference)owl_element).getURI());


      }




      log.addTitle("Mapping the owl:DatatypeProperty "+datatype_property.getURI(),3);

      ISAttributeDefinition attr_definition = new
	  ISAttributeDefinition (attr_name,attr_domain,attr_type,attr_max_cardinality, attr_min_cardinality);
      conceptualization.addAttributeDefinition(attr_definition);

      _mapDocumentation (datatype_property,attr_definition);

      _mapSubpropertiesOf(datatype_property);

      _mapInverseOf(datatype_property);

      _mapEquivalentProperties(datatype_property);



      HashSet property_types = datatype_property.getPropertyTypes();


      if (property_types.contains(owl_names_container.FUNCTIONAL_PROPERTY))
      {
	log.addLine ("The owl:ObjectProperty "+datatype_property.getURI()+"is unique, so its maximum cardinality should set to 1");
	attr_definition.setMaxCardinality("1");
      }

      if (property_types.contains(owl_names_container.INVERSE_FUNCTIONAL_PROPERTY))
      {
	log.addError("In OWL DL an owl:DatatypeProperty cannot have the owl:InverseFunctionalProperty");
      }

      if (property_types.contains(owl_names_container.TRANSITIVE_PROPERTY))
      {
	log.addError("In OWL DL an owl:DatatypeProperty cannot have the owl:TransitiveProperty");
      }
      if (property_types.contains(owl_names_container.SYMMETRIC_PROPERTY))
      {
	log.addError("In OWL DL an owl:DatatypeProperty cannot have the owl:SymmetricProperty");

      }




      ISInstanceAttributeDescriptor instance_attribute=attr_definition.obtainInstanceAttributeDescriptor();

      //	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>"+instance_attribute.getName()+attr_domain );
      ISConcept is_concept=null;

      if (conceptualization.hasConcept(attr_domain))
      {
	is_concept=conceptualization.getConcept(attr_domain);
	is_concept.addInstanceAttribute(instance_attribute);
      }
      else
      {

	//if (ontology.belongsTo(attr_domain))
      {
	//	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>"+instance_attribute.getName() );
	is_concept = new ISConcept(attr_domain);
	conceptualization.addConcept(is_concept);
	is_concept.addInstanceAttribute(instance_attribute);
      }
	/*
		else
		{
      //METER EN EL LOG!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		System.out.println("SE QUEDA FUERA "+attr_name);

		}
	*/
      }



    }
    else
    {

    }





  }

  private void _mapObjectPropertyDefinition(OWLObjectProperty object_property)
  {
    if (ontology.belongsTo(object_property.getURI()))
    {
      String relation_comments=null;
      String relation_origin=null;
      String relation_destination=null;
      String relation_max_cardinality =webode_names_container.N_CARDINALITY;
      String relation_min_cardinality ="0";

      log.addTitle("Mapping the owl:ObjectProperty "+object_property.getURI(),3);

      if (ontology.belongsTo(object_property.getURI()))
      {

	_mapSubpropertiesOf(object_property);
	_mapInverseOf(object_property);
	_mapEquivalentProperties(object_property);


	//Both extremes of the relation are added. The idea is to a single class expression
	//(whatever it could be) or an intersection of two or more expressions
	relation_destination= _resolveRelationExtreme(object_property.getProperty(owl_names_container.RANGE),object_property.getProperty(owl_names_container.RANGE));
	relation_origin=_resolveRelationExtreme(object_property.getProperty(owl_names_container.DOMAIN),object_property.getProperty(owl_names_container.DOMAIN));
	HashSet types_set= object_property.getPropertyTypes();
	HashSet property_types = object_property.getPropertyTypes();

	//At this moment, two strings should be defined, relation_origin and relation_destination. Then, a WebODE's term relation is
	//created and added to the term relations table

	ISTermRelationDefinition term_relation_definition =
	    new ISTermRelationDefinition (_resolveTermName(object_property.getURI()),relation_origin,relation_destination,relation_max_cardinality,relation_min_cardinality);
	_mapDocumentation (object_property,term_relation_definition);
	conceptualization.addTermRelationDefinition(term_relation_definition);

	if (property_types.contains(owl_names_container.FUNCTIONAL_PROPERTY))
	{
	  log.addLine ("The owl:ObjectProperty "+object_property.getURI()+"is unique, so its maximum cardinality should set to 1");
	  term_relation_definition.setMaxCardinality("1");
	}

	if (property_types.contains(owl_names_container.INVERSE_FUNCTIONAL_PROPERTY))
	{
	  if (!conceptualization.hasProperty("InverseFunctional Property"))
	  {
	    ISProperty inversefunctional_prop= new ISProperty("InverseFunctional Property");
	    conceptualization.addProperty(inversefunctional_prop);
	    String formula_expression="forall (?x,?y,?z) (p(?x,?y) and p(?x,?z))->(?y=?z)";
	    ISFormula inversefunctional_formula =
		new ISFormula ("InverseFunctional Formula","Formula",formula_expression);
	    inversefunctional_prop.addFormula("InverseFunctional Formula");
	    conceptualization.addFormula(inversefunctional_formula);
	  }
	  term_relation_definition.addProperty("InverseFunctional Property");

	}

	if (property_types.contains(owl_names_container.TRANSITIVE_PROPERTY))
	{
	  log.addLine ("The owl:ObjectProperty "+object_property.getURI()+" has the transitive property");
	  term_relation_definition.addProperty("Transitive");
	}
	if (property_types.contains(owl_names_container.SYMMETRIC_PROPERTY))
	{
	  log.addLine ("The owl:ObjectProperty "+object_property.getURI()+" has the transitive property");
	  term_relation_definition.addProperty("Symmetric");
	}
      }

    }

  }


  private void _mapEquivalentProperties(OWLProperty owl_property)
  {
    ISFormula equivalent_axiom=null;

    String property_name=null;
    String equivalent_name=_resolveTermName(owl_property.getURI());
    String begining_expression="forall (?x,?y) ("+equivalent_name+"(?x,?y))<->(";
    String axiom_description=null;

    //Iterator i=property.getSuperProperties(false);
    Enumeration equivalents_enu=owl_property.getProperty(owl_names_container.EQUIVALENT_PROPERTY);

    while (equivalents_enu.hasMoreElements())
    {
      property_name=_resolveTermName(((OWLClassDescription)equivalents_enu.nextElement()).getURI());
      log.addLine("This property is defined as the owl:equivalentProperty "+property_name);
      String axiom_expression=begining_expression+"("+property_name+"(?x,?y))";
      equivalent_axiom= new ISFormula();
      String axiom_name = webode_names_container.bindAnonymous(equivalent_axiom,webode_names_container.AXIOM);
      equivalent_axiom.setName(axiom_name);
      equivalent_axiom.setType(webode_names_container.AXIOM);
      equivalent_axiom.setExpression(axiom_expression);
      axiom_description="This axiom  describes that the property "+equivalent_name
		       +" is equivalent to the property"+property_name;
      equivalent_axiom.setDescription(axiom_description);
      conceptualization.addFormula(equivalent_axiom);
    }
  }


  private void _mapSubpropertiesOf(OWLProperty property)
  {
    ISFormula subproperty_axiom=null;

    String property_name=null;
    String subproperty_name=_resolveTermName(property.getURI());
    String begining_expression="forall (?x,?y) ("+subproperty_name+"(?x,?y))->(";
    String axiom_description=null;

    //Iterator i=property.getSuperProperties(false);
    Enumeration subproperties_enu = property.getProperty(owl_names_container.SUB_PROPERTY_OF);
    while (subproperties_enu.hasMoreElements())
    {
      property_name=_resolveTermName(((OWLClassDescription)subproperties_enu.nextElement()).getURI());
      log.addLine("This property is defined as the owl:subPropertyOf "+property_name);
      String axiom_expression=begining_expression+"("+property_name+"(?x,?y))";
      subproperty_axiom= new ISFormula();
      String axiom_name = webode_names_container.bindAnonymous(subproperty_axiom,webode_names_container.AXIOM);
      subproperty_axiom.setName(axiom_name);
      subproperty_axiom.setType(webode_names_container.AXIOM);
      subproperty_axiom.setExpression(axiom_expression);
      axiom_description="This axiom describes that the property "+subproperty_name
		       +" is subproperty of the property "+property_name;
      subproperty_axiom.setDescription(axiom_description);
      conceptualization.addFormula(subproperty_axiom);
    }
  }




  private void _mapInverseOf(OWLProperty property)
  {

    if (property instanceof OWLObjectProperty)
    {
      ISFormula inverse_of_axiom=null;
      String property_name=null;
      String inverse_of_name=property.getLocalName();
      String begining_expression="forall (?x,?y) ("+inverse_of_name+"(?x,?y))->(";
      String axiom_description=null;

      //Iterator i=property.getSuperProperties(false);
      Enumeration inverse_of_enu = property.getProperty(owl_names_container.INVERSE_OF);
      while (inverse_of_enu.hasMoreElements())
      {
	property_name=_resolveTermName(((OWLClassDescription)inverse_of_enu.nextElement()).getURI());
	log.addLine("This property is defined as the owl:inverseOf "+property_name);
	String axiom_expression=begining_expression+"("+property_name+"(?y,?x))";
	inverse_of_axiom= new ISFormula();
	String axiom_name = webode_names_container.bindAnonymous(inverse_of_axiom,webode_names_container.AXIOM);
	inverse_of_axiom.setName(axiom_name);
	inverse_of_axiom.setType(webode_names_container.AXIOM);
	inverse_of_axiom.setExpression(axiom_expression);
	axiom_description="This axiom describes that the property "+inverse_of_name
		  +"is the inverse of the property "+property_name;
	inverse_of_axiom.setDescription(axiom_description);
	conceptualization.addFormula(inverse_of_axiom);

      }
    }
    else
    {
      Enumeration inverse_of_enu = property.getProperty(owl_names_container.INVERSE_OF);
      if (inverse_of_enu.hasMoreElements())
	log.addError("An owl:DatatypeProperty cannot have an owl:inverseOf relation");
    }
  }





  private String _resolveRelationExtreme(Enumeration extreme_enu, Enumeration count_enu)
  {
    String extreme_name=null;
    Vector class_descriptions=new Vector();
    int extreme_counter=0;

    Enumeration aux_enu=count_enu;
    while (aux_enu.hasMoreElements())
    {
      OWLCommon owl_element= (OWLCommon)aux_enu.nextElement();
      if (owl_element instanceof OWLClassDescription)
      {
	class_descriptions.add(owl_element);
      }
      if (owl_element instanceof OWLDataRange)
      {
	log.addError("owl:DataRanges cannot be translated to WebODE, the data range is therefore ignored");
      }
    }

    if (class_descriptions.size()>0)
    {

      if (class_descriptions.size()==1)
      {
	//Object aux_object=extreme_it.next();
	extreme_name=_mapClassDescription((OWLClassDescription)extreme_enu.nextElement(),null,null,null,true);
      }
      else
      {
	//If the extreme is composed by more than a class expression, then it is
	ISConcept is_concept = new ISConcept();
	extreme_name =webode_names_container.bindAnonymous(is_concept,webode_names_container.CONCEPT);

	is_concept.setName(extreme_name);
	conceptualization.addConcept(is_concept);

	Enumeration class_descriptions_enu = class_descriptions.elements();
	while (class_descriptions_enu.hasMoreElements())
   _mapClassDescription((OWLClassDescription)class_descriptions_enu.nextElement(),extreme_name,webode_names_container.SUBCLASS_OF,null,false);
      }
    }
    if (extreme_name==null)
      extreme_name=_resolveTermName(owl_names_container.THING);
    return extreme_name;
  }


  private void _mapDocumentation (OWLProperty owl_property, ISStructureElement is_element)
  {

    Enumeration annotation_properties = owl_model.getAnnotationProperties();
    while (annotation_properties.hasMoreElements())
    {
      String annotation_tag = (String)annotation_properties.nextElement();
      Enumeration enu = owl_property.getProperty(annotation_tag);

      while (enu.hasMoreElements())
      {
	Object aux_obj = enu.nextElement();
	if (aux_obj instanceof OWLDataValue)
	{
	  OWLDataValue data_value=(OWLDataValue) aux_obj;
	  is_element.addDescription(annotation_tag+" : "+data_value.getValue()+"||");
	}
	else
	{
	  if (aux_obj instanceof OWLNamedElement)
	  {
	    OWLNamedElement named_element = (OWLNamedElement)aux_obj;
	    is_element.addDescription(annotation_tag+" : "+named_element.getURI()+"||");
	  }
	}
      }
    }
  }

  void _mapIndividuals ()
  {
    Hashtable attributes_table = new Hashtable();
    Enumeration concepts_enu = conceptualization.getConcepts();
    while (concepts_enu.hasMoreElements())
    {
      ISConcept is_concept= (ISConcept)concepts_enu.nextElement();
      attributes_table.put(is_concept.getName(),is_concept.getAttributeNames());
    }

    log.addTitle("Mapping the individuals",3);
    ISInstancesSet instances_set = new ISInstancesSet ("Ontology Instances");
    ontology.addInstancesSet(instances_set);
    Enumeration owl_individuals_enu = owl_model.getIndividuals();
    while (owl_individuals_enu.hasMoreElements())
    {
      OWLIndividual owl_individual = ((OWLIndividual)owl_individuals_enu.nextElement());
      //log.addTitle(owl_individual.getURI(),1);
      _mapIndividual (owl_individual,instances_set,attributes_table );
    }

  }



  protected void _mapIndividual (OWLIndividual owl_individual, ISInstancesSet instances_set,Hashtable attributes_table)
  {
    if (ontology.belongsTo(owl_individual.getURI()))
    {
      HashSet added_values = new HashSet();
      HashSet taxonomy_relations = new HashSet();
      taxonomy_relations.add(owl_names_container.TYPE);
      HashSet annotation_properties = owl_model.getAnnotationPropertiesSet();


      log.addTitle("Mapping the individual "+owl_individual.getURI(),1);

      System.out.println("Mapping the individual "+owl_individual.getURI());

      ISInstance concept_instance =
	  new	ISInstance (owl_individual.getLocalName());

      instances_set.addInstance(concept_instance);
      _mapIndividualType(owl_individual,concept_instance);


      Enumeration properties_names_enu=owl_individual.getPropertyNames();
      while (properties_names_enu.hasMoreElements())
      {
	String property_name = (String)properties_names_enu.nextElement();
	if (!taxonomy_relations.contains(property_name))
	{
	  if (!annotation_properties.contains(property_name))
	  {
	    Enumeration properties_enu = owl_individual.getProperty(property_name);
	    while (properties_enu.hasMoreElements())
	    {
	      Object property_destination = properties_enu.nextElement();
	      //  	log.addLine(property_name+"----->"+property_destination.toString());
	      if (property_destination instanceof OWLURIReference)
	      {
		OWLURIReference uri_reference = (OWLURIReference)property_destination;
                System.out.println ("-----------------------------------------------");
                System.out.println ("Add term relation("+property_name+") instance");
                System.out.println ("-----------------------------------------------");
		log.addLine ("Add term relation("+property_name+") instance");


		ISRelationInstance relation_instance = new ISRelationInstance();
		String relation_instance_name=webode_names_container.bindAnonymous(relation_instance,webode_names_container.RELATION_INSTANCE);
		relation_instance.setName(relation_instance_name);
                System.out.println ("origin-"+owl_individual.getURI()+"---------------> "+_resolveTermName(owl_individual.getURI()));
		relation_instance.setParent(_resolveTermName(property_name));
                System.out.println ("destination-"+uri_reference.getURI()+"----------->"+_resolveTermName(uri_reference.getURI()));
		relation_instance.setOrigin(_resolveTermName(owl_individual.getURI()));
		relation_instance.setDestination(_resolveTermName(uri_reference.getURI()));
		instances_set.addRelationInstance(relation_instance);
	      }
	      else
	      {
		if (property_destination instanceof OWLDataValue)
		{
		  String value_name = _resolveTermName(property_name);
		  String class_concept_name=_getClassConcept(value_name,attributes_table);
		  //		System.out.println("---------->" + property_name + " ||| "+class_concept_name);
		  if (class_concept_name!=null)
		  {
		    if (!added_values.contains(value_name))
		    {
		    ISClass instance_class= new ISClass(class_concept_name);
		    ISAttribute attr = new ISAttribute(_resolveTermName(property_name),((OWLDataValue)property_destination).getValue());
		    instance_class.addAttribute(attr);
		    concept_instance.addClass(instance_class);
		    }
		    else
		    {
		      log.addError("An instance in WebODE cannot have two values with the same name");
		    }
		  }
		  else
      System.out.println("--PETA EL ATRIBUTO DE INSTANCIA");

		}
		else
		{

		  log.addLine ("ES UN CASO RARO DE VERDAD");
		}
	      }
	    }
	  }
	  else
	  {
	    //This part is the mapping of the annotations properties
	    //of the instance

	    Enumeration enu = owl_individual.getProperty(property_name);
	    while (enu.hasMoreElements())
	    {
						/*
					OWLDataValue data_value=(OWLDataValue) enu.nextElement();
					concept_instance.addDescription(property_name+" : "+data_value.getValue()+"||");
					*/
	      Object aux_obj = enu.nextElement();
	      if (aux_obj instanceof OWLDataValue)
	      {
		OWLDataValue data_value=(OWLDataValue) aux_obj;
		concept_instance.addDescription(property_name+" : "+data_value.getValue()+"||");
	      }
	      else
	      {
		if (aux_obj instanceof OWLNamedElement)
		{
		  OWLNamedElement named_element = (OWLNamedElement)aux_obj;
		  concept_instance.addDescription(property_name+" : "+named_element.getURI()+"||");
		}
	      }
	    }
	  }
	}
      }

    }
  }


  public String _getClassConcept(String attribute_name, Hashtable attributes_table)
  {
    System.out.println("Se busca "+attribute_name);
    String concept_with_property=null;
    Enumeration concept_names_enu = attributes_table.keys();
    while (concept_names_enu.hasMoreElements () && (concept_with_property==null))
    {
      String concept_name = (String)concept_names_enu.nextElement();
      System.out.println(concept_name);
      HashSet attributes_set= (HashSet)attributes_table.get(concept_name);
      System.out.println (attributes_set);
      if (attributes_set.contains(attribute_name))
	concept_with_property=concept_name;
    }
    return concept_with_property;
  }


  private void _mapIndividualType (OWLIndividual owl_individual,ISInstance is_instance)
  {
    Vector instance_types = new Vector();
    Enumeration types_enu = owl_individual.getProperty(owl_names_container.TYPE);
    while (types_enu.hasMoreElements())
    {
      instance_types.add(((OWLURIReference)types_enu.nextElement()).getURI());
    }
    if (instance_types.size()==0)
    {
      log.addWarning ("There is no type for the individual "+owl_individual.getURI()+" it is set to the default type (owl:Thing)");
      is_instance.addParentConcept(_resolveTermName(owl_names_container.THING));
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



}