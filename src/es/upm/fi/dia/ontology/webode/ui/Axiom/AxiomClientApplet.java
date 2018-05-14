package es.upm.fi.dia.ontology.webode.ui.Axiom;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import java.io.*;
import javax.swing.undo.*;
import javax.swing.table.*;



import java.net.*;

import java.util.*;
/**
 * Inserte aquí la descripción del tipo.
 * Fecha de creación: (21/09/2001 17:30:26)
 * @author:
 */
public class AxiomClientApplet extends javax.swing.JApplet {

  private Vector dummyHeaderA = new Vector();
  private Vector dummyHeaderC = new Vector();

  DefaultTableModel dmA = new DefaultTableModel();
  DefaultTableModel dmC = new DefaultTableModel();
  JTable tableA = new JTable( dmA );
  JTable tableC = new JTable( dmC );
  JScrollPane scrollTableA =new JScrollPane( tableA );
  JScrollPane scrollTableC =new JScrollPane( tableC );





  private ButtonGroup group = new ButtonGroup();

  private javax.swing.JButton ivjbtDeleteA = null;
  private javax.swing.JButton ivjbtDeleteCR = null;
  private javax.swing.JButton ivjbtSendR = null;
  private javax.swing.JPanel ivjrb = null;
  private javax.swing.JList ivjlsAntecendeR = null;
  private javax.swing.JList ivjlsConsecuenteR = null;
  private javax.swing.JPanel ivjPage = null;
  private javax.swing.JRadioButton ivjrbAntecedenteR = null;
  private javax.swing.JRadioButton ivjrbConsecuenteR = null;
  private UndoManager undo=new UndoManager();
  private javax.swing.JPanel ivjJAppletContentPane = null;
  private javax.swing.JTabbedPane ivjJTabbedPane1 = null;
  private javax.swing.JPanel ivjpageAxiom = null;
  private javax.swing.JPanel ivjpageRule = null;
  private javax.swing.JScrollPane ivjareaScrollPane = null;
  private javax.swing.JComboBox ivjcbAttributes = null;
  private javax.swing.JComboBox ivjcbConcepts = null;
  private javax.swing.JComboBox ivjcbConstants = null;
  private javax.swing.JComboBox ivjcbRelations = null;
  private javax.swing.JComboBox ivjcbAttributesR = null;
  private javax.swing.JComboBox ivjcbConceptsR = null;
  private javax.swing.JComboBox ivjcbConstantsR = null;
  private javax.swing.JComboBox ivjcbRelationsR = null;

  private boolean evento=false;
  private boolean eventoR=false;


  private javax.swing.JPanel ivjpaAttribute = null;
  private javax.swing.JPanel ivjpaConcept = null;
  private javax.swing.JPanel ivjpaConstant = null;
  private javax.swing.JPanel ivjpaRelation = null;
  private javax.swing.JPanel ivjpaAttributeR = null;
  private javax.swing.JPanel ivjpaConceptR = null;
  private javax.swing.JPanel ivjpaConstantR = null;
  private javax.swing.JPanel ivjpaRelationR = null;

  private javax.swing.JTextPane ivjtaText = null;
  private javax.swing.JButton ivjbtAnd = null;
  private javax.swing.JButton ivjbtDImp = null;
  private javax.swing.JButton ivjbtExists = null;
  private javax.swing.JButton ivjbtForall = null;
  private javax.swing.JButton ivjbtImp = null;
  private javax.swing.JButton ivjbtNot = null;
  private javax.swing.JButton ivjbtOr = null;
  private javax.swing.JButton ivjbtSend = null;
  private javax.swing.JButton ivjbtUndo = null;
  private javax.swing.JButton ivjbtRedo = null;

  private javax.swing.JPanel ivjJPanel1 = null;

  private java.util.Vector vRelations = new Vector();
  private java.util.Vector vRelationsR = new Vector();

  private javax.swing.text.MutableAttributeSet attr_var = new SimpleAttributeSet();
  private javax.swing.text.MutableAttributeSet attr_word = new SimpleAttributeSet();
  private javax.swing.text.MutableAttributeSet attr_update = new SimpleAttributeSet();
  private javax.swing.text.StyledDocument doc;
  private String name=null;
  private String expression=null;
  private String description=null;
  private String type=null;


  private void ponerListaReglas(){

    StringTokenizer st = new StringTokenizer(expression);
    String aux=null;
    boolean antecedente=true;
    while (st.hasMoreTokens()) {
      aux=st.nextToken();
      if(aux.equals("if"))
      {
        antecedente=true;

      }else if(aux.equals("then")){

        antecedente=false;
      }else if(aux.equals("and")){

      }else if(antecedente){

        dmA.addRow(new  String [] {aux});

      }else{

        dmC.addRow(new  String [] {aux});


      }
    }



  }
  private void _setComboBox()
  {
    evento=false;
    if (getcbConcepts().getItemCount()!=0)
    {
      getcbConcepts().removeAllItems();
    }
    int i=0;
    String aux=getParameter("concept"+i);
    _setCbAttributes(aux);
    _setCbRelations(aux);
    _setCbConstants();
    while (aux!=null)
    {
      getcbConcepts().addItem(aux);
      i++;
      aux=getParameter("concept"+i);

    }
    evento=true;
  }
  private void _setComboBoxR()
  {
    eventoR=false;
    if (getcbConceptsR().getItemCount()!=0)
    {
      getcbConceptsR().removeAllItems();
    }
    int i=0;
    String aux=getParameter("concept"+i);
    _setCbAttributesR(aux);
    _setCbRelationsR(aux);
    _setCbConstantsR();
    while (aux!=null)
    {
      getcbConceptsR().addItem(aux);
      i++;
      aux=getParameter("concept"+i);

    }
    eventoR=true;
  }
  private void _setCbConstants()
  {
    evento=false;
    if (getcbConstants().getItemCount()!=0)
    {
      getcbConstants().removeAllItems();
    }
    int i=0;
    String aux=getParameter("constant"+i);
    while (aux!=null)
    {
      getcbConstants().addItem(aux);
      i++;
      aux=getParameter("constant"+i);

    }
    evento=true;
  }
  private void _setCbConstantsR()
  {
    eventoR=false;
    if (getcbConstantsR().getItemCount()!=0)
    {
      getcbConstantsR().removeAllItems();
    }
    int i=0;
    String aux=getParameter("constant"+i);
    while (aux!=null)
    {
      getcbConstantsR().addItem(aux);
      i++;
      aux=getParameter("constant"+i);

    }
    eventoR=true;
  }
  private void _setCbAttributes(String concept)
  {
    evento=false;
    if (getcbAttributes().getItemCount()!=0)
    {
      getcbAttributes().removeAllItems();
    }

    int i=0;
    String aux1=getParameter("classAttribute"+ i +"_"+ concept);
    while (aux1!=null)
    {
      getcbAttributes().addItem(aux1);
      i++;
      aux1=getParameter("classAttribute"+ i +"_"+ concept);

    }

    i=0;
    aux1=getParameter("instanceAttribute"+ i +"_"+ concept);
    while (aux1!=null)
    {
      getcbAttributes().addItem(aux1);
      i++;
      aux1=getParameter("instanceAttribute"+ i +"_"+ concept);

    }
    evento=true;
  }
  private void _setCbAttributesR(String concept)
  {
    eventoR=false;
    if (getcbAttributesR().getItemCount()!=0)
    {
      getcbAttributesR().removeAllItems();
    }

    int i=0;
    String aux1=getParameter("classAttribute"+ i +"_"+ concept);
    while (aux1!=null)
    {
      getcbAttributesR().addItem(aux1);
      i++;
      aux1=getParameter("classAttribute"+ i +"_"+ concept);

    }

    i=0;
    aux1=getParameter("instanceAttribute"+ i +"_"+ concept);
    while (aux1!=null)
    {
      getcbAttributesR().addItem(aux1);
      i++;
      aux1=getParameter("instanceAttribute"+ i +"_"+ concept);

    }
    eventoR=true;
  }

