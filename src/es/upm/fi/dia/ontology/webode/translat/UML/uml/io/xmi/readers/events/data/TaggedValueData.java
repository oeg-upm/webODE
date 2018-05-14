//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\events\\data\\TaggedValueData.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.data;

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

public class TaggedValueData extends Data {

  /**
   * @param uri
   * @param name
   * @param qName
   * @param data
   */
  public TaggedValueData(String uri, String name, String qName, String data) {
    super(uri,name,qName,data);
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
    return "TaggedValueData {"+qName+"}";
  }

  /**
   * @return String
   */
  public String prettyString() {
    return "TaggedValueData {uri='"+uri+"', "+
                            "name='"+name+"', "+
                            "qName='"+qName+"', "+
                            "data='"+data.replaceAll("\n","\\\\n")+"'}";
  }
}
