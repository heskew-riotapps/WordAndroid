package com.riotapps.word;

import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
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
	private Player opponent;
	
	 @Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.findplayerresults);
			
			PlayerService.loadPlayerInHeader(this);
			
			Intent i = getIntent();
		 	this.opponent = (Player) i.getParcelableExtra(Constants.EXTRA_PLAYER);
		 	this.game =  (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
		 	
		 	TextView tvPlayerName = (TextView)findViewById(R.id.tvPlayerName);
		 	tvPlayerName.setText(opponent.getNickname());
		 	
			TextView tvPlayerWins = (TextView)findViewById(R.id.tvPlayerWins);
			//tvPlayerWins.setText(String.format(this.getString(R.string.line_item_num_wins),opponent.getNumWins()));
			
			if (opponent.getNumWins() == 1){
				tvPlayerWins.setText(this.getString(R.string.line_item_1_win)); 
			}
			else if (opponent.getNumWins() == -1){
				tvPlayerWins.setText(this.getString(R.string.line_item_invited)); 
			}
			else{
				tvPlayerWins.setText(String.format(this.getString(R.string.line_item_num_wins),opponent.getNumWins())); 
			}
		 	
		 	ImageFetcher imageLoader = new ImageFetcher(this, 34, 34, 0);
			imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
			ImageView ivPlayer = (ImageView) findViewById(R.id.ivPlayer);
			android.util.Log.i(TAG, "FindPlayerResults: playerImage=" + opponent.getImageUrl());
			
			imageLoader.loadImage(opponent.getImageUrl(), ivPlayer); //default image
			
			int badgeId = getResources().getIdentifier("com.riotapps.word:drawable/" + opponent.getBadgeDrawable(), null, null);
			ImageView ivBadge = (ImageView)findViewById(R.id.ivBadge);	 
			ivBadge.setImageResource(badgeId);
			
			Button bAddToGame = (Button)findViewById(R.id.bAddToGame);
			bAddToGame.setOnClickListener(this);
			bAddToGame.setText(String.format(this.getString(R.string.add_player_to_game_button_text), opponent.getNickname()));
			
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
			this.game = GameService.addPlayerToGame(this, this.game, this.opponent);

			Intent intent = new Intent(this, com.riotapps.word.AddOpponents.class);
       	    intent.putExtra(Constants.EXTRA_GAME, this.game);
    	    this.startActivity(intent);
		} 
		catch (DesignByContractException e) {
			DialogManager.SetupAlert(this, this.getString(R.string.oops), e.getMessage(), true);
		}
	}
}
