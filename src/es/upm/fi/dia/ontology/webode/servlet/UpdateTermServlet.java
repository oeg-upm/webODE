package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.webode.OntoClean.*;

public class UpdateTermServlet extends BaseServlet
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
	String parent       = req.getParameter (PARENT_NAME);
        int term_type = Integer.parseInt(req.getParameter(TERM_TYPE));
	String tnamef       = req.getParameter ("term_name_f");
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

	// These things are in the session.
//	String oauthor      = (String) session.getAttribute (USER);
//	String ogroup       = (String) session.getAttribute (GROUP);
	String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);
	Properties prop = (Properties)session.getAttribute("prop");

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
        if(s_anti_unity != null) anti_unity = (s_anti_unity.equals("yes"))? 1 : -1;
        if(s_is_dependent != null) is_dependent = (s_is_dependent.equals("yes"))? 1 : -1;
        if(s_carries_unity != null) carries_unity = (s_carries_unity.equals("yes"))? 1 : -1;
        if(s_anti_rigid != null) anti_rigid = (s_anti_rigid.equals("yes"))? 1 : -1;
        if(s_carries_identity != null) carries_identity = (s_carries_identity.equals("yes")) ? 1 : -1;
        if(s_supplies_identity != null) supplies_identity = (s_supplies_identity.equals("yes")) ? 1 : -1;
        if(s_is_rigid != null) rigid = (s_is_rigid.equals("yes")) ? 1 : -1;

/* ELIMINADO OSCAR 25Oct2002
	    OntoCleanServiceImp oc = new OntoCleanServiceImp();
	    if(s_anti_unity != null)
            {
              if(s_anti_unity.equals("yes"))
                anti_unity = 1;
              else
                anti_unity = -1;
            }
            if(s_is_dependent != null)
            {
              if(s_is_dependent.equals("yes"))
                is_dependent = 1;
              else
                is_dependent = -1;
            }
            if(s_carries_unity != null)
            {
              if(s_carries_unity.equals("yes"))
                carries_unity = 1;
              else
                carries_unity = -1;
            }
            if(s_anti_rigid != null)
            {
              if(s_anti_rigid.equals("yes"))
                anti_rigid = 1;
              else
                anti_rigid = -1;
            }
            if(s_carries_identity != null)
            {
              if(s_carries_identity.equals("yes"))
                carries_identity = 1;
              else
                carries_identity = -1;
            }
            if(s_supplies_identity != null)
            {
              if(s_supplies_identity.equals("yes"))
                 supplies_identity = 1;
              else
                supplies_identity = -1;
            }
            if(s_is_rigid != null)
            {
              if(s_is_rigid.equals("yes"))
                 rigid = 1;
              else
                 rigid = -1;
            }
            oc.updateTerm((ODEService) session.getAttribute (ODE_SERVICE), originalName, parent,
                                      new Term (oname, tname, tdescription, -1),anti_unity,
                                      is_dependent,carries_unity,anti_rigid,carries_identity,
                                      supplies_identity,rigid);
FIN ELIMINADO POR OSCAR */

        OntoCleanService oc = (OntoCleanService) session.getAttribute (ONTOCLEAN_SERVICE);
        if (oc!=null) //El usuario tiene acceso a OntoClean
        	oc.updateTerm(originalName, parent, new Term (oname, tname, tdescription, term_type, -1),
        				  	anti_unity, is_dependent,carries_unity,anti_rigid,carries_identity,
							supplies_identity,rigid);
        else { //El usuario no tiene acceso a OntoClean
        	ODEService ode = (ODEService) session.getAttribute (ODE_SERVICE);
		    if (parent == null) ode.updateTerm (originalName, new Term (oname, tname, tdescription, term_type, -1));
	    	else ode.updateTerm (originalName, parent, new Term (oname, tname, tdescription, term_type, -1));
		}
		//FIN MODIFICADO OSCAR

	    PrintWriter pw = res.getWriter ();
	    header(pw);
		sendUpdateEvent (pw, TermTypes.CONCEPT, originalName, tname);

	    if (oc==null) {
	    	//body (pw, "Term <i>" + (tnamef == null ? tname : tnamef) +
	   	//			"</i> in ontology <i>" + oname + "</i> updated successfully.");
	   	oname = oname + " ";
	   	if(tnamef != null)
	   	   tnamef = tnamef + " ";
	   	else
	   	   tname = tname + " ";
                body (pw, prop.getProperty("Termi")+ "<i>" + (tnamef == null ? tname : tnamef) +
			"</i>"+ prop.getProperty("inontology")+ "<i>" + oname + "</i>" + prop.getProperty("updatedsuccessfully"));
	    }
	    else {
	    	oname = oname + " ";
	    	String evs = oc.getEvaluationMode();
	    	if(!evs.equals("Synchronous"))
	      		body (pw, prop.getProperty("Termi")+ "<i>" + (tnamef == null ? tname : tnamef) +
		   				"</i>"+ prop.getProperty("inontology")+ "<i>" + oname + "</i>" + prop.getProperty("updatedsuccessfully"));
            else{
              ErrorOntoClean[] errors = oc.evaluationOntoClean(oname,(String) session.getAttribute (USER)," ");
              StringWriter doc = new StringWriter();
              doc.write(" <center> <h1 class=\"title\">" + evs + " Evaluation OntoClean for "+oname+" <br> " +errors.length +" ERRORS FOUND: </h1> <hr>");
              if(errors.length == 0) doc.write("<b>No Errors <br>");
              else {
              	for(int g=0;g<errors.length;g++)
              		doc.write(errors[g].convertStringAux());
              }
              doc.write("</center>");
             String onameaux;
              tnameaux = tnameaux + " ";
              onameaux = oname;
              onameaux=onameaux + " ";
              body (pw, prop.getProperty("Termi")+ "<i>" + (tnamef == null ? tname : tnamef) +
				   "</i>"+ prop.getProperty("inontology")+ "<i>" + oname + "</i>" + prop.getProperty("updatedsuccessfully.")+ "<hr>");
              body (pw, doc.getBuffer().substring(0,doc.getBuffer().length()));
	    	}
	    }

	    trailer (pw);
	} catch (Exception e) {
	    error (res, e.getMessage(), e);
	}
    }
}
