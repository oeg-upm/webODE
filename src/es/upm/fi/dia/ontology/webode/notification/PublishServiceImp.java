package es.upm.fi.dia.ontology.webode.notification;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import javax.naming.*;
import java.rmi.*;
import javax.jms.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class PublishServiceImp extends MinervaServiceImp implements PublishService {
  protected Context jndiContext=null;
  protected TopicConnection topicConn=null;
  protected TopicSession topicSession=null;
  protected HashMap topics=null;
  protected NotificationService notificationService=null;

  public PublishServiceImp() throws RemoteException {
    super();
  }

  public void start () throws CannotStartException {
    PublishServiceConfiguration cfg=(PublishServiceConfiguration)this.config;
    if(cfg.notificationService==null)
      throw new CannotStartException("Notification Service is not set.");

    try {
      notificationService=(NotificationService)this.context.getService(cfg.notificationService);
    }
    catch(MinervaException e) {

    }

    try {
      Hashtable env=new Hashtable();
/*      env.put("jms.proporties",cfg.j2ee_home +
                               java.io.File.separatorChar + "config" +
                               java.io.File.separatorChar + "jms_client.properties");
      env.put("org.omg.CORBA.ORBInitialHost",notificationService.getHost());*/
      env.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
      env.put(Context.PROVIDER_URL, notificationService.getHost());

      jndiContext=new InitialContext(env);
/*      jndiContext.addToEnvironment("jms.proporties",cfg.j2ee_home +
                                                    java.io.File.separatorChar + "config" +
                                                    java.io.File.separatorChar + "jms_client.properties");
      jndiContext.addToEnvironment("org.omg.CORBA.ORBInitialHost",notificationService.getHost());*/
    }
    catch(Exception e) {
      throw new CannotStartException("Error setting the JNDI API context: " + e.toString());
    }

    TopicConnectionFactory tfc=null;
    try {
      tfc=(TopicConnectionFactory)jndiContext.lookup(notificationService.getTopicConnectionFactory());
    }
    catch(Exception e) {
      throw new CannotStartException("JNDI API lookup failed: " + e.toString());
    }

    try {
      topicConn=tfc.createTopicConnection();
      topicSession=topicConn.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
    }
    catch(JMSException e) {
      try {
        if(topicConn!=null) topicConn.close();
      }
      catch(Exception ex) {
      }
      throw new CannotStartException("Error connecting to JMS Server: " + e.toString());
    }

    try {
      String[] atopics=notificationService.getTopics();
      Object topic;
      topics=new HashMap();
      for(int i=0; atopics!=null && i<atopics.length; i++) {
        topic=jndiContext.lookup(atopics[i]);
//        if(topic instanceof Topic) {
          System.out.println("Initializing Publisher of " + atopics[i]);
          topics.put(atopics[i],topicSession.createPublisher((Topic)topic));
//        }
      }
    }
    catch(Exception e) {
      try {
        if(topicConn!=null) topicConn.close();
      }
      catch(Exception ex) {
      }
      throw new CannotStartException("Error setting up the publishers: " + e.toString());
    }
  }

  public void stop() {
    try {
      if(topicConn!=null) topicConn.close();
    }
    catch(Exception e) {
    }
    super.stop();
  }

  public String[] getTopics() throws RemoteException {
    if(topics.size()==0)
      return null;
    else
      return (String[])topics.keySet().toArray(new String[0]);
  }

  public void sendMessage(java.io.Serializable msg, String topic_name) throws RemoteException, PublishException {
    if(!topics.containsKey(topic_name))
      throw new PublishException("Topic '" + topic_name + "' not found");

    try {
      TextMessage message=topicSession.createTextMessage();
      message.setText(msg.toString());
      ((TopicPublisher)topics.get(topic_name)).publish(message);
    }
    catch(Exception e) {
      throw new PublishException("Error sending a message: " + e.toString());
    }
  }

  public void localDisconnect() {
    try {
      if(topicConn!=null) topicConn.close();
    }
    catch(Exception e) {
    }
    super.localDisconnect();
  }
}