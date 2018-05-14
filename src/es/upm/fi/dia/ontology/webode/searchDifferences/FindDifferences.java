package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class FindDifferences </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.service.*;


public abstract class FindDifferences
{

   abstract public void findDifferences (ODEService odeService, Differences differences, String ontologyNameA, String ontologyNameB) throws WebODEException;

}