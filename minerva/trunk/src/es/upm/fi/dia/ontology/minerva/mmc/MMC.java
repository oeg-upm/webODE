package es.upm.fi.dia.ontology.minerva.mmc;

// UI stuff
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * This class implements the <i>Minerva Management Console</i> for an
 * unrestricted environment, i.e. a stand-alone application.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.3
 */
public class MMC extends JFrame implements MMCConstants
{
    /**
     * Current MMC version.
     */
    public static final String MMC_VERSION = "0.7";

    // Variables
    private JSplitPane jsp;
    private JTree jtree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private MMCListener mmcl;

    /**
     * Builds the MMC.
     *
     * @param url The URL describing the Minerva Server instance.
     * @param b   To exit on close.
     */
    public MMC (String url, boolean b)
    {
	super (MMC_TITLE);
	setIconImage (loadImage (MMC_MINERVA_ICON32x32));

	if (b) 
	    addWindowListener (new WindowAdapter () {
		public void windowClosing (WindowEvent we) {
		    setVisible (false);
		    dispose();
		    System.exit (0);
		}
	    });
	
	// Build appeareance.
	_createWindowArea ();
	_createMenuBar ();

	if (url != null)
	    _connect (url);


    }

    private Image loadImage (String name)
    {
	Toolkit tk = Toolkit.getDefaultToolkit();
	MediaTracker mt = new MediaTracker(this);

	try {
	    Image img = tk.getImage (name);
	    mt.addImage (img, 0);
	    mt.waitForAll();

	    return img;
	} catch (Exception e) {
	    System.err.println ("Error loading image: " + e);
	    return null;
	}
    }

    private void _connect(String url)
    {
	// Generate event...
	mmcl.connect (url);
    }


    /**
     * Build the menus.
     */
    private void _createMenuBar ()
    {
	// Listener
	mmcl = new MMCListener (this, treeModel, jtree, jsp);

	// Menubar
	JMenuBar jmb = new JMenuBar();
	setJMenuBar (jmb);

	// Menus
	JMenu jm = new JMenu (MMC_GENERAL);
	jmb.add (jm);
	JMenuItem jmi = new JMenuItem (MMC_CONNECT);
	jm.add (jmi);
	jmi.addActionListener (mmcl);

	jm.addSeparator();

	jmi = new JMenuItem (MMC_EXIT);
	jm.add (jmi);
	jmi.addActionListener (mmcl);

	// Options
	jm = new JMenu (MMC_OPTIONS);
	jmb.add (jm);
	jmi = new JMenuItem (MMC_STOP_SERVER);
	jm.add (jmi);
	jmi.addActionListener (mmcl);


	// Help menu
	jm = new JMenu (MMC_HELP);
	// jmb.setHelpMenu (jm);--> Not yet implemented.
	jmb.add (jm);

	jmi = new JMenuItem (MMC_ABOUT);
	jm.add (jmi);
	jmi.addActionListener (mmcl);

	setSize (MMC_WIDTH, MMC_HEIGHT);
    }
   
    /**
     * Create window area.
     */
    private void _createWindowArea ()
    {
	// Split up into two areas: one with a tree representing structure
	// and another with the configuration UI.
	jsp = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT);
	
	root = new DefaultMutableTreeNode (MMC_ROOT);
	treeModel = new DefaultTreeModel (root);
	jtree = new JTree(treeModel);
	jsp.setLeftComponent (new JScrollPane (jtree));
	
	jsp.setRightComponent (new JPanel());

	getContentPane().add (jsp);
    }

 
    /**
     * Builds the MMC.
     */
    public MMC ()
    {
	this (null, true);
    }

    /**
     * Entry method.
     * <p>
     * It can receive one parameter as a command-line argument.
     * This parameter will be the URL of the server to connect to.     
     */
    public static void main (String[] args)
    {
	// Native look and field
	boolean b = args != null && args.length != 0 && args[0].equals ("-native");
	
	// Set default look & feel.
	if (b) {
	    try {
		UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
		System.err.println ("Error setting system look and feel.  Working with Java's.");
	    }
	}
	
	if ((b && args.length == 1) || args.length == 0) {
	    new MMC ().setVisible (true);
	}
	else {
	    new MMC (b ? args[1] : args[0], true).setVisible (true);
	}
    }
}








