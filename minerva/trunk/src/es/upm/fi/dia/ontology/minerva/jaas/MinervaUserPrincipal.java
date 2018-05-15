package es.upm.fi.dia.ontology.minerva.jaas;

import java.security.Principal;

import es.upm.fi.dia.ontology.minerva.server.MinervaSession;

public class MinervaUserPrincipal implements Principal {
  protected String name=null;
  protected MinervaSession session=null;
  
  public MinervaUserPrincipal(MinervaSession session, String name) {
    this.name=name;
    this.session=session;
  }

  public String getName() {
    return this.name;
  }
  
  public MinervaSession getSession() {
    return this.session;
  }
}
