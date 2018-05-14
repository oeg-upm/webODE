package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.service.*;


public class FindDifferencesInConceptsFactory
{

  public FindDifferencesInConcepts findDifferences (int termType) throws WebODEException
  {
    FindDifferencesInConcepts findDiff = null;

    /* Future Work
    if (termType == TermTypes.CLASS_ATTRIBUTE)
    {
      findDiff = new FindClassAttributeDifferences ();
    }
    else if (termType == TermTypes.INSTANCE_ATTRIBUTE)
    {
      findDiff = new FindInstanceAttributeDifferences ();
    }
    Future Work: etc. */

    return findDiff;

  }

  public FindDifferencesInConcepts findDifferences (int termType, boolean subTypeTax) throws WebODEException
  {
    FindDifferencesInConcepts findDiff = null;

    if ((termType == TermTypes.RELATION) && (subTypeTax)) // Taxonomic Relation
    {
        findDiff = new FindTaxonomicRelationDifferences();
    }
    /* Future Work
    else if ((termType == TermTypes.RELATION) && (!subType)) // Ad-Hoc Relation ¿¿Cuantos tipos de relaciones existen??
    {
        findDiff = new FindAdHocRelationDifferences();
    }
    Future Work: etc. */

    return findDiff;
  }
}