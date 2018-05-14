package es.upm.fi.dia.ontology.webode.translat.UML.ode.component.impl;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.ode.component.*;

public class ODEMereologicalRelationImpl extends ODEConceptToConceptRelationHandler
                                         implements ODEMereologicalRelation {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private boolean relationIsTransitive=false;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public ODEMereologicalRelationImpl(ODEConcept origin,
                                     ODEConcept destination ) {
    this(origin,destination,false);
  }

  public ODEMereologicalRelationImpl(ODEConcept origin,
                                     ODEConcept destination,
                                     boolean makeTransitive ) {
    super((makeTransitive?
             RelationTypeValues.TRANSITIVE_PART_OF:
             RelationTypeValues.INTRANSITIVE_PART_OF),
          origin,
          destination);
    relationIsTransitive=makeTransitive;
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String toString() {
    String mySelf=new String();


    mySelf+="ODEMereologicalRelation {";
    mySelf+=getName()+", ";
    mySelf+=getOrigin().getName()+", ";
    mySelf+=getDestination().getName()+", ";
    mySelf+=isTransitive();
    mySelf+="}";

    return mySelf;
  }

  //-- Interface: Cloneable ----------------------------------------------------

  public Object clone() {
    ODEMereologicalRelationImpl cloned=
        (ODEMereologicalRelationImpl)super.clone();

    return cloned;
  }

  //-- Interface: ODEMereologicalRelation --------------------------------------

  public boolean isTransitive() {
    return relationIsTransitive;
  }

  public void setTransitiveStatus(boolean makeTransitive) {
    relationIsTransitive=makeTransitive;
    if(makeTransitive)
      setName(RelationTypeValues.TRANSITIVE_PART_OF);
    else
      setName(RelationTypeValues.INTRANSITIVE_PART_OF);
  }

}