package es.upm.fi.dia.ontology.webode.ui.designer;

import java.awt.*;
import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

public class DefaultRelationRenderer implements RelationRenderer
{
    public static final int SIZE = 15;
    public static final int DIST = 50*50;
    public static final int DIST1 = 45*45;

    public static final int ARROW_LENGTH = 15;
    public static final double ANGLE     = Math.PI * 15 / 180;

    /**
     * Renders the relation.
     *
     */
    public void render (Designer designer, Relation rel, boolean selected, Graphics g,
                        boolean ba,boolean bb, boolean bc)
    {
	DesignModel designModel = designer.getDesignModel();
	Element eOr = designModel.getElement (rel.getOrigin());
	Element eDes = designModel.getElement (rel.getDestination());
	
	//Quitar los comentarios de las dos lineas siguientes para que esta relación no
	//aparezca al hacer Focus On Rigid.
	/*if(rel.isSubclassOf() && ba && bb)
	  return;*/
	// Draw a line 
	FontMetrics fm = designer.getFontMetrics (designer.getFont());

	int height = fm.getMaxAscent() + fm.getMaxDescent() + 10;

	int widthOr = Math.max (DefaultDesignRenderer.WIDTH, fm.stringWidth (eOr.getName()) + 10) / 2;
	int widthDes = Math.max (DefaultDesignRenderer.WIDTH, fm.stringWidth (eDes.getName()) + 10) / 2;
	
	int x1, x2, y1, y2, x2aux = 0, y2aux = 0;
	int eOrx = eOr.getX();
	int eOry = eOr.getY();
	int eDesx = eDes.getX();
	int eDesy = eDes.getY();

       
	if (selected)
	    g.setColor (Color.blue);
	else {
	    if (rel.isSubclassOf())
	    {
	      if(bc)
	       g.setColor (Color.black);
	      else if(ba && bb)
	       g.setColor (Color.lightGray);
	      else	
		g.setColor (Color.green);
            }		
	    else 
		g.setColor (Color.red);
	}

	x1 = eOrx + widthOr; y1 = eOry + height / 2;
	x2 = eDesx + widthDes; y2 = eDesy + height / 2;
	
	double foo1, foo2, foo12, foo22;
	foo1 = x2 - x1;
	foo2 = y2 - y1;
	foo12 = foo1 * foo1;
	foo22 = foo2 * foo2;
	double factor,/* sol1, sol2,*/ a, b, c, factor2;
	a = foo12 + foo22;
	b = -2 * (foo12 + foo22);
	c = foo12 + foo22 - DIST;
	//sol1 = (-b + Math.sqrt (b * b - 4 * a * c)) / (2 * a);
	factor = (-b - Math.sqrt (b * b - 4 * a * c)) / (2 * a);
	c = foo12 + foo22 - DIST1;
	factor2 = (-b - Math.sqrt (b * b - 4 * a * c)) / (2 * a);

	if (rel.getOrigin().equals (rel.getDestination())) {
	    x1 -= 2 * SIZE;
	    x2 += 2 * SIZE;
	    y1 += 3 * SIZE / 4;
	    y2 += 3 * SIZE / 4;
	}
	else {
	    // Separate a little
	    x2 = (int) (x1 + factor * (foo1));
	    y2 = (int) (y1 + factor * (foo2));

	    x2aux = (int) (x1 + factor2 * (foo1));
	    y2aux = (int) (y1 + factor2 * (foo2));
	}

	/*	if (x2aux != 0 && y2aux != 0) {
	    _drawArrow (g, x2, y2, x2aux, y2aux);
	    }*/
	
	// Middle point
	int bendx, bendy;
	if ((bendx = rel.getBendX()) < 0) {
	    bendx = (x1 + x2) / 2;
	    if (rel.getOrigin().equals (rel.getDestination())) {
		bendy = y1 + SIZE * 3;
		rel.setBendX (bendx);
		rel.setBendY (bendy);
	    }
	    else 
		bendy = (y1 + y2) / 2;
	}
	else {
	    bendy = rel.getBendY();
	}

    

	g.drawLine (x1, y1, bendx, bendy);
	g.drawLine (bendx, bendy, x2, y2);
	_drawArrow (g, x2, y2, bendx, bendy);
	
	/*g.drawLine (x1 = eOrx + widthOr, y1 = eOry + height / 2, 
	  x2 = eDesx + widthDes, y2 = eDesy + height / 2);*/
	if (selected)
	    g.setColor (Color.white);
	else
	    g.setColor (Color.darkGray);
	int w = fm.stringWidth(rel.getName());
	g.drawString (rel.getName(), bendx - w / 2, bendy + 5);
    }
     
