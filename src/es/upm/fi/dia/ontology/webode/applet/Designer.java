package es.upm.fi.dia.ontology.webode.applet;

import java.net.*;
import java.awt.event.*;
import javax.swing.*;
import es.upm.fi.dia.ontology.webode.ui.designer.*;
import es.upm.fi.dia.ontology.webode.ui.designer.model.*;
import java.util.*;

/**
 * The applet to launch the designer.
 */
public class Designer extends JApplet implements ActionListener
{
  public static final String Valuesofthemetaprop = "Valuesofthemeta-prop";
  public static final String OntologyDesigner = "OntologyDesigner";
  public static final String ONTOLOGY = "ontology";
  public static final String VERSIONC="VERSIONC";
  public static final String  DEFAULT_VIEWC="DEFAULT_VIEWC";
  public static final String  NEW_VIEWC="NEW_VIEWC";
  public static final String  DELETE_VIEWC="DELETE_VIEWC";
  public static final String  COMMITC="COMMITC";
  public static final String  COMMIT_ALLC="COMMIT_ALLC";
  public static final String  ADD_TERMC="ADD_TERMC";
  public static final String  ADD_RELATIONC="ADD_RELATIONC";
  public static final String  REMOVE_ELEMENTC="REMOVE_ELEMENTC";
  public static final String  SUBCLASS_OFC="SUBCLASS_OFC";
  public static final String  EXHAUSTIVEC="EXHAUSTIVEC";
  public static final String  DISJOINTC="DISJOINTC";
  public static final String  PART_OFC="PART_OFC";
  public static final String  IN_PART_OFC="IN_PART_OFC";
  public static final String  REMOVE_ELEMENT_COMPLETELYC ="REMOVE_ELEMENT_COMPLETELYC";
  public static final String  ADHOCC="ADHOCC";
  public static final String  METAPROPERTIESC="METAPROPERTIESC";
  public static final String  EVALUATIONC="EVALUATIONC";
  public static final String  FOCUS_ON_RIGIDC="FOCUS_ON_RIGID" ;
  public static final String  OKC="OKC";
  public static final String  CANCELC="CANCELC";;
  public static final String  ORIGINC="ORIGINC";
  public static final String  DESTINATIONC="DESTINATIONC";
  public static final String  RELATIONC="RELATIONC";
  public static final String  POS_XC="POS_XC";
  public static final String  POS_YC="POS_YC";
  public static final String  ELEMENTC="ELEMENTC";
  public static final String  URLC="URLC";
  public static final String  VIEWC="VIEWC";
  public static final String  TERMC="TERMC";
  public static final String  INSTANCEC="INSTANCEC";
  public static final String  SYNCHRONOUSC="SYNCHRONOUSC";
  public static final String  ASYNCHRONOUSC="ASYNCHRONOUSC";
  public static final String  Insertvaluesformetapropertiesofconcepts="Insertvaluesformetapropertiesofconcepts";
  public static final String  Selectagrouptobetheoriginoftherelation="Selectagrouptobetheoriginoftherelation";
  public static final String  Modifythevaluesofthemetapropertiesof ="Modifythevaluesofthemetapropertiesof";
  public static final String  Incorrectvaluesofthemetaproperties="Incorrectvaluesofthemetaproperties";
  public static final String  Error ="Error";
  public static final String  Errorinsertingmetaproperties ="Errorinsertingmetaproperties";
  public static final String  Nameforthenewterm ="Nameforthenewterm";
  public static final String  Namefortherelation ="Namefortherelation";
  public static final String  Unknowntypeofrelation="Unknowntypeofrelation";
  public static final String  Aviewwithname="Aviewwithname";
  public static final String  alreadyexists ="alreadyexists";
  public static final String  Newterm = "Newterm";
  public static final String Existingterm = "Existingterm";
  public static final String MaximumCardinality="MaximumCardinality";
  public static final String Valuesofthemeta = "Valuesofthemeta";
  public static final String   Invalidcardinality ="Invalidcardinality";
  public static final String   greater = "greater";
  public static final String   Valuesmetaproperties="Valuesmetaproperties";
  public static final String   Transferring ="Transferring";
  public static final String Sending = "Sending";
  public static final String Rec="Rec";
  public static final String Receiveing  ="Receiveing";
  public static final String Errorcommittingdata = "Errorcommittingdata";
  public static final String Operationcompletedsucessfully="Operationcompletedsucessfully";
  public static final String Errorsending="Errorsending";
  public static final String Information="Information";
  public static final String Nameforthenewview="Nameforthenewview";
  public static final String Aview="Aview";
  public static final String Exists ="Exists";
  public static final String UnexpectedError="UnexpectedError";
  public static final String Unex ="Unex";
  public static final String Defaultview = "Defaultview";
  public static final String Sure="Sure";
  public static final String Confirmation="Confirmation";
  public static final String NoerrorOntoClean="NoerrorOntoClean";
  public static final String errorOntoClean = "errorOntoClean";
  public static final String Supervisorloaded="Supervisorloaded";
  public static final String Loadingsupervisor="Loadingsupervisor";
  public static final String Theview="Theview";
  public static final String hasn="hasn";
  public static final String want="want";
  public static final String OntocleanTools="OntocleanTools";
  public static final String ADD_TO_NEW_GROUP  ="ADD_TO_NEW_GROUP";
  public static final String ADD_TO_GROUP      ="ADD_TO_GROUP";
  public static final String REMOVE_FROM_GROUP ="REMOVE_FROM_GROUP";
  public static final String NameGroup="NameGroup";
  public static final String Erroraddinggroup ="Erroraddinggroup";

