package es.upm.fi.dia.ontology.minerva.tools;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class LogInfoManager {
  protected abstract LogInfo parseInfo(String info) throws ParseException;

  public static LogInfoManager getDefaultManager() {
    return new LogInfoManager() {
      public LogInfo parseInfo(String line) throws ParseException {
        return new LogInfo() {
          protected String info;

          public String getInfo() {
            return this.info;
          }

          public void setInfo(String info) {
            this.info=info;
          }
        };
      }
    };
  }

  public LogInfo getInfo(String line) throws ParseException {
    MessageFormat msgInfo=new MessageFormat("{0,date,dd MMM yyyy HH:mm:ss,SSS}: [{1}] {2,choice,0#INFO|1#DEBUG|2#ERROR}  {3}");
    Object[] objs=msgInfo.parse(line);
    String info=(String)objs[3];
    LogInfo log=parseInfo(info);

    log.setDate((Date)objs[0]);
    log.setThreadName((String)objs[1]);
    log.setType(((Number)objs[2]).intValue());

    return log;
  }
}