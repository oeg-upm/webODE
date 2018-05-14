package es.upm.fi.dia.ontology.webode.ui.tree;

import java.util.*;
import java.applet.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.io.UnsupportedEncodingException;
import es.upm.fi.dia.ontology.webode.util.ConceptData;
import es.upm.fi.dia.ontology.webode.util.NodeData;
import es.upm.fi.dia.ontology.webode.util.NodeInfo;
import es.upm.fi.dia.ontology.webode.util.Node;

import javax.swing.plaf.metal.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.dnd.peer.*;
import java.net.*;

public class InheritanceTreeApplet extends     JApplet
                                   implements  TreeWillExpandListener {

  public static final String LOCATION                =  "LOCATION";
  public static final String CHILD2                  =  "CHILD";
  public static final String DEST                    =  "DEST";
  public static final String DESCEND                 =  "DESCEND";
  public static final String PARE                    =  "PARE";
  public static final String STORING                 =  "STORING";
  public static final String CICLES                  =  "CICLES";
  public static final String DISJOINT                =  "DISJOINT";
  public static final String NOTSUBCLASS             =  "NOTSUBCLASS";
  public static final String EXHAUSTIVE              =  "EXHAUSTIVE";
  public static final String FOO                      = "foo";
  public static final String PATHS                    = "Nodos";
  public static final String IDSESION                 = "IDSESION";
  public static final String ROOT                     = "root";
  public static final String PARENT                   = "parent";
  public static final String CHILD                    = "child";
  public static final String REFERENCE                = "reference";
  public static final String URL_NEW_TERM             = "urlNewTerm";
  public static final String URL_UPDATE_TERM          = "urlUpdateTerm";
  public static final String URL_CLASS_ATTRIBUTES     = "urlClassAttributes";
  public static final String URL_INSTANCE_ATTRIBUTES  = "urlInstanceAttributes";
  public static final String URL_REFERENCES           = "urlReferences";
  public static final String URL_NEW_REFERENCE        = "urlNewReference";
  public static final String URL_UPDATE_REFERENCE     = "urlUpdateReference";
  public static final String URL_ABBREVIATIONS        = "urlAbbreviations";
  public static final String URL_SYNONYMS             = "urlSynonyms";
  public static final String URL_IMPORTED_TERMS       = "urlImportedTerms";
  public static final String URL_NEW_CONSTANT         = "urlNewConstant";
  public static final String URL_UPDATE_CONSTANT      = "urlUpdateConstant";
  public static final String URL_SUBCLASS_OF          = "urlSubclassOf";
  public static final String URL_NOT_SUBCLASS_OF      = "urlNotSubclassOf";
  public static final String URL_DISJOINT             = "urlDisjoint";
  public static final String URL_EXHAUSTIVE           = "urlExhaustive";
  public static final String URL_TRANSITIVE_PART_OF   = "urlTransitivePartOf";
  public static final String URL_INTRANSITIVE_PART_OF = "urlIntransitivePartOf";
  public static final String URL_ADHOC                = "urlAdhoc";
  public static final String URL_NEW_FORMULA          = "urlNewFormula";
  public static final String URL_UPDATE_FORMULA       = "urlUpdateFormula";
  public static final String URL_NEW_PROPERTY         = "urlNewProperty";
  public static final String URL_UPDATE_PROPERTY      = "urlUpdateProperty";
  public static final String URL_FORMULA              = "urlFormulas";
  public static final String URL_INSTANCES            = "urlInstances";
  public static final String URL_RELATION_INSTANCES   = "urlRelationInstances";
  public static final String URL_NEW_GROUP            = "urlNewGroup";
  public static final String URL_UPDATE_GROUP         = "urlUpdateGroup";
  public static final String FRAME_NAME               = "frameName";
  public static final String IMPORTED_TERM            = "imported";
  public static final String CONSTANT                 = "constant";
  public static final String FORMULA                  = "formula";
  public static final String PROPERTY                 = "property";
  public static final String GROUP                    = "group";
  public static final String SHOW_PROPERTIES          = "DE";
  public static final String SHOW_CLASS_ATTRIBUTES    = "CA";
  public static final String SHOW_INSTANCE_ATTRIBUTES = "IA";
  public static final String SHOW_REFERENCES          = "RE";
  public static final String SHOW_ABBREVIATIONS       = "AB";
  public static final String SHOW_SYNONYMS            = "SY";
  public static final String SHOW_RELATIONS           = "REL";
  public static final String SHOW_SUBCLASS_OF         = "SO";
  public static final String SHOW_NOT_SUBCLASS_OF     = "NSO";
  public static final String SHOW_DISJOINT            = "DSP";
  public static final String SHOW_EXHAUSTIVE          = "ESP";
  public static final String SHOW_TRANSITIVE_PART_OF  = "TPO";
  public static final String SHOW_INTRANSITIVE_PART_OF= "IPO";
  public static final String SHOW_ADHOC               = "AH";
  public static final String SHOW_FORMULA             = "FOR";
  public static final String SHOW_INSTANCES           = "INS";
  public static final String PARENTS                  = "PAR";
  public static final String Showsparentsinadrilldownfashion  = "SPI";
  public static final String Children                         = "CHI";
  public static final String Showschildreninadrilldownfashion = "SCI";
  public static final String References                       = "REF";
  public static final String Showsallavailablereferences      = "SAR";
  public static final String Taxonomy                         = "TAX" ;
  public static final String Showstheglossaryofterms          = "STGT" ;
  public static final String ImportedTerms                    = "IMT" ;
  public static final String Showsalltheimportedterms         = "SIT";
  public static final String Constants                        = "CONS";
  public static final String Showsalltheconstants             = "SAC";
  public static final String Showsalltheformulas              = "SAF";
  public static final String Propertie                        = "PROS";
  public static final String Showsalltherelationproperties    = "SARP";
  public static final String Showsallthegroups                = "SATG";
  public static final String Formulas                         = "FOR";
  public static final String Groups                           = "GROUPS";

  // Enumerated types specific
  // Miguel Esteban Gutiérrez, 29/07/2004
  public static final String ENUMERATED_TYPE           = "enumerated_type";
  public static final String EnumeratedTypes           = "ET" ;
  public static final String Showsalltheenumeratedtypes= "SET";
  public static final String URL_NEW_ENUMERATED_TYPE   = "urlNewEnumeratedType";
  public static final String URL_UPDATE_ENUMERATED_TYPE= "urlUpdateEnumeratedType";
  private             URL    urlNewEnumeratedType      = null;
  private             URL    urlUpdateEnumeratedType   = null;

  private URL urlNewTerm, urlUpdateTerm, urlClassAttributes, urlReferences,
              urlNewReference, urlUpdateReference, urlAbbreviations,
              urlSynonyms, urlInstanceAttributes, urlImportedTerms,
              urlNewConstant, urlUpdateConstant, urlSubclassOf, urlDisjoint,
              urlExhaustive, urlTransitivePartOf, urlIntransitivePartOf,
              urlNotSubclassOf, urlAdhoc, urlNewFormula, urlUpdateFormula,
              urlNewProperty, urlUpdateProperty, urlFormula, urlInstances,
              urlNewGroup, urlUpdateGroup;

  private Integer            clave;
  private DefaultTreeModel   modelo;

  private Integer            claveficticia;
  private Vector             vectorChildren;
  private String             frameName;
  private Vector             vRoot;
  private HashMap            alreadyCount;
  public  DefaultTreeModel[] models = new DefaultTreeModel[1];
  private String             sesionactual;
  private String             ontologiaactual;
  private HashMap            hashactual;

  /** Stores the selected node info */
  protected TreePath SelectedTreePath = null;
  protected String   SelectedNode     = null;

  /** Variables needed for DnD */
  private DragSource        dragSource        = null;
  private DragSourceContext dragSourceContext = null;
  private StringSelection   SS;

  private Hashtable NodosDesarrollados = new Hashtable();


  // CLASE QUE CONTIENE LOS DATOS QUE EL APPLET LE MANDARA AL SERVLET

  public class OdeJTree extends    JTree
                        implements TreeSelectionListener,
                                   DragGestureListener,
                                   DropTargetListener,
                                   DragSourceListener {

    protected TreePath SelectedTreePath = null;
    protected Node     SelectedNode     = null;

    /** Variables needed for DnD */
    private DragSource        dragSource        = null;
    private DragSourceContext dragSourceContext = null;
    private Container         Parent            = null;
    private Applet            applet;

    /** Constructor
     * @param root The root node of the tree
     * @param parent Parent JFrame of the JTree
     **/
    public OdeJTree(DefaultTreeModel modelo, Container parent, Applet applet ) {
      super (modelo);
      this.applet=applet;
      Parent     = parent;

      addTreeSelectionListener(this);

      /* ********************** CHANGED ********************** */
      dragSource = DragSource.getDefaultDragSource() ;
      /* ****************** END OF CHANGE ******************** */

      DragGestureRecognizer dgr =
          dragSource.createDefaultDragGestureRecognizer(
            this,                             //DragSource
            DnDConstants.ACTION_COPY_OR_MOVE, //specifies valid actions
            this                              //DragGestureListener
          );


      /**
       * Eliminates right mouse clicks as valid actions - useful especially
       * if you implement a JPopupMenu for the JTree
       */
      dgr.setSourceActions(dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);

      /**
       * First argument:  Component to associate the target with
       * Second argument: DropTargetListener
       */
      DropTarget dropTarget = new DropTarget(this, this);
    }

    /**
     * DropTaregetListener interface method
     **/
    public void dropActionChanged(DropTargetDragEvent e) {
    }

    /**
     * TreeSelectionListener - sets selected node
     **/
    public void valueChanged(TreeSelectionEvent evt) {
      SelectedTreePath = evt.getNewLeadSelectionPath();

      if(SelectedTreePath==null) {
        SelectedNode=null;
        return;
      }

      SelectedNode=(Node)SelectedTreePath.getLastPathComponent();
    }

    /**
     * Returns The selected node
     **/
    public Node getSelectedNode() {
      return SelectedNode;
    }

    /**
     * DragGestureListener interface method
     **/
    public void dragGestureRecognized(DragGestureEvent e) {
      Node dragNode=getSelectedNode();
      if(dragNode!=null) {
        //Get the Transferable Object
        Transferable transferable=(Transferable)dragNode.getUserObject();
        //Select the appropriate cursor;
        Cursor cursor=DragSource.DefaultCopyDrop;
        //Cursor cursor=DragSource.MOVE_CURSOR;
        int action=e.getDragAction();
        // if(action==DnDConstants.ACTION_MOVE)
        //   cursor=DragSource.DefaultMoveNoDrop;

        // In fact the cursor is set to NoDrop because once an action is rejected
        // by a dropTarget, the dragSourceListener are no more invoked.
        // Setting the cursor to no drop by default is so more logical, because
        // when the drop is accepted by a component, then the cursor is changed by the
        // dropActionChanged of the default DragSource.

        // Begin the drag
        dragSource.startDrag(e, cursor, transferable, this);
      }
    }

    /**
     * DragSourceListener interface method
     **/
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    /**
     * DragSourceListener interface method
     **/
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    /**
     * DragSourceListener interface method
     **/
    public void dragOver(DragSourceDragEvent dsde) {
    }

    /**
     * DragSourceListener interface method
     **/
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    /**
     * DragSourceListener interface method
     **/
    public void dragExit(DragSourceEvent dsde) {
    }


    public Vector GiveTerms(Node x, DefaultTreeModel model){
      Node     root=(Node)model.getRoot();
      Vector   v   =new Vector();
      NodeInfo y   =(NodeInfo) x.getUserObject();
      String   name=y.getName();
      for(Enumeration enume=root.breadthFirstEnumeration();
          enume.hasMoreElements();) {
        Node    current =(Node)enume.nextElement();
        //NodeInfo Padre = ((NodeInfo) current.getUserObject()).getParent();
        NodeInfo nuevo  =(NodeInfo) current.getUserObject();
        String   auxilio=nuevo.getName();

        if(name.equals(auxilio)) {
         // PadreHijo a = PadreHijo(Padre,current);
          v.addElement(current);
        }
      }
      return(v);
    }

    public void Anadir(Vector v, DefaultTreeModel model, Vector v2) {
      Vector recarga=new Vector();
      for(Enumeration e=v.elements();e.hasMoreElements();) {
         Node    current=(Node)e.nextElement();
         Integer a      =(((NodeInfo)current.getUserObject()).getKey());
         int     aa     =a.intValue();
         claveficticia  =new Integer(-aa);

         Node fict= new Node(new NodeInfo("zft6801",claveficticia));
         if(!current.Belong(fict)){
           current.add(fict);
         }

         TreePath path=new TreePath(current.getPath());
         if(isExpanded(path)){
           collapsePath(path);
         }
         recarga.add(current);
      }

      for(Enumeration enum=v2.elements(); enum.hasMoreElements();) {
        Node N=(Node)enum.nextElement();
        model.reload(N);
        NodeInfo NI=(NodeInfo)N.getUserObject();
      }

      for(Enumeration en = recarga.elements(); en.hasMoreElements();) {
        Node N=(Node)en.nextElement();
        model.reload(N);
        NodeInfo NI=(NodeInfo)N.getUserObject();
      }

      for(Enumeration enu = recarga.elements(); enu.hasMoreElements();) {
        Node     N         =(Node)enu.nextElement();
        TreePath parentPath=new TreePath(N.getPath());
        expandPath(parentPath);
      }
    }

    public Vector Borrar(Node Parent, Vector v, DefaultTreeModel model) {
      Node   root      =(Node)model.getRoot();
      Vector actualizar=new Vector();

      for(Enumeration e = v.elements(); e.hasMoreElements();) {
        Node   current=(Node)e.nextElement();
        String Name   =((NodeInfo)current.getUserObject()).getName();

        NodeInfo Padre      =((NodeInfo)current.getUserObject()).getParent();
        String   NombrePadre=Padre.getName();

        if(NombrePadre.equals(((NodeInfo)Parent.getUserObject()).getName())) {
          // TENGO EN NAME EL nombre del NODO QUE DEBO BORRAR y en
          // NombrePadre el nombre del Padre

          // BUSCO ELTOS QUE TENGAN EL MISMO PADRE
          boolean existe=false;
          for(Enumeration en=root.breadthFirstEnumeration();
              en.hasMoreElements()&&(!existe);) {
            Node     Father  =(Node)en.nextElement();
            //NodeInfo Padre =((NodeInfo) current.getUserObject()).getParent();
            NodeInfo NIFather=(NodeInfo) Father.getUserObject();
            String   SFather =NIFather.getName();

            if(NombrePadre.equals(SFather)) {
              int     n   =Father.getChildCount();
              boolean sigo=true;
              int     i=0;
              while(sigo&&i<n){
                Node child=(Node)Father.getChildAt(i);
                if(((NodeInfo)child.getUserObject()).getName().equals(Name)) {
                  sigo  =false;
                  existe=true;
                  actualizar.addElement(Father);
                  Father.remove(child);
                }
                i ++;
              }
            }
          }
        }
      }
      return(actualizar);
    }

    public void drop(DropTargetDropEvent e) {
      try {
        Transferable tr=e.getTransferable();

        // flavor not supported, reject drop
        if(!tr.isDataFlavorSupported(NodeInfo.INFO_FLAVOR))
          e.rejectDrop();

        // cast into appropriate data type
        NodeInfo childInfo=
            (NodeInfo)tr.getTransferData(NodeInfo.INFO_FLAVOR);

        // get new parent node
        Point    loc            =e.getLocation();
        TreePath destinationPath=getPathForLocation(loc.x, loc.y);

        //-- NUEVO -------------------------------------------------------------

        final String msg=testDropTarget(destinationPath, SelectedTreePath);
        if(msg!=null) {
          e.rejectDrop();
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              JOptionPane.showMessageDialog(
                  Parent, msg, "Error Dialog", JOptionPane.ERROR_MESSAGE
              );
            }
          });
          return;
        }

        //-- FIN NUEVO ---------------------------------------------------------

        DefaultTreeModel model    =(DefaultTreeModel) getModel();
        Node             newParent=(Node)destinationPath.getLastPathComponent();
        Node             newChild =(Node)getSelectedNode();


        MutableTreeNode  newParent2=(Node)destinationPath.getLastPathComponent();
        Node             oldParent =(Node)getSelectedNode().getParent();
       //MutableTreeNode oldParent2=(Node)getSelectedNode().getParent();
        int     action    =e.getDropAction();
        boolean copyAction=(action==DnDConstants.ACTION_COPY);

        //-- INICIO SERVLET ----------------------------------------------------

        URL direccion=new URL(this.applet.getCodeBase(),
                              "/webode/servlet/DragDrop");

        NodeData      TheDatosServlet=new NodeData(oldParent,
                                                   newParent,
                                                   newChild);
        URLConnection Conexion       =direccion.openConnection();

        Conexion.setDoOutput(true);
        Conexion.setUseCaches(false);
        Conexion.setDefaultUseCaches (false);

        // Specify the content type that we will send binary data
        Conexion.setRequestProperty("Content-Type",
                                    "application/octet-stream");
        Conexion.setRequestProperty("Cookie",
                                    "JSESSIONID="+sesionactual);

        // send the student object to the servlet using serialization
        ObjectOutputStream outputToServlet=
          new ObjectOutputStream(Conexion.getOutputStream());

        // serialize the object
        outputToServlet.writeObject(TheDatosServlet);
        outputToServlet.flush();
        outputToServlet.close();

        ObjectInputStream inputFromServlet=
          new ObjectInputStream(Conexion.getInputStream());

        try {
          Vector nuevo=(Vector)inputFromServlet.readObject();
          if(((String)nuevo.elementAt(0)).equals("M")) {
            e.rejectDrop();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                JOptionPane.showMessageDialog(
                  Parent,
                  getParameter(STORING),
                  "Error Dialog",
                  JOptionPane.ERROR_MESSAGE
               );
              }
            });
            return;
          }

          if(((String)nuevo.elementAt(0)).equals("C")) {
            e.rejectDrop();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                JOptionPane.showMessageDialog(
                  Parent,
                  getParameter(CICLES),
                  "Error Dialog",
                  JOptionPane.ERROR_MESSAGE
                );
              }
            });
            return;
          }

          if(((String)nuevo.elementAt(0)).equals("MM")) {
            e.rejectDrop();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                JOptionPane.showMessageDialog(
                  Parent,
                  getParameter(DISJOINT),
                  "Error Dialog",
                  JOptionPane.ERROR_MESSAGE
                );
              }
            });
            return;
          }

          if(((String)nuevo.elementAt(0)).equals("MI")) {
            e.rejectDrop();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                JOptionPane.showMessageDialog(
                  Parent,
                  getParameter(NOTSUBCLASS),
                  "Error Dialog",
                  JOptionPane.ERROR_MESSAGE
                );
              }
            });
            return;
          }

          if(((String)nuevo.elementAt(0)).equals("MJ")) {
            e.rejectDrop();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                JOptionPane.showMessageDialog(
                  Parent,
                  getParameter(EXHAUSTIVE),
                  "Error Dialog",
                  JOptionPane.ERROR_MESSAGE
                );
              }
            });
            return;
          }
        } catch(Exception ex) {
          e.rejectDrop();
        }

        //-- FIN SERVLET -------------------------------------------------------

        try {
          //if (!copyAction)
          Node   PNR=(Node)model.getRoot();
          String a1 =((NodeInfo)newParent.getUserObject()).getName();
          String b1 =((NodeInfo)PNR.getUserObject()).getName();
          Vector Actualizar;
          if(a1.equals(b1)){
            Vector items=GiveTerms(getSelectedNode(),model);
            Actualizar=Borrar(oldParent,items,model);
            Vector items2=GiveTerms(newParent,model);

            // oldParent.remove(getSelectedNode());
            newParent.add(newChild);
            model.reload(oldParent);
            model.reload(newParent);
           // model.reload();
          } else {
            Vector items=GiveTerms(getSelectedNode(),model);
            Actualizar=Borrar(oldParent,items,model);
            Vector items2=GiveTerms(newParent,model);

            Anadir(items2,model,Actualizar);
            int x=clave.intValue();
            clave=new Integer(x+1);
          }

          if(copyAction)
            e.acceptDrop(DnDConstants.ACTION_COPY);
          else
            e.acceptDrop(DnDConstants.ACTION_MOVE);

        } catch(java.lang.IllegalStateException ils) {
          e.rejectDrop();
        }

        e.getDropTargetContext().dropComplete(true);

        TreePath parentPath = new TreePath(newParent.getPath());
      } catch(IOException io) {
        e.rejectDrop();
      } catch(UnsupportedFlavorException ufe) {
        e.rejectDrop();
      }
    }

    private String testDropTarget(TreePath destination, TreePath dropper) {
      //Typical Tests for dropping

      //Test 1.
      boolean destinationPathIsNull = destination == null;
      if(destinationPathIsNull)
        //  return "Invalid drop location.";
        return getParameter(LOCATION);

      //Test 2.
      Node node=(Node)destination.getLastPathComponent();
      if(!node.getAllowsChildren() )
        //return "This node does not allow children";
        return getParameter(CHILD2);
      if(destination.equals(dropper))
        //return "Destination cannot be same as source";
        return getParameter(DEST);
      //Test 3.
      if(dropper.isDescendant(destination))
        //return "Destination node cannot be a descendant.";
        return getParameter(DESCEND);
      //Test 4.
      if(dropper.getParentPath().equals(destination))
        //return "Destination node cannot be a parent.";
        return getParameter(PARE);

      return null;
    }

    /**
     * DropTaregetListener interface method
     **/
    public void dragEnter(DropTargetDragEvent e) {
    }

    /**
     * DropTaregetListener interface method
     **/
    public void dragExit(DropTargetEvent e) {
    }

    /**
     * DropTaregetListener interface method
     **/
    public void dragOver(DropTargetDragEvent e) {
    }
  }

  private void _display(final String title, final String msg) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        JOptionPane.showMessageDialog(
          getParent(),
          msg,
          title,
          JOptionPane.PLAIN_MESSAGE
        );
      }
    });
  }

  public class MouseControl extends MouseAdapter {
    private JTree jtree;
    private int   i;

    public MouseControl(JTree jtree) {
      this(jtree,-1);
    }

    public MouseControl(JTree jtree, int i) {
      this.jtree=jtree;
      this.i    =i;
    }



    public void mouseClicked(MouseEvent e) {
      int      selRow =jtree.getRowForLocation(e.getX(),e.getY());
      TreePath selPath=jtree.getPathForLocation(e.getX(),e.getY());
      if(selRow!=-1&&e.getClickCount()==2) {
        //_display("Mouse clicked","Controller: "+i+" [button="+e.getButton()+", count="+e.getClickCount()+"]");
        switch (i) {
          case 0:
            _referenceDoubleClick(selRow,selPath);
            break;
          case 1:
            _importedTermDoubleClick(selRow,selPath);
            break;
          case 2:
            _constantDoubleClick(selRow,selPath);
            break;
          case 3:
            _formulaDoubleClick(selRow,selPath);
            break;
          case 4:
            _propertyDoubleClick(selRow,selPath);
            break;
          case 5:
            _groupDoubleClick(selRow,selPath);
            break;
          case 6:
            _enumeratedTypesDoubleClick(selRow,selPath);
            break;
          default:
            _doubleClick(selRow,selPath);
        }
      } else if(i<0&&selRow!=-1&&e.getClickCount()==1&&e.isMetaDown()) {
        _popup(e.getComponent(),e.getX(),e.getY(),selPath);
      }
    }
  }

  private void _popup(Component c, int x, int y, TreePath tp) {
          JPopupMenu jpm =new JPopupMenu();
    final TreePath   path=tp;

    if(path.getPath().length==1)
      return;

    ActionListener al=new ActionListener () {
      public void actionPerformed(ActionEvent ae) {
        AppletContext ctx=getAppletContext();

        Object[] opath=path.getPath();
        String   str  =ae.getActionCommand();
        try {
          URL urlTerm = null;
          if(str.equals (getParameter(SHOW_PROPERTIES))) {
            urlTerm = urlUpdateTerm;
          } else if(str.equals(getParameter(SHOW_CLASS_ATTRIBUTES))) {
            urlTerm = urlClassAttributes;
          } else if(str.equals(getParameter(SHOW_REFERENCES))) {
            urlTerm = urlReferences;
          } else if(str.equals(getParameter(SHOW_ABBREVIATIONS))) {
            urlTerm = urlAbbreviations;
          } else if(str.equals(getParameter(SHOW_SYNONYMS))) {
            urlTerm = urlSynonyms;
          } else if(str.equals(getParameter(SHOW_INSTANCE_ATTRIBUTES))) {
            urlTerm = urlInstanceAttributes;
          } else if(str.equals(getParameter(SHOW_SUBCLASS_OF))) {
            urlTerm = urlSubclassOf;
          } else if(str.equals(getParameter(SHOW_NOT_SUBCLASS_OF))) {
            urlTerm = urlNotSubclassOf;
          } else if(str.equals(getParameter(SHOW_DISJOINT))) {
            urlTerm = urlDisjoint;
          } else if(str.equals(getParameter(SHOW_EXHAUSTIVE))) {
            urlTerm = urlExhaustive;
          } else if(str.equals(getParameter(SHOW_TRANSITIVE_PART_OF))) {
            urlTerm = urlTransitivePartOf;
          } else if(str.equals(getParameter(SHOW_INTRANSITIVE_PART_OF))) {
            urlTerm = urlIntransitivePartOf;
          } else if(str.equals(getParameter(SHOW_ADHOC))) {
            urlTerm = urlAdhoc;
          } else if(str.equals(getParameter(SHOW_FORMULA))) {
            urlTerm = urlFormula;
          } else if(str.equals(getParameter(SHOW_INSTANCES))) {
            urlTerm = urlInstances;
          }

          URL url= new URL(urlTerm +
                           (urlTerm.toString().indexOf("?")<0?"?term=":"&term=")+
                           URLEncoder.encode ("" + opath[opath.length - 1],"ISO-8859-1"));

          ctx.showDocument (url, frameName);
        } catch (MalformedURLException mfue) {
          System.out.println ("Error in URL: " + mfue);
        } catch (UnsupportedEncodingException mfue) {
          System.out.println ("Error in URL: " + mfue);
        }
      }
    };

    JMenuItem jmi;
    // Relations submenu.
    JMenu jmr=new JMenu(getParameter(SHOW_RELATIONS));
    jmr.add(jmi=new JMenuItem(getParameter(SHOW_SUBCLASS_OF)));
    jmi.addActionListener(al);
    jmr.add(jmi=new JMenuItem(getParameter(SHOW_NOT_SUBCLASS_OF)));
    jmi.addActionListener(al);
    jmr.add(jmi= new JMenuItem(getParameter(SHOW_DISJOINT)));
    jmi.addActionListener(al);
    jmr.add(jmi=new JMenuItem(getParameter(SHOW_EXHAUSTIVE)));
    jmi.addActionListener(al);
    jmr.add(jmi=new JMenuItem(getParameter(SHOW_TRANSITIVE_PART_OF)));
    jmi.addActionListener(al);
    jmr.add(jmi=new JMenuItem(getParameter(SHOW_INTRANSITIVE_PART_OF)));
    jmi.addActionListener(al);
    jmr.addSeparator();
    jmr.add(jmi=new JMenuItem(getParameter(SHOW_ADHOC)));
    jmi.addActionListener(al);

    jpm.add(jmr);
    jpm.addSeparator();
    jpm.add(jmi=new JMenuItem(getParameter(SHOW_PROPERTIES)));
    jmi.addActionListener(al);
    jpm.add(jmi=new JMenuItem(getParameter(SHOW_CLASS_ATTRIBUTES)));
    jmi.addActionListener(al);
    jpm.add(jmi=new JMenuItem(getParameter(SHOW_INSTANCE_ATTRIBUTES)));
    jmi.addActionListener(al);
    jpm.add(jmi=new JMenuItem(getParameter(SHOW_REFERENCES)));
    jmi.addActionListener(al);
    jpm.add(jmi=new JMenuItem(getParameter(SHOW_SYNONYMS)));
    jmi.addActionListener(al);
    jpm.add(jmi=new JMenuItem(getParameter(SHOW_ABBREVIATIONS)));
    jmi.addActionListener(al);
    jpm.add(jmi=new JMenuItem(getParameter(SHOW_FORMULA)));
    jmi.addActionListener(al);
    jpm.addSeparator();
    jpm.add(jmi=new JMenuItem(getParameter(SHOW_INSTANCES)));
    jmi.addActionListener (al);

    // This code prevents the menu from getting out of
    // the window.
    int menuHeight=jpm.getPreferredSize().height;
    if(y+menuHeight>getSize().height)
      y-=menuHeight;
    jpm.show(c,x,y);
  }

  public void init () {
    clave        =new Integer(1);
    claveficticia=new Integer(-1);
    alreadyCount= new HashMap();

    Container   cont=getContentPane();
    JTabbedPane jtp =new JTabbedPane();
    String      ID;

    cont.setLayout(new BorderLayout ());
    setBackground(Color.white);

    try {
      ID=getParameter(IDSESION);
      sesionactual=ID;
      URL urlBase          =getDocumentBase();
      urlBase              =new URL(urlBase,"./");
      urlAdhoc             =new URL(urlBase+getParameter(URL_ADHOC));
      urlNotSubclassOf     =new URL(urlBase+getParameter(URL_NOT_SUBCLASS_OF));
      urlSubclassOf        =new URL(urlBase+getParameter(URL_SUBCLASS_OF));
      urlDisjoint          =new URL(urlBase+getParameter(URL_DISJOINT));
      urlExhaustive        =new URL(urlBase+getParameter(URL_EXHAUSTIVE));
      urlTransitivePartOf  =new URL(urlBase+getParameter(URL_TRANSITIVE_PART_OF));
      urlIntransitivePartOf=new URL(urlBase+getParameter(URL_INTRANSITIVE_PART_OF));
      urlNewTerm           =new URL(urlBase+getParameter(URL_NEW_TERM));
      urlUpdateTerm        =new URL(urlBase+getParameter(URL_UPDATE_TERM));
      urlClassAttributes   =new URL(urlBase+getParameter(URL_CLASS_ATTRIBUTES));
      urlInstanceAttributes=new URL(urlBase+getParameter(URL_INSTANCE_ATTRIBUTES));
      urlReferences        =new URL(urlBase+getParameter(URL_REFERENCES));
      urlNewReference      =new URL(urlBase+getParameter(URL_NEW_REFERENCE));
      urlUpdateReference   =new URL(urlBase+getParameter(URL_UPDATE_REFERENCE));
      urlAbbreviations     =new URL(urlBase+getParameter(URL_ABBREVIATIONS));
      urlSynonyms          =new URL(urlBase+getParameter(URL_SYNONYMS));
      urlImportedTerms     =new URL(urlBase+getParameter(URL_IMPORTED_TERMS));
      urlNewConstant       =new URL(urlBase+getParameter(URL_NEW_CONSTANT));
      urlUpdateConstant    =new URL(urlBase+getParameter(URL_UPDATE_CONSTANT));
      urlNewFormula        =new URL(urlBase+getParameter(URL_NEW_FORMULA));
      urlUpdateFormula     =new URL(urlBase+getParameter(URL_UPDATE_FORMULA));
      urlNewProperty       =new URL(urlBase+getParameter(URL_NEW_PROPERTY));
      urlUpdateProperty    =new URL(urlBase+getParameter(URL_UPDATE_PROPERTY));
      urlFormula           =new URL(urlBase+getParameter(URL_FORMULA));
      urlInstances         =new URL(urlBase+getParameter(URL_INSTANCES));
      urlNewGroup          =new URL(urlBase+getParameter(URL_NEW_GROUP));
      urlUpdateGroup       =new URL(urlBase+getParameter(URL_UPDATE_GROUP));

      // Enumerated types specific
      // Miguel Esteban Gutiérrez, 29/07/2004
      urlNewEnumeratedType   =new URL(urlBase+getParameter(URL_NEW_ENUMERATED_TYPE));
      urlUpdateEnumeratedType=new URL(urlBase+getParameter(URL_UPDATE_ENUMERATED_TYPE));
    } catch (Exception e) {
      System.err.println ("Error in init: " + e);
    }

    frameName=getParameter(FRAME_NAME);
    if(frameName==null)
      frameName="main1";

    JTree jt;
    JTree jt1;

    jtp.addTab(getParameter(References),
               null,
               new JScrollPane(jt=_buildReferenceTree()),
                               getParameter(Showsallavailablereferences));
    jt.addMouseListener(new MouseControl(jt,0));

    jtp.addTab(getParameter(Taxonomy),
               null,
               new JScrollPane(
                 jt1=new OdeJTree(models[0]=_buildGlossaryTreeModel(),
                                  cont,
                                  this)),
                 getParameter(Showstheglossaryofterms));
    jt1.addMouseListener(new MouseControl (jt1));
    jt1.addTreeWillExpandListener(this);

    jtp.addTab(getParameter(ImportedTerms),
               null,
               new JScrollPane(jt=_buildImportedTermsTree()),
                               getParameter(Showsalltheimportedterms));
    jt.addMouseListener(new MouseControl(jt,1));

    jtp.addTab(getParameter(Constants),
               null,
               new JScrollPane(jt=_buildConstantsTree()),
                               getParameter(Showsalltheconstants));
    jt.addMouseListener(new MouseControl(jt,2));

    jtp.addTab(getParameter(Formulas),
               null,
               new JScrollPane(jt=_buildFormulaTree()),
                               getParameter(Showsalltheformulas));
    jt.addMouseListener(new MouseControl(jt,3));

    jtp.addTab(getParameter(Propertie),
               null,
               new JScrollPane(jt=_buildPropertyTree()),
                               getParameter(Showsalltherelationproperties));
    jt.addMouseListener(new MouseControl(jt,4));

    jtp.addTab(getParameter(Groups),
               null,
               new JScrollPane(jt=_buildGroupTree()),
                               getParameter(Showsallthegroups));
    jt.addMouseListener (new MouseControl (jt, 5));

    // Enumerated types specific
    // Miguel Esteban Gutiérrez, 29/07/2004

    jtp.addTab(getParameter(EnumeratedTypes),
               null,
               new JScrollPane(jt=_buildEnumeratedTypesTree()),
                               getParameter(Showsalltheenumeratedtypes));
    jt.addMouseListener (new MouseControl (jt, 6));


    // By default, select the taxonomy tab.
    jtp.setSelectedIndex (1);

    setBackground(Color.white);
    cont.add(jtp,BorderLayout.CENTER);
  }

  private DefaultTreeModel _buildGlossaryTreeModel() {
    String root=getParameter(ROOT);
    ontologiaactual=root;

    // crear el objeto
    NodeInfo PIR=new NodeInfo(root,clave);
    Node     PNR=new Node(PIR);

    vRoot=new Vector();
    int k=clave.intValue();
    clave=new Integer(k+1);

    for(int i=0;;i++) {
      String parentc=getParameter(PARENT+i);
      String childc =getParameter(CHILD+i);

      if(parentc==null)
        break;

      if(childc==null){
        NodeInfo PI      =new NodeInfo(parentc,clave);
        Node     HijoRoot=new Node(PI);

        if(!PNR.Belong(HijoRoot)) {
          PNR.add(HijoRoot);
          int x=clave.intValue();
          clave=new Integer(x+1);
        }

        for(int j=0;;j++) {
          String child =getParameter(PARENT+j);
          String parent=getParameter(CHILD+j);

          if(parent==null&&child!=null)
            continue;
          if(child==null)
            break;

          // crear el objeto
          NodeInfo aux=(NodeInfo)HijoRoot.getUserObject();
          String   au =aux.getName();
          if(parent.equals(au)) {
            // crear el objeto
            Integer a=(((NodeInfo)HijoRoot.getUserObject()).getKey());
            int aa=a.intValue();
            claveficticia=new Integer(-aa);

            Node ficticio = new Node( new NodeInfo ("zft6801",claveficticia));
            if(!HijoRoot.Belong(ficticio)) {
              HijoRoot.add(ficticio);
            }
          }
        }
      }
    }

    return new DefaultTreeModel(PNR);
  }

  /**
   * FUNCION PARA AÑADIR NODOS BAJO DEMANDA LAZY TREE
   */
  private void _buildGlossaryTreeModelaux(Vector v,
                                          Integer concepto,
                                          Vector v2) {
    Vector v3=new Vector();
    Node   nodeaux;

    //v3.addElement("tx");

    DefaultTreeModel M=models[0];
    //HashMap hash = hashactual;
    Vector x=new Vector ();

    // NodeInfo PIConcepto  = new NodeInfo(concepto);
    Node root=(Node)M.getRoot();

    // BUSCO CONCEPTO EN LA LISTA DE NODOS
    Node node=null;

    if(root!=null) {
      for(Enumeration en=root.breadthFirstEnumeration();
          en.hasMoreElements();
          /**/) {
        Node     current=(Node)en.nextElement();
        NodeInfo nuevo  =(NodeInfo)current.getUserObject();
        Integer  auxilio=nuevo.getKey();

        if(concepto.equals(auxilio)) {
          node=current;
          break;
        }
      }
    }

    // TENGO EN node donde tengo que insertar
    //-- INICIO BORRAR NODO FICTICIO -------------------------------------------
    Node badNode;
    Integer x2=new Integer(-concepto.intValue());
/*
    int x1=concepto.intValue();

    x1=-x1;
    Integer x2=new Integer(x1);
*/

    for(Enumeration enume=root.breadthFirstEnumeration();
        enume.hasMoreElements();
        /**/) {
      Node     current=(Node)enume.nextElement();
      NodeInfo nuevo  =(NodeInfo)current.getUserObject();
      Integer  auxilio=nuevo.getKey();

      if(x2.equals(auxilio)) {
        badNode=current;
        node.remove(badNode);
        break;
      }
    }
    //-- FIN BORRAR NODO FICTICIO ----------------------------------------------

    int    j   =0;
    int    pos =0;
    Vector Vect=(Vector)v.clone();
    for(int i=0;i<Vect.size();i++) {
      String   aux  =(String)Vect.elementAt(i);
      NodeInfo PIaux=new NodeInfo(aux,clave);
      Node     node2=new Node(PIaux);

      if(!node.Belong(node2)) {
        node.add(node2);
        x.addElement(clave);
        int y=clave.intValue();
        clave=new Integer(y+1);
        pos++;
      } else {
        v.remove(pos);
        v2.remove(pos);
      }
    }

    for(int i=0;i<v.size();i++) {
      String elto=(String)v2.elementAt(i);
      if(!elto.equals("b")) {
        Integer comp=(Integer)x.elementAt(i);

        //NodeInfo PIelto  = new NodeInfo(comp,clave);
        // BUSCAR PIelto
        Node node3=null;
        for(Enumeration en=root.breadthFirstEnumeration();
            en.hasMoreElements();
            /**/) {
          Node     current=(Node)en.nextElement();
          NodeInfo nuevo  =(NodeInfo)current.getUserObject();
          Integer  auxilio=nuevo.getKey();
          if(comp.equals(auxilio)) {
            node3 = current;
            break;
          }
        }

        NodeInfo insertado=new NodeInfo(elto,clave);
        Node     insert   =new Node(insertado);
        Integer  a        =(((NodeInfo)node3.getUserObject()).getKey());
        int      aa       =a.intValue();
        claveficticia     =new Integer(-aa);

        Node fict=new Node(new NodeInfo("zft6801",claveficticia));
        if(!node3.Belong(fict)) {
          node3.add(fict);
        }
      }
    }
  }

  public JTree _buildReferenceTree () {
    return _buildTree(REFERENCE);
  }

  public JTree _buildImportedTermsTree () {
    return _buildTree(IMPORTED_TERM);
  }

  public JTree _buildConstantsTree () {
    return _buildTree(CONSTANT);
  }

  public JTree _buildFormulaTree () {
    return _buildTree(FORMULA);
  }

  public JTree _buildPropertyTree () {
    return _buildTree(PROPERTY);
  }

  public JTree _buildGroupTree () {
    return _buildTree(GROUP);
  }

  // Enumerated types specific
  // Miguel Esteban Gutiérrez, 29/07/2004
  public JTree _buildEnumeratedTypesTree () {
    return _buildTree(ENUMERATED_TYPE);
  }

  public JTree _buildTree(String param) {
    Hashtable hash=new Hashtable();

    String root =getParameter(ROOT);
    Vector vRoot=new Vector();
    hash.put(root,vRoot);

    String str;
    for(int i=0;(str=getParameter(param+i))!=null;i++) {
      vRoot.addElement (str);
    }

    return new JTree (hash);
  }

  public void _doubleClick(int selRow,TreePath path) {
    AppletContext ctx=getAppletContext();

    Object[] opath=path.getPath();
    if(opath.length==1) {
      ctx.showDocument(urlNewTerm,frameName);
    } else {
      try {
        URL url=new URL(urlUpdateTerm+"?term="+
                        URLEncoder.encode(""+opath[opath.length-1],
                                          "ISO-8859-1"));
        System.out.println("URL: "+url);

        ctx.showDocument(url,frameName);
      } catch(MalformedURLException mfue) {
        System.out.println("Error in URL: "+mfue);
      } catch(UnsupportedEncodingException mfue) {
        System.out.println("Error in URL: "+mfue);
      }
    }
  }

  private void _referenceDoubleClick(int selRow, TreePath path) {
    AppletContext ctx=getAppletContext();

    Object[] opath=path.getPath();
    if(opath.length==2) {
      ctx.showDocument(urlNewReference, frameName);
    } else {
      try {
        URL url=new URL(urlUpdateReference+"?reference_name="+
                        URLEncoder.encode(""+opath[opath.length-1],
                                          "ISO-8859-1"));
        System.out.println("URL: "+url);

        ctx.showDocument(url,frameName);
      } catch(MalformedURLException mfue) {
        System.out.println("Error in URL: "+mfue);
      } catch(UnsupportedEncodingException mfue) {
        System.out.println ("Error in URL: "+mfue);
      }
    }
  }

  private void _constantDoubleClick(int selRow, TreePath path) {
    AppletContext ctx=getAppletContext();

    Object[] opath=path.getPath();
    if(opath.length==2) {
      ctx.showDocument(urlNewConstant,frameName);
    } else {
      try {
        URL url=new URL(urlUpdateConstant+"?name="+
                        URLEncoder.encode(""+opath[opath.length-1],
                                          "ISO-8859-1"));
        System.out.println("URL: "+url);

        ctx.showDocument(url,frameName);
      } catch(MalformedURLException mfue) {
        System.out.println("Error in URL: "+mfue);
      } catch(UnsupportedEncodingException mfue) {
        System.out.println("Error in URL: "+mfue);
      }
    }
  }

  private void _formulaDoubleClick(int selRow, TreePath path) {
    AppletContext ctx=getAppletContext();

    Object[] opath=path.getPath();
    if(opath.length==2) {
      ctx.showDocument(urlNewFormula,frameName);
    } else {
      try {
        URL url=new URL(urlUpdateFormula+"?name="+
                        URLEncoder.encode(""+opath[opath.length-1],
                                          "ISO-8859-1"));
        System.out.println("URL : "+url);

        ctx.showDocument(url,frameName);
      } catch(MalformedURLException mfue) {
        System.out.println("Error in URL: "+mfue);
      } catch(UnsupportedEncodingException mfue) {
        System.out.println("Error in URL: "+mfue);
      }
    }
  }

  private void _propertyDoubleClick (int selRow, TreePath path) {
    AppletContext ctx=getAppletContext();

    Object[] opath=path.getPath();
    if(opath.length==2) {
      ctx.showDocument(urlNewProperty,frameName);
    } else {
      try {
        URL url=new URL(urlUpdateProperty+"?name="+
                        URLEncoder.encode(""+opath[opath.length-1],
                                          "ISO-8859-1"));
        System.out.println("URL: "+url);

        ctx.showDocument(url,frameName);
      } catch(MalformedURLException mfue) {
        System.out.println("Error in URL: "+mfue);
      } catch(UnsupportedEncodingException mfue) {
        System.out.println("Error in URL: "+mfue);
      }
    }
  }

  private void _groupDoubleClick (int selRow, TreePath path) {
    AppletContext ctx=getAppletContext();

    Object[] opath=path.getPath();
    if(opath.length==2) {
      ctx.showDocument(urlNewGroup, frameName);
    } else {
      try {
        URL url=new URL(urlUpdateGroup+"?name="+
                        URLEncoder.encode(""+opath[opath.length-1],
                                          "ISO-8859-1"));
        System.out.println("URL: "+url);

        ctx.showDocument(url,frameName);
      } catch(MalformedURLException mfue) {
        System.out.println("Error in URL: "+mfue);
      } catch(UnsupportedEncodingException mfue) {
        System.out.println("Error in URL: "+mfue);
      }
    }
  }

  private void _importedTermDoubleClick(int selRow, TreePath path) {
    AppletContext ctx=getAppletContext();

    ctx.showDocument(urlImportedTerms, frameName);
  }

  // Enumerated types specific
  // Miguel Esteban Gutiérrez, 29/07/2004
  private void _enumeratedTypesDoubleClick(int selRow, TreePath path) {
    AppletContext ctx=getAppletContext();

    Object[] opath=path.getPath();
    if(opath.length==2) {
      ctx.showDocument(urlNewEnumeratedType, frameName);
    } else {
      try {
        URL url=new URL(urlUpdateEnumeratedType+"?action=init&enumerated_type="+
                        URLEncoder.encode(""+opath[opath.length-1],
                                          "ISO-8859-1"));
        System.out.println("URL: "+url);

        ctx.showDocument(url,frameName);
      } catch(MalformedURLException mfue) {
        System.out.println("Error in URL: "+mfue);
      } catch(UnsupportedEncodingException mfue) {
        System.out.println("Error in URL: "+mfue);
      }
    }
  }

  // Required by TreeWillExpandListener interface.
  public void treeWillExpand(TreeExpansionEvent e) {
    try{
      TreePath path    =e.getPath();
      Node     conc    =(Node)path.getLastPathComponent();
      NodeInfo c       =(NodeInfo)conc.getUserObject();
      String   concepto=c.getName();
      Integer  key     =c.getKey();

      URL      direccion=new URL(this.getCodeBase(),
                                 "/webode/servlet/GiveTerms");

      ConceptData   TheDatosServlet=new ConceptData(concepto,ontologiaactual);
      URLConnection Conexion       =direccion.openConnection();

      Conexion.setDoOutput(true);
      Conexion.setUseCaches(false);
      Conexion.setDefaultUseCaches(false);

      // Specify the content type that we will send binary data
      Conexion.setRequestProperty("Content-Type","application/octet-stream");
      Conexion.setRequestProperty("Cookie","JSESSIONID="+sesionactual);

      // send the student object to the servlet using serialization
      ObjectOutputStream outputToServlet=
        new ObjectOutputStream(Conexion.getOutputStream());

      // serialize the object
      outputToServlet.writeObject(TheDatosServlet);
      outputToServlet.flush();
      outputToServlet.close();

      // Now read in the response
      ObjectInputStream inputFromServlet=
        new ObjectInputStream(Conexion.getInputStream());
      Vector SubclassVector=(Vector)inputFromServlet.readObject();
      Vector v             =new Vector(SubclassVector.size()/2);
      Vector v2            =new Vector(SubclassVector.size()/2);

      for(int i=0;i<SubclassVector.size();i++) {
        if(i==0) {
          v.addElement((String)SubclassVector.elementAt(i));
        } else {
          if(i%2==0) {
            v.addElement((String)SubclassVector.elementAt(i));
          } else {
            v2.addElement((String)SubclassVector.elementAt(i));
          }
        }
      }

      _buildGlossaryTreeModelaux(v,key,v2);
      NodosDesarrollados.put(concepto,  new Integer(1));
    } catch(Exception m) {
      System.out.println("Error: "+m);
    }
  }

  // Required by TreeWillExpandListener interface.
  public void treeWillCollapse(TreeExpansionEvent e) {

  }
}