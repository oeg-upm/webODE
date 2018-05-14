package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


import es.upm.fi.dia.ontology.webode.service.*;


public class ISInstancesSet extends ISStructureElement
{
    private String name; //Mandatory and unique

    private Hashtable concept_instances;	//This vector contains Instances (the whole object). D
						//From cero to many
    private Hashtable relation_instances;


    public void toWebODE(ODEService ode, String ontologyName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException
    {
      ode.insertTerm(ontologyName,name,description,TermTypes.INSTANCE_SET);

      ISInstance instance;
      for(Enumeration enum=concept_instances.elements(); enum.hasMoreElements(); ) {
        instance=(ISInstance)enum.nextElement();
        instance.toWebODE(ode,ontologyName,name);
      }

      ISRelationInstance rinstance;
      for(Enumeration enum=relation_instances.elements(); enum.hasMoreElements(); ) {
        rinstance=(ISRelationInstance)enum.nextElement();
        rinstance.toWebODE(ode,ontologyName,name);
      }
    }

	public ISInstancesSet(String instance_set_name)
	{

		empty=false;
		name=instance_set_name;
		concept_instances=new Hashtable();
		relation_instances=new Hashtable();

	}


	public String getName()
	{
		return name;
	}



	public void addInstance(ISInstance instance)
	{


		concept_instances.put(instance.getName(),instance);
	}

	public void addRelationInstance(ISRelationInstance relation_instance)
	{
		relation_instances.put(relation_instance.getName(),relation_instance);
	}


	public boolean isEmpty()
	{
		return (concept_instances.size()==0);
	}


	public Enumeration getConceptInstances ()
	{
		return concept_instances.elements();
	}

	public ISInstance getConceptInstance(String instance_name)
	{
		return (ISInstance)concept_instances.get(instance_name);
	}

	public Enumeration getRelationInstances()
	{
		return relation_instances.elements();
	}
//-------------------------------------------------------------------------
	public Enumeration getInstances ()
	{
		Vector all_instances = new Vector();

		all_instances.addAll(concept_instances.values());
		all_instances.addAll(relation_instances.values());
		return all_instances.elements();
	}

	public boolean hasConceptInstance (String instance_uri)
	{
		return (concept_instances.containsKey(instance_uri));
	}



	public Node obtainXML(Document owner_document)
	{
	Element instance_set_element = owner_document.createElement("Instance-Set");
	if (name!=null)
	  	addElement(owner_document,instance_set_element,"Name",name);

	if (description!=null)
	  	addElement(owner_document,instance_set_element,"Description",description);

	for (Enumeration e = concept_instances.elements() ; e.hasMoreElements() ;)
		 {
         instance_set_element.appendChild(((ISInstance) e.nextElement()).obtainXML(owner_document));
	     }

	for (Enumeration e = relation_instances.elements() ; e.hasMoreElements() ;)
		 {
         instance_set_element.appendChild(((ISRelationInstance) e.nextElement()).obtainXML(owner_document));
	     }

	return instance_set_element;
	}



}