    public void render (Designer designer, Relation rel, boolean selected, Graphics g)
    {
	DesignModel designModel = designer.getDesignModel();
	Element eOr = designModel.getElement (rel.getOrigin());
	Element eDes = designModel.getElement (rel.getDestination());
	
	// Draw a line 
	FontMetrics fm = designer.getFontMetrics (designer.getFont());

	int height = fm.getMaxAscent() + fm.getMaxDescent() + 10;

	int widthOr = Math.max (DefaultDesignRenderer.WIDTH, fm.stringWidth (eOr.getName()) + 10) / 2;
	int widthDes = Math.max (DefaultDesignRenderer.WIDTH, fm.stringWidth (eDes.getName()) + 10) / 2;
	
	int x1, x2, y1, y2, x2aux = 0, y2aux = 0;
	int eOrx = eOr.getX();
	int eOry = eOr.getY();
	int eDesx = eDes.getX();
	int eDesy = eDes.getY();

       
	if (selected)
	    g.setColor (Color.blue);
	else {
	    if (rel.isSubclassOf())
		g.setColor (Color.green);
	    else 
		g.setColor (Color.red);
	}

	x1 = eOrx + widthOr; y1 = eOry + height / 2;
	x2 = eDesx + widthDes; y2 = eDesy + height / 2;
	
	double foo1, foo2, foo12, foo22;
	foo1 = x2 - x1;
	foo2 = y2 - y1;
	foo12 = foo1 * foo1;
	foo22 = foo2 * foo2;
	double factor,/* sol1, sol2,*/ a, b, c, factor2;
	a = foo12 + foo22;
	b = -2 * (foo12 + foo22);
	c = foo12 + foo22 - DIST;
	//sol1 = (-b + Math.sqrt (b * b - 4 * a * c)) / (2 * a);
	factor = (-b - Math.sqrt (b * b - 4 * a * c)) / (2 * a);
	c = foo12 + foo22 - DIST1;
	factor2 = (-b - Math.sqrt (b * b - 4 * a * c)) / (2 * a);

	if (rel.getOrigin().equals (rel.getDestination())) {
	    x1 -= 2 * SIZE;
	    x2 += 2 * SIZE;
	    y1 += 3 * SIZE / 4;
	    y2 += 3 * SIZE / 4;
	}
	else {
	    // Separate a little
	    x2 = (int) (x1 + factor * (foo1));
	    y2 = (int) (y1 + factor * (foo2));

	    x2aux = (int) (x1 + factor2 * (foo1));
	    y2aux = (int) (y1 + factor2 * (foo2));
	}

	/*	if (x2aux != 0 && y2aux != 0) {
	    _drawArrow (g, x2, y2, x2aux, y2aux);
	    }*/
	
	// Middle point
	int bendx, bendy;
	if ((bendx = rel.getBendX()) < 0) {
	    bendx = (x1 + x2) / 2;
	    if (rel.getOrigin().equals (rel.getDestination())) {
		bendy = y1 + SIZE * 3;
		rel.setBendX (bendx);
		rel.setBendY (bendy);
	    }
	    else 
		bendy = (y1 + y2) / 2;
	}
	else {
	    bendy = rel.getBendY();
	}

    

	g.drawLine (x1, y1, bendx, bendy);
	g.drawLine (bendx, bendy, x2, y2);
	_drawArrow (g, x2, y2, bendx, bendy);
	
	/*g.drawLine (x1 = eOrx + widthOr, y1 = eOry + height / 2, 
	  x2 = eDesx + widthDes, y2 = eDesy + height / 2);*/
	if (selected)
	    g.setColor (Color.white);
	else
	    g.setColor (Color.darkGray);
	int w = fm.stringWidth(rel.getName());
	g.drawString (rel.getName(), bendx - w / 2, bendy + 5);
    }

