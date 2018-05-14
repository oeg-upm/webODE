package es.upm.fi.dia.ontology.webode.ui.designer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddMetapropertiesDialog extends JDialog implements ActionListener, ItemListener
{
    private String selectedItem;
    private JPanel jpOption;
    private JTextField jtfNew;
    private String name;
    private String values;
    

    public AddMetapropertiesDialog (JFrame jfr, String message,String name,String mp)
    {
	super (jfr,message + name , true);
	this.name = name;
	Container cont = getContentPane();
	cont.setLayout (new BorderLayout ());
	
	JPanel top = new JPanel();
	top.setLayout (new BoxLayout (top, BoxLayout.Y_AXIS));

	JPanel jp;
	String metaproperties = new String();
	if(mp != null)
	 metaproperties = mp;
	cont.add (top, BorderLayout.CENTER);

	// Add text field
	jtfNew = new JTextField(metaproperties,10);
	jtfNew.addActionListener (this);
	jpOption = new JPanel();
	jpOption.add(new JLabel (DesignerConstants.Valuesmetaproperties));
	jpOption.add (jtfNew);
	top.add (jpOption);	
	
	
	jp = new JPanel();
	JButton foo;
	jp.add (foo = new JButton (DesignerConstants.OKC));
	foo.addActionListener (this);
	jp.add (foo = new JButton (DesignerConstants.CANCELC));
	foo.addActionListener (this);
	cont.add (jp, BorderLayout.SOUTH);

	jtfNew.setRequestFocusEnabled (true);
	jtfNew.requestFocus();

	setSize (350, 125);
	setVisible (true);
    }

    public void itemStateChanged (ItemEvent ie)
    {
	if (ie.getStateChange () == ie.SELECTED) {
	    jpOption.removeAll();
	    
	    // Validate new layout
	    validate();
	}
    }

    public void actionPerformed (ActionEvent ae)
    {
	if (ae.getSource() == jtfNew || 
	    ae.getActionCommand().equals (DesignerConstants.OK)) {
	    this.selectedItem = jtfNew.getText();
	    if (this.selectedItem.equals (""))
		this.selectedItem = null;
        }
	else {
	    this.selectedItem = null;
	}
	
	setVisible (false);
	dispose();
    }

    /**
     * Returns the name chosen by the user either new or existing.
     */
    public String getValues ()
    {
	return this.selectedItem;
    }
    
}
