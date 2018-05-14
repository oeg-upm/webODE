package es.upm.fi.dia.ontology.webode.ui.designer;

import java.awt.*;
import javax.swing.*;
import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

public class TriangleRenderer implements ElementRenderer
{
    public static final int SIZE = 10;

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
	FontMetrics fm = designer.getFontMetrics (designer.getFont());
	int width = fm.stringWidth (value.getName()) + 10;
	int height = fm.getMaxAscent() + fm.getMaxDescent() + 10;
	int[] ax = new int[] {
	    value.getX() - SIZE, value.getX() + width / 2 , value.getX() + width + SIZE };
	int[] ay = new int[] {
	    value.getY() + height + SIZE, value.getY(), value.getY() + height + SIZE };
	
	if (selected) {
	    g.setColor (Color.blue);
	}
	else {
	    g.setColor (Color.white);
	}
	g.fillPolygon (ax, ay, 3);
	g.setColor (Color.black);
	g.drawPolygon (ax, ay, 3);
	g.setColor (selected ? Color.white : Color.blue);
	g.drawString (value.getName(), value.getX() + 5, value.getY() + height / 2 + 10 + SIZE);
    }

    public Rectangle getRectangle (Component designer,
				   Element value)
    {
	return null;
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
	int width = fm.stringWidth (value.getName()) + 10;
	int height = fm.getMaxAscent() + fm.getMaxDescent() + 10;
	int[] ax = new int[] {
	    value.getX() - SIZE, value.getX() + width / 2 , value.getX() + width + SIZE };
	int[] ay = new int[] {
	    value.getY() + height + SIZE, value.getY(), value.getY() + height + SIZE };

	Polygon pol = new Polygon (ax, ay, 3);
	return pol.contains (x, y);
    }
}
