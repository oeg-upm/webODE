package es.upm.fi.dia.ontology.webode.util;
import java.io.*;
import java.awt.datatransfer.*;
import java.util.*;


public class NodeInfo implements  Transferable,Serializable {



  final public static DataFlavor INFO_FLAVOR =
      new DataFlavor(NodeInfo.class, "Personal Information");

  static DataFlavor flavors[] = {INFO_FLAVOR };
  private NodeInfo Parent = null;
  private Vector Children = new Vector();
  private String Name = null;
// CLAVE
  Integer Key;




  public NodeInfo(String name,Integer key) {
    Key = key;
    Name = name;

  }
  public Vector getChildren() {



    return Children;
  }

  public Integer getKey() {
   return Key;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public void add(NodeInfo info) {

    info.setParent(this);


    Children.add(info);


  }

  public void remove(NodeInfo info) {

    info.setParent(null);

    Children.remove(info);

  }

  public NodeInfo getParent() {
    return Parent;
  }

  public void setParent(NodeInfo parent) {
    Parent = parent;
  }

  public Vector getChilds() {
    return Children;
  }


  public Object clone() {
    return new NodeInfo(Name,Key);
  }

  public String toString() {
    return Name;
  }



  // --------- Transferable --------------

  public boolean isDataFlavorSupported(DataFlavor df) {
    return df.equals(INFO_FLAVOR);
  }

  /** implements Transferable interface */
  public Object getTransferData(DataFlavor df)
      throws UnsupportedFlavorException, IOException {
    if (df.equals(INFO_FLAVOR)) {
      return this;
    }
    else throw new UnsupportedFlavorException(df);
  }

  /** implements Transferable interface */
  public DataFlavor[] getTransferDataFlavors() {
    return flavors;
  }



  // --------- Serializable --------------

  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

  private void readObject(java.io.ObjectInputStream in)
      throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }
}