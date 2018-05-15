package es.upm.fi.dia.ontology.minerva.server.rmi;

import java.util.*;

public class CheckerThread extends Thread implements BeaconConstants
{
    private Vector v;

    public CheckerThread ()
    {
	super ();
	setDaemon (true);

	v = new Vector(100);
    }

    public void run ()
    {
	Vector cloned;
	while (true) {
	    synchronized (v) {
		if (v.isEmpty()) {
		    try {
			v.wait();
		    } catch (Exception e) {
		    }
		}
		cloned = (Vector) v.clone();
	    }

	    Vector vToDelete = new Vector();
	    for (int i = 0; i < cloned.size(); i++) {
		try {
		    long t = ((MinervaInputStream) cloned.elementAt (i)).getLastTime ();
		    if (System.currentTimeMillis () - t > CHECKER_THREAD_SLEEP_TIME)
			vToDelete.addElement (new Integer (i));
		} catch (Exception e) {
		    vToDelete.addElement (new Integer (i));
		}
	    }

	    // Delete bad sockets.
	    synchronized (v) {
		for (int i = 0; i < vToDelete.size(); i++)
		    v.removeElementAt (((Integer) vToDelete.elementAt(vToDelete.size()-1)).intValue());
	    }

	    // Sleep
	    try {
		sleep (CHECKER_THREAD_SLEEP_TIME);
		System.out.println ("Checker thread");
	    } catch (Exception e) {
	    }
	}
    }

    public void addInputStream (MinervaInputStream mos)
    {
	synchronized (v) {
	    v.addElement (mos);
	    v.notify();
	}
    }
}
