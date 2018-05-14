package es.upm.fi.dia.ontology.webode.translat.internalstructure;

//Jena related imports
//The
import es.upm.fi.dia.ontology.webode.translat.logs.*;
import es.upm.fi.dia.ontology.webode.translat.namescontainers.*;
import es.upm.fi.dia.ontology.webode.translat.rdfsapi.*;
import java.io.*;
import java.util.*;


class RDFSISStructureImpl extends ISStructureImpl
{
  //Attributes
  private ISConceptualization conceptualization;
  private RDFSModel rdfs_model;
  private WEBODENamesContainer webode_names_container;
  private RDFSNamesContainer rdfs_names_container;


  public RDFSISStructureImpl (Reader input, String name,String namespace)

  {
    log =new LogTextImpl();
    rdfs_model=new RDFSModel();
    webode_names_container=new WEBODENamesContainer(namespace);
    rdfs_names_container=new RDFSNamesContainer(namespace);

    //System.out.println("Leo el modelo");
    rdfs_model.read(input,namespace);
//	rdfs_model.showStatements();
    String date =(new Date(System.currentTimeMillis())).toString();
    log.addTitle ("Adding the ontology Header and Version Info. Wrapping time"+date,3);

    ontology=new ISOntology(name,namespace,date);

    conceptualization=new ISConceptualization();
    ontology.setConceptualization(conceptualization);

    _mapProperties();

    _mapClasses();


    _addGlobalObjectProperties ();
_checkImportedTerms ();
    _mapInstances();

    _checkTermRelations();
  }



  private void _mapClasses()
  {
    Enumeration classes_enu = rdfs_model.getClasses();
    while (classes_enu.hasMoreElements())
    {
      RDFSClass rdfs_class = (RDFSClass) classes_enu.nextElement();


      _mapClass(rdfs_class);



    }
  }

  private void _mapClass(RDFSClass rdfs_class)
  {
    log.addTitle("Mapping the class "+rdfs_class.getLocalName()+" into a WebODE:Concept",3);
    ISConcept is_concept=null;
    if (conceptualization.hasConcept(rdfs_class.getLocalName()))
    {
      is_concept=conceptualization.getConcept(rdfs_class.getLocalName());
    }
    else
    {
      is_concept=new ISConcept(rdfs_class.getLocalName());
      conceptualization.addConcept(is_concept);
    }



    _mapSubclassOfProperties(rdfs_class);
    _mapProperties(rdfs_class,is_concept);
    _mapDocumentation (rdfs_class,is_concept);

  }



  private void _mapSubclassOfProperties(RDFSClass rdfs_class)
  {
    log.addTitle("Mapping subClassOf properties "+rdfs_class.getURI(),1);
    Enumeration enu = rdfs_class.getProperty(rdfs_names_container.SUBCLASS_OF);
    try
    {
      do
      {
	Object aux_obj=enu.nextElement();
	log.addLine(rdfs_names_container.SUBCLASS_OF+" is trasnformed into a WebODE:Subclass-of");

	if (aux_obj instanceof RDFSURIReference)
	{
	  ISTermRelation term_relation =
	      new ISTermRelation(webode_names_container.SUBCLASS_OF,rdfs_class.getLocalName(),_resolveTermName(((RDFSURIReference)aux_obj).getURI()),webode_names_container.N_CARDINALITY);
	  conceptualization.addTermRelation(term_relation);
	}
	else
	{
	  if (aux_obj instanceof RDFSClass)
	  {
	    RDFSClass anon_class= (RDFSClass)aux_obj;
	    _mapClass(anon_class);
	    ISTermRelation term_relation =
		new ISTermRelation(webode_names_container.SUBCLASS_OF,rdfs_class.getLocalName(),anon_class.getLocalName(),webode_names_container.N_CARDINALITY);
	    conceptualization.addTermRelation(term_relation);
	  }
	}
      }
      while (enu.hasMoreElements());
    }
    catch (Exception e)
    {
      log.addLine("No "+rdfs_names_container.SUBCLASS_OF+" to transform");
    }
  }


