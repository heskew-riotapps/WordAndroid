package com.riotapps.word;

import java.util.ArrayList;
import java.util.List;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.TrayTile;
import com.riotapps.word.ui.GameState;
import com.riotapps.word.ui.GameStateService;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.ui.GameTile;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class GameSurface extends FragmentActivity implements View.OnClickListener{
	private static final String TAG = GameSurface.class.getSimpleName();
	GameSurface context = this;
	GameSurfaceView gameSurfaceView;
	ImageFetcher imageLoader;
	private RelativeLayout scoreboard;
	 SurfaceView surfaceView;
	 Button bShuffle;
	 Button bChat;
	 Button bPlayedWords;
	//View bottom;
	
	public static final int MSG_SCOREBOARD_VISIBILITY = 1;
	public static final int MSG_POINTS_SCORED = 2;
	public static final int SCOREBOARD_HEIGHT = 30;
	public static final int BUTTON_CONTROL_HEIGHT = 48;
	private int windowHeight;
	private int scoreboardHeight;
	private Game game;
	private GameState gameState;
 
	private Player player;
	private TextView tvNumPoints;
	private PlayerGame contextPlayerGame;
 

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getScoreboardHeight() {
		return scoreboardHeight;
	}

	public void setScoreboardHeight(int scoreboardHeight) {
		this.scoreboardHeight = scoreboardHeight;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamesurface);
		//setContentView(new GameSurfaceView2(this));
		
		//gravatar size = max size...default images
		//https://graph.facebook.com/hunter.eskew/picture?return_ssl_resources=1
	//	String gravatar = "http://graph.facebook.com/donna.guyton/picture?r=1&type=square"; //"http://www.gravatar.com/avatar/" + Utils.md5("hunter.eskew@gmail.com");
		
	//	imageLoader = new ImageFetcher(this, 100, 100);
	//	imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
	// 	this.surfaceView = (SurfaceView)findViewById(R.id.gameSurface);
		
	 	//this.surfaceView = new GameSurfaceView(this);
	 	
	 //	this.scoreboard = (RelativeLayout)findViewById(R.id.scoreboard);
	 //	this.scoreboard.setVisibility(android.view.View.GONE);
	 	//this.bottom = (View)findViewById(R.id.bottomControlsPlaceholder);
	 	//this.bottom.//se
		
		//Logger.d(TAG, "oncreate");
		
		// SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);
	     //this.contextUserId = settings.getString(Constants.USER_PREFS_USER_ID, "");  
	    this.player = PlayerService.getPlayerFromLocal(); 
		this.tvNumPoints = (TextView)findViewById(R.id.tvNumPoints);
	   
	     //check for turn number, if not a match, clearGameState
	     
	     
	 //    Log.w(TAG, "contextUserID=" + this.contextUserId);
		
		 Display display = getWindowManager().getDefaultDisplay(); 
	     this.windowHeight = display.getHeight();  // deprecated
	     
	     
	 //    Log.w(TAG, "long press " + android.view.ViewConfiguration.getLongPressTimeout() + 
	  //  		 " dbltap " + android.view.ViewConfiguration.getDoubleTapTimeout() + 
	 //   		  " tap  " + android.view.ViewConfiguration.getTapTimeout());
	     
	     
	  	this.scoreboard = (RelativeLayout)findViewById(R.id.scoreboard);
	  	this.scoreboardHeight = this.scoreboard.getHeight();
	 //	ImageView ivPlayer = (ImageView) findViewById(R.id.ivPlayerScoreboard);
	// 	imageLoader.loadImage(gravatar, ivPlayer); //default image
	    
	 	Bundle extras = getIntent().getExtras(); 
	 	if(extras !=null)
	 	{
	 		String value = extras.getString("gameId");
	 	}
	 	 
	 	//Logger.d(TAG, "Game about to be fetched from extra");
	 	Intent i = getIntent();
	 	String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	//this.game = (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
	 	this.game = GameService.getGameFromLocal(gameId); //(Game) i.getParcelableExtra(Constants.EXTRA_GAME);
	 	
	 	//temp
	 	//this.game = getTempGame();

		this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
		this.gameSurfaceView.construct(this);
		this.gameSurfaceView.setParent(this);
	 	
		//Logger.d(TAG, "scoreboard about to be loaded");
	 	//this.loadScoreboard();
	 	
		this.contextPlayerGame = GameService.loadScoreboard(this, this.game, this.player);
	 	
	 	this.fillGameState();
	 	
	 	bShuffle = (Button) findViewById(R.id.bShuffle);
	 	bChat = (Button) findViewById(R.id.bChat);
		bPlayedWords = (Button) findViewById(R.id.bPlayedWords);
	 	bShuffle.setOnClickListener(this);
	 	bChat.setOnClickListener(this);
	 	bPlayedWords.setOnClickListener(this);
	 	
	//	this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
	// 	this.gameSurfaceView.setParent(this);
	 	
	 	
	 //	this.gameSurfaceView.setGame(game);
	 	//retrieve game from server
	 	 
	 	
//	 	 ImageView iv;
 //        if (convertView == null)
 //            convertView = iv = new ImageView(UrlImageViewHelperSample.this);
 //        else
 //            iv = (ImageView)convertView;
         
         // yep, that's it. it handles the downloading and showing an interstitial image automagically.
         // UrlImageViewHelper.setUrlDrawable(ivPlayer, getItem(position), R.drawable.badge_0, null);
         
 //        return iv;
		//this._surfaceView = new GameSurfaceView(this); 
	}
	
	 public Handler updateHandler = new Handler(){
	        /** Gets called on every message that is received */
	        // @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what){
	            case GameSurface.MSG_POINTS_SCORED:
	            
	            	break;
	            }
	            super.handleMessage(msg);
	        }
	    };

	 private void fillGameState(){
		 this.gameState = GameStateService.getGameState(context, "123"); ///fix this temp code
		 
		 //if this.turn != gameState.turn, clearGameState
		 
		 ////come back and add this logic back
		 
		 if (this.gameState.getTrayTiles().size() == 0){
			 for(int x = 0; x < 7; x++){
				 TrayTile trayTile = new TrayTile();
				 trayTile.setIndex(x);
				 trayTile.setLetter(this.contextPlayerGame.getTrayLetters().size() > x ? this.contextPlayerGame.getTrayLetters().get(x) : "");
				 this.gameState.getTrayTiles().add(trayTile);
				 
			 }
			 //this.gameState.setTrayTiles(this.contextPlayerGame.getTrayLetters());
		 }
	 }
	 
	    
	 public class UpdateThread implements Runnable {
	    	 
	        @Override
	        public void run() {
	             while(true){
	            	 GameSurface.this.updateHandler.sendEmptyMessage(0);
	            }
	        }
	 
	    }

	    
