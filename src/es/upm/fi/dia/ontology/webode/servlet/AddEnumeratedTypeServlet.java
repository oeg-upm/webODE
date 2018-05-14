package es.upm.fi.dia.ontology.webode.servlet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

public class AddEnumeratedTypeServlet extends BaseServlet {

  protected void doPost(HttpServletRequest  req,
                        HttpServletResponse res,
                        HttpSession session) throws IOException,
                                                    ServletException {
    // These things are in the session.
    String oname=(String)session.getAttribute(CURRENT_ONTOLOGY);
    Properties prop = (Properties)session.getAttribute("prop");

    // Get parameters
    String enumeratedTypeName=req.getParameter(ENUMERATED_TYPE_NAME);
    String description=req.getParameter(ENUMERATED_TYPE_DESCRIPTION);
    String nValues=req.getParameter("values");
    String[] values=null;
//    System.out.println("=- ADD SERVLET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//    System.out.println("Name.......: "+enumeratedTypeName);
//    System.out.println("Description: "+description);
//    System.out.println("Ontology...: "+oname);
//    System.out.println("Values.....:");
    if(nValues!=null) {
      int val_count=Integer.parseInt(nValues)-1;

      HashSet vals=new HashSet();
      String val;
      for(int i=0;i<val_count;i++) {
        val=req.getParameter(ENUMERATED_TYPE_VALUE_NAME+"_"+i);
        if(val!=null && (val=val.trim()).length()>0)
          vals.add(val);
      }
      if(vals.size()>0)
        values=(String[])vals.toArray(new String[0]);
    }

    try {
      // all right.  Let's insert this data into the database
      ODEService odeService=(ODEService)session.getAttribute(ODE_SERVICE);
      odeService.insertEnumeratedType(oname,enumeratedTypeName,description,values);

      PrintWriter pw = res.getWriter ();

      // NUEVO PARA MULTILINGUALIDAD DANI
      pw.println("<html>");
      pw.println("  <head>");
      pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=../jsp/webode/about.jsp\"> ");
      pw.println("  </head>");
      // FIN NUEVO

      header(pw);
      body (pw, prop.getProperty("EnumeratedType")+ "<i> " + enumeratedTypeName + " </i> "+ prop.getProperty("addedtoontology")+"<i> " + oname + " </i> "+prop.getProperty("successfully."));

      sendAdditionEvent(pw, -1, enumeratedTypeName);
      trailer(pw);
    } catch(Exception e) {
      error(res, e.getMessage(), e);
    }
  }
}