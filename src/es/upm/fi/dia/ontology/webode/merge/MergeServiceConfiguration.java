/*****************************************/
/* MergeServiceImp class *****************/
/*****************************************/
/* Author: J.A.R.G. **********************/
/*****************************************/
package es.upm.fi.dia.ontology.webode.merge;

import es.upm.fi.dia.ontology.minerva.server.services.*;

public class MergeServiceConfiguration extends MinervaServiceConfiguration
{
   public String odeService; /* ODE service to use */

   public MergeServiceConfiguration()
   {
      super(true);
   }

   public MergeServiceConfiguration(String odeService)
   {
      super(true);
      this.odeService=odeService;
   }

}