  private void _mapProperties(RDFSClass rdfs_class, ISConcept is_concept)
  {
    log.addTitle("Mapping ad-hoc properties "+rdfs_class.getURI(),1);
    HashSet taxonomy_relations = new HashSet();
    taxonomy_relations.add(rdfs_names_container.TYPE);
    taxonomy_relations.add(rdfs_names_container.SUBCLASS_OF);
    HashSet annotation_properties = new HashSet();
    annotation_properties.add(rdfs_names_container.LABEL);
    annotation_properties.add(rdfs_names_container.COMMENT);
    annotation_properties.add(rdfs_names_container.IS_DEFINED_BY);
    annotation_properties.add(rdfs_names_container.LABEL);

    Enumeration properties_enu = rdfs_class.getPropertiesNames();
    while (properties_enu.hasMoreElements())
    {
      String property_uri = (String)properties_enu.nextElement();
      if ((!annotation_properties.contains(property_uri))&&(!taxonomy_relations.contains(property_uri)))
      {
	log.addLine(">> "+property_uri);
	boolean is_object=conceptualization.hasTermRelationDefinition(_localName(property_uri));
	boolean is_data=conceptualization.hasAttributeDefinition(_localName(property_uri));
	if (is_object)
	{
	  _mapObjectProperty(rdfs_class,is_concept,property_uri);
	}
	if (is_data)
	{
	  log.addLine("FITASH");
	  _mapDatatypeProperty(rdfs_class,is_concept,property_uri);
	}
	if ((!is_data)&&(!is_object))
	{
	  _mapUnknownTypeProperty(rdfs_class,is_concept,property_uri);
	}

      }
    }
  }


  private void _mapDatatypeProperty(RDFSClass rdfs_class,ISConcept is_concept,String property_uri)
  {
    String property_local_name=_localName(property_uri);
    Enumeration property_destinations_enu=rdfs_class.getProperty(property_uri);
    while (property_destinations_enu.hasMoreElements())
    {
      Object aux_obj=property_destinations_enu.nextElement();
      if (aux_obj instanceof RDFSDataValue)
      {
	if (is_concept.hasInstanceAttribute(property_local_name))
	{
	  log.addLine("The instance attribute "+property_local_name+" is upgraded to class attribute");
	  ISInstanceAttributeDescriptor instance_attr = is_concept.getInstanceAttribute(property_local_name);
	  ISClassAttributeDescriptor class_attr=instance_attr.upgradeToClassAttribute();
	  is_concept.deleteInstanceAttribute(property_local_name);
	  class_attr.addValue(((RDFSDataValue)aux_obj).getValue());
	  is_concept.addClassAttribute(class_attr);
	}
	else
	{
	  log.addLine("The class attribute "+property_local_name+" is added");
	  ISClassAttributeDescriptor class_attr=
	      conceptualization.getAttributeDefinition(property_local_name).obtainInstanceAttributeDescriptor().upgradeToClassAttribute();
	  class_attr.addValue(((RDFSDataValue)aux_obj).getValue());
	  is_concept.addClassAttribute(class_attr);
	}
      }
      else
      {
	log.addError("There is an mapping the property "+property_local_name);
      }

    }

  }




