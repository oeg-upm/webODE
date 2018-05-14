package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

public class UpdateOntologyServlet extends BaseServlet
{
    public void init (ServletConfig sc) throws ServletException
    {
	super.init (sc);

    }

    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String originalName = req.getParameter (ORIGINAL_ONTOLOGY_NAME);
	String oname        = req.getParameter (ONTOLOGY_NAME);
//	String originalNameaux = req.getParameter (ORIGINAL_ONTOLOGY_NAME);
	String onameaux        = req.getParameter (ONTOLOGY_NAME);
	String odescription = req.getParameter (ONTOLOGY_DESCRIPTION);
	String onamespace = req.getParameter (NAMESPACE);
	boolean bAllow = req.getParameter (ALLOW_GROUP) != null &&
	    req.getParameter (ALLOW_GROUP).equals ("yes");

	// These things are in the session.
	String oauthor      = (String) session.getAttribute (USER);
	String ogroup       =  req.getParameter (GROUP);
	Properties prop = (Properties)session.getAttribute("prop");
	if (oname == null || oname.trim().equals("")) {
	      error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	    return;
	}

	try {
	    // all right.  Let's insert this data into the database
	    ((ODEService) session.getAttribute (ODE_SERVICE)).updateOntology
		(originalName,
		 new OntologyDescriptor (oname, onamespace, odescription, oauthor,
					 ogroup, null, null, bAllow));

	    PrintWriter pw = res.getWriter ();


	    header(pw);

	     onameaux = onameaux + " ";
	    body (pw, prop.getProperty("Ontologya")+ "<i>" + oname + " </i>"+prop.getProperty("updatedsuccessfully."));
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}






