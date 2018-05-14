package es.upm.fi.dia.ontology.webode.ui.designer.model;

public class DesignEvent extends java.util.EventObject
{
    private Relation rel;
    private Element el;
    private String metaproperties;

    public DesignEvent (DesignModel source, Relation rel, Element el,String metaproperties)
    {
	super (source);
	this.rel = rel;
	this.el = el;
	this.metaproperties = metaproperties;
    }

    public Element getElement ()
    {
	return el;
    }

    public Relation getRelation ()
    {
	return rel;
    }
    
    public String getMetaproperties ()
    {
	return metaproperties;
    }
}
