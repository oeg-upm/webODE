package es.upm.fi.dia.ontology.webode.ui.designer.model;

/**
 * Class that describes an element.
 *
 * @author   Julio César Arpírez Vega
 * @version  1.0
 */
public class Element implements java.io.Serializable, Cloneable
{
    private String name;
    private int x, y;
    private transient boolean isNew, dirty;
    private transient boolean selected;

    public Element (String name)
    {
	this (name,-1, -1);
    }
 
    public Element (String name,int x,int y)
    {
	this.name = name;
	this.x = x;
	this.y = y;  
     }
    
    /*public Element (String name, String values, int x, int y)
    {
	this.name = name;
	this.values = values;
	this.x = x;
	this.y = y;
    }*/

    protected Object clone()
    {
	Element el = new Element (name,x, y);
	el.setNew (isNew);
	el.setDirty (dirty);

	return el;
    }

    public String toString ()
    {
	return "Element: '" + name + "'.  Position: (" + x + ", " + y + ")";
    }

    public void setNew (boolean isNew)
    {
	this.isNew = isNew;
    }

    public boolean isNew ()
    {
	return isNew;
    }
    
    public boolean isDirty ()
    {
	return dirty;
    }

    public void setDirty (boolean b)
    {
	dirty = b;
    }

    public void setX (int newX)
    {
	dirty = true;
	this.x = newX;
    }

    public void setY (int newY)
    {
	dirty = true;
	this.y = newY;
    }

    public int getX ()
    {
	return x;
    }
    
    public int getY ()
    {
	return y;
    }

    public void setName (String newName)
    {
	dirty = true;
	this.name = newName;
    }

    public String getName ()
    {
	return name;
    }
    
    /*public void setValues (String newValues)
    {
	dirty = true;
	this.values = newValues;
    }

    public String getValues ()
    {
	return values;
    }*/

    public void setSelected (boolean b)
    {
	this.selected = b;
    }

    public boolean isSelected ()
    {
	return selected;
    }

    public int hashCode ()
    {
	return name.hashCode();
    }

    public boolean equals (Object o)
    {
	if (o instanceof Element) {
	    Element el = (Element) o;
	    return el.name.equals(name);
	}
	return false;
    }
}
