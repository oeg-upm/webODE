package es.upm.fi.dia.ontology.webode.ui.designer;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

/**
 * The general interface for a cell within the designer.
 *
 * @author   Julio César Arpírez Vega
 * @version  1.8
 */
public class Designer extends JComponent implements DesignModelListener, GroupListener
{
    /* Some constants */
    //public static final String ADD_TO_NEW_GROUP  = "Add to new group";
    //public static final String ADD_TO_GROUP      = "Add to group";
    //public static final String REMOVE_FROM_GROUP = "Remove from group";

    private DesignModel designModel;
    private ElementRenderer renderer;
    private RelationRenderer relRenderer;

    // Current element held by the user
    //private Element currentElement;
    private Vector currentElements;

    private int currentElementX, currentElementY;
    // Current relation held by the user
    private Relation currentRelation;
    private boolean bEditable = true;
    
    private boolean bSubclass, bExhaustive, bDisjoint, 
	bPartOf, bInPartOf, bAdhoc,bMetaproperties,bEvaluation,bFocusOnRigid;
	
    private Vector errors;	
   
    /**
     * Constructor.
     * Builds a designer based on the model.
     *
     * @param designModel The model to be used.
     */ 
    public Designer (DesignModel designModel)
    {
	this.designModel = designModel;
	
	renderer = new DefaultDesignRenderer();
	relRenderer = new DefaultRelationRenderer();
	designModel.addDesignModelListener (this);
	designModel.addGroupListener (this);

	currentElements = new Vector();

	// register listeners
	addMouseMotionListener (new MouseMovementControl());
	addMouseListener (new MouseControl());

	// Double buffer
	//setDoubleBuffered (false);
	bSubclass = bExhaustive = bDisjoint = bPartOf = bInPartOf = bAdhoc = true;
	bMetaproperties = false;
	bEvaluation = false;
    }
    
    public void setEditable (boolean b)
    {
	bEditable = b;
    }

    /**
     * Sets a new design model.
     *
     * @param newModel The new model.
     */
    public void setDesignModel (DesignModel newModel)
    {
	//System.out.println ("Designer: setDesignModel");
	designModel = newModel;

	designModel.addDesignModelListener (this);
	designModel.addGroupListener (this);

	repaint();
    }

    /**
     * Gets the model.
     *
     * @return The current model.
     */
    public DesignModel getDesignModel ()
    {
	return designModel;
    }

    /**
     * Returns dimension.
     *
     * @return The minimum dimension.
     */
    public Dimension getMinimumSize ()
    {
	return new Dimension (designModel.getMaxX() + 100, designModel.getMaxY() + 50);
    }

    public Dimension getPreferredSize ()
    {
	return getMinimumSize();
    }

    /**
     * Sets a new renderer.
     *
     * @param renderer The new element renderer.
     */
    public void setElementRenderer (ElementRenderer renderer)
    {
	this.renderer = renderer;
	repaint();
    }

    /**
     * Sets the relation renderer.
     * 
     * @param relRenderer The relation renderer.
     */
    public void setRelationRenderer (RelationRenderer relRenderer)
    {
	this.relRenderer = relRenderer;
	repaint();
    }
    
    public void changeEvaluation (boolean b)
    {
      bEvaluation = b;
    }
    
    public boolean getEvaluation()
    {
      return bEvaluation;
    }
    
    public void setDrawMetaproperties (boolean b)
    {
	bMetaproperties = b;
    }
    
    public boolean getDrawMetaproperties ()
    {
	return this.bMetaproperties;
    }
    /**
     * Draw metaproperties?
     */
    public void drawMetaproperties (boolean b)
    {
	bMetaproperties = b;
	repaint();
    }
    
    public void drawFocusOnRigid (boolean b)
    {
	bFocusOnRigid = b;
	repaint();
    }
    
    public void drawErrorsOntoClean ()
    {
	repaint();
    }
    
    public void putConceptsErrors(String [] errs)
    {
      errors = new Vector(errs.length);
      for(int u=0;u<errs.length;u++)
      {
         errors.add(errs[u]);
      }
    }
    /**
     * Draw subclasses?
     */
    public void drawSubclass (boolean b)
    {
	bSubclass = b;
	repaint();
    }

