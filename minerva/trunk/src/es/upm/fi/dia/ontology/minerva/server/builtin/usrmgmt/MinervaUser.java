package es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt;

import java.security.MessageDigest;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.builtin.AuthenticationException;

/**
 * Class to encapsulate the information about a Minerva user.
 *
 * @author   Julio César Arpírez Vega
 * @version  0.1
 */
public class MinervaUser implements java.io.Serializable 
{
  static final long serialVersionUID = -2724508583027959677L;

  /** The administration user name. */
  public static final String ADMIN_USER = "admin";
  /** The administrator's password. */
  public static final String ADMIN_PASSWORD = "admin";
  /** The administration group description. */
  public static final String ADMIN_DESC  = "The Minerva Server's administrator.";

  private String login, password, description;
  private java.util.Date creationDate;
  private Vector groups;

  /**
   * Creates a new user with the specified login, password and description.
   *
   * @param login The user's login.
   * @param password The user's password.
   * @param description The user's description.
   */
  public MinervaUser (String login, String password, String description)
  {
    this.login       = login;
    this.password    = password;
    this.description = description;

    creationDate = new java.util.Date();
    groups = new Vector();
  }

  /**
   * Sets a new password.
   *
   * @param newPassword The new password.
   */
  public void setPassword (String newPassword)
  {
    password = newPassword;
  }

  /**
   * Ask whether the user password is ok or not.
   *
   * @param password The password to check against.
   */
  public boolean isRightPassword (String password) {
    return password.equals (this.password);
  }

  /**
   * Ask whether the digest of the user with the password is correct.
   *
   * @param messageDigest The digester of the usr and password.
   * @param digest The digest to check against.
   */
  public boolean isRightDigest(MinervaUserMessageDigest messageDigest, byte[] digest) throws AuthenticationException {
    try {
      return MessageDigest.isEqual(digest, messageDigest.digest(this.login, this.password));
    }
    catch(Exception e) {
      throw new AuthenticationException("An error occurs while try to varify digest message", e);
    }
  }
  
  /**
   * Gets a user description.
   */
  public String getDescription ()
  {
    return description;
  }

  /**
   * Sets a user description.
   *
   * @param desc The description.
   */
  public void setDescription (String desc)
  {
    this.description = desc;
  }

  /**
   * Gets the login for the user.
   */
  public String getLogin ()
  {
    return login;
  }

  /**
   * Gets the creation date for the user.
   */
  public java.util.Date getCreationDate ()
  {
    return creationDate;
  }

  /**
   * Set equality to just the login field.
   */
  public boolean equals (Object o) 
  {
    if (o instanceof MinervaUser) {
      MinervaUser mu = (MinervaUser) o;
      return mu.login.equals (login);
    }
    return false;
  }

  /**
   * The hash code comes from the login field.
   */
  public int hashCode ()
  {
    return login.hashCode();
  }

  /**
   * Gets the user's group.
   */
  public MinervaGroup[] getGroups ()
  {
    MinervaGroup[] amg = null;
    synchronized (groups) {
      amg = new MinervaGroup[groups.size()];

      groups.copyInto (amg);
    }
    return amg;
  }

  /**
   * Adds a new user's group.
   */
  public void addGroup (MinervaGroup group)
  {
    synchronized (groups) {
      groups.addElement (group);
    }
  }

  /**
   * Removes a group.
   */
  public void removeGroup (MinervaGroup group)
  {
    synchronized (groups) {
      groups.removeElement (group);
    }
  }

  public String toString ()
  {
    return login + " - " + description;
  }
}



