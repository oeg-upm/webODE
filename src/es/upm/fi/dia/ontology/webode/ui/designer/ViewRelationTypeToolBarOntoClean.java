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
public class ViewRelationTypeToolBarOntoClean extends JToolBar
    implements  ItemListener
{
    private Designer designer;
    private JCheckBox jcbMetaproperties,jcbEvaluation,jcbFocusOnRigid;
    
    public ViewRelationTypeToolBarOntoClean (Designer designer)
    {
	setFloatable (true);
	
	this.designer = designer;

	
	boolean valor;
	if(designer.getDrawMetaproperties ())
	 valor=true;
	else
	 valor=false; 
	jcbMetaproperties      = new JCheckBox (DesignerConstants.METAPROPERTIESC, valor);
        jcbEvaluation          = new JCheckBox (DesignerConstants.EVALUATIONC, false);
        jcbFocusOnRigid     = new JCheckBox (DesignerConstants.FOCUS_ON_RIGIDC, false);
	
	jcbMetaproperties.addItemListener (this);
	jcbEvaluation.addItemListener(this);
	jcbFocusOnRigid.addItemListener(this);
	
	//add (new JLabel (DesignerConstants.OntocleanTools));
	add (new JLabel ("OntoClean tools:   "));
	add (jcbMetaproperties);
	add (jcbEvaluation);
	add (jcbFocusOnRigid);
    }   

    public void itemStateChanged (ItemEvent ie)
    {
	Object source = ie.getSource();
	if (ie.getStateChange () == ie.DESELECTED) {
	    if (source == jcbMetaproperties) {
		designer.drawMetaproperties (false);
	    }
	    else if (source == jcbEvaluation) {
		designer.changeEvaluation (false);
	    }
	    else if (source == jcbFocusOnRigid) {
		designer.drawFocusOnRigid (false);
	    }
	}
	else {
	    
	    if (source == jcbMetaproperties) {
		designer.drawMetaproperties (true);
	    }
	    else if (source == jcbEvaluation) {
		designer.changeEvaluation (true);
	    }
	    else if (source == jcbFocusOnRigid) {
		designer.drawFocusOnRigid (true);
	    }
	}
    }
}
