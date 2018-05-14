package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Servlet responsible for inserting a new instance.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class InsertNewInstanceServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String instanceName  = req.getParameter (INSTANCE_NAME);
	String termName      = req.getParameter (TERM_NAME);
	String instanceNameaux  = req.getParameter (INSTANCE_NAME);
	String termNameaux      = req.getParameter (TERM_NAME);
	String description   = req.getParameter (TERM_DESCRIPTION);


	// These things are in the session.
	String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);
	String instanceSet  = (String) session.getAttribute (ACTIVE_INSTANCE_SET);
        Properties prop = (Properties)session.getAttribute("prop");
	if (oname == null || oname.trim().equals("")) {
	    error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	    return;
	}
	if (termName == null || termName.trim().equals("") ||
	    instanceName == null || instanceName.trim().equals("")) {
	    error (res, prop.getProperty("Thetermandinstancenamesaremandatoryparameters."));
	    return;
	}

	try {
	    // all right.  Let's insert this data into the database
	    ((ODEService) session.getAttribute (ODE_SERVICE)).insertInstance
		(oname, new Instance (instanceName, termName, instanceSet, description));

	    PrintWriter pw = res.getWriter ();
	    header(pw);
	    setRedirectParam (termName);
	    String onameaux;
	    onameaux = oname;
	    onameaux = onameaux + " ";
	    instanceNameaux = instanceNameaux + " ";
	    termNameaux = termNameaux + " ";
	    body (pw, prop.getProperty("Instance")+ "<i> " + instanceNameaux + "</i>" + prop.getProperty("forterm")+ "<i>" +
		  termNameaux + "</i>"+prop.getProperty("insertedsucessfullyinontology")+"<i>" + onameaux + "</i>.");
	    trailer (pw);
	} catch (Exception e) {
	    error (res,  e.getMessage(), e);
	}
    }
}





