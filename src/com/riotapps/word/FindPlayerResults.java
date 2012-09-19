package com.riotapps.word;

import com.riotapps.word.hooks.Player;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FindPlayerResults extends Activity  implements View.OnClickListener{
	private static final String TAG = FindPlayerResults.class.getSimpleName();
	
	
	 @Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.findplayerresults);
			
			Intent i = getIntent();
		 	Player player = (Player) i.getParcelableExtra(Constants.EXTRA_PLAYER);
			
		 	TextView tvPlayerName = (TextView)findViewById(R.id.tvPlayerName);
		 	tvPlayerName.setText(player.getNickname());
		 	
		 	ImageFetcher imageLoader = new ImageFetcher(this, 30, 30, 4);
			imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
			ImageView ivPlayer = (ImageView) findViewById(R.id.ivPlayer);
			android.util.Log.i(TAG, "FindPlayerResults: playerImage=" + player.getImageUrl());
			
			imageLoader.loadImage(player.getImageUrl(), ivPlayer); //default image
			
			int badgeId = getResources().getIdentifier("com.riotapps.word:drawable/" + player.getBadgeDrawable(), null, null);
			ImageView ivBadge = (ImageView)findViewById(R.id.ivBadge);	 
			ivBadge.setImageResource(badgeId);
			
			Button bAddToGame = (Button)findViewById(R.id.bAddToGame);
			bAddToGame.setOnClickListener(this);
		}


	@Override
	public void onClick(View v) {
		switch(v.getId()){  
        case R.id.bAddToGame:  
        	this.addPlayer();
			break;

    	}
	}
	 
	private void addPlayer(){
		
	}
}
