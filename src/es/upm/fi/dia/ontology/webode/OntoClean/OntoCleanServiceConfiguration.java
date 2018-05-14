package es.upm.fi.dia.ontology.webode.OntoClean;

import es.upm.fi.dia.ontology.minerva.server.services.*;


public class OntoCleanServiceConfiguration extends MinervaServiceConfiguration
{
  public String prologService; /* Service to translate to prolog */
  public String odeService; /* ODE service to use */
  public String inferenceService; /* Service that makes inferences */
  
  public OntoCleanServiceConfiguration() {super(true);}
  
  public OntoCleanServiceConfiguration(String odeService, String prologService, String inferenceService) 
  { 	
    super(true); 
    this.odeService= odeService;
    this.prologService = prologService;
    this.inferenceService = inferenceService;	
    
  }
}