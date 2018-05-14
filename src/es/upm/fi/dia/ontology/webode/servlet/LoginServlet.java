package es.upm.fi.dia.ontology.webode.servlet;

// Java stuff
import java.io.*;
import java.util.*;

// Servlets stuff
import javax.servlet.*;
import javax.servlet.http.*;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.client.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.MinervaService;

import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.webode.xml.*;
import es.upm.fi.dia.ontology.webode.servlet.session.*;

import es.upm.fi.dia.ontology.webode.Axiom.*;
import es.upm.fi.dia.ontology.webode.translat.OIL.*;
import es.upm.fi.dia.ontology.webode.translat.DAMLOIL.*;
import es.upm.fi.dia.ontology.webode.translat.Prolog.*;
import es.upm.fi.dia.ontology.webode.translat.XCARIN.*;
import es.upm.fi.dia.ontology.webode.Inference.*;
import es.upm.fi.dia.ontology.webode.OntoClean.*;

/**
 * This servlet is responsible for authenticating and authorizing the user
 * to log in WebODE.
 * <p>
 * It establishes a session if the user is authenticated and the machine
 * he is attempting to access is allowed to do so.
 * <p>
 * A sweeper thread is also started to keep the server clean of invalidate
 * sessions.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.7
 */
public class LoginServlet extends BaseServlet implements SingleThreadModel, WebODEConstants
{
  /** Default time limit for the sessions.  Time after which the session
   * is invalidated and the user must re-login (defaults to 10 minutes). */
  public static int DEFAULT_TIME_LIMIT = 600;

  private String successURL, minervaURL, unauthorizedURL, minervaPortStr;
  private int minervaPort=-1;
  private int timeLimit;
  private static Vector allowedIPs;
  private SweeperThread sweeperThread;

  public void init (ServletConfig sc) throws ServletException
  {
    super.init (sc);

    try {
      timeLimit = Integer.parseInt (getInitParameter (TIME_LIMIT));
    } catch (Exception e) {
      timeLimit = DEFAULT_TIME_LIMIT;
    }

    minervaURL = getInitParameter (MINERVA_URL);
    minervaPortStr = getInitParameter (MINERVA_PORT);
    if(minervaPortStr!=null) {
      try {
        minervaPort=Integer.parseInt(minervaPortStr);
      }
      catch(Exception e) {
        minervaPort=-1;
      }
    }
    successURL = getInitParameter (LOGIN_SUCCESS);
    unauthorizedURL = getInitParameter (UNAUTHORIZED);
    if (minervaURL == null || errorURL == null || successURL == null) {
      getServletContext().log ("Error: ha de especificarse la URL, " +
                               "la pagina a visualizar en caso de exito y la" +
                               " que se visualizara en caso de error en el login.");

      throw new ServletException
      ("Error: ha de especificarse la URL, " +
      "la pagina a visualizar en caso de exito y la" +
      " que se visualizara en caso de error en el login.");
    }

    if (unauthorizedURL == null)
      unauthorizedURL = errorURL;

    // Get allowed IP addresses
    allowedIPs = new Vector();
    String foo;
    for (int i = 0; (foo = getInitParameter (ALLOWED + i)) != null; i++)
      allowedIPs.addElement (foo);

    // Start sweeper thread
    ServletContext ctxt = getServletContext();
    ctxt.log (new Date() + ": starting sweeper thread...");
    sweeperThread = new SweeperThread (ctxt, timeLimit);
    sweeperThread.start();
  }

  public void doGet(HttpServletRequest req,
                    HttpServletResponse res) throws ServletException, IOException {
    doPost(req,res);
  }

  public void doPost (HttpServletRequest req,
                      HttpServletResponse res,
                      HttpSession session)
      throws ServletException, IOException
  {
  }


