package es.upm.fi.dia.ontology.webode.Axiom;

import java.util.*;

public class AxiomException extends Exception 
{

	Vector errors;
	public AxiomException(Vector errors)
	{
		this.errors=errors;
	}
	public Vector getErrors()
	{
	
		return errors;
	}

}