package es.upm.fi.dia.ontology.webode.Inference;

import java.io.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.others.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;
import es.upm.fi.dia.ontology.webode.translat.Prolog.*;
import java.rmi.*;
import es.upm.fi.dia.ontology.minerva.server.services.CannotStartException;

public class InferenceServiceImp extends MinervaServiceImp implements InferenceService
{
  private PrologExportService prolog;

  private String[] errors=null;
  private boolean isCorrect;
  private javax.swing.Timer[] timers;
  private boolean isdead = true;
  public final static int ONE_SECOND = 6000;
  public static final String PROLOG_MODULES="Prolog-Modules";

// Variables para el control de los predicados cargados así como para saber de donde se lee: err o in
  private ReadThread[] inth, errth;
  private boolean error = false;
  private String text;

//Variable que contiene el directorio de instalación.
  private File MODULE_DIR = null;

  private String prologPath = null;

// Variables para el control de usuarios
  private String[] sessions;
  private ProcConnection[] procs;
  private int connections = 0;

  /****************************/
  private ProcConnection proc;
  /****************************/

  public InferenceServiceImp() throws RemoteException {};

  public void start() throws CannotStartException
  {
    System.out.print("Launching Inference Service............");
    String prologService = ((InferenceServiceConfiguration) config).prologService;
    connections = Integer.parseInt( ((InferenceServiceConfiguration) config).connections );
    prologPath = ((InferenceServiceConfiguration) config).prologPath;

    MODULE_DIR=new File(new File(System.getProperty(MinervaServerConstants.MINERVA_HOME)),PROLOG_MODULES);

    for(int i=0;i<prologPath.length();i++){
      if (prologPath.charAt(i)=='\\') {prologPath=prologPath.substring(0,i)+"\\\\"+prologPath.substring(i+1,prologPath.length());i++;};
    }
    System.out.println("Inference Service with prologPath: " + prologPath);

    if (prologService == null)
      throw new CannotStartException ("No prolog traslator specified ");
    else if (connections == 0)
      throw new CannotStartException ("No connections ");
    else if (prologPath == null)
      throw new CannotStartException ("Prolog path not especificated ");

    try
    {
      prolog= (PrologExportService)context.getService (prologService);
      System.out.print("........................  ");
    }catch (Exception e){
      throw new CannotStartException ("Cannot obtain the prolog export service:" + e);
    }

    try {
      //Inicializamos todos los vectores
      sessions =  new String [connections];
      procs = new ProcConnection [connections];
      inth = new ReadThread[connections];
      errth = new ReadThread[connections];
      inicializa();

      System.out.println("     OK!");
      System.out.println("Inference Service Started.");
      System.out.println("Number of connections: " + connections + ".");
    } catch (Exception e) {
      System.err.println("Problems starting Prolog Server: " + e);
      e.printStackTrace();
      System.exit(1);
    }
  }

  public String[] list(String user, String id, String ontology) throws IOException, RemoteException
  {
    int pos;
    //Se quita el IF para corregir el error de que no cargue diferentes ontologías en una misma sesion
    if(checkId(id))
    {
      if(connections >0)
      {
        connections--;
        pos = findPos();
        sessions[pos] = id;
        procs[pos] = create_proc(prologPath, user, id, pos);

        //Se comentan las dos lineas siguientes debido al fallo de Oracle
        writeOnto(user, ontology);
        loadOnto(ontology, user, id);
      }
    } else
    {
      //se sobrescribe la posicion
      pos = getUserIndex(id);
      procs[pos].kill();
      inth[pos].kill() ;
      errth[pos].kill();
      procs[pos] = create_proc(prologPath, user, id, pos);
      writeOnto(user, ontology);
      loadOnto(ontology, user, id);
    }
    String [] files = MODULE_DIR.list();
    return files;
  }

/*public void reserve(String text, String user, String id) throws IOException, RemoteException{

 String head;

// Hay que poner el user para que podamos cargarlo tras salvarlo.
 this.text = text;
 System.out.println("El modulo que se intenta reservar es: ");
 System.out.println("Contenido: \n" + text);
}
*/

  public void save(String module, String text, String user) throws IOException, RemoteException{

    String head;
    // Hay que poner el user para que podamos cargarlo tras salvarlo.
    System.out.println("El modulo que se intenta salvar es: " + module);
    System.out.println("Contenido: \n" + text);

    FileOutputStream file = new FileOutputStream(new File(MODULE_DIR, module + ".pl"));
    file.write(text.getBytes());
    file.flush();
    file.close();
  }

  public String load(String module, String user, String id) throws IOException, RemoteException{

    System.out.println("User " + user + " is loading module " + module);
    File file = new File(MODULE_DIR, user + module + ".pl");
    File fileitf = new File(MODULE_DIR, user + module + ".itf");
    File filepo = new File(MODULE_DIR, user + module + ".po");
    int indice = getUserIndex(id);
    int pos;

    if(indice == -1)
    {
      //return "Error: User " + user + " does not have permission.";
      // Codigo incluido para OntoClean (9 Octubre 2002)--------------------------------
      if(connections >0)
      {
        connections--;
        pos = findPos();
        sessions[pos] = id;
        procs[pos] = create_proc(prologPath, user, id, pos);
        indice = pos;
      }
      //---------------------------------------------------------------------------------
    }
    fileitf.delete();
    filepo.delete();

    if(file.exists()) {
      ProcConnection proc = procs[indice];
      proc.write("ensure_loaded('" + (new File(MODULE_DIR, user + module)).getPath().replace('\\','/') + "').\n");
      return proc.readThreads();
    } else {
      return "Error: File Module does not exist.";
    }
  }

