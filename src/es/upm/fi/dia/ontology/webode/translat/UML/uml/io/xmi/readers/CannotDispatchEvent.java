package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.*;

public class CannotDispatchEvent extends Exception {

  private Vector handlers=null;
  private Event  event   =null;

  private String getNames() {
    String resultado=new String();

    for(int i=0;i<handlers.size();i++) {
      EventHandler handler=(EventHandler)handlers.get(i);
      if(i!=0)
        resultado+=", ";
      resultado+=handler.toString();
    }

    return resultado;
  }

  public CannotDispatchEvent(Vector handlers, Event event) {
    this.handlers=handlers;
    this.event   =event;
  }

  public String toString() {
    return "None of the active handlers {"+getNames()+"} support the event "+event;
  }
}