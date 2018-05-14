package es.upm.fi.dia.ontology.webode.translat.UML.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.*;
import java.io.*;

public class OperationLog {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private OperationLog operationParent =null;
  private String operationName         =null;
  private String operationDescription  =null;
  private Date   operationCreationDate =null;
  private Logger logger                =null;
  private Vector operationSuboperations=null;

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public OperationLog(String name, Logger logger, OperationLog parent) {
    operationParent      =parent;
    operationName        =name;
    operationDescription =new String();
    operationCreationDate=new Date(System.currentTimeMillis());
    this.logger          =logger;
    operationSuboperations=new Vector();
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public String getName() {
    return operationName;
  }

  public void setDescription(String description) {
    operationDescription=description;
  }

  public String toString() {
    Tabulator tab=logger.getTabulator();

    return operationName+" : "+
           operationDescription;
  }

  public void print(PrintWriter writer) {
    Tabulator tab=logger.getTabulator();

    writer.println("["+operationCreationDate+"]"+
                   tab.getTabulator()+" "+
                   operationName+" : "+
                   operationDescription);

    tab.increaseTabulation();

    for(int i=0;i<operationSuboperations.size();i++) {
      OperationLog suboperation=(OperationLog)operationSuboperations.get(i);

      suboperation.print(writer);
    }

    tab.decreaseTabulation();
  }

  public final void print(PrintStream stream) {
    print(new PrintWriter(stream,true));
  }

  public void nestOperation(OperationLog operation) {
    operationSuboperations.add(operation);
  }

  public boolean isRoot() {
    return operationParent==null;
  }
}