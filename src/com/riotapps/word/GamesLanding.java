package com.riotapps.word;

import com.riotapps.word.utils.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GamesLanding  extends FragmentActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameslanding);
        
        SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);
        
        Toast t = Toast.makeText(this, "Hello " + settings.getString(Constants.USER_PREFS_USER_ID, "bywbyw"), Toast.LENGTH_LONG);  
	    t.show();
    }
}
        
