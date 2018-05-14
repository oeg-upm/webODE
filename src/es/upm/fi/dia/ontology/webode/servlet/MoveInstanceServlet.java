package es.upm.fi.dia.ontology.webode.servlet;

import es.upm.fi.dia.ontology.webode.service.*;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.Properties;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class MoveInstanceServlet extends BaseServlet {
  public void doPost (HttpServletRequest req,
                      HttpServletResponse res,
                      HttpSession session)
      throws ServletException, IOException {
    // Get parameters
    String instance        = req.getParameter (INSTANCE_NAME);
    String instanceSet     = (String)session.getAttribute(ACTIVE_INSTANCE_SET);
    String targetConcept   = req.getParameter (TERM_NAME);

    // These things are in the session.
    String oname        = req.getParameter(ONTOLOGY_NAME);
    if(oname==null || oname.length()==0)
      oname = (String) session.getAttribute (CURRENT_ONTOLOGY);

    Properties prop = (Properties)session.getAttribute("prop");

    if (oname == null || oname.trim().equals("")) {
      error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
      return;
    }

    try {
      // all right.  Let's insert this data into the database

      ((ODEService) session.getAttribute (ODE_SERVICE)).moveInstance(oname, instanceSet, instance, targetConcept);

      PrintWriter pw = res.getWriter ();
      header(pw);
      setRedirectParam (instance);

      body (pw, "The instance has been moved succesfully");
      trailer (pw);
    } catch (Exception e) {
      error (res, e.getMessage(), e);
    }
  }
}