package es.upm.fi.dia.ontology.webode.notification;

import es.upm.fi.dia.ontology.minerva.server.services.*;
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

public class NotificationServiceImp extends MinervaServiceImp implements NotificationService {
  protected final String TOPIC_CONTEXT="webode";
  protected Context jndiContext=null;
  protected TopicConnection topicConn=null;
  protected TopicSession topicSession=null;
  protected HashSet topics=null;

  public NotificationServiceImp() throws RemoteException {
    super();
  }

  public void start () throws CannotStartException {
    NotificationServiceConfiguration cfg=(NotificationServiceConfiguration)this.config;
    if(cfg.host==null)
      throw new CannotStartException("JMS Server is not set.");
    if(cfg.j2ee_home==null)
      throw new CannotStartException("J2EE_HOME is not set.");
    if(cfg.topicConnectionFactory==null)
      throw new CannotStartException("Topic Connection Factory is not set.");

    try {
      Hashtable env=new Hashtable();
/*      env.put("jms.proporties",cfg.j2ee_home +
                               java.io.File.separatorChar + "config" +
                               java.io.File.separatorChar + "jms_client.properties");
      env.put("org.omg.CORBA.ORBInitialHost",cfg.host);*/
      env.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
      env.put(Context.PROVIDER_URL, ((NotificationServiceConfiguration)config).host);

      jndiContext=new InitialContext(env);
/*      jndiContext.addToEnvironment("jms.proporties",cfg.j2ee_home +
                                                    java.io.File.separatorChar + "config" +
                                                    java.io.File.separatorChar + "jms_client.properties");
      jndiContext.addToEnvironment("org.omg.CORBA.ORBInitialHost",cfg.host);*/
    }
    catch(NamingException e) {
      throw new CannotStartException("Error setting the JNDI API context: " + e.toString());
    }

    TopicConnectionFactory tfc=null;
    try {
      tfc=(TopicConnectionFactory)jndiContext.lookup(cfg.topicConnectionFactory);
    }
    catch(NamingException e) {
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
      topics=new HashSet();
      NameClassPair pair;
      Object topic;
      for(NamingEnumeration ne=jndiContext.list(this.TOPIC_CONTEXT); ne.hasMore(); ) {
        pair=(NameClassPair)ne.next();
        topic=jndiContext.lookup(this.TOPIC_CONTEXT + "/" + pair.getName());
//        if(topic instanceof Topic) {
          System.out.println("Getting topic for notifying " + pair.getName());
          topics.add(this.TOPIC_CONTEXT + "/" + pair.getName());
//        }
      }
    }
    catch(NamingException e) {
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
      return (String[])topics.toArray(new String[0]);
  }

  public String getHost() throws RemoteException {
    return ((NotificationServiceConfiguration)this.config).host;
  }

  public String getTopicConnectionFactory() throws RemoteException {
    return ((NotificationServiceConfiguration)this.config).topicConnectionFactory;
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