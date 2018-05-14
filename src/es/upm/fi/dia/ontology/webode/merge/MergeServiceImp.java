/*****************************************/
/* MergeServiceImp class *****************/
/*****************************************/
/* Author: J.A.R.G. **********************/
/*****************************************/

package es.upm.fi.dia.ontology.webode.merge;
import java.util.*;
import java.io.*;
import java.rmi.*;
import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.others.*;

public class MergeServiceImp extends MinervaServiceImp
    implements MergeService {

  public MergeServiceImp() throws RemoteException {};

  public void start() throws CannotStartException
  {
    System.out.println("Launching ODEMerge............");
    String odeService=((MergeServiceConfiguration) config).odeService;

    if (odeService == null)
      throw new CannotStartException ("no ode service specified. \nThe service is not properly configured.");
  }

  public void mergeOntologies(String fileName, String ont1, String ont2, String table1, String table2, String ont3, String user, String usergroup) throws RemoteException
  {
    algoJARG algo=new algoJARG(fileName);
    ODEService ode=null;
    try
    {
      ode=(ODEService)this.context.getService(((MergeServiceConfiguration) config).odeService);
      algo.ejecutar(ont1, ont2, table1, table2, ont3, ode, user, usergroup);
    }
    catch (Exception e)
    {
      throw new RemoteException("Error merging ontologies: " + e.getMessage(),e);
    }
    finally
    {
      if (ode!=null) ode.disconnect();
    }
  }

  public String findEqualities(String fileName, String ontology1, String ontology2) throws RemoteException
  {
    synonyms syn=new synonyms(fileName);
    ODEService ode=null;
    try
    {
      ode=(ODEService)this.context.getService(((MergeServiceConfiguration) config).odeService);
      syn.execute(ontology1, ontology2, ode);

      StringWriter out=new StringWriter();
      FileReader fin=new FileReader(fileName);
      char[] buffer=new char[1024];
      int length;
      while((length=fin.read(buffer))>-1)
        out.write(buffer, 0, length);
      return out.toString();
    }
    catch (Exception e)
    {
      throw new RemoteException("Error merging ontologies: " + e.getMessage(),e);
    }
    finally
    {
      if (ode!=null) ode.disconnect();
    }
  }
}