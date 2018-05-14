package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class SearchAdded </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.util.*;

public class SearchAdded extends SearchOperation
{

  public ArrayList operate (ArrayList arrayListA, ArrayList arrayListB)
  {
    ArrayList arrayListResult = new ArrayList (arrayListB);

    arrayListResult.removeAll(arrayListA);

    return arrayListResult;
  }
}