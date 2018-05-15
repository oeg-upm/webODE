package es.upm.fi.dia.ontology.minerva.mmc.ui;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.management.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.admin.*;
import es.upm.fi.dia.ontology.minerva.mmc.ui.*;
import es.upm.fi.dia.ontology.minerva.mmc.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * This class implements a generic user interface to configure
 * a service based on its description.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class ServiceDialog extends JPanel implements ActionListener {
    private MinervaManager mm;
    private AdministrationService admService;
    private MinervaServiceDescription msd;
    private ServiceDescriptor sd;
    private JFrame jf;

    /**
     * Constructor.
     *
     * @param mm The manager for the service to set up.
     */
    public ServiceDialog (MinervaManager mm, DefaultMutableTreeNode dtn, JFrame jf) throws RemoteException
    {
	this.mm = mm;
        this.msd = mm.getServiceDescription();
	this.jf = jf;
        this.admService=((ConnectionDescriptor)((DefaultMutableTreeNode)dtn.getPath()[1]).getUserObject()).getAdministrationService();

        this.sd = null;
        ServiceDescriptor[] a_sd=admService.getServices();
        for(int i=0; a_sd!=null && i<a_sd.length; i++)
          if(a_sd[i].name.equals(dtn.toString()))
            this.sd = a_sd[i];

	// Let's see if the service has a user interface.
	MinervaUI mui = mm.getServiceUI ();
	if (mui != null) {
	    setLayout (new BorderLayout ());
	    mui.setManager (mm);
	    add (mui, BorderLayout.CENTER);
	}
	else {
	    // Build a default user interface based on the service descriptor.
	    _buildDefaultUserInterface (mm.getServiceDescription(), dtn);
	}
    }

    private void _buildDefaultUserInterface (MinervaServiceDescription msd,
					     DefaultMutableTreeNode dtn) throws RemoteException
    {
	this.msd = msd;

	//System.out.println ("Building default interface for " + (msd == null ? "nulo" : msd.toString()));

	setLayout (new BorderLayout ());

	JPanel root = new JPanel();
	root.setLayout (new BoxLayout (root, BoxLayout.Y_AXIS));
	root.add (new JLabel ("Description: " + msd.description, JLabel.LEFT));

	add (root, BorderLayout.CENTER);

	// Paint a border.
        String title=msd.name;
        if(this.sd!=null) {
          title += "; ";
          if(sd.type==MinervaServiceConfiguration.STATEFUL)
            title += "statefull (" + sd.nInstances + ")";
          else
            title += "stateless (1)";
        }

	root.setBorder (new TitledBorder (title));

	// Add a separation
	root.add (Box.createVerticalStrut (15));

	// Add attributes
	for (int i = 0; i < msd.mad.length; i++) {
	    JPanel jp = new JPanel();
	    jp.setLayout (new FlowLayout (FlowLayout.LEFT));
	    root.add (jp);

	    // Put name and description as a tooltip.
	    JLabel foo;
	    jp.add (foo = new JLabel (msd.mad[i].name));
	    //dtn.setToolTipText (msd.mad[i].description);

	    // Depending on the type of the attribute, show
	    // a different control.

	    // Boolean
	    if (msd.mad[i].cl.equals (boolean.class)) {
		// Ignore values and show a radio group.
		ButtonGroup bg = new ButtonGroup();

		final JRadioButton jrb1 = new JRadioButton ("Yes", ((Boolean) msd.mad[i].currentValue).booleanValue());
		final JRadioButton jrb2 = new JRadioButton ("No", !((Boolean) msd.mad[i].currentValue).booleanValue());
		bg.add (jrb1);
		bg.add (jrb2);

		// Add to panel.
		jp.add (jrb1);
		jp.add (jrb2);

		// This is to keep the value synchronized.
		final MinervaAttributeDescription dummy = msd.mad[i];
		ItemListener il = new ItemListener () {
		    public void itemStateChanged (ItemEvent ie) {
			if (ie.getSource() == jrb1) {
			    dummy.currentValue = new Boolean (true);
			}
			else {
			    dummy.currentValue = new Boolean (false);
			}
			dummy.changed = true;
		    }
		};
		jrb1.addItemListener (il);
		jrb2.addItemListener (il);
	    }
	    // String
	    else if (msd.mad[i].cl.equals (String.class)) {

		// Let's see whether it has a list of possible values.
		if (msd.mad[i].values == null) {
		    // No list of values available
		    final JTextField jtf = new JTextField
			(msd.mad[i].currentValue == null ? "" : msd.mad[i].currentValue.toString(), 30);

		    final MinervaAttributeDescription dummy = msd.mad[i];
		    jtf.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
			    dummy.currentValue = jtf.getText();
			    dummy.changed = true;
			}
		    });
		    jtf.addFocusListener (new FocusAdapter() {
			public void focusLost (FocusEvent fe) {
			    dummy.currentValue = jtf.getText();
			    dummy.changed = true;
			}
		    });
		    jp.add (jtf);
		}
		else {
		    // Let's see whether it has a list of possible values.
		    // Build a combo box with valid values.
		    JComboBox jcb = new JComboBox ();

		    int cur = 0;
		    for (int j = 0; j < msd.mad[i].values.length; j++) {
			jcb.addItem (msd.mad[i].values[j]);

			if (msd.mad[i].values[j].equals (msd.mad[i].currentValue))
			    cur = j;
		    }

		    // Select current index
		    jcb.setSelectedIndex (cur);
		    jp.add (jcb);

		    final MinervaAttributeDescription dummy = msd.mad[i];
		    jcb.addItemListener (new ItemListener () {
			public void itemStateChanged (ItemEvent ie) {
			    dummy.currentValue = ((JComboBox) ie.getSource()).getSelectedItem();
			    dummy.changed = true;
			}
		    });
		}
	    }
	    else if (msd.mad[i].cl.equals (Integer.class)) {
		if (msd.mad[i].values == null) {
		    // No list of values available
		    final JTextField jtf = new JTextField
			(msd.mad[i].currentValue == null ? "" : msd.mad[i].currentValue.toString(), 30);

		    final MinervaAttributeDescription dummy = msd.mad[i];
		    jtf.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
			    Integer in;
			    try {
			     in = Integer.valueOf (jtf.getText());
			    } catch (Exception e) {
				JOptionPane.showMessageDialog (jf,
							       "Invalid integer number",
							       "Error",
							       JOptionPane.ERROR_MESSAGE);
				jtf.setText ("0");
				return;
			    }
			    dummy.currentValue = in;
			    dummy.changed = true;
			}
		    });
		    jtf.addFocusListener (new FocusAdapter() {
			public void focusLost (FocusEvent fe) {
			    Integer in;
			    try {
				in = Integer.valueOf (jtf.getText());
			    } catch (Exception e) {
				JOptionPane.showMessageDialog (jf,
							       "Invalid integer number",
							       "Error",
							       JOptionPane.ERROR_MESSAGE);
				jtf.setText ("0");
				return;
			    }

			    dummy.currentValue = in;
			    dummy.changed = true;
			}
		    });
		    jp.add (jtf);

		}
	    }
	    else if (msd.mad[i].cl.equals (int.class)) {
	    }
	    else {
	    }
	}

	    JPanel jb = new JPanel();
	JButton jbt = new JButton ("Commit changes");
	jbt.setMnemonic ('C');
	jb.add (jbt);
	jbt.addActionListener (this);

	add (jb, BorderLayout.SOUTH);
    }

    /**
     * Visible to fulfill the contract.
     */
    public void actionPerformed (ActionEvent ae)
    {
	//System.out.println ("Commit changes");

	try {
	    // Set all the attributes with current values.
	    for (int i = 0; i < msd.mad.length; i++) {
		if (msd.mad[i].changed) {
		    mm.setAttribute (msd.mad[i].name, msd.mad[i].currentValue);

		    //System.out.println ("setting attribute " + msd.mad[i].name + " with value " +
		    //		msd.mad[i].currentValue);
		}
	    }

	    // Commit changes
	    mm.commit();

	    // Ask to restart the service.
	    JOptionPane.showMessageDialog (jf,
					   "Probably the service needs to be restarted so that changes can take effect.",
					   "Warning",
					   JOptionPane.WARNING_MESSAGE);
	} catch (Exception e) {
 	    JOptionPane.showMessageDialog (jf,
					   e.getMessage(),
					   "Error",
					   JOptionPane.ERROR_MESSAGE);
	}
    }
}
