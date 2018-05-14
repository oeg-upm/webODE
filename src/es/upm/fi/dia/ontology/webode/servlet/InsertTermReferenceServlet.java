package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

public class InsertTermReferenceServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String tname        = req.getParameter (TERM_NAME);
	String reference    = req.getParameter (REFERENCE_NAME);
	String parent       = req.getParameter (PARENT_NAME);
	String label        = req.getParameter (LABEL);
	String backURL      = req.getParameter (BACK_URL);

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

	    System.out.println ("PArent: " + parent + "-- TERM: " + tname);
	    if (parent == null)
		((ODEService) session.getAttribute (ODE_SERVICE)).relateReferenceToTerm
		    (oname, tname, reference);
	    else
		((ODEService) session.getAttribute (ODE_SERVICE)).relateReferenceToTerm
		    (oname, tname, parent, reference);

	    PrintWriter pw = res.getWriter ();
	    header(pw);
	    setRedirectParam (tname);
	    setRedirectParam (1, parent);
	    if (label != null) {
		setRedirectParam (2, label);
		setRedirectParam (3, backURL);

	    }
	    reference= reference + " ";
	    tname = tname + "";
	    oname=oname + "";

	    body (pw, prop.getProperty("Referencia")+ "<i> " + reference + "</i> "+ prop.getProperty("addedtoterm")+ "<i> " +
		  tname + "</i> "+ prop.getProperty("inontology")+ "<i> " + oname + "</i> " + prop.getProperty("successfully."));
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}
