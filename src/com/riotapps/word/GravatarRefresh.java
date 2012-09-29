package com.riotapps.word;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
 

import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;

public class GravatarRefresh extends FragmentActivity implements View.OnClickListener{
	
	 
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.gravatarrefresh);

	   		PlayerService.loadPlayerInHeader(this);
	   		
	   		Button bOK = (Button) findViewById(R.id.bOK);
	   		bOK.setOnClickListener(this);
	 
	 }
	 
	 @Override
		public void onClick(View v) {
			switch(v.getId()){  
	        case R.id.bOK:  
	        	Intent intent = new Intent(this, MainLanding.class);
	    		startActivity(intent);
				break;
			}
			
		}
}