    /**
     * Draw exhaustive?
     */
    public void drawExhaustive (boolean b)
    {
	bExhaustive = b;
	repaint();
    }

    /**
     * Draw disjoint?
     */
    public void drawDisjoint (boolean b)
    {
	bDisjoint = b;
	repaint();
    }

    /**
     * Draw Part Of?
     */
    public void drawPartOf (boolean b)
    {
	bPartOf = b;
	repaint();
    }

    /**
     * Draw Intransitive Part Of?
     */
    public void drawInPartOf (boolean b)
    {
	bInPartOf = b;
	repaint();
    }

    /**
     * Draw Adhoc?
     */
    public void drawAdhoc (boolean b)
    {
	bAdhoc = b;
	repaint();
    }

    /**
     * Paints the component.
     */
    protected void paintComponent(Graphics g)
    {
	// Get clipping region
	Rectangle rClip = g.getClipBounds();
	
	int x1 = (int) (rClip.getX() + rClip.getWidth());
	int y1 = (int) (rClip.getY() + rClip.getHeight());
	int x  = (int) rClip.getX();
	int y  = (int) rClip.getY();

	// Get relations and paint them first
	Relation[] ar = designModel.getRelations();
	if (ar != null) {
	    for (int i = 0; i < ar.length; i++) {
		// Check if the element is within the limits of the clipping region
		if (!ar[i].getName().equals (Relation.EXHAUSTIVE) && !ar[i].getName().equals (Relation.DISJOINT)) {
		    if (ar[i].getName().equals (Relation.SUBCLASS_OF)) {
			if (bSubclass)
			{
			   String mp1 = designModel.getMetaproperties(ar[i].getOrigin());
			   boolean b1 = IsAntiRigid(mp1);
			   String mp2 = designModel.getMetaproperties(ar[i].getDestination());
			   boolean b2 = IsAntiRigid(mp2);
			   boolean b_final = b2 || b1;
			   String origin = ar[i].getOrigin();
			   boolean b3 = isError(origin);
			   String destination = ar[i].getDestination();
			   boolean b4 = isError(destination);
			   boolean b5 = b4 && b3;
			   relRenderer.render (this, ar[i], ar[i].equals (currentRelation), g,b_final,bFocusOnRigid,b5);
			}    
		    }
		    else if (ar[i].getName().equals (Relation.PART_OF)) {
			if (bPartOf)
			    relRenderer.render (this, ar[i], ar[i].equals (currentRelation), g);
		    }
		    else if (ar[i].getName().equals (Relation.IN_PART_OF)) {
			if (bInPartOf)
			    relRenderer.render (this, ar[i], ar[i].equals (currentRelation), g);
		    }
		    else if (bAdhoc) {
			relRenderer.render (this, ar[i], ar[i].equals (currentRelation), g);
		    }
		}
		else {
		    if ((ar[i].getName().equals (Relation.EXHAUSTIVE) && bExhaustive) ||
			(ar[i].getName().equals (Relation.DISJOINT) && bDisjoint)) {

			String destination = ar[i].getDestination();
			Group group;
			if (designModel.getSupervisor() == null)
			    group = designModel.getGroup (ar[i].getOrigin());
			else
			    group = designModel.getSupervisor().getGroup (ar[i].getOrigin());
			Vector els = group.getElements();

			if (els != null) {
			    int minX, minY, maxX, maxY;
			    minY = minX = Integer.MAX_VALUE;
			    maxX = maxY = Integer.MIN_VALUE;
			    
			    for (int k = 0; k < els.size(); k++) {
				Element el = designModel.getElement (((Element) els.elementAt (k)).getName());
				Rectangle rect = renderer.getRectangle (this, el);
				if (minY > rect.y)
				    minY = rect.y;
				if (minX > rect.x)
				    minX = rect.x;
				if (maxX < rect.x + rect.width)
				    maxX = rect.x + rect.width;
				if (maxY < rect.y + rect.height)
				    maxY = rect.y + rect.height;
			    }
			    g.setColor (Color.orange);
			    g.drawRect (minX - 5, minY - 5, maxX + 10 - minX, maxY + 10 - minY);
			    relRenderer.render (this, new Relation (ar[i].getName(), 
								    "foo",
								    destination),
						minX, minY, (maxX - minX + 1) / 2, g);
			}
		    }
		}
	    }
	}

	// Get elements and paint them
	Element[] ae = designModel.getElements();
	if (ae == null) return;
	
	for (int i = 0; i < ae.length; i++) {
	    // Check if the element is within the limits of the clipping region
	    if (!(ae[i] instanceof Group) && 
		(ae[i].getX() > x || ae[i].getY() > y ||
		 ae[i].getX() < x1 || ae[i].getY() < y1))
           { 		 
		String value = designModel.getMetaproperties(ae[i].getName());
		boolean f_on_rigid = IsAntiRigid(value);
		renderer.render (this, ae[i], ae[i].isSelected(), g,value,bMetaproperties,bFocusOnRigid,f_on_rigid);
	    }	
	    else if (ae[i] instanceof Group) {
		Group group = (Group) ae[i];
		Vector els = group.getElements();
		
		if (els != null) {
		    int minX, minY, maxX, maxY;
		    minY = minX = Integer.MAX_VALUE;
		    maxX = maxY = Integer.MIN_VALUE;
		    
		    for (int k = 0; k < els.size(); k++) {
			Element el = designModel.getElement (((Element) els.elementAt (k)).getName());
			Rectangle rect = renderer.getRectangle (this, el);
			if (minY > rect.y)
			    minY = rect.y;
			if (minX > rect.x)
			    minX = rect.x;
			if (maxX < rect.x + rect.width)
			    maxX = rect.x + rect.width;
			if (maxY < rect.y + rect.height)
			    maxY = rect.y + rect.height;
		    }
		    g.setColor (Color.orange);
		    g.drawRect (minX - 5, minY - 5, maxX + 10 - minX, maxY + 10 - minY);
		    
		}
	    }
	}
    }

