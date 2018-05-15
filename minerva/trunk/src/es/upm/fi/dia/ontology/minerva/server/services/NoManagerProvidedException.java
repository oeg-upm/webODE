package es.upm.fi.dia.ontology.minerva.server.services;

/**
 * Exception thrown when requesting management on a non-manageable service.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class NoManagerProvidedException extends es.upm.fi.dia.ontology.minerva.server.MinervaException {
  public NoManagerProvidedException (String str) {
    super (str);
  }

  public NoManagerProvidedException (String str, Throwable th) {
    super (str, th);
  }

  public NoManagerProvidedException () {
  }

  public NoManagerProvidedException (Throwable th) {
    super(th);
  }
}