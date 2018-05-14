package es.upm.fi.dia.ontology.webode.service;

import java.rmi.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.webode.service.irs.*;

/**
 * This class describes the essentials of the ODE service.
 *
 * @author  Julio César Arpírez Vega
 * @version 3.0
 */
public interface ODEService extends MinervaService {
  /**
   * Opens an ontology (for caching purposes).
   *
   * @param ontology The ontology to open.
   */
  void openOntology (String ontology) throws RemoteException, WebODEException;

  /**
   * Gets the user's group.
   */
  String getGroup () throws RemoteException;

  /** Gets all user's groups. */
  String[] getUserGroups() throws RemoteException;

  /**
   * Retrieves the list of available ontologies.
   *
   * @return The names of the available ontologies.
   */
  OntologyDescriptor[] getAvailableOntologies () throws RemoteException, WebODEException;

  /**
   * Retrieves specific data about a given ontology.
   *
   * @param name The name of the ontology.
   * @throws WebODEException Throws an exception if the ontology is not found.
   */

  OntologyDescriptor getOntologyDescriptor (String name) throws RemoteException, WebODEException;

  /**
   * Retrieves the list of ontologies with the same namespace
   * @param namespace
   * @return list of ontologies.
   */
  OntologyDescriptor[] findOntologyByNamespace (String namespace) throws RemoteException, WebODEException;

  /**
   * Gets the ontologies related to an ontology in the context of the
   * first ontology.
   *
   * @param The ontology which represents the context.
   * @return Current relations or <tt>null</tt> if no one is available.
   */
  OntologyRelation[] getRelatedOntologies (String name) throws RemoteException, WebODEException;

  /**
   * Remove related ontologies.
   *
   * @param ontology The ontology in context.
   * @param ontologyToRemove The ontology to be removed in this context.
   */
  void removeRelatedOntology (String ontology, String ontologyToRemove) throws RemoteException, WebODEException;

  /**
   * Remove an ontology relation in the context of a given relation.
   *
   * @param ontology The ontology in context.
   * @param relation The relation to remove.
   */
  void removeOntologyRelation (String ontology, String relation) throws RemoteException, WebODEException;

  /**
   * Creates an ontology with the given attributes.
   *
   * @param od The ontology descriptor.
   */
  void createOntology (OntologyDescriptor od) throws RemoteException, WebODEException;

  /**
   * Removes an ontology completely from server.
   *
   * @param name The ontology name.
   */
  void removeOntology (String name)  throws RemoteException, WebODEException;

  /**
   * Modify the data about an ontology.
   * <p>
   * Everything but the creation and modification date can be changed.  These
   * two fields are simply ignored.
   *
   * @param name The name of the ontology to alter.
   * @param od The ontology descriptor with the new data.
   */
  void updateOntology (String name, OntologyDescriptor od) throws RemoteException, WebODEException;

  /**
   * Insert a relation between two ontologies.
   *
   * @param rd The relation descriptor.
   */
  void createOntologyRelation (OntologyRelation or) throws RemoteException, WebODEException;

  /**
   * Creates an ontology to be related to another in the context of the last one.
   *
   * @param ontology The ontology.
   * @param name     The ontology to be related.
   * @param x        The x position
   * @param y        the y position
   */
  void createOntologyOntology (String ontology, String name, int x, int y) throws RemoteException, WebODEException;

  /**
   * Get positions of ontologies in the context of an ontology.
   *
   * @return An array holding the positions of the elements.  <tt>null</tt> if no one is present.
   */
  OntologyPositionDescriptor[] getOntologyPositions (String name) throws RemoteException, WebODEException;

  /**
   * Update the position of an ontology.
   *
   * @param contextOntology contextOntology The context ontology.
   * @param opd The descriptor for the new position.
   */
  void updateOntologyPosition (String contextOntology,  OntologyPositionDescriptor opd)
      throws RemoteException, WebODEException;

  /**
   * Adds a reference.
   *
   * @param name The ontology.
   * @param rd   The reference information.
   */
  void addReference (String name, ReferenceDescriptor rd) throws RemoteException, WebODEException;

  /**
   * Adds a reference to the ontology whose name is specified.
   *
   * @param name The name of the ontology to add a reference to.
   */
  void addReferenceToOntology (String name, ReferenceDescriptor rd) throws RemoteException, WebODEException;

  /**
   * Retrieves the references related to a given ontology by means of a relation <i>ontology has reference</i>.
   *
   * @param name The name of the ontology.
   * @return The list of references.
   */
  ReferenceDescriptor[] getOntologyReferences (String name) throws RemoteException, WebODEException;

  /**
   * Retrieves information for a given reference.
   *
   * @param name The name of the reference.
   * @param ontology The name of the ontology the reference is tied to.
   * @exception WebODEException In case of error (e.g. no reference with that name).
   */
  ReferenceDescriptor getReference (String name, String ontologyName) throws RemoteException, WebODEException;

