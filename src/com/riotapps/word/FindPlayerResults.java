package com.riotapps.word;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FindPlayerResults extends FragmentActivity  implements View.OnClickListener{
	private static final String TAG = FindPlayerResults.class.getSimpleName();
	
	private Game game;
	private Player player;
	
	 @Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.findplayerresults);
			
			Intent i = getIntent();
		 	this.player = (Player) i.getParcelableExtra(Constants.EXTRA_PLAYER);
		 	this.game =  (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
		 	
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
		
		try 
		{
			this.game = GameService.addPlayerToGame(this, this.game, this.player);
			
			//go to 
			Intent intent = new Intent(this, com.riotapps.word.FindPlayerResults.class);
    	      //  intent.putExtra("gameId", game.getId());
    	      //	intent.putExtra("game", s);
       	    intent.putExtra(Constants.EXTRA_GAME, this.game);
    	    intent.putExtra(Constants.EXTRA_PLAYER, player);
    	    this.startActivity(intent);
		} 
		catch (DesignByContractException e) {
			DialogManager.SetupAlert(this, this.getString(R.string.oops), e.getMessage(), true);
		}
	}
}
