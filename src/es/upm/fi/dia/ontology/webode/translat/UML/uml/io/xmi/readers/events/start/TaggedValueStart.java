//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\events\\start\\TaggedValueStart.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.start;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import org.xml.sax.*;

//import o2u.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.tools.*;

public class TaggedValueStart extends StartElement {

  /**
   * @param uri
   * @param name
   * @param qName
   * @param atts
   */
  public TaggedValueStart(String uri, String name, String qName, Attributes atts) {
    super(uri,name,qName,atts);
  }

  /**
   * @param handler
   */
  public void accept(EventHandler handler) throws EventNotSupported {
    handler.handle(this);
  }

  /**
   * @param handler
   * @param binder
   */
  public void accept(RegistrableEventHandler handler,
                     EventHandlerBinder      binder) {
    handler.accept(binder,this);
  }

  /**
   * @return String
   */
  public String toString() {
    return "TaggedValueStart {"+qName+"}";
  }

  /**
   * @return String
   */
  public String prettyString() {
    return "TaggedValueStart {uri='"+uri+
                      "', name='"+name+
                      "', qName='"+qName+
                      "', atts="+new AttributePrettyPrinter(atts)+"}";
  }
}
