package es.upm.fi.dia.ontology.webode.notification;

import es.upm.fi.dia.ontology.minerva.server.services.MinervaService;
import java.rmi.RemoteException;

/**
 * <p>Title: Notification Service</p>
 * <p>Description: The service to create susbcribers</p>
 * <p>Company: UPM</p>
 * @author Angel Lopez-Cima
 * @version 1.0
 */

public interface NotificationService extends MinervaService {

  /**
   * Get the topics of the message server
   * @return A list of topics
   */
  public String[] getTopics() throws RemoteException;

  /**
   * Get the host of the message server
   * @return The message server
   * @throws RemoteException
   */
  public String getHost() throws RemoteException;

  /**
   * Get the Topic Connection Factory. By default, the Topic Connection Factory is in the J2EE libraries.
   * @return The Topic Connection Factory.
   * @throws RemoteException
   */
  public String getTopicConnectionFactory() throws RemoteException;
}