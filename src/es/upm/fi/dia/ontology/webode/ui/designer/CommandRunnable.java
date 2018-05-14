package es.upm.fi.dia.ontology.webode.ui.designer;

/**
 * Interface that defines the life-cycle of something
 * to be executed after a communication with the server.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class CommandRunnable implements Runnable
{
    /** The command descriptor. */
    protected CommandDescriptor cd;
	
    /**
     * Sets the command descriptor before executing
     * the run method.
     *
     * @param cd the command descriptor.
     */
    public void setCommandDescriptor (CommandDescriptor cd)
    {
	this.cd = cd;
    }

    public void run ()
    {
    }
}
