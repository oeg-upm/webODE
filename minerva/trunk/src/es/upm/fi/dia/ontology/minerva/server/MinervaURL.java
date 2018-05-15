package es.upm.fi.dia.ontology.minerva.server;

import java.net.*;

/**
 * A Minerva URL.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.2
 */
public class MinervaURL implements java.io.Serializable
{
  public static final String PROTOCOL = "minerva";

  private String host;
  private int port;
  private String protocol=PROTOCOL;
  private String uri;

  /**
   * Parses the given url.
   *
   * @param url The given URL.
   */
  public MinervaURL (String url) throws java.net.MalformedURLException
  {
    this (url, PROTOCOL);
  }

  /**
   * Parses the given url according to the specified protocol.
   *
   * @param url The given URL.
   */
  public MinervaURL (String url, String protocol) throws java.net.MalformedURLException
  {
    this.protocol = protocol;

    if (url.startsWith (this.protocol + "://")) {
      int i = url.indexOf (":", (this.protocol + "://").length());

      if (i < 0)
        port = MinervaServerConstants.MINERVA_SERVER_PORT;
      else {
        try {
          port = Integer.parseInt (url.substring (i + 1, url.length()));
        } catch (Exception e) {
          throw new MalformedURLException ("invalid port.");
        }
      }

      if (i == this.protocol.length())
        host = "127.0.0.1";  // loopback
      else
        host = url.substring ((this.protocol + "://").length(), i > 0 ? i : url.length());

      i = url.indexOf ("/", (this.protocol + "://").length());
      if (i > 0)
        uri = url.substring (i, url.length());
    }
    else
      throw new MalformedURLException ("invalid protocol.");
  }


  /**
   * Another constructor.
   *
   * @param host The Minerva host.
   * @param port The Minerva port.
   */
  public MinervaURL (String host, int port)
  {
    this.port = port;
    this.host = host;
  }

  public String toString()
  {
    return protocol + "://" + host + ":" + port + (uri == null ? "" : "/" + uri);
  }

  /**
   * Gets protocol.
   *
   * @return "minerva"
   */
  public String getProtocol ()
  {
    return protocol;
  }

  /**
   * Gets port.
   *
   * @return The port.
   */
  public int getPort ()
  {
    return port;
  }

  /**
   * Gets host.
   *
   * @return The host.
   */
  public String getHost ()
  {
    return host;
  }

  /**
   * Gets the URI.
   *
   * @return The URL's URI.
   */
  public String getURI ()
  {
    return uri;
  }
}