  private void _setCbRelations(String concept)
  {
    evento=false;
    if (getcbRelations().getItemCount()!=0)
    {
      getcbRelations().removeAllItems();
      vRelations.removeAllElements();
    }


    int i=0;
    String origin=getParameter("origin"+i);
    String destination=getParameter("destination"+i);
    while (origin != null && destination!=null)
    {
      if (origin.equals(concept) || destination.equals(concept))
      {
        vRelations.add(""+i);
        getcbRelations().addItem(origin +"-$-"+getParameter("relation"+i)+"-$-"+ destination );

      }
      i++;
      origin=getParameter("origin"+i);
      destination=getParameter("destination"+i);

    }

    evento=true;

  }
  private void _setCbRelationsR(String concept)
  {
    eventoR=false;
    if (getcbRelationsR().getItemCount()!=0)
    {
      getcbRelationsR().removeAllItems();
      vRelationsR.removeAllElements();
    }


    int i=0;
    String origin=getParameter("origin"+i);
    String destination=getParameter("destination"+i);
    while (origin != null && destination!=null)
    {
      if (origin.equals(concept) || destination.equals(concept))
      {
        vRelationsR.add(""+i);
        getcbRelationsR().addItem(origin +"-$-"+getParameter("relation"+i)+"-$-"+ destination );

      }
      i++;
      origin=getParameter("origin"+i);
      destination=getParameter("destination"+i);

    }

    eventoR=true;

  }

