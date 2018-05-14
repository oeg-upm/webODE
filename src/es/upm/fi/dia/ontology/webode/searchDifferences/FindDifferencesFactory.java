package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class FindDifferencesFactory </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.service.*;

public class FindDifferencesFactory
{

  public FindDifferences findDifferences (int termType) throws WebODEException
  {
    FindDifferences findDiff;

    if (termType == TermTypes.CONCEPT)
    {
      findDiff = new FindConceptDifferences ();
    }
    /* Future Work
    else if (termType == TermTypes.AXIOM)
    {
      findDiff = new FindAxiomDifferences ();
    }
    Future Work: etc. */
    else
      findDiff = null;

    return findDiff;
  }
}