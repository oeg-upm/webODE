package es.upm.fi.dia.ontology.webode.translat.UML.uml.io;

/**
 * <p>Title: Sistema de Importaci�n y Exportaci�n de Ontolog�as a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontolog�as</p>
 * @author Miguel Esteban Guti�rrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.*;
import org.xml.sax.*;

public interface UMLModelReader {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  public UMLModel read(String fileName) throws UMLIOException;
  public UMLModel read(InputSource source) throws UMLIOException;

}