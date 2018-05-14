package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Servlet responsible for inserting a new class or instance attribute value.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public class InsertClassAttributeValueServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// Get parameters
	String attributeName  = req.getParameter (ATTRIBUTE_NAME);
	String termName       = req.getParameter (TERM_NAME);
	String attributeNameaux  = req.getParameter (ATTRIBUTE_NAME);
	String termNameaux       = req.getParameter (TERM_NAME);
	String attributeValue = req.getParameter (ATTRIBUTE_VALUE);
	String foo            = req.getParameter (INSTANCE);
	boolean bIns                = foo != null && foo.equals ("true");

	// These things are in the session.
	String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);

	Properties prop = (Properties)session.getAttribute("prop");

	if (oname == null || oname.trim().equals("")) {
	    error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	    return;
	}
	if (termName == null || termName.trim().equals("") ||
	    attributeName == null || attributeName.trim().equals("")) {
	   //error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	   error (res, prop.getProperty("Thetermandattributenamesaremandatoryparameters."));
	    return;
	}
	if (attributeValue == null || attributeValue.trim().equals("")) {
	    error (res, prop.getProperty("Theattributevaluecannotbeempty."));
	    return;
	}

	try {
	    // all right.  Let's insert this data into the database
	    if (bIns) {
		((ODEService) session.getAttribute (ODE_SERVICE)).
		    addValueToInstanceAttribute (oname, attributeName, termName, attributeValue);
	    }
	    else {
		((ODEService) session.getAttribute (ODE_SERVICE)).
		    addValueToClassAttribute (oname, attributeName, termName, attributeValue);
	    }

	    PrintWriter pw = res.getWriter ();
	    header(pw);
	    setRedirectParam (termName);
	    setRedirectParam (1, attributeName);
	    setRedirectParam (2, bIns ? "true" : "false");

	    //body (pw, (bIns ? "Instance" : "Class") +
	    //	  " attribute value for <i>" + attributeName + "</i> in term <i>" +
	    //	  termName + "</i> inserted sucessfully in ontology <i>" + oname + "</i>.");
	    String onameaux;
	     onameaux=oname;
             onameaux = onameaux + " ";
             attributeNameaux = attributeNameaux + " ";
             termNameaux = termNameaux + " ";
	     body (pw, (bIns ?  prop.getProperty("Instancia") :  prop.getProperty("Clase")) +
	     prop.getProperty("attributevaluefor") + "<i>" +  attributeNameaux + "</i>" + prop.getProperty("interm")+"<i>" +
	     termNameaux + "</i>"+ prop.getProperty("insertedsucessfullyinontology")+ "<i>" + onameaux + "</i>.");
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}
