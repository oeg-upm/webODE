package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.webode.OntoClean.*;

import java.util.*;

/**
 * Inserts a new relation.
 *
 * @version 0.5
 */
public class InsertInheritanceRelationServlet extends BaseServlet
{
  public void doPost (HttpServletRequest req,
                      HttpServletResponse res,
                      HttpSession session)
      throws ServletException, IOException
  {
    // Get parameters
    String tname        = req.getParameter (TERM_NAME);
    String pname        = req.getParameter (PARENT_NAME);
    String foo          = req.getParameter ("delete");
    boolean bDelete     = foo != null && foo.equals ("yes");
    String relation     =  req.getParameter (RELATION_NAME);
    boolean bAdhoc       = req.getParameter ("adhoc") != null && req.getParameter ("adhoc").equals("yes");
    String properties    = req.getParameter ("relation_properties");
    String relationaux= "";
    Properties prop = (Properties)session.getAttribute("prop");
    int maxCardinality = -1;
    try {
      maxCardinality = Integer.parseInt (req.getParameter (MAX_CARDINALITY));
      } catch (Exception e) {}

      // These things are in the session.
      String oname        = (String) session.getAttribute (CURRENT_ONTOLOGY);

      //NUEVO PARA MULTILINGUALIDAD
      if (relation.equals("Subclass-of")){

        relationaux="Subclassof";


      }else{

        if (relation.equals("Not-Subclass-of")){

          relationaux="NotSubclassof";
        }   else{

          if(relation.equals("Disjoint")){

            relationaux="Disjoint";
          } else{

            if (relation.equals("Adhoc")){

              relationaux="Adhoc";
            }   else{

              if (relation.equals("Exhaustive")){
                relationaux="Exhaustive";
              }
              else{

                if (relation.equals("Transitive-Part-of")){
                  relationaux="Transitive-Part-of";
                }   else{

                  if (relation.equals("Intransitive-Part-of")){
                    relationaux="IntransitivePartof";
      }}}}}}}

      // FIN NUEVO PARA MULTILINGUALIDAD

      if (oname == null || oname.trim().equals("")) {
        error (res, prop.getProperty("Theontologynameisamandatoryparameter."));
        return;
      }
      if (tname == null || tname.trim().equals("") ||
          pname == null || pname.trim().equals("") ||
          relation == null || relation.trim().equals("")) {
        error (res, prop.getProperty("Theterm,parentandrelationnamesaremandatoryparameters."));
        return;
      }

      try {
        ODEService odeService = (ODEService) session.getAttribute (ODE_SERVICE);
        String user = (String) session.getAttribute (USER);

        if (bDelete) {
          odeService.removeTermRelation (new TermRelation (oname, null,
              relation,
              tname, pname, maxCardinality));

        }
        else {
          String[] aprops = null;
          // Extract properties
          if (properties != null) {
            Vector v = new Vector();
            StringTokenizer strToken = new StringTokenizer (properties, ",");
            while (strToken.hasMoreElements())
              v.addElement (strToken.nextToken().trim());

            aprops = new String[v.size()];
            v.copyInto (aprops);
          }

          // all right.  Let's insert this data into the database
          if (aprops == null) {
            odeService.insertTermRelation
            (new TermRelation (oname, null,
            relation,
            tname,
            pname, maxCardinality));
          }
          else {
            odeService.insertTermRelation
            (new TermRelation (oname, null,
            relation,
            tname,
            pname, maxCardinality, aprops));
          }
        }

        PrintWriter pw = res.getWriter ();
  /*
        // NUEVO PARA MULTILINGUALIDAD DANI
        pw.println("<html>");
        pw.println("  <head>");
        pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=../jsp/webode/about.jsp\"> ");
        pw.println("  </head>");
        // FIN NUEVO */
        headeraux(pw);
        setRedirectParam (bAdhoc & !bDelete ? tname : pname);
        setRedirectParam (1, bAdhoc ? TermRelation.ADHOC : relation);
        //relation= relation + " ";


        //MODIFICADO POR OSCAR CORCHO (25Oct2002)
        //OntoCleanServiceImp oc = new OntoCleanServiceImp();



        // MODIFICADO PARA AÑADIR MULTILINGUALIDAD



        OntoCleanService oc = (OntoCleanService) session.getAttribute (ONTOCLEAN_SERVICE);
        if (bAdhoc)
          relationaux= "Adhoc";

        if (oc==null) {


          if (bDelete) body (pw, prop.getProperty("Relation")+ "<i>" + prop.getProperty(relationaux) + "</i> "+prop.getProperty("removedsuccessfully."));

          else
            body (pw, prop.getProperty("Relation")+ "<i>" + prop.getProperty(relationaux) + "</i> "+prop.getProperty("addedsuccessfully."));
        } else {
          String evs =oc.getEvaluationMode();

          if (bDelete) {
            if(evs.equals("Synchronous") && relation.equals (TermRelation.SUBCLASS_OF)) {
              ErrorOntoClean[] errors=oc.evaluationOntoClean(oname,user," ");
              StringWriter doc = new StringWriter();
              doc.write(" <center> <h1 class=\"title\">" + evs + " Evaluation OntoClean for "+oname+" <br> " +errors.length +" ERRORS FOUND: </h1> <hr>");
              if(errors.length == 0) doc.write("<b>No Errors <br>");
              else {
                for(int g=0;g<errors.length;g++) {
                  String aux=errors[g].convertStringAux();
                  doc.write(aux);
                }
              }
              doc.write("</center>");
              body (pw, prop.getProperty("Relation")+ "<i> " + prop.getProperty(relationaux) + " </i>"+prop.getProperty("removedsuccessfully."));
              body (pw, doc.getBuffer().substring(0,doc.getBuffer().length()));
            }
            else   body (pw, prop.getProperty("Relation")+ "<i> " + prop.getProperty(relationaux) + " </i>"+prop.getProperty("removedsuccessfully."));
          } // (!bDelete)
          else {
            if(evs.equals("Synchronous") && relation.equals (TermRelation.SUBCLASS_OF)) {
              ErrorOntoClean[] errors=oc.evaluationOntoClean(oname,user," ");
              StringWriter doc = new StringWriter();
              doc.write(" <center> <h1 class=\"title\">" + evs + " Evaluation OntoClean for "+oname+" <br> " +errors.length +" ERRORS FOUND: </h1> <hr>");
              if(errors.length == 0) doc.write("<b>No Errors <br>");
              else {
                for(int g=0;g<errors.length;g++) {
                  String aux=errors[g].convertStringAux();
                  doc.write(aux);
                }
              }
              doc.write("</center>");

              body (pw, prop.getProperty("Relacion")+ "<i>" + relationaux + "</i> "+prop.getProperty("addedsuccessfully."));
              body (pw, doc.getBuffer().substring(0,doc.getBuffer().length()));
            }
            else  body (pw, prop.getProperty("Relacion")+ "<i>" + prop.getProperty(relationaux) + " </i>"+prop.getProperty("addedsuccessfully."));
          }
        }

        sendAdditionEvent (pw, TermTypes.CONCEPT, tname);
        pw.println ("<hr><small>" + VERSION + "</small></body></html>");
        pw.flush();
      } catch (Exception e) {
        error (res, e.getMessage(), e);
      }
  }
}
