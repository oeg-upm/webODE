/*****************************************/
/* MergeServiceImp class *****************/
/*****************************************/
/* Author: J.A.R.G. **********************/
/*****************************************/
package es.upm.fi.dia.ontology.webode.merge;

import java.rmi.*;
import java.util.*;
import java.io.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;

import es.upm.fi.dia.ontology.webode.service.*;


public interface MergeService extends MinervaService
{
  /* Merge the specified ontologies */
  void mergeOntologies(String fileName, String ont1, String ont2, String table1, String table2, String ont3, String user, String usergroup) throws RemoteException;

  /* Find equalities between two ontologies */
  String findEqualities(String fileName, String ontology1, String ontology2) throws RemoteException;
}
