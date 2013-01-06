package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.gson.Gson;
import com.riotapps.word.hooks.AlphabetService;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.CustomDialog;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.ui.GameAction.GameActionType;
import com.riotapps.word.ui.GameState;
import com.riotapps.word.ui.GameStateService;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.ui.PlacedResult;
import com.riotapps.word.ui.WordLoaderThread;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.CustomProgressDialog;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.RequestType;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.ads.*;
import com.google.ads.AdRequest.ErrorCode;

//import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class GameSurface extends FragmentActivity implements View.OnClickListener, AdListener{
	private static final String TAG = GameSurface.class.getSimpleName();
	GameSurface context = this;
	GameSurfaceView gameSurfaceView;
	ImageFetcher imageLoader;
	private RelativeLayout scoreboard;
	 SurfaceView surfaceView;
	 NetworkTask runningTask = null;
	 Button bRecall;
	 Button bPlay;
	 Button bSkip;
	 Button bShuffle;
	
	 private com.google.ads.InterstitialAd interstitial;
	 private GameActionType postTurnAction;
	 private CustomProgressDialog preInterstitialSpinner;
	 
	 //View bottom;
	private int currentPoints = 0;
	public static final int MSG_SCOREBOARD_VISIBILITY = 1;
	public static final int MSG_POINTS_SCORED = 2;
	//public static int SCOREBOARD_HEIGHT = 30;
	//public static final int BUTTON_CONTROL_HEIGHT = 48;
	private boolean buttonsLoaded = false;
	private int windowHeight;
	private int scoreboardHeight;
	private Game game;
	private GameState gameState;
	private AlphabetService alphabetService;
