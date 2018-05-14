package es.upm.fi.dia.ontology.webode.ui.Inference;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Object.*;
import java.net.*;
import java.io.*;
//import javax.swing.*;
//import javax.swing.event.*;
//import javax.swing.tree.*;
//import javax.swing.text.*;


import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;

import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.webode.servlet.*;
import es.upm.fi.dia.ontology.webode.Inference.*;
import es.upm.fi.dia.ontology.webode.ui.Inference.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.client.*;

import java.util.*;

/**
 * Inserte aquí la descripción del tipo.
 * Fecha de creación: (27/7/01 14:18:43)
 * @author:
 */
public class InferenceJApplet extends JApplet {
  static String Class="";


  //  public static final String PARENT  = "Parents";

  private JButton ivjclearB = null;
  private JPanel ivjJAppletContentPane = null;
  private JScrollPane ivjJScrollPane1 = null;
  private JScrollPane ivjJScrollPane11 = null;
  private JScrollPane ivjJScrollPane2 = null;
  private JSeparator ivjJSeparator1 = null;
  private JComboBox ivjlist = null;
  private JPanel ivjpanelCBs = null;
  private JPanel ivjpanelQuery = null;
  private JTextArea ivjprogram = null;
  private JTextPane ivjqueryTP = null;
  private JButton ivjresetB = null;
  private JTextArea ivjresults = null;
  private JButton ivjsaveB = null;
  private JButton ivjsubmitB = null;
  private JComboBox ivjvar1 = null;
  private JComboBox ivjvar2 = null;
  private JComboBox ivjvar3 = null;
  private JComboBox ivjvar4 = null;
  private JLabel ivjresultL = null;
/*	private javax.swing.text.StyledDocument doc;
 private javax.swing.text.MutableAttributeSet attr_var = new SimpleAttributeSet();
 private javax.swing.text.MutableAttributeSet attr_word = new SimpleAttributeSet();
*/
  private javax.swing.text.StyledDocument doc;
  private javax.swing.text.MutableAttributeSet attr_var = new SimpleAttributeSet();
  private javax.swing.text.MutableAttributeSet attr_word = new SimpleAttributeSet();
  private Vector classes = new Vector();
  private Vector instances= new Vector();
  private Vector class_atr= new Vector();
  private Vector instance_atr= new Vector();
  private Vector modules = new Vector();
  private boolean stateRule = false;
  IvjEventHandler ivjEventHandler = new IvjEventHandler();
  private JPanel ivjpanelModules = null;
  private JComboBox ivjloadCB = null;
  private JRadioButton ivjruleB = null;
  private JTextField ivjJTextField1 = null;


  class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.ItemListener {
    public void actionPerformed(java.awt.event.ActionEvent e) {
      if (e.getSource() == InferenceJApplet.this.getlist())
        connEtoC1(e);
      if (e.getSource() == InferenceJApplet.this.getresetB())
        connEtoC3(e);
      if (e.getSource() == InferenceJApplet.this.getsubmitB())
        connEtoC5(e);
      if (e.getSource() == InferenceJApplet.this.getclearB())
        connEtoC6(e);
      if (e.getSource() == InferenceJApplet.this.getvar1())
        connEtoC7(e);
      if (e.getSource() == InferenceJApplet.this.getvar2())
        connEtoC8(e);
      if (e.getSource() == InferenceJApplet.this.getvar3())
        connEtoC9(e);
      if (e.getSource() == InferenceJApplet.this.getvar4())
        connEtoC10(e);
      if (e.getSource() == InferenceJApplet.this.getsaveB())
        connEtoC11(e);
      if (e.getSource() == InferenceJApplet.this.getloadCB())
        connEtoC13(e);
    };
    public void itemStateChanged(java.awt.event.ItemEvent e) {
      if (e.getSource() == InferenceJApplet.this.getruleB())
        connEtoC2(e);
    };
  };

