package es.upm.fi.dia.ontology.minerva.server.others;

import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * Exception to be thrown by batched jobs.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class BatchJobException extends MinervaException {
  public BatchJobException () {
  }

  public BatchJobException (Throwable th) {
    super(th);
  }

  public BatchJobException (String msg) {
    super (msg);
  }

  public BatchJobException (String msg, Throwable th) {
    super (msg, th);
  }


}