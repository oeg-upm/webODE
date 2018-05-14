/*****************************************/
/* COWOVA y COVAWO ***********************/
/* COnversión de WedOde a Vector Anidado */
/* y de Vector Anidado a WebOde **********/
/*****************************************/
/* Author: J.A.R.G. **********************/
/*****************************************/

package es.upm.fi.dia.ontology.webode.merge;

import java.io.*;
import java.util.*;
import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.client.*;

public class COWOVA
{

  PrintWriter out;
  private String minervaURL="minerva://localhost";
  private String username="admin";
  private String password="admin";
  Vector adhocs=new Vector();
  ODEService ServicioODE;

  private String Util=new String();
  private int profundidad=0;

  public COWOVA(PrintWriter sal)
  {
    super();
    out=sal;
  }

  Casilla Leer(String name) throws Exception
  {
    Term raiz;
    Casilla casilla=new Casilla();
    casilla.profundidad=profundidad;

    out.println("Leyendo "+name);
    try
    {
      raiz=ServicioODE.getTerm(Util, name);
      out.println(raiz.term+"... Read");
      casilla.nombre=new String (raiz.term);

      casilla.catributos=LeerCAttributos(name);
      casilla.iatributos=LeerIAttributos(name);
      casilla.relaciones=LeerRelaciones(name);
    }
    catch (WebODEException e)
    {
      throw new Exception("An error ocurred accessing the ontology: "+e.getMessage());
    }

    return casilla;
  }

  Vector LeerCAttributos(String name) throws Exception
  {
    Vector atrib=new Vector();
    ClassAttributeDescriptor attributes[];

    try
    {
      attributes=ServicioODE.getClassAttributes(Util,name);
      if (attributes!=null)
        for(int i=0;i<attributes.length;i++)
        {
          Atributo pasante=new Atributo();
          pasante.nombre=attributes[i].name.toString();
          if (attributes[i].description!=null)
            pasante.descripcion=attributes[i].description.toString();
          pasante.tipo=Integer.toString(attributes[i].valueType);
          pasante.card_minima=Integer.toString(attributes[i].minCardinality);
          pasante.card_maxima=Integer.toString(attributes[i].maxCardinality);
          if (attributes[i].measurementUnit!=null)
            pasante.unidad=attributes[i].measurementUnit.toString();
          if (attributes[i].precision!=null)
            pasante.precision=attributes[i].precision.toString();
          pasante.referencias=BusquedaReferencias(attributes[i].name);
          pasante.sinonimos=BusquedaSinonimos(attributes[i].name,name);
          pasante.abreviaturas=BusquedaAbreviaturas(attributes[i].name,name);
          pasante.formulas=BusquedaFormulas(attributes[i].name);
          pasante.deducido_de=BusquedaDeducciones(attributes[i].name,name);
          atrib.addElement(pasante);
        }
    }
    catch (WebODEException e)
    {
      throw new Exception("An error ocurred acceding ontology: "+e.getMessage());
    }
    return atrib;
  }

  Vector LeerIAttributos(String name) throws Exception
  {
    Vector atrib=new Vector();
    InstanceAttributeDescriptor attributes[];

    try
    {
      attributes=ServicioODE.getInstanceAttributes(Util,name);
      if (attributes!=null)
        for(int i=0;i<attributes.length;i++)
        {
          Atributo pasante=new Atributo();
          pasante.nombre=attributes[i].name.toString();
          if (attributes[i].description!=null)
            pasante.descripcion=attributes[i].description.toString();
          pasante.tipo=Integer.toString(attributes[i].valueType);
          pasante.card_minima=Integer.toString(attributes[i].minCardinality);
          pasante.card_maxima=Integer.toString(attributes[i].maxCardinality);
          if (attributes[i].measurementUnit!=null)
            pasante.unidad=attributes[i].measurementUnit.toString();
          if (attributes[i].precision!=null)
            pasante.precision=attributes[i].precision.toString();
          pasante.valor.addElement(attributes[i].minValue);
          pasante.valor.addElement(attributes[i].maxValue);
          pasante.referencias=BusquedaReferencias(attributes[i].name);
          pasante.sinonimos=BusquedaSinonimos(attributes[i].name,name);
          pasante.abreviaturas=BusquedaAbreviaturas(attributes[i].name,name);
          pasante.formulas=BusquedaFormulas(attributes[i].name);
          pasante.deducido_de=BusquedaDeducciones(attributes[i].name,name);
          atrib.addElement(pasante);
        }
    }
    catch (WebODEException e)
    {
      throw new Exception("An error ocurred acceding ontology: "+e.getMessage());
    }
    return atrib;
  }

