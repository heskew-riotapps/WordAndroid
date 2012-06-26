package com.riotapps.word;

import com.riotapps.word.hooks.PlayerService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JoinNative extends Activity {
	    /** Called when the activity is first created. */
		
		Button bCancel; 
		Button bSave;
		//EditText 
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.joinnative);
	        
	        bCancel = (Button) findViewById(R.id.bCancel);
	        bSave = (Button) findViewById(R.id.bSave);
	        
	        bCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();      
				}
			});
	        
	        
	        bSave.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PlayerService playerSvc = new PlayerService();
					
				//	playerSvc.SavePlayer(id)
					
				}
			});
	    }
	}
