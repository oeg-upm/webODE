package test;

import java.security.Principal;
import java.util.Iterator;

import javax.security.auth.Subject;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;

import es.upm.fi.dia.ontology.minerva.jaas.MinervaUserPrincipal;
import es.upm.fi.dia.ontology.minerva.server.MinervaSession;

public class Main1 {
  public static void main(String[] args) throws Exception {
    LoginContext loginContext = null;
    
    loginContext = new LoginContext("Minerva", new SampleCallbackHandler());
    
    try {
      // If we return without an exception, authentication succeeded
      loginContext.login();
      Subject subject=loginContext.getSubject();
      System.out.println("The user " + ((Principal)subject.getPrincipals(MinervaUserPrincipal.class).iterator().next()).getName() + " has been logged succesfully");
      MinervaSession session=(MinervaSession)subject.getPrivateCredentials().iterator().next();
      System.out.println("With the following Principals:");
      for(Iterator it=subject.getPrincipals().iterator(); it.hasNext(); )
        System.out.println("  " + ((Principal)it.next()).getName());
      System.out.println("Session:" + session);
    }
    catch(FailedLoginException fle) {
      System.out.println("Authentication Failed, " + fle.getMessage());
      System.exit(-1);
    }
    catch(AccountExpiredException aee) {
      System.out.println("Authentication Failed: Account Expired");
      System.exit(-1);
    }
    catch(CredentialExpiredException cee) {
      System.out.println("Authentication Failed: Credentials Expired");
      System.exit(-1);
    }
    catch(Exception e) {
      System.out.println("Authentication Failed: Unexpected Exception, " + e.getMessage());
      e.printStackTrace();
      System.exit(-1);
    }
  }
}
