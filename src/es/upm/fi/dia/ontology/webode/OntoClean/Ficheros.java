package es.upm.fi.dia.ontology.webode.OntoClean;

import java.io.*;

public class Ficheros 
{
      
  private String path;
     
  public Ficheros(String path)
  {
    this.path = path;
  }
        
  public void BorrarFichero()
  {
    File f = new File(path);
    f.delete();
  }

  public void CopiarFichero(String path_copia) throws java.io.IOException
  {
     String contenido = new String();
     contenido = LeerFichero(path_copia);
     EscribirFichero(path,contenido);  
  }

  /*public void IntroducirPredicadoIndice (String buscada, String predicado) throws java.io.IOException
  {
     String contenido = new String();
     contenido = InsertarEnFichero(path,buscada,predicado);
     EscribirFichero(path,contenido);  
  }
  
  public void IntroducirPredicado (String predicado) throws java.io.IOException
  {
     String contenido = new String();
     String buscada = ObtenerIndice(predicado); 
     contenido = InsertarEnFichero(path,buscada,predicado);
     EscribirFichero(path,contenido);  
  }
  
  public void BorrarPredicado (String predicado) throws java.io.IOException
  {
     String contenido = new String();
     contenido = BorrarEnFichero(path,predicado);
     EscribirFichero(path,contenido);  
  }
  public void IncluirCabecera (String cabecera) throws java.io.IOException
  {
    String contenido = new String();
    contenido = IntroducirPrincipio(path,cabecera);	
    EscribirFichero(path,contenido);
  }
  
  public void BorrarDesdeHasta (String desde, String hasta) throws java.io.IOException
  {
     String contenido = new String();
     contenido = BorrarDesdeHastaAux(path,desde,hasta);
     EscribirFichero(path,contenido);  
  } 
  
  private String InsertarEnFichero(String  path, String buscada, String introducida) throws java.io.IOException
  {
     String linea = new String();
     String total = new String();
     FileReader lector =  new FileReader(path);
     BufferedReader buffer = new BufferedReader(lector);
     linea = buffer.readLine();
     while(linea != null)
     {
       total = total + linea + '\n';
       if(linea.equals(buscada))
         total = total + introducida;
       linea = buffer.readLine();
     }
     return total;
  }*/
 
  private String LeerFichero(String path) throws java.io.IOException
  {
     String linea = new String();
     String total = new String();
     FileReader lector =  new FileReader(path);
     BufferedReader buffer = new BufferedReader(lector);
     linea = buffer.readLine();
     while(linea != null)
     {
       if(! linea.equals("\\n"));	
         total = total + linea + '\n';
       linea = buffer.readLine();
     }
     return total;
  }
  /*private String IntroducirPrincipio(String path, String cabecera) throws java.io.IOException
  {
     String linea = new String();
     String total = new String(cabecera);
     FileReader lector =  new FileReader(path);
     BufferedReader buffer = new BufferedReader(lector);
     linea = buffer.readLine();
     while(linea != null)
     {
       total = total + linea + '\n';
       linea = buffer.readLine();
     }
     return total;
  }
  private static String ObtenerIndice(String predicado) throws java.io.IOException
  {
    StringTokenizer cadena = new StringTokenizer(predicado);
    String indice = cadena.nextToken("(");*/
  //  return "/*" + indice + "*/";
  //}
  
  /*private String BorrarEnFichero(String  path, String buscada) throws java.io.IOException
  {
     String linea = new String();
     String total = new String();
     FileReader lector =  new FileReader(path);
     BufferedReader buffer = new BufferedReader(lector);
     linea = buffer.readLine();
     while(linea != null)
     {
       if(!(linea.equals(buscada)))
         total = total + linea + '\n';
       linea = buffer.readLine();
     }
     return total;
  }
  
  private String BorrarDesdeHastaAux(String path,String desde, String hasta) throws java.io.IOException
  {
     String linea = new String();
     String total = new String();
     int dentro = 0;
     FileReader lector =  new FileReader(path);
     BufferedReader buffer = new BufferedReader(lector);
     linea = buffer.readLine();
     while(linea != null)
     {
       if(linea.equals(desde))
       {
         dentro = 1;
         total = total + linea + '\n'; 
       }
       if(dentro == 0)
         total = total + linea + '\n';
       if(linea.equals(hasta))
       {
         dentro = 0;
         total = total + linea + '\n'; 
       }
       linea = buffer.readLine();
     }
     return total;
  } */
  
  private void EscribirFichero(String path, String contenido) throws java.io.IOException
  {
    FileWriter escritor =  new FileWriter(path);
    BufferedWriter buffer = new BufferedWriter(escritor);  
    buffer.write(contenido,0,contenido.length());
    buffer.flush();
  }

}