  private void _mapUnknownTypeProperty(RDFSClass rdfs_class,ISConcept is_concept,String property_uri)
  {
    String property_local_name=_localName(property_uri);
    Enumeration property_destinations_enu=rdfs_class.getProperty(property_uri);
    while (property_destinations_enu.hasMoreElements())
    {
      Object aux_obj=property_destinations_enu.nextElement();
      if (aux_obj instanceof RDFSDataValue)
      {
	ISClassAttributeDescriptor class_attr=null;
	if (is_concept.hasInstanceAttribute(property_local_name))
	{
	  ISInstanceAttributeDescriptor instance_attr = is_concept.getInstanceAttribute(property_local_name);
	  class_attr=instance_attr.upgradeToClassAttribute();
	  is_concept.deleteInstanceAttribute(property_local_name);

	}
	else
	{
	  class_attr=
	      new ISClassAttributeDescriptor (property_local_name,"0",webode_names_container.N_CARDINALITY,"String");

	}
	class_attr.addValue(((RDFSDataValue)aux_obj).getValue());
	is_concept.addClassAttribute(class_attr);
	log.addLine("ENTRA AQUISH");
      }
      else
      {
	if (aux_obj instanceof RDFSNamedElement)
	{
	  //	log.addLine("ss "+((RDFSNamedElement)aux_obj).getURI());

	  String destination_name = _resolveTermName(((RDFSNamedElement)aux_obj).getURI());

	  ISTermRelation term_relation=
	      new ISTermRelation (property_local_name,is_concept.getName(),destination_name,webode_names_container.N_CARDINALITY);

	  conceptualization.addTermRelation(term_relation);

	  log.addLine("Adding the term relation"+term_relation.getName()+"with destination"+destination_name);
	}
  /*
   else
    log.addError("PETASH "+aux_obj.toString());
  */

      }

    }

  }











  private void _mapObjectProperty(RDFSClass rdfs_class,ISConcept is_concept,String property_uri)
  {
    Enumeration property_destinations_enu=rdfs_class.getProperty(property_uri);
    while (property_destinations_enu.hasMoreElements())
    {
      Object aux_obj=property_destinations_enu.nextElement();
      if (aux_obj instanceof RDFSNamedElement)
      {
	log.addLine("ss "+((RDFSNamedElement)aux_obj).getURI());

	String destination_name = _resolveTermName(((RDFSNamedElement)aux_obj).getURI());

	ISTermRelation term_relation= conceptualization.getTermRelationDefinition(_localName(property_uri)).obtainTermRelation();
	term_relation.setDestination(destination_name);
	term_relation.setOrigin(is_concept.getName());
	conceptualization.addTermRelation(term_relation);

	if (aux_obj instanceof RDFSClass)
	{
	  log.addError("NO ANON CLASS ALLOWED HERE");
	}
	log.addLine("Adding the term relation"+term_relation.getName()+"with destination"+destination_name);
      }
      else
	log.addError("PETASH "+aux_obj.toString());


    }

  }















  private void _mapProperties()
  {
    Enumeration properties_enu = rdfs_model.getProperties();

 /*
 while (properties_enu.hasMoreElements())
  log.addLine("-: "+properties_enu.nextElement().toString());
 properties_enu = rdfs_model.getProperties();
 */
    while (properties_enu.hasMoreElements())
    {
      boolean is_datatype=false;
      boolean is_object=false;
      String range_name=null;
      String domain_name=null;
      RDFSProperty property =(RDFSProperty) properties_enu.nextElement();


      log.addTitle ("Mapping the property "+property.getURI(),3);

      _mapSubpropertiesOf(property);
      is_object=rdfs_model.guessIfObjectProperty(property);
      is_datatype=rdfs_model.guessIfDatatypeProperty(property);


      if (is_object)
      {
	log.addWarning ("The property "+property.getURI()+" is classified as an ObjectProperty");
	_mapObjectPropertyDefinition(property);
      }

      if (is_datatype)
      {
	log.addWarning ("The property "+property.getURI()+" is classified as a DatatypeProperty");
	_mapDatatypePropertyDefinition(property);
      }

      if ((!is_object)&&(!is_datatype))
      {
	log.addWarning ("The property "+property.getURI()+" cannot be classified, it's added as an object and datatype property");
	_mapObjectPropertyDefinition(property);
	_mapDatatypePropertyDefinition(property);
      }
    }
  }


