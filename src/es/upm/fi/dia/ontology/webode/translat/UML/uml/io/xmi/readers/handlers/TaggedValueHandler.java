//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\TaggedValueHandler.java

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
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.data.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;

public class TaggedValueHandler extends SimpleEventHandler {

  private static final String TAGGED_VALUE="UML:TaggedValue";
  private static final String VALUE="UML:TaggedValue.value";

  private String tag  =null;
  private String id   =null;
  private String value=null;

  public TaggedValueHandler() {
    // Nothing to be done here
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, TaggedValueEnd event) {
    binder.unbind(this);
  }

  /**
   * @param event
   */
  public void handle(TaggedValueStart event) throws EventNotSupported {
    if(event.qName.equals(TAGGED_VALUE)) {
      id   =event.atts.getValue(XMIAttributes.MODEL_ELEMENT);
      value=event.atts.getValue(XMIAttributes.VALUE);
      tag  =event.atts.getValue(XMIAttributes.TAG);

    } else if(event.qName.equals(VALUE)) {
      value=new String();
    }
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @param event
   */
  public void handle(TaggedValueData event) throws EventNotSupported {
    if(!value.equals(""))
      value+="\n";
    value+=event.data;
//    logger.info(event.prettyString());
  }

  /**
   * @param event
   */
  public void handle(TaggedValueEnd event) throws EventNotSupported {
    if(event.qName.equals(TAGGED_VALUE)) {
      if(tag.equals("documentation")) {
        logger.info("Found description '"+value+"' for id="+id);

        t_table.addDocumentation(value,id);
      }
    }
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @return String
   */
  public String toString() {
   return "TaggedValueHandler";
  }
}
