package com.riotapps.word.utils;

import com.riotapps.word.BuildConfig;

import android.util.Log;

public class Logger {

	public static void w(String tag, String msg){
		//is logging on?
		if (BuildConfig.DEBUG) {
			Log.w(tag, msg);
		}
	}
	
	public static void d(String tag, String msg){
		//is logging on?
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void e(String tag, String msg){
		//is logging on?
		if (BuildConfig.DEBUG) {
			Log.e(tag, msg);
		}
	}
	
	public static void v(String tag, String msg){
		//is logging on?
		if (BuildConfig.DEBUG) {
			Log.v(tag, msg);
		}
	}
	
	public static void i(String tag, String msg){
		//is logging on?
		if (BuildConfig.DEBUG) {
			Log.i(tag, msg);
		}
	}
}
