package com.riotapps.word.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOHelper {

	public static String streamToString(InputStream iStream)
	{
		 BufferedReader r = new BufferedReader(new InputStreamReader(iStream));
	     StringBuilder total = new StringBuilder();
	     String line;
	     try {
			while ((line = r.readLine()) != null) {
			     total.append(line);
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return total.toString();
	}
}
