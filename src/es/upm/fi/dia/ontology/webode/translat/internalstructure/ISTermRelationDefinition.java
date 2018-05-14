package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;




public class ISTermRelationDefinition extends ISStructureElement
{
    private String name;     
    private String origin;
    private String destination;
    private String maximum_cardinality;
	private String minimum_cardinality;
    private Vector related_references;
    private Vector related_properties; 
	
	public ISTermRelationDefinition (String definition_name,String definition_origin,
					 String definition_destination,String definition_maximum_cardinality, String definition_minimum_cardinality)
	{
	name=definition_name;
	origin=definition_origin;
	destination=definition_destination;
	maximum_cardinality=definition_maximum_cardinality;
	minimum_cardinality=definition_minimum_cardinality;
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
	
    public Node obtainXML (Document owner_document)
    {
    
    Element definition_element= owner_document.createElement("Term-Relation");
 	 if (name!=null)
     	addElement(owner_document,definition_element,"Name",name);
    
    if (description!=null)
      	addElement(owner_document,definition_element,"Description",description);
    
    if (origin!=null)
      	addElement(owner_document,definition_element,"Origin",origin);
    	
    if (destination!=null)
      	addElement(owner_document,definition_element,"Destination",destination);
    
    if (maximum_cardinality!=null)
	  	addElement(owner_document,definition_element,"Maximum-Cardinality",maximum_cardinality);
	
	for (Enumeration e = related_references.elements() ; e.hasMoreElements() ;) 
		addElement (owner_document,definition_element,"Related-Reference",(String)(e.nextElement()));
	
	for (Enumeration e = related_properties.elements() ; e.hasMoreElements() ;) 
		addElement (owner_document,definition_element,"Related-Property",(String)(e.nextElement()));
	
    	
     return definition_element;
     

    }
	
	public ISTermRelation obtainTermRelation ()
	{
	 ISTermRelation new_term_relation = new ISTermRelation (name,origin,destination,maximum_cardinality);
	 new_term_relation.setProperties(related_properties);
	 new_term_relation.setReferences(related_references);
	 new_term_relation.setDescription(description);
	 return new_term_relation;	 
	}
    
	
	
	public String getName()
	{	
		return name;
	}

	public void setName(String definition_name)
	{
		name=definition_name;
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



}