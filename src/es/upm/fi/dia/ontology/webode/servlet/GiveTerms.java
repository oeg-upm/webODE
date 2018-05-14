
package es.upm.fi.dia.ontology.webode.servlet;
import java.io.*;
import java.util.*;
import javax.swing.tree.*;
import javax.servlet.*;
import javax.servlet.http.*;
import es.upm.fi.dia.ontology.webode.service.*;

import es.upm.fi.dia.ontology.webode.util.*;
/**
 *
 *
 * The Servlet.  Basically, this reads in whatever's sent to it then sends
 * back an appropriate message.
 *
 *
 **/

public class GiveTerms extends HttpServlet implements WebODEConstants {
  /**
   * Method to handle GET queries.
   **/

  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
    PrintWriter out = resp.getWriter();
    out.println("Error: this servlet does not support the GET method!");
    out.close();
  }

//  Servlet server-side code to send a serialized
//  vector of  objects to an applet.
//
//

  public void sendSubclassList(HttpServletResponse response, Vector SubclassVector)
  {
    ObjectOutputStream outputToApplet;
    ObjectOutputStream outputToApplet2;

    try
    {
      outputToApplet = new ObjectOutputStream(response.getOutputStream());
      outputToApplet.writeObject(SubclassVector);
      outputToApplet.flush();
      outputToApplet.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }



  /**
   * Method to handle POST queries.
   **/
  public void doPost(HttpServletRequest req, HttpServletResponse resp)   throws ServletException, IOException {

// Read in the message from the servlet

    ObjectInputStream inputFromApplet = null;
    ConceptData TheDatosServlet = null;

    // Check if there is a valid session available.
    HttpSession session = req.getSession(false);
    if (session == null) {

    }
    else{
      ODEService odeService = (ODEService) session.getAttribute (ODE_SERVICE);
      try{

        // get an input stream from the applet
        inputFromApplet = new ObjectInputStream(req.getInputStream());
        // read the serialized student data from applet
        TheDatosServlet = (ConceptData) inputFromApplet.readObject();

        inputFromApplet.close();


        // METO EL CODIGO NECESARIO
        // Vector Grande = new Vector();
        String Concepto = TheDatosServlet.getConcept();
        String Ontologia = TheDatosServlet.getOntology();



        Vector Subclases = new Vector();
        Term[] listas =  odeService.getChildConcepts (Ontologia, Concepto);



        for (int i=0;listas != null &&  i<listas.length;i++){


            Subclases.addElement(listas[i].term);

            // NUEVO PARA DISTINGUIR LAS QUE TIENEN SUBCLASES DE LAS QUE NO


            int vi = 0;
            String elto="";
            Term[] listas2 =  odeService.getChildConcepts (Ontologia, listas[i].term);



              if  (listas2 != null){  // SI el elto es una superclase

                 Subclases.addElement(listas2[0].term);
              }
             else
                 Subclases.addElement("b");

            } // end for






// FIN CODIGO NECESARIO

        sendSubclassList(resp,Subclases);

      }  // end try

      catch (Exception e) {

        //error (res, e.getMessage());

      }
    }// end else
  } // end post
}  // end class