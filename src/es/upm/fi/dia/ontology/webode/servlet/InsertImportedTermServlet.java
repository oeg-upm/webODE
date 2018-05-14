package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 *
 * @version 0.1
 */
public class InsertImportedTermServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
    	//String term         = req.getParameter (TERM_NAME);
	String namespace    = req.getParameter (NAMESPACE);
	String namespace_identifier = req.getParameter (NAMESPACE_IDENTIFIER);
	String uri         = req.getParameter (URI);
	String uriaux         = req.getParameter (URI);
	Properties prop = (Properties)session.getAttribute("prop");

	if (namespace == null || namespace.trim().equals("") ||
	    uri == null || uri.trim().equals("") ||
	    namespace_identifier == null || namespace_identifier.trim().equals("")) {
	    error (res, prop.getProperty("Thenamespace,namespaceidentifierandURItermnamesaremandatoryparameters."));

	    return;
	}

	String oname;
	int j = uri.indexOf('#', 0);

	if (j<0 || j+1 >= uri.length()) {
	 	oname = namespace_identifier + ":";
	 	error (res, prop.getProperty("Theurimusthaveanidentifiername."));
	 	return;
	} else {
		oname = namespace_identifier + ":" + uri.substring(j+1);
	}

	String ontologyName = (String) session.getAttribute (CURRENT_ONTOLOGY);
	if (ontologyName == null || ontologyName.trim().equals("")) {
	    error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
	    return;
	}


	try {
	   /* String url = "webode://" + host + "/" +
		ontology + "/" + originalName;*/
	    // all right.  Let's insert this data into the database
	    ((ODEService) session.getAttribute (ODE_SERVICE)).
		importTerm (ontologyName, namespace, namespace_identifier, oname, uri);


	    PrintWriter pw = res.getWriter ();
	    header(pw);
	    setRedirectParam ("foo");
	    sendAdditionEvent (pw, TermTypes.IMPORTED_TERM, oname);
	    String onameaux;
	    onameaux=oname;
	    onameaux = onameaux + " ";
	    uriaux = uriaux + " ";
	    body (pw, prop.getProperty("Termi") + "<i>" + onameaux + "</i>"+ prop.getProperty("importedfromURI")+ "<i>" + uriaux + "</i>.");
	    sendAdditionEvent (pw, TermTypes.CONCEPT, oname);
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}






