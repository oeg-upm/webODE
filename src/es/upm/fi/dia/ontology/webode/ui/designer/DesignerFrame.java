package es.upm.fi.dia.ontology.webode.ui.designer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

/**
 * OntoDesigner window.
 *
 * @author  Julio César Arpírez Vega
 * @version 1.4
 */
public class DesignerFrame extends JFrame implements ItemListener, 
						     ActionListener
{
    public static final int STRUT_SIZE = 15;

    private Designer designer;
    private DesignModel  ddm;
    private JToggleButton btAddTerm, btAddRelation, btRemove,
	btSubclass, btExhaustive, btDisjoint, btPartOf, btRemoveCompletely,
	btInPartOf, btFoo,btMetaproperties;
    private int index = 1;
    private Element firstElement;
    private boolean bRelation;
    private ActionListener al;
    JButton jbtCommit, jbtCommitAll, jbtNewView, jbtDeleteView;
    private String[] terms, relations,instances;
    private JComboBox jcb;  // Views
    Vector views;
    ViewDataController vdc;
    private ButtonGroup bg;
    private int cardinality;
    private  boolean bEditable;

    /*static {
	try {
	    UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	    System.err.println ("Error setting system look and feel.  Working with Java's.");
	}
	}*/

    public DesignerFrame (ElementRenderer er, boolean includeSubclass, DesignModel ddm, 
			  String[] views, String[] terms, String[] relations,String[] instances, 
			  ActionListener al, URL urlPost, Icon[] images) 
    {
	super (DesignerConstants.VERSION);

	this.al = al;
	this.ddm = ddm;
	((DefaultDesignModel) ddm).markAsNew (true);
	designer = new Designer (ddm);

	this.terms = terms;
	this.relations = relations;
	this.instances = instances;
        
	// Some buttons
	btAddTerm = new JToggleButton (images[0]);
	btAddTerm.setToolTipText (DesignerConstants.ADD_TERMC);
	btAddRelation = new JToggleButton (images[5]);
	btAddRelation.setToolTipText (DesignerConstants.ADD_RELATIONC);
	btRemove = new JToggleButton (images[6]);
	btRemove.setToolTipText (DesignerConstants.REMOVE_ELEMENTC);
	btRemoveCompletely = new JToggleButton (images[9]);
	btRemoveCompletely.setToolTipText (DesignerConstants.REMOVE_ELEMENT_COMPLETELYC);
	btSubclass = new JToggleButton (images[2]);
	btSubclass.setToolTipText (DesignerConstants.SUBCLASS_OFC);
	btPartOf = new JToggleButton (images[1]);
	btPartOf.setToolTipText (DesignerConstants.PART_OFC);
	btInPartOf = new JToggleButton (images[12]);
	btInPartOf.setToolTipText (DesignerConstants.IN_PART_OFC);

	btFoo = new JToggleButton ("foo");
	
	btMetaproperties = new JToggleButton (DesignerConstants.METAPROPERTIESC);
	btMetaproperties.setToolTipText (DesignerConstants.Insertvaluesformetapropertiesofconcepts);

	btExhaustive = new JToggleButton (images[10]);
	btExhaustive.setToolTipText (DesignerConstants.EXHAUSTIVEC);
	btDisjoint   = new JToggleButton (images[11]);
	btDisjoint.setToolTipText (DesignerConstants.DISJOINTC);
	
	// Create a floatable tool bar
	JToolBar jtb = new JToolBar ();
	jtb.setFloatable (true);

	JToolBar jtb1 = new JToolBar ();
	jtb1.setFloatable (true);
        
	// Add a choice
	jcb = new JComboBox();
	jcb.addItem (DesignerConstants.DEFAULT_VIEWC);
	this.views = new Vector();
	if (views != null) {
	    for (int i = 0; i < views.length; i++) {
		jcb.addItem (views[i]);
		this.views.addElement (views[i]);
	    }	   
	}
	jtb.add (jcb);

	// View management
	vdc = new ViewDataController(jcb, this, ddm, designer, urlPost);
	jcb.addItemListener (vdc);
	jtb1.add (jbtNewView = new JButton (images[8]));
	jbtNewView.setToolTipText (DesignerConstants.NEW_VIEWC);
	ViewController vc = new ViewController (jcb, this, 
						vdc, urlPost,instances,designer);
	jbtNewView.addActionListener (vc);
	jtb1.add (jbtDeleteView = new JButton (images[7]));
	jbtDeleteView.setToolTipText (DesignerConstants.DELETE_VIEW);
	jbtDeleteView.addActionListener (vc);

	jtb.add (Box.createHorizontalStrut (STRUT_SIZE));
	jtb.add (btAddTerm);
	jtb.add (btAddRelation);
	jtb.add (Box.createHorizontalStrut (STRUT_SIZE));
	jtb.add (btRemove);
	jtb.add (btRemoveCompletely);
	jtb.add (Box.createHorizontalStrut (STRUT_SIZE));
	jtb.add (btPartOf);
	jtb.add (btInPartOf);
	if (includeSubclass)
	    jtb.add (btSubclass);
	jtb.add (Box.createHorizontalStrut (STRUT_SIZE));
	jtb.add (btExhaustive);
	jtb.add (btDisjoint);
	btAddTerm.addItemListener (this);
	btAddRelation.addItemListener (this);
	btRemove.addItemListener (this);
	btRemoveCompletely.addItemListener (this);
	btSubclass.addItemListener (this);
	btExhaustive.addActionListener (this);
	btDisjoint.addActionListener (this);
	btMetaproperties.addItemListener (this);
	bg = new ButtonGroup();
	bg.add (btSubclass);
	bg.add (btAddTerm);
	bg.add (btAddRelation);
	bg.add (btRemove);
	bg.add (btRemoveCompletely);
	bg.add (btPartOf);
	bg.add (btInPartOf);
	bg.add (btExhaustive);
	bg.add (btDisjoint);
	bg.add (btFoo);
	bg.add (btMetaproperties);
	
	JButton jbt = new JButton (images[3]);
	jbt.setToolTipText (DesignerConstants.COMMITC);
	jbt.addActionListener (vc);
	jtb1.add (jbt);
	jbtCommit = jbt;
	jbt = new JButton (images[4]);
	jbt.setToolTipText (DesignerConstants.COMMIT_ALLC);
	jbt.addActionListener (vc);
	jtb1.add (jbt);
	jbtCommitAll = jbt;
	jbtCommitAll.addActionListener (vc);
        jtb1.add (btMetaproperties);
	
	
	Container cont = getContentPane();
	cont.add (new JScrollPane (designer), BorderLayout.CENTER);
	if (er != null)
	    designer.setElementRenderer (er);
	
	designer.addMouseListener (new MouseAdapter() {
	    public void mousePressed (MouseEvent me) {
		DesignerFrame.this.mouseClicked (me);
	    }
	});
	JPanel pb = new JPanel();
	pb.setLayout (new BorderLayout());
	pb.add (jtb1, BorderLayout.NORTH);
	pb.add (jtb, BorderLayout.CENTER);
	pb.add (new ViewRelationTypeToolBar (designer), BorderLayout.SOUTH);
	
	cont.add (pb, BorderLayout.NORTH);
        cont.add (new ViewRelationTypeToolBarOntoClean (designer), BorderLayout.SOUTH);
   }

    public boolean getEvaluationOntoClean()
    {
      return designer.getEvaluation();
    }
    public void setEditable (boolean b)
    {
	bEditable = b;
	designer.setEditable (b);
    }

    /**
     * Sets a new design model.
     */
    public void setDesignModel (DesignModel ddm)
    {
	this.ddm = (DefaultDesignModel) ddm;
	designer.setDesignModel (ddm);
    }

    private transient boolean f;
    public void itemStateChanged (ItemEvent ie)
    {	
	/*if (f) return;
	f = true;
	btAddTerm.setSelected (false);
	btAddRelation.setSelected (false);
	btRemove.setSelected (false);
	btSubclass.setSelected (false);
	
	if (ie.getStateChange() == ie.SELECTED) {
	    ((JToggleButton) ie.getSource()).setSelected (true);
	    f = false;
	    }*/
    }

    public void actionPerformed (ActionEvent ae1)
    {
	// Deselect other buttons
	deselectActive ();

	bRelation = false;
	firstElement = null;

	// First part
	DesignModel ddmS = ddm.getSupervisor();
	if (ddmS != null) {
	    Group[] groups = ddmS.getGroups();
	    if (groups != null && groups.length > 0) {
		firstElement = (Group) 
		    JOptionPane.showInputDialog (this,DesignerConstants.Selectagrouptobetheoriginoftherelation ,
						 DesignerConstants.ADD_RELATIONC ,
						 JOptionPane.INFORMATION_MESSAGE, null, groups,
						 groups[0]);
		if (firstElement != null) {
		    bRelation = true;
		    ((JToggleButton) ae1.getSource()).setSelected (true);
		}
	    }
	}
    }

    private void deselectActive ()
    {
	btFoo.setSelected (true);
    }
    
    public void addView(String name)
    {
    	this.views.add(name);
    }
    
    public void removeView(String name)
    {
    	this.views.remove(name);
    }
    	
    	
    public void mouseClicked (MouseEvent me)
    {
	if (!bEditable) return;
	
	if (btMetaproperties.isSelected()) {
	    //bg.getSelection().setSelected (false)
	    Element el = designer.getCurrentElement();
	    String mp = ddm.getMetaproperties(el.getName());  	
            String values = askMetaproperties (DesignerConstants.Modifythevaluesofthemetapropertiesof,el.getName(),mp);
            if (values == null)
            { 
              return; 	             
            }
            if(!isCorrect(values))
            {
              JOptionPane.showMessageDialog (this, 
					      DesignerConstants.Incorrectvaluesofthemetaproperties,
					       DesignerConstants.Error,
					       JOptionPane.ERROR_MESSAGE);					       
	      return;
            }             
            deselectActive ();
            try {
		ddm.addMetaproperties (el.getName()+"*"+values);
		if( !views.isEmpty() && (views.size() > 1))
		{
		 String[] views_aux = new String[views.size()];
		 views.copyInto(views_aux);
		 for(int t=0;t<views_aux.length;t++)
		 {
		  if(!views_aux[t].equals(DesignerConstants.DEFAULT_VIEW))
		  { 				       
                  DefaultDesignModel ddm_aux = (DefaultDesignModel) vdc.getModel ((String) views_aux[t]);					       
                  if(ddm_aux != null)
                   ddm_aux.addMetaproperties (el.getName()+"*"+values);
                  } 
                 }
                }   
	    } catch (Exception e) {
		JOptionPane.showMessageDialog (this, 
					        DesignerConstants.Errorinsertingmetaproperties ,
					       DesignerConstants.Error ,
					       JOptionPane.ERROR_MESSAGE);
		return;
	    }	       
	}
	if (btAddTerm.isSelected()) {
	    //bg.getSelection().setSelected (false);
	    String[] name = askTermName (DesignerConstants.Nameforthenewterm);
	    String values = name[1];
	    if (name[0] == null) 
	      return;	  
	    deselectActive ();
	    // Add a new term
	    try {
		ddm.addElement (new Element (name[0], me.getX(), me.getY()));
	        if(! isCorrect(values))
                {                    
                      JOptionPane.showMessageDialog (this, 
					       DesignerConstants.Incorrectvaluesofthemetaproperties,
					       DesignerConstants.Error ,
					       JOptionPane.ERROR_MESSAGE);					       
	              return;
                 }  
                 ddm.addMetaproperties (name[0]+"*"+values);
                 if( !views.isEmpty() && (views.size() > 1))
		 {
                   String[] views_aux = new String[views.size()];
		   views.copyInto(views_aux);
		   for(int t=0;t<views_aux.length;t++)
		   {
		      if(! views_aux[t].equals(DesignerConstants.DEFAULT_VIEW))
		      {				       
                        DefaultDesignModel ddm_aux = (DefaultDesignModel) vdc.getModel ((String) views_aux[t]);					       
                        if(ddm_aux != null)
                         ddm_aux.addMetaproperties (name[0]+"*"+values);
                      } 
                   }  				   	
                 }   						   	
	    } catch (Exception e) {
		JOptionPane.showMessageDialog (this, 
					       e.getMessage()+    DesignerConstants.Errorinsertingmetaproperties ,
					       DesignerConstants.Error ,
					       JOptionPane.ERROR_MESSAGE);
		return;
	    }
	}
	else if (btAddRelation.isSelected () || btSubclass.isSelected() || btPartOf.isSelected() ||
		 btInPartOf.isSelected()) {
	    // Add a new relation
	    if (bRelation) {
		// Second part
		Element el = designer.getCurrentElement ();
		if (el != null) {
		    String relName;
		    if (btSubclass.isSelected())
			relName = Relation.SUBCLASS_OF;
		    else if (btPartOf.isSelected())
			relName = Relation.PART_OF;
		    else if (btInPartOf.isSelected())
			relName = Relation.IN_PART_OF;
		    else {
			Object[] ao = askRelationName (DesignerConstants.Namefortherelation);
			if (ao == null) {
			    bRelation = false;
			    firstElement = null;

			    deselectActive ();
			    return;
			}
			relName = (String) ao[0];
			cardinality = ((Integer) ao[1]).intValue();
		    }
		    
		    try {
			ddm.addRelation (new Relation (relName, firstElement.getName(), el.getName(), cardinality));
		    } catch (Exception e) {
			JOptionPane.showMessageDialog (this, 
						       e.getMessage(),
						       DesignerConstants.Error,
						       JOptionPane.ERROR_MESSAGE);
			return;
		    } finally {
			deselectActive ();
			bRelation = false;
			firstElement = null;
		    }
		}		
	    }
	    else {
		// First part
		firstElement = designer.getCurrentElement ();
		if (firstElement != null)
		    bRelation = true;
	    }
	}
	else if (btExhaustive.isSelected() || btDisjoint.isSelected()) {
	    // Exhaustive or disjoint
	    // Add a new relation
	    if (bRelation) {
		// Second part
		Element el = designer.getCurrentElement ();
		if (el != null) {
		    String relName;
		    if (btExhaustive.isSelected())
			relName = Relation.EXHAUSTIVE;
		    else if (btDisjoint.isSelected())
			relName = Relation.DISJOINT;
		    else {
			deselectActive ();
			
			bRelation = false;
			firstElement = null;

			throw new RuntimeException (DesignerConstants.Unknowntypeofrelation);
		    }		    
		    try {
			ddm.addRelation (new Relation (relName, firstElement.getName(), el.getName()));
		    } catch (Exception e) {
			JOptionPane.showMessageDialog (this, 
						       e.getMessage(),
						       DesignerConstants.Error,
						       JOptionPane.ERROR_MESSAGE);
			return;
		    } finally {
			bRelation = false;
			firstElement = null;
			
			deselectActive ();
		    }
		}		
	    }
	}		
	else if (btRemove.isSelected() || btRemoveCompletely.isSelected()) {
	    // Remove an element or relation
	    Element el = designer.getCurrentElement();
	    if (el != null) {
		if (btRemoveCompletely.isSelected()) {
		    vdc.getSupervisor().removeElement (el.getName());
		    vdc.getSupervisor().removeMetaproperties(el.getName());
		    //Object selectedView = jcb.getSelectedItem ();
		    //DefaultDesignModel dde = (DefaultDesignModel) vdc.getModel (selectedView.toString());
		    //dde.removeMetaproperties(el.getName());
		    if( !views.isEmpty())
		    {
		      String[] views_aux = new String[views.size()];
		      views.copyInto(views_aux);
		      for(int t=0;t<views_aux.length;t++)
		      {
		        if(!views_aux[t].equals(DesignerConstants.DEFAULT_VIEW))
		        {  				       
                         DefaultDesignModel ddm_aux = (DefaultDesignModel) vdc.getModel ((String) views_aux[t]);					       
                         if(ddm_aux != null)
                         ddm_aux.removeMetaproperties (el.getName());
                        } 
                      }
                    }   
		}
		else
		{
		    ddm.removeElement (el.getName());
                }
		deselectActive ();
	    }
	    else {
		Relation rel = designer.getCurrentRelation();
		if (rel != null) {
		    if (btRemoveCompletely.isSelected())
			vdc.getSupervisor().removeRelation (rel);
		    else
			ddm.removeRelation (rel/*new Relation (rel.getName(), rel.getOrigin(), rel.getDestination())*/);

		    deselectActive ();
		}
	    }
	}
    }

    private String askName (String message)
    {
	return JOptionPane.showInputDialog(message); 
    }	
    

    private String askMetaproperties (String message,String name,String mp)
    {
	String values = new AddMetapropertiesDialog (this,message,name,mp).getValues();

	return values;
    }	
    
    private String[] askTermName (String message)
    {
	AddTermDialog atd = new AddTermDialog (this, vdc.getTerms(),ddm);
        String name = atd.getTermName();
        String metaproperties = atd.getMetapropertiesAux();
	ComboBoxModel cbm = jcb.getModel();
	for (int i = 0; i < cbm.getSize(); i++) {
	    if (cbm.getElementAt (i).equals (name)) {
		JOptionPane.showMessageDialog (this, 
					        DesignerConstants.Aviewwithname + name + DesignerConstants.alreadyexists ,
					       DesignerConstants.Error ,
					       JOptionPane.ERROR_MESSAGE);
		return null;
	    }
	}
	return new String [] {name,metaproperties};
    }
    
    private boolean isNew(String name)
    {
       String [] terms = vdc.getTerms();
       for(int indice=0;indice < terms.length;indice++)
       {
         if(name.equals(terms[indice]))
          return false;
       }	
       
       return true;
    }	

    private Object[] askRelationName (String message)
    {
	AddTermDialog atd = new AddTermDialog (this, vdc.getRelations(), true,ddm);
	String name = atd.getTermName();
	int card = atd.getCardinality();
	ComboBoxModel cbm = jcb.getModel();
	for (int i = 0; i < cbm.getSize(); i++) {
	    if (cbm.getElementAt (i).equals (name)) {
		JOptionPane.showMessageDialog (this, 
					        DesignerConstants.Aviewwithname + name + DesignerConstants.alreadyexists ,
					       DesignerConstants.Error ,
					       JOptionPane.ERROR_MESSAGE);
		return null;
	    }
	}
	if (name == null || card < -10)
	    return null;
	return new Object[] {name, new Integer (card)};
    }	
    
    private boolean isCorrect(String metaproperties) 
    {
        int l = metaproperties.length();
        int i;
        int anti_rigid = 0,dependent=0,carries_identity=0,supplies_identity=0,anti_unity=0;
        int s=0,c=0; 
        
        if(l % 2 != 0)
          return false;
        
        for(i=0;i<l-1;i=i+2)
        {
          String aux = metaproperties.substring(i,i+2);
          if(aux.equals(DesignerConstants.ANTI_RIGID) || 
                                     aux.equals(DesignerConstants.IS_RIGID) ||
                                     aux.equals(DesignerConstants.IS_NOT_RIGID))
              anti_rigid++;
          else if(aux.equals(DesignerConstants.IS_DEPENDENT) || aux.equals(DesignerConstants.IS_NOT_DEPENDENT))
              dependent++;  
          else if(aux.equals(DesignerConstants.CARRIES_IDENTITY_CRITERION) || 
                                     aux.equals(DesignerConstants.NO_CARRIES_IDENTITY_CRITERION))
          {                           
              carries_identity++;        
              if(aux.equals(DesignerConstants.NO_CARRIES_IDENTITY_CRITERION))
                c=1;
          }    
          else if(aux.equals(DesignerConstants.SUPPLIES_IDENTITY_CRITERION) || 
                                     aux.equals(DesignerConstants.NO_SUPPLIES_IDENTITY_CRITERION))
          {                           
              supplies_identity++;
              if(aux.equals(DesignerConstants.SUPPLIES_IDENTITY_CRITERION))
                s=1;
          }
          else if(aux.equals(DesignerConstants.ANTI_UNITY) || 
                                     aux.equals(DesignerConstants.CARRIES_UNITY) ||
                                     aux.equals(DesignerConstants.NO_CARRIES_UNITY))
              anti_unity++;                
          else
              return false;    
        }       
        if(s==1 && c==1)
         return false;
        if(anti_rigid > 1)
         return false;
        else if(dependent > 1)
         return false;
        else if(carries_identity > 1)
         return false;
        else if(supplies_identity > 1)
         return false;
        else if(anti_unity > 1)
         return false;          
        else
         return true;
     }

}






