package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ISViewRelation extends ISStructureElement
{
    private String name;
    private String origin;
    private String destination;
    private String x;
    private String y;
	
 	public ISViewRelation (String view_relation_name, String view_relation_x, 
							String view_relation_y, String view_relation_origin,String view_relation_destination)
    {
    	name=view_relation_name;
    	x=view_relation_x;
    	y=view_relation_y;
		origin=view_relation_origin;
		destination=view_relation_destination;
    }
    
    public Node obtainXML (Document owner_document)
    {
    
     Element view_relation_element = owner_document.createElement("View-Relation");
    if (name!=null)
      	addElement(owner_document,view_relation_element,"Name",name);
  	if (origin!=null)
  	   	addElement(owner_document,view_relation_element,"Origin",origin);
  	   
  	if (destination!=null)
  	     	addElement(owner_document,view_relation_element,"Destination",destination);
	
    if (x!=null)
      	addElement(owner_document,view_relation_element,"X",x);
    	
    if (y!=null)
      	addElement(owner_document,view_relation_element,"Y",y);

   
    	
     return view_relation_element;
     
     
    
    }
	
	
	
	
	
}
