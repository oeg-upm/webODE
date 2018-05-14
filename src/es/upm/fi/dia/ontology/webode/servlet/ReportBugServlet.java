package es.upm.fi.dia.ontology.webode.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import java.util.*;
import java.io.*;

public class ReportBugServlet extends BaseServlet implements SingleThreadModel
{
     String dir;

     public void init (ServletConfig sc) throws ServletException
     {
	super.init (sc);

	dir =System.getProperty(MinervaServerConstants.MINERVA_HOME)+ sc.getInitParameter ("directory");
        if (dir == null)
	   throw new ServletException ("Directory parameter is mandatory");
     
     }

  
     public void doPost (HttpServletRequest req, 
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
     {
	String person = req.getParameter ("person");
	String description = req.getParameter ("description");
	String steps = req.getParameter ("steps");
    Properties prop = (Properties)session.getAttribute("prop");
	new BugDescriptor (person, description, steps).store (new File (dir, "bug" +
		System.currentTimeMillis()));	
	
	PrintWriter pw = res.getWriter();
	header(pw);
	body (pw, prop.getProperty("Thankyou!"));
	trailer (pw);
      }
}
        