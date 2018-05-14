package es.upm.fi.dia.ontology.webode.ui.designer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.io.*;

import es.upm.fi.dia.ontology.webode.Inference.*;
import es.upm.fi.dia.ontology.webode.OntoClean.*;
import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

/**
 * Class responsible for controlling actions related
 * to view management.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.3
 */
public class ViewController implements ActionListener
{
    private JComboBox jcbViews;
    private DesignerFrame jfr;
    private ViewDataController vdc;
    private URL urlPost;
    private String[] instances;
    private Designer designer;
 
    
    public ViewController (JComboBox views, DesignerFrame jfr, ViewDataController vdc, URL urlPost,String[] instances,Designer designer)
    {
	this.jcbViews = views;
	this.jfr      = jfr;
	this.vdc      = vdc;
	this.urlPost  = urlPost;
	this.instances = instances;
	this.designer = designer;
	
    }

    public void actionPerformed (ActionEvent ae)
    {
    	int all=0;
    	int eval = 0;
	String str = ae.getActionCommand ();
	if (ae.getSource() == jfr.jbtNewView) {
	    String viewName = askName (DesignerConstants.Nameforthenewview);
	    
	    if (viewName != null) {
		// Check if duplicate
		for (int i = 0, l = jcbViews.getItemCount (); i < l; i++) {
		    if (viewName.equals (jcbViews.getItemAt (i))) {
			JOptionPane.showMessageDialog (jfr, 
						       DesignerConstants.Aview + viewName + DesignerConstants.Exists,
						       DesignerConstants.Error,
						       JOptionPane.ERROR_MESSAGE);
			return;
		    }
		}

		// Commit to server
		new Communicator (jfr, urlPost, Command.ADD_VIEW, viewName, null, null).start();
		
		// Add to list
		jcbViews.addItem (viewName);
		
		
		// Change combo
		// jcbViews.setSelectedItem (viewName);
		
		// Add model to view data controller 
		DefaultDesignModel m = new DefaultDesignModel();
		Object[] views = vdc.getViews();
		if (views != null && (views.length >0)) 
		{	
	           DefaultDesignModel ddm_aux = (DefaultDesignModel) vdc.getModel ((String) views[0]);
		   String [] metaproperties = ddm_aux.getMetaproperties();
		   for (int h = 0; h < metaproperties.length; h++) 
		   {
		     try {
		        m.addMetaproperties (metaproperties[h]);
		     } catch (Exception e) {
		        System.err.println (DesignerConstants.UnexpectedError  +
					e);
		        e.printStackTrace();
		     }   
		     
		   }
		}
		else
		{
		  if(instances != null)
		  {
		    for (int g = 0; g < instances.length; g++) 
		    {
		      try {
		         m.addMetaproperties (instances[g]);
		      } catch (Exception e) {
		        System.err.println (DesignerConstants.UnexpectedError +
					e);
		        e.printStackTrace();
		     }   
		     
		    }
		  }
		}
		vdc.addModel (viewName, m); // <<<<<<---------------------------
		jcbViews.setSelectedItem (viewName);
		jfr.addView(viewName);
	    }
	}
	else if (ae.getSource() == jfr.jbtDeleteView) {
	    // Get currently selected view
	    Object selectedView = jcbViews.getSelectedItem ();
	    if (selectedView.equals (DesignerConstants.DEFAULT_VIEW)) {
		JOptionPane.showMessageDialog (jfr, 
					       DesignerConstants.Defaultview,
					       DesignerConstants.Error,
					       JOptionPane.ERROR_MESSAGE);
	    }
	    else {
		// Ask for confirmation
		if (JOptionPane.showConfirmDialog (jfr, 
						   DesignerConstants.Sure + selectedView + "'?",
						   DesignerConstants.Confirmation,
						   JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
		    // Delete view 
		    vdc.removeModel (selectedView.toString());
		    
		    // Commit to server
		    new Communicator (jfr, urlPost, Command.DELETE_VIEW, 
				      selectedView.toString(), null, null).start();

		    // Remove from list
		    jfr.removeView(selectedView.toString());
		    jcbViews.removeItem (selectedView);
		}
	    }
	}
	else if (ae.getSource() == jfr.jbtCommit) {
	    // Commit data to server
	    all = 0;
	    if (!jcbViews.getSelectedItem().toString().equals (DesignerConstants.DEFAULT_VIEW))
		sendData ((DefaultDesignModel) vdc.getModel (jcbViews.getSelectedItem().toString()),
			  jcbViews.getSelectedItem().toString(),all,1);
	}
	else if (ae.getSource() == jfr.jbtCommitAll) {
	    // Get all views
	    Object[] views = vdc.getViews();
	    all = 1;
	    if (views != null) 
	    {
	    	for (int k = 0; k < views.length;k++) 
		{
		    // Commit data to server
		    if(k == views.length-1)
		     eval =1;
		    /*if (!views[k].equals (DesignerFrame.DEFAULT_VIEW))
		    {
	             sendData ((DefaultDesignModel) vdc.getModel ((String) views[k]),
		  	  (String) views[k],all,eval);
		    }*/	  
		}		  
	    }
	}
    }
    
    
    
    private void sendData (DefaultDesignModel ddm, String view,int all_views,int eval)
    {
	Vector vNew = new Vector (200);
	Vector vDirty = new Vector (200);
	int mp=0;
	String[] evaluation = new String[1];
	String[] only_evaluation = new String[1];
	//System.out.println ("SEND DATA");
	// Get new terms --> Classify them according to if they are
	// new or they are dirty.
	evaluation[0]="0";
	only_evaluation[0]="0";
	if (jfr.getEvaluationOntoClean() && eval == 1) 
         evaluation[0] = "1";  
	                
	String [] metaproperties = ddm.getMetaproperties();
	mp = ddm.getMetapropertiesModified();
	Element[] ae = ddm.getElements();
	for (int i = 0; ae != null && i < ae.length; i++) {
	    //System.out.println ("XX: " + ae[i] + "-" + ae[i].isNew() + "-" + ae[i].isDirty());
	    if (!(ae[i] instanceof Group)) {
		if (ae[i].isNew()) {
		    vNew.addElement (ae[i]);
		    ae[i].setNew (false);
		}
		else if (ae[i].isDirty()) {
		    //System.out.println ("THIS: " + ae[i]);
		    vDirty.addElement (ae[i]);
		}
		ae[i].setDirty (false);
	    }
	}
	Relation[] ar = ddm.getRelations();
	for (int i = 0; ar != null && i < ar.length; i++) {
	    if (ar[i].isNew()) {
		vNew.addElement (ar[i]);
		ar[i].setNew (false);
	    }
	    else if (ar[i].isDirty())
		vDirty.addElement (ar[i]);
	    ar[i].setDirty (false);
	}

	Vector vRemoved = vdc.getRemovedElements (ddm);
	Vector vRemovedCompletely = vdc.getCompletelyRemovedElements ();
	// Groups are taken for the supervisor...
	Group[] ag = vdc.getSupervisor().getGroups();
	Vector vGroupsDirty = new Vector();
	Vector vGroupsNew = new Vector();
	if (ag != null) {
	    for (int k = 0; k < ag.length; k++) {
		Group g = ag [k];
		if (g.isNew()) {
		    vGroupsNew.addElement (g);
		    g.setNew (false);
		}
		else if (g.isDirty()) {
		    vGroupsDirty.addElement (g);
		    g.setDirty (false);
		}
	    }
	}

	// Has anything changed?
	if ((vRemoved == null || vRemoved.isEmpty()) && vNew.isEmpty() && vDirty.isEmpty() &&
	    (vRemovedCompletely == null || vRemovedCompletely.isEmpty()) &&
	    vGroupsDirty.isEmpty() && vGroupsNew.isEmpty() && mp == 0 && jfr.getEvaluationOntoClean())
	    // Send nothing
	    only_evaluation[0]="1";

        if ((vRemoved == null || vRemoved.isEmpty()) && vNew.isEmpty() && vDirty.isEmpty() &&
	    (vRemovedCompletely == null || vRemovedCompletely.isEmpty()) &&
	    vGroupsDirty.isEmpty() && vGroupsNew.isEmpty() && mp == 0 && !jfr.getEvaluationOntoClean())
	    // Send nothing
	    return;
	// this method is invoked when commit is activated...
	//new ComHelper (urlPost, vRemoved, vNew, vDirty, df).start();	
	new Communicator (jfr, urlPost, Command.COMMIT, view, 
			  new Object[] { vRemoved, vNew, vDirty, 
					 vGroupsNew, vGroupsDirty, vRemovedCompletely,
					 metaproperties,evaluation,only_evaluation},new CommandRunnable () {
		    public void run() {
			Object[] ao = (Object[]) cd.data;
			int [] p = (int [])ao[0];
			if(p[0] == 1)
			{
			  String[] errors = (String[]) ao[1];
			  String[] concs = (String[]) ao[2];
			  if(p[1] == 0)
			  {
			     JOptionPane.showMessageDialog (jfr, 
						        DesignerConstants.NoerrorOntoClean,
						       DesignerConstants.NoerrorOntoClean,
						       JOptionPane.ERROR_MESSAGE);
			  }			       
		 	  else			       
		 	  { 
		 	    StringWriter doc = new StringWriter();	
		 	    for(int u=0;u<errors.length;u++)
		 	    {
		 	       doc.write(errors[u]);
		 	    }
		 	    int num_errors=p[1];	
	                    JOptionPane.showMessageDialog (jfr,
						       doc.getBuffer(),
						       num_errors + DesignerConstants.errorOntoClean ,
						       JOptionPane.ERROR_MESSAGE);			       			       
	                   }
	                   String [] concs_aux = new String[concs.length];
	                   for(int cu=0;cu<concs_aux.length;cu++)
		 	   {
		 	      concs_aux[cu] = concs[cu].replace('_',' ').substring(0,concs[cu].length()-1);
		 	   }
	                   designer.putConceptsErrors(concs_aux);	                 
	                   designer.drawErrorsOntoClean (); 					       
                         } 						       
		    }}).start();
    }
      
    private String askName (String message)
    {
	return JOptionPane.showInputDialog(message); 
    }	
}















