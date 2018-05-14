package es.upm.fi.dia.ontology.webode.translat.UML.translation.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.regex.*;

public class ODEDocPlus extends ODEDoc {

  protected static String preffix="/\\x2A\\x2A@";
  protected static String suffix ="\\x2A/";
  protected static String ws="[ \t]*";
  protected static String WS="[ \t]+";
  protected static String propertyPattern="[a-zA-Z0-9_]+";
  protected static String argumentPattern="(\\\"([^\\\"]*)\\\")|("+propertyPattern+")";
  protected static String simpleArgumentPattern=".";
  protected static String docPattern=ws+preffix+ws+propertyPattern+WS+"("+simpleArgumentPattern+")*"+ws+suffix+ws;

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  protected Pattern pDoc     =null;
  protected Pattern pProperty=null;
  protected Pattern pArgument=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEDocPlus() {
    super();
    pDoc     =Pattern.compile(docPattern);
    pProperty=Pattern.compile(propertyPattern);
    pArgument=Pattern.compile(argumentPattern);
  }

  public ODEDocPlus(String doc) throws CannotParseDoc {
    this();
    unmarshall(doc);
  }

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  //-- IO logic ----------------------------------------------------------------

  public String marshall() {
    String data=PREFIX+" "+property+" ";
    for(int i=0;i<argv.size();i++) {
      if(i!=0)
        data+=" ";
      String cadena=(String)argv.get(i);
      if(cadena==null) {
        data+="\"\"";
      } else {
        if((cadena.indexOf(" ")>=0)||
           (cadena.indexOf("\t")>=0))
          data+="\""+cadena+"\"";
        else
          data+=cadena;
      }
    }
    data+=" "+SUFFIX;
    return data;
  }

  public void unmarshall(String doc) throws CannotParseDoc {
    Matcher docMatcher     =pDoc.matcher(doc);
    Matcher propertyMatcher=pProperty.matcher(doc);
    Matcher argumentMatcher=pArgument.matcher(doc);

/*
    while(argumentMatcher.find()) {
      int gc=argumentMatcher.groupCount();
      for(int i=1;i<=gc;i++) {
        String g=argumentMatcher.group(i);
        if(g!=null&&!(g.startsWith("\"")&&g.endsWith("\"")))
          System.out.println("{"+g+"}");
      }
    }
*/
    if(!docMatcher.matches())
      throw new CannotParseDoc(doc);

    if(propertyMatcher.find()) {
      property=propertyMatcher.group(0);
    }

    while(argumentMatcher.find()) {
      int gc=argumentMatcher.groupCount();
      for(int i=1;i<=gc;i++) {
        String g=argumentMatcher.group(i);
        if(g!=null&&!(g.startsWith("\"")&&g.endsWith("\"")))
          argv.add(g);
      }
    }

    if(argv.size()>0&&argv.get(0).equals(property)) {
        argv.remove(0);
    }
  }

}
