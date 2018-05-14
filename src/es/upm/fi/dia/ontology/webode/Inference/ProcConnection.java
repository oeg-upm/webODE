// Communication with ciaos
// Author: Juan Pablo Perez Aldea
// Version 1.0
// Date: 9-6-2001

package es.upm.fi.dia.ontology.webode.Inference;

import java.io.*;
import java.util.*;


public class ProcConnection
{
  private String[] cmd;
  private Process proc;
  private OutputStreamWriter out;
  private InputStreamReader in;
  private PipedReader pipeIn;
  private PipedWriter pipeOut;
  private ReadThread inth, errth;

  public ProcConnection(Process proc, InputStreamReader in, OutputStreamWriter out, PipedReader pipeIn)
  {
    try
    {
      this.proc = proc;
      this.in = in;
      this.out = out;
      this.pipeIn = pipeIn;
      }catch(Exception e)
      {
        System.out.println("Error: " + e);
      }
  }

  public void kill()
  {
    try
    {

      proc.destroy();
      } catch (Exception e)
      {
        System.out.println("Error: " + e);
      }
  }

  public String readThreads()
  {
    Vector vc = new Vector();
    int chr1, chr2;
    int size;
    boolean error = false;
    try
    {
      chr1 = pipeIn.read();
      chr2 = pipeIn.read();
      while (!(chr2 == '-' && chr1 == '?'))
      {
        if (chr1 == '?')
        {
          this.write(";\n");
          chr1 = pipeIn.read();
        }else
        {
          vc.add(new Integer(chr1));
          chr1 = chr2;
        }
        chr2 = pipeIn.read();
      }
      size = vc.size();
      char [] v = new char[size] ;

      for(int i=0; i<vc.size(); i++)
      {
        chr1 = ((Integer) vc.elementAt(i)).intValue();
        v[i] = (char) chr1;
      }

      return new String(v);

      }catch(Exception e)
      {
        System.out.println("Error: " + e);
        return null;
      }
  }

  public String readThreads2()
  {
    Vector vc = new Vector();
    int chr1, chr2;
    int size;
    try
    {
      while (pipeIn.ready())
      {
        chr1 = pipeIn.read();
        vc.add(new Integer(chr1));
      }
      size = vc.size();
      char [] v = new char[size] ;

      for(int i=0; i<vc.size(); i++)
      {
        chr1 = ((Integer) vc.elementAt(i)).intValue();
        v[i] = (char) chr1;
      }
      return new String(v);
      }catch(Exception e)
      {
        System.out.println("Error: " + e);
        return null;
      }
  }

  public void readStart()
  {
    int chr;
    try
    {
      chr = in.read();
      while (chr != '?')
      {
        chr = in.read();
      }
      chr = in.read();
      chr = in.read();

      }catch(Exception e)
      {
        System.out.println("Error: " + e);
      }
  }

  public void write(String text)
  {
    try
    {
      out.write(text, 0, text.length());
      out.flush();
      }catch(Exception e)
      {
        System.out.println("Error: " + e);
      }
  }

/*
 public static void main(String argv[])
 {
  String [] cmd = new String [5];
  String wdir = "D:\\Archivos de programa\\ciao-1.7p30Win32\\shell";

  cmd[0] = "D:\\Archivos de programa\\ciao-1.7p30Win32\\Win32\\bin\\ciaoengine.exe";
  cmd[1] = "-C";
  cmd[2] = "-i";
  cmd[3] = "-b";
  cmd[4] = "$/shell/ciaosh";

  Process proc;
  InputStreamReader in,err;
  OutputStreamWriter out;
  PipedReader pipeIn;
  PipedWriter pipeOut;
  ReadThread inth, errth;
  try
  {
   proc = (Runtime.getRuntime()).exec(cmd, null, new File(wdir));
   in = new InputStreamReader(proc.getInputStream());
   err = new InputStreamReader(proc.getErrorStream());
   out = new OutputStreamWriter(proc.getOutputStream());
   pipeIn = new PipedReader();
   pipeOut = new PipedWriter(pipeIn);
   ProcConnection shell = new ProcConnection(proc, in, out, pipeIn);
   inth = new ReadThread(in, pipeOut);
   errth = new ReadThread(err, pipeOut);
   shell.readStart();
   inth.start();
   System.out.println("1 esta iniciada");
   errth.start();
   System.out.println("2 esta iniciada");

   shell.write("ensure_loaded(iengine1).\n");
   System.out.println("1: " + shell.readThreads());
   shell.write("ensure_loaded(iengine).\n");
   System.out.println("2: " + shell.readThreads());
   shell.write("ensure_loaded(animales).\n");
   System.out.println("3: " + shell.readThreads());
   shell.write("get_class_instances(X,Y).\n");
   System.out.println("4: " + shell.readThreads());
   System.out.println("fin!");
   shell.kill();
  }catch(Exception e)
  {
  }
 }

 */
}