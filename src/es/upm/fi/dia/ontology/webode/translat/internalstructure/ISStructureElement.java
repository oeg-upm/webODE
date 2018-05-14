package es.upm.fi.dia.ontology.webode.translat.internalstructure;
import org.w3c.dom.Node;
import org.w3c.dom.Document;



abstract class ISStructureElement implements Cloneable
{

  protected boolean empty=true;
  //METER QUE SEA DOCUMENTABLE Y ANOTABLE_____

  protected String description;


  public abstract Node obtainXML(Document owner_document);

  protected void addElement (Document owner_document,Node parent_node,String tag_name,String text)
  {
    Node new_text_node = owner_document.createTextNode(text);
    Node new_element=owner_document.createElement(tag_name);
    parent_node.appendChild(new_element);
    new_element.appendChild(new_text_node);
  }

  public boolean isEmpty()

  {
    return empty;
  }

  public Object clone() {
    Object o = null;
    try {
      o = super.clone();
    } catch(CloneNotSupportedException e) {
      System.err.println("MyObject can't clone");
    }
    return o;
  }

  public void addDescription (String element_description)
  {
    if (description==null)
      description=element_description;
    else
      description = description + element_description;
  }

  public void setDescription (String element_description)
  {
    if (description==null)
      description=element_description;
    else
      description = description + element_description;
  }

  public String getDescription ()
  {
    return description;
  }


}