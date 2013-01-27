package com.riotapps.word.utils;

import com.riotapps.word.BuildConfig;

import android.util.Log;

public class Logger {
	
	public static final boolean LOG_OK = true; 

	public static void w(String tag, String msg){
		if (BuildConfig.DEBUG && LOG_OK) {
			Log.w((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg), null);
		}
	}
	
	public static void w(String tag, String msg, Exception e){  
		//is logging on?
		 if (BuildConfig.DEBUG && LOG_OK) {    
			Log.w((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg));
		 }
	}
	
	public static void d(String tag, String msg){  
		Logger.d((tag==null?"UNKNOWN_TAG":tag), msg, null);
	}
	
	
	public static void d(String tag, String msg, Exception e){
		//is logging on?
		if (BuildConfig.DEBUG && LOG_OK) {
			Log.d((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg));
		}
	}
	
	public static void longInfo(String tag, String str) {
	    if(str.length() > 1000) {
	        Log.d(tag, str.substring(0, 1000));
	        longInfo(tag, str.substring(1000));
	    } else
	        Log.d(tag, str);
	}

	public static void e(String tag, String msg){
		Logger.e((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg), null);
		
	}
	
	public static void e(String tag, String msg, Exception e){
		////is logging on?
		if (BuildConfig.DEBUG && LOG_OK) {
			Log.e((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg));
		 }
	}
	
	public static void v(String tag, String msg){
		//is logging on?
		if (BuildConfig.DEBUG && LOG_OK) {
			Log.v((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg));
		 }
	}
	
	public static void i(String tag, String msg){
		//is logging on?
		if (BuildConfig.DEBUG && LOG_OK) {
			Log.i((tag==null?"UNKNOWN_TAG":tag), (msg==null?"unknown message":msg));
		}
	}
}
