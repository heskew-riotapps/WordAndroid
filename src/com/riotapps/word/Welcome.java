package com.riotapps.word;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.hooks.Player;
 
public class Welcome  extends FragmentActivity implements View.OnClickListener{
	private static final String TAG = Welcome.class.getSimpleName();

	Player player;
    final Welcome context = this;	

  
	TextView txtFB;
	TextView txtNative;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome);
        
        Logger.d(TAG, "Welcome onCreate called");
  
        
        txtFB = (TextView) findViewById(R.id.byFacebook);
        txtFB.setOnClickListener(this);
        txtNative = (TextView) findViewById(R.id.byEmail);
        txtNative.setOnClickListener(this);   
    }

 
    
    @Override 
    public void onClick(View v) {
    	Intent intent;
    	switch(v.getId()){  
        case R.id.byFacebook:  
			intent = new Intent(this, ConnectToFacebook.class);
			startActivity(intent);
			break;
//        	this.connectToFacebook();
 //       	break;
        case R.id.byEmail:  
			intent = new Intent(this, JoinNative.class);
			startActivity(intent);
			break;
        }  
      }
    
 
}

