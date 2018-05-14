package es.upm.fi.dia.ontology.webode.service;

import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.others.*;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.util.*;
import es.upm.fi.dia.ontology.webode.service.irs.*;

import es.upm.fi.dia.ontology.webode.consistency.*;

/**
 * This class describes the essentials of the ODE service.
 *
 * @author  Julio César Arpírez Vega
 * @version 3.1
 */
public class ODEServiceImp extends MinervaServiceImp implements ODEService {
  protected DatabaseService db;
  protected ConsistencyCacheService cache;

  public ODEServiceImp () throws RemoteException
  {
    super();
  }

  // Life-cycle methods --------------------------------------------------------------------------
  public void start () throws CannotStartException
  {
    // See what databse service to use.
    String dbService = ((ODEServiceConfiguration) config).dbService;
    if (dbService == null)
      throw new CannotStartException ("no database service specified. \nThe service is not properly configured.");

    String cacheService = ((ODEServiceConfiguration) config).cacheService;

    try {
      context.logDebug ("obtaining database service...");
      // Get the service
      db = (DatabaseService) context.getService (dbService);

      context.logDebug ("obtaining consistency cache service...");
      if (cacheService == null || cacheService.equals(""))
        cache = null;
      else
        cache = (ConsistencyCacheService) context.getService (cacheService);

      context.logDebug ("ODE Service (" + context.getName() + ") all right : " + db + ", " + cache + ".");

    } catch (Exception e) {
      throw new CannotStartException ("ODE service cannot start because it cannot obtain a necessary service:\n" +
                                      e.getMessage(), e);
    }
  }

  // Business methods ----------------------------------------------------------------------------

  protected void touch (String ontology) throws RemoteException
  {
    if (cache != null)
      cache.touch (ontology);
  }

