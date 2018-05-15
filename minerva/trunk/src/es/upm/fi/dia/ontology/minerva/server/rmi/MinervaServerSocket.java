package es.upm.fi.dia.ontology.minerva.server.rmi;

import java.io.*;
import java.net.*;

public class MinervaServerSocket extends ServerSocket
{
    private Disconnect dis;

    public MinervaServerSocket (int port, Disconnect dis) throws IOException
    {
	super (port);
	this.dis = dis;
    }

    public Socket accept () throws IOException
    {
	System.out.println ("TO CREATE...");
	Socket s = new MinervaSocket(dis);
	
	System.out.println ("CREATED...");
	
	implAccept (s);
	/*try {
	  // Not to buffered anything.  Necessary for the beacons to work properly.
	  s.setTcpNoDelay(false);
	  } catch (IOException e) {
	  System.out.println ("This shouldn't have happened...: " + e);
	  throw e;
	  }*/
	return s;
    }

    public void close() throws IOException
    {
	System.out.println ("SERVER CLOSED");
	super.close();
    }
}


