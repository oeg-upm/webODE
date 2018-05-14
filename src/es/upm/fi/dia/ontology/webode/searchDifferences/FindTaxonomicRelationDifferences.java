package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class FindTaxonomicRelationDifferences </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.util.*;
import java.rmi.RemoteException;

import es.upm.fi.dia.ontology.webode.service.*;

public class FindTaxonomicRelationDifferences extends FindDifferencesInConcepts
{

  public void findDifferences (ODEService odeService, Differences differences, String ontologyNameA, String ontologyNameB, String concept) throws WebODEException
  {
    ArrayList arrayListTaxRelA = new ArrayList();
    ArrayList arrayListTaxRelB = new ArrayList();
    TermRelation termA[];
    TermRelation termB[];

    Utility util = new Utility();

    try
    {
      //Taxonomic relations in ontologyNameA
      termA = odeService.getTermRelations (ontologyNameA, true);
      util.TermRelationToArrayList(termA, arrayListTaxRelA);

      //Taxonomic relations in ontologyNameB
      termB = odeService.getTermRelations (ontologyNameB, true);
      util.TermRelationToArrayList(termB, arrayListTaxRelB);

      //arrayListTaxConceptA: arrayList of taxonomic relations, in ontologyNameA,
      //that contains the concept
      ArrayList arrayListTaxConceptA = new ArrayList();

      // Taxonomic relations of the concept, in ontologyNameA, are obtained
      getConceptTaxonomicRelation (arrayListTaxRelA, concept, arrayListTaxConceptA);

      //arrayListTaxConceptB: arrayList of taxonomic relations, in ontologyNameB,
      //that contains the concept
      ArrayList arrayListTaxConceptB = new ArrayList();

      // Taxonomic relations of the concept, in ontologyNameB, are obtained
      getConceptTaxonomicRelation (arrayListTaxRelB, concept, arrayListTaxConceptB);

      // Compares 'arrayListTaxConceptA' and 'arrayListTaxConceptB'
      CompareByName compare = new CompareByName();
      boolean equals = false;

      equals = compare.operate (arrayListTaxConceptA, arrayListTaxConceptB);

      if (!equals)
      {
        // Added taxonomic relations
        SearchAdded searchAdded = new SearchAdded ();
        ArrayList addedTaxRelations;// = new ArrayList ();
        // Add the added taxonomic relations of the concept
        addedTaxRelations = searchAdded.operate(arrayListTaxConceptA, arrayListTaxConceptB);

        if (!addedTaxRelations.isEmpty())
        {
          differences.insertDifference (DifferencesTypes.ADDED_SC_RELATIONS, concept, addedTaxRelations);
          differences.insertConceptWithDifferences (concept);
          // differences.conceptsWithDifferences.add(concept);
        }

        // Removed taxonomic relations
        SearchRemoved searchRemoved = new SearchRemoved ();
        ArrayList removedTaxRelations; // = new ArrayList ();
        // Add the removed taxonomic relations of the concept
        removedTaxRelations = searchRemoved.operate(arrayListTaxConceptA, arrayListTaxConceptB);

        if (!removedTaxRelations.isEmpty())
        {
          differences.insertDifference (DifferencesTypes.REMOVED_SC_RELATIONS, concept, removedTaxRelations);
          differences.insertConceptWithDifferences (concept);
          //differences.conceptsWithDifferences.add(concept);
        }
      }
    }
    catch (RemoteException re)
    {
      System.out.println("ERROR: RemoteException" + re);
      re.printStackTrace();
    }
  }

  private void getConceptTaxonomicRelation (ArrayList arrayListTaxRelation, String concept, ArrayList arrayListConceptTaxRelation)
  {
    int i = 0;

    while (i<arrayListTaxRelation.size())
    {
      ArrayList relation = new ArrayList();
      relation = (ArrayList)arrayListTaxRelation.get(i);

      if ((relation.contains(concept)) && (relation.get(0).equals(concept)))
      {
        arrayListConceptTaxRelation.add(relation);
      }
      i++;
    }
  }
}