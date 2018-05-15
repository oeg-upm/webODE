package es.upm.fi.dia.ontology.minerva.server;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Minerva Server's class loader.
 *
 * @author   Julio César Arpírez Vega
 * @version  1.0
 */
public class MinervaClassLoader extends URLClassLoader {
  public MinervaClassLoader (URL[] aurl) {
    super (aurl);
  }

  public MinervaClassLoader (URL[] aurl, ClassLoader parent) {
    super (aurl, parent);
  }

  public void addURL (URL url) {
    super.addURL (url);
  }

  public String toString() {
    URL[] urs=this.getURLs();
    if(urs!=null) {
      StringBuffer str=new StringBuffer();
      for(int i=0; i<urs.length; i++) 
        str.append(urs[i] + "\n");
      return str.toString();
    }
    else
      return null;
  }
}
