package es.upm.fi.dia.ontology.webode.xml;

// Java and XML stuff
import java.io.*;
import com.sun.xml.tree.*;
import org.w3c.dom.*;
import java.rmi.*;
import java.sql.*;
import java.util.*;

// Minerva Stuff
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.others.*;

// WebODE stuff
import es.upm.fi.dia.ontology.webode.service.*;

/**
 * Export service.
 * <p>
 * This service provides an export of an ontology in XML format,
 * according to WebODE's DTD.
 *
 * @author  Julio César Arpírez Vega
 * @author  Oscar Corcho
 * @author  Rafael González Cabero.
 * @version 1.3
 */
public class ExportServiceImp extends MinervaServiceImp implements ExportService, XMLConstants
{
  public static final String ABOUT_TEXT =
      "\n\n\t====================================================================================\n" +
      "\tAutomatically generated XML file.\n\t" +
      es.upm.fi.dia.ontology.webode.servlet.WebODEConstants.VERSION + ".\n\t" +
      "(c) Laboratory of Artificial Intelligence, 2000.  School of Computer Science (FI).\n" +
      "\tTechnical University of Madrid (UPM).\n" +
      "\t====================================================================================\n\n" ;

  // Back-up stuff.
  private boolean bFinished = true;
  private OntologyDescriptor[] aod;
  private int index = -1;

  /**
   * Default constructor.
   */
  public ExportServiceImp () throws RemoteException
  {
  }

  // Life-cycle methods --------------------------------------------------------------------------
  public void start () throws CannotStartException
  {
    String odeService = ((ExportServiceConfiguration) config).odeService;
    if (odeService == null) throw new CannotStartException ("No ODE service specified");
  }