    public Element getCurrentElement ()
    {
	if (currentElements.isEmpty())
	    return null;
	else return (Element) currentElements.elementAt (0);
    }

    public Element[] getCurrentElements ()
    {
	if (currentElements.isEmpty())
	    return null;
	else {
	    Element[] ae = new Element[currentElements.size()];
	    currentElements.copyInto (ae);

	    return ae;
	}
    }

    public Relation getCurrentRelation ()
    {
	return currentRelation;
    }


    public void relationAdded (DesignEvent de)
    {
	repaint ();
    }

    public void elementAdded (DesignEvent de)
    {
	repaint();
    }

    public void metapropertiesAdded (DesignEvent de)
    {
	repaint();
    }
    
    public void relationRemoved (DesignEvent de)
    {
	repaint ();
    }

    public void elementRemoved (DesignEvent de)
    {
	//System.out.println ("REMOVED");
	repaint ();
    }

    public void elementAddedToGroup (GroupEvent ge)
    {
	//System.out.println ("Designer: elementAddedToGroup");
	repaint ();
    }

    public void elementRemovedFromGroup (GroupEvent ge)
    {
	//System.out.println ("Designer: elementRemovedFromGroup");
	repaint ();
    }

    /**
     * Inner class to react to mouse movements.
     */
    protected class MouseMovementControl implements MouseMotionListener
    {
	public void mouseDragged (MouseEvent me) 
	{
	    if (!bEditable) return;

	    // Right click?
	    if (me.isMetaDown())
		return;

	    //System.out.println ("Mouse dragged: (" + me.getX() + ", " + me.getY() + ")");
	    if (!currentElements.isEmpty()) {
		// Move related relationships as well
		HashMap hashCounted; // A hash table to keep track or relationships already moved.
		hashCounted = new HashMap();

		for (int i = 0; i < currentElements.size(); i++) {
		    Element currentElement = (Element) currentElements.elementAt (i);
		    currentElement.setX (currentElement.getX() + me.getX() - currentElementX);
		    currentElement.setY (currentElement.getY() + me.getY() - currentElementY);

		    if (currentElements.size() > 1) {
			Relation[] arel = designModel.getRelations(currentElement.getName());
			if (arel != null) {
			    for (int j = 0; j < arel.length; j++)
				hashCounted.put (arel[j], arel[j]);
			}
		    }
		}
		
		// More than one element
		if (currentElements.size() > 1) {
		    // Move relationships
		    Object[] ao = hashCounted.values().toArray();
		    if (ao != null) {
			for (int k = 0; k < ao.length; k++) {
			    Relation foo = (Relation) ao[k];
			    if (foo.getBendX() >= 0) {
				foo.setBendX (foo.getBendX() + me.getX() - currentElementX);
				foo.setBendY (foo.getBendY() + me.getY() - currentElementY);
			    }
			}
		    }		
		}
		
		currentElementX = me.getX();
		currentElementY = me.getY();

		
		repaint();
	    }		
	    if (currentRelation != null) {
		currentRelation.setBendX (me.getX());
		currentRelation.setBendY (me.getY());

		repaint();
	    }
	}

