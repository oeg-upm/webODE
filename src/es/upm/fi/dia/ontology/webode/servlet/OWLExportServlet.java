package es.upm.fi.dia.ontology.webode.servlet;

// Java and XML stuff
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.translat.OWL.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;

import es.upm.fi.dia.ontology.minerva.server.*;


/**
 * Servlet responsible for exporting ontologies to OWL
 *
 * @author  Oscar Corcho
 * @version 0.1
 */
public class OWLExportServlet extends BaseServlet
{
  public void doPost (HttpServletRequest req,
                      HttpServletResponse res,
                      HttpSession session)
      throws ServletException, IOException
  {
    String ontology            = req.getParameter (ONTOLOGY_NAME);
    boolean bConceptualization = req.getParameter (CONCEPTUALIZATION) != null &&
                                 req.getParameter (CONCEPTUALIZATION).equals (TRUE);
    String[] instanceSets      = req.getParameterValues (INSTANCE_SETS);
    if (instanceSets!=null && instanceSets.length==1 && instanceSets[0].equals(""))
      instanceSets=null;
    String namespace           = req.getParameter ("namespace");
    Properties prop = (Properties)session.getAttribute("prop");
    try {

      //If the OWL export service is not stored in the HTTP session
      MinervaSession ms = (MinervaSession) session.getAttribute(MINERVA_SESSION);
      OWLExportService exportService = (OWLExportService) ms.getService (OWL_SERVICE);
      //If it is stored there
      //OWLExportService exportService = (OWLExportService) session.getAttribute("owl");

      // Export ontology and requested instance sets.
      String user = (String) session.getAttribute("user");
      byte[] zipFile = exportService.ExportOntologyOWL (ontology, user, bConceptualization, instanceSets, namespace);

      // Set appropiate content type.
      res.setContentType("application/download");
      res.setContentLength(zipFile.length);
      res.setHeader("Content-Disposition", "inline; filename=\"" + ontology +".zip" + "\"");
      java.io.OutputStream out=res.getOutputStream();
      out.write(zipFile);
    } catch (AuthenticationException e) {
      e.printStackTrace(System.out);
      error(res, prop.getProperty("UserhasnoaccesstoOWLService"), e);
    } catch (Exception e) {
      e.printStackTrace(System.out);
      error (res, prop.getProperty("Errorexportingontology:") + e, e);
    }
  }

}

