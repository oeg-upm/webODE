package es.upm.fi.dia.ontology.webode.ui.designer.model;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

import es.upm.fi.dia.ontology.webode.service.TermRelation;

/**
 * An implementation for the design model based on a list of 
 * relations.
 *
 * @author   Julio César Arpírez Vega
 * @version  1.5
 */
public class DefaultDesignModel implements DesignModel, GroupListener
{
    private transient boolean markAsNew = false;

    private static final String[] builtInRelations = {
	"Subclass-of", "Not-Subclass-of",
	"Disjoint", "Exhaustive", "Transitive-Part-of", "Intransitive-Part-of" };


    // Hashtable holding elements
    private HashMap hashEls;
    // Hashtable holding relations from the origin
    private HashMap hashOrRel;
    // Hashtable holding relations from the destination
    private HashMap hashDestRel;
    // Hashtable holding relations indexed from name
    private HashMap hashRels;
    // Hashtable holding metaproperties indexed from name of concepts
    private HashMap hashMetaproperties;
    private int modified =0;
    // Groups
    private Vector vGroups;
    

    DesignModel supervisor;

    // Listeners 
    private transient Vector v, vGroupListeners;

       

    // Flag to indicate whether or not checks are performed
    private boolean bCheck;
    private boolean bRelCheck;

    
    public DefaultDesignModel ()
    {
       this(null);
    }

    
    public DefaultDesignModel (DefaultDesignModel supervisor)
    {
	hashEls     = new HashMap();
	hashOrRel   = new HashMap();
	hashDestRel = new HashMap();
	hashRels    = new HashMap();
	hashMetaproperties    = new HashMap();
	v           = new Vector();
	vGroups     = new Vector();
	vGroupListeners = new Vector();
	
	this.supervisor = supervisor;

	bCheck = true;
	bRelCheck = true;
    }

    /**
     * Mark added elements to be new.
     */
    public void markAsNew (boolean b)
    {
	markAsNew = b;
    }


    /**
     * Gets groups.
     */
    public Group[] getGroups()
    {
	if (vGroups.isEmpty())
	    return null;
	
	Group[] groups = new Group[vGroups.size()];
	vGroups.copyInto (groups);
	return groups;
    }
    
    /**
     * Gets a given group.
     */
    public Group getGroup (String name)
    {
	int foo = vGroups.indexOf (new Group (name));
	if (foo < 0)
	    return null;
	return (Group) vGroups.elementAt (foo);
    }
    
    /**
     * Adds a new element.
     * 
     * @param el The element to be added.
     */
    public void addElement (Element el) throws DesignException
    {
	// Check if the supervisor agrees
	if (supervisor != null) {
	    supervisor.addElement (el instanceof Group ? el : (Element) el.clone());
	}

	if (el instanceof Group)
	    ((Group) el).addGroupListener (this);
	  
	// Check if the elements exists
	synchronized (hashEls) {
	    if (_checkElementExistence (el)) return;
	    _checkRelationExistence (el.getName());
	    hashEls.put (el, el);
	    if (el instanceof Group)
		vGroups.addElement (el);
	    if (markAsNew)
		el.setNew (true);
	}

	// Notify listeners
	fireElementAdded (el);
    }
    
    
    /**
     * Adds a new element.
     * 
     * @param el The element to be added.
     */
    public void addMetaproperties (String values) throws DesignException
    {
        StringTokenizer token = new StringTokenizer(values);
        String k = token.nextToken("*");
        int l = k.length();
        String c = values.substring(l+1);
        if (supervisor != null) {
	    supervisor.addMetaproperties (values);
	}

	// Check if the elements exists
	synchronized (hashMetaproperties){ 
	    hashMetaproperties.remove (k);	
	    hashMetaproperties.put (k, c);
	} 
	this.modified=1;   
	// Notify listeners
	fireMetapropertiesAdded (c);
    }

    public void addMetapropertiesI (String values) throws DesignException
    {
        StringTokenizer token = new StringTokenizer(values);
        String k = token.nextToken("*");
        int l = k.length();
        String c = values.substring(l+1);
        if (supervisor != null) {
	    supervisor.addMetaproperties (values);
	}

	// Check if the elements exists
	synchronized (hashMetaproperties){ 
	    hashMetaproperties.remove (k);	
	    hashMetaproperties.put (k, c);
	} 
	// Notify listeners
	fireMetapropertiesAdded (c);
    }
    
