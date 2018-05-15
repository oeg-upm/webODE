package es.upm.fi.dia.ontology.minerva.server.others;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import java.rmi.*;
import java.net.*;
import java.io.*;

/**
 * The implementation for the backup service.
 *
 * @author  Julio César Arpírez Vega
 * @version 1.0
 */
public class BackupServiceImp extends MinervaServiceImp
    implements BackupService, BatchJob
{
  private long pendingJob = -1;
  private BatchWorkerService scheduler;

  public BackupServiceImp () throws RemoteException
  {
  }

  // Life-cycle -----------------------------------------------------
  public void start () throws CannotStartException
  {
    BackupServiceConfiguration bsc = (BackupServiceConfiguration) config;

    if (bsc.destinationURL == null)
      throw new CannotStartException ("Incorrect URL parameter in service " + context.getName() + ".");
    if (bsc.time <= 0)
      throw new CannotStartException ("Incorrect time parameter in service " + context.getName() + ".");
    if (bsc.scheduler == null)
      throw new CannotStartException ("Incorrect scheduler service parameter in service " + context.getName() + ".");

    try {
      scheduler = (BatchWorkerService) context.getService (bsc.scheduler);
    } catch (Exception e) {
      throw new CannotStartException ("Cannot obtain scheduler service (" + bsc.scheduler + ") in " +
                                      context.getName() + " service.", e);
    }

    // Scheduler backup work
    _scheduleJob(System.currentTimeMillis());
  }

  private void _scheduleJob (long origin)
  {
    BackupServiceConfiguration bsc = (BackupServiceConfiguration) config;
    try {
      // Register job
      pendingJob = scheduler.registerJob
      (new JobDescription (context.getName (), this,
      origin + bsc.time * 60 * 1000));
    } catch (Exception e) {
      context.logError ("error scheduling backup: " + e.getMessage() + ".");
    }
  }

  public void stop ()
  {
    try {
      // Unregister any pending jobs.
      if (pendingJob > 0)
        scheduler.unregisterJob ((int) pendingJob);
    } catch (Exception e) {
      context.logError ("error stopping " + context.getName() + " due to the following problem: " +
                        e.getMessage() + ".");
    }
  }

  public void executeJob (JobDescription jd) throws BatchJobException, RemoteException
  {
    ObjectOutputStream oos = null;
    PrintWriter pw = null;
    try {
      BackupServiceConfiguration bsc = (BackupServiceConfiguration) config;
      BackupSource source;

      if (bsc.source == null)
        throw new CannotStartException ("Incorrect source parameter in service " + context.getName() + ".");
      try {
        source = (BackupSource) context.getService (bsc.source);
      } catch (ClassCastException cce) {
        throw new BatchJobException ("service " + bsc.source + " is not a valid backup service for the " +
                                     context.getName() + " service.", cce);
      } catch (Exception e) {
        throw new BatchJobException ("Cannot obtain source service (" + bsc.source + ") in " +
                                     context.getName() + " service.", e);
      }

      // Are there more elements?
      while (source.hasNext()) {
        URL url = new URL (bsc.destinationURL + "/" + source.getName());

        Object obj = source.getSource();

        if (!url.getProtocol().equals ("file"))
          throw new BatchJobException ("Unsupported protocol in backup service (" + context.getName() +
                                       ": " + url.getProtocol());

        if (obj instanceof String) {
          pw = new PrintWriter (new FileWriter (url.getFile()));
          pw.print (obj.toString());
          pw.flush();
          pw.close();
        }
        else {
          oos = new ObjectOutputStream (new FileOutputStream (url.getFile()));
          oos.writeObject (obj);
          oos.close();
        }

  /*URLConnection urlc = url.openConnection();
    urlc.setDoOutput (true);
    urlc.setDoInput (false);
    urlc.setUseCaches (false);

    oos = new ObjectOutputStream (urlc.getOutputStream ());
    oos.writeObject (obj);
    oos.close();*/
      }
    } catch (Exception e) {
      throw new BatchJobException ("unexpected error during backup: " + e.getMessage() + ".", e);
    } finally {
      pendingJob = -1;

      try {
        if (oos != null) oos.close();
        if (pw != null) pw.close();
        } catch (Exception e) {}

        context.logDebug ("rescheduling backup...");
        _scheduleJob (jd.executionTime);
    }
  }

  protected void localDisconnect() {
  }
}
