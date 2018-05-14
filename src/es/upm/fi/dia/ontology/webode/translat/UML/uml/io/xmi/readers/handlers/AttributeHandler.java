//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\AttributeHandler.java

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

import es.upm.fi.dia.ontology.webode.translat.UML.uml.component.*;

public class AttributeHandler extends CompositeEventHandler {

  private static String CLASSIFIER   ="UML:Classifier.feature";
  private static String ATTRIBUTE    ="UML:Attribute";
  private static String MULTIPLICITY ="UML:StructuralFeature.multiplicity";
  private static String INITIAL_VALUE="UML:Attribute.initialValue";
  private static String EXPRESSION   ="UML:Expression";

  private MultiplicityHandler multiplicityHandler=null;

  public AttributeHandler() {
    multiplicityHandler=new MultiplicityHandler();
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AttributeStart event) {
    if(event.qName.equals(MULTIPLICITY)) {
      binder.bind(multiplicityHandler);
    }
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, AttributeEnd event) {
    if(event.qName.equals(CLASSIFIER)) {
      binder.unbind(this);
    }
  }

  private UMLAttribute attribute=null;

  /**
   * @param event
   */
  public void handle(AttributeStart event) throws EventNotSupported {
    if(event.qName.equals(ATTRIBUTE)) {
      String id  =event.atts.getValue(XMIAttributes.XMI_ID);
      String name=event.atts.getValue(XMIAttributes.NAME);
      String type=event.atts.getValue(XMIAttributes.TYPE);

      attribute=t_table.addAttribute(name,type,id);

      logger.info("Added attribute '"+name+
                  "' to class '"+attribute.getParentClass().getName()+"'");
    } else if(event.qName.equals(EXPRESSION)) {
      String body    =event.atts.getValue(XMIAttributes.BODY);
      String language=event.atts.getValue(XMIAttributes.LANGUAGE);

      attribute.setInitialValue(body);
      logger.info("Updated attribute '"+attribute.getName()+
                  "' initial value: "+body);
    }
  }

  /**
   * @param event
   */
  public void handle(AttributeEnd event) throws EventNotSupported {
    if(event.qName.equals(ATTRIBUTE)) {
      attribute=null;
    } else if(event.qName.equals(MULTIPLICITY)) {
      attribute.setMultiplicity(multiplicityHandler.lower+
                                ".."+
                                multiplicityHandler.upper);
      logger.info("Updated attribute '"+attribute.getName()+
                  "' multiplicity: "+attribute.getMultiplicity());
    }
  }

  /**
   * @return String
   */
  public String toString() {
   return "AttributeHandler";
  }
}
