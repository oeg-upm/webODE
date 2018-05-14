package es.upm.fi.dia.ontology.webode.translat.UML.ode.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.service.*;

public final class AttributeValueTypes {

  //----------------------------------------------------------------------------
  //-- Data model constants ----------------------------------------------------
  //----------------------------------------------------------------------------

  public final static int BOOLEAN =1;
  public final static int CARDINAL=2;
  public final static int DATE    =3;
  public final static int FLOAT   =4;
  public final static int INTEGER =5;
  public final static int ONTOLOGY=6;
  public final static int RANGE   =7;
  public final static int STRING  =8;
  public final static int URL     =9;

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public final static boolean isValid(int type) {
    if(type!=BOOLEAN&&
       type!=CARDINAL&&
       type!=DATE&&
       type!=FLOAT&&
       type!=INTEGER&&
       type!=ONTOLOGY&&
       type!=RANGE&&
       type!=STRING&&
       type!=URL)
      return false;

    return true;
  }

  public final static int fromString(String type) {
    int theType=0;

    if(type.equalsIgnoreCase("boolean")) {
      theType=BOOLEAN;
    } else if(type.equalsIgnoreCase("cardinal")) {
      theType=CARDINAL;
    } else if(type.equalsIgnoreCase("date")) {
      theType=DATE;
    } else if(type.equalsIgnoreCase("float")) {
      theType=FLOAT;
    } else if(type.equalsIgnoreCase("integer")) {
      theType=INTEGER;
    } else if(type.equalsIgnoreCase("ontology")) {
      theType=ONTOLOGY;
    } else if(type.equalsIgnoreCase("range")) {
      theType=RANGE;
    } else if(type.equalsIgnoreCase("string")) {
      theType=STRING;
    } else if(type.equalsIgnoreCase("url")) {
      theType=URL;
    }

    return theType;
  }

  public final static String toString(int type) {
    if(!isValid(type))
      throw new RuntimeException("Not valid attribute type descriptor");

    switch(type) {
      case BOOLEAN :
        return "Boolean";
      case CARDINAL:
        return "Cardinal";
      case DATE    :
        return "Date";
      case FLOAT   :
        return "Float";
      case INTEGER :
        return "Integer";
      case ONTOLOGY:
        return "Ontology";
      case RANGE   :
        return "Range";
      case STRING  :
        return "String";
      case URL     :
        return "Url";
    }

    return null;
  }

  public final static int toODEDataType(int originalValueType) {
    int valueType=0;
    switch(originalValueType) {
      case ValueTypes.BOOLEAN:
        return AttributeValueTypes.BOOLEAN;
      case ValueTypes.CARDINAL:
        return AttributeValueTypes.CARDINAL;
      case ValueTypes.DATE:
        return AttributeValueTypes.DATE;
      case ValueTypes.FLOAT:
        return AttributeValueTypes.FLOAT;
      case ValueTypes.INTEGER:
        return AttributeValueTypes.INTEGER;
//      case ValueTypes.ONTOLOGY:
//        return AttributeValueTypes.ONTOLOGY;
      case ValueTypes.RANGE:
        return AttributeValueTypes.RANGE;
      case ValueTypes.STRING:
        return AttributeValueTypes.STRING;
      case ValueTypes.URL:
        return AttributeValueTypes.URL;
    }
    return valueType;
  }

  public final static int toWebODEDataType(int originalValueType) {
    int valueType=0;
    switch(originalValueType) {
      case AttributeValueTypes.BOOLEAN:
        return ValueTypes.BOOLEAN;
      case AttributeValueTypes.CARDINAL:
        return ValueTypes.CARDINAL;
      case AttributeValueTypes.DATE:
        return ValueTypes.DATE;
      case AttributeValueTypes.FLOAT:
        return ValueTypes.FLOAT;
      case AttributeValueTypes.INTEGER:
        return ValueTypes.INTEGER;
//      case AttributeValueTypes.ONTOLOGY:
//        return ValueTypes.ONTOLOGY;
      case AttributeValueTypes.RANGE:
        return ValueTypes.RANGE;
      case AttributeValueTypes.STRING:
        return ValueTypes.STRING;
      case AttributeValueTypes.URL:
        return ValueTypes.URL;
    }
    return valueType;
  }

}