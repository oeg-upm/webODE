package es.upm.fi.dia.ontology.minerva.server.builtin;

// Java stuff
import java.io.*;
import java.rmi.*;

// Minerva stuff
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.services.*;

/**
 * This class defines the basis of the log service.
 *
 * @author   Julio César Arpírez Vega
 * @version  1.0
 */
public abstract class AbstractLog extends MinervaServiceImp implements LogService
{
  /** Prefix for informative messages. */
  public static final String INFORMATIVE = "INFO";
  /** Prefix for error messages. */
  public static final String ERROR       = "ERROR";
  /** Prefix for debug messages. */
  public static final String DEBUG       = "DEBUG";
  /** Default log file size. */
  public static final int    MAX_LENGTH  = 1048576; // 1MB.

  /** The identifier for the log entries. */
  protected String id;
  /** The writer to send entries to. */
  protected PrintWriter pw;
  /** The current log file number. */
  protected int logNumber;
  /** Prefix. */
  protected String prefix;
  /** Suffix */
  protected String suffix;
  /** Log directory. */
  protected File logDirectory;
  /** Level. */
  protected int level;
  /** Maximum length of the log file. */
  protected long maxLength = MAX_LENGTH;

  protected long byteCount = -1;

  /**
   * Needed constructor.
   */
  public AbstractLog () throws RemoteException {}

  /**
   * Sets a unique identifier for log entries.
   *
   * @param id Identifier.
   */
  public void setId (String id)
  {
    this.id = id;
  }

  /**
   * Sets the length of the log file.
   *
   * @param length The length.
   */
  public void setLength (long length)
  {
    this.maxLength = length;
  }

  /**
   * Updates the byteCount.
   */
  protected void updateCount (String str)
  {
    if (str != null)
      byteCount += str.length();
  }

  /**
   * Gets id.
   */
  public String getId ()
  {
    return id;
  }

  protected void _newFile () throws IOException
  {
    if (byteCount < 0 || byteCount > maxLength) {
      if (pw == null) {
        // Discover highest log number file.
        for (;new File (logDirectory, prefix + logNumber + suffix).exists();
             logNumber++);

             if (logNumber > 0 &&
                 new File (logDirectory, prefix + (logNumber - 1) + suffix).length() < maxLength)
               logNumber--;
      }
      else { // Flush file and close.
        pw.flush();
        pw.close();

        logNumber++;
      }

      pw = new PrintWriter (new FileWriter (new File (logDirectory,
          prefix + logNumber + suffix).toString(), true));

      byteCount = 0;
    }
  }


  /**
   * Sets the prefix for the log files.
   * This change will take effect the next time a new file for logging is created.
   *
   * @param prefix The new prefix for the log files.
   */
  public void setPrefix (String prefix)
  {
    if (prefix == null)
      this.prefix = "";
    else
      this.prefix = prefix;
  }

  /**
   * Returns current prefix for log files.
   *
   * @return Current prefix.
   */
  public String getPrefix ()
  {
    return prefix;
  }

  /**
   * Sets the suffix for the log files.
   * This change will take effect the next time a new file for logging is created.
   *
   * @param suffix The new suffix for the log files.
   */
  public void setSuffix (String suffix)
  {
    if (suffix == null)
      suffix = "";
    else
      this.suffix = suffix;
  }

  /**
   * Returns current suffix for log files.
   *
   * @return Current suffix.
   */
  public String getSuffix ()
  {
    return suffix;
  }

  /**
   * Sets the logging level.
   *
   * @param level The new logging level.
   */
  public void setLevel (int level)
  {
    if (level > ERROR_LEVEL)
      throw new IllegalArgumentException
      ("Log level must be one of INFORMATIVE_LEVEL, DEBUG_LEVEL or ERROR_LEVEL.");

    this.level = level;
  }

  /**
   * Gets the logging level.
   *
   * @return The logging level.
   */
  public int getLevel ()
  {
    return level;
  }

  /**
   * Sets a new directory for the log.  This change will take effect the next time a new
   * file for logging is created.
   *
   * @param directory The new directory name.
   */
  public void setDirectory (String directory)
  {
    if (directory == null)
      throw new IllegalArgumentException ("the directoy cannot be null.");

    logDirectory = new File (directory);

    if (!logDirectory.exists()) {
      if (!logDirectory.mkdir())
        System.err.println ("Error creating directory " + directory);
    }
  }

  /**
   * Gets current directory for logging.
   *
   * @return The current log directory.
   */
  public String getDirectory ()
  {
    return logDirectory.toString();
  }
}
