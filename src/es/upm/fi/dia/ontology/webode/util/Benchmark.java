package es.upm.fi.dia.ontology.webode.util;

import java.sql.*;
import java.lang.reflect.*;
import es.upm.fi.dia.ontology.webode.service.ODEService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Benchmark {
  public static void main(String[] args) {
    Class ode=ODEService.class;
    Method[] methods=ode.getDeclaredMethods();
    StringBuffer meth;
    Class[] m_args;
    for(int i=0; methods!=null && i<methods.length; i++) {
      meth=new StringBuffer("INSERT INTO ODE_BENCHMARK (method_name,count,t) VALUES ('").append(methods[i].getName()).append('(');
      m_args=methods[i].getParameterTypes();
      for(int j=0; m_args!=null && j<m_args.length; j++) {
        if(j>0) meth.append(',');
        meth.append(m_args[j].getName());
      }
      meth.append(")',0,0);");
      System.out.println(meth);
    }
  }
}