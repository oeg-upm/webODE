package es.upm.fi.dia.ontology.webode.util;
import javax.swing.tree.*;
import java.util.*;

public class ConceptData implements java.io.Serializable
{
  // data members
  protected String concept;
  protected String ontology;

  // constructors
  public ConceptData()
  {
  }

  public ConceptData(String aConcept, String aOntology)
  {
    concept = aConcept;
    ontology = aOntology;
  }

  //  accessors
  public String getConcept()
  {
    return concept;
  }

  public String getOntology()
  {
    return ontology;
  }
}