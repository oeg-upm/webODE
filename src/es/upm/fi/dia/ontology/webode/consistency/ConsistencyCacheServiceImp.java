package es.upm.fi.dia.ontology.webode.consistency;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.others.*;

// Webode stuff
import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

import java.sql.*;
import java.rmi.*;
import java.util.*;

/**
 * The implementation for the consistency cache module.
 * <p>
 * Ontologies are loaded on demand (<tt>loadOntology</tt>)
 * method or implicitly (invocation of any other method
 * without the ontology being in memory).
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.8
 * @see     es.upm.fi.dia.ontology.webode.service.ODEService
 */
public class ConsistencyCacheServiceImp extends MinervaServiceImp
    implements ConsistencyCacheService, BatchJob
{
  // Variables
  // The ontology cache
  private HashMap hashOntologies;

  // Last access times.
  private HashMap hashTimes;

  // The database service
  private DatabaseService db;

  // Scheduler service
  private BatchWorkerService batch;

  // Constructor
  public ConsistencyCacheServiceImp () throws RemoteException
  {
    hashOntologies = new HashMap();
    hashTimes      = new HashMap();
  }

  // Life-cycle methods --------------------------------------------------------------------
  public void start () throws CannotStartException
  {
    // See what databse service to use.
    String dbService = ((ConsistencyCacheServiceConfiguration) config).dbService;
    if (dbService == null)
      throw new CannotStartException ("no database service specified. \nThe service is not properly configured.");

    String scheduler = ((ConsistencyCacheServiceConfiguration) config).scheduler;
    if (scheduler == null)
      throw new CannotStartException ("no batch worker service specified. \nThe service is not properly configured.");

    try {
      // Get the service
      db = (DatabaseService) context.getService (dbService);

      batch = (BatchWorkerService) context.getService (scheduler);

      // Tell the scheduler to call me after the time specified in the
      // configuration file
      batch.registerJob (new JobDescription ("cache keeping (" + context.getName() + ")",
          this,
          System.currentTimeMillis() +
          (((ConsistencyCacheServiceConfiguration) config).time <= 0 ?
          DEFAULT_TIME : ((ConsistencyCacheServiceConfiguration) config).time)));
    } catch (Exception e) {
      throw new CannotStartException ("Consistency cache service cannot start because it cannot" +
                                      " obtain the service:\n" +
                                      e.getMessage());
    }
  }

  public void stop ()
  {
    synchronized (hashTimes) {
      synchronized (hashOntologies) {
        hashTimes.clear();
        hashOntologies.clear();
      }
    }
  }

  // Business methods -----------------------------------------------------------------------

  /**
   * Opens an ontology (load it into the cache).
   *
   * @param ontology The ontology to be loaded.
   */
  public void loadOntology (String ontology)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    context.logInfo ("request to cache ontology " + ontology + ".");
    DBConnection con = db.getConnection();
    try {
      context.logDebug ("retrieving ontology information...");
      // Read all terms and relations
      Term[] at = Term.getTerms (con, ontology, new int[] { Term.CONCEPT });
      TermRelation[] atr = TermRelation.getTermRelations (con, ontology, false);
      es.upm.fi.dia.ontology.webode.service.Group[] ag = es.upm.fi.dia.ontology.webode.service.Group.getGroups(con, ontology);
      context.logDebug ("done.");

      context.logDebug ("building cache in memory for ontology " + ontology + ".");
      // Create a model to store everything inside.
      DefaultDesignModel ddm = new DefaultDesignModel();
      // Disable checks.  The data is safe.
      ddm.setChecks (false);
      ddm.setRelationChecks (false);

      Element[] ae = null;
      HashMap els = new HashMap();
      if (at != null) {
        ae = new Element[at.length];
        for (int i = 0; i < ae.length; i++) {
          ae[i] = new Element (at[i].term);
          els.put (at[i].term, ae[i]);

          // Add element to model.
          ddm.addElement (ae[i]);
        }
      }
      if (atr != null) {
        for (int i = 0; i < atr.length; i++) {
          // Add relation to model
          ddm.addRelation (new Relation (atr[i].name, atr[i].origin, atr[i].destination));
        }
      }
      if (ag != null) {
        for (int i = 0; i < ag.length; i++) {
          es.upm.fi.dia.ontology.webode.ui.designer.model.Group g = new es.upm.fi.dia.ontology.webode.ui.designer.model.Group (ag[i].name);
          for (int j = 0; ag[i] != null && j < ag[i].concepts.length; j++) {
            g.addElement ((Element) els.get (ag[i].concepts[j]));
          }

          // Add the group to the model
          ddm.addElement (g);
        }
      }

      // Enable checks again
      ddm.setChecks (true);
      ddm.setRelationChecks (true);

      context.logDebug ("done.");

      // Add to cache
      addOntology (ontology, ddm);

      // Put timestamp
      updateTime (ontology);
    } catch (DesignException de) {
      throw new ConsistencyCacheException (de.getMessage());
    } catch (SQLException sqle) {
      sqle.printStackTrace(System.out);
      throw new ConsistencyCacheException ("Database error: " + sqle.getMessage(), sqle);
    } finally {
      db.releaseConnection (con);
    }
  }

  private void addOntology (String ontology, DesignModel ddm)
  {
    synchronized (hashOntologies) {
      hashOntologies.put (ontology, ddm);
    }
  }

  private void updateTime (String ontology)
  {
    // Put timestamp
    synchronized (hashTimes) {
      hashTimes.put (ontology, new Long (System.currentTimeMillis()));
    }
  }

  /**
   * Unloads an ontology, discarding it.
   *
   * @param ontology The ontology to be discarded.
   */
  public void unloadOntology (String ontology)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    synchronized (hashTimes) {
      synchronized (hashOntologies) {
        hashTimes.remove (ontology);
        hashOntologies.remove (ontology);
      }
    }
  }


  /**
   * Update ontology name.
   *
   * @param oldName The old name.
   * @param newName The new name.
   */
  public void updateOntologyName (String oldName, String newName)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    ensureLoaded (oldName);

    synchronized (hashTimes) {
      synchronized (hashOntologies) {
        hashTimes.remove (oldName);
        hashTimes.put (newName, new Long (System.currentTimeMillis()));

        Object model = hashOntologies.remove (oldName);
        hashOntologies.put (newName, model);
      }
    }
  }

  /**
   * Insert a new concept.
   *
   * @param ontology The ontology the concept is added to.
   * @param name     The concept's name.
   */
  public void insertConcept (String ontology, String name)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    ensureLoaded (ontology);

    try {
      synchronized (hashTimes) {
        synchronized (hashOntologies) {
          // Update access time
          touch (ontology);

          DesignModel ddm = (DefaultDesignModel) hashOntologies.get (ontology);
          ddm.addElement (new Element (name));
        }
      }
    } catch (DesignException de) {
      throw new ConsistencyCacheException ("Error inserting concept: " + de.getMessage() + ".");
    }
  }

  /**
   * Updates a concept.
   *
   * @param ontology The ontology the concept is added to.
   * @param oldName  The old concept's name.
   * @param newName  The new concept's name.
   */
  public void updateConcept (String ontology, String oldName, String newName)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    ensureLoaded (ontology);

    try {
      synchronized (hashTimes) {
        synchronized (hashOntologies) {
          // Update access time
          touch (ontology);

          DesignModel ddm = (DefaultDesignModel) hashOntologies.get (ontology);
          ddm.updateElement (oldName, newName);
        }
      }
    } catch (DesignException de) {
      throw new ConsistencyCacheException (de.getMessage());
    }
  }

  /**
   * Removes a concept.
   *
   * @param ontology The ontology the concept is added to.
   * @param name     The concept's name.
   */
  public void removeConcept (String ontology, String name)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    ensureLoaded (ontology);

    synchronized (hashTimes) {
      synchronized (hashOntologies) {
        // Update access time
        touch (ontology);

        DesignModel ddm = (DefaultDesignModel) hashOntologies.get (ontology);
        ddm.removeElement (name);
      }
    }
  }


  /**
   * Inserts a new relationship.
   *
   * @param ontology The ontology the concept is added to.
   * @param name     The relation's name.
   * @param origin   The origin's name.
   * @param destination The destination's name.
   */
  public void insertTermRelation (String ontology, String name, String origin, String destination)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    ensureLoaded (ontology);

    try {
      synchronized (hashTimes) {
        synchronized (hashOntologies) {
          // Update access time
          touch (ontology);

          DesignModel ddm = (DefaultDesignModel) hashOntologies.get (ontology);
          ddm.addRelation (new Relation (name, origin, destination));
        }
      }
    } catch (DesignException de) {
      throw new ConsistencyCacheException ("Error inserting relation: " + de.getMessage());
    }
  }

  /**
   * Removes a relationship.
   *
   * @param ontology The ontology the concept is added to.
   * @param name     The relation's name.
   * @param origin   The origin's name.
   * @param destination The destination's name.
   */
  public void removeTermRelation (String ontology, String name, String origin, String destination)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    ensureLoaded (ontology);

    synchronized (hashTimes) {
      synchronized (hashOntologies) {
        // Update access time
        touch (ontology);

        DesignModel ddm = (DefaultDesignModel) hashOntologies.get (ontology);
        ddm.removeRelation (new Relation (name, origin, destination));
      }
    }
  }

  /**
   * Insert a new group.
   *
   * @param ontology The ontology the concept is added to.
   * @param group    The group.
   */
  public void insertGroup (String ontology, es.upm.fi.dia.ontology.webode.service.Group group)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    ensureLoaded (ontology);

    try {
      synchronized (hashTimes) {
        synchronized (hashOntologies) {
          // Update access time
          touch (ontology);

          DesignModel ddm = (DefaultDesignModel) hashOntologies.get (ontology);

          // Turn the group into another
          es.upm.fi.dia.ontology.webode.ui.designer.model.Group gr =
              new es.upm.fi.dia.ontology.webode.ui.designer.model.Group (group.name);
          for (int i = 0; i < group.concepts.length; i++) {
            gr.addElement (ddm.getElement (group.concepts[i]));
          }

          ddm.addElement (gr);
        }
      }
    } catch (DesignException de) {
      throw new ConsistencyCacheException ("Error inserting group: " + de.getMessage() + ".");
    }
  }

  /**
   * Updates a group.
   *
   * @param ontology The ontology the concept is added to.
   * @param oldName  The old group's name.
   * @param group    The group.
   */
  public void updateGroup (String ontology, String oldName, es.upm.fi.dia.ontology.webode.service.Group group)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    ensureLoaded (ontology);

    //	try {
    synchronized (hashTimes) {
      synchronized (hashOntologies) {
        // Update access time
        touch (ontology);

        // Has name changed?
        if (!oldName.equals (group.name))
          // Update it
          updateConcept (ontology, oldName, group.name);

        DesignModel ddm = (DefaultDesignModel) hashOntologies.get (ontology);

        es.upm.fi.dia.ontology.webode.ui.designer.model.Group gr =
            (es.upm.fi.dia.ontology.webode.ui.designer.model.Group) ddm.getElement (group.name);
        // Remove all elements
        Vector v = gr.getElements ();
        for (int i = 0; i < v.size(); i++)
          gr.removeElement ((Element) v.elementAt (i));
        // Add new ones
        for (int i = 0; i < group.concepts.length; i++)
          gr.addElement (ddm.getElement (group.concepts[i]));
      }
    }
     /*} catch (DesignException de) {
     throw new ConsistencyCacheException ("Error updating group: " + de.getMessage() + ".");
     }*/
  }


  /**
   * Ensures an ontology is loaded.
   */
  private void ensureLoaded (String ontology)
      throws RemoteException, WebODEException, ConsistencyCacheException
  {
    synchronized (hashTimes) {
      synchronized (hashOntologies) {
        context.logDebug ("ensureLoaded: checking ontology " + ontology + "...");
        if (!hashOntologies.containsKey (ontology)) {
          context.logDebug ("cache miss.  Loading ontology...");
          loadOntology (ontology);
        }
      }
    }
  }

  /**
   * "Touches" an ontology to modify its access time.
   *
   */
  public void touch (String ontology)
  {
    synchronized (hashTimes) {
      if (hashTimes.containsKey (ontology))
        hashTimes.put (ontology, new Long (System.currentTimeMillis ()));
    }
  }

  /**
   * Invoked periodically to to cache keeping.
   */
  public void executeJob (JobDescription jd)
  {
    long time1 = ((ConsistencyCacheServiceConfiguration) config).time <= 0 ?
                 DEFAULT_TIME : ((ConsistencyCacheServiceConfiguration) config).time;

    context.logDebug ("Doing cache keeping...");
    synchronized (hashTimes) {
      Iterator it = hashTimes.keySet().iterator();

      long currentTime = System.currentTimeMillis();
      while (it.hasNext()) {
        String key = (String) it.next();

        long l = ((Long) hashTimes.get (key)).longValue();

        if (l + time1 <= currentTime) {
          // Eliminate
          synchronized (hashOntologies) {
            hashTimes.remove (key);
            hashOntologies.remove (key);
          }
          context.logInfo ("ontology " + key + " evicted from memory.");
        }
      }
    }

    while (true) {
      try {
        // Tell the scheduler to call me after the time specified in the
        // configuration file
        batch.registerJob (new JobDescription ("cache keeping (" + context.getName() + ")",
            this,
            jd.executionTime + time1));
        break;
      } catch (Exception e) {
        context.logError ("Cannot register withing BatchWorker: " + e.getMessage());
        context.logInfo  ("Waiting...");
        try {
          Thread.sleep (60000);
          } catch (Exception e1) {}
          context.logInfo  ("Retrying...");
      }
    }
  }
}




