package es.upm.fi.dia.ontology.webode.util;
import javax.swing.tree.*;
import java.util.*;

/** This class forces "male" nodes to have leaf icons and
forbids male childbaring ability */
public class Node extends DefaultMutableTreeNode  {
//private Vector Children = new Vector();

  public Node(NodeInfo info) {
    super(info);
  }


 public void add(DefaultMutableTreeNode child) {
    super.add(child);


    NodeInfo childPI = (NodeInfo) ((Node) child).getUserObject();

    NodeInfo oldParent = childPI.getParent();
    //if (parent != null) oldParent.remove(childPI);

    NodeInfo newParent = (NodeInfo) getUserObject();

    newParent.add(childPI);
    //Children.add(childPI);
    NodeInfo aux = (NodeInfo) this.getUserObject();


  }


  public void remove(DefaultMutableTreeNode child) {


    super.remove(child);

    NodeInfo childPI = (NodeInfo) ((Node) child).getUserObject();

    NodeInfo ParentPI = (NodeInfo) getUserObject();

   // if (!parent != null){

      ParentPI.remove(childPI);

  }
/*
  public boolean getAllowsChildren() {
     //Note: Male == true;
     return (((PersonalInfo) getUserObject()).getChildren() != null) && posible;
  }
  */
  public boolean Belong(Node PN){
       Vector Children =  ((NodeInfo)getUserObject()).getChilds() ;
       Enumeration e = Children.elements();
       boolean sigo = false;
       while (e.hasMoreElements() && (! sigo )){
          NodeInfo NI = (NodeInfo)e.nextElement();
          sigo = NI.getName().equals(((NodeInfo)PN.getUserObject()).getName());

       }

    return(sigo);

  }
}
