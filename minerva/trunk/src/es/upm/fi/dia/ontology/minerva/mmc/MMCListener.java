package es.upm.fi.dia.ontology.minerva.mmc;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.net.*;

import es.upm.fi.dia.ontology.minerva.client.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.admin.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;

import es.upm.fi.dia.ontology.minerva.mmc.ui.*;

/**
 * The controller for the MMC.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.3
 */
public class MMCListener implements ActionListener, TreeSelectionListener, MMCConstants
{
    private JFrame jf;
    private DefaultTreeModel treeModel;
    private JTree jtree;
    private JSplitPane jsp;

    /**
     * Constructor.
     *
     * @param jf The MMC window
     * @param treeModel The treee model to show the services in.
     */
    public MMCListener (JFrame jf, DefaultTreeModel treeModel, JTree jtree, JSplitPane jsp)
    {
	this.treeModel = treeModel;
	this.jf = jf;
	this.jsp = jsp;

	this.jtree = jtree;

	jtree.addTreeSelectionListener (this);

	MouseListener ml = new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		int selRow = MMCListener.this.jtree.getRowForLocation(e.getX(), e.getY());
		TreePath selPath = MMCListener.this.jtree.getPathForLocation(e.getX(), e.getY());

		if(selRow != -1 && e.getClickCount() == 1 && e.isMetaDown()) {
		    _popup(e.getComponent(), e.getX(), e.getY(), selPath);
		}
	    }
	};
	jtree.addMouseListener (ml);
    }

    private void _popup(Component c, int x, int y, TreePath tp)
    {
	JPopupMenu jpm = new JPopupMenu();
	final Object[] path = tp.getPath();

	if (path.length == 1)
	    return;

	JMenuItem jmi;
	final AdministrationService admService = 
	    ((ConnectionDescriptor)((DefaultMutableTreeNode)path[1]).getUserObject()).getAdministrationService();
	if (path.length == 2) {
	    // Server properties
	    jpm.add (jmi = new JMenuItem (MMC_STOP_SERVER));
	    
	    jmi.addActionListener (new ActionListener() {
		public void actionPerformed (ActionEvent ae) {
		    try {
			if (JOptionPane.showConfirmDialog(null, 
							  "Stopping the server will make it unreachable.  " + 
							  "Are you sure you want to shut it down?", 
							  "Shutdown", 
							  JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			    admService.stopServer ();

			    // Remove node from tree
			    EventQueue.invokeLater (new Runnable() {
				public void run () {
				    treeModel.removeNodeFromParent ((MutableTreeNode) path[1]);
				}
			    });
			}
		    } catch (Exception e) {
			JOptionPane.showMessageDialog (jf, 
						       e.getMessage(),
						       "Error",
						       JOptionPane.ERROR_MESSAGE);
		    }
		}
	    });
	    jpm.addSeparator();
	    jpm.add (jmi = new JMenuItem (MMC_DISCONNECT));
	    jmi.addActionListener (new ActionListener() {
		public void actionPerformed (ActionEvent ae) {
		    // Remove node from tree
		    EventQueue.invokeLater (new Runnable() {
			public void run () {
			    treeModel.removeNodeFromParent ((MutableTreeNode) path[1]);
			}
		    });
		}
	    });	    
	}
	else {
	    // Service properties
	    final ActionListener al1, al2;
	    final JMenuItem jmi1;
	    jpm.add (jmi1 = new JMenuItem (MMC_STOP_SERVICE));
	    jmi1.addActionListener (al1 = new ActionListener() {
		public void actionPerformed (ActionEvent ae) {
		    try {
			if (JOptionPane.showConfirmDialog(null, 
							  "Stopping the service may cause momentaneous or permanent\n" + 
							  "disruption.  Do you want to continue?",
							  "Shutdown service", 
							  JOptionPane.YES_NO_OPTION) 
			    == JOptionPane.NO_OPTION)
			    return;
			    
			admService.stopService (path[2].toString());
			JOptionPane.showMessageDialog (jf, 
						       "Service " + path[2].toString() + " stopped.",
						       "Information",
						       JOptionPane.INFORMATION_MESSAGE);
		    } catch (Exception e) {
			JOptionPane.showMessageDialog (jf, 
						       e + "",
						       "Error",
						       JOptionPane.ERROR_MESSAGE);
		    }
		}
	    });
	    final JMenuItem jmi2;
	    jpm.add (jmi2 = new JMenuItem (MMC_START_SERVICE));
	    jmi2.addActionListener (al2 = new ActionListener() {
		public void actionPerformed (ActionEvent ae) {
		    try {
			admService.startService (path[2].toString());

			JOptionPane.showMessageDialog (jf, 
						       "Service " + path[2].toString() + " started.",
						       "Information",
						       JOptionPane.INFORMATION_MESSAGE);
		    } catch (Exception e) {
			JOptionPane.showMessageDialog (jf, 
						       e + "",
						       "Error",
						       JOptionPane.ERROR_MESSAGE);
		    }		    
		}
	    });
	    jpm.addSeparator();
	    jpm.add (jmi = new JMenuItem (MMC_RESTART_SERVICE));
	    jmi.addActionListener (new ActionListener() {
		public void actionPerformed (ActionEvent ae) {
		    al1.actionPerformed (ae);
		    al2.actionPerformed (ae);
		}
	    });

	    if (!path[2].toString().equals (MinervaServerConstants.AUTHENTICATION_SERVICE) &&
		!path[2].toString().equals (MinervaServerConstants.LOG_SERVICE)) {
		jpm.addSeparator();
		jpm.add (jmi = new JMenuItem (MMC_REMOVE_SERVICE));
		jmi.addActionListener (new ActionListener() {
		    public void actionPerformed (ActionEvent ae) {
			if (JOptionPane.showConfirmDialog(null, 
							  "Removing the service may cause permanent\n" + 
							  "disruption in services operation.  Do you want to continue?",
							  "Remove service", 
							  JOptionPane.YES_NO_OPTION) 
			    == JOptionPane.NO_OPTION)
			    return;
			try {
			    admService.removeService (path[2].toString());
			    JOptionPane.showMessageDialog (jf, 
							   "Service " + path[2].toString() + " removed sucessfully.",
							   "Information",
							   JOptionPane.INFORMATION_MESSAGE);
			    EventQueue.invokeLater (new Runnable() {
				public void run () {
				    treeModel.removeNodeFromParent ((MutableTreeNode) path[2]);
				}
			    });
			} catch (Exception e) {
			    JOptionPane.showMessageDialog (jf, 
							   e + "",
							   "Error",
							   JOptionPane.ERROR_MESSAGE);
			}
		    }
		});
	    }    
	}
	jpm.show (c, x, y);
    }
	
    /**
     * Deals with the events generated in the MMC window.
     *
     * @param ae The event.
     */
    public void actionPerformed (ActionEvent ae)
    {
	String str = ae.getActionCommand ();

	if (str.equals (MMC_EXIT)) {
	    jf.setVisible (false);
	    jf.dispose();

	    System.exit (0);
	}
	else if (str.equals (MMC_CONNECT)) {
	    connect();
	}
	else if (str.equals (MMC_STOP_SERVER)) {
	}
	else if (str.equals (MMC_ABOUT)) {
	    JOptionPane.showMessageDialog(jf, 
					  "Minerva Management Console " + MMC.MMC_VERSION + ".\n\n" +
					  "Designed and written by Julio César Arpírez Vega.\n" +
					  "(c) JCAV 2000.", MMC_ABOUT,
					  JOptionPane.INFORMATION_MESSAGE,
					  new ImageIcon (MMC_MINERVA_ICON));

	}
    }

    public void connect ()
    {
	connect (null);
    }

    public void connect(String url) 
    {
	// Show login screen
	try {
	    LoginScreen ls = new LoginScreen (jf);
	    if (url != null)
		ls.setURL (new MinervaURL (url));
	    
	    ls.setVisible (true);
	    
	    if (ls.getURL() == null)
		return;

	    MinervaSession ms = MinervaClient.getMinervaSession 
		(ls.getURL(), ls.getUsername(), ls.getPassword());
	    
	    _populateTree (ms, ls.getURL());
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   e.getMessage(),
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
    }


    /**
     * Fills the tree with the new nodes.
     */
    private void _populateTree (MinervaSession ms, MinervaURL url)
    {
	DefaultMutableTreeNode foo;
	AdministrationService admService;
	
	try {
	    admService =  MinervaClient.getAdministrator(ms);
	    ServiceDescriptor[] asd = admService.getServices();
	    
	    // Insert into tree so that the user can manage services.
	    treeModel.insertNodeInto (foo = new DefaultMutableTreeNode 
				      (new ConnectionDescriptor (ms, admService, url)),
				      (DefaultMutableTreeNode) treeModel.getRoot(), 
				      treeModel.getChildCount (treeModel.getRoot()));

	    
	    if (asd != null) {
		for (int i = 0; i < asd.length; i++) {
		    treeModel.insertNodeInto (new DefaultMutableTreeNode 
					      (asd[i].name),
					      foo,
					      0);
		}
	    }
	} catch (Exception e) {
	    JOptionPane.showMessageDialog (jf, 
					   e.getMessage(),
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
    }

    /**
     * To listen to tree events.
     */
    public void valueChanged (TreeSelectionEvent tse)
    {
	TreePath tp = tse.getNewLeadSelectionPath();
	if (tp == null) return;
	Object[] path = tp.getPath();
	DefaultMutableTreeNode dmtn;

	switch (path.length) {
	case 1:
	    // Root node
	    
	    break;
	case 2:
	    // A concrete server
	    dmtn = (DefaultMutableTreeNode) path[1];
	    ConnectionDescriptor cd1 = (ConnectionDescriptor) dmtn.getUserObject();
	    
	    jsp.setRightComponent (new ServerDialog (cd1, jf, dmtn, treeModel));

	    break;
	case 3:
	    // A service.  Try to get a description of the service's attributes.
	    // Get a reference to the management service (the second element in the path).
	    dmtn = (DefaultMutableTreeNode) path[1];
	    ConnectionDescriptor cd = (ConnectionDescriptor) dmtn.getUserObject();
	    AdministrationService admService = cd.getAdministrationService();

	    try {
		// Get the manager for the service (if any).
		MinervaManager mm = admService.getServiceManager 
		    (((DefaultMutableTreeNode) path[2]).getUserObject().toString());
		
		// Build a user interface and show it to the user.
		ServiceDialog sdi = new ServiceDialog (mm, (DefaultMutableTreeNode) path[2], jf);
		jsp.setRightComponent (new JScrollPane(sdi));
	    } catch (NotManageableException nme) {
		// Show the warning.
		JOptionPane.showMessageDialog (jf, 
					       nme.getMessage(),
					       "Warning",
					       JOptionPane.WARNING_MESSAGE);
	    } catch (Exception e) {
		e.printStackTrace();
		// Show the error.
		JOptionPane.showMessageDialog (jf, 
					       e.getMessage(),
					       "Error",
					       JOptionPane.ERROR_MESSAGE);
	    }
	    
	    break;
	}
    }
}


