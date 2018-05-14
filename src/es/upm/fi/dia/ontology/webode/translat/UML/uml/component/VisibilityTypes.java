package es.upm.fi.dia.ontology.webode.translat.UML.uml.component;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

public final class VisibilityTypes {

  //----------------------------------------------------------------------------
  //-- Data model constants ----------------------------------------------------
  //----------------------------------------------------------------------------

  public final static int PUBLIC   =1;
  public final static int PRIVATE  =2;
  public final static int PROTECTED=3;

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public final static boolean isValid(int type) {
    if(type!=PUBLIC&&
       type!=PRIVATE&&
       type!=PROTECTED)
      return false;

    return true;
  }

  public final static String toString(int type) {
    if(!isValid(type))
      throw new RuntimeException("Not valid visibility descriptor");

    switch(type) {
      case PUBLIC:
        return "public";
      case PRIVATE:
        return "private";
      case PROTECTED:
        return "protected";
    }

    return null;
  }

}