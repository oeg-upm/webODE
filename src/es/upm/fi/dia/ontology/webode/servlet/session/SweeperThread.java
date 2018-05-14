package es.upm.fi.dia.ontology.webode.servlet.session;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.servlet.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * Class responsible for cleaning-up invalidate sessions and
 * free their related resources.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.6
 */
public class SweeperThread extends Thread implements WebODEConstants
{
    public static final int SLEEP_TIME = 60000; // 1 minute

    private ServletContext context;
    private ArrayList al;
    private volatile boolean bRun = true;
    private long timeToSweep;

    /**
     * Builds a new sweeper thread.
     *
     * @param context The servlet context (for logging purposes).
     * @param timeToSweep The maximum time a session will be valid if it is not accessed.
     */
    public SweeperThread (ServletContext context, long timeToSweep)
    {
	this.context = context;
	this.timeToSweep = timeToSweep - 90; // minus 1.5 minutes

	al = new ArrayList (10);
    }

    public void start ()
    {
	super.start();

	context.log (new Date() + ": sweeper thread started.  Maximum age: " + timeToSweep + " s.");
    }

    /**
     * Adds a new session to be tracked.
     *
     * @param session The session to take care of.
     */
    public void addSession (HttpSession session)
    {
        synchronized (al) {
            if (!al.contains (session)) {
                al.add (session);

                try {
                    al.notify();
                } catch (Exception e) {}
            }
        }
    }

    /**
     * Removes a session.
     *
     * @param session The session to take off.
     */
    public void removeSession (HttpSession session) {
      synchronized (al) {
        al.remove(session);
        String param;
        Object val;
        for(Enumeration enum=session.getAttributeNames(); enum.hasMoreElements(); ) {
          param=enum.nextElement().toString();
          try {
            val=session.getAttribute(param);
            if(val instanceof MinervaService && ((MinervaService)val).isStatefull())
              ((MinervaService)val).disconnect();
          }
          catch(Exception e) {
            context.log (new Date() + ": session invalidated: " + e.getMessage());
          }
        }
      }
    }

    /**
     * This method runs every minute to determine which
     * sessions are not valid.
     */
    public void run ()
    {
	while (bRun) {
	    synchronized (al) {
		if (al.isEmpty()) {
		    // Sleep
		    try {
			al.wait();
		    } catch (Exception e) {}
		}
		context.log (new Date() + ": checking sessions...");
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < al.size(); i++) {
		    // Check session last access time.
		    HttpSession session = (HttpSession) al.get (i);

		    boolean b = false;
		    try {
			if (currentTime - session.getLastAccessedTime() > timeToSweep * 1000) {

			    al.remove (i);
			    i--;

			    b = true;
			    context.log (new Date() + ": " + session.getAttribute (WebODEConstants.USER) +
					 "'s session invalidated.");

			    // Disconnect services
                            String param;
                            Object val;
                            for(Enumeration enum=session.getAttributeNames(); enum.hasMoreElements(); ) {
                              param=enum.nextElement().toString();
                              try {
                                val=session.getAttribute(param);
                                if(val instanceof MinervaService)
                                  if(((MinervaService)val).isStatefull())
                                    ((MinervaService)val).disconnect();
                                session.removeAttribute(param);
                              }
                              catch(Exception e) {
                              }
                            }
/*			    ((ODEService) session.getValue (ODE_SERVICE)).disconnect();
			    MinervaService ms = (ImportService) session.getValue (IMPORT_SERVICE);
			    if (ms != null)
				ms.disconnect();
			    ms = (ExportService) session.getValue (EXPORT_SERVICE);
			    if (ms != null)
				ms.disconnect();
*/
          MinervaSession ms=(MinervaSession)session.getAttribute(MINERVA_SESSION);
          if(ms!=null) ms.disconnect();
			    session.invalidate();
			}
		    } catch (Exception e) {
                        if((i + (b ? 1 : 0))<al.size())
                          al.remove (i + (b ? 1 : 0));
			context.log (new Date() + ": session invalidated: " + e.getMessage());
		    }
		}
	    }
	    context.log (new Date() + ": sessions checked.  Sleeping...");
	    try {
		sleep (SLEEP_TIME);
	    } catch (Exception e) {}
	}
    }

    /**
     * Shutdowns the thread.
     */
    public void shutdown ()
    {
	bRun = false;
    }
}