  Vector BusquedaReferencias(String name_attribute) throws Exception
  {
    Vector salida=new Vector();
    ReferenceDescriptor referencias[];
    try
    {
      referencias=ServicioODE.getTermReferences(Util,name_attribute);
      if (referencias!=null)
        for(int i=0;i<referencias.length;i++)
          salida.addElement(referencias[i]);
    }
    catch (WebODEException e)
    {
      throw new Exception("An error ocurred accessing the ontology: "+e.getMessage());
    }
    return salida;
  }

  Vector BusquedaSinonimos(String name_attribute, String name_concept) throws Exception
  {
    Vector salida=new Vector();
    SynonymAbbreviation sinonimos[];
    try
    {
      sinonimos=ServicioODE.getSynonyms(Util,name_attribute,name_concept);
      if (sinonimos!=null)
        for(int i=0;i<sinonimos.length;i++)
          salida.addElement(sinonimos[i]);
    }
    catch (WebODEException e)
    {
      throw new Exception("An error ocurred accessing the ontology: "+e.getMessage());
    }
    return salida;
  }

  Vector BusquedaAbreviaturas(String name_attribute, String name_concept) throws Exception
  {
    Vector salida=new Vector();
    SynonymAbbreviation abreviaturas[];
    try
    {
      abreviaturas=ServicioODE.getAbbreviations(Util,name_attribute,name_concept);
      if (abreviaturas!=null)
        for(int i=0;i<abreviaturas.length;i++)
          salida.addElement(abreviaturas[i]);
    }
    catch (WebODEException e)
    {
      throw new Exception("An error ocurred accessing the ontology: "+e.getMessage());
    }
    return salida;
  }

  Vector BusquedaFormulas(String name_attribute) throws Exception
  {
    Vector salida=new Vector();
    FormulaDescriptor formulas[];
    try
    {
      formulas=ServicioODE.getReasoningElements(Util,name_attribute);
      if (formulas!=null)
        for(int i=0;i<formulas.length;i++)
          salida.addElement(formulas[i]);
    }
    catch (WebODEException e)
    {
      throw new Exception("An error ocurred accessing the ontology: "+e.getMessage());
    }
    return salida;
  }

  Vector BusquedaDeducciones(String name_attribute,String name_concept) throws Exception
  {
    Vector salida=new Vector();
    AttributeInference deducciones[];
    try
    {
      deducciones=ServicioODE.getInferringAttributes(Util,name_attribute,name_concept);
      if (deducciones!=null)
        for(int i=0;i<deducciones.length;i++)
          salida.addElement(deducciones[i]);
    }
    catch (WebODEException e)
    {
      throw new Exception("An error ocurred accessing the ontology: "+e.getMessage());
    }
    return salida;
  }

  Vector LeerRelaciones(String name) throws Exception
  {
    Vector rel=new Vector();
    TermRelation relaciones[];

    try
    {
      relaciones=ServicioODE.getTermRelations(Util,TermRelation.ADHOC);
      if (relaciones!=null)
        if (relaciones!=null)
          for(int i=0;i<relaciones.length;i++)
            if (relaciones[i].origin.compareTo(name)==0)
            {
            Relacion pasante=new Relacion();
            pasante.nombre=relaciones[i].name.toString();
            pasante.origen=relaciones[i].origin.toString();
            pasante.destino=relaciones[i].destination.toString();
            pasante.card_maxima=Integer.toString(relaciones[i].maxCardinality);
            pasante.referencias=BusquedaReferencias(relaciones[i].name);
            if (relaciones[i].properties!=null)
              for(int j=0;j<relaciones[i].properties.length;j++)
                pasante.propiedades.addElement(relaciones[i].properties[j]);
            rel.addElement(pasante);
          }
    }
    catch (WebODEException e)
    {
      throw new Exception("An error ocurred accessing the ontology: "+e.getMessage());
    }

    return rel;
  }

