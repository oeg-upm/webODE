package es.upm.fi.dia.ontology.minerva.server.management;

/**
 * This class provides management information about a service.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class MinervaServiceDescription implements java.io.Serializable
{
    /**
     * Name for the service.
     */
    public String name;
    /**
     * Description for the service attributes.
     */
    public MinervaAttributeDescription[] mad;
    /**
     * Textual description for the service.
     */
    public String description;

    /**
     * Builds a service description based on its attributes.
     *
     * @param name The service name.
     * @param mad An array of descriptions on attributes.
     * @param description A description for the service.
     */
    public MinervaServiceDescription (String name, String description, MinervaAttributeDescription[] mad)
    {
	this.name = name;
	this.mad = mad;
	this.description = description;
    }    
}
