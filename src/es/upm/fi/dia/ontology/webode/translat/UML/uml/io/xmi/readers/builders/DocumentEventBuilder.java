//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\builders\\DocumentEventBuilder.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.builders;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;

import java.util.*;

public class DocumentEventBuilder extends DefaultEventBuilder {

  private String[] tags      ={"XMI",
                               "XMI.header",
                               "XMI.documentation",
                               "XMI.exporter",
                               "XMI.exporterVersion",
                               "XMI.metamodel",
                               "XMI.content"};
  private HashSet  set       =null;

  private String   startClassName="es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.start.DocumentStart";
  private String   endClassName  ="es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.end.DocumentEnd";
  private String   dataClassName ="es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.events.data.DocumentData";

  private Class    startClass=null;
  private Class    endClass  =null;
  private Class    dataClass =null;

  /**
   * @param factory
   */
  public DocumentEventBuilder(EventFactory factory) {
    // Create builder
    super(factory);
    // Set up valid tags
    set=new HashSet();
    set.addAll(Arrays.asList(tags));
    // Preload builded classes
    ClassLoader loader=ClassLoader.getSystemClassLoader();

    try {
      if(startClassName!=null)
        startClass=loader.loadClass(startClassName);
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("Event class "+startClassName+
                                 " cannot be loaded.",
                                 ex);
    }

    try {
      if(endClassName!=null)
        endClass=loader.loadClass(endClassName);
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("Event class "+endClassName+
                                 " cannot be loaded.",
                                 ex);
    }

    try {
      if(dataClassName!=null)
        dataClass=loader.loadClass(dataClassName);
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("Event class "+dataClassName+
                                 " cannot be loaded.",
                                 ex);
    }
  }

  /**
   * @param qName
   * @return boolean
   */
  protected boolean isValid(String qName) {
   return set.contains(qName);
  }

  /**
   * @return Class
   */
  protected Class getStartClass() {
   return startClass;
  }

  /**
   * @return Class
   */
  protected Class getEndClass() {
   return endClass;
  }

  /**
   * @return Class
   */
  protected Class getDataClass() {
   return dataClass;
  }
}
