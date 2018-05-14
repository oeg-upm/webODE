package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Servlet responsible for updating a class attribute.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.3
 */
public class UpdateClassAttributeServlet extends BaseServlet
{
  public void doPost (HttpServletRequest req,
                      HttpServletResponse res,
                      HttpSession session)
      throws ServletException, IOException
  {
    // Get parameters
    String attributeName        = req.getParameter (ATTRIBUTE_NAME);
    String originalAttrName     = req.getParameter (ORIGINAL_ATTRIBUTE_NAME);
    String attributeNameaux        = req.getParameter (ATTRIBUTE_NAME);
    String parent               = req.getParameter (PARENT_NAME);
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

    // These things are in the session.
//	String oauthor      = (String) session.getAttribute (USER);
//	String ogroup       = (String) session.getAttribute (GROUP);
    String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);

    if (oname == null || oname.trim().equals("")) {
      error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
      return;
    }
    if (originalAttrName == null || originalAttrName.trim().equals("") ||
        attributeName == null || attributeName.trim().equals("") ||
        parent == null || parent.trim().equals("")) {
      error (res, prop.getProperty("Thetermandattributenamesaremandatoryparameters."));
      return;
    }

    try {
      // all right.  Let's insert this data into the database
      ODEService odeService=(ODEService) session.getAttribute (ODE_SERVICE);
      odeService.updateClassAttribute(oname, originalAttrName, parent,
                                      new ClassAttributeDescriptor (attributeName,
                                                                    null,  // It's not considered
                                                                    valueTypeName,
                                                                    measurementUnit,
                                                                    precision,
                                                                    minCardinality,
                                                                    maxCardinality,
                                                                    description));


      PrintWriter pw = res.getWriter ();
      header(pw);
      setRedirectParam (parent);
      String onameaux;
      onameaux=oname;
      onameaux = onameaux + " ";
      attributeNameaux = attributeNameaux + " ";
      body (pw, prop.getProperty("Claseattributo")+ " <i>" + attributeNameaux +
            "</i>" + prop.getProperty("updatedsucessfullyinontology")+ "<i>" + onameaux + "</i>.");
      trailer (pw);
    } catch (Exception e) {
      error (res, e.getMessage(), e);
    }
  }
}