  Casilla AlgoCOWOVA(String name) throws Exception
  {
    Casilla casilla=new Casilla();

    casilla=Leer(name);
    casilla.hijos=new Vector();
    try
    {
      TermRelation hijos1[];
      TermRelation hijos2[];
      TermRelation hijos3[];
      Vector hijosparciales1=new Vector();
      Vector hijosparciales2=new Vector();
      Vector hijosparciales3=new Vector();

      hijos1=ServicioODE.getTermRelations(Util,TermRelation.SUBCLASS_OF);
      hijos2=ServicioODE.getTermRelations(Util,TermRelation.DISJOINT);
      hijos3=ServicioODE.getTermRelations(Util,TermRelation.EXHAUSTIVE);

      if (hijos1!=null)
        for(int i=0;i<hijos1.length;i++)
        {
          if (hijos1[i].destination.compareTo(name)==0)
          {
          profundidad++;
          casilla.hijos.addElement(AlgoCOWOVA(hijos1[i].origin));
          profundidad--;
        }
        }

        if (hijos2!=null)
          for(int i=0;i<hijos2.length;i++)
          {
            if (hijos2[i].destination.compareTo(name)==0)
            {
            Group grupo=ServicioODE.getGroup(Util,hijos2[i].origin);
            profundidad++;
            for(int k=0;k<grupo.concepts.length;k++)
              casilla.hijos.addElement(AlgoCOWOVA(grupo.concepts[k]));
            profundidad--;
          }
          }

          if (hijos3!=null)
            for(int i=0;i<hijos3.length;i++)
              if (hijos3[i].destination.compareTo(name)==0)
              {
              Group grupo=ServicioODE.getGroup(Util,hijos3[i].origin);
              profundidad++;
              for(int k=0;k<grupo.concepts.length;k++)
                casilla.hijos.addElement(AlgoCOWOVA(grupo.concepts[k]));
              profundidad--;
            }
    }
    catch (WebODEException e)
    {
      throw new Exception("An error ocurred accessing the ontology: "+e.getMessage());
    }
    return casilla;
  }

  Vector PreAlgoCOWOVA(String name) throws Exception
  {
    Vector Supervector=new Vector();
    Util=name.toString();
    try
    {
      int[] tipos=new int[1];
      tipos[0]=TermTypes.CONCEPT;
      Term[] terminos=ServicioODE.getTerms(name,tipos);
      String[] raices=new String[terminos.length];
      int indiceraices=0;
      TermRelation[] subclases=ServicioODE.getTermRelations(name,true);
      Group[] grupos=ServicioODE.getGroups(Util);
      for(int i=0;i<terminos.length;i++)
      {
        int j=0;
        boolean esraiz=true;
        if (subclases!=null)
          while (esraiz && j<subclases.length)
          {
            if (subclases[j].origin.compareTo(terminos[i].term)==0)
              esraiz=false;
            j++;
          }
          j=0;
          if (grupos!=null)
            while (esraiz && j<grupos.length)
            {
              for(int k=0;k<grupos[j].concepts.length;k++)
                if (grupos[j].concepts[k].compareTo(terminos[i].term)==0)
                  esraiz=false;
              j++;
            }
            if (esraiz)
            {
              raices[indiceraices]=terminos[i].term;
              indiceraices++;
            }
      }
      out.print("Roots: ");
      for(int i=0;i<indiceraices;i++)
        out.print(raices[i]+",");
      out.println();
      for(int i=0;i<indiceraices;i++)
        Supervector.addElement(AlgoCOWOVA(raices[i]));
    }
    catch (WebODEException e)
    {
      throw new Exception("Error searching roots: "+e.getMessage());
    }
    catch (Exception e) {
      System.out.println(e.toString());
      e.printStackTrace(System.out);
      throw e;
    }
    return  Supervector;
  }