  /**
   * Disregard a reference of an ontology.
   *
   * @param ontologyName The ontology.
   * @param refName      The reference to untie.
   */
  void removeOntologyReference (String ontologyName, String refName) throws RemoteException, WebODEException;

  /**
   * Updates an reference
   *
   * @param name The name of the reference to be updated.
   * @param rd   The new information for the reference.
   */
  void updateReference (String name, ReferenceDescriptor rd) throws RemoteException, WebODEException;

  /**
   * Gets all the references contained in the ontology.
   *
   * @param name The name of the ontology.
   */
  ReferenceDescriptor[] getReferences (String name) throws RemoteException, WebODEException;

  /**
   * Ties a reference to a term.  Both of them have to exist previously.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param reference The reference.
   */
  void relateReferenceToTerm (String ontology, String term, String reference)
      throws RemoteException, WebODEException;

  /**
   * Ties a reference to a term.  Both of them have to exist previously.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parent   The parent.
   * @param reference The reference.
   */
  public void relateReferenceToTerm (String ontology, String term, String parent, String reference)
      throws RemoteException, WebODEException;

  /**
   * Unties a reference to a term.  Both of them have to exist previously.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param reference The reference.
   */
  void unrelateReferenceToTerm (String ontology, String term, String reference)
      throws RemoteException, WebODEException;

  /**
   * Unties a reference to a term.  Both of them have to exist previously.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parent   The parent term.
   * @param reference The reference.
   */
  void unrelateReferenceToTerm (String ontology, String term, String parent, String reference)
      throws RemoteException, WebODEException;

  /**
   * Gets a term's references. This method must be used for those terms that do not depend on other terms
   * in the ontology, that is, concepts, ad-hoc relations, formulae, group concepts, etc.
   *
   * @param ontology The ontology.
   * @param term     The term's name.
   */
  ReferenceDescriptor[] getTermReferences (String ontology, String term) throws RemoteException, WebODEException;

  /**
   * Gets a term's references. This method must be used for those terms that depend on other terms
   * in the ontology, that is, attributes. It cannot be used, as others, with null form the parent
   * term: this will throw and exception.
   *
   * @param ontology The ontology.
   * @param term     The term's name.
   * @param parent   The parent's name.
   */
  ReferenceDescriptor[] getTermReferences (String ontology, String term,
      String parent) throws RemoteException, WebODEException;

  /**
   * Removes a reference.
   *
   * @param ontology  The ontology.
   * @param reference The name of the reference.
   */
  void removeReference (String ontology, String term) throws RemoteException, WebODEException;


  /**
   * Gets available term types.
   *
   * @return The term types.
   */
  //TermType getTermTypes () throws RemoteException, WebODEException;

  /**
   * Insert a new term.
   *
   * @param ontology The ontology.
   * @param term     The term's name.
   * @param desc     The term's description.
   * @param type     The term's type.
   */
  void insertTerm (String ontology, String term, String desc, int type)
      throws RemoteException, WebODEException; // <<<<<<<<<<< Term...

  /**
   * Get terms.
   *
   * @param ontology The ontology.
   * @param types    The types to retrieve.
   */
  Term[] getTerms (String ontology, int[] types)  throws RemoteException, WebODEException;

  /**
   * Get a term.
   *
   * @param ontology The ontology.
   * @param name     The term.
   */
  Term getTerm (String ontology, String name)  throws RemoteException, WebODEException;

  /**
   * Get a term.
   *
   * @param ontology The ontology.
   * @param name     The term.
   * @param parent   The parent.
   */
  Term getTerm (String ontology, String name, String parent)  throws RemoteException, WebODEException;

  /**
   * Get the parent term of a term. In case of relation instance or instance it returns an instance set.
   * In the case of synonym, abbreviation, class attribute or instance attribue it retunrs a concept.
   * @param ontology The ontology
   * @param term The child parent
   * @return The parent concept.
   */
  Term getParentTerm(String ontology, Term term) throws RemoteException, WebODEException;

  /**
   * Updates a term.
   *
   * @param originalName The original term name.
   * @param td           The new term descriptor.  The type is not taken into account.
   */
  void updateTerm (String originalName, Term td) throws RemoteException, WebODEException;

  /**
   * Updates a term.
   *
   * @param originalName The original term name.
   * @param parent       The term's parent.
   * @param td           The new term descriptor.  The type is not taken into account.
   */
  void updateTerm (String originalName, String parent, Term td) throws RemoteException, WebODEException;


  /**
   * Remove a term.
   *
   * @param ontology
   * @param term
   */
  void removeTerm (String ontology, String term) throws RemoteException, WebODEException;

  /**
   * Remove a term whose parent is the one given.
   *
   * @param ontology
   * @param term
   * @param parent
   */
  void removeTerm (String ontology, String term, String parent) throws RemoteException, WebODEException;

