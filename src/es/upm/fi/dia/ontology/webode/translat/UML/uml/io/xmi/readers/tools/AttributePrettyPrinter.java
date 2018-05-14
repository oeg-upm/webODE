package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import org.xml.sax.*;
import java.io.*;

public class AttributePrettyPrinter {

  private StringWriter writer=null;
  private PrintWriter  logger=null;

  protected void printAttributes(Attributes atts) {
    int n=atts.getLength();
    if(n>0) {
      logger.print("[ ");

      for(int i=0;i<n;i++) {
        if(i!=0)
          logger.print(", ");
        printAttribute(atts.getURI(i),
                       atts.getLocalName(i),
                       atts.getQName(i),
                       atts.getType(i),
                       atts.getValue(i));
      }
      logger.print("]");
    }
  }

  protected void printAttribute(String uri,
                                String name,
                                String qName,
                                String type,
                                String value) {
    logger.print("uri: "+uri);
    logger.print(", name: "+name);
    logger.print(", qName: "+qName);
    logger.print(", type: "+type);
    logger.print(", value: "+value);
  }

  public AttributePrettyPrinter(Attributes atts) {
    writer=new StringWriter();
    logger=new PrintWriter(writer);
    printAttributes(atts);
    logger.flush();
  }

  public String toString() {
    return writer.getBuffer().toString();
  }
}