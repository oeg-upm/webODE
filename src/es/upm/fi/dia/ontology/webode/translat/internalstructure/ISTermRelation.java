package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;

public class ISTermRelation extends ISStructureElement
{
    private String name;

    private String origin;
    private String destination;
    private String maximum_cardinality;
    private String minimum_cardinality;
    private Vector related_references;
    private Vector related_properties;

    public void toWebODE(ODEService ode, String ontologyName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException
    {
//      ode.insertTermRelation(new TermRelation(ontologyName,name,origin,destination,Integer.parseInt(this.maximum_cardinality),(String[])this.related_properties.toArray(new String[0])));
      System.out.println("ode.insertTermRelation(new TermRelation('"+ontologyName+"',null,'"+name+"','"+origin+"','"+destination+"',"+Integer.parseInt(this.maximum_cardinality)+",'"+(String[])this.related_properties.toArray(new String[0])+"'))");
      ode.insertTermRelation(new TermRelation(ontologyName,null,name,origin,destination,Integer.parseInt(this.maximum_cardinality),(String[])this.related_properties.toArray(new String[0])));
    }
	public ISTermRelation (String relation_name,String relation_origin,
					 String relation_destination,String relation_maximum_cardinality)
	{
	name=relation_name;
	origin=relation_origin;
	destination=relation_destination;
	maximum_cardinality=relation_maximum_cardinality;
	related_references= new Vector();
	related_properties =new Vector();
	}


	public void setMaxCardinality (String max)
	{
		maximum_cardinality=max;
	}


	public String getMaxCardinality()
	{
		return maximum_cardinality;
	}

	public void setMinCardinality (String min)
	{
		minimum_cardinality=min;
	}

	public String getMinCardinality()
	{
		return minimum_cardinality;
	}

    public Node obtainXML (Document owner_document)
    {

     Element relation_element= owner_document.createElement("Term-Relation");
    if (name!=null)
      	addElement(owner_document,relation_element,"Name",name);

    if (description!=null)
      	addElement(owner_document,relation_element,"Description",description);

    if (origin!=null)
      	addElement(owner_document,relation_element,"Origin",origin);

    if (destination!=null)
      	addElement(owner_document,relation_element,"Destination",destination);

    if (maximum_cardinality!=null)
	  	addElement(owner_document,relation_element,"Maximum-Cardinality",maximum_cardinality);

	for (Enumeration e = related_references.elements() ; e.hasMoreElements() ;)
		addElement (owner_document,relation_element,"Related-Reference",(String)(e.nextElement()));

	for (Enumeration e = related_properties.elements() ; e.hasMoreElements() ;)
		addElement (owner_document,relation_element,"Related-Property",(String)(e.nextElement()));


     return relation_element;




    }


public String getName()
{
	return name;
}

public void setName(String term_relation_name)
{
	name=term_relation_name;
}

public String getOrigin()
{
	return origin;
}

public void setOrigin(String origin_name)
{
	origin=origin_name;
}

public String getDestination()
{
	return destination;
}

public void setDestination(String destination_name)
{
	destination=destination_name;
}




public void addProperty(String property_name)
{
	related_properties.add(property_name);
}


public void setProperties(Vector properties)
{
	related_properties=properties;
}
public void setReferences(Vector references)
{
	related_references=references;
}



}