package es.upm.fi.dia.ontology.webode.Axiom;

class Yytoken{

  public String m_text;
  public int m_line;


  Yytoken (  String text, int line)
  {
	m_text = new String(text);
	m_line = line;

   }

  public String toString() {
      return "Token #"+m_text+": (line "+m_line+")";
  }
  public String toError() {
      return "#" + m_text + ": (line "+m_line+")";
  }


}