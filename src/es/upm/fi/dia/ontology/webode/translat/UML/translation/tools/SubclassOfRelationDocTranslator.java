package es.upm.fi.dia.ontology.webode.translat.UML.translation.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.io.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;

public class SubclassOfRelationDocTranslator {

  public static final String ORIGIN     ="origin";
  public static final String DESTINATION="destination";

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public SubclassOfRelationDocTranslator() {
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String fromOde(ODESubclassOfRelation relation) throws IOException {
    ODEDocManager dm=new ODEDocManager(relation.getDescription());

/*
    ODEDocPlus originDoc=new ODEDocPlus();

    originDoc.setProperty(ORIGIN);
    originDoc.setArgument(0,relation.getOrigin().getName());

    dm.addDoc(originDoc);

    ODEDocPlus destinationDoc=new ODEDocPlus();

    destinationDoc.setProperty(DESTINATION);
    destinationDoc.setArgument(0,relation.getDestination().getName());

    dm.addDoc(destinationDoc);
*/
    return dm.marshall();
  }

  public void toOde(ODESubclassOfRelation relation, ODEDocManager dm) {

  }
}