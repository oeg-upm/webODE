package es.upm.fi.dia.ontology.webode.notification;

import es.upm.fi.dia.ontology.minerva.server.services.MinervaServiceConfiguration;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class NotificationServiceConfiguration extends MinervaServiceConfiguration {
//  public HashSet topics=null;
  public String host=null;
  public String j2ee_home=null;
  public String topicConnectionFactory=null;

  public NotificationServiceConfiguration() {
    super (true, STATELESS);
  }
}