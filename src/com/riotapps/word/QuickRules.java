package com.riotapps.word;

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
	    
}
