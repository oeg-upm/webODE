package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class FindConceptDifferences </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.rmi.RemoteException;
import java.util.*;

import es.upm.fi.dia.ontology.webode.service.*;

public class FindConceptDifferences extends FindDifferences
{

  /**
   * This...
   * @param odeService
   * @param ontologyNameA
   * @param ontologyNameB
   */
  public void findDifferences (ODEService odeService, Differences differences, String ontologyNameA, String ontologyNameB) throws WebODEException
  {
    int types[] = {TermTypes.CONCEPT};
    Term ontologyConceptsA[], ontologyConceptsB[];
    ArrayList arrayListA = new ArrayList(), arrayListB = new ArrayList();
    ArrayList aux;
    Utility util = new Utility();
    boolean equalConcepts;
    CompareByName compare = new CompareByName();

    try
    {
      ontologyConceptsA = odeService.getTerms(ontologyNameA, types);
      ontologyConceptsB = odeService.getTerms(ontologyNameB, types);

      util.TermToArrayList(ontologyConceptsA, arrayListA);
      util.TermToArrayList(ontologyConceptsB, arrayListB);

      // Concepts names in A and concepts names in B are compared
      // using: "CompareByName"
      equalConcepts = compare.operate(arrayListA, arrayListB);

      // If concepts names in A and in B are differents,
      // then added and removed concepts in B are searched
      if (!equalConcepts)
      {
        SearchAdded added = new SearchAdded();
        ArrayList addedConcepts = new ArrayList();

        // Added Concepts in B
        addedConcepts = added.operate (arrayListA, arrayListB);
        if (!addedConcepts.isEmpty())
          differences.insertSimpleDifference (DifferencesTypes.ADDED_CONCEPTS, addedConcepts);

        SearchRemoved removed = new SearchRemoved();
        ArrayList removedConcepts = new ArrayList();

        // Removed Concepts in B
        removedConcepts = removed.operate (arrayListA, arrayListB);
        if (!removedConcepts.isEmpty())
          differences.insertSimpleDifference (DifferencesTypes.REMOVED_CONCEPTS, removedConcepts);
      }

      //aqui tenemos que poner por cada concepto en 1, buscamos el mismo en 2.....
      int i = 0;
      int termTypes[] = {TermTypes.RELATION}; // Future Work: TermTypes.ClassAttributes, etc.
      while (i<arrayListA.size())
      {
        String concept = (String)arrayListA.get(i);
        boolean found;

        found = searchConcept(concept, arrayListB);

        //si el concepto esta en la otra onto
        if (found)
        {
          //vamos a buscar differencias entre ambos conceptos
          int j = 0;
          boolean taxType = true;
          FindDifferencesInConceptsFactory findDiffInConceptFactory = new FindDifferencesInConceptsFactory();
          FindDifferencesInConcepts findDiff;
          while (j<termTypes.length)
          {
            if (termTypes[j] == TermTypes.RELATION)
            {
              findDiff = findDiffInConceptFactory.findDifferences (termTypes[j], taxType);
              findDiff.findDifferences (odeService, differences, ontologyNameA, ontologyNameB, concept);
              taxType = false;
            }
            else
            {
              findDiff = findDiffInConceptFactory.findDifferences (termTypes[j]);
              findDiff.findDifferences (odeService, differences, ontologyNameA, ontologyNameB, concept);
            }
            j++;
          }
        }
        i++;
      }
    }
    catch (RemoteException e)
    {
      System.out.println("ERROR: RemoteException. " + e);
    }
  }

  private boolean searchConcept (String concept, ArrayList ontologyConceptNames)
  {
    boolean found;

    found = ontologyConceptNames.contains (concept);

    return found;
  }
}