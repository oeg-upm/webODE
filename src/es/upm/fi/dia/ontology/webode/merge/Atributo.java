/*****************************************/
/* Atributo class ************************/
/*****************************************/
/* Author: J.A.R.G. **********************/
/*****************************************/

package es.upm.fi.dia.ontology.webode.merge;
import java.util.*;

public class Atributo
{
   String nombre;
   String descripcion;
   String tipo;
   String card_minima;
   String card_maxima;
   String unidad;
   String precision;
   Vector valor;
   Vector referencias;
   Vector sinonimos;
   Vector abreviaturas;
   Vector formulas;
   Vector deducido_de;

public Atributo()
{
   valor=new Vector();
   referencias=new Vector();
   sinonimos=new Vector();
   abreviaturas=new Vector();
   formulas=new Vector();
   deducido_de=new Vector();
}

public static boolean Iguales(Atributo atributo1, Atributo atributo2)
{
   return (atributo1.nombre.compareTo(atributo2.nombre)==0);
}

public static boolean Contiene(Vector atributos, Atributo atributo)
{
   boolean esta=false;
   int i=0;
   while (!esta && i<atributos.size())
   {
      esta=Atributo.Iguales(atributo,(Atributo)atributos.get(i));
      i++;
   }
   return esta;
}

}