    public boolean metapropertiesCoherent(DefaultDesignModel ddm)
    {
      return false;
    }
    /**
     * Updates an element.
     */
    public void updateElement (String oldName, String newName) throws DesignException
    {
	_checkElementExistence (new Element (newName));
	int index;
	if (hashEls.containsKey (new Element (oldName))) {
	    Element el = (Element) hashEls.remove (new Element (oldName));
	    el.setName (newName);
	    hashEls.put (el, el);
	} 
	else if ((index = vGroups.indexOf (new Group (oldName))) >= 0) {
	    ((Group) vGroups.elementAt (index)).setName (newName);
	}
    }

    private boolean _checkMetapropertiesExistence (String name) throws DesignException
    {
	// Falta comprobar relaciones...----------------------------------------
	if (hashMetaproperties.containsKey (name)){
	    return true;
	}
	return false;
    }
    
    private boolean _checkElementExistence (Element el) throws DesignException
    {
	// Falta comprobar relaciones...----------------------------------------
	if (hashEls.containsKey (el) || vGroups.contains (el)) {
	    if (bCheck)
		throw new DesignException ("an element with name '" + el.getName() +
					   "' already exists.");
	    else
		return true;
	}
	return false;
    }
    
    private void _checkRelationExistence (String name) throws DesignException
    {
	if (hashRels.containsKey (name))
	    throw new DesignException ("A relation with name '" + name + 
				       "' already exists.");
    }
    
    

    /**
     * Adds a new relationship.
     *
     * @param rel The relationship to be added.
     */
    public void addRelation (Relation rel) throws DesignException
    {
	if (supervisor != null) {
	    supervisor.addRelation ((Relation) rel.clone());
	}

	synchronized (hashOrRel) {
	    // Check the existence of the relationship
	    _checkExistence (rel);

	    // In the reverse order
	    //_checkExistence (new Relation (rel.getName(), rel.getDestination(), rel.getOrigin()));

	    String name = rel.getName();
	    if (name.equals (TermRelation.EXHAUSTIVE) ||
		name.equals (TermRelation.DISJOINT) || 
		name.equals (TermRelation.SUBCLASS_OF)) {
		name = TermRelation.SUBCLASS_OF;

		// Check the existence of an already existing relation
		_checkSubclassingAllowed (rel);
		// Check there are no cycles.
		_checkCycles (name, rel.getOrigin(), rel.getDestination());
	    }
	    // Check part-of relationships.
	    //_checkPartOf (rel);
	    
	    // Add relation both in origin and destination
	    Vector v = (Vector) hashOrRel.get (rel.getOrigin());
	    if (v == null) {
		v = new Vector ();
		hashOrRel.put (rel.getOrigin(), v);
	    }
	    v.addElement (rel);
	    
	    v = (Vector) hashDestRel.get (rel.getDestination());
	    if (v == null) {
		v = new Vector ();
		hashDestRel.put (rel.getDestination(), v);
	    }
	    v.addElement (rel);
	    
	    if (!_isBuiltin (rel.getName()))
		hashRels.put (rel.getName(), rel);
	    
	    if (markAsNew)
		rel.setNew (true);
	}	
	
	fireRelationAdded (rel);
    }

    private boolean _isBuiltin (String name)
    {
	for (int i = 0; i < builtInRelations.length; i++)
	    if (name.equals (builtInRelations[i]))
		return true;
	return false;
    }
    
