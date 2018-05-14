package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.minerva.client.*;
import es.upm.fi.dia.ontology.minerva.server.*;


import es.upm.fi.dia.ontology.webode.service.util.*;

/**
 * Class that encapsulates information regarding an imported term.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class ImportedTerm implements java.io.Serializable
{
  /**
   * The namespcae for the imported term.
   */
  public String namespace;
  /**
   * The namespace identifier for the imported term.
   */
  public String namespace_identifier;
  /**
   * The name given in this ontology.
   */
  public String name;

  /**
   * The URI.
   */
  public String URI;

  public int type=TermTypes.CONCEPT;

  /**
   * Constructor.
   */
  public ImportedTerm (String namespace, String namespace_identifier, String name, String uri)
  {
    this.namespace  = namespace;
    this.namespace_identifier  = namespace_identifier;
    this.URI           = uri;

    if(name == null) {
      int j = this.URI.indexOf('#', 0);
      if (j<0 || j+1 >= this.URI.length()) {
        this.name = namespace_identifier + ":";
      } else {
        this.name = namespace_identifier + ":" + this.URI.substring(j+1);
      }
    } else {
      this.name = name;
    }
  }

  /**
   * Constructor.
   */
  public ImportedTerm (String namespace, String namespace_identifier, String name, String uri, int type) {
    this.namespace  = namespace;
    this.namespace_identifier  = namespace_identifier;
    this.URI           = uri;

    if(name == null) {
      int j = this.URI.indexOf('#', 0);
      if (j<0 || j+1 >= this.URI.length())
        this.name = namespace_identifier + ":";
      else
        this.name = namespace_identifier + ":" + this.URI.substring(j+1);
    }
    else
      this.name = name;

    this.type=type;
  }


  /**
   * Stores the term.
   */
  public void store (DBConnection con, String ontology) throws WebODEException, SQLException
  {
    if (namespace == null || (namespace = namespace.trim()).equals("")||
        (namespace_identifier == null) ||
        (namespace_identifier = namespace_identifier.trim()).equals("") ||
        name == null || (name = name.trim()).equals("") ||
        URI == null || (URI = URI.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    PreparedStatement pstmt = null;
    try {
      con.setAutoCommit (false);

      // First, insert the term into the glossary of terms
      int termId = new Term (ontology, name, "<Imported Term>",
                             this.type).store(con);

                              // now, insert the information into the ode_imported_term table
                              pstmt = con.prepareStatement ("insert into ode_imported_term " +
                                  "(ontology_id, term_id, URI, namespace, namespace_identifier) " +
                                  "values (?, ?, ?, ?, ?)");
                              pstmt.setInt    (1, SQLUtil.getOntologyId (con, ontology));
                              pstmt.setInt    (2, termId);
                              pstmt.setString (3, URI);
                              pstmt.setString (4, namespace);
                              pstmt.setString (5, namespace_identifier);

                              pstmt.executeUpdate();

                              // Commit changes
                              con.commit();
    } catch (SQLException sqle) {
      con.rollback();
      throw sqle;
    } finally {
      if (pstmt != null) pstmt.close();
      con.setAutoCommit (true);
    }
  }

  /**
   * Update the imported term.
   */
  public void update (DBConnection con, String ontology, String originalName) throws WebODEException, SQLException {
    if (namespace == null || (namespace = namespace.trim()).equals("")||
        (namespace_identifier == null) ||
        (namespace_identifier = namespace_identifier.trim()).equals("") ||
        name == null || (name = name.trim()).equals("") ||
        URI == null || (URI = URI.trim()).equals(""))
      throw new WebODEException ("Invalid parameters.");

    ImportedTerm oldImported=ImportedTerm.getImportedTerm(con, ontology, originalName);
    if(oldImported==null)
      throw new WebODEException("The previous imported term doesn't exist");

    PreparedStatement pstmt = null;
    try {
      con.setAutoCommit (false);

      // First, insert the term into the glossary of terms
      int termId=(new Term (ontology, name, "<Imported Term>", type)).update(con,originalName,oldImported.type);

      // now, insert the information into the ode_imported_term table
      pstmt = con.prepareStatement ("update ode_imported_term set " +
                                    "URI=?, namespace=?, namespace_identifier=? " +
                                    "where " +
                                    "term_id=?");
      pstmt.setString (1, URI);
      pstmt.setString (2, namespace);
      pstmt.setString (3, namespace_identifier);
      pstmt.setInt(4,termId);

      pstmt.executeUpdate();

      // Commit changes
      con.commit();
    } catch (SQLException sqle) {
      con.rollback();
      throw sqle;
    } finally {
      if (pstmt != null) pstmt.close();
      con.setAutoCommit (true);
    }
  }

  /**
   * Retrieves all imported terms.
   */
  public static ImportedTerm[] getImportedTerms (DBConnection con, String ontology)
      throws WebODEException, SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select it.namespace, it.namespace_identifier, t.name, it.URI, t.type " +
                                    " from ode_terms_glossary t, ode_imported_term it, ode_ontology o " +
                                    " where it.term_id = t.term_id and it.ontology_id = o.ontology_id " +
                                    " and o.name = ?");
      pstmt.setString (1, ontology);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector(200);

        do {
          v.addElement (new ImportedTerm (rset.getString (1),
              rset.getString (2),
              rset.getString (3),
              rset.getString (4),
              rset.getInt(5)));
        } while (rset.next());
        ImportedTerm[] ait = new ImportedTerm[v.size()];
        v.copyInto (ait);

        return ait;
      }
      else return null;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /**
   * Retrieves all imported terms.
   */
  public static ImportedTerm getImportedTerm (DBConnection con, String ontology, String name)
      throws WebODEException, SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select it.namespace, it.namespace_identifier, t.name, it.URI, t.type " +
                                    " from ode_terms_glossary t, ode_imported_term it, ode_ontology o " +
                                    " where it.term_id = t.term_id and it.ontology_id = o.ontology_id " +
                                    " and o.name = ? and t.name = ?");
      pstmt.setString (1, ontology);
      pstmt.setString (2, name);
      rset = pstmt.executeQuery();
      if (rset.next())
        return (new ImportedTerm (rset.getString (1), rset.getString (2),  rset.getString (3),  rset.getString (4), rset.getInt(5)));
      else return null;
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /**
   * Decides whether the term is an imported term or not.
   */
  public static boolean isImportedTerm (DBConnection con, String ontology, String term)
      throws WebODEException, SQLException
  {
    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select it.namespace, it.namespace_identifier, t.name, it.URI " +
                                    " from ode_terms_glossary t, ode_imported_term it, ode_ontology o " +
                                    " where it.term_id = t.term_id and it.ontology_id = o.ontology_id " +
                                    " and o.name = ? and t.name = ?");
      pstmt.setString (1, ontology);
      pstmt.setString (2, term);
      rset = pstmt.executeQuery();
      return (rset.next());
    } finally {
      if (rset != null) rset.close();
      if (pstmt != null) pstmt.close();
    }
  }

  /**
   * This Method find a concept by its URI.
   *
   * @param URI The URI of the concept to be found.
   * @param user The minerva remote user.
   * @param password The password of minerva remote user.
   * @return The concept with the specified URI
   */
  public static ImportedTerm importConcept(String odeServiceName, WebODEURIConcept uri, String user, String password) throws WebODEException, java.rmi.RemoteException {
    MinervaSession remoteMinerva=null;
    ODEService remoteODE=null;
    try {
      remoteMinerva=MinervaClient.getMinervaSession(new MinervaURL(uri.getHost(),uri.getPort()),user,password);
      remoteODE=(ODEService)remoteMinerva.getService(odeServiceName);

      Term term=remoteODE.getTerm(uri.getOntology(),uri.getConcept());

      ImportedTerm imported=null;
      if(term!=null) {
        if(term.type!=TermTypes.CONCEPT)
          throw new WebODEException("Specified term by the uri isn't a concept");
        imported=new ImportedTerm("","",term.term,uri.toString(),term.type);
      }
      return imported;
    }
    catch (ClassCastException caste) {
      throw new WebODEException("Remote Minerva Server Application and/or ODEService are different from local");
    }
    catch (Exception e) {
      throw new WebODEException(e.getMessage());
    }
    finally {
      if(remoteODE!=null) remoteODE.disconnect();
      if(remoteMinerva!=null) remoteMinerva.disconnect();
    }
  }

  /**
   * This Method find a concept by its URI.
   *
   * @param URI The URI of the concept to be found.
   * @param user The minerva remote user.
   * @param password The password of minerva remote user.
   * @return The concept with the specified URI
   */
  public static ImportedTerm importInstance(String odeServiceName, WebODEURIInstance uri, String user, String password) throws WebODEException, java.rmi.RemoteException {
    MinervaSession remoteMinerva=null;
    ODEService remoteODE=null;
    try {
      remoteMinerva=MinervaClient.getMinervaSession(new MinervaURL(uri.getHost(),uri.getPort()),user,password);
      remoteODE=(ODEService)remoteMinerva.getService(odeServiceName);

      Instance instance=remoteODE.getInstance(uri.getOntology(),uri.getInstanceSet(),uri.getInstance());

      ImportedTerm imported=null;
      if(instance!=null) {
        imported=new ImportedTerm("","",instance.name,uri.toString(),TermTypes.INSTANCE);
      }
      return imported;
    }
    catch (ClassCastException caste) {
      throw new WebODEException("Remote Minerva Server Application and/or ODEService are different from local");
    }
    catch (Exception e) {
      throw new WebODEException(e.getMessage());
    }
    finally {
      if(remoteODE!=null) remoteODE.disconnect();
      if(remoteMinerva!=null) remoteMinerva.disconnect();
    }
  }

  public static InstanceAttributeDescriptor[] getInstanceAttributes(String odeServiceName, WebODEURIConcept uri, String user, String password) throws WebODEException, java.rmi.RemoteException {
    MinervaSession remoteMinerva=null;
    ODEService remoteODE=null;
    try {
      remoteMinerva=MinervaClient.getMinervaSession(new MinervaURL(uri.getHost(),uri.getPort()),user,password);
      remoteODE=(ODEService)remoteMinerva.getService(odeServiceName);

      Term term=remoteODE.getTerm(uri.getOntology(),uri.getConcept());

      InstanceAttributeDescriptor[] iads=null;
      if(term!=null) {
        if(term.type!=TermTypes.CONCEPT)
          throw new WebODEException("Specified term by the uri isn't a concept");
        iads=remoteODE.getInstanceAttributes(uri.getOntology(),term.term,true);
      }
     return iads;
    }
    catch (ClassCastException caste) {
      throw new WebODEException("Remote Minerva Server Application and/or ODEService are different from local");
    }
    catch (Exception e) {
      throw new WebODEException(e.getMessage());
    }
    finally {
      if(remoteODE!=null) remoteODE.disconnect();
      if(remoteMinerva!=null) remoteMinerva.disconnect();
    }
  }


  public static ClassAttributeDescriptor[] getClassAttributes(String odeServiceName, WebODEURIConcept uri, String user, String password) throws WebODEException, java.rmi.RemoteException {
    MinervaSession remoteMinerva=null;
    ODEService remoteODE=null;
    try {
      remoteMinerva=MinervaClient.getMinervaSession(new MinervaURL(uri.getHost(),uri.getPort()),user,password);
      remoteODE=(ODEService)remoteMinerva.getService(odeServiceName);

      Term term=remoteODE.getTerm(uri.getOntology(),uri.getConcept());

      ClassAttributeDescriptor[] cads=null;
      if(term!=null) {
        if(term.type!=TermTypes.CONCEPT)
          throw new WebODEException("Specified term by the uri isn't a concept");
        cads=remoteODE.getClassAttributes(uri.getOntology(),term.term,true);
      }
     return cads;
    }
    catch (ClassCastException caste) {
      throw new WebODEException("Remote Minerva Server Application and/or ODEService are different from local");
    }
    catch (Exception e) {
      throw new WebODEException(e.getMessage());
    }
    finally {
      if(remoteODE!=null) remoteODE.disconnect();
      if(remoteMinerva!=null) remoteMinerva.disconnect();
    }
  }
}