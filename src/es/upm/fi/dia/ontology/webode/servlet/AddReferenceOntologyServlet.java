package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

public class AddReferenceOntologyServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String oname        = req.getParameter (ONTOLOGY_NAME);
	String rname        = req.getParameter (REFERENCE_NAME);

	String rdescription = req.getParameter (REFERENCE_DESCRIPTION);

	// These things are in the session.
	String oauthor      = (String) session.getAttribute (USER);
	String ogroup       = (String) session.getAttribute (GROUP);
        Properties prop = (Properties)session.getAttribute("prop");
	if (oname == null || oname.trim().equals("")) {
	     error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	    return;
	}

	if (rname == null || rname.trim().equals("")) {
	    error (res, prop.getProperty("Thereferencenameisamandatoryparameter."));
	    return;
	}

	try {

	    // all right.  Let's insert this data into the database
	    ((ODEService) session.getAttribute (ODE_SERVICE)).addReferenceToOntology
		(oname, new ReferenceDescriptor (rname, rdescription));

	    PrintWriter pw = res.getWriter ();
	     // NUEVO PARA MULTILINGUALIDAD DANI
      /*  pw.println("<html>");
        pw.println("  <head>");
        pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=../jsp/webode/about.jsp\"> ");
        pw.println("  </head>");
        // FIN NUEVO */
        //headerauxAddReferenceOntology(pw);
        header(pw);
	    body (pw, prop.getProperty("Addedreference")+ "<i> " + rname + "</i> "+ prop.getProperty("toontology") + oname + ".");

	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}
