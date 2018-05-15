package es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.gui;

import es.upm.fi.dia.ontology.minerva.server.management.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Dialog for creating a new user or group.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class NewGroupDialog extends JDialog implements ActionListener
{
    private boolean bCancel = true;
    private JFrame jf;
    private JTextField jtfName;
    private JTextArea jtaDescription;

    public NewGroupDialog (JFrame parent)
    {
	this (parent, null);
    }

    public NewGroupDialog (JFrame parent, MinervaGroup mg)
    {
	super (parent, "Create new group", true);
	jf = parent;
	
	JPanel jp = new JPanel();
	jp.setLayout (new BoxLayout (jp, BoxLayout.Y_AXIS));
	jp.add (new JLabel ("Name"));
	jp.add (new JLabel ("Description"));

	Container cont = getContentPane();
	cont.setLayout (new BorderLayout (5, 5));
	cont.add (jp, BorderLayout.WEST);

	jp = new JPanel();
	jp.setLayout (new BoxLayout (jp, BoxLayout.Y_AXIS));
	jp.add (jtfName = new JTextField (20));
	jp.add (new JScrollPane (jtaDescription = new JTextArea (10, 20)));
	cont.add (jp, BorderLayout.CENTER);

	if (mg != null) {
	    jtfName.setText (mg.getName());
	    jtfName.setEditable (false);  
	    jtaDescription.setText (mg.getDescription());
	}

	jp = new JPanel();
	JButton jbt;
	jp.add (jbt = new JButton ("Ok"));
	jbt.setMnemonic ('O');
	jbt.addActionListener (this);
	jp.add (jbt = new JButton ("Cancel"));
	jbt.setMnemonic ('C');
	jbt.addActionListener (this);
	cont.add (jp, BorderLayout.SOUTH);

	pack();
    }

    public void actionPerformed (ActionEvent ae)
    {
	if (ae.getActionCommand().equals ("Ok")) {
	    // Check the login is correct
	    if (jtfName.getText().trim().equals("")) {
		JOptionPane.showMessageDialog (jf, 
					       "Group name cannot be empty.",
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    bCancel = false;
	}
	else 
	    bCancel = true;
	
	setVisible (false);
    }
    
    public String getName ()
    {
	return bCancel ? null : jtfName.getText().trim();
    }

    public String getDescription ()
    {
	return bCancel ? null : jtaDescription.getText().trim();
    }
}
    
