package es.upm.fi.dia.ontology.minerva.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Comparator;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.text.ParseException;
import java.text.MessageFormat;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class LogAnalyzer {
  class LogEnumeration implements Enumeration {
    protected String lastLine;
    protected java.io.BufferedReader fin;
    protected LogInfo info=null;
    protected LogInfoManager infoManager;

    LogEnumeration(File file, LogInfoManager infoManager) throws FileNotFoundException {
      fin=new BufferedReader(new FileReader(file));
      this.infoManager=infoManager;

      try {
        this.lastLine=fin.readLine();
        if(lastLine!=null)
          info=infoManager.getInfo(lastLine);
        else
          info=null;
      }
      catch(IOException e) {
        e.printStackTrace();
        info=null;
      }
      catch(ParseException e) {
        e.printStackTrace();
        info=null;
      }
    }

    public boolean hasMoreElements() {
      return info!=null;
    }

    public Object nextElement() throws NoSuchElementException {
      LogInfo res=null;
      try {
        res=this.info;
        this.lastLine=fin.readLine();
        try {
          if(lastLine!=null) {
            info=infoManager.getInfo(lastLine);
          }
          else
            info=null;
        }
        catch(ParseException e) {
          throw (NoSuchElementException)(new NoSuchElementException(e.getMessage())).initCause(e);
        }
      }
      catch(IOException e) {
        throw (NoSuchElementException)(new NoSuchElementException(e.getMessage())).initCause(e);
      }
      return res;
    }

    public String getLine() {
      return this.lastLine;
    }
  }

  class LogFile implements FileFilter {
    private MessageFormat lff;
    private String prefix;
    private String suffix;

    LogFile(String prefix, String suffix) {
      this.prefix=prefix;
      this.suffix=suffix;
      this.lff=new MessageFormat(prefix+suffix+"."+"{0,number,integer}");
    }

    public boolean accept(java.io.File file) {
      if(file.isDirectory())
        return false;
      try {
        if(file.getName().equals(prefix+suffix))
          return true;
        Object[] objs=lff.parse(file.getName());
        return objs[0]!=null;
      }
      catch(ParseException pe) {
        return false;
      }
    }
  }

  class LogFileComparator implements Comparator {
    private MessageFormat lff;
    private String prefix;
    private String suffix;

    LogFileComparator(String prefix, String suffix) {
      this.prefix=prefix;
      this.suffix=suffix;
      this.lff=new MessageFormat(prefix+suffix+"."+"{0,number,integer}");
    }

    public int compare(Object o1, Object o2) {
      try {
        int idx1;
        if(((File)o1).getName().equals(prefix+suffix))
          idx1=0;
        else
          idx1=((Number)lff.parse(((File)o1).getName())[0]).intValue();
        int idx2;
        if(o2.toString().equals(prefix+suffix))
          idx2=0;
        else
          idx2=((Number)lff.parse(((File)o2).getName())[0]).intValue();
        return (idx1<idx2)?-1:+1;
      }
      catch(java.text.ParseException pe) {
        return 0;
      }
    }
  }

  public class LogFileEnumeration implements Enumeration {
    private File directory;
    private String prefix;
    private String suffix;
    private int actualIndex;
    private LogEnumeration logEnum=null;
    private String lastLine=null;
    private File lastFile=null;
    private LogInfoManager infoManager;

    private void init(java.io.File directory, String prefix, String suffix, int index) throws FileNotFoundException {
      if(!directory.isDirectory())
        throw new FileNotFoundException("'" + directory + "' is not a directory.");
      String strFile;
      if(index==0)
        strFile=prefix+suffix;
      else
        strFile=prefix+suffix+"."+index;
      File file=new File(directory,strFile);
      if(!file.exists())
        throw new FileNotFoundException("File '" + file + "' does not exists.");
      this.directory=directory;
      this.prefix=prefix;
      this.suffix=suffix;
      this.actualIndex=index;
      logEnum=new LogEnumeration(file, infoManager);
      lastLine=logEnum.getLine();
      lastFile=file;
    }

    LogFileEnumeration (java.io.File directory, String prefix, String suffix, int index, LogInfoManager infoManager) throws FileNotFoundException {
      this.infoManager=infoManager;
      init(directory,prefix,suffix,index);
    }

    LogFileEnumeration (java.io.File directory, String prefix, String suffix, LogInfoManager infoManager) throws FileNotFoundException {
      this.infoManager=infoManager;
      if(!directory.isDirectory())
        throw new FileNotFoundException("'" + directory + "' is not a directory.");
      File[] files=directory.listFiles(new LogFile(prefix,suffix));
      logEnum=null;

      if(files!=null) {
        Arrays.sort(files,new LogFileComparator(prefix,suffix));

        java.text.MessageFormat lff=new java.text.MessageFormat(prefix+suffix+"."+"{0,number,integer}");
        if(files[0].getName().equals(prefix+suffix))
          init(directory,prefix,suffix,0);
        else {
          try {
            Object[] objs=lff.parse(files[0].getName());
            init(directory,prefix,suffix,((Number)objs[0]).intValue());
          }
          catch(ParseException pe) {
            logEnum=null;
          }
        }
      }
    }

    public boolean hasMoreElements() {
      if(logEnum==null)
        return false;

      if(logEnum.hasMoreElements())
        return true;
      else {
        logEnum=null;
        try {
          String fileName;
          File file=new java.io.File(directory,prefix+suffix+"."+(++actualIndex));
          if(!file.exists())
            return false;
          lastFile=file;
          logEnum=new LogEnumeration(file, infoManager);
          lastLine=logEnum.getLine();
          return logEnum.hasMoreElements();
        }
        catch(Exception e) {
          logEnum=null;
          return false;
        }
      }
    }

    public Object nextElement() throws NoSuchElementException {
      hasMoreElements();
      if(logEnum==null)
        throw new NoSuchElementException();
      Object res=logEnum.nextElement();
      lastLine=logEnum.getLine();
      return res;
    }

    public String getLine() {
      return this.lastLine;
    }

    public File getFile() {
      return this.lastFile;
    }
  }

  protected LogInfoManager infoManager;

  public LogAnalyzer(LogInfoManager infoManager) {
    this.infoManager=infoManager;
  }

  public java.util.Enumeration lines(java.io.File file) throws FileNotFoundException {
    return new LogEnumeration(file, infoManager);
  }

  public java.util.Enumeration lines(java.io.File directory, String prefix, String suffix) throws FileNotFoundException {
    return new LogFileEnumeration(directory,prefix,suffix, infoManager);
  }

  public java.util.Enumeration lines(java.io.File directory, String prefix, String suffix, int index) throws FileNotFoundException {
    return new LogFileEnumeration(directory,prefix,suffix,index, infoManager);
  }
}