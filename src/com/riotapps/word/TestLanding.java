package com.riotapps.word;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

public class TestLanding extends FragmentActivity implements View.OnClickListener{
	 final Context context = this;	
		TextView txtJoinNative;
		TextView txtMainLanding;
		TextView txtRules;
		TextView txtBadges;
		TextView txtWelcome;
		TextView txtGameSurface;
		TextView txt2d;
			
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.testlanding);

	        txtJoinNative = (TextView) findViewById(R.id.testLandingJoinNative);
	        txtJoinNative.setOnClickListener(this);
	        txtMainLanding = (TextView) findViewById(R.id.testLandingMainLanding);
	        txtMainLanding.setOnClickListener(this); 
	        txtRules = (TextView) findViewById(R.id.testLandingRules);
	        txtRules.setOnClickListener(this); 
	        txtBadges = (TextView) findViewById(R.id.testLandingBadges);
	        txtBadges.setOnClickListener(this); 
	        txtWelcome = (TextView) findViewById(R.id.testLandingWelcome);
	        txtWelcome.setOnClickListener(this); 
	        txtGameSurface = (TextView) findViewById(R.id.testLandingGameSurface);
	        txtGameSurface.setOnClickListener(this); 
	        
	    }
	
	    @Override 
	    public void onClick(View v) {
	    	Intent goToActivity;
	    	
	    	switch(v.getId()){  
	        case R.id.testLandingJoinNative:  
	        	goToActivity = new Intent(getApplicationContext(), JoinNative.class);
				startActivity(goToActivity);
				break;
	       	case R.id.testLandingMainLanding:  
        		goToActivity = new Intent(getApplicationContext(), StartGame.class);
        		startActivity(goToActivity);
        		break;
	       	
	       	case R.id.testLandingBadges:  
        		goToActivity = new Intent(getApplicationContext(), Badges.class);
        		startActivity(goToActivity);
        		break;
	       	case R.id.testLandingWelcome:  
        		goToActivity = new Intent(getApplicationContext(), Welcome.class);
        		startActivity(goToActivity);   
        		break;
	     	case R.id.testLandingGameSurface:  
        		goToActivity = new Intent(getApplicationContext(), GameSurface.class);
        		startActivity(goToActivity);
        		break;

	    	}
	    	
        }  
	 }