  /**
  * Comment
  */
  public void clearB_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    getprogram().setText(null);
    return;
  }
  /**
   * connEtoC1:  (list.action.actionPerformed(java.awt.event.ActionEvent) --> InferenceJApplet.list_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
   * @param arg1 java.awt.event.ActionEvent
   */
  /* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC1(java.awt.event.ActionEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.list_ActionPerformed(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }
  /**
   * connEtoC10:  (var4.action.actionPerformed(java.awt.event.ActionEvent) --> InferenceJApplet.var4_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
   * @param arg1 java.awt.event.ActionEvent
   */
  /* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC10(java.awt.event.ActionEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.var4_ActionPerformed(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }

  /**
   * connEtoC11:  (saveB.action.actionPerformed(java.awt.event.ActionEvent) --> InferenceJApplet.saveB_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
   * @param arg1 java.awt.event.ActionEvent
   */
  /* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC11(java.awt.event.ActionEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.saveB_ActionPerformed(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }

  /**
   * connEtoC13:  (loadCB.action.actionPerformed(java.awt.event.ActionEvent) --> InferenceJApplet.loadCB_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
   * @param arg1 java.awt.event.ActionEvent
   */
  /* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC13(java.awt.event.ActionEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.loadCB_ActionPerformed(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }

  /**
   * connEtoC2:  (ruleB.item.itemStateChanged(java.awt.event.ItemEvent) --> InferenceJApplet.ruleB_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
   * @param arg1 java.awt.event.ItemEvent
   */
  /* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC2(java.awt.event.ItemEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.ruleB_ItemStateChanged(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }

  /**
   * connEtoC3:  (resetB.action.actionPerformed(java.awt.event.ActionEvent) --> InferenceJApplet.resetB_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
   * @param arg1 java.awt.event.ActionEvent
   */
  /* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC3(java.awt.event.ActionEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.resetB_ActionPerformed(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }

  /**
   * connEtoC5:  (submitB.action.actionPerformed(java.awt.event.ActionEvent) --> InferenceJApplet.submitB_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
   * @param arg1 java.awt.event.ActionEvent
   */
  /* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC5(java.awt.event.ActionEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.submitB_ActionPerformed(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }

  /**
   * connEtoC6:  (clearB.action.actionPerformed(java.awt.event.ActionEvent) --> InferenceJApplet.clearB_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
   * @param arg1 java.awt.event.ActionEvent
   */
  /* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC6(java.awt.event.ActionEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.clearB_ActionPerformed(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }

  /**
   * connEtoC7:  (var1.action.actionPerformed(java.awt.event.ActionEvent) --> InferenceJApplet.var1_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
   * @param arg1 java.awt.event.ActionEvent
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC7(java.awt.event.ActionEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.var1_ActionPerformed(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }
  /**
   * connEtoC8:  (var2.action.actionPerformed(java.awt.event.ActionEvent) --> InferenceJApplet.var2_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
   * @param arg1 java.awt.event.ActionEvent
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC8(java.awt.event.ActionEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.var2_ActionPerformed(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }
  /**
   * connEtoC9:  (var3.action.actionPerformed(java.awt.event.ActionEvent) --> InferenceJApplet.var3_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
   * @param arg1 java.awt.event.ActionEvent
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private void connEtoC9(java.awt.event.ActionEvent arg1) {
    try {
      // user code begin {1}
      // user code end
      this.var3_ActionPerformed(arg1);
      // user code begin {2}
      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {3}
      // user code end
      handleException(ivjExc);
    }
  }
  /**
   * Libera los recursos retenidos. Si el applet está activo
   * se detiene.
   *
   * @see #init
   * @see #start
   * @see #stop
   */
  public void destroy() {
    super.destroy();

    // inserte aquí el código para liberar recursos
  }
  /**
   * Retorna información acerca de este applet.
   * @return una serie de caracteres de información acerca de este applet
   */
  public String getAppletInfo() {
    return "InferenceJApplet\n" +
        "\n" +
        "Inserte aquí la descripción del tipo.\n" +
        "Fecha de creación: (27/7/01 14:18:34)\n" +
        "@author: \n" +
        "";
  }
  /**
   * Devolver el valor de la propiedad clearB.
   * @return javax.swing.JButton
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JButton getclearB() {
    String Clear=getParameter("Clear");
    if (ivjclearB == null) {
      try {
        ivjclearB = new javax.swing.JButton();
        ivjclearB.setName("clearB");
        ivjclearB.setText(Clear);
        ivjclearB.setBounds(595, 86, 96, 19);
        ivjclearB.setForeground(new java.awt.Color(102,102,153));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjclearB;
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
        getJAppletContentPane().add(getpanelQuery(), getpanelQuery().getName());
        getJAppletContentPane().add(getpanelCBs(), getpanelCBs().getName());
        getJAppletContentPane().add(getJSeparator1(), getJSeparator1().getName());
        getJAppletContentPane().add(getpanelModules(), getpanelModules().getName());
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
   * Devolver el valor de la propiedad JScrollPane1.
   * @return javax.swing.JScrollPane
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JScrollPane getJScrollPane1() {
    if (ivjJScrollPane1 == null) {
      try {
        ivjJScrollPane1 = new javax.swing.JScrollPane();
        ivjJScrollPane1.setName("JScrollPane1");
        ivjJScrollPane1.setBounds(21, 42, 553, 62);
        getJScrollPane1().setViewportView(getprogram());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjJScrollPane1;
  }
  /**
   * Devolver el valor de la propiedad JScrollPane11.
   * @return javax.swing.JScrollPane
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JScrollPane getJScrollPane11() {
    if (ivjJScrollPane11 == null) {
      try {
        ivjJScrollPane11 = new javax.swing.JScrollPane();
        ivjJScrollPane11.setName("JScrollPane11");
        ivjJScrollPane11.setBounds(15, 76, 676, 118);
        getJScrollPane11().setViewportView(getresults());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjJScrollPane11;
  }
  /**
   * Devolver el valor de la propiedad JScrollPane2.
   * @return javax.swing.JScrollPane
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JScrollPane getJScrollPane2() {
    if (ivjJScrollPane2 == null) {
      try {
        ivjJScrollPane2 = new javax.swing.JScrollPane();
        ivjJScrollPane2.setName("JScrollPane2");
        ivjJScrollPane2.setBounds(15, 26, 676, 28);
        getJScrollPane2().setViewportView(getqueryTP());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjJScrollPane2;
  }
  /**
   * Devolver el valor de la propiedad JSeparator1.
   * @return javax.swing.JSeparator
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JSeparator getJSeparator1() {
    if (ivjJSeparator1 == null) {
      try {
        ivjJSeparator1 = new javax.swing.JSeparator();
        ivjJSeparator1.setName("JSeparator1");
        ivjJSeparator1.setBounds(20, 322, 704, 7);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjJSeparator1;
  }
  /**
   * Devolver el valor de la propiedad JTextField1.
   * @return javax.swing.JTextField
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */
  private javax.swing.JTextField getJTextField1() {
    if (ivjJTextField1 == null) {
      try {
        ivjJTextField1 = new javax.swing.JTextField();
        ivjJTextField1.setName("JTextField1");
        ivjJTextField1.setBounds(595, 64, 96, 19);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjJTextField1;
  }
  /**
   * Devolver el valor de la propiedad list.
   * @return javax.swing.JComboBox
   */
/* AVISO: ESTE MÉTODO SE REGENERARÁ. */


  private javax.swing.JComboBox getlist() {

    String a=getParameter("SelectaOKBCPrimitive");
    String b=getParameter("get-class-instances");
    String c=getParameter("get-class-subclasses");
    String d=getParameter("getclasssuperclasses");
    String e=getParameter("get-slots");
    String f=getParameter("get-instance-types");
    String g=getParameter("get-slot-domain");
    String h=getParameter("get-slot-facets");
    String i=getParameter("get-slot-type");
    String j=getParameter("get-slot-value");
    String k=getParameter("get-slot-values");
    String l=getParameter("get-slot-values-in-detail");
    String m=getParameter("individual-p");
    String n=getParameter("instance-of-p");
    String o=getParameter("get-frame-details");
    String p=getParameter("get-frame-details");
    String q=getParameter("member-slot-value-p");
    String r=getParameter("member-facet-value-p");
    String s=getParameter("get-facet-values");
    if (ivjlist == null) {
      try {
        ivjlist = new javax.swing.JComboBox();
        ivjlist.setName("list");
        ivjlist.setBounds(12, 21, 187, 19);
        ivjlist.setForeground(new java.awt.Color(102,102,153));
        // user code begin {1}
        ivjlist.addItem("Select a OKBC Primitive");
        ivjlist.addItem("get-class-instances");
        ivjlist.addItem("get-class-subclasses");
        ivjlist.addItem("get-class-superclasses");
        ivjlist.addItem("get-slots");
        ivjlist.addItem("get-instance-types");
        ivjlist.addItem("get-slot-domain");
        ivjlist.addItem("get-slot-facets");
        ivjlist.addItem("get-slot-type");
        ivjlist.addItem("get-slot-value");
        ivjlist.addItem("get-slot-values");
        ivjlist.addItem("get-slot-values-in-detail");
        ivjlist.addItem("individual-p");
        ivjlist.addItem("instance-of-p");
        ivjlist.addItem("get-frame-details");
        ivjlist.addItem("member-slot-value-p");
        ivjlist.addItem("member-facet-value-p");
        ivjlist.addItem("get-facet-value");
        ivjlist.addItem("get-facet-values");

      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjlist;
  }
  /**
   * Devolver el valor de la propiedad loadCB.
   * @return javax.swing.JComboBox
   */
  private javax.swing.JComboBox getloadCB() {

    String loadCB=getParameter("loadCB");
    String Load=getParameter("Load");
    if (ivjloadCB == null) {
      try {
        ivjloadCB = new javax.swing.JComboBox();
        ivjloadCB.setName(loadCB);
        ivjloadCB.setBounds(595, 16, 96, 19);

        // user code begin {1}
        ivjloadCB.addItem(Load);
        // user code end

      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjloadCB;
  }
  /**
   * Devolver el valor de la propiedad panelCBs.
   * @return javax.swing.JPanel
   */

  private javax.swing.JPanel getpanelCBs() {
    if (ivjpanelCBs == null) {
      try {
        ivjpanelCBs = new javax.swing.JPanel();
        ivjpanelCBs.setName("panelCBs");
        ivjpanelCBs.setLayout(null);
        ivjpanelCBs.setBounds(20, 14, 703, 75);
        getpanelCBs().add(getvar1(), getvar1().getName());
        getpanelCBs().add(getvar3(), getvar3().getName());
        getpanelCBs().add(getlist(), getlist().getName());
        getpanelCBs().add(getvar2(), getvar2().getName());
        getpanelCBs().add(getvar4(), getvar4().getName());
        getpanelCBs().add(getsubmitB(), getsubmitB().getName());
        getpanelCBs().add(getresetB(), getresetB().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpanelCBs;
  }
  /**
   * Devolver el valor de la propiedad JPanel1.
   * @return javax.swing.JPanel
   */

  private javax.swing.JPanel getpanelModules() {
    if (ivjpanelModules == null) {
      try {
        ivjpanelModules = new javax.swing.JPanel();
        ivjpanelModules.setName("panelModules");
        ivjpanelModules.setLayout(null);
        ivjpanelModules.setBounds(21, 337, 703, 119);
        getpanelModules().add(getruleB(), getruleB().getName());
        getpanelModules().add(getJScrollPane1(), getJScrollPane1().getName());
        getpanelModules().add(getloadCB(), getloadCB().getName());
        getpanelModules().add(getsaveB(), getsaveB().getName());
        getpanelModules().add(getJTextField1(), getJTextField1().getName());
        getpanelModules().add(getclearB(), getclearB().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpanelModules;
  }
  /**
   * Devolver el valor de la propiedad panelQuery.
   * @return javax.swing.JPanel
   */

  private javax.swing.JPanel getpanelQuery() {
    if (ivjpanelQuery == null) {
      try {
        ivjpanelQuery = new javax.swing.JPanel();
        ivjpanelQuery.setName("panelQuery");
        ivjpanelQuery.setLayout(null);
        ivjpanelQuery.setBounds(20, 97, 703,  214);
        getpanelQuery().add(getJScrollPane2(), getJScrollPane2().getName());
        getpanelQuery().add(getJScrollPane11(), getJScrollPane11().getName());
        getpanelQuery().add(getresultL(), getresultL().getName());
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjpanelQuery;
  }
  /**
   * Devolver el valor de la propiedad program.
   * @return javax.swing.JTextArea
   */

  private javax.swing.JTextArea getprogram() {
    if (ivjprogram == null) {
      try {
        ivjprogram = new javax.swing.JTextArea();
        ivjprogram.setName("program");
        ivjprogram.setBounds(0, 0, 550, 50);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjprogram;
  }
  /**
   * Devolver el valor de la propiedad queryTP.
   * @return javax.swing.JTextPane
   */

  private javax.swing.JTextPane getqueryTP() {
    if (ivjqueryTP == null) {
      try {
        ivjqueryTP = new javax.swing.JTextPane();
        ivjqueryTP.setName("queryTP");
        ivjqueryTP.setBounds(0, 0, 673, 25);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjqueryTP;
  }
  /**
   * Devolver el valor de la propiedad resetB.
   * @return javax.swing.JButton
   */

  private javax.swing.JButton getresetB() {
    //String resetB=getParameter("resetB");
    String Reset=getParameter("Reset");
    if (ivjresetB == null) {
      try {
        ivjresetB = new javax.swing.JButton();
        //ivjresetB.setName(resetb);
        ivjresetB.setName("resetb");
        ivjresetB.setText(Reset);
        //ivjresetB.setText(ResetB);
        ivjresetB.setBounds(615, 46, 74, 19);
        ivjresetB.setForeground(new java.awt.Color(102,102,153));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjresetB;
  }
  /**
   * Devolver el valor de la propiedad resultL.
   * @return javax.swing.JLabel
   */
  private javax.swing.JLabel getresultL() {
    String Results=getParameter("Results");
    if (ivjresultL == null) {
      try {
        ivjresultL = new javax.swing.JLabel();
        ivjresultL.setName("resultl");
        ivjresultL.setText(Results);
        ivjresultL.setBounds(15, 62, 50, 9);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjresultL;
  }

  /**
   * Devolver el valor de la propiedad results.
   * @return javax.swing.JTextArea
   */
  private javax.swing.JTextArea getresults() {
    if (ivjresults == null) {
      try {
        ivjresults = new javax.swing.JTextArea();
        ivjresults.setName("results");
        ivjresults.setBounds(0, 0, 673, 52);
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjresults;
  }

  /**
   * Devolver el valor de la propiedad ruleB.
   * @return javax.swing.JRadioButton
   */
  private javax.swing.JRadioButton getruleB() {

    String Rules=getParameter("Rules");
    if (ivjruleB == null) {
      try {
        ivjruleB = new javax.swing.JRadioButton();
        ivjruleB.setName("ruleB");
        ivjruleB.setText(Rules);
        ivjruleB.setBounds(21, 17, 110, 18);
        ivjruleB.setForeground(new java.awt.Color(102,102,153));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjruleB;
  }

  /**
   * Devolver el valor de la propiedad saveB.
   * @return javax.swing.JButton
   */
  private javax.swing.JButton getsaveB() {
    String Save=getParameter("Save");
    if (ivjsaveB == null) {
      try {
        ivjsaveB = new javax.swing.JButton();
        ivjsaveB.setName("saveB");
        ivjsaveB.setText(Save);
        ivjsaveB.setBounds(595, 40, 96, 19);
        ivjsaveB.setForeground(new java.awt.Color(102,102,153));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjsaveB;
  }

  /**
   * Devolver el valor de la propiedad submitB.
   * @return javax.swing.JButton
   */

  private javax.swing.JButton getsubmitB() {
    //String SubmitB=getParameter("submitB");
    String Submit=getParameter("submit");
    if (ivjsubmitB == null) {
      try {
        ivjsubmitB = new javax.swing.JButton();
        ivjsubmitB.setName("submitB");
        ivjsubmitB.setText(Submit);
        ivjsubmitB.setBounds(615, 21, 74, 19);
        ivjsubmitB.setForeground(new java.awt.Color(102,102,153));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjsubmitB;
  }
  /**
   * Devolver el valor de la propiedad var1.
   * @return javax.swing.JComboBox
   */
  private javax.swing.JComboBox getvar1() {
    if (ivjvar1 == null) {
      try {
        ivjvar1 = new javax.swing.JComboBox();
        ivjvar1.setName("var1");
        ivjvar1.setBounds(208, 21, 187, 19);
        ivjvar1.setForeground(new java.awt.Color(102,102,153));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjvar1;
  }
  /**
   * Devolver el valor de la propiedad var2.
   * @return javax.swing.JComboBox
   */
  private javax.swing.JComboBox getvar2() {
    if (ivjvar2 == null) {
      try {
        ivjvar2 = new javax.swing.JComboBox();
        ivjvar2.setName("var2");
        ivjvar2.setBounds(408, 21, 187, 19);
        ivjvar2.setForeground(new java.awt.Color(102,102,153));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjvar2;
  }
  /**
   * Devolver el valor de la propiedad var3.
   * @return javax.swing.JComboBox
   */

  private javax.swing.JComboBox getvar3() {
    if (ivjvar3 == null) {
      try {
        ivjvar3 = new javax.swing.JComboBox();
        ivjvar3.setName("var3");
        ivjvar3.setBounds(208, 46, 187, 19);
        ivjvar3.setForeground(new java.awt.Color(102,102,153));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjvar3;
  }
  /**
   * Devolver el valor de la propiedad var4.
   * @return javax.swing.JComboBox
   */

  private javax.swing.JComboBox getvar4() {
    if (ivjvar4 == null) {
      try {
        ivjvar4 = new javax.swing.JComboBox();
        ivjvar4.setName("var4");
        ivjvar4.setBounds(408, 46, 187, 19);
        ivjvar4.setForeground(new java.awt.Color(102,102,153));
        // user code begin {1}
        // user code end
      } catch (java.lang.Throwable ivjExc) {
        // user code begin {2}
        // user code end
        handleException(ivjExc);
      }
    }
    return ivjvar4;
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



      // mio
      String Query=getParameter("Query");
      String OKBCPrimitives=getParameter("OKBCPrimitives");
      String PrologModules=getParameter("PrologModules");
      // fin mio

      setName("InferenceJApplet");
      setSize(747, 439);
      setContentPane(getJAppletContentPane());
      initConnections();
      // user code begin {1}

      getvar1().setVisible(false);
      getvar2().setVisible(false);
      getvar3().setVisible(false);
      getvar4().setVisible(false);

  /*Oscar*/
      doc=new DefaultStyledDocument();
      getqueryTP().setDocument(doc);
      StyleConstants.setForeground(attr_var, Color.red);
      StyleConstants.setForeground(attr_word, Color.blue);

      getpanelQuery().setBorder(
                                BorderFactory.createCompoundBorder(
                                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(Query),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                                getpanelQuery().getBorder()));


      getpanelCBs().setBorder(
                              BorderFactory.createCompoundBorder(
                              BorderFactory.createCompoundBorder(
                              BorderFactory.createTitledBorder(OKBCPrimitives),
                              BorderFactory.createEmptyBorder(5,5,5,5)),
                              getpanelCBs().getBorder()));



      getpanelModules().setBorder(
                                  BorderFactory.createCompoundBorder(
                                  BorderFactory.createCompoundBorder(
                                  BorderFactory.createTitledBorder(PrologModules),
                                  BorderFactory.createEmptyBorder(5,5,5,5)),
                                  getpanelModules().getBorder()));

  /*END Oscar*/

  /*Captura de datos de la base de datos*/
      receive_modules();
      receive_params();


  /*END captura datos*/

      // user code end
    } catch (java.lang.Throwable ivjExc) {
      // user code begin {2}
      // user code end
      handleException(ivjExc);
    }
  }

  private void receive_modules()
  {

    int i=0;
    String aux=getParameter("module"+i);

    while (aux!=null)
    {
      getloadCB().addItem(aux);
      modules.add(aux);
      i++;
      aux=getParameter("module"+i);

    }

  }


  private void receive_params()
  {

    int i=0;
    String aux=getParameter("concept"+i);

    while (aux!=null)
    {
      receive_atr(aux);
      classes.add(aux);
      i++;
      aux=getParameter("concept"+i);

    }

  }

  private void receive_atr(String concept)
  {

    int i=0;
    String aux1=getParameter("classAttribute"+ i +"_"+ concept);
    while (aux1!=null)
    {
      class_atr.add(aux1);
      i++;
      aux1=getParameter("classAttribute"+ i +"_"+ concept);

    }

    i=0;
    aux1=getParameter("instanceAttribute"+ i +"_"+ concept);
    while (aux1!=null)
    {
      instance_atr.add(aux1);
      i++;
      aux1=getParameter("instanceAttribute"+ i +"_"+ concept);

    }

    i=0;
    aux1=getParameter("instance"+ i +"_"+ concept);
    while (aux1!=null)
    {
      instances.add(aux1);
      i++;
      aux1=getParameter("instance"+ i +"_"+ concept);

    }

  }


  /**
   * Inicializa las conexiones
   * @exception java.lang.Exception La descripción de excepción.
   */
  private void initConnections() throws java.lang.Exception {
    // user code begin {1}
    // user code end
    getlist().addActionListener(ivjEventHandler);
    getresetB().addActionListener(ivjEventHandler);
    getsubmitB().addActionListener(ivjEventHandler);
    getclearB().addActionListener(ivjEventHandler);
    getvar1().addActionListener(ivjEventHandler);
    getvar2().addActionListener(ivjEventHandler);
    getvar3().addActionListener(ivjEventHandler);
    getvar4().addActionListener(ivjEventHandler);
    getsaveB().addActionListener(ivjEventHandler);
    getloadCB().addActionListener(ivjEventHandler);
    getruleB().addItemListener(ivjEventHandler);
  }
  /**
   * Comment
   */

  public void list_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    int pos = 0;
    int i=0;
    Vector v = new Vector();
    // case 1:
    String SelectClass=getParameter("SelectClass");
    String SelectInstances=getParameter("SelectInstances");
    String Classe =getParameter("Class");
    String Instance=getParameter("Instance");
    String SelectInstance=getParameter("SelectInstance");
    // Case 2:
    String SelectInferenceLevel=getParameter("SelectInferenceLevel");
    String InferenceLevel=getParameter("InferenceLevel");
    String Subclass=getParameter("Subclass");
    String SelectSubclass=getParameter("SelectSubclass");
    // Case 3:
    String SelectSuperclass=getParameter("SelectSuperclass");
    String direct="direct";
    String taxonomic="taxonomic";
    //   case 4:
    String SelectSlot=getParameter("SelectSlot");
    String Slots=getParameter("Slots");
    String Slot=getParameter("Slot");

    //   case 5:
    String SelectType=getParameter("SelectType");
    String SelectTypes=getParameter("SelectTypes");
    String Types=getParameter("Types");

    //   case 6:
    String Domain=getParameter("Domain");
    String SelectDomain=getParameter("SelectDomain");

    //  case 7:
    String SelectFacets=getParameter("SelectFacets");
    String SelectFacet=getParameter("SelectFacet");
    String Facets=getParameter("Facets");

    //  case 8:

    //  case 9:
    String Value=getParameter("Value");
    // case 10:
    String Values=getParameter("Values");
    //case 11:
    String Details=getParameter("Details");
    //case 12:
    String SelectConcept=getParameter("SelectConcept");
    String Concept=getParameter("Concept");
    //   case 13:
    String instance_of_p=getParameter("instanceofp");
    //   case 14:
    String individual_p=getParameter("individualp");
    //   case 15:

    //   case 16:
    String Facet=getParameter("Facet");
    //String SelectFacet=getParameter("SelectFacet");
    //   case 17:

    //   case 18:

    //   case 19:
    String Memberfacetvaluep=getParameter("Memberfacetvaluep");
//        String getslottype=getParameter("getslottype");

//        String getslotvalues=getParameter("getslotvalues");



    //      String SelectInstance=getParameter("SelectInstance");

//        String getclassinstances=getParameter("getclassinstances");


//        String direct=getParameter("direct");
//        String taxonomic=getParameter("taxonomic");


//        String getclasssubclasses=getParameter("getclasssubclasses");

//        String getclasssuperclasses=getParameter("getclasssuperclasses");
    //     String SelectSlot=getParameter("SelectSlot");
//        String getslots=getParameter("getslots");
    //       String Slots=getParameter("Slots");
    //       String Slot=getParameter("Slot");
//        String SelectTypes=getParameter("SelectTypes");

    String Type=getParameter("Type");
//        String getinstancetypes=getParameter("getinstancetypes");

//        String Memberslotvaluep=getParameter("Memberslotvaluep");



//        String getslotdomain=getParameter("getslotdomain");
//        String getslotvalue=getParameter("getslotvalue");
//        String getslotfacets=getParameter("getslotfacets");

//        String getslotvaluesindetail=getParameter("getslotvaluesindetail");
    //String get-facet-values=getParameter("get-facet-values");
//        String getfacetvalues=getParameter("getfacetvalues");
    //  String member-facet-value-p=getParameter("member-facet-value-p");
//        String memberfacetvaluep=getParameter("memberfacetvaluep");
//        String individual_p=getParameter("individualp");

    //String get-frame-details=getParameter("get-frame-details");
//        String getframedetails=getParameter("getframedetails");

//        String getfacetvalue=getParameter("getfacetvalue");

    getvar1().setVisible(false);
    getvar1().removeAllItems();

    getvar2().setVisible(false);
    getvar2().removeAllItems();

    getvar3().setVisible(false);
    getvar3().removeAllItems();

    getvar4().setVisible(false);
    getvar4().removeAllItems();

/*
    //try {
    String[][] OKBC_PRIMITIVES={
   {"get_class_instances", Class, Instance} ,
   {"get_class_subclasses", Class, InferenceLevel, Subclass} ,
   {"get_class_superclasses", Class, InferenceLevel, Subclass} ,
   {"get_slots", Class, Slots} ,
   {"get_instance_types", Class, Types} ,
   {"get_slot_domain", Slot, Domain} ,
   {"get_slot_facets", Slot, Facets} ,
   {"get_slot_type", Class, Slot, Type} ,
   {"get_slot_value", Class, Slot, Value} ,
   {"get_slot_values", Class, Slot, Values} ,
   {"get_slot_values_in_detail", Class, Slot, Details} ,
   {"individual_p", Concept} ,
   {"instance_of_p", Instance, Class} ,
   {"get-frame-details", Class, Details} ,
   {"member-slot-value-p", Class, Slot, Value} ,
   {"member-facet-value-p", Class, Slot, Facet, Value} ,
   {"get-facet-value", Class, Slot, Facet, Value} ,
   {"get-facet-values", Class, Slot, Facet, Value}};

  int j;
  int idx=getlist().getSelectedIndex()-1;
  javax.swing.JComboBox var= getvar1();
  for( j=1; j<OKBC_PRIMITIVES[idx].length; i++) {


   switch (j) {
    case 1:
     var=getvar1();
    //getvar1().setVisible(true);
     break;
    case 2:
    //getvar2().setVisible(true);
                    var=getvar2();
     break;
    case 3:
     var=getvar3();
    //getvar3().setVisible(true);
     break;
    case 4:
        var=getvar4();
    //getvar4().setVisible(true);
     break;
   }
    //var.setVisible(true);
   if(OKBC_PRIMITIVES[idx][j].equals(Class)) {
    var.addItem(SelectClass);
    v = new Vector();
    for(i=0; i<classes.size();i++) {
     if(!checkVector((String) classes.get(i), v)) {
      var.addItem((String) classes.get(i));
      v.add(classes.get(i));
     }
    }
   }
   else if(OKBC_PRIMITIVES[idx][j].equals(Subclass)) {
    var.addItem(SelectSubclass);
    v = new Vector();
    for(i=0; i<classes.size();i++) {
     if(!checkVector((String) classes.get(i), v)) {
      var.addItem((String) classes.get(i));
      v.add(classes.get(i));
     }
    }
   }
  }
  getqueryTP().replaceSelection("");
  pos=getqueryTP().getSelectionStart();

  int pos_aux=0;

  doc.insertString(pos, OKBC_PRIMITIVES[idx][0], attr_word);
  pos_aux+=OKBC_PRIMITIVES[idx][0].length();

  doc.insertString(pos+pos_aux,"(",attr_word);
  pos_aux+=1;

  for(i=1; i<OKBC_PRIMITIVES[idx].length; i++) {
   if(i>0) {
    doc.insertString(pos+pos_aux,", ",attr_word);
    pos_aux+=2;
   }
   doc.insertString(pos+pos_aux,OKBC_PRIMITIVES[idx][i],attr_var);
   pos_aux+=1;
  }

  doc.insertString(pos+pos_aux,")",attr_word);
  pos_aux+=1;
 }
 catch(Exception e) {
  e.printStackTrace();
 }





*/
    int pos_aux;
    switch (getlist().getSelectedIndex())
    {
      //int pos_aux;
      case 1:



        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {
          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }
        getvar1().setVisible(true);
        //getvar1().setForeground(Color.red);

        getvar2().addItem(SelectInstances);
        getvar2().setVisible(true);
        //getvar2().setForeground(Color.red);

        //taText.insert(" get_class_instances(Class, Instance)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          /*
   doc.insertString(pos, "get_class_instances", attr_word);
   doc.insertString(pos+19, "(", attr_word);
   doc.insertString(pos+20, Class, attr_var);
   doc.insertString(pos+25, ", ", attr_word);
   doc.insertString(pos+27, Instance, attr_var);
     doc.insertString(pos+35, ")", attr_word);
           */




          doc.insertString(pos, "get_class_instances", attr_word);
          doc.insertString(pos+19, "(", attr_word);
          doc.insertString(pos+20, Classe, attr_var);

          pos_aux = pos + 20 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Instance, attr_var);
          pos_aux = pos_aux + Instance.length();
          doc.insertString(pos_aux, ")", attr_word);
        }catch (BadLocationException exc) {

        }

        break;
      case 2:

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {
          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Color.red);

        getvar2().addItem(SelectInferenceLevel);
        getvar2().addItem("taxonomic");
        getvar2().addItem("direct");
        //getvar2().setForeground(Color.red);
        getvar2().setVisible(true);

        getvar3().addItem(SelectSubclass);
        v = new Vector();
        for(i=0; i<classes.size();i++)
        {

          if(!checkVector((String) classes.get(i), v))
          {
          getvar3().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar3().setVisible(true);
        //getvar3().setForeground(Color.red);

        //taText.insert(" get_class_subclasses(Class, InferenceLevel, Subclass)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get_class_subclasses", attr_word);
          doc.insertString(pos+20, "(", attr_word);
          doc.insertString(pos+21, Classe, attr_var);
          pos_aux = pos +21 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, InferenceLevel, attr_var);
          pos_aux = pos_aux + InferenceLevel.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Subclass, attr_var);
          pos_aux = pos_aux + Subclass.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;

      case 3:

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {
          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }
        getvar1().setVisible(true);
        //getvar1().setForeground(Color.red);

        getvar2().addItem(SelectInferenceLevel);
        getvar2().addItem("taxonomic");
        getvar2().addItem("direct");
        //getvar2().setForeground(Colored);
        getvar2().setVisible(true);

        getvar3().addItem(SelectSuperclass);
        v = new Vector();
        for(i=0; i<classes.size();i++)
        {
          if(!checkVector((String) classes.get(i), v))
          {
          getvar3().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }
        getvar3().setVisible(true);

        //taText.insert(" get_class_superclasses(Class, InferenceLevel, Subclass)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get_class_superclasses", attr_word);
          doc.insertString(pos+22, "(", attr_word);
          doc.insertString(pos+23, Classe, attr_var);
          pos_aux = pos +23 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, InferenceLevel, attr_var);
          pos_aux = pos_aux + InferenceLevel.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Subclass, attr_var);
          pos_aux = pos_aux + Subclass.length();
          doc.insertString(pos_aux, ")", attr_word);





        }catch (BadLocationException exc) {

        }

        break;

      case 4://get-slots

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {
          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }
        getvar1().setVisible(true);
        //getvar1().setForeground(Color.red);

        getvar2().addItem(SelectSlot);
        v= new Vector();
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar2().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }

        }
        v= new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          if(!checkVector((String) instance_atr.get(i), v))
          {
          getvar2().addItem((String) instance_atr.get(i));
          v.add(instance_atr.get(i));
        }

        }
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);

        //taText.insert("get-slots(Class, Slots)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get_slots", attr_word);
          doc.insertString(pos+9, "(", attr_word);
          doc.insertString(pos+10, Classe, attr_var);
          pos_aux = pos+10 + Classe.length();
          doc.insertString(pos+pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Slots, attr_var);
          pos_aux = pos_aux + Slots.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;

      case 5: //get_instance_types

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {
          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectTypes);
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);

        //taText.insert(" get_instance_types(Class, InferenceLevel, Subclass)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get_instance_types", attr_word);
          doc.insertString(pos+18, "(", attr_word);
          doc.insertString(pos+19, Classe, attr_var);
          pos_aux = pos+19 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Types, attr_var);
          pos_aux = pos_aux + Types.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;

      case 6://get_slot_domain

        getvar1().addItem(SelectSlot);
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar1().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }
        }
        v = new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          if(!checkVector((String) instance_atr.get(i), v))
          {
          getvar1().addItem((String) instance_atr.get(i));
          v.add(instance_atr.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectDomain);
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);
        //taText.insert(" get_slot_domain(Class, InferenceLevel, Subclass)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get_slot_domain", attr_word);
          doc.insertString(pos+15, "(", attr_word);
          doc.insertString(pos+16, Slot, attr_var);
          pos_aux = pos+16 + Slot.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Domain, attr_var);
          pos_aux = pos_aux + Domain.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;
      case 7://get_slot_facets

        getvar1().addItem(SelectSlot);
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar1().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }
        }
        v = new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          if(!checkVector((String) instance_atr.get(i), v))
          {
          getvar1().addItem((String) instance_atr.get(i));
          v.add(instance_atr.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectFacets);
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);
        //taText.insert("get_slot_facets(Class, InferenceLevel, Subclass)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get_slot_facets", attr_word);
          doc.insertString(pos+15, "(", attr_word);
          doc.insertString(pos+16, Slot, attr_var);
          pos_aux = pos+16 + Slot.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Facets, attr_var);
          pos_aux = pos_aux + Facets.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }
        break;

      case 8://get_slot_type

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {
          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectSlot);
        v= new Vector();
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar2().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }

        }
        v= new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          if(!checkVector((String) instance_atr.get(i), v))
          {
          getvar2().addItem((String) instance_atr.get(i));
          v.add(instance_atr.get(i));
        }
        }
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);

        getvar3().addItem(SelectType);
        getvar3().setVisible(true);
        //getvar3().setForeground(Colored);

        //taText.insert("get_slot_type(Class, InferenceLevel, Subclass)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get_slot_type", attr_word);
          doc.insertString(pos+13, "(", attr_word);
          doc.insertString(pos+14, Classe, attr_var);
          pos_aux = pos+14 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos+21, Slot, attr_var);
          pos_aux = pos_aux + Slot.length();
          doc.insertString(pos+25, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Type, attr_var);
          pos_aux = pos_aux + Type.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }
        break;

      case 9://get_slot_value

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {
          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectSlot);
        v= new Vector();
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar2().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }
        }
        v= new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          if(!checkVector((String) instance_atr.get(i), v))
          {
          getvar2().addItem((String) instance_atr.get(i));
          v.add(instance_atr.get(i));
        }
        }
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);

        //getvar3().setVisible(true);
        ////getvar3().setForeground(Colored);
        //taText.insert("get_slot_value(Class, InferenceLevel, Subclass)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get_slot_value", attr_word);
          doc.insertString(pos+14, "(", attr_word);
          doc.insertString(pos+15, Classe, attr_var);
          pos_aux = pos+15 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Slot, attr_var);
          pos_aux = pos_aux + Slot.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Value, attr_var);
          pos_aux = pos_aux + Value.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;

      case 10://get_slot_values

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {
          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectSlot);
        v= new Vector();
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar2().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }
        }
        v= new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          if(!checkVector((String) instance_atr.get(i), v))
          {
          getvar2().addItem((String) instance_atr.get(i));
          v.add(instance_atr.get(i));
        }
        }
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);

        //getvar3().setVisible(true);
        ////getvar3().setForeground(Colored);
        //taText.insert(" get_slot_values(Class, Slot, Values)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get_slot_values", attr_word);
          doc.insertString(pos+15, "(", attr_word);
          doc.insertString(pos+16, Classe, attr_var);
          pos_aux = pos+16 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Slot, attr_var);
          pos_aux = pos_aux + Slot.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Values, attr_var);
          pos_aux = pos_aux + Values.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;

      case 11: //get_slot_values_in_detail

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {
          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectSlot);
        v=new Vector();
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar2().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }
        }
        v=new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          if(!checkVector((String) instance_atr.get(i), v))
          {
          getvar2().addItem((String) instance_atr.get(i));
          v.add(instance_atr.get(i));
        }
        }
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);

        //getvar3().setVisible(true);
        ////getvar3().setForeground(Colored);
        //taText.insert(" get_slot_values_in_details(Class, Slot, Values)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get_slot_values_in_detail", attr_word);
          doc.insertString(pos+25, "(", attr_word);
          doc.insertString(pos+26, Classe, attr_var);
          pos_aux = pos+26 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Slot, attr_var);
          pos_aux = pos_aux + Slot.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Details, attr_var);
          pos_aux = pos_aux + Details.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;

      case 12: //individual_p

        getvar1().addItem(SelectConcept);
        for(i=0; i<classes.size();i++)
        {

          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);
        //taText.insert(" get_slot_values(Class, Slot, Values)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "individual_p", attr_word);
          doc.insertString(pos+12, "(", attr_word);
          doc.insertString(pos+13, Concept, attr_var);
          pos_aux = pos+13 + Concept.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;
      case 13: //instance_of_p

        getvar1().addItem(SelectInstance);
        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);


        getvar2().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {

          if(!checkVector((String) classes.get(i), v))
          {
          getvar2().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);
        //taText.insert(" get_slot_values(Class, Slot, Values)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "instance_of_p", attr_word);
          doc.insertString(pos+13, "(", attr_word);
          doc.insertString(pos+14, Instance, attr_var);
          pos_aux = pos+14 + Instance.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Classe, attr_var);
          pos_aux = pos_aux + Classe.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }


        break;

      case 14: //get-frame-details

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {

          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        //getvar2().setVisible(true);
        ////getvar2().setForeground(Colored);

        //taText.insert("get-frame-details(Class, Slot, Values)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get-frame-details", attr_word);
          doc.insertString(pos+17, "(", attr_word);
          doc.insertString(pos+18, Classe, attr_var);
          pos_aux = pos+18 + Classe.length();
          doc.insertString(pos+23, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Details, attr_var);
          pos_aux = pos_aux + Details.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;

      case 15: //member-slot-value-p


        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {

          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectSlot);
        v = new Vector();
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar2().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }
        }
        v = new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          getvar2().addItem((String) instance_atr.get(i));
        }
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);

        //getvar3().setVisible(true);
        ////getvar3().setForeground(Colored);

        //taText.insert("member-slot-value-p(Class, Slot, Values)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "member-slot-value-p", attr_word);
          doc.insertString(pos+19, "(", attr_word);
          doc.insertString(pos+20, Classe, attr_var);
          pos_aux = pos+20 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Slot, attr_var);
          pos_aux = pos_aux + Slot.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Value, attr_var);
          pos_aux = pos_aux + Value.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;

      case 16: //member-facet-value-p

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {

          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectSlot);
        v = new Vector();
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar2().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }
        }
        v = new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          getvar2().addItem((String) instance_atr.get(i));
        }
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);

        getvar3().addItem(SelectFacet);
        getvar3().setVisible(true);
        //getvar3().setForeground(Colored);

        //getvar4().setVisible(true);
        ////getvar4().setForeground(Colored);

        //taText.insert("member-facet-value-p(Class, Slot, Values)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "member-facet-value-p", attr_word);
          doc.insertString(pos+20, "(", attr_word);
          doc.insertString(pos+21, Classe, attr_var);
          pos_aux = pos+21 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Slot, attr_var);
          pos_aux = pos_aux + Slot.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Facet, attr_var);
          pos_aux = pos_aux + Facet.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Value, attr_var);
          pos_aux = pos_aux + Value.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }
        break;

      case 17: //get-facet-value

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {

          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectSlot);
        v = new Vector();
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar2().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }
        }
        v = new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          if(!checkVector((String) instance_atr.get(i), v))
          {
          getvar2().addItem((String) instance_atr.get(i));
          v.add(instance_atr.get(i));
        }
        }
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);

        getvar3().addItem(SelectFacet);
        getvar3().setVisible(true);
        //getvar3().setForeground(Colored);

        //getvar4().setVisible(true);
        ////getvar4().setForeground(Colored);
        //taText.insert(" get_slot_values(Class, Slot, Values)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get-facet-value", attr_word);
          doc.insertString(pos+15, "(", attr_word);
          doc.insertString(pos+16, Classe, attr_var);
          pos_aux = pos+16 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Slot, attr_var);
          pos_aux = pos_aux + Slot.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Facet, attr_var);
          pos_aux = pos_aux + Facet.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Value, attr_var);
          pos_aux = pos_aux + Value.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;

      case 18: //get-facet-values

        getvar1().addItem(SelectClass);
        for(i=0; i<classes.size();i++)
        {

          if(!checkVector((String) classes.get(i), v))
          {
          getvar1().addItem((String) classes.get(i));
          v.add(classes.get(i));
        }
        }

        getvar1().setVisible(true);
        //getvar1().setForeground(Colored);

        getvar2().addItem(SelectSlot);
        v = new Vector();
        for(i=0; i<class_atr.size();i++)
        {
          if(!checkVector((String) class_atr.get(i), v))
          {
          getvar2().addItem((String) class_atr.get(i));
          v.add(class_atr.get(i));
        }
        }
        v = new Vector();
        for(i=0; i<instance_atr.size();i++)
        {
          if(!checkVector((String) instance_atr.get(i), v))
          {
          getvar2().addItem((String) instance_atr.get(i));
          v.add(instance_atr.get(i));
        }
        }
        getvar2().setVisible(true);
        //getvar2().setForeground(Colored);

        getvar3().addItem(SelectFacet);
        getvar3().setVisible(true);
        //getvar3().setForeground(Colored);

        //getvar4().setVisible(true);
        ////getvar4().setForeground(Colored);
        //taText.insert(" get_slot_values(Class, Slot, Values)",taText.getSelectionEnd());
        getqueryTP().replaceSelection("");
        pos=getqueryTP().getSelectionStart();
        try
        {
          doc.insertString(pos, "get-facet-values", attr_word);
          doc.insertString(pos+16, "(", attr_word);
          doc.insertString(pos+17, Classe, attr_var);
          pos_aux = pos+17 + Classe.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Slot, attr_var);
          pos_aux = pos_aux + Slot.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Facet, attr_var);
          pos_aux = pos_aux + Facet.length();
          doc.insertString(pos_aux, ", ", attr_word);
          pos_aux = pos_aux +2;
          doc.insertString(pos_aux, Value, attr_var);
          pos_aux = pos_aux + Value.length();
          doc.insertString(pos_aux, ")", attr_word);

        }catch (BadLocationException exc) {

        }

        break;


      default:
        getvar1().setVisible(false);
      getvar2().setVisible(false);
      getvar3().setVisible(false);
      getvar4().setVisible(false);
    }

    getqueryTP().repaint();


    return;

  }






  public void loadCB_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    //Realizamos la carga del modulo de inferencia seleccionado

    URL url;
    URLConnection uc;
    BufferedReader in;
    int chr;
    String LoadingModule=getParameter("LoadingModule");

    if( !((getloadCB().getSelectedItem()).equals("Load")) ) {

      try {
        //String query = new String(doc.getText(doc.getStartPosition().getOffset(), doc.getEndPosition().getOffset()-1));

        url = new URL(new URL(getDocumentBase(),"./") +"inferenceLoadModule.jsp?selectModule=" + URLEncoder.encode((String) getloadCB().getSelectedItem(),"ISO-8859-1"));
        //getresults().append("Loading Module " + (String) getloadCB().getSelectedItem() + "...:");
        getresults().append(LoadingModule + (String) getloadCB().getSelectedItem() + "...:");
        uc=url.openConnection();
        uc.setUseCaches(false);


        //System response
        in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String line;
        boolean print=false;
        while((line=in.readLine())!=null) {
          print=print && !line.trim().toLowerCase().equals("</pre>");
          if(print)
            getresults().append(line+"\n");
          print=print || line.trim().toLowerCase().equals("<pre>");
        }

        in.close();
        }
        catch(Exception e2) {
          getresults().append(e2.toString() + "\n");
        }
    }


    return;
  }
  /**
   * Punto de entrada principal - inicia el componente cuando se ejecuta como una aplicación
   * @param args java.lang.String[]
   */
 /*
public static void main(java.lang.String[] args) {
 try {
  javax.swing.JFrame frame = new javax.swing.JFrame();
  InferenceJApplet aInferenceJApplet;
  Class iiCls = Class.forName("InferenceJApplet");
  ClassLoader iiClsLoader = iiCls.getClassLoader();
  aInferenceJApplet = (InferenceJApplet)java.beans.Beans.instantiate(iiClsLoader,"InferenceJApplet");
  frame.getContentPane().add("Center", aInferenceJApplet);
  frame.setSize(aInferenceJApplet.getSize());
  frame.addWindowListener(new java.awt.event.WindowAdapter() {
   public void windowClosing(java.awt.event.WindowEvent e) {
    System.exit(0);
   };
  });
  frame.setVisible(true);
 } catch (Throwable exception) {
  System.err.println("Se ha producido una excepción en main() de javax.swing.JApplet");
  exception.printStackTrace(System.out);
 }
}
*/

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
  /**
   * Comment
   */
  public void resetB_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    getqueryTP().setText(null);

    return;
  }
  /**
   * Comment
   */
  public void ruleB_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {

    stateRule = !stateRule;
    return;
  }
  /**
   * Comment
   */
  public void saveB_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

 /*Salvamos el modulo*/
    URL url;
    URLConnection uc;
    InputStreamReader in;
    OutputStreamWriter out;
    int chr;

    String name = getJTextField1().getText();
    String texto =	getprogram().getText();
    String Savingmodule=getParameter("Savingmodule");

    try
    {

      //getresults().append("Saving module "+ name + "...");
      getresults().append(Savingmodule + name + "...");
      url = new URL(new URL(getDocumentBase(),"./") +"inferenceSaveModule.jsp?selectName=" + URLEncoder.encode(name,"ISO-8859-1") +
                    "&selectText=" + URLEncoder.encode(texto,"ISO-8859-1"));

      uc=url.openConnection();

      uc.setUseCaches(false);

      //results
      in = new InputStreamReader(uc.getInputStream());
      chr=in.read();
      while (chr != '<')
      {
        getresults().append(String.valueOf((char) chr));
        chr = in.read();
      }


      }catch(Exception e2)
      {
        getresults().append(e2.toString() + "\n");
      }

      return;
  }

  /**
   * Se llama para iniciar el applet. Nunca es necesario llamar a este método
   * directamente, se llama cuando se visita el documento del applet.
   * @see #init
   * @see #stop
   * @see #destroy
   */
  public void start() {
    super.start();
// inserte aquí el código que deba ejecutarse cuando se inicie el applet



  }
  /**
   * Comment
   */
  public void submitB_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    URL url;
    URLConnection uc;
    BufferedReader in;
    int chr;
    String Results=getParameter("Results");
    try
    {
      String query = new String(doc.getText(doc.getStartPosition().getOffset(), doc.getEndPosition().getOffset()-1));

      url = new URL(new URL(getDocumentBase(),"./") +"inferenceResult.jsp?selectQuery=" + URLEncoder.encode(query,"ISO-8859-1"));
      //getresults().append("Resultsssssssssssssssssss: ");
      getresults().append(Results);
      //getresults().append(getParameter(PARENTS));
      uc=url.openConnection();
      uc.setUseCaches(false);

      if(stateRule == true)
      {
        getprogram().append(query + ".\n");
      }

      //results
      in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
      String line;
      boolean print=false;
      while((line=in.readLine())!=null) {
        print=print && !line.trim().toLowerCase().equals("</pre>");
        if(print)
          getresults().append(line+"\n");
        print=print || line.trim().toLowerCase().equals("<pre>");
      }

      in.close();
      }
      catch(Exception e2) {
        getresults().append(e2.toString() + "\n");
      }

      return;
  }
  /**
   * Comment
   */
  public void var1_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    if (getvar1().getSelectedIndex() != 0) {

      getqueryTP().replaceSelection("");
      int pos=getqueryTP().getSelectionStart();
      try
      {
        doc.insertString(pos, marshalling((String)getvar1().getSelectedItem()), attr_var);


      }catch (BadLocationException exc) {

      }
      getqueryTP().repaint();
    }

    return;
  }
  /**
   * Comment
   */
  public void var2_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    if (getvar2().getSelectedIndex() != 0) {

      getqueryTP().replaceSelection("");
      int pos=getqueryTP().getSelectionStart();
      try
      {
        doc.insertString(pos, marshalling((String)getvar2().getSelectedItem()), attr_var);


      }catch (BadLocationException exc) {

      }
      getqueryTP().repaint();
    }
    return;
  }
  /**
   * Comment
   */
  public void var3_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    if (getvar3().getSelectedIndex() != 0) {

      getqueryTP().replaceSelection("");
      int pos=getqueryTP().getSelectionStart();
      try
      {
        doc.insertString(pos, marshalling((String)getvar3().getSelectedItem()), attr_var);


      }catch (BadLocationException exc) {

      }
      getqueryTP().repaint();
    }


    return;
  }

  /**
   * Comment
   */
  public void var4_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    if (getvar4().getSelectedIndex() != 0) {

      getqueryTP().replaceSelection("");
      int pos=getqueryTP().getSelectionStart();
      try
      {
        doc.insertString(pos, marshalling((String)getvar4().getSelectedItem()), attr_var);


      }catch (BadLocationException exc) {

      }
      getqueryTP().repaint();
    }
    return;
  }

  private boolean checkVector(String elto, Vector v) {
    for(int i = 0; i<v.size(); i++)
      if( elto.equals(v.get(i)))
        return true;
    return false;
  }

  private String marshalling(String str) {
    if(!Character.isLetter(str.charAt(0))) str="_"+str;
    return str.replace('.','_').replace(' ','_').replace(',', '_').replace(')', '_').replace('(', '_').toLowerCase();
  }
}