  void AlgoCOVAWO(Vector ontology, String o_name) throws Exception
  {
    Casilla concept=new Casilla();

    for(int i=0;i<ontology.size();i++)
    {
      concept=(Casilla)ontology.get(i);
      try
      {
        if (ServicioODE.getTerm(o_name,concept.nombre)==null)
            {           out.println("Writing concept "+concept.nombre);
        ServicioODE.insertTerm(o_name,concept.nombre,concept.descripcion,TermTypes.CONCEPT);
        }
      }
      catch (WebODEException e)
      {
        throw new Exception("Error inserting concept: "+e.getMessage());
      }
      if (concept.catributos!=null && !concept.catributos.isEmpty())
        EscribirCAtributos(o_name,concept.nombre,concept.catributos);
      if (concept.iatributos!=null && !concept.iatributos.isEmpty())
        EscribirIAtributos(o_name,concept.nombre,concept.iatributos);
      if (concept.relaciones!=null && !concept.relaciones.isEmpty())
        EscribirRelaciones(o_name,concept.nombre,concept.relaciones);
      if (concept.hijos!=null && !concept.hijos.isEmpty())
      {
        AlgoCOVAWO(concept.hijos,o_name);
        for(int j=0;j<concept.hijos.size();j++)
        {
          Casilla hijo=(Casilla)concept.hijos.get(j);
          TermRelation hijode=new TermRelation(o_name,null,TermRelation.SUBCLASS_OF,hijo.nombre,concept.nombre);
          try
          {
            TermRelation[] Existentes=ServicioODE.getTermRelations(o_name,true);
            boolean yaeshijo=false;
            int k=0;
            if (Existentes!=null)
              while (!yaeshijo && k<Existentes.length)
              {
                if (Existentes[k].origin.compareTo(hijo.nombre)==0 && Existentes[k].destination.compareTo(concept.nombre)==0)
                  yaeshijo=false;
                k++;
              }
              if (!yaeshijo)
                ServicioODE.insertTermRelation(hijode);
          }
          catch (WebODEException e)
          {
            throw new Exception("Error inserting subclass relation: "+e.getMessage());
          }
        }
      }
    }
  }

  void EscribirCAtributos(String o_name,String c_name,Vector attributes) throws Exception
  {
    Atributo attribute;
    for(int i=0;i<attributes.size();i++)
    {
      attribute=(Atributo)attributes.get(i);
      ClassAttributeDescriptor o_attribute;
      if (attribute.valor!=null && attribute.valor.size()>1)
        o_attribute=new ClassAttributeDescriptor(attribute.nombre,c_name,ValueTypes.NAMES[Integer.parseInt(attribute.tipo)],attribute.unidad,attribute.precision,Integer.parseInt(attribute.card_minima),Integer.parseInt(attribute.card_maxima),attribute.descripcion);
      else if (attribute.valor!=null && attribute.valor.size()==1)
        o_attribute=new ClassAttributeDescriptor(attribute.nombre,c_name,ValueTypes.NAMES[Integer.parseInt(attribute.tipo)],attribute.unidad,attribute.precision,Integer.parseInt(attribute.card_minima),Integer.parseInt(attribute.card_maxima),attribute.descripcion);
      else
        o_attribute=new ClassAttributeDescriptor(attribute.nombre,c_name,ValueTypes.NAMES[Integer.parseInt(attribute.tipo)],attribute.unidad,attribute.precision,Integer.parseInt(attribute.card_minima),Integer.parseInt(attribute.card_maxima),attribute.descripcion);
      out.print("Attribute "+i+"de"+attributes.size()+": "+attribute.nombre);
      try
      {
        out.println(" ("+o_name+","+attribute.nombre+","+c_name+")");
        ClassAttributeDescriptor[] todos=ServicioODE.getClassAttributes(o_name,c_name);
        for(int j=0;j<todos.length;j++)
          out.print(todos[j].name);
        out.println();
        if (ServicioODE.getClassAttribute(o_name,attribute.nombre,c_name)==null)
            {           out.println("Writing cattribute "+attribute.nombre);
        ServicioODE.insertClassAttribute(o_name,o_attribute);
        }
      }
      catch (WebODEException e)
      {
        throw new Exception("Error inserting attribute: "+e.getMessage());
      }
      if (attribute.sinonimos!=null && !attribute.sinonimos.isEmpty())
        for(int j=0;j<attribute.sinonimos.size();j++)
        {
          try
          {
          if (ServicioODE.getSynonym(o_name,((SynonymAbbreviation)attribute.sinonimos.get(j)).relatedTerm,((SynonymAbbreviation)attribute.sinonimos.get(j)).parentTerm,((SynonymAbbreviation)attribute.abreviaturas.get(j)).name)==null)
            ServicioODE.insertTerm(o_name,attribute.nombre,attribute.descripcion,TermTypes.SYNONYM);
        }
        catch (WebODEException e)
        {
          throw new Exception("Error inserting synonym: "+e.getMessage());
        }
        }
        if (attribute.abreviaturas!=null && !attribute.abreviaturas.isEmpty())
          for(int j=0;j<attribute.abreviaturas.size();j++)
          {
            try
            {
            if (ServicioODE.getAbbreviation(o_name,((SynonymAbbreviation)attribute.abreviaturas.get(j)).relatedTerm,((SynonymAbbreviation)attribute.abreviaturas.get(j)).parentTerm,((SynonymAbbreviation)attribute.abreviaturas.get(j)).name)==null)
              ServicioODE.insertTerm(o_name,attribute.nombre,attribute.descripcion,TermTypes.ABBREVIATION);
          }
          catch (WebODEException e)
          {
            throw new Exception("Error inserting abbreviation: "+e.getMessage());
          }
          }
          if (attribute.formulas!=null && !attribute.formulas.isEmpty())
            for(int j=0;j<attribute.formulas.size();j++)
            {
              try
              {
              FormulaDescriptor formula=(FormulaDescriptor)attribute.formulas.get(j);
              if ((ServicioODE.getReasoningElement(o_name,formula.name))==null)
              {
                ServicioODE.insertReasoningElement(formula);
                ServicioODE.relateFormulaToTerm(o_name,attribute.nombre,formula.name);
              }
            }
            catch (WebODEException e)
            {
              throw new Exception("Error inserting formula: "+e.getMessage());
            }
            }
/*
      if (attribute.deducido_de!=null && !attribute.deducido_de.isEmpty())
         for(int j=0;j<attribute.deducido_de.size();j++)
         {
            try
            {
               InferenceAttribute deduccion=(InferenceAttribute)attribute.deducciones.get(j);
               ServicioODE.insertInferenceAttribute(deduccion);
               ServicioODE.relateFormulaToTerm(o_name,attribute.name,formula.name);
            }
            catch (WebODEException e)
            {
               throw new Exception("Error inserting formula: "+e.getMessage());
            }
         }
*/
            if (attribute.referencias!=null && !attribute.referencias.isEmpty())
              for(int j=0;j<attribute.referencias.size();j++)
              {
                try
                {
                ReferenceDescriptor referencia=(ReferenceDescriptor)attribute.referencias.get(j);
                if ((ServicioODE.getReference(o_name,referencia.name))==null)
                {
                  ServicioODE.addReferenceToOntology(o_name,referencia);
                  ServicioODE.relateFormulaToTerm(o_name,attribute.nombre,referencia.name);
                }
              }
              catch (WebODEException e)
              {
                throw new Exception("Error inserting reference: "+e.getMessage());
              }
              }
    }
  }