  /**
   * Retorna información acerca de este applet.
   * @return una serie de caracteres de información acerca de este applet
   */
  public String getAppletInfo() {
    return "AxiomClientApplet\n" +
        "\n" +
        "Inserte aquí la descripción del tipo.\n" +
        "Fecha de creación: (21/09/2001 17:30:21)\n" +
        "@author: \n" +
        "";
  }
  /**
   * Return the JPanel1 property value.
   * @return javax.swing.JPanel
   */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
  private javax.swing.JPanel getrb() {
    if (ivjrb == null) {
      try {
        ivjrb = new javax.swing.JPanel();
        ivjrb.setName("rb");
        ivjrb.setLayout(null);
        ivjrb.setBounds(165, 73, 465, 24);
        getrb().add(getrbAntecedenteR(), getrbAntecedenteR().getName());
        getrb().add(getrbConsecuenteR(), getrbConsecuenteR().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjrb;
  }
  /**
   * Return the lsAntecendeR property value.
   * @return javax.swing.JList
   */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
  private javax.swing.JList getlsAntecendeR() {
    if (ivjlsAntecendeR == null) {
      try {
        ivjlsAntecendeR = new javax.swing.JList();
        ivjlsAntecendeR.setName("lsAntecendeR");
        ivjlsAntecendeR.setBounds(0, 0, 160, 60);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjlsAntecendeR;
  }
  /**
   * Return the btDeleteA property value.
   * @return javax.swing.JButton
   */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
  private javax.swing.JButton getbtDeleteA() {
    if (ivjbtDeleteA == null) {
      try {
        ivjbtDeleteA = new javax.swing.JButton();
        ivjbtDeleteA.setName("btDeleteA");
        ivjbtDeleteA.setIcon(new ImageIcon (new URL (getCodeBase() + "../images/delete.jpg")));
        ivjbtDeleteA.setText("");
        ivjbtDeleteA.setBounds(343, 50, 30, 25);

        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtDeleteA;
  }
  /**
   * Return the btDeleteCR property value.
   * @return javax.swing.JButton
   */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
  private javax.swing.JButton getbtDeleteCR() {
    if (ivjbtDeleteCR == null) {
      try {
        ivjbtDeleteCR = new javax.swing.JButton();
        ivjbtDeleteCR.setName("btDeleteCR");
        ivjbtDeleteCR.setIcon(new ImageIcon (new URL (getCodeBase() + "../images/delete.jpg")));
        ivjbtDeleteCR.setText("");
        ivjbtDeleteCR.setBounds(420, 50, 30, 25);
        ivjbtDeleteCR.setBackground(java.awt.SystemColor.info);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtDeleteCR;
  }
  /**
   * Return the btSendR property value.
   * @return javax.swing.JButton
   */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
  private javax.swing.JButton getbtSendR() {
    String MakeProlog=getParameter("MakeProlog");
    if (ivjbtSendR == null) {
      try {
        ivjbtSendR = new javax.swing.JButton();
        ivjbtSendR.setName("btSendR");
        ivjbtSendR.setText(MakeProlog);
        ivjbtSendR.setBounds(343, 10, 107, 25);
        ivjbtSendR.setBackground(java.awt.SystemColor.info);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtSendR;
  }

  private javax.swing.JScrollPane getareaScrollPane() {
    if (ivjareaScrollPane == null) {
      try {
        ivjareaScrollPane = new javax.swing.JScrollPane();
        ivjareaScrollPane.setName("areaScrollPane");
        ivjareaScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ivjareaScrollPane.setBounds(0, 0, 775, 70);
        getareaScrollPane().setViewportView(gettaText());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjareaScrollPane;
  }
  /**
   * Devolver el valor de la propiedad btAnd.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getbtAnd() {
    if (ivjbtAnd == null) {
      try {
        ivjbtAnd = new javax.swing.JButton();
        ivjbtAnd.setName("btAnd");
        ivjbtAnd.setIcon(new ImageIcon (new URL (getCodeBase() + "../images/and.jpg")));
        ivjbtAnd.setText("");
        ivjbtAnd.setBackground(new java.awt.Color(221,205,255));
        ivjbtAnd.setBounds(133, 5, 43, 25);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtAnd;
  }
  /**
   * Devolver el valor de la propiedad btDImp.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getbtDImp() {
    if (ivjbtDImp == null) {
      try {
        ivjbtDImp = new javax.swing.JButton();
        ivjbtDImp.setName("btDImp");

        ivjbtDImp.setIcon(new javax.swing.ImageIcon(new URL (getCodeBase() + "../images/dobleImplica.jpg")));
        ivjbtDImp.setText("");
        ivjbtDImp.setBackground(new java.awt.Color(221,205,255));
        ivjbtDImp.setBounds(269, 5, 43, 25);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtDImp;
  }
  /**
   * Devolver el valor de la propiedad btExists.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getbtExists() {
    if (ivjbtExists == null) {
      try {
        ivjbtExists = new javax.swing.JButton();
        ivjbtExists.setName("btExists");
        ivjbtExists.setIcon(new javax.swing.ImageIcon(new URL (getCodeBase() + "../images/exists.jpg")));
        ivjbtExists.setText("");
        ivjbtExists.setBackground(new java.awt.Color(221,205,255));
        ivjbtExists.setBounds(45, 5, 43, 25);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtExists;
  }
  /**
   * Devolver el valor de la propiedad btForall.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getbtForall() {
    if (ivjbtForall == null) {
      try {
        ivjbtForall = new javax.swing.JButton();
        ivjbtForall.setName("btForall");
        ivjbtForall.setIcon(new javax.swing.ImageIcon(new URL (getCodeBase() + "../images/forall.jpg")));
        ivjbtForall.setText("");
        ivjbtForall.setBackground(new java.awt.Color(221,205,255));
        ivjbtForall.setBounds(1, 5, 43, 25);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtForall;
  }
  /**
   * Devolver el valor de la propiedad btImp.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getbtImp() {
    if (ivjbtImp == null) {
      try {
        ivjbtImp = new javax.swing.JButton();
        ivjbtImp.setName("btImp");
        ivjbtImp.setIcon(new javax.swing.ImageIcon(new URL (getCodeBase() + "../images/implica.jpg")));
        ivjbtImp.setText("");
        ivjbtImp.setBackground(new java.awt.Color(221,205,255));
        ivjbtImp.setBounds(225, 5, 43, 25);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtImp;
  }
  /**
   * Devolver el valor de la propiedad btNot.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getbtNot() {
    if (ivjbtNot == null) {
      try {
        ivjbtNot = new javax.swing.JButton();
        ivjbtNot.setName("btNot");
        ivjbtNot.setIcon(new javax.swing.ImageIcon(new URL (getCodeBase() + "../images/not.jpg")));
        ivjbtNot.setText("");
        ivjbtNot.setBackground(new java.awt.Color(221,205,255));
        ivjbtNot.setBounds(89, 5, 43, 25);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtNot;
  }
  /**
   * Devolver el valor de la propiedad btOr.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getbtOr() {
    if (ivjbtOr == null) {
      try {
        ivjbtOr = new javax.swing.JButton();
        ivjbtOr.setName("btOr");
        ivjbtOr.setIcon(new javax.swing.ImageIcon(new URL (getCodeBase() + "../images/or.jpg")));
        ivjbtOr.setText("");
        ivjbtOr.setBackground(new java.awt.Color(221,205,255));
        ivjbtOr.setBounds(181, 5, 43, 25);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtOr;
  }
  /**
   * Devolver el valor de la propiedad btSend.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getbtSend() {
    if (ivjbtSend == null) {
      try {
        ivjbtSend = new javax.swing.JButton();
        ivjbtSend.setName("btSend");
        ivjbtSend.setText("Make Prolog");
        ivjbtSend.setBackground(java.awt.SystemColor.info);
        ivjbtSend.setBounds(666, 70, 109, 27);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtSend;
  }
  /**
   * Devolver el valor de la propiedad btUndo.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getbtUndo() {
    if (ivjbtUndo == null) {
      try {
        ivjbtUndo = new javax.swing.JButton();
        ivjbtUndo.setName("btUndo");
        ivjbtUndo.setText("Undo");
        ivjbtUndo.setBackground(java.awt.SystemColor.info);
        ivjbtUndo.setBounds(5, 72, 70, 25);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtUndo;
  }

  /**
   * Devolver el valor de la propiedad btUndo.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getbtRedo() {
    if (ivjbtRedo == null) {
      try {
        ivjbtRedo = new javax.swing.JButton();
        ivjbtRedo.setName("btRedo");
        ivjbtRedo.setText("Redo");
        ivjbtRedo.setBackground(java.awt.SystemColor.info);
        ivjbtRedo.setBounds(75, 72, 70, 25);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjbtRedo;
  }

  /**
   * Devolver el valor de la propiedad cbAttributes.
   * @return javax.swing.JComboBox
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JComboBox getcbAttributes() {
    if (ivjcbAttributes == null) {
      try {
        ivjcbAttributes = new javax.swing.JComboBox();
        ivjcbAttributes.setName("cbAttributes");
        ivjcbAttributes.setBackground(new java.awt.Color(221,205,255));
        ivjcbAttributes.setBounds(17, 18, 336, 23);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjcbAttributes;
  }
  /**
   * Devolver el valor de la propiedad cbAttributes.
   * @return javax.swing.JComboBox
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JComboBox getcbAttributesR() {
    if (ivjcbAttributesR == null) {
      try {
        ivjcbAttributesR = new javax.swing.JComboBox();
        ivjcbAttributesR.setName("cbAttributesR");
        ivjcbAttributesR.setBackground(new java.awt.Color(221,205,255));
        ivjcbAttributesR.setBounds(17, 18, 336, 23);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjcbAttributesR;
  }

  /**
   * Devolver el valor de la propiedad cbConcepts.
   * @return javax.swing.JComboBox
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JComboBox getcbConcepts() {
    if (ivjcbConcepts == null) {
      try {
        ivjcbConcepts = new javax.swing.JComboBox();
        ivjcbConcepts.setName("cbConcepts");
        ivjcbConcepts.setBackground(new java.awt.Color(221,205,255));
        ivjcbConcepts.setBounds(17, 18, 356, 23);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjcbConcepts;
  }
  /**
   * Devolver el valor de la propiedad cbConcepts.
   * @return javax.swing.JComboBox
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JComboBox getcbConceptsR() {
    if (ivjcbConceptsR == null) {
      try {
        ivjcbConceptsR = new javax.swing.JComboBox();
        ivjcbConceptsR.setName("cbConceptsR");
        ivjcbConceptsR.setBackground(new java.awt.Color(221,205,255));
        ivjcbConceptsR.setBounds(17, 18, 356, 23);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjcbConceptsR;
  }

  /**
   * Devolver el valor de la propiedad cbConstants.
   * @return javax.swing.JComboBox
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JComboBox getcbConstants() {
    if (ivjcbConstants == null) {
      try {
        ivjcbConstants = new javax.swing.JComboBox();
        ivjcbConstants.setName("cbConstants");
        ivjcbConstants.setBackground(new java.awt.Color(221,205,255));
        ivjcbConstants.setBounds(17, 18, 336, 23);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjcbConstants;
  }
  /**
   * Devolver el valor de la propiedad cbConstants.
   * @return javax.swing.JComboBox
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JComboBox getcbConstantsR() {
    if (ivjcbConstantsR == null) {
      try {
        ivjcbConstantsR = new javax.swing.JComboBox();
        ivjcbConstantsR.setName("cbConstantsR");
        ivjcbConstantsR.setBackground(new java.awt.Color(221,205,255));
        ivjcbConstantsR.setBounds(17, 18, 336, 23);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjcbConstantsR;
  }

  /**
   * Devolver el valor de la propiedad cbRelations.
   * @return javax.swing.JComboBox
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JComboBox getcbRelations() {
    if (ivjcbRelations == null) {
      try {
        ivjcbRelations = new javax.swing.JComboBox();
        ivjcbRelations.setName("cbRelations");
        ivjcbRelations.setBackground(new java.awt.Color(221,205,255));
        ivjcbRelations.setBounds(17, 18, 356, 23);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjcbRelations;
  }
  /**
   * Devolver el valor de la propiedad cbRelations.
   * @return javax.swing.JComboBox
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JComboBox getcbRelationsR() {
    if (ivjcbRelationsR == null) {
      try {
        ivjcbRelationsR = new javax.swing.JComboBox();
        ivjcbRelationsR.setName("cbRelationsR");
        ivjcbRelationsR.setBackground(new java.awt.Color(221,205,255));
        ivjcbRelationsR.setBounds(17, 18, 356, 23);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjcbRelationsR;
  }

  /**
   * Devolver el valor de la propiedad JAppletContentPane.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getJAppletContentPane() {
    if (ivjJAppletContentPane == null) {
      try {
        ivjJAppletContentPane = new javax.swing.JPanel();
        ivjJAppletContentPane.setName("JAppletContentPane");
        ivjJAppletContentPane.setLayout(null);
        getJAppletContentPane().add(getJTabbedPane1(), getJTabbedPane1().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjJAppletContentPane;
  }
  /**
   * Devolver el valor de la propiedad JPanel1.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getJPanel1() {
    if (ivjJPanel1 == null) {
      try {
        ivjJPanel1 = new javax.swing.JPanel();
        ivjJPanel1.setName("JPanel1");
        ivjJPanel1.setLayout(null);
        ivjJPanel1.setBounds(242, 68, 316, 33);
        getJPanel1().add(getbtForall(), getbtForall().getName());
        getJPanel1().add(getbtExists(), getbtExists().getName());
        getJPanel1().add(getbtNot(), getbtNot().getName());
        getJPanel1().add(getbtAnd(), getbtAnd().getName());
        getJPanel1().add(getbtOr(), getbtOr().getName());
        getJPanel1().add(getbtImp(), getbtImp().getName());
        getJPanel1().add(getbtDImp(), getbtDImp().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjJPanel1;
  }
  /**
   * Devolver el valor de la propiedad JTabbedPane1.
   * @return javax.swing.JTabbedPane
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JTabbedPane getJTabbedPane1() {
    String Rule=getParameter("Rule");
    String Axiom=getParameter("Axiom");

    if (ivjJTabbedPane1 == null) {
      try {
        ivjJTabbedPane1 = new javax.swing.JTabbedPane();
        ivjJTabbedPane1.setName("JTabbedPane1");
        ivjJTabbedPane1.setBounds(0, 0, 800, 225);
        //ivjJTabbedPane1.insertTab("Axiom", null, getpageAxiom(), null, 0);
        //ivjJTabbedPane1.insertTab("Rule", null, getpageRule(), null, 1);
        ivjJTabbedPane1.insertTab(Axiom, null, getpageAxiom(), null, 0);
        ivjJTabbedPane1.insertTab(Rule, null, getpageRule(), null, 1);

        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjJTabbedPane1;
  }
  /**
   * Devolver el valor de la propiedad paAttribute.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getpaAttribute() {
    if (ivjpaAttribute == null) {
      try {
        ivjpaAttribute = new javax.swing.JPanel();
        ivjpaAttribute.setName("paAttribute");
        ivjpaAttribute.setLayout(null);
        ivjpaAttribute.setBounds(405, 97, 370, 49);
        getpaAttribute().add(getcbAttributes(), getcbAttributes().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpaAttribute;
  }
  /**
   * Devolver el valor de la propiedad paAttribute.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getpaAttributeR() {
    if (ivjpaAttributeR == null) {
      try {
        ivjpaAttributeR = new javax.swing.JPanel();
        ivjpaAttributeR.setName("paAttributeR");
        ivjpaAttributeR.setLayout(null);
        ivjpaAttributeR.setBounds(405, 97, 370, 49);
        getpaAttributeR().add(getcbAttributesR(), getcbAttributesR().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpaAttributeR;
  }

  /**
   * Devolver el valor de la propiedad paConcept.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getpaConcept() {
    if (ivjpaConcept == null) {
      try {
        ivjpaConcept = new javax.swing.JPanel();
        ivjpaConcept.setName("paConcept");
        ivjpaConcept.setLayout(null);
        ivjpaConcept.setBounds(5, 97, 390, 49);
        getpaConcept().add(getcbConcepts(), getcbConcepts().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpaConcept;
  }
  /**
   * Devolver el valor de la propiedad paConcept.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getpaConceptR() {
    if (ivjpaConceptR == null) {
      try {
        ivjpaConceptR = new javax.swing.JPanel();
        ivjpaConceptR.setName("paConceptR");
        ivjpaConceptR.setLayout(null);
        ivjpaConceptR.setBounds(5, 97, 390, 49);
        getpaConceptR().add(getcbConceptsR(), getcbConceptsR().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpaConceptR;
  }

  /**
   * Devolver el valor de la propiedad paConstant.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getpaConstant() {
    if (ivjpaConstant == null) {
      try {
        ivjpaConstant = new javax.swing.JPanel();
        ivjpaConstant.setName("paConstant");
        ivjpaConstant.setLayout(null);
        ivjpaConstant.setBounds(405, 143, 370, 49);
        getpaConstant().add(getcbConstants(), getcbConstants().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpaConstant;
  }
  /**
   * Devolver el valor de la propiedad paConstant.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getpaConstantR() {
    if (ivjpaConstantR == null) {
      try {
        ivjpaConstantR = new javax.swing.JPanel();
        ivjpaConstantR.setName("paConstantR");
        ivjpaConstantR.setLayout(null);
        ivjpaConstantR.setBounds(405, 143, 370, 49);
        getpaConstantR().add(getcbConstantsR(), getcbConstantsR().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpaConstantR;
  }


  /**
   * Devolver el valor de la propiedad pageAxiom.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getpageAxiom() {
    if (ivjpageAxiom == null) {
      try {
        ivjpageAxiom = new javax.swing.JPanel();
        ivjpageAxiom.setName("pageAxiom");
        ivjpageAxiom.setLayout(null);
        getpageAxiom().add(getareaScrollPane(), getareaScrollPane().getName());
        getpageAxiom().add(getpaConcept(), getpaConcept().getName());
        getpageAxiom().add(getpaAttribute(), getpaAttribute().getName());
        getpageAxiom().add(getpaRelation(), getpaRelation().getName());
        getpageAxiom().add(getpaConstant(), getpaConstant().getName());
        getpageAxiom().add(getJPanel1(), getJPanel1().getName());
        getpageAxiom().add(getbtSend(), getbtSend().getName());
        getpageAxiom().add(getbtUndo(), getbtUndo().getName());
        getpageAxiom().add(getbtRedo(), getbtRedo().getName());




        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpageAxiom;
  }
  /**
   * Return the pageRule property value.
   * @return javax.swing.JPanel
   */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
  private javax.swing.JPanel getpageRule() {
    if (ivjpageRule == null) {
      try {
        ivjpageRule = new javax.swing.JPanel();
        ivjpageRule.setName("pageRule");
        ivjpageRule.setLayout(null);
        getpageRule().add(scrollTableA);
        getpageRule().add(scrollTableC);
        getpageRule().add(getbtDeleteA(), getbtDeleteA().getName());
        getpageRule().add(getbtDeleteCR(), getbtDeleteCR().getName());
        getpageRule().add(getrb(), getrb().getName());
        getpageRule().add(getbtSendR(), getbtSendR().getName());
        getpageRule().add(getpaConceptR(), getpaConceptR().getName());
        getpageRule().add(getpaAttributeR(), getpaAttributeR().getName());
        getpageRule().add(getpaRelationR(), getpaRelationR().getName());
        getpageRule().add(getpaConstantR(), getpaConstantR().getName());

        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpageRule;
  }
  /**
   * Return the rbAntecedenteR property value.
   * @return javax.swing.JRadioButton
   */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
  private javax.swing.JRadioButton getrbAntecedenteR() {
    String AddAntecedent=getParameter("AddAntecedent");
    if (ivjrbAntecedenteR == null) {
      try {
        ivjrbAntecedenteR = new javax.swing.JRadioButton();
        ivjrbAntecedenteR.setName("rbAntecedenteR");
        ivjrbAntecedenteR.setSelected(true);
        ivjrbAntecedenteR.setText(AddAntecedent);
        ivjrbAntecedenteR.setBounds(20, 1, 120, 22);
        ivjrbAntecedenteR.setBackground(new java.awt.Color(221,205,255));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjrbAntecedenteR;
  }
  /**
   * Return the rbConsecuenteR property value.
   * @return javax.swing.JRadioButton
   */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
  private javax.swing.JRadioButton getrbConsecuenteR() {
    String AddConsequent=getParameter("AddConsequent");
    if (ivjrbConsecuenteR == null) {
      try {
        ivjrbConsecuenteR = new javax.swing.JRadioButton();
        ivjrbConsecuenteR.setName("rbConsecuenteR");
        ivjrbConsecuenteR.setText(AddConsequent);
        ivjrbConsecuenteR.setBounds(329, 1, 120, 22);
        ivjrbConsecuenteR.setBackground(new java.awt.Color(221,205,255));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjrbConsecuenteR;
  }

  /**
   * Devolver el valor de la propiedad paRelation.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getpaRelation() {
    if (ivjpaRelation == null) {
      try {
        ivjpaRelation = new javax.swing.JPanel();
        ivjpaRelation.setName("paRelation");
        ivjpaRelation.setLayout(null);
        ivjpaRelation.setBounds(5, 143, 390, 49);
        getpaRelation().add(getcbRelations(), getcbRelations().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpaRelation;
  }
  /**
   * Devolver el valor de la propiedad paRelation.
   * @return javax.swing.JPanel
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JPanel getpaRelationR() {
    if (ivjpaRelationR == null) {
      try {
        ivjpaRelationR = new javax.swing.JPanel();
        ivjpaRelationR.setName("paRelationR");
        ivjpaRelationR.setLayout(null);
        ivjpaRelationR.setBounds(5, 143, 390, 49);
        getpaRelationR().add(getcbRelationsR(), getcbRelationsR().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpaRelationR;
  }

  /**
   * Devolver el valor de la propiedad JTextPane1.
   * @return javax.swing.JTextPane
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JTextPane gettaText() {
    if (ivjtaText == null) {
      try {

        ivjtaText = new javax.swing.JTextPane();
        ivjtaText.setName("taText");
        ivjtaText.setBounds(0, 0, 257, 113);

        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjtaText;
  }
  /**
   * Método al que se llama siempre que el componente lanza una excepción.
   * @param exception java.lang.Throwable
   */
  private void handleException(java.lang.Throwable exception) {

 /* Elimine el comentario de las líneas siguientes para imprimir en la salida estándar las excepciones no capturadas */
    // System.out.println("--------- EXCEPCIÓN NO CAPTURADA ---------");
    // exception.printStackTrace(System.out);
  }
  /**
   * Inicializa el applet.
   *
   * @see #start
   * @see #stop
   * @see #destroy
   */
  public void init() {
    try {

      String Concept=getParameter("Concept");
      String Relation=getParameter("Relation");
      String Attribute=getParameter("Attribute");
      String Constant=getParameter("Constant");
      name=getParameter("fname");
      expression=getParameter("fexpression");
      description=getParameter("fdescription");
      type=getParameter("ftype");

      setName("AxiomClientApplet");
      setSize(800, 225);

      dummyHeaderA.addElement("");
      dummyHeaderC.addElement("");

      dmA.setDataVector(new Vector(),dummyHeaderA);
      dmC.setDataVector(new Vector(),dummyHeaderC);


      tableA.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      tableC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      tableA.setBackground(Color.lightGray);
      tableC.setBackground(Color.lightGray);
      tableA.setShowVerticalLines(false);
      tableC.setShowVerticalLines(false);


      scrollTableA.setColumnHeader(null);
      scrollTableC.setColumnHeader(null);
      scrollTableA.setBounds(5, 11, 319, 62);
      scrollTableC.setBounds(470, 11, 300, 62);

      setContentPane(getJAppletContentPane());
      // user code begin {1}
      doc=new DefaultStyledDocument();
      gettaText().setDocument(doc);
      StyleConstants.setForeground(attr_var, Color.red);
      StyleConstants.setForeground(attr_word, Color.blue);
      StyleConstants.setForeground(attr_update, Color.orange);

      if(name!=null && type.equals("axiom")){

        doc.insertString(0,expression, attr_update);



      }else if (name!=null && type.equals("rule")){

        ponerListaReglas();
        try{
          getJTabbedPane1().setSelectedIndex(1);
        }catch(IllegalArgumentException excep){

        }
      }


      _setComboBox();
      _setComboBoxR();



      getpaConcept().setBorder(
                               BorderFactory.createCompoundBorder(
                               BorderFactory.createCompoundBorder(
                               BorderFactory.createTitledBorder(Concept),
                               BorderFactory.createEmptyBorder(5,5,5,5)),
                               getpaConcept().getBorder()));

      getpaConceptR().setBorder(
                                BorderFactory.createCompoundBorder(
                                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(Concept),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                                getpaConceptR().getBorder()));


      getpaAttribute().setBorder(
                                 BorderFactory.createCompoundBorder(
                                 BorderFactory.createCompoundBorder(
                                 BorderFactory.createTitledBorder(Attribute),
                                 BorderFactory.createEmptyBorder(5,5,5,5)),
                                 getpaAttribute().getBorder()));

      getpaAttributeR().setBorder(
                                  BorderFactory.createCompoundBorder(
                                  BorderFactory.createCompoundBorder(
                                  BorderFactory.createTitledBorder(Attribute),
                                  BorderFactory.createEmptyBorder(5,5,5,5)),
                                  getpaAttributeR().getBorder()));


      getpaRelation().setBorder(
                                BorderFactory.createCompoundBorder(
                                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(Relation),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                                getpaRelation().getBorder()));

      getpaRelationR().setBorder(
                                 BorderFactory.createCompoundBorder(
                                 BorderFactory.createCompoundBorder(
                                 BorderFactory.createTitledBorder(Relation),
                                 BorderFactory.createEmptyBorder(5,5,5,5)),
                                 getpaRelationR().getBorder()));


      getpaConstant().setBorder(
                                BorderFactory.createCompoundBorder(
                                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(Constant),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                                getpaConstant().getBorder()));


      getpaConstantR().setBorder(
                                 BorderFactory.createCompoundBorder(
                                 BorderFactory.createCompoundBorder(
                                 BorderFactory.createTitledBorder(Constant),
                                 BorderFactory.createEmptyBorder(5,5,5,5)),
                                 getpaConstantR().getBorder()));







      getcbConcepts().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

          if(evento){

            JComboBox cb = (JComboBox)e.getSource();
            gettaText().replaceSelection("");
            int pos=gettaText().getSelectionStart();

            try
            {
              String sub=(String)cb.getSelectedItem();
              doc.insertString(pos,
                               sub+"(",null);
              doc.insertString(pos + 1 + sub.length(),
                               "?"+sub,attr_var);
              doc.insertString(pos+(2*sub.length())+2,
                               ")", null);




            }catch (BadLocationException exc) {

            }catch (Exception except){
              System.out.println(except);
            }
            gettaText().repaint();


            _setCbAttributes((String)cb.getSelectedItem());
          _setCbRelations((String)cb.getSelectedItem());}


        }
      });
      getcbConceptsR().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

          if(eventoR){

            JComboBox cb = (JComboBox)e.getSource();


            try
            {
              String sub=(String)cb.getSelectedItem();
              String expresion= sub+"(?"+sub+")";
              if(!sub.equals("")){
                if(getrbAntecedenteR().isSelected()){
                  dmA.addRow(new  String [] {expresion});
                }else if (dmC.getRowCount() ==0) {
                  dmC.addRow(new  String [] {expresion});

                }
              }


            }catch (Exception except){
              System.out.println(except);
            }



            _setCbAttributesR((String)cb.getSelectedItem());
          _setCbRelationsR((String)cb.getSelectedItem());}


        }
      });


      getcbAttributes().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

          if(evento){

            JComboBox cb = (JComboBox)e.getSource();
            gettaText().replaceSelection("");
            int pos=gettaText().getSelectionStart();

            try
            {
              String sub=(String)cb.getSelectedItem();
              doc.insertString(pos,
                               sub+"(",null);
              doc.insertString(pos + 1 + sub.length(),
                               "?"+(String)getcbConcepts().getSelectedItem(),attr_var);
              doc.insertString(pos +2 + sub.length()+((String)getcbConcepts().getSelectedItem()).length(),
                               ",",null);

              doc.insertString(pos +3 + sub.length()+((String)getcbConcepts().getSelectedItem()).length(),
                               "?"+sub,attr_var);

              doc.insertString(pos +4 + (2*sub.length())+((String)getcbConcepts().getSelectedItem()).length(),
                               ")", null);




            }catch (BadLocationException exc) {

            }
            gettaText().repaint();
          }



        }
      });

      getcbAttributesR().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {



          if(eventoR){

            JComboBox cb = (JComboBox)e.getSource();


            try
            {
              String sub=(String)cb.getSelectedItem();
              String expresion= sub+"(?"+(String)getcbConceptsR().getSelectedItem()+",?"+sub+")";
              if(!sub.equals("")){
                if(getrbAntecedenteR().isSelected()){
                  dmA.addRow(new  String [] {expresion});
                }else if (dmC.getRowCount() ==0){
                  dmC.addRow(new  String [] {expresion});

                }
              }


            }catch (Exception except){
              System.out.println(except);
            }


          }
        }
      });


      getcbRelationsR().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(eventoR){
            JComboBox cb = (JComboBox)e.getSource();



            try
            {
              String sub=(String)cb.getSelectedItem();
              int principio=sub.indexOf("-$-")+3;
              int fin=sub.lastIndexOf("-$-");
              String concepto1=sub.substring(0,principio-3);
              String concepto2=sub.substring(fin+3,sub.length());
              sub=sub.substring(principio,fin);

              String expresion= sub+"(?"+concepto1+",?"+concepto2+")";
              if(!sub.equals("")){
                if(getrbAntecedenteR().isSelected()){
                  dmA.addRow(new  String [] {expresion});
                }else if (dmC.getRowCount() ==0){
                  dmC.addRow(new  String [] {expresion});

                }
              }

            }catch (Exception exc) {

            }



          }

        }
      });

      getcbRelations().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {


          if(evento){
            JComboBox cb = (JComboBox)e.getSource();

            gettaText().replaceSelection("");
            int pos=gettaText().getSelectionStart();

            try
            {
              String sub=(String)cb.getSelectedItem();
              int principio=sub.indexOf("-$-")+3;
              int fin=sub.lastIndexOf("-$-");
              String concepto1=sub.substring(0,principio-3);
              String concepto2=sub.substring(fin+3,sub.length());
              sub=sub.substring(principio,fin);


              doc.insertString(pos,
                               sub+"(",null);
              doc.insertString(pos + 1 + sub.length(),
                               "?"+concepto1,attr_var);
              doc.insertString(pos + 2 + sub.length()+concepto1.length(),
                               ",",null);

              doc.insertString(pos + 3 + sub.length()+concepto1.length(),
                               "?"+concepto2,attr_var);

              doc.insertString(pos + 4 + sub.length()+concepto1.length()+concepto2.length(),
                               ")", null);

            }catch (BadLocationException exc) {

            }
          gettaText().repaint();}


        }
      });

      getcbConstants().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(evento){
            JComboBox cb = (JComboBox)e.getSource();

            gettaText().replaceSelection("");
            int pos=gettaText().getSelectionStart();
            try
            {
              doc.insertString(pos,
                               (String)cb.getSelectedItem(),null);

            }catch (BadLocationException exc) {

            }
          gettaText().repaint();}




        }
      });


      getcbConstantsR().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if(eventoR){

            JComboBox cb = (JComboBox)e.getSource();


            try
            {
              String sub=(String)cb.getSelectedItem();
              if(!sub.equals("")){
                if(getrbAntecedenteR().isSelected()){
                  dmA.addRow(new  String [] {sub});
                }else if (dmC.getRowCount() ==0){
                  dmC.addRow(new  String [] {sub});

                }
              }

            }catch (Exception except){
              System.out.println(except);
            }


          }





        }
      });

      getbtForall().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

          //taText.insert(" forall(variables)(expression)",taText.getSelectionEnd());
          gettaText().replaceSelection("");
          int pos=gettaText().getSelectionStart();
          try
          {
            doc.insertString(pos,
                             " forall", attr_word);
            doc.insertString(pos+7,
                             "(", attr_word);
            doc.insertString(pos+8,
                             "variables", attr_var);

            doc.insertString(pos+17,
                             ")(", null);
            doc.insertString(pos+19,
                             "expression", attr_var);

            doc.insertString(pos+29,
                             ")", null);


          }catch (BadLocationException exc) {

          }
          gettaText().repaint();
        }
      });
      getbtExists().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          //taText.insert(" exists(variables)(expression)",taText.getSelectionEnd());
          gettaText().replaceSelection("");
          int pos=gettaText().getSelectionStart();
          try
          {
            doc.insertString(pos,
                             " exists", attr_word);

            doc.insertString(pos+7,
                             "(", attr_word);

            doc.insertString(pos+8,
                             "variables", attr_var);

            doc.insertString(pos+17,
                             ")(", null);
            doc.insertString(pos+19,
                             "expression", attr_var);

            doc.insertString(pos+29,
                             ")", null);


          }catch (BadLocationException exc) {

          }


          gettaText().repaint();
        }
      });
      getbtImp().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          //taText.insert(" -> expression",taText.getSelectionEnd());
          gettaText().replaceSelection("");
          int pos=gettaText().getSelectionStart();
          try
          {
            doc.insertString(pos,
                             " ->", attr_word);

            doc.insertString(pos+3,
                             " expression", attr_var);


          }catch (BadLocationException exc) {

          }
          gettaText().repaint();


        }
      });
      getbtDImp().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          //taText.insert(" <-> expression",taText.getSelectionEnd());
          gettaText().replaceSelection("");
          int pos=gettaText().getSelectionStart();
          try
          {
            doc.insertString(pos,
                             " <->", attr_word);

            doc.insertString(pos+4,
                             " expression", attr_var);


          }catch (BadLocationException exc) {

          }
          gettaText().repaint();


        }
      });
      getbtNot().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          //taText.insert(" not expression",taText.getSelectionEnd());
          gettaText().replaceSelection("");
          int pos=gettaText().getSelectionStart();
          try
          {
            doc.insertString(pos,
                             " not", attr_word);

            doc.insertString(pos+4,
                             " expression", attr_var);


          }catch (BadLocationException exc) {

          }
          gettaText().repaint();


        }
      });
      getbtAnd().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          //taText.insert(" and expression",taText.getSelectionEnd());
          gettaText().replaceSelection("");
          int pos=gettaText().getSelectionStart();
          try
          {
            doc.insertString(pos,
                             " and", attr_word);

            doc.insertString(pos+4,
                             " expression", attr_var);


          }catch (BadLocationException exc) {

          }


          gettaText().repaint();
        }
      });
      getbtOr().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          //taText.insert(" or expression",taText.getSelectionEnd());
          gettaText().replaceSelection("");
          int pos=gettaText().getSelectionStart();
          try
          {
            doc.insertString(pos,
                             " or", attr_word);


            doc.insertString(pos+3,
                             " expression", attr_var);


          }catch (BadLocationException exc) {

          }


          gettaText().repaint();
        }
      });
      getbtSend().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {



          try
          {

            if (name==null){
//						getAppletContext().showDocument(new java.net.URL(getDocumentBase()+"ShowAxiomResult.jsp?axiom_expression="+doc.getText(doc.getStartPosition().getOffset(),doc.getEndPosition().getOffset())), "AxiomResult");
              getAppletContext().showDocument(new java.net.URL(new java.net.URL(getDocumentBase(),"./")+"ShowAxiomResult.jsp?axiom_expression="+doc.getText(doc.getStartPosition().getOffset(),doc.getEndPosition().getOffset())), "AxiomResult");
            }else{
//						getAppletContext().showDocument(new java.net.URL(getDocumentBase()+"ShowAxiomResultUpdate.jsp?axiom_expression="+doc.getText(doc.getStartPosition().getOffset(),doc.getEndPosition().getOffset())+"&fname="+name+"&fdescription="+description), "AxiomResult");
              getAppletContext().showDocument(new java.net.URL(new java.net.URL(getDocumentBase(),"./")+"ShowAxiomResultUpdate.jsp?axiom_expression="+doc.getText(doc.getStartPosition().getOffset(),doc.getEndPosition().getOffset())+"&fname="+name+"&fdescription="+description), "AxiomResult");

            }
            }catch(MalformedURLException exc)
            {
              getAppletContext().showStatus("URL not found");
              }catch(BadLocationException exc)
              {
                getAppletContext().showStatus(exc.toString());
              }




        }

      });

      getbtSendR().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {


          int nfilasA=dmA.getRowCount();
          String antecedente=new String();
          for(int i=0;i<nfilasA;i++){

            antecedente=(String)((Vector)dmA.getDataVector().elementAt(i)).elementAt(0)+" and "+antecedente;

          }
          try{

            antecedente=antecedente.substring(0,antecedente.length()-5);


          }catch (StringIndexOutOfBoundsException excep){
          }


          int nfilasC=dmC.getRowCount();
          String consecuente=new String();
          for(int i=0;i<nfilasC;i++){

            consecuente=(String)((Vector)dmC.getDataVector().elementAt(i)).elementAt(0)+" and "+consecuente;

          }
          try{

            consecuente=consecuente.substring(0,consecuente.length()-5);


          }catch (StringIndexOutOfBoundsException excep){
          }

          String regla= "if "+ antecedente + " then " + consecuente;







          try
          {

            if (name==null){
//						getAppletContext().showDocument(new java.net.URL(getDocumentBase()+"ShowAxiomResult.jsp?rule_expression="+regla), "AxiomResult");
              getAppletContext().showDocument(new java.net.URL(new java.net.URL(getDocumentBase(),"./")+"ShowAxiomResult.jsp?rule_expression="+regla), "AxiomResult");
            }else{
//						getAppletContext().showDocument(new java.net.URL(getDocumentBase()+"ShowAxiomResultUpdate.jsp?rule_expression="+regla+"&fname="+name+"&fdescription="+description), "AxiomResult");
              getAppletContext().showDocument(new java.net.URL(new java.net.URL(getDocumentBase(),"./")+"ShowAxiomResultUpdate.jsp?rule_expression="+regla+"&fname="+name+"&fdescription="+description), "AxiomResult");
            }
            }catch(MalformedURLException exc)
            {
              getAppletContext().showStatus("URL not found");
            }




        }

      });
      getbtUndo().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            undo.undo();
          } catch (CannotUndoException ex) {
            System.out.println("Unable to undo: " + ex);
            ex.printStackTrace();
          }


        }
      });
      getbtRedo().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            undo.redo();
          } catch (CannotRedoException ex) {
            System.out.println("Unable to redo: " + ex);
            ex.printStackTrace();
          }


        }
      });


      getbtDeleteA().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

          try{

            dmA.removeRow(tableA.getSelectedRow());

          }catch(ArrayIndexOutOfBoundsException exc){

          }


        }
      });

      getbtDeleteCR().addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

          try{

            dmC.removeRow(tableC.getSelectedRow());

          }catch(ArrayIndexOutOfBoundsException exc){

          }


        }
      });


      doc.addUndoableEditListener(new UndoableEditListener(){

        public void undoableEditHappened(UndoableEditEvent e) {
          //Recuerda la edición y actualiza los menús
          undo.addEdit(e.getEdit());

        }


      });

      group.add(getrbAntecedenteR());
      group.add(getrbConsecuenteR());




      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {2}
      // user code end
      handleException(ivjExc);
    }
  }
  /**
   * Dibuja el applet.
   * Si el applet no precisa dibujarse (por ejemplo, si simplemente es un contenedor de otros
   * componentes awt) esté método puede eliminarse de forma segura.
   *
   * @param g  la ventana de gráficos especificada
   * @see #update
   */
  public void paint(Graphics g) {
    super.paint(g);

    // inserte aquí el código para dibujar el applet
  }

}
