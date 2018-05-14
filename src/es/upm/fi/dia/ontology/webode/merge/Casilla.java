/*****************************************/
/* Casilla class *************************/
/*****************************************/
/* Author: J.A.R.G. **********************/
/*****************************************/

package es.upm.fi.dia.ontology.webode.merge;
import java.util.*;
import java.io.*;

public class Casilla
{
   String nombre;
   String descripcion;
   int profundidad;
   int proviene=0;
   boolean visto=false;
   Vector hijos;
   Vector catributos;
   Vector iatributos;
   Vector relaciones;

public Casilla()
{
	super();
}

static public void ImprimirCasilla(Vector vec, PrintWriter sal)
{
   int i=0;
   Casilla u=new Casilla();

   for(i=0;i<vec.size();i++)
   {
      u=(Casilla)vec.get(i);
      int j=0;
      for(j=0;j<u.profundidad;j++)
         sal.print("  ");
      sal.println(u.nombre);
      if (u.hijos!=null && !u.hijos.isEmpty())
         ImprimirCasilla(u.hijos,sal);
   }
}

static public void ImprimirConProcedencia(Vector vec, PrintWriter sal)
{
   int i=0;
   Casilla u=new Casilla();

   for(i=0;i<vec.size();i++)
   {
      u=(Casilla)vec.get(i);
      for(int j=0;j<u.profundidad;j++)
         sal.print("  ");
      sal.println(u.nombre+" ("+u.proviene+")");
      if (u.hijos!=null && !u.hijos.isEmpty())
         ImprimirConProcedencia(u.hijos,sal);
   }
}

static public void ImprimirConAtributos(Vector vec, PrintWriter sal)
{
   int i=0;
   Casilla u=new Casilla();

   for(i=0;i<vec.size();i++)
   {
      u=(Casilla)vec.get(i);
      for(int j=0;j<u.profundidad;j++)
         sal.print("  ");
      sal.print(u.nombre+" (");
      if (u.catributos!=null && !u.catributos.isEmpty())
         for(int k=0;k<u.catributos.size();k++)
            sal.print(((Atributo)u.catributos.get(k)).nombre+"-"+((Atributo)u.catributos.get(k)).tipo+" ");
      sal.println(")");
      if (u.iatributos!=null && !u.iatributos.isEmpty())
         for(int k=0;k<u.iatributos.size();k++)
            sal.print(((Atributo)u.iatributos.get(k)).nombre+"-"+((Atributo)u.iatributos.get(k)).tipo+" ");
      sal.println(")");
      if (u.hijos!=null && !u.hijos.isEmpty())
         ImprimirConAtributos(u.hijos,sal);
   }
}

static public void ImprimirConRelaciones(Vector vec, PrintWriter sal)
{
   int i=0;
   Casilla u=new Casilla();

   for(i=0;i<vec.size();i++)
   {
      u=(Casilla)vec.get(i);
      for(int j=0;j<u.profundidad;j++)
         sal.print("  ");
      sal.print(u.nombre+" [");
      if (u.relaciones!=null && !u.relaciones.isEmpty())
         for(int k=0;k<u.relaciones.size();k++)
            sal.print(((Relacion)u.relaciones.get(k)).nombre+" ");
      sal.println("]");
      if (u.hijos!=null && !u.hijos.isEmpty())
         ImprimirConRelaciones(u.hijos,sal);
   }
}

static public void ImprimirConTodo(Vector vec, PrintWriter sal)
{
   int i=0;
   Casilla u=new Casilla();

   for(i=0;i<vec.size();i++)
   {
      u=(Casilla)vec.get(i);
      for(int j=0;j<u.profundidad;j++)
         sal.print("  ");
      sal.print(u.nombre);
      if (u.catributos!=null && !u.catributos.isEmpty())
      {
         sal.print(" (");
         for(int k=0;k<u.catributos.size();k++)
            sal.print(((Atributo)u.catributos.get(k)).nombre+" ");
         sal.print(")");
      }
      if (u.iatributos!=null && !u.iatributos.isEmpty())
      {
         sal.print(" (");
         for(int k=0;k<u.iatributos.size();k++)
            sal.print(((Atributo)u.iatributos.get(k)).nombre+" ");
         sal.print(")");
      }
      if (u.relaciones!=null && !u.relaciones.isEmpty())
      {
         sal.print(" [");
         for(int k=0;k<u.relaciones.size();k++)
            sal.print(((Relacion)u.relaciones.get(k)).nombre+" ");
         sal.print("]");
      }
      sal.println();
      if (u.hijos!=null && !u.hijos.isEmpty())
         ImprimirConTodo(u.hijos,sal);
   }
}

static public void ImprimirXML(Vector vector, PrintWriter fichero)
{
   ImprimirXML_Cabecera(fichero);
   ImprimirXML_Conceptos(vector,fichero);
   ImprimirXML_RelacionesSC(vector,fichero);
   ImprimirXML_RelacionesAH(vector,fichero);
   ImprimirXML_Pie(fichero);
}

static void ImprimirXML_Cabecera(PrintWriter fichero)
{
   fichero.println("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
   fichero.println("<!DOCTYPE Ontology SYSTEM 'http://babage.dia.fi.upm.es/webode/DTD/webode_1_0.dtd'>");
   fichero.println("<Ontology>");
   fichero.println("  <Name>Ontologia Mezcla</Name>");
   fichero.println("  <Description>Esta es una Ontologia para WebODE</Description>");
   fichero.println("  <Author>jramos</Author>");
   fichero.println("  <Creation-Date>Sin fecha definida</Creation-Date>");
   fichero.println("  <Conceptualization>");
}

static void ImprimirXML_Pie(PrintWriter fichero)
{
   fichero.println("  </Conceptualization>");
   fichero.println("</Ontology>");
}

static void ImprimirXML_Conceptos(Vector vector, PrintWriter fichero)
{
   int i=0;
   Casilla u=new Casilla();

   for(i=0;i<vector.size();i++)
   {
      String prof=new String();
      u=(Casilla)vector.get(i);
      for(int j=0;j<u.profundidad;j++) prof+=" ";
      fichero.println(prof+"<Concept>");
      fichero.println(prof+" <Name>"+u.nombre+"</Name>");
      if (u.catributos!=null && !u.catributos.isEmpty())
         ImprimirXML_CAtributos(u.catributos,prof,fichero);
      if (u.iatributos!=null && !u.iatributos.isEmpty())
         ImprimirXML_IAtributos(u.iatributos,prof,fichero);
      fichero.println(prof+"</Concept>");
      if (u.hijos!=null && !u.hijos.isEmpty())
         ImprimirXML_Conceptos(u.hijos, fichero);
   }
}

static void ImprimirXML_CAtributos(Vector vector, String prof, PrintWriter fichero)
{
   int i=0;
   Atributo atrib=new Atributo();

   for(i=0;i<vector.size();i++)
   {
      atrib=(Atributo)vector.get(i);
      fichero.println(prof+"<Class-Attribute>");
      fichero.println(prof+" <Name>"+atrib.nombre+"</Name>");
      if (atrib.descripcion==null)
         fichero.println(prof+" <Description>"+atrib.nombre+"</Description>");
      fichero.println(prof+" <Type>"+atrib.tipo+"</Type>");
      fichero.println(prof+" <Minimum-Cardinality>"+atrib.card_minima+"</Minimum-Cardinality>");
      fichero.println(prof+" <Maximim-Cardinality>"+atrib.card_maxima+"</Maximum-Cardinality>");
      if (atrib.unidad==null)
         fichero.println(prof+" <>"+atrib.unidad+"</>");
      if (atrib.precision==null)
         fichero.println(prof+" <>"+atrib.precision+"</>");
      if (!atrib.valor.isEmpty())
         for(int j=0;j<atrib.valor.size();j++)
            fichero.println(prof+" <Value>"+(String)atrib.valor.get(i)+"</Value>");
      if (!atrib.referencias.isEmpty())
         for(int j=0;j<atrib.referencias.size();j++)
            fichero.println(prof+" <Related-Reference>"+(String)atrib.referencias.get(i)+"</Related-Reference>");
      if (!atrib.sinonimos.isEmpty())
         for(int j=0;j<atrib.sinonimos.size();j++)
            fichero.println(prof+" <Synonym>"+(String)atrib.sinonimos.get(i)+"</Synonym>");
      if (!atrib.abreviaturas.isEmpty())
         for(int j=0;j<atrib.abreviaturas.size();j++)
            fichero.println(prof+" <Abbreviation>"+(String)atrib.abreviaturas.get(i)+"</Abbreviation>");
      if (!atrib.formulas.isEmpty())
         for(int j=0;j<atrib.formulas.size();j++)
            fichero.println(prof+" <Related-Formula>"+(String)atrib.formulas.get(i)+"</Related-Formula>");
      if (!atrib.deducido_de.isEmpty())
         for(int j=0;j<atrib.deducido_de.size();j++)
            fichero.println(prof+" <Inferred>"+(String)atrib.deducido_de.get(i)+"</Inferred>");
      fichero.println(prof+"</Class-Attribute>");
   }
}

static void ImprimirXML_IAtributos(Vector vector, String prof, PrintWriter fichero)
{
   int i=0;
   Atributo atrib=new Atributo();

   for(i=0;i<vector.size();i++)
   {
      atrib=(Atributo)vector.get(i);
      fichero.println(prof+"<Instance-Attribute>");
      fichero.println(prof+" <Name>"+atrib.nombre+"</Name>");
      if (atrib.descripcion==null)
         fichero.println(prof+" <Description>"+atrib.nombre+"</Description>");
      fichero.println(prof+" <Type>"+atrib.tipo+"</Type>");
      fichero.println(prof+" <Minimum-Cardinality>"+atrib.card_minima+"</Minimum-Cardinality>");
      fichero.println(prof+" <Maximim-Cardinality>"+atrib.card_maxima+"</Maximum-Cardinality>");
      if (atrib.unidad==null)
         fichero.println(prof+" <>"+atrib.unidad+"</>");
      if (atrib.precision==null)
         fichero.println(prof+" <>"+atrib.precision+"</>");
      if (!atrib.valor.isEmpty())
         for(int j=0;j<atrib.valor.size();j++)
            fichero.println(prof+" <Value>"+(String)atrib.valor.get(i)+"</Value>");
      if (!atrib.referencias.isEmpty())
         for(int j=0;j<atrib.referencias.size();j++)
            fichero.println(prof+" <Related-Reference>"+(String)atrib.referencias.get(i)+"</Related-Reference>");
      if (!atrib.sinonimos.isEmpty())
         for(int j=0;j<atrib.sinonimos.size();j++)
            fichero.println(prof+" <Synonym>"+(String)atrib.sinonimos.get(i)+"</Synonym>");
      if (!atrib.abreviaturas.isEmpty())
         for(int j=0;j<atrib.abreviaturas.size();j++)
            fichero.println(prof+" <Abbreviation>"+(String)atrib.abreviaturas.get(i)+"</Abbreviation>");
      if (!atrib.formulas.isEmpty())
         for(int j=0;j<atrib.formulas.size();j++)
            fichero.println(prof+" <Related-Formula>"+(String)atrib.formulas.get(i)+"</Related-Formula>");
      if (!atrib.deducido_de.isEmpty())
         for(int j=0;j<atrib.deducido_de.size();j++)
            fichero.println(prof+" <Inferred>"+(String)atrib.deducido_de.get(i)+"</Inferred>");
      fichero.println(prof+"</Instance-Attribute>");
   }
}

static void ImprimirXML_RelacionesSC(Vector vector, PrintWriter fichero)
{
   int i=0;
   String prof=new String();
   Casilla u=new Casilla();

   for(i=0;i<vector.size();i++)
   {
      u=(Casilla)vector.get(i);
      if (u.hijos!=null && !u.hijos.isEmpty())
         ImprimirXML_RelacionesSC2(u.hijos, u.nombre, fichero);
   }
}

static void ImprimirXML_RelacionesSC2(Vector vector, String padre, PrintWriter fichero)
{
   int i=0;
   String prof=new String();
   Casilla u=new Casilla();

   for(i=0;i<vector.size();i++)
   {
      u=(Casilla)vector.get(i);
      fichero.println("<Term-Relation>");
      fichero.println(" <Name>Subclass-of</Name>");
      fichero.println(" <Origin>"+u.nombre+"</Origin>");
      fichero.println(" <Destination>"+padre+"</Destination>");
      fichero.println(" <Maximum-Cardinality>-1</Maximum-Cardinality>");
      fichero.println("</Term-Relation>");
      if (u.hijos!=null && !u.hijos.isEmpty())
         ImprimirXML_RelacionesSC2(u.hijos, u.nombre, fichero);
   }
}

static void ImprimirXML_RelacionesAH(Vector vector, PrintWriter fichero)
{
   int i=0;
   String prof=new String();
   Casilla u=new Casilla();

   for(i=0;i<vector.size();i++)
   {
      u=(Casilla)vector.get(i);
      if (u.relaciones!=null && !u.relaciones.isEmpty())
         ImprimirXML_Relaciones(u.relaciones,fichero);
   }
}

static void ImprimirXML_Relaciones(Vector vector, PrintWriter fichero)
{
   int i=0;
   Relacion rel=new Relacion();

   for(i=0;i<vector.size();i++)
   {
      rel=(Relacion)vector.get(i);
      fichero.println("<Term-Relation>");
      fichero.println(" <Name>"+rel.nombre+"</Name>");
      if (rel.descripcion==null)
         fichero.println(" <Description>"+rel.nombre+"</Description>");
      fichero.println(" <Origin>"+rel.origen+"</Origin>");
      fichero.println(" <Destination>"+rel.destino+"</Destination>");
      fichero.println(" <Maximum-Cardinality>"+rel.card_maxima+"</Maximum-Cardinality>");
      if (!rel.referencias.isEmpty())
         for(int j=0;j<rel.referencias.size();j++)
            fichero.println(" <Related-Reference>"+(String)(rel.referencias).get(i)+"</Related-Reference>");
      if (!rel.propiedades.isEmpty())
         for(int j=0;j<rel.propiedades.size();j++)
            fichero.println(" <Related-Property>"+(String)(rel.propiedades).get(i)+"</Related-Property>");
      fichero.println("</Term-Relation>");
   }
}

}
