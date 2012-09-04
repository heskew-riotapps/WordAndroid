package com.riotapps.word.utils;

import android.util.Log;

public class Logger {

	public static void w(String tag, String msg){
		//is logging on?
		
		Log.w(tag, msg);
	}
	
}
