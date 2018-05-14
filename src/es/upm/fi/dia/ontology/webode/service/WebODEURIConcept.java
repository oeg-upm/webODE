package es.upm.fi.dia.ontology.webode.service;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class WebODEURIConcept extends es.upm.fi.dia.ontology.webode.service.WebODEURI {
  static final java.text.MessageFormat mf=new java.text.MessageFormat("/{0}");

  protected String concept;

  WebODEURIConcept(String ontology, String concept)  throws WebODEURIException {
    if(concept==null)
      throw new  WebODEURIException("Concept can not be null");
    if(ontology==null)
      throw new  WebODEURIException("Ontology can not be null");

    try {
      this.ontology=java.net.URLDecoder.decode(ontology,WebODEURI.CODIFICATION);
      this.concept=java.net.URLDecoder.decode(concept,WebODEURI.CODIFICATION);
    }
    catch(java.io.UnsupportedEncodingException ex) {
      this.ontology=ontology;
      this.concept=concept;
    }
  }

  static WebODEURIConcept getWebODEURIConcept(java.net.URI uri) throws WebODEURIException {
    try {
      Object[] objs=mf.parse(uri.getPath());
      return new WebODEURIConcept((String)objs[0],uri.getFragment());
    }
    catch(java.text.ParseException pe) {
      return null;
    }
  }

  public String getConcept() {
    return concept;
  }
}