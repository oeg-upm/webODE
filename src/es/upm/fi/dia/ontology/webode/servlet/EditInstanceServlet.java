package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Servlet responsible for update instance attributes values.
 *
 * @author  Jose F. Cebrian Benito
 * @version 0.1
 */
public class EditInstanceServlet extends BaseServlet {
  class RelationInstanceElement {
    public String termRelation;
    public String originInstance;
    public String destinationInstance;
    public String originConcept;
    public String destinationConcept;
    public TermRelationInstance tri=null;

    RelationInstanceElement(TermRelationInstance tri) {
      termRelation=tri.termRelation.name;
      originConcept=tri.termRelation.origin;
      destinationConcept=tri.termRelation.destination;
      originInstance=tri.origin;
      destinationInstance=tri.destination;
      this.tri=tri;
    }

    RelationInstanceElement(String termRelation, String originInstance, String destinationInstance, String originConcept, String destinationConcept) {
      this.termRelation=termRelation;
      this.originConcept=originConcept;
      this.destinationConcept=destinationConcept;
      this.originInstance=originInstance;
      this.destinationInstance=destinationInstance;
      this.tri=null;
    }

    public boolean equals(Object obj) {
      RelationInstanceElement rie=(RelationInstanceElement)obj;
      return this.termRelation==rie.termRelation &&
             this.originConcept==rie.originConcept &&
             this.destinationConcept==rie.destinationConcept &&
             this.originInstance==rie.originInstance &&
             this.destinationInstance==rie.destinationInstance;
    }
  }

  private String[] getDataArray(String ai_stream) {
    Vector v=new Vector();
    String[] dataarray=null;

    int i=0,j=0,k;
    while((j=ai_stream.indexOf("~|",i))>=0) {
      v.addElement(ai_stream.substring(i,j));
      i=j+2;
    }
    if(v.size()>0) {
      dataarray=new String[v.size()];
      for(i=0; i<dataarray.length; i++) {
//        try {
//          System.out.println((String)v.elementAt(i));
//          dataarray[i]=java.net.URLDecoder.decode((String)v.elementAt(i),"UTF-8");
//        }
//        catch(java.io.UnsupportedEncodingException e) {
          dataarray[i]=(String)v.elementAt(i);
//        }
        while((k=dataarray[i].indexOf("[EOL]"))>0)
          dataarray[i]=dataarray[i].substring(0,k) + "\n" + dataarray[i].substring(k+5);
      }
    }
    return dataarray;
  }