	public void mouseMoved (MouseEvent me)
	{
	    //System.out.println ("Moused moved: " + me);
	}
    }

    /**
     * Inner class to react to mouse events.
     */
    protected class MouseControl extends MouseAdapter
    {
	public void mousePressed (MouseEvent me)
	{
	    if (!bEditable) return;

	    boolean bRepaint = false;
	    if (currentElements != null) {
		for (int i = 0; i < currentElements.size(); i++) {
		    Element currentElement1 = (Element) currentElements.elementAt (i);
		    currentElement1.setSelected (false);
		}
		bRepaint = true;
	    }
	    if (currentRelation != null) {
		currentRelation.setSelected (false);
		bRepaint = true;
	    }
	    
	    //System.out.println ("MousePressed: (" + me.getX() + ", " + me.getY() + ")");

	    // Try to find a suitable element
	    Element currentElement = locateComponent (me.getX(), me.getY());
	    if (currentElement == null || !me.isControlDown())
		currentElements.removeAllElements();

	    if (currentElement != null) {
		// Ok, we found it.
		if (currentElements.contains (currentElement)) {
		    currentElements.removeElement (currentElement);
		    currentElement.setSelected (false);
		}
		else {
		    currentElements.addElement (currentElement);
		}
		currentElementX = me.getX();
		currentElementY = me.getY();
		
		
		for (int i = 0; i < currentElements.size(); i++) {
		    Element currentElement1 = (Element) currentElements.elementAt (i);
		    currentElement1.setSelected (true);
		}
		
		currentRelation = null;

		// Right click
		if (me.isMetaDown()) {
		    _popup (me);
		}
	    }
	    else {
		// Look for a relation instead.
		currentRelation = locateRelation (me.getX(), me.getY());
		if (currentRelation != null) {
		    currentRelation.setSelected (true);
		    bRepaint = true;
		    currentElement = null;
		}
	    }
		
	    if (bRepaint)
		repaint();
	}

	public void MouseReleased (MouseEvent me)
	{
	    if (!bEditable) return;

	    for (int i = 0; i < currentElements.size(); i++) {
		Element currentElement = (Element) currentElements.elementAt (i);
		currentElement.setSelected (false);
	    }
	    currentElements.removeAllElements ();
	    if (currentRelation != null) {
		currentRelation.setSelected (false);
		currentRelation = null;
	    }
	}

	private void _popup (MouseEvent me)
	{
	    JPopupMenu jpm = new JPopupMenu ();
	    
	    ActionListener al = new PopupMenuListener ();

	    // Check what actions to put in the menu
	    JMenuItem jmi = new JMenuItem (DesignerConstants.ADD_TO_NEW_GROUP);
	    jpm.add (jmi);
	    jmi.addActionListener (al);

	    // If there are groups...
	    Group[] groups = designModel.getSupervisor().getGroups();
	    //Group[] belongs = designModel.getSupervisor().getGroupsFor(currentElement.getName());
	    if (groups != null) {
		JMenu jm = new JMenu (DesignerConstants.ADD_TO_GROUP);
		jpm.add (jm);
		ActionListener alAdd = new AddToGroupListener ();
		for (int j = 0; j < groups.length; j++) {
		    //if (!isInGroup(belongs, groups[j])) {
		    jmi = new JMenuItem (groups[j].toString());
		    jm.add (jmi);
		    jmi.addActionListener (alAdd);
		    //}
		}
	    }
	    // Add separator
	    jpm.addSeparator();

	    if (currentElements != null && currentElements.size() == 1) {
		groups = designModel.getSupervisor().getGroupsFor (((Element) currentElements.elementAt (0)).getName());
		if (groups != null) {
		    JMenu jm = new JMenu (DesignerConstants.REMOVE_FROM_GROUP);
		    ActionListener alRemove = new RemoveFromGroupListener ();
		    jpm.add (jm);
		    for (int j = 0; j < groups.length; j++) {
			jmi = new JMenuItem (groups[j].toString());
			jm.add (jmi);
			jmi.addActionListener (alRemove);
		    }
		}
	    }	  
	    jpm.show (Designer.this, me.getX(), me.getY());
	}
    }

