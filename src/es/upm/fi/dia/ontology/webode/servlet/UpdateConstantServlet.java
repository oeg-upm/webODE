package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Updates a constant.
 *
 * @version 0.1
 */
public class UpdateConstantServlet extends BaseServlet
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
    String originalName = req.getParameter (ORIGINAL_TERM_NAME);
    Properties prop = (Properties)session.getAttribute("prop");

    String valueTypeName = req.getParameter (ATTRIBUTE_TYPE);
    if(valueTypeName!=null && (valueTypeName.equals(ValueTypes.NAMES[ValueTypes.XML_SCHEMA_DATATYPE-1]) ||
                               valueTypeName.equals(ValueTypes.NAMES[ValueTypes.ENUMERATED-1])))
      valueTypeName = req.getParameter (ATTRIBUTE_SUBTYPE);
    if(valueTypeName==null)
      error(res, prop.getProperty("Therewasanerrorwhenretrievingtheattributetype:"));

    if (ontology == null || ontology.trim().equals("") ||
        term == null || term.trim().equals("")) {
      error (res, prop.getProperty("Theontologyandtermnamesaremandatoryparameters."));
      return;
    }

    try {
      // all right.  Let's insert this data into the database
      ODEService odeService=(ODEService) session.getAttribute (ODE_SERVICE);
      odeService.updateConstant(ontology, originalName, new ConstantDescriptor (term, desc, valueTypeName, unit, value));

      PrintWriter pw = res.getWriter ();
      header(pw);

      term = term + " ";
      ontology = ontology + " ";
      body (pw, prop.getProperty("Constante")+ "<i>" + term + "</i>" + prop.getProperty("updatedsuccessfullyinontology")+ "<i> " + ontology + "</i>.");
      sendUpdateEvent (pw, TermTypes.CONSTANT, originalName, term);
      trailer (pw);
    } catch (Exception e) {
      error (res, e.getMessage(), e);
    }
  }
}
