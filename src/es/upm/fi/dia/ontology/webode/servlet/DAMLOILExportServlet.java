package es.upm.fi.dia.ontology.webode.servlet;

// Java and XML stuff
import java.io.*;
import com.sun.xml.tree.*;
import org.w3c.dom.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.webode.translat.DAMLOIL.*;
import es.upm.fi.dia.ontology.webode.xml.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;

import es.upm.fi.dia.ontology.minerva.server.*;


/**
 * Servlet responsible for exporting ontologies to DAML+OIL
 *
 * @author  Oscar Corcho (based on Julio César Arpírez work)
 * @version 0.1
 */
public class DAMLOILExportServlet extends BaseServlet
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

		//If the DAML+OIL export service is not stored in the HTTP session
		MinervaSession ms = (MinervaSession) session.getAttribute(MINERVA_SESSION);
		DAMLOILExportService exportService = (DAMLOILExportService) ms.getService (DAMLOIL_SERVICE);
		//If it is stored there
	    //DAMLOILExportService exportService = (DAMLOILExportService) session.getAttribute("damloil");

	    // Export ontology and requested instance sets.
        String user = (String) session.getAttribute("user");
	    byte[] zipFile = exportService.ExportOntologyDAMLOIL (ontology, user, bConceptualization, instanceSets, namespace);

	    // Set appropiate content type.
	    res.setContentType("application/download");
	    res.setContentLength(zipFile.length);
		res.setHeader("Content-Disposition", "inline; filename=\"" + ontology +".zip" + "\"");
	    java.io.OutputStream out=res.getOutputStream();
		out.write(zipFile);
	} catch (AuthenticationException e) {
		//Error(res, prop.getProperty("UserhasnoaccesstoDAML+OILService."));
		 error (res, prop.getProperty("UserhasnoaccesstoDAML+OILService."), e);
	} catch (Exception e) {
	   error (res, prop.getProperty("Errorexportingontology:") + e, e);
	}
    }

}


