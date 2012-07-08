package com.riotapps.word;

import com.riotapps.word.utils.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainLanding  extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlanding);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);
        
        //Toast t = Toast.makeText(this, "Hello " + settings.getString(Constants.USER_PREFS_USER_ID, "bywbyw"), Toast.LENGTH_LONG);  
	    //t.show();
        
       
        TextView tvFB =(TextView)findViewById(R.id.startByFacebook);
        TextView tvByOpponent =(TextView)findViewById(R.id.startByOpponent);

        //if not in by facebook, hide this option
        tvFB.setVisibility(View.GONE);
        //if num complete games = zero, hide this option
        tvByOpponent.setVisibility(View.GONE);
        
    }
}
        
