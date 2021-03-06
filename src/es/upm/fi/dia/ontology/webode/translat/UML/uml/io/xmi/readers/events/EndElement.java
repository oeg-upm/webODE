//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\events\\EndElement.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;

public class EndElement extends XMLEvent {

  /**
   * @param uri The base uri where the tag is defined
   * @param name The name of the tag
   * @param qName The fully qualified name of the tag
   */
  public EndElement(String uri, String name, String qName) {
    super(uri,name,qName);
  }

  /**
   * @param handler
   */
  public void accept(EventHandler handler) throws EventNotSupported  {
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
    return "EndElement {"+qName+"}";
  }

  /**
   * @return String
   */
  public String prettyString() {
    return "EndElement {uri='"+uri+"', name='"+name+"', qName='"+qName+"'}";
  }
}
