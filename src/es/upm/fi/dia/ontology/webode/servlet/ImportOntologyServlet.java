package es.upm.fi.dia.ontology.webode.servlet;

import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.webode.util.*;
import es.upm.fi.dia.ontology.webode.translat.UML.*;
import es.upm.fi.dia.ontology.webode.translat.DAMLOIL.*;
import es.upm.fi.dia.ontology.webode.translat.OWL.*;
import es.upm.fi.dia.ontology.webode.translat.RDFS.*;
import es.upm.fi.dia.ontology.webode.translat.evaluators.*;
import es.upm.fi.dia.ontology.webode.translat.logs.*;
import es.upm.fi.dia.ontology.webode.xml.*;
import http.utils.multipartrequest.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.UMLIOException;

import java.util.regex.*;

/**
 * Servlet responsible for receiving a file to import an ontology in different ontology languages.
 *
 * @author Julio César Arpírez Vega
 * @author Oscar Corcho
 * @version 0.3
 */
public class ImportOntologyServlet extends BaseServlet
{
  protected static int MAX_LENGTH_FILE=1024*1024*50;

  /**
   * This constant means that the UML import process finished properly
   */
  private static final int IMPORT_OK       =1;

  /**
   * This constant means that there was a problem with the DTD specified in
   * the source document
   */
  private static final int IMPORT_ERROR_DTD=2;

  /**
   * This constant means that the UML import process could not finish properly
   */
  private static final int IMPORT_EXCEPTION=3;

  /**
   * This attribute is used to let auxiliar methods use the active http session
   */
  HttpSession theSession=null;

  /**
   * This constant means that no ontology name was supplied
   */
  private static final int IMPORT_NO_NAME=4;

  /**
   * This attribute is used to let auxiliar methods use the http response
   */
  HttpServletResponse theResponse=null;

  /**
   * This attribute is used to let auxiliar methods use the http request
   */
  HttpServletRequest theRequest=null;

  //----------------------------------------------------------------------------
  //-- Begin auxiliar methods --------------------------------------------------
  //---------------------------------------------------------- Miguel Esteban --
  //----------------------------------------------------------------------------


  private void responseUML(int status, String data,MultilingualityProperties prop) throws IOException {
    PrintWriter out=theResponse.getWriter();
  //  MultilingualityProperties prop = (MultilingualityProperties)session.getAttribute("prop");
    out.println("<html>");
    out.println("  <head>");
    out.println("    <link rel=\"stylesheet\" type=\"text/css\" href=\"/webode/css/queries.css\">");
    out.println("    <META http-equiv=\"Pragma\" content=\"no-cache\">");
    out.println("    <META HTTP-EQUIV=\"CACHE-CONTROL\" CONTENT=\"NO-CACHE\">");
    out.println("  </head>");
    out.println("  <body>");
    switch(status) {
      case IMPORT_OK       :

        //out.println("      prop.getProperty("Theontologycouldnotbeimported:")Ontology <i>"+data+"</i> successfully imported.");
        out.println("     "+prop.getProperty("Ontologysuccessfullyimported",new String[] {data}));
        break;
      case IMPORT_ERROR_DTD:
        String file=data.substring(data.lastIndexOf("/")+1,data.length());
        out.println("      "+prop.getProperty("Theontologycouldnotbeimported."));
        out.println("      <br>");
        out.println("      "+prop.getProperty("TheassociatedDTDfile",new String[] {file}));

        String url   =theRequest.getHeader("referer");
        String subs  ="jsp/webode/ImportOntology.jsp?language=UML";
        String append="DTD/UML1311.dtd";
        String theDTDLocation=url.substring(0,url.indexOf(subs))+append;
        out.println(prop.getProperty("Use",new String[] {theDTDLocation}));
/*
        Enumeration enum=theRequest.getHeaderNames();
        out.println("      <br>");
        out.println("      <table>");
        while(enum.hasMoreElements()) {
          String header=(String)enum.nextElement();
          String value=theRequest.getHeader(header);
          out.println("      <tr>");
          out.println("        <td>");
          out.println("          "+header);
          out.println("        </td>");
          out.println("        <td>");
          out.println("          "+value);
          out.println("        </td>");
          out.println("      </tr>");

        }
        out.println("      </table>");
*/
        break;
      case IMPORT_EXCEPTION:
        out.println("      "+prop.getProperty("OntologycouldnotbeimportedExceptionthrown"));
         out.println(" <br>");
        out.println("       <i>"+data.replaceAll("\n","<br>")+"</i>");
        break;
      case IMPORT_NO_NAME:
        out.println("      "+prop.getProperty("OntologynamenotsuppliedImportprocesscantcontinue."));
        break;
    }
    out.println("    <hr>");
    out.println("    <div align=\"left\">");
    out.println("      <small>WebODE 2.0</small>");
    out.println("    </div>");
    out.println("    <form name=foo>");
    out.println("      <div align=\"right\">");
    out.println("        <input type=\"button\" value=\"Help\" onClick=\"document.location = '/webode/help.html';\">");
    out.println("      </div>");
    out.println("    </form>");
    out.println("  </body>");
    out.println("</html>");
  }

