package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;




class ISAbbreviation extends  ISStructureElement 
{
    private String name;

	
   public ISAbbreviation (String abbreviation_name, String abbreviation_description)
      {
    name=abbreviation_name;
    description=abbreviation_description;		
      }
      
      public Node obtainXML (Document owner_document)
      {
    
    Element abbreviation_element = owner_document.createElement("Abbreviation");
    if (name!=null)
        addElement(owner_document,abbreviation_element,"Name",name);
    
    if (description!=null)
        addElement(owner_document,abbreviation_element,"Description",description);
    
    return abbreviation_element;
    
    
      }
	
}