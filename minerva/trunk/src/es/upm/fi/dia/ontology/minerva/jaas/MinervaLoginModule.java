package es.upm.fi.dia.ontology.minerva.jaas;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.Principal;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import es.upm.fi.dia.ontology.minerva.client.MinervaClient;
import es.upm.fi.dia.ontology.minerva.server.MinervaException;
import es.upm.fi.dia.ontology.minerva.server.MinervaSession;
import es.upm.fi.dia.ontology.minerva.server.MinervaURL;

/*
 * <p>The configuration the MinervaLoginModule must have the following schema:</p>
 * <pre>
 * &lt;&lt;application name&gt;&gt; {
 *   es.upm.fi.dia.ontology.minerva.jaas.MinervaLoginModule &lt;&lt;flag&gt;&gt;
 *     host="&lt;&lt;host&gt;&gt;"
 *     port="&lt;&lt;port&gt;&gt;";
 * };
 * </pre>
 * <p>or</p>
 * <pre>
 * &lt;&lt;application name&gt;&gt; {
 *   es.upm.fi.dia.ontology.minerva.jaas.MinervaLoginModule &lt;&lt;flag&gt;&gt;
 *     minerva_url="minerva://&lt;&lt;host&gt;&gt;:&lt;&lt;port&gt;&gt;"
 * };
 * </pre>
 */
public class MinervaLoginModule implements LoginModule {
  public static final String MINERVA_URL="minerva_url";
  public static final String MINERVA_HOST="host";
  public static final String MINERVA_PORT="port";
  
  protected Subject subject=null;
  protected CallbackHandler cbh=null;
  protected String url=null;
  protected MinervaURL minerva_url=null;
  protected String user=null; 
  protected String password=null; 

  public void initialize(Subject subject, CallbackHandler cbhandler, Map sharedState, Map options) {
    this.subject=subject;
    this.cbh=cbhandler;
    this.url=(String)options.get(MINERVA_URL);
    if(this.url==null) {
      String host=(String)options.get(MINERVA_HOST);
      int port=2000;
      try {
        port=Integer.parseInt((String)options.get(MINERVA_PORT));
      }
      catch(NumberFormatException ne) {};
      if(host!=null)
        this.url="minerva://"+host+":"+port;
    }
  }

  public boolean login() throws LoginException {
    try {
      if (this.url == null)
        throw new LoginException("There is not a correct Minerva URL specified. Please, set the attribute minerva_url or the attribute host and port");
      Callback[] cls = new Callback[] { new NameCallback("Login name:"), new PasswordCallback("Password:", true) };
      this.cbh.handle(cls);
      this.user = ((NameCallback) cls[0]).getName();
      this.password = new String(((PasswordCallback) cls[1]).getPassword());
      this.minerva_url=new MinervaURL(this.url);
      return true;
    }
    catch (IOException ioe) {
      LoginException le=new LoginException("An error occurs while trying to retrieve user and password");
      le.initCause(ioe);
      throw le;
    }    
    catch (UnsupportedCallbackException ucbe) {
      LoginException le=new LoginException("An error occurs while trying to retrieve user and password");
      le.initCause(ucbe);
      throw le;
    }    
  }

  public boolean commit() throws LoginException {
    try {
      MinervaSession session = MinervaClient.getMinervaSession(this.minerva_url, this.user, this.password);
      Principal principal=new MinervaUserPrincipal(session, this.user);
      subject.getPrincipals().add(principal);
      String[] grps=session.getGroups();
      if(grps!=null) {
        for(int i=0; i<grps.length; i++)
          subject.getPrincipals().add(new MinervaGroupPrincipal(grps[i]));
      }
      return true;
    }
    catch (RemoteException re) {
      LoginException le=new LoginException("An error occurs while trying authenticate");
      le.initCause(re);
      throw le;
    }    
    catch (MinervaException me) {
      LoginException le=new LoginException("An error occurs while trying authenticate");
      le.initCause(me);
      throw le;
    }    
  }

  public boolean abort() throws LoginException {
    this.user=null;
    this.password=null;
    return true;
  }

  public boolean logout() throws LoginException {
    MinervaSession session=(MinervaSession)subject.getPrivateCredentials(MinervaSession.class).iterator().next();
    try {
      session.disconnect();
      return true;
    }
    catch (RemoteException re) {
      LoginException le=new LoginException("An error occurs while trying authenticate");
      le.initCause(re);
      throw le;
    }
  }
}
