package es.upm.fi.dia.ontology.minerva.server.builtin;

// Java stuff
import java.util.*;
import java.rmi.*;
import java.io.*;

// Log4J
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggerFactory;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * This class provides the implementation for the logger thread.
 * <p>
 * This is responsible for logging all the messages to
 * the appropiate log file.  This file is built using the prefix,
 * the current log file number and the suffix specified.
 * <p>
 * When the size of the log file becomes greater than the value of the
 * <tt>maxLength</tt> property, the file is closed and
 * a new file is opened, thus providing an automatic rolling mechanism.
 *
 * <p>
 * For the time being, this service cannot be stopped.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class LogServiceImp extends AbstractLog implements LogService
{
  /** Initial size for the message vector. */
  public static final int INIT_SIZE = 100;

  protected Properties props;

  /**
   * Constructor for the log service.
   */
  public LogServiceImp() throws RemoteException {}

  /**
   * Sets the log configuration by getting the information from
   * the <tt>LogConfiguration</tt> object passed as an argument.
   *
   * @param mc The <tt>LogConfiguration</tt> object.
   * @see   LogConfiguration
   */
  public void setConfiguration (MinervaServiceConfiguration mc)
  {
    // Parent processing.
    super.setConfiguration (mc);

    // This object is really a LogConfiguration
    LogConfiguration logCfg = (LogConfiguration) mc;

    // set internal parameters
//    setDirectory (logCfg.logDirectory);
    setPrefix    (logCfg.preffix);
    setSuffix    (logCfg.suffix);
    setLevel     (logCfg.level);
  }

  /**
   * Starts the service.
   *
   * @exception CannotStartException In case of an IO error.
   */
  public void start () throws CannotStartException
  {
    super.start();
/*
    try {
      // Create new file for the log.
      _newFile();
    } catch (Exception e) {
      throw new CannotStartException ("cannot start log service: " + e.getMessage(), e);
    }

    // Create buffer.
    v = new Vector (INIT_SIZE);

    Thread th = new Thread (this, "Logger Thread");

    // This thread runs as a daemon because it makes no sense it to be running
    // without other threads working.
    th.setDaemon (true);

    // start thread.
    th.start();
*/
    LogConfiguration cfg=(LogConfiguration)this.config;
    Level lvl;
    switch(cfg.level) {
      case INFORMATIVE_LEVEL:
        lvl=Level.INFO;
        break;
      case DEBUG_LEVEL:
        lvl=Level.DEBUG;
        break;
      case ERROR_LEVEL:
        lvl=Level.ERROR;
        break;
      default:
       lvl=Level.DEBUG;
    }

    Logger logger=Logger.getRootLogger();
    logger.removeAllAppenders();
    String file=System.getProperty("MINERVA_VAR") + File.separator + "log" + File.separator + cfg.preffix + cfg.suffix;
    Layout layout=new PatternLayout("%d{dd MMM yyyy HH:mm:ss,SSS}: [%t] %-5p %c - %m%n");
    Appender appender=null;
    try {
      appender=new RollingFileAppender(layout,file,true);
      ((RollingFileAppender)appender).setMaxFileSize("2MB");
      ((RollingFileAppender)appender).setMaxBackupIndex(10);
      logger.addAppender(appender);
      logger.setLevel(lvl);
    }
    catch(IOException e) {
      throw new CannotStartException("Can not star SeWLog service: " + e.getMessage(), e);
    }
  }

  /**
   * Stops the log service.
   */
  public void stop() {
    logInfo ("log service stopped.");
  }

  protected Logger getLogger() {
    String interfaceName=null;
    try {
      throw new Exception("get stack");
    }
    catch(Exception e) {
      StackTraceElement[] stack=e.getStackTrace();
      if(stack!=null && stack.length>2) {
        try {
          Class[] interfaces;
          for(int i=2; interfaceName==null && i<stack.length; i++) {
            interfaces=Class.forName(stack[i].getClassName()).getInterfaces();
            for(int j=0; interfaces!=null && interfaceName==null && j<interfaces.length; j++) {
              if(MinervaService.class.isAssignableFrom(interfaces[j]))
                interfaceName=interfaces[j].getName();
            }
          }
        }
        catch (Exception ex) {
        }
      }
    }
    Logger logger;
    if(interfaceName!=null) {
      String loggerName=interfaceName.substring(interfaceName.lastIndexOf('.')+1);
      logger=Logger.getLogger(loggerName);
      if(logger==null || !logger.getAllAppenders().hasMoreElements() || !logger.getName().equals(loggerName)) {
        if(logger==null || !logger.getName().equals(loggerName)) {
          Hierarchy hierarchy=new Hierarchy(Logger.getRootLogger());
          logger=hierarchy.getLogger(loggerName);
        }
        LogConfiguration cfg=(LogConfiguration)this.config;
        String file=System.getProperty("MINERVA_VAR") + File.separator + "log" + File.separator + loggerName + cfg.suffix;
        Layout layout=new PatternLayout("%d{dd MMM yyyy HH:mm:ss,SSS}: [%t] %-5p %c - %m%n");
        Appender appender=null;
        try {
          appender=new RollingFileAppender(layout,file,true);
          ((RollingFileAppender)appender).setMaxFileSize("2MB");
          ((RollingFileAppender)appender).setMaxBackupIndex(10);
        }
        catch(IOException e) {
          for(Enumeration enume=Logger.getRootLogger().getAllAppenders(); appender==null && enume.hasMoreElements(); )
            appender=(Appender)enume.nextElement();
        }
        logger.addAppender(appender);
      }
    }
    else
      logger=Logger.getRootLogger();

    return logger;
  }

  /**
   * Writes an entry with an error message.
   *
   * @param msg The message to be written.
   */
  public void logError (String msg) {
    LogConfiguration cfg=(LogConfiguration)this.config;
    if(cfg.level<=LogService.ERROR_LEVEL)
      getLogger().error(msg);
  }

  /**
   * Writes an entry with an error message.
   *
   * @param msg The message to be written.
   * @param th The Throwable to be trace.
   */
  public void logError (String msg, Throwable th)
  {
    LogConfiguration cfg=(LogConfiguration)this.config;
    if(cfg.level<=LogService.ERROR_LEVEL)
      getLogger().error(msg,th);
  }

  /**
   * Writes an entry with a debug message.
   *
   * @param msg The message to be written.
   */
  public void logDebug (String msg)
  {
    LogConfiguration cfg=(LogConfiguration)this.config;
    if(cfg.level<=LogService.DEBUG_LEVEL)
      getLogger().debug(msg);
  }

  /**
   * Writes an entry with an informative message.
   *
   * @param msg The message to be written.
   */
  public void logInfo (String msg)
  {
    LogConfiguration cfg=(LogConfiguration)this.config;
    if(cfg.level<=LogService.INFORMATIVE_LEVEL)
      getLogger().info(msg);
  }

  protected void localDisconnect() {
  }
}