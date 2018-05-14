package es.upm.fi.dia.ontology.webode.translat.internalstructure;

//import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import es.upm.fi.dia.ontology.webode.service.*;


public class ISOntology extends ISStructureElement
{
    //Attributes
  private String name; //one
      private String namespace; //one
  private String author;//one or cero
  private String creation_date;//one
  private Vector related_references;//cero or many
  private Hashtable instances_sets;//one or cero
  private ISConceptualization conceptualization;//one or cero
  private Vector views;//one or cero




    public ISOntology ()
      {
       //This contructor fills at least those fields which are compulsory
       empty = false;
       related_references=new Vector();
       instances_sets= new Hashtable();
       conceptualization = new ISConceptualization ();
       views= new Vector();


	}


         public ISOntology(String ontology_name,String ontology_namespace,String ontology_date)
        {
        this();
        name=ontology_name;
        namespace=ontology_namespace;
//	System.out.println ("El namespace es "+namespace);
        creation_date=ontology_date;

        }




        public void addInstancesSet (ISInstancesSet set)
        {
        try
        {
                instances_sets.put(set.getName(),set);
        }
        catch (Exception e)
        {
                System.out.println("ALGO PETO");
        }
        }


        public Enumeration getInstancesSets ()
        {
                return instances_sets.elements();
        }



    public Node obtainXML  (Document owner_document)
    {



        Node ontology_element;




        //First of all we should create the document, which root must be the element "Ontology"

        ontology_element=owner_document.createElement("Ontology");


        //the part of the DTD should be here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //**********************************************************************************
               if (name!=null)

            addElement (owner_document,ontology_element,"Name",name);

        if (description!=null)

            addElement (owner_document,ontology_element,"Description",description);

        if (author!=null)

            addElement (owner_document,ontology_element,"Author",author);



        if (creation_date!=null)
            addElement (owner_document,ontology_element,"Creation-Date",creation_date);




        for (Enumeration e = related_references.elements() ; e.hasMoreElements() ;)

            addElement (owner_document,ontology_element,"Related-Reference",(String)(e.nextElement()));

        //Conceptualization part goes here
        if (conceptualization !=null)
        {
        if (!conceptualization.isEmpty())
            ontology_element.appendChild(conceptualization.obtainXML(owner_document));
        else
        {
                System.out.println("PETOO");
        }
        }


        // Instances part goes here
        //If there aren't any instance set, thought allowed by the DTD, the "Instances"

        if (instances_sets.size() >0)
            {
                Element instances_element = owner_document.createElement("Instances");
                ontology_element.appendChild(instances_element);
                for (Enumeration e = instances_sets.elements() ; e.hasMoreElements() ;)
                    {


                        instances_element.appendChild(((ISInstancesSet)e.nextElement()).obtainXML(owner_document));


                        }

            }




        Enumeration e = views.elements();
        if (e.hasMoreElements())
        {
                Element views_element= owner_document.createElement("Views");
                ontology_element.appendChild(views_element);

        while (e.hasMoreElements())
                {
            views_element.appendChild(((ISView)e.nextElement()).obtainXML(owner_document));
                }
        }

        return ontology_element;

    }

        public void setAttributes (String ontology_name,String ontology_date)
        {
        empty=false;
        name=ontology_name;
        creation_date=ontology_date;

        }


        public void	setConceptualization(ISConceptualization ontology_conceptualization)
        {
                conceptualization=ontology_conceptualization;
        }

        public ISConceptualization getConceptualization()
        {
        return conceptualization;
        }

        public void	setAuthor(String ontology_author)
        {
        author=ontology_author;
        }

        public String getNamespace()
        {
                return namespace;
        }

        public void setNamespace (String ontology_namespace)
        {
        namespace=ontology_namespace;
        }

        public boolean belongsTo(String uri)
        {
        int index= uri.indexOf('#');
        String term_namespace=uri.substring(0,index+1);
        return namespace.equals(term_namespace);
        }


        public void toWebODE(ODEService ode, String user, String groupName) throws java.rmi.RemoteException, WebODEException, java.sql.SQLException, ISException
        {
                if(user==null)
                  throw new ISException("User can not be null.");

                OntologyDescriptor od=new OntologyDescriptor(name, description, user, groupName, new java.util.Date(System.currentTimeMillis()), new java.util.Date(System.currentTimeMillis()));
                ode.createOntology(od);
        /*
                ISReference ref;
                for(Enumeration enum=related_references.elements(); enum.hasMoreElements(); )
                {
                        ref=(ISReference)enum.nextElement();
                        ref.toWebODE(ode,...);
                }
        */
                conceptualization.toWebODE(ode,name);

                ISInstancesSet instanceSet;
                String instanceSetName;
                for(Enumeration enum=instances_sets.keys(); enum.hasMoreElements(); )
                {
                        instanceSetName=(String)enum.nextElement();
                        instanceSet=(ISInstancesSet)instances_sets.get(instanceSetName);
                        instanceSet.toWebODE(ode,name);
                }
        /*
                ISView view;
                for(Enumeration enum=views.elements(); enum.hasMoreElements(); )
                {
                        view=(ISView)enum.nextElement();
                        view.toWebODE(ode,...);
                }
        */
        }




}






