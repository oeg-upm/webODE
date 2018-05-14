package es.upm.fi.dia.ontology.webode.servlet;



import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.webode.OntoClean.*;

public class CreateNewOntologyServlet extends BaseServlet
{
  public void doPost (HttpServletRequest req,
                      HttpServletResponse res,
                      HttpSession session)
      throws ServletException, IOException
  {
    // Get parameters
    String oname        = req.getParameter (ONTOLOGY_NAME);
//    String  aux          = req.getParameter (ONTOLOGY_NAME);
    String odescription = req.getParameter (ONTOLOGY_DESCRIPTION);

    //namespace--> add --------------------------------
    String onamespace = req.getParameter (NAMESPACE);
    //--------------------------------------------------
    boolean bAllow = req.getParameter (ALLOW_GROUP) != null &&
                     req.getParameter (ALLOW_GROUP).equals ("yes");

    // These things are in the session.
    //session = request.getSession(false);
    String oauthor      = (String) session.getAttribute (USER);
    String ogroup       = req.getParameter (GROUP);

    Properties prop = (Properties)session.getAttribute("prop");

    if (oname == null || oname.trim().equals("")) {




      error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
      return;
    }





    try {

      // all right.  Let's insert this data into the database
      ODEService ode = (ODEService) session.getAttribute (ODE_SERVICE);
      OntologyDescriptor od = new OntologyDescriptor (oname, onamespace, odescription, oauthor, ogroup, null, null, bAllow);

      //MODIFICADO POR OSCAR CORCHO (25Oct2002)
      //OntoCleanServiceImp oc = new OntoCleanServiceImp();
      //oc.createOntology((ODEService) session.getAttribute (ODE_SERVICE),od);
      OntoCleanService oc = (OntoCleanService) session.getAttribute (ONTOCLEAN_SERVICE);
      if (oc!=null) oc.createOntology(od);
      else {
        if (ode==null) {error(res,prop.getProperty("User.") + oauthor + prop.getProperty("hasnoaccesstotheODEService.")); return;}
        else ode.createOntology(od);
      }
      //FIN MODIFICADO

      PrintWriter pw = res.getWriter ();

        /*NUEVO PARA MULTILINGUALIDAD DANI
        pw.println("<html>");
        pw.println("  <head>");
        pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=../jsp/webode/about.jsp\"> ");
        pw.println("  </head>");
      // FIN NUEVO */

      headeraux(pw);

      // incluido para multilingualidad problema de recargar un servlet
      //setRedirectParam (4, "../jsp/webode/about.jsp");
      // fin añadido
      body (pw, prop.getProperty("Ontologya")+ "<i> " + oname + " </i>"+ prop.getProperty("createdcorrectly."));

      trailer (pw);
    } catch (Exception e) {
      error (res, e.getMessage(), e);
    }
  }
}