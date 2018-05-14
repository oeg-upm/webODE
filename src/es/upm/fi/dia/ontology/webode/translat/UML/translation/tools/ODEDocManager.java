package es.upm.fi.dia.ontology.webode.translat.UML.translation.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.io.*;
import java.util.*;

public class ODEDocManager {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private TreeMap docs   =null;
  private String  remains=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private boolean checkProperty(String property) {
    return docs.containsKey(property);
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEDocManager() {
    docs=new TreeMap();
  }

  public ODEDocManager(String buffer) throws IOException {
    this();
    unmarshall(buffer);
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  //-- IO logic ----------------------------------------------------------------

  public String marshall() {
    Vector values=new Vector(docs.values());
    StringBuffer buffer=new StringBuffer();
    int n=0;
    for(int i=0;i<values.size();i++) {
      Vector v=(Vector)values.get(i);
      for(int j=0;j<v.size();j++) {
        ODEDoc doc=(ODEDoc)v.get(j);
        if((n++)!=0)
          buffer.append("\n");
        buffer.append(doc.toString());
      }
    }
    if(!buffer.toString().equals("")&&!remains.equals("")) {
      buffer.append("\n");
    }
    buffer.append(remains);
    return buffer.toString();
  }

  public void unmarshall(String buffer) throws IOException {
    if(buffer!=null) {
      String trimmed=buffer.trim();
      BufferedReader theBuffer=new BufferedReader(new StringReader(trimmed));

      String line=null;
      while((line=theBuffer.readLine())!=null) {
        try {
          ODEDoc newDoc=new ODEDocPlus(line);
          addDoc(newDoc);
        } catch (CannotParseDoc ex) {
          if(remains==null) {
            remains=line;
          } else {
            remains+="\n"+line;
          }
        }
      }
    }
    if(remains==null)
      remains=new String();
  }

  //-- Data management logic ---------------------------------------------------

  public boolean containsProperty(String property) {
    return checkProperty(property);
  }

  public String[] getPropertys() {
    String[] propertys=null;

    Set keys=docs.keySet();

    propertys=new String[keys.size()];
    keys.toArray(propertys);

    return propertys;
  }

  public ODEDoc[] getDocs(String property) {
    ODEDoc[] res=null;

    if(checkProperty(property)) {
      Vector v=(Vector)docs.get(property);
      res=new ODEDoc[v.size()];
      v.toArray(res);
    }

    return res;
  }

  public void addDoc(ODEDoc doc) {
    if(docs.containsKey(doc.getProperty())) {
      Vector v=(Vector)docs.get(doc.getProperty());
      v.add(doc);
    } else {
      Vector v=new Vector();
      v.add(doc);
      docs.put(doc.getProperty(),v);
    }
  }

  public String getRemainingData() {
    return remains;
  }

  //-- Others ------------------------------------------------------------------

  public String toString() {
    return marshall();
  }
}

