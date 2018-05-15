package es.upm.fi.dia.ontology.minerva.server.rmi;

import java.io.*; 
import java.net.*; 
import java.rmi.server.*; 
  
public class MinervaServerSocketFactory 
    implements RMIServerSocketFactory, Serializable { 

    private transient Disconnect dis;

    public MinervaServerSocketFactory (Disconnect dis)
    {
	//System.err.println ("MSSF: " + dis);
	this.dis = dis;
    }


    public ServerSocket createServerSocket(int port) 
        throws IOException 
    { 
	//System.err.println ("MSSF.createSocket: " + port + ", " + dis);
	return new MinervaServerSocket(port, dis); 
    } 
} 
  

