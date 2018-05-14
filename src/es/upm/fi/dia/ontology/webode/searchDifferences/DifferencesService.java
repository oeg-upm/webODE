package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import es.upm.fi.dia.ontology.minerva.server.services.MinervaService;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface DifferencesService extends MinervaService, Remote
{
  /**
   * This method searches differences between two given ontologies
   * @param ontologyNameBase The name of the base ontology
   * @param ontologyName The name of the second ontology
   * @return A xml with differences or null if there are not differences between the ontologies
   * @throws DifferencesException
   */
  public char[] searchDifferences (String ontologyNameBase, String ontologyName) throws RemoteException, DifferencesException;

}