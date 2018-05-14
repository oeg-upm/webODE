package es.upm.fi.dia.ontology.webode.service;

/**
 * The key to look-up instance attributes values.
 */
public class InstanceKey implements java.io.Serializable
{
    public String termName, attributeName;
    
    public InstanceKey (String termName, String attributeName)
    {
	this.termName      = termName;
	this.attributeName = attributeName;
    }

    public int hashCode ()
    {
	return termName.hashCode() + attributeName.hashCode();
    }

    public boolean equals (Object o)
    {
	if (o instanceof InstanceKey) {
	    InstanceKey ik = (InstanceKey) o;
	    return ik.termName.equals (termName) && ik.attributeName.equals (attributeName);
	}
	return false;
    }
}
