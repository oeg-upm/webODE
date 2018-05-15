package es.upm.fi.dia.ontology.minerva.server.rmi;

import java.io.*; 
import java.net.*; 
import java.rmi.server.*; 

public class MinervaClientSocketFactory 
    implements RMIClientSocketFactory, Serializable { 

    private transient Disconnect dis;

    public MinervaClientSocketFactory (Disconnect dis)
    {
	System.err.println ("MCSF: " + dis);
	this.dis = dis;
    }

    public Socket createSocket(String host, int port) 
        throws IOException 
    { 
	System.err.println ("MCSF.createSocket: " + host + ", " + port + ", " + dis);
        return new MinervaSocket(host, port, dis); 
    } 
} 
 
