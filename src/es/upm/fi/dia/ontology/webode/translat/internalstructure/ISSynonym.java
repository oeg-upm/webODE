package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class ISSynonym extends ISStructureElement
{
    private String name;

	
   public ISSynonym (String synonym_name, String synonym_description)
    {
	name=synonym_name;
	description=synonym_description;		
    }
    
    public Node obtainXML (Document owner_document)
    {
	
	Element synonym_element = owner_document.createElement("Synonym");
	if (name!=null)
	    addElement(owner_document,synonym_element,"Name",name);
	
	if (description!=null)
	    addElement(owner_document,synonym_element,"Description",description);
	
	return synonym_element;
	
	
    }



	
	
}
