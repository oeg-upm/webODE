package es.upm.fi.dia.ontology.webode.ui.designer;

/**
 * The class to transmit orders and results between the client
 * designer and the server.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class CommandDescriptor implements java.io.Serializable
{
    /** 
     * The command code as established in <tt>Command</tt>.
     *
     * @see es.upm.fi.dia.ontology.webode..ui.designer.Command
     */
    public int command;

    /**
     * The command parameters. 
     */
    public Object parameters;

    /**
     * The data object.
     */
    public Object data;

    public CommandDescriptor (int command, Object parameters, Object data)
    {
	this.command    = command;
	this.parameters = parameters;
	this.data       = data;
    }
    
    public String toString ()
    {
	return "COMMAND: " + command + "\nPARAMETERS: " + parameters + "\nDATA: " + data;
    }
}