  void EscribirIAtributos(String o_name,String c_name,Vector attributes) throws Exception
  {
    Atributo attribute;
    for(int i=0;i<attributes.size();i++)
    {
      attribute=(Atributo)attributes.get(i);
      InstanceAttributeDescriptor o_attribute;
      if (attribute.valor!=null && attribute.valor.size()>1)
        o_attribute=new InstanceAttributeDescriptor(attribute.nombre,c_name,ValueTypes.NAMES[Integer.parseInt(attribute.tipo)],attribute.unidad,attribute.precision,Integer.parseInt(attribute.card_minima),Integer.parseInt(attribute.card_maxima),attribute.descripcion,(String)attribute.valor.get(0),(String)attribute.valor.get(1));
      else if (attribute.valor!=null && attribute.valor.size()==1)
        o_attribute=new InstanceAttributeDescriptor(attribute.nombre,c_name,ValueTypes.NAMES[Integer.parseInt(attribute.tipo)],attribute.unidad,attribute.precision,Integer.parseInt(attribute.card_minima),Integer.parseInt(attribute.card_maxima),attribute.descripcion,(String)attribute.valor.get(0),"");
      else
        o_attribute=new InstanceAttributeDescriptor(attribute.nombre,c_name,ValueTypes.NAMES[Integer.parseInt(attribute.tipo)],attribute.unidad,attribute.precision,Integer.parseInt(attribute.card_minima),Integer.parseInt(attribute.card_maxima),attribute.descripcion,"","");
      out.print("Attribute "+i+"de"+attributes.size()+": "+attribute.nombre);
      try
      {
        out.println(" ("+o_name+","+attribute.nombre+","+c_name+")");
        InstanceAttributeDescriptor[] todos=ServicioODE.getInstanceAttributes(o_name,c_name);
        for(int j=0;j<todos.length;j++)
          out.print(todos[j].name);
        out.println();
        if (ServicioODE.getInstanceAttribute(o_name,attribute.nombre,c_name)==null)
            {           out.println("Writing iattribute "+attribute.nombre);
        ServicioODE.insertInstanceAttribute(o_name,o_attribute);
        }
      }
      catch (WebODEException e)
      {
        throw new Exception("Error inserting attribute: "+e.getMessage());
      }
      if (attribute.sinonimos!=null && !attribute.sinonimos.isEmpty())
        for(int j=0;j<attribute.sinonimos.size();j++)
        {
          try
          {
          if (ServicioODE.getSynonym(o_name,((SynonymAbbreviation)attribute.sinonimos.get(j)).relatedTerm,((SynonymAbbreviation)attribute.sinonimos.get(j)).parentTerm,((SynonymAbbreviation)attribute.abreviaturas.get(j)).name)==null)
            ServicioODE.insertTerm(o_name,attribute.nombre,attribute.descripcion,TermTypes.SYNONYM);
        }
        catch (WebODEException e)
        {
          throw new Exception("Error inserting synonym: "+e.getMessage());
        }
        }
        if (attribute.abreviaturas!=null && !attribute.abreviaturas.isEmpty())
          for(int j=0;j<attribute.abreviaturas.size();j++)
          {
            try
            {
            if (ServicioODE.getAbbreviation(o_name,((SynonymAbbreviation)attribute.abreviaturas.get(j)).relatedTerm,((SynonymAbbreviation)attribute.abreviaturas.get(j)).parentTerm,((SynonymAbbreviation)attribute.abreviaturas.get(j)).name)==null)
              ServicioODE.insertTerm(o_name,attribute.nombre,attribute.descripcion,TermTypes.ABBREVIATION);
          }
          catch (WebODEException e)
          {
            throw new Exception("Error inserting abbreviation: "+e.getMessage());
          }
          }
          if (attribute.formulas!=null && !attribute.formulas.isEmpty())
            for(int j=0;j<attribute.formulas.size();j++)
            {
              try
              {
              FormulaDescriptor formula=(FormulaDescriptor)attribute.formulas.get(j);
              if ((ServicioODE.getReasoningElement(o_name,formula.name))==null)
              {
                ServicioODE.insertReasoningElement(formula);
                ServicioODE.relateFormulaToTerm(o_name,attribute.nombre,formula.name);
              }
            }
            catch (WebODEException e)
            {
              throw new Exception("Error inserting formula: "+e.getMessage());
            }
            }
/*
      if (attribute.deducido_de!=null && !attribute.deducido_de.isEmpty())
         for(int j=0;j<attribute.deducido_de.size();j++)
         {
            try
            {
               InferenceAttribute deduccion=(InferenceAttribute)attribute.deducciones.get(j);
               ServicioODE.insertInferenceAttribute(deduccion);
               ServicioODE.relateFormulaToTerm(o_name,attribute.name,formula.name);
            }
            catch (WebODEException e)
            {
               throw new Exception("Error inserting formula: "+e.getMessage());
            }
         }
*/
            if (attribute.referencias!=null && !attribute.referencias.isEmpty())
              for(int j=0;j<attribute.referencias.size();j++)
              {
                try
                {
                ReferenceDescriptor referencia=(ReferenceDescriptor)attribute.referencias.get(j);
                if ((ServicioODE.getReference(o_name,referencia.name))==null)
                {
                  ServicioODE.addReferenceToOntology(o_name,referencia);
                  ServicioODE.relateFormulaToTerm(o_name,attribute.nombre,referencia.name);
                }
              }
              catch (WebODEException e)
              {
                throw new Exception("Error inserting reference: "+e.getMessage());
              }
              }
    }
  }

