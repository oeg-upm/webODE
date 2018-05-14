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
import java.rmi.*;

public class UpdateEnumeratedTypeServlet extends BaseServlet {

  //---------------------------------------------------------------------------
  //-- Variables --------------------------------------------------------------
  //---------------------------------------------------------------------------

  HttpServletRequest request=null;
  HttpSession        session=null;

  //---------------------------------------------------------------------------
  //-- Constants --------------------------------------------------------------
  //---------------------------------------------------------------------------

  //-- Values related constants -----------------------------------------------

  public final String ORIGINAL_VALUE="original_value_";
  public final String MODIFIED_VALUE="modified_value_";
  public final String REMOVED_VALUE ="removed_value_";
  public final String ADDED_VALUE   ="added_value_";

  public final String ACTION           ="action";
  public final String DELETE_DESCRIPTOR="delete";
  public final String ADD_DESCRIPTOR   ="add";
  public final String INIT_DESCRIPTOR  ="init";

  public final int    NO_ACTION        =-1;
  public final int    INIT             =0;
  public final int    DELETE           =1;
  public final int    ADD              =2;

  public final String TO_DELETE        ="toDelete";

  //-- Type related constants -------------------------------------------------

  public final String TYPE_NAME         ="enumerated_type";
  public final String TYPE_DESCRIPTION  ="enumerated_type_description";
  public final String ORIGINAL_TYPE_NAME="original_enumerated_type";

  //-- Other constants --------------------------------------------------------

  public final String ONTOLOGY_NAME    ="ontology_name";
  public final String BLANK_FILLER     ="12357111319232937";

  //---------------------------------------------------------------------------
  //-- Data structures --------------------------------------------------------
  //---------------------------------------------------------------------------

  public class ModifiedValueDescriptor {
    public String original_value=null;
    public String modified_value=null;

    public ModifiedValueDescriptor(String original, String modified) {
      original_value=original;
      modified_value=modified;
    }
  }

  //---------------------------------------------------------------------------
  //-- Business logic ---------------------------------------------------------
  //---------------------------------------------------------------------------

  //-- Values related logic ---------------------------------------------------

  public int getAction() {
    String action=request.getParameter(ACTION);
    if(action==null)
      return NO_ACTION;

    if(action.equals(DELETE_DESCRIPTOR))
      return DELETE;
    else if(action.equals(ADD_DESCRIPTOR))
      return ADD;
    else if(action.equals(INIT_DESCRIPTOR))
      return INIT;

    return NO_ACTION;
  }

  public String getDeletedTag() {
    return request.getParameter(TO_DELETE);
  }

  public String getDeletedValue() {
    String dTag=getDeletedTag();
    String dValue=null;

    if(dTag!=null)
      dValue=request.getParameter(dTag);

    return dValue;
  }

  public boolean isDeletedValueValid() {
    String dTag=getDeletedTag();
    boolean isValid=false;

    if(dTag!=null)
      isValid=dTag.startsWith(ORIGINAL_VALUE);

    return isValid;
  }

  public ModifiedValueDescriptor[] loadModifiedValues() {
    Vector v=new Vector();

    String oValue,cValue;

    for(int i=0;(oValue=request.getParameter(ORIGINAL_VALUE+i))!=null&&
                (cValue=request.getParameter(MODIFIED_VALUE+i))!=null;i++) {
      if(cValue.equals(BLANK_FILLER))
        cValue=new String();

      oValue=oValue.trim();
      cValue=cValue.trim();
      v.add(new ModifiedValueDescriptor(oValue,cValue));
    }

    if(getAction()==DELETE) {
      if(isDeletedValueValid()) {
        boolean found=false;
        String  toDelete=getDeletedValue();
        for(int i=0;!found&&i<v.size();i++) {
          ModifiedValueDescriptor mv=(ModifiedValueDescriptor)v.get(i);
          if(mv.original_value.equals(toDelete)) {
            v.remove(mv);
            found=true;
          }
        }
      }
    }

    ModifiedValueDescriptor[] a=new ModifiedValueDescriptor[v.size()];
    v.toArray(a);

    return a;
  }

  public String[] loadRemovedValues() {
    Vector v=new Vector();

    String rValue;

    for(int i=0;(rValue=request.getParameter(REMOVED_VALUE+i))!=null;i++) {
      v.add(rValue);
    }

    if(getAction()==DELETE) {
      if(isDeletedValueValid()) {
        v.add(getDeletedValue());
      }
    }

    String[] a=new String[v.size()];
    v.toArray(a);

    return a;
  }

  public String[] loadAddedValues() {
    Vector v=new Vector();

    String aValue;
    for(int i=0;(aValue=request.getParameter(ADDED_VALUE+i))!=null;i++) {
      if(aValue.equals(BLANK_FILLER))
        aValue=new String();

      v.add(aValue);
    }

    if(getAction()==DELETE) {
      if(!isDeletedValueValid()) {
        v.remove(getDeletedValue());
      }
    } else if(getAction()==ADD) {
      v.add(new String());
    }

    if(v.size()==0)
      v.add(new String());

    String[] a=new String[v.size()];
    v.toArray(a);

    return a;
  }

