package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class DifferencesTypes </p>
 * <p>Description: This class contains the types of differences to be found </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

public interface DifferencesTypes
{

  static final Integer ADDED_CONCEPTS = new Integer(0);
  static final Integer REMOVED_CONCEPTS = new Integer(1);
  static final Integer ADDED_SC_RELATIONS = new Integer(2);
  static final Integer REMOVED_SC_RELATIONS = new Integer(3);
  // Future Work: Added_Class_Attributes, etc.

  static final Integer DIFF_TYPES[] = {ADDED_CONCEPTS, REMOVED_CONCEPTS,
                             ADDED_SC_RELATIONS, REMOVED_SC_RELATIONS}; // Future Work: Added_Class_Attributes, etc.

}