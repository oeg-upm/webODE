package es.upm.fi.dia.ontology.webode.servlet;

// Java and XML stuff
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.xml.*;
import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * Servlet responsible for exporting ontologies in XML according
 * to WebODE's DTD.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class XMLExportServlet extends BaseServlet
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
	String[] views             = req.getParameterValues (VIEWS);
	String dtd		   = getInitParameter("dtd_file");
    Properties prop = (Properties)session.getAttribute("prop");
	try {
	//Añadido por Oscar Corcho: 26/02/2002
	    MinervaSession minerva = (MinervaSession) session.getAttribute (MINERVA_SESSION);
	    ExportService exportService = (ExportService) minerva.getService (EXPORT_SERVICE);
	    //ExportService exportService = (ExportService) session.getAttribute (EXPORT_SERVICE);
	//Fin añadido
        //Añadido por Angel Lopez Cima: 10/07/2003
            String server=req.getServerName();
            String scheme=req.getScheme();
            int port=req.getServerPort();
        //Fin añadido
	    if (exportService == null) return;
	    // Export ontology and requested instance sets.
	    StringBuffer doc = exportService.export (ontology, bConceptualization, instanceSets, views, scheme + "://" + server + ((port!=80)?":"+port:"") + dtd);

	    // Set appropiate content type.
	    res.setContentType (XMLConstants.MIME_TYPE);

	    PrintWriter pw = res.getWriter ();
	    pw.println (doc.toString());
	    pw.flush();
	} catch (Exception e) {
	    error (res, prop.getProperty("Errorexportingontology:") + e, e);
	}
    }
}


