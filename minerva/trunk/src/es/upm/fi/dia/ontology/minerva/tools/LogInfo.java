package es.upm.fi.dia.ontology.minerva.tools;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.text.ParseException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class LogInfo {
  protected Date date;
  protected String threadName;
  protected int type;
  protected static MessageFormat msgInfo=null;
  protected static String[] ATYPE=new String[] {
    es.upm.fi.dia.ontology.minerva.server.builtin.AbstractLog.INFORMATIVE,
    es.upm.fi.dia.ontology.minerva.server.builtin.AbstractLog.DEBUG,
    es.upm.fi.dia.ontology.minerva.server.builtin.AbstractLog.ERROR};

  private static synchronized void init() {
    if(msgInfo==null)
      msgInfo=new MessageFormat("{0,date,dd MMM yyyy HH:mm:ss,SSS}: [{1}] {2,choice,0#INFO|1#DEBUG|2#ERROR}  {3}");
  }

  public Date getDate() {
    return this.date;
  }

  public void setDate(Date date) {
    this.date=date;
  }

  public String getThreadName() {
    return this.threadName;
  }

  public void setThreadName(String thread) {
    this.threadName=thread;
  }

  public int getType() {
    return this.type;
  }

  public void setType(int type) {
    this.type=type;
  }

  public String getTypeString() {
    return ATYPE[this.type].trim();
  }

  public abstract String getInfo();

  public abstract void setInfo(String info) throws ParseException;

  public String toString() {
    init();
    return msgInfo.format(new Object[]{this.getDate(), this.getThreadName(), new Integer(this.getType()), this.getInfo()});
  }
}