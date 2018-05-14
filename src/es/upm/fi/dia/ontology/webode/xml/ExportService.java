package es.upm.fi.dia.ontology.webode.xml;

import java.rmi.*;
import java.io.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.others.*;

import org.w3c.dom.*;

import es.upm.fi.dia.ontology.webode.service.*;

/**
 * This class provides the implementation for the XML export
 * service.
 * <p>
 * It extends the <tt>BackupSource</tt> interface in order to
 * be a valid source for back-ups.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.7
 */
public interface ExportService extends MinervaService, BackupSource
{
    /**
     * Generates the XML document of an ontology.
     *
     * @param ontology The ontology to export.
     * @param bConceptualization If <tt>true</tt> export the conceptualization.
     * @param instanceSets The instance sets to be exported.
     * @param views        The views to be exported.
     */
    StringBuffer export (String ontology, boolean bConceptualization, 
			 String[] instanceSets, String[] views, String dtd)
	throws IOException, DOMException, WebODEException, RemoteException;
}