  /**
   * Remove a term whose parent is the one given and possesses a related term (i.e. class attribute or
   * instance attribute).  Intended for synonyms and abbreviations.
   *
   * @param ontology
   * @param term
   * @param parent
   * @param relatedTerm
   */
  void removeTerm (String ontology, String term, String parent, String relatedTerm)
      throws RemoteException, WebODEException;


  /**
   * Inserts a relation between two terms.
   *
   * @param tr The TermRelation.
   */
  void insertTermRelation (TermRelation tr) throws RemoteException, WebODEException;

  /**
   * Gets relation of a given type.
   *
   * @param ontology The ontology.
   * @param name The relation name
   * @param conceptOrigin The origin concept of the relation
   * @param concepDestination The destination concept of the relation
   */
  TermRelation getTermRelation (String ontology, String name, String conceptOrigin, String concepDestination) throws RemoteException, WebODEException;

  /**
   * Gets relation of a given type.
   *
   * @param ontology The ontology.
   * @param subclass Only subclasses?
   */
  TermRelation[] getTermRelations (String ontology, boolean subclass) throws RemoteException, WebODEException;

  /**
   * Gets relation of a given type from an origin concept.
   *
   * @param ontology The ontology.
   * @param origin The origin concepts from the relations.
   * @param subclass Only subclasses?
   */
  public TermRelation[] getTermRelations (String ontology, String origin, boolean subclass) throws RemoteException, WebODEException;
  /**
   * Gets relation of a given type.
   *
   * @param ontology The ontology.
   * @param type     The type of the relation according to <tt>TermRelation</tt>.
   *
   * @see es.upm.fi.dia.ontology.webode.service.TermRelation
   */
  TermRelation[] getTermRelations (String ontology, String type) throws RemoteException, WebODEException;

  /**
   * It obtains all the ad-hoc relations that can be applied to the concept through inheritance.
   * As inheritance, it considers both subclass-of concept taxonomies and disjoint and exhaustive
   * subclass partitions.
   *
   * @author  Oscar Corcho
   * @param ontology The ontology where the concept is defined
   * @param concept The concept for which we wish to obtain the inherited term relations
   * @version 2.0.6
   *
   * @see es.upm.fi.dia.ontology.webode.service.TermRelation
   */
  TermRelation[] getInheritedTermRelations (String ontology, String concept) throws RemoteException, WebODEException;

  /**
   * Remove a relation.
   *
   * @param tr The relation
   */
  void removeTermRelation (TermRelation tr) throws RemoteException, WebODEException;

  /**
   * Get term positions.
   *
   * @param ontology The ontology
   * @param view     The view.
   */
  TermPositionDescriptor[] getTermPositions (String ontology, String view) throws RemoteException, WebODEException;

  /**
   * Insert term position.
   *
   * @param ontology The ontology.
   * @param tp       The term position.
   * @param view     The view.
   */
  void insertTermPosition (String ontology, TermPositionDescriptor tp, String view)
      throws RemoteException, WebODEException;

  /**
   * Update term position.
   *
   * @param ontology The ontology.
   * @param tp       The term position.
   * @param view     The view.
   */
  void updateTermPosition (String ontology, TermPositionDescriptor tp,
                           String view)  throws RemoteException, WebODEException;

  /**
   * Update term position.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param view     The view.
   */
  void removeTermPosition (String ontology, String name,
                           String view)  throws RemoteException, WebODEException;


  /**
   * Get relation positions.
   *
   * @param ontology The ontology
   * @param view     The view.
   */
  TermRelationPositionDescriptor[] getTermRelationPositions (String ontology, String view)
      throws RemoteException, WebODEException;

  /**
   * Inserts the position of a relationship.
   *
   * @param ontology The ontology.
   * @param rp       The relation position.
   * @param view     The view.
   */
  void insertTermRelationPosition (String ontology, TermRelationPositionDescriptor rp, String view)
      throws RemoteException, WebODEException;

  /**
   * Update term relation position.
   *
   * @param ontology The ontology.
   * @param tp       The relation position.
   * @param view     The view.
   */
  void updateTermRelationPosition (String ontology, TermRelationPositionDescriptor rp,
                                   String view)  throws RemoteException, WebODEException;

  /**
   * Removes a term relation position.
   *
   * @param ontology The ontology.
   * @param tp       The relation position.
   * @param view     The view.
   */
  void removeTermRelationPosition (String ontology, TermRelationPositionDescriptor rp,
                                   String view)  throws RemoteException, WebODEException;


  /**
   * Insert a new class attribute.
   *
   * @param ontology The ontology.
   * @param attribute The new class attribute.
   */
  void insertClassAttribute (String ontology, ClassAttributeDescriptor attribute)
      throws RemoteException, WebODEException;

