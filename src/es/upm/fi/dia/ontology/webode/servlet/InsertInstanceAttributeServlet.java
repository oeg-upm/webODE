package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Servlet responsible for inserting a new instance attribute.
 *
 * @author  Julio César Arpírez Vega
 * @author  David Manzano
 * @author  Oscar Corcho
 * @version 0.2
 */
public class InsertInstanceAttributeServlet extends BaseServlet
{
  public void doPost (HttpServletRequest req,
                      HttpServletResponse res,
                      HttpSession session)
      throws ServletException, IOException
  {
    // Get parameters
    String attributeName = req.getParameter (ATTRIBUTE_NAME);
    String termName      = req.getParameter (ORIGINAL_TERM_NAME);
    String attributeNameaux = req.getParameter (ATTRIBUTE_NAME);
    String termNameaux      = req.getParameter (ORIGINAL_TERM_NAME);
//	String attributeDescription = req.getParameter (ATTRIBUTE_DESCRIPTION);
    Properties prop = (Properties)session.getAttribute("prop");

    String valueTypeName = req.getParameter (ATTRIBUTE_TYPE);
    if(valueTypeName!=null && (valueTypeName.equals(ValueTypes.NAMES[ValueTypes.XML_SCHEMA_DATATYPE-1]) ||
                               valueTypeName.equals(ValueTypes.NAMES[ValueTypes.ENUMERATED-1])))
      valueTypeName = req.getParameter (ATTRIBUTE_SUBTYPE);
    if(valueTypeName==null)
      error(res, prop.getProperty("Therewasanerrorwhenretrievingtheattributetype:"));

    int minCardinality = -1;
    try {
      minCardinality = Integer.parseInt (req.getParameter (MIN_CARDINALITY));
    } catch (Exception e) {
    }
    int maxCardinality = -1;
    try {
      maxCardinality = Integer.parseInt (req.getParameter (MAX_CARDINALITY));
    } catch (Exception e) {
    }
    String measurementUnit = req.getParameter (ATTRIBUTE_MEASUREMENT_UNIT);
    String precision = req.getParameter (PRECISION);
    String description = req.getParameter (ATTRIBUTE_DESCRIPTION);

    String maxValue    = req.getParameter (MAX_VALUE);
    String minValue    = req.getParameter (MIN_VALUE);

    // These things are in the session.
//	String oauthor      = (String) session.getAttribute (USER);
//	String ogroup       = (String) session.getAttribute (GROUP);
    String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);

    if (oname == null || oname.trim().equals("")) {
      error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
      return;
    }
    if (termName == null || termName.trim().equals("") ||
        attributeName == null || attributeName.trim().equals("")) {
      error (res, prop.getProperty("Thetermandattributenamesaremandatoryparameters."));
      return;
    }

    try {
      // all right.  Let's insert this data into the database
      ((ODEService) session.getAttribute (ODE_SERVICE)).insertInstanceAttribute
      (oname,
      new InstanceAttributeDescriptor (attributeName,
      termName,
      valueTypeName,
      measurementUnit,
      precision,
      minCardinality,
      maxCardinality,
      description,
      minValue,
      maxValue));

      System.out.println("It was a XML Schema Datatype and inserted data into the database: " + valueTypeName);

      PrintWriter pw = res.getWriter ();
      header(pw);
      setRedirectParam (termName);
      String onameaux;
      onameaux=oname;

      onameaux = onameaux + " ";
      attributeNameaux = attributeNameaux + " ";
      termNameaux = termNameaux + " ";
      body (pw, prop.getProperty("Instanceattribute") + " <i>" + attributeNameaux + "</i>"+prop.getProperty("forconcept")+ "<i>" +
            termNameaux + "</i>"+prop.getProperty("insertedsuccessfullyinontology")+ "<i> " + onameaux + "</i>.");
      trailer (pw);
    } catch (Exception e) {
      error (res, e.getMessage(), e);
    }
  }
}