package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;




public class ISView extends ISStructureElement
{
    private String name;
    private Vector view_elements;
    private Vector view_relations;
	
	public ISView (String view_name)
	{
		name=view_name;
		view_elements=new Vector();
		view_relations=new Vector();
		ISViewElement view_element=new ISViewElement(view_name+".element1","element1-x","element1-y");
		view_elements.addElement(view_element);
		ISViewRelation view_relation=new ISViewRelation (view_name+".relation","relation-x","relation-y","orig","dest");
		view_relations.addElement(view_relation);
	}
	
	public Node obtainXML (Document owner_document)
	{
		Element view_element=owner_document.createElement("View");
	
		if (name!=null)

			addElement (owner_document,view_element,"Name",name);
		
		for (Enumeration e = view_elements.elements() ; e.hasMoreElements() ;) 
			view_element.appendChild(((ISViewElement)e.nextElement()).obtainXML(owner_document));
	
			
		for (Enumeration e = view_relations.elements() ; e.hasMoreElements() ;) 
			view_element.appendChild(((ISViewRelation)e.nextElement()).obtainXML(owner_document));
		
		return view_element;
	}
}
