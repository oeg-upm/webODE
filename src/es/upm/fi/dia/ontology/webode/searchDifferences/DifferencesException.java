package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DifferencesException extends Exception {
  public DifferencesException() {
  }

  public DifferencesException(String msg) {
    super(msg);
  }

  public DifferencesException(Throwable th) {
    super(th);
  }

  public DifferencesException(String msg, Throwable th) {
    super(msg, th);
  }
}