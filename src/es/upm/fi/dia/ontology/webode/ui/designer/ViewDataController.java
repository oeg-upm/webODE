package es.upm.fi.dia.ontology.webode.ui.designer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;

import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

/**
 * Class responsible for controlling the view
 * management, that is, loading, commiting and
 * changing views as appropiate.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.6
 */
public class ViewDataController implements ItemListener
{
    public static final int DEFAULT_ORIGIN_X = 30;
    public static final int DEFAULT_ORIGIN_Y = 100;
    public static final int DEFAULT_ORIGIN_ROOT= 30;
    public static final int GAP = 60;
    public static final int GAP_Y = 70;
    public static final int DEFAULT_HEIGHT = 30;
    public static final int DEFAULT_WIDTH = 70;

    private JComboBox jcbViews;
    private DesignerFrame jfr;

    // Models for the views indexed by name
    private HashMap hashViews;
    private DesignModel defaultView;
    private Designer designer;
    private URL urlPost;
    private HashMap hashRemoved;
    private Vector vRemovedCompletely;
    private DefaultDesignModel ddmSupervisor;

    
    public ViewDataController (JComboBox jcbViews, DesignerFrame jfr, 
			       DesignModel defaultView, Designer designer, URL urlPost)
    {
	this.jcbViews    = jcbViews;
	this.jfr         = jfr;
	this.defaultView = defaultView;
	this.designer    = designer;
	this.urlPost     = urlPost;
	
	hashViews        = new HashMap();
	hashRemoved      = new HashMap();
	vRemovedCompletely = new Vector();

	_loadSupervisor ();
    }
    
    private void _loadSupervisor ()
    {
	ddmSupervisor = new DefaultDesignModel();
	System.out.println (DesignerConstants.Loadingsupervisor);
	new Communicator (jfr, urlPost, Command.RETRIEVE_DEFAULT, 
			  null, null, new CommandRunnable () {
	    public void run() {
		// First elements
		Object[] ao = (Object[]) cd.data;
		
		Element[] ae = (Element[]) ao[0];
		Relation[] ar = (Relation[]) ao[1];
		Group[] ag = (Group[]) ao[2];
		String[] metaproperties = (String[]) ao[3];

		ddmSupervisor = new DefaultDesignModel();
		// Disable checks (data from server comes ok)
		ddmSupervisor.setChecks (false);
		ddmSupervisor.setRelationChecks (false);
		
		try {
		    if (ae != null) {
			for (int i = 0; i < ae.length; i++) 
			    ddmSupervisor.addElement (ae[i]);
		    }
		    if (ar != null) {
			for (int i = 0; i < ar.length; i++)
			    ddmSupervisor.addRelation (ar[i]);
		    }
		    if (ag != null) {
			for (int i = 0; i < ag.length; i++) {
			    ddmSupervisor.addElement (ag[i]);
			}
		    }
		    if (metaproperties != null) {
		      for (int i = 0; i < metaproperties.length; i++) {
			    ddmSupervisor.addMetapropertiesI (metaproperties[i]);
			}
		    }
		} catch (Exception e) {
		    System.err.println (DesignerConstants.UnexpectedError +
					e);
		    e.printStackTrace();
		}
		System.out.println (DesignerConstants.Supervisorloaded);
		// Enable checks again
		//ddm.setChecks (true);
		ddmSupervisor.setRelationChecks (true);
		registerModelListeners (ddmSupervisor);
		
		jfr.setEditable (false);
		jfr.setDesignModel (_createDefaultView());
		
		// Mark elements as new
		//ddm.markAsNew (true);
	    }
	}).start();
    }	    

