//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\events\\Data.java

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

public class Data extends XMLEvent {

  public String data=null;

  /**
   * @param uri
   * @param name
   * @param qName
   * @param data
   */
  public Data(String uri, String name, String qName, String data) {
    super(uri,name,qName);
    this.data=data;
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
   * @param handler
   */
  public void accept(EventHandler handler) throws EventNotSupported {
    handler.handle(this);
  }

  /**
   * @return String
   */
  public String toString() {
    return "Data {"+qName+"}";
  }

  /**
   * @return String
   */
  public String prettyString() {
    return "Data {uri='"+uri+"', name='"+name+"', qName='"+qName+"', data='"+data+"'}";
  }
}
