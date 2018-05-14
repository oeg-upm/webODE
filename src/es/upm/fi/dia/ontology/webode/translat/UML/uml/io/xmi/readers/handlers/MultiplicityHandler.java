//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\MultiplicityHandler.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.handlers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.start.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.end.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;

public class MultiplicityHandler extends CompositeEventHandler {

  private static String MULTIPLICITY      ="UML:Multiplicity";
  private static String MULTIPLICITY_RANGE="UML:Multiplicity.range";
  private static String RANGE             ="UML:MultiplicityRange";

  public String lower=null;
  public String upper=null;

  public MultiplicityHandler() {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, MultiplicityEnd event) {
    if(event.qName.equals(MULTIPLICITY))
      binder.unbind(this);
  }

  /**
   * @param event
   */
  public void handle(MultiplicityStart event) throws EventNotSupported {
    if(event.qName.equals(RANGE)) {
      lower=event.atts.getValue(XMIAttributes.LOWER);
      upper=event.atts.getValue(XMIAttributes.UPPER);
    }
  }

  /**
   * @param event
   */
  public void handle(MultiplicityEnd event) throws EventNotSupported {
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @return String
   */
  public String toString() {
   return "MultiplicityHandler";
  }
}
