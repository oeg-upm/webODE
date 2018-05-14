package es.upm.fi.dia.ontology.webode.xml;

import com.sun.xml.tree.XmlDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.*;

// Minerva Stuff
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.MinervaException;

// Webode stuff
import es.upm.fi.dia.ontology.webode.service.*;

import java.rmi.*;
import java.io.*;
import java.util.*;
import java.sql.*;

/**
 * The import service.
 * <p>
 * It takes an XML file fitting WebODE's DTD and loads it into
 * the database. There is still a problem: namespace is not loaded!!
 *
 * @author  Julio César Arpírez Vega.
 * @author  Rafael González Cabero.
 * @version 1.6
 */
public class ImportServiceImp extends MinervaServiceImp
    implements ImportService, XMLConstants
{
  // The parsed XML document.
  private XmlDocument doc;

  // The ontology where everything will be imported to.
  private String ontologyName;

  // Life-cycle methods --------------------------------------------------------------------------
  public void start () throws CannotStartException {
    String odeService = ((ImportServiceConfiguration) config).odeService;
    if (odeService == null) throw new CannotStartException ("No ODE service specified");
  }

  public ImportServiceImp() throws RemoteException {
  }

  /**
   * Converts the string into a valid XML document, checking it
   * against WebODE's DTD.
   *
   * @param strBuffer The string buffer holding the document as
   *        plain text.
   * @exception XMLImportException If the document cannot be parsed
   *            sucessfully.
   */
  public void loadInMemory (StringBuffer strBuffer) throws XMLImportException {
    //context.logInfo ("Request to load an XML file in memory.");
    InputSource	input;

    context.logInfo ("Request to parse ontology.");
    try {
     /*PrintWriter pw = new PrintWriter (new FileWriter ("d:\\foo.xml"));
       pw.print (strBuffer.toString());
       pw.flush();
       pw.close();*/
      // turn the filename into an input source
     /*input = Resolver.createInputSource (MIME_TYPE,
       new FileInputStream ("d:\\foo.xml"),
       true,
       "http");*/
      //System.out.println ("--" + strBuffer.toString() + "--");
      input = new InputSource (new StringReader (strBuffer.toString()));
      // turn it into an in-memory object
      // VALIDATE
      doc = XmlDocument.createXmlDocument (input, true);

      // normalize text representation
      //doc.getDocumentElement ().normalize ();

      // Check for the DTD to see whether it is correct.
      _checkDTD (doc);
    } catch (SAXParseException err) {
      throw new XMLImportException ("Parsing error at "
                                    + " line " + err.getLineNumber ()
                                    + " and URI " + err.getSystemId () + "." +
                                    "\nReason: " + err.getMessage(), err);

    } catch (SAXException e) {
      Exception	x = e.getException ();

      ((x == null) ? e : x).printStackTrace ();

      throw new XMLImportException ("SAX error: " + (x == null ? e : x), e);
    } catch (java.net.UnknownHostException uhe) {
      throw new XMLImportException ("DTD cannot be retrieved from host " + uhe.getMessage() + ".", uhe);
    } catch (Throwable t) {
      throw new XMLImportException ("unexpected error: " + t, t);
    }

    // Log
    context.logInfo ("File parsed sucessfully.");
  }

  private void _checkDTD (XmlDocument doc)
      throws XMLImportException
  {
    DocumentType dtd = doc.getDoctype();

    String name = dtd.getName();
    if (name == null || !name.equals (ROOT))
      throw new XMLImportException ("XML file not of correct type.");
  }

  public boolean isConceptualization ()
  {
    if (doc == null) return false;

    NodeList nl = doc.getDocumentElement().getElementsByTagName (CONCEPTUALIZATION);
    return nl != null && nl.getLength() == 1;
  }

  public String[][] getInstanceSets()
  {
    if (doc == null) return null;

    NodeList nl = doc.getDocumentElement().getElementsByTagName (INSTANCES);
    if (nl == null || nl.getLength() == 0)
      return null;

    NodeList nl2 = ((Element) nl.item (0)).getElementsByTagName (INSTANCE_SET);
    if (nl2 == null || nl2.getLength() == 0)
      return null;

    String[][] astr = new String[nl2.getLength()][2];

    for (int i = 0; i < nl2.getLength(); i++) {
      astr[i][0] = ((Element) nl2.item(i)).getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();
      NodeList nlfoo = ((Element) nl2.item(i)).getElementsByTagName(DESCRIPTION);
      if (nlfoo != null && nlfoo.getLength() > 0)
        astr[i][1] = nlfoo.item(0).getFirstChild().getNodeValue();
    }

    return astr;
  }

  /**
   * Returns all the views.
   */
  public String[] getViews() throws RemoteException
  {
    if (doc == null) return null;

    NodeList nl = doc.getDocumentElement().getElementsByTagName (VIEWS);
    if (nl == null || nl.getLength() == 0)
      return null;

    NodeList nl2 = ((Element) nl.item (0)).getElementsByTagName (VIEW);
    if (nl2 == null || nl2.getLength() == 0)
      return null;

    String[]astr = new String[nl2.getLength()];
    for (int i = 0; i < nl2.getLength(); i++) {
      astr[i] = ((Element) nl2.item(i)).getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();
    }

    return astr;


  }


  /**
   * Gets the ontology name.
   *
   * @return The ontology name.
   */
  public String getOntologyName ()
  {
    if (doc == null) return null;

    return doc.getDocumentElement().getElementsByTagName (NAME).item(0).getFirstChild().getNodeValue();
  }

  /**
   * Gets the ontology description.
   *
   * @return The ontolgy description or null if no one is present.
   */
  public String getOntologyDescription()
  {
    if (doc == null) return null;

    NodeList nl = doc.getDocumentElement().getElementsByTagName (DESCRIPTION);

    if (nl == null || nl.getLength() == 0 ||
        nl.item(0).getParentNode() != doc.getDocumentElement().getElementsByTagName (ROOT).item(0))
      return null;

    return (nl.item (0)).getFirstChild().getNodeValue();
  }

  /**
   * Gets the ontology namespace
   * @return The ontology namespace or null if no ine is present.
   */
  public String getOntologyNamespace() throws RemoteException {
    if (doc == null) return null;

    NodeList nl = doc.getDocumentElement().getElementsByTagName (ONT_NAMESPACE);

    if (nl == null || nl.getLength() == 0 ||
        nl.item(0).getParentNode() != doc.getDocumentElement().getElementsByTagName (ROOT).item(0))
      return null;

    return (nl.item (0)).getFirstChild().getNodeValue();
  }

  /**
   * Sets the name of the ontology where everything will be imported.
   *
   * @param name The name of the ontology.
   */
  public void setOntologyName (String name) throws RemoteException
  {
    this.ontologyName = name;
  }

  /**
   * Imports the references.
   */
  public void importReferences () throws RemoteException, XMLImportException, WebODEException {
    ODEService odeService=null;
    context.logInfo ("Request to import references.");

    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);

      _checkPre();

      // Related references in a hash table
      HashMap hashRefs = new HashMap();
      NodeList nlORefs =  doc.getDocumentElement().getElementsByTagName (RELATED_REFERENCE);
      for (int i = 0; i < nlORefs.getLength(); i++)
        hashRefs.put (nlORefs.item (i).getFirstChild().getNodeValue(), null);


      // First, retrieve references from the tree.
      NodeList nl = ((Element) doc.getDocumentElement().
                     getElementsByTagName (CONCEPTUALIZATION).item(0)).getElementsByTagName (REFERENCE);

      // For each reference, retrieve name and description
      for (int i = 0, l = nl.getLength(); i < l; i++) {
        Element el = (Element) nl.item (i);

        String name =  el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();
        NodeList nlAux = el.getElementsByTagName(DESCRIPTION);
        String description = nlAux == null || nlAux.getLength() == 0 ?
                             null :  nlAux.item(0).getFirstChild().getNodeValue();

        // Effectively commit to database
        ReferenceDescriptor rd = new ReferenceDescriptor (name, description, ontologyName);
        if (hashRefs.containsKey (name))
          odeService.addReferenceToOntology (ontologyName,rd);
        else
          odeService.addReference(ontologyName,rd);
        // rd.store (con, hashRefs.containsKey (name));
      }
    } catch (Exception ex) {
      throw new XMLImportException ("Import error: " + ex.getMessage(), ex);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
    context.logInfo ("References imported successfully");
  }

  /**
   * Imports the formulas.
   */
  public void importFormulas () throws RemoteException, XMLImportException, WebODEException
  {
    ODEService odeService=null;
    context.logInfo ("Request to import formulas.");

    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);

      _checkPre();
      // First, retrieve references from the tree.
      NodeList nl = ((Element) doc.getDocumentElement().
                     getElementsByTagName (CONCEPTUALIZATION).item(0)).getElementsByTagName (FORMULA);

      // For each reference, retrieve name and description
      for (int i = 0, l = nl.getLength(); i < l; i++) {
        Element el = (Element) nl.item (i);

        String name =  el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

        NodeList nlAux = el.getElementsByTagName(DESCRIPTION);
        String description = nlAux == null || nlAux.getLength() == 0 ?
                             null :  nlAux.item(0).getFirstChild().getNodeValue();

        String expression =  el.getElementsByTagName(EXPRESSION).item(0).getFirstChild().getNodeValue();
        int type =  FormulaDescriptor.getNumericType
        (el.getElementsByTagName (TYPE).item(0).getFirstChild().getNodeValue());

        // Effectively commit to database
        FormulaDescriptor fd = new FormulaDescriptor (ontologyName, name, description, expression, type);
        odeService.insertReasoningElement (fd);
        //  fd.store (con);
      }
    } catch (Exception ex) {
      throw new XMLImportException ("Import error: " + ex.getMessage(), ex);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }

    context.logInfo ("Formulas imported successfully.");
  }


  private void _checkPre () throws XMLImportException
  {
    if (ontologyName == null)
      throw new XMLImportException ("The ontology name must be set first.");
    if (doc == null)
      throw new XMLImportException ("The XML document must be loaded first.");
    if (doc.getDocumentElement().getElementsByTagName (CONCEPTUALIZATION) == null)
      throw new XMLImportException ("No conceptualization available.");
  }

  /**
   * Imports the imported terms.
   */
  public void importImportedTerms () throws RemoteException, XMLImportException, WebODEException
  {
    ODEService odeService=null;
    context.logInfo ("Request to import imported terms.");

    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);
      _checkPre();

      // First, retrieve references from the tree.
      NodeList nl = ((Element) doc.getDocumentElement().
                     getElementsByTagName (CONCEPTUALIZATION).item(0)).getElementsByTagName (IMPORTED_TERM);

      // For each reference, retrieve name and description
      for (int i = 0, l = nl.getLength(); i < l; i++) {
        Element el = (Element) nl.item (i);
        //Imported term--> modified
        String name = el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();
        String uri =  el.getElementsByTagName(URI).item(0).getFirstChild().getNodeValue();
        String namespace =  el.getElementsByTagName(NAMESPACE).item(0).getFirstChild().getNodeValue();
        String namespace_identifier =  el.getElementsByTagName(NAMESPACE_IDENTIFIER).item(0).getFirstChild().getNodeValue();
        /*String originalName =  el.getElementsByTagName(ORIGINAL_NAME).item(0).getFirstChild().getNodeValue();
        String name =  el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();
        String url =  el.getElementsByTagName(URL).item(0).getFirstChild().getNodeValue();

        MinervaURL murl = new MinervaURL (url, "webode");
 */

//        ImportedTerm importedTerm =  new ImportedTerm (namespace, namespace_identifier, null, uri);
        odeService.importTerm (ontologyName, namespace, namespace_identifier, name, uri);

//        new ImportedTerm (namespace, namespace_identifier, null, uri).store (ode, ontologyName);
      }
    } catch (Exception ex) {
      throw new XMLImportException ("Import error: " + ex.getMessage(), ex);
      //} catch (java.net.MalformedURLException mfue) {
      //  throw new XMLImportException (mfue.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
    context.logInfo ("Imported terms imported successfully.");
  }

  /**
   * Imports the properties.
   */
  public void importProperties () throws RemoteException, XMLImportException, WebODEException
  {
    ODEService odeService=null;
    context.logInfo ("Request to import properties.");
    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);
      _checkPre();

      // Get a connection


      // First, retrieve references from the tree.
      NodeList nl = ((Element) doc.getDocumentElement().
                     getElementsByTagName (CONCEPTUALIZATION).item(0)).getElementsByTagName (PROPERTY);

      // For each reference, retrieve name and description
      for (int i = 0, l = nl.getLength(); i < l; i++) {
        Element el = (Element) nl.item (i);

        String name =  el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

        NodeList nlAux = el.getElementsByTagName(DESCRIPTION);
        String description = nlAux == null || nlAux.getLength() == 0 ?
                             null :  nlAux.item(0).getFirstChild().getNodeValue();

        NodeList refs =  el.getElementsByTagName(RELATED_REFERENCE);
        NodeList fors =  el.getElementsByTagName(RELATED_FORMULA);

        odeService.insertTerm (ontologyName, name, description, TermTypes.PROPERTY);
//        new Term (ontologyName, name, description, TermTypes.PROPERTY).store (ode);

        for (int k = 0, l1 = refs.getLength(); k < l1; k++) {
          String refName = refs.item(k).getFirstChild().getNodeValue();
          ReferenceDescriptor ref = new ReferenceDescriptor(name, description, ontologyName);
          odeService.addReference (ontologyName,ref);
          //odeService.relateReferenceToTerm (ontologyName, refName, name);
        }

        for (int k = 0, l1 = fors.getLength(); k < l1; k++) {
          String forName = fors.item(k).getFirstChild().getNodeValue();
          odeService.relateFormulaToTerm (ontologyName, forName, name);
//          FormulaDescriptor.relateFormulaToTerm (ode, ontologyName, forName, name);
        }
      }
    }
    catch (RemoteException remex) {
      throw new XMLImportException ("Remote error: " + remex.getMessage(), remex);
    }
    catch(MinervaException me) {
      throw new XMLImportException("Can't get ODE service : " + me.getMessage(), me);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }

    context.logInfo ("Properties imported successfully.");
  }

  /**
   * Imports the user types
   */
  public void importTypes() throws RemoteException, XMLImportException, WebODEException {
    ODEService odeService=null;
    context.logInfo ("Request to import user tpyes.");

    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);
      _checkPre();

      NodeList types = ((Element) doc.getDocumentElement().getElementsByTagName (CONCEPTUALIZATION).item(0)).getElementsByTagName (TYPES);
      if(types!=null && types.getLength()>0) {
        NodeList enums = ((Element)types.item(0)).getElementsByTagName(ENUMERATED_TYPE);
        if(enums!=null) {
          Element enum;
          String name, description;
          LinkedHashSet values;
          NodeList vals;
          for(int i=0; i<enums.getLength(); i++) {
            enum=(Element)enums.item(i);
            name =  enum.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

            NodeList nlAux = enum.getElementsByTagName(DESCRIPTION);
            description = (nlAux == null || nlAux.getLength() == 0) ?
                          null :  nlAux.item(0).getFirstChild().getNodeValue();

            vals =  enum.getElementsByTagName(VALUE);
            values=new LinkedHashSet();
            for (int j=0; j<vals.getLength(); j++)
              values.add(vals.item(j).getFirstChild().getNodeValue());

            odeService.insertEnumeratedType(ontologyName, name, description, (values.size()>0)?(String[])values.toArray(new String[0]):null);
          }
        }
      }
    }
    catch(MinervaException me) {
      throw new XMLImportException("Can't get ODE service : " + me.getMessage(), me);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
    context.logInfo ("User types imported successfully.");
  }

  /**
   * Imports the concepts.
   */
  public void importConcepts () throws RemoteException, XMLImportException, WebODEException
  {
    ODEService odeService=null;
    context.logInfo ("Request to import concepts.");

    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);
      _checkPre();

      // Get a connection


      // First, retrieve references from the tree.
      NodeList nl = ((Element) doc.getDocumentElement().
                     getElementsByTagName (CONCEPTUALIZATION).item(0)).getElementsByTagName (TERM);

      // For each reference, retrieve name and description
      for (int i = 0, l = nl.getLength(); i < l; i++) {
        Element el = (Element) nl.item (i);

        //System.out.println ("#-6");
        String name =  el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

        //System.out.println ("#-5 " + name);
        NodeList nlAux = el.getElementsByTagName(DESCRIPTION);
        String description = nlAux == null || nlAux.getLength() == 0 ?
                             null :  nlAux.item(0).getFirstChild().getNodeValue();
        //System.out.println ("#-4");
        NodeList refs  =  el.getElementsByTagName(RELATED_REFERENCE);
        NodeList fors  =  el.getElementsByTagName(RELATED_FORMULA);
        NodeList syns  =  el.getElementsByTagName(SYNONYM);
        NodeList abbrs =  el.getElementsByTagName(ABBREVIATION);
        //System.out.println ("#-3");
        // Insert the concept if it is not an imported term
        if (!odeService.isImportedTerm (ontologyName, name))
          // if (!ImportedTerm.isImportedTerm (con, ontologyName, name))

          odeService.insertTerm (ontologyName, name, description, TermTypes.CONCEPT);

//          new Term (ontologyName, name, description, TermTypes.CONCEPT).store (ode);

        // References
        for (int k = 0, l1 = refs.getLength(); k < l1; k++) {
          String refName = refs.item(k).getFirstChild().getNodeValue();
          if (refs.item(k).getParentNode() == el) {
            //System.out.println ("REF: " + refName + ".  " + name);
            odeService.relateReferenceToTerm (ontologyName, name, refName);
          }
        }
        //System.out.println ("#-1");
        // Formulas
        for (int k = 0, l1 = fors.getLength(); k < l1; k++) {
          if (fors.item(k).getParentNode() == el) {
            String forName = fors.item(k).getFirstChild().getNodeValue();
            //System.out.println ("O: " + ontologyName + ", " + forName + ", name: " + name);

            odeService.relateFormulaToTerm (ontologyName, forName, name);
            //FormulaDescriptor.relateFormulaToTerm (ode, ontologyName, forName, name);
          }
        }

        //System.out.println ("#0");
        // Synonyms
        for (int k = 0, l1 = syns.getLength(); k < l1; k++) {
          if (syns.item(k).getParentNode() == el) {
            String synName = ((Element) syns.item(k)).
                     getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();


            nlAux = ((Element) syns.item(k)).getElementsByTagName(DESCRIPTION);
            String synDescription = nlAux == null || nlAux.getLength() == 0 ?
                                    null :  nlAux.item(0).getFirstChild().getNodeValue();

            //System.out.println ("N: " + synName + ", " + synDescription);

            SynonymAbbreviation sa = new SynonymAbbreviation (synName, name, null, TermTypes.SYNONYM, synDescription);
            odeService.addSynonymToTerm (ontologyName, sa);

//            new SynonymAbbreviation (synName, name, null, TermTypes.SYNONYM, synDescription)
//                .store(con, ontologyName);
          }
        }
        //System.out.println ("#1");

        // Abbreviations
        for (int k = 0, l1 = abbrs.getLength(); k < l1; k++) {
          if (abbrs.item(k).getParentNode() == el) {
            String abbrName = ((Element) abbrs.item(k)).
                     getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

            nlAux = ((Element) abbrs.item(k)).getElementsByTagName(DESCRIPTION);
            String abbrDescription = nlAux == null || nlAux.getLength() == 0 ?
                                     null :  nlAux.item(0).getFirstChild().getNodeValue();

            SynonymAbbreviation sa = new SynonymAbbreviation (abbrName, name, null, TermTypes.SYNONYM, abbrDescription);
            odeService.addSynonymToTerm (ontologyName, sa);


//            new SynonymAbbreviation (abbrName, name, null, TermTypes.ABBREVIATION, abbrDescription)
//                .store(ode, ontologyName);
          }
        }
        //System.out.println ("#2");

        // Class attributes
        NodeList classAttrs = el.getElementsByTagName(CLASS_ATTRIBUTE);
        _importAttribute (odeService, name, classAttrs, false);
        //System.out.println ("#3");

        // Instance attributes
        NodeList instanceAttrs = el.getElementsByTagName(INSTANCE_ATTRIBUTE);
        _importAttribute (odeService, name, instanceAttrs, true);
        //System.out.println ("#4");
      }
    }
    catch(MinervaException me) {
      throw new XMLImportException("Can't get ODE service : " + me.getMessage(), me);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
    context.logInfo ("Concepts imported successfully.");
  }

  private void _importAttribute (ODEService odeService, String termName, NodeList nl, boolean bIns)
      throws WebODEException, XMLImportException, RemoteException
  {
    if (nl != null) {
      for (int i = 0, l = nl.getLength(); i < l; i++) {
        //System.out.println ("*1");
        Element el = (Element) nl.item (i);

        String name =  el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

        //System.out.println ("*2");
        NodeList nlAux = el.getElementsByTagName (DESCRIPTION);
        String description = nlAux == null || nlAux.getLength() == 0 ?
                             null :  nlAux.item(0).getFirstChild().getNodeValue();

        //System.out.println ("*3");
        nlAux = el.getElementsByTagName(MEASUREMENT_UNIT);
        String measurementUnit = nlAux == null || nlAux.getLength() == 0 ?
                                 null :  nlAux.item(0).getFirstChild().getNodeValue();

        //System.out.println ("*4");
        nlAux = el.getElementsByTagName(PRECISION);
        String precision = nlAux == null || nlAux.getLength() == 0 ?
                           null :  nlAux.item(0).getFirstChild().getNodeValue();
        //System.out.println ("*5");
        int minCardinality, maxCardinality;
        try {
          minCardinality = Integer.parseInt
          (el.getElementsByTagName(MIN_CARDINALITY).item(0).getFirstChild().getNodeValue());
          maxCardinality = Integer.parseInt
          (el.getElementsByTagName(MAX_CARDINALITY).item(0).getFirstChild().getNodeValue());

        } catch (Exception e) {
          throw new XMLImportException ("Error in cardinalities: " + e.getMessage(), e);
        }

        //System.out.println ("*6");
        String minValue = null, maxValue = null;
        if (bIns) {
          NodeList nla = el.getElementsByTagName(MIN_VALUE);
          minValue = nla == null || nla.getLength() == 0 ? null : nla.item(0).getFirstChild().getNodeValue();
          nla = el.getElementsByTagName(MAX_VALUE);
          maxValue = nla == null || nla.getLength() == 0 ? null : nla.item(0).getFirstChild().getNodeValue();
        }
        //System.out.println ("*7");
        String stringType = el.getElementsByTagName(TYPE).item(0).getFirstChild().getNodeValue();
        int type =  Term.getValueType (stringType);


        NodeList refs  =  el.getElementsByTagName(RELATED_REFERENCE);
        NodeList fors  =  el.getElementsByTagName(RELATED_FORMULA);
        NodeList syns  =  el.getElementsByTagName(SYNONYM);
        NodeList abbrs =  el.getElementsByTagName(ABBREVIATION);
        //System.out.println ("*8");
        Attribute attr;
        // Insert the concept
        if (bIns) {
          //System.out.println ("1 " + ontologyName);
          attr = new InstanceAttributeDescriptor (name, termName,
              stringType, measurementUnit,
              precision, minCardinality, maxCardinality,
              description, minValue, maxValue);
          odeService.insertInstanceAttribute (ontologyName,(InstanceAttributeDescriptor)attr);

          //((InstanceAttributeDescriptor) attr).store (con, ontologyName);
          //System.out.println ("2");
        }
        else {
          //System.out.println ("1.1 " + ontologyName);
          attr = new ClassAttributeDescriptor (name, termName,
              stringType, measurementUnit,
              precision, minCardinality, maxCardinality, description);
          odeService.insertClassAttribute (ontologyName,(ClassAttributeDescriptor)  attr);
          //((ClassAttributeDescriptor) attr).store (ode, ontologyName);
          //System.out.println ("1.2");
        }
        //System.out.println ("*9");
        // References
        for (int k = 0, l1 = refs.getLength(); k < l1; k++) {
          String refName = refs.item(k).getFirstChild().getNodeValue();
          odeService.relateReferenceToTerm (ontologyName, refName, name,termName);
          //ReferenceDescriptor.relateReferenceToTerm (con, ontologyName, refName, name, termName);
        }
        //System.out.println ("*10");
        // Formulas
        for (int k = 0, l1 = fors.getLength(); k < l1; k++) {
          String forName = fors.item(k).getFirstChild().getNodeValue();
          odeService.relateFormulaToTerm (ontologyName, forName, name, termName);
          //FormulaDescriptor.relateFormulaToTerm (ode, ontologyName, forName, name, termName);
        }
        //System.out.println ("*11");
        // Synonyms
        for (int k = 0, l1 = syns.getLength(); k < l1; k++) {
          String synName = ((Element) syns.item(k)).
                     getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

          nlAux = ((Element) syns.item(k)).getElementsByTagName(DESCRIPTION);
          String synDescription = nlAux == null || nlAux.getLength() == 0 ?
                                  null :  nlAux.item(0).getFirstChild().getNodeValue();
          SynonymAbbreviation abb =
              new SynonymAbbreviation (synName, name, termName, TermTypes.SYNONYM, synDescription);
          odeService.addAbbreviationToTerm(ontologyName,abb);


          // new SynonymAbbreviation (synName, name, termName, TermTypes.SYNONYM, synDescription)
          // .store(ode, ontologyName);

        }
        //System.out.println ("*12");

        // Abbreviations
        for (int k = 0, l1 = abbrs.getLength(); k < l1; k++) {
          String abbrName = ((Element) abbrs.item(k)).
                     getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

          nlAux = ((Element) abbrs.item(k)).getElementsByTagName(DESCRIPTION);
          String abbrDescription = nlAux == null || nlAux.getLength() == 0 ?
                                   null :  nlAux.item(0).getFirstChild().getNodeValue();
          SynonymAbbreviation abb =
              new SynonymAbbreviation (abbrName, name, termName, TermTypes.ABBREVIATION, abbrDescription);
          odeService.addAbbreviationToTerm(ontologyName,abb);
//          new SynonymAbbreviation (abbrName, name, termName, TermTypes.ABBREVIATION, abbrDescription)
//              .store(con, ontologyName);



        }
        //System.out.println ("*13");

        // Inference
        NodeList inferred =  el.getElementsByTagName(INFERRED);
        for (int k = 0, l1 = inferred.getLength(); k < l1; k++) {

          odeService.addAttributeInferenceRelation (ontologyName,termName, name, termName, inferred.item(k).getNodeValue());

//          new AttributeInference (termName, name, termName,
//          inferred.item(k).getNodeValue()).
//           store (ode, ontologyName);

        }
        //System.out.println ("*14");

        // Values
        NodeList values =  el.getElementsByTagName(VALUE);
//        ClassAttributeValue cad = new ClassAttributeValue (attr);
        for (int k = 0, l1 = values.getLength(); k < l1; k++) {
          odeService.addValueToClassAttribute (ontologyName,  attr.name, termName, values.item(k).getFirstChild().getNodeValue());

//          cad.addValue (con, values.item(k).getFirstChild().getNodeValue(), ontologyName);
        }
        //System.out.println ("*15");
      }
    }
  }

  /**
   * Imports a given instance set.
   */
  public void importInstanceSet (String instanceSet, String description)
      throws RemoteException, XMLImportException, WebODEException
  {
    ODEService odeService=null;
    context.logInfo ("Request to import instance set (" + instanceSet + ").");

    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);
      _checkPre();

      // First, retrieve references from the tree.
      NodeList nl = ((Element) doc.getDocumentElement().
                     getElementsByTagName (INSTANCES).item(0)).getElementsByTagName (INSTANCE_SET);


      int looked = -1;
      for (int i = 0; i < nl.getLength(); i++) {
        if (((Element) nl.item(i)).getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue().
            equals (instanceSet)) {
        looked = i;
        break;
      }
      }
      if (looked < 0)
        throw new XMLImportException ("No such instance set (" + instanceSet + ").");

      // Create instance set
      odeService.insertTerm (ontologyName, instanceSet, description, TermTypes.INSTANCE_SET);