  protected void logMethod(String method) {
    if(!((ODEServiceConfiguration)this.config).trace.booleanValue())
      return;
    DBConnection conn=null;
    PreparedStatement pstmt=null;
    try {
      conn=db.getConnection();
      pstmt=conn.prepareStatement("UPDATE ode_benchmark SET count=count+1 WHERE method_name=?");
      pstmt.setString(1,method);
      pstmt.executeUpdate();
    }
    catch (Exception e) {
      System.out.print("-=-=-=-= ERROR EN  BENCHMARK =-=-=-=-");
      e.printStackTrace(System.out);
      System.out.print("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
    }
    finally {
      try {
        if(pstmt!=null) pstmt.close();
        if(conn!=null) db.releaseConnection(conn);
      }
      catch (Exception e) {
        System.out.print("-=-=-=-= ERROR EN  BENCHMARK =-=-=-=-");
        e.printStackTrace(System.out);
        System.out.print("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
      }
    }
  }

  /**
   * Opens an ontology (for caching purposes).
   *
   * @param ontology The ontology to open.
   */
  public void openOntology (String ontology) throws RemoteException, WebODEException
  {
    logMethod("openOntology(java.lang.String)");
    try {
      if(cache!=null)
        cache.loadOntology (ontology);
    } catch (Exception e) {
      throw new WebODEException ("Error caching ontology: " + e.getMessage(),e);
    }
  }

  /**
   * Gets the user's group.
   */
  public String getGroup ()
  {
    logMethod("getGroup()");
    return context.getGroup();
  }

  public String[] getUserGroups()
  {
    logMethod("getUserGroups()");
    return context.getGroups();
  }

  /**
   * Retrieves the list of available ontologies.
   *
   * @return The names of the available ontologies.
   */
  public OntologyDescriptor[] getAvailableOntologies () throws RemoteException, WebODEException
  {
    logMethod("getAvailableOntologies()");
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.
      return OntologyDescriptor.getAvailableOntologies(con = db.getConnection(),
          context.getUser(), context.getGroups());
    } catch (SQLException sqle) {
      throw new WebODEException ("Error retrieving available ontologies: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Retrieves specific data about a given ontology.
   *
   * @param name The name of the ontology.
   */
  public OntologyDescriptor getOntologyDescriptor (String name) throws RemoteException, WebODEException
  {
    logMethod("getOntologyDescriptor(java.lang.String)");
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.
      return OntologyDescriptor.getOntologyDescriptor (con = db.getConnection(), name);
    } catch (SQLException sqle) {
      throw new WebODEException ("Error retrieving available ontologies: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Retrieves the list of ontologies with the same namespace
   * @param namespace
   * @return list of ontologies.
   */
  public OntologyDescriptor[] findOntologyByNamespace (String namespace) throws RemoteException, WebODEException {
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.
      return OntologyDescriptor.findOntologyDescriptorByNamespace (con = db.getConnection(), namespace);
    } catch (SQLException sqle) {
      throw new WebODEException ("Error retrieving available ontologies: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets the ontologies related to an ontology in the context of the
   * first ontology.
   *
   * @param The ontology which represents the context.
   */
  public OntologyRelation[] getRelatedOntologies (String name) throws RemoteException, WebODEException
  {
    logMethod("getRelatedOntologies(java.lang.String)");
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.
      return OntologyRelation.getRelatedOntologies (con = db.getConnection(), name);
    } catch (SQLException sqle) {
      throw new WebODEException ("Error retrieving related ontologies: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Remove related ontologies.
   *
   * @param ontology The ontology in context.
   * @param ontologyToRemove The ontology to be removed in this context.
   */
  public void removeRelatedOntology (String ontology, String ontologyToRemove)
      throws RemoteException, WebODEException
  {
    logMethod("removeRelatedOntology(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.
      OntologyRelation.removeRelatedOntology (con = db.getConnection(), ontology, ontologyToRemove);
    } catch (SQLException sqle) {
      throw new WebODEException ("Error deleting related ontology: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Remove an ontology relation in the context of a given relation.
   *
   * @param ontology The ontology in context.
   * @param relation The relation to remove.
   */
  public void removeOntologyRelation (String ontology, String relation) throws RemoteException, WebODEException
  {
    logMethod("removeOntologyRelation(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.
      OntologyRelation.removeOntologyRelation (con = db.getConnection(), ontology, relation);
    } catch (SQLException sqle) {
      throw new WebODEException ("Error deleting related ontology: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Creates an ontology with the given attributes.
   *
   * @param od The ontology descriptor.
   */
  public void createOntology (OntologyDescriptor od) throws RemoteException, WebODEException
  {
    logMethod("createOntology(es.upm.fi.dia.ontology.webode.service.OntologyDescriptor)");
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.

      // USER AND GROUP FROM CONTEXT IF ODE-SERVICE STARTS FROM A MINERVA USER -----------
      if(!context.getUser().equals("server"))
        od.login     = context.getUser();
      else
        if(od.login==null)
          throw new WebODEException("User of the ontology mustn't be null");
      //od.groupName = context.getGroup();

      od.store (con = db.getConnection());
    } catch (Exception e) {
      throw new WebODEException ("Error creating ontology: " + e.getMessage(), e);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Removes an ontology completely from server.
   *
   * @param name The ontology name.
   */
  public void removeOntology (String name)  throws RemoteException, WebODEException
  {
    logMethod("removeOntology(java.lang.String)");
    DBConnection con = null;
    try {
      new OntologyDescriptor (name, null, null, null, null, null).remove(con = db.getConnection());

      if (cache != null)
        cache.unloadOntology (name);
    } catch (Exception e) {
      throw new WebODEException ("Error deleting ontology: " + e.getMessage(), e);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Modify the data about an ontology.
   * <p>
   * Everything but the creation and modification date can be changed.  These
   * two fields are simply ignored.
   *
   * @param name The name of the ontology to alter.
   * @param od The ontology descriptor with the new data.
   */
  public void updateOntology (String name, OntologyDescriptor od) throws RemoteException, WebODEException
  {
    logMethod("updateOntology(java.lang.String,es.upm.fi.dia.ontology.webode.service.OntologyDescriptor)");
    DBConnection con = null;
    try {
      // Cache
      if (cache != null && !name.equals (od.name))
        cache.updateOntologyName (name, od.name);

      // Delegate in the one who knows how to do it.
      OntologyDescriptor.update (con = db.getConnection(), name, od);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating ontology: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Insert a relation between two ontologies.
   *
   * @param rd The relation descriptor.
   */
  public void createOntologyRelation (OntologyRelation or) throws RemoteException, WebODEException
  {
    logMethod("createOntologyRelation(es.upm.fi.dia.ontology.webode.service.OntologyRelation)");
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.
      or.store (con = db.getConnection(), true, context.getUser(), context.getGroup());
    } catch (SQLException sqle) {
      throw new WebODEException ("Error creating ontology relation: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Creates an ontology to be related to another in the context of the last one.
   *
   * @param ontology The ontology.
   * @param name     The ontology to be related.
   * @param x        The x position
   * @param y        the y position
   */
  public void createOntologyOntology (String ontology, String name, int x, int y)
      throws RemoteException, WebODEException
  {
    logMethod("createOntologyOntology(java.lang.String,java.lang.String,int,int)");
    DBConnection con = null;
    try {
      // If the ontology does not exist, create it.
      try {
        OntologyDescriptor.getOntologyDescriptor (con = db.getConnection(), name);
      } catch (WebODEException me) {
        // Insert ontology
        new OntologyDescriptor (name, "", context.getUser(), context.getGroup(),
                                new java.util.Date(), new java.util.Date()).store(con);
      }
      // Delegate in the one who knows how to do it.
      OntologyPositionDescriptor opd = new OntologyPositionDescriptor (name, x, y);
      opd.store (con, ontology);
    } catch (SQLException sqle) {
      throw new WebODEException ("Error inserting ontology in ontology: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Get positions of ontologies in the context of an ontology.
   *
   * @return An array holding the positions of the elements.  <tt>null</tt> if no one is present.
   */
  public OntologyPositionDescriptor[] getOntologyPositions (String name) throws RemoteException, WebODEException
  {
    logMethod("getOntologyPositions(java.lang.String)");
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.
      return OntologyPositionDescriptor.getOntologyPositions (con = db.getConnection(), name);
    } catch (SQLException sqle) {
      throw new WebODEException ("Error retrieving positions: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Update the position of an ontology.
   *
   * @param contextOntology contextOntology The context ontology.
   * @param opd The descriptor for the new position.
   */
  public void updateOntologyPosition (String contextOntology, OntologyPositionDescriptor opd)
      throws RemoteException, WebODEException
  {
    logMethod("updateOntologyPosition(java.lang.String,es.upm.fi.dia.ontology.webode.service.OntologyPositionDescriptor)");
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.
      opd.update (con = db.getConnection(), contextOntology);
    } catch (SQLException sqle) {
      throw new WebODEException ("Error updating ontology position: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Adds a reference to the ontology whose name is specified.
   *
   * @param name The name of the ontology to add a reference to.
   */
  public void addReferenceToOntology (String name, ReferenceDescriptor rd) throws RemoteException, WebODEException
  {
    logMethod("addReferenceToOntology(java.lang.String,es.upm.fi.dia.ontology.webode.service.ReferenceDescriptor)");
    DBConnection con = null;
    try {
      // Delegate in the one who knows how to do it.
      rd.ontology = name;
      rd.store (con = db.getConnection());
    } catch (Exception sqle) {
      throw new WebODEException ("Error inserting reference: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Adds a reference.
   *
   * @param name The ontology.
   * @param rd   The reference information.
   */
  public void addReference (String name, ReferenceDescriptor rd) throws RemoteException, WebODEException
  {
    logMethod("addReference(java.lang.String,es.upm.fi.dia.ontology.webode.service.ReferenceDescriptor)");
    DBConnection con = null;
    try {
      touch (rd.ontology);

      // Delegate in the one who knows how to do it.
      rd.ontology = name;
      rd.store (con = db.getConnection(), false);
    } catch (Exception sqle) {
      throw new WebODEException ("Error inserting reference: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Retrieves the references related to a given ontology by means of a relation <i>ontology has reference</i>.
   *
   * @param name The name of the ontology.
   * @return The list of references.
   */
  public ReferenceDescriptor[] getOntologyReferences (String name) throws RemoteException, WebODEException
  {
    logMethod("getOntologyReferences(java.lang.String)");
    DBConnection con = null;
    try {
      touch (name);

      return ReferenceDescriptor.getOntologyReferences (con = db.getConnection(), name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error inserting reference: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Retrieves information for a given reference.
   *
   * @param name The name of the reference.
   * @param ontology The name of the ontology the reference is tied to.
   * @exception WebODEException In case of error (e.g. no reference with that name).
   */
  public ReferenceDescriptor getReference (String name, String ontologyName) throws RemoteException, WebODEException
  {
    logMethod("getReference(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontologyName);

      return ReferenceDescriptor.getReference (con = db.getConnection(), name, ontologyName);
    } catch (Exception sqle) {
      throw new WebODEException ("Error obtaining reference: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Disregard a reference of an ontology.
   *
   * @param ontologyName The ontology.
   * @param refName      The reference to untie.
   */
  public void removeOntologyReference (String ontologyName, String refName) throws RemoteException, WebODEException
  {
    logMethod("removeOntologyReference(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontologyName);

      new ReferenceDescriptor (refName, null, ontologyName).
          removeOntologyReference (con = db.getConnection());
    } catch (Exception sqle) {
      throw new WebODEException ("Error deleting reference: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Updates an reference
   *
   * @param name The name of the reference to be updated.
   * @param rd   The new information for the reference.
   */
  public void updateReference (String name, ReferenceDescriptor rd) throws RemoteException, WebODEException
  {
    logMethod("updateReference(java.lang.String,es.upm.fi.dia.ontology.webode.service.ReferenceDescriptor)");
    DBConnection con = null;
    try {
      touch (rd.ontology);

      ReferenceDescriptor.update (con = db.getConnection(), name, rd);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating reference: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets all the references contained in the ontology.
   *
   * @param name The name of the ontology.
   */
  public ReferenceDescriptor[] getReferences (String name) throws RemoteException, WebODEException
  {
    logMethod("getReferences(java.lang.String)");
    DBConnection con = null;
    try {
      touch (name);

      return ReferenceDescriptor.getReferences (con = db.getConnection(), name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving all references: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Ties a reference to a term.  Both of them have to exist previously.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param reference The reference.
   */
  public void relateReferenceToTerm (String ontology, String term, String reference)
      throws RemoteException, WebODEException
  {
    logMethod("relateReferenceToTerm(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      ReferenceDescriptor.relateReferenceToTerm (con = db.getConnection(),
          ontology, reference, term);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving all references: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Ties a reference to a term.  Both of them have to exist previously.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parent   The parent.
   * @param reference The reference.
   */
  public void relateReferenceToTerm (String ontology, String term, String parent, String reference)
      throws RemoteException, WebODEException
  {
    logMethod("relateReferenceToTerm(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      ReferenceDescriptor.relateReferenceToTerm (con = db.getConnection(),
          ontology, reference, term, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving all references: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }



  /**
   * Unties a reference to a term.  Both of them have to exist previously.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param reference The reference.
   */
  public void unrelateReferenceToTerm (String ontology, String term, String reference)
      throws RemoteException, WebODEException
  {
    logMethod("unrelateReferenceToTerm(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      ReferenceDescriptor.unrelateReferenceToTerm (con = db.getConnection(),
          ontology, reference, term);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving all references: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Unties a reference to a term.  Both of them have to exist previously.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parent   The parent term.
   * @param reference The reference.
   */
  public void unrelateReferenceToTerm (String ontology, String term, String parent, String reference)
      throws RemoteException, WebODEException
  {
    logMethod("unrelateReferenceToTerm(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      ReferenceDescriptor.unrelateReferenceToTerm (con = db.getConnection(),
          ontology, reference, term, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving all references: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets a term's references.
   *
   * @param ontology The ontology.
   * @param term     The term's name.
   */
  public ReferenceDescriptor[] getTermReferences (String ontology, String term)
      throws RemoteException, WebODEException
  {
    logMethod("getTermReferences(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return ReferenceDescriptor.getTermReferences (con = db.getConnection(), ontology, term);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving references: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets a term's references.
   *
   * @param ontology The ontology.
   * @param term     The term's name.
   * @param parent   The parent's name.
   */
  public ReferenceDescriptor[] getTermReferences (String ontology, String term,
      String parent) throws RemoteException, WebODEException
  {
    logMethod("getTermReferences(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return ReferenceDescriptor.getTermReferences (con = db.getConnection(), ontology, term, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving references: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }



  /**
   * Removes a reference.
   *
   * @param ontology  The ontology.
   * @param reference The name of the reference.
   */
  public void removeReference (String ontology, String reference) throws RemoteException, WebODEException
  {
    logMethod("removeReference(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      new ReferenceDescriptor(reference, null, ontology).remove(con = db.getConnection());
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing reference: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets available term types.
   *
   * @return The term types.
   */
    /*public TermType getTermTypes () throws RemoteException, WebODEException
    {
 DBConnection con = null;
 try {
     TermType.getTermTypes (con = db.getConnection());
 } catch (Exception sqle) {
     throw new WebODEException ("Error retrieving available term types: " + sqle.getMessage(), sqle);
 } finally {
     if (con != null) db.releaseConnection (con);
 }
 }*/

/**
 * Insert a new term.
 *
 * @param ontology The ontology.
 * @param term     The term's name.
 * @param desc     The term's description.
 * @param type     The term's type.
 */
  public void insertTerm (String ontology, String term, String desc, int type) throws RemoteException, WebODEException
  {
    logMethod("insertTerm(java.lang.String,java.lang.String,java.lang.String,int)");
    DBConnection con = null;
    try {
      // Caching...
      if (cache != null && type == Term.CONCEPT) {
        cache.insertConcept (ontology, term);
      }

      new Term (ontology, term, desc, type).store (con = db.getConnection());
    } catch (Exception sqle) {
      throw new WebODEException ("Error inserting term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Get terms.
   *
   * @param ontology The ontology.
   * @param types    The types to retrieve.
   */
  public Term[] getTerms (String ontology, int[] types)  throws RemoteException, WebODEException
  {
    logMethod("getTerms(java.lang.String,int[])");
    DBConnection con = null;
    try {
      touch (ontology);

      return Term.getTerms (con = db.getConnection(), ontology, types);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving terms: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Get a term.
   *
   * @param ontology The ontology.
   * @param name     The term.
   */
  public Term getTerm (String ontology, String name)  throws RemoteException, WebODEException
  {
    logMethod("getTerm(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return Term.getTerm (con = db.getConnection(), ontology, name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Get a term.
   *
   * @param ontology The ontology.
   * @param name     The term.
   * @param parent   The parent.
   */
  public Term getTerm (String ontology, String name, String parent)  throws RemoteException, WebODEException
  {
    logMethod("getTerm(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return Term.getTerm (con = db.getConnection(), ontology, name, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Get the parent term of a term. In case of relation instance or instance it returns an instance set.
   * In the case of synonym, abbreviation, class attribute or instance attribue it retunrs a concept.
   * @param ontology The ontology
   * @param term The child parent
   * @return The parent concept.
   */
  public Term getParentTerm(String ontology, Term term) throws RemoteException, WebODEException {
//    logMethod("getParentTerm(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      return term.getParent(con=db.getConnection());
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Updates a term.
   *
   * @param originalName The original term name.
   * @param td           The new term descriptor.  The type is not taken into account.
   */
  public void updateTerm (String originalName, Term td) throws RemoteException, WebODEException
  {
    logMethod("updateTerm(java.lang.String,es.upm.fi.dia.ontology.webode.service.Term)");
    DBConnection con = null;
    try {
      if (cache != null && !originalName.equals (td.term))
        cache.updateConcept (td.ontology, originalName, td.term);

      td.update (con = db.getConnection(), originalName);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Updates a term.
   *
   * @param originalName The original term name.
   * @param parent       The term's parent.
   * @param td           The new term descriptor.  The type is not taken into account.
   */
  public void updateTerm (String originalName, String parent, Term td)
      throws RemoteException, WebODEException
  {
    logMethod("updateTerm(java.lang.String,java.lang.String,es.upm.fi.dia.ontology.webode.service.Term)");
    DBConnection con = null;
    try {
      touch (td.ontology);

      td.update (con = db.getConnection(), originalName, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Remove a term.
   *
   * @param ontology
   * @param term
   */
  public void removeTerm (String ontology, String term) throws RemoteException, WebODEException
  {
    logMethod("removeTerm(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      // Caching...
      if (cache != null)
        cache.removeConcept (ontology, term);

      Term.removeTerm (con = db.getConnection(), ontology, term);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Remove a term whose parent is the one given.
   *
   * @param ontology
   * @param term
   * @param parent
   */
  public void removeTerm (String ontology, String term, String parent) throws RemoteException, WebODEException
  {
    logMethod("removeTerm(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      Term.removeTerm (con = db.getConnection(), ontology, term, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Remove a term whose parent is the one given and possesses a related term (i.e. class attribute or
   * instance attribute).  Intended for synonyms and abbreviations.
   *
   * @param ontology
   * @param term
   * @param parent
   * @param relatedTerm
   */
  public void removeTerm (String ontology, String term, String parent, String relatedTerm)
      throws RemoteException, WebODEException
  {
    logMethod("removeTerm(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      Term.removeTerm (con = db.getConnection(), ontology, term, parent, relatedTerm);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Inserts a relation between two terms.
   *
   * @param tr The TermRelation.
   */
  public void insertTermRelation (TermRelation tr) throws RemoteException, WebODEException
  {
    logMethod("removeTerm(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      // Caching...
      if (cache != null)
        cache.insertTermRelation (tr.ontology, tr.name, tr.origin, tr.destination);

      tr.store (con = db.getConnection());
    } catch (Exception sqle) {
      throw new WebODEException ("Error storing term relation: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets relation of a given type.
   *
   * @param ontology The ontology.
   * @param name The relation name
   * @param conceptOrigin The origin concept of the relation
   * @param concepDestination The destination concept of the relation
   */
  public TermRelation getTermRelation (String ontology, String name, String conceptOrigin, String concepDestination) throws RemoteException, WebODEException {
    logMethod("getTermRelation(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      con=db.getConnection();
      return TermRelation.getTermRelation(con,ontology, name, conceptOrigin, concepDestination);
    } catch (Exception sqle) {
      throw new WebODEException ("Error storing term relation: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets relation of a given type.
   *
   * @param ontology The ontology.
   * @param subclass Only subclasses?
   */
  public TermRelation[] getTermRelations (String ontology, boolean subclass) throws RemoteException, WebODEException
  {
    logMethod("getTermRelations(java.lang.String,boolean)");
    DBConnection con = null;
    try {
      touch (ontology);

      return TermRelation.getTermRelations (con = db.getConnection(), ontology, subclass);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving term relations: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets relation of a given type from an origin concept.
   *
   * @param ontology The ontology.
   * @param origin The origin concepts from the relations.
   * @param subclass Only subclasses?
   */
  public TermRelation[] getTermRelations (String ontology, String origin, boolean subclass) throws RemoteException, WebODEException
  {
    logMethod("getTermRelations(java.lang.String,boolean)");
    DBConnection con = null;
    try {
      touch (ontology);

      return TermRelation.getTermRelations (con = db.getConnection(), ontology, origin, subclass);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving term relations: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets relation of a given type.
   *
   * @param ontology The ontology.
   * @param type     The type of the relation according to <tt>TermRelation</tt>.
   *
   * @see es.upm.fi.dia.ontology.webode.service.TermRelation
   */
  public TermRelation[] getTermRelations (String ontology, String type) throws RemoteException, WebODEException
  {
    logMethod("getTermRelations(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return TermRelation.getTermRelations (con = db.getConnection(), ontology, type);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving term relations: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

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
  public TermRelation[] getInheritedTermRelations (String ontology, String concept)
      throws RemoteException, WebODEException
  {
    logMethod("getInheritedTermRelations(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);
      return TermRelation.getInheritedTermRelations (con = db.getConnection(), ontology, concept);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving inherited term relations: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Remove a relation.
   *
   * @param ontology
   * @param tr The relation
   */
  public void removeTermRelation (TermRelation tr) throws RemoteException, WebODEException
  {
    logMethod("removeTermRelation(es.upm.fi.dia.ontology.webode.service.TermRelation)");
    DBConnection con = null;
    try {
      // Caching...
      if (cache != null)
        cache.removeTermRelation (tr.ontology, tr.name, tr.origin, tr.destination);

      tr.remove (con = db.getConnection());
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing term relation: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Get term positions.
   *
   * @param ontology
   * @param view     The view.
   */
  public TermPositionDescriptor[] getTermPositions (String ontology, String view)
      throws RemoteException, WebODEException
  {
    logMethod("getTermPositions(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return TermPositionDescriptor.getTermPositions (con = db.getConnection(), ontology, view);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving term positions: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Insert term position.
   *
   * @param ontology The ontology.
   * @param tp       The term position.
   * @param view     The view.
   */
  public void insertTermPosition (String ontology, TermPositionDescriptor tp,
                                  String view)  throws RemoteException, WebODEException
  {
    logMethod("insertTermPosition(java.lang.String,es.upm.fi.dia.ontology.webode.service.TermPositionDescriptor,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      con = db.getConnection();
      int ontologyId = SQLUtil.getTermId (con, ontology, tp.name, TermTypes.CONCEPT);
      if (ontologyId < 0)
        // Term not present
        new Term (ontology, tp.name, "", Term.CONCEPT).store (con);

      tp.store (con, ontology, view);
    } catch (Exception sqle) {
      throw new WebODEException ("Error storing term position: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Update term position.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param view     The view.
   */
  public void removeTermPosition (String ontology, String name,
                                  String view)  throws RemoteException, WebODEException
  {
    logMethod("removeTermPosition(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      TermPositionDescriptor.remove (con = db.getConnection(), name, ontology, view);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing term position: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Removes a term relation position.
   *
   * @param ontology The ontology.
   * @param tp       The relation position.
   * @param view     The view.
   */
  public void removeTermRelationPosition (String ontology, TermRelationPositionDescriptor rp,
      String view)  throws RemoteException, WebODEException
  {
    logMethod("removeTermRelationPosition(java.lang.String,es.upm.fi.dia.ontology.webode.service.TermRelationPositionDescriptor,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      rp.remove (con = db.getConnection(), ontology, view);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing term relation position: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Inserts the position of a relationship.
   *
   * @param ontology The ontology.
   * @param rp       The relation position.
   * @param view     The view.
   */
  public void insertTermRelationPosition (String ontology, TermRelationPositionDescriptor rp, String view)
      throws RemoteException, WebODEException
  {
    logMethod("insertTermRelationPosition(java.lang.String,es.upm.fi.dia.ontology.webode.service.TermRelationPositionDescriptor,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      con = db.getConnection();

      // If the relationship is not present, store it.
      int relId = SQLUtil.getTermId (con, ontology, rp.name, TermTypes.RELATION);
      if (relId < 0) {
        new TermRelation (ontology, null, rp.name, rp.origin, rp.destination, rp.cardinality).store (con);
      }

      // Store position
      rp.store (con, ontology, view);
    } catch (Exception sqle) {
      throw new WebODEException ("Error storing relation position: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Get relation positions.
   *
   * @param ontology The ontology
   * @param view     The view.
   */
  public TermRelationPositionDescriptor[] getTermRelationPositions (String ontology, String view)
      throws RemoteException, WebODEException
  {
    logMethod("getTermRelationPositions(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return TermRelationPositionDescriptor.getTermRelationPositions (con = db.getConnection(), ontology, view);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving relation positions: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Update term position.
   *
   * @param ontology The ontology.
   * @param tp       The relation position.
   * @param view     The view.
   */
  public void updateTermRelationPosition (String ontology, TermRelationPositionDescriptor rp,
      String view)  throws RemoteException, WebODEException
  {
    logMethod("updateTermRelationPosition(java.lang.String,es.upm.fi.dia.ontology.webode.service.TermRelationPositionDescriptor,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      con = db.getConnection();
      rp.update (con, ontology, view);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating relation position: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Update term position.
   *
   * @param ontology The ontology.
   * @param tp       The term position.
   * @param view     The view.
   */
  public void updateTermPosition (String ontology, TermPositionDescriptor tp, String view)
      throws RemoteException, WebODEException
  {
    logMethod("updateTermPosition(java.lang.String,es.upm.fi.dia.ontology.webode.service.TermPositionDescriptor,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      con = db.getConnection();
      tp.update (con, ontology, view);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating term position: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Insert a new class attribute.
   *
   * @param ontology The ontology.
   * @param attribute The new class attribute.
   */
  public void insertClassAttribute (String ontology, ClassAttributeDescriptor attribute)
      throws RemoteException, WebODEException
  {
    logMethod("insertClassAttribute(java.lang.String,es.upm.fi.dia.ontology.webode.service.ClassAttributeDescriptor)");
    DBConnection con = null;
    try {
      touch (ontology);

      con = db.getConnection();
      attribute.store (con, ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error inserting class attribute: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * List all available class attributes for a given term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   */
  public ClassAttributeDescriptor[] getClassAttributes (String ontology, String name)
      throws RemoteException, WebODEException {
    logMethod("getClassAttributes(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return ClassAttributeDescriptor.getClassAttributes (con = db.getConnection(),
          ontology, name, false);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving class attributes: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * List all available class attributes for a given term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   */
  public ClassAttributeDescriptor[] getClassAttributes (String ontology, String name, boolean inheritance)
      throws RemoteException, WebODEException
  {
    logMethod("getClassAttributes(java.lang.String,java.lang.String,boolean)");
    DBConnection con = null;
    try {
      touch (ontology);

      return ClassAttributeDescriptor.getClassAttributes (con = db.getConnection(),
          ontology, name, inheritance);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving class attributes: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets a particular class attribute for a given term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param attribute The attribute's name,
   */
  public ClassAttributeDescriptor getClassAttribute (String ontology, String name, String attribute)
      throws RemoteException, WebODEException
  {
    logMethod("getClassAttribute(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return ClassAttributeDescriptor.getClassAttribute (con = db.getConnection(),
          ontology, name, attribute);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving class attribute: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

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
  public void updateClassAttribute (String ontology, String attribute, String parent, ClassAttributeDescriptor cad)
      throws RemoteException, WebODEException
  {
    logMethod("updateClassAttribute(java.lang.String,java.lang.String,java.lang.String,es.upm.fi.dia.ontology.webode.service.ClassAttributeDescriptor)");
    DBConnection con = null;
    try {
      touch (ontology);

      cad.update (con = db.getConnection(), ontology, attribute, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating class attribute: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Adds a value to a class attribute.
   *
   * @param ontology The ontology.
   * @param attribute The attribute to add the value to.
   * @param parent    The parent concept.
   * @param value     The value.
   */
  public void addValueToClassAttribute (String ontology, String attribute, String parent, String value)
      throws RemoteException, WebODEException
  {
    logMethod("addValueToClassAttribute(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      ClassAttributeDescriptor cad = ClassAttributeDescriptor.getClassAttribute (con = db.getConnection(),
          ontology, parent, attribute);
      ClassAttributeValue cav = new ClassAttributeValue (cad);
      cav.addValue (con, value, ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error adding class attribute value: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Removes a value from a class attribute.
   *
   * @param ontology The ontology.
   * @param attribute The attribute to add the value to.
   * @param value     The value.
   * @param parent    The parent concept.
   */
  public void removeValueFromClassAttribute (String ontology, String attribute, String parent, String value)
      throws RemoteException, WebODEException
  {
    logMethod("removeValueFromClassAttribute(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      ClassAttributeDescriptor cad = ClassAttributeDescriptor.getClassAttribute (con = db.getConnection(),
          ontology, parent, attribute);
      ClassAttributeValue cav = new ClassAttributeValue (cad);
      cav.removeValue (con, value, ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing class attribute value: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Insert a new instance attribute.
   *
   * @param ontology The ontology.
   * @param attribute The new instance attribute.
   */
  public void insertInstanceAttribute (String ontology, InstanceAttributeDescriptor attribute)
      throws RemoteException, WebODEException
  {
    logMethod("insertInstanceAttribute(java.lang.String,es.upm.fi.dia.ontology.webode.service.InstanceAttributeDescriptor)");
    DBConnection con = null;
    try {
      touch (ontology);

      con = db.getConnection();
      attribute.store (con, ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error inserting instance attribute: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * List all available instance attributes for a given term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   */
  public InstanceAttributeDescriptor[] getInstanceAttributes (String ontology, String name)
      throws RemoteException, WebODEException
  {
    logMethod("getInstanceAttributes(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return InstanceAttributeDescriptor.getInstanceAttributes (con = db.getConnection(),
          ontology, name,false);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instance attributes: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets all the instance attributes for a given term, included the inheritance attributes.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param inheritance     Look for inheritance attributes too.
   */
  public InstanceAttributeDescriptor[] getInstanceAttributes (String ontology, String name, boolean inheritance)
      throws RemoteException, WebODEException
  {
    logMethod("getInstanceAttributes(java.lang.String,java.lang.String,boolean)");
    DBConnection con = null;
    try {
      touch (ontology);

      return InstanceAttributeDescriptor.getInstanceAttributes (con = db.getConnection(),
          ontology, name,inheritance);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instance attributes: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Gets a particular instance attribute for a given term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param attribute The attribute's name,
   */
  public InstanceAttributeDescriptor getInstanceAttribute (String ontology, String name, String attribute)
      throws RemoteException, WebODEException
  {
    logMethod("getInstanceAttribute(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return InstanceAttributeDescriptor.getInstanceAttribute (con = db.getConnection(),
          ontology, name, attribute);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instance attribute: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

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
  public void updateInstanceAttribute (String ontology, String attribute, String parent, InstanceAttributeDescriptor iad)
      throws RemoteException, WebODEException
  {
    logMethod("updateInstanceAttribute(java.lang.String,java.lang.String,java.lang.String,es.upm.fi.dia.ontology.webode.service.InstanceAttributeDescriptor)");
    DBConnection con = null;
    try {
      touch (ontology);

      iad.update (con = db.getConnection(), ontology, attribute, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating instance attribute: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Adds a value to a instance attribute.
   *
   * @param ontology The ontology.
   * @param attribute The attribute to add the value to.
   * @param value     The value.
   * @param parent    The parent concept.
   */
  public void addValueToInstanceAttribute (String ontology, String attribute, String parent, String value)
      throws RemoteException, WebODEException
  {
    logMethod("addValueToInstanceAttribute(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      InstanceAttributeDescriptor iad =
          InstanceAttributeDescriptor.getInstanceAttribute (con = db.getConnection(),
          ontology, parent, attribute);
      ClassAttributeValue cav = new ClassAttributeValue (iad);
      cav.addValue (con, value, ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error adding class attribute value: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Removes a value from an instance attribute.
   *
   * @param ontology The ontology.
   * @param attribute The attribute to add the value to.
   * @param value     The value.
   * @param parent    The parent concept.
   */
  public void removeValueFromInstanceAttribute (String ontology, String attribute, String parent, String value)
      throws RemoteException, WebODEException
  {
    logMethod("removeValueFromInstanceAttribute(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      InstanceAttributeDescriptor iad =
          InstanceAttributeDescriptor.getInstanceAttribute (con = db.getConnection(),
          ontology, parent, attribute);
      ClassAttributeValue cav = new ClassAttributeValue (iad);
      cav.removeValue (con, value, ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing instance attribute value: " + sqle.getMessage(), sqle);

    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Adds a new synonym to the given term.
   *
   * @param ontology The ontology.
   * @param sa       The synonym.
   */
  public void addSynonymToTerm (String ontology, SynonymAbbreviation sa)
      throws RemoteException, WebODEException
  {
    logMethod("addSynonymToTerm(java.lang.String,es.upm.fi.dia.ontology.webode.service.SynonymAbbreviation)");
    DBConnection con = null;
    try {
      touch (ontology);

      sa.type = TermTypes.SYNONYM;
      sa.store (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error adding synonym: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Returns all the synonyms for a given term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parentTerm The parent.
   * @return All the synonyms or <tt>null</tt> if no one is found.
   */
  public SynonymAbbreviation[] getSynonyms (String ontology, String term, String parentTerm)
      throws RemoteException, WebODEException
  {
    logMethod("getSynonyms(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return SynonymAbbreviation.getSynonyms (con = db.getConnection(), ontology, term, parentTerm);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving synonyms: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Returns a synonym for a given term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parentTerm The parent.
   * @param synonym  The synonym's name.
   * @return The synonym or <tt>null</tt> if no one is found.
   */
  public SynonymAbbreviation getSynonym (String ontology, String term, String parentTerm,
      String synonym)
      throws RemoteException, WebODEException
  {
    logMethod("getSynonym(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return SynonymAbbreviation.getSynonym (con = db.getConnection(), ontology, term, parentTerm, synonym);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving synonym: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Adds a new synonym to the given term.
   *
   * @param ontology The ontology.
   * @param sa       The abbreviation.
   */
  public void addAbbreviationToTerm (String ontology, SynonymAbbreviation sa)
      throws RemoteException, WebODEException
  {
    logMethod("addAbbreviationToTerm(java.lang.String,es.upm.fi.dia.ontology.webode.service.SynonymAbbreviation)");
    DBConnection con = null;
    try {
      touch (ontology);

      sa.type = TermTypes.ABBREVIATION;
      sa.store (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error adding abbreviation: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Returns all the abbreviations for a given term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parentTerm The parent.
   * @return All the abbreviations or <tt>null</tt> if no one is found.
   */
  public SynonymAbbreviation[] getAbbreviations (String ontology, String term, String parentTerm)
      throws RemoteException, WebODEException
  {
    logMethod("getAbbreviations(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return SynonymAbbreviation.getAbbreviations (con = db.getConnection(), ontology, term, parentTerm);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving abbreviations: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Returns an abbreviation for a given term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parentTerm The parent term.
   * @param abbr     The abbreviation's name.
   * @return The abbreviation or <tt>null</tt> if no one is found.
   */
  public SynonymAbbreviation getAbbreviation (String ontology, String term, String parentTerm, String abbr)
      throws RemoteException, WebODEException
  {
    logMethod("getAbbreviation(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return SynonymAbbreviation.getAbbreviation (con = db.getConnection(), ontology, term, parentTerm, abbr);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving abbreviations: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Updates an abbreviation.
   *
   * @param ontology The ontology.
   * @param originalName The original abbreviation's name.
   * @param sa  The new abbreviation information.
   */
  public void updateAbbreviation (String ontology, String originalName, SynonymAbbreviation sa)
      throws RemoteException, WebODEException
  {
    logMethod("updateAbbreviation(java.lang.String,java.lang.String,es.upm.fi.dia.ontology.webode.service.SynonymAbbreviation)");
    DBConnection con = null;
    try {
      touch (ontology);

      sa.type = TermTypes.ABBREVIATION;
      sa.update (con = db.getConnection(), ontology, originalName);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating abbreviation: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Updates a synonym.
   *
   * @param ontology The ontology.
   * @param originalName The original synonym's name.
   * @param sa  The new abbreviation information.
   */
  public void updateSynonym (String ontology, String originalName, SynonymAbbreviation sa)
      throws RemoteException, WebODEException
  {
    logMethod("updateSynonym(java.lang.String,java.lang.String,es.upm.fi.dia.ontology.webode.service.SynonymAbbreviation)");
    DBConnection con = null;
    try {
      touch (ontology);

      sa.type = TermTypes.SYNONYM;
      sa.update (con = db.getConnection(), ontology, originalName);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating synonym: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Deletes an abbreviation.
   *
   * @param ontology The ontology.
   * @param name The abbreviation's name.
   * @param container The term this term is contained in. <tt>null</tt> if is a container (concept).
   * @param relatedTerm The term this is an abbreviation of.
   */
  public void deleteAbbreviation (String ontology, String name, String relatedTerm, String container)
      throws RemoteException, WebODEException
  {
    logMethod("deleteAbbreviation(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      SynonymAbbreviation sa = new SynonymAbbreviation (name, relatedTerm, container,
          TermTypes.ABBREVIATION, null);
      sa.remove (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing abbreviation: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Deletes an synonym.
   *
   * @param ontology The ontology.
   * @param name The synonym's name.
   * @param relatedTerm The term this is a synonym of.
   */
  public void deleteSynonym (String ontology, String name, String relatedTerm, String container)
      throws RemoteException, WebODEException
  {
    logMethod("deleteSynonym(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      SynonymAbbreviation sa = new SynonymAbbreviation (name, relatedTerm, container,
          TermTypes.SYNONYM, null);
      sa.remove (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing abbreviation: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Adds an inference relation among attributes for the given terms.
   *
   * @param ontology The ontology.
   * @param term   The term the attribute <tt>attr</tt> belongs to.
   * @param attr   The attribute that infers.
   * @param iterm  The term the attribute <tt>inferredAttr</tt> belongs to.
   * @param inferredAttr The attribute inferred.
   */
  public void addAttributeInferenceRelation (String ontology, String term, String attr,
      String iterm, String inferredAttr)
      throws RemoteException, WebODEException
  {
    logMethod("addAttributeInferenceRelation(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      AttributeInference ai = new AttributeInference (term, attr, iterm, inferredAttr);
      ai.store (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error inserting attribute inference relationship: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets all the attributes inferred by the one specified.
   *
   * @param ontology The ontology.
   * @param term     The attribute's term.
   * @param attr     The attribute's name.
   */
  public AttributeInference[] getAttributesInferredBy (String ontology, String term, String attr)
      throws RemoteException, WebODEException
  {
    logMethod("getAttributesInferredBy(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return AttributeInference.getAttributesInferredBy (con = db.getConnection(),
          ontology, term, attr);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving attribute inference relationship: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Gets all the attributes that infer the one specified.
   *
   * @param ontology The ontology.
   * @param term     The attribute's term.
   * @param attr     The attribute's name.
   */
  public AttributeInference[] getInferringAttributes (String ontology, String term, String attr)
      throws RemoteException, WebODEException
  {
    logMethod("getInferringAttributes(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return AttributeInference.getInferringAttributes (con = db.getConnection(),
          ontology, term, attr);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving attribute inference relationship: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Removes an inference relation among attributes for the given terms.
   *
   * @param ontology The ontology.
   * @param term   The term the attribute <tt>attr</tt> belongs to.
   * @param attr   The attribute that infers.
   * @param iterm  The term the attribute <tt>inferredAttr</tt> belongs to.
   * @param inferredAttr The attribute inferred.
   */
  public void removeAttributeInferenceRelation (String ontology, String term, String attr,
      String iterm, String inferredAttr)
      throws RemoteException, WebODEException
  {
    logMethod("removeAttributeInferenceRelation(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      AttributeInference ai = new AttributeInference (term, attr, iterm, inferredAttr);
      ai.remove (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error deleting attribute inference relationship: " + sqle.getMessage(), sqle);
    } finally {
        if (con != null) db.releaseConnection (con);
      }
  }

  /**
   * Gets all the imported terms.
   *
   * @param ontology The ontology.
   */
  public ImportedTerm[] getImportedTerms (String ontology) throws RemoteException, WebODEException
  {
    DBConnection con = null;
    try {
      touch (ontology);

      return ImportedTerm.getImportedTerms (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving imported terms: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
      logMethod("getImportedTerms(java.lang.String)");
    }
  }

  /**
   * Gets an imported term whose name in the ontology is the one specified.
   *
   * @param ontology The ontology.
   * @param name The imported term name, that is xxxx:yyyy.
  */
  public ImportedTerm getImportedTerm (String ontology, String name) throws RemoteException, WebODEException
  {
    logMethod("getImportedTerm(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return ImportedTerm.getImportedTerm (con = db.getConnection(), ontology, name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving imported term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Imports a term.
   *
   * @param ontology The ontology
   * @param namespace The namespace.
   * @param namespace_identifier The namespace identifier.
   * @param name         The new name.
   * @param URI          The URL.
   */
  public void importTerm (String ontology, String namespace, String namespace_identifier, String name, String URI)
      throws RemoteException, WebODEException
  {
    logMethod("importTerm(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      // Caching...
      if (cache != null)
        cache.insertConcept (ontology, name);

      new ImportedTerm (namespace, namespace_identifier, name, URI).store (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error storing imported term: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Checks whether a term is an imported term in the ontology or not.
   *
   * @param ontology The ontology
   * @param name The concept name.
   */
  public boolean isImportedTerm (String ontology, String name)
      throws RemoteException, WebODEException
  {
    logMethod("isImportedTerm(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      return ImportedTerm.isImportedTerm(con = db.getConnection(), ontology, name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error checking whether a term is imported or not: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Update an imported term
   * @param ontology The ontology
   * @param originalName The original name of the imported term
   * @param importedTerm The new definition of the imported Term
   */
  public void updateImportedTerm(String ontology, String originalName, ImportedTerm importedTerm) throws RemoteException, WebODEException {
    logMethod("updateImportedTerm(java.lang.String,java.lang.String,es.upm.fi.dia.ontology.webode.service.ImportedTerm)");
    DBConnection con = null;
    try {
      importedTerm.update (con = db.getConnection(), ontology, originalName);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating constant: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Inserts a new constant.
   *
   * @param ontology The ontology.
   * @param cd       The constant descriptor.
   */
  public void insertConstant (String ontology, ConstantDescriptor cd)
      throws RemoteException, WebODEException
  {
    logMethod("insertConstant(java.lang.String,es.upm.fi.dia.ontology.webode.service.ConstantDescriptor)");
    DBConnection con = null;
    try {
      touch (ontology);

      cd.store (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error storing constant: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Retrieves a constant by name.
   *
   * @param ontology The ontology.
   * @param constant The constant's name.
   */
  public ConstantDescriptor getConstant (String ontology, String constant)
      throws RemoteException, WebODEException
  {
    logMethod("getConstant(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return ConstantDescriptor.getConstant (con = db.getConnection(), ontology, constant);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving constant: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Updates a constant.
   *
   * @param ontology The ontology.
   * @param originalName The originalName.
   * @param cd The constant descriptor.
   */
  public void updateConstant (String ontology, String originalName, ConstantDescriptor cd)
      throws RemoteException, WebODEException
  {
    logMethod("updateConstant(java.lang.String,java.lang.String,es.upm.fi.dia.ontology.webode.service.ConstantDescriptor)");
    DBConnection con = null;
    try {
      cd.update (con = db.getConnection(), ontology, originalName);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating constant: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Inserts a new reasoning element.
   *
   * @param fd The descriptor for the element.
   * @param type     The type of the reasoning element according to the <tt>TermTypes</tt>
   *                 interface.
   */
  public void insertReasoningElement (FormulaDescriptor fd)
      throws RemoteException, WebODEException
  {
    logMethod("insertReasoningElement(es.upm.fi.dia.ontology.webode.service.FormulaDescriptor)");
    DBConnection con = null;
    try {
      touch (fd.ontology);

      fd.store (con = db.getConnection());
    } catch (Exception sqle) {
      throw new WebODEException ("Error inserting reasoning element: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Updates an existing reasoning element.
   *
   * @param originalName The original element's name.
   * @param fd  The new reasoning element descriptor.
   */
  public void updateReasoningElement (String originalName, FormulaDescriptor fd)
      throws RemoteException, WebODEException
  {
    logMethod("updateReasoningElement(java.lang.String,es.upm.fi.dia.ontology.webode.service.FormulaDescriptor)");
    DBConnection con = null;
    try {
      touch (fd.ontology);

      fd.update (con = db.getConnection(), originalName);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating reasoning element: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets a given reasoining element.
   *
   * @param ontology The ontology.
   * @param name     The element's name.
   */
  public FormulaDescriptor getReasoningElement (String ontology, String name)
      throws RemoteException, WebODEException
  {
    logMethod("getReasoningElement(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return FormulaDescriptor.getReasoningElement (con = db.getConnection(), ontology, name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving reasoning element: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets all reasoning elements in the ontology.
   *
   * @param ontology The ontology.
   * @param name     The element's name.
   */
  public FormulaDescriptor[] getReasoningElements (String ontology)
      throws RemoteException, WebODEException
  {
    logMethod("getReasoningElements(java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return FormulaDescriptor.getReasoningElements (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving reasoning elements: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets all reasoning elements in the ontology related to a term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   */
  public FormulaDescriptor[] getReasoningElements (String ontology, String name)
      throws RemoteException, WebODEException
  {
    logMethod("getReasoningElements(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return FormulaDescriptor.getReasoningElements (con = db.getConnection(), ontology, name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving reasoning elements: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets all reasoning elements in the ontology related to a term.
   *
   * @param ontology The ontology.
   * @param name     The term's name.
   * @param parent   The parent term.
   */
  public FormulaDescriptor[] getReasoningElements (String ontology, String name, String parent)
      throws RemoteException, WebODEException
  {
    logMethod("getReasoningElements(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return FormulaDescriptor.getReasoningElements (con = db.getConnection(), ontology, name, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving reasoning elements: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Ties a formula to a term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param formula  The formula.
   */
  public void relateFormulaToTerm (String ontology, String term, String formula)
      throws RemoteException, WebODEException
  {
    logMethod("relateFormulaToTerm(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      FormulaDescriptor.relateFormulaToTerm (con = db.getConnection(), ontology, formula, term);
    } catch (Exception sqle) {
      throw new WebODEException ("Error  relating reasoning element: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Ties a formula to a term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parent   The parent term.
   * @param formula  The formula.
   */
  public void relateFormulaToTerm (String ontology, String term, String parent, String formula)
      throws RemoteException, WebODEException
  {
    logMethod("relateFormulaToTerm(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      FormulaDescriptor.relateFormulaToTerm (con = db.getConnection(), ontology, formula, term, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error  relating reasoning element: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Unties a formula from a term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param formula  The formula.
   */
  public void unrelateFormulaFromTerm (String ontology, String term, String formula)
      throws RemoteException, WebODEException
  {
    logMethod("relateFormulaToTerm(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      FormulaDescriptor.unrelateFormulaFromTerm (con = db.getConnection(), ontology, formula, term);
    } catch (Exception sqle) {
      throw new WebODEException ("Error unrelating reasoning element: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Unties a formula from a term.
   *
   * @param ontology The ontology.
   * @param term     The term.
   * @param parent   The parent term.
   * @param formula  The formula.
   */
  public void unrelateFormulaFromTerm (String ontology, String term, String parent, String formula)
      throws RemoteException, WebODEException
  {
    logMethod("unrelateFormulaFromTerm(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      FormulaDescriptor.unrelateFormulaFromTerm (con = db.getConnection(), ontology, formula, term, parent);
    } catch (Exception sqle) {
      throw new WebODEException ("Error unrelating reasoning element: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets all instances for a give instance set.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   */
  public Term[] getInstances (String ontology, String instanceSet)
      throws RemoteException, WebODEException
  {
    logMethod("getInstances(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return Term.getTerms (con = db.getConnection(), ontology, instanceSet);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instances: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Gets an instance given the ontology and instance set to which it belongs and its name.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param instanceName The name of the instance set to be retrieved.
   */
  public Instance getInstance (String ontology, String instanceSet, String instanceName)
      throws RemoteException, WebODEException
  {
    //RAUL!!! logMethod("getInstances(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);
      return Instance.getInstance (con = db.getConnection(), ontology, instanceSet, instanceName);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving an instance: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets all instances for a give instance set.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   */
  public Instance[] getInstInstances (String ontology, String instanceSet)
      throws RemoteException, WebODEException
  {
    logMethod("getInstInstances(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return Instance.getInstances (con = db.getConnection(), ontology, instanceSet);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instances: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  /**
   * Gets all instances for a term.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param name     The term.
   */
  public Instance[] getInstances (String ontology, String instanceSet, String name)
      throws RemoteException, WebODEException
  {
    logMethod("getInstances(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return Instance.getInstances (con = db.getConnection(), ontology, instanceSet, name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instances: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Move one instance from its concept to another concept.
   * All attributes, relation from and to this instance could be removed
   * @param ontology Ontology Name of the instance
   * @param instanceSet Instance Set of the instance
   * @param instanceName Instance Name of the instance
   * @param targetConcept Target concept
   */
  public void moveInstance(String ontology, String instanceSet, String instanceName, String targetConcept)
      throws RemoteException, WebODEException {
    logMethod("moveInstance(java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      Instance.moveInstance(con = db.getConnection(), ontology, instanceSet, instanceName, targetConcept);
    } catch (Exception sqle) {
      throw new WebODEException ("Error moving the instance: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets the values of an instance attribute of an instance
   * @param ontology Ontology name
   * @param instanceSet Instance set name
   * @param instance Instance name
   * @param attribute Attribute name
   */
  public String[] getInstanceAttrValues (String ontology,  String instanceSet,
      String instance, String attribute)
      throws RemoteException, WebODEException
  {
    //RAUL!!! logMethod("getInstances(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);
      return Instance.getInstanceAttrValues (con = db.getConnection(), ontology,
          instanceSet, instance, attribute);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instance attribute values: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }





  /**
   * Inserts a new instance.
   *
   * @param ontology The ontology
   * @param instance The instance descriptor.
   */
  public void insertInstance (String ontology, Instance instance)
      throws RemoteException, WebODEException
  {
    logMethod("insertInstance(java.lang.String,es.upm.fi.dia.ontology.webode.service.Instance)");
    DBConnection con = null;
    try {
      touch (ontology);

      instance.store (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error inserting instance: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets relation instances.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   */
  public TermRelationInstance[] getRelationInstances (String ontology, String instanceSet)
      throws RemoteException, WebODEException
  {
    logMethod("getRelationInstances(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return TermRelationInstance.getRelationInstances (con = db.getConnection(), ontology, instanceSet);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instances: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets relation instances from direct instances from a given concept.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param origin The origin concept.
   */
  public TermRelationInstance[] getRelationInstances (String ontology, String instanceSet, String origin)
      throws RemoteException, WebODEException
  {
    logMethod("getRelationInstances(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return TermRelationInstance.getRelationInstances (con = db.getConnection(),
          ontology, instanceSet, origin);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instances: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets relation instances from given instance.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param origin The origin instance.
   */
  public TermRelationInstance[] getRelationInstancesFromInstance (String ontology,
      String instanceSet, String origin_instance)
      throws RemoteException, WebODEException {
    logMethod("getRelationInstancesFromInstance(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return TermRelationInstance.getRelationInstancesFromInstance (con = db.getConnection(),
          ontology, instanceSet, origin_instance);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instances: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets relation instances which have a given destination instance.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param origin The origin instance.
   */
  public TermRelationInstance[] getRelationInstancesToInstance (String ontology, String instanceSet, String dest_instance)
      throws RemoteException, WebODEException {
    logMethod("getRelationInstancesToInstance(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return TermRelationInstance.getRelationInstancesToInstance (con = db.getConnection(),
          ontology, instanceSet, dest_instance);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instances: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Inserts a new relation instance.
   *
   * @param ontology The ontology
   * @param instance The instance descriptor.
   */
  public void insertRelationInstance (String ontology, TermRelationInstance instance)
      throws RemoteException, WebODEException
  {
    logMethod("insertRelationInstance(java.lang.String,es.upm.fi.dia.ontology.webode.service.TermRelationInstance)");
    DBConnection con = null;
    try {
      touch (ontology);

      instance.store (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error storing relation instance: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

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
  public TermRelationInstance insertRelationInstance (String ontology, String relationName, String instanceSet, String name, String origin, String destination)
      throws RemoteException, WebODEException {

    logMethod("insertRelationInstance(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    TermRelationInstance tri=null;
    DBConnection con = null;
    try {
      con = db.getConnection();
      touch (ontology);
      Instance originInstance=Instance.getInstance(con,ontology,instanceSet,origin);
      if(originInstance==null)
        throw new WebODEException("None instance '" + origin + "' could be found in the instance set '" + instanceSet + "' in the ontology '" + ontology + "'.");
      Instance destinationInstance=Instance.getInstance(con,ontology,instanceSet,destination);
      if(destinationInstance==null)
        throw new WebODEException("None instance '" + destination + "' could be found in the instance set '" + instanceSet + "' in the ontology '" + ontology + "'.");

      TermRelation rel=TermRelation.getTermRelation(con,ontology,relationName,originInstance.term,destinationInstance.term);
      if(rel==null) {
        ArrayList concepts=new ArrayList();
        concepts.add(originInstance.term);
        String concept;
        InstanceAttributeDescriptor iad=null;
        Term[] parents;
        while(iad==null && concepts.size()>0) {
          concept=(String)concepts.remove(0);
          parents=Concept.getParentConcepts(con,ontology,concept);
          for(int i=0; parents!=null && i<parents.length; i++)
            concepts.add(parents[i].term);
          iad=InstanceAttributeDescriptor.getInstanceAttribute(con,ontology,concept,relationName);
/*          if(iad!=null && iad.valueType!=ValueTypes.URL)
            iad=null;*/
        }
        if(iad==null)
          throw new WebODEException("None relation '" + relationName + "' could be found from concept '" + originInstance.term + "' in ontology '" + ontology + "'");
        Instance.addInstanceValue(con, ontology, instanceSet, iad.termName, origin, iad.name, destination);
      }
      else {
        (tri=new TermRelationInstance(name,rel,instanceSet,"",origin,destination)).store(con,ontology);
      }
      return tri;

    } catch (Exception sqle) {
      throw new WebODEException ("Error storing relation instance: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }

  }

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
  public HashMap getInstanceValues (String ontology, String instanceSet, String instance)
      throws RemoteException, WebODEException
  {
    logMethod("getInstanceValues(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return Instance.getInstanceValue (con = db.getConnection(), ontology, instanceSet, instance);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instance values: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

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
  public HashMap getLogicalInstanceValues (String ontology, String instanceSet, String instance)
      throws RemoteException, WebODEException
  {
    logMethod("getLogicalInstanceValues(java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return Instance.getLogicalInstanceValue (con = db.getConnection(), ontology, instanceSet, instance);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving instance values: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Add value to an instance.
   *
   * @param ontology The ontology.
   * @param instanceSet The active instance set.
   * @param instance The instance.
   * @param term The term.
   * @param value The value.
   */
  public void addValueToInstance (String ontology, String instanceSet, String instance,
                                  String term, String attribute, String value)
      throws RemoteException, WebODEException
  {
    logMethod("addValueToInstance(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      Instance.addInstanceValue (con = db.getConnection(), ontology, instanceSet, term,
                                 instance, attribute, value);
    } catch (Exception sqle) {
      throw new WebODEException ("Error adding instance values: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

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
  public void removeValueFromInstance (String ontology, String instanceSet, String instance,
                                       String term, String attribute, String value)
      throws RemoteException, WebODEException
  {
    logMethod("removeValueFromInstance(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      Instance.removeInstanceValue (con = db.getConnection(), ontology, instanceSet, term,
                                    instance, attribute, value);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing instance values: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Creates a new group.
   *
   * @param ontology The ontology.
   * @param group The group descriptor.
   */
  public void addGroup (String ontology, Group group)
      throws RemoteException, WebODEException
  {
    logMethod("addGroup(java.lang.String,es.upm.fi.dia.ontology.webode.service.Group)");
    DBConnection con = null;
    try {
      // Caching...
      if (cache != null)
        cache.insertGroup (ontology, group);

      group.store (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error storing group: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Updates a group.
   *
   * @param ontology The ontology.
   * @param name Previous group name.
   * @param group the new group description.
   */
  public void updateGroup (String ontology, String name, Group group)
      throws RemoteException, WebODEException
  {
    logMethod("updateGroup(java.lang.String,java.lang.String,es.upm.fi.dia.ontology.webode.service.Group)");
    DBConnection con = null;
    try {
      // Caching...
      if (cache != null)
        cache.updateGroup (ontology, name, group);

      group.update (con = db.getConnection(), ontology, name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating group: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Get a given group.
   *
   * @param ontology The ontology;
   * @param name The name.
   */
  public Group getGroup (String ontology, String name)
      throws RemoteException, WebODEException
  {
    logMethod("getGroup(java.lang.String,java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return Group.getGroup (con = db.getConnection(), ontology, name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving group: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Gets all available groups.
   *
   * @param ontology The ontology;
   */
  public Group[] getGroups (String ontology)
      throws RemoteException, WebODEException
  {
    logMethod("getGroups(java.lang.String)");
    DBConnection con = null;
    try {
      touch (ontology);

      return Group.getGroups (con = db.getConnection(), ontology);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving groups: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }


  //-------------------------------------------------------------------
  /**
   * Gets the given intermediate representation.
   *
   * @param ontology The ontology.
   * @param ir       The intermediate representation identifier.
   * @return  The intermediate representation content or <tt>null</tt> if no
   *          elements were found in it.
   * @see   es.upm.fi.dia.ontology.webode.service.irs.IntermediateRepresentation
   */
  public IntermediateRepresentation getIntermediateRepresentation (String ontology, int ir)
      throws RemoteException, WebODEException
  {
    logMethod("getIntermediateRepresentation(java.lang.String,int)");
    DBConnection con = null;
    try {
      touch (ontology);

      con = db.getConnection();
      return IntermediateRepresentation.getIR (con, ontology, ir);
    } catch (Exception sqle) {
      throw new WebODEException ("Error retrieving intermediate representation: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * This method checks if concept1 is a subclass of concept2 in the ontology. It returns true if it is
   *  either a direct subclass of the concept or if it is an indirect subclass of the concept. It also
   *  takes into account disjoint and exhaustive subclass partitions.
   *  CAMBIAR PARA QUE UTILICE LA BASE DE DATOS INSTEAD!!!!
   *
   * @param ontology The ontology in which the terms are defined
   * @param concept1 The child concept
   * @param concept2 The parent concept
   */
  public boolean isSubclassOf (String ontology, Term concept1, Term concept2)
      throws WebODEException, RemoteException
  {
    logMethod("isSubclassOf(java.lang.String,es.upm.fi.dia.ontology.webode.service.Term,es.upm.fi.dia.ontology.webode.service.Term)");
    DBConnection conn=null;
    try {
      return Concept.isAccesible(conn=db.getConnection(), ontology, concept2.term, concept1.term);
    }catch (Exception e){
      throw new WebODEException("isSubclassOf" + e, e);
    }
    finally {
      if(conn!=null) db.releaseConnection(conn);
    }
  }


  /**
   * This method returns the names of the concepts that have an attribute attached with the same name
   *
   * @param ontology The ontology in which the attribute is and concepts are defined
   * @param attr The attribute name
   */
  public String[] getConceptsInWhichAttributeIsDefined (String ontology, String attr)
      throws WebODEException, RemoteException
  {
    logMethod("getConceptsInWhichAttributeIsDefined(java.lang.String,java.lang.String)");
    DBConnection con = db.getConnection();
    Vector v = new Vector (200);

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select concept.name " +
                                    " from ode_terms_glossary concept, ode_terms_glossary attr " +
                                    " where " +
                                    "  attr.name = ? and concept.ontology_id = ? and " +
                                    "  attr.parent_id = concept.term_id " +
                                    "order by concept.name");
      pstmt.setString (1, attr);
      pstmt.setInt    (2, SQLUtil.getOntologyId (con, ontology));
      rset = pstmt.executeQuery ();

      if (rset.next()) {
        do {  	v.addElement (rset.getString(1));	} while (rset.next());
      }

      pstmt = con.prepareStatement ("select concept.name " +
                                    "from ode_terms_glossary concept, ode_terms_glossary attr " +
                                    "where " +
                                    "  attr.name = ? and concept.ontology_id = ? and " +
                                    "  attr.parent_id = concept.term_id " +
                                    " order by concept.name");
      pstmt.setString (1, attr);
      pstmt.setInt    (2, SQLUtil.getOntologyId (con, ontology));
      rset = pstmt.executeQuery ();

      if (rset.next()) {
        do {  	v.addElement (rset.getString(1));	} while (rset.next());
      }

      if (v.size()>0){
        String[] concepts = new String[v.size()];
        v.copyInto (concepts);
        return concepts;
      } else return null;
    } catch (SQLException e) {
      throw new WebODEException("Exception in ODEExtension (getConceptsInWhichAttributeIsDefined): " + e.getMessage(), e);
    } finally {
      try {
        if (con != null) db.releaseConnection (con);
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        }catch (Exception e) {}
    }
  }

  /**
   * This method returns all the root concepts fron an ontology not included the imported terms.
   *
   * @param ontology The ontology in which the concepts are defined
   * @return Return an array of root concepts of an ontology. It returns null when none concepts has been founded.
   */
  public Term[] getRootConcepts (String ontology)
      throws WebODEException, RemoteException {
    logMethod("getRootConcepts(java.lang.String)");
    return getRootConcepts(ontology,false);
  }

  /**
   * This method returns all the root concepts fron an ontology
   *
   * @param ontology The ontology in which the concepts are defined
   * @param imported Specified if you the imported concepts are included.
   * @return Return an array of root concepts of an ontology. It returns null when none concepts has been founded.
   */
  public Term[] getRootConcepts (String ontology, boolean imported)
      throws WebODEException, RemoteException {
    logMethod("getRootConcepts(java.lang.String,boolean)");
    DBConnection con = db.getConnection();
    try {
      return Concept.getRootConcepts(con,ontology,imported);
    }
    catch(SQLException e) {
      throw new WebODEException("Exception in ODEExtension (getRootConcepts): " + e.getMessage(), e);
    }
    finally {
      try {
        if (con != null) db.releaseConnection (con);
      }
      catch (Exception e) {
      }
    }
  }

  /**
   * This method returns all the child concepts from a given concept
   *
   * @param ontology The ontology in which the concepts are defined
   * @param concept The parent concept of an hierarchy
   * @return Return an array of child concepts from the specified concept of an ontology. It returns null when none concepts has been founded.
   */
  public Term[] getChildConcepts (String ontology, String concept)
      throws WebODEException, RemoteException {
    logMethod("getChildConcepts(java.lang.String,java.lang.String)");
    DBConnection con = db.getConnection();
    try {
      return Concept.getChildConcepts(con,ontology,concept);
    }
    catch(SQLException e) {
      throw new WebODEException("Exception in ODEExtension (getChildConcepts): " + e.getMessage(), e);
    }
    finally {
      try {
        if (con != null) db.releaseConnection (con);
      }
      catch (Exception e) {
      }
    }
  }

  /**
   * This method returns all parents concepts from a given concept
   *
   * @param ontology The ontology in which the concepts are defined
   * @param concept The child concept of an hierarchy
   * @return Return an array of parent concepts from the specified concept of an ontology. It returns null when concept is a root concept of the ontology.
   */
  public Term[] getParentConcepts(String ontology, String concept)
      throws WebODEException, RemoteException  {
    logMethod("getParentConcepts(java.lang.String,java.lang.String)");
    DBConnection con = db.getConnection();
    try {
      return Concept.getParentConcepts(con,ontology,concept);
    }
    catch(SQLException e) {
      throw new WebODEException("Exception in ODE (getParentConcepts): " + e.getMessage(), e);
    }
    finally {
      try {
        if (con != null) db.releaseConnection (con);
      }
      catch (Exception e) {
      }
    }
  }


  /**
   * This method returns all ad-hoc relations directly attached to a concept
   *
   * @param ontology The ontology in which the relations are defined
   * @param concept The concept that is the domain of the relations
   * @return It returns an array of term relations whose domain is the concept specified. It returns null if there are not ad-hoc relations defined for that concept.
   */
  public TermRelation[] getAdHocTermRelationsAttachedToConcept(String ontology, String concept)
      throws WebODEException, RemoteException {
    logMethod("getAdHocTermRelationsAttachedToConcept(java.lang.String,java.lang.String)");
    DBConnection con = db.getConnection();
    try {
      return TermRelation.getAdHocTermRelationsAttachedToConcept(con,ontology,concept);
    }
    catch(SQLException e) {
      throw new WebODEException("Exception in ODE (getAdHocTermRelationsAttachedToConcept): " + e.getMessage(), e);
    }
    finally {
      try {
        if (con != null) db.releaseConnection (con);
      }
      catch (Exception e) {
      }
    }
  }

  /**
   * Insert a new enumerated value type bounded to an ontology
   * @param ontology The ontology name
   * @param name The name of the enumerated value type
   * @param values The range of values of the enumerated value type
   */
  public void insertEnumeratedType(String ontology, String name, String description, String[] values) throws WebODEException, RemoteException {
    logMethod("insertEnumeratedType(java.lang.String,java.lang.String,java.lang.String[])");
    DBConnection con = db.getConnection();
    try {
//      System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//      System.out.println("Name: "+name);
//      System.out.println("Ontology: "+ontology);
//      System.out.println("Valores:");
//      for(int i=0;i<values.length;i++) {
//        System.out.println(" "+(i+1)+".- "+values[i]);
//      }
     EnumeratedType type=new EnumeratedType(ontology,name,description,values);
     type.store(con);
    } catch (Exception sqle) {
      throw new WebODEException ("Error storing enumerated type: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Remove a enumerated value type from an ontology
   * @param ontology The ontology name
   * @param name The name of the enumerated value type
   */
  public void removeEnumeratedType(String ontology, String name) throws WebODEException, RemoteException {
    logMethod("removeEnumeratedType(java.lang.String,java.lang.String)");
    DBConnection con = db.getConnection();;
    try {
      touch (ontology);

      EnumeratedType type=new EnumeratedType(ontology,name,null,null);
      type.remove(con);
    } catch (Exception sqle) {
      throw new WebODEException ("Error removing enumerated type: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Update a enumerated value type from an ontology
   * @param ontology The ontology name
   * @param old_name The old name of the enumerated value type
   * @param name The new name of the enumerated value type
   * @param values The range of values of the enumerated value type
   */
  public void updateEnumeratedType(String ontology, String old_name, String name, String description) throws WebODEException, RemoteException {
    logMethod("updateEnumeratedType(java.lang.String,java.lang.String,java.lang.String,java.lang.String[])");
    DBConnection con = db.getConnection();
    try {
      EnumeratedType type=EnumeratedType.getEnumeratedType(con,ontology,old_name);
      type.name=name;
      type.description=description;
      type.update(con,old_name);
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating enumerated type: " + sqle.getMessage(), sqle);
    } finally {
      if (con != null) db.releaseConnection (con);
    }
  }

  /**
   * Get all enumerated value types defined in an ontology
   * @param ontology The ontology name
   * @return An array of enumerated value types in an ontology. It returns null if it doesn't found any.
   */
  public EnumeratedType[] getEnumeratedTypes(String ontology) throws WebODEException, RemoteException {
    logMethod("getEnumeratedTypes(java.lang.String)");
    DBConnection con = db.getConnection();
    try {
      return EnumeratedType.getEnumeratedTypes(con,ontology);
    }
    catch(SQLException e) {
      throw new WebODEException("Exception in ODE (getEnumeratedTypes): " + e.getMessage(), e);
    }
    finally {
      try {
        if (con != null) db.releaseConnection (con);
      }
      catch (Exception e) {
      }
    }
  }

  /**
   * Get an enumerated value type defined in an ontology
   * @param ontology The ontology name
   * @param name The name of the enumerated value type
   * @return The enumarated data type. It returns null if it doesn't found.
   */
  public EnumeratedType getEnumeratedType(String ontology, String name) throws WebODEException, RemoteException {
    logMethod("getEnumeratedTypes(java.lang.String, java.lang.String)");
    DBConnection con = db.getConnection();
    try {
      return EnumeratedType.getEnumeratedType(con,ontology,name);
    }
    catch(SQLException e) {
      throw new WebODEException("Exception in ODE (getEnumeratedType): " + e.getMessage(), e);
    }
    finally {
      try {
        if (con != null) db.releaseConnection (con);
      }
      catch (Exception e) {
      }
    }
  }

  /**
   * Add a value to an enumerated type
   * @param ontology The ontology name
   * @param name The enumerated type name
   * @param value The new value
   */
  public void addEnumeratedValue(String ontology, String name, String value) throws WebODEException, RemoteException {
    logMethod("addEnumeratedValue(java.lang.String, java.lang.String, java.lang.String)");
    DBConnection con = db.getConnection();
    try {
      if(value!=null&&
         (value.trim().length()>0))
        EnumeratedType.addValue(con,ontology,name,value);
    }
    catch(SQLException e) {
      throw new WebODEException("Exception in ODE (addEnumeratedValue): " + e.getMessage(), e);
    }
    finally {
      try {
        if (con != null) db.releaseConnection (con);
      }
      catch (Exception e) {
      }
    }
  }

  /**
    * Remove a value of an enumerated type
   * @param ontology The ontology name
   * @param name The enumerated type name
   * @param value The value to be removed
   */
  public void removeEnumeratedValue(String ontology, String name, String value) throws WebODEException, RemoteException {
    logMethod("removeEnumeratedValue(java.lang.String, java.lang.String, java.lang.String)");
    DBConnection con = db.getConnection();
    try {
      if(value!=null&&
         (value.trim().length()>0))
        EnumeratedType.removeValue(con,ontology,name,value);
    }
    catch(SQLException e) {
      throw new WebODEException("Exception in ODE (removeEnumeratedValue): " + e.getMessage(), e);
    }
    finally {
      try {
        if (con != null) db.releaseConnection (con);
      }
      catch (Exception e) {
      }
    }
  }

  /**
   * Update a value of an enumerated type
   * @param ontology The ontology name
   * @param name The enumerated type name
   * @param old_value The old value
   * @param new_value The new value
   */
  public void updateEnumeratedValue(String ontology, String name, String old_value, String new_value) throws WebODEException, RemoteException {
    logMethod("updateEnumeratedValue(java.lang.String, java.lang.String, java.lang.String, java.lang.String)");
    DBConnection con = db.getConnection();
    try {
      EnumeratedType.updateValue(con,ontology,name,old_value,new_value);
    }
    catch(SQLException e) {
      throw new WebODEException("Exception in ODE (updateEnumeratedValue): " + e.getMessage(), e);
    }
    finally {
      try {
        if (con != null) db.releaseConnection (con);
      }
      catch (Exception e) {
      }
    }
  }

  /**
   * Retrieve a list of instance values refered to a list of a given instances
   * @param ontology Name of the instance set's ontology
   * @param instanceSet Name of the instances' instance set
   * @param instances List of instances
   * @return List of instance values
   */
  public HashMap[] getInstancesValues(String ontology, String instanceSet, String[] instances)
      throws RemoteException, WebODEException {
    DBConnection conn=null;
    try {
      conn=db.getConnection();
      return Instance.getInstancesValues(conn, ontology, instanceSet, instances);
    }
    catch(SQLException sqle) {
      throw new WebODEException("Error retriving a list of relation instances: " + sqle.getMessage(), sqle);
    }
    finally {
      if(conn!=null) db.releaseConnection(conn);
    }
  }

  /**
   * Retrieve a list of relation instance from each instances in a given list of instances
   * @param ontology Name of the instance set's ontology
   * @param instanceSet Name of the instances' instance set
   * @param instances List of instances
   * @return List of relation instances
   */
  public TermRelationInstance[][] getRelationInstances (String ontology, String instanceSet, String[] instances)
      throws RemoteException, WebODEException {
    DBConnection conn=null;
    try {
      conn=db.getConnection();
      return TermRelationInstance.getRelationInstances(conn, ontology, instanceSet, instances);
    }
    catch(SQLException sqle) {
      throw new WebODEException("Error retriving a list of relation instances: " + sqle.getMessage(), sqle);
    }
    finally {
      if(conn!=null) db.releaseConnection(conn);
    }
  }
}