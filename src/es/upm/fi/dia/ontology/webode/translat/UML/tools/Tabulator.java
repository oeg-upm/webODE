package es.upm.fi.dia.ontology.webode.translat.UML.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.*;

public class Tabulator {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private int    tabulationLength=0;
  private int    tabulationLevel =0;
  private String tabulationString=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void adjustTabulation() {
    char[] blanks=new char[tabulationLength*tabulationLevel];

    Arrays.fill(blanks,' ');

    tabulationString=new String(blanks);
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public Tabulator() {
    tabulationLength=2;
    tabulationString=new String();
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public int getTabulationLength() {
    return tabulationLength;
  }

  public void setTabulationLength(int tabulationLength) {
    this.tabulationLength=tabulationLength;
  }

  public int getTabulationLevel() {
    return tabulationLevel;
  }

  public void increaseTabulation() {
    tabulationLevel++;
    adjustTabulation();
  }

  public void decreaseTabulation() {
    tabulationLevel--;
    adjustTabulation();
  }

  public int getTabulatorLength() {
    return tabulationLength*tabulationLevel;
  }

  public String getTabulator() {
    return tabulationString;
  }
}