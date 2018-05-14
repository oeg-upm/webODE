package es.upm.fi.dia.ontology.webode.translat.internalstructure;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.ODEService;
import es.upm.fi.dia.ontology.webode.service.WebODEException;

public class ISImportedTerm extends ISStructureElement
{

  String namespace;
  String namespace_identifier;
  String URI;


  public ISImportedTerm ()
  {
  }

  public ISImportedTerm (String namespace,String namespace_identifier,String URI)
  {
          this.namespace=namespace;
        this.namespace_identifier=namespace_identifier;
        this.URI=URI;
  }

  public String getNamespace()
  {
          return namespace;
  }

  public String getNamespaceIdentifier()
  {
          return namespace_identifier;
  }

  public String getURI()
  {
          return URI;
  }

  public void setNamespace (String namespace)
  {
          this.namespace=namespace;
  }

  public void setNamespaceIdentifier (String namespace_identifier)
  {
          this.namespace_identifier=namespace_identifier;
  }

  public void setURI (String URI)
  {
          this.URI=URI;
  }

 // Namespace, Namespace-Identifier, URI)>


  public Node obtainXML(Document owner_document)
  {
  Element imported_term_element=owner_document.createElement("Imported-Term");

  if (URI!=null)
      addElement (owner_document,imported_term_element,"URI",URI);

  if (namespace!=null)
      addElement (owner_document,imported_term_element,"namespace",namespace);

  if (namespace_identifier!=null)
      addElement (owner_document,imported_term_element,"namespace_identifier",namespace_identifier);
  else
          System.out.println("----------------HOLKA-------------------------------------");
  return imported_term_element;
  }

  public void toWebODE(ODEService ode, String ontologyName) throws java.rmi.RemoteException, java.sql.SQLException, WebODEException, ISException
  {
//    System.out.println("ode.importTerm('"+ontologyName+"','"+namespace+"','"+namespace_identifier+"',null,'"+URI+"')");
    ode.importTerm(ontologyName,namespace,namespace_identifier,namespace_identifier + ":" + URI.substring(URI.lastIndexOf('#')+1),URI);
  }

}