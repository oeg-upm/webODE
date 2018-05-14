package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class ISReference extends ISStructureElement
{
    private String name;
    private String description;
	
	ISReference (String reference_name, String reference_description)
	{
		name=reference_name;
		description=reference_description;
	}
	
public Node obtainXML (Document owner_document)
{
	Element reference_element =owner_document.createElement("Reference");
	if (name!=null)
		addElement (owner_document,reference_element,"Name",name);
	
	if (description!=null)    
		addElement (owner_document,reference_element,"Description",description);
		
	return reference_element;
}
}
