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

public class PublishServiceManager extends MinervaManagerImp {
  public static final String NOTIFICATION_SERVICE    = "Notification Service";
  public static final String J2EE_HOME    = "J2EE HOME";

  public PublishServiceManager () throws RemoteException {}

  public MinervaServiceDescription getServiceDescription()
  {
      return new MinervaServiceDescription
          ("ODE service", "Service used to access ontologies.",
           new MinervaAttributeDescription[] {
             new MinervaAttributeDescription (NOTIFICATION_SERVICE,
                                             "The Notification Service.",
                                             String.class,
                                             null,
                                             ((PublishServiceConfiguration) serviceConfig).notificationService),
              new MinervaAttributeDescription (J2EE_HOME,
                                               "The J2EE home.",
                                               String.class,
                                               null,
                                               ((PublishServiceConfiguration) serviceConfig).j2ee_home)
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
      PublishServiceConfiguration cfg = (PublishServiceConfiguration) serviceConfig;

      if (attrName.equals (NOTIFICATION_SERVICE)) {
          cfg.notificationService = attrValue.toString();
      }
      else if (attrName.equals (J2EE_HOME)) {
        cfg.j2ee_home = attrValue.toString();
      }
      else
          throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
  }
}