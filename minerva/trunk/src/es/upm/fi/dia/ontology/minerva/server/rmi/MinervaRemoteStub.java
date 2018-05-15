package es.upm.fi.dia.ontology.minerva.server.rmi;

import java.rmi.*;
import java.rmi.server.*;
import java.io.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * The remote stub for Minerva Services.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 * @see     es.upm.fi.dia.ontology.minerva.server.services.MinervaServiceImp
 * @see     es.upm.fi.dia.ontology.minerva.server.services.MinervaService
 */
public class MinervaRemoteStub extends RemoteStub
{
    public static final int SLEEP_TIME = 45000;

    public MinervaRemoteStub (RemoteRef ref)
    {
	super (ref);
	//System.out.println (">>> COnstructor");
    }


    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException
    {
//	System.out.println ("BINGO!!!!!!!!!!!!!!!!!!!");
	stream.defaultReadObject();
//	System.out.println ("Default read successfull");

	final MinervaService ms = (MinervaService) this;
        if(ms.isStatefull()) {
          Thread th = new Thread (new Runnable () {
            public void run () {
              boolean b = true;
              do {
                try {
                  Thread.sleep (SLEEP_TIME);
                } catch (Exception e) {
                }
                try {
//                  System.out.println ("Alive? : " + ms.getClass());
                  ms.sendBeacon();
//                  System.out.println ("Yes!");
                } catch (Exception e) {
//                  System.out.println ("A failure took place.  Disconnecting.");
                  b = false;
                }
              } while (b);
            }
          });
          // This thread must be a daemon
          th.setDaemon (true);

          // Start it up
          th.start();
        }
    }
}
