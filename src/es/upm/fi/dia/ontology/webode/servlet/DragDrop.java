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

public class DragDrop extends HttpServlet implements WebODEConstants {


  /**
   * Method to handle GET queries.
   **/

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    out.println("Error: this servlet does not support the GET method!");
    out.close();
  }




  public void sendSubclassList(HttpServletResponse response, Vector SubclassVector)
  {
    ObjectOutputStream outputToApplet;


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
  public void doPost (HttpServletRequest req,
                      HttpServletResponse res)

  throws ServletException, IOException {

    String CURRENT_ONTOLOGY = "current_ontology";
    //       res.setContentType ("text/html");
    HttpSession session = req.getSession(false);

    ObjectInputStream inputFromApplet = null;
    NodeData TheDatosServlet = null;

    if (session == null) {

    }
    else{
      ODEService odeService = (ODEService) session.getAttribute (ODE_SERVICE);
      Vector anteriores = new Vector();
      try{

        // get an input stream from the applet
        inputFromApplet = new ObjectInputStream(req.getInputStream());
        // read the serialized student data from applet
        TheDatosServlet = (NodeData) inputFromApplet.readObject();

        inputFromApplet.close();


        String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);
        Node Parent_ant = TheDatosServlet.getParent_ant();
        Node Parent_act = TheDatosServlet.getParent_act();
        Node Child = TheDatosServlet.getChild();

        NodeInfo Child2 = (NodeInfo)Child.getUserObject();
        String Child3 = Child2.getName();
        NodeInfo Parent_ant2 = (NodeInfo)Parent_ant.getUserObject();
        String Parent_ant3 = Parent_ant2.getName();

        NodeInfo Parent_act2 = (NodeInfo)Parent_act.getUserObject();
        String Parent_act3 = Parent_act2.getName();


       boolean pertenece = false;
       Group[] agroups = odeService.getGroups (oname);



        for (int i = 0; agroups != null && i < agroups.length; i++) {
            String[] conceptos = agroups[i].concepts;
            pertenece = false;
            int j = 0;
            while((!pertenece) && j < conceptos.length){
               pertenece = Child3.equals(conceptos[j]);

               if(pertenece){
                  // RECORRO DISJOINT
                 TermRelation[] art2 = odeService.getTermRelations (oname,
				TermRelation.DISJOINT);
                 boolean sigo = true;
                 for (int k = 0 ; art2 != null && k < art2.length && sigo; k++) {

                     if (art2[k].origin.equals(agroups[i].name)){
                         anteriores.add("MM");
                         sigo = false;

                     } // end if
                 }
                 if (sigo){
                   TermRelation[] art1 = odeService.getTermRelations (oname, TermRelation.EXHAUSTIVE);
                   for (int p = 0 ; art1 != null && p < art1.length && sigo; p++) {

                    if (art1[p].origin.equals(agroups[i].name)){
                        anteriores.add("MJ");
                        sigo = false;
                     } // end if
                 } // end for
                 if (sigo )
                   pertenece = false;

                 } // END if(sigo)

          } // end while
           j++;
        }
        }
        if(pertenece){


           sendSubclassList(res,anteriores);
        }
        else{
        boolean hay = false;

         TermRelation[] Nosubclases =  odeService.getTermRelations (oname, "Not-Subclass-of");

          hay = false;
         int z = 0;

         while((!hay) && Nosubclases != null && z < Nosubclases.length){

                 hay = (Child3.equals(Nosubclases[z].origin)) && (Parent_act3.equals(Nosubclases[z].destination));
                 z++;
            }

         if(hay){

             anteriores.add("MI");
             sendSubclassList(res,anteriores);
         }
        else{

        if (oname.equals(Parent_act3)){
            boolean sigo = true;
            Term[] listas3 =odeService.getRootConcepts (oname,true);
            for (int i = 0; listas3 != null && i < listas3.length;  i++) {

            }
           for (int i = 0; listas3 != null && i < listas3.length && sigo; i++) {
              if (listas3[i].term.equals(Child3)){
                anteriores.add("C");
                odeService.insertTermRelation(new TermRelation (oname, null,
                                     "Subclass-of",  Child3, Parent_ant3, -1));

                sigo = false;
               }

             }

             odeService.removeTermRelation (new TermRelation (oname, null,
            "Subclass-of",Child3, Parent_ant3, -1));
             TermRelation[] terminos  = odeService.getTermRelations (oname,
				true);


             for (int s = 0; terminos != null && s < terminos.length && sigo; s++) {
                if(terminos[s].origin.equals(Child3)){
                   anteriores.add("C");
                   odeService.insertTermRelation(new TermRelation (oname, null,
                                        "Subclass-of",  Child3, Parent_ant3, -1));
                   sigo = false;

                 }
              }
                 if(sigo)
                   anteriores.add("B");

          sendSubclassList(res,anteriores);

        }
        else{
          try{

          odeService.removeTermRelation (new TermRelation (oname, null,
            "Subclass-of",Child3, Parent_ant3, -1));
           Term term1 = odeService.getTerm (oname, Child3);
            Term term2 = odeService.getTerm (oname, Parent_act3);
          if(odeService.isSubclassOf (oname, term1, term2)){

              odeService.insertTermRelation(new TermRelation (oname, null,
                                        "Subclass-of",  Child3, Parent_ant3, -1));
             anteriores.add("C");
          }
          else{


           odeService.insertTermRelation(new TermRelation (oname, null,
                                        "Subclass-of",  Child3, Parent_act3, -1));


           anteriores.add("B");
         }
           sendSubclassList(res,anteriores);
          } catch(Exception ex) {

            anteriores.add("M");
            //anteriores.add(ex);
            sendSubclassList(res,anteriores);
          }
        }

        }
        }

      }  // end try





      catch (Exception e) {
        //System.out.println ("Error: " + e);


      }
    }// end else

  } // end post

}