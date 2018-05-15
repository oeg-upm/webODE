package es.upm.fi.dia.ontology.minerva.mmc;

import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.admin.*;


public class ConnectionDescriptor 
{
    private MinervaURL url;
    private MinervaSession ms;
    private AdministrationService admService;

    public ConnectionDescriptor (MinervaSession ms, AdministrationService admService, MinervaURL url)
    {
	this.ms = ms;
	this.url = url;
	this.admService = admService;
    }

    public String toString ()
    {
	return "Host " + url.getHost() + " at port " + url.getPort();
    }

    public MinervaSession getMinervaSession ()
    {
	return ms;
    }

    public MinervaURL getMinervaURL ()
    {
	return url;
    }

    public AdministrationService getAdministrationService ()
    {
	return admService;
    }
}
