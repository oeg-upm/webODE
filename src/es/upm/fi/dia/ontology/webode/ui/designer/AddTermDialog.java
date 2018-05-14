package es.upm.fi.dia.ontology.webode.ui.designer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

public class AddTermDialog extends JDialog implements ActionListener, ItemListener
{
    private String selectedItem;
    private String metaproperties;
    private JPanel jpOption;
    private JPanel jpAux;
    private String[] items;
    private JTextField jtfNew, jtfCardinality,jtfMetaproperties;
    private JComboBox jcbItems;
    private JRadioButton jbNew, jbExisting;
    private int maxCardinality = -50;
    private DesignModel ddm;
   
    public AddTermDialog (JFrame jfr, String[] items,DesignModel ddm)
    {
	this (jfr, items, false,ddm);
    }

    public AddTermDialog (JFrame jfr, String[] items, boolean isRelation,DesignModel ddm)
    {
	super (jfr, DesignerConstants.ADD_TERMC, true);
	
	this.items = items;
        this.ddm = ddm;
	Container cont = getContentPane();
	cont.setLayout (new BorderLayout ());
	
	JPanel top = new JPanel();
	top.setLayout (new BoxLayout (top, BoxLayout.Y_AXIS));

	JPanel jp;
	if (items != null && items.length > 0) {
	    jcbItems = new JComboBox (items);
	    jcbItems.addItemListener(this);
	    
	    // Add radio buttons
	    ButtonGroup bg = new ButtonGroup();
	    jbNew  = new JRadioButton (DesignerConstants.Newterm , true);
	    jbExisting = new JRadioButton (DesignerConstants.Existingterm);
	    jbNew.addItemListener (this);
	    jbExisting.addItemListener (this);
	    bg.add (jbNew);
	    bg.add (jbExisting);
	    jp = new JPanel();
	    
	    jp.add (jbNew);
	    jp.add (jbExisting);
	    top.add (jp);
	}
	cont.add (top, BorderLayout.CENTER);

	// Add text field
	jtfNew = new JTextField (40);
	jtfNew.addActionListener (this);
	jpOption = new JPanel();
	jpOption.add (jtfNew);
	top.add (jpOption);	
	
	// If relation, add field to ask for cardinality
	if (isRelation) {
	    jp = new JPanel();
	    jp.add (new JLabel (DesignerConstants.MaximumCardinality));
	    jtfCardinality = new JTextField (3);
	    jp.add (jtfCardinality);
	    
	    top.add (jp);
	}	    
	if (! isRelation) {
	    jpAux = new JPanel();
	    jpAux.add (new JLabel (DesignerConstants.Valuesofthemeta));
	    jtfMetaproperties = new JTextField (10);
	    jpAux.add (jtfMetaproperties);
	    
	    top.add (jpAux);
	}	    
	jp = new JPanel();
	JButton foo;
	jp.add (foo = new JButton (DesignerConstants.OKC));
	foo.addActionListener (this);
	jp.add (foo = new JButton (DesignerConstants.CANCELC));
	foo.addActionListener (this);
	cont.add (jp, BorderLayout.SOUTH);

	jtfNew.setRequestFocusEnabled (true);
	jtfNew.requestFocus();

	setSize (475, 200);
	setVisible (true);
    }

    public void itemStateChanged (ItemEvent ie)
    {
	if (ie.getStateChange () == ie.SELECTED) {
	    jpOption.removeAll();
	    
	    if (ie.getSource() == jbExisting) {
		jpOption.add (jcbItems);
		jpAux.removeAll(); 
		jpAux.add (new JLabel (DesignerConstants.AValuesofthemetaprop));
                String metap = ddm.getMetaproperties(jcbItems.getSelectedItem().toString());
	        String mp = new String();
	        if(metap != null)
	          mp = metap;
	        jtfMetaproperties = new JTextField (mp,10);
	        jpAux.add (jtfMetaproperties);
		
	    }
	    else if (ie.getSource() == jbNew){
		jpOption.add (jtfNew);
		jpAux.removeAll(); 
		jpAux.add (new JLabel (DesignerConstants.Valuesofthemeta));
		jtfMetaproperties = new JTextField (10);
	        jpAux.add (jtfMetaproperties);
	    }
            else
            {
              jpOption.add (jcbItems);	
              jpAux.removeAll(); 
              jpAux.add (new JLabel (DesignerConstants.Valuesofthemeta));
              String metap = ddm.getMetaproperties(jcbItems.getSelectedItem().toString());
	      String mp = new String();
	      if(metap != null)
	        mp = metap;
	      jtfMetaproperties = new JTextField (mp,10);
	      jpAux.add (jtfMetaproperties);
            }
	    // Validate new layout
	    validate();
	}
    }

    public void actionPerformed (ActionEvent ae)
    {
	 if (ae.getSource() == jtfNew ||
	    ae.getActionCommand().equals (DesignerConstants.OK)) {
	    this.selectedItem = items == null || items.length == 0 || jbNew.isSelected() ? 
		jtfNew.getText() : jcbItems.getSelectedItem().toString();
	    if (this.selectedItem.equals (""))
		this.selectedItem = null;	                                                     
	    if(jtfMetaproperties != null)
	     this.metaproperties = jtfMetaproperties.getText();
	    // Relation?
	    if (jtfCardinality != null) {
		String text = jtfCardinality.getText();
		if (text.equalsIgnoreCase ("n"))
		    maxCardinality = -1;
		else {
		    try {
			maxCardinality = Integer.parseInt (text);
			if (maxCardinality <= 0) {
			    JOptionPane.showMessageDialog (this, DesignerConstants.Invalidcardinality + 
							   DesignerConstants.greater,
							   DesignerConstants.Error, JOptionPane.ERROR_MESSAGE);
			    return;
			}
		    } catch (Exception e) {
			  JOptionPane.showMessageDialog (this, DesignerConstants.Invalidcardinality + 
							   DesignerConstants.greater,
							   DesignerConstants.Error, JOptionPane.ERROR_MESSAGE);
			return;
		    }
		}
	    }
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
    public String getTermName ()
    {
	return this.selectedItem;
    }
    
    public String getMetapropertiesAux()
    {
	return this.metaproperties;
    }

    public int getCardinality ()
    {
	return maxCardinality;
    }
}