    private boolean isInGroup (Group[] belongs, Group g)
    {
	for (int i = 0; i < belongs.length; i++) {
	    if (g.equals (belongs[i])) 
		return true;
	}
	return false;
    }
    
    private class PopupMenuListener implements ActionListener
    {
	public void actionPerformed (ActionEvent ae) 
	{
	    String str = ae.getActionCommand ();
	    if (str.equals (DesignerConstants.ADD_TO_NEW_GROUP)) {
		String groupName = JOptionPane.showInputDialog (DesignerConstants.NameGroup);
		if (groupName != null) {
		    try {
			Group g = new Group (groupName);
			designModel.addElement (g);
			if (currentElements != null) {
			    for (int i = 0; i < currentElements.size(); i++)
				g.addElement ((Element) currentElements.elementAt (i));
			}
		    } catch (Exception e) {
			JOptionPane.showMessageDialog (Designer.this,
						       DesignerConstants.Erroraddinggroup + e.getMessage() + ".",
						       DesignerConstants.Error ,
						       JOptionPane.ERROR_MESSAGE);
		    }
		}
	    }
	}
    }

    private class AddToGroupListener implements ActionListener
    {
	public void actionPerformed (ActionEvent ae) 
	{
	    String groupName = ae.getActionCommand ();
	    Group group = designModel.getSupervisor().getGroup (groupName);

	    for (int i = 0; i < currentElements.size(); i++) {
		group.addElement ((Element) currentElements.elementAt (i));
	    }
	}
    }

    private class RemoveFromGroupListener implements ActionListener
    {
	public void actionPerformed (ActionEvent ae) 
	{
	    String groupName = ae.getActionCommand ();
	    Group group = designModel.getSupervisor().getGroup (groupName);
	    
	    for (int i = 0; i < currentElements.size(); i++) {
		group.removeElement ((Element) currentElements.elementAt (i));
	    }
	}
    }

    
    /**
     * Locates an element based on the x and y coordinates.
     *
     * @return The element at the specified coordinate.   If no element
     *         is found, <tt>false</tt> is returned.
     */
    protected Element locateComponent (int x, int y)
    {
	// Get the appropiate element.
	Element[] ae = designModel.getElements();
	if (ae == null) return null;

	for (int i = 0; i < ae.length; i++) {
	    if (renderer.isWithin (this, ae[i], x, y))
		return ae[i];
	}
	return null;
    }   

    /**
     * Locates a relationship.
     *
     * @return The relationship if found. <tt>null</tt> otherwise.
     */
    protected Relation locateRelation (int x, int y)
    {
	// Get relations and paint them first
	Relation[] ar = designModel.getRelations();
	if (ar == null) return null;

	for (int i = 0; i < ar.length; i++) {
	    if (relRenderer.isWithin (this, ar[i], x, y))
		return ar[i];
	}
	return null;
    }
    
    private boolean IsAntiRigid(String mp)
    {
      if(mp == null || mp == "")
       return false;
      for(int i=0;i<mp.length()-1;i=i+2)
      {
        String aux = mp.substring(i,i+2);
        if(aux.equals("~R") || aux.equals("-R"))
         return true;
      }   
      return false;
    }	
    
    private boolean isError(String name)
    {
      if(errors == null || errors.isEmpty())
        return false;
      else
      {
        String name_aux = name.toLowerCase();
        String [] cs = new String[errors.size()];
        errors.copyInto(cs);
        for(int y=0;y<cs.length;y++)
        {
          if(name_aux.equals(cs[y]))
           return true;
        }
      }  
      return false;      
    }	    
}



