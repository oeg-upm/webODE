package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import es.upm.fi.dia.ontology.webode.servlet.*;

public class MakeReport 
{
	public static void main (String[] args) throws Exception
	{
		if (args.length == 0) {
			System.err.println ("MakeReport <dir>");
			System.exit (1);
		}

		File dir = new File (args[0]);
		String[] files = dir.list();
	
		PrintWriter pw = new PrintWriter (new FileWriter ("report.html"));
		header (pw);
		for (int i = 0; i < files.length; i++) {
			ObjectInputStream ois = new ObjectInputStream 
				(new FileInputStream (new File (dir, files[i])));
			writeEntry (pw, (BugDescriptor) ois.readObject(), i + 1);
		}

		trailer (pw);
	}

	private static void header (PrintWriter pw)
	{
		pw.println ("<html><head><title>Bug report</title></head><body>");
		pw.println ("<center>");
		pw.println ("<table border=1>");
		pw.println ("<tr>");
		pw.println ("<th bgcolor=wheat>No.<th bgcolor=wheat>Person</th><th bgcolor=wheat>Description</th><th bgcolor=wheat>Steps</th>");
		pw.println ("</tr>");
	}

	private static void writeEntry (PrintWriter pw, BugDescriptor bd, int i)	
	{
		pw.println ("<tr>");
	   	pw.println (" <td>" + i + "</td>");
		pw.println (" <td>" + (bd.person == null ? "&nbsp;" : bd.person) + "</td>");
		pw.println (" <td>" + (bd.description == null ? "&nbsp;" : bd.description )+ "</td>");
		pw.println (" <td>" + (bd.steps == null ? "&nbsp;" : bd.steps) + "</td>");
		pw.println ("</tr>");
	}

	private static void trailer (PrintWriter pw)
	{
		pw.println ("</table></body></html>");
		pw.flush();
		pw.close();
	}
}