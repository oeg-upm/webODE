package es.upm.fi.dia.ontology.webode.Axiom;

public class Connection 
{
	private static java.sql.Connection connection;
	private static String ontology;
	
	public static void setConnection(java.sql.Connection con)
	{
			connection=con;
	}

	public static java.sql.Connection getConnection()
	{
			return connection;
	}

	public static void setOntology(String ont)
	{
			ontology=ont;
	}

	public static String getOntology()
	{
			return ontology;
	}

}

