package es.upm.fi.dia.ontology.webode.ui.tree;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import java.util.*;

public class InheritanceTreeModel implements TreeModel
{
    private HashMap hash;
    String root;

    /**
     *
     * Hashmap containing parent-list of children (vector).
     */
    public InheritanceTreeModel (String root, HashMap hash)
    {
	this.hash = hash;
	this.root = root;
    }
    
    public Object getRoot ()
    {
	return root;
    }

    public int getChildCount(Object parent)
    {
	Vector v = (Vector) hash.get (parent);
	if (v == null) return 0;
	return v.size();
    }


    public Object getChild(Object parent, int index)
    {
	return ((Vector) hash.get (parent)).elementAt (index);
    }

    public int getIndexOfChild(Object parent, Object child)
    {
	return ((Vector) hash.get (parent)).indexOf (child);
    }
    
    public boolean isLeaf (Object obj)
    {
	Vector v = (Vector) hash.get (obj);
	return v == null || v.isEmpty();
	//return false;
    }

    public void addTreeModelListener(TreeModelListener l)
    {
    }

    public void removeTreeModelListener(TreeModelListener l)
    {
    }
    
    public void valueForPathChanged(TreePath path,
				    Object newValue)
    {
    }

    public void addTerm (String name)
    {
	((Vector) hash.get (root)).addElement (name);
    }

    public void removeTerm (String name)
    {
	removeTerm (name, root);
    }

    private boolean removeTerm (String name, String root)
    {
	Vector v = (Vector) hash.get (root);
	
	if (v.contains (name)) {
	    v.removeElement (name);
	    Vector foo = (Vector) hash.get (name);

	    if (foo != null) {
		Vector vRoot = (Vector) hash.get (root);
		for (int i = 0; i < foo.size(); i++) {
		    if (!vRoot.contains (foo.elementAt (i)))
			vRoot.addElement (foo.elementAt (i));
		}
	    }
	    return true;
	}
	else {
	    for (int i = 0; i < v.size(); i++) {
		if (removeTerm (name, (String) v.elementAt (i)))
		    return true;
	    }
	    return false;		    
	}
    }
}

