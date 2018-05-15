package es.upm.fi.dia.ontology.minerva.server.management;

import es.upm.fi.dia.ontology.minerva.server.MinervaException;

/**
 * This exception might be thrown in case of attempting to set
 * a non-existent value.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class NoSuchAttributeException extends MinervaException {
  public NoSuchAttributeException (String str) {
    super (str);
  }

  public NoSuchAttributeException (String str, Throwable th) {
    super (str, th);
  }

  public NoSuchAttributeException () {
  }

  public NoSuchAttributeException (Throwable th) {
    super(th);
  }
}