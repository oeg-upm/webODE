package es.upm.fi.dia.ontology.minerva.mmc.ui;

// UI stuff
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

// Java stuff
import java.net.*;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;
import es.upm.fi.dia.ontology.minerva.mmc.*;

/**
 * Implement a login screen.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.2
 */ 
public class LoginScreen extends CenteredJDialog implements ActionListener
{
    private JTextField jtfUser, jtfURL;
    private JPasswordField jpfPassword;
    private boolean bCancel;
    private MinervaURL url;

    /**
     * Builds a login screen based on the frame.
     *
     * @param jf Parent frame.
     */
    public LoginScreen (JFrame jf)
    {
	super (jf, "Minerva Application Server Log In", true);


	JPanel jp1 = new JPanel();
	jp1.setLayout (new FlowLayout(FlowLayout.LEFT));
	jp1.add (new JLabel ("Username"));
	jp1.add (jtfUser = new JTextField (20));
	
	JPanel jp2 = new JPanel();
	jp2.setLayout (new FlowLayout(FlowLayout.LEFT));
	jp2.add (new JLabel ("Password"));
	jp2.add (jpfPassword = new JPasswordField (20));

	JPanel jp3 = new JPanel();
	jp3.setLayout (new FlowLayout(FlowLayout.LEFT));
	jp3.add (new JLabel ("URL"));
	jp3.add (jtfURL = new JTextField (20));

	JPanel jp = new JPanel();
	jp.setLayout (new GridLayout (3, 1, 50, 2));
	jp.add (jp1);
	jp.add (jp2);
	jp.add (jp3);

	Container cont = getContentPane();
	cont.setLayout (new BorderLayout (5, 5));

	JPanel jpButtons = new JPanel();
	JButton jbt = new JButton ("Connect");
	jbt.setMnemonic ('n');						
	jpButtons.add (jbt);
	jbt.addActionListener (this);
	jbt = new JButton ("Cancel");
	jbt.setMnemonic ('C');						
	jpButtons.add (jbt);
	jbt.addActionListener (this);

	cont.add (jp, BorderLayout.CENTER);
	cont.add (jpButtons, BorderLayout.SOUTH);
	cont.add (new JLabel (new ImageIcon (MMCConstants.MMC_MINERVA_ICON)), BorderLayout.WEST);

	pack();
    }

    /** 
     * For internal use only.
     */
    public void actionPerformed (ActionEvent ae)
    {
	if (ae.getActionCommand ().equals ("Cancel")) 
	    bCancel = true;
	else {
	    try {
		url = new MinervaURL (jtfURL.getText());
	    } catch (Exception e) {
		JOptionPane.showMessageDialog (this, 
					       "URL " + jtfURL.getText() + " is not valid: " + e.getMessage(),
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    if (jtfUser.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog (this, 
					       "Invalid username.",
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
		return;
	    }
	}	

	setVisible (false);
	dispose();
    }

    /**
     * Gets the selected password.
     *
     * @return The selected password or <tt>null</tt> if the process was cancelled.
     */
    public String getPassword () 
    {
	return bCancel ? null : new String(jpfPassword.getPassword()).trim();
    }

    /**
     * Gets the selected username.
     *
     * @return The selected username or <tt>null</tt> if the process was cancelled.
     */
    public String getUsername ()
    {
	return bCancel ? null : jtfUser.getText().trim();
    }

    /**
     * Gets the selected URL.
     *
     * @return The selected URL or <tt>null</tt> if the process was cancelled.
     */
    public MinervaURL getURL ()
    {
	return bCancel ? null : url;
    }

    /**
     * Sets the url for the dialog.
     *
     * @param url The URL.
     */
    public void setURL (MinervaURL url)
    {
	this.url = url;

	jtfURL.setText (url.toString());
    }
}
