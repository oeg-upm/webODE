package es.upm.fi.dia.ontology.minerva.server;

/**
 * This is the root exception of all Minerva exceptions.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class MinervaException extends Exception implements java.io.Serializable
{
  public MinervaException (String str) {
    super (str);
  }

  public MinervaException (String str, Throwable th) {
    super (str, th);
  }

  public MinervaException ()  {
  }

  public MinervaException (Throwable th)  {
    super(th);
  }
}