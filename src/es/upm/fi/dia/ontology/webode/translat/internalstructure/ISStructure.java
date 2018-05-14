/*
Definition of t......
*/



package es.upm.fi.dia.ontology.webode.translat.internalstructure;
import org.w3c.dom.Document;
import es.upm.fi.dia.ontology.webode.translat.logs.Log;

import es.upm.fi.dia.ontology.webode.service.ODEService;

public interface ISStructure extends Cloneable
{
	public  Document obtainXML();
	public 	Log obtainLog();
        public void toWebODE (ODEService ode, String user, String userGroup) throws Exception;
}