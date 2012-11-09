package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.TrayTile;
import com.riotapps.word.hooks.Error.ErrorType;
import com.riotapps.word.ui.CustomDialog;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.ui.GameAction.GameActionType;
import com.riotapps.word.ui.GameState;
import com.riotapps.word.ui.GameStateService;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.ui.GameTile;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.RequestType;

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
	 NetworkTask runningTask = null;
	 Button bRecall;
	 Button bShuffle;
	
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
	 	
	 	this.setupButtons();
	 	
	 	
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

	 public void switchToRecall(){
		//by default recall button will be hidden, it will be switched with shuffle button when a letter is dropped on the board
		 context.runOnUiThread(new handleButtonSwitchRunnable(2));
		 //	this.bRecall.setVisibility(View.VISIBLE);
		 //	this.bShuffle.setVisibility(View.GONE);
	 }

	 public void switchToShuffle(){
		 context.runOnUiThread(new handleButtonSwitchRunnable(1));
		 
		 
			//this.bRecall.setVisibility(View.GONE);
		 	//this.bShuffle.setVisibility(View.VISIBLE);
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
		    	}
		    }
	  }
	 
	 private void setupButtons(){
		this.bRecall = (Button) findViewById(R.id.bRecall);
		this.bShuffle = (Button) findViewById(R.id.bShuffle);
		Button bChat = (Button) findViewById(R.id.bChat);
		Button bPlayedWords = (Button) findViewById(R.id.bPlayedWords);
		Button bCancel = (Button) findViewById(R.id.bCancel);
		Button bResign = (Button) findViewById(R.id.bResign);
		Button bDecline = (Button) findViewById(R.id.bDecline);
	 	this.bShuffle.setOnClickListener(this);
	 	bChat.setOnClickListener(this);
	 	bPlayedWords.setOnClickListener(this);
	 	this.bRecall.setOnClickListener(this);
	 	
	 	//by default recall button will be hidden, it will be switched with shuffle button when a letter is dropped on the board
	 	this.bRecall.setVisibility(View.GONE); 
	 	
	 	//set cancel button area mode:
	 	//if it's the first play of the game by starting player, it should be "CANCEL" mode
	 	//if it's the first play of the game by a non-starting player, it should be in "DECLINE" mode
	 	//if it's not the first play of the game, it should be in "RESIGN" mode
	 	
	 	//the starting player get one chance (one turn) to cancel
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
	 		}
	 		else{
	 			//else we are past each opponents first turn, therefore show the resign button 
	 			bCancel.setVisibility(View.GONE);	
		 		bResign.setOnClickListener(this);
		 		bDecline.setVisibility(View.GONE);
	 		}
	 	}
	 	else if (this.game.getNumActiveOpponents() == 2){
	 		if (this.game.getTurn() < 3 && this.game.getContextPlayerOrder(this.player) < 3){
	 			//in a three player game, the invited players gets one chance to decline
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
	 		}
 		
	 	}
	 
	}
	    
	    
	    
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
		if (this.runningTask != null){
    		this.runningTask.cancel(true);
    	}
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
	    
	    
	    private void handleGameCancelOnClick(){
	    	//stop thread first
	    	this.gameSurfaceView.onStop();
	    	try { 
				String json = GameService.setupCancelGame(context, this.game.getId());
				
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(context, RequestType.POST, json,  getString(R.string.progress_cancelling), GameActionType.CANCEL_GAME);
				runningTask.execute(Constants.REST_CANCEL_GAME);
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
	    		         
	    		         Log.i(GameSurface.TAG, "StatusCode: " + statusCode);

	    		         switch(statusCode){  
	    		             case 200:  
	    		             case 201: {
	    		            	 switch(this.actionType){
	    		            	 	case CANCEL_GAME:
	    		            	 		 
	    		    		 				//remove game from local storage
	    		            	 			GameService.removeGameFromLocal(context, context.game);
	    		            	 			
	    		            	 			//refresh player's game list with response from server
	    		            	 			Player player = GameService.handleCancelGameResponse(context, iStream);
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
	    		            	 		
	    		            	 		break;
	    		            	 	case RESIGN:
	    		            	 		
	    		            	 		break;
	    		            	 	case PLAY:
	    		            	 		
	    		            	 		break;
	    		            	 	case SKIP:
	    		            	 		
	    		            	 		break;
	    		            	 	case SWAP:
	    		            		 
	    		            	 		break;
	    		            	 
	    		            	 }
	    		             
	    		             }//end of case 200 & 201 
	    		             case 401:
	    			             //case Status code == 422
	    			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.validation_unauthorized));  
	    			            	 break;
	    		             case 404:
	    		             //case Status code == 422
	    		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.find_player_opponent_not_found), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
	    		            	 break;
	    		             case 422: 
	    		             case 500:

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
	    
}
