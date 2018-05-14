package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

public class InsertTermFormulaServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String tname        = req.getParameter (TERM_NAME);
	String formula      = req.getParameter (FORMULA_NAME);
	String parent       = req.getParameter (PARENT_NAME);
	String tnameaux        = req.getParameter (TERM_NAME);
	String formulaaux      = req.getParameter (FORMULA_NAME);
//	String parentaux       = req.getParameter (PARENT_NAME);
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
	    formula == null || formula.trim().equals("")) {
	    error (res, prop.getProperty("Thetermandformulanamesaremandatoryparameters."));
	    return;
	}

	try {
	    // all right.  Let's insert this data into the database

	    if (parent == null)
		((ODEService) session.getAttribute (ODE_SERVICE)).relateFormulaToTerm
		    (oname, tname, formula);
	    else
		((ODEService) session.getAttribute (ODE_SERVICE)).relateFormulaToTerm
		    (oname, tname, parent, formula);

	    PrintWriter pw = res.getWriter ();
	    header(pw);
	    setRedirectParam (tname);
	    setRedirectParam (1, parent);
	    if (label != null) {
		setRedirectParam (2, label);
		setRedirectParam (3, backURL);
	    }
	    String onameaux;
	     onameaux= oname;
	     onameaux = onameaux + " ";
	     formula = formulaaux + " ";
	     tname = tnameaux + " ";
	     body (pw, prop.getProperty("Formulae")+ "<i>" + formulaaux + "</i> "+ prop.getProperty("addedtoterm")+
	    "<i>" +  tnameaux + "</i> " + prop.getProperty("inontology")+ "<i>" + onameaux + "</i>"+ prop.getProperty("successfully."));
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}

