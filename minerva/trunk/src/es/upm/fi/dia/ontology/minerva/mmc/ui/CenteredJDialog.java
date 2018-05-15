package es.upm.fi.dia.ontology.minerva.mmc.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * A base class for dialogs centered on screen.
 * 
 * @author  Julio César Arpírez Vega.
 * @version 0.2
 */
public class CenteredJDialog extends JDialog
{
    /**
     * Creates a new dialog centered on the screen.
     *
     * @param jf The parent window.
     * @param title The dialog's title.
     * @param model Whether the dialog is modal or not.
     */
    public CenteredJDialog (JFrame jf, String title, boolean modal)
    {
	super (jf, title, modal);
    }

    /**
     * Sets the dialog visible.
     *
     * @param b Is the dialog visible?
     */
    public void setVisible (boolean b)
    {
	if (b) {
	    Toolkit tk = Toolkit.getDefaultToolkit();

	    Dimension sdim = tk.getScreenSize();
	    Dimension dim  = getSize();

	    setLocation ((sdim.width - dim.width) / 2, (sdim.height - dim.height) / 2);
	}
	
	super.setVisible (b);
    }
}
