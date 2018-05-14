package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;

public class BugDescriptor implements java.io.Serializable
{
	String person, description, steps;
	Date date;

	public BugDescriptor (String person, String description, String steps)
	{
		this.person = person;
		this.description = description;
		this.steps = steps;

		date = new Date();
	}

	public void store (File file) throws IOException
	{
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream (
				new FileOutputStream (file));
			oos.writeObject (this);
		} finally {
			if (oos != null) oos.close();
		}
	}
	
}