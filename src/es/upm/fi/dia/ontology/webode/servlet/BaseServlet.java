package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

public abstract class BaseServlet extends HttpServlet implements WebODEConstants
{
  protected static String errorURL;
  protected String[] javascriptFunctions;
  protected String url;
  protected String[] redirectParams = new String[10];
  protected String[] params = new String[10];

  public void init (ServletConfig sc) throws ServletException
  {
    super.init (sc);

    if (errorURL == null) {
      errorURL = getInitParameter (LOGIN_FAILURE);
      if (errorURL == null) {
        getServletContext().log ("Error: you must specify the errorURL parameter.");
        throw new ServletException ("Error: you must specify the errorURL parameter.");
      }
    }

    url = getInitParameter (REDIRECTION_URL);
    String val=null;
    ArrayList ajs=new ArrayList();
    for(int i=0; (val=getInitParameter(JAVASCRIPT + ((i==0)?"":""+i)))!=null; i++)
      ajs.add(val);
    if(ajs.size()>0)
      javascriptFunctions=(String[])ajs.toArray(new String[0]);
    else
      javascriptFunctions=null;

    for (int i = 0; i < params.length; i++) {
      String foo = getInitParameter (PARAM + i);
      if (foo == null)
        break;
      params[i] = foo;
    }
  }

  public void doPost (HttpServletRequest req,
                      HttpServletResponse res)
      throws ServletException, IOException
  {
    HttpSession session = req.getSession (false);
    if (session != null) {
      res.setContentType ("text/html");
      doPost (req, res, session);
    }
    else
      res.sendRedirect (errorURL);
  }

  protected abstract void doPost (HttpServletRequest req,
                                  HttpServletResponse res,
                                  HttpSession session)
      throws ServletException, IOException;

  protected void error (HttpServletResponse res, String msg) throws IOException, ServletException
  {
    PrintWriter pw = res.getWriter();
    pw.println ("<html><head> <link rel=\"stylesheet\" type=\"text/css\" href=\"/webode/css/queries.css\">");
    pw.println ("<title>Error</title>");
    pw.println ("</head><body>");
    pw.println ("<h1>Error</h1><hr><i> " + msg + "</i><p>");
    pw.println ("<hr><small>" + VERSION + "</small></body></html>");
    pw.flush();
  }

  protected void error (HttpServletResponse res, String msg, Throwable th) throws IOException, ServletException
  {
    PrintWriter pw = res.getWriter();
    pw.println ("<html><head> <link rel=\"stylesheet\" type=\"text/css\" href=\"/webode/css/queries.css\">");
    pw.println ("<title>Error</title>");
    pw.println ("</head><body>");
    pw.println ("<h1>Error</h1><hr><i> " + msg + "</i><p>");
    pw.println ("<hr><small>" + VERSION + "</small></body></html>");
    pw.println ("<pre>");
    pw.flush();

    th.printStackTrace(pw);

    pw.flush();
    pw.println ("</pre>");
  }

  protected void headeraux (PrintWriter pw)
  {
    pw.println ("<html><head>");
    pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"1;URL=/webode/jsp/webode/about.jsp\"> ");
    pw.println("<link rel='stylesheet' type='text/css' href='/webode/css/webode.css'>");
    pw.println("<link rel='stylesheet' type='text/css' href='/webode/css/queries.css'>");

    if (javascriptFunctions != null)
      for(int i=0; i<javascriptFunctions.length; i++)
        pw.println ("<script language=\"javascript\" src=\"" + javascriptFunctions[i] + "\"></script>");
    pw.println ("</head><body>");
    pw.flush();
  }

  protected void header (PrintWriter pw)
  {
    pw.println ("<html><head>");
    pw.println("<link rel='stylesheet' type='text/css' href='/webode/css/webode.css'>");
    pw.println("<link rel='stylesheet' type='text/css' href='/webode/css/queries.css'>");

    if (javascriptFunctions != null)
      for(int i=0; i<javascriptFunctions.length; i++)
        pw.println ("<script language=\"javascript\" src=\"" + javascriptFunctions[i] + "\"></script>");
    pw.println ("</head><body>");
    pw.flush();
  }

  protected void body (PrintWriter pw, String msg)
  {
    pw.println ("<p>" + msg);
    pw.flush();
  }

  protected void setRedirectParam (String value)
  {
    setRedirectParam (0, value);
  }

  protected void setRedirectParam (int pos, String value)
  {
    redirectParams[pos] = value;
  }

  protected void setURL (String url)
  {
    this.url = url;
  }

  protected void redirect (PrintWriter pw)
  {
    if (url != null) {
      pw.println ("<script language=\"JavaScript\">");
      StringBuffer strBuffer = new StringBuffer ();
      strBuffer.append ("redirect ('" + url + "?");
      for (int i = 0; i < redirectParams.length && redirectParams[i] != null; i++) {
        if (i != 0)
          strBuffer.append ('&');
        try {
          strBuffer.append (params[i] + "=" + URLEncoder.encode (redirectParams[i],"ISO-8859-1"));
        } catch (UnsupportedEncodingException mfue) {
          System.out.println ("Error in URL: " + mfue);
        }
      }
      strBuffer.append ("');\n</script>");
      pw.println (strBuffer);
    }
  }

  protected void trailer (PrintWriter pw)
  {
    redirect(pw);
    pw.println ("<hr><small>" + VERSION + "</small></body></html>");
    pw.flush();
  }

  protected void sendAdditionEvent (PrintWriter pw, int type, String name)
  {
    pw.println ("<script language=\"JavaScript\">");
    pw.println ("termAdded (" + type + ", '" + name + "');");
    pw.println ("</script>");
  }

  protected void sendDeletionEvent (PrintWriter pw, int type, String name)
  {
    pw.println ("<script language=\"JavaScript\">");
    pw.println ("termDeleted (" + type + ", '" + name + "');");
    pw.println ("</script>");
  }

  protected void sendUpdateEvent (PrintWriter pw, int type, String oldName, String newName)
  {
    pw.println ("<script language=\"JavaScript\">");
    pw.println ("termUpdated (" + type + ", '" + oldName + "', '" + newName + "');");
    pw.println ("</script>");
  }

}




