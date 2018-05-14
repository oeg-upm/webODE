package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class Differences </p>
 * <p>Description: This class is in charge of storing the differences between two ontologies </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.util.*;

public class Differences
{

  ArrayList ontologyBase, ontology; //Name; ¿Version?
  ArrayList conceptsWithDifferences;
  HashMap differencesInfo;

  /**
   * This constructor initializes an instance of Differences
   * @param ontoBase Ontology base name
   * @param onto Ontology name
   */
  public Differences (String ontoBase, String onto)
  {
    ontologyBase = new ArrayList();
    ontology = new ArrayList();
    conceptsWithDifferences = new ArrayList();
    differencesInfo = new HashMap();

    int i = 0;

    /* The structure, that stores the differences between 'ontoBase' and 'onto',
       is initialized */
    ontologyBase.add(ontoBase);
    ontology.add(onto);

    while (i<(DifferencesTypes.DIFF_TYPES.length))
    {
      differencesInfo.put (DifferencesTypes.DIFF_TYPES[i], null);
      i++;
    }
  }

  /**
   * This method insert the concept name, if this does not exist in 'conceptWithDifferences'
   * @param concept The concept name to be inserted in 'conceptWithDifferences'
   */
  public void insertConceptWithDifferences (String concept)
  {
    if (!conceptsWithDifferences.contains(concept))
      conceptsWithDifferences.add (concept);
  }

  /**
   * This method inserts simple differences in an instance of Differences
   * @param diffType Type of inserted differences
   * @param diff List of differences to be inserted
   */
  public void insertSimpleDifference (Integer diffType, ArrayList diff)
  {
    differencesInfo.put (diffType, diff);
  }

  /**
   * This method obtains a list of simple differences of a given type
   * @param diffType Type of differences to obtain
   * @return A list of differences of a give type
   */
  public ArrayList getSimpleDifference (Integer diffType)
  {
    ArrayList diff;

    diff = (ArrayList) differencesInfo.get(diffType);

    if (diff==null)
      diff= new ArrayList();

    return diff;
  }

  /**
   * This method inserts differences in an instance of Differences
   * @param diffType Type of inserted differences
   * @param diff List of differences to be inserted
   */
  public void insertDifference (Integer diffType, String concept, ArrayList diff)
  {
    HashMap diffMap;
    if (differencesInfo.get (diffType) == null)
      diffMap = new HashMap();
    else
      diffMap = (HashMap)differencesInfo.get (diffType);

    diffMap.put (concept, diff);
    differencesInfo.put (diffType, diffMap);
  }

  /**
   * This method obtains a list of differences of a given type
   * @param diffType Type of differences to obtain
   * @return A list of differences of a give type
   */
  public ArrayList getDifference (Integer diffType, String concept)
  {
    ArrayList diff;
    HashMap diffMap;

    diffMap = (HashMap) differencesInfo.get(diffType);

    if (diffMap != null)
    {
      if (diffMap.containsKey(concept))
        diff = (ArrayList) diffMap.get(concept);
      else
        //if (diff==null)
        diff = new ArrayList();
    }
    else
      diff = new ArrayList();

    return diff;
  }

  /**
   *
   * @return
   */
  public boolean existDifferences ()
  {
    boolean exist = false;
    int i = 0;

    ArrayList diff = null;

    while ((i<(DifferencesTypes.DIFF_TYPES.length)) && (!exist))
    {
      diff = (ArrayList)differencesInfo.get(DifferencesTypes.DIFF_TYPES[i]);
      exist = (diff != null);
      i++;
    }

    return exist;
  }
}