package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.io.*;
import java.util.logging.*;

//import o2u.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.tools.*;

public abstract class LoggableHandler implements RegistrableEventHandler {

  public static Logger         logger       =null;
  public static WindowHandler  windowHandler=null;
  public static EventFormatter formatter    =null;

  static {
    try {
      logger=Logger.getLogger("es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.EventHandler");
      logger.setUseParentHandlers(false);

      formatter=new EventFormatter("EVENT HANDLERS");

      FileHandler fileHandler=new FileHandler("..\\var\\log\\XMI_model_handlers.log");
      fileHandler.setFormatter(formatter);
      logger.setLevel(Level.FINE);
      fileHandler.setLevel(Level.FINE);
      logger.addHandler(fileHandler);
/*
      windowHandler=new WindowHandler();
      windowHandler.setFormatter(formatter);
      windowHandler.setLevel(Level.FINE);
      logger.setLevel(Level.FINE);
      logger.addHandler(windowHandler);
*/
    } catch (IOException ex) {
    } catch (SecurityException ex) {
    }
  }

}