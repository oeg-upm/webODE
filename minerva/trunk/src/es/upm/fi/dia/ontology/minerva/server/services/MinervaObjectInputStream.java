package es.upm.fi.dia.ontology.minerva.server.services;

import java.io.*;

public class MinervaObjectInputStream extends ObjectInputStream
{
    private ClassLoader cl;

    public MinervaObjectInputStream (ClassLoader cl, InputStream is) throws IOException
    {
	super (is);

	this.cl = cl;
    }

    protected Class resolveClass (ObjectStreamClass v)
	throws IOException, ClassNotFoundException
    {
	try {
	    return super.resolveClass (v);
	} catch (ClassNotFoundException cnfe) {
	    return Class.forName (v.getName(), true, cl);
	}
    }
}
