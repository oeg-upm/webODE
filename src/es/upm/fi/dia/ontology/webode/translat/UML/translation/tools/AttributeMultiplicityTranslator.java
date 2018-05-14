package es.upm.fi.dia.ontology.webode.translat.UML.translation.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public class AttributeMultiplicityTranslator {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private int minCardinality=0;
  private int maxCardinality=0;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private int getCardinality(String multiplicity, boolean minimum) {
    int cardinality=0;
    String number=null;

    if(minimum) {
      number=multiplicity.substring(0,multiplicity.indexOf(".."));
    } else {
      number=multiplicity.substring(multiplicity.indexOf("..")+2,multiplicity.length());
    }

    cardinality=Integer.parseInt(number);

    return cardinality;
  }

  //----------------------------------------------------------------------------
  //-- Constructor --------------------------------------------------------------
  //----------------------------------------------------------------------------

  public AttributeMultiplicityTranslator(String multiplicity) {
    minCardinality=getCardinality(multiplicity,true);
    maxCardinality=getCardinality(multiplicity,false);
  }

  public AttributeMultiplicityTranslator(int minCardinality,
                                         int maxCardinality) {
    this.minCardinality=minCardinality;
    this.maxCardinality=maxCardinality;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public int getMinimumCardinality() {
    return minCardinality;
  }

  public int getMaximumCardinality() {
    return maxCardinality;
  }

  public String getMultiplicity() {
    return  getMinimumCardinality()+".."+getMaximumCardinality();
  }

}