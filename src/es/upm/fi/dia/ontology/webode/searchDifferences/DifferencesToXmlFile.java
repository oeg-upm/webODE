package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class DifferencesToXmlFile </p>
 * <p>Description: This class converts an instance of Differences in a XML file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.output.*;

public class DifferencesToXmlFile
{
  private static java.text.SimpleDateFormat SDF=new java.text.SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
  Writer out;

  public DifferencesToXmlFile (String xmlFilePath) throws IOException
  {
    this.out=new FileWriter(xmlFilePath);
  }

  public DifferencesToXmlFile (Writer output)
  {
    this.out=output;
  }

  /**
   * This method converts an instance of differences in a XML file
   * @param diffs A instance of class Differences
   */
  public void convert (Differences diffs)
  {
    Element root, element;
    Document doc;

    XMLOutputter output = new XMLOutputter ();
    output.setIndent("  ");
    output.setNewlines(true);

    try
    {
      root = new Element ("message");

      element = new Element ("ontologyBase");
      element.setText ((String)diffs.ontologyBase.get(0));

      ArrayList childrenList = new ArrayList ();
      childrenList.add(element);

      element = new Element ("ontologyBaseVersion");
      childrenList.add(element);

      element = new Element ("ontology");
      element.setText ((String)diffs.ontology.get(0));
      childrenList.add(element);

      element = new Element ("ontologyVersion");
      childrenList.add(element);

      element = new Element ("from");
      childrenList.add(element);

      element = new Element ("to");
      childrenList.add(element);

      element = new Element ("date");
      Date date = new Date ();
      element.setText (SDF.format(new Date(date.getTime())));
      childrenList.add(element);

      element = new Element ("type");
      childrenList.add(element);

      ArrayList diffsList = new ArrayList ();

      // If there are added concepts
      diffsList = diffs.getSimpleDifference(DifferencesTypes.ADDED_CONCEPTS);
      if (!diffsList.isEmpty())
        addSimpleChildrenList("addConcept", "concept", diffsList, childrenList);

      // If there are removed concepts
      diffsList = diffs.getSimpleDifference(DifferencesTypes.REMOVED_CONCEPTS);
      if (!diffsList.isEmpty())
        addSimpleChildrenList("removeConcept", "concept", diffsList, childrenList);

      //generacion de xml cuando estamos dentro de un concepto
      int i = 0;

      while (i<diffs.conceptsWithDifferences.size()) //mientras hay conceptos con diferencias
      {
        String concept;
        concept = (String) diffs.conceptsWithDifferences.get(i); //concepto en el que hay diff
        Element conceptElement = new Element ("concept");
        Element nameElement = new Element ("name");
        nameElement.setText(concept);
        conceptElement.addContent (nameElement);

        // ADDConcepts y REMConcepts ya se han tratado antes...
        int j = 2;
        ArrayList diffList;
        ArrayList diffChildrenList = new ArrayList();
        while (j<DifferencesTypes.DIFF_TYPES.length)
        {
          diffList = diffs.getDifference (DifferencesTypes.DIFF_TYPES[j], concept);

          if (!diffList.isEmpty())
          {
            Element parent1 = new Element ("element");
            ArrayList elementDataList = new ArrayList ();

            createChildrenList(DifferencesTypes.DIFF_TYPES[j], diffList, conceptElement);
          }
          j++;
        }
        childrenList.add(conceptElement);
        i++;
      }
      root.setContent (childrenList);
      doc = new Document (root);
      output.output(doc, out);
    }
    catch (IOException ie)
    {
      ie.printStackTrace();
    }
  }

  /**
   * This method adds a simple children list into the XML structure
   * @param elementName Element name of the children list
   * @param parentElementName Parent of the element name
   * @param diffsList List of differences to add
   * @param childrenList Children list to add in the XML structure
   */
  private void addSimpleChildrenList (String elementName, String parentElementName, ArrayList diffsList, ArrayList childrenList)
  {
    int i = 0;
    Element elementParent, elementChild;

    while (i<diffsList.size())
    {
      elementParent = new Element (parentElementName);
      elementChild = new Element (elementName);
      elementChild.setText((String)diffsList.get(i).toString());
      elementParent.addContent(elementChild);
      childrenList.add(elementParent);
      i++;
    }
  }

  /**
   *
   * @param listType
   * @param diffList
   * @param parent
   * @param childrenList
   */
  private void createChildrenList (Integer listType, ArrayList diffList, Element parent)// ArrayList childrenList)
  {
    Element parent1 = null;
    Element parent2 = null;

    if (listType == DifferencesTypes.ADDED_SC_RELATIONS)
    {
      int i = 0;
      ArrayList elementList = new ArrayList();
      elementList.add ("origin");
      elementList.add ("destination");
      while (i<diffList.size())
      {
        ArrayList elementData = new ArrayList ();
        ArrayList list = new ArrayList();

        parent1 = new Element ("add");
        list = (ArrayList)diffList.get(i);
        parent2 = new Element ("taxRelations");

        int j = 0;
        while (j<elementList.size())
        {
          Element element = new Element ((String)elementList.get(j));
          element.setText ((String) list.get(j));

          elementData.add (element);

          j++;
        }
        parent2.setContent(elementData);
        parent1.addContent(parent2);

        parent.addContent(parent1);

        i++;
      }
    }

    else if (listType == DifferencesTypes.REMOVED_SC_RELATIONS)
    {
      int i = 0;
      ArrayList elementList = new ArrayList();
      elementList.add ("origin");
      elementList.add ("destination");
      while (i<diffList.size())
      {
        ArrayList elementData = new ArrayList ();
        ArrayList list = new ArrayList();

        parent1 = new Element ("remove");
        list = (ArrayList)diffList.get(i);
        parent2 = new Element ("taxRelations");

        int j = 0;
        while (j<elementList.size())
        {
          Element element = new Element ((String)elementList.get(j));
          element.setText ((String) list.get(j));

          elementData.add (element);

          j++;
        }
        parent2.setContent(elementData);
        parent1.addContent(parent2);

        parent.addContent(parent1);

        i++;
      }
    }

    /* Future Work: ADDED_ATTRIBUTES, etc.
    else if.....
    */
  }
}