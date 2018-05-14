package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

public class InsertTermServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String tname        = req.getParameter (TERM_NAME);
	String reference    = req.getParameter (REFERENCE_NAME);
	String tnameaux        = req.getParameter (TERM_NAME);
	String referenceaux    = req.getParameter (REFERENCE_NAME);

	// These things are in the session.
	String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);
	Properties prop = (Properties)session.getAttribute("prop");

	if (oname == null || oname.trim().equals("")) {
	    error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	    return;
	}
	if (tname == null || tname.trim().equals("") ||
	    reference == null || reference.trim().equals("")) {
	    error (res, prop.getProperty("Thetermandreferencenamesaremandatoryparameters."));
	    return;
	}

	try {
	    // all right.  Let's insert this data into the database
	    ((ODEService) session.getAttribute (ODE_SERVICE)).relateReferenceToTerm
		(oname, tname, reference);

	    PrintWriter pw = res.getWriter ();
	    headeraux(pw);
	    String onameaux;
	    onameaux = oname;

	    onameaux = onameaux + " ";
	    referenceaux = referenceaux + " ";
	     tnameaux = tnameaux + " ";
	    body (pw, prop.getProperty("Referencia")+ "<i>" + referenceaux + "</i>" + prop.getProperty("addedtoterm")+ "<i>" +
         tnameaux + "</i>" + prop.getProperty("inontology")+ "<i>" + onameaux + "</i>"+ prop.getProperty("successfully."));
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}
