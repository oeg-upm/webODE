package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Servlet responsible for inserting a new class attribute.
 *
 * @author  Julio César Arpírez Vega
 * @author  David Manzano-Macho
 * @version 0.3
 */
public class InsertClassAttributeServlet extends BaseServlet
{
  public void doPost (HttpServletRequest req,
                      HttpServletResponse res,
                      HttpSession session)
      throws ServletException, IOException
  {
    // Get parameters
    String attributeName = req.getParameter (ATTRIBUTE_NAME);
    String termName      = req.getParameter (ORIGINAL_TERM_NAME);
//	String attributeNameaux = req.getParameter (ATTRIBUTE_NAME);
//	String termNameaux      = req.getParameter (ORIGINAL_TERM_NAME);
//	String attributeDescription = req.getParameter (ATTRIBUTE_DESCRIPTION);
    Properties prop = (Properties)session.getAttribute("prop");

    String valueTypeName = req.getParameter (ATTRIBUTE_TYPE);
    if(valueTypeName!=null && (valueTypeName.equals(ValueTypes.NAMES[ValueTypes.XML_SCHEMA_DATATYPE-1]) ||
                               valueTypeName.equals(ValueTypes.NAMES[ValueTypes.ENUMERATED-1])))
      valueTypeName = req.getParameter (ATTRIBUTE_SUBTYPE);
    if(valueTypeName==null)
      error(res, prop.getProperty("Therewasanerrorwhenretrievingtheattributetype:"));

    //String referencedOntology = null;
    //if (valueType == ValueTypes.ONTOLOGY)
    //    referencedOntology = req.getParameter (ATTRIBUTE_ONTOLOGY);

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
      ODEService odeService=((ODEService) session.getAttribute (ODE_SERVICE));
      odeService.insertClassAttribute(oname,
                                      new ClassAttributeDescriptor (attributeName,
                                                                    termName,
                                                                    valueTypeName,
                                                                    measurementUnit,
                                                                    precision,
                                                                    minCardinality,
                                                                    maxCardinality,
                                                                    description));


      PrintWriter pw = res.getWriter ();
      header(pw);
      setRedirectParam (termName);


      // body (pw, prop.getProperty("Ontologya")+ "<i>" + oname + "</i>"+ prop.getProperty("createdcorrectly."));
      body (pw, prop.getProperty("Classattribute")+ "<i> " + attributeName + "</i> " + prop.getProperty("forterm")+ "<i> " +
            termName + "</i> " + prop.getProperty("insertedsucessfullyinontology")+ "<i> " + oname + "</i>.");
      trailer (pw);
    } catch (Exception e) {
      error (res, e.getMessage(), e);
    }
  }
}