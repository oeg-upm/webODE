package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;





public class ISViewElement extends ISStructureElement
{
    private String name;
    private String x;
    private String y;


	public ISViewElement (String view_element_name, String view_element_x, String view_element_y)
	{
		name=view_element_name;
		x=view_element_x;
		y=view_element_y;
	}
	
	public Node obtainXML (Document owner_document)
	{
	
	 Element view_element_element = owner_document.createElement("View-Element");
	if (name!=null)
	  	addElement(owner_document,view_element_element,"Name",name);
		
	if (x!=null)
	  	addElement(owner_document,view_element_element,"X",x);
		
	if (y!=null)
	  	addElement(owner_document,view_element_element,"Y",y);

		
	 return view_element_element;
	 
	 
	
	}
}