  private Vector vRemoved;
  private URL urlPost;
  private DefaultDesignModel ddm;
  private DesignerFrame df;
  private DesignerConstants dc=new DesignerConstants();

  private static int language;

  public void init ()
  {
    String foo = getParameter (ONTOLOGY);
    //Constants.DEFAULT_VIEW=getParameter("DEFAULT_VIEW");

    // the current language
    DesignerConstants.AValuesofthemetaprop=getParameter(Valuesofthemetaprop);
    DesignerConstants.VERSIONC=getParameter(VERSIONC);
    DesignerConstants.DEFAULT_VIEWC=getParameter(DEFAULT_VIEWC);
    DesignerConstants.NEW_VIEWC=getParameter(NEW_VIEWC);
    DesignerConstants.DELETE_VIEW=getParameter(DELETE_VIEWC);
    DesignerConstants.COMMITC=getParameter(COMMITC);
    DesignerConstants.COMMIT_ALLC=getParameter(COMMIT_ALLC);
    DesignerConstants.ADD_TERMC=getParameter(ADD_TERMC);
    DesignerConstants.ADD_RELATIONC=getParameter(ADD_RELATIONC);
    DesignerConstants.REMOVE_ELEMENTC=getParameter(REMOVE_ELEMENTC);
    DesignerConstants.SUBCLASS_OFC=getParameter(SUBCLASS_OFC);
    DesignerConstants.EXHAUSTIVEC=getParameter(EXHAUSTIVEC);
    DesignerConstants.DISJOINTC=getParameter(DISJOINTC);
    DesignerConstants.PART_OFC=getParameter(PART_OFC);
    DesignerConstants.IN_PART_OFC=getParameter(IN_PART_OFC);
    DesignerConstants.REMOVE_ELEMENT_COMPLETELYC=getParameter(REMOVE_ELEMENT_COMPLETELYC);
    DesignerConstants.ADHOCC=getParameter(ADHOCC);
    DesignerConstants.METAPROPERTIESC=getParameter(METAPROPERTIESC);
    DesignerConstants.EVALUATIONC=getParameter(EVALUATIONC);
    DesignerConstants.FOCUS_ON_RIGIDC=getParameter(FOCUS_ON_RIGIDC);
    DesignerConstants.OKC=getParameter(OKC);
    DesignerConstants.CANCELC=getParameter(CANCELC);
    DesignerConstants.ORIGINC=getParameter(ORIGINC);
    DesignerConstants.DESTINATIONC=getParameter(DESTINATIONC);
    DesignerConstants.RELATIONC=getParameter(RELATIONC);
    DesignerConstants.POS_XC=getParameter(POS_XC);
    DesignerConstants.POS_YC=getParameter(POS_YC);
    DesignerConstants.ELEMENTC=getParameter(ELEMENTC);
    DesignerConstants.VIEWC=getParameter(VIEWC);
    DesignerConstants.TERMC=getParameter(TERMC);
    DesignerConstants.INSTANCEC=getParameter(INSTANCEC);
    DesignerConstants.SYNCHRONOUSC=getParameter(SYNCHRONOUSC);
    DesignerConstants.ASYNCHRONOUSC=getParameter(ASYNCHRONOUSC);
    DesignerConstants.Insertvaluesformetapropertiesofconcepts=getParameter(Insertvaluesformetapropertiesofconcepts);
    DesignerConstants.Selectagrouptobetheoriginoftherelation=getParameter(Selectagrouptobetheoriginoftherelation);
    DesignerConstants.Modifythevaluesofthemetapropertiesof=getParameter(Modifythevaluesofthemetapropertiesof);
    DesignerConstants.Incorrectvaluesofthemetaproperties=getParameter(Incorrectvaluesofthemetaproperties);
    DesignerConstants.Error=getParameter(Error);
    DesignerConstants.Errorinsertingmetaproperties=getParameter(Errorinsertingmetaproperties);
    DesignerConstants.Nameforthenewterm=getParameter(Nameforthenewterm);
    DesignerConstants.Namefortherelation=getParameter(Namefortherelation);
    DesignerConstants.Unknowntypeofrelation=getParameter(Unknowntypeofrelation);
    DesignerConstants.Aviewwithname=getParameter(Aviewwithname);
    DesignerConstants.alreadyexists=getParameter(alreadyexists);
    DesignerConstants.Newterm=getParameter(Newterm);
    DesignerConstants.Existingterm=getParameter(Existingterm);
    DesignerConstants.MaximumCardinality=getParameter(MaximumCardinality);
    DesignerConstants.Valuesofthemeta=getParameter(Valuesofthemeta);
    DesignerConstants.Invalidcardinality=getParameter(Invalidcardinality);
    DesignerConstants.greater=getParameter(greater);
    DesignerConstants.Valuesmetaproperties=getParameter(Valuesmetaproperties);
    DesignerConstants.Transferring=getParameter(Transferring);
    DesignerConstants.Sending=getParameter(Sending);
    DesignerConstants.Rec=getParameter(Rec);
    DesignerConstants.Receiveing=getParameter(Receiveing);
    DesignerConstants.Errorcommittingdata=getParameter(Errorcommittingdata);
    DesignerConstants.Operationcompletedsucessfully=getParameter(Operationcompletedsucessfully);
    DesignerConstants.Errorsending=getParameter(Errorsending);
    DesignerConstants.Information=getParameter(Information);
    DesignerConstants.Nameforthenewview=getParameter(Nameforthenewview);
    DesignerConstants.Aview=getParameter(Aview);
    DesignerConstants.Exists=getParameter(Exists);
    DesignerConstants.UnexpectedError=getParameter(UnexpectedError);
    DesignerConstants.Unex=getParameter(Unex);
    DesignerConstants.Defaultview=getParameter(Defaultview);
    DesignerConstants.Sure=getParameter(Sure);
    DesignerConstants.Confirmation=getParameter(Confirmation);
    DesignerConstants.NoerrorOntoClean=getParameter(NoerrorOntoClean);
    DesignerConstants.errorOntoClean=getParameter(errorOntoClean);
    DesignerConstants.Supervisorloaded=getParameter(Supervisorloaded);
    DesignerConstants.Loadingsupervisor=getParameter(Loadingsupervisor);
    DesignerConstants.Theview=getParameter(Theview);
    DesignerConstants.hasn=getParameter(hasn);
    DesignerConstants.want=getParameter(want);
    DesignerConstants.OntocleanTools=getParameter(OntocleanTools);
    DesignerConstants.ADD_TO_NEW_GROUP=getParameter(ADD_TO_NEW_GROUP);
    DesignerConstants.ADD_TO_GROUP=getParameter(ADD_TO_GROUP );
    DesignerConstants.REMOVE_FROM_GROUP=getParameter(REMOVE_FROM_GROUP);
    DesignerConstants.NameGroup=getParameter(NameGroup);
    DesignerConstants.Erroraddinggroup=getParameter(Erroraddinggroup);
    DesignerConstants.OntologyDesigner=getParameter(OntologyDesigner);

    boolean bOnto = foo != null && foo.equalsIgnoreCase ("yes");

    // URL for the servlet to process data.
    try {
      String url = getCodeBase().toString();
      url = url.substring (0, url.indexOf ('/', 8));
      System.out.println ("URL: " + url + "/webode/servlet/" + getParameter (DesignerConstants.URL));
      urlPost = new URL (url + "/webode/servlet/" + getParameter (DesignerConstants.URL));
    } catch (Exception e) {
      System.err.println ("Error in URL: " + e);
    }

    getContentPane().add (new JLabel (DesignerConstants.OntologyDesigner, JLabel.CENTER));
    vRemoved = new Vector(200);
    df = new DesignerFrame (bOnto ?
                            (ElementRenderer) (new TriangleRenderer()) :
                            (ElementRenderer) (new DefaultDesignRenderer()),
                            !bOnto,
                            // Get relations and build model
                            _buildModel (),
                            _getSomething (DesignerConstants.VIEW),
                            _getSomething (DesignerConstants.TERM),
                            _getSomething (DesignerConstants.RELATION),
                            _getSomething (DesignerConstants.INSTANCE),
                            this,
                            urlPost,
                            _loadImages());
    df.setSize (900, 700);
    df.setVisible (true);
  }

