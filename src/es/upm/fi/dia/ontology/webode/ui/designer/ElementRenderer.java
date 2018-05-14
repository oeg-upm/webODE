package es.upm.fi.dia.ontology.webode.ui.designer;

import java.awt.*;
import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

/**
 * The general interface for a cell within the designer.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.2
 */
public interface ElementRenderer 
{
    /**
     * Returns the renderer for the element.
     *
     * @param Designer The designer the element is for.
     * @param value The value of the element.
     * @param selected If true, the element is selected.
     */
    void render (Designer designer,
		 Element value, 
		 boolean selected,
		 Graphics g, String values,boolean drawMetaproperties,boolean bFocusOnNonRigid, boolean is_anti_rigid);

    Rectangle getRectangle (Component designer,
			    Element value);
    
    /**
     * Returns whether a point is within this element.
     *
     * @param designer The designer.
     * @param value The element to check.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return true or false.
     */
    boolean isWithin (Designer designer, Element value, int x, int y);
}

