package es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.text.MessageFormat;

public class MinervaUserMessageDigest implements Serializable {
  protected MessageFormat mf=null;
  protected String algorithm=null;
  protected Provider provider=null;
  protected String strProvider=null;
  
  public MinervaUserMessageDigest(String algorithm, MessageFormat mf) {
    this.mf=mf;
    this.algorithm=algorithm;
  }
  
  public MinervaUserMessageDigest(String algorithm, Provider provider,  MessageFormat mf) {
    this.mf=mf;
    this.algorithm=algorithm;
    this.provider=provider;
  }
  
  public MinervaUserMessageDigest(String algorithm, String provider,  MessageFormat mf) {
    this.mf=mf;
    this.algorithm=algorithm;
    this.strProvider=provider;
  }
  
  protected MessageDigest getMessageDigest() throws NoSuchAlgorithmException, NoSuchProviderException {
    if(this.provider!=null)
      return MessageDigest.getInstance(this.algorithm, this.provider);
    else if(this.strProvider!=null)
      return MessageDigest.getInstance(this.algorithm, this.strProvider);
    else
      return MessageDigest.getInstance(algorithm);
  }
  
  public final byte[] digest(String user, String password) throws NoSuchAlgorithmException, NoSuchProviderException {
    String message=this.mf.format(new Object[]{user, password});
    return this.getMessageDigest().digest(message.getBytes());
  }
}
