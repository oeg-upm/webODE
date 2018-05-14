package es.upm.fi.dia.ontology.webode.servlet;

// Java stuff
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.translat.RDFS.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;

import es.upm.fi.dia.ontology.minerva.server.*;

/**
 * Servlet responsible for exporting ontologies to RDF(S)
 *
 * @author  Oscar Corcho (based on Julio César Arpírez work)
 * @version 0.1
 */
public class RDFSExportServlet extends BaseServlet
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

		//If the RDF(S) export service is not stored in the HTTP session
		  MinervaSession ms = (MinervaSession) session.getAttribute(MINERVA_SESSION);
		  RDFSExportService exportService = (RDFSExportService) ms.getService (RDFS_SERVICE);
		//If it is stored there
	    //RDFSExportService exportService = (RDFSExportService) session.getAttribute("rdfs");

        String user = (String) session.getAttribute("user");

/*
	    //Variables para ficheros temporales
   	    String tmpDir = new String(System.getProperty(MinervaServerConstants.MINERVA_VAR) + "\\tmp");
	    String zipFileName = new String(tmpDir + "\\" + user.replace(' ','_') + ontology.replace(' ','_') + "TargetRDFS.zip");
	    String rdfsFileName = new String(tmpDir + "\\" + user.replace(' ','_') + ontology.replace(' ','_') + ".rdfs");

	    // Export ontology and requested instance sets.
	    exportService.ExportOntologyRDFS (ontology, user, bConceptualization, instanceSets, namespace, rdfsFileName);

		//Initialize the zip file where the RDF(S) files must be included
        //java.io.FileOutputStream outputZipFile= new java.io.FileOutputStream(zipFileName);
        ZipOutputStream zipFile= new ZipOutputStream(new java.io.FileOutputStream(zipFileName));

		//Include the RDF(S) files in the zip file
    	//ZipEntry entryZipFile= new ZipEntry(rdfsFileName);
	    zipFile.putNextEntry(new ZipEntry(ontology.replace(' ','_') + ".rdfs"));
	    java.io.FileInputStream rdfsFile = new FileInputStream(rdfsFileName);
	    byte[] buff=new byte[4096];
	    int size;
	    while ((size=rdfsFile.read(buff))!=-1) zipFile.write(buff, 0, size);
	    zipFile.closeEntry();
	    zipFile.close();
*/
	    // Export ontology and requested instance sets.
	    //String zipFileName = exportService.ExportOntologyRDFS (ontology, user, bConceptualization, instanceSets, namespace);
	    byte[] zipFile = exportService.ExportOntologyRDFS (ontology, user, bConceptualization, instanceSets, namespace);


	    // Set appropiate content type.
	    res.setContentType("application/download");
	    //java.io.File file=new java.io.File(zipFileName);
	    //res.setContentLength((int)file.length());
	    res.setContentLength(zipFile.length);
	    //res.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
		res.setHeader("Content-Disposition", "inline; filename=\"" + ontology +".zip" + "\"");

		//Send content
	    //java.io.FileInputStream fin=new java.io.FileInputStream(zipFileName);
	    java.io.OutputStream out=res.getOutputStream();
	    //int size2;
	    //byte[] buff2=new byte[4096];
	    //while((size2=fin.read(buff2))!=-1) out.write(buff2,0,size2);
		out.write(zipFile);

	} catch (AuthenticationException e) {
		error (res, prop.getProperty("UserhasnoaccesstoRDFExportService"));
	} catch (Exception e) {


	   error (res, prop.getProperty("Errorexportingontology:") + e, e);
	}
    }

}


