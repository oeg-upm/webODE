package es.upm.fi.dia.ontology.webode.ui.designer;

import java.net.*;
import java.util.*;
import java.io.*;
import java.applet.*;

import javax.swing.*;

public class Communicator extends Thread
{
    private URL urlPost;
    private int command;
    private Object data;
    private JFrame jf;
    private Object parameters;
    private CommandRunnable op;


    public Communicator (JFrame jf, URL urlPost, int command, Object parameters, Object data,
			 CommandRunnable op)
    {
	this.jf         = jf;
	this.urlPost    = urlPost;
	this.command    = command;
	this.data       = data;
	this.parameters = parameters;
	this.op         = op;
	
	//System.out.println ("PARA1: " + parameters);
    }

    public void run ()
    {
	ProgressMonitor pg = new ProgressMonitor (jf, DesignerConstants.Transferring, DesignerConstants.Sending, 0, 2);
	try {
	    // Open the connection to the server.
	    URLConnection urlc = urlPost.openConnection();
	    urlc.setDoOutput (true);
	    
	    ObjectOutputStream oos = new ObjectOutputStream (urlc.getOutputStream());
		pg.setMillisToDecideToPopup (0);
	    pg.setMillisToPopup (0);
	    // Send the command and the data
	    oos.writeObject (new CommandDescriptor (command, parameters, data));
	    oos.flush();
	    oos.close();
	    pg.setProgress (1);
	    pg.setNote (DesignerConstants.Rec);

	    // Receive response
	    InputStream is = urlc.getInputStream();
	    ObjectInputStream ois = new ObjectInputStream 
		(new ProgressMonitorInputStream (jf, DesignerConstants.Receiveing, is));
	    CommandDescriptor cd = (CommandDescriptor) ois.readObject();
	    pg.setProgress (2);
	    pg.setNote ("Done!");
	    
	    // Use the command descriptor as a return descriptor this time.
	    int i = cd.command;
	    if (i < 0)
		JOptionPane.showMessageDialog (jf, 
					       DesignerConstants.Errorcommittingdata +
					       cd.parameters + ".",
					       DesignerConstants.Error ,
					       JOptionPane.ERROR_MESSAGE);
	    else
		JOptionPane.showMessageDialog (jf, 
					       DesignerConstants.Operationcompletedsucessfully ,
					       DesignerConstants.Information,
					       JOptionPane.INFORMATION_MESSAGE);
	    ois.close();
	    
	    //System.out.println ("RECEIVED DATA: " + cd);

	    // If there is an operation to perform, carry it out within the same
	    // thread.
	    if (op != null) {
		op.setCommandDescriptor (cd);
		op.run();
	    }
	} catch (Exception ioe) {
	    JOptionPane.showMessageDialog (jf, 
					  DesignerConstants.Errorsending + ioe + ".",
					   DesignerConstants.Error ,
					   JOptionPane.ERROR_MESSAGE);
	    ioe.printStackTrace();
	} finally {
	    pg.close();
	}
    }
}





