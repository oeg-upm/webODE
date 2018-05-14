package es.upm.fi.dia.ontology.webode.translat.UML;

import es.upm.fi.dia.ontology.minerva.server.services.MinervaServiceConfiguration;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class UMLServiceConfiguration extends MinervaServiceConfiguration {
  public String odeService;

  public UMLServiceConfiguration() {
    super(false,STATELESS);
  }
}