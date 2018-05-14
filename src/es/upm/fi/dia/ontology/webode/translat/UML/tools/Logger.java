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

public class Logger {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private Tabulator    loggerTabulator     =null;
  private Stack        loggerOperationStack=null;
  private OperationLog loggerLastOperation =null;
  private Vector       loggerOperations    =null;
  private boolean      loggerAutomatic     =false;

  private static Logger logger=null;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  protected Tabulator getTabulator() {
    return loggerTabulator;
  }

  private void setAutomaticMode(boolean ok) {
    loggerAutomatic=ok;
  }

  private void initOperation(String operation,
                             String description) {
    OperationLog newOperation=new OperationLog(operation,this,loggerLastOperation);
    newOperation.setDescription(description);

    if(loggerLastOperation!=null) {
      loggerLastOperation.nestOperation(newOperation);
      if(loggerAutomatic)
        loggerTabulator.increaseTabulation();
    }

    if(loggerAutomatic)
      System.out.println(loggerTabulator.getTabulator()+"* "+newOperation);

    loggerLastOperation=newOperation;
    loggerOperationStack.push(newOperation);
  }

  private void finishOperation(String operationName) {
    boolean found=false;

    while(loggerOperationStack.size()!=0&&!found) {
      OperationLog operation=(OperationLog)loggerOperationStack.pop();
      found=operation.getName().equals(operationName);
      if(loggerAutomatic)
        if(loggerOperationStack.size()>0)
          loggerTabulator.decreaseTabulation();
      if(operation.isRoot())
        loggerOperations.add(operation);
    }

    if(!found) {
      OperationLog error=new OperationLog("Logger Error",this,null);
      error.setDescription("Operation "+operationName+" should have been begun before being finished... Operation stack cleared.");
      loggerOperations.add(error);
      loggerLastOperation=null;
    } else {
      if(loggerOperationStack.size()>0)
        loggerLastOperation=(OperationLog)loggerOperationStack.peek();
      else
        loggerLastOperation=null;
    }
  }

  private void write(PrintWriter out) {
    for(int i=0;i<loggerOperations.size();i++) {
      OperationLog operation=(OperationLog)loggerOperations.get(i);
      operation.print(out);
    }
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  private Logger() {
    loggerTabulator     =new Tabulator();
    loggerOperationStack=new Stack();
    loggerOperations    =new Vector();
  }

  //----------------------------------------------------------------------------
  //-- Bussiness logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  public static Logger getInstance() {
    if(logger==null)
      logger=new Logger();

    return logger;
  }

  public static void initLogger() {
    logger=null;
  }

  public static void setDirectLogging(boolean automatic) {
    Logger logger=getInstance();

    logger.setAutomaticMode(automatic);
  }

  public static void initOperationLog(String operationName,
                                      String operationDescription) {
    Logger logger=getInstance();

    logger.initOperation(operationName,operationDescription);
  }

  public static void finishOperationLog(String operationName) {
    Logger logger=getInstance();

    logger.finishOperation(operationName);
  }

  public static void writeLog(PrintWriter out) {
    Logger logger=getInstance();

    logger.write(out);
  }

  public static void writeLog(PrintStream stream) {
    Logger logger=getInstance();

    logger.write(new PrintWriter(stream,true));
  }

}


