package es.upm.fi.dia.ontology.webode.searchDifferences;

/**
 * <p>Title: Class Context </p>
 * <p>Description: This class is used to manage the ODEService </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ontology Engineering Group. LIA </p>
 * @author Mª del Carmen Suárez de Figueroa Baonza
 * @version 1.0
 */

import java.rmi.RemoteException;
import java.net.MalformedURLException;

import es.upm.fi.dia.ontology.minerva.client.*;
import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.minerva.server.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.*;


public class Context
{
  private MinervaSession session = null;
  private ODEService odeService = null;

  /**
   * This constructor creates an instance of Context
   * with a MinervaSession and an ODEService
   */
  public Context ()
  {
    try
    {
      this.session = MinervaClient.getMinervaSession(new MinervaURL ("minerva://localhost"), "admin", "admin");
      this.odeService = (ODEService) this.session.getService("ode");
    }
    catch (RemoteException re)
    {
      System.out.println("ERROR: " + re);
      re.printStackTrace();
    }
    catch (AuthenticationException ae)
    {
      System.out.println("ERROR: " + ae);
      ae.printStackTrace();
    }
    catch (MalformedURLException me)
    {
      System.out.println("ERROR: " + me);
      me.printStackTrace();
    }
    catch (MinervaException me)
    {
      System.out.println("ERROR: " + me);
      me.printStackTrace();
    }
  }

  /**
   * This method obtains the ODEService from the instance of Context
   * @return an ODEService
   */
  public ODEService getService ()
  {
    return this.odeService;
  }

  /**
   * This method disconnect the ODEService and the MinervaSession from the instance
   * of Context
   */
  public void disconnectService ()
  {
    try
    {
      if (this.odeService != null)
        this.odeService.disconnect();
      if (this.session != null)
        this.session.disconnect();
        this.session = null;
    }
    catch (RemoteException re)
    {
      System.out.println("ERROR: " + re);
      re.printStackTrace();
    }
  }
}