  public void doPost (HttpServletRequest req,
                      HttpServletResponse res)
      throws ServletException, IOException
  {
    String user     = req.getParameter (USER);
    String password = req.getParameter (PASSWORD);
    HttpSession session = req.getSession (true);

    // Delete previous session data and disconnect services
    _cleanSession (session);
    session=req.getSession(true);

    Object[] obj = null;
    Object[] guest;
    try {
      MinervaSession minervaSession;
      if(minervaPort<0) {
        minervaSession = MinervaClient.getMinervaSession(new MinervaURL (minervaURL), user, password);
      }
      else {
        minervaSession = MinervaClient.getMinervaSession(new MinervaURL(minervaURL,minervaPort), user, password);
      }

      ODEService odeService = (ODEService) minervaSession.getService (ODE_SERVICE);

      guest = _isSpecialGuest(minervaSession,odeService);
      if (guest==null){
        obj = _isValid(minervaSession,odeService);
      /*Primero tratamos el caso de que no tenga Login especial mas abajo el caso habitual*/
      }
      else{
        getServletContext().log (new Date () + ": granting access to guest user " +
                                 user + " from no IP address");
        // Ponemos el límite de tiempo especificado en los parámetros.
        session.setMaxInactiveInterval (timeLimit);

        session.setAttribute (ODE_SERVICE, guest[0]);
        if (guest[1] != null) session.setAttribute (EXPORT_SERVICE, guest[1]);
        session.setAttribute (MINERVA_SESSION, guest[2]);
        if (guest[3] != null) session.setAttribute (AXIOM_SERVICE, guest[3]);
        if (guest[4] != null) session.setAttribute (INFERENCE_SERVICE, guest[4]);
        if (guest[5] != null) session.setAttribute ("oil", guest[5]);
        if (guest[6] != null) session.setAttribute ("damloil", guest[6]);
        if (guest[7] != null) session.setAttribute ("prolog", guest[7]);
        if (guest[8] != null) session.setAttribute ("xcarin", guest[8]);
   /* if (guest[9] != null) session.setAttribute (ONTOCLEAN_SERVICE, guest[9]); */

        // Guardamos el usuario.
        session.setAttribute (USER, user);
        session.setAttribute (GROUP, ((ODEService) guest[0]).getGroup());
        session.setAttribute (GROUPS, ((ODEService) guest[0]).getUserGroups());
        sweeperThread.addSession(session);
        res.sendRedirect (successURL);
        return;/*Hope this rules!!!*/
      }/*end else*/
    } catch (Exception e) {
      session.invalidate();
      _error (res, e.getMessage());
      return;
    }


    if (obj != null) {
      if (_allowedIP (req.getRemoteAddr())) {
        getServletContext().log (new Date () + ": granting access to user " +
                                 user + " from " + req.getRemoteAddr());

        // Ponemos el límite de tiempo especificado en los parámetros.
        session.setMaxInactiveInterval (timeLimit);

        session.setAttribute (ODE_SERVICE, obj[0]);
        if (obj[1] != null) session.setAttribute (EXPORT_SERVICE, obj[1]);
        session.setAttribute (MINERVA_SESSION, obj[2]);
        if (obj[3] != null) session.setAttribute (AXIOM_SERVICE, obj[3]);
        if (obj[4] != null) session.setAttribute (INFERENCE_SERVICE, obj[4]);
        if (obj[5] != null) session.setAttribute ("oil", obj[5]);
        if (obj[6] != null) session.setAttribute ("damloil", obj[6]);
        if (obj[7] != null) session.setAttribute ("prolog", obj[7]);
        if (obj[8] != null) session.setAttribute ("xcarin", obj[8]);
   /* if (obj[9] != null) session.setAttribute (ONTOCLEAN_SERVICE, obj[9]); */
        // Guardamos el usuario.
        session.setAttribute (USER, user);
        session.setAttribute (GROUP, ((ODEService) obj[0]).getGroup());
        session.setAttribute (GROUPS, ((ODEService) obj[0]).getUserGroups());
        sweeperThread.addSession(session);
        res.sendRedirect (successURL);
      } else {
        getServletContext().log (new Date () + ": denying access to user " +
                                 user + " from " + req.getRemoteAddr() + ". " +
                                 "IP address not authorized to log in.");

        res.sendRedirect (unauthorizedURL);
      }
    }
    else {
      if (session != null)
        session.invalidate();

      getServletContext().log (new Date () + ": denying access to user " +
                               user + " from " + req.getRemoteAddr());
      //Deberíamos desconectar aquí la minervaSession.

      res.sendRedirect (errorURL);
    }
  }



  private boolean _allowedIP (String addr)
  {
    if (allowedIPs.isEmpty()) return true;

    for (int i = 0; i < allowedIPs.size(); i++) {
      if (addr.startsWith((String) allowedIPs.elementAt(i)))
        return true;
    }
    return false;
  }

