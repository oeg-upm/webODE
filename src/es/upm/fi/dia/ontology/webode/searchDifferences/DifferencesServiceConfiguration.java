package es.upm.fi.dia.ontology.webode.searchDifferences;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DifferencesServiceConfiguration extends MinervaServiceConfiguration {

  String odeServiceName = null;

  public DifferencesServiceConfiguration () {
    super(false, STATELESS);

  }

  public DifferencesServiceConfiguration (String odeService) {
    super(false, STATELESS);
    this.odeServiceName=odeService;
  }
}