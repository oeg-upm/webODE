package es.upm.fi.dia.ontology.minerva.server.others;

// Java stuff
import java.lang.reflect.Field;
import java.rmi.RemoteException;

// own stuff
import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * Manager for the database service.
 */
public class DBCPServiceManager extends MinervaManagerImp
{
  static Object[][] PARAMS=null;

  public DBCPServiceManager () throws RemoteException, NoSuchFieldException {
    PARAMS=new Object[][] {
        {DBCPServiceConfiguration.class.getField("driverClassName"),"Driver","Sets the jdbc driver class name."},
        {DBCPServiceConfiguration.class.getField("url"),"URL","Sets the url."},
        {DBCPServiceConfiguration.class.getField("username"),"User","Sets the username."},
        {DBCPServiceConfiguration.class.getField("password"),"Password","Sets the password."},
        {DBCPServiceConfiguration.class.getField("initialSize"),"Initial size","Sets the initial size of the connection pool."},
        {DBCPServiceConfiguration.class.getField("maxActive"),"Max active","Sets the maximum number of active connections that can be allocated at the same time."},
        {DBCPServiceConfiguration.class.getField("minIdle"),"Min idle","Sets the minimum number of idle connections in the pool."},
        {DBCPServiceConfiguration.class.getField("maxIdle"),"Max idle","Sets the maximum number of connections that can remail idle in the pool."},
        {DBCPServiceConfiguration.class.getField("loginTimeout"),"Login timeout","Set the login timeout (in seconds) for connecting to the database."},
        {DBCPServiceConfiguration.class.getField("maxWait"),"Max wait","Sets the maxWait property."},
        {DBCPServiceConfiguration.class.getField("defaultAutoCommit"),"Default autoCommit","Sets default auto-commit state of connections returned by this datasource."},
        {DBCPServiceConfiguration.class.getField("accessToUnderlyingConnectionAllowed"),"Access to underlying connection allowed","Sets the value of the accessToUnderlyingConnectionAllowed property."},
        {DBCPServiceConfiguration.class.getField("defaultCatalog"),"Default catalog","Sets the default catalog."},
        {DBCPServiceConfiguration.class.getField("defaultReadOnly"),"Default read-only","Sets defaultReadonly property."},
        {DBCPServiceConfiguration.class.getField("defaultTransactionIsolation"),"Default transaction isolation","Sets the default transaction isolation state for returned connections."},
        {DBCPServiceConfiguration.class.getField("poolPreparedStatements"),"Pool PreparedStatements","Sets whether to pool statements or not."},
        {DBCPServiceConfiguration.class.getField("maxOpenPreparedStatements"),"Max open PreparedStatements","Sets the value of the maxOpenPreparedStatements property."},
        {DBCPServiceConfiguration.class.getField("validationQuery"),"Validation query","Sets the validationQuery."},
        {DBCPServiceConfiguration.class.getField("testOnBorrow"),"Test on borrow","Sets the testOnBorrow property."},
        {DBCPServiceConfiguration.class.getField("testOnReturn"),"Test on return","Sets the testOnReturn property."},
        {DBCPServiceConfiguration.class.getField("testWhileIdle"),"Test while idle","Sets the testWhileIdle property."},
        {DBCPServiceConfiguration.class.getField("numTestsPerEvictionRun"),"Num tests per eviction run","Sets the value of the numTestsPerEvictionRun property."},
        {DBCPServiceConfiguration.class.getField("timeBetweenEvictionRunsMillis"),"Time between eviction runs","Sets the timeBetweenEvictionRunsMillis property."},
        {DBCPServiceConfiguration.class.getField("minEvictableIdleTimeMillis"),"Min evictable idle","Sets the minEvictableIdleTimeMillis property."}
      };
  }

  public MinervaServiceDescription getServiceDescription() {
    try {
      DBCPServiceConfiguration cfg=(DBCPServiceConfiguration)serviceConfig;
      MinervaAttributeDescription[] mads=new MinervaAttributeDescription[PARAMS.length];
      for(int i=0; i<mads.length; i++) {
        Field field=(Field)PARAMS[i][0];
        Class clazz=field.getType();
        if(clazz.equals(int.class) || clazz.equals(long.class)) {
//          System.out.println(field.getName()+":"+clazz+"-->"+String.class);
          clazz=Integer.class;
        }
        mads[i]=new MinervaAttributeDescription((String)PARAMS[i][1],(String)PARAMS[i][2],clazz,null,field.get(cfg));
      }
      return new MinervaServiceDescription("Database service", "Service used to access database sources.",mads);
    }
    catch(IllegalAccessException iae) {
      System.out.println(iae);
      return null;
    }
  }

  /**
   * This method is used if no UI has been defined over the service.
   *
   * @param attrName  Name of the configurable attribute.
   * @param attrValue The attribute's value.
   */
  public void setAttribute (String attrName, Object attrValue) throws NoSuchAttributeException, RemoteException {
    DBCPServiceConfiguration dsc = (DBCPServiceConfiguration) serviceConfig;
    
    Object[] param=null;
    for(int i=0; param==null && i<PARAMS.length; i++)
      if(attrName.equals(PARAMS[i][1]))
        param=PARAMS[i];
    try {
      if(param!=null) {
        Field field=(Field)param[0];
        String strVal=(attrValue==null)?null:attrValue.toString().trim();
        if(strVal!=null && strVal.length()==0)
          strVal=null;
        if(field.getType().equals(String.class)) {
          if(strVal!=null)
            field.set(dsc, strVal);
        }
        else if(field.getType().equals(boolean.class))
          field.set(dsc, Boolean.getBoolean(attrValue.toString()));
        else if(field.getType().equals(int.class)) {
          int val=-1;
          if(strVal!=null)
            val=Integer.parseInt(strVal);
          field.set(dsc, val);
        }
        else if(field.getType().equals(long.class)) {
          long val=-1;
          if(strVal!=null)
            val=Long.parseLong(strVal);
          field.set(dsc, val);
        }
      }
      else
        throw new NoSuchAttributeException ("The attribute " + attrName + " is not valid.");
    }
    catch(IllegalAccessException iae) {
      System.out.println(iae);
    }
  }
}
