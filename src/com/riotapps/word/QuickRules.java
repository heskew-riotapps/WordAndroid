package com.riotapps.word;

import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.word.hooks.PlayerService;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class QuickRules extends FragmentActivity{
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.quickrules);
	        
	        PlayerService.loadPlayerInHeader(this, false);

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
