package es.upm.fi.dia.ontology.minerva.server.rmi;

import java.io.*;
import java.net.*;

public class MinervaSocket extends Socket
{
    private InputStream is;
    private OutputStream os;
    private Disconnect dis;
    private Flag flag;
    private boolean bClosed;

    public MinervaSocket (Disconnect dis) 
    {
	super ();
	System.out.println ("Socket NEW 1");
	
	this.dis = dis;
	this.flag = new Flag (false);
    }

    public MinervaSocket (String host, int port, Disconnect dis) throws IOException
    {
	super (host, port);
	System.out.println ("Socket NEW 2 " + (dis == null ? "NULL " : dis.getClass().getName()));
	this.dis = dis;

	this.flag = new Flag (false);
    }

    public InputStream getInputStream () throws IOException
    {
	if (is == null) {
	    is = new MinervaInputStream (super.getInputStream(), dis, flag);
	}

	return is;
    }

    public OutputStream getOutputStream() throws IOException
    {
	if (os == null) {
	    os = new MinervaOutputStream (super.getOutputStream(), dis, flag);
	} 
	
	return os;
    }

    public synchronized void close() throws IOException
    {
	System.out.println ("CLOSE-----------------" + this + "-" + bClosed);
	
	if (bClosed)
	    return;

	bClosed = true;	
	if (!flag.isSet() && dis != null) {
	    flag.set();
	    dis.disconnect();
	}

	os.flush();
	super.close();
    }
}



