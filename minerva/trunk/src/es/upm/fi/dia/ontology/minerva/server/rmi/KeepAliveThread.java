package es.upm.fi.dia.ontology.minerva.server.rmi;

import java.util.*;

public class KeepAliveThread extends Thread implements BeaconConstants
{
    private Vector v;

    public KeepAliveThread ()
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
		    ((MinervaOutputStream) cloned.elementAt (i)).checkAndWriteBeacon ();
		} catch (Exception e) {
		    vToDelete.addElement (new Integer (i));
		}
	    }

	    // Delete bad sockets.
	    synchronized (v) {
		int j = 0;
		for (int i = 0; i < vToDelete.size(); i++)
		    v.removeElementAt (((Integer) vToDelete.elementAt(vToDelete.size()-1)).intValue());
	    }

	    // Sleep
	    try {
		sleep (KEEP_ALIVE_THREAD_SLEEP_TIME);
		System.out.println ("Keep alive thread");
	    } catch (Exception e) {
	    }
	}
    }

    public void addOutputStream (MinervaOutputStream mos)
    {
	synchronized (v) {
	    v.addElement (mos);
	    v.notify();
	}
    }
}
