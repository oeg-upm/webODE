package es.upm.fi.dia.ontology.minerva.server.builtin;

// Java stuff
import java.util.*;
import java.io.*;
import java.rmi.*;

// own stuff
import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * This class provides a manager for the log service.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.1
 */
public class LogServiceManager extends MinervaManagerImp
{
  public static final String INFORMATIVE_STR = "Informative";
  public static final String DEBUG_STR       = "Debug";
  public static final String ERROR_STR       = "Error";
  public static final String LEVEL           = "Level";
  public static final String PREFFIX         = "Preffix";
  public static final String SUFFIX          = "Suffix";

  public LogServiceManager () throws RemoteException
  {
  }

  /**
   * This method returns information about the management attributes
   * of the log service.
   *
   * @return A <tt>MinervaServiceDescriptor</tt> providing information about the log service.
   */
  public MinervaServiceDescription getServiceDescription () throws RemoteException
  {
    // This should read the configuration file and current values...<<<<<<<<<<<<<<<

    return new MinervaServiceDescription
    ("Log service", "Log service responsible for all the logging in the server.",
     new MinervaAttributeDescription[] {
        new MinervaAttributeDescription (LEVEL,
                                         "The log level", String.class,
                                         new Object[] { INFORMATIVE_STR, ERROR_STR, DEBUG_STR },
                                         ((LogConfiguration) serviceConfig).level == LogService.INFORMATIVE_LEVEL ?
                                         INFORMATIVE_STR :
                                         (((LogConfiguration) serviceConfig).level == LogService.DEBUG_LEVEL ?
                                         DEBUG_STR : ERROR_STR)),
        new MinervaAttributeDescription (PREFFIX,
                                         "The preffix to be put in the log filenames",
                                         String.class,
                                         null,
                                         ((LogConfiguration) serviceConfig).preffix),
        new MinervaAttributeDescription (SUFFIX,
                                         "The suffix to be put in the log filenames",
                                         String.class,
                                         null,
                                         ((LogConfiguration) serviceConfig).suffix),
     }
    );
  }

  /**
   * This method is used if no UI has been defined over the service.
   *
   * @param attrName  Name of the configurable attribute.
   * @param attrValue The attribute's value.
   */
  public void setAttribute (String attrName, Object attrValue)
      throws NoSuchAttributeException, RemoteException {
    LogConfiguration lsc = (LogConfiguration) serviceConfig;

    if (attrName.equals (LEVEL)) {
      if (attrValue.equals (INFORMATIVE_STR)) {
        lsc.level = LogService.INFORMATIVE_LEVEL;
      }
      else if (attrValue.equals (DEBUG_STR)) {
        lsc.level = LogService.DEBUG_LEVEL;
      }
      else if (attrValue.equals (ERROR_STR)) {
        lsc.level = LogService.ERROR_LEVEL;
      }
      else
        throw new NoSuchAttributeException ("The attribute " + attrName + " does not have a value of " +
            attrValue + ".");
    }
    else if (attrName.equals (PREFFIX)) {
      lsc.preffix = attrValue.toString();
    }
    else if (attrName.equals (SUFFIX)) {
      lsc.suffix = attrValue.toString();
    }
    else
      throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
  }
}

