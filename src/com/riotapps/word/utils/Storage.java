package com.riotapps.word.utils;

 
import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
	
	public static SharedPreferences getSharedPreferences(){
		return getSharedPreferences(Constants.USER_PREFS);
	}

	public static SharedPreferences getSharedPreferences(String name){
		Context ctx = ApplicationContext.getAppContext();
		return ctx.getSharedPreferences(name, Context.MODE_PRIVATE);	
	}
	
	
}