    /**
     * Checks whether the relation to be added already exists.
     */
    private void _checkExistence (Relation rel) throws DesignException
    {
	if (_checkElementExistence (new Element (rel.getName())))
	    throw new DesignException ("An element with name '" + rel.getName() + 
				       "' already exists.");
	
	// Check if the relation matches the one to insert
	/*Relation rel1 = (Relation) hashRels.get (rel.getName());
	if (rel1 != null) {
	    if (!rel1.getOrigin().equals (rel.getOrigin()) 
		|| !rel1.getDestination().equals (rel.getDestination())) {
		throw new DesignException ("A relation with name '" + rel.getName() +
					   "' already exists between '" + rel1.getOrigin() + 
					   "' and '" + rel1.getDestination() + "'.");
	    }
	    }*/
	
	if (!bCheck)
	    return;	
	
	/*if (hashRels.containsKey (rel.getName()))
	    throw new DesignException ("A relation with name '" + rel.getName() + 
				       "' already exists.");
	*/
	Vector v = (Vector) hashOrRel.get (rel.getOrigin());
	if (v != null && v.contains (rel)) {
	    throw new DesignException ("relation '" + rel.getName() + "' already " +
				       "exists between '" + rel.getOrigin() + "' and '" +
				       rel.getDestination() + "'.");
	}
	v = (Vector) hashDestRel.get (rel.getDestination());
	if (v != null && v.contains (rel)) {
	    throw new DesignException ("relation '" + rel.getName() + "' already " +
				       "exists between '" + rel.getOrigin() + "' and '" +
				       rel.getDestination() + "'.");
	}
    }

    private void _checkPartOf (Relation rel) throws DesignException
    {
	if (rel.getName().equals (TermRelation.TRANSITIVE_PART_OF) ||
	    rel.getName().equals (TermRelation.INTRANSITIVE_PART_OF)) {
	    
	    Vector v = (Vector) hashDestRel.get (rel.getDestination());
	    if (v != null && !v.isEmpty()) {
		for (int i = 0; i < v.size(); i++) {
		    Relation relation = (Relation) v.elementAt (i);	
		    if ((relation.getName().equals (TermRelation.TRANSITIVE_PART_OF) ||
			relation.getName().equals (TermRelation.INTRANSITIVE_PART_OF)) &&
			!relation.equals (rel))
			throw new DesignException ("You cannot add more 'part of' relations to that pair.");
		}
	    }
	}
    }

    private void _checkSubclassingAllowed (Relation relation)
	throws DesignException
    {
	if (!bRelCheck)
	    return;

	Vector v = (Vector) hashDestRel.get (relation.getDestination());
	if (v != null) {
	    for (int i = 0; i < v.size(); i++) {
		Relation rel = (Relation) v.elementAt (i);
		
		if (relation.getName().equals (TermRelation.EXHAUSTIVE) ||
		    relation.getName().equals (TermRelation.DISJOINT)) {
		    
		    if (rel.getName().equals  (TermRelation.EXHAUSTIVE) ||
			rel.getName().equals (TermRelation.DISJOINT) || 
			rel.getName().equals (TermRelation.SUBCLASS_OF)) {
			
			if (!rel.getOrigin().equals (relation.getOrigin()) ||
			    !rel.getDestination().equals (relation.getDestination()) ||
			    !rel.getName().equals (relation.getName()))
			    throw new DesignException ("You cannot add more relationships of that type to '" +
						       relation.getDestination() + "'.");
		    }
		}
		else { // Subclass-of
		    // A subclass-of can be added only if there are no other exhaustive or
		    // disjoint relations for the destination
		    if (rel.getName().equals  (TermRelation.EXHAUSTIVE) ||
			rel.getName().equals (TermRelation.DISJOINT))
			throw new DesignException ("A partition has already been established.\nYou cannot" + 
						   " add more subclasses to the concept '" + 
						   relation.getDestination() + "'.");
		}
	    }
	}
    }	

