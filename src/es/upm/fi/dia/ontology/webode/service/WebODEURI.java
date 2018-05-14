package es.upm.fi.dia.ontology.webode.service;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class WebODEURI implements java.io.Serializable {
  protected static final String WEBODE_SCHEME="webode";
  protected String host=null;
  protected int port=0;
  protected String ontology=null;
  protected static String CODIFICATION="ISO-8859-1";

  public static WebODEURI getWebODEURI(java.net.URI uri) throws WebODEURIException {
    if(!uri.getScheme().equals(WEBODE_SCHEME))
      throw new WebODEURIException("The uri doesn't have the WebODE scheme '" + WEBODE_SCHEME+ "'");
    WebODEURI wuri=WebODEURIConcept.getWebODEURIConcept(uri);
    if(wuri==null)
      wuri=WebODEURIInstance.getWebODEURIInstance(uri);
    if(wuri==null)
      throw new WebODEURIException("URI malformed");

    wuri.host=uri.getHost();
    wuri.port=uri.getPort();

    return wuri;
  }

  public static WebODEURI getWebODEURI(String uri) throws java.net.URISyntaxException, WebODEURIException {
    return getWebODEURI(new java.net.URI(uri));
  }

  protected static void decomposeURI(java.net.URI uri, WebODEURI webode_uri) {
    webode_uri.host=uri.getHost();
    webode_uri.port=uri.getPort();
  }

  public String getScheme() {
    return WEBODE_SCHEME;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getOntology() {
    return ontology;
  }
}