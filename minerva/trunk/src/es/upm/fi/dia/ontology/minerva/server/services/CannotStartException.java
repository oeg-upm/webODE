package es.upm.fi.dia.ontology.minerva.server.services;

/**
 * Exception thrown when a service cannot start.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class CannotStartException extends es.upm.fi.dia.ontology.minerva.server.MinervaException {
  public CannotStartException (String str) {
    super (str);
  }

  public CannotStartException (String str, Throwable th) {
    super (str, th);
  }

  public CannotStartException () {
  }

  public CannotStartException (Throwable th) {
    super(th);
  }
}