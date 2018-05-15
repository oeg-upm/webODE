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
public class NewUserDialog extends JDialog implements ActionListener
{
    private boolean bCancel = true;
    private JFrame jf;
    private JTextField jtfLogin;
    private JPasswordField jtfPassword, jtfConfirmPassword;
    private JTextArea jtaDescription;

    public NewUserDialog (JFrame parent)
    {
	this (parent, null);
    }

    public NewUserDialog (JFrame parent, MinervaUser mu)
    {
	super (parent, "Create new user", true);
	jf = parent;
	
	JPanel jp = new JPanel();
	jp.setLayout (new BoxLayout (jp, BoxLayout.Y_AXIS));
	jp.add (new JLabel ("Login"));
	jp.add (new JLabel ("Password"));
	jp.add (new JLabel ("Confirm Password"));
	jp.add (new JLabel ("Description"));

	Container cont = getContentPane();
	cont.setLayout (new BorderLayout (5, 5));
	cont.add (jp, BorderLayout.WEST);

	jp = new JPanel();
	jp.setLayout (new BoxLayout (jp, BoxLayout.Y_AXIS));
	jp.add (jtfLogin = new JTextField (20));
	jp.add (jtfPassword = new JPasswordField (20));
	jp.add (jtfConfirmPassword = new JPasswordField (20));
	jp.add (new JScrollPane (jtaDescription = new JTextArea (10, 20)));
	cont.add (jp, BorderLayout.CENTER);

	if (mu != null) {
	    jtfLogin.setText (mu.getLogin());
	    jtfLogin.setEditable (false);  
	    jtaDescription.setText (mu.getDescription());
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
	    if (jtfLogin.getText().trim().equals("")) {
		JOptionPane.showMessageDialog (jf, 
					       "Login cannot be empty.",
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    String password1, password2;
	    password1 = new String (jtfPassword.getPassword());
	    password2 = new String (jtfConfirmPassword.getPassword());
	    if (!password1.equals (password2)) {
		JOptionPane.showMessageDialog (jf, 
					       "Passwords don't match.",
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
    
    public String getLogin ()
    {
	return bCancel ? null : jtfLogin.getText().trim();
    }

    public String getPassword ()
    {
	String foo = new String (jtfPassword.getPassword()).trim();
	return bCancel ? null : (foo.equals("") ? null : foo);
    }
    
    public String getDescription ()
    {
	return bCancel ? null : jtaDescription.getText().trim();
    }
}
    
