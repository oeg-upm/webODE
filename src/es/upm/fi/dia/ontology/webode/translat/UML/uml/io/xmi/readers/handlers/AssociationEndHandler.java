//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\AssociationEndHandler.java

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

public class AssociationEndHandler extends CompositeEventHandler {

  private static String END         ="UML:AssociationEnd";
  private static String MULTIPLICITY="UML:AssociationEnd.multiplicity";

  private String  classId       =null;
  private boolean isNavigable   =false;
  private int     minCardinality=0;
  private int     maxCardinality=0;
  private boolean isAggregation =false;

  private MultiplicityHandler multiplicityHandler=null;

  public AssociationEndHandler() {
    multiplicityHandler=new MultiplicityHandler();
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AssociationEndStart event) {
    if(event.qName.equals(MULTIPLICITY)) {
      binder.bind(multiplicityHandler);
    }
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AssociationEndEnd event) {
    if(event.qName.equals(END)) {
      binder.unbind(this);
    }
  }

  /**
   * @param event
   */
  public void handle(AssociationEndStart event) throws EventNotSupported {
    if(event.qName.equals(END)) {
      classId    =event.atts.getValue(XMIAttributes.TYPE);
      isNavigable=Boolean.getBoolean(event.atts.getValue(XMIAttributes.IS_NAVIGABLE));
      String property=event.atts.getValue(XMIAttributes.AGGREGATION);
      if(property!=null) {
        isAggregation=property.equals("aggregate");
      } else {
        isAggregation=false;
      }

      minCardinality=0;
      maxCardinality=0;
    }
  }

  /**
   * @param event
   */
  public void handle(AssociationEndEnd event) throws EventNotSupported {
    if(event.qName.equals(END)) {
      t_table.addAssociationEnd(classId,isNavigable,minCardinality,maxCardinality,isAggregation);
    } else if(event.qName.equals(MULTIPLICITY)) {
      minCardinality=Integer.parseInt(multiplicityHandler.lower);
      maxCardinality=Integer.parseInt(multiplicityHandler.upper);
    }
  }

  /**
   * @return String
   */
  public String toString() {
   return "AssociationEndHandler";
  }
}
