package es.upm.fi.dia.ontology.webode.ui.designer.model;

public class GroupEvent extends java.util.EventObject
{
    private Element el;

    public GroupEvent (Object source, Element el)
    {
	super (source);

	this.el = el;
    }

    public Element getElement ()
    {
	return el;
    }
}
    
