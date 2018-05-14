package es.upm.fi.dia.ontology.webode.service;

/**
 * This class is the root for all ODE exceptions.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class WebODEException extends Exception
{
  public WebODEException (String str) {
    super (str);
  }

  public WebODEException () {
  }

  public WebODEException (String str, Throwable th) {
    super (str, th);
  }

  public WebODEException (Throwable th) {
    super(th);
  }
}