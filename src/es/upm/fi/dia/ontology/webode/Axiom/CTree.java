package es.upm.fi.dia.ontology.webode.Axiom;

import java.util.Vector;

public class CTree{

	public String node;
	public CTree left_tree;
	public CTree right_tree;
	public int type;
	public Vector lista;
	public String prolog;

	public void Add(CTree left,String node,int type,CTree right)
	{
		left_tree=left;
		right_tree=right;
		this.node=node;
		this.type=type;
	}
	
	public void Add(CTree left,String node,int type)
	{
		left_tree=left;
		right_tree=new CTree();
		right_tree.node="null";
		right_tree.type=CNodeType.NODO_HOJA;
		this.node=node;
		this.type=type;
	}

}