  private void _mapObjectPropertyDefinition (RDFSProperty object_property)
  {
    //if (ontology.belongsTo(object_property.getURI()))
    //{
    String relation_comments=null;
    String relation_origin=null;
    String relation_destination=null;
    String relation_max_cardinality =webode_names_container.N_CARDINALITY;
    String relation_min_cardinality ="0";

    log.addTitle("Mapping the owl:ObjectProperty "+object_property.getURI(),1);

    //if (ontology.belongsTo(object_property.getURI()))
    //{

    //_mapSubpropertiesOf(object_property);

    relation_destination= _resolveRelationExtreme(object_property.getProperty(rdfs_names_container.RANGE),object_property.getProperty(rdfs_names_container.RANGE));
    relation_origin=_resolveRelationExtreme(object_property.getProperty(rdfs_names_container.DOMAIN),object_property.getProperty(rdfs_names_container.DOMAIN));


    ISTermRelationDefinition term_relation_definition =
	new ISTermRelationDefinition (_resolveTermName(object_property.getURI()),relation_origin,relation_destination,relation_max_cardinality,relation_min_cardinality);
    _mapDocumentation (object_property,term_relation_definition);
    conceptualization.addTermRelationDefinition(term_relation_definition);

  }

  private void _mapDatatypePropertyDefinition (RDFSProperty datatype_property)
  {
 /*
 if (oçntology.belongsTo(datatype_property.getURI()))
 {
 */
    String attr_name= _resolveTermName(datatype_property.getURI());
    String attr_description = null;
    String attr_type = "String";
    String attr_min_cardinality = "0";
    String attr_max_cardinality = webode_names_container.N_CARDINALITY;
    String attr_domain= _resolveRelationExtreme(datatype_property.getProperty(rdfs_names_container.DOMAIN),datatype_property.getProperty(rdfs_names_container.DOMAIN));


//	log.addLine("Este es el dominio>"+attr_domain);


    //String attr_destination=_resolveRelationExtreme(datatype_property.getProperty(owl_names_container.DOMAIN),datatype_property.getProperty(owl_names_container.DOMAIN));

 /*
 METER AQUI EL TRATAMIENTO DEL RANGE (OJITO DATARANGE)
 */

    RDFSCommon rdfs_element = datatype_property.getFirstOfProperty(rdfs_names_container.RANGE);
    if (rdfs_element!=null)
    {
      if ((rdfs_element instanceof RDFSURIReference))
      {
	attr_type = webode_names_container.translateDatatype(((RDFSURIReference)rdfs_element).getURI());
      }
    }





    log.addTitle("Mapping the owl:DatatypeProperty "+datatype_property.getURI(),1);

    ISAttributeDefinition attr_definition = new
	ISAttributeDefinition (attr_name,attr_domain,attr_type,attr_max_cardinality, attr_min_cardinality);
    conceptualization.addAttributeDefinition(attr_definition);

    _mapDocumentation (datatype_property,attr_definition);






    ISInstanceAttributeDescriptor instance_attribute=attr_definition.obtainInstanceAttributeDescriptor();

    //	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>"+instance_attribute.getName()+attr_domain );
    ISConcept is_concept=null;

    log.addLine ("El attr_domain es "+attr_domain);
    if (conceptualization.hasConcept(attr_domain))
    {
      log.addLine("PILLA EL QUE ESTA"+instance_attribute.getName());
      is_concept=conceptualization.getConcept(attr_domain);
      is_concept.addInstanceAttribute(instance_attribute);
      log.addLine(is_concept.getName()+"|"+is_concept.getAttributeNames().toString());
    }
    else
    {
      if (!rdfs_model.hasInstance(rdfs_names_container.namespace+attr_domain)&&!rdfs_model.hasProperty(rdfs_names_container.namespace+attr_domain))
      {
	log.addLine("CREA OTRO "+rdfs_names_container.namespace+attr_domain);
	is_concept = new ISConcept(attr_domain);
	conceptualization.addConcept(is_concept);
	is_concept.addInstanceAttribute(instance_attribute);
      }

    }









  }


