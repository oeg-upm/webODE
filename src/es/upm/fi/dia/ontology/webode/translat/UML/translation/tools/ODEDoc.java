package es.upm.fi.dia.ontology.webode.translat.UML.translation.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.*;

public class ODEDoc {

  protected static final String PREFIX="/**@";
  protected static final String SUFFIX="*/";

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  protected String property=null;
  protected Vector argv    =null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEDoc() {
    property="";
    argv=new Vector();
  }

  public ODEDoc(String doc) throws CannotParseDoc {
    this();
    unmarshall(doc);
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  //-- IO logic ----------------------------------------------------------------

  public String marshall() {
    String data=PREFIX+" "+property+" ";
    for(int i=0;i<argv.size();i++) {
      if(i!=0)
        data+=" ";
      data+=(String)argv.get(i);
    }
    data+=" "+SUFFIX;
    return data;
  }

  public void unmarshall(String doc) throws CannotParseDoc {
    String trimmed=doc.trim();

    if(!(trimmed.startsWith(PREFIX)&&trimmed.endsWith(SUFFIX)))
      throw new CannotParseDoc(doc);

    String theData=trimmed.substring(PREFIX.length(),
                                     trimmed.length()-SUFFIX.length()).trim();

    StringTokenizer lexer=new StringTokenizer(theData," \t");

    if(!lexer.hasMoreTokens())
      throw new CannotParseDoc(doc);

    property=lexer.nextToken();
    argv=new Vector();

    while(lexer.hasMoreTokens())
      argv.add(lexer.nextToken());
  }

  //-- Data management logic ---------------------------------------------------

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property=property;
  }

  public int getArgumentCount() {
    return argv.size();
  }

  public String getArgument(int i) {
    return (String)argv.get(i);
  }

  public void setArgument(int arg_n, String arg_data) {
    if(arg_n>=argv.size()) {
      argv.setSize(arg_n+1);
      for(int i=argv.size();i<arg_n;i++) {
        argv.add(new String());
      }
    }
    argv.set(arg_n,arg_data);
  }

  //-- Others ------------------------------------------------------------------

  public String toString() {
    return marshall();
  }

}
