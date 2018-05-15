package es.upm.fi.dia.ontology.minerva.server.builtin;

import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * Class thrown to indicate a login problem or a restriction
 * of access to a service.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class AuthenticationException extends MinervaException
{
  public AuthenticationException (String str) {
    super (str);
  }

  public AuthenticationException (String str, Throwable th) {
    super (str, th);
  }

  public AuthenticationException ()  {
  }

  public AuthenticationException (Throwable th)  {
    super(th);
  }
}