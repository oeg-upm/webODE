package es.upm.fi.dia.ontology.minerva.server.management;

import java.awt.*;
import javax.swing.*;

/**
 * This class is the basis on which management UIs should be built.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.3
 */
public class MinervaUI extends JPanel
{
    /** The reference to the remote manager. */
    protected MinervaManager manager;
    
    /**
     * This method sets the reference to the remote manager.
     * <p>
     * If the widget has to update its state, this is the appropriate
     * method to do it.
     *
     * @param manager The manager.
     */
    public void setManager (MinervaManager minervaManager)
    {
	this.manager = manager;

	System.out.println ("Manager Set!");
    }

    /**
     * A convenience method to get the parent frame for this
     * component.
     *
     * @return The parent frame.
     */
    public JFrame getParentFrame ()
    {
	Component jc = this;
	while (jc != null || jc instanceof JFrame) {
	    jc = jc.getParent();
	}
	return (JFrame) jc;
    }
}
