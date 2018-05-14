package es.upm.fi.dia.ontology.webode.translat.UML;

import java.io.*;
import java.rmi.*;

import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;

import es.upm.fi.dia.ontology.webode.service.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.*;
import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.readers.*;
import es.upm.fi.dia.ontology.webode.translat.UML.tools.*;
import es.upm.fi.dia.ontology.webode.translat.UML.translation.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.writers.*;

import es.upm.fi.dia.ontology.webode.translat.UML.ode.io.writers.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;

import org.xml.sax.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class UMLServiceImp extends MinervaServiceImp implements UMLService {

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public UMLServiceImp() throws RemoteException {

  }

  //----------------------------------------------------------------------------
  //-- Bussiness Logic ---------------------------------------------------------
  //----------------------------------------------------------------------------

  /**
   * Translates the specified ontology to XMI
   *
   * @param ontologyName Name of the ontology to be exported
   * @param dtdFile Location of the dtd file
   * @return The XMI representation of the ontology
   */
  public StringBuffer exportOntologyUML(String ontologyName,
                                        String dtdFile) throws RemoteException,
                                                               O2UException {
    ODEModelReader reader=null;
    StringWriter   result=null;
    ODEService     ode   =null;

    try {
      ode=(ODEService)context.getService(((UMLServiceConfiguration)config).odeService);

      reader=new ODEModelReader(ode);

      Logger.setDirectLogging(false);

      ODEModel odeModel=reader.read(ontologyName);

      ODE2UMLTranslator translator=new ODE2UMLTranslator();

      UMLModel umlModel=translator.translate(odeModel);

      XMIDocumentWriter doc=
          new XMIDocumentWriter(new PrintWriter(result=new StringWriter(),true));
      if(dtdFile!=null)
        doc.setDTDFile(dtdFile);

      UMLModelWriter xmiWriter=doc;

      Logger.initOperationLog("UML Model Writing",
                              "XMI output in process");

      xmiWriter.write(umlModel);

      Logger.finishOperationLog("UML Model Writing");
    }
    catch (O2UException ex) {
      ex.printStackTrace(System.out);
      throw ex;
    }
    catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
    finally {
      if(reader!=null) reader.close();
      if(ode!=null) ode.disconnect();
    }
    if(result!=null)
      return result.getBuffer();
    else
      return null;
  }

  public void importOntologyUML(String         ontologyName,
                                String         user,
                                String         userGroup,
                                StringBuffer   buffer,
                                MinervaSession session) throws RemoteException,
                                                               O2UException {
    ODEService odeService=null;
    InputSource source=new InputSource(new StringReader(buffer.toString()));

    ODEModelWriter odeWriter=null;

    try {
      odeService=(ODEService)this.context.getService(((UMLServiceConfiguration)config).odeService);
      UMLModelReader reader=new XMIReader();

      Logger.setDirectLogging(false);

      UMLModel umlModel=reader.read(source);

      UML2ODETranslator translator=new UML2ODETranslator();

      ODEModel odeModel=translator.translate(umlModel);

      odeModel.setName(ontologyName);

      odeWriter=new ODEModelWriter(odeService,user,userGroup);

      Logger.initOperationLog("ODE Model Writing",
                               "Talking with ODE in progress");

      odeWriter.write(odeModel);

      Logger.finishOperationLog("ODE Model Writing");
    } catch (O2UException ex) {
      if(odeWriter!=null)
        odeWriter.close();
      throw ex;
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
      if(odeWriter!=null)
        odeWriter.close();
      throw new RuntimeException(ex.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }
}