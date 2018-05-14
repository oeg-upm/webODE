package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class CompareOperation </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.util.*;

public abstract class CompareOperation
{
  public abstract boolean operate (ArrayList arrayListA, ArrayList arrayLisB);
}