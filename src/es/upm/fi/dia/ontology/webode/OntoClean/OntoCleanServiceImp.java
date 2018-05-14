package es.upm.fi.dia.ontology.webode.OntoClean;

import java.rmi.*;
import java.io.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.webode.Inference.*;
import es.upm.fi.dia.ontology.webode.translat.Prolog.*;

/**
 * This class describes the essentials of the OntoClean service
 *
 * @author  Emilio Raya and Oscar Corcho
 * @version 1.0
 */
public class OntoCleanServiceImp extends MinervaServiceImp
    implements OntoCleanService {

  private PrologExportService prologService;
  private InferenceService inferenceService;

  public OntoCleanServiceImp() throws RemoteException {};

  public void start() throws CannotStartException
  {
    System.out.print("Launching OntoClean Service............");

    String ode=((OntoCleanServiceConfiguration) config).odeService;
    String prolog = ((OntoCleanServiceConfiguration) config).prologService;
    String inference = ((OntoCleanServiceConfiguration) config).inferenceService;

    if (ode == null)
      throw new CannotStartException ("no ODE service specified. \nThe service is not properly configured.");
    if (prolog == null)
      throw new CannotStartException ("no Prolog service specified. \nThe service is not properly configured.");
    if (inference == null)
      throw new CannotStartException ("no inference service specified. \nThe service is not properly configured.");

    try {
      prologService = (PrologExportService) context.getService (prolog);
      inferenceService = (InferenceService) context.getService (inference);
      System.out.println("    launched");
    } catch (Exception e) {
      throw new CannotStartException("Cannot obtain a needed service: " + e.getMessage());
    }
  }

  /**
   * Creates an ontology with the given attributes.
   * @param od The ontology descriptor.
   *
   * Cuando creamos una ontologia hemos de crear tambien el INSTANCE_SET de esa ontologia para
   * la ontologia de top level universals.
   * Tambien copiamos el fichero toplevel.pl para hacer los razonamientos de OntoClean en prolog.
   */
  public void createOntology (OntologyDescriptor od) throws WebODEException, RemoteException {
    ODEService odeService=null;
    String instance_set_name = new String(od.name);
    int i;
    try {
      odeService=(ODEService)this.context.getService(((OntoCleanServiceConfiguration) config).odeService);

      odeService.createOntology(od);
      Ficheros fichero = new Ficheros(System.getProperty(MinervaServerConstants.MINERVA_HOME) +  File.separator + "webserver" +  File.separator + "webode" +  File.separator + "Prolog-Modules" +  File.separator + od.login + od.name + "toplevel.pl");
      fichero.CopiarFichero(System.getProperty(MinervaServerConstants.MINERVA_HOME) +  File.separator + "webserver" +  File.separator + "webode" +  File.separator + "Prolog-Modules" +  File.separator + "toplevel.pl");
      //Nos aseguramos de que el instance set no exista previamente
      Term [] instances_set = odeService.getTerms(OntoCleanConstants.TOP_LEVEL,new int [] {TermTypes.INSTANCE_SET});
      if (instances_set != null) {
        for(i=0; i<instances_set.length; i++) {
          if(instance_set_name.equals(instances_set[i].term)) {
            odeService.removeTerm(OntoCleanConstants.TOP_LEVEL,instances_set[i].term);
            i=instances_set.length;
          }
        }
      }
      odeService.insertTerm(OntoCleanConstants.TOP_LEVEL,instance_set_name," ",TermTypes.INSTANCE_SET);
    } catch (Exception e) {
      throw new WebODEException ("Error creating ontology: " + e.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }

  /**
   * Creates the instance set of the top level of universals for an ontology.
   * @param user The user who owns the ontology.
   * @param ontologyName The ontology name.
   * @author Oscar Corcho
   * Cuando se crea una ontología, o se había creado previamente pero sin OntoClean,
   * se debe crear el instance set correspondiente de la ontología top-level de
   * universals.
   * También se crea el fichero Prolog que se utilizará para razonar
   */
  public void initializeOntology (String user, String ontologyName)  throws WebODEException, RemoteException
  {
    ODEService odeService=null;
    int i;
    try {
      odeService=(ODEService)this.context.getService(((OntoCleanServiceConfiguration) config).odeService);

      Ficheros fichero = new Ficheros(System.getProperty(MinervaServerConstants.MINERVA_HOME) +  File.separator + "webserver" +  File.separator + "webode" +  File.separator + "Prolog-Modules" +  File.separator + user + ontologyName + "toplevel.pl");
      fichero.CopiarFichero(System.getProperty(MinervaServerConstants.MINERVA_HOME) +  File.separator + "webserver" +  File.separator + "webode" +  File.separator + "Prolog-Modules" +  File.separator + "toplevel.pl");
      //Nos aseguramos de que el instance set no exista previamente
      Term [] instances_set = odeService.getTerms(OntoCleanConstants.TOP_LEVEL,new int [] {TermTypes.INSTANCE_SET});
      if (instances_set != null) {
        for(i=0; i<instances_set.length && !ontologyName.equals(instances_set[i].term); i++) {};
        if (i==instances_set.length)
          odeService.insertTerm(OntoCleanConstants.TOP_LEVEL,ontologyName," ",TermTypes.INSTANCE_SET);
      } else {
        odeService.insertTerm(OntoCleanConstants.TOP_LEVEL,ontologyName," ",TermTypes.INSTANCE_SET);
      }
    } catch (Exception e) {
      throw new WebODEException ("Error creating instance set: " + e.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }


  /**
   * Removes an ontology, together with its instance set of the top level ontology of universals,
   * if it exists, and the Prolog module used to infer.
   * @param user The user who owns the ontology.
   * @param ontologyName The ontology name.
   *
   * Cuando borramos una ontologia hemos de borrar tambien el INSTANCE_SET de esa ontologia para
   * la ontologia de top level universals.
   * Tambien hemos de borrar el fichero prolog con el que razonabamos para OntoClean.
   */
  public void removeOntology (String user, String ontologyName)  throws WebODEException, RemoteException
  {
    ODEService odeService=null;
    try {
      odeService=(ODEService)this.context.getService(((OntoCleanServiceConfiguration) config).odeService);

      odeService.removeOntology(ontologyName);
      Ficheros fichero_aux3 = new Ficheros(System.getProperty(MinervaServerConstants.MINERVA_HOME) +  File.separator + "webserver" +  File.separator + "webode" +  File.separator + "Prolog-Modules" +  File.separator + user + ontologyName + "toplevel.pl");
      fichero_aux3.BorrarFichero();
      Instance[] instances = odeService.getInstances(OntoCleanConstants.TOP_LEVEL,ontologyName,OntoCleanConstants.PROPERTY);
      if(instances != null)
      {
        for(int k=0;k<instances.length;k++)
          odeService.removeTerm(OntoCleanConstants.TOP_LEVEL,instances[k].name);
      }
      odeService.removeTerm(OntoCleanConstants.TOP_LEVEL,ontologyName);
    } catch (Exception e) {
      throw new WebODEException ("Error deleting ontology: " + e.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }


     /*
  * Cuando insertamos un termino hemos de insertar tambien la instancia de ese termino para
  * el termino "property" de la ontologia de top level universals.
     */
  public void insertTerm (String ontology, String term,
                          String desc, int type,int anti_unity,int is_dependent,
                          int carries_unity,int anti_rigid,int carries_identity,
                          int supplies_identity,int rigid)
      throws WebODEException, RemoteException
  {
    ODEService odeService=null;
    int i,j;
    try {
      odeService=(ODEService)this.context.getService(((OntoCleanServiceConfiguration) config).odeService);

      odeService.insertTerm(ontology,term,desc,type);
      if(type == TermTypes.CONCEPT)
      {
/*
        //Aseguramos que el instance set esté creado. Es decir, que ya se haya
        //utilizado esta ontología alguna vez con OntoClean.
        //Si no, se crea el instance set correspondiente para la ontología top level
        //de universals
          String instance_set_name = new String(od.name);
        //Ficheros fichero = new Ficheros("../webserver/webode/Prolog-Modules/" + od.login + od.name + "toplevel.pl");
        //fichero.CopiarFichero("../webserver/webode/Prolog-Modules/" + "toplevel.pl");
        //Nos aseguramos de que el instance set no exista previamente
             Term [] instances_set = odeService.getTerms(OntoCleanConstants.TOP_LEVEL,new int [] {TermTypes.INSTANCE_SET});
             if (instances_set != null) {
                for(i=0; i<instances_set.length; i++) {
                  if(instance_set_name.equals(instances_set[i].term)) {
                     odeService.removeTerm(OntoCleanConstants.TOP_LEVEL,instances_set[i].term);
                     i=instances_set.length;
                  }
                }
             }
             odeService.insertTerm(OntoCleanConstants.TOP_LEVEL,instance_set_name," ",TermTypes.INSTANCE_SET);
*/

        odeService.removeTerm(OntoCleanConstants.TOP_LEVEL,term);
        Instance instance = new Instance(term, OntoCleanConstants.PROPERTY, ontology,"");
        odeService.insertInstance(OntoCleanConstants.TOP_LEVEL,instance);
        InstanceAttributeDescriptor[] atrInsts = odeService.getInstanceAttributes(OntoCleanConstants.TOP_LEVEL,OntoCleanConstants.PROPERTY);
        //******************************************************************************
        //   AÑADIR VALORES SEGUN LAS METAPROPIEDADES
        //******************************************************************************
        if(atrInsts != null) {
          for(j=0;j<atrInsts.length;j++) {
            if(atrInsts[j].name.equals(OntoCleanConstants.ATT_CARRIES_ANTI_UNITY)) {
              if(anti_unity == 1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
              else if(anti_unity == -1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
              else
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
            } else if (atrInsts[j].name.equals(OntoCleanConstants.ATT_CARRIES_UNITY)) {
              if(carries_unity == 1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
              else if(carries_unity == -1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
              else
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term,OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
            } else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_IS_DEPENDENT)) {
              if(is_dependent == 1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
              else if(is_dependent == -1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
              else
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term,OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
            }else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_ANTI_RIGID)) {
              if(anti_rigid == 1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
              else if(anti_rigid == -1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
              else
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term,OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
            } else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_CARRIES_IDENTITY_CRITERION)){
              if(carries_identity == 1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
              else if(carries_identity == -1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
              else
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
            } else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_SUPPLIES_IDENTITY_CRITERION)) {
              if(supplies_identity ==1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
              else if(supplies_identity == -1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
              else
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
            } else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_IS_RIGID)) {
              if(rigid ==1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
              else if(rigid == -1)
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
              else
                odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
            } else
              odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
          }
        }
      }
    } catch (Exception sqle) {
      throw new WebODEException ("Error inserting term: " + sqle.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }


     /*
  * Cuando modifiacamos un termino hemos de modificar tambien la instancia de ese termino para
  * el termino "property" de la ontologia de top level universals.
     */
  public void updateTerm (String originalName, String parent, Term td,
                          int anti_unity,int is_dependent,int carries_unity,
                          int anti_rigid,int carries_identity,int supplies_identity,int rigid)
      throws WebODEException, RemoteException
  {
    ODEService odeService=null;
    int j;
    try {
      odeService=(ODEService)this.context.getService(((OntoCleanServiceConfiguration) config).odeService);

      if (parent == null)		odeService.updateTerm(originalName,td);
      else					odeService.updateTerm(originalName,parent,td);

      //if(td.type == TermTypes.CONCEPT)
      //{
      odeService.removeTerm(OntoCleanConstants.TOP_LEVEL,originalName);
      Instance instance = new Instance(td.term, OntoCleanConstants.PROPERTY, td.ontology,"");
      odeService.insertInstance(OntoCleanConstants.TOP_LEVEL,instance);
      InstanceAttributeDescriptor[] atrInsts = odeService.getInstanceAttributes(OntoCleanConstants.TOP_LEVEL,OntoCleanConstants.PROPERTY);
      //*****************************************************************************************
      //   AÑADIR VALORES SEGUN LAS METAPROPIEDADES
      //*****************************************************************************************
      if(atrInsts != null)
      {
        for(j=0;j<atrInsts.length;j++)
        {
          if(atrInsts[j].name.equals(OntoCleanConstants.ATT_CARRIES_ANTI_UNITY))
          {
          if(anti_unity == 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
          else if(anti_unity == -1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
          else
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
        }
        else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_CARRIES_UNITY))
        {
          if(carries_unity == 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
          else if(carries_unity == - 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
          else
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term,OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
        }
        else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_IS_DEPENDENT))
        {
          if(is_dependent == 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
          else if(is_dependent == - 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
          else
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
        }
        else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_ANTI_RIGID))
        {
          if(anti_rigid == 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
          else if(anti_rigid == - 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
          else
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
        }
        else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_CARRIES_IDENTITY_CRITERION))
        {
          if(carries_identity == 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
          else if(carries_identity == - 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
          else
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
        }
        else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_SUPPLIES_IDENTITY_CRITERION))
        {
          if(supplies_identity == 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
          else if(supplies_identity == - 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
          else
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
        }
        else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_IS_RIGID))
        {
          if(rigid == 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.T);
          else if(rigid == - 1)
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.F);
          else
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
        }
        else
          odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,td.ontology, td.term, OntoCleanConstants.PROPERTY, atrInsts[j].name, OntoCleanConstants.N);
        }
      }
      //}
    } catch (Exception sqle) {
      throw new WebODEException ("Error updating term: " + sqle.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }

     /*
  * Cuando borramos un termino hemos de borrar tambien la instancia asociada que creamos en
  * la ontologia de top level universals.
     */
  public void removeTerm (String ontology, String term,
                          String parent, String relatedTerm)
      throws WebODEException, RemoteException
  {
    ODEService odeService=null;
    try {
      odeService=(ODEService)this.context.getService(((OntoCleanServiceConfiguration) config).odeService);

      Term t = odeService.getTerm(ontology,term);
      if (parent == null)
        odeService.removeTerm (ontology, term);
      else
      {
        if (relatedTerm == null)
          odeService.removeTerm (ontology, term, parent);
        else
          odeService.removeTerm (ontology, term, parent, relatedTerm);
      }
      if (t.type == TermTypes.CONCEPT)
      {
        odeService.removeTerm(OntoCleanConstants.TOP_LEVEL, term);
      }

    } catch (Exception e) {
      throw new WebODEException ("Error deleting term: " + e.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }


     /*
  * Evaluacion de la ontologia segun la metodologia de OntoClean.
     */
  public ErrorOntoClean [] evaluationOntoClean (String ontology, String user, String id)
      throws WebODEException, RemoteException
  {
    ODEService odeService=null;

    String installdir   = null;
    String exportado    = null;
    StringBuffer buffer = null;
    String predicado    = null;
    int i,j,k;
    int anti_unity = 0,anti_rigid = 0,is_rigid =0,carries_unity = 0;
    int carries_identity = 0 ,supplies_identity = 0,dependent = 0;
    String anti_unity_s = new String();
    String anti_rigid_s = new String();
    String is_rigid_s = new String();
    String carries_unity_s = new String();
    String carries_identity_s = new String();
    String supplies_identity_s = new String();
    String dependent_s = new String();
    String resultado,valorX, valorY,aux;
    Vector errores = new Vector();
    try {
      odeService=(ODEService)this.context.getService(((OntoCleanServiceConfiguration) config).odeService);

      //Ficheros fichero = new Ficheros("../webserver/webode/Prolog-Modules/" + user + ontology + "toplevel.pl");
      //----------------------------------------------------------------------------
      //Hacemos una copia de la top level ontology en prolog para razonar sobre ella.
      //fichero.CopiarFichero("../webserver/webode/Prolog-Modules/" + "toplevel.pl");
      //System.out.println("ONTOLOGIA ANTES: "+"*"+ontology.substring(0,1)+"*");
      while(ontology.substring(0,1).equals(" "))
        ontology = ontology.substring(1,ontology.length());
      System.out.println("ONTOLOGIA: "+"*"+ontology+"*");
      resultado = inferenceService.load(ontology+"toplevel",user, id);
      //System.out.println("ONTOLOGIA DESPUES: "+"*"+ontology.substring(0,1)+"*");
      System.out.println("RESULTADO cargar modulo "+user+ontology+"toplevel"+": "  + resultado);
      //-------------------------------------------------------------------------------
      // Creamos las instancias de property para cada concepto en prolog.
      Term [] concepts = odeService.getTerms(ontology,new int [] {TermTypes.CONCEPT});
      if(concepts != null)
      {
        for(j=0;j<concepts.length;j++)
        {
          aux = concepts[j].term.replace(' ','_').toLowerCase();
          resultado = inferenceService.query("assert(instance_of("+ aux + ","+OntoCleanConstants.PROPERTY+")).\n",user, id);
          System.out.println("assert(instance_of("+ aux + ","+OntoCleanConstants.PROPERTY+")).\n");
          System.out.println(resultado);
          //resultado = inferenceService.query("assert(type_of("+ OntoCleanConstants.PROPERTY + ","+ aux+ ")).\n",user, id);
          //System.out.println("assert(type_of("+ OntoCleanConstants.PROPERTY + ","+ aux+ ")).\n");
          //System.out.println(resultado);
        }
      }
      //------------------------------------------------------------
      //Exportamos a prolog la ontologia que estamos construyendo.
      buffer= prologService.ExportOntologyProlog(ontology,true,null);
      exportado = buffer.toString();
      StringTokenizer cadena = new StringTokenizer(exportado);
      while(cadena.hasMoreTokens())
      {
        predicado=cadena.nextToken(".\n");
        if((predicado.length() > 5 && predicado.substring(0,5).equals("class")) ||
           (predicado.length() > 11 && predicado.substring(0,11).equals("subclass_of")))
        {
          resultado = inferenceService.query("assert("+ predicado + ").\n",user, id);
          System.out.println("assert("+ predicado + ").\n");
          System.out.println(resultado);
        }
      }
      //-----------------------------------------------------------------------------
      //Incluímos los valores de las metapropiedades como atributos de las instancias.
      Instance [] insts = odeService.getInstances(OntoCleanConstants.TOP_LEVEL,ontology,OntoCleanConstants.PROPERTY);
      InstanceAttributeDescriptor[] atrInsts = odeService.getInstanceAttributes(OntoCleanConstants.TOP_LEVEL,OntoCleanConstants.PROPERTY);
      if(insts!=null)
      {
        for(i=0;i<insts.length;i++)
        {
          HashMap hash = odeService.getInstanceValues(OntoCleanConstants.TOP_LEVEL,ontology,insts[i].name);
          anti_unity = 0;
          anti_rigid = 0;
          is_rigid =0;
          carries_unity = 0;
          carries_identity = 0;
          supplies_identity = 0;
          dependent = 0;
          if(atrInsts != null)
          {
            for(j=0;j<atrInsts.length;j++)
            {

              String att1 = new String(atrInsts[j].name);
              String[] value = (String[])hash.get(new InstanceKey (OntoCleanConstants.PROPERTY,att1));
              if(att1.equals(OntoCleanConstants.ATT_CARRIES_ANTI_UNITY))
              {
                anti_unity = anti_unity+1;
                anti_unity_s=value[0];
              }
              else if(att1.equals(OntoCleanConstants.ATT_CARRIES_UNITY))
              {
                carries_unity = carries_unity+1;
                carries_unity_s=value[0];
              }
              else if(att1.equals(OntoCleanConstants.ATT_IS_DEPENDENT))
              {
                dependent = dependent+1;
                dependent_s= value[0];
              }
              else if(att1.equals(OntoCleanConstants.ATT_ANTI_RIGID))
              {
                anti_rigid = anti_rigid+1;
                anti_rigid_s= value[0];
              }
              else if(att1.equals(OntoCleanConstants.ATT_CARRIES_IDENTITY_CRITERION))
              {
                carries_identity = carries_identity+1;
                carries_identity_s= value[0];
              }
              else if(att1.equals(OntoCleanConstants.ATT_SUPPLIES_IDENTITY_CRITERION))
              {
                supplies_identity = supplies_identity+1;
                supplies_identity_s= value[0];
              }
              else if(att1.equals(OntoCleanConstants.ATT_IS_RIGID))
              {
                is_rigid = is_rigid + 1;
                is_rigid_s = value[0];
              }
              if(anti_unity == 0)
                anti_unity_s="no_value";
              if(anti_rigid == 0)
                anti_rigid_s="no_value";
              if(is_rigid ==0)
                is_rigid_s="no_value";
              if(carries_unity == 0)
                carries_unity_s="no_value";
              if(carries_identity == 0)
                carries_identity_s="no_value";
              if(supplies_identity == 0)
                supplies_identity_s="no_value";
              if(dependent == 0)
                dependent_s="no_value";
              //String[] value = (String[])hash.get(new InstanceKey (OntoCleanConstants.PROPERTY,att1));
              //String att2 = new String(att1.replace(' ','_'));
              //aux = insts[i].name.replace(' ','_').toLowerCase();
              //resultado = inferenceService.query("assert(value_of_facet_of("+value[0]+",value,"+att2+","+aux+ ")).\n",user, id);
              //System.out.println("assert(value_of_facet_of("+value[0]+",value,"+att2+","+insts[i].name+ ")).\n");
              //System.out.println(resultado);
            }
          }
          aux = insts[i].name.replace(' ','_').toLowerCase();
          resultado = inferenceService.query("assert(value_of_mp("+aux+","+anti_unity_s+","+carries_unity_s+","+dependent_s+","+anti_rigid_s+","+carries_identity_s+","+supplies_identity_s+","+is_rigid_s+")).\n",user,id);
          System.out.println("assert(value_of_mp("+insts[i].name+","+anti_unity_s+","+carries_unity_s+","+dependent_s+","+anti_rigid_s+","+carries_identity_s+","+supplies_identity_s+","+is_rigid_s+")).\n");
          System.out.println(resultado);
        }
      }
      //-----------------------------------------------------------------------------
      // Consultamos si los valores de las metapropiedades son correctos.
      // Si alguna metapropiedad tiene varios valores -> ERROR (CONTRADICCIÓN)
      //-----------------------------------------------------------------------------
      for(int indice=0; indice < Predicates.PREDICATES.length;indice++)
      {
        resultado = inferenceService.query(Predicates.PREDICATES[indice],user, id);
        String resultado1 = resultado.replace(',',' ');
        StringTokenizer list_result = new StringTokenizer(resultado1.substring(0,resultado.length()-3));
        list_result.nextToken("\n");
        while(list_result.hasMoreTokens())
        {
          valorX=list_result.nextToken("\n");
          valorY=list_result.nextToken("\n");
          ErrorOntoClean err = new ErrorOntoClean(valorY.substring(4),valorX.substring(4),indice+1);
          errores.add(err);
        }
      }
      ErrorOntoClean [] lista_errores = new ErrorOntoClean[errores.size()];
      errores.copyInto(lista_errores);
      Ficheros fichero_aux1 = new Ficheros(System.getProperty(MinervaServerConstants.MINERVA_HOME) + "webserver" +  File.separator + "webode" +  File.separator + "Prolog-Modules" +  File.separator + user + ontology + "toplevel.itf");
      fichero_aux1.BorrarFichero();
      Ficheros fichero_aux2 = new Ficheros(System.getProperty(MinervaServerConstants.MINERVA_HOME) + "webserver" +  File.separator + "webode" +  File.separator + "Prolog-Modules" +  File.separator + user + ontology + "toplevel.po");
      fichero_aux2.BorrarFichero();
      return lista_errores;
    } catch (Exception e) {
      throw new WebODEException ("Error evaluating by OntoClean: " + e.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }

     /*
  * Modificar el modo de evaluacion.
     */
  public void changeEvaluationMode () throws WebODEException, RemoteException
  {
    try {
      if (EvaluationMode.evaluationMode == EvaluationModeTypes.SYNCHRONOUS)
        EvaluationMode.evaluationMode = EvaluationModeTypes.ASYNCHRONOUS;
      else
        EvaluationMode.evaluationMode = EvaluationModeTypes.SYNCHRONOUS;
    } catch (Exception e) {
      throw new WebODEException ("Error changing evaluation mode: " + e.getMessage());
    }
  }

     /*
  * Obtener el modo de evaluacion.
     */
  public String getEvaluationMode () throws WebODEException, RemoteException
  {
    try {
      if (EvaluationMode.evaluationMode == EvaluationModeTypes.SYNCHRONOUS)
        return "Synchronous";
      else
        return "Asynchronous";
    } catch (Exception e) {
      throw new WebODEException ("Error getting evaluation mode: " + e.getMessage());
    }
  }

     /*
  * Establecer el modo de evaluacion dado un predefinido.
     */
  public void putEvaluationMode (int mode) throws WebODEException, RemoteException
  {
    try {
      EvaluationMode.evaluationMode = mode;
    } catch (Exception e) {
      throw new WebODEException ("Error putting evaluation mode: " + e.getMessage());
    }
  }

  public void insertMetaproperties(String ontology,String name, String mp)
      throws RemoteException, WebODEException
  {
    ODEService odeService=null;
    int i;
    int anti_rigid = 0,anti_unity=0,supplies_identity=0,carries_identity=0,is_dependent=0;
    int supplies_identity_yes=0,carries_identity_yes=0,supplies_identity_no=0,carries_identity_no=0;
    try {
      odeService=(ODEService)this.context.getService(((OntoCleanServiceConfiguration) config).odeService);

      if(!mp.equals(" "))
      {
        for(i=0;i<mp.length()-1;i=i+2)
        {
          String aux = mp.substring(i,i+2);
          if(aux.equals(OntoCleanConstants.IS_RIGID))
          {
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_ANTI_RIGID, "false");
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_IS_RIGID, "true");
            anti_rigid++;
          }
          else if(aux.equals(OntoCleanConstants.IS_NOT_RIGID))
          {
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_ANTI_RIGID, "false");
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_IS_RIGID, "false");
            anti_rigid++;
          }
          else if(aux.equals(OntoCleanConstants.ANTI_RIGID))
          {
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_ANTI_RIGID, "true");
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_IS_RIGID, "false");
            anti_rigid++;
          }
          else if(aux.equals(OntoCleanConstants.NO_CARRIES_UNITY))
          {
            anti_unity++;
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_UNITY, "false");
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_ANTI_UNITY, "false");
          }
          else if(aux.equals(OntoCleanConstants.CARRIES_UNITY))
          {
            anti_unity++;
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_UNITY, "true");
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_ANTI_UNITY, "false");
          }
          else if(aux.equals(OntoCleanConstants.ANTI_UNITY))
          {
            anti_unity++;
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_ANTI_UNITY, "true");
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_UNITY, "false");
          }
          else if(aux.equals(OntoCleanConstants.SUPPLIES_IDENTITY_CRITERION))
          {
            supplies_identity++;
            supplies_identity_yes++;
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_SUPPLIES_IDENTITY_CRITERION, "true");

          }
          else if(aux.equals(OntoCleanConstants.NO_SUPPLIES_IDENTITY_CRITERION))
          {
            supplies_identity++;
            supplies_identity_no++;
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_SUPPLIES_IDENTITY_CRITERION, "false");
          }
          else if(aux.equals(OntoCleanConstants.CARRIES_IDENTITY_CRITERION))
          {
            carries_identity++;
            carries_identity_yes++;
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_IDENTITY_CRITERION, "true");
          }
          else if(aux.equals(OntoCleanConstants.NO_CARRIES_IDENTITY_CRITERION))
          {
            carries_identity++;
            carries_identity_no++;
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_IDENTITY_CRITERION, "false");
          }
          else if(aux.equals(OntoCleanConstants.IS_DEPENDENT))
          {
            is_dependent++;
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_IS_DEPENDENT, "true");
          }
          else if(aux.equals(OntoCleanConstants.IS_NOT_DEPENDENT))
          {
            is_dependent++;
            odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_IS_DEPENDENT, "false");
          }
        }
      }
      if(carries_identity_no == 1 && supplies_identity_no ==0)
      {
        supplies_identity_no++;
        supplies_identity++;
        odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_SUPPLIES_IDENTITY_CRITERION, "false");
      }
      if( supplies_identity_yes==1 && carries_identity_yes == 0)
      {
        carries_identity_yes++;
        carries_identity++;
        odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_IDENTITY_CRITERION, "true");
      }
      if(anti_rigid == 0)
      {
        odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_ANTI_RIGID, "no_value");
        odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_IS_RIGID, "no_value");
      }
      if(anti_unity == 0)
      {
        odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_ANTI_UNITY, "no_value");
        odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_UNITY, "no_value");
      }
      if(supplies_identity == 0)
        odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,"supplies identity criterion", "no_value");
      if(carries_identity == 0)
        odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_CARRIES_IDENTITY_CRITERION, "no_value");
      if(is_dependent == 0)
        odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,OntoCleanConstants.ATT_IS_DEPENDENT, "no_value");
      InstanceAttributeDescriptor[] atrInsts = odeService.getInstanceAttributes(OntoCleanConstants.TOP_LEVEL,OntoCleanConstants.PROPERTY);
      for(int r=0;r<atrInsts.length;r++)
      {
        if(!atrInsts[r].name.equals(OntoCleanConstants.ATT_SUPPLIES_IDENTITY_CRITERION)
           && !atrInsts[r].name.equals(OntoCleanConstants.ATT_CARRIES_ANTI_UNITY)
           && !atrInsts[r].name.equals(OntoCleanConstants.ATT_ANTI_RIGID)
           && !atrInsts[r].name.equals(OntoCleanConstants.ATT_CARRIES_IDENTITY_CRITERION)
           && !atrInsts[r].name.equals(OntoCleanConstants.ATT_IS_DEPENDENT)
           && !atrInsts[r].name.equals(OntoCleanConstants.ATT_CARRIES_UNITY)
           && !atrInsts[r].name.equals(OntoCleanConstants.ATT_IS_RIGID))
          odeService.addValueToInstance(OntoCleanConstants.TOP_LEVEL,ontology, name, OntoCleanConstants.PROPERTY,atrInsts[r].name, "no_value");
      }
    } catch (Exception e) {
      throw new WebODEException ("Error inserting values to metaproperties: " + e.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }


  public String [] getStringValues (String ontology) throws WebODEException, RemoteException
  {
    ODEService odeService=null;
    Vector v = new Vector();
    int i,j;
    int anti_unity=0,carries_unity=0,anti_rigid=0,is_rigid=0;
    try {
      odeService=(ODEService)this.context.getService(((OntoCleanServiceConfiguration) config).odeService);

      Instance [] insts = odeService.getInstances(OntoCleanConstants.TOP_LEVEL,ontology,OntoCleanConstants.PROPERTY);
      InstanceAttributeDescriptor[] atrInsts = odeService.getInstanceAttributes(OntoCleanConstants.TOP_LEVEL,OntoCleanConstants.PROPERTY);
      if(insts!=null)
      {
        for(i=0;i<insts.length;i++)
        {
          String resultado = new String(insts[i].name + "*");
          HashMap hash = odeService.getInstanceValues(OntoCleanConstants.TOP_LEVEL,ontology,insts[i].name);
          if(atrInsts != null)
          {
            for(j=0;j<atrInsts.length;j++)
            {
              String att1 = new String(atrInsts[j].name);
              String[] value = (String[])hash.get(new InstanceKey (OntoCleanConstants.PROPERTY,att1));
              if(atrInsts[j].name.equals(OntoCleanConstants.ATT_CARRIES_IDENTITY_CRITERION))
              {
                if(value[0].equals(OntoCleanConstants.F))
                  resultado = resultado + OntoCleanConstants.NO_CARRIES_IDENTITY_CRITERION;
                else if  (value[0].equals(OntoCleanConstants.T))
                  resultado = resultado + OntoCleanConstants.CARRIES_IDENTITY_CRITERION;
              }
              else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_CARRIES_ANTI_UNITY))
              {
                if(value[0].equals(OntoCleanConstants.T))
                  anti_unity=1;
              }
              else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_CARRIES_UNITY))
              {
                if(value[0].equals(OntoCleanConstants.F))
                  carries_unity=-1;
                else if(value[0].equals(OntoCleanConstants.T))
                  carries_unity=1;
              }
              else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_SUPPLIES_IDENTITY_CRITERION))
              {
                if(value[0].equals(OntoCleanConstants.F))
                  resultado = resultado + OntoCleanConstants.NO_SUPPLIES_IDENTITY_CRITERION ;
                else if(value[0].equals(OntoCleanConstants.T))
                  resultado = resultado + OntoCleanConstants.SUPPLIES_IDENTITY_CRITERION ;
              }
              else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_IS_DEPENDENT))
              {
                if(value[0].equals(OntoCleanConstants.F))
                  resultado = resultado + OntoCleanConstants.IS_NOT_DEPENDENT ;
                else if(value[0].equals(OntoCleanConstants.T))
                  resultado = resultado + OntoCleanConstants.IS_DEPENDENT ;
              }
              else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_ANTI_RIGID))
              {
                if(value[0].equals(OntoCleanConstants.T))
                  anti_rigid=1;
              }
              else if(atrInsts[j].name.equals(OntoCleanConstants.ATT_IS_RIGID))
              {
                if(value[0].equals(OntoCleanConstants.F))
                  is_rigid = -1;
                else if(value[0].equals(OntoCleanConstants.T))
                  is_rigid = 1;
              }
            }
            if(anti_unity == 1)
              resultado = resultado + OntoCleanConstants.ANTI_UNITY;
            else
            {
              if(carries_unity == 1)
                resultado = resultado + OntoCleanConstants.CARRIES_UNITY;
              else if (carries_unity == -1)
                resultado = resultado + OntoCleanConstants.NO_CARRIES_UNITY;
            }
            if(anti_rigid == 1)
              resultado = resultado + OntoCleanConstants.ANTI_RIGID;
            else
            {
              if(is_rigid == 1)
                resultado = resultado + OntoCleanConstants.IS_RIGID;
              else if (is_rigid == -1)
                resultado = resultado + OntoCleanConstants.IS_NOT_RIGID;
            }
            anti_unity = 0;
            carries_unity = 0;
            anti_rigid=0;
            is_rigid=0;
          }
          v.add(resultado);
        }
      }
      String [] lista = new String[v.size()];
      v.copyInto(lista);
      return lista;
    }catch (Exception e) {
      throw new WebODEException ("Error getting string for metaproperties: " + e.getMessage());
    }
    finally {
      if(odeService!=null) odeService.disconnect();
    }
  }
}