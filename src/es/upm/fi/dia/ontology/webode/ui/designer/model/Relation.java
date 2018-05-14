package es.upm.fi.dia.ontology.webode.ui.designer.model;

/**
 * Class that describes a relationship
 *
 * @author   Julio César Arpírez Vega
 * @version  1.0
 */
public class Relation implements java.io.Serializable, Cloneable
{
    public static final String SUBCLASS_OF = "Subclass-of";
    public static final String EXHAUSTIVE  = "Exhaustive";
    public static final String DISJOINT    = "Disjoint";
    public static final String PART_OF     = "Transitive-Part-of";
    public static final String IN_PART_OF  = "Intransitive-Part-of";

    private String name, origin, destination;
    private transient boolean selected;
    private transient boolean isNew, dirty;
    private int bendX = -1, bendY = -1;
    private int maxCardinality;

    public Relation (String name, String origin, String destination)
    {
	this.name = name;
	this.origin = origin;
	this.destination = destination;
    }

    public Relation (String name, String origin, String destination, int card)
    {
	this.name = name;
	this.origin = origin;
	this.destination = destination;
	maxCardinality = card;
    }

    public Relation (String name, String origin, String destination, int bendX, int bendY)
    {
	this.name = name;
	this.origin = origin;
	this.destination = destination;
	this.bendX = bendX;
	this.bendY = bendY;
    }

    public Object clone()
    {
	Relation rel = new Relation (name, origin, destination);
	rel.setDirty (dirty);
	rel.setNew (isNew);

	return rel;
    }

    public boolean equals (Object o)
    {
	if (o instanceof Relation) {
	    Relation rel = (Relation) o;
	    return name.equals (rel.name) && origin.equals (rel.origin) && destination.equals (rel.destination);
	}
	return false;
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

    public String toString()
    {
	return "Relation: '" + name + "' from '" + origin + "' to '" + destination + "'.";
    }

    public boolean isSubclassOf ()
    {
	return name.equals (SUBCLASS_OF);
    }

    public String getName ()
    {
	return name;
    }

    public String getOrigin ()
    {
	return origin;
    }

    public String getDestination ()
    {
	return destination;
    }

    public void setName (String name)
    {
	dirty = true;
	this.name = name;
    }

    public void setOrigin (String origin)
    {
	dirty = true;
	this.origin = origin;
    }

    public void setDestination (String destination)
    {
	dirty = true;
	this.destination = destination;
    }

    public void setSelected (boolean selected)
    {
	this.selected = selected;
    }

    public boolean getSelected ()
    {
	return selected;
    }

    public int getBendX()
    {
	return bendX;
    }

    public int getBendY()
    {
	return bendY;
    }

    public void setBendX(int bendX)
    {
	dirty = true;
	this.bendX = bendX;
    }

    public void setBendY (int bendY)
    {
	dirty = true;
	this.bendY = bendY;
    }

    public int getCardinality ()
    {
	return maxCardinality;
    }

    public void setCardinality (int c)
    {
	maxCardinality = c;
    }
}
