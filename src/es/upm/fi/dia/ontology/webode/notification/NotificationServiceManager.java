package es.upm.fi.dia.ontology.webode.notification;

import es.upm.fi.dia.ontology.minerva.server.management.*;
import java.rmi.RemoteException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class NotificationServiceManager extends MinervaManagerImp {
  public static final String HOST    = "JMS Server";
  public static final String J2EE_HOME = "J2EE_HOME";
  public static final String TOPIC_CONNECTION_FACTORY = "Topic Connection Factory";

  public NotificationServiceManager () throws RemoteException {}

  public MinervaServiceDescription getServiceDescription()
  {
      return new MinervaServiceDescription
          ("ODE service", "Service used to access ontologies.",
           new MinervaAttributeDescription[] {
              new MinervaAttributeDescription (HOST,
                                               "The JMS server.",
                                               String.class,
                                               null,
                                               ((NotificationServiceConfiguration) serviceConfig).host),
              new MinervaAttributeDescription (J2EE_HOME,
                                               "The home directory of J2EE.",
                                               String.class,
                                               null,
                                               ((NotificationServiceConfiguration) serviceConfig).j2ee_home),
              new MinervaAttributeDescription (TOPIC_CONNECTION_FACTORY,
                                               "The topic connection factory name.",
                                               String.class,
                                               null,
                                               ((NotificationServiceConfiguration) serviceConfig).topicConnectionFactory),
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
      NotificationServiceConfiguration cfg = (NotificationServiceConfiguration) serviceConfig;

      if (attrName.equals (HOST)) {
          cfg.host = attrValue.toString();
      }
      else if (attrName.equals (J2EE_HOME)) {
          cfg.j2ee_home = attrValue.toString();
      }
      else if (attrName.equals (TOPIC_CONNECTION_FACTORY)) {
          cfg.topicConnectionFactory = attrValue.toString();
      }
      else
          throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
  }
}