  /**
   * List all available class attributes for a given term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   */
  ClassAttributeDescriptor[] getClassAttributes (String ontology, String name)
      throws RemoteException, WebODEException;

  /**
   * List all available class attributes for a given term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param inheritance Get class attributes from parents.
   */
  ClassAttributeDescriptor[] getClassAttributes (String ontology, String name, boolean inheritance)
      throws RemoteException, WebODEException;

  /**
   * Gets a particular class attribute for a given term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param attribute The attribute's name,
   */
  ClassAttributeDescriptor getClassAttribute (String ontology, String name, String attribute)
      throws RemoteException, WebODEException;

  /**
   * Update an existing class attribute.
   *
   * @param ontology The ontology.
   * @param attribute The old name of the attribute to update.
   * @param parent The parent concept.
   * @param cad The class attribute description.  You cannot update the term of which this term
   *            is a class attribute.   In such a case, you should delete this attribute and create
   *            a new one.
   */
  void updateClassAttribute (String ontology, String attribute, String parent, ClassAttributeDescriptor cad)
      throws RemoteException, WebODEException;


  /**
   * Adds a value to a class attribute.
   *
   * @param ontology The ontology.
   * @param attribute The attribute to add the value to.
   * @param value     The value.
   * @param parent    The parent concept.
   */
  void addValueToClassAttribute (String ontology, String attribute, String parent, String value)
      throws RemoteException, WebODEException;

  /**
   * Removes a value from a class attribute.
   *
   * @param ontology The ontology.
   * @param attribute The attribute to add the value to.
   * @param value     The value.
   * @param parent    The parent concept.
   */
  void removeValueFromClassAttribute (String ontology, String attribute, String parent, String value)
      throws RemoteException, WebODEException;

  /**
   * Insert a new instance attribute.
   *
   * @param ontology The ontology.
   * @param attribute The new instance attribute.
   */
  void insertInstanceAttribute (String ontology, InstanceAttributeDescriptor attribute)
      throws RemoteException, WebODEException;

  /**
   * List all available instance attributes for a given term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   */
  InstanceAttributeDescriptor[] getInstanceAttributes (String ontology, String name)
      throws RemoteException, WebODEException;

  /**
   * Gets all the instance attributes for a given term, included the inheritance attributes.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param inheritance     Look for inheritance attributes too.
   */
  public InstanceAttributeDescriptor[] getInstanceAttributes (String ontology, String name, boolean inheritance)
      throws RemoteException, WebODEException;

  /**
   * Gets a particular instance attribute for a given term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param attribute The attribute's name,
   */
  InstanceAttributeDescriptor getInstanceAttribute (String ontology, String name, String attribute)
      throws RemoteException, WebODEException;

  /**
   * Update an existing instance attribute.
   *
   * @param ontology The ontology.
   * @param attribute The old name of the attribute to update.
   * @param parent The parent concept.
   * @param iad The instance attribute description.  You cannot update the term of which this term
   *            is a instance attribute.   In such a case, you should delete this attribute and create
   *            a new one.
   */
  void updateInstanceAttribute (String ontology, String attribute, String parent, InstanceAttributeDescriptor iad)
      throws RemoteException, WebODEException;

  /**
   * Adds a value to an instance attribute.
   *
   * @param ontology The ontology.
   * @param attribute The attribute to add the value to.
   * @param value     The value.
   * @param parent    The parent concept.
   */
  void addValueToInstanceAttribute (String ontology, String attribute, String parent, String value)
      throws RemoteException, WebODEException;

  /**
   * Removes a value from an instance attribute.
   *
   * @param ontology The ontology.
   * @param attribute The attribute to add the value to.
   * @param value     The value.
   * @param parent    The parent concept.
   */
  void removeValueFromInstanceAttribute (String ontology, String attribute, String parent, String value)
      throws RemoteException, WebODEException;


  /**
   * Adds a new synonym to the given term.
   *
   * @param ontology The ontology.
   * @param sa       The synonym.
   */
  void addSynonymToTerm (String ontology, SynonymAbbreviation sa)
      throws RemoteException, WebODEException;

  /**
   * Returns all the synonyms for a given term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parentTerm The parent.
   * @return All the synonyms or <tt>null</tt> if no one is found.
   */
  SynonymAbbreviation[] getSynonyms (String ontology, String term, String parentTerm)
      throws RemoteException, WebODEException;

  /**
   * Returns a synonym for a given term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parentTerm The parent.
   * @param synonym  The synonym's name.
   * @return The synonym or <tt>null</tt> if no one is found.
   */
  SynonymAbbreviation getSynonym (String ontology, String term, String parentTerm, String synonym)
      throws RemoteException, WebODEException;

  /**
   * Adds a new synonym to the given term.
   *
   * @param ontology The ontology.
   * @param sa       The abbreviation.
   */
  void addAbbreviationToTerm (String ontology, SynonymAbbreviation sa)
      throws RemoteException, WebODEException;

