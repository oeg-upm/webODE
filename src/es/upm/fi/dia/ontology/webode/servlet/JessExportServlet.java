package es.upm.fi.dia.ontology.webode.servlet;

// Java and XML stuff
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.translat.xml2java.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;

import es.upm.fi.dia.ontology.minerva.server.*;


/**
 * Servlet responsible for exporting ontologies in Java/Jess
 *
 * @author  Oscar Corcho García (based on Julio César Arpírez Vega work)
 * @version 0.1
 */
public class JessExportServlet extends BaseServlet
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	String ontology            = req.getParameter (ONTOLOGY_NAME);
//	boolean bConceptualization = req.getParameter (CONCEPTUALIZATION) != null &&
//	    req.getParameter (CONCEPTUALIZATION).equals (TRUE);
//	String[] instanceSets      = req.getParameterValues (INSTANCE_SETS);
	Properties prop = (Properties)session.getAttribute("prop");
	try {

//Recupero primero la sesión de Minerva
	    MinervaSession ms = (MinervaSession) session.getAttribute(MINERVA_SESSION);
          JessExportService exportService = (JessExportService) ms.getService ("jess");
//        JessExportService exportService = (JessExportService) session.getAttribute ("jess");

          String user = (String) session.getAttribute("user");

	    //Variables para ficheros temporales y para la hoja de estilos
    	    String tmpDir = new String(System.getProperty(MinervaServerConstants.MINERVA_VAR) + "\\tmp");
	    String tmpXMLFile = new String(tmpDir + "\\" + user.replace(' ','_') + ontology.replace(' ','_') + "SourceXWEBODE.xml");
	    String zipFile = new String(tmpDir + "\\" + user.replace(' ','_') + ontology.replace(' ','_') + "TargetJAVA.zip");

	    // Export ontology and requested instance sets.
	    exportService.ExportOntologyJess (ontology, tmpXMLFile, zipFile);

	    // Set appropiate content type.
	    res.setContentType("application/download");
	    java.io.OutputStream out=res.getOutputStream();
	    java.io.FileInputStream fin=new java.io.FileInputStream(zipFile);
	    java.io.File file_paper=new java.io.File(zipFile);
	    res.setContentLength((int)file_paper.length());
	    res.setHeader("Content-Disposition", "inline; filename=\"" + file_paper.getName() + "\"");
	    int size;
	    byte[] buff=new byte[4096];
	    while((size=fin.read(buff))!=-1) {
		out.write(buff,0,size);
	    }

	} catch (AuthenticationException e) {

		 error (res, prop.getProperty("UserhasnoaccesstoJavaJessService"), e);

	} catch (Exception e) {

	    error (res, prop.getProperty("ErrorexportingontologytoJavaJess") + e, e);
	}
    }

}


