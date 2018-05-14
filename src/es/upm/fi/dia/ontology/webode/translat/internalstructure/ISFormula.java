package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;

public class ISFormula extends ISStructureElement
{
  private String name;

  private String type;
  private String expression;
  private Vector related_references;

  public void toWebODE(ODEService ode, String ontologyName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException
  {
    ode.insertReasoningElement(new FormulaDescriptor(ontologyName,name,description,expression,FormulaDescriptor.getNumericType(this.type)));
    for(Enumeration enum=related_references.elements(); enum.hasMoreElements(); )
      ode.relateReferenceToTerm(ontologyName,(String)enum.nextElement(),name);
  }
  public ISFormula (String formula_name,String formula_type, String formula_expression)
  {
    this();
    name=formula_name;
    type=formula_type;
    expression=formula_expression;

  }

  public ISFormula ()
  {
    related_references=new Vector();
  }


  public void setName (String formula_name)
  {
    name=formula_name;
  }

  public String getName ()
  {
    return name;
  }


  public void setType (String formula_type)
  {
    type= formula_type;
  }

  public void setExpression (String formula_expression)
  {
    expression=formula_expression;
  }





  public Node obtainXML (Document owner_document)
  {

    Element formula_element= owner_document.createElement("Formula");
    if (name!=null)
      addElement(owner_document,formula_element,"Name",name);

    if (description!=null)
      addElement(owner_document,formula_element,"Description",description);

    if (type!=null)
      addElement(owner_document,formula_element,"Type",type);

    if (expression!=null)
      addElement(owner_document,formula_element,"Expression",expression);


    for (Enumeration e = related_references.elements() ; e.hasMoreElements() ;)
      addElement (owner_document,formula_element,"Related-Reference",(String)(e.nextElement()));


    return formula_element;

  }
}