  private Icon[] _loadImages ()
  {
    try {
      Icon[] aic = new Icon[13];
      for (int i = 1; i < 14; i++)
        aic[i - 1] = new ImageIcon (new URL (getCodeBase() + "../images/" + i + ".gif"));
      return aic;
    } catch (Exception e) {
      System.out.println ("Error loading images: " + e);
      return null;
    }
  }

  private String[] _getSomething (String something)
  {
    String str = null;
    Vector v = new Vector();
    for (int i = 0; (str = getParameter (something + i)) != null; i++)
      v.addElement (str);

    if (v.isEmpty())
      return null;
    String[] astr = new String[v.size()];
    v.copyInto (astr);

    return astr;
  }

  private DesignModel _buildModel ()
  {
    // Get parameters name DESTINATION, ORIGIN AND RELATION
    String origin, destination, relation;
    int posx, posy;

    ddm = new DefaultDesignModel();

    // Make the model not to validate
    ddm.setChecks (false);

 /*ddm.addDesignModelListener (new DesignModelAdapter() {
     public void relationRemoved (DesignEvent de) {
  System.out.println ("Removed:" + de);
    // Add only if not new
  Relation rel = de.getRelation ();
  if (!rel.isNew())
      vRemoved.addElement (rel);
     }

     public void elementRemoved (DesignEvent de) {
  System.out.println ("Element removed: "+ de);
  Element el = de.getElement();
    // Add only if not new
  if (!el.isNew())
      vRemoved.addElement (de.getElement());
     }
     });*/


    return ddm;
  }

