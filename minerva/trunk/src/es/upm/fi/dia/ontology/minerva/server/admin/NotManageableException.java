package es.upm.fi.dia.ontology.minerva.server.admin;

import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * Thrown in case the service is not manageable.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class NotManageableException extends MinervaException {
  public NotManageableException (String str) {
    super (str);
  }

  public NotManageableException (String str, Throwable th) {
    super (str, th);
  }

  public NotManageableException () {
  }

  public NotManageableException (Throwable th) {
    super(th);
  }
}