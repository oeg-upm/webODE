package es.upm.fi.dia.ontology.webode.service.util;

/**
 * A class for general text replacing.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public class TextReplacer 
{
    public static final char[] special = { '<', '>', '&' };
    public static final String[] escaped = { "&lt;", "&gt;", "&amp;" };

    public static String replaceSpecial (String str)
    {
	if (str == null)
	    return null;

	StringBuffer strBuffer = new StringBuffer();	
	char ch;
	
    ICantBelieveIUseThis: 
	for (int i = 0; i < str.length(); i++) {
	    ch = str.charAt (i);
	    for (int j = 0; j < special.length; j++) {
		if (ch == special[j]) {
		    strBuffer.append (escaped[j]);
		    continue ICantBelieveIUseThis;
		}
	    }
	    strBuffer.append (ch);
	}
	
	return strBuffer.toString();
    }

    public static String toList (Object[] ao)
    {
	StringBuffer strBuffer = new StringBuffer();
	strBuffer.append ("" + ao[0]);
	for (int i = 1; i < ao.length; i++)
	    strBuffer.append (", " + ao[i]);

	return strBuffer.toString();
    }
}