  private String loadOnto(String ontology, String user, String id) throws IOException, RemoteException{

    System.out.println("User " + user + " is loading ontology " + ontology);
    File file = new File(MODULE_DIR, "o" + user + ontology + ".pl");
    int indice = getUserIndex(id);

    if(indice == -1){
      return "Error: User " + user + " does not have permission.";
    }

    if(file.exists()) {
      ProcConnection proc = procs[indice];
      proc.write("ensure_loaded('" + file.getPath().replace('\\','/') + "').\n");
      System.out.println("User " + user + " has loaded ontology " + ontology);
      return proc.readThreads();
    } else {
      return "Error: Ontology does not exist.";
    }
  }

  public String query(String squery, String user, String id) throws IOException, RemoteException{

    String response = null;
    int indice  = getUserIndex(id);
    if(indice == -1){
      return "Error: User " + user + " does not have permission.";
    }

    ProcConnection proc = procs[indice];
    try {
      proc.write(squery);
      return proc.readThreads();
    }catch(Exception e){
      System.out.println("Error: Query error");
      return "Fatal error";
    }
  }

  private ProcConnection create_proc(String prologPath, String user, String id, int pos) throws IOException
  {
    Process proc = null;
    InputStreamReader in,err;
    OutputStreamWriter out;
    PipedReader pipeIn;
    PipedWriter pipeOut;

    String [] cmd = new String [5];
//	String wdir = "\"" + prologPath + "\\shell" + "\"";
    String wdir = prologPath + "\\shell";

//	cmd[0] = "\"" + prologPath + "\\Win32\\bin\\ciaoengine.exe\"";
    cmd[0] = prologPath + "\\Win32\\bin\\ciaoengine.exe";
//	cmd[0] = "ciaoengine.exe";
//	cmd[0] = "pepe";
    cmd[1] = "-C";
    cmd[2] = "-i";
    cmd[3] = "-b";
    cmd[4] = "$/shell/ciaosh";

//	String [] cmd = new String [2];
//	cmd[0] = "dir";
//	cmd[1] = "/P";

    try
    {
      System.out.println("About to start a process with the following command:");
      System.out.println("" + cmd[0] + "::" + cmd[1] + "::" + cmd[2] + "::" + cmd[3] + "::" + cmd[4]);
//		System.out.println("" + cmd[0] + "::" + cmd[1]);
      System.out.println("wdir: " + wdir);
      try{
//			proc = (Runtime.getRuntime()).exec(cmd, null, new File(wdir));
        proc = (Runtime.getRuntime()).exec(cmd);
      } catch(Throwable t) {
        System.out.println("Error: Throwable error" + t.getMessage());
      };
      System.out.println("Process started");

      in = new InputStreamReader(proc.getInputStream());
      err = new InputStreamReader(proc.getErrorStream());
      out = new OutputStreamWriter(proc.getOutputStream());

      pipeIn = new PipedReader();
      pipeOut = new PipedWriter(pipeIn);

      ProcConnection shell = new ProcConnection(proc, in, out, pipeIn);
      inth[pos] = new ReadThread(in, pipeOut);
      errth[pos] = new ReadThread(err, pipeOut);
      shell.readStart();
      inth[pos].start();

      errth[pos].start();

  /* Ya cargamos el motor*/
      File file3 = new File(MODULE_DIR, "iengine.pl");
      if(file3.exists())
      {
        //read_primitives(file3, "iengine", getUserIndex(id));
        shell.write("ensure_loaded('" + (new File(MODULE_DIR, "iengine")).getPath().replace('\\','/') + "').\n");
        System.out.println(shell.readThreads());
      } else
      {
        System.out.println("Error. File Module doesn't exist.");
      }

      System.out.print("Checking new session...");
      System.out.println("   OK!");
      System.out.println("Number of connections: " + connections + ".");

      return shell;
    } catch(Exception e) {

      System.out.println("Error: Cannot create process");
      return null;
    }
  }

  private int getUserIndex(String id) {
    for(int i=0;i<connections;i++)
    {
      if(id.equals(sessions[i]))
        return i;
    }
    return -1;
  }

  private boolean checkId(String id) {
    if(getUserIndex(id) == -1)
      return true;
    else
      return false;
  }

  private void writeOnto(String user, String ontology) throws IOException {
    try {
      StringBuffer buf = prolog.ExportOntologyProlog(ontology, true, null);
      FileOutputStream file = new FileOutputStream(new File(MODULE_DIR,  "o" + user + ontology + ".pl"));
      String text = buf.toString();
      file.write(text.getBytes());
      file.flush();
      file.close();
    } catch(Exception e) {
      System.out.println("Error: Error traduciendo la ontología");
    }
  }

  private int findPos() {
    for(int i=0;i<connections;i++)
    {
      if(sessions[i]==null)
      {
      return i;
    }
    }
    return -1;
  }

  private void inicializa() {
    //Inicializamos todos los vectores
    for(int i=0;i<connections;i++)
    {
      sessions[i] =  null;
      inth[i] = null;
      errth[i] = null;
      procs[i] = null;
    }
  }
}