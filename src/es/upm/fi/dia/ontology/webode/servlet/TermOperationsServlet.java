package es.upm.fi.dia.ontology.webode.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import es.upm.fi.dia.ontology.webode.service.*;
import es.upm.fi.dia.ontology.webode.OntoClean.*;
import es.upm.fi.dia.ontology.webode.ui.designer.*;
import es.upm.fi.dia.ontology.webode.ui.designer.model.*;


/**
 * Servlet that performs operations on behalf of the designer applet.
 *
 * @author  Julio César Arpírez Vega, Emilio Raya and Oscar Corcho
 * @version 2.0
 */
public class TermOperationsServlet extends BaseServlet implements Command
{
    public void doPost (HttpServletRequest req,
			HttpServletResponse res)
	throws ServletException, IOException
    {
	HttpSession session = req.getSession (false);
	doPost (req, res, session);
    }


    public void doPost (HttpServletRequest req,
			HttpServletResponse res,
			HttpSession session)
	throws ServletException, IOException
    {
	// These things are in the session.
//	String oauthor      = (String) session.getAttribute (USER);
//	String ogroup       = (String) session.getAttribute (GROUP);

	try {
	    ObjectInputStream ois = new ObjectInputStream (req.getInputStream());

	    // Read command descriptor
	    Object o = ois.readObject();
	    CommandDescriptor cd = (CommandDescriptor) o;
	    ois.close();

	    if (session != null) {
		try {
		    cd = _performOperation (cd, (String) session.getAttribute (CURRENT_ONTOLOGY),
					    (ODEService) session.getAttribute (ODE_SERVICE),
					    (OntoCleanService) session.getAttribute(ONTOCLEAN_SERVICE),
					    (String) session.getAttribute (USER));
		} catch (Exception e) {
		    cd = new CommandDescriptor (ERROR, e.getMessage(), null);
		}
	    }
	    else
		cd = new CommandDescriptor (ERROR, "Invalid session.  Log in again.", null);

	    // Response to client
	    ObjectOutputStream oos = new ObjectOutputStream (res.getOutputStream());
	    oos.writeObject (cd);
	    oos.close();

	} catch (Exception e) {
	    getServletContext().log (new Date() + ": Error serving designer client (" + e.getMessage() + ").");
	}
    }

