package es.upm.fi.dia.ontology.webode.applet;

import java.net.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

/**
 * No longer in use
 */
public class ComHelper extends Thread
{
    private Vector vRemoved, vNew, vDirty;
    private URL urlPost;
    private JFrame jf;

    public ComHelper (URL urlPost, Vector vRemoved, Vector vNew, Vector vDirty, JFrame jf)
    {
	this.vRemoved = vRemoved;
	this.urlPost  = urlPost;
	this.vNew     = vNew;
	this.vDirty   = vDirty;
	this.jf       = jf;
    }

    public void run ()
    {
	if (vRemoved.isEmpty() && vNew.isEmpty() && vDirty.isEmpty())
	    // Send nothing
	    return;

	try {
	    // Open the connection to the server.
	    URLConnection urlc = urlPost.openConnection();
	    urlc.setDoOutput (true);
	    
	    System.out.println ("1");
	    ObjectOutputStream oos = new ObjectOutputStream 
		(urlc.getOutputStream());

	    System.out.println ("2");

	    // First, send the vector of removed elements
	    oos.writeObject (vRemoved);
	    System.out.println ("3");
	    // Now, new ones.
	    oos.writeObject (vNew);
	    System.out.println ("4");
	    // Dirty ones.
	    oos.writeObject (vDirty);
	    System.out.println ("5");

	    oos.flush();
	    oos.close();

	    // Receive response
	    InputStream is = urlc.getInputStream();
	    DataInputStream dis = new DataInputStream (is);
	    int i = dis.readInt();
	    if (i < 0)
		JOptionPane.showMessageDialog (jf, 
					       "There was an error committing data.",
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
	    else
		JOptionPane.showMessageDialog (jf, 
					       "Commit Ok.",
					       "Information",
					       JOptionPane.INFORMATION_MESSAGE);
	    dis.close();
	} catch (IOException ioe) {
	    JOptionPane.showMessageDialog (jf, 
					   "There was an error committing data: " + ioe.getMessage() + ".",
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
    }
}





