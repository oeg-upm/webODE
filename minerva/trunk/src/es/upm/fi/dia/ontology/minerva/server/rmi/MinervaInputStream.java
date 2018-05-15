package es.upm.fi.dia.ontology.minerva.server.rmi;

import java.io.*;

public class MinervaInputStream extends FilterInputStream implements MinervaStream, BeaconConstants
{
    private static CheckerThread checkerThread;    
    private static Object sem = new Object[0];

    private InputStream is;
    private Disconnect dis;
    private long time;
    private Flag bDisconnected;

    public MinervaInputStream (InputStream is, Disconnect dis, Flag bDisconnected)
    {
	super (is);

	this.is            = is;
	this.dis           = dis;
	this.bDisconnected = bDisconnected;
	
	/*synchronized (sem) {
	  if (checkerThread == null) {
	  checkerThread = new CheckerThread ();
	  checkerThread.start();
	  }
	  }
	  checkerThread.addInputStream (this);
	  time = System.currentTimeMillis();*/
    }
    
    public synchronized int read () throws IOException
    {
	/*System.out.println ("ir");*/
	try {
	    int foo = is.read();
	    /*time = System.currentTimeMillis();*/

	    /**if (foo < 0 && dis != null) {
		// Disconnect
		dis.disconnect();
		}*/
	    return foo;
	} catch (IOException ioe) {
	    // Disconnect
	    //System.out.println ("ATJU2: " + ioe);
	    /*if (dis != null) {
		if (!bDisconnected.isSet())
		    bDisconnected.set ();
		dis.disconnect();
		}*/
		
	    // Rethrow
	    throw ioe;
	}
    }

    public synchronized int read (byte[] b, int off, int len) throws IOException
    {
	//System.out.println ("br");
	try {
	    int foo;
	    //while (true) {
	    foo = is.read(b, off, len);
		/*time = System.currentTimeMillis();
		  if (foo == BEACON.length) {
		  // It was a beacon...
		  System.err.println ("Beacon received...");
		  continue;
		  }
		  else if (foo == BEACON.length + 1) {
		  // Skip last byte
		  foo--;
		  }
		  break;
		  }*/
	    
	    /**if (foo < 0) {
	       // Disconnect
	       if (dis != null) dis.disconnect();
	       }*/
	    return foo;
	} catch (IOException ioe) {
	    //System.out.println ("ATJU: " + ioe);
	    // Disconnect
	    /*if (dis != null) {
		if (!bDisconnected.isSet())
		    bDisconnected.set ();
		dis.disconnect();
		}*/
	    // Rethrow
	    throw ioe;
	}
    }

    public synchronized void close() throws IOException
    {
	System.out.println ("MISCLOSE");
	/*if (!bDisconnected.isSet()) {
	    bDisconnected.set ();
	    if (dis != null) dis.disconnect();
	    }*/
	super.close();
    }

    public synchronized long getLastTime()
    {
	return time;
    }
}