    private CommandDescriptor _performOperation (CommandDescriptor cd, String ontology,
                                                 ODEService odeService,
                                                 OntoCleanService oc, String user)
	throws Exception
    {

	Element[] ae = null;
	Relation[] ar = null;
	es.upm.fi.dia.ontology.webode.ui.designer.model.Group[] agr = null;
	String[] values = null;

	int [] p = new int [2];

	switch (cd.command) {
	case RETRIEVE_VIEW:
	    TermPositionDescriptor[] atp = odeService.getTermPositions (ontology, (String) cd.parameters);
	    TermRelationPositionDescriptor[] atrp = odeService.getTermRelationPositions (ontology, (String) cd.parameters);

		//************************** ONTOCLEAN *********************************
		if (oc!=null) values = oc.getStringValues(ontology);
		//************************** ONTOCLEAN *********************************

	    if (atp != null) {
		ae = new Element[atp.length];
		for (int i = 0; i < atp.length; i++)
		    ae[i] = new Element (atp[i].name, atp[i].x, atp[i].y);
	    }
	    if (atrp != null) {
		ar = new Relation[atrp.length];
		for (int i = 0; i < atrp.length; i++) {
		    ar[i] = new Relation (atrp[i].name, atrp[i].origin, atrp[i].destination,
					  atrp[i].x, atrp[i].y);
		}
	    }

	    // Retrieve relations as well...
	    return new CommandDescriptor (OK, null, new Object[] {ae, ar,values});

	case RETRIEVE_DEFAULT:
	    // Read all terms and relations
	    Term[] at = odeService.getTerms (ontology, new int[] { Term.CONCEPT });
	    TermRelation[] atr = odeService.getTermRelations (ontology, false);
	    es.upm.fi.dia.ontology.webode.service.Group[] ag = odeService.getGroups(ontology);

		//************************** ONTOCLEAN *********************************
	    if (oc!=null) values = oc.getStringValues(ontology);
		//************************** ONTOCLEAN *********************************

	    HashMap els = new HashMap();
	    if (at != null) {
		ae = new Element[at.length];
		for (int i = 0; i < ae.length; i++) {
		    ae[i] = new Element (at[i].term);
		    els.put (at[i].term, ae[i]);
		}
	    }
	    if (atr != null) {
		ar = new Relation[atr.length];
		for (int i = 0; i < ar.length; i++) {
		    ar[i] = new Relation (atr[i].name, atr[i].origin, atr[i].destination);
		}
	    }
	    if (ag != null) {
		agr = new es.upm.fi.dia.ontology.webode.ui.designer.model.Group[ag.length];
		for (int i = 0; i < agr.length; i++) {
		    agr[i] = new es.upm.fi.dia.ontology.webode.ui.designer.model.Group (ag[i].name);
		    for (int j = 0; ag[i] != null && j < ag[i].concepts.length; j++) {
			agr[i].addElement ((Element) els.get (ag[i].concepts[j]));
		    }
		}
	    }

	    return new CommandDescriptor (OK, null, new Object[] {ae,ar,agr,values});
	case ADD_VIEW:
	    odeService.insertTerm (ontology, (String) cd.parameters, "", TermTypes.VIEW);
	    return new CommandDescriptor (OK, null,null);

	case DELETE_VIEW:
	    odeService.removeTerm (ontology, (String) cd.parameters);
	    return new CommandDescriptor (OK, null, null);

	case COMMIT:
	    Object[] ao = (Object[]) cd.data;

	    String [] only_evaluation = (String[]) ao[8];
	    if(only_evaluation[0].equals("0"))
	    {
	       if (oc!=null) removeElementsCompletelyOntoClean (odeService, oc, ontology, (Vector) ao[5], (String) cd.parameters);
	       removeElementsCompletely (odeService, ontology, (Vector) ao[5], (String) cd.parameters);
	       removeElements (odeService, ontology, (Vector) ao[0], (String) cd.parameters);
	       if (oc!=null) addElementsOntoClean (odeService, oc, ontology, (Vector) ao[1],(String []) ao[6], (String) cd.parameters);
	       addElements (odeService, ontology, (Vector) ao[1],(String []) ao[6], (String) cd.parameters);
	       if (oc!=null) updateElementsOntoClean (odeService, oc, ontology, (Vector) ao[2],(String []) ao[6],(String) cd.parameters);
	       updateElements (odeService, ontology, (Vector) ao[2],(String []) ao[6],(String) cd.parameters);
	       addGroups (odeService, ontology, (Vector) ao[3], (String) cd.parameters);
	       updateGroups (odeService, ontology, (Vector) ao[4], (String) cd.parameters);
	       addRelations (odeService, ontology, (Vector) ao[1], (String) cd.parameters);
	       updateRelations (odeService, ontology, (Vector) ao[2], (String) cd.parameters);
	    }
	    String [] evaluation = (String[]) ao[7];
	    if(evaluation[0].equals("1"))
	    {
	       if (oc!=null){
				ErrorOntoClean[] errors = oc.evaluationOntoClean(ontology,user," ");
				p[0]= 1;
				p[1]=errors.length;
				String[] messages = new String [errors.length];
				String[] ts = new String [errors.length*2];
				for(int g=0;g<errors.length;g++){
					messages[g] = errors[g].convertString();
					ts[g] = errors[g].GetTermName();
				}
				for(int f=0;f<errors.length;f++){
					ts[f+errors.length] = errors[f].GetParentName();
				}
				return new CommandDescriptor (OK, null,new Object[] { p,messages,ts });
			}else{  //Solicitó evaluación pero no tiene acceso a OntoClean
				p[0] = 0;
				p[1] = 0;
				String[] messages = new String[1];
				String[] ts = new String[1];
				messages[0] = "Sorry, but you have no access to OntoClean";
				return new CommandDescriptor (OK, null, new Object[] {p,messages,ts});
			}
		}else{
			p[0]= 0;
			p[1]= 0;
			String[] messages = new String[1];
			String[] ts = new String[1];
			return new CommandDescriptor (OK, null,new Object[] { p,messages,ts });
		}
	default:
	    getServletContext().log (new Date() + ": operation not supported: " + cd.command + ".");
	    return new CommandDescriptor (ERROR, "Operation not supported: " + cd.command + ".", null);
	}
    }


    protected void removeElements (ODEService odeService, String currentOntology,
				   Vector vRemoved, String view) throws Exception
    {
	//System.out.println ("Removing elements: " + vRemoved.size());
	// Try to actually remove elements
	for (int i = 0; i < vRemoved.size(); i++) {
	    Object obj = vRemoved.elementAt (i);
	    if (obj instanceof Relation) {
		//System.out.println ("Relation: "+ ((Relation) obj).getName());
		odeService.removeTermRelationPosition (currentOntology,
						       new TermRelationPositionDescriptor
						       (((Relation) obj).getName(),
							((Relation) obj).getOrigin(),
							((Relation) obj).getDestination(), -1, 0, 0),
						       view);
	    }
	    else if (obj instanceof Element) {
		//System.out.println ("Element: "+ ((Element) obj).getName());
		odeService.removeTermPosition (currentOntology, ((Element) obj).getName(), view);
	    }
	}
    }

