package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class FindDifferencesInConcepts </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.service.*;

public abstract class FindDifferencesInConcepts
{

  public abstract void findDifferences (ODEService odeService, Differences differences, String ontologyNameA, String ontologyNameB, String concept) throws WebODEException;
}