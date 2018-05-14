package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class SearchRemoved </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.util.*;

public class SearchRemoved extends SearchOperation
{

  public ArrayList operate (ArrayList arrayListA, ArrayList arrayListB)
 {
   ArrayList arrayListResult = new ArrayList (arrayListA);

   arrayListResult.removeAll(arrayListB);

   return arrayListResult;
  }
}