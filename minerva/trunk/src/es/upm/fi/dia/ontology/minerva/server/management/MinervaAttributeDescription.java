package es.upm.fi.dia.ontology.minerva.server.management;

/**
 * This class provides information about a service attribute.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class MinervaAttributeDescription implements java.io.Serializable
{
    /** The attribute's name. */
    public String name;
    /** The attribute's description. */
    public String description;
    /** The class of the attribute. */
    public Class cl;
    /** The possible attribute values. */
    public Object[] values;
    /** The default or current value. */
    public Object currentValue;

    /** To flag if an attribute changed. */
    public transient boolean changed;

    /**
     * Constructs an attribute description.
     * 
     * @param name The attribute's name.
     * @param description The attribute's description.
     * @param cl  The attribute's class.
     * @param values The possible values.
     * @param currentValue Current attribute value.
     */
    public MinervaAttributeDescription (String name, String description, 
					Class cl, Object[] values,
					Object currentValue)
    {
	this.name  = name;
	this.description = description;
	this.cl = cl;
	this.values = values;
	this.currentValue = currentValue;
    }
}




