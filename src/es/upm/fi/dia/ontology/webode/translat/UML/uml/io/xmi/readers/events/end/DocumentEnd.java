//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\events\\end\\DocumentEnd.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.end;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;

public class DocumentEnd extends EndElement {

  /**
   * @param uri
   * @param name
   * @param qName
   */
  public DocumentEnd(String uri, String name, String qName) {
    super(uri,name,qName);
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
    return "DocumentEnd {"+qName+"}";
  }

  /**
   * @return String
   */
  public String prettyString() {
    return "DocumentEnd {uri='"+uri+"', name="+name+"', qName="+qName+"'}";
  }
}