  /**
   * Returns all the abbreviations for a given term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parentTerm The parent.
   * @return All the abbreviations or <tt>null</tt> if no one is found.
   */
  SynonymAbbreviation[] getAbbreviations (String ontology, String term, String parentTerm)
      throws RemoteException, WebODEException;

  /**
   * Returns an abbreviation for a given term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parentTerm The parent.
   * @param abbr     The abbreviation's name.
   * @return The abbreviation or <tt>null</tt> if no one is found.
   */
  SynonymAbbreviation getAbbreviation (String ontology, String term, String parentTerm, String abbr)
      throws RemoteException, WebODEException;

  /**
   * Updates an abbreviation.
   *
   * @param ontology The ontology.
   * @param originalName The original abbreviation's name.
   * @param sa  The new abbreviation information.
   */
  void updateAbbreviation (String ontology, String originalName, SynonymAbbreviation sa)
      throws RemoteException, WebODEException;

  /**
   * Updates a synonym.
   *
   * @param ontology The ontology.
   * @param originalName The original synonym's name.
   * @param sa  The new abbreviation information.
   */
  void updateSynonym (String ontology, String originalName, SynonymAbbreviation sa)
      throws RemoteException, WebODEException;

  /**
   * Deletes an abbreviation.
   *
   * @param ontology The ontology.
   * @param name The abbreviation's name.
   * @param container The term this term is contained in. <tt>null</tt> if is a container (concept).
   * @param relatedTerm The term this is an abbreviation of.
   */
  void deleteAbbreviation (String ontology, String name, String relatedTerm, String container)
      throws RemoteException, WebODEException;

  /**
   * Deletes an synonym.
   *
   * @param ontology The ontology.
   * @param name The synonym's name.
   * @param container The term this term is contained in. <tt>null</tt> if is a container (concept).
   * @param relatedTerm The term this is a synonym of.
   */
  void deleteSynonym (String ontology, String name, String relatedTerm, String container)
      throws RemoteException, WebODEException;

  /**
   * Adds an inference relation among attributes for the given terms.
   *
   * @param ontology The ontology.
   * @param term   The term the attribute <tt>attr</tt> belongs to.
   * @param attr   The attribute that infers.
   * @param iterm  The term the attribute <tt>inferredAttr</tt> belongs to.
   * @param inferredAttr The attribute inferred.
   */
  void addAttributeInferenceRelation (String ontology, String term, String attr, String iterm, String inferredAttr)
      throws RemoteException, WebODEException;

  /**
   * Gets all the attributes inferred by the one specified.
   *
   * @param ontology The ontology.
   * @param term     The attribute's term.
   * @param attr     The attribute's name.
   */
  AttributeInference[] getAttributesInferredBy (String ontology, String term, String attr)
      throws RemoteException, WebODEException;

  /**
   * Gets all the attributes that infer the one specified.
   *
   * @param ontology The ontology.
   * @param term     The attribute's term.
   * @param attr     The attribute's name.
   */
  AttributeInference[] getInferringAttributes (String ontology, String term, String attr)
      throws RemoteException, WebODEException;

  /**
   * Removes an inference relation among attributes for the given terms.
   *
   * @param ontology The ontology.
   * @param term   The term the attribute <tt>attr</tt> belongs to.
   * @param attr   The attribute that infers.
   * @param iterm  The term the attribute <tt>inferredAttr</tt> belongs to.
   * @param inferredAttr The attribute inferred.
   */
  void removeAttributeInferenceRelation (String ontology, String term, String attr, String iterm, String inferredAttr)
      throws RemoteException, WebODEException;

  /**
   * Gets all the imported terms.
   *
   * @param ontology The ontology.
   */
  ImportedTerm[] getImportedTerms (String ontology) throws RemoteException, WebODEException;

  /**
   * Gets an imported term whose name in the ontology is the one specified.
   *
   * @param ontology The ontology.
   * @param name The imported term name, that is xxxx:yyyy.
  */
  ImportedTerm getImportedTerm (String ontology, String name) throws RemoteException, WebODEException;

  /**
   * Imports a term.
   *
   * @param ontology The ontology
   * @param originalName Term's original name.
   * @param name         The new name.
   * @param URL          The URL.
   */
  void importTerm (String ontology, String namespace, String namespace_identifier, String name, String URI)
      throws RemoteException, WebODEException;

  /**
   * Checks whether a term is an imported term in the ontology or not.
   *
   * @param ontology The ontology
   * @param name The concept name.
   */
  boolean isImportedTerm (String ontology, String name)
      throws RemoteException, WebODEException;

  /**
   * Update an imported term
   * @param ontology The ontology
   * @param origianlName The original name of the imported term
   * @param importedTerm The new definition of the imported Term
   */
  void updateImportedTerm(String ontology, String originalName, ImportedTerm importedTerm) throws RemoteException, WebODEException;

