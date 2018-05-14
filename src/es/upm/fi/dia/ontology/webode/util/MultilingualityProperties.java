package es.upm.fi.dia.ontology.webode.util;

public class MultilingualityProperties extends java.util.Properties {

  protected String javascriptMarshall(String str) {
    String result = "";
    for(int size=0; size < str.length(); size++) {
      if (str.charAt(size) == '\'') {
        result += "\\'";
      } else {
        result += str.charAt(size);
      }
    }
    return result;
  }

  public String getProperty(String property, String[] params) {
    String prop=getProperty(property);
    if(params!=null) {
      int idx;
      String param;
      for(int i=0; i<params.length; i++) {
        param="|~"+(i+1);
        while((idx=prop.indexOf(param))!=-1) {
          prop=prop.substring(0,idx) + params[i]+ prop.substring(idx+param.length());
        }
      }
    }
    return prop;
  }

  public String getPropertyJavascript(String property, String[] params) {
  	String res=getProperty(property, params);
  	if(res!=null)
  	  res=javascriptMarshall(res);
  	return res;
  }

  public  String Transform(Node PN) {

  	NodeInfo PI = (NodeInfo) PN.getUserObject();

  	String res= PI.getName();

  	return res;
  }


}