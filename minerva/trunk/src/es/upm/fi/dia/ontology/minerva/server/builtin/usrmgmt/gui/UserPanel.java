package es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.gui;

import es.upm.fi.dia.ontology.minerva.server.management.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class provides a panel for managing users.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.2
 */
public class UserPanel extends MinervaUI implements ActionListener, ItemListener
{
    private static final String[] LABELS = {
	"New", "Delete", "Modify" };

    //private transient JComboBox jcbGroups;
    private transient Choice jcbGroups;
    private transient JList jlUsers;

    private transient MinervaGroup[] amg = null;
    private transient MinervaUser[] amu  = null;
    private transient AuthenticationServiceManagerIntf manager;

    private JFrame jf;

    public void setManager(MinervaManager minervaManager)
    {
	this.manager = (AuthenticationServiceManagerIntf) minervaManager;

	removeAll();

	jf = getParentFrame ();

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
	jcbGroups.addItem ("<<All groups>>");
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

	jlUsers = new JList (amu);
	add (new JScrollPane (jlUsers), BorderLayout.CENTER);

	JPanel jp = new JPanel();
	jp.setLayout (new BoxLayout (jp, BoxLayout.Y_AXIS));
	for (int i = 0; i < LABELS.length; i++) {
	    JButton jbt = new JButton (LABELS[i]);
	    jbt.setMnemonic (LABELS[i].charAt (0));
	    jbt.addActionListener (this);
	    jp.add (jbt);
	}

	add (jp, BorderLayout.EAST);
    }
    
    
    public void actionPerformed (ActionEvent ae)
    {
	String str = ae.getActionCommand();
	if (str.equals ("New")) {
	    NewUserDialog nud = new NewUserDialog (jf);
	    nud.setVisible (true);

	    try {
		if (nud.getLogin () != null) {
		    manager.createUser (nud.getLogin(), nud.getPassword(), nud.getDescription());
		}
	    } catch (Exception e) {
		JOptionPane.showMessageDialog (jf, 
					       "Error creating user: " + e.getMessage(),
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
	    }
	}
	else if (str.equals ("Delete")) {
	    Object obj = jlUsers.getSelectedValue();
	    if (obj != null) {
		try {
		    manager.deleteUser (((MinervaUser) obj).getLogin());
		} catch (Exception e) {
		    JOptionPane.showMessageDialog (jf, 
						   "Error deleting user: " + e.getMessage(),
						   "Error",
						   JOptionPane.ERROR_MESSAGE);
		}
	    }
	}
	else if (str.equals ("Modify")) {
	    MinervaUser mu = (MinervaUser) jlUsers.getSelectedValue();
	    if (mu == null) return;

	    NewUserDialog nud = new NewUserDialog (jf, mu);
	    nud.setVisible (true);
	    
	    try {
		if (nud.getPassword () != null) {
		    manager.updateUser (nud.getLogin(), nud.getPassword(), nud.getDescription());
		}
	    } catch (Exception e) {
		JOptionPane.showMessageDialog (jf, 
					       "Error creating user: " + e.getMessage(),
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
	    }
	    
	}
	
	// Sync
	itemStateChanged (null);
    }

    public void itemStateChanged (ItemEvent ie)
    {
	System.out.println ("Oops");

	try {
	    int i = jcbGroups.getSelectedIndex();
	    if (i == 0) {
		jlUsers.setListData (manager.getUsers());
	    }
	    else {
		jlUsers.setListData (manager.getGroup(amg[i - 1].getName()).getUsers());
	    }
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   "Error retrieving users: " + e.getMessage() + ".",
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
    }

    public void updateChoice (MinervaGroup[] amg)
    {
	try {
	    this.amg = amg;
	    jcbGroups.removeAll();
	    jcbGroups.addItem ("<<All groups>>");
	    for (int i = 0; i < amg.length; i++) {
		jcbGroups.addItem (amg[i] + "");
	    }
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   "Error updating group information: " + e.getMessage() + ".",
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
    }
}