  private void _error (HttpServletResponse res, String msg) throws IOException
  {
    res.setContentType ("text/html");
    PrintWriter pw = res.getWriter();
    pw.println ("<html><head><title>Error contacting server</title></head><body>");
    pw.println ("<h1>Error contacting server</h1><p><i>" + msg + "</i>");
    pw.println ("<hr><small>" + VERSION + "</small><body></html>");
    pw.flush();
  }

  public String getServletInfo ()
  {
    return "The servlet to validate username/password pairs.";
  }

  private Object[] _isValid (MinervaSession minervaSession, ODEService odeService)
      throws ServletException
  {
    try {

      ExportService exportService = null;
      OILExportService oilExportService = null;
      DAMLOILExportService damloilExportService = null;
      PrologExportService prologExportService = null;
      XCARINExportService XCARINExportService = null;
      AxiomService axiomService=null;
      InferenceService inferenceService=null;
      OntoCleanService ontoCleanService=null;


      try {
        axiomService=(AxiomService) minervaSession.getService(AXIOM_SERVICE);
        } catch (AuthenticationException ae) {}

        try {
          inferenceService=(InferenceService) minervaSession.getService(INFERENCE_SERVICE);
          } catch (AuthenticationException ae) {}

//Añadido por Oscar Corcho: 26/02/2002
//        try {
//        exportService = (ExportService) minervaSession.getService (EXPORT_SERVICE);
//        } catch (AuthenticationException ae) {}
//
//        try {
//        importService = (ImportService) minervaSession.getService (IMPORT_SERVICE);
//        } catch (AuthenticationException ae) {}
//Fin añadido

          try {
            oilExportService = (OILExportService) minervaSession.getService ("oil");
            } catch (AuthenticationException ae) {}

            try {
              damloilExportService = (DAMLOILExportService) minervaSession.getService("damloil");
              } catch (AuthenticationException ae) {}

              try {
                prologExportService = (PrologExportService) minervaSession.getService("prolog");
                } catch (AuthenticationException ae) {}

                try {
                  XCARINExportService = (XCARINExportService) minervaSession.getService("xcarin");
                  } catch (AuthenticationException ae) {}

// Aunque un usuario tenga acceso al OntoCleanService, no se activa de primeras, porque así se controla
//más fácilmente la creación del instance set de una ontología cuando se activa esta opción en el editor
//de ontologías de WebODE
//        try {
//           ontoCleanService = (OntoCleanService) minervaSession.getService(ONTOCLEAN_SERVICE);
//        } catch (AuthenticationException ae) {}


                  return new Object[] { odeService, exportService, minervaSession,axiomService,inferenceService, oilExportService,damloilExportService, prologExportService,
                    XCARINExportService,ontoCleanService};
    } catch (AuthenticationException e) {
      return null;
    } catch (Exception e) {
      getServletContext().log (new Date() + ": an error ocurred contacting Minerva Server: " + e);
      throw new ServletException ("an error ocurred contacting Minerva Server: " + e.getMessage());
    }
  }

  private boolean isUserInGuestGroup(ODEService odeService) {
    boolean isGuestGroup=false;
    try {
      String[] groups=odeService.getUserGroups();
      for(int i=0; !isGuestGroup && i<groups.length; i++)
        isGuestGroup=groups[i].equals("guests");
    }
    catch (Exception e) {
    }
    return isGuestGroup;
  }

