package es.upm.fi.dia.ontology.minerva.jaas;

import java.security.Principal;

public class MinervaGroupPrincipal implements Principal {
  protected String group;
  
  public MinervaGroupPrincipal(String group) {
    this.group=group;
  }
  
  public String getName() {
    return this.group;
  }
}
