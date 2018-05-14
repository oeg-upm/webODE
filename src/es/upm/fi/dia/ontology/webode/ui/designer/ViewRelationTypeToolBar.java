package es.upm.fi.dia.ontology.webode.ui.designer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The toolbar that controls what kind of relationships
 * are shown.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class ViewRelationTypeToolBar extends JToolBar
    implements  ItemListener
{
    private Designer designer;
    private JCheckBox jcbSubclass, jcbExhaustive, jcbDisjoint,
	jcbPartOf, jcbInPartOf, jcbAdhoc;
    
    public ViewRelationTypeToolBar (Designer designer)
    {
	setFloatable (true);
	
	this.designer = designer;

	jcbSubclass   = new JCheckBox (DesignerConstants.SUBCLASS_OFC, true);
	jcbExhaustive = new JCheckBox (DesignerConstants.EXHAUSTIVEC, true);
	jcbDisjoint   = new JCheckBox (DesignerConstants.DISJOINTC, true);
	jcbPartOf     = new JCheckBox (DesignerConstants.PART_OFC, true);
	jcbInPartOf   = new JCheckBox (DesignerConstants.IN_PART_OFC, true);
	jcbAdhoc      = new JCheckBox (DesignerConstants.ADHOCC, true);

	jcbSubclass.addItemListener (this);
	jcbExhaustive.addItemListener (this);
	jcbDisjoint.addItemListener (this);
	jcbPartOf.addItemListener (this);
	jcbInPartOf.addItemListener (this);
	jcbAdhoc.addItemListener (this);
	
	
	add (jcbSubclass);
	add (jcbExhaustive);
	add (jcbDisjoint);
	add (jcbPartOf);
	add (jcbInPartOf);
	add (jcbAdhoc);
	
    }   

    public void itemStateChanged (ItemEvent ie)
    {
	Object source = ie.getSource();
	if (ie.getStateChange () == ie.DESELECTED) {
	    if (source == jcbSubclass) {
		designer.drawSubclass (false);
	    }
	    else if (source == jcbExhaustive) {
		designer.drawExhaustive (false);
	    }
	    else if (source == jcbDisjoint) {
		designer.drawDisjoint (false);
	    }
	    else if (source == jcbPartOf) {
		designer.drawPartOf (false);
	    }
	    else if (source == jcbInPartOf) {
		designer.drawInPartOf (false);
	    }
	    else if (source == jcbAdhoc) {
		designer.drawAdhoc (false);
	    }
	    
	}
	else {
	    if (source == jcbSubclass) {
		designer.drawSubclass (true);
	    }
	    else if (source == jcbExhaustive) {
		designer.drawExhaustive (true);
	    }
	    else if (source == jcbDisjoint) {
		designer.drawDisjoint (true);
	    }
	    else if (source == jcbPartOf) {
		designer.drawPartOf (true);
	    }
	    else if (source == jcbInPartOf) {
		designer.drawInPartOf (true);
	    }
	    else if (source == jcbAdhoc) {
		designer.drawAdhoc (true);
	    }
	    
	}
    }
}
