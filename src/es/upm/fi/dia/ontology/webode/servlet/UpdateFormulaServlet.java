package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

public class UpdateFormulaServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String originalName = req.getParameter (ORIGINAL_TERM_NAME);
	String tname        = req.getParameter (TERM_NAME);
//	String originalNameaux = req.getParameter (ORIGINAL_TERM_NAME);
	String tnameaux        = req.getParameter (TERM_NAME);
	String tdescription = req.getParameter (TERM_DESCRIPTION);
	int    type         = Integer.parseInt (req.getParameter ("type"));
	String expression   = req.getParameter (EXPRESSION);
	String prolog_expression = req.getParameter ("prolog_expression");
	Properties prop = (Properties)session.getAttribute("prop");

	// These things are in the session.
	String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);

	if (oname == null || oname.trim().equals("")) {
	   error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	    return;
	}
	if (tname == null || tname.trim().equals("")) {
	    error (res, prop.getProperty("Thetermnameisamandatoryparameter."));
	    return;
	}

	try {
	    // all right.  Let's insert this data into the database
	    ((ODEService) session.getAttribute (ODE_SERVICE)).updateReasoningElement
		(originalName, new FormulaDescriptor (oname, tname, tdescription, expression, prolog_expression, type));

	    PrintWriter pw = res.getWriter ();
	    header(pw);
	    sendUpdateEvent (pw, TermTypes.RULE, originalName, tname);
	    String onameaux;
	    onameaux=oname;
	    onameaux = onameaux + " ";
	    tnameaux = tnameaux + " ";
	    body (pw, prop.getProperty("Razonamiento")+ "<i>" + tnameaux + "</i>"+ prop.getProperty("inontology")+ "<i>" + onameaux + "</i>"+ prop.getProperty("updatedsuccessfully."));
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}
