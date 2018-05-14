package es.upm.fi.dia.ontology.webode.xml;

import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * The manager for the Import Service
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class ImportServiceManager extends MinervaManagerImp
{
    public static final String ODE_SERVICE = "ODE service";

    public ImportServiceManager () throws RemoteException {}

    public MinervaServiceDescription getServiceDescription()
    {
	return new MinervaServiceDescription
	    ("XML import service", "Service used to import ontologies in XML.",
	     new MinervaAttributeDescription[] {
		new MinervaAttributeDescription (ODE_SERVICE,
						 "The ODE service to be used for accessing ontologies.",
						 String.class,
						 null,
						 ((ImportServiceConfiguration) serviceConfig).odeService)
		    });
    }

    /**
     * This method is used if no UI has been defined over the service.
     *
     * @param attrName  Name of the configurable attribute.
     * @param attrValue The attribute's value.
     */
    public void setAttribute (String attrName, Object attrValue)
	throws NoSuchAttributeException, RemoteException
    {
	ImportServiceConfiguration odesc = (ImportServiceConfiguration) serviceConfig;

	if (attrName.equals (ODE_SERVICE)) {
	    odesc.odeService = attrValue.toString();
	}
	else
	    throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
    }
}












