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

public class PublishServiceConfiguration extends MinervaServiceConfiguration {
//  public HashSet topics=null;
  public String notificationService=null;
  public String j2ee_home=null;

  public PublishServiceConfiguration() {
    super (false, STATELESS);
  }
}