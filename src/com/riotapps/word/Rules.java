package com.riotapps.word;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.riotapps.word.utils.Constants;

public class Rules extends Activity  {
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.rules);
	        
	        SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);
	        
	        Toast t = Toast.makeText(this, "Hello " + settings.getString(Constants.USER_PREFS_USER_ID, "bywbyw"), Toast.LENGTH_LONG);  
		    t.show();
	    }
}
