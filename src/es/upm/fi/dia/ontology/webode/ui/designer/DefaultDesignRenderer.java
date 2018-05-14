package es.upm.fi.dia.ontology.webode.ui.designer;

import java.awt.*;
import javax.swing.*;
import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

public class DefaultDesignRenderer implements ElementRenderer
{
    public static final int WIDTH = 70;
    public static final int HEIGHT = 30;
    

    /**
     * Returns the renderer for the element.
     *
     * @param Designer The designer the element is for.
     * @param value The value of the element.
     * @param selected If true, the element is selected.
     */
    public void render (Designer designer,
			Element value, 
			boolean selected,
			Graphics g, String values,boolean drawMetaproperties,
			boolean bFocusOnNonRigid, boolean is_anti_rigid)
    {
    	//Quitar los comentarios de las dos lineas siguientes para que este elemento no
	//aparezca al hacer Focus On Rigid.
    	/*if(bFocusOnNonRigid && is_anti_rigid)
    	 return;*/
	FontMetrics fm = designer.getFontMetrics (designer.getFont());
	int jander1 = fm.stringWidth (value.getName());
	int jander;
	if(values != null)
	{
          if(values.length() >= value.getName().length())
            jander = fm.stringWidth (values);
          else
            jander = fm.stringWidth (value.getName());
        }
        else    
          jander = fm.stringWidth (value.getName());
	int width = Math.max (jander + 10, WIDTH);
	int height;
	if(drawMetaproperties)
	   height = Math.max (fm.getMaxAscent() + fm.getMaxDescent() + 40, HEIGHT);
	else   
	   height = Math.max (fm.getMaxAscent() + fm.getMaxDescent() + 10, HEIGHT);

	int x, y;
	x = value.getX();
	y = value.getY();
	if (values == null)
	  values = " ";
	if (value.isSelected()) {
	    g.setColor (Color.blue);
	    g.fillRect (x, y, width, height);
	}
	else {
	    g.setColor (Color.white);
	    g.fillRect (x, y, width, height);
	}
	if(bFocusOnNonRigid && is_anti_rigid)
	  g.setColor (Color.gray);
	else  
	  g.setColor (Color.black);  
	g.drawRect (x, y, width, height);
	if(bFocusOnNonRigid && is_anti_rigid)
	  g.setColor (Color.lightGray);
	else  
	  g.setColor (Color.black);  
	if(drawMetaproperties)
	  g.drawLine (x, y + height / 2 + 15,x+width,y + height / 2 + 15);
	if(bFocusOnNonRigid && is_anti_rigid)
	 g.setColor (value.isSelected() ? Color.white : Color.gray);
	else 
	 g.setColor (value.isSelected() ? Color.white : Color.blue);
	g.drawString (value.getName(), x + (width - jander1) / 2, y + height / 2 + 5);
	if(drawMetaproperties)
	  g.drawString (values, x + (width - jander) / 2, y + height / 2 + 25); 
    }

    /**
     * Gets element's bounding rectangle.
     */
    public Rectangle getRectangle (Component designer,
				   Element value)
    {
	return getRectangleStatic (designer, value);
    }

    /**
     * Gets element's bounding rectangle.
     */
    public static Rectangle getRectangleStatic (Component designer,
					 Element value)
    {
 	FontMetrics fm = designer.getFontMetrics (designer.getFont());
	int jander = fm.stringWidth (value.getName());
	int width = Math.max (jander + 10, WIDTH);
	int height = Math.max (fm.getMaxAscent() + fm.getMaxDescent() + 40, HEIGHT);

	return new Rectangle (value.getX(), value.getY(), width, height);
    }

    
    /**
     * Returns whether a point is within this element.
     *
     * @param designer The designer.
     * @param value The element to check.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return true or false.
     */
    public boolean isWithin (Designer designer, Element value, int x, int y)    
    {
	FontMetrics fm = designer.getFontMetrics (designer.getFont());
	int jander = fm.stringWidth (value.getName());
	int width = Math.max (jander + 10, WIDTH);
	int height = Math.max (fm.getMaxAscent() + fm.getMaxDescent() + 40, HEIGHT);
	Rectangle rect = new Rectangle (value.getX(), value.getY(), width, height);
	return rect.contains (x, y);	
    }
}








