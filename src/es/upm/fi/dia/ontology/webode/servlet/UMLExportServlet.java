package es.upm.fi.dia.ontology.webode.servlet;

// Java and XML stuff
import java.io.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
//import es.upm.fi.dia.ontology.webode.service.others.*;
//import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.webode.translat.UML.*;
import es.upm.fi.dia.ontology.minerva.server.*;


/**
 * Servlet responsible for exporting ontologies in Prolog
 *
 * @author  Oscar Corcho García (based on Julio César Arpírez Vega work)
 * @version 0.1
 */
public class UMLExportServlet extends BaseServlet {

  protected static final String DTD_FILE="dtd_file";
  protected String dtdFile=null;

  public void init (ServletConfig sc) throws ServletException {
    super.init (sc);


    if (dtdFile == null) {
      dtdFile = getInitParameter (DTD_FILE);
      if (dtdFile == null) {
        getServletContext().log ("Error: you must specify the dtd_file parameter.");
        throw new ServletException ("Error: you must specify the dtd_file parameter.");

      }
    }
  }

  public void doPost (HttpServletRequest req, HttpServletResponse res, HttpSession session)
      throws ServletException, IOException {
    String ontology = req.getParameter (ONTOLOGY_NAME);
    Properties prop = (Properties)session.getAttribute("prop");
    try {
      MinervaSession ms=(MinervaSession) session.getAttribute(MINERVA_SESSION);
      UMLService umlService = (UMLService) ms.getService (UML_SERVICE);

      StringBuffer doc = umlService.exportOntologyUML(ontology,null);

      ByteArrayOutputStream tmpZipFile=null;
      ZipOutputStream zipFile= new ZipOutputStream(tmpZipFile=new java.io.ByteArrayOutputStream());

      zipFile.putNextEntry(new ZipEntry(ontology.replace(' ','_') + ".xmi"));
      zipFile.write(doc.toString().getBytes());
      zipFile.closeEntry();

      java.net.URL dtdURL=new java.net.URL("http://localhost" + dtdFile);
      zipFile.putNextEntry(new ZipEntry(dtdURL.getFile()));
      java.io.InputStream rdfsFile = dtdURL.openStream();
      byte[] buff=new byte[4096];
      int size;
      while ((size=rdfsFile.read(buff))!=-1) zipFile.write(buff, 0, size);
      zipFile.closeEntry();

      zipFile.close();

      res.setContentType("application/download");

      byte[] array=tmpZipFile.toByteArray();
      res.setContentLength(array.length);

      res.setHeader("Content-Disposition", "inline; filename=\"" + ontology +".zip" + "\"");

      java.io.OutputStream out=res.getOutputStream();

      out.write(array);
    }
    catch (Exception e) {
     error(res, prop.getProperty("ErrorexportingontologytoUML") + e, e);
    }
  }
}