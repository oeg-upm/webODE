package es.upm.fi.dia.ontology.webode.servlet;

// Java and XML stuff
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.translat.OIL.*;
import es.upm.fi.dia.ontology.webode.xml.*;

/**
 * Servlet responsible for exporting ontologies in XML according
 * to WebODE's DTD.
 *
 * @author  Oscar Corcho García (based on Julio César Arpírez Vega work)
 * @version 0.1
 */
public class OILExportServlet extends BaseServlet
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

//ASI HASTA QUE JULIO ARREGLE LO DEL SERVLET DE LOGIN:
//	    OILExportService exportService = (OILExportService) session.getAttribute ("OIL export service");
//   	    MinervaSession ms = MinervaClient.getMinervaSession (new MinervaURL ("minerva://localhost"), (String)session.getAttribute(USER), req.getParameter(PASSWORD));
//Recupero primero la sesión de Minerva que se abrió inicialmente
//	    MinervaSession ms = (MinervaSession) session.getAttribute(MINERVA_SESSION);
//          OILExportService exportService = (OILExportService) ms.getService ("OIL");
          OILExportService exportService = (OILExportService) session.getAttribute ("oil");


	    // Export ontology and requested instance sets.
	    StringBuffer doc = exportService.ExportOntologyOIL (ontology, bConceptualization, instanceSets);

	    // Set appropiate content type.
	    res.setContentType (XMLConstants.MIME_TYPE); //OIL va en XML

	    PrintWriter pw = res.getWriter ();

//Esta es de prueba
//	    pw.println ("<html><head><title>Ontology in OIL</title></head><body>");

	    pw.println (doc.toString());

//Esta es de prueba
//	    pw.println ("</body></html>");


	    pw.flush();
//	} catch (AuthenticationException e) {
//		error (res, "User has no access to OIL Service");

	} catch (Exception e) {
	    error (res, prop.getProperty("Errorexportingontology:") + e, e);
	}
    }

}


