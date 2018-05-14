package es.upm.fi.dia.ontology.webode.applet.clipboard;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;
import java.net.*;

/**
 * This class implements an applet to provide a clipboard within
 * WebODE.
 * <p>
 * In the next releases, it will have the ability to communicate
 * with the platform's native clipboard.
 *
 * @author   Julio César Arpírez Vega.
 * @version  0.2
 */
public class ClipboardApplet extends JApplet
{
    /** 
     * The maximum number of elements the clipboard can hold at
     * a given time.
     */
    public static final String ELEMENTS = "elements";

    /** The default number of elements. */
    public static final int DFT_ELEMENTS = 4;
    
    /** An array of vectors where the elements will be placed. */
    private Vector[] aVector;

    private Color colorSelected1, colorSelected2;

    private JLabel previousSelected;
    private int selectedIndex;
    private JLabel[] ajlb;
    private int[] clipboardTypes;
    private String[] texts;
    
    private Icon engagedIcon, emptyIcon;

    /**
     * Inits the applet by creating the clipboard internally.
     */
    public void init ()
    {
	int foo = DFT_ELEMENTS;
	try {
	    foo = Integer.parseInt (getParameter (ELEMENTS));
	    if (foo <= 0)
		foo = DFT_ELEMENTS;
	} catch (Exception e) {}

	aVector = new Vector[foo];
	for (int i = 0; i < foo; i++)
	    aVector[i] = new Vector();

	// Some colors.
	colorSelected1 = new Color (255, 45, 0);
	colorSelected2 = colorSelected1.darker();


	try {
	    // Load icon
	    System.out.println ("Loading image from: " +  new URL (getCodeBase() + "../images/clipboard.gif"));
	    engagedIcon = new ImageIcon (new URL (getCodeBase() + "../images/clipboard.jpg"));
	    emptyIcon = new ImageIcon (new URL (getCodeBase() + "../images/clipboard-white.gif"));
	} catch (Exception e) {
	    System.err.println ("Error loading image: " + e);
	}

	// Sets the layout
	Container cont = getContentPane();
	cont.setLayout (new GridLayout (1, foo, 0, 0));
	ajlb = new JLabel[foo];
	texts = new String[foo];
	for (int i = 0; i < foo; i++) {
	    JLabel jlb;
	    cont.add (jlb = new JLabel (engagedIcon, JLabel.CENTER));
	    ajlb[i] = jlb;
	    jlb.addMouseListener (new MouseControl (i));
	    if (i == 0) {
		jlb.setBorder (BorderFactory.createBevelBorder 
			       (BevelBorder.RAISED, colorSelected1, colorSelected2));
		previousSelected = jlb;
		selectedIndex = 0;
	    }
	}

	clipboardTypes = new int[foo];
    }

    private class MouseControl extends MouseAdapter
    {
	int index;

	public MouseControl (int index)
	{
	    this.index = index;
	}

	public void mousePressed(MouseEvent me) 
	{
	    // Unset previous selection
	    previousSelected.setBorder (BorderFactory.createEmptyBorder());

	    previousSelected = (JLabel) me.getComponent();
	    previousSelected.setBorder (BorderFactory.createBevelBorder 
					(BevelBorder.RAISED, colorSelected1, colorSelected2));
	    selectedIndex = index;
	}

	public void mouseEntered (MouseEvent me) 
	{
	    getAppletContext().showStatus ("Content: " + 
					   (texts[index] == null ? "<empty>" : texts[index]));
	}

	public void mouseExited (MouseEvent me) 
	{
	    getAppletContext().showStatus ("");
	}
    }	    

    /**
     * Sets the text for the selected slot.
     */
    public void setSlotText (String text)
    {
	texts[selectedIndex] = text;
	ajlb[selectedIndex].setIcon (engagedIcon);	
    }

    /**
     * Sets the type of current slot.
     */ 
    public void setType (int type)
    {
	clipboardTypes[selectedIndex] = type;
    }

    public int getType ()
    {
	return 	clipboardTypes[selectedIndex];
    }
    
    /**
     * Fills selected position with something.
     * It adds the value to the vector.
     */
    public void addCurrent (String parameter, String value)
    {
	System.out.println ("addCurrent " + parameter + "-" + value + "--" + ajlb + "--" + ajlb[selectedIndex]);
	/*if (aVector[selectedIndex].isEmpty())
	  ajlb[selectedIndex].setText (value);*/
	aVector[selectedIndex].addElement (new Object[] {parameter, value});
    }
    
    /**
     * Deletes current position.
     */
    public void deleteCurrent ()
    {
	System.out.println ("deleteCurrent");
	aVector[selectedIndex].clear();
    }

    /**
     * Gets the i-th element in the selected element.
     * If no one is found, <tt>null</tt> is returned.
     */
    public String getElementAt (int i)
    {
	System.out.println ("getElementAt");
	if (i >= aVector[selectedIndex].size())
	    return null;
	return (String) ((Object[]) aVector[selectedIndex].elementAt (i))[1];
    }
    
    public String getKeyAt (int i)
    {
	System.out.println ("getKeyAt");
	if (i >= aVector[selectedIndex].size())
	    return null;
	return (String) ((Object[]) aVector[selectedIndex].elementAt (i))[0];
    }
}