  //-- Type related logic -----------------------------------------------------

  public String getTypeName() {
    String type=request.getParameter(TYPE_NAME);

    return type;
  }

  public String getTypeDescription() {
    String description=request.getParameter(TYPE_DESCRIPTION);

    return description;
  }

  public String getOriginalTypeName() {
    String type=request.getParameter(ORIGINAL_TYPE_NAME);
    if(type==null)
      type=request.getParameter(TYPE_NAME);

    return type;
  }

  //-- Other logic ------------------------------------------------------------

  public String getOntologyName() {
    String ontologyName=request.getParameter(ONTOLOGY_NAME);

    if(ontologyName==null)
      ontologyName=(String)session.getAttribute("current_ontology");

    return ontologyName;
  }

  public void setRequest(HttpServletRequest req) {
    request=req;
  }

  public void setSession(HttpSession ses) {
    session=ses;
  }

  EnumeratedType            theType        =null;
  String                    ontologyName   =null;
  String                    type           =null;
  ModifiedValueDescriptor[] mValues        =null;
  String[]                  rValues        =null;
  String[]                  aValues        =null;
  String                    typeName       =null;
  String                    typeDescription=null;

  public void myInit(HttpServletRequest req,
                     HttpSession        ses) {
    try {
      setRequest(req);
      setSession(ses);

      ODEService odeService  =(ODEService)session.getAttribute("ode");

      ontologyName=getOntologyName();
      type        =getOriginalTypeName();
      theType     =odeService.getEnumeratedType(ontologyName,type);

      Enumeration c=request.getParameterNames();
//      System.out.println("Parameters:");
      while(c.hasMoreElements()) {
        String k=(String)c.nextElement();
//        System.out.println("  "+k+"="+request.getParameter(k));
      }
//      System.out.println();

      mValues=loadModifiedValues();
      rValues=loadRemovedValues();
      aValues=loadAddedValues();
      typeName=getTypeName();
      typeDescription=getTypeDescription();

//      System.out.println("ontologyName: "+ontologyName);
//      System.out.println("type        : "+type);
//     System.out.println("description : "+typeDescription);
//      System.out.println();

      if(getAction()==DELETE) {
//        System.out.println("getDeletedTag(): "+getDeletedTag());
//        System.out.println("getDeletedValue(): "+getDeletedValue());
//        System.out.println("isValidDeletedValue(): "+isDeletedValueValid());
//        System.out.println();
      }

//      System.out.println("mValues: "+mValues.length);
//      System.out.println("rValues: "+rValues.length);
//      System.out.println("aValues: "+aValues.length);
    } catch (WebODEException ex) {
      ex.printStackTrace();
    } catch (RemoteException ex) {
      ex.printStackTrace();
    }

  }

  protected void doPost(HttpServletRequest  req,
                        HttpServletResponse res,
                        HttpSession session) throws IOException,
                                                    ServletException {
    // These things are in the session.
    String oname=(String)session.getAttribute(CURRENT_ONTOLOGY);
    Properties prop = (Properties)session.getAttribute("prop");

    // Get parameters
//    System.out.println("=- UPDATE SERVLET =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

    myInit(req,session);

    try {
      // all right.  Let's insert this data into the database
      ODEService odeService=(ODEService)session.getAttribute(ODE_SERVICE);

      odeService.updateEnumeratedType(ontologyName,theType.name,typeName,typeDescription);
      for(int i=0;i<mValues.length;i++) {
        ModifiedValueDescriptor mv=mValues[i];
        if(!mv.original_value.equals(mv.modified_value)) {
          if(mv.modified_value.length()>0)
            odeService.updateEnumeratedValue(ontologyName,typeName,mv.original_value,mv.modified_value);
          else
            odeService.removeEnumeratedValue(ontologyName,typeName,mv.original_value);
        }
      }

      for(int i=0;i<rValues.length;i++) {
        odeService.removeEnumeratedValue(ontologyName,typeName,rValues[i]);
      }

      for(int i=0;i<aValues.length;i++) {
        odeService.addEnumeratedValue(ontologyName,typeName,aValues[i]);
      }

      //odeService.insertEnumeratedType(oname,enumeratedTypeName,description,values);

      PrintWriter pw = res.getWriter ();

      // NUEVO PARA MULTILINGUALIDAD DANI
      pw.println("<html>");
      pw.println("  <head>");
      pw.println("     <META HTTP-EQUIV=\"refresh\" content=\"5;URL=../jsp/webode/about.jsp\"> ");
      pw.println("  </head>");
      // FIN NUEVO


      header(pw);
      body (pw, prop.getProperty("EnumeratedType")+ "<i> " + theType.name + " </i> "+ prop.getProperty("updatedsuccesfully")+".");

      sendAdditionEvent(pw, -1, theType.name);
      trailer(pw);
    } catch(Exception e) {
      error(res, e.getMessage(), e);
    }
  }
}