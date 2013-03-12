package com.riotapps.word;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.conn.ConnectTimeoutException;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.gson.Gson;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.fullscreen.RevMobFullscreen;
import com.riotapps.word.hooks.AlphabetService;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.CustomButtonDialog;

import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.ui.GameAction;
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
import com.riotapps.word.utils.NetworkTaskResult;
 
import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest.ErrorCode;
import java.util.Timer;
import java.util.TimerTask;

//Import the Chartboost SDK
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;

//import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class GameSurface extends FragmentActivity implements View.OnClickListener, AdListener{
	private static final String TAG = GameSurface.class.getSimpleName();
	
	private Chartboost cb;
	private GameSurface context = this;
	private GameSurfaceView gameSurfaceView;
	private ImageFetcher imageLoader;
	 private RelativeLayout scoreboard;
	 private SurfaceView surfaceView;
	 private  NetworkTask runningTask = null;
	 private Button bRecall;
	 private Button bPlay;
	 private Button bSkip;
	 private Button bShuffle;
	 private boolean isButtonActive = false;
	 private boolean isNetworkTaskActive = false;
	 private boolean isRestartFromInterstitialAd = false;
	 private boolean isAdStarted = false; 
	 private boolean isGameReloaded = false;
	 private boolean hideInterstitialAd = false;
	 private boolean isChartBoostActive = false;
	 private boolean isRevMobActive = false;
	 private boolean isAdMobActive = false;
	 private RevMob revmob;
	 private RevMobAdsListener revmobListener;
	 private RevMobFullscreen revMobFullScreen;
	 private boolean hasFinalPostTurnRun = false;
	 private boolean isBoundToGCMService = false;
	 
	 private Timer timer = null;
	 private Timer runawayAdTimer = null;
	// CustomProgressDialog spinner = null;
	
	 private com.google.ads.InterstitialAd interstitial;
	 private GameActionType postTurnAction;
	 private CustomProgressDialog spinner;
	 
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
	
	public long runningTime = System.nanoTime();
	public long captureTime = System.nanoTime();

 
	private Player player;
	private TextView tvNumPoints;
	public PlayerGame contextPlayerGame;
	private BroadcastReceiver gcmReceiver;
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
	
    
    public void unfreezeButtons(){
    	this.isButtonActive = false;
    }

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamesurface);
 
	    this.player = PlayerService.getPlayerFromLocal(); 
		this.tvNumPoints = (TextView)findViewById(R.id.tvNumPoints);
 
		 Display display = getWindowManager().getDefaultDisplay(); 
	     this.windowHeight = display.getHeight();  // deprecated
	     
	     this.captureTime("onCreate starting");
	     
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
	 	
	 	this.captureTime("get game from local starting");
	 	this.game = GameService.getGameFromLocal(gameId); //(Game) i.getParcelableExtra(Constants.EXTRA_GAME);
		Logger.d(TAG, "onCreate game turn=" + game.getTurn());
	 	this.captureTime("get game from local ended");	 	
	// 	spinner = new CustomProgressDialog(this);
    //    spinner.setMessage(this.getString(R.string.progress_loading));
    //    spinner.show();

  		//Gson gson = new Gson();  
	    //Logger.d(TAG, "game json=" + gson.toJson(game));
	 	
	 	//temp
	 	//this.game = getTempGame();

		this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
		
	 	this.captureTime("alphabet service starting");
		this.alphabetService = new AlphabetService(context);
		//this.wordService = new WordService(context);
	 	this.captureTime("alphabet service started");
		
		ApplicationContext appContext = (ApplicationContext)this.getApplicationContext();
		//appContext.getWordService()
		
	 	this.captureTime("gamesurfaceview starting");
		this.gameSurfaceView.construct(this, this.alphabetService, appContext.getWordService(), this, appContext);
		//this.gameSurfaceView.setParent(this);
	 	this.captureTime("gamesurfaceview started");		
	
		// Logger.d(TAG, "SetDerivedValues  this.getWindowHeight=" +  this.getWindowHeight() );
		// Logger.d(TAG, "SetDerivedValues  this.gameSurfaceView.height=" +  gameSurfaceView.getHeight() );
	 	
		//Logger.d(TAG, "scoreboard about to be loaded");
	 	//this.loadScoreboard();
	 	
	 	this.captureTime("setup game starting");
		this.setupGame();
	 	this.captureTime("setup game ended");
		
	 	this.captureTime("checkGameStatus starting");
		this.checkGameStatus();
	 	this.captureTime("checkGameStatus ended");
	 	this.checkFirstTimeStatus();
	 	//this.setupTimer();
	 	this.setupAdServer();
	 	this.setupGCMReceiver();
	 }
	
	private void setupGCMReceiver(){
		if (this.gcmReceiver == null){
			this.gcmReceiver = new BroadcastReceiver() {
			    @Override
			    public void onReceive(Context context, Intent intent) {
			    	 
					String messageGameId = intent.getStringExtra(Constants.EXTRA_GAME_ID);
			    	
					//refresh the game if its not the user's turn and a message is received from gcm.
					//determine logic difference between incoming comment message and played turn message
					if (messageGameId.equals(GameSurface.this.game.getId()) && !GameSurface.this.game.isContextPlayerTurn(GameSurface.this.player)){
						((Activity) context).runOnUiThread(new handleGameRefresh());
					}
			    	// Toast.makeText(GameSurface.this, (messageGameId.equals(GameSurface.this.game.getId()) ? "true" : "false"),
				     //           Toast.LENGTH_LONG).show();
			    }
			};
			this.registerReceiver(this.gcmReceiver, new IntentFilter(Constants.INTENT_GCM_MESSAGE_RECEIVED));
		}
	}

	private void setupAdServer(){
		Logger.d(TAG, "setupAdServer called");
		
	 	boolean isAdMob = Constants.INTERSTITIAL_ADMOB;
	 	boolean isChartBoost = Constants.INTERSTITIAL_CHARTBOOST;
		boolean isRevMob = Constants.INTERSTITIAL_REVMOB;
		final int useRevMob = 0;
		
	 	if (!Constants.HIDE_ALL_ADS)
	 	{
	 		//assign either chartboost or revmob randomly
	 		if (isChartBoost && isRevMob){
	 			
	 			int coinFlip = (int)(Math.random() * 2);
 
	 			if (coinFlip == useRevMob ){
		 			isChartBoost = false;
				}
				else{
		 			isRevMob = false;
				}
			}

	 		if (isRevMob){
	 			Logger.d(TAG, "setupAdServer isRevMob=true");
	 			
	 			
		 		this.isRevMobActive = true;
		 		this.revmob = RevMob.start(this, this.getString(R.string.rev_mob_app_id));
	 		}
	 		else if (isChartBoost){
	 			Logger.d(TAG, "setupAdServer isCharBoost=true");
	 			
		 		this.isChartBoostActive = true;
		 		this.setupChartBoost();
	 		}
	 		else if (isAdMob){
	 			this.isAdMobActive = true;
	 		}
	 	}
	 	else{
	 		this.hideInterstitialAd = true;
	 	}
	 		 		
	 	
	 	Logger.d(TAG, "revMob=" + this.isRevMobActive + " chartBoost=" + this.isChartBoostActive);
	}
	
	private void setupChartBoost(){
		this.cb = Chartboost.sharedChartboost();
		this.cb.onCreate(this, this.getString(R.string.chartboost_app_id), this.getString(R.string.chartboost_app_signature), this.chartBoostDelegate);
		this.cb.setImpressionsUseActivities(true);
		this.cb.startSession();
		this.cb.cacheInterstitial();

	}
	
	public void captureTime(String text){
	     this.captureTime = System.nanoTime();
	     Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", text, Utils.convertNanosecondsToMilliseconds(this.captureTime - this.runningTime)));
	     this.runningTime = this.captureTime;

	}
	/*
	private void setupTimer(){
		this.setupTimer(Constants.GAME_SURFACE_CHECK_START_IN_MILLISECONDS);
	}
	
    private void setupTimer(long delay){
    	Logger.d(TAG, "setupTimer called");
    	//expand this for chat updates at some point
    	if (this.game.getStatus() == 1 && !this.game.isContextPlayerTurn(this.player)){
    		if (this.timer == null){
    		 
				Logger.d(TAG, "setupTimer starting");
				this.captureTime("setupTimer starting");
		    	timer = new Timer();  
		    	updateGameTask updateGame = new updateGameTask();
		    	timer.scheduleAtFixedRate(updateGame, delay, Constants.GAME_SURFACE_CHECK_INTERVAL_IN_MILLISECONDS);
		    	this.captureTime("checkGameStatus ended");
    		}
    	}
    	else {
    		stopTimer();
    	}
    }
    
    private void stopTimer(){
    	Logger.d(TAG, "stopTimer called");
    	if (this.timer != null) {
    		this.timer.cancel();
    		this.timer = null;
    	}
    
    }
    
    private class updateGameTask extends TimerTask {
    	   public void run() {
    		   if (!game.isContextPlayerTurn(player)){
    			   ((Activity) context).runOnUiThread(new handleGameRefresh());
    		   }
    	   }
    }
	*/
	private void setupGame(){
		 Logger.d(TAG,"setupGame game turn=" + this.game.getTurn());
		this.contextPlayerGame = GameService.loadScoreboard(this, this.game, this.player);
	 	
		//if (!this.game.isCompleted()){
			this.fillGameState();
		//}
	 	
	 	this.setupButtons();

	}
	
	public void onInitialRenderComplete(){
	//	this.spinner.dismiss();
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
		    	if (game.isCompleted()){
		    		bRecall.setVisibility(View.GONE);
				 	bShuffle.setVisibility(View.GONE);
		    		bPlay.setVisibility(View.GONE);
				 	bSkip.setVisibility(View.GONE);
					bRecall.setVisibility(View.GONE);
		    	}
		    	else{
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
	  }
	 
	 public void setPointsView(int points){
		 this.currentPoints = points;
		 context.runOnUiThread(new handlePointsViewRunnable(points, 1));
	 }
	 
	 public void setPointsAfterPlayView(){
		 if (this.game.getStatus() == 1) {
			 context.runOnUiThread(new handlePointsViewRunnable(this.currentPoints, 2));
		 }
		 else {
			 //hide points view since the game is over
			 context.runOnUiThread(new handlePointsViewRunnable(0, 2));
		 }
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
			 
				 DialogManager.SetupAlert(this, this.getString(R.string.game_alert_game_over_title), message);
			 }
		 }
	 }
	 
	 private void checkFirstTimeStatus(){
		 //completed
		 //first check to see if this score has already been alerted (from local storage) 
		 
		 if (!PlayerService.checkFirstTimeGameSurfaceAlertAlreadyShown(this)) {
			 DialogManager.SetupAlert(this, this.getString(R.string.game_surface_first_time_alert_title), String.format(this.getString(R.string.game_surface_first_time_alert_message), this.player.getFirstNameOrNickname()));
		 }
		 
	 }
	 
	 private void setupButtons(){
		this.isButtonActive = false;
		buttonsLoaded = true;
		//Logger.d(TAG,  "setupButtons called");
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
	 	
	 	//bRecall.setVisibility(View.GONE);
	 	bShuffle.setVisibility(View.VISIBLE);
	 	
	 	bPlay.setVisibility(View.GONE);
	 	bSkip.setVisibility(View.VISIBLE);
	 	
	 	bPlay.setClickable(this.game.isContextPlayerTurn(this.player));
	 	
	 	Logger.d(TAG, "getNumLettersLeft=" + this.game.getNumLettersLeft());
	 	
	  	if (this.game.getNumLettersLeft() > 0){
	 		bSwap.setOnClickListener(this);
	 		bSwap.setClickable(this.game.isContextPlayerTurn(this.player));
 		//	Logger.d(TAG, "bSwap clickable");
	  	}
 		else{
 			//Logger.d(TAG, "bSwap NOT clickable");
 			bSwap.setClickable(false);
 			bSwap.setTextColor(Color.parseColor(this.getString(R.color.button_text_color_off)));
 		}
	  	
	 	
	 	bSkip.setClickable(this.game.isContextPlayerTurn(this.player));
	 	bResign.setClickable(this.game.isContextPlayerTurn(this.player));

	 	
	 	//by default recall button will be hidden, it will be switched with shuffle button when a letter is dropped on the board
	 	this.bRecall.setVisibility(View.GONE); 
	 	
	 	
	 //	Logger.d(TAG, "setupButtons this.game.getNumActiveOpponents()=" + this.game.getNumActiveOpponents());
	 //	Logger.d(TAG, "setupButtons this.game.getTurn()=" + this.game.getTurn());
	 //	Logger.d(TAG, "setupButtons this.game.isContextPlayerStarter()=" + this.game.isContextPlayerStarter(this.player));
	 //	Logger.d(TAG, "setupButtons this.game.getStatus()=" + this.game.getStatus());
	 	
	 	
	 	bChat.setCompoundDrawables(null, null, null, null);
	 	//set cancel button area mode:
	 	//if it's the first play of the game by starting player, it should be "CANCEL" mode
	 	//if it's the first play of the game by a non-starting player, it should be in "DECLINE" mode
	 	//if it's not the first play of the game, it should be in "RESIGN" mode
	 	
	 	Logger.d(TAG, "setupButtons game status=" + this.game.getStatus());
	 	
	 	if (this.game.getStatus() == 1) { //active
	 		if (GameService.checkGameChatAlert(context, this.game, true)){
	 			Drawable chatAlert = context.getResources().getDrawable( R.drawable.chat_alert );
	 			bChat.setCompoundDrawablesWithIntrinsicBounds(null, null, chatAlert, null);
	 			
	 			Logger.d(TAG, "setupButtons checkGameChatAlert=true");
	 		}
	 		else if (this.game.getChats().size() > 0){
	 			Drawable chatAlert = context.getResources().getDrawable( R.drawable.chat_exists );
	 			bChat.setCompoundDrawablesWithIntrinsicBounds(null, null, chatAlert, null);
	 			
	 			//Logger.d(TAG, "setupButtons checkGameChatAlert=true");
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
	 	else if(this.game.isCompleted()){  
	 		if (this.game.getChats().size() > 0){
	 			Drawable chatAlert = context.getResources().getDrawable( R.drawable.chat_exists );
	 			bChat.setCompoundDrawablesWithIntrinsicBounds(null, null, chatAlert, null);
	 		}
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
	 
	 	
	 	Logger.d(TAG, "setupButtons bRecall visible=" + bRecall.getVisibility() + " bShuffle=" + bShuffle.getVisibility());
	}
	    
	    
	    
	 private void fillGameState(){
		 this.gameState = GameStateService.getGameState(context, this.game.getId());  
		 
		 //if this.turn != gameState.turn, clearGameState
		 
		 //if the game state is dated, clear it out
		 if (this.game.getContextPlayerTrayVersion(this.player) != this.gameState.getTrayVersion()){
			 this.gameState = GameStateService.clearGameState(this.game.getId());
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
		Logger.d(TAG, "onDestroy called");
		
		this.gameSurfaceView.onDestroy();
		
	//	if (this.wordLoaderThread != null){
	//		this.wordLoaderThread.interrupt();
	//		this.wordLoaderThread = null;
	//	}
		
		
		if (this.isChartBoostActive){
			
			this.cb.onDestroy(this);
			this.cb = null;
		}
		
		super.onDestroy();
		//doUnbindService();
	 
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onStop called");
		if (this.getGameState() != null){
			GameStateService.setGameState(this.getGameState());
		}
		
	///	this.stopTimer();
		this.stopRunawayAdTimer();
		this.gameSurfaceView.onStop();
	//	if (this.wordLoaderThread != null){
	//		this.wordLoaderThread.interrupt();
	//		this.wordLoaderThread = null;
	//	}
		
	//	try{
	//		this.spinner.dismiss();
	//	}
	//	catch(Exception e){}
		super.onStop();
		
		if (this.isChartBoostActive){
			
			if (this.cb.hasCachedInterstitial()){ this.cb.clearCache(); }
			this.cb.onStop(this);
		 
			//this.cb = n//ull;
		}
		
		if (this.isRevMobActive){
			this.revMobFullScreen = null;
			this.revmobListener = null;
			this.revmob = null;		
		}
		
		if (this.gcmReceiver != null) {
			this.unregisterReceiver(this.gcmReceiver);
			this.gcmReceiver = null;
		}
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onPause called");
		super.onPause();
		if (this.runningTask != null){
    		this.runningTask.cancel(true);
    		this.runningTask.dismiss();
    	}
		Logger.d(TAG, "onPause - stop timer about to be called");
		if (this.getGameState() != null){
			GameStateService.setGameState(this.getGameState());
		}
		
	///	this.stopTimer();
		this.stopRunawayAdTimer();

	//	try{
	//		this.spinner.dismiss();
	//	}
	//	catch(Exception e){}
		this.gameSurfaceView.onPause();
		
		if (this.spinner != null){
			this.spinner.dismiss();
		}
		
		
	//	if (this.wordLoaderThread != null){
	//		this.wordLoaderThread.interrupt();
	//		this.wordLoaderThread = null;
	//	}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onResume called");
	//	this.captureTime("onResume starting");
		super.onResume();
	//	this.captureTime("unfreezeButtons starting");

		GameActionType lastAction = this.postTurnAction;
		if (lastAction == null){
			lastAction = GameActionType.NO_TRANSLATION;
		}
		
		switch (lastAction){
			case CANCEL_GAME:
			case DECLINE_GAME:
			case RESIGN:
				//do nothing for these options
				break;
			default:
				this.unfreezeButtons();
				//	this.captureTime("gameSurfaceview resume starting");
				this.gameSurfaceView.onResume();
				//	this.captureTime("setup timer_ starting");
			
				Logger.d(TAG,"onResume - setup timer about to be called");
				//this.setupTimer();
				if (this.runningTask != null){
					this.runningTask.dismiss();
				}

		}
			 

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
 
		if (this.isChartBoostActive && this.cb.onBackPressed())
			// If a Chartboost view exists, close it and return
			return;
		else{
			this.handleBack(com.riotapps.word.MainLanding.class);
		}
	}
	
	private void handleBack(Class<?> cls){
		this.spinner = new CustomProgressDialog(this);
		this.spinner.setMessage(this.getString(R.string.progress_saving));
		this.spinner.show();
			
		if (this.runningTask != null){
    		this.runningTask.cancel(true);
    	}
		
		if (this.timer != null){
			this.timer.cancel();
			this.timer = null;
		}
		
		if (this.getGameState() != null){
			GameStateService.setGameState(this.getGameState());
		}
		
		this.gameSurfaceView.onStop();
		this.game = null;
		this.player = null;
		this.gameState = null;
		
		Intent intent = new Intent(this.context, cls );
	    this.context.startActivity(intent);
	    this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		
		Logger.d(TAG, "onActivityResult called requestCode=" + requestCode + "  resultCode=" + resultCode);
		 switch(requestCode) { 
		    case (1) : { 
		      if (resultCode == Activity.RESULT_OK) { 
		    	  boolean hasGameBeenUpdated = intent.getBooleanExtra(Constants.EXTRA_IS_GAME_UPDATED, false);
		    	  if (hasGameBeenUpdated){
			    	  this.game = GameService.getGameFromLocal(game.getId());
			    	  this.isGameReloaded = true;
			    	  this.setupGame();
					  this.checkGameStatus();
					  this.gameSurfaceView.resetGameAfterRefresh();
		    	  }
		      } 
		      break; 
		    } 
		  } 
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		Logger.d(TAG, "onStart isChartBoostActive=" + isChartBoostActive + " isRevMobActive=" + isRevMobActive);
		//Toast.makeText(this, "onstart cb=" + isChartBoostActive + " rm="  + isRevMobActive, Toast.LENGTH_SHORT).show();
		
		if (this.isChartBoostActive) {
			if (this.cb == null) {
				this.setupChartBoost();	
			}
			this.cb.onStart(this);
		}
		
		if (this.isRevMobActive){
			this.revmobListener = new RevMobAdsListener() {
	            @Override
	            public void onRevMobAdReceived() {
	                Logger.d(TAG, "onRevMobAdReceived");
	              //  Toast.makeText(context, "onRevMobAdReceived", 1000);
	              //  handlePostTurnFinalAction(postTurnAction);
	            }
	 
	            @Override
	            public void onRevMobAdNotReceived(String message) {
	            	 Logger.d(TAG, "onRevMobAdNotReceived");
	            	// handlePostTurnFinalAction(postTurnAction);
	            }
	 
	            @Override
	            public void onRevMobAdDismiss() {
	            	 Logger.d(TAG, "onRevMobAdDismiss");
                     //Toast.makeText(GameSurface.this, "onRevMobAdDismiss.", Toast.LENGTH_SHORT).show();

	            	 handlePostTurnFinalAction(postTurnAction);
	            }
	 
	            @Override
	            public void onRevMobAdClicked() {
	            	GameSurface.this.runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	Logger.d(TAG, "onRevMobAdClicked");
	                    	handlePostTurnFinalAction(postTurnAction);
	                       // Toast.makeText(GameSurface.this, "Click intercepted.", Toast.LENGTH_SHORT).show();
	                    }
	                });
	            }
	
				@Override
				public void onRevMobAdDisplayed() {
					 Logger.d(TAG, "onRevMobAdDisplayed");
					 handlePostTurnFinalAction(postTurnAction);
					
				}
	        };
	        if (this.revmob == null){
	        	this.revmob = RevMob.start(this, this.getString(R.string.rev_mob_app_id));	
	        }
	        this.revMobFullScreen = revmob.createFullscreen(this, this.getString(R.string.rev_mob_main_between_play), this.revmobListener);
		}
		//this.doBindService();
		
	}
	
	@Override
	protected void onRestart() {
		//Log.w(TAG, "onRestart called");
		super.onRestart();
		this.gameSurfaceView.onRestart(); 
		
		this.setupGCMReceiver();
		Logger.d(TAG, "onRestart buttonsLoaded=" + buttonsLoaded);  
		
		if (this.isRestartFromInterstitialAd){
			Logger.d(TAG, "onRestart from InterstitialAd");  

			this.handlePostTurnFinalAction(this.postTurnAction);
			this.isRestartFromInterstitialAd = false;
		}
		else {
			//Intent i = getIntent();
			
			//boolean hasGameBeenUpdated = i.getBooleanExtra(Constants.EXTRA_IS_GAME_UPDATED, false);
			/*
			int previousTurn = this.game.getTurn();
			Game previousGame = GameService.getGameFromLocal(game.getId());
			
			if (previousTurn < previousGame.getTurn()){
				//if its not the context user's turn and he goes to chat activity and leaves a chat text after the opponent
				//takes his turn, but before coming back to game surface, this logic handles that
				this.setupGame();
				this.checkGameStatus();
				this.gameSurfaceView.resetGameAfterRefresh();
			}
			else 
			*/
			if (buttonsLoaded){ 
				//reset buttons
				Logger.d(TAG, "onRestart game being fetched from local");
				if (!this.isGameReloaded) {this.game = GameService.getGameFromLocal(game.getId());}
				Logger.d(TAG, "onRestart game turn=" + game.getTurn());
				this.fillGameState();
				this.setupButtons();
				this.gameSurfaceView.setInitialButtonStates();
				this.isButtonActive = false;
			}
			///this.setupTimer(Constants.GAME_SURFACE_CHECK_START_AFTER_RESTART_IN_MILLISECONDS);
		}
	}

	
	 @Override 
	    public void onClick(View v) {
	    	Intent intent;
	    	if (this.isButtonActive == true){
	    		//skip processing to stop dialogs from doubling up
	    	}
	    	else {	
	    		this.isButtonActive = true;
		    	    	
		    	switch(v.getId()){  
			        case R.id.bShuffle:  
			        	this.gameSurfaceView.shuffleTray();
			        	this.unfreezeButtons();
			        	break;
			        case R.id.bChat:  
			        	intent = new Intent(this, GameChat.class);
			        	intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
						//startActivity(intent);
						startActivityForResult(intent, 1);
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
			        	this.unfreezeButtons();
						break;
			        case R.id.bPlay:
			        	//this.loadPlaySpinner();
			        	v.post(new Runnable() {
		                    public void run() {
		                        // Your job here
		                     gameSurfaceView.onPlayClick();
		                    }
		                });
			        	//this.gameSurfaceView.onPlayClick();
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
	 }
	
	 private void loadPlaySpinner(){
		// Toast.makeText(this, "checking spinner loading", Toast.LENGTH_LONG).show();
			this.spinner = new CustomProgressDialog(this);
			this.spinner.setMessage(this.getString(R.string.progress_checking));
			this.spinner.show();
	 }
	 
	 public void unloadPlaySpinner(){
		 if (spinner != null){
				spinner.dismiss();
			}
	 }
	 
	 public void onFinishPlayNoErrors(final PlacedResult placedResult) {
		    runOnUiThread(new Runnable() {
		        public void run() {
		            // use data here
		        //	unloadPlaySpinner();
		            gameSurfaceView.postPlayNoErrors(placedResult);
		        }
		    });
		}
	 
	 public void onFinishPlayErrors(final String message) {
		    runOnUiThread(new Runnable() {
		        public void run() {
		            // use data here
		        	//unloadPlaySpinner();
		        	openAlertDialog(context.getString(R.string.sorry), message);
					unfreezeButtons();
		        }
		    });
		}
	  /*  private class handleFinishPlay implements Runnable {
		    public void run() {
		    	try { 
		    		if (spinner != null){
	    				spinner.dismiss();
	    			}
	    			//handlePostTurnFinalAction(postTurnAction);
				} catch (Exception e) {
					 Logger.d(TAG, "handleFinishPlay error=" + e.toString());
				}
		    }
	   }*/
	 
	    private void handleCancel(){
	    	final CustomButtonDialog dialog = new CustomButtonDialog(this, 
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

	    	dialog.setOnDismissListener(new View.OnClickListener() {
			 		@Override
					public void onClick(View v) {
			 			dialog.dismiss(); 
			 			unfreezeButtons();
			 		}
				});
	    	
	     	dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					unfreezeButtons();
					
				}
			});  
	    	dialog.show();	
	    }
	    
	    private void handleDecline(){
	    	final CustomButtonDialog dialog = new CustomButtonDialog(this, 
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
	    	
	    	dialog.setOnDismissListener(new View.OnClickListener() {
		 		@Override
				public void onClick(View v) {
		 			dialog.dismiss(); 
		 			unfreezeButtons();
		 		}
			});
 
	    	dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					unfreezeButtons();
					
				}
			});
			 
	    	dialog.show();	
	    }

	    private void handleResign(){
	    	final CustomButtonDialog dialog = new CustomButtonDialog(this, 
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
	    	
	    	dialog.setOnDismissListener(new View.OnClickListener() {
			 		@Override
					public void onClick(View v) {
			 			dialog.dismiss(); 
			 			unfreezeButtons();
			 		}
				});
 
	    	dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					unfreezeButtons();
					
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
	    	
	    	final CustomButtonDialog dialog = new CustomButtonDialog(this, 
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
	    	
	    	dialog.setOnDismissListener(new View.OnClickListener() {
			 		@Override
					public void onClick(View v) {
			 			dialog.dismiss(); 
			 			unfreezeButtons();
			 		}
				});
	    	 
	    	dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					unfreezeButtons();
					
				}
			});
			 

	    	dialog.show();	
	    }
	    
	    
	    private void handleGameCancelOnClick(){
	    	//stop thread first
	    	this.gameSurfaceView.onStop();
	    	try { 
				String json = GameService.setupCancelGame(this.game.getId());
				
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_cancelling), GameActionType.CANCEL_GAME);
				runningTask.execute(Constants.REST_GAME_CANCEL);
			} catch (DesignByContractException e) {
				 
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
			}
	    }
	    private class handleGameRefresh implements Runnable {
		    public void run() {
		    	 try { 
		    		 if (isNetworkTaskActive == false && game != null){
			    		 Logger.d(TAG, "handleGameRefresh");
			    		 String json = GameService.setupGameCheck(game.getId(), game.getTurn());
			
						runningTask = new NetworkTask(context, RequestType.POST, json, getString(R.string.progress_syncing), GameActionType.REFRESH);
						runningTask.execute(Constants.REST_GAME_REFRESH_URL);
		    		 }
				} catch (DesignByContractException e) {
					 Logger.d(TAG, "handleGameRefresh error=" + e.toString());
					//DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
				}
		    }
	   }
	    
	    private void handleGameDeclineOnClick(){
	    	//stop thread first
	    	this.gameSurfaceView.onStop();
	    	try { 
				String json = GameService.setupDeclineGame(this.game.getId());
				
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
				String json = GameService.setupResignGame(this.game.getId());
				
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

				String json = GameService.setupStartGame(newGame);
				
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
				String json = GameService.setupGameTurn(this.game, placedResult);
				
				Logger.d(TAG, "handleGamePlayOnClick json=" + json);
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_sending), GameActionType.PLAY);
				runningTask.execute(Constants.REST_GAME_PLAY);

			} catch (DesignByContractException e) {
				 
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());
				this.unfreezeButtons();
			}
			 
	    }
	    
	    public void handleGameSkipOnClick(){
	    	//stop thread first
	    	
	    	//DialogManager.SetupAlert(context, "played", "clicked");
 	    	this.gameSurfaceView.stopThreadLoop();
	    	try { 
				String json = GameService.setupGameSkip(this.game);
				
				Logger.d(TAG, "handleGameSkipOnClick json=" + json);
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_sending), GameActionType.SKIP);
				runningTask.execute(Constants.REST_GAME_SKIP);

			} catch (DesignByContractException e) {
				 
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
				this.unfreezeButtons();
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
				String json = GameService.setupGameSwap(this.game, swappedLetters);
				
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
	    			super(GameSurface.this, requestType, shownOnProgressDialog, json);
	    			this.context = ctx;
	    			this.actionType = actionType;
	    			isNetworkTaskActive = true;
	    		 
	    		}

	    		@Override
	    		protected void onPostExecute(NetworkTaskResult result) {
	    		 
	    			super.onPostExecute(result);
	    			
	    			this.handleResponse(result);
	    			isNetworkTaskActive = false;

	    		}
	     
	    		private void handleResponse(NetworkTaskResult result){
	    		    
	    		     Exception exception = result.getException();
	    		     
	    		     Logger.d(TAG, "handleResponse status=" + result.getStatusCode() + " result=" + result.getResult());
	    		    		 
	    		     if(result.getResult() != null){  

	    		         //Gson gson = new Gson();

	    		         //Player player = null;
	    		         
	    		         switch(result.getStatusCode()){  
	    		             case 200:  
	    		             case 201: 
	    		            	 if (this.actionType == GameAction.GameActionType.REFRESH){
	    		            		 game = GameService.handleCreateGameResponse(result.getResult());
	    		     			    
	    			            	 GameService.putGameToLocal(game);
	    			            	 gameState = GameStateService.clearGameState(game.getId());
	    			            	 setupGame();
	    			 	 			 checkGameStatus();
	    			 	 			 gameSurfaceView.resetGameAfterRefresh();
	    			 	 			 
	    			 	 			 if (context.isRevMobActive){
	    			 	 				GameSurface.this.revMobFullScreen = GameSurface.this.revmob.createFullscreen(GameSurface.this, GameSurface.this.getString(R.string.rev_mob_main_between_play), GameSurface.this.revmobListener);
	    			 	 			 }
	    			 	 			 ///setupTimer();
	    		            	 }
	    		            	 else {
	    		            		 context.handlePostTurn(this.actionType, result.getResult());
	    		            	 }
	    		            	 break;
	    		             case 202:
	    		            	 if (this.actionType == GameAction.GameActionType.REFRESH){
	    		            		 //take no action, nothing to do here
	    		            	 }
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

	    		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), result.getStatusCode() + " " + result.getStatusReason(), 0);  
	    		            	 break;
	    		         }  
	    		     }else if (exception instanceof ConnectTimeoutException ||  exception instanceof java.net.SocketTimeoutException) {
	    		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_connection_timeout), 0);
	    		     }else if(exception != null){  
	    		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_not_connected), 0);  

	    		     }  
	    		     else{  
	    		         Log.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

	    		     }//end of else  
	    		}
	    	}
	    
	    private class SwapDialog extends AlertDialog implements View.OnClickListener{ 
	    //	private Dialog dialog;
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
	    	private RelativeLayout rlLetter1;
	    	private RelativeLayout rlLetter2;
	    	private RelativeLayout rlLetter3;
	    	private RelativeLayout rlLetter4;
	    	private RelativeLayout rlLetter5;
	    	private RelativeLayout rlLetter6;
	    	private RelativeLayout rlLetter7;
	    	
	    	private View layout;
	    	Context context;
	    	int fullWidth;
	    	int tileSize;
	    	int textSize;
	    	//private Context context;
	    	//private Boolean onCancelFinishActivity;
	    	
	     
	    	@SuppressLint({ "NewApi", "NewApi" })
			public SwapDialog(Context context, List<String> letters) {
	    		super(context);
	    	    this.context = context;
	    	    Display display = getWindowManager().getDefaultDisplay();
	    	    Point size = new Point();
	    		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
	    			 display.getSize(size);
	    			 this.fullWidth = size.x;
	    		 }
	    		 else {
	    			 this.fullWidth = display.getWidth();  // deprecated
	    		}
	    	    	
	    	    int tileWidth = Math.round(this.fullWidth / 7.1f);
	    	    if (tileWidth > 70){
	    	    	this.tileSize = 70;
	    	    }
	    	    else {
	    	    	this.tileSize = tileWidth;
	    	    }
	    	    	
	    	    textSize = Math.round(this.tileSize * .75f);
	    	    
	    	    this.letters = letters;
	    	//	this.dialog = new Dialog(ctx, R.style.DialogStyle);
	    	//	this.dialog.setContentView(R.layout.swapdialog);
	    	
	    		
	    	 
	    	}
	    	
	    	protected void onCreate(Bundle savedInstanceState) {
	    		// TODO Auto-generated method stub
	    		super.onCreate(savedInstanceState);
//	    		this.setContentView(BUTTON1);
	          //  this.setProgressStyle(R.style.CustomProgressStyle);
	    		
	    	}

	    	@Override
	    	public void onStart() {
	    		// TODO Auto-generated method stub
	    		super.onStart();
	 
	    		LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
	            this.layout = inflater.inflate(R.layout.swapdialog, 
	                                            (ViewGroup) findViewById(R.id.progress_root));
	    		
	    		 //loop through letters, filling the views

	    		bOK = (Button) this.layout.findViewById(R.id.bOK);
	    		bOK.setOnClickListener(this);
	    		bCancel = (Button) this.layout.findViewById(R.id.bCancel);
	    		
	    		/*
	    		//RelativeLayout.LayoutParams tile1 = new RelativeLayout.LayoutParams(this.tileSize, this.tileSize);
	    		//rlLetter1 = (RelativeLayout) this.layout.findViewById(R.id.rlLetter1);
	    		//rlLetter1.setLayoutParams(tile1);
	 
	    		//RelativeLayout.LayoutParams tile2 = new RelativeLayout.LayoutParams(this.tileSize, this.tileSize);
	    		//tile2.addRule(RelativeLayout.RIGHT_OF, rlLetter1.getId());
	    		//tile2.setMargins(2, 0, 0, 0);
	    		//rlLetter2 = (RelativeLayout) this.layout.findViewById(R.id.rlLetter2);
	    		//rlLetter2.setLayoutParams(tile2);
	    		
	    		//RelativeLayout.LayoutParams tile3 = new RelativeLayout.LayoutParams(this.tileSize, this.tileSize);
	    		//tile3.addRule(RelativeLayout.RIGHT_OF, rlLetter2.getId());
	    		//tile3.setMargins(2, 0, 0, 0);
	    		//rlLetter3 = (RelativeLayout) this.layout.findViewById(R.id.rlLetter3);
	    		//rlLetter3.setLayoutParams(tile3);
	    		
	    		//RelativeLayout.LayoutParams tile4 = new RelativeLayout.LayoutParams(this.tileSize, this.tileSize);
	    		//tile4.addRule(RelativeLayout.RIGHT_OF, rlLetter3.getId());
	    		//tile4.setMargins(2, 0, 0, 0);
	    		//rlLetter4 = (RelativeLayout) this.layout.findViewById(R.id.rlLetter4);
	    		//rlLetter4.setLayoutParams(tile4);
	    	 
	    		RelativeLayout.LayoutParams tile5 = new RelativeLayout.LayoutParams(this.tileSize, this.tileSize);
	    		tile5.addRule(RelativeLayout.RIGHT_OF, rlLetter4.getId());
	    		tile5.setMargins(2, 0, 0, 0);
	    		rlLetter5 = (RelativeLayout) this.layout.findViewById(R.id.rlLetter5);
	    		rlLetter5.setLayoutParams(tile5);
	    		
	    		RelativeLayout.LayoutParams tile6 = new RelativeLayout.LayoutParams(this.tileSize, this.tileSize);
	    		tile6.addRule(RelativeLayout.RIGHT_OF, rlLetter5.getId());
	    		tile6.setMargins(2, 0, 0, 0);
	    		rlLetter6 = (RelativeLayout) this.layout.findViewById(R.id.rlLetter6);
	    		rlLetter6.setLayoutParams(tile6);
	    		
	    		RelativeLayout.LayoutParams tile7 = new RelativeLayout.LayoutParams(this.tileSize, this.tileSize);
	    		tile7.addRule(RelativeLayout.RIGHT_OF, rlLetter6.getId());
	    		tile7.setMargins(2, 0, 0, 0);
	    		rlLetter7 = (RelativeLayout) this.layout.findViewById(R.id.rlLetter7);
	    		rlLetter7.setLayoutParams(tile7); 
	    		*/
	    		tvLetter1 = (TextView) this.layout.findViewById(R.id.tvLetter1);
	    		//tvLetter1.setTextSize(this.textSize);
	    		tvLetter2 = (TextView) this.layout.findViewById(R.id.tvLetter2);
	    		//tvLetter2.setTextSize(this.textSize);
	    		tvLetter3 = (TextView) this.layout.findViewById(R.id.tvLetter3);
	    		//tvLetter3.setTextSize(this.textSize);
	    		tvLetter4 = (TextView) this.layout.findViewById(R.id.tvLetter4);
	    		//tvLetter4.setTextSize(this.textSize);
	    		tvLetter5 = (TextView) this.layout.findViewById(R.id.tvLetter5);
	    		//tvLetter5.setTextSize(this.textSize);
	    		tvLetter6 = (TextView) this.layout.findViewById(R.id.tvLetter6);
	    		//tvLetter6.setTextSize(this.textSize);
	    		tvLetter7 = (TextView) this.layout.findViewById(R.id.tvLetter7);
	    		//tvLetter7.setTextSize(this.textSize);
	    		
	    		TextView tvValue1 = (TextView) this.layout.findViewById(R.id.tvValue1);
	    		TextView tvValue2 = (TextView) this.layout.findViewById(R.id.tvValue2);
	    		TextView tvValue3 = (TextView) this.layout.findViewById(R.id.tvValue3);
	    		TextView tvValue4 = (TextView) this.layout.findViewById(R.id.tvValue4);
	    		TextView tvValue5 = (TextView) this.layout.findViewById(R.id.tvValue5);
	    		TextView tvValue6 = (TextView) this.layout.findViewById(R.id.tvValue6);
	    		TextView tvValue7 = (TextView) this.layout.findViewById(R.id.tvValue7);

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

	    		
	    		this.setCanceledOnTouchOutside(false);
	    		bCancel.setOnClickListener(new View.OnClickListener() {
	    			@Override
	    			public void onClick(View v) {
	    				unfreezeButtons();
	    				dismiss();
	    			}
	    		});

	    		ImageView close = (ImageView) this.layout.findViewById(R.id.img_close);
	    		//if button is clicked, close the custom dialog
	    		close.setOnClickListener(new View.OnClickListener() {
	    	 		@Override
	    			public void onClick(View v) {
	    	 			unfreezeButtons();
	    				dismiss();
	    			}
	    		});
	    		
	    		this.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						unfreezeButtons();
						
					}
				});

	    		this.setContentView(this.layout);
	    	}
	    		
	    	
	    	@Override
	    	public void dismiss(){
	    		unfreezeButtons();
	    		super.dismiss();	
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

	    private void handlePostTurn(GameActionType action, String result){
	    	Gson gson = new Gson();
	    	
	    	this.postTurnAction = action;
	    	this.unfreezeButtons();
	    	
	    	switch(action){
    	 	case CANCEL_GAME:
    	 		 
 				//remove game from local storage
	 			GameService.removeGameFromLocal(context.game);
	 			
	 			//refresh player's game list with response from server
	 			player = GameService.handleCancelGameResponse(result);
	 			GameService.updateLastGameListCheckTime();
	 			
    	 		this.handlePostTurnOption(action);
    	 			
    	 		break;
    	 	case DECLINE_GAME:
    	 		//remove game from local storage
	 			GameService.removeGameFromLocal(context.game);
	 			
	 			//refresh player's game list with response from server
	 			player = GameService.handleDeclineGameResponse(result);
	 			GameService.updateLastGameListCheckTime();
	 			
    	 		this.handlePostTurnOption(action);

    	 		break;
    	 	case RESIGN:
    	 		//remove game from local storage
	 			GameService.removeGameFromLocal(context.game);
	 			
	 			//refresh player's game list with response from server
	 			player = GameService.handleResignGameResponse(result);
	 			GameService.updateLastGameListCheckTime();
	 			
    	 		this.handlePostTurnOption(action);
	 
    	 		break;
    	 	case PLAY:
	 			//update game list for active games for last played action
	 			
	 			//refresh player's game list with response from server
	 			
    	 		//refresh game board
    	 		game = GameService.handleGamePlayResponse(result);
    	 		
    	 		Logger.d(TAG,"handlePostTurn result=" + result);
    	 		if (game.isCompleted()){
    	 			Logger.d(TAG, "handlePostTurn game isCompleted");
    	 			GameStateService.clearGameState(this.game.getId());
    	 		}
    	 		this.setupButtons();
    	 		//Logger.d(TAG, "handleResponse game=" + gson.toJson(game));
    	 		
    	 		this.handlePostTurnOption(action);
	 			
	 			break;
    	 	case SKIP:
    	 		
    	 		//update game list for active games for last played action
	 			
	 			//refresh player's game list with response from server
	 			
    	 		//refresh game board
    	 		game = GameService.handleGamePlayResponse(result);
    	 		GameStateService.clearGameState(this.game.getId());
    	 		
    	 		Logger.d(TAG, "handleResponse SKIP game=" + gson.toJson(game));
    	 		this.setupButtons();

    	 		
    	 		this.handlePostTurnOption(action);
	 			
	 			break;
    	 	case SWAP:
    	 		
    	 		//update game list for active games for last played action
	 			
	 			//refresh player's game list with response from server
	 			
    	 		//refresh game board
    	 		game = GameService.handleGamePlayResponse(result);
    	 		//GameStateService.clearGameState(context, this.game.getId());
    	 		this.gameSurfaceView.clearPlacedTiles();
    	 		this.setupButtons();
    	 		//Logger.d(TAG, "handleResponse SWAP result=" + result);
    	 		Logger.d(TAG, "handleResponse SWAP game=" + gson.toJson(game));
    	 		
    	 		this.handlePostTurnOption(action);

    	 		break;
    	 	case REMATCH:
    	 		Game newGame = GameService.handleCreateGameResponse(result);

            	 GameService.putGameToLocal(newGame);
            	 GameService.clearLastGameListCheckTime();
            	 
            	 Intent intent = new Intent(this.context, com.riotapps.word.GameSurface.class);
            	 intent.putExtra(Constants.EXTRA_GAME_ID, newGame.getId());
             
      	      	 this.context.startActivity(intent);
                 break;  
    	 }
		}
	    
	    private void handlePostTurnOption(GameActionType action){
	    	this.hasFinalPostTurnRun = false;
	    	if (this.hideInterstitialAd){
	 			this.handlePostTurnFinalAction(action);   		            	 					
	 			}
	 		else{
	 			if (this.isAdMobActive){
		 			this.spinner = new CustomProgressDialog(this);
		 			this.spinner.setMessage(this.getString(R.string.progress_updating));
		 			this.spinner.show();
		 		    this.isAdStarted = false;
		 			this.setupRunawayAdTimer();
	
		 			this.loadAdMobInterstitialAd();	 			
	 			}
	 			else if (this.isChartBoostActive) {
		 			this.cb.setTimeout((int)Constants.GAME_SURFACE_INTERSTITIAL_AD_CHECK_IN_MILLISECONDS);
		 			this.cb.showInterstitial();
			    	Logger.d(TAG, "showInterstitial from Chartboost");
			    	//String toastStr = "Loading Interstitial";
			    	//if (cb.hasCachedInterstitial()) toastStr = "Loading Interstitial From Cache";
			    	//Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show();
	 			}
	 			else if (this.isRevMobActive) {
	 				 
	 				 this.revMobFullScreen.show();
			    	Logger.d(TAG, "showInterstitial RevMob");
			    	//String toastStr = "Loading Interstitial";
			    	//if (cb.hasCachedInterstitial()) toastStr = "Loading Interstitial From Cache";
			    	//Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show();
	 			}
 			}
	    }
	    
	    private void setupRunawayAdTimer(){
	    	Logger.d(TAG, "setupRunawayAdTimer called");
	    	//interstitial sometimes gets in a funky state, and always does when ad blovkers are present
	    	//so let stop the load of the ad after 10 seconds, if it has not been displayed yet
	    	 
			this.captureTime("setupTimer starting");
	    	if (this.runawayAdTimer == null){
	    		this.runawayAdTimer = new Timer();  
	    	}
	    	checkRunawayAdTask checkRunawayAd = new checkRunawayAdTask();
	    	this.runawayAdTimer.schedule(checkRunawayAd, Constants.GAME_SURFACE_INTERSTITIAL_AD_CHECK_IN_MILLISECONDS);
	    	this.captureTime("setupRunawayAdTimer ended");
 
	    }
	    
	    private void stopRunawayAdTimer(){
	    	Logger.d(TAG, "stopRunawayAdTimer called");
	    	if (this.runawayAdTimer != null) {
	    		this.runawayAdTimer.cancel();
	    		this.runawayAdTimer = null;
	    	}
	    
	    }
	    
	    private class checkRunawayAdTask extends TimerTask {
    	   public void run() {
    		   if (!isAdStarted){
    				((Activity) context).runOnUiThread(new checkRunawayAdTaskRunnable());
    		   }
    	   }
	    }
	    
	    private class checkRunawayAdTaskRunnable implements Runnable {
		    public void run() {
		    	try { 
		    		interstitial.stopLoading();
  				    interstitial = null;
  				      
		    		if (spinner != null){
	    				spinner.dismiss();
	    			}
	    			handlePostTurnFinalAction(postTurnAction);
				} catch (Exception e) {
					 Logger.d(TAG, "checkRunawayAdTaskRunnable error=" + e.toString());
				}
		    }
	   }
	    private void handlePostTurnFinalAction(GameActionType action){
	    	 Logger.d(TAG, "handlePostTurnFinalAction called");
	    	 if (!this.hasFinalPostTurnRun){
	    		 	this.hasFinalPostTurnRun = true;
	    		 	
	    		 	if (action != null){
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
			    	 	case PLAY: //xxxxx
				 			//refresh game board and buttons
			    	 		Logger.d(TAG, "handlePostTurnFinalAction game status=" + this.game.getStatus());
				 			setupGame();
				 			checkGameStatus();
				 			gameSurfaceView.resetGameAfterPlay();
				 			///this.setupTimer();
			
				 			break;
			    	 	case SKIP:
			
				 			//refresh game board and buttons
				 			setupGame();
				 			checkGameStatus();
				 			gameSurfaceView.resetGameAfterPlay();
				 		///this.setupTimer();
				 			break;
			    	 	case SWAP:
			
				 			//refresh game board and buttons
			    	 		Logger.d(TAG,"handlePostTurnFinalAction SWAP");
				 			setupGame();
				 			gameSurfaceView.resetGameAfterPlay();
				 			//this.setupTimer();
			    	 		break;
				    	}	
		    	 }
	    	 }
		}

	    private void loadAdMobInterstitialAd(){
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
			this.isAdStarted = true;
			this.spinner.dismiss();
			this.isRestartFromInterstitialAd = true;
			//this.handlePostTurnFinalAction(this.postTurnAction); //this will be handled by restart
		}

		@Override
		public void onFailedToReceiveAd(Ad ad, ErrorCode arg1) {
			Logger.d(TAG, "interstitial onFailedToReceiveAd called");
			this.isAdStarted = true;
			this.spinner.dismiss();
			this.handlePostTurnFinalAction(this.postTurnAction);
		}

		@Override
		public void onLeaveApplication(Ad ad) {
			this.isAdStarted = true;
			Logger.d(TAG, "interstitial onLeaveApplication called");
			 //not sure what this needs here
		//	this.handlePostTurnFinalAction(this.postTurnAction);
			
		}

		@Override
		public void onPresentScreen(Ad ad) {
			Logger.d(TAG, "interstitial onPresentScreen called");
		}

		@Override
		public void onReceiveAd(Ad ad) {
			Logger.d(TAG, "interstitial onReceiveAd called");
			this.isAdStarted = true;
			  if (ad == interstitial) {
			    interstitial.show();
			  }
		}

		
		private ChartboostDelegate chartBoostDelegate = new ChartboostDelegate() { 

			/*
			 * Chartboost delegate methods
			 * 
			 * Implement the delegate methods below to finely control Chartboost's behavior in your app
			 * 
			 * Minimum recommended: shouldDisplayInterstitial()
			 */


			/* 
			 * shouldDisplayInterstitial(String location)
			 *
			 * This is used to control when an interstitial should or should not be displayed
			 * If you should not display an interstitial, return false
			 *
			 * For example: during gameplay, return false.
			 *
			 * Is fired on:
			 * - showInterstitial()
			 * - Interstitial is loaded & ready to display
			 */
			@Override
			public boolean shouldDisplayInterstitial(String location) {
				Logger.i(TAG, "SHOULD DISPLAY INTERSTITIAL '"+location+ "'?");
				return true;
			}

			/*
			 * shouldRequestInterstitial(String location)
			 * 
			 * This is used to control when an interstitial should or should not be requested
			 * If you should not request an interstitial from the server, return false
			 *
			 * For example: user should not see interstitials for some reason, return false.
			 *
			 * Is fired on:
			 * - cacheInterstitial()
			 * - showInterstitial() if no interstitial is cached
			 * 
			 * Notes: 
			 * - We do not recommend excluding purchasers with this delegate method
			 * - Instead, use an exclusion list on your campaign so you can control it on the fly
			 */
			@Override
			public boolean shouldRequestInterstitial(String location) {
				Logger.i(TAG, "SHOULD REQUEST INSTERSTITIAL '"+location+ "'?");
				return true;
			}

			/*
			 * didCacheInterstitial(String location)
			 * 
			 * Passes in the location name that has successfully been cached
			 * 
			 * Is fired on:
			 * - cacheInterstitial() success
			 * - All assets are loaded
			 * 
			 * Notes:
			 * - Similar to this is: cb.hasCachedInterstitial(String location) 
			 * Which will return true if a cached interstitial exists for that location
			 */
			@Override
			public void didCacheInterstitial(String location) {
				Logger.i(TAG, "INTERSTITIAL '"+location+"' CACHED");
			}

			/*
			 * didFailToLoadInterstitial(String location)
			 * 
			 * This is called when an interstitial has failed to load for any reason
			 * 
			 * Is fired on:
			 * - cacheInterstitial() failure
			 * - showInterstitial() failure if no interstitial was cached
			 * 
			 * Possible reasons:
			 * - No network connection
			 * - No publishing campaign matches for this user (go make a new one in the dashboard)
			 */
			@Override
			public void didFailToLoadInterstitial(String location) {
			    // Show a house ad or do something else when a chartboost interstitial fails to load

				Logger.i(TAG, "ChartBoost INTERSTITIAL '"+location+"' REQUEST FAILED");
				//Toast.makeText(context, "Interstitial '"+location+"' Load Failed",
				//		Toast.LENGTH_SHORT).show();
				handlePostTurnFinalAction(postTurnAction);
			}

			/*
			 * didDismissInterstitial(String location)
			 *
			 * This is called when an interstitial is dismissed
			 *
			 * Is fired on:
			 * - Interstitial click
			 * - Interstitial close
			 *
			 * #Pro Tip: Use the delegate method below to immediately re-cache interstitials
			 */
			@Override
			public void didDismissInterstitial(String location) {

				// Immediately re-caches an interstitial
				cb.cacheInterstitial(location);
				handlePostTurnFinalAction(postTurnAction);


				Logger.i(TAG, "ChartBoost INTERSTITIAL '"+location+"' DISMISSED");
				//Toast.makeText(context, "Dismissed Interstitial '"+location+"'",
				//		Toast.LENGTH_SHORT).show();
				
			}

			/*
			 * didCloseInterstitial(String location)
			 *
			 * This is called when an interstitial is closed
			 *
			 * Is fired on:
			 * - Interstitial close
			 */
			@Override
			public void didCloseInterstitial(String location) {
				Logger.i(TAG, "ChartBoost INSTERSTITIAL '"+location+"' CLOSED");
				handlePostTurnFinalAction(postTurnAction);
				//Toast.makeText(context, "Closed Interstitial '"+location+"'",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didClickInterstitial(String location)
			 *
			 * This is called when an interstitial is clicked
			 *
			 * Is fired on:
			 * - Interstitial click
			 */
			@Override
			public void didClickInterstitial(String location) {
				Logger.i(TAG, "ChartBoost DID CLICK INTERSTITIAL '"+location+"'");
				handlePostTurnFinalAction(postTurnAction);
				//Toast.makeText(context, "Clicked Interstitial '"+location+"'",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didShowInterstitial(String location)
			 *
			 * This is called when an interstitial has been successfully shown
			 *
			 * Is fired on:
			 * - showInterstitial() success
			 */
			@Override
			public void didShowInterstitial(String location) {
				Logger.i(TAG, "ChartBoost INTERSTITIAL '" + location + "' SHOWN");
				//Toast.makeText(context, "Interstitial '"+location+"' shown",
				//		Toast.LENGTH_SHORT).show();
				
				//Dont do it here because boost does not use full page interstitial and 
				//tray can be seen through modal
				//handlePostTurnFinalAction(postTurnAction);
			}

			/*
			 * More Apps delegate methods
			 */

			/*
			 * shouldDisplayLoadingViewForMoreApps()
			 *
			 * Return false to prevent the pretty More-Apps loading screen
			 *
			 * Is fired on:
			 * - showMoreApps()
			 */
			@Override
			public boolean shouldDisplayLoadingViewForMoreApps() {
				return true;
			}

			/*
			 * shouldRequestMoreApps()
			 * 
			 * Return false to prevent a More-Apps page request
			 *
			 * Is fired on:
			 * - cacheMoreApps()
			 * - showMoreApps() if no More-Apps page is cached
			 */
			@Override
			public boolean shouldRequestMoreApps() {

				return true;
			}

			/*
			 * shouldDisplayMoreApps()
			 * 
			 * Return false to prevent the More-Apps page from displaying
			 *
			 * Is fired on:
			 * - showMoreApps() 
			 * - More-Apps page is loaded & ready to display
			 */
			@Override
			public boolean shouldDisplayMoreApps() {
				Logger.i(TAG, "ChartBoost SHOULD DISPLAY MORE APPS?");
				return true;
			}

			/*
			 * didFailToLoadMoreApps()
			 * 
			 * This is called when the More-Apps page has failed to load for any reason
			 * 
			 * Is fired on:
			 * - cacheMoreApps() failure
			 * - showMoreApps() failure if no More-Apps page was cached
			 * 
			 * Possible reasons:
			 * - No network connection
			 * - No publishing campaign matches for this user (go make a new one in the dashboard)
			 */
			@Override
			public void didFailToLoadMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS REQUEST FAILED");
				//Toast.makeText(context, "More Apps Load Failed",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didCacheMoreApps()
			 * 
			 * Is fired on:
			 * - cacheMoreApps() success
			 * - All assets are loaded
			 */
			@Override
			public void didCacheMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS CACHED");
			}

			/*
			 * didDismissMoreApps()
			 *
			 * This is called when the More-Apps page is dismissed
			 *
			 * Is fired on:
			 * - More-Apps click
			 * - More-Apps close
			 */
			@Override
			public void didDismissMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS DISMISSED");
				//Toast.makeText(context, "Dismissed More Apps",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didCloseMoreApps()
			 *
			 * This is called when the More-Apps page is closed
			 *
			 * Is fired on:
			 * - More-Apps close
			 */
			@Override
			public void didCloseMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS CLOSED");
				//Toast.makeText(context, "Closed More Apps",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didClickMoreApps()
			 *
			 * This is called when the More-Apps page is clicked
			 *
			 * Is fired on:
			 * - More-Apps click
			 */
			@Override
			public void didClickMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS CLICKED");
				//Toast.makeText(context, "Clicked More Apps",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didShowMoreApps()
			 *
			 * This is called when the More-Apps page has been successfully shown
			 *
			 * Is fired on:
			 * - showMoreApps() success
			 */
			@Override
			public void didShowMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS SHOWED");
			}

			/*
			 * shouldRequestInterstitialsInFirstSession()
			 *
			 * Return false if the user should not request interstitials until the 2nd startSession()
			 * 
			 */
			@Override
			public boolean shouldRequestInterstitialsInFirstSession() {
				return true;
			}
		};

		
		
		
		
		/*
		//let's bind to the GCM Service so that when player is in the context of a game, and an alert
		//for that specific game is detected, we will run a refresh of the game to get latest
		//doing this will allow the removal of the refresh timer
		private GCMIntentService mBoundService;

		private ServiceConnection mConnection = new ServiceConnection() {
		    public void onServiceConnected(ComponentName className, IBinder service) {
		        // This is called when the connection with the service has been
		        // established, giving us the service object we can use to
		        // interact with the service.  Because we have bound to a explicit
		        // service that we know is running in our own process, we can
		        // cast its IBinder to a concrete class and directly access it.
		        mBoundService = ((GCMIntentService.LocalBinder)service).getService();

		        // Tell the user about this for our demo.
		        Toast.makeText(GameSurface.this, mBoundService.gameId,
		                Toast.LENGTH_LONG).show();
		    }

		    public void onServiceDisconnected(ComponentName className) {
		        // This is called when the connection with the service has been
		        // unexpectedly disconnected -- that is, its process crashed.
		        // Because it is running in our same process, we should never
		        // see this happen.
		        mBoundService = null;
		        Toast.makeText(GameSurface.this, "disconnected",
		                Toast.LENGTH_LONG).show();
		    }
		};

		void doBindService() {
		    // Establish a connection with the service.  We use an explicit
		    // class name because we want a specific service implementation that
		    // we know will be running in our own process (and thus won't be
		    // supporting component replacement by other applications).
		    bindService(new Intent(GameSurface.this, 
		    		GCMIntentService.class), mConnection, Context.BIND_AUTO_CREATE);
		    isBoundToGCMService = true;
		}
		
		void doUnbindService() {
			if (isBoundToGCMService) {
				// Detach our existing connection.
				unbindService(mConnection); 
				isBoundToGCMService = false;
				}
			}
 	*/
}
