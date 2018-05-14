package es.upm.fi.dia.ontology.webode.searchDifferences;

import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.webode.service.*;
import java.rmi.RemoteException;
import java.rmi.Remote;
import java.io.CharArrayWriter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DifferencesServiceImp extends MinervaServiceImp implements Remote, DifferencesService
{
  public DifferencesServiceImp() throws RemoteException {
  }

  public void start() throws CannotStartException {
    String odeServiceName = ((DifferencesServiceConfiguration) config).odeServiceName;
    if (odeServiceName == null)
      throw new CannotStartException ("No ODE service specified");
  }

  /**
   * This method searches differences between two given ontologies
   * @param ontologyNameBase The name of the base ontology
   * @param ontologyName The name of the second ontology
   * @return A xml with differences or null if there are not differences between the ontologies
   * @throws DifferencesException
   */
  public char[] searchDifferences (String ontologyNameBase, String ontologyName) throws RemoteException, DifferencesException
  {
    ODEService odeService=null;
    boolean existDiffs = false;

    try {
      odeService=(ODEService)this.context.getService(((DifferencesServiceConfiguration) config).odeServiceName);

      SearchDifferences searchDiffs = new SearchDifferences();

      // Do the two ontologies exist in WebODE???
      OntologyDescriptor descriptor1 = null;
      OntologyDescriptor descriptor2 = null;
      try
      {
        descriptor1 = odeService.getOntologyDescriptor(ontologyNameBase);
      }
      catch (WebODEException a)
      {
        throw new DifferencesException("Ontology \"" + ontologyNameBase+ "\" doesn't exist", a);
      }
      try
      {
        descriptor2 = odeService.getOntologyDescriptor(ontologyName);
      }
      catch (WebODEException a)
      {
        throw new DifferencesException("Ontology \"" + ontologyName+ "\" doesn't exist", a);
      }

      CharArrayWriter output=new CharArrayWriter();
      existDiffs = searchDiffs.searchDifferences(odeService, ontologyNameBase, ontologyName, output);
      if(existDiffs)
        return output.toCharArray();
      else
        return null;
    }
    catch(MinervaException me) {
      throw new DifferencesException("Can not get the ODE service: " + me.getMessage(), me);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }
}