  public void doGet (HttpServletRequest req,
                     HttpServletResponse res,
                     HttpSession session)
      throws ServletException, IOException
  {
    doPost(req,res,session);
  }
  public void doPost (HttpServletRequest req,
                      HttpServletResponse res,
                      HttpSession session)
      throws ServletException, IOException
  {


//    	System.out.println("**********************Ha entrado en el Servlet**********************");
    // Check if there is a valid session available.
    HttpSession sessionLocal = req.getSession(false);
    if (sessionLocal == null) {
      res.sendRedirect ("../../login_error.html");
      return;
    }

    // Get parameters
    Properties prop = (Properties)session.getAttribute("prop");
    String ont = req.getParameter("ontologyName");
    String numConceptsString = req.getParameter("numConcepts");
    String instanceName = req.getParameter("instanceName");
    String instanceSetName = req.getParameter("instanceSetName");

    int numConceptsINT;
    if (!(numConceptsString.equals("null"))){
      numConceptsINT = Integer.parseInt(numConceptsString);
    }else{
      numConceptsINT = 0;
    }/*Else*/

//      System.out.println("Numero de conceptos: " + numConceptsINT);

    //Check if there is a valid session available.
    ODEService odeLocal = (ODEService) sessionLocal.getAttribute ("ode");

    Enumeration params = req.getParameterNames();
    Vector paramsVector = new Vector(5,3);

    String param;
    while(params.hasMoreElements()){
      param = (String) params.nextElement();
      if (param.indexOf("concept")!=(-1)){
//			System.out.println("Concepto : " + req.getParameter(param));
      }
      if (param.indexOf("instanceAttribute")!=(-1)){
//			System.out.println("Parametro : " + req.getParameter(param));
      }
      if (( param.indexOf("instValues")!=(-1) ) ||
          ( param.indexOf("true")!=(-1) ) ||
          ( param.indexOf("false")!=(-1) ) ||
          ( param.indexOf("valueToRemove")!=(-1) ) ||
          ( param.indexOf("valueBooleanToRemove")!=(-1) )){
        paramsVector.add(param);
      }/*End if*/

    }/*End While*/

/* Inicio Codigo de angelito */
/*	String key_name;
 String[] vals;
 for(Enumeration enum=req.getParameterNames(); enum.hasMoreElements(); ){
  key_name=(String)enum.nextElement();
  vals=req.getParameterValues(key_name);
  for(int it=0; it<vals.length; it++)
   System.out.print(vals[it]);
 }*/

/* Fin Codigo de angelito */

//	System.out.println("El vector con todos los parametros tiene: " + paramsVector);

    int varTrue,varFalse,varIV,varBR;

//  int numInserts=0;
  if (numConceptsINT > 0){
    for (int numConcept=0; numConcept < numConceptsINT ; numConcept++){
      String numInstAttsString = req.getParameter("numInstAttsOfConcept" + numConcept);
      int numInstAttsINT;
      if (!(numInstAttsString.equals("null"))){
        numInstAttsINT = Integer.parseInt(numInstAttsString);
      }else{
        numInstAttsINT = 0;
      }/*End else*/
      String[] bValues=new String[numInstAttsINT];

      //System.out.println("     Numero de atributos de instancia: " + numInstAttsINT);
      if (numInstAttsINT > 0){
        for (int numInstAtts=0; numInstAtts< numInstAttsINT; numInstAtts++){
          varTrue = paramsVector.indexOf("true" + numInstAtts + numConcept);
          varFalse = paramsVector.indexOf("false" + numInstAtts + numConcept);
          varIV = paramsVector.indexOf("instValues" + numInstAtts + numConcept);
          varBR = paramsVector.indexOf("valueBooleanToRemove" + numInstAtts + numConcept);
          bValues[numInstAtts] =  req.getParameter("valueBooleanToRemove" + numInstAtts + numConcept);
      /*varTA = paramsVector.indexOf("topic_add" + numInstAtts + numConcept);*/
          if (varTrue != (-1)){
            //String bValue;
            //bValue = req.getParameter("valueBooleanToRemove" + numInstAtts + numConcept);
            //System.out.println("        El valor de bValue en la rama TRUE numero " + numInstAtts+ " es: " + bValues[numInstAtts]);

            try{ /* adding */
//			 			System.out.println("***INSERTO true en el InsAtt " + req.getParameter("instanceAttribute" + numInstAtts + numConcept) + " del concepto " + req.getParameter("concept" + numConcept) + " de la ontologia " + ont + "instanceSetName: " + instanceSetName + "instanceName: " + instanceName + " y el concepto: " + req.getParameter("concept" + numConcept));
              odeLocal.removeValueFromInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept), "true" );
              odeLocal.removeValueFromInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept), "false" );
              //odeLocal.removeValueFromInstanceAttribute(ont, req.getParameter("instanceAttribute" + numInstAtts + numConcept) , req.getParameter("concept" + numConcept) , "true");
              //odeLocal.removeValueFromInstanceAttribute(ont, req.getParameter("instanceAttribute" + numInstAtts + numConcept) , req.getParameter("concept" + numConcept) , "false");

              odeLocal.addValueToInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept),"true");


              //odeLocal.addValueToInstanceAttribute(ont, req.getParameter("instanceAttribute" + numInstAtts + numConcept), req.getParameter("concept" + numConcept), "true");




            } catch (Exception e) {
            }
            //System.out.println("Meteria esto: True");
//            numInserts++;
          } else if ( varFalse != (-1)){
            //String bValue;
            //bValue = req.getParameter("valueBooleanToRemove" + numInstAtts + numConcept);
            //System.out.println("         El valor de bValue en la rama FALSE numero " +numInstAtts+ "es: " + bValues[numInstAtts]);
//			 			System.out.println(numInstAtts + " : Entra por FALSE");
            //if (bValues[numInstAtts]!= "null" || bValues[numInstAtts]!= ""){
            //	System.out.println("ENTRA EN REMOVING******************");
            //	try{/* Removing */
            //	System.out.println("***ELIMINO el valor " + bValues[numInstAtts] + " en el InsAtt " + req.getParameter("instanceAttribute" + numInstAtts + numConcept) + " del concepto " + req.getParameter("concept" + numConcept) + " perteneciente a la ontologia " + ont);
            //	odeLocal.removeValueFromInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept),bValues[numInstAtts] );
            //	} catch (Exception e) {
            //	}
            //}/*End if*/
            try{
//			 			System.out.println("***INSERTO el valor FALSE en el InsAtt " + req.getParameter("instanceAttribute" + numInstAtts + numConcept) + " del concepto " + req.getParameter("concept" + numConcept) + " de la ontologia " + ont);

              odeLocal.removeValueFromInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept), "true" );
              odeLocal.removeValueFromInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept), "false" );
              //////odeLocal.removeValueFromInstanceAttribute(ont, req.getParameter("instanceAttribute" + numInstAtts + numConcept) , req.getParameter("concept" + numConcept) , "true");
              //////odeLocal.removeValueFromInstanceAttribute(ont, req.getParameter("instanceAttribute" + numInstAtts + numConcept) , req.getParameter("concept" + numConcept) , "false");

              odeLocal.addValueToInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept), "false" );

              ///odeLocal.addValueToInstanceAttribute(ont, req.getParameter("instanceAttribute" + numInstAtts + numConcept), req.getParameter("concept" + numConcept), "false" );

            } catch (Exception e) {
            }
            //System.out.println("Meteria esto:  False");
