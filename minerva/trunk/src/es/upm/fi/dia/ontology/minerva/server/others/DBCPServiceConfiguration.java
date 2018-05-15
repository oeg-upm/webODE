package es.upm.fi.dia.ontology.minerva.server.others;

import es.upm.fi.dia.ontology.minerva.server.services.MinervaServiceConfiguration;

public class DBCPServiceConfiguration  extends MinervaServiceConfiguration {
  public boolean accessToUnderlyingConnectionAllowed=false;
  public boolean defaultAutoCommit=true;
  public String defaultCatalog=null;
  public boolean defaultReadOnly=false;
  public int defaultTransactionIsolation=-1;
  public int loginTimeout=-1;
  public int maxActive=-1;
  public int minIdle=-1;
  public int maxIdle=-1;
  public int maxOpenPreparedStatements=-1;
  public int maxWait=-1;
  public int minEvictableIdleTimeMillis=-1;
  public int numTestsPerEvictionRun=-1;
  public boolean poolPreparedStatements=false;
  public boolean testOnBorrow=false;
  public boolean testOnReturn=true;
  public boolean testWhileIdle=true;
  public long timeBetweenEvictionRunsMillis=-1;
  public String validationQuery=null;
  
  public String driverClassName=null;
  public String url=null;
  public String password=null;
  public String username=null;
  public int initialSize=15;
  
  public DBCPServiceConfiguration() {
    super (false);
  }
}
