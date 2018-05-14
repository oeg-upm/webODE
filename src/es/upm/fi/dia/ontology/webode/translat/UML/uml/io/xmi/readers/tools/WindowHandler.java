package es.upm.fi.dia.ontology.webode.translat.UML.uml.io.xmi.readers.tools;

/**
 * <p>Title: Sistema de Importación y Exportación de Ontologías a UML</p>
 * <p>Description: Trabajo Fin de Carrera</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Laboratorio de Inteligencia Artificial - Grupo de Ontologías</p>
 * @author Miguel Esteban Gutiérrez
 * @version 1.0
 */

import java.util.logging.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class WindowHandler extends StreamHandler {

  private JFrame    frame =null;
  private JTextArea output=null;

  public WindowHandler() {
    frame =new JFrame();
    output=new JTextArea();

    frame.setTitle("Debugging");
    frame.setSize(640,480);
    frame.setContentPane(new JScrollPane(output));
    frame.show();

    frame.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        frame.dispose();
        System.exit(0);
      }
    });

    setOutputStream(new OutputStream() {
      public void write(int b) {
        output.append(""+(char)b);
      }

      public void write(byte[] b, int off, int len) {
        output.append(new String(b,off,len));
      }
    });
  }

  public void publish(LogRecord record) {
    super.publish(record);

    flush();
  }

}