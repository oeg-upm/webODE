//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\AssociationHandler.java

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

public class AssociationHandler extends CompositeEventHandler {

  private static String ASSOCIATION="UML:Association";
  private static String CONNECTION ="UML:Association.connection";


  private AssociationEndHandler associationEndHandler=null;

  public AssociationHandler() {
    associationEndHandler=new AssociationEndHandler();
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AssociationStart event) {
    if(event.qName.equals(CONNECTION)) {
      binder.bind(associationEndHandler);
    }
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AssociationEnd event) {
    if(event.qName.equals(ASSOCIATION)) {
      binder.unbind(this);
    }
  }

  /**
   * @param event
   */
  public void handle(AssociationStart event) throws EventNotSupported {
    if(event.qName.equals(ASSOCIATION)) {
      String name=event.atts.getValue(XMIAttributes.NAME);
      String id  =event.atts.getValue(XMIAttributes.XMI_ID);

      t_table.addAssociation(name,id);
    }
  }

  /**
   * @param event
   */
  public void handle(AssociationEnd event) throws EventNotSupported {
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @return String
   */
  public String toString() {
   return "AssociationHandler";
  }
}
