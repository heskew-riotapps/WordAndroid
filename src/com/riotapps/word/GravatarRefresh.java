package com.riotapps.word;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
 

import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;

public class GravatarRefresh extends FragmentActivity implements View.OnClickListener{
	
	 
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.gravatarrefresh);

	        Player player = PlayerService.getPlayerFromLocal();
	   		PlayerService.loadPlayerInHeader(this);
	   		
	   		Button bOK = (Button) findViewById(R.id.bOK);
	   		bOK.setOnClickListener(this);
	   		
	   		TextView tvRefreshed = (TextView) findViewById(R.id.tvRefreshed);
	   		tvRefreshed.setText(String.format(this.getString(R.string.gravatar_refreshed_text, player.getEmail())));
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

