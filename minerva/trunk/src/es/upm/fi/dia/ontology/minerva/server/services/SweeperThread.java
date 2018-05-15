package es.upm.fi.dia.ontology.minerva.server.services;

//Java stuff
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

//Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.rmi.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;

/**
 * This is the implementation for a thread that follows the
 * track of the services and disconnects those not accessed
 * for DISCONNECT_TIME milliseconds.
 *
 * @author  Julio César Arpírez Vega
 * @version 1.1
 */
public class SweeperThread extends Thread
{
  /**
   * The disconnection time.  The maximum period of time
   * a service may be unaccessed.
   */
  public static final int DISCONNECT_TIME = 50000;

  private LinkedList serviceList;
  private LogServiceImp logger;

  public SweeperThread (LogServiceImp logger)
  {
    super ("Sweeper-Tracker thread");

    // Set priority
    setPriority (MAX_PRIORITY);

    serviceList = new LinkedList();
    this.logger = logger;

    logger.logInfo ("sweeper thread started up.");
  }

  public void removeService(MinervaService ms) {
    boolean found=false;
    synchronized(serviceList) {
      found=serviceList.remove(ms);
    }

    if(found) {
      try {
        ms.disconnect();
      }
      catch (Exception e) {
        logger.logError ("Unexpected error during disconnection: " + e);
        e.printStackTrace();
      }
    }
  }

  public void addService (MinervaService ms)
  {
    synchronized (serviceList) {
      serviceList.add (ms);

      ((MinervaServiceImp) ms).sendBeacon();
    }

    logger.logDebug ("Service " + ms + " added to track list.");
  }

  public void run ()
  {
    while (true) {
      try {
        sleep (DISCONNECT_TIME);
      } catch (Exception e) {}

      try {
        logger.logDebug ("sweeper-thread about to check...");

        List lcopy;
        synchronized (serviceList) {
          lcopy = (List) serviceList.clone();
        }

        String str_services="";
        for (int i = 0, l = lcopy.size(); i < l; i++) {
          MinervaServiceImp msi = (MinervaServiceImp) lcopy.get (i);
          str_services+=msi.getClass() + ", ";
        }

        logger.logDebug (lcopy.size() + " services to check: " + str_services);

        long currentTime = System.currentTimeMillis();
        Vector vRemoveIndex = new Vector();
        Vector vRemoveService = new Vector();
        for (int i = 0, l = lcopy.size(); i < l; i++) {
          MinervaServiceImp msi = (MinervaServiceImp) lcopy.get (i);

          if (currentTime - msi.getLastAccessTime () >= DISCONNECT_TIME) {
            vRemoveIndex.addElement (new Integer (i));
            vRemoveService.addElement (msi);
          }
        }

        // Actually remove and disconnect service instances
        synchronized (serviceList) {
          for (int i = 0; i < vRemoveIndex.size(); i++) {
            serviceList.remove (((Integer) vRemoveIndex.elementAt (i)).intValue() - i);
          }
        }

        // Disconnect unused services
        for (int i = 0; i < vRemoveService.size(); i++) {
          try {
            Disconnect dis = (Disconnect) vRemoveService.elementAt (i);
            dis.disconnect();

            logger.logDebug ("service " + dis + " disconnected and returned to pool.");
          } catch (Exception e) {
            logger.logError ("Unexpected error during disconnection: " + e);
            e.printStackTrace();
          }
        }

        logger.logDebug ("sweeper-thread check completed.");
      } catch (Exception e) {
        logger.logError ("Unexpected error in sweeper thread: " + e);
      }
    }
  }
}