  // Logic methods ----------------------------------------------------------
  public StringBuffer export (String ontology, boolean bConceptualization,
                              String[] instanceSets, String[] views, String dtd)
      throws IOException, DOMException, WebODEException {
    ODEService odeService = null;
    try {
      odeService=(ODEService)this.context.getService(((ExportServiceConfiguration) config).odeService);

      XmlDocument	doc = new XmlDocument ();
      ElementNode	root = (ElementNode) doc.createElement (ROOT);
      ElementNode conceptualization = (ElementNode) doc.createElement (CONCEPTUALIZATION);
      StringWriter out = new StringWriter ();


      // Add the ontology as the root
      // Data about the ontology
      _addOntology (odeService, ontology, doc, root);

      if (bConceptualization) {
        root.appendChild (conceptualization);

        // Add imported terms
        _addImportedTerms (odeService, ontology, doc, conceptualization);

        // Add references
        _addReferencesX (odeService, ontology, doc, conceptualization);

        // Add User Types
        _addTypes(odeService, ontology, doc, conceptualization);

        // Add terms
        _addTerms (odeService, ontology, doc, conceptualization);

        // Add groups
        _addGroups (odeService, ontology, doc, conceptualization);

        // Add relations
        _addRelations (odeService, ontology, doc, conceptualization);

        // Add formulas
        _addFormulasX (odeService, ontology, doc, conceptualization);

        // Add constants
        _addConstants (odeService, ontology, doc, conceptualization);

        // Add properties
        _addPropertiesX (odeService, ontology, doc, conceptualization);
      }

      // Add instance sets
      _addInstanceSets (odeService, ontology, doc, root, instanceSets);

      // Add views
      _addViews (odeService, ontology, doc, root, views);

      // Add WebODE's DTD as defined in servlet.properties. Sent by the servlet
      doc.setDoctype (
          null,	// no public identifier
          dtd,	//instead of    DTD,
          null	// no internal subset
          );

     /*PrintWriter pw = new PrintWriter (new FileWriter ("d:\\temp\\esther.xml"));
     doc.write (pw, ENCODING);
     pw.close ();*/

      doc.write (out, ENCODING);

      // Return document
      return out.getBuffer();
    }
    catch (Exception ex) {
      throw new WebODEException ("Error exporting ontology: " + ex.getMessage(), ex);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }

  private void _addOntology (ODEService odeService, String ontology, XmlDocument doc, Element root)
      throws WebODEException, SQLException, RemoteException, RemoteException
  {
    Element ontologyElement;
    OntologyDescriptor od = odeService.getOntologyDescriptor (ontology);
    //OntologyDescriptor od = OntologyDescriptor.getOntologyDescriptor (con, ontology);

    root.appendChild (doc.createComment ("***********************************************\n" +
        "             Ontology Description\n" +
        "***********************************************\n"));

    doc.appendChild (doc.createComment (ABOUT_TEXT));

    doc.appendChild (root);
    root.appendChild (ontologyElement = doc.createElement (NAME));
    ontologyElement.appendChild (doc.createTextNode (ontology));

    if (od.namespace != null) {
      root.appendChild (ontologyElement = doc.createElement (ONT_NAMESPACE));
      ontologyElement.appendChild (doc.createTextNode (od.namespace));
    }

    if (od.description != null) {
      root.appendChild (ontologyElement = doc.createElement (DESCRIPTION));
      ontologyElement.appendChild (doc.createTextNode (od.description));
    }
    root.appendChild (ontologyElement = doc.createElement (AUTHOR));
    ontologyElement.appendChild (doc.createTextNode (od.login));

    root.appendChild (ontologyElement = doc.createElement (CREATION_DATE));
    ontologyElement.appendChild (doc.createTextNode ("" + od.creationDate));

    // References for the ontology
    _addReferences (odeService, doc, root, ontology, null);
  }

  private void _addTypes (ODEService odeService, String ontology, XmlDocument doc, ElementNode root)
      throws IOException, DOMException, SQLException, WebODEException {
    EnumeratedType[] enum=odeService.getEnumeratedTypes(ontology);
    if(enum!=null) {
      Element typesNode, enumNode, typeName, typeDescription, typeValue;
      root.appendChild(typesNode=doc.createElement(TYPES));
      for(int i=0; i<enum.length; i++) {
        typesNode.appendChild(enumNode=doc.createElement(ENUMERATED_TYPE));
        enumNode.appendChild(typeName=doc.createElement(NAME));
        typeName.appendChild(doc.createTextNode(enum[i].name));

        if(enum[i].description!=null && enum[i].description.length()>0) {
          enumNode.appendChild(typeDescription=doc.createElement(DESCRIPTION));
          typeDescription.appendChild(doc.createTextNode(enum[i].description));
        }

        if(enum[i].values!=null) {
          for(int j=0; j<enum[i].values.length; j++) {
            enumNode.appendChild(typeValue=doc.createElement(VALUE));
            typeValue.appendChild(doc.createTextNode(enum[i].values[j]));
          }
        }
      }
    }
  }

  private void _addTerms (ODEService odeService, String ontology,
                          XmlDocument doc, ElementNode root)
      throws IOException, DOMException, SQLException, WebODEException
  {
    _addTerms (odeService, ontology, doc, root, TERM, new int [] {TermTypes.CONCEPT}, CONCEPTS);
  }


  /**
   * Adds the terms to the XML document.
   */
  private void _addTerms (ODEService odeService, String ontology,
                          XmlDocument doc, ElementNode root,
                          String leaf, int[] types, String comment)
      throws IOException, DOMException, SQLException, WebODEException
  {
    Element termNode;
    Element termName, termName1;
    Element termDescription;

    // Get concepts
    Term[] aterm= odeService.getTerms(ontology,types);
//    Term[] aterm = Term.getTerms (con, ontology, types);

    if (aterm != null) {
      root.appendChild (doc.createComment ("***********************************************\n" +
          "                  " + comment + "\n" +
          "***********************************************"));


      for (int i = 0; i < aterm.length; i++) {
        //System.out.println ("Export term " + i);

        root.appendChild (termNode = doc.createElement (leaf));
        termNode.appendChild (termName = doc.createElement (NAME));
        termName.appendChild (doc.createTextNode (aterm[i].term));
        if (aterm[i].des != null) {
          termNode.appendChild (termDescription = doc.createElement (DESCRIPTION));
          termDescription.appendChild (doc.createTextNode (aterm[i].des));
        }

        if (types[0] == TermTypes.CONCEPT) {
          // Class attributes
          _addClassAttributes (odeService, doc, termNode, ontology, aterm[i].term);

          // Instance attributes
          _addInstanceAttributes (odeService, doc, termNode, ontology, aterm[i].term);

          // Synonyms
          _addSynonyms (odeService, doc, termNode, ontology, aterm[i].term, true);

          // Abbreviations
          _addSynonyms (odeService, doc, termNode, ontology, aterm[i].term, false);
        }

        // References
        _addReferences (odeService, doc, termNode, ontology, aterm[i].term);


        // Formulas
        _addFormulas (odeService, doc, termNode, ontology, aterm[i].term);

        // Leave a blank line
        root.appendChild (doc.createTextNode ("\n"));
      }
    }
  }


  private void _addSynonyms (ODEService odeService, XmlDocument doc,
                             Element termNode, String ontology,
                             String term, boolean bSynonym)
      throws SQLException, WebODEException, RemoteException
  {
    _addSynonyms (odeService, doc, termNode, ontology, term, null, bSynonym);
  }


  private void _addSynonyms (ODEService odeService, XmlDocument doc,
                             Element termNode, String ontology,
                             String term, String attribute, boolean bSynonym)
      throws SQLException, WebODEException, RemoteException
  {
    Element el, el1;
    String attr = attribute == null ? term : attribute;
    String ter = attribute == null ? null : term;

    SynonymAbbreviation[] asa = null;
    if (bSynonym)
      asa=odeService.getSynonyms (ontology, attr, ter);
      //asa = SynonymAbbreviation.getSynonyms (con, ontology, attr, ter);
    else

      asa=odeService.getAbbreviations (ontology, attr, ter);
      //asa = SynonymAbbreviation.getAbbreviations (con, ontology, attr, ter);

    if (asa != null) {
      for (int i = 0; i < asa.length; i++) {
        termNode.appendChild (el = doc.createElement (bSynonym ? SYNONYM : ABBREVIATION));
        el.appendChild (el1 = doc.createElement (NAME));
        el1.appendChild (doc.createTextNode (asa[i].name));
        if (asa[i].description != null) {
          el.appendChild (el1 = doc.createElement (DESCRIPTION));
          el1.appendChild (doc.createTextNode (asa[i].description));
        }
      }
    }
  }

  private void _addReferences (ODEService odeService, XmlDocument doc,
                               Element termNode, String ontology,
                               String term)
      throws SQLException, WebODEException, RemoteException
  {
    _addReferences (odeService, doc, termNode, ontology, term, null);
  }

  private void _addReferences (ODEService odeService, XmlDocument doc,
                               Element termNode, String ontology,
                               String term, String attribute)
      throws SQLException, WebODEException, RemoteException
  {
    Element el;

    ReferenceDescriptor[] ard;
    if (term == null) {
      ard = odeService.getOntologyReferences(ontology);
     // ard = ReferenceDescriptor.getOntologyReferences(con, ontology);
    }
    else {
      if (attribute == null)
        ard= odeService.getTermReferences(ontology, term);
     //   ard = ReferenceDescriptor.getTermReferences (con, ontology, term);

      else
        ard= odeService.getTermReferences(ontology, attribute, term);
        //ard = ReferenceDescriptor.getTermReferences (con, ontology, attribute, term);
    }

    if (ard != null) {
      for (int i = 0; i < ard.length; i++) {
        termNode.appendChild (el = doc.createElement (RELATED_REFERENCE));
        el.appendChild (doc.createTextNode (ard[i].name));
      }
    }
  }

  private void _addFormulas (ODEService odeService, XmlDocument doc,
                             Element termNode, String ontology, String term)
      throws SQLException, WebODEException, RemoteException
  {
    _addFormulas (odeService, doc, termNode, ontology, term, null);
  }


  private void _addFormulas (ODEService odeService, XmlDocument doc,
                             Element termNode, String ontology,
                             String term, String attribute)
      throws SQLException, WebODEException, RemoteException
  {

    Element el;

    FormulaDescriptor[] ard;
    if (attribute == null)
      ard=odeService.getReasoningElements (ontology, term);
      //ard = FormulaDescriptor.getReasoningElements (con, ontology, term);
    else
      ard=odeService.getReasoningElements (ontology, attribute, term);
      //ard = FormulaDescriptor.getReasoningElements (con, ontology, attribute, term);

    if (ard != null) {
      for (int i = 0; i < ard.length; i++) {
        termNode.appendChild (el = doc.createElement (RELATED_FORMULA));
        el.appendChild (doc.createTextNode (ard[i].name));
      }
    }
  }

  private void _addAttributeInference (ODEService odeService, XmlDocument doc,
                                       Element termNode, String ontology, String term, String attribute)
      throws SQLException, WebODEException, RemoteException
  {
    Element el;
    AttributeInference[] aai = odeService.getAttributesInferredBy (ontology, term, attribute);
//    AttributeInference[] aai = AttributeInference.getAttributesInferredBy (ode,ontology, term, attribute);
    if (aai != null) {
      for (int i = 0; i < aai.length; i++) {
        termNode.appendChild (el = doc.createElement (INFERRED));
        el.appendChild (doc.createTextNode (aai[i].inferredAttr));
      }
    }
  }

  private void _addClassAttributes (ODEService odeService, XmlDocument doc,
                                    Element termNode,
                                    String ontology, String term)
      throws SQLException, WebODEException, RemoteException
  {
    _addClassInstanceAttributes (odeService, doc, termNode, ontology, term, true);
  }

  private void _addInstanceAttributes (ODEService odeService, XmlDocument doc,
                                       Element termNode,
                                       String ontology, String term)
      throws SQLException, WebODEException, RemoteException
  {
    _addClassInstanceAttributes (odeService, doc, termNode, ontology, term, false);
  }

  private void _addClassInstanceAttributes (ODEService odeService, XmlDocument doc,
      Element termNode,
      String ontology, String term, boolean bClass)
      throws  WebODEException, SQLException, RemoteException
  {
    Element termName, termName1;
    Element termDescription;

    Attribute[] acad;
    if (bClass)
      acad = odeService.getClassAttributes (ontology, term);
      //acad = ClassAttributeDescriptor.getClassAttributes (ode, ontology, term);
    else
      acad = odeService.getInstanceAttributes (ontology, term);
//      acad = InstanceAttributeDescriptor.getInstanceAttributes (ode, ontology, term);

    if (acad != null) {
      for (int k = 0; k < acad.length; k++) {
        // Leave a blank line
        termNode.appendChild (doc.createTextNode ("\n"));

        termNode.appendChild (termName = doc.createElement (bClass ? CLASS_ATTRIBUTE : INSTANCE_ATTRIBUTE));
        termName.appendChild (termName1 = doc.createElement (NAME));
        termName1.appendChild (doc.createTextNode (acad[k].name));
        if (acad[k].description != null) {
          termName.appendChild (termDescription = doc.createElement (DESCRIPTION));
          termDescription.appendChild (doc.createTextNode (acad[k].description));
        }
        // Value type
        termName.appendChild (termName1 = doc.createElement (TYPE));
        termName1.appendChild (doc.createTextNode (acad[k].getValueName()));

        // Minimum cardinality
        termName.appendChild (termName1 = doc.createElement (MIN_CARDINALITY));
        termName1.appendChild (doc.createTextNode ("" + acad[k].minCardinality));

        // Maximum cardinality
        termName.appendChild (termName1 = doc.createElement (MAX_CARDINALITY));
        termName1.appendChild (doc.createTextNode ("" + acad[k].maxCardinality));

        // Measurement unit
        if (acad[k].measurementUnit != null) {
          termName.appendChild (termName1 = doc.createElement (MEASUREMENT_UNIT));
          termName1.appendChild (doc.createTextNode (acad[k].measurementUnit));
        }

        // Precision
        if (acad[k].precision != null) {
          termName.appendChild (termName1 = doc.createElement (PRECISION));
          termName1.appendChild (doc.createTextNode (acad[k].precision));
        }

        if (acad[k] instanceof InstanceAttributeDescriptor) {
          if (((InstanceAttributeDescriptor) acad[k]).minValue != null) {
            termName.appendChild (termName1 = doc.createElement (MIN_VALUE));
            termName1.appendChild (doc.createTextNode (((InstanceAttributeDescriptor) acad[k]).minValue));
          }
          if (((InstanceAttributeDescriptor) acad[k]).maxValue != null) {
            termName.appendChild (termName1 = doc.createElement (MAX_VALUE));
            termName1.appendChild (doc.createTextNode (((InstanceAttributeDescriptor) acad[k]).maxValue));
          }
        }

        // Values
        if (acad[k].values != null) {
          for (int j = 0; j < acad[k].values.length; j++) {
            termName.appendChild (termName1 = doc.createElement (VALUE));
            termName1.appendChild (doc.createTextNode (acad[k].values[j]));
          }
        }

        // References
        _addReferences (odeService, doc, termName, ontology, term, acad[k].name);

        // Synonyms
        _addSynonyms (odeService, doc, termName, ontology, term, acad[k].name, true);

        // Abbreviations
        _addSynonyms (odeService, doc, termName, ontology, term, acad[k].name, false);

        // Formulas
        _addFormulas (odeService, doc, termName, ontology, term, acad[k].name);

        // Inference of attributes
        _addAttributeInference (odeService, doc, termName, ontology, term, acad[k].name);
      }
    }
  }

  // Adds relations
  private void _addRelations (ODEService odeService, String ontology,
                              XmlDocument doc, ElementNode root)
      throws IOException, DOMException, SQLException, WebODEException
  {
    Element termNode;
    Element termName, termName1;
    Element termDescription;

    // Get concepts
    TermRelation[] atr = odeService.getTermRelations (ontology,false);
//    TermRelation[] atr = TermRelation.getTermRelations (ode, ontology);

    if (atr != null) {
      root.appendChild (doc.createTextNode ("\n"));
      root.appendChild (doc.createComment ("***********************************************\n" +
          "                  Relationships\n" +
          "***********************************************"));

      for (int i = 0; i < atr.length; i++) {
        root.appendChild (termNode = doc.createElement (TERM_RELATION));
        termNode.appendChild (termName = doc.createElement (NAME));
        termName.appendChild (doc.createTextNode (atr[i].name));
        termNode.appendChild (termName = doc.createElement (ORIGIN));
        termName.appendChild (doc.createTextNode (atr[i].origin));
        termNode.appendChild (termName = doc.createElement (DESTINATION));
        termName.appendChild (doc.createTextNode (atr[i].destination));
        termNode.appendChild (termName = doc.createElement (MAX_CARDINALITY));
        termName.appendChild (doc.createTextNode ("" + atr[i].maxCardinality));

        if (atr[i].properties != null) {
          for (int k = 0; k < atr[i].properties.length; k++) {
            termNode.appendChild (termName = doc.createElement (RELATED_PROPERTY));
            termName.appendChild (doc.createTextNode (atr[i].properties[k]));
          }
        }
      }
    }
  }

  // Adds formulas
  private void _addFormulasX (ODEService odeService, String ontology,
                              XmlDocument doc, ElementNode root)
      throws IOException, DOMException, SQLException, WebODEException
  {
    Element termNode;
    Element termName, termName1;
    Element termDescription;

    // Get concepts
    FormulaDescriptor[] atr = odeService.getReasoningElements (ontology);
    //    FormulaDescriptor[] atr = FormulaDescriptor.getReasoningElements (con, ontology);

    if (atr != null) {
      root.appendChild (doc.createTextNode ("\n"));
      root.appendChild (doc.createComment ("***********************************************\n" +
          "                   Formulas\n" +
          "***********************************************"));

      for (int i = 0; i < atr.length; i++) {
        root.appendChild (termNode = doc.createElement (FORMULA));
        termNode.appendChild (termName = doc.createElement (NAME));
        termName.appendChild (doc.createTextNode (atr[i].name));
        if (atr[i].description != null) {
          termNode.appendChild (termName = doc.createElement (DESCRIPTION));
          termName.appendChild (doc.createTextNode (atr[i].description));
        }
        termNode.appendChild (termName = doc.createElement (TYPE));
        termName.appendChild (doc.createTextNode (TermTypes.NAMES [atr[i].type - 1]));

        termNode.appendChild (termName = doc.createElement (EXPRESSION));
        termName.appendChild (doc.createTextNode (atr[i].expression));

        // References
        _addReferences (odeService, doc, termNode, ontology, atr[i].name);
      }
    }
  }

  // Adds properties
  private void _addPropertiesX (ODEService odeService, String ontology,
                                XmlDocument doc, ElementNode root)
      throws IOException, DOMException, SQLException, WebODEException
  {
    root.appendChild (doc.createTextNode ("\n"));
    _addTerms (odeService, ontology, doc, root, PROPERTY, new int[] { TermTypes.PROPERTY }, PROPERTIES);
  }

  // Adds references
  private void _addReferencesX (ODEService odeService, String ontology,
                                XmlDocument doc, ElementNode root)
      throws IOException, DOMException, SQLException, WebODEException
  {
    Element termName, termNode;

    ReferenceDescriptor[] ard;
    ard = odeService.getReferences (ontology);
    //ard = ReferenceDescriptor.getReferences (con, ontology);

    if (ard != null) {
      root.appendChild (doc.createTextNode ("\n"));
      root.appendChild (doc.createComment ("***********************************************\n" +
          "                  References\n" +
          "***********************************************"));

      for (int i = 0; i < ard.length; i++) {
        root.appendChild (termNode = doc.createElement (REFERENCE));
        termNode.appendChild (termName = doc.createElement (NAME));
        termName.appendChild (doc.createTextNode (ard[i].name));
        if (ard[i].description != null) {
          termNode.appendChild (termName = doc.createElement (DESCRIPTION));
          termName.appendChild (doc.createTextNode (ard[i].description));
        }
      }
    }
  }


  // Add imported terms
  private void _addImportedTerms (ODEService odeService, String ontology,
                                  XmlDocument doc, ElementNode root)
      throws IOException, DOMException, SQLException, WebODEException
  {
    Element termName, termNode;

    ImportedTerm[] ard;
    ard = odeService.getImportedTerms (ontology);
    //ard = ImportedTerm.getImportedTerms (con, ontology);

    if (ard != null) {
      root.appendChild (doc.createTextNode ("\n"));
      root.appendChild (doc.createComment ("***********************************************\n" +
          "                 Imported terms\n" +
          "***********************************************"));

      for (int i = 0; i < ard.length; i++) {
        root.appendChild (termNode = doc.createElement (IMPORTED_TERM));
        termNode.appendChild (termName = doc.createElement (NAME));
        termName.appendChild (doc.createTextNode (ard[i].name));
        termNode.appendChild (termName = doc.createElement (URI));
        termName.appendChild (doc.createTextNode (ard[i].URI));
        termNode.appendChild (termName = doc.createElement (NAMESPACE));
        termName.appendChild (doc.createTextNode (ard[i].namespace));
        termNode.appendChild (termName = doc.createElement (NAMESPACE_IDENTIFIER));
        termName.appendChild (doc.createTextNode (ard[i].namespace_identifier));
        //URI, namespace, namespaceidentifier
       /* termNode.appendChild (termName = doc.createElement (ORIGINAL_NAME));
        termName.appendChild (doc.createTextNode (ard[i].originalName));
        termNode.appendChild (termName = doc.createElement (NAME));
        termName.appendChild (doc.createTextNode (ard[i].name));
        termNode.appendChild (termName = doc.createElement (URL));
        termName.appendChild (doc.createTextNode (ard[i].URL));*/
      }
    }
  }

  // Add imported terms
  private void _addGroups (ODEService odeService, String ontology,
                           XmlDocument doc, ElementNode root)
      throws IOException, DOMException, WebODEException
  {
    Element termName, termNode;

    Group[] ard = odeService.getGroups (ontology);

    if (ard != null) {
      root.appendChild (doc.createTextNode ("\n"));
      root.appendChild (doc.createComment ("***********************************************\n" +
          "                   Groups\n" +
          "***********************************************"));

      for (int i = 0; i < ard.length; i++) {
        root.appendChild (termNode = doc.createElement (GROUP));
        termNode.appendChild (termName = doc.createElement (NAME));
        termName.appendChild (doc.createTextNode (ard[i].name));
        if (ard[i].description != null) {
          termNode.appendChild (termName = doc.createElement (DESCRIPTION));
          termName.appendChild (doc.createTextNode (ard[i].description));
        }
        for (int k = 0; k < ard[i].concepts.length; k++) {
          termNode.appendChild (termName = doc.createElement (RELATED_CONCEPT));
          termName.appendChild (doc.createTextNode (ard[i].concepts[k]));
        }
      }
    }
  }


  private void _addInstanceSets (ODEService odeService, String ontology,
                                 XmlDocument doc, ElementNode root,
                                 String[] instanceSets)
      throws IOException, DOMException, SQLException, WebODEException
  {
    Element termName, t1, termNode, instance, t2, tk;

    if (instanceSets != null) {
      root.appendChild (doc.createTextNode ("\n"));
      root.appendChild (doc.createComment ("***********************************************\n" +
          "                   Instances\n" +
          "***********************************************"));
      root.appendChild (termNode = doc.createElement (INSTANCES));

      for (int i = 0; i < instanceSets.length; i++) {
        // Retrieve instance set information
        Term term = odeService.getTerm (ontology, instanceSets[i]);

        if (term != null) {
          root.appendChild (doc.createTextNode ("\n"));
          termNode.appendChild (doc.createComment (" \"" + term.term + "\" instance set "));

          termNode.appendChild (termName = doc.createElement (INSTANCE_SET));
          termName.appendChild (t1 = doc.createElement (NAME));
          t1.appendChild (doc.createTextNode (term.term));
          if (term.des != null) {
            termName.appendChild (t1 = doc.createElement (DESCRIPTION));
            t1.appendChild (doc.createTextNode (term.des));
          }

          // Retrieve instances
          Instance[] aterm = odeService.getInstInstances (ontology, instanceSets[i]);
          //Instance[] aterm = Instance.getInstances (ode, ontology, instanceSets[i]);

          if (aterm != null) {
            for (int k = 0; k < aterm.length; k++) {
              termName.appendChild (instance = doc.createElement (INSTANCE));
              instance.appendChild (t1 = doc.createElement (NAME));
              t1.appendChild (doc.createTextNode (aterm[k].name));
              instance.appendChild (t1 = doc.createElement (INSTANCE_OF));
              t1.appendChild (doc.createTextNode (aterm[k].term));

              if (aterm[k].description != null) {
                instance.appendChild (t1 = doc.createElement (DESCRIPTION));
                t1.appendChild (doc.createTextNode (aterm[k].description));
              }

              // Get values now
              HashMap elements = odeService.getLogicalInstanceValues
              (ontology, instanceSets[i], aterm[k].name);

              if (elements != null) {
                Iterator iterator = elements.keySet().iterator();

                while (iterator.hasNext()) {
                  String str = (String) iterator.next();

                  instance.appendChild (tk = doc.createElement (CLASS));
                  tk.appendChild (t1 = doc.createElement (NAME));
                  t1.appendChild (doc.createTextNode (str));

                  HashMap hashMap = (HashMap) elements.get (str);
                  Iterator iterator1 = hashMap.keySet().iterator();

                  while (iterator1.hasNext()) {
                    String ik = (String) iterator1.next();
                    String[] values = (String[]) hashMap.get (ik);
                    for (int l = 0; l < values.length; l++) {
                      tk.appendChild (t1 = doc.createElement (ATTRIBUTE));
                      t1.appendChild (t2 = doc.createElement (NAME));
                      t2.appendChild (doc.createTextNode (ik));
                      t1.appendChild (t2 = doc.createElement (VALUE));
                      t2.appendChild (doc.createTextNode (values[l]));
                    }
                  }
                }
              }
            }
          }

          //-------------------------------------------------------------------------------
          // Relation instances
          TermRelationInstance[] atri = odeService.getRelationInstances(ontology, instanceSets[i]);
          // TermRelationInstance[] atri = TermRelationInstance.getRelationInstances(ode, ontology, instanceSets[i]);
          if (atri != null) {
            for (int k = 0; k < atri.length; k++) {
              termName.appendChild (instance = doc.createElement (RELATION_INSTANCE));
              instance.appendChild (t1 = doc.createElement (NAME));
              t1.appendChild (doc.createTextNode (atri[k].name));
              instance.appendChild (t1 = doc.createElement (INSTANCE_OF));
              t1.appendChild (doc.createTextNode (atri[k].termRelation.name));
              instance.appendChild (t1 = doc.createElement (ORIGIN_CONCEPT));
              t1.appendChild (doc.createTextNode (atri[k].termRelation.origin));
              instance.appendChild (t1 = doc.createElement (DESTINATION_CONCEPT));
              t1.appendChild (doc.createTextNode (atri[k].termRelation.destination));

              if (atri[k].description != null) {
                instance.appendChild (t1 = doc.createElement (DESCRIPTION));
                t1.appendChild (doc.createTextNode (atri[k].description));
              }

              // Origin and destination
              instance.appendChild (t1 = doc.createElement (ORIGIN));
              t1.appendChild (doc.createTextNode (atri[k].origin));
              instance.appendChild (t1 = doc.createElement (DESTINATION));
              t1.appendChild (doc.createTextNode (atri[k].destination));
            }
          }
        }
      }
    }
  }

  // Adds constants
  private void _addConstants (ODEService odeService, String ontology,
                              XmlDocument doc, ElementNode root)
      throws IOException, DOMException, SQLException, WebODEException
  {
    Element termName, termNode;

    ConstantDescriptor[] ard=null;
    Term[] constantsTerms=odeService.getTerms (ontology, new int[] { TermTypes.CONSTANT});
    if(constantsTerms!=null) {
      ard=new ConstantDescriptor[constantsTerms.length];
      for(int i=0; i<ard.length; i++)
        ard[i]=odeService.getConstant(ontology,constantsTerms[i].term);
    }


    //ard = ConstantDescriptor.getConstants (con, ontology);

    if (ard != null) {
      root.appendChild (doc.createTextNode ("\n"));
      root.appendChild (doc.createComment ("***********************************************\n" +
          "                    Constants\n" +
          "***********************************************"));

      for (int i = 0; i < ard.length; i++) {
        root.appendChild (termNode = doc.createElement (CONSTANT));
        termNode.appendChild (termName = doc.createElement (NAME));
        termName.appendChild (doc.createTextNode (ard[i].name));
        if (ard[i].description != null) {
          termNode.appendChild (termName = doc.createElement (DESCRIPTION));
          termName.appendChild (doc.createTextNode (ard[i].description));
        }
        termNode.appendChild (termName = doc.createElement (TYPE));
        termName.appendChild (doc.createTextNode (ValueTypes.NAMES[ard[i].valueType - 1]));
        termNode.appendChild (termName = doc.createElement (VALUE));
        termName.appendChild (doc.createTextNode (ard[i].value));
        termNode.appendChild (termName = doc.createElement (MEASUREMENT_UNIT));
        termName.appendChild (doc.createTextNode (ard[i].measurementUnit));
      }
    }
  }

  private void _addViews (ODEService odeService, String ontology,
                          XmlDocument doc, ElementNode root,
                          String[] views)
      throws IOException, DOMException, SQLException, WebODEException
  {
    Element termName, t1, termNode, instance, t2, tk;

    if (views != null) {
      root.appendChild (doc.createTextNode ("\n"));
      root.appendChild (doc.createComment ("***********************************************\n" +
          "                    Views\n" +
          "***********************************************"));
      root.appendChild (termNode = doc.createElement (VIEWS));

      for (int i = 0; i < views.length; i++) {
        termNode.appendChild (tk = doc.createElement (VIEW));
        tk.appendChild (termName = doc.createElement (NAME));
        termName.appendChild (doc.createTextNode (views [i]));

        // Retrieve instance set information
        TermPositionDescriptor[] atp = odeService.getTermPositions (ontology, views[i]);
        // TermPositionDescriptor[] atp = TermPositionDescriptor.getTermPositions (ode, ontology, views[i]);
        TermRelationPositionDescriptor[] atrp = odeService.getTermRelationPositions (ontology, views[i]);

//        TermRelationPositionDescriptor[] atrp = TermRelationPositionDescriptor.getTermRelationPositions (ode, ontology, views[i]);

        if (atp != null) {
          for (int j = 0; j < atp.length; j++) {
            tk.appendChild (t1 = doc.createElement (VIEW_ELEMENT));
            t1.appendChild (t2 = doc.createElement (NAME));
            t2.appendChild (doc.createTextNode (atp[j].name));
            t1.appendChild (t2 = doc.createElement (X));
            t2.appendChild (doc.createTextNode ("" + atp[j].x));
            t1.appendChild (t2 = doc.createElement (Y));
            t2.appendChild (doc.createTextNode ("" + atp[j].y));
          }
        }
        if (atrp != null) {
          for (int j = 0; j < atrp.length; j++) {
            tk.appendChild (t1 = doc.createElement (VIEW_RELATION));
            t1.appendChild (t2 = doc.createElement (NAME));
            t2.appendChild (doc.createTextNode (atrp[j].name));
            t1.appendChild (t2 = doc.createElement (ORIGIN));
            t2.appendChild (doc.createTextNode (atrp[j].origin));
            t1.appendChild (t2 = doc.createElement (DESTINATION));
            t2.appendChild (doc.createTextNode (atrp[j].destination));
            t1.appendChild (t2 = doc.createElement (X));
            t2.appendChild (doc.createTextNode ("" + atrp[j].x));
            t1.appendChild (t2 = doc.createElement (Y));
            t2.appendChild (doc.createTextNode ("" + atrp[j].y));
          }
        }
      }
    }
  }

  // Back-up stuff -------------------------------------------
  public boolean hasNext () throws BackupException, RemoteException
  {
    context.logDebug ("hasNext");
    ODEService odeService=null;
    try {
      odeService=(ODEService)this.context.getService(((ExportServiceConfiguration) config).odeService);

      if (bFinished) {
        aod = odeService.getAvailableOntologies ();
        //aod = OntologyDescriptor.getAvailableOntologies (ode);
        if (aod == null || aod.length == 0)
          return false;
        else {
          bFinished = false;
          index = 0;
          return true;
        }
      }
      if (++index < aod.length)
        return true;
      else {
        bFinished = true;
        return false;
      }
    }
    catch (Exception e) {
      throw new BackupException (e.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }

  public String getName ()
  {
    context.logDebug ("getName: " + aod[index].name + ".xml");
    return aod[index].name + ".xml";
  }

  public Object getSource () throws BackupException, RemoteException
  {
    context.logDebug ("getSource (beginning)");
    ODEService odeService=null;

    try {
      odeService=(ODEService)this.context.getService(((ExportServiceConfiguration) config).odeService);
      context.logDebug ("retrieving instance sets and views for ontology " + aod[index].name + "...");

      Term[] insset = odeService.getTerms (aod[index].name, new int [] { Term.INSTANCE_SET });
      Term[] views  = odeService.getTerms (aod[index].name, new int [] { Term.VIEW });
      context.logDebug ("done!");

      context.logDebug ("exporting " + aod[index].name + "...");
      StringBuffer strB = export (aod[index].name, true, _toStringA(insset), _toStringA(views), "dtd");
      context.logDebug (aod[index].name + " exported.");
      return strB.toString();
    }
    catch (Exception e) {
      throw new BackupException (e.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }

  private String[] _toStringA (Term[] at)
  {
    if (at == null)
      return null;
    String[] as = new String[at.length];
    for (int i = 0; i < at.length; i++)
      as[i] = at[i].term;
    return as;
  }
}