//	private WordService wordService;
 
	private Player player;
	private TextView tvNumPoints;
	private PlayerGame contextPlayerGame;
	
	private WordLoaderThread wordLoaderThread = null;
 

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
 
	    this.player = PlayerService.getPlayerFromLocal(); 
		this.tvNumPoints = (TextView)findViewById(R.id.tvNumPoints);
 
		 Display display = getWindowManager().getDefaultDisplay(); 
	     this.windowHeight = display.getHeight();  // deprecated
	     
 
	  	this.scoreboard = (RelativeLayout)findViewById(R.id.scoreboard);
	  	this.scoreboardHeight = this.scoreboard.getHeight();
	 //	ImageView ivPlayer = (ImageView) findViewById(R.id.ivPlayerScoreboard);
	// 	imageLoader.loadImage(gravatar, ivPlayer); //default image
	    
	 //	Bundle extras = getIntent().getExtras(); 
	 //	if(extras !=null)
	// 	{
	// 		String value = extras.getString("gameId");
	// 	}
	 	 
	 	//Logger.d(TAG, "Game about to be fetched from extra");
	 	Intent i = getIntent();
	 	String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	//this.game = (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
	 	this.game = GameService.getGameFromLocal(gameId); //(Game) i.getParcelableExtra(Constants.EXTRA_GAME);
	 	

  		//Gson gson = new Gson();  
	    //Logger.d(TAG, "game json=" + gson.toJson(game));
	 	
	 	//temp
	 	//this.game = getTempGame();

		this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
		this.alphabetService = new AlphabetService(context);
		//this.wordService = new WordService(context);
		
		ApplicationContext appContext = (ApplicationContext)this.getApplicationContext();
		//appContext.getWordService()
		
		this.gameSurfaceView.construct(this, this.alphabetService, appContext.getWordService());
		this.gameSurfaceView.setParent(this);
		
	
		 Logger.d(TAG, "SetDerivedValues  this.getWindowHeight=" +  this.getWindowHeight() );
		 Logger.d(TAG, "SetDerivedValues  this.gameSurfaceView.height=" +  gameSurfaceView.getHeight() );
	 	
		//Logger.d(TAG, "scoreboard about to be loaded");
	 	//this.loadScoreboard();
	 	
		this.setupGame();
	 	
		this.checkGameStatus();
		///this.wordLoaderThread = new WordLoaderThread(appContext.getWordService(), this.game, this.player.getId());
	 	
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
	
	private void setupGame(){
		Logger.d(TAG,"setupGame game turn=" + this.game.getTurn());
		this.contextPlayerGame = GameService.loadScoreboard(this, this.game, this.player);
	 	
	 	this.fillGameState();
	 	
	 	this.setupButtons();

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

	 public void switchToRecall(){
		//by default recall button will be hidden, it will be switched with shuffle button when a letter is dropped on the board
		 context.runOnUiThread(new handleButtonSwitchRunnable(2));
	 }

	 public void switchToShuffle(){
		 context.runOnUiThread(new handleButtonSwitchRunnable(1));
	 }
	 
	 public void switchToPlay(){
			//by default play button will be hidden, it will be switched with skip button when a letter is dropped on the board
			 context.runOnUiThread(new handleButtonSwitchRunnable(3));
		 }

	public void switchToSkip(){
			 context.runOnUiThread(new handleButtonSwitchRunnable(4));
		 }
		 
	 
	 public void openAlertDialog(String title, String message){
		 DialogManager.SetupAlert(this.context, title, message);
	 }
	 
	 
	 private class handleButtonSwitchRunnable implements Runnable {
		 private int activeButton; //1 = shuffle, 2 = recall 	
		 
		 public handleButtonSwitchRunnable(int activeButton){
		 		this.activeButton = activeButton;
		 	}
		 
		    public void run() {
		    	switch (this.activeButton){
			    	case 1:
			    		bRecall.setVisibility(View.GONE);
					 	bShuffle.setVisibility(View.VISIBLE);
			    		break;
			    	case 2:
			    		bRecall.setVisibility(View.VISIBLE);
					 	bShuffle.setVisibility(View.GONE);
			    		break;
			    	case 3:
			    		bPlay.setVisibility(View.VISIBLE);
					 	bSkip.setVisibility(View.GONE);
			    		break;
			    	case 4:
			    		bPlay.setVisibility(View.GONE);
			    		bSkip.setVisibility(View.VISIBLE);
			    		break;
		    	}
		    }
	  }
	 
	 public void setPointsView(int points){
		 this.currentPoints = points;
		 context.runOnUiThread(new handlePointsViewRunnable(points, 1));
	 }
	 
	 public void setPointsAfterPlayView(){
		 context.runOnUiThread(new handlePointsViewRunnable(this.currentPoints, 2));
	 }
	 
	 private class handlePointsViewRunnable implements Runnable {
		 private int points;   	
		 private int type; //1 = normal, 2 = afterPlay  	
		 
		 public handlePointsViewRunnable(int points, int type){
		 	this.points = points;
		 	this.type = type;
		 	}
		 
		 
		    public void run() {
		    	if (this.type == 1){
			    	if (points == 0){
			 			tvNumPoints.setVisibility(View.INVISIBLE);
			 		}
			 		else {
			 			tvNumPoints.setText(String.format(context.getString(R.string.scoreboard_num_points),points));
			 			tvNumPoints.setVisibility(View.VISIBLE);
			 			tvNumPoints.setTextColor(Color.parseColor(context.getString(R.color.scoreboard_text_color)));
			 		}
		    	}
		    	else { //last played points returned after a play 
		    		if (points == 0){
			 			tvNumPoints.setVisibility(View.INVISIBLE);
			 		}
			 		else {
			 			tvNumPoints.setText(String.format(context.getString(R.string.scoreboard_num_points_scored),points));
			 			tvNumPoints.setVisibility(View.VISIBLE);
			 			tvNumPoints.setTextColor(Color.parseColor(context.getString(R.color.scoreboard_text_last_played_color)));
			 		}
		    		
		    	}
		    /*	switch (this.activeButton){
			    	case 1:
			    		bRecall.setVisibility(View.GONE);
					 	bShuffle.setVisibility(View.VISIBLE);
			    		break;
			    	case 2:
			    		bRecall.setVisibility(View.VISIBLE);
					 	bShuffle.setVisibility(View.GONE);
			    		break;
		    	}
		    	*/
		    }
	  }
	 
	 private void checkGameStatus(){
		
		 //completed
		 //first check to see if this score has already been alerted (from local storage) 
		 if (this.game.getStatus() == 3 || this.game.getStatus() == 4){
			 if (!GameService.checkGameAlertAlreadyShown(this, this.game.getId())) {
				 String message = this.game.getWinnerAlertText(this, this.contextPlayerGame);
			 
				 DialogManager.SetupAlert(this, this.getString(R.string.game_alert_game_over_title), message, Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);
			 }
		 }
	 }
	 
	 private void setupButtons(){
		  buttonsLoaded = true;
		Button bRematch = (Button) findViewById(R.id.bRematch);
		this.bRecall = (Button) findViewById(R.id.bRecall);
		this.bPlay = (Button) findViewById(R.id.bPlay);
		this.bSkip = (Button) findViewById(R.id.bSkip);
		this.bShuffle = (Button) findViewById(R.id.bShuffle);
		Button bChat = (Button) findViewById(R.id.bChat);
		Button bSwap = (Button) findViewById(R.id.bSwap);
		Button bPlayedWords = (Button) findViewById(R.id.bPlayedWords);
		Button bCancel = (Button) findViewById(R.id.bCancel);
		Button bResign = (Button) findViewById(R.id.bResign);
		Button bDecline = (Button) findViewById(R.id.bDecline);
	 	this.bShuffle.setOnClickListener(this);
	 	bChat.setOnClickListener(this);
	 
	 	this.bSkip.setOnClickListener(this);
	 	bPlayedWords.setOnClickListener(this);
	 	this.bRecall.setOnClickListener(this);
	 	this.bPlay.setOnClickListener(this);
	 	
	 	String btnTextColor = this.game.isContextPlayerTurn(this.player) ? this.getString(R.color.button_text_color_on) : this.getString(R.color.button_text_color_off);
	 	
	 	this.bPlay.setTextColor(Color.parseColor(btnTextColor));
	 	this.bSkip.setTextColor(Color.parseColor(btnTextColor));
	 	bCancel.setTextColor(Color.parseColor(btnTextColor));
	 	bDecline.setTextColor(Color.parseColor(btnTextColor));
	 	bResign.setTextColor(Color.parseColor(btnTextColor));
	 	bSwap.setTextColor(Color.parseColor(btnTextColor));
	 	
	 	bRecall.setVisibility(View.GONE);
	 	bShuffle.setVisibility(View.VISIBLE);
	 	
	 	bPlay.setVisibility(View.GONE);
	 	bSkip.setVisibility(View.VISIBLE);
	 	
	 	bPlay.setClickable(this.game.isContextPlayerTurn(this.player));
	 	
	 	Logger.d(TAG, "getNumLettersLeft=" + this.game.getNumLettersLeft());
	 	
	 //	if (this.game.getNumLettersLeft() > 0){
	 		bSwap.setOnClickListener(this);
	 		bSwap.setClickable(this.game.isContextPlayerTurn(this.player));
	// 	}
	 	
	 	bSkip.setClickable(this.game.isContextPlayerTurn(this.player));
	 	bResign.setClickable(this.game.isContextPlayerTurn(this.player));

	 	
	 	//by default recall button will be hidden, it will be switched with shuffle button when a letter is dropped on the board
	 	this.bRecall.setVisibility(View.GONE); 
	 	
	 	
	 	Logger.d(TAG, "setupButtons this.game.getNumActiveOpponents()=" + this.game.getNumActiveOpponents());
	 	Logger.d(TAG, "setupButtons this.game.getTurn()=" + this.game.getTurn());
	 	Logger.d(TAG, "setupButtons this.game.isContextPlayerStarter()=" + this.game.isContextPlayerStarter(this.player));
	 	Logger.d(TAG, "setupButtons this.game.getStatus()=" + this.game.getStatus());
	 	
	 	
	 	bChat.setCompoundDrawables(null, null, null, null);
	 	//set cancel button area mode:
	 	//if it's the first play of the game by starting player, it should be "CANCEL" mode
	 	//if it's the first play of the game by a non-starting player, it should be in "DECLINE" mode
	 	//if it's not the first play of the game, it should be in "RESIGN" mode
	 	
	 	if (this.game.getStatus() == 1) { //active
	 		if (GameService.checkGameChatAlert(context, this.game, true)){
	 			Drawable chatAlert = context.getResources().getDrawable( R.drawable.chat_alert );
	 			bChat.setCompoundDrawablesWithIntrinsicBounds(null, null, chatAlert, null);
	 			
	 			Logger.d(TAG, "setupButtons checkGameChatAlert=true");
	 		}
	 		
	 		bRematch.setVisibility(View.GONE);
	 		
		 	//the starting player gets one chance (one turn) to cancel
		 	if (this.game.getTurn() == 1 && this.game.isContextPlayerStarter(this.player)){
		 		bCancel.setOnClickListener(this);	
		 		bResign.setVisibility(View.GONE);
		 		bDecline.setVisibility(View.GONE);
		 	}
		 	else if (this.game.getNumActiveOpponents() == 1){
		 		if (this.game.getTurn() < 3 && !this.game.isContextPlayerStarter(this.player)){
		 			//in a two player game, the invited player gets one chance to decline
		 			//we check for turn 1 (which is not his turn) in case he sees the game before the starting player
		 			//makes the first move
		 			bCancel.setVisibility(View.GONE);	
			 		bResign.setVisibility(View.GONE);
			 		bDecline.setOnClickListener(this);
			 		bDecline.setVisibility(View.VISIBLE);
		 		}
		 		else{
		 			//else we are past each opponents first turn, therefore show the resign button 
		 			bCancel.setVisibility(View.GONE);	
			 		bResign.setOnClickListener(this);
			 		bDecline.setVisibility(View.GONE);
			 		bResign.setVisibility(View.VISIBLE);
			 		bResign.setClickable(this.game.isContextPlayerTurn(this.player));
		 		}
		 	}
		 	else if (this.game.getNumActiveOpponents() == 2){
		 		if (this.game.getTurn() < 3 && this.game.getContextPlayerOrder(this.player) < 3){
		 			//in a three player game, the invited players get one chance to decline
		 			//in this case we are checking for the second player in order
		 			//we check for turn 1 (which is not his turn) in case he sees the game before the starting player
		 			//makes the first move
		 			bCancel.setVisibility(View.GONE);	
			 		bResign.setVisibility(View.GONE);
			 		bDecline.setOnClickListener(this);
		 		}
		 		else if (this.game.getTurn() < 4 && this.game.getContextPlayerOrder(this.player) == 3){
		 			//in a three player game, the invited players get one chance to decline
		 			//in this case we are checking for the third player in order
		 			//we check for turn 1 and 2 (which are not his turns) in case he sees the game before the starting player
		 			//makes the first move or the second player makes a move
		 			bCancel.setVisibility(View.GONE);	
			 		bResign.setVisibility(View.GONE);
			 		bDecline.setOnClickListener(this);
		 		}
		 		else{
		 			//else we are past each opponents first turn, therefore show the resign button 
		 			bCancel.setVisibility(View.GONE);	
			 		bResign.setOnClickListener(this);
			 		bResign.setVisibility(View.VISIBLE);
			 		bResign.setClickable(this.game.isContextPlayerTurn(this.player));
			 		bDecline.setVisibility(View.GONE);
		 		}
		 	}
		 	else if (this.game.getNumActiveOpponents() == 3){
		 		if (this.game.getTurn() < 3 && this.game.getContextPlayerOrder(this.player) < 3){
		 			//in a four player game, the invited players get one chance to decline
		 			//in this case we are checking for the second player in order
		 			//we check for turn 1 (which is not his turn) in case he sees the game before the starting player
		 			//makes the first move
		 			bCancel.setVisibility(View.GONE);	
			 		bResign.setVisibility(View.GONE);
			 		bDecline.setOnClickListener(this);
		 		}
		 		else if (this.game.getTurn() < 4 && this.game.getContextPlayerOrder(this.player) == 3){
		 			//in a four player game, the invited players get one chance to decline
		 			//in this case we are checking for the third player in order
		 			//we check for turn 1 and 2(which are not his turns) in case he sees the game before the starting player
		 			//makes the first move or the second player makes a move
		 			bCancel.setVisibility(View.GONE);	
			 		bResign.setVisibility(View.GONE);
			 		bDecline.setOnClickListener(this);
		 		}
		 		else if (this.game.getTurn() < 5 && this.game.getContextPlayerOrder(this.player) == 4){
		 			//in a four player game, the invited players get one chance to decline
		 			//in this case we are checking for the fourth player in order
		 			//we check for turn 1, 2, 3 (which are not his turns) in case he sees the game before the starting player
		 			//makes the first move or the second player makes a move
		 			bCancel.setVisibility(View.GONE);	
			 		bResign.setVisibility(View.GONE);
			 		bDecline.setOnClickListener(this);
		 		}
		 		else{
		 			//else we are past each opponents first turn, therefore show the resign button 
		 			bCancel.setVisibility(View.GONE);	
			 		bResign.setOnClickListener(this);
			 		bDecline.setVisibility(View.GONE);
			 		bResign.setVisibility(View.VISIBLE);
			 		bResign.setClickable(this.game.isContextPlayerTurn(this.player));
		 		}
		 	}
		 	
	 	}
	 	else if(this.game.getStatus() == 3 || this.game.getStatus() == 4){
	 		bRecall.setVisibility(View.GONE);
	 		bSwap.setVisibility(View.GONE);
	 		bSkip.setVisibility(View.GONE);
	 		bPlay.setVisibility(View.GONE);
	 		bResign.setVisibility(View.GONE);
	 		bDecline.setVisibility(View.GONE);
	 		bCancel.setVisibility(View.GONE);
	 		bShuffle.setVisibility(View.GONE);
	 		if (this.game.getPlayedWords().size() == 0){
	 			bPlayedWords.setVisibility(View.GONE);
	 		}
	 		bRematch.setVisibility(View.VISIBLE);
	 		
	 		bRematch.setOnClickListener(this);
	 	}
	 
	}
	    
	    
	    
	 private void fillGameState(){
		 this.gameState = GameStateService.getGameState(context, this.game.getId());  
		 
		 //if this.turn != gameState.turn, clearGameState
		 
		 //if the game state is dated, clear it out
		 if (this.game.getContextPlayerTrayVersion(this.player) != this.gameState.getTrayVersion()){
			 this.gameState = GameStateService.clearGameState(context, this.game.getId());
		 }
		 
		// this.gameState.setTrayVersion(this.game.getContextPlayerTrayVersion(this.player));
		 
		 //load played tiles into game state
		 this.gameState.setPlayedTiles(this.game.getPlayedTiles());
		 
		 //locations are used to keep track of the tray tiles as they are placed on the board or tray
		 if (this.gameState.getLocations().size() == 0){

 //reset the game tiles based on the locally saved state (as long as the tray tiles have not been updated on the server)
			 this.gameState.setTrayVersion(this.game.getContextPlayerTrayVersion(this.player));
			 
			 int numTrayTiles = this.contextPlayerGame.getTrayLetters().size();
			 
			 for(int x = 0; x < 7; x++){
				// TrayTile trayTile = new TrayTile();
				// trayTile.setIndex(x);
				// trayTile.setLetter(this.contextPlayerGame.getTrayLetters().size() > x ? this.contextPlayerGame.getTrayLetters().get(x) : "");
				 this.gameState.addTrayLocation(x, numTrayTiles > x ? this.contextPlayerGame.getTrayLetters().get(x) : "");
				// this.gameState.getLocations().get(x).setTrayLocation(x);
				// this.gameState.getLocations().get(x).setLetter(this.contextPlayerGame.getTrayLetters().size() > x ? this.contextPlayerGame.getTrayLetters().get(x) : "");
			 }
			 //this.gameState.setTrayTiles(this.contextPlayerGame.getTrayLetters());
		 }
	 }
	 
	    
/*	 public class UpdateThread______ implements Runnable {
	    	 
	        @Override
	        public void run() {
	             while(true){
	            	 GameSurface.this.updateHandler.sendEmptyMessage(0);
	            }
	        }
	 
	    }

	 */   
//	public RelativeLayout getScoreboard() {
//		return scoreboard;
//	}

//	public void setScoreboard(RelativeLayout scoreboard) {
//		this.scoreboard = scoreboard;
//	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy called");
		
		this.gameSurfaceView.onDestroy();
		
	//	if (this.wordLoaderThread != null){
	//		this.wordLoaderThread.interrupt();
	//		this.wordLoaderThread = null;
	//	}
		
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStop called");
		this.gameSurfaceView.onStop();
	//	if (this.wordLoaderThread != null){
	//		this.wordLoaderThread.interrupt();
	//		this.wordLoaderThread = null;
	//	}
		
		
		super.onStop();
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause called");
		super.onPause();
		if (this.runningTask != null){
    		this.runningTask.cancel(true);
    	}
		this.gameSurfaceView.onPause();
		
	//	if (this.wordLoaderThread != null){
	//		this.wordLoaderThread.interrupt();
	//		this.wordLoaderThread = null;
	//	}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume called");
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
		this.handleBack(com.riotapps.word.MainLanding.class);
	}
	
	private void handleBack(Class<?> cls){
		if (this.runningTask != null){
    		this.runningTask.cancel(true);
    	}
		
		this.gameSurfaceView.onStop();
		Intent intent = new Intent(this.context, cls );
	    this.context.startActivity(intent);
	    this.finish();
	}

	@Override
	protected void onRestart() {
		//Log.w(TAG, "onRestart called");
		super.onRestart();
		this.gameSurfaceView.onRestart();
		if (buttonsLoaded){
			//reset buttons
			this.game = GameService.getGameFromLocal(game.getId());
			this.setupButtons();
		}
		

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
		        case R.id.bCancel:  
		        	this.handleCancel();
					break;
		        case R.id.bRecall:
		        	this.gameSurfaceView.recallLetters();
					break;
		        case R.id.bPlay:
		        	this.gameSurfaceView.onPlayClick();
					break;
		       case R.id.bSkip:
		        	this.gameSurfaceView.onPlayClick();
					break;
		       case R.id.bSwap:
		        	this.onSwapClick();
					break;
		        case R.id.bDecline:  
		        	this.handleDecline();
					break;
		        case R.id.bResign:  
		        	this.handleResign();
					break;
		        case R.id.bRematch:  
		        	this.handleRematch();
					break;
	    	}
	 }
	
	 
	    private void handleCancel(){
	    	final CustomDialog dialog = new CustomDialog(this, 
	    			this.getString(R.string.game_surface_cancel_game_confirmation_title), 
	    			this.getString(R.string.game_surface_cancel_game_confirmation_text),
	    			this.getString(R.string.yes),
	    			this.getString(R.string.no));
	    	
	    	dialog.setOnOKClickListener(new View.OnClickListener() {
		 		@Override
				public void onClick(View v) {
		 			dialog.dismiss(); 
		 			handleGameCancelOnClick();
		 		
		 		}
			});

	    	dialog.show();	
	    }
	    
	    private void handleDecline(){
	    	final CustomDialog dialog = new CustomDialog(this, 
	    			this.getString(R.string.game_surface_decline_game_confirmation_title), 
	    			this.getString(R.string.game_surface_decline_game_confirmation_text),
	    			this.getString(R.string.yes),
	    			this.getString(R.string.no));
	    	
	    	dialog.setOnOKClickListener(new View.OnClickListener() {
		 		@Override
				public void onClick(View v) {
		 			dialog.dismiss(); 
		 			handleGameDeclineOnClick();
		 		
		 		}
			});

	    	dialog.show();	
	    }

	    private void handleResign(){
	    	final CustomDialog dialog = new CustomDialog(this, 
	    			this.getString(R.string.game_surface_resign_game_confirmation_title), 
	    			this.getString(R.string.game_surface_resign_game_confirmation_text),
	    			this.getString(R.string.yes),
	    			this.getString(R.string.no));
	    	
	    	dialog.setOnOKClickListener(new View.OnClickListener() {
		 		@Override
				public void onClick(View v) {
		 			dialog.dismiss(); 
		 			handleGameResignOnClick();
		 		
		 		}
			});

	    	dialog.show();	
	    }
	    
	    private void handleRematch(){
	    	String rematchText = "";
	    	String opponent1 = "";
	    	String opponent2 = "";
	    	String opponent3 = "";
	    	List<Player> opponents = this.game.getAllOpponents(this.player);
	    	for (int i = 0; i < opponents.size(); i++){
	    		if (i == 0){opponent1 = opponents.get(i).getNameWithMaxLength(40);}
	    		else if (i == 1){opponent2 = opponents.get(i).getNameWithMaxLength(40);}
	    		else {opponent3 = opponents.get(i).getNameWithMaxLength(40);}
	    	}
	    	
	    	if (opponents.size() == 3){
	    		rematchText = String.format(this.getString(R.string.game_surface_rematch_with_3), opponent1, opponent2, opponent3);
	    	}
	    	else if (opponents.size() == 2){
	    		rematchText = String.format(this.getString(R.string.game_surface_rematch_with_2), opponent1, opponent2);
	    	}
	    	else {//single opponent
	    		rematchText = String.format(this.getString(R.string.game_surface_rematch_with_1), opponent1);	    		
	    	}
	    	
	    	final CustomDialog dialog = new CustomDialog(this, 
	    			this.getString(R.string.game_surface_rematch_title), 
	    			rematchText,
	    			this.getString(R.string.yes),
	    			this.getString(R.string.no));
	    	
	    	dialog.setOnOKClickListener(new View.OnClickListener() {
		 		@Override
				public void onClick(View v) {
		 			dialog.dismiss(); 
		 			handleGameRematchOnClick();
		 		
		 		}
			});

	    	dialog.show();	
	    }
	    
	    
	    private void handleGameCancelOnClick(){
	    	//stop thread first
	    	this.gameSurfaceView.onStop();
	    	try { 
				String json = GameService.setupCancelGame(context, this.game.getId());
				
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_cancelling), GameActionType.CANCEL_GAME);
				runningTask.execute(Constants.REST_GAME_CANCEL);
			} catch (DesignByContractException e) {
				 
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
			}
	    }
	    
	    private void handleGameDeclineOnClick(){
	    	//stop thread first
	    	this.gameSurfaceView.onStop();
	    	try { 
				String json = GameService.setupDeclineGame(context, this.game.getId());
				
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_sending), GameActionType.DECLINE_GAME);
				runningTask.execute(Constants.REST_GAME_DECLINE);
			} catch (DesignByContractException e) {
				 
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
			}
	    }
	    
	    private void handleGameResignOnClick(){
	    	//stop thread first
	    	this.gameSurfaceView.onStop();
	    	try { 
				String json = GameService.setupResignGame(context, this.game.getId());
				
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_sending), GameActionType.RESIGN);
				runningTask.execute(Constants.REST_GAME_RESIGN);
			} catch (DesignByContractException e) {
				 
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
			}
	    }
	    
	    private void handleGameRematchOnClick(){
	    	//stop thread first
	    	this.gameSurfaceView.onStop();
	    	try { 
		    	Game newGame = GameService.createGame(this.context, this.player);
		    	
		    	List<Player> opponents = this.game.getAllOpponents(this.player);
		    	for (Player opponent : opponents){
		    		newGame =  GameService.addPlayerToGame(this, newGame, opponent);
		    	}

				String json = GameService.setupStartGame(context, newGame);
				
				//kick off thread to start game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_starting_game), GameActionType.REMATCH);
				runningTask.execute(Constants.REST_CREATE_GAME_URL);
			} catch (DesignByContractException e) {
				 
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
			}
	    }
	    
	    
	    public void handleGamePlayOnClick(PlacedResult placedResult){
	    	//stop thread first
	    	
	    	//DialogManager.SetupAlert(context, "played", "clicked");
 	    	this.gameSurfaceView.stopThreadLoop();
	    	try { 
				String json = GameService.setupGameTurn(context, this.game, placedResult);
				
				Logger.d(TAG, "handleGamePlayOnClick json=" + json);
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_sending), GameActionType.PLAY);
				runningTask.execute(Constants.REST_GAME_PLAY);

			} catch (DesignByContractException e) {
				 
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
			}
			 
	    }
	    
	    public void handleGameSkipOnClick(){
	    	//stop thread first
	    	
	    	//DialogManager.SetupAlert(context, "played", "clicked");
 	    	this.gameSurfaceView.stopThreadLoop();
	    	try { 
				String json = GameService.setupGameSkip(context, this.game);
				
				Logger.d(TAG, "handleGameSkipOnClick json=" + json);
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_sending), GameActionType.SKIP);
				runningTask.execute(Constants.REST_GAME_SKIP);

			} catch (DesignByContractException e) {
				 
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
			}
			 
	    }
	    
	    private void onSwapClick(){
	    	
	    	final SwapDialog dialog = new SwapDialog(context, this.contextPlayerGame.getTrayLetters());
	    	 
	    /*	dialog.setOnOKClickListener(new View.OnClickListener() {
		 		@Override
				public void onClick(View v) {
		 			dialog.dismiss(); 
		 			handleGameSwapOnClick();
		 		}
			});
			*/ 
	    	dialog.show();
	    	
	    }
	    
	    public void handleGameSwapOnClick(List<String> swappedLetters){
	    	//stop thread first
	    	
	    	//DialogManager.SetupAlert(context, "played", "clicked");
 	    	this.gameSurfaceView.stopThreadLoop();
	    	try { 
				String json = GameService.setupGameSwap(context, this.game, swappedLetters);
				
				Logger.d(TAG, "handleGameSkipOnClick json=" + json);
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_sending), GameActionType.SWAP);
				runningTask.execute(Constants.REST_GAME_SWAP);

			} catch (DesignByContractException e) {
				 
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
			}
			 
	    }
	    
	    private class NetworkTask extends AsyncNetworkRequest{
			
	    	GameSurface context;
	    	//int action = 0; //1 = cancel game, 2 = decline, 3 = resign, 4 = play a turn
	    	GameActionType actionType;
	    		
	    		public NetworkTask(GameSurface ctx, RequestType requestType,
	    				String json,
	    				String shownOnProgressDialog,
	    				GameActionType actionType) {
	    			super(ctx, requestType, shownOnProgressDialog, json);
	    			this.context = ctx;
	    			this.actionType = actionType;
	    		 
	    		}

	    		@Override
	    		protected void onPostExecute(ServerResponse serverResponseObject) {
	    		 
	    			super.onPostExecute(serverResponseObject);
	    			
	    			this.handleResponse(serverResponseObject);

	    		}
	     
	    		private void handleResponse(ServerResponse serverResponseObject){
	    		     HttpResponse response = serverResponseObject.response;   
	    		     Exception exception = serverResponseObject.exception;   

	    		     if(response != null){  

	    		         InputStream iStream = null;  

	    		         try {  
	    		             iStream = response.getEntity().getContent();  
	    		         } catch (IllegalStateException e) {  
	    		             Log.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IllegalStateException " + e);  
	    		         } catch (IOException e) {  
	    		             Log.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IOException " + e);  
	    		         }  

	    		         int statusCode = response.getStatusLine().getStatusCode();  
	    		         
	    		         Log.d(GameSurface.TAG, "StatusCode: " + statusCode);
	    		         Gson gson = new Gson();

	    		         Player player = null;
	    		         
	    		         switch(statusCode){  
	    		             case 200:  
	    		             case 201: {
	    		            	 context.handlePostTurn(this.actionType, iStream);
	    		            	 /*
	    		            	 switch(this.actionType){
	    		            	 	case CANCEL_GAME:
	    		            	 		 
	    		    		 				//remove game from local storage
	    		            	 			GameService.removeGameFromLocal(context, context.game);
	    		            	 			
	    		            	 			//refresh player's game list with response from server
	    		            	 			player = GameService.handleCancelGameResponse(context, iStream);
	    		            	 			GameService.updateLastGameListCheckTime(this.context);
	    		            	 			
	    		            	 			if (player.getTotalNumLocalGames() == 0){
	    		            	 				context.handleBack(com.riotapps.word.StartGame.class);	    		            	 					
	    		            	 			}
	    		            	 			else{
	    		            	 				//send player back to main landing 
	    		            	 				context.handleBack(com.riotapps.word.MainLanding.class);
	    		            	 			}
	    		            	 			
	    		            	 		break;
	    		            	 	case DECLINE_GAME:
	    		            	 		//remove game from local storage
    		            	 			GameService.removeGameFromLocal(context, context.game);
    		            	 			
    		            	 			//refresh player's game list with response from server
    		            	 			player = GameService.handleDeclineGameResponse(context, iStream);
    		            	 			GameService.updateLastGameListCheckTime(this.context);
    		            	 			
    		            	 			if (player.getTotalNumLocalGames() == 0){
    		            	 				context.handleBack(com.riotapps.word.StartGame.class);	    		            	 					
    		            	 			}
    		            	 			else{
    		            	 				//send player back to main landing 
    		            	 				context.handleBack(com.riotapps.word.MainLanding.class);
    		            	 			}

	    		            	 		break;
	    		            	 	case RESIGN:
	    		            	 		//remove game from local storage
    		            	 			GameService.removeGameFromLocal(context, context.game);
    		            	 			
    		            	 			//refresh player's game list with response from server
    		            	 			player = GameService.handleResignGameResponse(context, iStream);
    		            	 			GameService.updateLastGameListCheckTime(this.context);
    		            	 			
    		            	 			//send player back to main landing 
    		            	 			context.handleBack(com.riotapps.word.MainLanding.class);
    		            	 
	    		            	 		break;
	    		            	 	case PLAY:
    		            	 			//update game list for active games for last played action
    		            	 			
    		            	 			//refresh player's game list with response from server
    		            	 			
	    		            	 		//refresh game board
	    		            	 		game = GameService.handleGamePlayResponse(context, iStream);
	    		            	 		
	    		            	 		
	    		            	 		Logger.d(TAG, "handleResponse game=" + gson.toJson(game));
	    		            	 		
    		            	 			//refresh game board and buttons
    		            	 			setupGame();
    		            	 			gameSurfaceView.resetGameAfterPlay();
    		            	 			
    		            	 			context.loadInterstitialAd();
    		            	 			
    		            	 			break;
	    		            	 	case SKIP:
	    		            	 		
	    		            	 		//update game list for active games for last played action
    		            	 			
    		            	 			//refresh player's game list with response from server
    		            	 			
	    		            	 		//refresh game board
	    		            	 		game = GameService.handleGamePlayResponse(context, iStream);
	    		            	
	    		            	 		Logger.d(TAG, "handleResponse SKIP game=" + gson.toJson(game));
	    		            	 		
    		            	 			//refresh game board and buttons
    		            	 			setupGame();
    		            	 			gameSurfaceView.resetGameAfterPlay();
    		            	 			
    		            	 			break;
	    		            	 	case SWAP:
	    		            	 		
	    		            	 		//update game list for active games for last played action
    		            	 			
    		            	 			//refresh player's game list with response from server
    		            	 			
	    		            	 		//refresh game board
	    		            	 		game = GameService.handleGamePlayResponse(context, iStream);
	    		            	
	    		            	 		Logger.d(TAG, "handleResponse SWAP game=" + gson.toJson(game));
	    		            	 		
    		            	 			//refresh game board and buttons
    		            	 			setupGame();
    		            	 			gameSurfaceView.resetGameAfterPlay();

	    		            	 		break;
	    		            	 	case REMATCH:
	    		            	 		Game newGame = GameService.handleCreateGameResponse(this.context, iStream);
	    		 
    		    		            	 GameService.putGameToLocal(this.context, newGame);
    		    		            	 GameService.clearLastGameListCheckTime(this.context);
    		    		            	 
    		    		            	 Intent intent = new Intent(this.context, com.riotapps.word.GameSurface.class);
    		    		            	 intent.putExtra(Constants.EXTRA_GAME_ID, newGame.getId());
    		    		             
    		    		      	      	 this.context.startActivity(intent);
    		    		                 break;  
	    		            	 }
	    		            	 */
	    		             
	    		             }//end of case 200 & 201 
	    		             break;
	    		             case 401:
	    			             //case Status code == 422
	    			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.validation_unauthorized));  
	    			            	 break;
	    		             case 404:
	    		             //case Status code == 422
	    		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.find_player_opponent_not_found), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
	    		            	 break;
	    		             case 422: 
	    		             default:

	    		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase(), 0);  
	    		            	 break;
	    		         }  
	    		     }else if (exception instanceof ConnectTimeoutException) {
	    		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_connection_timeout), 0);
	    		     }else if(exception != null){  
	    		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_not_connected), 0);  

	    		     }  
	    		     else{  
	    		         Log.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

	    		     }//end of else  
	    		}
	    		
	     
	    	}
	    
	    private class SwapDialog  implements View.OnClickListener{ 
	    	private Dialog dialog;
	    	private Button bOK;
	    	private Button bCancel;
	    	private List<String> swapped = new ArrayList<String>();
	    	private List<String> letters = new ArrayList<String>();
	    	private boolean letter_1 = false;
	    	private boolean letter_2 = false;
	    	private boolean letter_3 = false;
	    	private boolean letter_4 = false;
	    	private boolean letter_5 = false;
	    	private boolean letter_6 = false;
	    	private boolean letter_7 = false;
	    	private TextView tvLetter1;
	    	private TextView tvLetter2;
	    	private TextView tvLetter3;
	    	private TextView tvLetter4;
	    	private TextView tvLetter5;
	    	private TextView tvLetter6;
	    	private TextView tvLetter7;
	    	//private Context context;
	    	//private Boolean onCancelFinishActivity;
	    	
	     
	    	public SwapDialog(Context context, List<String> letters) {
	    	    final Context ctx = context;

	    	    this.letters = letters;
	    		this.dialog = new Dialog(ctx, R.style.DialogStyle);
	    		this.dialog.setContentView(R.layout.swapdialog);
	    		
	    		 //loop through letters, filling the views

	    		bOK = (Button) dialog.findViewById(R.id.bOK);
	    		bOK.setOnClickListener(this);
	    		bCancel = (Button) dialog.findViewById(R.id.bCancel);
	    		tvLetter1 = (TextView) dialog.findViewById(R.id.tvLetter1);
	    		tvLetter2 = (TextView) dialog.findViewById(R.id.tvLetter2);
	    		tvLetter3 = (TextView) dialog.findViewById(R.id.tvLetter3);
	    		tvLetter4 = (TextView) dialog.findViewById(R.id.tvLetter4);
	    		tvLetter5 = (TextView) dialog.findViewById(R.id.tvLetter5);
	    		tvLetter6 = (TextView) dialog.findViewById(R.id.tvLetter6);
	    		tvLetter7 = (TextView) dialog.findViewById(R.id.tvLetter7);
	   	
	    		TextView tvValue1 = (TextView) dialog.findViewById(R.id.tvValue1);
	    		TextView tvValue2 = (TextView) dialog.findViewById(R.id.tvValue2);
	    		TextView tvValue3 = (TextView) dialog.findViewById(R.id.tvValue3);
	    		TextView tvValue4 = (TextView) dialog.findViewById(R.id.tvValue4);
	    		TextView tvValue5 = (TextView) dialog.findViewById(R.id.tvValue5);
	    		TextView tvValue6 = (TextView) dialog.findViewById(R.id.tvValue6);
	    		TextView tvValue7 = (TextView) dialog.findViewById(R.id.tvValue7);

	    		tvLetter1.setText(letters.get(0));
	    		tvLetter2.setText(letters.get(1));
	    		tvLetter3.setText(letters.get(2));
	    		tvLetter4.setText(letters.get(3));
	    		tvLetter5.setText(letters.get(4));
	    		tvLetter6.setText(letters.get(5));
	    		tvLetter7.setText(letters.get(6));
	    		
	    		tvValue1.setText(Integer.toString(alphabetService.getLetterValue(letters.get(0))));
	    		tvValue2.setText(Integer.toString(alphabetService.getLetterValue(letters.get(1))));
	    		tvValue3.setText(Integer.toString(alphabetService.getLetterValue(letters.get(2))));
	    		tvValue4.setText(Integer.toString(alphabetService.getLetterValue(letters.get(3))));
	    		tvValue5.setText(Integer.toString(alphabetService.getLetterValue(letters.get(4))));
	    		tvValue6.setText(Integer.toString(alphabetService.getLetterValue(letters.get(5))));
	    		tvValue7.setText(Integer.toString(alphabetService.getLetterValue(letters.get(6))));
	    		
	    		tvLetter1.setOnClickListener(this);
	    		tvLetter2.setOnClickListener(this);
	    		tvLetter3.setOnClickListener(this);
	    		tvLetter4.setOnClickListener(this);
	    		tvLetter5.setOnClickListener(this);
	    		tvLetter6.setOnClickListener(this);
	    		tvLetter7.setOnClickListener(this);

	    		
	    		
	    		bCancel.setOnClickListener(new View.OnClickListener() {
	    			@Override
	    			public void onClick(View v) {
	    				dialog.dismiss();
	    			}
	    		});

	    		ImageView close = (ImageView) dialog.findViewById(R.id.img_close);
	    		//if button is clicked, close the custom dialog
	    		close.setOnClickListener(new View.OnClickListener() {
	    	 		@Override
	    			public void onClick(View v) {
	    				dialog.dismiss();
	    			}
	    		});
	    	}
	    	
	    	public void show(){
	    		this.dialog.show();	
	    	}
	    	
	    	public void dismiss(){
	    		this.dialog.dismiss();	
	    	}

	    	public void handleOKClick(){
	    		//if no swapped letters were picked, inform user
	    		if (!letter_1 && !letter_2 && !letter_3 && !letter_4 && !letter_5 && !letter_6 && !letter_7){
	    			DialogManager.SetupAlert(context, context.getString(R.string.oops), context.getString(R.string.gameboard_swap_dialog_please_choose_text), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);
	    		}
	    		//call handler in main class, passing the swapped letters
	    		else{
	    			if (letter_1) {this.swapped.add(this.letters.get(0));}
	    			if (letter_2) {this.swapped.add(this.letters.get(1));}
	    			if (letter_3) {this.swapped.add(this.letters.get(2));}
	    			if (letter_4) {this.swapped.add(this.letters.get(3));}
	    			if (letter_5) {this.swapped.add(this.letters.get(4));}
	    			if (letter_6) {this.swapped.add(this.letters.get(5));}
	    			if (letter_7) {this.swapped.add(this.letters.get(6));}
	    			
	    			this.dismiss();
	    			handleGameSwapOnClick(this.swapped);
	    		}
	    	}

			@Override
			public void onClick(View v) {
				switch(v.getId()){  
				case R.id.bOK:
					this.handleOKClick();
					break;
				case R.id.tvLetter1:
					if (!letter_1){
						tvLetter1.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter1.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_1 = !letter_1;
					break;
				case R.id.tvLetter2:
					if (!letter_2){
						tvLetter2.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter2.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_2 = !letter_2;
					break;
				case R.id.tvLetter3:
					if (!letter_3){
						tvLetter3.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter3.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_3 = !letter_3;
					break;
				case R.id.tvLetter4:
					if (!letter_4){
						tvLetter4.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter4.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_4 = !letter_4;
					break;
				case R.id.tvLetter5:
					if (!letter_5){
						tvLetter5.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter5.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_5 = !letter_5;
					break;
				case R.id.tvLetter6:
					if (!letter_6){
						tvLetter6.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter6.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_6 = !letter_6;
					break;
				case R.id.tvLetter7:
					if (!letter_7){
						tvLetter7.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter7.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_7 = !letter_7;
					break;
				}
			}
	    	
	    }

	    private void handlePostTurn(GameActionType action, InputStream iStream){
	    	Gson gson = new Gson();
	    	
	    	this.postTurnAction = action;
	    	
	    	switch(action){
    	 	case CANCEL_GAME:
    	 		 
 				//remove game from local storage
	 			GameService.removeGameFromLocal(context, context.game);
	 			
	 			//refresh player's game list with response from server
	 			player = GameService.handleCancelGameResponse(context, iStream);
	 			GameService.updateLastGameListCheckTime(this.context);
	 			
    	 		this.handlePostTurnOption(action);
    	 			
    	 		break;
    	 	case DECLINE_GAME:
    	 		//remove game from local storage
	 			GameService.removeGameFromLocal(context, context.game);
	 			
	 			//refresh player's game list with response from server
	 			player = GameService.handleDeclineGameResponse(context, iStream);
	 			GameService.updateLastGameListCheckTime(this.context);
	 			
    	 		this.handlePostTurnOption(action);

    	 		break;
    	 	case RESIGN:
    	 		//remove game from local storage
	 			GameService.removeGameFromLocal(context, context.game);
	 			
	 			//refresh player's game list with response from server
	 			player = GameService.handleResignGameResponse(context, iStream);
	 			GameService.updateLastGameListCheckTime(this.context);
	 			
    	 		this.handlePostTurnOption(action);
	 
    	 		break;
    	 	case PLAY:
	 			//update game list for active games for last played action
	 			
	 			//refresh player's game list with response from server
	 			
    	 		//refresh game board
    	 		game = GameService.handleGamePlayResponse(context, iStream);
    	 		
    	 		
    	 		//Logger.d(TAG, "handleResponse game=" + gson.toJson(game));
    	 		
    	 		this.handlePostTurnOption(action);
	 			
	 			break;
    	 	case SKIP:
    	 		
    	 		//update game list for active games for last played action
	 			
	 			//refresh player's game list with response from server
	 			
    	 		//refresh game board
    	 		game = GameService.handleGamePlayResponse(context, iStream);
    	
    	 		Logger.d(TAG, "handleResponse SKIP game=" + gson.toJson(game));
    	 		
    	 		this.handlePostTurnOption(action);
	 			
	 			break;
    	 	case SWAP:
    	 		
    	 		//update game list for active games for last played action
	 			
	 			//refresh player's game list with response from server
	 			
    	 		//refresh game board
    	 		game = GameService.handleGamePlayResponse(context, iStream);
    	
    	 		Logger.d(TAG, "handleResponse SWAP game=" + gson.toJson(game));
    	 		
    	 		this.handlePostTurnOption(action);

    	 		break;
    	 	case REMATCH:
    	 		Game newGame = GameService.handleCreateGameResponse(this.context, iStream);

            	 GameService.putGameToLocal(this.context, newGame);
            	 GameService.clearLastGameListCheckTime(this.context);
            	 
            	 Intent intent = new Intent(this.context, com.riotapps.word.GameSurface.class);
            	 intent.putExtra(Constants.EXTRA_GAME_ID, newGame.getId());
             
      	      	 this.context.startActivity(intent);
                 break;  
    	 }
		}
	    
	    private void handlePostTurnOption(GameActionType action){
	    	if (this.player.isNoInterstitialAdsOption() || Constants.HIDE_ALL_ADS){
	 			this.handlePostTurnFinalAction(action);   		            	 					
	 			}
	 		else{
	 			this.preInterstitialSpinner = new CustomProgressDialog(this);
	 			this.preInterstitialSpinner.setMessage(this.getString(R.string.progress_updating));
	 			this.preInterstitialSpinner.show();
	 			this.loadInterstitialAd();
 			}
	    }
	    
	    private void handlePostTurnFinalAction(GameActionType action){
	    	Gson gson = new Gson();
 	    	
	    	switch(action){
    	 	case CANCEL_GAME:
	 			if (player.getTotalNumLocalGames() == 0){
	 				context.handleBack(com.riotapps.word.StartGame.class);	    		            	 					
	 			}
	 			else{
	 				//send player back to main landing 
	 				context.handleBack(com.riotapps.word.MainLanding.class);
	 			}
    	 	
    	 		break;
    	 	case DECLINE_GAME:
	 			if (player.getTotalNumLocalGames() == 0){
	 				context.handleBack(com.riotapps.word.StartGame.class);	    		            	 					
	 			}
	 			else{
	 				//send player back to main landing 
	 				context.handleBack(com.riotapps.word.MainLanding.class);
	 			}

    	 		break;
    	 	case RESIGN:

	 			//send player back to main landing 
	 			context.handleBack(com.riotapps.word.MainLanding.class);
	 
    	 		break;
    	 	case PLAY:
	 			//refresh game board and buttons
	 			setupGame();
	 			gameSurfaceView.resetGameAfterPlay();

	 			break;
    	 	case SKIP:

	 			//refresh game board and buttons
	 			setupGame();
	 			gameSurfaceView.resetGameAfterPlay();
	 			
	 			break;
    	 	case SWAP:

	 			//refresh game board and buttons
	 			setupGame();
	 			gameSurfaceView.resetGameAfterPlay();

    	 		break;
 
    	 }
		}
 
	    
	    private void loadInterstitialAd(){
		    // Create the interstitial
	        interstitial = new  com.google.ads.InterstitialAd(this, this.getString(R.string.admob_pub_id_interstitial));
	
	        // Create ad request
	        AdRequest adRequest = new AdRequest();
	
	        // Begin loading your interstitial
	        interstitial.loadAd(adRequest);
	
	        // Set Ad Listener to use the callbacks below
	        interstitial.setAdListener(this);
	    }
	    
		@Override
		public void onDismissScreen(Ad ad) {
			// TODO Auto-generated method stub
			Logger.d(TAG, "interstitial onDismissScreen called");
			this.preInterstitialSpinner.dismiss();
			this.handlePostTurnFinalAction(this.postTurnAction);
		}

		@Override
		public void onFailedToReceiveAd(Ad ad, ErrorCode arg1) {
			Logger.d(TAG, "interstitial onFailedToReceiveAd called");
			this.preInterstitialSpinner.dismiss();
			this.handlePostTurnFinalAction(this.postTurnAction);
		}

		@Override
		public void onLeaveApplication(Ad ad) {
			Logger.d(TAG, "interstitial onLeaveApplication called");
			 //not sure what this needs here
			
		}

		@Override
		public void onPresentScreen(Ad ad) {
			Logger.d(TAG, "interstitial onPresentScreen called");
		}

		@Override
		public void onReceiveAd(Ad ad) {
			Logger.d(TAG, "interstitial onReceiveAd called");
		  if (ad == interstitial) {
		    interstitial.show();
		  }
		}

}
