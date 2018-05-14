//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\events\\StartElement.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events;

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

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.tools.*;

public class StartElement extends XMLEvent {

  public Attributes atts;

  /**
   * @param uri
   * @param name
   * @param qName
   * @param atts
   */
  public StartElement(String uri, String name, String qName, Attributes atts) {
    super(uri,name,qName);
    this.atts=atts;
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
    return "StartElement {"+qName+"}";
  }

  /**
   * @return String
   */
  public String prettyString() {
    return "StartElement {uri='"+uri+
                      "', name='"+name+
                      "', qName='"+qName+
                      "', atts="+new AttributePrettyPrinter(atts)+"}";
  }
}
