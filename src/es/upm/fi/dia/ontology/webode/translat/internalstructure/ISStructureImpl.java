
//Description of the internal estructure used by the wrappers.

package es.upm.fi.dia.ontology.webode.translat.internalstructure;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import es.upm.fi.dia.ontology.webode.translat.logs.Log;

import es.upm.fi.dia.ontology.webode.service.ODEService;


abstract class ISStructureImpl implements ISStructure
{
    //Attributes
    protected ISOntology ontology;
	protected Log log;



    public Document obtainXML ()
    {
	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	Document doc;

		try
		    {

			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			//doc= new DocumentImpl();



			doc.appendChild(ontology.obtainXML(doc));

			return (doc);
		    }

		catch (Exception e)
		    {
			System.out.println("|Fatal Error: XML document cannot be created");
			e.printStackTrace();
		    }

		return null;

	}



	public void initializeOntology (String name,String description,String author,String date)
	{
	ontology.setAttributes(name,date);

	}

	public Log obtainLog ()
	{
		return log;
	}

        public void toWebODE (ODEService ode, String user, String userGroup) throws Exception {
          ontology.toWebODE(ode,user,userGroup);
        }


}


















