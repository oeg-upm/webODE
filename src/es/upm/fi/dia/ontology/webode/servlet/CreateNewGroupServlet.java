package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;


import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Servlet for creating a new group.
 *
 * @version 0.2
 */
public class CreateNewGroupServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
    	Properties prop = (Properties)session.getAttribute("prop");
	String ontology     = (String) session.getAttribute (CURRENT_ONTOLOGY);
	String term         = req.getParameter (TERM_NAME);
//	String ontologyaux     = (String) session.getAttribute (CURRENT_ONTOLOGY);
//	String termaux         = req.getParameter (TERM_NAME);

	String desc         = req.getParameter (TERM_DESCRIPTION);
	String[] concepts   = req.getParameterValues (CONCEPTS);
	boolean bUpdate      = req.getParameter ("update") != null;
	String originalName = req.getParameter (ORIGINAL_TERM_NAME);
	String backURL      = req.getParameter (BACK_URL);

	if (ontology == null || ontology.trim().equals("") ||
	    term == null || term.trim().equals("") ||
	    concepts == null) {
	    error (res, prop.getProperty("Theontologyandtermnamesaremandatoryparameters."));
	    return;
	}

	try {
	    if (bUpdate) {
		// all right.  Let's insert this data into the database
		((ODEService) session.getAttribute (ODE_SERVICE)).
		    updateGroup (ontology, originalName, new Group (term, desc, concepts));

	    }
	    else {
		// all right.  Let's insert this data into the database
		((ODEService) session.getAttribute (ODE_SERVICE)).
		    addGroup (ontology, new Group (term, desc, concepts));
	    }

	    PrintWriter pw = res.getWriter ();
	    /*
	    // NUEVO PARA MULTILINGUALIDAD DANI
        pw.println("<html>");
        pw.println("  <head>");
        pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=../jsp/webode/about.jsp\"> ");
        pw.println("  </head>");
        // FIN NUEVO */
        header(pw);
	    if (backURL != null) {
		System.out.println ("BackURL: " + backURL);
		setURL (backURL);
		setRedirectParam ("");
	    }

	     body (pw, prop.getProperty("Grupo")+ "<i> " + term + "</i> "+  (bUpdate ?  prop.getProperty("Actualizar") :  prop.getProperty("inserte")) +
	     prop.getProperty("inontology") + "<i> " +  ontology + " </i> " + prop.getProperty("correctly."));
	    //body (pw, "Group <i>" + term + "</i> " + (bUpdate ? "updated" : "inserted") +
	    //	  " in ontology <i>" + ontology + "</i> correctly.");
	    sendAdditionEvent (pw, TermTypes.CONCEPT, term);
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}