  private String _resolveRelationExtreme(Enumeration extreme_enu, Enumeration count_enu)
  {
    String extreme_name=null;
    Vector extreme_elements=new Vector();
    int extreme_counter=0;

    Enumeration aux_enu=count_enu;
    while (aux_enu.hasMoreElements())
    {
      RDFSCommon rdfs_element= (RDFSCommon)aux_enu.nextElement();
      extreme_elements.add(rdfs_element);
    }

    if (extreme_elements.size()>0)
    {

      if (extreme_elements.size()==1)
      {
	log.addLine("Caso de uno");
	RDFSElement rdfs_element = (RDFSElement)extreme_elements.get(0);
	if (rdfs_element instanceof RDFSURIReference)
	{
	  extreme_name= _resolveTermName(((RDFSURIReference)rdfs_element).getURI());
	}
	else
	{
	  if (rdfs_element instanceof RDFSClass)
	  {
	    extreme_name= _resolveTermName(((RDFSClass)rdfs_element).getURI());
	    _mapClass((RDFSClass)rdfs_element);
	  }
	}
      }
      else
      {
	//If the extreme is composed by more than a class expression, then it is
	//log.addLine("Caso de uno");
	ISConcept is_concept = new ISConcept();
	extreme_name =webode_names_container.bindAnonymous(is_concept,webode_names_container.CONCEPT);

	is_concept.setName(extreme_name);
	conceptualization.addConcept(is_concept);

	Enumeration extreme_elements_enu = extreme_elements.elements();
	//String element_name=null;
	while (extreme_elements_enu.hasMoreElements())
	{
	  RDFSElement rdfs_element=(RDFSElement)extreme_elements_enu.nextElement();
	  if (rdfs_element instanceof RDFSURIReference)
	  {
	    extreme_name= _resolveTermName(((RDFSURIReference)rdfs_element).getURI());
	  }
	  else
	  {
	    if (rdfs_element instanceof RDFSClass)
	    {
	      extreme_name= _resolveTermName(((RDFSClass)rdfs_element).getURI());
	      _mapClass((RDFSClass)rdfs_element);
	    }
	  }
	  ISTermRelation term_relation =
	      new ISTermRelation(webode_names_container.SUBCLASS_OF,is_concept.getName(),extreme_name,webode_names_container.N_CARDINALITY);
	  conceptualization.addTermRelation(term_relation);


	}
	extreme_name=is_concept.getName();

      }

    }
    if (extreme_name==null)
      extreme_name=_resolveTermName(rdfs_names_container.RESOURCE);
//	log.addLine("Extreme name: "+extreme_name);
    return extreme_name;
  }






