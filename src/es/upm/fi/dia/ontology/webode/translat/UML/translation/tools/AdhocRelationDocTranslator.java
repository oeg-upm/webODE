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

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;

public class AdhocRelationDocTranslator {

  public static final String ORIGIN     ="origin";
  public static final String DESTINATION="destination";
  public static final String PROPERTIES ="properties";

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public AdhocRelationDocTranslator() {
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String fromOde(ODEAdhocRelation relation) throws IOException {
    ODEDocManager dm=new ODEDocManager(relation.getDescription());

    ODEDocPlus originDoc=new ODEDocPlus();

    originDoc.setProperty(ORIGIN);
    originDoc.setArgument(0,relation.getOrigin().getName());

    dm.addDoc(originDoc);

    ODEDocPlus destinationDoc=new ODEDocPlus();

    destinationDoc.setProperty(DESTINATION);
    destinationDoc.setArgument(0,relation.getDestination().getName());

    dm.addDoc(destinationDoc);

    String[] properties=relation.getProperties();

    if(properties!=null&&properties.length>0) {
      ODEDocPlus propertiesDoc=new ODEDocPlus();

      propertiesDoc.setProperty(PROPERTIES);
      for(int i=0;i<properties.length;i++) {
        propertiesDoc.setArgument(i,properties[i]);
      }

      dm.addDoc(propertiesDoc);
    }

    return dm.marshall();
  }

  public void toOde(ODEAdhocRelation relation, ODEDocManager dm) {
    ODEDoc   doc =null;
    ODEDoc[] docs=null;

    relation.setDescription(dm.getRemainingData());

/*
    if(dm.containsProperty(ORIGIN)) {
      docs=dm.getDocs(ORIGIN);
      if(docs.length>=1) {
        doc=docs[0];
        if(doc.getArgumentCount()==1) {
          String conceptName=doc.getArgument(0);
          ODEConcept origin=relation.getModel().findConcept(conceptName);
          relation.setOrigin(origin);
        }
      }
    }

    if(dm.containsProperty(DESTINATION)) {
      docs=dm.getDocs(DESTINATION);
      if(docs.length>=1) {
        doc=docs[0];
        if(doc.getArgumentCount()==1) {
          String conceptName=doc.getArgument(0);
          ODEConcept origin=relation.getModel().findConcept(conceptName);
          relation.setOrigin(origin);
        }
      }
    }
*/

    if(dm.containsProperty(PROPERTIES)) {
      docs=dm.getDocs(PROPERTIES);
      if(docs.length>=1) {
        doc=docs[0];
        int length=doc.getArgumentCount();
        if(length>=1) {
          String[] properties=new String[length];
          for(int i=0;i<length;i++) {
            properties[i]=doc.getArgument(i);
          }
          try {
            relation.setProperties(properties);
          }
          catch (ODEException ex) {
            ex.printStackTrace();
            System.err.println("Limpiando...");
            try {
              relation.setProperties(new String[]{});
            } catch (ODEException ignoreMe) {
            }
          }
        }
      }
    }

  }
}