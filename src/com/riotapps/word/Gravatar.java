package com.riotapps.word;

import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

public class Gravatar extends FragmentActivity implements View.OnClickListener{
	
	 
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.gravatar);
	       
	   		PlayerService.loadPlayerInHeader(this, false);
	   		
	   		Button bGravatar = (Button) findViewById(R.id.bGravatar);
	   		bGravatar.setOnClickListener(this);
	   		Button bGravatarRefresh = (Button) findViewById(R.id.bGravatarRefresh);
	   		bGravatarRefresh.setOnClickListener(this);
	   		
	 }

	@Override
	public void onClick(View v) {
		switch(v.getId()){  
        case R.id.bGravatar:  
        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GRAVATAR_SITE_URL));
	   		startActivity(browserIntent);
			break;
        case R.id.bGravatarRefresh:  
        	PlayerService.clearImageCache(this);
        	Intent intent = new Intent(this, GravatarRefresh.class);
    		startActivity(intent);
			break;
		}
		
	}
	@Override
	protected void onStart() {
		 
		super.onStart();
		 EasyTracker.getInstance().activityStart(this);
	}


	@Override
	protected void onStop() {
	 
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}   
}
