package es.upm.fi.dia.ontology.minerva.tools;

import java.io.*;

/**
 * The Minerva RMI Compiler.
 * <p>
 * It uses the standard RMI compiler and replaces some
 * things to make stubs suitable for the Minerva Application
 * Server.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class MinervaRMICompiler
{
    public static void main (String[] args) throws Exception
    {
	if (args.length == 0) {
	    System.err.println ("Usage: minervarmic <class names>");
	    System.exit (1);
	}

	String[] rmiArgs = new String[args.length + 5];
	System.arraycopy (args, 0, rmiArgs, 5, args.length);

	// Arguments for the compiler
	rmiArgs[0] = "rmic";
	rmiArgs[1] = "-keep";
	rmiArgs[2] = "-d";
	rmiArgs[3] = ".";
        rmiArgs[4] = "-v1.2";

	// First, invoke the rmi compiler by means of an exec.
	Process proc = Runtime.getRuntime().exec (rmiArgs);

	// Wait for the process to finish
	proc.waitFor ();

	// Get exit value
	int exitValue = proc.exitValue ();
	if (exitValue != 0) {
	    System.err.println ("Error: " + exitValue);
            String line;
            BufferedReader err=new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            while((line=err.readLine())!=null)
              System.err.println("  " + line);

            BufferedReader out=new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while((line=out.readLine())!=null)
              System.err.println("  " + line);

	    System.exit (exitValue);
	}

	// Replace RemoteStub by MinervaRemoteStub in all generated stubs
	for (int i = 0; i < args.length; i++)
	    replaceStub (args[i]);

	// Finally, compile the files
	String[] javaArgs = new String[args.length + 1];
	for (int i = 0; i < args.length; i++) {
	    javaArgs[i + 1] = args[i].replace ('.', File.separatorChar) + "_Stub.java";
	}
	javaArgs[0] = "javac";
	// First, invoke the java compiler by means of an exec.
        proc = Runtime.getRuntime().exec (javaArgs);

	// Wait for the process to finish
	proc.waitFor ();

	// Get exit value
	exitValue = proc.exitValue ();
	if (exitValue != 0) {
	    System.err.println ("Error: " + exitValue);
	    System.exit (exitValue);
	}

	// Remove temporary files
	for (int i = 0; i < args.length; i++) {
	    new File (args[i].replace ('.', File.separatorChar) + "_Stub.java").delete();
	}

	System.exit (0);
    }

    private static void replaceStub (String arg)
    {
	// Replace dots by file separators.
	arg = arg.replace ('.', File.separatorChar) + "_Stub.java";

	FileReader fr = null;
	FileWriter fw = null;
	try {
	    fr = new FileReader (arg);
	    File tmpFile = new File ("mrmic" + System.currentTimeMillis());
	    fw = new FileWriter (tmpFile);
	    char[] buffer = new char[5000];

	    int read = fr.read (buffer, 0, 1000);

	    String str = new String (buffer, 0, read);
	    int index = str.indexOf ("java.rmi.server.RemoteStub");

	    //System.out.println ("STR: " + str);

	    // Modify headers
	    fw.write (str.substring (0, index));
	    fw.write ("es.upm.fi.dia.ontology.minerva.server.rmi.MinervaRemoteStub");
	    fw.write (str.substring (index + "java.rmi.server.RemoteStub".length()));

	    // Copy the rest of the file
	    while ((read = fr.read (buffer)) != -1) {
		fw.write (buffer, 0, read);
	    }

	    fr.close();
	    fw.close();

	    File f = new File(arg);
	    f.delete();
	    tmpFile.renameTo (f);
	} catch (Exception e) {
	    System.err.println ("Error compiling file " + arg + ": " + e);
	}
    }
}
