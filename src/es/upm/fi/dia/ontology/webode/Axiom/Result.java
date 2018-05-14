package es.upm.fi.dia.ontology.webode.Axiom;

public class Result implements java.io.Serializable
{
	public boolean isCorrect;
	public String[] clauses;
	public String[] prologClauses;
	public Result(String[] clauses,boolean isCorrect)
	{
			this.isCorrect=isCorrect;
			this.clauses=clauses;
			prologClauses=null;

	}
	public Result(String[] clauses, String[] prologClauses,boolean isCorrect)
	{
			this.isCorrect=isCorrect;
			this.clauses=clauses;
			this.prologClauses=prologClauses;

	}




}