    /**
     * Checks the existence of cycles.
     */
    private void _checkCycles (String name, String origin, String destination)
	throws DesignException
    {
	if (!bRelCheck)
	    return;
	
	if (name.equals (TermRelation.SUBCLASS_OF)) {
	    if (origin.equals (destination))
		throw new DesignException ("Cycles are not allowed.");
	   
	    // Look if parents have this element as a child.
	    Vector v = (Vector) hashOrRel.get (destination);

	    // If not a single element, may be part of a group.
	    if (v == null  || v.isEmpty()) {
		v = _getGroupsFor (destination); 
		if (v != null) 
		    for (int i = 0; i < v.size(); i++)
			_checkCycles (name, origin, ((Group) v.elementAt (i)).getName());
	    }
	    else {
		Vector vGroups = new Vector();
		
		for (int i = 0; i < v.size(); i++) {
		    Relation rel1 = (Relation) v.elementAt (i);

		    if (rel1.getName().equals(name) || rel1.getName().equals (TermRelation.EXHAUSTIVE) ||
			rel1.getName().equals (TermRelation.DISJOINT)) {
			destination = rel1.getDestination();
			
			if (rel1.getDestination().equals (origin))
			    throw new DesignException ("Cycles are not allowed.");

			Element el = (Element) hashEls.get (destination);
			if (el != null && el instanceof Group) {
			    // Get destination element
			   
			    // Get group elements and repeat process
			    Vector vGr = ((Group) el).getElements();

			    if (vGr.contains (new Element (origin)))
				throw new DesignException ("Cycles are not allowed (partition).");
			    vGroups.addElement (el);
			}
		    }
		}
		
		// Recursive call
		for (int i = 0; i < v.size(); i++) {
		    Relation rel1 = (Relation) v.elementAt (i);
		    if (rel1.getName ().equals (name) || rel1.getName().equals (TermRelation.EXHAUSTIVE) ||
			rel1.getName().equals (TermRelation.DISJOINT))
			_checkCycles (name, origin, ((Relation) v.elementAt (i)).getDestination());
		}

		for (int i = 0; i < vGroups.size(); i++) {
		    Group group = (Group) vGroups.elementAt (i);
		    
		    Vector foo = group.getElements();
		    for (int j = 0; j < foo.size(); j++) {
			_checkCycles (name, origin, ((Element) foo.elementAt (j)).getName());
		    }
		}
	    }
	}
    }

    /**
     * Gets the groups for a given term.
     */
    private Vector _getGroupsFor (String destination)
    {
	Vector vHas = new Vector();
	synchronized (vGroups) {
	    for (int i = 0; i < vGroups.size(); i++) {
		Group foo = (Group) vGroups.elementAt (i);
		if (foo.hasElement (destination))
		    vHas.addElement (foo);
	    }
	}
	return vHas.isEmpty() ? null : vHas;
    }

    public Group[] getGroupsFor (String element)
    {
	Vector foo = _getGroupsFor (element);

	if (foo == null)
	    return null;

	Group[] ag = new Group[foo.size()];
	foo.copyInto (ag);
	return ag;
    }

    /**
     * Removes a given element.
     *
     * @param name The element's name.
     */
    public void removeElement (String name)
    {
	/*if (supervisor != null)
	  supervisor.removeElement (name);*/

	Vector vRels1;
	// Remove related relationships
	synchronized (hashOrRel) {
	    // Origin
	    vRels1 = (Vector) hashOrRel.get (name);
	}
	// Notify listeners
	// Relationships first
	if (vRels1 != null) {
	    vRels1 = (Vector) vRels1.clone();
	    for (int i = 0; i < vRels1.size(); i++) {
		Relation rel = (Relation) vRels1.elementAt (i);
		removeRelation (rel);				
	    }
	}
	synchronized (hashDestRel) {
	    // Origin
	    vRels1 = (Vector) hashDestRel.get (name);
	}
	// Notify listeners
	// Relationships first
	if (vRels1 != null) {
	    vRels1 = (Vector) vRels1.clone();
	    for (int i = 0; i < vRels1.size(); i++) {
		Relation rel = (Relation) vRels1.elementAt (i);
		removeRelation (rel);				
	    }
	}

	/*synchronized (hashOrRel) {
	    // Origin
	    hashOrRel.remove (name);
	    // Destination
	    hashDestRel.remove (name);
	}		
	*/
	Object obj;
	synchronized (hashEls) {
	    // Remove from elements
	    obj = hashEls.remove (new Element (name, 0, 0));
	}
	if (obj instanceof Group) {
	    vGroups.removeElement (obj);
	    ((Group) obj).removeGroupListener (this);
	}
	
	// Now elements
	fireElementRemoved (new Element (name, 0, 0));
    }

    public void removeMetaproperties (String name)
    {
	if(supervisor != null)
	  supervisor.removeMetaproperties(name);
	synchronized (hashMetaproperties) {
	    // Remove from metaproperties
	    hashMetaproperties.remove (name);
	}
	this.modified=1;
    }
    /**
     * Removes a given relationship.
     *
     * @param rel The relation descriptor.
     */
    public void removeRelation (Relation rel)
    {
	/*if (supervisor != null)
	  supervisor.removeRelation (rel);*/

	synchronized (hashOrRel) {
	    // Origin
	    Vector v = (Vector) hashOrRel.get (rel.getOrigin());
	    if (v != null)
		v.removeElement (rel);
	    
	    // Destination
	    v = (Vector) hashDestRel.get (rel.getDestination());
	    if (v != null)
		v.removeElement (rel);

	    // Hashtable
	    hashRels.remove (rel.getName());
	}
	
	// Notify listeners
	fireRelationRemoved (rel);
    }