    protected void removeElementsCompletely (ODEService odeService, String currentOntology,
					     Vector vRemoved, String view) throws Exception
    {
		for (int i = 0; i < vRemoved.size(); i++) {
	    	Object obj = vRemoved.elementAt (i);
		    if (obj instanceof Relation) {
				Relation rel = (Relation) obj;
				odeService.removeTermRelation (new TermRelation
					       (currentOntology, null, rel.getName(),
							rel.getOrigin(), rel.getDestination()));
	    	}
	    	else if (obj instanceof Element) {
	    		Element el = (Element)obj;
				odeService.removeTerm (currentOntology, el.getName());
		    }
		}
    }


    protected void addElements (ODEService odeService, String currentOntology,
				Vector vAdded,String[] mp, String view) throws Exception
    {
		for (int i = 0; i < vAdded.size(); i++) {
		    Object obj = vAdded.elementAt (i);
		    if (obj instanceof Element) {
				Element el = (Element) obj;
				odeService.insertTermPosition (currentOntology,
					       new TermPositionDescriptor (el.getName(), el.getX(), el.getY()),
					       view);
	    	}
		}
    }


    protected void addRelations (ODEService odeService, String currentOntology,
				 Vector vAdded,String view) throws Exception
    {

      for (int i = 0; i < vAdded.size(); i++)
      {
	    Object obj = vAdded.elementAt (i);
	    if (obj instanceof Relation) {
			Relation rel = (Relation) obj;
			/*odeService.insertTermRelation (new TermRelation
			  (currentOntology, rel.getName(), rel.getOrigin(),
					       rel.getDestination()));*/
		 	try {
			  odeService.insertTermRelationPosition (currentOntology,
						       new TermRelationPositionDescriptor
						       (rel.getName(), rel.getOrigin(), rel.getDestination(),
								rel.getBendX(), rel.getBendY(), rel.getCardinality()),
					                view);
			}catch(Exception e) { }
	    }
       }
    }

    protected void updateElements (ODEService odeService, String currentOntology,
				   Vector vDirty, String[] mp,String view) throws Exception
    {
		for (int i = 0; i < vDirty.size(); i++) {
	    	Object obj = vDirty.elementAt (i);
		    if (obj instanceof Element) {
				Element el = (Element) obj;
				// Update ontology position
				odeService.updateTermPosition (currentOntology,
					       new TermPositionDescriptor (el.getName(), el.getX(), el.getY()),
					       view);
		    }
		}
    }

    protected void updateRelations (ODEService odeService, String currentOntology,
				   Vector vDirty, String view) throws Exception
    {
	//System.out.println ("updating relations");
	for (int i = 0; i < vDirty.size(); i++) {
	    Object obj = vDirty.elementAt (i);

	    if (obj instanceof Relation) {
		Relation rel = (Relation) obj;
		//System.out.println ("Update rel");
		odeService.updateTermRelationPosition (currentOntology, new TermRelationPositionDescriptor
						       (rel.getName(), rel.getOrigin(), rel.getDestination(), -1,
							rel.getBendX(), rel.getBendY()), view);
	    }
	}
    }

    protected void addGroups (ODEService odeService, String currentOntology,
			      Vector vNewGroups, String view) throws Exception
    {
	//System.out.println ("Adding groups...");
	for (int i = 0; i < vNewGroups.size(); i++) {
	    es.upm.fi.dia.ontology.webode.ui.designer.model.Group g = (es.upm.fi.dia.ontology.webode.ui.designer.model.Group) vNewGroups.elementAt (i);
	    Vector foo = g.getElements();
	    String[] astr = new String[foo.size()];
	    for (int k = 0; k < foo.size(); k++) {
		astr[k] = ((Element) foo.elementAt (k)).getName();
	    }
	    odeService.addGroup (currentOntology,
				 new es.upm.fi.dia.ontology.webode.service.Group (g.getName(),
							   "",
							   astr));
	}
    }

