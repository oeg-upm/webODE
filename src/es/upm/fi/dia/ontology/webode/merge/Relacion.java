/*****************************************/
/* Relacion class ************************/
/*****************************************/
/* Author: J.A.R.G. **********************/
/*****************************************/

package es.upm.fi.dia.ontology.webode.merge;
import java.util.*;

public class Relacion
{
   String nombre;
   String descripcion;
   String origen;
   String destino;
   String card_maxima;
   Vector referencias;
   Vector propiedades;

public Relacion()
{
   referencias=new Vector();
   propiedades=new Vector();
}

public static boolean Iguales(Relacion relacion1, Relacion relacion2)
{
   return (relacion1.nombre.compareTo(relacion2.nombre)==0);
}

public static boolean Contiene(Vector relaciones, Relacion relacion)
{
   boolean esta=false;
   int i=0;
   while (!esta && i<relaciones.size())
   {
      esta=Relacion.Iguales(relacion,(Relacion)relaciones.get(i));
      i++;
   }
   return esta;
}

}
