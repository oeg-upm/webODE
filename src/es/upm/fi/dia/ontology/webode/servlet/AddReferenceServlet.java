package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 *
 * @version 0.2
 */
public class AddReferenceServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String reference    = req.getParameter (REFERENCE_NAME);

	String description  = req.getParameter (REFERENCE_DESCRIPTION);

	// These things are in the session.
	String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);

	Properties prop = (Properties)session.getAttribute("prop");

	if (oname == null || oname.trim().equals("")) {
	    error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	    return;
	}
	if (reference == null || reference.trim().equals("")) {
	    error (res, prop.getProperty("Thereferencenameisamandatoryparameter."));
	    return;
	}

	try {
	    // all right.  Let's insert this data into the database
	    ((ODEService) session.getAttribute (ODE_SERVICE)).addReference
		(oname, new ReferenceDescriptor (reference, description));

	    PrintWriter pw = res.getWriter ();
	    /*
	    // NUEVO PARA MULTILINGUALIDAD DANI
        pw.println("<html>");
        pw.println("  <head>");
        pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=../jsp/webode/about.jsp\"> ");
        pw.println("  </head>");
        // FIN NUEVO
	    */
	    header(pw);
	    body (pw, prop.getProperty("Referencia")+ "<i> " + reference + " </i> "+ prop.getProperty("addedtoontology")+"<i> " + oname + " </i> "+prop.getProperty("successfully."));

	    sendAdditionEvent (pw, -1, reference);
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}



