  /**
   * Defines the temporaly authorized users.
   */
  private Object[] _isSpecialGuest (MinervaSession minervaSession, ODEService odeService)
      throws ServletException
  {
    try{

      if(!isUserInGuestGroup(odeService))
        return null;

      ExportService exportService = null;
      OILExportService oilExportService = null;
      DAMLOILExportService damloilExportService = null;
      PrologExportService prologExportService = null;
      XCARINExportService XCARINExportService = null;
      AxiomService axiomService=null;
      InferenceService inferenceService=null;
      OntoCleanService ontoCleanService=null;

      try {
        axiomService=(AxiomService) minervaSession.getService(AXIOM_SERVICE);
        } catch (AuthenticationException ae) {;}

        try {
          inferenceService=(InferenceService) minervaSession.getService(INFERENCE_SERVICE);
          } catch (AuthenticationException ae) {}

//Añadido por Oscar Corcho: 26/02/2002
//        try {
//        exportService = (ExportService) minervaSession.getService (EXPORT_SERVICE);
//        } catch (AuthenticationException ae) {}
//
//        try {
//        importService = (ImportService) minervaSession.getService (IMPORT_SERVICE);
//        } catch (AuthenticationException ae) {}
//Fin añadido

          try {
            oilExportService = (OILExportService) minervaSession.getService ("oil");
            } catch (AuthenticationException ae) {}

            try {
              damloilExportService = (DAMLOILExportService) minervaSession.getService("damloil");
              } catch (AuthenticationException ae) {}

              try {
                prologExportService = (PrologExportService) minervaSession.getService("prolog");
                } catch (AuthenticationException ae) {}

                try {
                  XCARINExportService = (XCARINExportService) minervaSession.getService("xcarin");
                  } catch (AuthenticationException ae) {}

// Aunque un usuario tenga acceso al OntoCleanService, no se activa de primeras, porque así se controla
//más fácilmente la creación del instance set de una ontología cuando se activa esta opción en el editor
//de ontologías de WebODE
//        try {
//           ontoCleanService = (OntoCleanService) minervaSession.getService(ONTOCLEAN_SERVICE);
//        } catch (AuthenticationException ae) {}


                  return new Object[] { odeService, exportService, minervaSession,axiomService,inferenceService, oilExportService,damloilExportService, prologExportService,
                    XCARINExportService,ontoCleanService};
    } catch (AuthenticationException e) {
      return null;
    } catch (Exception e) {
      getServletContext().log (new Date() + ": an error ocurred contacting Minerva Server: " + e);
      throw new ServletException ("an error ocurred contacting Minerva Server: " + e.getMessage());
    }
  }


  /**
   * Cleans current session.
   */
  private void _cleanSession (HttpSession session)
  {
    // A session already exists.  Destroy its data and add new information.
    if (session != null && session.getAttribute (USER) != null) {
      getServletContext().log (new Date() + ": cleaning previous user's session (" + session.getId() + ".)");
      try {
        String param;
        Object val=null;
        for(Enumeration enum=session.getAttributeNames(); enum.hasMoreElements();) {
          param=enum.nextElement().toString();
          try {
            val=session.getAttribute(param);
            if(val instanceof MinervaService && ((MinervaService)val).isStatefull())
              ((MinervaService)val).disconnect();
          }
          catch(Exception e) {
            System.out.println("param : " + param.getClass());
            e.printStackTrace(System.out);
          }
        }
/*        MinervaService ms = (MinervaService) session.getAttribute (MINERVA_SESSION);
        session.removeValue (MINERVA_SESSION);
        if (ms != null)
            ms.disconnect();

        ms = (MinervaService) session.getAttribute (ODE_SERVICE);
        session.removeValue (ODE_SERVICE);
        if (ms != null)
            ms.disconnect();

        ms = (MinervaService) session.getAttribute (IMPORT_SERVICE);
        session.removeValue (IMPORT_SERVICE);
        if (ms != null)
            ms.disconnect();

        ms = (MinervaService) session.getAttribute (EXPORT_SERVICE);
        session.removeValue (EXPORT_SERVICE);
        if (ms != null)
            ms.disconnect();

        ms = (MinervaService) session.getAttribute ("oil");
        session.removeValue ("oil");
        if (ms != null)
            ms.disconnect();

        ms = (MinervaService) session.getAttribute ("damloil");
        session.removeValue ("damloil");
        if (ms != null)
            ms.disconnect();

        ms = (MinervaService) session.getAttribute ("prolog");
        session.removeValue ("prolog");
        if (ms != null)
            ms.disconnect();

        ms = (MinervaService) session.getAttribute ("xcarin");
        session.removeValue ("xcarin");
        if (ms != null)
            ms.disconnect();

         ms = (MinervaService) session.getAttribute (AXIOM_SERVICE);
        session.removeValue (AXIOM_SERVICE);
        if (ms != null)
            ms.disconnect();

        ms = (MinervaService) session.getAttribute (INFERENCE_SERVICE);
        session.removeValue (INFERENCE_SERVICE);
        if (ms != null)
            ms.disconnect();

        ms = (MinervaService) session.getAttribute (ONTOCLEAN_SERVICE);
        session.removeValue (ONTOCLEAN_SERVICE);
        if (ms != null)
            ms.disconnect();
*/
        session.invalidate();
      } catch (Exception e) {
        getServletContext().log (new Date() + ": an error ocurred while redefining user's session: " + e);
      }
    }
  }

  /**
   * When destroyed, stop sweeper thread.
   */
  public void destroy()
  {
    sweeperThread.shutdown();
  }
}