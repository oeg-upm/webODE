package es.upm.fi.dia.ontology.minerva.server.others;

import java.rmi.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * Class that implements the thread pool service.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class ThreadPoolServiceImp extends MinervaServiceImp
    implements ThreadPoolService
{
  private ThreadGroup threadGroup;
  private List availableThreads;
  private Vector allThreads;
  private List jobs;

  /**
   * Constructor.
   *
   */
  public ThreadPoolServiceImp () throws RemoteException
  {
  }

  public void start () throws CannotStartException
  {
    ThreadPoolServiceConfiguration tpsc = (ThreadPoolServiceConfiguration) config;

    if (tpsc.numThreads <= 0 && tpsc.priority < 0)
      throw new CannotStartException ("service " + context.getName() + " not configured.");

    availableThreads = new LinkedList();
    allThreads = new Vector();
    jobs = new LinkedList();

    context.logDebug ("creating thread group...");
    // Create the thread group
    threadGroup = new ThreadGroup (context.getName());
    context.logDebug ("creating threads...");
    for (int i = 0; i < tpsc.numThreads; i++) {
      Thread th = new PoolThread (threadGroup, context.getName() + "-" + (i + 1),
      jobs, context);
      availableThreads.add (th);
      allThreads.addElement (th);

      // Start thread
      th.start();
    }

    context.logDebug ("done!");
  }

  public void stop()
  {
    // Discard jobs
    synchronized (jobs) {
      context.logInfo (jobs.size() + " jobs will be discarded on user's demand.");
      jobs.clear();
    }

    context.logInfo ("shutting down threads...");
    // Shutdown threads (the hard way).
    // Angel 15-10-02 -->
    // In order to the newest JDKs (above 1.3), the method Thread.stop() has been become
    // deprecated. See %JAVA_HOME%\docs\guide\misc\threadPrimitiveDeprecation.html
    Thread moribund;
//	ThreadGroup threadGroup=new ThreadGroup (context.getName());
    for (int i = 0; i < allThreads.size(); i++) {
      context.logDebug("Angel --> The thread \"" + ((Thread) allThreads.elementAt(i)).getName() + "\" was stopped.");
//	    ((Thread) allThreads.elementAt (i)).stop();
      moribund=(Thread) allThreads.elementAt(i);
      allThreads.setElementAt (null,i);
      moribund.interrupt();
      allThreads.setElementAt(new PoolThread (threadGroup, context.getName() + "-" + (i + 1),
          jobs, context),i);
    }
    // <-- Angel
    context.logInfo ("thread pool service " + context.getName() + " stopped completely.");
  }

  // Logic methods
  public void executeWork (Runnable runnable) throws RemoteException
  {
    synchronized (jobs) {
      try {
        jobs.add (runnable);

        // Notify worker threads
        jobs.notify();
      } catch (Exception e) {
        context.logError ("unexpected error executing work in thread pool service: " + e);
      }
    }
  }

  protected void localDisconnect() {
  }
}