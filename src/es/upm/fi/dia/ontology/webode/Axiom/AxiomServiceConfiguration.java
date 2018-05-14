package es.upm.fi.dia.ontology.webode.Axiom;


import es.upm.fi.dia.ontology.minerva.server.services.*;


public class AxiomServiceConfiguration extends MinervaServiceConfiguration
{
  public String odeService; /* DB service to use */

  public AxiomServiceConfiguration() { super(true); }  //Ponía false StATELESS

  public AxiomServiceConfiguration(String dbService) {
    super(true);    //Ponía false StATELESS
    this.odeService=dbService;
  }
}