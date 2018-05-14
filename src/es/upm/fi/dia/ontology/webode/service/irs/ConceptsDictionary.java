package es.upm.fi.dia.ontology.webode.service.irs;

/* importaciones */
import java.util.*;
import java.rmi.*;
import java.sql.*;
import es.upm.fi.dia.ontology.webode.service.*;


public class ConceptsDictionary extends Object /* implements intafaz */ {

  public ConceptsDictionary() {

  };

  public Vector getRow(ODEService ode, String ontology, String instanceSet) throws SQLException, WebODEException, RemoteException {
    try {
      Term[] concepts	= ode.getTerms (ontology, new int [] {TermTypes.CONCEPT});
      Vector myVector= new Vector(300,10);
      TermRelation[] relations = ode.getTermRelations(ontology, TermRelation.ADHOC);
      //System.out.println("Las relaciones son: " + relations);
      if (concepts != null) {
        for (int i=0; i < concepts.length; i++){/*Main for*/
          myVector.addElement(concepts[i].term);
          String parentTerm=null;
          /**************Synonyms**************************/
          SynonymAbbreviation[] synonyms2 = ode.getSynonyms( ontology, concepts[i].term, parentTerm);
          if (synonyms2!=null){
            Vector synonymsVector = new Vector(8,4);
            for (int m=0; m< synonyms2.length ; m++){
              synonymsVector.addElement(synonyms2[m].name);
            }/*End for*/
            if (synonymsVector.size()==0){
              myVector.addElement(null);//There aren´t any synonym
            }else{
              myVector.addElement(synonymsVector);
            }/*End if*/
          }else{
            myVector.addElement(null);//There aren´t any synonym
          }/*End else*/
          /*********************Acronysm*****************/
          SynonymAbbreviation[] synonyms = ode.getAbbreviations( ontology, concepts[i].term, parentTerm);
          if (synonyms!=null){
            Vector abbreviationVector = new Vector(8,4);
            for (int l=0; l< synonyms.length ; l++){
              abbreviationVector.addElement(synonyms[l].name);
            }/*End for*/
            if (abbreviationVector.size()==0){
              myVector.addElement(null);//There aren´t any abbreviation
            }else{
              myVector.addElement(abbreviationVector);
            }/*End if*/
          }else{
            myVector.addElement(null);//There aren´t any abbreviation
          }/*End else*/
          /*************Instances*****************/
          if (instanceSet!=null) {
            Instance[] instances = ode.getInstances(ontology, instanceSet, concepts[i].term);
            if (instances!=null){
              Vector instancesVector= new Vector(10,4);
              for(int j=0; j < instances.length; j++){
                instancesVector.addElement(instances[j].name);
              }/*end for*/
              myVector.addElement(instancesVector);
            }else{
              myVector.addElement(null);
            }
          }else{
            myVector.addElement(null);////There aren´t any instance
          }/*End else*/
          /*************Class Attributes**********/
          ClassAttributeDescriptor[] attributes=ode.getClassAttributes(ontology,concepts[i].term);
          if (attributes!=null){
            Vector attributeNamesList= new Vector(25,5);
            for(int j=0; j < attributes.length; j++){
              attributeNamesList.addElement(attributes[j].name);
            }/*end for*/
            myVector.addElement(attributeNamesList);
          }else
            myVector.addElement(null);
          /*************Instance Attributes*********/
          if (instanceSet!=null){
            InstanceAttributeDescriptor[] instanceAttributes;
            instanceAttributes=ode.getInstanceAttributes(ontology, concepts[i].term);
            if (instanceAttributes!=null){
              Vector instancesNamesList= new Vector(25,5);
              for(int j=0; j < instanceAttributes.length; j++){
                instancesNamesList.addElement(instanceAttributes[j].name);
              }/*end for*/
              myVector.addElement(instancesNamesList);
            }else
              myVector.addElement(null);
          }else
            myVector.addElement(null);
          /**************Relations*****************/
          Vector relationsVector = new Vector(10,4);
          if (relations != null){
            for (int j=0; j < relations.length; j++){
              if (relationsVector.indexOf(relations[j].name)==-1){
                if ((relations[j].origin.equals(concepts[i].term))){
                  relationsVector.addElement(relations[j].name);
                }/*end if*/
              }/*End if*/
            }/*End for*/
            if (relationsVector.size()==0){
              myVector.addElement(null);
            }else{
              myVector.addElement(relationsVector);
            }/*End else*/
          }else{
            myVector.addElement(null);
          }/*End else*/
        }/*End main for*/
      }else{
        myVector.addElement(null);
      }/*End if*/
      return myVector;
    }
    catch (Exception e){
      System.out.println(e.getMessage());
      return null;
    }
  }
}