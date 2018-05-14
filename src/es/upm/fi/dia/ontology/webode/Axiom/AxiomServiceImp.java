package es.upm.fi.dia.ontology.webode.Axiom;

import java.io.*;
import java.util.*;
import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.others.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.services.CannotStartException;
import es.upm.fi.dia.ontology.webode.service.ODEService;

public class AxiomServiceImp extends MinervaServiceImp implements AxiomService {
  private String[] errors=null;
  private boolean isCorrect;

  public AxiomServiceImp() throws RemoteException {};

  public void start() throws CannotStartException {
    System.out.println("Launching Axiom Service....................");
    if(((AxiomServiceConfiguration)this.config).odeService==null)
      throw new CannotStartException("Axiom service can not start without any ODE service specified");

    System.out.println("Axiom Service Started");
  }

  public Result parseAxiom(String ontology, String axiom) throws java.rmi.RemoteException {
    Yylex yy;
    StringReader fichero_entrada;
    String [] clauses=null;
    String [] prologClauses=null;
    ODEService odeService=null;

    try {
      odeService=(ODEService)this.context.getService(((AxiomServiceConfiguration)this.config).odeService);

      fichero_entrada= new StringReader(axiom);
      yy = new Yylex(fichero_entrada);

      parser p = new parser(yy,odeService,ontology);

      p.parse(); // will call y.yylex()

      clauses=p.getClauses();
      prologClauses=p.getPrologClauses();

      return new Result(clauses,prologClauses,true);
    }
    catch (AxiomException ex) {

      Vector er=ex.getErrors();
      errors=new String[er.size()];
      for(int i=0;i<er.size();i++)
      {
        errors[i]=(String)er.elementAt(i);
      }
      return new Result(errors,false);
    }
    catch(Exception e) {

      System.err.println(e);
      return new Result(null,false);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }

  public Result parseRule(String ontology, String rule) throws java.rmi.RemoteException {
    ScannerRule yy;
    StringReader fichero_entrada;
    String [] clauses=null;
    String [] prologClauses=null;
    ODEService odeService=null;

    try {
      odeService=(ODEService)this.context.getService(((AxiomServiceConfiguration)this.config).odeService);


      fichero_entrada= new StringReader(rule);
      yy = new ScannerRule(fichero_entrada);

      parserRule p = new parserRule(yy,odeService,ontology);



      p.parse(); // will call y.yylex()


      clauses=p.getClauses();
      prologClauses=p.getPrologClauses();

      return new Result(clauses,prologClauses,true);
    }
    catch (AxiomException ex){
      Vector er=ex.getErrors();
      errors=new String[er.size()];
      for(int i=0;i<er.size();i++) {
        errors[i]=(String)er.elementAt(i);
      }
  /*isCorrect=false;
  return errors;*/
      return new Result(errors,false);
    }
    catch(Exception e){
      System.err.println(e);
/*		isCorrect=false;
  return null;*/
      return new Result(null,false);
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }
}