package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.logging.*;
import java.util.*;
import java.text.*;

public class EventFormatter extends Formatter{

  private String component=null;

  public EventFormatter(String component) {
    super();
    this.component=component;
  }

  public String format(LogRecord record) {
    String result=null;
    DateFormat df=DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
    Date date=new Date(record.getMillis());
    String head="["+df.format(date)+"] -> ";
    char[] white=new char[head.length()];
    Arrays.fill(white,' ');
    String fill=new String(white,0,white.length);
    String m1=record.getMessage();
    String m2=m1.replaceAll("\n","&ln;"+fill);
    String m3=m2.replaceAll("&ln;","\n");
    result=head+m3+"\n";
    return result;
  }

  public String getHead(Handler h) {
    return ">>>>> ["+component+"] Debugging initiated <<<<<\n";
  }

  public String getTail(Handler h) {
    return ">>>>> ["+component+"] Debugging stopped   <<<<<\n";
  }
}