//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\Event.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public interface Event {

  /**
   * @param handler
   */
  public void accept(EventHandler handler) throws EventNotSupported;

  /**
   * @param handler
   * @param binder
   */
  public void accept(RegistrableEventHandler handler,
                     EventHandlerBinder      binder);

  /**
   * @return String
   */
  public String toString();

  /**
   * @return String
   */
  public String prettyString();

}