    /**
     * Gets all the elements in the design (only concepts, not relations).
     *
     * @return All the elements.
     */
    public Element[] getElements ()
    {
	Object[] ao;
	synchronized (hashEls) {
	    ao = hashEls.keySet().toArray ();
	}
	Element[] ae = new Element[ao.length];
	System.arraycopy (ao, 0, ae, 0, ao.length);
	return ae;
    }
    
    public String[] getMetaproperties ()
    {
	Object[] ao;
	synchronized (hashMetaproperties) {
	    ao = hashMetaproperties.keySet().toArray ();
	}
	String[] am = new String[ao.length];
	for(int i=0;i<am.length;i++)
	{
	  String value = (String)hashMetaproperties.get(ao[i]);
	  am[i]=ao[i]+"*"+value;
	}
	return am;
    }
    
    public String getMetaproperties (String name)
    {
	synchronized (hashMetaproperties) {
	   String value = (String)hashMetaproperties.get(name);
	   return value;
	}
    }
    /**
     * Gets a given element.
     *
     * @param name The term's name.
     */
    public Element getElement (String name)
    {
	synchronized (hashEls) {
	    return (Element) hashEls.get (new Element (name));
	}
    }

    /**
     * Gets all the given relations.
     */
    public Relation[] getRelations ()
    {
	Object[] ao;
	synchronized (hashOrRel) {
	    ao = hashOrRel.keySet().toArray(); // Keys
	}
	
	Vector vrel = new Vector();
	synchronized (hashOrRel) {
	    for (int i = 0; i < ao.length; i++) {
		Vector foo = (Vector) hashOrRel.get (ao[i]);
		for (int j = 0; j < foo.size(); j++)
		    vrel.addElement (foo.elementAt (j));
	    }
	}
	
	Relation[] ar = new Relation[vrel.size()];
	vrel.copyInto (ar);
	return ar;
    }

    public Relation[] getRelations (String name)
    {
	Vector v, v1;
	
	synchronized (hashOrRel) {
	    v = (Vector) hashOrRel.get (name);
	}
	synchronized (hashDestRel) {
	    v1 = (Vector) hashDestRel.get (name);
	}

	if (v == null) {
	    if (v1 == null) 
		return null;
	    else {
		Relation[] ao = new Relation[v1.size()];
		v1.copyInto (ao);

		return ao;
	    }
	}
	else {
	    if (v1 != null) {
		v.addAll (v1);
	    }
	    Relation[] ao = new Relation[v.size()];
	    v.copyInto (ao);
	    
	    return ao;
	}
    }

    /**
     * Gets the maximum x coordinate.
     *
     * @return The maximum x coordinate.
     */
    public int getMaxX ()
    {
	int maxx = 0;
	synchronized (hashEls) {
	    Iterator it = hashEls.values().iterator();

	    // ----------------------- Cuidado con los grupos...
	    while (it.hasNext()) {
		Element el = (Element) it.next();
		if (el.getX() > maxx)
		    maxx = el.getX();
	    }	    
	}
	return maxx;
    }


    /**
     * Gets the maximum y coordinate.
     *
     * @return The maximum y coordinate.
     */
    public synchronized int getMaxY ()
    {
	int maxy = 0;
	synchronized (hashEls) {
	    Iterator it = hashEls.values().iterator();

	    // Grupos------------------------------------------------
	    while (it.hasNext()) {
		Element el = (Element) it.next();
		if (el.getY() > maxy)
		    maxy = el.getY();
	    }	    
	}
	return maxy;
    }

    public void addDesignModelListener (DesignModelListener dml)
    {
	synchronized (v) {
	    if (!v.contains (dml))
		v.addElement (dml);
	}
    }

    public void removeDesignModelListener (DesignModelListener dml)
    {
	synchronized (v) {
	    v.removeElement (dml);
	}
    }

