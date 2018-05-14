package es.upm.fi.dia.ontology.webode.servlet;

// Java and XML stuff
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

import es.upm.fi.dia.ontology.webode.translat.XCARIN.*;
import es.upm.fi.dia.ontology.webode.xml.*;

/**
 * Servlet responsible for exporting ontologies in XCARIN
 *
 * @author  Oscar Corcho García (based on Julio César Arpírez Vega work)
 * @version 0.1
 */
public class XCARINExportServlet extends BaseServlet
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
	Properties prop = (Properties)session.getAttribute("prop");
	try {

//Recupero primero la sesión de Minerva que se abrió inicialmente
//	    MinervaSession ms = (MinervaSession) session.getAttribute(MINERVA_SESSION);
//          XCARINExportService exportService = (XCARINExportService) ms.getService ("XCARIN");
          XCARINExportService exportService = (XCARINExportService) session.getAttribute ("xcarin");


	    // Export ontology and requested instance sets.
	    StringBuffer doc = exportService.ExportOntologyXCARIN (ontology, bConceptualization, instanceSets);

	    // Set appropiate content type.
	    res.setContentType (XMLConstants.MIME_TYPE); //XCARIN va en XML

	    PrintWriter pw = res.getWriter ();

	    pw.println (doc.toString());

	    pw.flush();
//	} catch (AuthenticationException e) {
//		error (res, "User has no access to XCARIN Service");

	} catch (Exception e) {
	     error (res, prop.getProperty("Errorexportingontology:") + e, e);
	}
    }

}


