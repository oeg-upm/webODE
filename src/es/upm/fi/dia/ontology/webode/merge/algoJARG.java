/*****************************************/
/* JARG Algorithm ************************/
/*****************************************/
/* Author: J.A.R.G. **********************/
/*****************************************/

package es.upm.fi.dia.ontology.webode.merge;

import java.io.*;
import java.util.*;
import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.client.*;

public class algoJARG
{

PrintWriter out;
Vector tax1=new Vector();
Vector tax2=new Vector();
Vector tax3=new Vector();
Vector tax3_final=new Vector();
Vector igualdades=new Vector();
Vector inclusiones=new Vector();
int profundidad1=0;
int profundidad2=0;
int profundidad3=0;
int nivelmezcla=0;
protected LineNumberReader entrada;
protected LineNumberReader entrada2;
protected PrintWriter salida;

public algoJARG(String nombreFichero)
{
   super();
   try{
   	FileOutputStream ficheroSalida = new FileOutputStream(nombreFichero);
   	out=new PrintWriter(ficheroSalida,true);
   }catch (FileNotFoundException e){
   	System.out.println("Error al crear fichero: " + nombreFichero + "  " + e.getMessage());
   };

}

public algoJARG(Writer sal)
{
   super();
   out=new PrintWriter(sal);
}

public algoJARG(PrintStream sal)
{
   super();
   out=new PrintWriter(sal,true);
}

Casilla LeerNuevo(Vector nivel)
{
   Casilla termino=new Casilla();
   boolean encontrado=false;
   int i=0;

   while (i<nivel.size() && encontrado==false)
   {
      termino=(Casilla)nivel.get(i);
      encontrado=!termino.visto;
      i++;
   }
   return termino;
}

boolean YaIncluido(String nombre)
{
   Casilla tno=new Casilla();
   Vector estoy=new Vector();
   boolean incluido=false;
   int i=0;
   while (!incluido && i<tax3.size())
   {
      estoy=(Vector)tax3.get(i);
      int ultimo=estoy.size()-1;
      tno=(Casilla)estoy.get(ultimo);
      incluido=(tno.nombre.compareTo(nombre)==0);
      i++;
   }
   return incluido;
}

void SubirNivel3(int n)
{
   int tamano=((Vector)tax3.get(n-1)).size();
   if (tamano!=0)
   {
      Vector descendientes=new Vector();
      descendientes=(Vector)tax3.remove(n);
      ((Casilla)((Vector)tax3.get(n-1)).get(tamano-1)).hijos=new Vector();
      ((Casilla)((Vector)tax3.get(n-1)).get(tamano-1)).hijos=(Vector)descendientes.clone();
   }
}

Vector Heredar(Vector hijos, int proc, int prof)
{
   Casilla u=new Casilla();

   for(int i=0;i<hijos.size();i++)
   {
      u=(Casilla)hijos.get(i);
      u.proviene=proc;
      u.profundidad=u.profundidad+prof;
      if (u.hijos!=null && !u.hijos.isEmpty())
         Heredar(u.hijos, proc, prof);
   }
   return hijos;
}

void Anadir(Casilla tno, int proc)
{
   if (YaIncluido(tno.nombre))
      return;
   Casilla aanadir=new Casilla();
   aanadir.nombre=tno.nombre;
   aanadir.proviene=proc;
   aanadir.profundidad=profundidad3;
   if (tno.catributos!=null && !tno.catributos.isEmpty())
      aanadir.catributos=(Vector)tno.catributos.clone();
   if (tno.iatributos!=null && !tno.iatributos.isEmpty())
      aanadir.iatributos=(Vector)tno.iatributos.clone();
   if (tno.relaciones!=null && !tno.relaciones.isEmpty())
      aanadir.relaciones=(Vector)tno.relaciones.clone();
   if (profundidad3>(tax3.size()-1))
   {
      Vector nuevo=new Vector();
      nuevo.addElement(aanadir);
      tax3.addElement(nuevo);
   }
   else if (profundidad3==(tax3.size()-1))
      ((Vector)tax3.get(profundidad3)).addElement(aanadir);
   else
   {
      int i=0;
      for (i=(tax3.size()-1);i>aanadir.profundidad;i--)
         SubirNivel3(i);
      if (tno.nombre!="")
         ((Vector)tax3.get(profundidad3)).addElement(aanadir);
   }
out.println("Añadido: "+aanadir.nombre);
}

boolean RelacionDirecta(String nombre1, String nombre2)
{
   boolean encontrado=false;
   Vector lista=new Vector();
   int fila=0;
   if (Iguales(nombre1,nombre2))
      encontrado=true;
   while (!encontrado && fila<igualdades.size())
   {
      lista=(Vector)igualdades.get(fila);
      boolean encontrado2=false;
      boolean encontrado3=false;
      int i=0;
      while (!encontrado3 && i<lista.size())
      {
         String palabra=(String)lista.get(i);
         if (nombre1.compareTo(palabra)==0 || nombre2.compareTo(palabra)==0)
            if (!encontrado2)
               encontrado2=true;
            else /* ya encontrada una */
               encontrado3=true;
         i++;
      }
      encontrado=(encontrado2 && encontrado3);
      fila++;
   }
if (encontrado) out.println("RD: "+nombre1+","+nombre2);
   return encontrado;
}

boolean Iguales(String palabra1, String palabra2)
{
   return (palabra1.compareTo(palabra2)==0);
}

boolean InclusionDirecta(String nombre1, String nombre2)
{
   boolean encontrado=false;
   boolean encontrado2=false;
   Vector pareja=new Vector();
   Vector lista=new Vector();
   int fila=0;
   while (!encontrado && fila<inclusiones.size())
   {
      pareja=(Vector)inclusiones.get(fila);
      String incluyente=(String)pareja.get(0);
      if (nombre1.compareTo(incluyente)==0)
      {
         encontrado=true;
         lista=(Vector)pareja.get(1);
         encontrado2=false;
         int i=0;
         while (!encontrado2 && i<lista.size())
         {
            String palabra=(String)lista.get(i);
            if (nombre2.compareTo(palabra)==0)
               encontrado2=true;
            i++;
         }
      }
      fila++;
   }
if (encontrado && encontrado2) out.println("ID: "+nombre1+","+nombre2);
   return (encontrado && encontrado2);
}

boolean InclusionInversa(String nombre1, String nombre2)
{
   boolean encontrado=false;
   boolean encontrado2=false;
   Vector pareja=new Vector();
   Vector lista=new Vector();
   int fila=0;
   while (!encontrado && fila<inclusiones.size())
   {
      pareja=(Vector)inclusiones.get(fila);
      String incluyente=(String)pareja.get(0);
      if (nombre2.compareTo(incluyente)==0)
      {
         encontrado=true;
         lista=(Vector)pareja.get(1);
         encontrado2=false;
         int i=0;
         while (!encontrado2 && i<lista.size())
         {
            String palabra=(String)lista.get(i);
            if (nombre1.compareTo(palabra)==0)
               encontrado2=true;
            i++;
         }
      }
      fila++;
   }
if (encontrado && encontrado2) out.println("II: "+nombre1+","+nombre2);
   return (encontrado && encontrado2);
}

boolean CompararRelacion1(Casilla tno1, Casilla tno2)
{
   boolean RD=RelacionDirecta(tno1.nombre, tno2.nombre);
   boolean hoja1=(tno1.hijos==null || tno1.hijos.isEmpty());
   boolean hoja2=(tno2.hijos==null || tno2.hijos.isEmpty());

   if (RD && hoja1 && hoja2)
   {
      if (nivelmezcla==2 || nivelmezcla==4)
      {
         tno1.catributos=MezclarAtributos(tno1.catributos,tno2.catributos);
         tno1.iatributos=MezclarAtributos(tno1.iatributos,tno2.iatributos);
      }
      if (nivelmezcla==3 || nivelmezcla==4)
         tno1.relaciones=MezclarRelaciones(tno1.relaciones,tno2.relaciones);
      Anadir(tno1,1);
      tno1.visto=true;
      tno2.visto=true;
      return true;
   }
   if (RD && hoja1 && !hoja2)
   {
      if (nivelmezcla==2 || nivelmezcla==4)
      {
         tno2.catributos=MezclarAtributos(tno1.catributos,tno2.catributos);
         tno2.iatributos=MezclarAtributos(tno1.iatributos,tno2.iatributos);
      }
      if (nivelmezcla==3 || nivelmezcla==4)
         tno2.relaciones=MezclarRelaciones(tno1.relaciones,tno2.relaciones);
      AnadirConHijos(tno2,2);
      tno1.visto=true;
      tno2.visto=true;
      return true;
   }
   if (RD && !hoja1 && hoja2)
   {
      if (nivelmezcla==2 || nivelmezcla==4)
      {
         tno1.catributos=MezclarAtributos(tno1.catributos,tno2.catributos);
         tno1.iatributos=MezclarAtributos(tno1.iatributos,tno2.iatributos);
      }
      if (nivelmezcla==3 || nivelmezcla==4)
         tno1.relaciones=MezclarRelaciones(tno1.relaciones,tno2.relaciones);
      AnadirConHijos(tno1,1);
      tno1.visto=true;
      tno2.visto=true;
      return true;
   }
   if (RD && !hoja1 && !hoja2)
   {
      if (nivelmezcla==2 || nivelmezcla==4)
      {
         tno1.catributos=MezclarAtributos(tno1.catributos,tno2.catributos);
         tno1.iatributos=MezclarAtributos(tno1.iatributos,tno2.iatributos);
      }
      if (nivelmezcla==3 || nivelmezcla==4)
         tno1.relaciones=MezclarRelaciones(tno1.relaciones,tno2.relaciones);
      Anadir(tno1,1);
      tno1.visto=true;
      tno2.visto=true;
      profundidad1++;
      profundidad2++;
      profundidad3++;
      AlgoritmoJARGNivel(tno1.hijos, tno2.hijos);
      profundidad3--;
      profundidad2--;
      profundidad1--;
      return true;
   }
   else
      return false;
}

void CompararRelacion2(Casilla tno, int proc)
{
out.println("Termino nuevo "+tno.nombre);
   if (tno.hijos==null || tno.hijos.isEmpty())
   {
      Anadir(tno,proc);
      tno.visto=true;
   }
   else
   {
      AnadirConHijos(tno, proc);
      tno.visto=true;
   }
}

boolean CompararRelacion3(Casilla tno1, Casilla tno2, Vector nivel1, Vector nivel2)
{
   boolean id=InclusionDirecta(tno1.nombre,tno2.nombre);
   if (id && (tno1.hijos==null || tno1.hijos.isEmpty()))
   {
      Anadir(tno1,1);
      profundidad3++;
      if (!(tno2.hijos==null || tno2.hijos.isEmpty()))
         AnadirConHijos(tno2,2);
      else
         Anadir(tno2,2);
      tno2.visto=true;
      profundidad3--;
      SeguirComparandoID(tno1, nivel1, nivel2);
      tno1.visto=true;
      return true;
   }
   else if (id)
   {
      Anadir(tno1,1);
      profundidad1++;
      profundidad3++;
      CompararS2ID(tno2, tno1.hijos, nivel2);
      profundidad3--;
      profundidad1--;
      SeguirComparandoID(tno1, nivel1, nivel2);
      profundidad1++;
      profundidad3++;
      Enriquecer(tno1.hijos);
      profundidad3--;
      profundidad1--;
      tno1.visto=true;
      return true;
   }
   else
      return false;
}

boolean CompararRelacion4(Casilla tno1, Casilla tno2, Vector nivel1, Vector nivel2)
{
   boolean ii=InclusionInversa(tno1.nombre,tno2.nombre);
   if (ii && (tno2.hijos==null || tno2.hijos.isEmpty()))
   {
      Anadir(tno2,2);
      profundidad3++;
      if (!(tno1.hijos==null || tno1.hijos.isEmpty()))
         AnadirConHijos(tno1,1);
      else
         Anadir(tno1,1);
      tno1.visto=true;
      profundidad3--;
      SeguirComparandoII(tno1, nivel1, nivel2);
      tno2.visto=true;
      return true;
   }
   else if (ii)
   {
      Anadir(tno2,2);
      profundidad2++;
      profundidad3++;
      CompararS2II(tno1, nivel1, tno2.hijos);
      tno1.visto=true;
      profundidad3--;
      profundidad2--;
      SeguirComparandoII(tno2, nivel1, nivel2);
      profundidad1++;
      profundidad3++;
      Enriquecer(tno2.hijos);
      profundidad3--;
      profundidad1--;
      tno2.visto=true;
      return true;
   }
   else
      return false;
}

void Comparar(Casilla tno1, Vector nivel1, Vector nivel2)
{
   boolean directo=false;
   boolean incluido=false;
   boolean encontrado=false;
   Casilla tno2=new Casilla();
   int i=0;
   while (i<nivel2.size() && !encontrado)
   {
      tno2=(Casilla)nivel2.get(i);
      if (tno2.visto==false)
      {
         directo=CompararRelacion1(tno1, tno2);
         if (!directo)
            incluido=CompararRelacion3(tno1, tno2, nivel1, nivel2);
         if (!directo && !incluido)
            incluido=CompararRelacion4(tno1, tno2, nivel1, nivel2);
         if (directo || incluido)
            encontrado=true;
      }
      i++;
   }
   if (!directo && !incluido)
      CompararRelacion2(tno1,1);
}

void CompararS2ID(Casilla tno2, Vector nivel1, Vector nivel2)
{
out.println("+++CompararS2ID "+tno2.nombre);
   boolean directo=false;
   boolean incluido=false;
   boolean relacionado=false;
   Casilla tno1=new Casilla();
   for(int i=0;i<nivel1.size();i++)
   {
      tno1=(Casilla)nivel1.get(i);
out.println("Miro en S2ID: "+tno1.nombre+","+tno2.nombre);
      if (tno1.visto==false)
      {
         directo=CompararRelacion1(tno1, tno2);
         if (!directo)
            incluido=CompararRelacion3(tno1, tno2, nivel1, nivel2);
         if (!directo && !incluido)
            incluido=CompararRelacion4(tno1, tno2, nivel1, nivel2);
         if (directo || incluido)
            relacionado=true;
      }
   }
   if (!relacionado)
      CompararRelacion2(tno2,1);
out.println("---CompararS2ID "+tno2.nombre);
}

void CompararS2II(Casilla tno1, Vector nivel1, Vector nivel2)
{
out.println("+++CompararS2II "+tno1.nombre);
   boolean directo=false;
   boolean incluido=false;
   boolean relacionado=false;
   Casilla tno2=new Casilla();
   for(int i=0;i<nivel2.size();i++)
   {
      tno2=(Casilla)nivel2.get(i);
out.println("Miro en S2II: "+tno1.nombre+","+tno2.nombre);
      if (tno2.visto==false)
      {
         directo=CompararRelacion1(tno1, tno2);
         if (!directo)
            incluido=CompararRelacion3(tno1, tno2, nivel1, nivel2);
         if (!directo && !incluido)
            incluido=CompararRelacion4(tno1, tno2, nivel1, nivel2);
         if (directo || incluido)
            relacionado=true;
      }
   }
   if (!relacionado)
      CompararRelacion2(tno1,1);
out.println("---CompararS2II "+tno1.nombre);
}

void SeguirComparandoID(Casilla tno1, Vector nivel1, Vector nivel2)
{
out.println("+++SeguirComparandoID "+tno1.nombre);
   boolean directo=false;
   boolean incluido=false;
   Casilla tno2=new Casilla();
   for(int i=0;i<nivel2.size();i++)
   {
      tno2=(Casilla)nivel2.get(i);
      if (tno2.visto==false)
      {
         directo=CompararRelacion1(tno1, tno2);
         if (!directo)
            incluido=CompararRelacion3(tno1, tno2, nivel1, nivel2);
         if (!directo && !incluido)
            incluido=CompararRelacion4(tno1, tno2, nivel1, nivel2);
      }
   }
out.println("---SeguirComparandoID "+tno1.nombre);
}

void SeguirComparandoII(Casilla tno2, Vector nivel1, Vector nivel2)
{
out.println("+++SeguirComparandoII "+tno2.nombre);
   boolean directo=false;
   boolean incluido=false;
   Casilla tno1=new Casilla();
   for(int i=0;i<nivel1.size();i++)
   {
      tno1=(Casilla)nivel1.get(i);
      if (tno1.visto==false)
      {
         directo=CompararRelacion1(tno1, tno2);
         if (!directo)
            incluido=CompararRelacion3(tno1, tno2, nivel1, nivel2);
         if (!directo && !incluido)
            incluido=CompararRelacion4(tno1, tno2, nivel1, nivel2);
      }
   }
out.println("---SeguirComparandoII "+tno2.nombre);
}

Vector MezclarAtributos(Vector atributos1, Vector atributos2)
{
   for(int i=0;i<atributos2.size();i++)
      if (!Atributo.Contiene(atributos1,(Atributo)atributos2.get(i)))
{out.println("Añadiendo atributo "+((Atributo)atributos2.get(i)).nombre);
         atributos1.addElement((Atributo)atributos2.get(i));
}
   return atributos1;
}

Vector MezclarRelaciones(Vector relaciones1, Vector relaciones2)
{
   for(int i=0;i<relaciones2.size();i++)
      if (!Relacion.Contiene(relaciones1,(Relacion)relaciones2.get(i)))
         relaciones1.addElement((Atributo)relaciones2.get(i));
   return relaciones1;
}

void AnadirConHijos(Casilla tno, int proc)
{
   Casilla aanadir=new Casilla();
   Vector descendientes=new Vector();
   aanadir.nombre=tno.nombre;
   aanadir.proviene=proc;
   int prof=profundidad3-tno.profundidad;
   descendientes=(Vector)tno.hijos.clone();
   aanadir.hijos=Heredar(descendientes,proc,prof);
   aanadir.profundidad=profundidad3;
   if (tno.catributos!=null && !tno.catributos.isEmpty())
      aanadir.catributos=(Vector)tno.catributos.clone();
   if (tno.iatributos!=null && !tno.iatributos.isEmpty())
      aanadir.iatributos=(Vector)tno.iatributos.clone();
   if (tno.relaciones!=null && !tno.relaciones.isEmpty())
      aanadir.relaciones=(Vector)tno.relaciones.clone();
   if (profundidad3>(tax3.size()-1))
   {
      Vector nuevo=new Vector();
      nuevo.addElement(aanadir);
      tax3.addElement(nuevo);
   }
   else if (profundidad3==(tax3.size()-1))
      ((Vector)tax3.get(profundidad3)).addElement(aanadir);
   else
   {
      int i=0;
      for (i=(tax3.size()-1);i>aanadir.profundidad;i--)
         SubirNivel3(i);
      if (tno.nombre!="")
         ((Vector)tax3.get(profundidad3)).addElement(aanadir);
   }
out.println("Añadido: "+aanadir.nombre);
}

void Enriquecer(Vector nivel)
{
   Casilla tno=new Casilla();
   while (!NivelVisto(nivel))
   {
      tno=LeerNuevo(nivel);
      CompararRelacion2(tno,2);
   }
}

boolean NivelVisto(Vector nivel)
{
   int i=0;
   boolean visto=true;

   while (i<nivel.size() && visto==true)
   {
      if (((Casilla)nivel.get(i)).visto==false)
         visto=false;
      i++;
   }
   return visto;
}

int Vistos(Vector nivel)
{
   int i=0;
   int vistos=0;

   for (i=0;i<nivel.size();i++)
      if (((Casilla)nivel.get(i)).visto==true)
         vistos++;
   return vistos;
}

void AlgoritmoJARGNivel(Vector nivel1, Vector nivel2)
{
   Casilla tno1=new Casilla();
   while (!NivelVisto(nivel1))
   {
      tno1=LeerNuevo(nivel1);
      Comparar(tno1, nivel1, nivel2);
   }
   Enriquecer(nivel2);
   int i=0;
   for (i=(tax3.size()-1);i>profundidad3+1;i--)
      SubirNivel3(i);
}

void Completar()
{
   int i=0;
   for (i=(tax3.size()-1);i>0;i--)
      SubirNivel3(i);
}

Vector CargarTablaIgualdades(String fichero) throws Exception
{
   String palabra;
   String linea;
   StringTokenizer lineaseparada;
   Vector tabla=new Vector();

   if(fichero==null)
     return tabla;
   try
   {
      entrada=new LineNumberReader(new InputStreamReader(new FileInputStream(fichero)));
   }
   catch (IOException excepcion)
   {
      throw new Exception("Se ha producido un error al abrir la tabla de igualdades");
   }
   try
   {
      while ((linea=entrada.readLine())!=null && linea.length()>1)
      {
         lineaseparada=new StringTokenizer(linea,"\t",false);
         Vector iguales=new Vector();
         while (lineaseparada.hasMoreTokens())
         {
            palabra=new String(lineaseparada.nextToken());
            iguales.addElement(palabra);
         }
         tabla.addElement(iguales);
      }
   }
   catch (IOException excepcion)
   {
      throw new Exception("Error leyendo igualdades - error during equals table's read");
   }
   return tabla;
}

Vector CargarTablaInclusiones(String fichero) throws Exception
{
   String palabra;
   String linea;
   StringTokenizer lineaseparada;
   String incluyente;
   Vector tabla=new Vector();

   if(fichero==null)
     return tabla;
   try
   {
      entrada2=new LineNumberReader(new InputStreamReader(new FileInputStream(fichero)));
   }
   catch (IOException excepcion)
   {
      throw new Exception("Se ha producido un error al abrir la tabla de inclusiones");
   }
   try
   {
      while ((linea=entrada2.readLine())!=null && linea.length()>1)
      {
         lineaseparada=new StringTokenizer(linea,"\t",false);
         incluyente=new String(lineaseparada.nextToken());
         Vector linea2=new Vector();
         Vector incluidas=new Vector();
         while (lineaseparada.hasMoreTokens())
         {
            palabra=new String(lineaseparada.nextToken());
            incluidas.addElement(palabra);
         }
         linea2.addElement(incluyente);
         linea2.addElement(incluidas);
         tabla.addElement(linea2);
      }
   }
   catch (IOException excepcion)
   {
      throw new Exception("Error leyendo inclusiones - error during including table's read");
   }
   return tabla;
}

void AperturaSalida(String fichero) throws Exception
{
   try
   {
      salida=new PrintWriter(new FileOutputStream(fichero));
   }
   catch (IOException excepcion)
   {
      throw new Exception("Error abriendo fichero de salida - error opening exit file");
   }
}

void CierreSalida()
{
   salida.close();
}

public void ejecutar(String cat1, String cat2, String tabla1, String tabla2, String cat3, ODEService os,
                     String usuario, String grupo) throws Exception
{
   COWOVA conversor=new COWOVA(out);
   tax1=conversor.Read(cat1,os);
   tax2=conversor.Read(cat2,os);
   nivelmezcla=4;
   out.println("<h1>Ontología 1:</h1>");
   Casilla.ImprimirCasilla(tax1,out);
   out.println("<hr>");
   out.println("<h1>Ontología 2:</h1>");
   Casilla.ImprimirCasilla(tax2,out);
   out.println("<hr>");
   out.println("<h1>Tabla igualdades:</h1>");
   igualdades=CargarTablaIgualdades(tabla1);
   LT.IT1(igualdades,out);
   inclusiones=CargarTablaInclusiones(tabla2);
   out.println("<hr>");
   out.println("<h1>Tabla inclusiones:</h1>");
   LT.IT2(inclusiones,out);
   out.println("<hr>");
   out.println("<h1>Proceso de mezcla</h1>");
   AlgoritmoJARGNivel(tax1, tax2);
   Completar();
   tax3_final=(Vector)tax3.get(0);
   out.println("<hr>");
   out.println("<h1>Ontología mezcla:</h1>");
   Casilla.ImprimirConTodo(tax3_final,out);
   conversor.Write(tax3_final,cat3,os,usuario,grupo);
}


public void ejecutar(String opcion1, String opcion2, String cat1, String cat2,
 String tabla1, String tabla2, String cat3, int opcion3, String usuario, String grupo)
 throws Exception
{
   if (opcion1.compareTo("1")==0)
   {
/*      CODEVA conversor=new CODEVA();
      tax1=conversor.Convertir(cat1);
      tax2=conversor.Convertir(cat2);
*/   }
   else if (opcion1.compareTo("2")==0)
   {
/*      COXAVA conversor=new COXAVA();
      tax1=conversor.Convertir(cat1);
      tax2=conversor.Convertir(cat2);
*/   }
   else if (opcion1.compareTo("3")==0)
   {
/*      COWOVA conversor=new COWOVA(out);
      tax1=conversor.Lectura(cat1);
      tax2=conversor.Lectura(cat2);
*/   }
   nivelmezcla=Integer.parseInt(opcion2);
   if (opcion3==2)
      out.println("<h1>Ontología 1:</h1>");
   else
      out.println("--> Ontología 1:");
   Casilla.ImprimirCasilla(tax1,out);
   if (opcion3==2)
   {
      out.println("<hr>");
      out.println("<h1>Ontología 2:</h1>");
   }
   else
   {
      out.println("");
      out.println("--> Ontología 2:");
   }
   Casilla.ImprimirCasilla(tax2,out);
   if (opcion3==2)
   {
      out.println("<hr>");
      out.println("<h1>Tabla igualdades:</h1>");
   }
   else
   {
      out.println("");
      out.println("--> Tabla Igualdades:");
   }
   igualdades=CargarTablaIgualdades(tabla1);
   LT.IT1(igualdades,out);
   inclusiones=CargarTablaInclusiones(tabla2);
   if (opcion3==2)
   {
      out.println("<hr>");
      out.println("<h1>Tabla inclusiones:</h1>");
   }
   else
   {
      out.println("");
      out.println("--> Tabla inclusiones:");
   }
   LT.IT2(inclusiones,out);
   if (opcion3==2)
   {
      out.println("<hr>");
      out.println("<h1>Proceso de mezcla</h1>");
   }
   else
   {
      out.println("");
      out.println("--> Proceso de mezcla:");
   }
   AlgoritmoJARGNivel(tax1, tax2);
   Completar();
   tax3_final=(Vector)tax3.get(0);
   if (opcion3==2)
   {
      out.println("<hr>");
      out.println("<h1>Ontología mezcla:</h1>");
   }
   else
   {
      out.println("");
      out.println("--> Ontología mezcla:");
   }
   Casilla.ImprimirCasilla(tax3_final,out);
   if (opcion3!=2)
   {
      AperturaSalida(cat3);
      Casilla.ImprimirXML(tax3_final,salida);
      CierreSalida();
   }
   else
   {
      COWOVA conversor=new COWOVA(out);
      conversor.Escritura(tax3_final,cat3,usuario,grupo);
   }
}

}