    public String toString ()
    {
	return "ELEMENTS:\n-----------------------------\n" + 
	    hashEls + "\n\nRELATIONS (ORIGIN):\n-----------------------------\n" + 
	    hashOrRel + "\n\nRELATIONS (DESTINATION):\n---------------------------\n" + 
	    hashDestRel;
    }
    public int getMetapropertiesModified()
    {
      return this.modified;
    }
    /**
     * Enables or disables checks.
     *
     * @param b <tt>true</tt> enables checks.
     */
    public void setChecks (boolean b)
    {
	bCheck = b;
    }
    
    /**
     * Disable relation checks.
     */
    public void setRelationChecks (boolean b)
    {
	this.bRelCheck = b;
    }
    
    /**
     * Fires an event signaling an element was added.
     */
    private void fireElementAdded (Element el)
    {
	Vector co;
	synchronized (v) {
	    co = (Vector) v.clone();
	}
	DesignEvent de = new DesignEvent (this, null, el,null);
	for (int i = 0; i < co.size(); i++)
	    ((DesignModelListener) co.elementAt(i)).elementAdded (de);
    }

    private void fireMetapropertiesAdded (String metaproperties)
    {
	Vector co;
	synchronized (v) {
	    co = (Vector) v.clone();
	}
	DesignEvent de = new DesignEvent (this, null, null,metaproperties);
	for (int i = 0; i < co.size(); i++)
	    ((DesignModelListener) co.elementAt(i)).metapropertiesAdded (de);
    }

    
    private void fireRelationAdded (Relation rel)
    {
	Vector co;
	synchronized (v) {
	    co = (Vector) v.clone();
	}
	DesignEvent de = new DesignEvent (this, rel, null,null);
	for (int i = 0; i < co.size(); i++)
	    ((DesignModelListener) co.elementAt(i)).relationAdded (de);
    }

    private void fireRelationRemoved (Relation rel)
    {
	Vector co;
	synchronized (v) {
	    co = (Vector) v.clone();
	}
	DesignEvent de = new DesignEvent (this, rel, null,null);
	for (int i = 0; i < co.size(); i++)
	    ((DesignModelListener) co.elementAt(i)).relationRemoved (de);
    }


    private void fireElementRemoved (Element el)
    {
	Vector co;
	synchronized (v) {
	    co = (Vector) v.clone();
	}
	DesignEvent de = new DesignEvent (this, null, el,null);
	for (int i = 0; i < co.size(); i++) {
	    ((DesignModelListener) co.elementAt(i)).elementRemoved (de);
	}
    }

    public void elementAddedToGroup (GroupEvent ge)
    {
	// Check cycles and all that stuff
	for (int i = 0; i < vGroupListeners.size(); i++)
	    ((GroupListener) vGroupListeners.elementAt (i)).elementAddedToGroup (ge);
    }

    public void elementRemovedFromGroup (GroupEvent ge)
    {
	for (int i = 0; i < vGroupListeners.size(); i++)
	    ((GroupListener) vGroupListeners.elementAt (i)).elementRemovedFromGroup (ge);
    }

    public void addGroupListener (GroupListener dml)
    {
	synchronized (vGroupListeners) {
	    if (!vGroupListeners.contains (dml))
		vGroupListeners.addElement (dml);
	}
    }

    public void removeGroupListener (GroupListener dml)
    {
	synchronized (vGroupListeners) {
	    vGroupListeners.removeElement (dml);
	}
	
    }

    /**
     * Sets the supervisor model.
     */
    public void setSupervisor (DesignModel dm)
    {
	this.supervisor = dm;
    }

    /**
     * Gets the supervisor model.
     */
    public DesignModel getSupervisor ()
    {
	return supervisor;
    }

    /**
     * Returns whether an element is within this
     * view.
     */
    public boolean containsElement (Element el)
    {
	return hashEls.containsKey (el);
    }

    public Relation[] getRelationsByDestination (String destination)
    {
	Vector v = (Vector) hashDestRel.get (destination);
	if (v == null)
	    return null;
	Relation[] ar = new Relation[v.size()];
	v.copyInto (ar);

	return ar;
    }
	

    public Relation[] getRelationsByOrigin (String origin)
    {
	Vector v = (Vector) hashOrRel.get (origin);
	if (v == null)
	    return null;
	Relation[] ar = new Relation[v.size()];
	v.copyInto (ar);

	return ar;
    }
}
    









