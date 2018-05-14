package es.upm.fi.dia.ontology.webode.notification;

import es.upm.fi.dia.ontology.minerva.server.services.MinervaService;
import java.rmi.RemoteException;

/**
 * <p>Title: Publish Service</p>
 * <p>Description: The service to send message to a topic</p>
 * <p>Company: UPM</p>
 * @author Angel López-Cima
 * @version 1.0
 */

public interface PublishService extends MinervaService {

  /**
   * Get the topics of the message server
   * @return A list of topics
   */
  public String[] getTopics() throws RemoteException;

  /**
   * Send a message to the message server to deliver to the corresponding subscribers
   * @param msg The message to be delivered. This message must be a text message.
   * @param topic The topic of the message
   * @throws PublishException It's thrown when the topic doesn't exist.
   */
  public void sendMessage(java.io.Serializable msg, String topic) throws RemoteException, PublishException;
}