  /**
   * Inserts a new constant.
   *
   * @param ontology The ontology.
   * @param cd       The constant descriptor.
   */
  void insertConstant (String ontology, ConstantDescriptor cd)
      throws RemoteException, WebODEException;

  /**
   * Retrieves a constant by name.
   *
   * @param ontology The ontology.
   * @param constant The constant's name.
   */
  ConstantDescriptor getConstant (String ontology, String constant)
      throws RemoteException, WebODEException;

  /**
   * Updates a constant.
   *
   * @param ontology The ontology.
   * @param originalName The originalName.
   * @param cd The constant descriptor.
   */
  void updateConstant (String ontology, String originalName, ConstantDescriptor cd)
      throws RemoteException, WebODEException;

  /**
   * Inserts a new reasoning element.
   *
   * @param fd       The descriptor for the element.
   * @param type     The type of the reasoning element according to the <tt>TermTypes</tt>
   *                 interface.
   */
  void insertReasoningElement (FormulaDescriptor fd)
      throws RemoteException, WebODEException;

  /**
   * Updates an existing reasoning element.
   *
   * @param originalName The original element's name.
   * @param fd  The new reasoning element descriptor.
   */
  void updateReasoningElement (String originalName, FormulaDescriptor fd)
      throws RemoteException, WebODEException;

  /**
   * Gets a given reasoning element.
   *
   * @param ontology The ontology.
   * @param name     The element's name.
   */
  FormulaDescriptor getReasoningElement (String ontology, String name)
      throws RemoteException, WebODEException;

  /**
   * Gets all reasoning elements in the ontology.
   *
   * @param ontology The ontology.
   */
  FormulaDescriptor[] getReasoningElements (String ontology)
      throws RemoteException, WebODEException;

  /**
   * Gets all reasoning elements in the ontology related to a term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   */
  FormulaDescriptor[] getReasoningElements (String ontology, String name)
      throws RemoteException, WebODEException;

  /**
   * Gets all reasoning elements in the ontology related to a term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param parent   The parent term.
   */
  FormulaDescriptor[] getReasoningElements (String ontology, String name, String parent)
      throws RemoteException, WebODEException;

  /**
   * Ties a formula to a term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param formula  The formula.
   */
  void relateFormulaToTerm (String ontology, String term, String formula)
      throws RemoteException, WebODEException;

  /**
   * Ties a formula to a term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parent   The parent term.
   * @param formula  The formula.
   */
  void relateFormulaToTerm (String ontology, String term, String parent, String formula)
      throws RemoteException, WebODEException;

  /**
   * Unties a formula from a term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param formula  The formula.
  */    void unrelateFormulaFromTerm (String ontology, String term, String formula)
      throws RemoteException, WebODEException;

  /**
   * Unties a formula from a term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parent   The parent term.
   * @param formula  The formula.
   */
  void unrelateFormulaFromTerm (String ontology, String term, String parent, String formula)
      throws RemoteException, WebODEException;

  /**
   * Gets all instances for a term.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param name     The term.
   */
  Instance[] getInstances (String ontology, String instanceSet, String name)
      throws RemoteException, WebODEException;

  /**
   * Gets an instance given the ontology and instance set to which it belongs and its name.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param instanceName     The name of the instance to be retrieved.
   */
  Instance getInstance (String ontology, String instanceSet, String instanceName)
      throws RemoteException, WebODEException;

  /**
   * Gets all instances for a given instance set.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   */
  Instance[] getInstInstances (String ontology, String instanceSet)
      throws RemoteException, WebODEException;

  /**
   * Gets all instances for a give instance set.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   */
  Term[] getInstances (String ontology, String instanceSet)
      throws RemoteException, WebODEException;

  /**
   * Gets the values of an instance attribute of an instance
   * @param ontology Ontology name
   * @param instanceSet Instance set name
   * @param instance Instance name
   * @param attribute Attribute name
   */
  String[] getInstanceAttrValues (String ontology,  String instanceSet,
                                  String instance, String attribute)
      throws RemoteException, WebODEException;

  /**
   * Inserts a new instance.
   *
   * @param ontology The ontology
   * @param instance The instance descriptor.
   */
  void insertInstance (String ontology, Instance instance)
      throws RemoteException, WebODEException;

  /**
   * Move one instance from its concept to another concept.
   * All attributes, relation from and to this instance could be removed
   * @param ontology Ontology Name of the instance
   * @param instanceSet Instance Set of the instance
   * @param instanceName Instance Name of the instance
   * @param targetConcept Target concept
   */
  void moveInstance(String ontology, String instanceSet, String instanceName, String targetConcept)
      throws RemoteException, WebODEException;

  /**
   * Gets relation instances.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   */
  TermRelationInstance[] getRelationInstances (String ontology, String instanceSet)
      throws RemoteException, WebODEException;