  void EscribirRelaciones(String o_name,String c_name,Vector relaciones)
  {
    Relacion relacion;
    for(int i=0;i<relaciones.size();i++)
    {
      relacion=(Relacion)relaciones.get(i);
      out.println("Writing relation "+relacion.nombre);
      TermRelation o_relacion=new TermRelation(o_name,null,relacion.nombre,relacion.origen,relacion.destino);
      adhocs.addElement(o_relacion);
    }
  }

  void AñadirAdHocs() throws Exception
  {
    if (adhocs!=null && !adhocs.isEmpty())
      for(int i=0;i<adhocs.size();i++)
      {
        out.println("Add ad hocs");
        try
        {
          if (ServicioODE.getTermRelations(((TermRelation)adhocs.get(i)).name,false)!=null)
          {
            ServicioODE.insertTermRelation((TermRelation)adhocs.get(i));
          }
        }
        catch (WebODEException e)
        {
          throw new Exception("Error inserting relations: "+e.getMessage());
        }
      }
  }

  public Vector Lectura(String ontologia) throws Exception
  {
    MinervaSession minervaSession;
    try
    {
      minervaSession=MinervaClient.getMinervaSession(new MinervaURL (minervaURL),username,password);
      ServicioODE=(ODEService) minervaSession.getService("ode");
    }
    catch (Exception e)
    {
      throw new Exception("Error contactando con Minerva - an error ocurred contacting Minerva Server: "+e.getMessage());
    }

    Vector tax=new Vector();
    tax=PreAlgoCOWOVA(ontologia);
    out.println();
    out.println("Tax con atributos:");
    out.println("------------------");
    Casilla.ImprimirConAtributos(tax,out);
    out.println("Tax con relaciones:");
    out.println("-------------------");
    Casilla.ImprimirConRelaciones(tax,out);
    out.println("Tax con atributos y relaciones:");
    out.println("-------------------------------");
    Casilla.ImprimirConTodo(tax,out);
    out.println("");

    try
    {
      ServicioODE.disconnect();
      minervaSession.disconnect();
    }
    catch (Exception e)
    {
      throw new Exception("Error descontactando con Minerva - an error ocurred contacting Minerva Server: "+e.getMessage());
    }
    return tax;
  }

