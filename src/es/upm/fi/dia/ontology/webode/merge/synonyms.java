/*****************************************/
/* Synonyms Search ***********************/
/*****************************************/
/* Author: J.A.R.G. **********************/
/* Date: 04-04-06 ************************/
/*****************************************/

package es.upm.fi.dia.ontology.webode.merge;

import java.io.*;
import java.util.*;
import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.client.*;

public class synonyms
{

PrintWriter out;
protected PrintWriter salida;
ODEService os;

public synonyms(String nombreFichero)
{
   super();
   try{
   	FileOutputStream ficheroSalida = new FileOutputStream(nombreFichero);
   	out=new PrintWriter(ficheroSalida,true);
   }catch (FileNotFoundException e){
   	System.out.println("Error al crear fichero: " + nombreFichero + "  " + e.getMessage());
   };

}

public synonyms(Writer sal)
{
   super();
   out=new PrintWriter(sal);
}

public synonyms(PrintStream sal)
{
   super();
   out=new PrintWriter(sal,true);
}

boolean sintacticEquality(String palabra1, String palabra2)
{
   return (palabra1.compareTo(palabra2)==0);
}

private void conceptEqualitiesSearch(Vector Equalities, String ontology1, String ontology2)
    throws Exception
{
   Term Concepts[];
   try
   {
      int constant[];
      constant=new int[] {TermTypes.CONCEPT};
      Concepts=os.getTerms(ontology1,constant);

      int index;
      int nconcepts=Concepts.length;
      for(index=0;index<nconcepts;index++)
      {
         try
         {
            Term term;
            term=os.getTerm(ontology2,(Concepts[index]).term);
            /* Se coge de la ontologia2 el término con nombre de la ontología1. */
            /* Si existe es que hay igualdad de conceptos. */
            if (term!=null)
            {
              Vector Filen=new Vector();
              String left=Concepts[index].term + " (Concept)";
              String right=term.term + " (Concept)";
              Filen.addElement(left);
              Filen.addElement(right);
              Equalities.addElement(Filen);
            }
         }
         catch (Exception e)
         {
            throw new Exception("Error leyendo conceptos de la ontología "+ontology2+".\n"+e.getMessage(),e);
         }
      }
   }
   catch (Exception e)
   {
      throw new Exception("Error leyendo conceptos de la ontología "+ontology1+".\n"+e.getMessage(),e);
   }
}

private void relationEqualitiesSearch(Vector Equalities, String ontology1, String ontology2)
    throws Exception
{
  TermRelation Relations1[];
  TermRelation Relations2[];
  try
  {
     Relations1=os.getTermRelations(ontology1,false);
     Relations2=os.getTermRelations(ontology2,false);

     int index1;
     int index2;
     int nrelations1=0;
     int nrelations2=0;
     if (Relations1!=null && Relations2!=null)
     {
       nrelations1=Relations1.length;
       nrelations2=Relations2.length;
     }

     if (nrelations1>0 && nrelations2>0)
        for(index1=0;index1<nrelations1;index1++)
        {
           for(index2=0;index2<nrelations2;index2++)
           {
              /* Si las dos relaciones se llaman igual, se añade explicitando los
              /* orígenes y destino de cada una. */
              if (sintacticEquality(Relations1[index1].name,Relations2[index2].name))
              {
                Vector Filem=new Vector();
                String left=Relations1[index1].name + " (Relation: " + Relations1[index1].origin + "-" + Relations1[index1].destination + ")";
                String right=Relations2[index2].name + " (Relation: " + Relations2[index2].origin + "-" + Relations2[index2].destination + ")";
                Filem.addElement(left);
                Filem.addElement(right);
                Equalities.addElement(Filem);
              }
           }
        }
  }
  catch (Exception e)
  {
     throw new Exception("Error leyendo relaciones de la ontología "+ontology1+".\n"+e.getMessage(),e);
  }
}

public void listEqualities(Vector Equalities)
{
  int i=0;
  out.println("List of equalities");
  if (Equalities.size()>0)
    for (i=0;i<Equalities.size();i++)
    {
      Vector Filai=(Vector)Equalities.get(i);
      out.print((String)Filai.get(0));
      out.print(" = ");
      out.println((String)Filai.get(1));
    }
}

public void execute(String ontology1, String ontology2, ODEService os2/*, String user, String group*/)
    throws Exception
{
   /* Este código se limitará a la búsqueda de elementos sinónimos entre dos */
   /* ontologías. Sólo conceptos y relaciones. La motivación que ha llevado a */
   /* su creación es la facilitación a la hora de generar las tablas para la */
   /* la mezcla. */
   /* Aun asi, la busqueda se va a realizar sobre toda la ontología, no como */
   /* en la mezcla que se realiza por niveles. La razón es que aquí no existe */
   /* el requisito de las tablas con las que se "navega". */
   /* Al método principal le entrarán los nombres de dos ontologías y */
   /* generará una tabla de sinónimos. */

   // Matriz de sinónimos (Vector x Vector)
   Vector Equalities=new Vector();

   os=os2;

   // Comparación de nombre de ontologías
   if (sintacticEquality(ontology1,ontology2))
   {
      Vector File1=new Vector();
      File1.addElement(ontology1);
      File1.addElement(ontology2);
      Equalities.addElement(File1);
   }

   // Comparación de conceptos
   try
   {
      conceptEqualitiesSearch(Equalities,ontology1,ontology2);
   }
   catch (Exception e)
   {
      throw new Exception("An error occurred when searching the concept equalities:\n"+e.getMessage(),e);
   }

   // Comparación de relaciones
   try
   {
      relationEqualitiesSearch(Equalities,ontology1,ontology2);
   }
   catch (Exception e)
   {
      throw new Exception("An error occurred when searching the relation equalities:\n"+e.getMessage(),e);
   }

   //Listado de igualdades
   listEqualities(Equalities);
}

}