  /**
   * Gets relation instances from direct instances from a given concept.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param origin The origin concept.
   */
  TermRelationInstance[] getRelationInstances (String ontology, String instanceSet, String origin)
      throws RemoteException, WebODEException;

  /**
   * Gets relation instances from given instance.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param origin The origin instance.
   */
  TermRelationInstance[] getRelationInstancesFromInstance (String ontology, String instanceSet, String origin_instance)
      throws RemoteException, WebODEException;

  /**
   * Gets relation instances which have a given destination instance.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param origin The origin instance.
   */
  TermRelationInstance[] getRelationInstancesToInstance (String ontology, String instanceSet, String dest_instance)
      throws RemoteException, WebODEException;

  /**
   * Inserts a new relation instance.
   *
   * @param ontology The ontology
   * @param instance The instance descriptor.
   */
  void insertRelationInstance (String ontology, TermRelationInstance instance)
      throws RemoteException, WebODEException;

  /**
   * Inserts a new relation instance.
   *
   * @param ontology The ontology
   * @param relationName The relation to be instanceated.
   * @param instanceSet The name of the intance set.
   * @param name The name of the relation instance.
   * @param origin The name of the origin instance.
   * @param destination The name of the destination instance.
   */
  TermRelationInstance insertRelationInstance (String ontology, String relationName, String instanceSet, String name, String origin, String destination)
      throws RemoteException, WebODEException;

  /**
   * Get values of an instance.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param instance The instance.
   * @return a hash table containing attribute name-array of Strings holding the values.
   *         The look-up key is a string array holding the name of the term and the
   *         name of the attribute.
   */
  HashMap getInstanceValues (String ontology, String instanceSet, String instance)
      throws RemoteException, WebODEException;

  /**
   * Get values of an instance.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param instance The instance.
   * @return a hash table containing attribute name-array of Strings holding the values.
   *         The look-up key is a string array holding the name of the term and the
   *         name of the attribute.
   */
  HashMap getLogicalInstanceValues (String ontology, String instanceSet, String instance)
      throws RemoteException, WebODEException;

  /**
   * Add value to an instance.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param instance The instance.
   * @param attribute The attribute.
   * @param term The term.
   * @param value The value.
   */
  void addValueToInstance (String ontology, String instanceSet, String instance,
                           String term, String attribute, String value)
      throws RemoteException, WebODEException;

  /**
   * Removes a value from an instance.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param instance The instance.
   * @param attribute The attribute.
   * @param term The term.
   * @param value The value.
   */
  void removeValueFromInstance (String ontology, String instanceSet, String instance,
                                String term, String attribute, String value)
      throws RemoteException, WebODEException;

  /**
   * Creates a new group.
   *
   * @param ontology The ontology.
   * @param group The group descriptor.
   */
  void addGroup (String ontology, Group group)
      throws RemoteException, WebODEException;

  /**
   * Updates a group.
   *
   * @param ontology The ontology.
   * @param name Previous group name.
   * @param group the new group description.
   */
  void updateGroup (String ontology, String name, Group group)
      throws RemoteException, WebODEException;

  /**
   * Get a given group.
   *
   * @param ontology The ontology;
   * @param name The name.
   */
  Group getGroup (String ontology, String name)
      throws RemoteException, WebODEException;

  /**
   * Gets all available groups.
   *
   * @param ontology The ontology;
   */
  Group[] getGroups (String ontology)
      throws RemoteException, WebODEException;


  // Intermediate Representations
  // --------------------------------------------------------------------------------------
  /**
   * Gets the given intermediate representation.
   *
   * @param ontology The ontology.
   * @param ir       The intermediate representation identifier.
   * @return  The intermediate representation content or <tt>null</tt> if no
   *          elements were found in it.
   * @see   es.upm.fi.dia.ontology.webode.service.irs.IntermediateRepresentation
   */
  IntermediateRepresentation getIntermediateRepresentation (String ontology, int ir)
      throws RemoteException, WebODEException;

  /**
   * This method checks if concept1 is a subclass of concept2 in the ontology. It returns true if it is
   *  either a direct subclass of the concept or if it is an indirect subclass of the concept. It also
   *  takes into account disjoint and exhaustive subclass partitions.
   *
   * @param ontology The ontology in which the terms are defined
   * @param concept1 The child concept
   * @param concept2 The parent concept
   */
  boolean isSubclassOf (String ontology, Term concept1, Term concept2)
      throws WebODEException, RemoteException;

  /**
   * This method returns the names of the concepts that have an attribute attached with the same name
   *
   * @param ontology The ontology in which the attribute is and concepts are defined
   * @param attr The attribute name
   */
  String[] getConceptsInWhichAttributeIsDefined (String ontology, String attr)
      throws WebODEException, RemoteException;

