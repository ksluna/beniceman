package com.beniceman.mathfacts.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonIce {
	
	public static String hashIt(String toHash) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(toHash.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        return sb.toString();
 		
	}

}
