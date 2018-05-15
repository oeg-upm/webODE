package es.upm.fi.dia.ontology.minerva.server.rmi;

import java.io.*;

public class MinervaOutputStream extends FilterOutputStream implements BeaconConstants
{
    private OutputStream os;
    private Disconnect dis;
    private long time;

    private static Object sem = new Object[0];
    private static KeepAliveThread keepAliveThread;
    private Flag bDisconnected;
    
    public MinervaOutputStream (OutputStream os, Disconnect dis, Flag bDisconnected)
    {
	super (os);

	this.os            = os;
	this.dis           = dis;
	this.bDisconnected = bDisconnected;
	
	/*synchronized (sem) {
	  if (keepAliveThread == null) {
	  keepAliveThread = new KeepAliveThread ();
	  keepAliveThread.start();
	  }
	  }
	  keepAliveThread.addOutputStream (this);
	  time = System.currentTimeMillis();*/
    }
    
    public synchronized void write (int i) throws IOException
    {
	/*System.out.println ("wi");
	  time = System.currentTimeMillis();*/
	try {
	    os.write (i);
	} catch (IOException ioe) {
	    //System.out.println ("O1;" + ioe);
	    // Disconnect
	    /*	    if (dis != null) {
		if (!bDisconnected.isSet())
		    bDisconnected.set ();
		dis.disconnect();
		}*/
	    // Rethrow
	    throw ioe;
	}
    }
    
    public synchronized void write (byte[] b, int off, int len) throws IOException
    {
	/*System.out.println ("wb");
	  if (b.length == BEACON.length) {
	  // If trying to send the same as a beacon, add a final one
	  b = new byte[BEACON.length + 1];
	  b[BEACON.length] = 1;
	  }
	  
	  time = System.currentTimeMillis();*/
	try {
	    os.write (b, off, len);
	} catch (IOException ioe) {
	    // Disconnect
	    //System.out.println ("O;" + ioe);
	    /*if (dis != null) {
		if (!bDisconnected.isSet())
		    bDisconnected.set ();
		dis.disconnect();
		}*/
	    // Rethrow
	    throw ioe;
	}
    }
    
    public synchronized void checkAndWriteBeacon () 
    {
	System.out.println ("$$$$$$$$$$$$$$$");
	try {
	    long l = System.currentTimeMillis();
	    if (l - time > MAX_QUANTUM) {
		os.write (BEACON, 0, BEACON.length);	    
		time = l;
		System.err.println ("Sending beacon from socket " + this + "...");
	    }
	} catch (IOException ioe) {
	    System.err.println ("cagoto:" + ioe);
	}
    }

    public synchronized void close() throws IOException
    {
	System.out.println ("MOSCLOSE");
	/*if (!bDisconnected.isSet()) {
	    bDisconnected.set ();
	    if (dis != null) dis.disconnect();
	    }*/
	super.close();
    }
}
