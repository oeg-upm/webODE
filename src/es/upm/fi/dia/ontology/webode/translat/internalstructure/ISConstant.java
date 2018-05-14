package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;




public class ISConstant extends ISStructureElement  
{
    private String name;

    private String type;
    private String value;
    private String measurement_unit;
	
 	public ISConstant (String constant_name,String constant_type, String constant_value, String constant_unit)
	{
		name=constant_name;
		type=constant_type;
		value=constant_value;
		measurement_unit=constant_unit;
		
		description="Descripcion de"+name;
 
	}
 
 	public Node obtainXML (Document owner_document)
	{
	Element constant_element= owner_document.createElement("Constant");

	if (name!=null)
	    addElement(owner_document,constant_element,"Name",name);
	    
	    
	if (description!=null)
	    addElement(owner_document,constant_element,"Description",description);
	    
	if (type!=null)
	    addElement(owner_document,constant_element,"Type",type);
	    	
	if (value!=null)
	    addElement(owner_document,constant_element,"Value",value);
	    	

	if (measurement_unit!=null)
	    addElement(owner_document,constant_element,"Measurement-Unit",measurement_unit);
		
	return constant_element;
	}
}
