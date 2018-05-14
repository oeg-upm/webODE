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
public class UpdateReferenceServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String orname       = req.getParameter (ORIGINAL_REFERENCE_NAME);
	String rname        = req.getParameter (REFERENCE_NAME);
	String rdescription = req.getParameter (REFERENCE_DESCRIPTION);
	String ontology     = req.getParameter (ONTOLOGY_NAME);

	// These things are in the session.
//	String oauthor      = (String) session.getAttribute (USER);
//	String ogroup       = (String) session.getAttribute (GROUP);
  Properties prop = (Properties)session.getAttribute("prop");
	if (orname == null || orname.trim().equals("")) {
	    error (res, prop.getProperty("Theoriginalreferencenameisamandatoryparameter."));
	    return;
	}

	if (rname == null || rname.trim().equals("")) {
	    error (res, prop.getProperty("Thereferencenameisamandatoryparameter."));
	    return;
	}

	if (ontology == null || ontology.trim().equals("")) {
	    error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	    return;
	}


	try {
	    // all right.  Let's insert this data into the database
	    ((ODEService) session.getAttribute (ODE_SERVICE)).updateReference
		(orname, new ReferenceDescriptor (rname, rdescription, ontology));

	    PrintWriter pw = res.getWriter ();
	    header(pw);
	    rname = rname + " ";
	    body (pw, prop.getProperty("Updatedreference")+ "<i>" + rname + "</i>" + prop.getProperty("successfully."));
	    sendUpdateEvent (pw, -1, orname, rname);
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}



