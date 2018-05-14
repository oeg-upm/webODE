// Communication with ciaos
// Author: Juan Pablo Perez Aldea
// Version 1.0
// Date: 9-6-2001

package es.upm.fi.dia.ontology.webode.Inference;
import java.io.*;

class ReadThread extends Thread {
  private InputStreamReader in;
  private PipedWriter pipe;

  ReadThread(InputStreamReader in, PipedWriter pipe) {
    this.in = in;
    this.pipe = pipe;
  }

  public void kill()
  {
    try
    {
      pipe.close();
      } catch (Exception e)
      {
        System.out.println("Error: " + e);
      }
  }

  public void run() {
    int chr;
    // read from the buffer
    try
    {
      while (true)
      {
        chr = in.read();
        pipe.write(chr);
        pipe.flush();
      }
      } catch(Exception e)
      {
   /*aqui*/
        //System.out.println("Error: " + e);
      }
  }//end run
} //end class