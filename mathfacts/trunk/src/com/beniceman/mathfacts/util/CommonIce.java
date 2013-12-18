package com.beniceman.mathfacts.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.SharedPreferences;

import com.beniceman.mathfacts.R;

public class CommonIce {

	public static String hashIt(String toHash) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(toHash.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return sb.toString();

	}

	public Boolean callLogin(String pUrl, String pPassword, String pEmail,
			String pDisplayName) {
		String result = "";
		String error = "";

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;

		try {

			URL url = new URL(pUrl + "?p=" + pPassword + "&email=" + pEmail
					+ "&displayname=" + pDisplayName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			InputStream is = null;
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				is = conn.getInputStream();
			} else {
				InputStream err = conn.getErrorStream();
				error = err.toString();
				result = "0";
				return false;
			}
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						error += e.getMessage();
						result = "0";
						return false;

					}
				}
			}
			result = sb.toString();
			conn.disconnect();

		} catch (MalformedURLException e) {
			error += e.getMessage();
			result = "0";
			return false;
		} catch (IOException e) {
			error += e.getMessage();
			result = "0";
			return false;
		}
		return true;
	} // end callWebService()

}