    public void itemStateChanged (ItemEvent ie)
    {
	if (ie.getStateChange() == ie.DESELECTED) return;

	final Object selectedItem = jcbViews.getSelectedItem ();
	if (selectedItem.equals (DesignerConstants.DEFAULT_VIEW)) {
	    jcbViews.setSelectedIndex (0);
	    
	    // Change window model
	    jfr.setEditable (false);
	    jfr.setDesignModel (_createDefaultView());
	    
	    return;
	}
	
	// Get loaded designed model if available
	DesignModel dm = (DesignModel) hashViews.get (selectedItem);
	if (dm == null) {
	    // Ask for confirmation
	   
	    if (JOptionPane.showConfirmDialog (jfr, 
					       DesignerConstants.Theview + selectedItem +  DesignerConstants.hasn  + 
					       DesignerConstants.want ,
					       DesignerConstants.Confirmation ,
					       JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
		// Load view from server
		new Communicator (jfr, urlPost, Command.RETRIEVE_VIEW, 
				  selectedItem, null, new CommandRunnable () {
		    public void run() {
			// Change window model
			DefaultDesignModel model = _buildModel ();
			jfr.setEditable (true);
			jfr.setDesignModel (model); 

			// Notify view data controller
			ViewDataController.this.addModel (selectedItem.toString(), model);
		    }

		    private DefaultDesignModel _buildModel () {
			// First elements
			Object[] ao = (Object[]) cd.data;
			
			Element[] ae = (Element[]) ao[0];
			Relation[] ar = (Relation[]) ao[1];
			String[] metaproperties = (String[]) ao[2];
			DefaultDesignModel ddm = new DefaultDesignModel(ddmSupervisor);
			// Disable checks (data from server comes ok)
			ddm.setChecks (false);
			
			try {
			    if (ae != null) {
				for (int i = 0; i < ae.length; i++) 
				    ddm.addElement (ae[i]);
			    }
			    if (ar != null) {
				for (int i = 0; i < ar.length; i++) {
				    //System.out.println ("REL: " + ar[i]);
				    ddm.addRelation (ar[i]);
				}
			    }	
			    if (metaproperties != null) {
			    	 for (int i = 0; i < metaproperties.length; i++) {
			            ddm.addMetapropertiesI (metaproperties[i]);
			         }
			    }
			    synchronized (hashViews) {
	                    Object [] views = hashViews.keySet().toArray();
	                    if (views != null && views.length > 0) 
		            {	
		              synchronized (hashViews) {
	                      DefaultDesignModel ddm_aux =(DefaultDesignModel) hashViews.get (views[0]);
		              String [] metaproperties1 = ddm_aux.getMetaproperties();
		              for (int h = 0; h < metaproperties1.length; h++) 
		              {
		                 ddm.addMetaproperties (metaproperties1[h]);
			      }
			     }
			    }
			   } 
			      
			    	
			} catch (Exception e) {
			    JOptionPane.showMessageDialog (jfr, 
							   DesignerConstants.Unex + 
							   e.getMessage() + ".",
							   DesignerConstants.Error,
							   JOptionPane.ERROR_MESSAGE);
			}
			// Enable checks again
			ddm.setChecks (true);
			
			// Mark elements as new
			ddm.markAsNew (true);

			return ddm;
		    }
			
		}).start();
	    }
	    else {
		// If nothing was selected, print out default view
		jcbViews.setSelectedIndex (0);

		// Change window model
		jfr.setDesignModel (_createDefaultView ());
	    }
	}
	else {
	    jfr.setEditable (true);
	    // Change window model
	    jfr.setDesignModel (dm);
	}
    }	

    
    public DesignModel getModel (String view)
    {
	synchronized (hashViews) {
	    return (DesignModel) hashViews.get (view);
	}
    }

    public Object[] getViews ()
    {
	synchronized (hashViews) {
	    return hashViews.keySet().toArray();
	}
    }


    public void addModel (String view, DesignModel dm)
    {
	synchronized (hashViews) {
	    hashViews.put (view, dm);
	}

	// Set supervisor
	dm.setSupervisor (ddmSupervisor);

	// Change window model
	jfr.setEditable (true);
	jfr.setDesignModel (dm);
	dm.addDesignModelListener (designer);

	((DefaultDesignModel) dm).markAsNew (true);

	// Add vector for removal.
	hashRemoved.put (dm, new Vector());
	registerModelListeners (dm);
	
    }

    
    public void removeModel (String view)
    {
	DefaultDesignModel ddm;
	synchronized (hashViews) {
	    ddm = (DefaultDesignModel) hashViews.remove (view);
	}
	// Change window model
	//jfr.setDesignModel (///); <<------------------
	//	ddm.removeDesignModelListener ();
    }

    private void registerModelListeners (DesignModel ddm) 
    {
	final DesignModel dd = ddm;
	ddm.addDesignModelListener (new DesignModelAdapter() {
	    public void relationRemoved (DesignEvent de) {
		Relation rel = de.getRelation();
		if (de.getSource() == ddmSupervisor) {
		    
		    /*if (!rel.isNew()) {
		      System.out.println ("2#");*/
		    vRemovedCompletely.addElement (rel);
		    //}
		    // Delete from all views
		    _deleteFromViews (rel);
		}
		else {
		    // Add only if not new
		    if (!rel.isNew())
			//vRemoved.addElement (rel);
			((Vector) hashRemoved.get (dd)).addElement (rel);
		}
	    }
	    
	    public void elementRemoved (DesignEvent de) {
		Element el = de.getElement();
		if (de.getSource() == ddmSupervisor) {
		    if (!el.isNew())
			vRemovedCompletely.addElement (el);

		    // Delete from all views
		    _deleteFromViews (el);
		}
		else {
		    // Add only if not new
		    if (!el.isNew())
			//vRemoved.addElement (de.getElement());
			((Vector) hashRemoved.get (dd)).addElement (de.getElement());
		}
	    }

	    /*public void elementRemovedCompletely (DesignEvent de) {
		Element el = de.getElement();
		if (!el.isNew())
		    vRemovedCompletely.addElement (el);
	    }
	    
	    public void relationRemovedCompletely (DesignEvent de) {
		Relation rel = de.getRelation();
		if (!rel.isNew())
		    vRemovedCompletely.addElement (rel);
		    }*/
	});
    }

    private void _deleteFromViews (Element el)
    {
	Iterator it = hashViews.values().iterator();
	while (it.hasNext()) 
	    ((DesignModel) it.next()).removeElement (el.getName());
    }

    private void _deleteFromViews (Relation rel)
    {
	Iterator it = hashViews.values().iterator();
	while (it.hasNext()) 
	    ((DesignModel) it.next()).removeRelation (rel);
    }

    public Vector getRemovedElements (DefaultDesignModel ddm)
    {
	return (Vector) hashRemoved.get (ddm);
    }

    public Vector getCompletelyRemovedElements ()
    {
	Vector foo = vRemovedCompletely;
	// Disregard elements next time
	vRemovedCompletely = new Vector();
	
	return foo;
    }

    public String[] getTerms()
    {
	Element[] ae = ddmSupervisor.getElements();
	if (ae == null)
	    return null;
	String[] foo = new String[ae.length];
	for (int i = 0; i < ae.length; i++)
	    foo[i] = ae[i].getName();

	return foo;
    }

    public String[] getRelations()
    {
	Relation[] ae = ddmSupervisor.getRelations();
	if (ae == null)
	    return null;

	// Remove duplicates
	HashMap aux = new HashMap();

	for (int i = 0; i < ae.length; i++)
	    aux.put (ae[i].getName(), null);
	
	int size = aux.size();
	Iterator it = aux.keySet().iterator();
	String[] foo = new String[size];
	int i = 0;
	while (it.hasNext()) 
	    foo[i++] = (String) it.next();
	return foo;
    }

    public DesignModel getSupervisor()
    {
	return ddmSupervisor;
    }

    public DesignModel _createDefaultView ()
    {
	// Get elements from supervisor
	Element[] ae = ddmSupervisor.getElements();
	if (ae != null) {
	    // Put in a hash table
	    HashMap names = new HashMap();
	    HashMap other = new HashMap();
	    for (int i = 0; i < ae.length; i++) {
		names.put (ae[i].getName(), ae[i]);
		other.put (ae[i].getName(), ae[i]);
	    }
           
	    // Discover roots
	    Relation[] ar = ddmSupervisor.getRelations();
	    if (ar != null) {
		for (int i = 0; i < ar.length; i++) {
		    String origin = ar[i].getOrigin();
		    Element el;
		    if ((el = (Element) names.get (origin)) instanceof Group) {
			// Remove all group members
			Vector v = ((Group) el).getElements();
			if (v != null) {
			    for (int k = 0; k < v.size(); k++) 
				names.remove (((Element) v.elementAt (k)).getName());
			}
		    }
		    else 
			// Remove a single element
			names.remove (origin);
		}
	    }

	    // In names, there are the roots
	    Iterator it = names.values().iterator();
	    int x = DEFAULT_ORIGIN_X;
	    int y = DEFAULT_ORIGIN_Y;
	    while (it.hasNext()) {
		Element el = (Element) it.next();
		//if (!(el instanceof Group)) {
		//System.out.println ("XXX: " + el.getName());
		int foo = _calculatePositions (el.getName(), other, x, y);
		
		//System.out.println ("X: " + x + "--" + foo + ":" + el.getName());
		x += foo == 0 ? 0  : (foo + 2 * GAP);
		//}
	    }
	}
	
	return ddmSupervisor;	    
    }
    
    private int _calculatePositions (String root, HashMap other, int x, int y)
    {
	
	Relation[] ar = ddmSupervisor.getRelationsByDestination (root);
	Vector vSub = new Vector();
	if (ar != null)
	    for (int i = 0; i < ar.length; i++)
		if (ar[i].getName().equals (Relation.SUBCLASS_OF) || ar[i].getName().equals(Relation.EXHAUSTIVE) ||
		    ar[i].getName().equals (Relation.DISJOINT))
		    vSub.addElement (ar[i]);
	
	if (vSub.isEmpty()) {
	    /*Element el = (Element) other.get (root);
	      if (el == null)
	      return -1;
	      else {
	      other.remove (root);
	      boolean bDirty = el.isDirty();
	      el.setX (x);
	      el.setY (y);
	      el.setDirty (bDirty);
	      // No nodes hanging
	      return DEFAULT_WIDTH;
	      }*/
	    Element elRoot = (Element) other.get (root);
	    if (elRoot == null)
		return -GAP;
	    else if (elRoot instanceof Group) return 0;
	    boolean bdirty = elRoot.isDirty();
	    elRoot.setX (x);
	    elRoot.setY (y);
	    elRoot.setDirty (bdirty);
	    other.remove (root);

	    return DefaultDesignRenderer.getRectangleStatic (jfr, elRoot).width;
	}
	else {
	    Element elRoot = (Element) other.get (root);
	    int width = 0;
	    for (int i = 0; i < vSub.size(); i++) {
		Element el = (Element) other.get (((Relation) vSub.elementAt (i)).getOrigin());
		if (el != null && el instanceof Group) {
		    Group gr = (Group) el;
		    Vector v = gr.getElements();
		    for (int k = 0; k < v.size(); k++) {
			el = (Element) v.elementAt (k);
			int foo = _calculatePositions (el.getName(), other, x + width, y + GAP_Y + DEFAULT_HEIGHT);
			width += foo + GAP;
		    }
		}
		else if (el != null) {
		    other.remove (root);
		    int foo = _calculatePositions (el.getName(), other, x + width, y + GAP_Y + DEFAULT_HEIGHT);
		    width += foo + GAP;
		}
	    }
	    width -= GAP;
	    boolean bdirty = elRoot.isDirty();
	    Rectangle rect = DefaultDesignRenderer.getRectangleStatic (jfr, elRoot);
	    //System.out.println ("width: " +width + "-" + rect.width);
	    elRoot.setX (x + (width - rect.width) / 2);
	    elRoot.setY (y);
	    elRoot.setDirty (bdirty);
	    	    
	    return width;
	}
    }
}

