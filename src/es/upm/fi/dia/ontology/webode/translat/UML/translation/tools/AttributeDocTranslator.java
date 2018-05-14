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

public class AttributeDocTranslator {

  public static final String ATTRIBUTE_TYPE  ="attributeType";
  public static final String TYPE            ="type";
  public static final String VALUES          ="values";
  public static final String CARDINALITY     ="cardinality";
  public static final String MEASUREMENT_UNIT="measurementUnit";
  public static final String PRECISION       ="precision";

  public static final String AT_INSTANCE="instanceAttribute";
  public static final String AT_CLASS   ="classAttribute";

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public AttributeDocTranslator() {
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String fromOde(ODEAttribute attribute) throws IOException {
    ODEDocManager dm=new ODEDocManager(attribute.getDescription());

    ODEDocPlus attributeTypeDoc=new ODEDocPlus();

    attributeTypeDoc.setProperty(ATTRIBUTE_TYPE);
    if(attribute instanceof ODEInstanceAttribute)
      attributeTypeDoc.setArgument(0,AT_INSTANCE);
    else
      attributeTypeDoc.setArgument(0,AT_CLASS);

    dm.addDoc(attributeTypeDoc);

    ODEDocPlus typeDoc=new ODEDocPlus();

    typeDoc.setProperty(TYPE);
    typeDoc.setArgument(0,
                        AttributeValueTypes.toString(attribute.getValueType()));

    String[] values=attribute.getValues();

    if(values!=null&&values.length>0) {
      ODEDocPlus valuesDoc=new ODEDocPlus();

      valuesDoc.setProperty(VALUES);
      for(int i=0;i<values.length;i++) {
        valuesDoc.setArgument(i,values[i]);
      }

      dm.addDoc(valuesDoc);
    }

    ODEDocPlus cardinalityDoc=new ODEDocPlus();

    cardinalityDoc.setProperty(CARDINALITY);
    cardinalityDoc.setArgument(0,Integer.toString(attribute.getMinimumCardinality()));
    cardinalityDoc.setArgument(1,Integer.toString(attribute.getMaximumCardinality()));

    dm.addDoc(cardinalityDoc);

    String mu=attribute.getMeasurementUnit();
    if(mu!=null&&!mu.equals("")) {
      ODEDocPlus muDoc=new ODEDocPlus();

      muDoc.setProperty(MEASUREMENT_UNIT);
      muDoc.setArgument(0,mu);

      dm.addDoc(muDoc);
    }

    String precision = attribute.getPrecision();
    if(precision!=null&&!precision.equals("")) {
      ODEDocPlus precisionDoc=new ODEDocPlus();

      precisionDoc.setProperty(PRECISION);
      precisionDoc.setArgument(0,precision);

      dm.addDoc(precisionDoc);
    }

    return dm.marshall();
  }

  public void toOde(ODEAttribute attribute, ODEDocManager dm) {
    ODEDoc   doc =null;
    ODEDoc[] docs=null;

    attribute.setDescription(dm.getRemainingData());

    if(dm.containsProperty(TYPE)) {
      docs=dm.getDocs(TYPE);
      if(docs.length>=1) {
        doc=docs[0];
        if(doc.getArgumentCount()==1) {
          String type   =doc.getArgument(0);
          int    theType=AttributeValueTypes.fromString(type);
          attribute.setValueType(theType);
        }
      }
    }

    if(dm.containsProperty(VALUES)) {
      docs=dm.getDocs(VALUES);
      if(docs.length>=1) {
        doc=docs[0];
        int length=doc.getArgumentCount();
        if(length>=1) {
          String[] values=new String[length];
          for(int i=0;i<length;i++) {
            values[i]=doc.getArgument(i);
          }
          attribute.setValues(values);
        }
      }
    }

    if(dm.containsProperty(CARDINALITY)) {
      docs=dm.getDocs(CARDINALITY);
      if(docs.length>=1) {
        doc=docs[0];
        if(doc.getArgumentCount()==2) {
          try {
            String minimum=doc.getArgument(0);
            int minCardinality=Integer.parseInt(minimum);
            attribute.setMinimumCardinality(minCardinality);
          } catch (NumberFormatException ex) {
            // Cardinality number not well written
          }

          try {
            String maximum=doc.getArgument(1);
            int maxCardinality=Integer.parseInt(maximum);
            attribute.setMaximumCardinality(maxCardinality);
          } catch (NumberFormatException ex) {
            // Cardinality number not well written
          }
        }
      }
    }

    if(dm.containsProperty(MEASUREMENT_UNIT)) {
      docs=dm.getDocs(MEASUREMENT_UNIT);
      if(docs.length>=1) {
        doc=docs[0];
        if(doc.getArgumentCount()==1) {
          String measurement_unit=doc.getArgument(0);
          attribute.setMeasurementUnit(measurement_unit);
        }
      }
    }

    if(dm.containsProperty(PRECISION)) {
      docs=dm.getDocs(PRECISION);
      if(docs.length>=1) {
        doc=docs[0];
        if(doc.getArgumentCount()==1) {
          String precision=doc.getArgument(0);
          attribute.setPrecision(precision);
        }
      }
    }
  }
}