//            numInserts++;
          } else if (varIV != (-1)){
//						System.out.println(numInstAtts + " : Entra por VARIABLE NO BOOLEANA");
            String values2DeleteChain = req.getParameter("valueToRemove" + numInstAtts + numConcept);
            //if (values2DeleteChain!=null){
            //if (values2DeleteChain.length()!=0){
            //	System.out.println("values2DeleteChain: " + values2DeleteChain/*[iter]*/);
            // }else{
            //	System.out.println("values2DeleteChain esta vacio");
            // }/*End else*/
            //}/*End if*/
            String[] values = req.getParameterValues("instValues" + numInstAtts + numConcept);

            if (values2DeleteChain.length() != 0){

              String[] values2Delete = getDataArray(values2DeleteChain/*[0]*/);

              //System.out.println("values2Delete: " +  values2Delete[0]);
              for (int a=0; a<values2Delete.length;a++){
                try{
//							System.out.println("***ELIMINO el valor " + values2Delete[a] + "  en el InsAtt " + req.getParameter("instanceAttribute" + numInstAtts + numConcept) + " del concepto " + req.getParameter("concept" + numConcept) + " de la ontologia " + ont);
                  odeLocal.removeValueFromInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept),values2Delete[a] );
                } catch (Exception e) {
                }
              }/*end for*/
            }/*End if*/
            String[] des = getDataArray(values[0]);
            if (des!=null){
              if (!(des[0].equals("Mandatory"))){
                for (int b=0; b<des.length;b++){
                  /********************************/
                  try{
//			 				System.out.println("***INSERTO el valor " + des[b] + "  en el InsAtt " + req.getParameter("instanceAttribute" + numInstAtts + numConcept) + " del concepto " + req.getParameter("concept" + numConcept) + " de la ontologia " + ont);

                    odeLocal.addValueToInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept), des[b]);
                  } catch (Exception e) {
                  }

                  /*********************************/
                  //System.out.println("Meteria esto: " + des[b]);

//                  numInserts++;
                }/*End for*/
              }/*End if*/
            }/*End if*/

          }else if (varBR!=(-1)){
//						System.out.println(numInstAtts + " : Entra por BOOLEAN TO REMOVE");
            if (bValues[numInstAtts]!= "null" || bValues[numInstAtts]!= ""){
//			 				System.out.println("ENTRA EN REMOVING DE BOOLEANOS******************");
              try{/* Removing */
              if (bValues[numInstAtts].equals("true") ){

                String valueT="";
//			 					System.out.println("--VALUET: " + valueT.valueOf(true));
//			 					System.out.println("***ELIMINO el valor " + valueT.valueOf(true) + " en el InsAtt " + req.getParameter("instanceAttribute" + numInstAtts + numConcept) + " del concepto " + req.getParameter("concept" + numConcept) + " de la ontologia " + ont);
                //System.out.println("VALUE" + (String)true);

                odeLocal.removeValueFromInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept),valueT.valueOf(true));
                //odeLocal.removeValueFromInstanceAttribute(ont, req.getParameter("instanceAttribute" + numInstAtts + numConcept) , req.getParameter("concept" + numConcept) , valueT.valueOf(true));

                //HashMap elements2 = odeLocal.getInstanceValues (ont, instanceSetName, instanceName);

                //if (elements1.equals(elements2)){
                //	System.out.println("++++++++++SON IGUALES");
                //}else{
                //	System.out.println("++++++++++SON DISTINTAS");
                //}/*End else*/
              }else if (bValues[numInstAtts].equals("false") ){

                String valueF="";
//                             					System.out.println("--VALUEF: " + valueF.valueOf(false));
//                             					System.out.println("***ELIMINO el valor " + valueF.valueOf(false) + " en el InsAtt " + req.getParameter("instanceAttribute" + numInstAtts + numConcept) + " del concepto " + req.getParameter("concept" + numConcept) + " de la ontologia " + ont);

                ////////HashMap elements1BIS = odeLocal.getInstanceValues (ont, instanceSetName, instanceName);

                odeLocal.removeValueFromInstance(ont , instanceSetName, instanceName, req.getParameter("concept" + numConcept), req.getParameter("instanceAttribute" + numInstAtts + numConcept),valueF.valueOf(false));
                //odeLocal.removeValueFromInstanceAttribute(ont, req.getParameter("instanceAttribute" + numInstAtts + numConcept) , req.getParameter("concept" + numConcept) , valueF.valueOf(false));

                ///////HashMap elements2BIS = odeLocal.getInstanceValues (ont, instanceSetName, instanceName);
                //if (elements1BIS.equals(elements2BIS)){
                //	System.out.println("++++++++++SON IGUALES");
                //}else{
                //	System.out.println("++++++++++SON DISTINTAS");
                //}/*End else*/

              }/*End else*/
              } catch (Exception e) {
              }
            }/*End if*/
          }/*End if*/
        }/*End for*/
      }else{/*numInstAttsINT == 0*/
//				System.out.println("numInstAttsINT == 0");
      }/*End else*/
    }/*End for*/
  }else{ /*numConcept == 0*/
//		System.out.println("numConcept == 0");
  }/*End else*/


