package es.upm.fi.dia.ontology.minerva.server.rmi;

/**
 * A covenience class to communicate connection state.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class Flag implements java.io.Serializable
{
    private boolean b;

    public Flag (boolean b)
    {
	this.b = b;
    }

    public Flag ()
    {
	b = false;
    }

    public void set()
    {
	b = true;
    }

    public boolean isSet ()
    {
	return b;
    }
}
