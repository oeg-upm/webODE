package es.upm.fi.dia.ontology.webode.OntoClean;

import java.io.*;
/**
 * This class describes the OntoClean errors.
 *
 * @author  
 * @version 
 */
public class ErrorOntoClean implements Serializable
{   
    private String term;
    private String parent;
    private int error_type;
    
    public ErrorOntoClean(String term,String parent, int error_type)
    {
      this.term = term;
      this.parent = parent;
      this.error_type = error_type;
    }
    
    public String GetTermName()
    {
      return this.term;
    }
    
    public String GetParentName()
    {
      return this.parent;
    }  
    
    public int GetErrorType()
    {
      return this.error_type;
    }
    
       
    public String convertString()
    {
      String res = new String();
      if(error_type == 1)
       res="ERROR: The property "+term+" is a subclass of "+parent+", however, "+parent+"is anti-rigid and "+term+" is not.\n";  
      else if(error_type == 2)
       res="ERROR: The property "+term+" is a subclass of "+parent+", however, "+parent+"has anti-unity and "+term+" has not.\n";		 
      else if(error_type == 3)
       res="ERROR: The property "+term+" is a subclass of "+parent+", however, "+parent+"carries an unity criterion and "+term+" does not.\n";		 
      else if(error_type == 4)
       res="ERROR: The property "+term+" is a subclass of "+parent+", however, "+parent+"carries an identity criterion and "+term+" does not.\n";		
      else if(error_type == 5)
       res="ERROR: The property "+term+" is a subclass of "+parent+", however, "+parent+"is dependent and "+term+" is not.\n";		
      
      return res; 
    }    
    
    public String convertStringAux()
    {
      String res = new String();
      if(error_type == 1)
       res="<b>The property "+term+" is a subclass of "+parent+", however, "+parent+"is anti-rigid and "+term+" is not.</b><br><br>";  
      else if(error_type == 2)
       res="<b>The property "+term+" is a subclass of "+parent+", however, "+parent+"has anti-unity and "+term+" has not.</b><br><br>";
      else if(error_type == 3)
       res="<b>The property "+term+" is a subclass of "+parent+", however, "+parent+"carries an unity criterion and "+term+" does not.</b><br><br>";
      else if(error_type == 4)
       res="<b>The property "+term+" is a subclass of "+parent+", however, "+parent+"carries an identity criterion and "+term+" does not.</b><br><br>";
      else if(error_type == 5)
       res="<b>The property "+term+" is a subclass of "+parent+", however, "+parent+"is dependent and "+term+" is not.</b><br><br>";
      
      return res; 
    }    
    
    /*
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
	  out.print("TERM
      if(error_type == 1)
       res="ERROR: The property "+term+" is a subclass of "+parent+", however, "+parent+"is anti-rigid and "+term+" is not.\n";  
      else if(error_type == 2)
       res="ERROR: The property "+term+" is a subclass of "+parent+", however, "+parent+"has anti-unity and "+term+" has not.\n";		 
      else if(error_type == 3)
       res="ERROR: The property "+term+" is a subclass of "+parent+", however, "+parent+"carries an unity criterion and "+term+" does not.\n";		 
      else if(error_type == 4)
       res="ERROR: The property "+term+" is a subclass of "+parent+", however, "+parent+"carries an identity criterion and "+term+" does not.\n";		
      else if(error_type == 5)
       res="ERROR: The property "+term+" is a subclass of "+parent+", however, "+parent+"is dependent and "+term+" is not.\n";		
	}
	    	
 private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException;
     
     */
 }