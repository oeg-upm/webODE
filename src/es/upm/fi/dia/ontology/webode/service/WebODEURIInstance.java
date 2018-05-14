package es.upm.fi.dia.ontology.webode.service;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class WebODEURIInstance extends WebODEURI {
  static final java.text.MessageFormat mf=new java.text.MessageFormat("/{0}");

  protected String instanceSet;
  protected String instance;

  WebODEURIInstance(String instanceSet, String instance)  throws WebODEURIException {
    if(instance==null)
      throw new  WebODEURIException("Instance can not be null");
    if(instanceSet==null)
      throw new  WebODEURIException("Instance Set can not be null");

    try {
      this.instanceSet=java.net.URLDecoder.decode(instanceSet,CODIFICATION);
      this.instance=java.net.URLDecoder.decode(instance,CODIFICATION);
    }
    catch(java.io.UnsupportedEncodingException ex) {
      this.instanceSet=instanceSet;
      this.instance=instance;
    }
  }

  static WebODEURIInstance getWebODEURIInstance(java.net.URI uri) throws WebODEURIException {
    try {
      Object[] objs=mf.parse(uri.getPath());
      return new WebODEURIInstance((String)objs[1],uri.getFragment());
    }
    catch(java.text.ParseException pe) {
      return null;
    }
  }

  public String getInstanceSet() {
    return instanceSet;
  }

  public String getInstance() {
    return instance;
  }
}
