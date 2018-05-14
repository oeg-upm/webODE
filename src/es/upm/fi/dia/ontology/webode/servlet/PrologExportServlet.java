package es.upm.fi.dia.ontology.webode.servlet;

// Java and XML stuff
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.translat.Prolog.*;

/**
 * Servlet responsible for exporting ontologies in Prolog
 *
 * @author  Oscar Corcho García (based on Julio César Arpírez Vega work)
 * @version 0.1
 */
public class PrologExportServlet extends BaseServlet
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
	//System.out.println("Empiezo el servlet");
//	    MinervaSession ms = (MinervaSession) session.getAttribute(MINERVA_SESSION);
//          PrologExportService exportService = (PrologExportService) ms.getService ("prolog");
	//System.out.println("Servicio capturado");
          PrologExportService exportService = (PrologExportService) session.getAttribute ("prolog");

	    // Export ontology and requested instance sets.
	    StringBuffer doc = exportService.ExportOntologyProlog (ontology, bConceptualization, instanceSets);

	//System.out.println("Exportado");
	    // Set appropiate content type.
	    res.setContentType ("text/plain");

	    PrintWriter pw = res.getWriter ();

	    pw.println (doc.toString());


	    pw.flush();
//	} catch (AuthenticationException e) {
//		error (res, "User has no access to Prolog Service");

	} catch (Exception e) {
	    error (res, prop.getProperty("ErrorexportingontologytoProlog") + e, e);
	}
    }

}


