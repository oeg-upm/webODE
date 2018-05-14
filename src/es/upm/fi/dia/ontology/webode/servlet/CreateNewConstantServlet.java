package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 *
 * @version 0.5
 */
public class CreateNewConstantServlet extends BaseServlet
{
  public void doPost (HttpServletRequest req,
                      HttpServletResponse res,
                      HttpSession session)
      throws ServletException, IOException
  {
    String ontology     = (String) session.getAttribute (CURRENT_ONTOLOGY);
    String term         = req.getParameter (TERM_NAME);
    String desc         = req.getParameter (TERM_DESCRIPTION);
    String unit         = req.getParameter (ATTRIBUTE_MEASUREMENT_UNIT);
    String value        = req.getParameter (ATTRIBUTE_VALUE);
    Properties prop = (Properties)session.getAttribute("prop");

    String valueTypeName = req.getParameter (ATTRIBUTE_TYPE);
    if(valueTypeName!=null && (valueTypeName.equals(ValueTypes.NAMES[ValueTypes.XML_SCHEMA_DATATYPE-1]) ||
                               valueTypeName.equals(ValueTypes.NAMES[ValueTypes.ENUMERATED-1])))
      valueTypeName = req.getParameter (ATTRIBUTE_SUBTYPE);
    if(valueTypeName==null)
      error(res, prop.getProperty("Therewasanerrorwhenretrievingtheattributetype:"));

    if (ontology == null || ontology.trim().equals("") ||
        term == null || term.trim().equals("")) {
      error (res,prop.getProperty("Theontologyandtermnamesaremandatoryparameters."));
      return;
    }

    try {

      // all right.  Let's insert this data into the database
      ODEService odeService=(ODEService) session.getAttribute (ODE_SERVICE);
      odeService.insertConstant(ontology, new ConstantDescriptor (term, desc, valueTypeName,unit, value));

      PrintWriter pw = res.getWriter ();

      // NUEVO PARA MULTILINGUALIDAD DANI
/*	    pw.println("<html>");
        pw.println("  <head>");
        pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=/webode/jsp/webode/frames3.html\"> ");

        pw.println("  </head>");*/
      // FIN NUEVO */
      // headerauxConstant(pw);
      // res.sendRedirect("/webode/jsp/webode/about.jsp");
      header(pw);
      body (pw, prop.getProperty("Constante") + "<i> " + term + "</i> "+ prop.getProperty("insertedinontology")+ "<i> " + ontology + "</i> " + prop.getProperty("correctly."));

      sendAdditionEvent (pw, TermTypes.CONSTANT, term);

      trailer (pw);
    } catch (Exception e) {
      error (res, e.getMessage(), e);
    }
  }
}
