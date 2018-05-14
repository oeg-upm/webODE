package es.upm.fi.dia.ontology.webode.service;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class RelationInstanceComparator implements java.util.Comparator {
  public int compare(Object obj1, Object obj2) {
    int res;
    TermRelationInstance ri1=(TermRelationInstance)obj1;
    TermRelationInstance ri2=(TermRelationInstance)obj2;
    res=ri1.termRelation.name.compareTo(ri2.termRelation.name);
    if(res==0)
      res=ri1.origin.compareTo(ri2.origin);
    if(res==0)
      res=ri1.destination.compareTo(ri2.destination);
    return res;
  }
}