  private void _mapSubpropertiesOf(RDFSProperty property)
  {
    ISFormula subproperty_axiom=null;

    String property_name=null;
    String subproperty_name=_resolveTermName(property.getURI());
    String begining_expression="forall (?x,?y) ("+subproperty_name+"(?x,?y))->(";
    String axiom_description=null;

    //Iterator i=property.getSuperProperties(false);
    Enumeration subproperties_enu = property.getProperty(rdfs_names_container.SUB_PROPERTY_OF);
    while (subproperties_enu.hasMoreElements())
    {
      property_name=_resolveTermName(((RDFSURIReference)subproperties_enu.nextElement()).getURI());
      log.addLine("This property is defined as the subPropertyOf "+property_name);
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

  private String _localName(String uri)
  {
    String local_name=null;
    String namespace=null;
    int index;
    index= uri.indexOf('#');
    namespace=uri.substring(0,index+1);
    local_name=uri.substring(index+1,uri.length());
    return local_name;

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
    if (rdfs_names_container.namespace.equals(namespace))
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

  private void _mapDocumentation (RDFSElement rdfs_element, ISStructureElement is_element)
  {

    String annotation_properties[]=
    {rdfs_names_container.LABEL,rdfs_names_container.COMMENT,rdfs_names_container.IS_DEFINED_BY,
      rdfs_names_container.SEE_ALSO};
    int index;

    for(index=0;index<annotation_properties.length;index++)
    {
      Enumeration enu = rdfs_element.getProperty(annotation_properties[index]);

      while (enu.hasMoreElements())
      {
	Object aux_obj = enu.nextElement();
	if (aux_obj instanceof RDFSDataValue)
	{
	  is_element.addDescription(annotation_properties[index]+" : "+((RDFSDataValue)aux_obj).getValue()+"||");

	}
	else
	{
	  if (aux_obj instanceof RDFSNamedElement)
	  {
	    //OWLNamedElement named_element = (OWLNamedElement)aux_obj;
	    is_element.addDescription(annotation_properties[index]+" : "+((RDFSNamedElement)aux_obj).getURI()+"||");
	  }
	}
      }
    }
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



  void _mapInstances ()
  {
    Hashtable attributes_table = new Hashtable();
    Enumeration concepts_enu = conceptualization.getConcepts();
    while (concepts_enu.hasMoreElements())
    {
      ISConcept is_concept= (ISConcept)concepts_enu.nextElement();
      attributes_table.put(is_concept.getName(),is_concept.getAttributeNames());
    }

    log.addTitle("Mapping the instances",3);
    ISInstancesSet instances_set = new ISInstancesSet ("Ontology Instances");
    ontology.addInstancesSet(instances_set);
    Enumeration rdfs_instances_enu = rdfs_model.getInstances();
    while (rdfs_instances_enu.hasMoreElements())
    {
      RDFSInstance rdfs_instance = ((RDFSInstance)rdfs_instances_enu.nextElement());
      //log.addTitle(rdfs_instance.getURI(),1);
      _mapInstance (rdfs_instance,instances_set,attributes_table );
    }

  }



  protected void _mapInstance (RDFSInstance rdfs_instance, ISInstancesSet instances_set,Hashtable attributes_table)
  {
    if (ontology.belongsTo(rdfs_instance.getURI()))
    {
      HashSet taxonomy_relations = new HashSet();
      taxonomy_relations.add(rdfs_names_container.TYPE);
      HashSet annotation_properties = new HashSet();//rdfs_model.getAnnotationPropertiesSet();
      annotation_properties.add(rdfs_names_container.LABEL);
      annotation_properties.add(rdfs_names_container.COMMENT);
      annotation_properties.add(rdfs_names_container.IS_DEFINED_BY);
      annotation_properties.add(rdfs_names_container.LABEL);

      log.addTitle("Mapping the instance "+rdfs_instance.getURI(),1);

      System.out.println("Mapping the instance "+rdfs_instance.getURI());

      ISInstance concept_instance =
	  new	ISInstance (rdfs_instance.getLocalName());

      instances_set.addInstance(concept_instance);
      _mapIndividualType(rdfs_instance,concept_instance);


      Enumeration properties_names_enu=rdfs_instance.getPropertiesNames();
      while (properties_names_enu.hasMoreElements())
      {
	String property_name = (String)properties_names_enu.nextElement();
	if (!taxonomy_relations.contains(property_name))
	{
	  if (!annotation_properties.contains(property_name))
	  {
	    Enumeration properties_enu = rdfs_instance.getProperty(property_name);
	    while (properties_enu.hasMoreElements())
	    {
	      Object property_destination = properties_enu.nextElement();
	      //  	log.addLine(property_name+"----->"+property_destination.toString());
	      if (property_destination instanceof RDFSURIReference)
	      {
		RDFSURIReference uri_reference = (RDFSURIReference)property_destination;
		log.addLine ("Add term relation("+property_name+") instance");

		ISRelationInstance relation_instance = new ISRelationInstance();
		String relation_instance_name=webode_names_container.bindAnonymous(relation_instance,webode_names_container.RELATION_INSTANCE);
		relation_instance.setName(relation_instance_name);
		relation_instance.setParent(_resolveTermName(property_name));
		relation_instance.setOrigin(_resolveTermName(rdfs_instance.getURI()));
		relation_instance.setDestination(_resolveTermName(uri_reference.getURI()));
		instances_set.addRelationInstance(relation_instance);
	      }
	      else
	      {
		if (property_destination instanceof RDFSDataValue)
		{

		  String class_concept_name=_getClassConcept(_resolveTermName(property_name),attributes_table);
		  //		System.out.println("---------->" + property_name + " ||| "+class_concept_name);
		  if (class_concept_name!=null)
		  {
		    log.addLine ("INST: "+concept_instance.getName()+"PROP "+property_name+"CONCEPT"+class_concept_name);
		    ISClass instance_class= new ISClass(class_concept_name);
		    ISAttribute attr = new ISAttribute(_resolveTermName(property_name),((RDFSDataValue)property_destination).getValue());
		    instance_class.addAttribute(attr);
		    concept_instance.addClass(instance_class);
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

	    Enumeration enu = rdfs_instance.getProperty(property_name);
	    while (enu.hasMoreElements())
	    {
      /*
     OWLDataValue data_value=(OWLDataValue) enu.nextElement();
     concept_instance.addDescription(property_name+" : "+data_value.getValue()+"||");
     */
	      Object aux_obj = enu.nextElement();
	      if (aux_obj instanceof RDFSDataValue)
	      {
		RDFSDataValue data_value=(RDFSDataValue) aux_obj;
		concept_instance.addDescription(property_name+" : "+data_value.getValue()+"||");
	      }
	      else
	      {
		if (aux_obj instanceof RDFSNamedElement)
		{
		  RDFSNamedElement named_element = (RDFSNamedElement)aux_obj;
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


  private void _mapIndividualType (RDFSInstance rdfs_instance,ISInstance is_instance)
  {
    Vector instance_types = new Vector();
    Enumeration types_enu = rdfs_instance.getProperty(rdfs_names_container.TYPE);
    while (types_enu.hasMoreElements())
    {
      instance_types.add(((RDFSURIReference)types_enu.nextElement()).getURI());
    }
    if (instance_types.size()==0)
    {
      log.addWarning ("There is no type for the individual "+rdfs_instance.getURI()+" it is set to the default type (owl:Thing)");
      //is_instance.addParentConcept(_resolveTermName(rdfs_names_container.THING));
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

  private void _checkTermRelations ()
  {
    Vector valid_term_relations = new Vector();
    Enumeration term_relations_enu = conceptualization.getTermRelations();
    while (term_relations_enu.hasMoreElements())
    {
      ISTermRelation term_relation = (ISTermRelation)term_relations_enu.nextElement();
      String relation_name = term_relation.getName();
      String origin_name=term_relation.getOrigin();
      String destination_name=term_relation.getDestination();
      if (!conceptualization.hasImportedTermWithName(relation_name))
      {
	if (!conceptualization.hasImportedTermWithName(origin_name)&&
	    !conceptualization.hasConcept(origin_name))
	{
	  System.out.println ("Se borra el TermRelation "+ term_relation.getName()+" ["+term_relation.getOrigin()+"->"+term_relation.getDestination()+"]");
	  conceptualization.removeTermRelation(term_relation.getName());
	}
	else
	{
	  if (!conceptualization.hasImportedTermWithName(destination_name)&&
	      !conceptualization.hasConcept(destination_name))
	  {
	    System.out.println (">> Se borra el TermRelation "+ term_relation.getName()+" ["+term_relation.getOrigin()+"->"+term_relation.getDestination()+"]");
	    conceptualization.removeTermRelation(term_relation.getName());
	  }
	  else
	  {
	    System.out.println("No se borra el TermRelation "+ term_relation.getName());
	    valid_term_relations.add(term_relation);
	  }
	}
      }
    }
    conceptualization.setTermRelations(valid_term_relations);
  }


}