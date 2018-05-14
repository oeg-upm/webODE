package es.upm.fi.dia.ontology.webode.xml;

public class XMLImportException extends Exception {
  public XMLImportException () {
  }

  public XMLImportException (Throwable th) {
    super(th);
  }

  public XMLImportException (String msg) {
    super (msg);
  }

  public XMLImportException (String msg, Throwable th) {
    super (msg,th);
  }
}