//         new Term (ontologyName, instanceSet, desc, TermTypes.INSTANCE_SET).store (ode);
      //new Term (ontologyName, instanceSet, description, TermTypes.INSTANCE_SET).store (ode);

      Element instanceSetn = (Element) nl.item(looked);

      nl = instanceSetn.getElementsByTagName (INSTANCE);
      if (nl != null) {
        for (int i = 0, l = nl.getLength(); i < l; i++) {
          Element aux = (Element) nl.item(i);
          // Name of the instance
          String instanceName =  aux.getElementsByTagName (NAME).item(0).
                                 getFirstChild().getNodeValue();

          // Term of the instance
          String termName = aux.getElementsByTagName (INSTANCE_OF).item(0).
                            getFirstChild().getNodeValue();

          NodeList nlAux = aux.getElementsByTagName(DESCRIPTION);
          description = nlAux == null || nlAux.getLength() == 0 ?
                        null :  nlAux.item(0).getFirstChild().getNodeValue();

          // Insert instance
          Instance instance=new Instance (instanceName, termName, instanceSet, description);
          odeService.insertInstance (ontologyName, instance);
          //new Instance (instanceName, termName, instanceSet, description).store (ode, ontologyName);

          // Now attributes
          nlAux = aux.getElementsByTagName (CLASS);
          for (int k = 0, l1 = nlAux.getLength(); k < l1; k++) {
            Element el = (Element) nlAux.item(k);

            NodeList attrs = el.getElementsByTagName (ATTRIBUTE);
            termName = el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();
            for (int j = 0, l2 = attrs.getLength(); j < l2; j++) {
              // Name of the attribute
              String attrName = ((Element) attrs.item(j)).getElementsByTagName (NAME).item(0).
                         getFirstChild().getNodeValue();
              // Value of the attribute
              String attrValue = ((Element) attrs.item(j)).getElementsByTagName (VALUE).item(0).
                                 getFirstChild().getNodeValue();

              // Insert into database
              odeService.addValueToInstance (ontologyName, instanceSet, instanceName, termName,
                                             attrName, attrValue);

//              Instance.addInstanceValue (ode, ontologyName, instanceSet,
//                  termName, instanceName,
//                  attrName, attrValue);
            }
          }
        }
      }

      // Add relations instances
      nl = instanceSetn.getElementsByTagName (RELATION_INSTANCE);
      if (nl != null) {
        for (int i = 0, l = nl.getLength(); i < l; i++) {
          Element aux = (Element) nl.item(i);
          // Name of the instance
          String instanceName =  aux.getElementsByTagName (NAME).item(0).
                                 getFirstChild().getNodeValue();

          // Term of the instance
          String termName = aux.getElementsByTagName (INSTANCE_OF).item(0).
                            getFirstChild().getNodeValue();

          NodeList nlAux = aux.getElementsByTagName(DESCRIPTION);
          description = nlAux == null || nlAux.getLength() == 0 ?
                        null :  nlAux.item(0).getFirstChild().getNodeValue();

          // Origin and destination
          String origin = aux.getElementsByTagName (ORIGIN).item(0).
                          getFirstChild().getNodeValue();
          String destination = aux.getElementsByTagName (DESTINATION).item(0).
                               getFirstChild().getNodeValue();
          String originConcept = aux.getElementsByTagName (ORIGIN_CONCEPT).item(0).
                                 getFirstChild().getNodeValue();
          String destinationConcept = aux.getElementsByTagName (DESTINATION_CONCEPT).item(0).
                                      getFirstChild().getNodeValue();
          // Store it
          TermRelationInstance termRelInstance=
              new TermRelationInstance (instanceName, new TermRelation(ontologyName,null,termName,originConcept,destinationConcept), instanceSet,
              description, origin, destination);
          odeService.insertRelationInstance (ontologyName, termRelInstance);
        }
      }
    } catch (Exception ex) {
      throw new XMLImportException ("Import error: " + ex.getMessage(), ex);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
    context.logInfo ("Instance set imported successfully.");
  }

  /**
   * Imports the term relationships.
   */
  public void importRelationships () throws RemoteException, XMLImportException, WebODEException
  {
    ODEService odeService=null;
    context.logInfo ("Request to import relationshipds.");

    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);
      _checkPre();

      // Get a connection


      // First, retrieve references from the tree.
      NodeList nl = ((Element) doc.getDocumentElement().
                     getElementsByTagName (CONCEPTUALIZATION).item(0)).getElementsByTagName (TERM_RELATION);

      // For each reference, retrieve name and description
      for (int i = 0, l = nl.getLength(); i < l; i++) {
        Element el = (Element) nl.item (i);

        String name =  el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

  /*		NodeList nlAux = el.getElementsByTagName(DESCRIPTION);
  String description = nlAux == null || nlAux.getLength() == 0 ?
  null :  nlAux.item(0).getFirstChild().getNodeValue();*/

        String originName = el.getElementsByTagName(ORIGIN).item(0).getFirstChild().getNodeValue();
        String destName = el.getElementsByTagName(DESTINATION).item(0).getFirstChild().getNodeValue();
        int maxCardinality;
        try {
          maxCardinality = Integer.parseInt
          (el.getElementsByTagName(MAX_CARDINALITY).item(0).getFirstChild().getNodeValue());
        } catch (Exception e) {
          throw new XMLImportException ("Wrong maximum cardinality in relation " + name + ".", e);
        }

        NodeList props =  el.getElementsByTagName(RELATED_PROPERTY);

        String[] strProps = null;
        if (props.getLength() > 0) {
          strProps = new String[props.getLength()];
          for (int k = 0, l1 = props.getLength(); k < l1; k++) {
            strProps[k] = props.item(k).getFirstChild().getNodeValue();
          }
        }

        // Store

        TermRelation termRelation =
            new TermRelation (ontologyName, null, name, originName, destName, maxCardinality, strProps);
        odeService.insertTermRelation(termRelation);
      }
    } catch (Exception ex) {
      throw new XMLImportException ("Import error: " + ex.getMessage(), ex);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
    context.logInfo ("Relationships imported successfully.");
  }

  /**
   * Imports the groups.
   */
  public void importGroups () throws RemoteException, XMLImportException, WebODEException
  {
    ODEService odeService=null;
    context.logInfo ("Request to import groups.");

    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);
      _checkPre();

      // First, retrieve references from the tree.
      NodeList nl = ((Element) doc.getDocumentElement().
                     getElementsByTagName (CONCEPTUALIZATION).item(0)).getElementsByTagName (GROUP);

      // For each reference, retrieve name and description
      for (int i = 0, l = nl.getLength(); i < l; i++) {
        Element el = (Element) nl.item (i);

        String name =  el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

        NodeList nlAux = el.getElementsByTagName(DESCRIPTION);
        String description = nlAux == null || nlAux.getLength() == 0 ?
                             null :  nlAux.item(0).getFirstChild().getNodeValue();
        NodeList props =  el.getElementsByTagName(RELATED_CONCEPT);
        String[] strProps = null;
        if (props.getLength() > 0) {
          strProps = new String[props.getLength()];
          for (int k = 0, l1 = props.getLength(); k < l1; k++) {
            strProps[k] = props.item(k).getFirstChild().getNodeValue();
          }
        }
        // Insert group
        Group newGroup =new Group (name, description, strProps);
        odeService.addGroup (ontologyName, newGroup);

      }
    } catch (Exception ex) {
      throw new XMLImportException ("Import error: " + ex.getMessage(), ex);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
    context.logInfo ("Groups imported successfully.");
  }

  /**
   * Imports the constants.
   */
  public void importConstants () throws RemoteException, XMLImportException, WebODEException
  {
    ODEService odeService=null;
    context.logInfo ("Request to import constants.");
    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);
      _checkPre();

      // First, retrieve references from the tree.
      NodeList nl = ((Element) doc.getDocumentElement().
                     getElementsByTagName (CONCEPTUALIZATION).item(0)).getElementsByTagName (CONSTANT);

      // For each reference, retrieve name and description
      for (int i = 0, l = nl.getLength(); i < l; i++) {
        Element el = (Element) nl.item (i);

        String name =  el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

        NodeList nlAux = el.getElementsByTagName(DESCRIPTION);
        String description = nlAux == null || nlAux.getLength() == 0 ?
                             null :  nlAux.item(0).getFirstChild().getNodeValue();

        String strType =  el.getElementsByTagName(TYPE).item(0).getFirstChild().getNodeValue();
        int type = Term.getValueType (strType);
        if (type < 0)
          throw new XMLImportException ("No such type (" + strType + ") in constant.");
        String value =  el.getElementsByTagName(VALUE).item(0).getFirstChild().getNodeValue();
        String measurementUnit =  el.getElementsByTagName(MEASUREMENT_UNIT).item(0).getFirstChild().getNodeValue();

        ConstantDescriptor constantDesc = new ConstantDescriptor (name, description, ValueTypes.NAMES[type-1], measurementUnit, value);
        odeService.insertConstant(ontologyName,constantDesc);
      }
    } catch (Exception ex) {
      throw new XMLImportException ("Import Service Error: " + ex.getMessage(), ex);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
    context.logInfo ("Constants imported successfully");
  }

  /**
   * Import view.
   */
  public void importView (String view) throws RemoteException, XMLImportException, WebODEException
  {
    ODEService odeService=null;
    context.logInfo ("Request to import views.");

    try {
      odeService=(ODEService)this.context.getService(((ImportServiceConfiguration)this.config).odeService);
      _checkPre();

      // First, retrieve references from the tree.
      NodeList nl = ((Element) doc.getDocumentElement().
                     getElementsByTagName (VIEWS).item(0)).getElementsByTagName (VIEW);

      int looked = -1;
      for (int i = 0; i < nl.getLength(); i++) {
        if (((Element) nl.item(i)).getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue().
            equals (view)) {
        looked = i;
        break;
      }
      }
      if (looked < 0)
        throw new XMLImportException ("No such view (" + view + ").");

      // Insert view into the database
      odeService.insertTerm (ontologyName, view, "", Term.VIEW);
//      new Term (ontologyName, view, "", Term.VIEW).store (ode);

      // For each reference, retrieve name and description
      Element el = (Element) nl.item (looked);
      String name =  el.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();

     /*NodeList nlAux = el.getElementsByTagName(DESCRIPTION);
       String description = nlAux == null || nlAux.getLength() == 0 ?
       null :  nlAux.item(0).getFirstChild().getNodeValue();*/
      NodeList nl1 = el.getElementsByTagName(VIEW_ELEMENT);

      if (nl1.getLength() > 0) {

        for (int k = 0; k < nl1.getLength(); k++) {
          Element foo = (Element) nl1.item(k);

          String name1 =  foo.getElementsByTagName (NAME).item(0).getFirstChild().getNodeValue();
          int x = Integer.parseInt (foo.getElementsByTagName (X).item(0).getFirstChild().getNodeValue());
          int y = Integer.parseInt (foo.getElementsByTagName (Y).item(0).getFirstChild().getNodeValue());

          // View
          TermPositionDescriptor termPositionDesc = new TermPositionDescriptor (name1, x, y);
          odeService.insertTermPosition (ontologyName, termPositionDesc, view);
        }
      }

      nl1 = el.getElementsByTagName(VIEW_RELATION);
      if (nl1.getLength() > 0) {

        for (int k = 0; k < nl1.getLength(); k++) {
          Element foo = (Element) nl1.item(k);

          String name1 =  foo.getElementsByTagName (NAME).item(0).getFirstChild().getNodeValue();
          String origin =  foo.getElementsByTagName (ORIGIN).item(0).getFirstChild().getNodeValue();
          String destination =  foo.getElementsByTagName (DESTINATION).item(0).getFirstChild().getNodeValue();
          int x = Integer.parseInt (foo.getElementsByTagName (X).item(0).getFirstChild().getNodeValue());
          int y = Integer.parseInt (foo.getElementsByTagName (Y).item(0).getFirstChild().getNodeValue());

          // View


          TermRelationPositionDescriptor termRelPosDesc =
              new TermRelationPositionDescriptor (name1, origin, destination,x, y);


          odeService.insertTermRelationPosition (ontologyName, termRelPosDesc,view);

        }
      }
    }
    /*
    catch (SQLException sqle) {
      throw new XMLImportException ("Database error: " + sqle.getMessage());

    }
   */
    catch (NumberFormatException nfe) {
      throw new XMLImportException ("Error parsing number.", nfe);
    }
    catch(MinervaException me) {
      throw new XMLImportException("Can't get ODE service : " + me.getMessage(), me);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
    context.logInfo ("Views imported successfully");
  }
}
