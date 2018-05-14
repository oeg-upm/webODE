package es.upm.fi.dia.ontology.webode.servlet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

public class DeleteEnumeratedTypeServlet extends BaseServlet {

  protected void doPost(HttpServletRequest  req,
                        HttpServletResponse res,
                        HttpSession session) throws IOException,
                                                    ServletException {
    // These things are in the session.
    String oname=(String)session.getAttribute(CURRENT_ONTOLOGY);
    Properties prop = (Properties)session.getAttribute("prop");

    // Get parameters
    String enumeratedTypeName=req.getParameter(ORIGINAL_ENUMERATED_TYPE_NAME);
//    System.out.println("=- DELETE SERVLET =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//    System.out.println("Name.......: "+enumeratedTypeName);
//    System.out.println("Ontology...: "+oname);

    try {
      // all right.  Let's insert this data into the database
      ODEService odeService=(ODEService)session.getAttribute(ODE_SERVICE);
      odeService.removeEnumeratedType(oname,enumeratedTypeName);

      PrintWriter pw = res.getWriter ();

      // NUEVO PARA MULTILINGUALIDAD DANI
      pw.println("<html>");
      pw.println("  <head>");
      pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=../jsp/webode/about.jsp\"> ");
      pw.println("  </head>");
      // FIN NUEVO

      header(pw);
      body (pw, prop.getProperty("EnumeratedType")+ "<i> " + enumeratedTypeName + " </i> "+ prop.getProperty("removedfromtheontology")+"<i> " + oname + " </i> "+prop.getProperty("successfully."));

      sendAdditionEvent(pw, -1, enumeratedTypeName);
      trailer(pw);
    } catch(Exception e) {
      error(res, e.getMessage(), e);
    }
  }
}