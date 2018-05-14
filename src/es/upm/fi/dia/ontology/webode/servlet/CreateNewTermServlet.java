package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.webode.OntoClean.*;


/**
 * @author Emilio Raya and Oscar Corcho
 * @version 1.0
 */
public class CreateNewTermServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
    	Properties prop = (Properties)session.getAttribute("prop");
	String ontology     = (String) session.getAttribute (CURRENT_ONTOLOGY);
	String term         = req.getParameter (TERM_NAME);
	int    type         = Integer.parseInt (req.getParameter (TYPE));
	String desc         = req.getParameter (TERM_DESCRIPTION);
	String backURL      = req.getParameter (BACK_URL);
	String s_anti_unity         = req.getParameter (CAU);
	String s_is_dependent       = req.getParameter (ID);
	String s_carries_unity      = req.getParameter (CU);
	String s_anti_rigid         = req.getParameter (AR);
	String s_supplies_identity  = req.getParameter (SIC);
	String s_carries_identity   = req.getParameter (CIC);
	String s_is_rigid           = req.getParameter (IR);
	int is_dependent = 0;
	int carries_unity = 0;
	int anti_rigid = 0;
    int carries_identity = 0;
	int supplies_identity = 0;
	int anti_unity = 0;
	int rigid = 0;


	if (ontology == null || ontology.trim().equals("") ||
	    term == null || term.trim().equals("")) {
	    error (res, prop.getProperty("Theontologyandtermnamesaremandatoryparameters."));
	    return;
	}

	try {
	    // all right.  Let's insert this data into the database
           if(s_anti_unity != null) anti_unity = (s_anti_unity.equals("yes"))? 1 : -1;
           //{
           //  if(s_anti_unity.equals("yes")) anti_unity = 1;
           //  else anti_unity = -1;
           //}

           if(s_is_dependent != null) is_dependent = (s_is_dependent.equals("yes"))? 1 : -1;
           //{
           //  if(s_is_dependent.equals("yes")) is_dependent = 1;
           //  else  is_dependent = -1;
           //}
           if(s_carries_unity != null) carries_unity = (s_carries_unity.equals("yes"))? 1 : -1;
           //{
           //  if(s_carries_unity.equals("yes")) carries_unity = 1;
           //  else  carries_unity = -1;
           //}
           if(s_anti_rigid != null) anti_rigid = (s_anti_rigid.equals("yes"))? 1 : -1;
           //{
           //  if(s_anti_rigid.equals("yes")) anti_rigid = 1;
           //  else   anti_rigid = -1;
           //}
           if(s_carries_identity != null) carries_identity = (s_carries_identity.equals("yes")) ? 1 : -1;
           //{
           //  if(s_carries_identity.equals("yes")) carries_identity = 1;
           //  else   carries_identity = -1;
           //}
           if(s_supplies_identity != null) supplies_identity = (s_supplies_identity.equals("yes")) ? 1 : -1;
           //{
           //  if(s_supplies_identity.equals("yes")) supplies_identity = 1;
           //  else  supplies_identity = -1;
           //}
           if(s_is_rigid != null) rigid = (s_is_rigid.equals("yes")) ? 1 : -1;
           //{
           //  if(s_is_rigid.equals("yes")) rigid = 1;
           //  else  rigid = -1;
           //}

		//MODIFICADO POR OSCAR CORCHO (25Oct2002)
        //OntoCleanServiceImp oc = new OntoCleanServiceImp();
        //oc.insertTerm(ontology, term, desc, type,
        //              anti_unity,is_dependent,carries_unity,anti_rigid,carries_identity,
        //              supplies_identity,rigid);
        OntoCleanService oc = (OntoCleanService) session.getAttribute (ONTOCLEAN_SERVICE);
        if (oc!=null)
        	oc.insertTerm(ontology, term, desc, type, anti_unity,is_dependent,carries_unity,
        				anti_rigid,carries_identity,supplies_identity,rigid);
        else {
        	ODEService ode = (ODEService) session.getAttribute (ODE_SERVICE);
			ode.insertTerm(ontology, term, desc, type);
		}
		//FIN MODIFICADO

	    /*
	    PrintWriter pw = res.getWriter ();
	    header(pw);

	  if (backURL != null) {
	     header(pw);
		setURL (backURL);
		setRedirectParam ("");}
		else {
		 // headerauxTerm(pw);
		 // NUEVO PARA MULTILINGUALIDAD DANI
       // pw.println("<html>");
       // pw.println("  <head>");
       // pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=../jsp/webode/about.jsp\"> ");
       // pw.println("  </head>");
        // FIN NUEVO


	   }
	    */

	    PrintWriter pw = res.getWriter ();
	    header(pw);
	    if (backURL != null) {
		setURL (backURL);
		setRedirectParam ("");
	    }
	    body (pw, prop.getProperty("Termi")+ "<i> " + term + "</i> "+ prop.getProperty("insertedinontology")+ "<i> " + ontology + "</i> " + prop.getProperty("correctly."));
	    sendAdditionEvent (pw, TermTypes.CONCEPT, term);
	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}






