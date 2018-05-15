package es.upm.fi.dia.ontology.minerva.server.admin;

import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * Thrown if attempting to bind a service to an existing name.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class NameAlreadyBoundException extends MinervaException {
  public NameAlreadyBoundException (String str) {
    super (str);
  }

  public NameAlreadyBoundException (String str, Throwable th) {
    super (str, th);
  }

  public NameAlreadyBoundException () {
  }

  public NameAlreadyBoundException (Throwable th) {
    super(th);
  }


}