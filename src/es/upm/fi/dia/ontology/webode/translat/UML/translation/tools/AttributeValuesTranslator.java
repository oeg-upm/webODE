package es.upm.fi.dia.ontology.webode.translat.UML.translation.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.*;

public class AttributeValuesTranslator {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private String[] values      =null;
  private String   initialValue=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void buildInitialValue() {
    initialValue="[";
    for(int j=0;j<values.length;j++) {
      if(j!=0)
        initialValue+=", ";
      initialValue+=values[j];
    }
    initialValue+="]";
  }

  private void buildValues() {
    if(initialValue.startsWith("[")&&
       initialValue.endsWith("]")) {
      StringTokenizer lexer=
          new StringTokenizer(initialValue.substring(1,initialValue.length()-1),
                              ",");
      Vector auxValues=new Vector();
      while(lexer.hasMoreTokens()) {
        auxValues.add(lexer.nextToken());
      }
      values=new String[auxValues.size()];
      auxValues.toArray(values);
    }
  }

  //----------------------------------------------------------------------------
  //-- Constructor --------------------------------------------------------------
  //----------------------------------------------------------------------------

  public AttributeValuesTranslator(String initialValue) {
    this.initialValue=initialValue;
    buildValues();
  }

  public AttributeValuesTranslator(String[] values) {
    this.values=values;
    buildInitialValue();
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String[] getValues() {
    return values;
  }

  public String getInitialValue() {
    return initialValue;
  }

}