package com.riotapps.word;

import com.riotapps.word.hooks.PlayerService;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
 

public class Badges extends FragmentActivity{
	
	private final Context context = this;

	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.badges);
	        
	        PlayerService.loadPlayerInHeader(this);
	 }
	
}
