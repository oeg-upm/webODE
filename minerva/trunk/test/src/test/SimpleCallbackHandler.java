package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.NameCallback;

/**
 * SampleCallbackHandler.java
 * Implementation of the CallbackHandler Interface
 *
 * @author Copyright (c) 2000-2002 by BEA Systems, Inc. All Rights
 * Reserved.
 */
class SampleCallbackHandler implements CallbackHandler {
  public SampleCallbackHandler() { }

  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    for(int i = 0; i < callbacks.length; i++) {
      if(callbacks[i] instanceof NameCallback) {
        // If username not supplied on cmd line, prompt the user
        // for the username.
        NameCallback nc = (NameCallback)callbacks[i];
        System.out.print(nc.getPrompt());
        System.out.flush();
        String str="";
        char c;
        while((c=(char)System.in.read())!='\n' && c!='\r')
          str+=c;
        if(c=='\r') System.in.read();
//        String str=(new BufferedReader(new InputStreamReader(System.in))).readLine();
//        System.out.println("NOMBRE:"+str);
        nc.setName(str);
      }
      else if(callbacks[i] instanceof PasswordCallback) { 
        PasswordCallback pc = (PasswordCallback)callbacks[i];

        System.out.print(pc.getPrompt());
        System.out.flush();

        // Note: JAAS specifies that the password is a char[]
        // rather than a String.
//        String tmpPassword = (new BufferedReader(new InputStreamReader(System.in))).readLine();
        String tmpPassword="";
        char c;
        while((c=(char)System.in.read())!='\n' && c!='\r')
          tmpPassword+=c;
        if(c=='\r') System.in.read();
//        System.out.println("PASSWORD:" + tmpPassword);
        int passLen = tmpPassword.length();
        char[] passwordArray = new char[passLen];
        for(int passIdx = 0; passIdx < passLen; passIdx++)
          passwordArray[passIdx] = tmpPassword.charAt(passIdx);
        pc.setPassword(passwordArray);
      }
      else {
        throw new UnsupportedCallbackException(callbacks[i],
                                           "Unrecognized Callback");
      }
    }
  }
}