// Angelito -->
  try {
    String rel_name,rel_origin,rel_destination;
    String[] inst_dests;
    TermRelationInstance[] tris=odeLocal.getRelationInstancesFromInstance(ont,instanceSetName,instanceName);

    HashSet ir_toDelete=new HashSet();
    HashSet ir_submitted=new HashSet();

    for(int i=0; tris!=null && i<tris.length; i++)
      ir_toDelete.add(new RelationInstanceElement(tris[i]));

    int relDef=0;
    while((rel_name=req.getParameter("relDef_" + (++relDef) + "_name"))!=null) {
      rel_origin=req.getParameter("relDef_" + relDef + "_origin");
      rel_destination=req.getParameter("relDef_" + relDef + "_destination");
      inst_dests=getDataArray(req.getParameter("reli_" + relDef));
      for(int i=0; inst_dests!=null && i<inst_dests.length; i++) {
        ir_submitted.add(new RelationInstanceElement(rel_name,instanceName,inst_dests[i],rel_origin,rel_destination));
      }
    }
    HashSet ir_toStore=new HashSet(ir_submitted);
    ir_toStore.removeAll(ir_toDelete);
    ir_toDelete.removeAll(ir_submitted);

    for(Iterator it=ir_toDelete.iterator(); it.hasNext(); ) {
      odeLocal.removeTerm(ont,((RelationInstanceElement)it.next()).tri.name,instanceSetName);
    }

    RelationInstanceElement rie;
    long milis=System.currentTimeMillis();
    for(Iterator it=ir_toStore.iterator(); it.hasNext(); ) {
      rie=(RelationInstanceElement)it.next();
      odeLocal.insertRelationInstance(ont,new TermRelationInstance("rel-ins-" + rie.termRelation + "-"+(milis++),new TermRelation(ont,null,rie.termRelation,rie.originConcept,rie.destinationConcept),instanceSetName,"",rie.originInstance,rie.destinationInstance));
    }
    this.setRedirectParam(instanceName);
  }
// Angelito <--
  catch(Exception e) {
    e.printStackTrace(System.out);
   // error (res, "Error adding values to attributes and relations : " + e);
     error (res, prop.getProperty("Erroraddingvaluestoattributesandrelations:") + e, e);
    return;
  }

  PrintWriter pw = res.getWriter ();
  header(pw);

  //setRedirectParam (termName);
  body (pw, prop.getProperty("Allvalueshavebeensucessfullyupdatedin")+ "<i> " + ont + "</i> "+ prop.getProperty("ontology."));
  //body (pw, "  <i>" + ont +"</i> ontology.");
  trailer (pw);

  }
}