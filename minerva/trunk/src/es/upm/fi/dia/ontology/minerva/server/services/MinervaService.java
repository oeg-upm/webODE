package es.upm.fi.dia.ontology.minerva.server.services;

// Java stuff
import java.rmi.*;

// Own stuff
import es.upm.fi.dia.ontology.minerva.server.management.MinervaManager;
import es.upm.fi.dia.ontology.minerva.server.rmi.*;

/**
 * This interface defines the life-cycle methods to be provided by
 * a Minerva service.
 * <p>
 * Service providers should inherit their interfaces from this one
 * and then write a class inheriting from MinervaServiceImp that implements
 * the provider's interface.
 *
 * @author   Julio César Arpírez Vega
 * @version  1.0
 */
public interface MinervaService extends Disconnect {
    /**
     * Obtains a reference to the service manager.
     *
     * @return A reference to a service manager.
     * @exception NoManagerProvidedException In case of no management be required.
     */
    //MinervaManager getManager () throws NoManagerProvidedException, RemoteException;

    public boolean isStatefull() throws RemoteException;
}











