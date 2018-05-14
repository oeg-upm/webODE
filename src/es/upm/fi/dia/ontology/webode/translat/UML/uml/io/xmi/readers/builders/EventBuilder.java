//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\builders\\EventBuilder.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.builders;

import org.xml.sax.Attributes;

public interface EventBuilder {
  
  /**
   * @param uri
   * @param name
   * @param qName
   * @param atts
   */
  public void build(String uri, String name, String qName, Attributes atts);
  
  /**
   * @param uri
   * @param name
   * @param qName
   */
  public void build(String uri, String name, String qName);
  
  /**
   * @param uri
   * @param name
   * @param qName
   * @param ch
   * @param begin
   * @param end
   */
  public void build(String uri, String name, String qName, char[] ch, int begin, int end);
}
