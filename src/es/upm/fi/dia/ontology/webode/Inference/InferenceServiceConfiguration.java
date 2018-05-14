package es.upm.fi.dia.ontology.webode.Inference;


import es.upm.fi.dia.ontology.minerva.server.services.*;


public class InferenceServiceConfiguration extends MinervaServiceConfiguration
{

  public String prologService; /* Traslate prolog service to use */
  public String connections; /* Number connections */
  public String prologPath; /*Ciao Path*/

  public InferenceServiceConfiguration() { super(true); }  //Ponía false StATELESS

  public InferenceServiceConfiguration(String dbService, String prologService, String connections, String prologPath) {
    super(true);    //Ponía false StATELESS
    this.prologService = prologService;
    this.connections = connections;
    this.prologPath = prologPath;
  }

}