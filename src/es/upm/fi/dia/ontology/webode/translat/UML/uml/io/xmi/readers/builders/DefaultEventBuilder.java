//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\builders\\DefaultEventBuilder.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.builders;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.*;

import org.xml.sax.*;

import java.lang.reflect.*;

public abstract class DefaultEventBuilder implements EventBuilder {

  private EventFactory factory;

  /**
   * @param factory
   */
  public DefaultEventBuilder(EventFactory factory) {
    this.factory=factory;
  }

  /**
   * @param qName
   * @return boolean
   */
  protected abstract boolean isValid(String qName);

  /**
   * @return Class
   */
  protected abstract Class getStartClass();

  /**
   * @return Class
   */
  protected abstract Class getEndClass();

  /**
   * @return Class
   */
  protected abstract Class getDataClass();

  /**
   * @param uri
   * @param name
   * @param qName
   * @param atts
   */
  public final void build(String uri, String name, String qName, Attributes atts) {
    if(isValid(qName)) {
      Class theClass=getStartClass();
//      System.out.println(theClass);
      try {
        Constructor constructor=theClass.getConstructor(new Class[]{
                                                            uri.getClass(),
                                                            name.getClass(),
                                                            qName.getClass(),
                                                            atts.getClass().getInterfaces()[0]});
//        System.out.println(constructor);
        try {
          Event event=(Event)constructor.newInstance(new Object[]{uri,
                                                                  name,
                                                                  qName,
                                                                  atts});
          factory.register(event);
        } catch (Exception ex) {
          System.out.flush();
          ex.printStackTrace();
          System.out.flush();
          throw new RuntimeException("Event object creation failed"+
                                     " ["+qName+"]",ex);
        }
      } catch (Exception ex) {
        System.out.flush();
        ex.printStackTrace();
        System.out.flush();
        throw new RuntimeException("Event object constructor cannot be used"+
                                   " ["+qName+"]", ex);
      }
    }
  }

  /**
   * @param uri
   * @param name
   * @param qName
   */
  public final void build(String uri, String name, String qName) {
    if(isValid(qName)) {
      Class theClass=getEndClass();
//      System.out.println(theClass);
      try {
        Constructor constructor=theClass.getConstructor(new Class[]{
                                                            uri.getClass(),
                                                            name.getClass(),
                                                            qName.getClass()});
//        System.out.println(constructor);
        try {
          Event event=(Event)constructor.newInstance(new Object[]{uri,
                                                                  name,
                                                                  qName});
          factory.register(event);
        } catch (Exception ex) {
          System.out.flush();
          ex.printStackTrace();
          System.out.flush();
          throw new RuntimeException("Event object creation failed"+
                                     " ["+qName+"]",ex);
        }

      } catch (Exception ex) {
        System.out.flush();
        ex.printStackTrace();
        System.out.flush();
        throw new RuntimeException("Event object constructor cannot be used"+
                                   " ["+qName+"]", ex);
      }
    }
  }

  /**
   * @param uri
   * @param name
   * @param qName
   * @param ch
   * @param begin
   * @param end
   */
  public final void build(String uri, String name, String qName,
                          char[] ch, int begin, int end) {
    if(isValid(qName)) {
      String data=new String(ch,begin,end);
      Class theClass=getDataClass();
//      System.out.println(theClass);
      try {
        Constructor constructor=theClass.getConstructor(new Class[]{
                                                            uri.getClass(),
                                                            name.getClass(),
                                                            qName.getClass(),
                                                            data.getClass()});
//        System.out.println(constructor);
        try {
          Event event=(Event)constructor.newInstance(new Object[]{uri,
                                                                  name,
                                                                  qName,
                                                                  data});
          factory.register(event);
        } catch (Exception ex) {
          System.out.flush();
          ex.printStackTrace();
          System.out.flush();
          throw new RuntimeException("Event object creation failed"+
                                     " ["+qName+"]",ex);
        }

      } catch (Exception ex) {
        System.out.flush();
        ex.printStackTrace();
        System.out.flush();
        throw new RuntimeException("Event object constructor cannot be used"+
                                   " ["+qName+"]", ex);
      }
    }
  }
}
