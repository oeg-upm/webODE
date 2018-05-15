package es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.gui;

import es.upm.fi.dia.ontology.minerva.server.management.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

/**
 * This class provides a panel for managing groups.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class GroupPanel extends MinervaUI implements ActionListener, ItemListener
{
    private transient AuthenticationServiceManagerIntf manager;
    private transient MinervaGroup[] amg = null;
    private transient MinervaUser[] amu  = null;
    private transient JFrame jf;
    private transient UserPanel userPanel;
    private transient ServicePanel servicePanel;
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
		    MinervaUser mu = (MinervaUser) ao[i];
		    try {
			manager.addUserToGroup (amg[jcbGroups.getSelectedIndex()].getName(), 
						mu.getLogin());
		    } catch (Exception e) {
			JOptionPane.showMessageDialog (jf, 
						       "Error updating group: " + e.getMessage() + ".",
						       "Error",
						       JOptionPane.ERROR_MESSAGE);
		    }
		}
	    }
	    else {
		Object[] ao = jlGroupUsers.getSelectedValues();
		if (ao == null) return;
		
		for (int i = 0; i < ao.length; i++) {
		    MinervaUser mu = (MinervaUser) ao[i];
		    
		    try {
			manager.removeUserFromGroup (amg[jcbGroups.getSelectedIndex()].getName(), 
						     mu.getLogin());
		    } catch (Exception e) {
			JOptionPane.showMessageDialog (jf, 
						       "Error updating group: " + e.getMessage() + ".",
						       "Error",
						       JOptionPane.ERROR_MESSAGE);
		    }
		}
	    }
	    // sync
	    itemStateChanged (null);
	}
    }
    
    public GroupPanel (UserPanel userPanel, ServicePanel servicePanel)
    {
	this.userPanel = userPanel;
	this.servicePanel = servicePanel;
    }
    
    public void setManager (MinervaManager minervaManager)
    {
	this.manager = (AuthenticationServiceManagerIntf) minervaManager;
	
	removeAll();
	
	jf = getParentFrame();
	
	setLayout (new BorderLayout (5, 5));

	try {
	    amg = manager.getGroups();
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   "Error retrieving groups: " + e.getMessage() + ".",
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
	    
	//jcbGroups = new JComboBox ();
	jcbGroups = new Choice();
	for (int i = 0; i < amg.length; i++) {
	    jcbGroups.addItem (amg[i] + "");
	}
	jcbGroups.addItemListener (this);
	JPanel jp1 = new JPanel();
	jp1.setLayout (new BoxLayout (jp1, BoxLayout.X_AXIS));
	jp1.add (new JLabel (" Group "));
	jp1.add (jcbGroups);
	add (jp1, BorderLayout.NORTH);

	try {
	    amu = manager.getUsers();
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   "Error retrieving users: " + e.getMessage() + ".",
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
	
	JPanel top = new JPanel();
	top.setLayout (new BoxLayout (top, BoxLayout.X_AXIS));
	top.setBorder (BorderFactory.createTitledBorder ("User Management"));

	JPanel jp = new JPanel();
	jp.setLayout (new BorderLayout());
	jp.add (new JLabel ("Total Users"), BorderLayout.NORTH);
	jp.add (new JScrollPane (jlUsers = new JList (amu)), BorderLayout.CENTER);
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

	try {
	    jp = new JPanel();
	    jp.setLayout (new BorderLayout());
	    jp.add (new JLabel ("Group Users"), BorderLayout.NORTH);
	    jp.add (new JScrollPane (jlGroupUsers = new JList (manager.getGroup(amg[0].getName()).getUsers())), 
		    BorderLayout.CENTER);
	    top.add (jp);
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   "Error retrieving users: " + e.getMessage() + ".",
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}

	add (top, BorderLayout.CENTER);

	jp = new JPanel ();
	JButton jbt;
	jp.setLayout (new BoxLayout (jp, BoxLayout.Y_AXIS));
	jp.add (jbt = new JButton ("New"));
	jbt.setMnemonic ('N');
	jbt.addActionListener (this);
	jp.add (jbt = new JButton ("Delete"));
	jbt.setMnemonic ('D');
	jbt.addActionListener (this);
	jp.add (jbt = new JButton ("Update"));
	jbt.setMnemonic ('U');
	jbt.addActionListener (this);
	add (jp, BorderLayout.EAST);

	// Sync
	itemStateChanged (null);
    }

    public void actionPerformed (ActionEvent ae)
    {
	String str = ae.getActionCommand();
	if (str.equals ("New")) {
	    try {
		NewGroupDialog ngd = new NewGroupDialog (jf);
		ngd.setVisible (true);
		//System.out.println ("Create group.: " + ngd.getName());
		if (ngd.getName() != null) 
		    manager.createGroup (ngd.getName(), ngd.getDescription());
	    } catch (Exception e) {
		JOptionPane.showMessageDialog (jf, 
					       "Error creating group: " + e.getMessage() + ".",
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
	    }		   
	}
	else if (str.equals ("Update")) {
	    int index = jcbGroups.getSelectedIndex();
	    try {
		NewGroupDialog ngd = new NewGroupDialog (jf, amg[index]);
		ngd.setVisible (true);
		
		manager.updateGroup (ngd.getName(), ngd.getDescription());
	    } catch (Exception e) {
		JOptionPane.showMessageDialog (jf, 
					       "Error updating group: " + e.getMessage() + ".",
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
	    }
	}
	else if (str.equals ("Delete")) {
	    int index = jcbGroups.getSelectedIndex();
	    try {
		manager.deleteGroup (amg[index].getName());
	    } catch (Exception e) {
		JOptionPane.showMessageDialog (jf, 
					       "Error creating group: " + e.getMessage() + ".",
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
	    }		    
	}

	// Synchronize
	updateChoice();
	itemStateChanged (null);
    }

    public void itemStateChanged (ItemEvent ie)
    {
	try {
	    MinervaGroup mg = manager.getGroup (amg[jcbGroups.getSelectedIndex()].getName());
	    
	    Vector v = new Vector();
	    arrayToVector (manager.getUsers(), v);

	    MinervaUser[] users = mg.getUsers();
	    jlGroupUsers.setListData (users);

	    for (int i = 0; i < users.length; i++)
		v.removeElement (users[i]);
	    users = new MinervaUser[v.size()];
	    v.copyInto (users);

	    jlUsers.setListData (users);
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   "Error retrieving user and groups: " + e.getMessage() + ".",
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
    }	

    private void updateChoice ()
    {
	try {
	    amg = manager.getGroups();
	    jcbGroups.removeAll();
	    for (int i = 0; i < amg.length; i++) {
		jcbGroups.addItem (amg[i] + "");
	    }
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   "Error updating group information: " + e.getMessage() + ".",
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
	userPanel.updateChoice(amg);
	servicePanel.updateChoice(amg);
    }

    private void arrayToVector (Object[] ao, Vector v)
    {
	for (int i = 0; i < ao.length; i++)
	    v.addElement (ao[i]);
    }
}

