package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class CompareByName </p>
 * <p>Description: This class is in charge of compare, using names, two arraylists </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.util.*;

public class CompareByName extends CompareOperation
{

  /**
   * This method informs whether two arraylists are equals
   * @param arrayListA
   * @param arrayListB
   * @return whether two arraylists are equals
   */
  public boolean operate (ArrayList arrayListA, ArrayList arrayListB)
  {
    boolean equals = false;

    equals = ((arrayListA.containsAll(arrayListB)) && (arrayListB.containsAll(arrayListA)));

    return equals;
  }
}