  //----------------------------------------------------------------------------
  //-- Begin auxiliar methods --------------------------------------------------
  //---------------------------------------------------------- Miguel Esteban --
  //----------------------------------------------------------------------------

  public void doPost (HttpServletRequest req, HttpServletResponse res, HttpSession session)
      throws ServletException, IOException
  {

    String tempDir = System.getProperty("MINERVA_VAR") + File.separator + "tmp";
    ServletMultipartRequest reqmulti=new ServletMultipartRequest(req,tempDir,MAX_LENGTH_FILE);

    String language = reqmulti.getURLParameter("language");
    File file_temp=reqmulti.getFile("importFile");
    //Properties prop = (Properties)session.getAttribute("prop");
    MultilingualityProperties prop = (MultilingualityProperties)session.getAttribute("prop");
    if(file_temp==null)
    {
      error (res, prop.getProperty("Therewasanerrorwhileuploadingthefile."));
     // error(res, "There was an error while uploading the file.");
      return;
    }
    String file_name = file_temp.getName();
    String namespace, ontologyName,evaluate;
    if (!language.equals("XML"))
    {
      namespace = reqmulti.getURLParameter("namespace");
      ontologyName = reqmulti.getURLParameter("ontologyName");
    }

    if (language.equals("XML"))
    {
      ImportService xmlImport = null;
      try
      {
	xmlImport = (ImportService) session.getAttribute (IMPORT_SERVICE);
	if (xmlImport == null)
	{
	  xmlImport = (ImportService) ((MinervaSession) session.getAttribute
				       (MINERVA_SESSION)).getService (IMPORT_SERVICE);
	  session.setAttribute (IMPORT_SERVICE, xmlImport);
	}
	//Open and read input file
	FileInputStream finput=new FileInputStream(new File(tempDir,file_name));
	byte[] buffer = new byte[4096];
	int foo;
	StringBuffer strBuffer = new StringBuffer (8096);
	while ((foo = finput.read(buffer, 0, buffer.length))>=0)
	{
	  strBuffer.append(new String(buffer,0,foo));
	}
	finput.close();
	// Parse tree
	xmlImport.loadInMemory (strBuffer);
	// Forward to the following page
	getServletContext().getRequestDispatcher("/jsp/webode/ImportOntology2.jsp").forward (req, res);
      }
      catch (Exception e)
      {
        e.printStackTrace(System.out);
        //Error(res, prop.getProperty("ErrorimportingXMLontology:")+ e.getMessage());
	error (res, prop.getProperty("ErrorimportingXMLontology:") + e.getMessage(), e);
	try
	{
	  session.removeAttribute (IMPORT_SERVICE);
	  xmlImport.disconnect();
	}
	catch (Exception e1)
	{
          e.printStackTrace(System.out);
	  getServletContext().log (new Date() +
				    prop.getProperty("unexpectederrordisconnectingXMLImportservice")  + e1);
	}
      }
    }
    else if (language.equals("RDFS"))
    {
      RDFSImportService rdfsImp=null;
      namespace=reqmulti.getURLParameter("namespace");
      ontologyName=reqmulti.getURLParameter("ontologyName");
      evaluate=reqmulti.getURLParameter("evaluate");

      try
      {
	if(namespace==null || namespace.length()==0 || ontologyName==null || ontologyName.length()==0)
	{
	  throw new Exception(prop.getProperty("ErrorimportingRDFSontologyontologynameornamespaceisempty"));
	}
	rdfsImp = (RDFSImportService) session.getAttribute (RDFS_IMPORT_SERVICE);
	MinervaSession minervaSession=(MinervaSession) session.getAttribute(MINERVA_SESSION);
	if (rdfsImp == null)
	{
	  rdfsImp = (RDFSImportService) minervaSession.getService (RDFS_IMPORT_SERVICE);
	  session.setAttribute (RDFS_IMPORT_SERVICE, rdfsImp);
	}
	File rdfsFile=new File(tempDir,file_name);
	char[] buffer=new char[(int)rdfsFile.length()];
	(new FileReader(rdfsFile)).read(buffer,0,buffer.length);
	rdfsImp.RDFS2WebODE(buffer,ontologyName,namespace,(String)session.getAttribute(USER),(String)session.getAttribute(GROUP));

	if ((evaluate!=null)&&(evaluate.equals("true")))
	{
	  EvaluatorBuilder evaluator_builder = new EvaluatorBuilder();
	  java.io.CharArrayReader in=new java.io.CharArrayReader(buffer);
	  Evaluator	evaluator =
	      evaluator_builder.buildEvaluator(EvaluatorBuilder.RDFS,in,namespace);
	  evaluator.evaluateCircularityErrors();
	  evaluator.evaluateIndirectRedundancies();
	  Log log=evaluator.getLog();
	  session.setAttribute("evaluationLog",log);
	  getServletContext().getRequestDispatcher("/jsp/webode/ShowEvaluationLog.jsp").forward (req, res);
	}
      }

      catch(Exception e)
      {
	e.printStackTrace(System.out);
	//error (res, "Error importing RDFS ontology: " + e.getMessage());
	error (res, prop.getProperty("ErrorimportingRDFSontology:") + e.getMessage(), e);
      }
    }
    else if (language.equals("DAMLOIL"))
    {
      DAMLOILImportService damloilImp=null;
      namespace=reqmulti.getURLParameter("namespace");
      ontologyName=reqmulti.getURLParameter("ontologyName");
      evaluate=reqmulti.getURLParameter("evaluate");
      try
      {
	if(namespace==null || namespace.length()==0 || ontologyName==null || ontologyName.length()==0)
	{
	  throw new Exception(prop.getProperty("ErrorimportingDAMLOILontologyontologynameornamespaceisempty:"));
	}
	damloilImp = (DAMLOILImportService) session.getAttribute (DAMLOIL_IMPORT_SERVICE);
	MinervaSession minervaSession=(MinervaSession) session.getAttribute(MINERVA_SESSION);
	if (damloilImp == null)
	{
	  damloilImp = (DAMLOILImportService) minervaSession.getService (DAMLOIL_IMPORT_SERVICE);
	  session.setAttribute (DAMLOIL_IMPORT_SERVICE, damloilImp);
	}
	File damloilFile=new File(tempDir,file_name);
	char[] buffer=new char[(int)damloilFile.length()];
	(new FileReader(damloilFile)).read(buffer,0,buffer.length);
	damloilImp.DAMLOIL2WebODE(buffer,ontologyName,namespace,(String)session.getAttribute(USER),(String)session.getAttribute(GROUP));
	if ((evaluate!=null)&&(evaluate.equals("true")))
	{
	  EvaluatorBuilder evaluator_builder = new EvaluatorBuilder();
	  java.io.CharArrayReader in=new java.io.CharArrayReader(buffer);
	  Evaluator	evaluator =
	      evaluator_builder.buildEvaluator(EvaluatorBuilder.DAML,in,namespace);
	  evaluator.evaluateCircularityErrors();
	  evaluator.evaluateIndirectRedundancies();
	  evaluator.evaluateDisjointPartitions();
	  evaluator.evaluateExhaustiveDisjointPartitions();
	  Log log=evaluator.getLog();
	  session.setAttribute("evaluationLog",log);
	  getServletContext().getRequestDispatcher("/jsp/webode/ShowEvaluationLog.jsp").forward (req, res);
	}
      }
      catch(Exception e)
      {
	e.printStackTrace(System.out);
	//error (res, "Error importing DAMLOIL ontology: " + e.getMessage());
	error (res, prop.getProperty("ErrorimportingDAMLOILontology:") + e.getMessage(), e);
      }

    }
    else if (language.equals("OWL"))
    {
      OWLImportService owlImp=null;
      namespace=reqmulti.getURLParameter("namespace");
      ontologyName=reqmulti.getURLParameter("ontologyName");
      evaluate=reqmulti.getURLParameter("evaluate");
      try
      {
	if(namespace==null || namespace.length()==0 || ontologyName==null || ontologyName.length()==0)
	{
	  //throw new Exception("Error importing OWL ontology: ontology name or namespace is empty");
	  throw new Exception(prop.getProperty("ErrorimportingOWLontology:ontologynameornamespaceisempty"));
	}
	owlImp = (OWLImportService) session.getAttribute (OWL_IMPORT_SERVICE);
	MinervaSession minervaSession=(MinervaSession) session.getAttribute(MINERVA_SESSION);
	if (owlImp == null)
	{
	  owlImp = (OWLImportService) minervaSession.getService (OWL_IMPORT_SERVICE);
	  session.setAttribute (OWL_IMPORT_SERVICE, owlImp);
	}
	File owlFile=new File(tempDir,file_name);
	char[] buffer=new char[(int)owlFile.length()];
	(new FileReader(owlFile)).read(buffer,0,buffer.length);
	owlImp.OWL2WebODE(buffer,ontologyName,namespace,(String)session.getAttribute(USER),(String)session.getAttribute(GROUP));
	if ((evaluate!=null)&&(evaluate.equals("true")))
	{
	  EvaluatorBuilder evaluator_builder = new EvaluatorBuilder();
	  java.io.CharArrayReader in=new java.io.CharArrayReader(buffer);
	  Evaluator	evaluator =
	      evaluator_builder.buildEvaluator(EvaluatorBuilder.OWL_DL,in,namespace);
	  evaluator.evaluateCircularityErrors();
	  evaluator.evaluateIndirectRedundancies();
	  evaluator.evaluateDisjointPartitions();
	  Log log=evaluator.getLog();
	  session.setAttribute("evaluationLog",log);
	  getServletContext().getRequestDispatcher("/jsp/webode/ShowEvaluationLog.jsp").forward (req, res);
	}


      }
      catch(Exception e)
      {
	e.printStackTrace(System.out);
	//error (res, "Error importing OWL ontology: " + e.getMessage());
	error (res, prop.getProperty("ErrorimportingOWLontology:") + e.getMessage(), e);
      }
    } else if(language.equals("UML")) {

      theSession =session;
      theRequest =req;
      theResponse=res;

      try {

        ontologyName=reqmulti.getURLParameter("ontologyName");
        if(ontologyName==null||ontologyName.equals("")) {
          responseUML(IMPORT_NO_NAME,null,prop);
          return;
        }


        // Open and read input file
        FileInputStream inputStream=
            new FileInputStream(new File(tempDir,file_name));

        int          length   =0;
        byte[]       buffer   =new byte[4096];
        StringBuffer strBuffer=new StringBuffer(8096);

        while((length=inputStream.read(buffer,0,buffer.length))>=0) {
          strBuffer.append(new String(buffer,0,length));
        }
        inputStream.close();

        UMLService umlImport=null;

        // Get the service
        umlImport=(UMLService)session.getAttribute(UML_IMPORT_SERVICE);
        if(umlImport==null) {
          MinervaSession ms=(MinervaSession)session.getAttribute(MINERVA_SESSION);
          umlImport=(UMLService)ms.getService(UML_IMPORT_SERVICE);
          session.setAttribute(UML_IMPORT_SERVICE, umlImport);
        }

        // Perform import
        try {
          MinervaSession ms=(MinervaSession)session.getAttribute(MINERVA_SESSION);
          umlImport.importOntologyUML(ontologyName,ms.getUser(), null, strBuffer,ms);
          responseUML(IMPORT_OK,ontologyName,prop);
        } catch(O2UException ex) {
          if(ex instanceof UMLIOException) {
            String mes=ex.getMessage();
            String fileNamePattern="\\\"([^\\\"]*)\\\"";
            String errorPattern   ="File "+fileNamePattern+" not found.";
            Pattern ep=Pattern.compile(errorPattern);
            Matcher em=ep.matcher(mes);
            if(em.find()) {
              responseUML(IMPORT_ERROR_DTD,em.group(1),prop);
            } else {
              StringWriter theWriter=new StringWriter();
              ex.printStackTrace(new PrintWriter(theWriter));
              responseUML(IMPORT_EXCEPTION,theWriter.getBuffer().toString(),prop);
            }
          }
        }
      } catch(Exception ex) {
        responseUML(IMPORT_EXCEPTION,ex.getMessage(),prop);
      }
    }
  }
}