    protected void updateGroups (ODEService odeService, String currentOntology,
				 Vector vGroups, String view) throws Exception
    {
	//System.out.println ("Updating groups...");
	for (int i = 0; i < vGroups.size(); i++) {
	    es.upm.fi.dia.ontology.webode.ui.designer.model.Group g = (es.upm.fi.dia.ontology.webode.ui.designer.model.Group) vGroups.elementAt (i);
	    Vector foo = g.getElements();
	    String[] astr = new String[foo.size()];
	    for (int k = 0; k < foo.size(); k++) {
		astr[k] = ((Element) foo.elementAt (k)).getName();
	    }
	    odeService.updateGroup (currentOntology, g.getName(),
				    new es.upm.fi.dia.ontology.webode.service.Group (g.getName(),
							      "",
							      astr));
	}
    }





// *********************************************** ONTOCLEAN *****************
// ***************************************************************************
// ***************************************************************************
// ***************************************************************************
// ***************************************************************************
    protected void removeElementsCompletelyOntoClean
    			(ODEService odeService, OntoCleanService oc, String currentOntology,
				 Vector vRemoved, String view) throws Exception
    {
		int existing = 0;
		for (int i = 0; i < vRemoved.size(); i++) {
	    	Object obj = vRemoved.elementAt (i);
		    if (obj instanceof Relation) {
		    	//No hacer nada. Es una relación
		    } else if (obj instanceof Element) {
	    		Element el = (Element)obj;
	    		if (oc!=null) { //SOBRA, PERO SE VUELVE A COMPROBAR POR SI ACASO  ;-)
			    	Instance[] instances = odeService.getInstances(TOP_LEVEL_ONTOLOGY,currentOntology,TOP_UNIVERSALS);
			    	if(instances != null){
			    	  for(int v=0;v<instances.length;v++){
		    		    if(instances[v].name.equals(el.getName())){
		    		      existing = 1;
		    	    	  v=instances.length;
		    		    }
		    		  }
	    			}
	    			if(existing == 1){
		    		  Term t = odeService.getTerm(currentOntology,el.getName());
			    	  if (t.type == TermTypes.CONCEPT)
    	                 odeService.removeTerm(TOP_LEVEL_ONTOLOGY, el.getName());
					}
				}
		    }
		}
    }


    protected void addElementsOntoClean
    			(ODEService odeService, OntoCleanService oc, String currentOntology,
				 Vector vAdded,String[] mp, String view) throws Exception
    {
		for (int i = 0; i < vAdded.size(); i++) {
		    Object obj = vAdded.elementAt (i);
		    if (obj instanceof Element) {
				Element el = (Element) obj;
				//odeService.insertTermPosition (currentOntology,
				//	       new TermPositionDescriptor (el.getName(), el.getX(), el.getY()),
				//	       view);

				if (oc!=null) {
					String c = new String();
					odeService.removeTerm(TOP_LEVEL_ONTOLOGY,el.getName());
					Instance instance = new Instance(el.getName(), TOP_UNIVERSALS, currentOntology,"");
					odeService.insertInstance(TOP_LEVEL_ONTOLOGY,instance);
					//Obtener las metapropiedades, parseándolas
					for (int j=0;j<mp.length;j++){
						StringTokenizer token = new StringTokenizer(mp[j]);
						String k = token.nextToken("*");
						int l = k.length();
						c = mp[j].substring(l+1);
						if(k.equals(el.getName()))
						j=mp.length;
					}
					oc.insertMetaproperties(currentOntology,el.getName(),c);
				}
	    	}
		}

		if (oc!=null) {
			String c = new String();
			if(vAdded.size() == 0){
				for (int h=0;h<mp.length;h++){
					StringTokenizer token = new StringTokenizer(mp[h]);
					String k = token.nextToken("*");
					int l = k.length();
					c = mp[h].substring(l+1);

					odeService.removeTerm(TOP_LEVEL_ONTOLOGY,k);
					Instance instance = new Instance(k, TOP_UNIVERSALS, currentOntology,"");
					odeService.insertInstance(TOP_LEVEL_ONTOLOGY,instance);
					oc.insertMetaproperties(currentOntology,k,c);
    	       }
			}
		}
    }


    protected void updateElementsOntoClean
    			 (ODEService odeService, OntoCleanService oc, String currentOntology,
				  Vector vDirty, String[] mp,String view) throws Exception
    {
		for (int i = 0; i < vDirty.size(); i++) {
	    	Object obj = vDirty.elementAt (i);
		    if (obj instanceof Element) {
				Element el = (Element) obj;
				// Update ontology position
				//odeService.updateTermPosition (currentOntology,
				//	       new TermPositionDescriptor (el.getName(), el.getX(), el.getY()),
				//	       view);
				if (oc!=null){
    	            odeService.removeTerm(TOP_LEVEL_ONTOLOGY,el.getName());
        	        Instance instance = new Instance(el.getName(), TOP_UNIVERSALS, currentOntology,"");
            	    odeService.insertInstance(TOP_LEVEL_ONTOLOGY,instance);
			        String c = new String();

    	            for (int j=0;j<mp.length;j++) {
        	           StringTokenizer token = new StringTokenizer(mp[j]);
            	       String k = token.nextToken("*");
                	   int l = k.length();
	                   c = mp[j].substring(l+1);
    	               if(k.equals(el.getName()))
        	           j=mp.length;
            	    }
                	oc.insertMetaproperties(currentOntology,el.getName(),c);
	            }
		    }
		}
    }

}