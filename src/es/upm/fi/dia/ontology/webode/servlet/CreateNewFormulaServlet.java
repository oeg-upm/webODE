package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Creates a new reasoning element.
 *
 * @version 0.1
 */
public class CreateNewFormulaServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	String ontology     = (String) session.getAttribute (CURRENT_ONTOLOGY);
	String term         = req.getParameter (TERM_NAME);

//	String ontologyaux     = (String) session.getAttribute (CURRENT_ONTOLOGY);
//	String termaux         = req.getParameter (TERM_NAME);

	int    type         = Integer.parseInt (req.getParameter ("type"));
	String desc         = req.getParameter (TERM_DESCRIPTION);
	String expression   = req.getParameter (EXPRESSION);
	String prolog_expression = req.getParameter ("prolog_expression");

        Properties prop = (Properties)session.getAttribute("prop");

	if (ontology == null || ontology.trim().equals("") ||
	    term == null || term.trim().equals("")) {
	     error (res, prop.getProperty("Theontologyandtermnamesaremandatoryparameters."));
	    return;
	}

	try {

	    // all right.  Let's insert this data into the database
	    ((ODEService) session.getAttribute (ODE_SERVICE)).insertReasoningElement
		(new FormulaDescriptor (ontology, term, desc, expression, prolog_expression, type));

	    PrintWriter pw = res.getWriter ();
	    /*
	    // NUEVO PARA MULTILINGUALIDAD DANI
        pw.println("<html>");
        pw.println("  <head>");
        pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=../jsp/webode/about.jsp\"> ");
        pw.println("  </head>");
        // FIN NUEVO */
        header(pw);
	    body (pw, prop.getProperty("Razonamiento")+ "<i> " + term + "</i> "+ prop.getProperty("insertedinontology")+ "<i> " + ontology + "</i> "+ prop.getProperty("correctly."));
	    //body (pw, prop.getProperty("Ontologya")+ "<i>" + oname + "</i>"+ prop.getProperty("createdcorrectly."));
	    sendAdditionEvent (pw, TermTypes.RULE, term);
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}






