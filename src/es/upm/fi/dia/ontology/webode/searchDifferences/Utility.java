package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class Utility </p>
 * <p>Description: This class contains methods to....  </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.util.*;

import es.upm.fi.dia.ontology.webode.service.*;


public class Utility
{

  public void TermToArrayList (Term[] terms, ArrayList arrayList)
  {
    int i;
    for (i=0; i<terms.length; i++)
    {
      arrayList.add(terms[i].term);
    }
  }

  public void TermRelationToArrayList (TermRelation[] termRelations, ArrayList arrayList)
  {
    int i;

    for (i=0; i<termRelations.length; i++)
    {
      ArrayList relation = new ArrayList();

      relation.add(termRelations[i].origin); // Add origin
      relation.add(termRelations[i].destination); // Add destination

      arrayList.add(relation);
    }
  }
}