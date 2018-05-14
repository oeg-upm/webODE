//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\handlers\\DocumentHandler.java

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

public class DocumentHandler extends CompositeEventHandler {

  private static String XMI             ="XMI";
  private static String HEADER          ="XMI.header";
  private static String DOCUMENTATION   ="XMI.documentation";
  private static String EXPORTER        ="XMI.exporter";
  private static String EXPORTER_VERSION="XMI.exporterVersion";
  private static String METAMODEL       ="XMI.metamodel";
  private static String CONTENT         ="XMI.content";

  private ModelHandler       modelHandler=null;
  private TaggedValueHandler taggedValueHandler=null;

  public DocumentHandler() {
    modelHandler      =new ModelHandler();
    taggedValueHandler=new TaggedValueHandler();
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DocumentStart event) {
    if(event.qName.equals(CONTENT)) {
      binder.bind(modelHandler);
      binder.bind(taggedValueHandler);
    }
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DocumentData event) {
    // Nothing to be done
  }

  /**
   * @param binder
   * @param event
   */
  public void accept(EventHandlerBinder binder, DocumentEnd event) {
    if(event.qName.equals(XMI))
      binder.unbind(this);
  }

  /**
   * @param event
   */
  public void handle(DocumentStart event) throws EventNotSupported {
    if(event.qName.equals(XMI)) {
      String version=event.atts.getValue(XMIAttributes.XMI_VERSION);

      logger.info("Version XMI: "+version);
    } else if(event.qName.equals(METAMODEL)) {
      String name=event.atts.getValue(XMIAttributes.XMI_NAME);
      if(name!=null) {
        logger.info("Metamodel name: "+name);
      }
      String version=event.atts.getValue(XMIAttributes.XMI_VERSION);
      if(version!=null) {
        logger.info("Metamodel version: "+version);
      }
    }
  }

  /**
   * @param event
   */
  public void handle(DocumentData event) throws EventNotSupported {
    if(event.qName.equals(EXPORTER)) {
      logger.info("Exporter name: "+event.data);
    } else if(event.qName.equals(EXPORTER_VERSION)) {
      logger.info("Exporter version: "+event.data);
    }
  }

  /**
   * @param event
   */
  public void handle(DocumentEnd event) throws EventNotSupported {
//    System.out.println("["+this+"]->"+event);
  }

  /**
   * @return String
   */
  public String toString() {
    return "DocumentHandler";
  }
}
