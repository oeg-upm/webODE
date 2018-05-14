//Source file: D:\\Trabajo\\xmi\\uml\\io\\xmi\\XMIReader.java

package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.io.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.*;
import es.upm.fi.dia.ontology.webode.translat.UML.uml.*;

import es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.handlers.*;

import org.xml.sax.helpers.*;

import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;

public class XMIReader extends DefaultHandler implements UMLModelReader {

  //----------------------------------------------------------------------------
  //-- Data model --------------------------------------------------------------
  //----------------------------------------------------------------------------

  private EventDispatcher  dispatcher=null;
  private TranslationTable t_table   =null;
  private EventFactory     factory   =null;
  private DocumentHandler  handler   =null;

  private String data=new String();
  private boolean isCharacterBacked=false;

  private char[] ch    =null;
  private int    start =0;
  private int    length=0;

  //----------------------------------------------------------------------------
  //-- Auxiliar methods --------------------------------------------------------
  //----------------------------------------------------------------------------

  private void releaseCharacterEvent() {
    if(isCharacterBacked) {
      Event event=factory.create(ch,start,length);

      if(event==null) {
        throw new RuntimeException("Cannot create data event");
      }

      try {
        dispatcher.dispatch(event);
      } catch (CannotDispatchEvent ex) {
        throw new RuntimeException(ex);
      }

      isCharacterBacked=false;
      data=new String();
    }
  }

  private void backCharacterEvent(char[] ch, int start, int length) {
    String newData=new String(ch,start,length);
    String fullData=data+newData;
    data=fullData;

    this.ch    =data.toCharArray();
    this.start =0;
    this.length=this.ch.length;

    isCharacterBacked=true;
  }

  //----------------------------------------------------------------------------
  //-- Constructor -------------------------------------------------------------
  //----------------------------------------------------------------------------

  public XMIReader() {
    dispatcher=new EventDispatcher(this);
    t_table   =new TranslationTable();
    factory   =new EventFactory();

    handler   =new DocumentHandler();

    dispatcher.bind(handler);
    dispatcher.commit();
  }

  //----------------------------------------------------------------------------
  //-- Business logic ----------------------------------------------------------
  //----------------------------------------------------------------------------

  public TranslationTable getTranslationTable() {
    return t_table;
  }

  //-- Interface: ContentHandler -----------------------------------------------

  public void startElement(String uri,
                           String name,
                           String qName,
                           Attributes atts) {
    releaseCharacterEvent();

    Event event=factory.create(uri,name,qName,atts);

    if(event==null) {
      throw new RuntimeException("Cannot create event for "+qName);
    }

    try {
      dispatcher.dispatch(event);
    } catch (CannotDispatchEvent ex) {
      throw new RuntimeException(ex);
    }
  }

  public void characters(char[] ch, int start, int length) {
    String cadena=new String(ch,start,length);

    if(cadena.equals("\n"))
      releaseCharacterEvent();

    String nuevaCadena=cadena.replaceAll("\n"," ");
    String prueba=nuevaCadena.trim();

    if(prueba.length()==0)
      return;

    backCharacterEvent(ch,start,length);
  }


  public void endElement(String uri,
                         String name,
                         String qName) {
    releaseCharacterEvent();

    Event event=factory.create(uri,name,qName);

    if(event==null)
      throw new RuntimeException("Cannot create event for "+qName);

    try {
      dispatcher.dispatch(event);
    } catch (CannotDispatchEvent ex) {
      throw new RuntimeException(ex);
    }
  }

  //-- Interface: UMLModelReader -----------------------------------------------

  public UMLModel read(String fileName) throws UMLIOException {
    XMLReader reader=null;
    UMLModel  model =null;

    try {
      reader=
        XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
      reader.setContentHandler(this);
      reader.parse(fileName);

      model=t_table.getModel();
    } catch (SAXException ex) {
      throw new UMLIOException(ex.getMessage(),getClass(), ex);
    } catch (RuntimeException ex) {
      throw new UMLIOException(ex.getMessage(),getClass(), ex);
    } catch (IOException ex) {
      throw new UMLIOException(ex.getMessage(),getClass(), ex);
    }

    return model;
  }

  public UMLModel read(InputSource source) throws UMLIOException {
    XMLReader reader=null;
    UMLModel  model =null;

    try {
      reader=
        XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
      reader.setContentHandler(this);
      reader.parse(source);

      model=t_table.getModel();
    } catch (SAXException ex) {
      throw new UMLIOException(ex.getMessage(),getClass(), ex);
    } catch (RuntimeException ex) {
      throw new UMLIOException(ex.getMessage(),getClass(), ex);
    } catch (IOException ex) {
      throw new UMLIOException(ex.getMessage(),getClass(), ex);
    }

    return model;
  }
}
