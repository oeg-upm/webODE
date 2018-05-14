package es.upm.fi.dia.ontology.webode.Inference;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import java.io.*;
import java.rmi.*;



public interface InferenceService extends MinervaService
{
  String[] list(String user, String id, String ontology) throws IOException, RemoteException;

  //void reserve(String text, String user, String id) throws IOException, RemoteException;

  void save(String module, String text, String user) throws IOException, RemoteException;

  String load(String module, String user, String id) throws IOException, RemoteException;

  String query(String query, String user, String id) throws IOException, RemoteException;



}