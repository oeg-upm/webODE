package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Search Differences </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.io.*;
import java.rmi.RemoteException;

import es.upm.fi.dia.ontology.webode.service.*;

public class SearchDifferences
{

  public boolean searchDifferences (String ontologyNameA, String ontologyNameB, String xmlFilePath)
  {
    ODEService odeService = null;
    FindDifferencesFactory findDiffFactory = new FindDifferencesFactory();
    FindDifferences findDiff;
    Differences diffs;

    int termTypes[] = {TermTypes.CONCEPT}; //Future Work: TermTypes.AXIOM, etc.
    boolean existDiffs = false;

    try
    {
      diffs = new Differences(ontologyNameA, ontologyNameB);

      // Do the two ontologies exist in WebODE???
      OntologyDescriptor descriptor1 = odeService.getOntologyDescriptor(ontologyNameA);
      OntologyDescriptor descriptor2 = odeService.getOntologyDescriptor(ontologyNameB);

      // The ontologies exist in WebODE
      int i=0;

      while (i<termTypes.length)
      {
        findDiff = findDiffFactory.findDifferences (termTypes[i]);
        findDiff.findDifferences (odeService, diffs, ontologyNameA, ontologyNameB);
        i++;
      }

      existDiffs = diffs.existDifferences();
      if (existDiffs)
      {

        DifferencesToXmlFile diffsToXml = new DifferencesToXmlFile(xmlFilePath);
        diffsToXml.convert(diffs);
      }
      else
          System.out.println ("No differences between the ontologies");
    }
    catch (WebODEException we)
    {
      we.printStackTrace();
      System.out.println ("Any ontology does not exist in WebODE");
    }
    catch (RemoteException re)
    {
      re.printStackTrace ();
    }
    catch (IOException ie)
    {
      ie.printStackTrace ();
    }
    return existDiffs;
  }

  public boolean searchDifferences (ODEService odeService, String ontologyNameA, String ontologyNameB, Writer out)
  {
    FindDifferencesFactory findDiffFactory = new FindDifferencesFactory();
    FindDifferences findDiff;
    Differences diffs;

    int termTypes[] = {TermTypes.CONCEPT}; //Future Work: TermTypes.AXIOM, etc.
    boolean existDiffs = false;

    try
    {
      diffs = new Differences(ontologyNameA, ontologyNameB);

      int i=0;

      while (i<termTypes.length)
      {
        findDiff = findDiffFactory.findDifferences (termTypes[i]);
        findDiff.findDifferences (odeService, diffs, ontologyNameA, ontologyNameB);
        i++;
      }

      existDiffs = diffs.existDifferences();
      if (existDiffs)
      {
        DifferencesToXmlFile diffsToXml = new DifferencesToXmlFile(out);
        diffsToXml.convert(diffs);
      }
      else
          System.out.println ("No differences between the ontologies");
    }
    catch (WebODEException we)
    {
      we.printStackTrace();
      System.out.println ("Any ontology does not exist in WebODE");
    }
    return existDiffs;
  }
}