  public void actionPerformed (ActionEvent ae1)
  {
 /*Vector vNew = new Vector (200);
 Vector vDirty = new Vector (200);

    // Get new terms --> Classify them according to if they are
    // new or they are dirty.
 Element[] ae = ddm.getElements();
 for (int i = 0; i < ae.length; i++) {
     if (ae[i].isNew()) {
  vNew.addElement (ae[i]);
  ae[i].setNew (false);
     }
     else if (ae[i].isDirty())
  vDirty.addElement (ae[i]);
     ae[i].setDirty (false);
 }
 Relation[] ar = ddm.getRelations();
 for (int i = 0; i < ar.length; i++) {
     if (ar[i].isNew()) {
  vNew.addElement (ar[i]);
  ar[i].setNew (false);
     }
     else if (ar[i].isDirty())
       vDirty.addElement (ar[i]);
     ar[i].setDirty (false);
 }

 if (vRemoved.isEmpty() && vNew.isEmpty() && vDirty.isEmpty())
    // Send nothing
     return;

    // this method is invoked when commit is activated...
    //new ComHelper (urlPost, vRemoved, vNew, vDirty, df).start();
 new Communicator (df, urlPost, Command.COMMIT, null,
     new Object[] { vRemoved, vNew, vDirty }, null).start();*/
  }
}













