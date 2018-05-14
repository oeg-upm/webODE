package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

public abstract class OperationsServlet extends BaseServlet
{    
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// These things are in the session.
//	String oauthor      = (String) session.getAttribute (USER);
//	String ogroup       = (String) session.getAttribute (GROUP);

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
		removeElements (odeService, currentOntology, vRemoved);
		addElements (odeService, currentOntology, vNew);
		updateElements (odeService, currentOntology, vDirty);
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
	    System.err.println ("Error in operations servlet: " + e);
	}
    }

    protected abstract void removeElements (ODEService odeService, String currentOntology, 
				  Vector vRemoved) throws Exception;

    protected abstract void addElements (ODEService odeService, String currentOntology, 
			       Vector vAdded) throws Exception;

    protected abstract void updateElements (ODEService odeService, String currentOntology, 
					    Vector vRemoved) throws Exception;
}





