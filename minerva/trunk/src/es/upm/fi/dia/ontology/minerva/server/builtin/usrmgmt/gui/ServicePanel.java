package es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.gui;

import es.upm.fi.dia.ontology.minerva.server.management.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

/**
 * This class provides a panel for managing services.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class ServicePanel extends MinervaUI implements/* ActionListener, */ItemListener
{
    private transient AuthenticationServiceManagerIntf manager;
    private transient MinervaServiceAccess[] asa = null;
    private transient JFrame jf;
    private transient UserPanel userPanel;
    private transient Choice jcbGroups;
    private JList jlUsers, jlGroupUsers;

    private class AddRemoveControl implements ActionListener
    {
	public void actionPerformed (ActionEvent ae)
	{
	    String str = ae.getActionCommand();
	    
	    if (str.equals (">")) {
		Object[] ao = jlUsers.getSelectedValues();
		if (ao == null) return;
		
		for (int i = 0; i < ao.length; i++) {
		    MinervaGroup mg = (MinervaGroup) ao[i];
		    try {
			manager.grantAccessToGroup (asa[jcbGroups.getSelectedIndex()].getName(), 
						    mg.getName());
		    } catch (Exception e) {
			JOptionPane.showMessageDialog (jf, 
						       "Error updating access: " + e.getMessage() + ".",
						       "Error",
						       JOptionPane.ERROR_MESSAGE);
		    }
		}
	    }
	    else {
		Object[] ao = jlGroupUsers.getSelectedValues();
		if (ao == null) return;
		
		for (int i = 0; i < ao.length; i++) {
		    MinervaGroup mg = (MinervaGroup) ao[i];
		    
		    try {
			manager.denyAccessToGroup (asa[jcbGroups.getSelectedIndex()].getName(), 
						   mg.getName());
		    } catch (Exception e) {
			JOptionPane.showMessageDialog (jf, 
						       "Error updating access: " + e.getMessage() + ".",
						       "Error",
						       JOptionPane.ERROR_MESSAGE);
		    }
		}
	    }
	    // sync
	    itemStateChanged (null);
	}
    }

    
    public void setManager (MinervaManager minervaManager)
    {
	this.manager = (AuthenticationServiceManagerIntf) minervaManager;
	
	removeAll();
	
	jf = getParentFrame();
	
	setLayout (new BorderLayout (5, 5));

	try {
	    asa = manager.getServices();
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   "Error retrieving groups: " + e.getMessage() + ".",
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
	    
	//jcbGroups = new JComboBox ();
	jcbGroups = new Choice();
	for (int i = 0; i < asa.length; i++) {
	    jcbGroups.addItem (asa[i] + "");
	}
	jcbGroups.addItemListener (this);
	JPanel jp1 = new JPanel();
	jp1.setLayout (new BoxLayout (jp1, BoxLayout.X_AXIS));
	jp1.add (new JLabel (" Service "));
	jp1.add (jcbGroups);
	add (jp1, BorderLayout.NORTH);

	JPanel top = new JPanel();
	top.setLayout (new BoxLayout (top, BoxLayout.X_AXIS));
	top.setBorder (BorderFactory.createTitledBorder ("Service Access Management"));
	
	JPanel jp = new JPanel();
	jp.setLayout (new BorderLayout());
	jp.add (new JLabel ("Total Groups"), BorderLayout.NORTH);
	jp.add (new JScrollPane (jlUsers = new JList ()), BorderLayout.CENTER);
	top.add (jp);
	
	jp = new JPanel();
	jp.setLayout (new BorderLayout());
	jp1 = new JPanel();
	jp1.setLayout (new BoxLayout (jp1, BoxLayout.Y_AXIS));
	JButton bt;
	AddRemoveControl arc = new AddRemoveControl();
	jp1.add (bt = new JButton (">"));
	bt.addActionListener (arc);
	jp1.add (bt = new JButton ("<"));
	bt.addActionListener (arc);
	jp.add (jp1, BorderLayout.CENTER);
	jp.add (new JPanel(), BorderLayout.NORTH);
	jp.add (new JPanel(), BorderLayout.SOUTH);
	top.add (jp);
	
	jp = new JPanel();
	jp.setLayout (new BorderLayout());
	jp.add (new JLabel ("Allowed Groups"), BorderLayout.NORTH);
	jp.add (new JScrollPane (jlGroupUsers = new JList ()), 
		BorderLayout.CENTER);
	top.add (jp);
	add (top, BorderLayout.CENTER);
	
	// Sync
	itemStateChanged (null);
    }

    public void updateChoice (MinervaGroup[] amg)
    {
	itemStateChanged (null);
    }	

    public void itemStateChanged (ItemEvent ie)
    {
	try {
	    MinervaServiceAccess asa = manager.getService (this.asa[jcbGroups.getSelectedIndex()].getName());
	    
	    Vector v = new Vector();
	    arrayToVector (manager.getGroups(), v);
	    
	    MinervaGroup[] groups = asa.getGroups();
	    
	    //System.out.println ("GroupS:");
	    //for (int i = 0; i < groups.length; i++)
	    //System.out.println ("GROUP" + i + ":" + groups[i]);

	    jlGroupUsers.setListData (groups);
	    
	    for (int i = 0; i < groups.length; i++)
		v.removeElement (groups[i]);
	    groups = new MinervaGroup[v.size()];
	    v.copyInto (groups);
	    
	    jlUsers.setListData (groups);
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   "Error retrieving access information: " + e.getMessage() + ".",
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
    }	
    
    private void arrayToVector (Object[] ao, Vector v)
    {
	for (int i = 0; i < ao.length; i++)
	    v.addElement (ao[i]);
    }
}

