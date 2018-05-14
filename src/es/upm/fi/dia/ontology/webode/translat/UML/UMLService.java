package es.upm.fi.dia.ontology.webode.translat.UML;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;

public interface UMLService extends MinervaService {

  //----------------------------------------------------------------------------
  //-- Common bussiness logic --------------------------------------------------
  //----------------------------------------------------------------------------

  /**
   * Translates the specified ontology to XMI
   *
   * @param ontologyName Name of the ontology to be exported
   * @param dtdFile Location of the dtd file
   * @return The XMI representation of the ontology
   */
  public StringBuffer exportOntologyUML(String ontologyName,
                                        String dtdFile) throws RemoteException,
                                                               O2UException;

  /**
   * Translates the specified ontology in XMI
   *
   * @param ontologyName Name of the ontology to be imported
   * @param buffer       The XMI file
   * @param session      The session where we will peak the ODE service
   */
  public void importOntologyUML(String       ontologyName,
                                String       user,
                                String       userGroup,
                                StringBuffer buffer,
                                MinervaSession session) throws RemoteException,
                                                               O2UException;
}