    public void render (Designer designer, Relation rel, int destX, int destY, int widthOr, Graphics g)
    {
	DesignModel designModel = designer.getDesignModel();
	Element eDes = designModel.getElement (rel.getDestination());
	
	// Draw a line 
	FontMetrics fm = designer.getFontMetrics (designer.getFont());

	int height = fm.getMaxAscent() + fm.getMaxDescent() + 10;

	int widthDes = Math.max (DefaultDesignRenderer.WIDTH, fm.stringWidth (eDes.getName()) + 10) / 2;
	
	int x1, x2, y1, y2, x2aux = 0, y2aux = 0;
	int eOrx = destX;
	int eOry = destY;
	int eDesx = eDes.getX();
	int eDesy = eDes.getY();
       
	if (rel.isSubclassOf())
	    g.setColor (Color.green);
	else 
	    g.setColor (Color.red);

	x1 = eOrx + widthOr; y1 = eOry - 6;
	x2 = eDesx + widthDes; y2 = eDesy + height / 2;
	
	double foo1, foo2, foo12, foo22;
	foo1 = x2 - x1;
	foo2 = y2 - y1;
	foo12 = foo1 * foo1;
	foo22 = foo2 * foo2;
	double factor,/* sol1, sol2,*/ a, b, c, factor2;
	a = foo12 + foo22;
	b = -2 * (foo12 + foo22);
	c = foo12 + foo22 - DIST;
	//sol1 = (-b + Math.sqrt (b * b - 4 * a * c)) / (2 * a);
	factor = (-b - Math.sqrt (b * b - 4 * a * c)) / (2 * a);
	c = foo12 + foo22 - DIST1;
	factor2 = (-b - Math.sqrt (b * b - 4 * a * c)) / (2 * a);

	if (rel.getOrigin().equals (rel.getDestination())) {
	    x1 -= 2 * SIZE;
	    x2 += 2 * SIZE;
	    y1 += 3 * SIZE / 4;
	    y2 += 3 * SIZE / 4;
	}
	else {
	    // Separate a little
	    x2 = (int) (x1 + factor * (foo1));
	    y2 = (int) (y1 + factor * (foo2));

	    x2aux = (int) (x1 + factor2 * (foo1));
	    y2aux = (int) (y1 + factor2 * (foo2));
	}

	/*	if (x2aux != 0 && y2aux != 0) {
	    _drawArrow (g, x2, y2, x2aux, y2aux);
	    }*/
	
	// Middle point
	int bendx, bendy;
	if ((bendx = rel.getBendX()) < 0) {
	    bendx = (x1 + x2) / 2;
	    if (rel.getOrigin().equals (rel.getDestination())) {
		bendy = y1 + SIZE * 3;
		rel.setBendX (bendx);
		rel.setBendY (bendy);
	    }
	    else 
		bendy = (y1 + y2) / 2;
	}
	else {
	    bendy = rel.getBendY();
	}

    

	g.drawLine (x1, y1, bendx, bendy);
	g.drawLine (bendx, bendy, x2, y2);
	_drawArrow (g, x2, y2, bendx, bendy);
	
	/*g.drawLine (x1 = eOrx + widthOr, y1 = eOry + height / 2, 
	  x2 = eDesx + widthDes, y2 = eDesy + height / 2);*/
	
	g.setColor (Color.darkGray);
	int w = fm.stringWidth(rel.getName());
	g.drawString (rel.getName(), bendx - w / 2, bendy + 5);
    }


	
    /**
     * Draws an arrow.
     */
    private void _drawArrow (Graphics g, int x2, int y2, int x2aux, int y2aux)
    {
	double teta;
	if (y2 > y2aux)
	    teta = Math.atan (((double) (x2 - x2aux))  / (double) (y2 - y2aux));
	else
	    teta = Math.atan (((double) (x2 - x2aux))  / (double) (-y2 + y2aux));
	double teta1 = teta - ANGLE;
	double teta2 = teta + ANGLE;

	// Some maths...
	int x3, y3, x4, y4;
	x3 = (int) (x2 - ARROW_LENGTH * Math.sin (teta1) + .5);
	x4 = (int) (x2 - ARROW_LENGTH * Math.sin (teta2) + .5);
	if (y2 > y2aux) {
	    y3 = (int) (y2 - ARROW_LENGTH * Math.cos (teta1) + .5);
	    y4 = (int) (y2 - ARROW_LENGTH * Math.cos (teta2) + .5);
	}
	else {
	    y3 = (int) (y2 + ARROW_LENGTH * Math.cos (teta1) + .5);
	    y4 = (int) (y2 + ARROW_LENGTH * Math.cos (teta2) + .5);
	}
	
	g.fillPolygon (new int[] {x2, x3, x4}, new int[] {y2, y3, y4}, 3);
    }

    /**
     * Is it inside?
     */
    public boolean isWithin (Designer designer, Relation rel, int x, int y)
    {
	FontMetrics fm = designer.getFontMetrics (designer.getFont());
	int height = (fm.getMaxAscent() + fm.getMaxDescent()) / 2 + 5;
	int height1 = (fm.getMaxAscent() + fm.getMaxDescent()) / 2;
	
	DesignModel designModel = designer.getDesignModel();
	Element eOr = designModel.getElement (rel.getOrigin());
	// A Group?
	if (eOr == null) return false;

	Element eDes = designModel.getElement (rel.getDestination());
	
	int relWidth = fm.stringWidth (rel.getName()) / 2;
		
	// Middle point
	int bendx, bendy;
	if ((bendx = rel.getBendX()) < 0) {
	    int widthOr = (fm.stringWidth (eOr.getName()) + 30) / 2;
	    int widthDes = (fm.stringWidth (eDes.getName()) + 30) / 2;
	    int eOrx = eOr.getX();
	    int eOry = eOr.getY();
	    int eDesx = eDes.getX();
	    int eDesy = eDes.getY();
	    
	    int x1, x2, y1, y2;
	    x1 = eOrx + widthOr; y1 = eOry + height / 2;
	    x2 = eDesx + widthDes; y2 = eDesy + height / 2; 
	    bendx = (x1 + x2) / 2;
	    bendy = (y1 + y2) / 2;
	}
	else {
	    bendy = rel.getBendY();
	}

	Rectangle pol = new Rectangle (bendx - relWidth, bendy - height1, bendx + relWidth, bendy + height1);
	
	return pol.contains (x, y);
    }
}