  public void Escritura(Vector ontologia, String o_nombre, String usuario, String grupo) throws Exception
  {
    MinervaSession minervaSession;
    try
    {
      minervaSession=MinervaClient.getMinervaSession(new MinervaURL (minervaURL),username,password);
      ServicioODE=(ODEService) minervaSession.getService("ode");
    }
    catch (Exception e)
    {
      throw new Exception("An error ocurred contacting Minerva Server: "+e.getMessage());
    }

    try
    {
      Date fecha=Calendar.getInstance().getTime();
      //Date fecha=new Date(2002,1,1);
      OntologyDescriptor od=new OntologyDescriptor(o_nombre,"Mezcla",usuario,grupo,fecha,fecha);
      ServicioODE.createOntology(od);
    }
    catch (WebODEException e)
    {
      throw new Exception("Error creando ontologia mezcla - an error ocurred creating final ontology: "+e.getMessage());
    }

    AlgoCOVAWO(ontologia,o_nombre);
    AñadirAdHocs();

    try
    {
      ServicioODE.disconnect();
      minervaSession.disconnect();
    }
    catch (Exception e)
    {
      throw new Exception("an error occurred while contacting Minerva Server: "+e.getMessage());
    }
  }

  public Vector Read(String ontology, ODEService os) throws Exception
  {
    ServicioODE=os;
    Vector tax=new Vector();
    tax=PreAlgoCOWOVA(ontology);
    out.println();
    out.println("Taxonomy with attributes:");
    out.println("-------------------------");
    Casilla.ImprimirConAtributos(tax,out);
    out.println("Taxonomy with relations:");
    out.println("------------------------");
    Casilla.ImprimirConRelaciones(tax,out);
    out.println("Taxonomy with attributes and relations:");
    out.println("---------------------------------------");
    Casilla.ImprimirConTodo(tax,out);
    out.println("");
    return tax;
  }

  public void Write(Vector ontology, String o_name, ODEService os, String user, String group) throws Exception
  {
    ServicioODE=os;
    try
    {
      Date fecha=new Date(System.currentTimeMillis());
      out.println("Usuario: "+user+" Grupo: "+group+" Fecha: "+fecha);
      OntologyDescriptor od=new OntologyDescriptor(o_name,"Merge process result",user,group,fecha,fecha);
      ServicioODE.createOntology(od);
    }
    catch (WebODEException e)
    {
      throw new Exception("An error occurred when creating the final ontology: "+e.getMessage());
    }

    AlgoCOVAWO(ontology,o_name);
    AñadirAdHocs();
  }
}