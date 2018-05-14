package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Servlet responsible for inserting a new synonym or abbreviation
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public class InsertSynonymAbbreviationServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String termName      = req.getParameter (TERM_NAME);
	String synAbbrName   = req.getParameter (SYNONYM_NAME);
	String termNameaux      = req.getParameter (TERM_NAME);
	String synAbbrNameaux   = req.getParameter (SYNONYM_NAME);
	String description   = req.getParameter (TERM_DESCRIPTION);
	String parentTerm    = req.getParameter (PARENT_NAME);
	String label        = req.getParameter (LABEL);
	String backURL      = req.getParameter (BACK_URL);
        Properties prop = (Properties)session.getAttribute("prop");
	boolean isAbbr = false;
	if (synAbbrName == null) {
	    synAbbrName = req.getParameter (ABBREVIATION_NAME);
	    synAbbrNameaux= req.getParameter (ABBREVIATION_NAME);
	    isAbbr = true;
	}

	String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);

	if (oname == null || oname.trim().equals("")) {
	   error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	    return;
	}
	if (termName == null || termName.trim().equals("") ||
	    synAbbrName == null || synAbbrName.trim().equals("")) {
	   error (res, prop.getProperty("Thetermandsynonym/abbreviationnamesaremandatoryparameters.") + termName + "-" + synAbbrName);
	    return;
	}

	try {
	    SynonymAbbreviation sa = new SynonymAbbreviation (synAbbrName,
							      termName,
							      parentTerm,
							      isAbbr ? TermTypes.ABBREVIATION : TermTypes.SYNONYM,
							      description);
	    // all right.  Let's insert this data into the database
	    if (isAbbr) {
		((ODEService) session.getAttribute (ODE_SERVICE)).
		    addAbbreviationToTerm (oname, sa);
	    }
	    else {
		((ODEService) session.getAttribute (ODE_SERVICE)).
		    addSynonymToTerm (oname, sa);
	    }

	    PrintWriter pw = res.getWriter ();
	    header(pw);
	    setRedirectParam (termName);
	    setRedirectParam (1, isAbbr ? "true" : "false");
	    if (parentTerm != null)
		setRedirectParam (2, parentTerm);
	    if (label != null) {
		setRedirectParam (3, label);
		setRedirectParam (4, backURL);
	    }
	     String onameaux;
	     onameaux= oname;
	     onameaux = onameaux + " ";
	     synAbbrNameaux = synAbbrNameaux + " ";
	     termNameaux = termNameaux + " ";
	     body (pw, (isAbbr ?  prop.getProperty("Abreviatura") :  prop.getProperty("Sinonimo")) + synAbbrNameaux +
	     prop.getProperty("forterm") + "<i>" +  termName + "</i> " + prop.getProperty("insertedsucessfullyinontology")
	     + "<i> " + oname + "</i>. ");


	    //body (pw, (isAbbr ? "Abbreviation" : "Synonym") + "<i> " + synAbbrName + "</i> for term <i>" +
            //termName + "</i> inserted sucessfully in ontology <i>" + oname + "</i>.");
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}
