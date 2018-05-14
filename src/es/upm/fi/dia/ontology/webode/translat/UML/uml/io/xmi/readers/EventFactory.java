//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\EventFactory.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.builders.*;

import org.xml.sax.*;

import java.util.*;

public class EventFactory {

  private Stack uri  =null;
  private Stack name =null;
  private Stack qName=null;

  private Event        buildedEvent;
  private EventBuilder builders[];

  public EventFactory() {
    builders=new EventBuilder[12];
    builders[ 0]=new AssociationEndEventBuilder(this);
    builders[ 1]=new AssociationEventBuilder(this);
    builders[ 2]=new AttributeEventBuilder(this);
    builders[ 3]=new ClassEventBuilder(this);
    builders[ 4]=new DataTypeEventBuilder(this);
    builders[ 5]=new DocumentEventBuilder(this);
    builders[ 6]=new GeneralizationEventBuilder(this);
    builders[ 7]=new ModelEventBuilder(this);
    builders[ 8]=new MultiplicityEventBuilder(this);
    builders[ 9]=new StereotypeEventBuilder(this);
    builders[10]=new TaggedValueEventBuilder(this);
    builders[11]=new NamespaceEventBuilder(this);

    uri=new Stack();
    name=new Stack();
    qName=new Stack();

    uri.push("");
    name.push("");
    qName.push("");
  }

  /**
   * @param uri
   * @param name
   * @param qName
   * @param atts
   * @return Event
   */
  public final Event create(String uri, String name, String qName, Attributes atts) {
   buildedEvent=null;

   this.uri.push(uri);
   this.name.push(name);
   this.qName.push(qName);

   for(int i=0;i<builders.length;i++)
     builders[i].build(uri,name,qName,atts);

   return buildedEvent;
  }

  /**
   * @param uri
   * @param name
   * @param qName
   * @return Event
   */
  public final Event create(String uri, String name, String qName) {
    buildedEvent=null;

    this.uri.pop();
    this.name.pop();
    this.qName.pop();

    for(int i=0;i<builders.length;i++)
      builders[i].build(uri,name,qName);

    return buildedEvent;
  }

  /**
   * @param ch
   * @param begin
   * @param end
   * @return Event
   */
  public final Event create(char[] ch, int begin, int end) {
    buildedEvent=null;

    for(int i=0;i<builders.length;i++)
      builders[i].build((String)uri.peek(),
                        (String)name.peek(),
                        (String)qName.peek(),
                        ch,begin,end);

    return buildedEvent;
  }

  /**
   * @param event
   */
  public final void register(Event event) {
    buildedEvent=event;
  }
}
