/*****************************************/
/* LT class ******************************/
/*****************************************/
/* Author: J.A.R.G. **********************/
/*****************************************/

package es.upm.fi.dia.ontology.webode.merge;
import java.util.*;
import java.io.*;

public class LT
{

public LT()
{
   super();
}

static void IT1(Vector tabla, PrintWriter out)
{
   for(int i=0;i<tabla.size();i++)
   {
      Vector linea=new Vector();
      linea=(Vector)tabla.get(i);
      for(int j=0;j<linea.size();j++)
         out.print((String)linea.get(j)+" = ");
      out.println();
   }
}

static void IT2(Vector tabla, PrintWriter out)
{
   for(int i=0;i<tabla.size();i++)
   {
      Vector linea=new Vector();
      linea=(Vector)tabla.get(i);
      out.print((String)linea.get(0)+" <- ");
      for(int j=0;j<((Vector)linea.get(1)).size();j++)
         out.print((String)((Vector)linea.get(1)).get(j)+", ");
      out.println();
   }
}

/*public static void main(String[] args)
{
   Vector tabla1=new Vector();
   Vector tabla2=new Vector();

   tabla1=algoJARG.CargarTablaIgualdades(args[0]);
   IT1(tabla1);
   tabla2=algoJARG.CargarTablaInclusiones(args[1]);
   IT2(tabla2);
}
*/
}

