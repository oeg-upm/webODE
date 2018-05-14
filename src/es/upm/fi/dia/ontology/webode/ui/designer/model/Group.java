package es.upm.fi.dia.ontology.webode.ui.designer.model;

import java.util.*;

/**
 * A class modelling a group.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.5
 */
public class Group extends Element
{
    private Vector v;
    private transient Vector listeners;

    public Group (String name)
    {
	this (name, null);
    }

    public Group (String name, Vector v)
    {
	super (name);
	
	if (v == null)
	    this.v = new Vector();
	else
	    this.v = v;

	listeners = new Vector();
    }

    public Vector getElements ()
    {
	return v;
    }

    public void addElement (Element el)
    {
	synchronized (v) {
	    if (!v.contains (el))
		v.addElement (el);
	}
	setDirty (true);

	fireElementAdded (el);
    }

    public void removeElement (Element el)
    {
	synchronized (v) {
	    v.removeElement (el);
	}
	setDirty (true);

	fireElementRemoved (el);
    }

    /**
     * Returns whether this group has an element or not.
     */
    public boolean hasElement (String destination)
    {
	synchronized (v) {
	    return v.contains (new Element (destination));
	}
    }
    
    public String toString()
    {
	//return "Group: " + super.toString();
	return getName();
    }
    
    /**
     * Adds a group listener.
     */
    public void addGroupListener (GroupListener gl)
    {
	if (listeners == null) listeners = new Vector();
	listeners.addElement (gl);
    }

    public void removeGroupListener (GroupListener gl)
    {
	if (listeners == null) listeners = new Vector();
	listeners.removeElement (gl);
    }

    private void fireElementAdded (Element el)
    {
	GroupEvent ge = new GroupEvent (this, el);
	for (int i = 0; i < listeners.size(); i++)
	    ((GroupListener) listeners.elementAt (i)).elementAddedToGroup (ge);
    }

    private void fireElementRemoved (Element el)
    {
	GroupEvent ge = new GroupEvent (this, el);
	for (int i = 0; i < listeners.size(); i++)
	    ((GroupListener) listeners.elementAt (i)).elementRemovedFromGroup (ge);
    }
}