//	public RelativeLayout getScoreboard() {
//		return scoreboard;
//	}

//	public void setScoreboard(RelativeLayout scoreboard) {
//		this.scoreboard = scoreboard;
//	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.w(TAG, "onDestroy called");
		
		this.gameSurfaceView.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.w(TAG, "onStop called");
		this.gameSurfaceView.onStop();
		super.onStop();
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.w(TAG, "onPause called");
		super.onPause();
		this.gameSurfaceView.onPause();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.w(TAG, "onResume called");
		super.onResume();
		this.gameSurfaceView.onResume();
	}

	
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		// TODO Auto-generated method stub
//		super.onWindowFocusChanged(hasFocus);
//		this.gameSurfaceView.onWindowFocusChanged();
//	}

//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		super.onBackPressed();
//		this.gameSurfaceView.onBackPressed();
//	}

	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		//override back button in case user just started game. this will make sure they don;t back through 
		//all of the pick opponent activities
		 Intent intent = new Intent(this.context, com.riotapps.word.MainLanding.class);
	     this.context.startActivity(intent);
	     this.finish();
	     
	}

	@Override
	protected void onRestart() {
		Log.w(TAG, "onRestart called");
		super.onRestart();
		this.gameSurfaceView.onRestart();
		

	}

	 @Override 
	    public void onClick(View v) {
	    	Intent intent;
	    	
	    	switch(v.getId()){  
		        case R.id.bShuffle:  
		        	this.gameSurfaceView.shuffleTray();
					break;
		        case R.id.bChat:  
		        	intent = new Intent(this, GameChat.class);
		        	intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
					startActivity(intent);
					break;
		        case R.id.bPlayedWords:  
		        	intent = new Intent(this, GameHistory.class);
		        	intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
					startActivity(intent);
					break;
	    	}
	 }
	
}
