 package es.upm.fi.dia.ontology.webode.util;
  import javax.swing.tree.*;
 import java.util.*;

    public class NodeData implements java.io.Serializable
{
    // data members
    protected Node Parent_ant;
    protected Node Parent_act;
    protected Node Child;



    // constructors
    public NodeData()
    {
    }

    public NodeData(Node aParent_ant, Node aParent_act, Node aChild)
    {
        Parent_ant = aParent_ant;
        Parent_act = aParent_act;
        Child = aChild;

    }


    //  accessors
    public Node getParent_ant()
    {
        return Parent_ant;
    }

    public Node getParent_act()
    {
        return Parent_act;
    }
    public Node getChild()
    {
        return Child;
    }

 }