  /**
   * This method returns all the root concepts fron an ontology not included the imported terms.
   *
   * @param ontology The ontology in which the concepts are defined
   * @return Return an array of root concepts of an ontology. It returns null when none concepts has been founded.
   */
  Term[] getRootConcepts (String ontology)
      throws WebODEException, RemoteException;

  /**
   * This method returns all the root concepts fron an ontology
   *
   * @param ontology The ontology in which the concepts are defined
   * @param imported Specified if you the imported concepts are included.
   * @return Return an array of root concepts of an ontology. It returns null when none concepts has been founded.
   */
  Term[] getRootConcepts (String ontology, boolean imported)
      throws WebODEException, RemoteException;

  /**
   * This method returns all the child concepts from a given concept
   *
   * @param ontology The ontology in which the concepts are defined
   * @param concept The parent concept of an hierarchy
   * @return Return an array of child concepts from the specified concept of an ontology. It returns null when none concepts has been founded.
   */
  Term[] getChildConcepts (String ontology, String concept)
      throws WebODEException, RemoteException;

  /**
   * This method returns all parents concepts from a given concept
   *
   * @param ontology The ontology in which the concepts are defined
   * @param concept The child concept of an hierarchy
   * @return Return an array of parent concepts from the specified concept of an ontology. It returns null when concept is a root concept of the ontology.
   */
  Term[] getParentConcepts(String ontology, String concept)
      throws WebODEException, RemoteException;

  /**
   * This method returns all ad-hoc relations directly attached to a concept
   *
   * @param ontology The ontology in which the relations are defined
   * @param concept The concept that is the domain of the relations
   * @return It returns an array of term relations whose domain is the concept specified. It returns null if there are not ad-hoc relations defined for that concept.
   */
  TermRelation[] getAdHocTermRelationsAttachedToConcept(String ontology, String concept)
      throws WebODEException, RemoteException;

  /**
   * Insert a new enumerated value type bounded to an ontology
   * @param ontology The ontology name
   * @param name The name of the enumerated value type
   * @param description The description of the enumerated type
   * @param values The range of values of the enumerated value type
   */
  void insertEnumeratedType(String ontology, String name, String description, String[] values) throws WebODEException, RemoteException;

  /**
   * Remove a enumerated value type from an ontology
   * @param ontology The ontology name
   * @param name The name of the enumerated value type
   */
  void removeEnumeratedType(String ontology, String name) throws WebODEException, RemoteException;

  /**
   * Update a enumerated value type from an ontology
   * @param ontology The ontology name
   * @param old_name The old name of the enumerated value type
   * @param name The new name of the enumerated value type
   * @param values The range of values of the enumerated value type
   */
  void updateEnumeratedType(String ontology, String old_name, String name, String description) throws WebODEException, RemoteException;

  /**
   * Get all enumerated value types defined in an ontology
   * @param ontology The ontology name
   * @return An array of enumerated value types in an ontology. It returns null if it doesn't found any.
   */
  EnumeratedType[] getEnumeratedTypes(String ontology) throws WebODEException, RemoteException;

  /**
   * Get an enumerated value type defined in an ontology
   * @param ontology The ontology name
   * @param name The name of the enumerated value type
   * @return The enumarated data type. It returns null if it doesn't found.
   */
  EnumeratedType getEnumeratedType(String ontology, String name) throws WebODEException, RemoteException;

  /**
   * Add a value to an enumerated type
   * @param ontology The ontology name
   * @param name The enumerated type name
   * @param value The new value
   */
  void addEnumeratedValue(String ontology, String name, String value) throws WebODEException, RemoteException;

  /**
   * Remove a value of an enumerated type
   * @param ontology The ontology name
   * @param name The enumerated type name
   * @param value The value to be removed
   */
  void removeEnumeratedValue(String ontology, String name, String value) throws WebODEException, RemoteException;

  /**
   * Update a value of an enumerated type
   * @param ontology The ontology name
   * @param name The enumerated type name
   * @param old_value The old value
   * @param new_value The new value
   */
  void updateEnumeratedValue(String ontology, String name, String old_value, String new_value) throws WebODEException, RemoteException;

  /**
   * Retrieve a list of instance values refered to a list of a given instances
   * @param ontology Name of the instance set's ontology
   * @param instanceSet Name of the instances' instance set
   * @param instances List of instances
   * @return List of instance values
   */
  HashMap[] getInstancesValues(String ontology, String instanceSet, String[] instances)
      throws RemoteException, WebODEException;

  /**
   * Retrieve a list of relation instance from each instances in a given list of instances
   * @param ontology Name of the instance set's ontology
   * @param instanceSet Name of the instances' instance set
   * @param instances List of instances
   * @return List of relation instances
   */
  TermRelationInstance[][] getRelationInstances (String ontology, String instanceSet, String[] instances)
      throws RemoteException, WebODEException;
}
