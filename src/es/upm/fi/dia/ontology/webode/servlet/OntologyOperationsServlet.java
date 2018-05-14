package es.upm.fi.dia.ontology.webode.servlet;

import java.util.*;

import es.upm.fi.dia.ontology.webode.service.*;
//import es.upm.fi.dia.ontology.webode.ui1.designer.*;
//import es.upm.fi.dia.ontology.webode.ui.designer.*;
import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

public class OntologyOperationsServlet extends OperationsServlet
{
    /*public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// These things are in the session.
	String oauthor      = (String) session.getAttribute (USER);
	String ogroup       = (String) session.getAttribute (GROUP);

	try {
	    // Read data
	    ObjectInputStream ois = new ObjectInputStream (req.getInputStream());

	    // Data to be read
	    Vector vRemoved, vNew, vDirty;

	    // First, read removed elements.
	    vRemoved = (Vector) ois.readObject();
	    // Now, new ones.
	    vNew     = (Vector) ois.readObject();
	    // Finally dirty ones.
	    vDirty   = (Vector) ois.readObject();

	    ois.close();

	    ODEService odeService = (ODEService) session.getAttribute (ODE_SERVICE);
	    String currentOntology = (String) session.getAttribute (CURRENT_ONTOLOGY);
	    int retCode = 0;
	    try {
		_removeElements (odeService, currentOntology, vRemoved);
		_addElements (odeService, currentOntology, vNew);
		_updateElements (odeService, currentOntology, vNew);
	    } catch (Exception e) {
		System.err.println ("OntologyOperationsServlet: " + e);
		e.printStackTrace (System.err);
		// There's been an error.
		retCode = -1;
	    }

	    // Answer client.
	    DataOutputStream dos = new DataOutputStream (res.getOutputStream());
	    dos.writeInt (retCode);
	    dos.close();


	} catch (Exception e) {
	    System.err.println ("error -------_ " + e);
	}
	}*/

    protected void removeElements (ODEService odeService, String currentOntology,
				  Vector vRemoved) throws Exception
    {
	System.out.println ("Removing elements: " + vRemoved.size());
	// Try to actually remove elements
	for (int i = 0; i < vRemoved.size(); i++) {
	    Object obj = vRemoved.elementAt (i);
	    if (obj instanceof Element) {
		System.out.println ("Element: "+ ((Element) obj).getName());
//		odeService.removeRelatedOntology (currentOntology, ((Element) obj).getName());
	    }
	    else {
		System.out.println ("Relation: "+ ((Relation) obj).getName());
//		odeService.removeOntologyRelation (currentOntology, ((Relation) obj).getName());
	    }
	}
    }

    protected void addElements (ODEService odeService, String currentOntology,
			       Vector vAdded) throws Exception
    {
	System.out.println ("Adding elements: " + vAdded.size());

	// Insert ontologies first.
	for (int i = 0; i < vAdded.size(); i++) {
	    Object obj = vAdded.elementAt (i);
	    if (obj instanceof Element) {
		Element el = (Element) obj;
		odeService.createOntologyOntology (currentOntology, el.getName(), el.getX(), el.getY());
	    }
	    else if (obj instanceof Relation) {
//    		Relation rel = (Relation) obj;
//		odeService.createOntologyRelation (new OntologyRelation
//						   (currentOntology, rel.getOrigin(),
//						    rel.getDestination(), rel.getName()));
	    }
	}
    }

    protected void updateElements (ODEService odeService, String currentOntology,
				   Vector vRemoved) throws Exception
    {
	System.out.println ("Updating elements: " + vRemoved.size());

	for (int i = 0; i < vRemoved.size(); i++) {
	    Object obj = vRemoved.elementAt (i);
	    if (obj instanceof Element) {
		Element el = (Element) obj;
		// Update ontology position
		odeService.updateOntologyPosition (currentOntology,
						   new OntologyPositionDescriptor
						   (el.getName(), el.getX(), el.getY()));
	    }
	}
    }
}
