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

public class MereologicalRelationDocTranslator {

  public static final String TYPE       ="type";
  public static final String ORIGIN     ="origin";
  public static final String DESTINATION="destination";
  public static final String PROPERTIES ="properties";

  public static final String TRANSITIVE  ="transitivePartOf" ;
  public static final String INTRANSITIVE="intransitivePartOf" ;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public MereologicalRelationDocTranslator() {
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String fromOde(ODEMereologicalRelation relation) throws IOException {
    ODEDocManager dm=new ODEDocManager(relation.getDescription());

    ODEDocPlus typeDoc=new ODEDocPlus();

    typeDoc.setProperty(TYPE);
    typeDoc.setArgument(0,(relation.isTransitive()?TRANSITIVE:INTRANSITIVE));

    dm.addDoc(typeDoc);

    ODEDocPlus originDoc=new ODEDocPlus();

    originDoc.setProperty(ORIGIN);
    originDoc.setArgument(0,relation.getOrigin().getName());

    dm.addDoc(originDoc);

    ODEDocPlus destinationDoc=new ODEDocPlus();

    destinationDoc.setProperty(DESTINATION);
    destinationDoc.setArgument(0,relation.getDestination().getName());

    dm.addDoc(destinationDoc);

    return dm.marshall();
  }

  public void toOde(ODEMereologicalRelation relation, ODEDocManager dm) {
    ODEDoc   doc =null;
    ODEDoc[] docs=null;

    relation.setDescription(dm.getRemainingData());

    if(dm.containsProperty(TYPE)) {
      docs=dm.getDocs(TYPE);
      if(docs.length>=1) {
        doc=docs[0];
        if(doc.getArgumentCount()==1) {
          String transitive_status=doc.getArgument(0);
          if(transitive_status.equals(TRANSITIVE))
            relation.setTransitiveStatus(true);
          else if(transitive_status.equals(INTRANSITIVE))
            relation.setTransitiveStatus(false);
        }
      }
    }

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
          ODEConcept destination=relation.getModel().findConcept(conceptName);
          relation.setDestination(destination);
        }
      }
    }


  }
}