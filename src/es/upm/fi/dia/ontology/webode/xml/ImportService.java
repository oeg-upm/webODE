package es.upm.fi.dia.ontology.webode.xml;

import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * This class provides the implementation for the XML import
 * service.
 *
 * This service is stateful.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.8
 */
public interface ImportService extends MinervaService
{
  /**
   * Converts the string into a valid XML document, checking it
   * against WebODE's DTD.
   *
   * @param strBuffer The string buffer holding the document as
   *        plain text.
   * @exception XMLImportException If the document cannot be parsed
   *            sucessfully.
   */
  void loadInMemory (StringBuffer strBuffer)
      throws XMLImportException, RemoteException;

  /**
   * Retrieves whether or not there is a conceptualization in
   * the in-memory document.
   *
   * @return <tt>true</tt> if a conceptualization is present.
   */
  boolean isConceptualization () throws RemoteException;

  /**
   * Retrieves the names of all instance sets present in the imported file.
   *
   * @return The names and descriptions of all instance sets.
   */
  String[][] getInstanceSets () throws RemoteException;

  /**
   * Returns all the views.
   */
  String[] getViews() throws RemoteException;

  /**
   * Gets the ontology name.
   *
   * @return The ontology name.
   */
  String getOntologyName () throws RemoteException;

  /**
   * Gets the ontology description.
   * @return The ontology description or null if no one is present.
   */
  String getOntologyDescription() throws RemoteException;

  /**
   * Gets the ontology namespace
   * @return The ontology namespace or null if no ine is present.
   */
  String getOntologyNamespace() throws RemoteException;

  /**
   * Sets the name of the ontology where everything will be imported.
   *
   * @param name The name of the ontology.
   */
  void setOntologyName (String name) throws RemoteException;

  /**
   * Imports the references.
   */
  void importReferences () throws RemoteException, XMLImportException, WebODEException;

  /**
   * Imports the formulas.
   */
  void importFormulas () throws RemoteException, XMLImportException, WebODEException;

  /**
   * Imports the imported terms.
   */
  void importImportedTerms () throws RemoteException, XMLImportException, WebODEException;

  /**
   * Imports the properties.
   */
  void importProperties () throws RemoteException, XMLImportException, WebODEException;

  /**
   * Imports the user types
   */
  void importTypes() throws RemoteException, XMLImportException, WebODEException;

  /**
   * Imports the concepts.
   */
  void importConcepts () throws RemoteException, XMLImportException, WebODEException;

  /**
   * Imports a given instance set.
   */
  void importInstanceSet (String name, String description)
      throws RemoteException, XMLImportException, WebODEException;

  /**
   * Imports the term relationships.
   */
  void importRelationships () throws RemoteException, XMLImportException, WebODEException;

  /**
   * Imports the groups.
   */
  void importGroups () throws RemoteException, XMLImportException, WebODEException;

  /**
   * Imports the constants.
   */
  void importConstants () throws RemoteException, XMLImportException, WebODEException;

  /**
   * Import view.
   */
  void importView (String name) throws RemoteException, XMLImportException, WebODEException;
}



