package es.upm.fi.dia.ontology.minerva.mmc.ui;

import es.upm.fi.dia.ontology.minerva.mmc.*;
import es.upm.fi.dia.ontology.minerva.server.admin.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import java.io.*;

public class ServerDialog extends JPanel
{
    private ConnectionDescriptor cd;
    private JTextField tfServiceName, tfClassName, tfJarFile, tfInstances;
    private JFrame jf;
    private DefaultMutableTreeNode dmtn;
    private DefaultTreeModel treeModel;
    private JRadioButton jrb1;

    public ServerDialog (ConnectionDescriptor cd, JFrame jf, DefaultMutableTreeNode dmtn,
			 DefaultTreeModel treeModel)
    {
	this.cd = cd;
	this.jf = jf;
	this.dmtn = dmtn;
	this.treeModel = treeModel;

	setLayout (new BorderLayout ());
	
	JPanel jp = new JPanel();
	jp.setBorder (new TitledBorder ("Install a new service"));
	
	jp.setLayout (new BoxLayout (jp, BoxLayout.Y_AXIS));
	JPanel jp1 = new JPanel();
	jp1.setLayout (new FlowLayout (FlowLayout.LEFT));
	jp1.add (new JLabel ("Service name"));
	jp1.add (tfServiceName = new JTextField (20));
	jp.add (jp1);

	jp1 = new JPanel();
	jp1.setLayout (new FlowLayout (FlowLayout.LEFT));
	jp1.add (new JLabel ("Service interface"));
	jp1.add (tfClassName = new JTextField (20));
	jp.add (jp1);
	
	jp1 = new JPanel();
	jp1.add (new JLabel ("Type of service"));
	ButtonGroup bg = new ButtonGroup();
	jrb1 = new JRadioButton("Stateless", true);
	JRadioButton jrb2 = new JRadioButton("Stateful", false);
	bg.add (jrb1);
	bg.add (jrb2);
	jp1.add (jrb1);
	jp1.add (jrb2);
	jp.add (jp1);

	jp1 = new JPanel();
	jp1.setLayout (new FlowLayout (FlowLayout.LEFT));
	jp1.add (new JLabel ("Number of pool instances"));
	jp1.add (tfInstances = new JTextField (20));
	jp.add (jp1);

	
	jp1 = new JPanel();
	jp1.setLayout (new FlowLayout (FlowLayout.LEFT));
	jp1.add (new JLabel ("JAR file"));
	jp1.add (tfJarFile = new JTextField (20));
	JButton bt;
	jp1.add (bt = new JButton ("Browse..."));
	bt.addActionListener (new ActionListener () {
	    public void actionPerformed (ActionEvent ae) {
		JFileChooser jfc = new JFileChooser();
		jfc.showOpenDialog (ServerDialog.this.jf);

		File file = jfc.getSelectedFile();
		if (file != null)
		    tfJarFile.setText (file.toString());
	    }
	});
	jp.add (jp1);

	add (jp, BorderLayout.CENTER);

	JPanel jb = new JPanel();
	jb.add (bt = new JButton ("Install"));
	bt.addActionListener (new InstallationControl());
	add (jb, BorderLayout.SOUTH);
    }

    /**
     * Class responsible for carrying out the installation operations.
     */
    protected class InstallationControl implements ActionListener, Runnable
    {	
	private ServiceDescriptor sd;
	private JButton jb;

	public void actionPerformed (ActionEvent ae) {
	    // Check parameters.
	    if (tfServiceName.getText().trim().length() == 0 ||
		tfClassName.getText().trim().length() == 0) {
		JOptionPane.showMessageDialog (jf, 
					       "The service and interface names are mandatory.",
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    if (tfJarFile.getText().trim().length() == 0) {
		if (JOptionPane.showConfirmDialog (jf, 
						   "You didn´t specify a jar file, so the service you are\n" + 
						   "installing must be contained in one of the already installed\n"+
						   "jar files.  Do you wish to continue?",
						   "Warning",
						   JOptionPane.YES_NO_OPTION)
		    == JOptionPane.NO_OPTION)
		    return;
	    }


	    // Disable button
	    jb = (JButton) ae.getSource();
	    jb.setEnabled (false);
 
	    // Create the service description.
	    String interfaceName = tfClassName.getText().trim();
	    String className     = interfaceName + "Imp";
	    String name          = tfServiceName.getText().trim();
	    try {
		sd = new ServiceDescriptor (name, className, interfaceName, null,
					    jrb1.isSelected() ? 
					    es.upm.fi.dia.ontology.minerva.server.services.MinervaServiceConfiguration.STATELESS:
					    es.upm.fi.dia.ontology.minerva.server.services.MinervaServiceConfiguration.STATEFUL, true, 
					    jrb1.isSelected() ? 1 : Integer.parseInt (tfInstances.getText()));
	    } catch (Exception e) {
		JOptionPane.showMessageDialog (jf, 
					       "The number of instances field must be numeric.",
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    // Create a worker thread to do the work
	    Thread th = new Thread(this, "installer thread");
	    th.start ();
	}

	public void run()
	{
	    InputStream is = null;
	    try {
		byte[] buffer = null;

		if (tfJarFile.getText().trim().length() > 0) {
		    // A jar file was specified...
		    // Display a progress bar.
		    ProgressMonitorInputStream pmis = new ProgressMonitorInputStream 
			(jf, "Installing service " + sd.name + ".  Transferring data...",
			 new FileInputStream (tfJarFile.getText().trim()));
		    is = pmis;
		    
		    
		    try {
			buffer = new byte[is.available()];
			is.read (buffer, 0, buffer.length);
		    } catch (InterruptedIOException ioe) {
			return;
		    }
		}

		// Invoke server's method.
		cd.getAdministrationService().installService (sd, buffer);

		JOptionPane.showMessageDialog (jf, 
					       "The service " + sd.name + " was successfully installed.",
					       "Information",
					       JOptionPane.INFORMATION_MESSAGE);

		// Update tree
		EventQueue.invokeLater (new Runnable () {
		    public void run () {
			treeModel.insertNodeInto (new DefaultMutableTreeNode 
						  (sd.name),
						  dmtn,
						  0);
		    }
		});

		
	    } catch (Exception e) {
		JOptionPane.showMessageDialog (jf, 
					       "An error occurred when installing the service: " + e.getMessage(),
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
	    } finally {
		try {
		    is.close();
		} catch (Exception e) {}
		jb.setEnabled (true);
	    }
	}
    }
}








