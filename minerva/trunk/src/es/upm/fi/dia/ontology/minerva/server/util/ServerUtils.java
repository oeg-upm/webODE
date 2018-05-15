package es.upm.fi.dia.ontology.minerva.server.util;

import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.admin.*;

/**
 * Class to provide some utilities.
 *
 * @author   Julio César Arpírez Vega.
 * @version  0.1
 */
public class ServerUtils 
{
    /**
     * Converts an iterator into an array.
     *
     * @param it The iterator to translate.
     * @return An object array holding the contents of the enumeration.
     */
    public static ServiceDescriptor[] iteratorToArray(Iterator en)
    {
	Vector v = new Vector();
	
	while (en.hasNext())
	    v.addElement (en.next());

	ServiceDescriptor[] ao = new ServiceDescriptor[v.size()];
	v.copyInto (ao);

	return ao;
    }
}


