
package es.upm.fi.dia.ontology.webode.translat.internalstructure;
import java.util.*;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;

public class ISProperty extends ISStructureElement
{
    private String name;
    private String description;
    private Vector related_references;
    private Vector related_formulas;

    public void toWebODE(ODEService ode, String ontologyName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException
    {
      Term[] terms=ode.getTerms(name,new int[]{TermTypes.PROPERTY});
      if(terms==null || terms.length==0)
        ode.insertTerm(ontologyName,name,description,TermTypes.PROPERTY);
    }

	public ISProperty (String property_name)
	{
		name=property_name;
		related_references= new Vector();
		related_formulas= new Vector();
	}

	public void setDescription(String property_description)
	{

		description=property_description;
	}



	public void setName(String property_name)
	{
		name=property_name;

	}

	public String getName()
	{
		return name;
	}

	public void addFormula(String formula_name)
	{
		related_formulas.add(formula_name);
	}


	 public Node obtainXML (Document owner_document)
    {

     Element property_element= owner_document.createElement("Property");
    if (name!=null)
      	addElement(owner_document,property_element,"Name",name);

    if (description!=null)
      	addElement(owner_document,property_element,"Description",description);


	for (Enumeration e = related_references.elements() ; e.hasMoreElements() ;)
		addElement (owner_document,property_element,"Related-Reference",(String)(e.nextElement()));

	for (Enumeration e = related_formulas.elements() ; e.hasMoreElements() ;)
		addElement (owner_document,property_element,"Related-Formula",(String)(e.nextElement()));


     return property_element;

    }



}
