package es.upm.fi.dia.ontology.webode.ui.designer.model;

import java.util.*;
import es.upm.fi.dia.ontology.webode.service.*;

public class Test
{
    public static void main (String[] args) throws Exception
    {
	DefaultDesignModel ddm = new DefaultDesignModel();

	System.out.println (ddm);
	
	// Allow checks
	//ddm.setChecks (true);

	System.out.println ("Try some elements:");
	try {
	    ddm.addElement (new Element ("hola", 1, 1));
	    ddm.addElement (new Element ("caracola", 1, 1));

	    ddm.removeElement ("hola");
	    ddm.addElement (new Element ("hola", 2, 2));
	    
	    ddm.addElement (new Element ("jander"));
	} catch (Exception e) {
	    System.out.println ("Error: " + e);
	}
	System.out.println ("Try some relationships:");
	try {
	    ddm.addRelation (new Relation ("caca", "hola", "caracola"));
	    //ddm.addRelation (new Relation ("caca", "caracola", "hola"));

	    ddm.addRelation (new Relation (TermRelation.SUBCLASS_OF, "hola", "caracola"));
	    ddm.addRelation (new Relation (TermRelation.SUBCLASS_OF, "caracola", "caracola"));
	    //ddm.addRelation (new Relation (TermRelation.SUBCLASS_OF, "caracola", "hola"));
	    //ddm.addRelation (new Relation (TermRelation.SUBCLASS_OF, "caracola", "jander"));
	    //ddm.addRelation (new Relation (TermRelation.SUBCLASS_OF, "jander", "caracola"));
	} catch (Exception e) {
	    System.out.println ("Error: " + e);
	    e.printStackTrace();
	}
	
	System.out.println (ddm);

	System.out.println ("\n\nSecond (Partitions):");
	
	// Create a group
	Vector v = new Vector();
	v.addElement (new Element ("eso"));
	v.addElement (new Element ("si"));
	Group group = new Group ("partition", v);
	ddm.addElement (group);

	try {
	    //ddm.addRelation (new Relation (TermRelation.SUBCLASS_OF, "hola", "caracola"));
	    ddm.addRelation (new Relation (TermRelation.EXHAUSTIVE, "partition", "hola"));
	    System.out.println ("--------------");
	    ddm.addRelation (new Relation (TermRelation.SUBCLASS_OF, "caracola", "si"));
	} catch (Exception e) {
	    System.out.println (">>> " + e);
	}
	System.out.println (ddm);	

	System.out.println ("RELATIONS");
	Relation[] arel = ddm.getRelations();
	for (int i = 0; arel != null && i < arel.length; i++)
	    System.out.println (" - " + arel[i]);
    }
}

